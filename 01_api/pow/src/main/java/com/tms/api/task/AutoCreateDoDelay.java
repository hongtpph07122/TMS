package com.tms.api.task;

import com.tms.api.dto.delivery.DeliveryDto;
import com.tms.api.dto.delivery.ProductDto;
import com.tms.api.entity.OdSaleOrder;
import com.tms.api.entity.OrderResult;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.*;
import com.tms.api.repository.SaleOrderRepository;
import com.tms.dto.*;
import com.tms.entity.*;
import com.tms.entity.log.InsDoNew;
import com.tms.entity.log.UpdDoNew;
import com.tms.service.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class AutoCreateDoDelay {

    private static final Logger logger = LoggerFactory.getLogger(AutoCreateDoDelay.class);

    @Autowired
    CLFreshService clFreshService;

    @Autowired
    LogService logService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    DeliveryOrderService deliveryOrderService;

    @Autowired
    CLProductService clProductService;

    @Autowired
    LCProvinceService lcProvinceService;

    @Autowired
    LCProvinceService provinceService;

    @Value("${config.country}")
    public String _COUNTRY;

    @Autowired
    DBLogWriter dbLog;

    @Autowired
    SaleOrderRepository saleOrderRepository;

    @Value("${config.auto-create-do}")
    private boolean isLoadDelayDO;

    public String SESSION_ID = UUID.randomUUID().toString();

    @Scheduled(cron = "0 35 0 * * ?")
    public void processAutoCreateDO() throws TMSException {
        if (!isLoadDelayDO) {
            logger.info("Auto create DO delay is off!");
            return;
        }
        LocalDateTime startTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endTime = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        LocalDateTime now = LocalDateTime.now();
        List<OdSaleOrder> listSODelay = saleOrderRepository.getListSaleOrderDelay(Timestamp.valueOf(endTime));
        List<OdSaleOrder> lstSOUpdate = new ArrayList<>();
        boolean isSucces = true;
        StringBuilder mesage = new StringBuilder();
        String formatMessage = "%s : %s : %s <br />";

        logger.info("Auto create DO delay at " + now);

        if (listSODelay == null || listSODelay.size() == 0) {
            logger.info("No Sale Order delay in " + startTime);
        } else {
            for (int i = 0; i < listSODelay.size(); i++) {

                OdSaleOrder order = listSODelay.get(i);
                int userId = 0;
                if (!StringUtils.isEmpty(order.getModifyby())) {
                    userId = order.getModifyby();
                }
                order.setStatus(EnumType.SALE_ORDER_STATUS.VALIDATED.getValue());
                order.setModifydate(new Date());
                order.setValidated(true);
                order = saleOrderRepository.save(order);
                logger.info("Udate SO delay to validated: " + LogHelper.toJson(order));

                InsDoNew doNew = new InsDoNew();
                doNew.setOrgId(order.getOrgId());
                doNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
                doNew.setSoId(order.getSoId());
                doNew.setCreateby(userId);
                doNew.setCustomerId(order.getLeadId());
                isSucces = CreateDONew(doNew, order.getLeadId(), order.getOrgId(), userId, order);
                mesage.append(String.format(formatMessage, order.getSoId(), order.getLeadId(), isSucces));
            }
            logger.info("Create DO delay success =  " + mesage);

        }

    }


    private boolean CreateDONew(InsDoNew insDoNew, Integer leadId, int _curOrgId, int userId, OdSaleOrder order) throws TMSException {
        logger.info("########################################################");
        StringBuilder errMessage = new StringBuilder();
        // get lead detail
        GetLeadParamsV8 clFreshParams = new GetLeadParamsV8();
        clFreshParams.setLeadId(leadId);
        clFreshParams.setOrgId(_curOrgId);
        DBResponse<List<CLFresh>> dbfresh = clFreshService.getLeadV8(SESSION_ID, clFreshParams);
        logger.info("--------------------------- {}", leadId);
        if (dbfresh.getResult().isEmpty()) {
            logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
            errMessage.append("LEAD NOT FOUND " + leadId + "  " + _curOrgId);
            insDoNew.setErrorMessage(errMessage.toString());
            logService.insDoNew(SESSION_ID, insDoNew);
            throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
        }

        CLFresh fresh = dbfresh.getResult().get(0);
        int provinceId = Integer.parseInt(fresh.getProvince());
        int subDistId = Integer.parseInt(fresh.getSubdistrict());
        int neighborId = 0;
        String zipcode = "";
        if (!StringHelper.isNullOrEmpty(fresh.getNeighborhood()))
            neighborId = Integer.parseInt(fresh.getNeighborhood());

        if (!StringHelper.isNullOrEmpty(fresh.getPostalCode()))
            zipcode = fresh.getPostalCode();

        boolean hasProdInFFToCreateDO = true;
        String prodMessage = "";
        // TODO need to read from config default value
        Integer parterId = 1;
        Integer warehouseId = 1;
        Integer lastmileId = 1;
        Integer threePl = 1;
        Integer pickupId = 1;
        String warehouseShortname = "";
        boolean isContinue = true;
        List<ProductDto> lst = new ArrayList<>();
        // Tiet kiem call toi Fresh, gui toi QUEUE o day
        // String message = fresh.getAgcId() + "|" + fresh.getClickId();
        logger.info("123 ####################################");
        // TODO need get agc_id

        LocationHelper locationHelper = new LocationHelper();
        DeliveryDto deliveryDto = new DeliveryDto(fresh);
        deliveryDto.setSESSION_ID(SESSION_ID);
        String mappingType = "1";
        int mappingLocation = provinceId;
        try {
            GetDefaultDOV3 getDefaultDO = new GetDefaultDOV3();
            getDefaultDO.setOrgId(_curOrgId);
            getDefaultDO.setProvinceId(provinceId);
            // TODO need to implement new code
            mappingType = RedisHelper.getGlobalParamValue(stringRedisTemplate, _curOrgId, 9, 2);
            logger.info("mappingType ======={}", mappingType);
            switch (mappingType) {
                case "1":
                    mappingLocation = provinceId;
                    break;
                case "2":
                    mappingLocation = Integer.parseInt(fresh.getDistrict());
                    break;
                case "3":
                    mappingLocation = subDistId;
                    break;
                case "4":
                    mappingLocation = Integer.parseInt(fresh.getNeighborhood());
                    break;
            }

            /*
             * if (_COUNTRY.equals("ID"))//neu la Indo thi mapping default do theo
             * subdistrict de tim warehouse and lastmile
             * getDefaultDO.setProvinceId(subDistId);
             */
            getDefaultDO.setProvinceId(mappingLocation);
            DBResponse<List<GetDefaultDOResp>> dbDo = deliveryOrderService.getDefaultDOV3(SESSION_ID, getDefaultDO);
            logger.info("provinceId = {}", provinceId);

            if (dbDo.getResult().isEmpty())
                parterId = 1;
            else {
                parterId = dbDo.getResult().get(0).getPartnerId();
                // gan code cua warehouse = warehouse name (DB desinger)
                // String tmpWarehouseId = dbDo.getResult().get(0).getWarehouseName();
                // warehouseId = Integer.parseInt(tmpWarehouseId);
                warehouseId = dbDo.getResult().get(0).getWarehouseId();

                logger.info("warehouseId = {}", warehouseId);

                warehouseShortname = dbDo.getResult().get(0).getWarehouseShortname();
                // TODO need to read from config default value
                lastmileId = dbDo.getResult().get(0).getLastmileId();
                /*
                 * if (warehouseId == Const.KERRY_FFM_PARTNER_ID || warehouseId ==
                 * Const.SAP_FFM_PARTNER_ID) {//neu la warehouse cua SAP FFM thi dat = ma cua
                 * SAP lastmile warehouseId = lastmileId; }
                 */
                pickupId = dbDo.getResult().get(0).getPickupId();
                logger.info("{} {} {}", SESSION_ID, insDoNew.getSoId(), LogHelper.toJson(dbDo.getResult().get(0)));
            }
            logger.info("**************************** " + parterId);
            // get list product
            lst = getProductListBySoId(insDoNew.getSoId(), insDoNew.getOrgId(), parterId);

            GetPickup pickupParam = new GetPickup();
            pickupParam.setOrgId(_curOrgId);
            pickupParam.setPickupId(pickupId != null ? pickupId.toString() : "1");

            DBResponse<List<GetPickupResp>> dbPickup = clProductService.getPickup(SESSION_ID, pickupParam);
            // luon co pickupid = 1 nen ko can check null
            GetPickupResp pickupResp = dbPickup.getResult().get(0);
            // set pickup
            deliveryDto.setPickupId(pickupResp.getPickupCodeInpartner().toString());
            deliveryDto.setSoldToAccountId(pickupResp.getSoldId().toString());
            deliveryDto.setPickupName(pickupResp.getPickupName());

            insDoNew.setCarrierId(lastmileId);
            insDoNew.setWarehouseId(warehouseId);
            insDoNew.setCustomerName(fresh.getName());
            insDoNew.setCustomerPhone(fresh.getPhone());
            insDoNew.setCustomerAddress(fresh.getAddress());
            insDoNew.setCustomerId(leadId);
            logger.info("{} {} warehouseId out = {}", SESSION_ID, insDoNew.getSoId(), warehouseId);
            // mapping data

            deliveryDto.setDoId(insDoNew.getSoId());
            deliveryDto.setWarehouseId(warehouseId);
            deliveryDto.setLastmileId(lastmileId);
            deliveryDto.setOrgId(_curOrgId);

            // get sender address from warehouse id
            GetWareHouse getWareHouse = new GetWareHouse();
            getWareHouse.setWarehouseId(String.valueOf(warehouseId));
            DBResponse<List<GetWareHouseResp>> dbWarehouse = clProductService.getWareHouse(SESSION_ID, getWareHouse);
            logger.info("warehouseId out2 = {}", warehouseId);
            if (!dbWarehouse.getResult().isEmpty()) {
                GetWareHouseResp wareHouseResp = dbWarehouse.getResult().get(0);
                deliveryDto.setsAddress(wareHouseResp.getAddress());
                logger.info("{} {} warehouseId out 3 = {}", SESSION_ID, insDoNew.getSoId(), wareHouseResp.getAddress());
                deliveryDto.setsDistrictName(wareHouseResp.getDistrictName());
                deliveryDto.setsProvinceName(wareHouseResp.getProvinceName());
                deliveryDto.setsDistrictId(wareHouseResp.getDistrictId());
                deliveryDto.setsProvinceId(wareHouseResp.getProvinceId());
                deliveryDto.setsDistrictCode(wareHouseResp.getDistrictCode());
                deliveryDto.setsProvinceCode(wareHouseResp.getProvinceCode());
                deliveryDto.setsWardName(wareHouseResp.getWardsName());
                deliveryDto.setGroupAddressId(wareHouseResp.getWhCodeInpartner());
                deliveryDto.setsPhone(wareHouseResp.getContactPhone());
                deliveryDto.setsWardCode(wareHouseResp.getWardsCode());
            }

            // get pickup
            logger.info("parterId ============ ============== " + parterId);
            /*
             * switch (parterId) { case Const.SAP_FFM_PARTNER_ID: case Const.BM_PARTNER_ID:
             * case Const.KERRY_FFM_PARTNER_ID: case Const.WFS_PARTNER_ID: threePl =
             * lastmileId; break; default: threePl = parterId; break; }
             */
            threePl = parterId == Const.BM_PARTNER_ID || parterId == Const.SAP_FFM_PARTNER_ID
                    || parterId == Const.KERRY_FFM_PARTNER_ID ? lastmileId : parterId;

            if (parterId == Const.NTL_FFM_PARTNER_ID)
                threePl = 1;
            GetProvinceMapResp provinceMapResp = locationHelper.getProvinceMapper(provinceService, threePl,
                    deliveryDto.getProvinceId(), SESSION_ID);
            logger.info("parterId ============ ============== " + parterId + " " + threePl + "  " + lastmileId);
            GetDistrictMapResp districtMapResp = locationHelper.getDistrictMapper(provinceService, threePl,
                    deliveryDto.getDistrictId(), SESSION_ID);
            GetSubdistrictMapResp subdistrictMapResp = null;
            if (Helper.isIndonesia(_COUNTRY) || Helper.isThailand(_COUNTRY) || parterId == Const.WFS_PARTNER_ID || parterId == Const.NTL_FFM_PARTNER_ID) {// Kho
                // GHN
                // APIv2
                // can
                // lay
                // them
                subdistrictMapResp = locationHelper.getSubDistrictMapper(provinceService, threePl,
                        deliveryDto.getWardsId(), SESSION_ID);
                if (subdistrictMapResp != null)
                    deliveryDto.setWardName(subdistrictMapResp.getName());
                if (parterId == Const.WFS_PARTNER_ID)
                    try {
                        deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
                    } catch (Exception e) {
                        logger.error("Can not convert subdistrictMapResp.getWardsPartnerCode {}", subdistrictMapResp.getWardsPartnerCode(), e);
                    }
            }

            try {
                if (provinceMapResp != null) {
                    if (threePl == Const.WFS_PARTNER_ID) {
                        int prvId = Integer.parseInt(provinceMapResp.getPrvPartnerCode());
                        deliveryDto.setProvinceId(prvId);
                    } else
                        deliveryDto.setProvinceId(provinceMapResp.getPrvId());
                    deliveryDto.setProvinceName(provinceMapResp.getPrvPartnerName());

                    String province = threePl != Const.DHL_PARTNER_ID ? provinceMapResp.getPrvPartnerCode() != null
                            ? provinceMapResp.getPrvPartnerCode().toString()
                            : "" : provinceMapResp.getName();
                    insDoNew.setCustomerProvince(province);
                }

                if (districtMapResp != null) {
                    if (threePl == Const.WFS_PARTNER_ID || threePl == Const.BM_INDO_PARTNER_ID) {
                        int dtId = Integer.parseInt(districtMapResp.getDtPartnerCode());
                        deliveryDto.setGhnDistrictCode(districtMapResp.getDtPartnerCode());
                        deliveryDto.setDistrictId(dtId);
                    } else if(threePl == Const.DHL_FFM_PARTNER_ID){
                        deliveryDto.setDistrictCode(districtMapResp.getDtPartnerCode());
                    } else
                        deliveryDto.setDistrictId(districtMapResp.getDistrictId());

                    deliveryDto.setDistrictName(districtMapResp.getDtPartnerName());
                    String district = threePl != Const.DHL_PARTNER_ID
                            ? districtMapResp.getDtPartnerCode() != null ? districtMapResp.getDtPartnerCode().toString()
                            : ""
                            : districtMapResp.getDtPartnerName();
                    logger.info("Check cau hinh ****************** {}-{}-{} ", threePl, lastmileId, parterId);
                    /*
                     * if (lastmileId == Const.DHL_PARTNER_ID)
                     * deliveryDto.setDistrictName(district);
                     */

                    insDoNew.setCustomerDistrict(district);
                }
            } catch (Exception e) {
                prodMessage = "Not mapping province and district ";
                logger.error(e.getMessage(), e);
            }

            // --------------------- NEW
            if(lastmileId == Const.SHIP60_LM_PARTNER_VN){
                subdistrictMapResp = locationHelper.getSubDistrictMapper(provinceService, threePl,
                        deliveryDto.getWardsId(), SESSION_ID);
                if (provinceMapResp != null){
                    deliveryDto.setProvinceCode(provinceMapResp.getPrvPartnerCode());
                    deliveryDto.setProvinceName(provinceMapResp.getName());
                }
                if (districtMapResp != null){
                    deliveryDto.setDistrictCode(districtMapResp.getDtPartnerCode());
                    deliveryDto.setDistrictName(districtMapResp.getName());
                }
                if (subdistrictMapResp !=null){
                    deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
                    deliveryDto.setWardName(subdistrictMapResp.getName());
                }
            }

            if (lastmileId == Const.DHL_PARTNER_ID && parterId == Const.WFS_PARTNER_ID) {
                GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService, Const.WFS_PARTNER_ID,
                        Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
                GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService, Const.WFS_PARTNER_ID,
                        Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
                if (provMapResp != null) {
                    int prvId = Integer.parseInt(provMapResp.getPrvPartnerCode());
                    deliveryDto.setProvinceId(prvId);
                }
                if (dtMapResp != null) {
                    int dtId = Integer.parseInt(dtMapResp.getDtPartnerCode());
                    deliveryDto.setDistrictId(dtId);
                }

            }

            if (lastmileId == Const.DHL_PARTNER_ID) {
                GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService, Const.DHL_PARTNER_ID,
                        Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
                GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService, Const.DHL_PARTNER_ID,
                        Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
                if (provMapResp != null)
                    deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
                if (dtMapResp != null)
                    deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());

            }
            if (lastmileId == Const.GHTK_LM_PARTNER_VN) {
                GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
                        Const.GHTK_LM_PARTNER_VN, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
                GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
                        Const.GHTK_LM_PARTNER_VN, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
                GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
                        Const.GHTK_LM_PARTNER_VN, deliveryDto.getWardsId(), SESSION_ID);
                if (provMapResp != null)
                    deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
                if (dtMapResp != null)
                    deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());
                if (sdtMapResp != null)
                    deliveryDto.setWardName(sdtMapResp.getName());

            }

            if(lastmileId == Const.GHN_PARTNER_ID && parterId == Const.DHL_FFM_PARTNER_ID){
                GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
                        Const.WFS_PARTNER_ID, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
                GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
                        Const.WFS_PARTNER_ID, deliveryDto.getWardsId(), SESSION_ID);
                if(dtMapResp != null)
                    deliveryDto.setGhnDistrictCode(dtMapResp.getDtPartnerCode());
                if(sdtMapResp != null)
                    deliveryDto.setGhnWardCode(sdtMapResp.getWardsPartnerCode());
            }

            if(lastmileId == Const.GHN_PARTNER_ID){
                GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
                        Const.WFS_PARTNER_ID, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
                GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
                        Const.WFS_PARTNER_ID, deliveryDto.getWardsId(), SESSION_ID);
                if(dtMapResp != null)
                    deliveryDto.setGhnDistrictCode(dtMapResp.getDtPartnerCode());
                if(sdtMapResp != null)
                    deliveryDto.setGhnWardCode(sdtMapResp.getWardsPartnerCode());
            }

            if(lastmileId == Const.SNAPPY_PARTNER_ID){
                deliveryDto.setWarehouseShortname(warehouseShortname);
                GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
                        Const.SNAPPY_PARTNER_ID, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
                GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
                        Const.SNAPPY_PARTNER_ID, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
                if(provMapResp != null)
                    deliveryDto.setLmProvinceId(Integer.parseInt(provMapResp.getPrvPartnerCode()));
                if(dtMapResp != null)
                    deliveryDto.setLmDistrictId(Integer.parseInt(dtMapResp.getDtPartnerCode()));
            }

            if(lastmileId == Const.FLASH_TH_PARTNER_ID){
                GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
                        lastmileId, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
                GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
                        lastmileId, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
                GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
                        lastmileId, Helper.IntergeTryParse(fresh.getSubdistrict()), SESSION_ID);
                if(provMapResp != null) {
                    deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
                }
                if(dtMapResp != null) {
                    deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());
                }
                if(sdtMapResp != null) {
                    deliveryDto.setWardName(sdtMapResp.getWardsPartnerId());
                    deliveryDto.setWardsCode(sdtMapResp.getWardsPartnerCode());
                }
            }

            if (subdistrictMapResp != null && !StringHelper.isNullOrEmpty(subdistrictMapResp.getDcsr()))
                deliveryDto.setLastmileService(subdistrictMapResp.getDcsr());

            // set sZipcode = subdistrict postcode by partner (lc_subdistrict_map.sdt_code)
            if (subdistrictMapResp != null)
                deliveryDto.setsZipCode(subdistrictMapResp.getWardsPartnerCode());

            // ---------------------
            if (Helper.isIndonesia(_COUNTRY) && subdistrictMapResp != null) {// only Indonesia need to get subdistrict
                deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
                deliveryDto.setWardName(subdistrictMapResp.getName());

                insDoNew.setCustomerWards(subdistrictMapResp.getWardsPartnerCode());

                if (threePl == Const.BM_INDO_PARTNER_ID) {

                    deliveryDto.setProvinceCode(deliveryDto.getDistrictId().toString());
                    deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
                    if (!StringHelper.isNullOrEmpty(subdistrictMapResp.getDcsr())) {
                        String[] path = subdistrictMapResp.getDcsr().split(",");
                        if (path.length == 5)
                            deliveryDto.setrZipCode(path[4]);
                    }
                } else if (parterId == Const.KERRY_FFM_PARTNER_ID) {
                    deliveryDto.setrZipCode(subdistrictMapResp.getWardsPartnerCode());
                    GetNeighbordhoodResp nghResp = locationHelper.getNeighborhoodMapper(deliveryOrderService,
                            Const.KERRY_LM_PARTNER_ID, neighborId, SESSION_ID);
                    if (nghResp != null)
                        deliveryDto.setrZipCode(nghResp.getCode());
                }

            }
//            return true;

            // update nghiep vu (lay so luong product)

            logger.info("---------------------- -----------------  {}", lst.size());
            if (lst.size() == 0) {
                hasProdInFFToCreateDO = false;
                prodMessage = "Size of SO Item not equal size of product mapping, getProductListBySoId  SO id = "
                        + insDoNew.getSoId();
                insDoNew.setErrorMessage(prodMessage);
                insDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
            }

            String packageName = "";
            String formatPackage = "%s(%s)";
            for (int i = 0; i < lst.size(); i++)
                packageName += String.format(formatPackage, lst.get(i).getProName(), lst.get(i).getQty()) + ", ";
            insDoNew.setPackageName(packageName);
            deliveryDto.setPackageName(packageName);
            insDoNew.setPackageListItem(LogHelper.toJson(lst));
        } catch (Exception e) {
            isContinue = false;
            logger.error(e.getMessage(), e);
            errMessage.append(e.getMessage());
            insDoNew.setErrorMessage(errMessage.toString());
            insDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
        }
        // INSERT TO donew
        DBResponse dbResponse = logService.insDoNew(SESSION_ID, insDoNew);
        String doNewId = dbResponse.getErrorMsg().trim();

        int doId = 0;
        try {
            doId = Integer.parseInt(doNewId);
            deliveryDto.setDoId(doId);
        } catch (Exception e) {

            doId = 0;
            order.setStatus(EnumType.SALE_ORDER_STATUS.DOCREATEFAIL.getValue());
            saleOrderRepository.save(order);
            logger.debug(e.getMessage());
            return false;

        }

        dbLog.writeDOStatusLog(userId, doId, EnumType.DO_STATUS_ID.NEW.getValue(), "Create DO");
        dbLog.writeAgentActivityLog(userId, "create delivery order", "delivery order", doId, "do_status",
                EnumType.DO_STATUS_ID.NEW.getValue() + "");

        // send to agentcy confirm lead is order success format leadId|doId|soId
//        QueueHelper.sendMessage((String.format("%s|%s|%s", leadId, doNewId, insDoNew.getSoId())), Const.QUEUE_AGENTCY);
        logger.info("---------------------- -----------------  2 {}", hasProdInFFToCreateDO);
//        QueueHelper.sendMessage(String.format("%s|$s|%s|%s|%s", "INSERT", doNewId, EnumType.DO_STATUS_ID.NEW.getValue(), _curUser.getUserId(), LogHelper.toJson(insDoNew)), Const.QUEUE_LOG);
        if (!hasProdInFFToCreateDO)
            return true;

        String key = RedisHelper.createRedisKey(Const.REDIS_PRERIX_DO, _curOrgId, userId);
        RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(doNewId), insDoNew.getSoId() + "|" + leadId);

        String globalValue = RedisHelper.getGlobalParamValue(stringRedisTemplate, _curOrgId, 4, 1);
        if (!isContinue)// co loi xay ra khi cau hinh, khong cho phep chay tiep
            return false;
        logger.info("{}  ---------------------- -----------------  3 {}", globalValue, hasProdInFFToCreateDO);
        if (globalValue == null)
            return true;
        if (globalValue.equals("2"))// run create delivery manual
            return true;
        // #################### kiem tra san pham co con trong kho hay ko?
        // ######################
        logger.info("---------------------- -----------------  4 {}", hasProdInFFToCreateDO);
        if (warehouseId == null) {
            prodMessage = "Not configure warehouse id for partner " + parterId;
            hasProdInFFToCreateDO = false;
        } else
            for (int j = 0; j < lst.size(); j++) {
                Boolean hasProductInFF = DeliveryHelper.hasProductInFullfillment(parterId, lst.get(j),
                        String.valueOf(warehouseId));
                if (!hasProductInFF) {
                    prodMessage = lst.get(j).getProName() + " has not enough quantity in Fullfillment " + parterId;
                    logger.info(
                            SESSION_ID + " " + insDoNew.getSoId() + "############### " + doNewId + "|" + prodMessage);
                    hasProdInFFToCreateDO = false;
                    break;
                }
            }
        if (!hasProdInFFToCreateDO) {
            UpdDoNew updDoNew = new UpdDoNew();
            updDoNew.setDoId(doId);
            updDoNew.setOrgId(_curOrgId);
            updDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
            updDoNew.setErrorMessage(prodMessage);
            logService.updDoNew(SESSION_ID, updDoNew);

            return false;
        }
        logger.info("---------------------- -----------------  5 {}", hasProdInFFToCreateDO);
        GetProvinceMapResp provinceMapWHResp = null;
        if (deliveryDto.getProvinceId() != null)
            provinceMapWHResp = locationHelper.getProvinceMapper(provinceService, parterId, deliveryDto.getProvinceId(),
                    SESSION_ID);
        GetDistrictMapResp districtMapWHResp = null;
        if (deliveryDto.getDistrictId() != null)
            districtMapWHResp = locationHelper.getDistrictMapper(provinceService, parterId, deliveryDto.getDistrictId(),
                    SESSION_ID);
        if (parterId == Const.SAP_FFM_PARTNER_ID || parterId == Const.KERRY_FFM_PARTNER_ID) {

        } else if (provinceMapWHResp != null && provinceMapWHResp.getPrvPartnerCode() != null) {
            int prvId = Integer.parseInt(provinceMapWHResp.getPrvPartnerCode());
            deliveryDto.setReicvWhProvinceId(prvId);
        }

        if (parterId == Const.SAP_FFM_PARTNER_ID || parterId == Const.KERRY_FFM_PARTNER_ID) {

        } else if (districtMapWHResp != null && districtMapWHResp.getDtPartnerCode() != null) {
            int dtId = Integer.parseInt(districtMapWHResp.getDtPartnerCode());
            deliveryDto.setReicvWhDistrictId(dtId);
            if (districtMapWHResp.getPnDistrictId() != null) {
                GetSubdistrict paramGetSubdistrict = new GetSubdistrict();
                paramGetSubdistrict.setSdtId(subDistId);
                DBResponse<List<Subdistrict>> lstSubdistrictRes = lcProvinceService.getSubDistrict(SESSION_ID,
                        paramGetSubdistrict);
                Subdistrict subdistrict = lstSubdistrictRes.getResult().get(0);
                deliveryDto.setrZipCode(subdistrict.getCode());
//                deliveryDto.setrZipCode(districtMapWHResp.getPnDistrictId());
            }
        }

        deliveryDto.setPartnerId(parterId);
        String doCode = DeliveryHelper.createEKWDoCode(doId);
        deliveryDto.setDoCode(doCode);

        //Nếu là DHL FFM (đã có ffm code -> set ffm code -< gọi api update sale order DHL FFM)
        if(parterId == Const.DHL_FFM_PARTNER_ID && insDoNew.getFfmCode() != null){
            deliveryDto.setFfmCode(insDoNew.getFfmCode());
        }

        int totalAmount = 0;
        try {
            SaleOrder saleOrder = getSaleOrderDetail(insDoNew.getSoId());
            deliveryDto.setPaymentMethod(saleOrder.getPaymentMethod());
            if (deliveryDto.getPhone().contains("|"))
                deliveryDto.setPhone(saleOrder.getLeadPhone());
            totalAmount = saleOrder.getAmount().intValue();
        } catch (Exception e) {
            logger.info(SESSION_ID + " " + doNewId + " $$$$$$$$$ Could not get total amount  " + e.getMessage());
        }
        deliveryDto.setAmount(totalAmount);
        // TODO need to be calculate
        if (_COUNTRY.equals("ID"))
            totalAmount = Helper.RoundToHundred(Double.valueOf(totalAmount));
        if (deliveryDto.getPaymentMethod() != EnumType.PAYMENT_METHOD.BANKING.getValue())
            deliveryDto.setCod_money(totalAmount);
        else// la banking thi set = 0
            deliveryDto.setCod_money(0);

        logger.info("{} {} deliveryDto: {}\r\n======= ========== createDelivery partnerId={}",
                SESSION_ID, doNewId, LogHelper.toJson(deliveryDto), parterId);

        List<OrderResult> lstOrder = DeliveryHelper.createDelivery(parterId, deliveryDto, lst);
        if (lstOrder == null)
            logger.info(SESSION_ID + "[CreateDONew] Could not config Partner ID " + parterId + " " + doNewId);

        // update result
        boolean isAllSuccess = true;
        if (lstOrder != null && lstOrder.size() > 0) {
            logger.info("List Order: " + LogHelper.toJson(lstOrder));
            for (int i = 0; i < lstOrder.size(); i++) {
                OrderResult tmpOrder = lstOrder.get(i);
                UpdDoNew updDoNew = new UpdDoNew();
                updDoNew.setDoId(doId);
                updDoNew.setDoCode(deliveryDto.getDoCode());
                updDoNew.setOrgId(_curOrgId);
                updDoNew.setCustomerPhone(deliveryDto.getPhone());
                updDoNew.setFfmId(parterId);
                // TODO need to be change this method
                updDoNew.setAmountcod(Double.valueOf(deliveryDto.getCod_money()));
                if (isAllSuccess) {
                    logger.info(tmpOrder.getType());
                    switch (tmpOrder.getResult()) {
                        case Const.DO_SUCCESS:
                            updDoNew.setErrorMessage(null);
                            updDoNew.setStatus(EnumType.DO_STATUS_ID.NEW.getValue());
                            /*
                             * if (parterId < 2 || parterId > 5)//kho GHN
                             * updDoNew.setFfmCode(tmpOrder.getMessage()); else//lastmile
                             * updDoNew.setTrackingCode(tmpOrder.getMessage());
                             */
                            if (tmpOrder.getType().equals(Const.BM_PARTNER_CODE)
                                    || tmpOrder.getType().equals(Const.SAPW_PARTNER_CODE)
                                    || tmpOrder.getType().equals(Const.MYCLOUD_PARTNER_CODE)
                            )
                                updDoNew.setFfmCode(tmpOrder.getMessage());
                            else if (tmpOrder.getType().equals(Const.BM_EXPRESS_CODE)) {
                                updDoNew.setFfmCode(tmpOrder.getMessage());
                                updDoNew.setTrackingCode(tmpOrder.getMessage());
                            }
                            else if (tmpOrder.getType().equals(Const.WFS_GHN_PARTNER_CODE)) {
                                if (tmpOrder.getCode().compareTo("0") == 0) {

                                    String[] trackings = tmpOrder.getMessage().split("\\|");
                                    updDoNew.setFfmCode(trackings[0]);
                                    updDoNew.setTrackingCode(trackings[1]);
                                } else {
                                    updDoNew.setErrorMessage(tmpOrder.getErrMessage());
                                    updDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
                                }
                            } else if (tmpOrder.getType().equals(String.valueOf(Const.DHL_FFM_PARTNER_ID))) {
                                if (tmpOrder.getMessage() != null && StringUtils.hasText(tmpOrder.getMessage())) {
                                    updDoNew.setFfmCode(tmpOrder.getMessage());
                                }
                            } else if (tmpOrder.getType().equals(Const.KERRY_TH_PARTNER_CODE)) {
                                updDoNew.setFfmCode(tmpOrder.getMessage());
                                updDoNew.setTrackingCode(tmpOrder.getMessage());
                            } else if (tmpOrder.getType().equals(Const.FLASH_TH_PARTNER_CODE)) {
                                String[] trackings = tmpOrder.getMessage().split("\\|");
                                updDoNew.setFfmCode(trackings[0]);
                                updDoNew.setTrackingCode(trackings[1]);
                            } else if (tmpOrder.getType().equals(String.valueOf(Const.HAISTAR_PARTNER_ID))) {
                                updDoNew.setTrackingCode(tmpOrder.getMessage());
                            } else if(tmpOrder.getType().equals(Const.NTL_EXPRESS_PARTNER_CODE)){
                                if(tmpOrder.getMessage() != null && StringUtils.hasText(tmpOrder.getMessage())){
                                    updDoNew.setFfmCode(tmpOrder.getMessage());
                                }
                            } else
                                updDoNew.setTrackingCode(tmpOrder.getMessage());
                            break;
                        case Const.DO_ERROR:
                            isAllSuccess = false;
                            updDoNew.setErrorMessage(tmpOrder.getMessage() + "|" + tmpOrder.getErrMessage());
                            updDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
                            break;
                        default:
                            isAllSuccess = false;
                            updDoNew.setErrorMessage(tmpOrder.getCode() + "|" + tmpOrder.getErrMessage());
                            break;
                    }
                    logService.updDoNew(SESSION_ID, updDoNew);
                }
            }
            if (isAllSuccess)
                return true;
        }
        return false;
    }

    private List<ProductDto> getProductListBySoId(int soId, int orgId, Integer partnerId) {
        List<ProductDto> lst = new ArrayList<>();
        GetSoItem getSoItem = new GetSoItem();
        // getSoItem.setSoId(insDoNew.getSoId());

        getSoItem.setSoId(soId);
        DBResponse<List<GetSoItemResp>> responseSOItem = deliveryOrderService.getSaleOrderItem(SESSION_ID, getSoItem);

        // lay tong gia tri don hang
        Double totalAmount = 0d;

        GetSOV2 params = new GetSOV2();
        params.setSoId(soId);
        params.setOrgId(orgId);
        DBResponse<List<SaleOrder>> resSO = deliveryOrderService.getSaleOrderV2(SESSION_ID, params);
        if (!resSO.getResult().isEmpty()) {
            SaleOrder so = resSO.getResult().get(0);
            totalAmount = so.getAmount();
        }

        List<GetSoItemResp> lstSOItem = responseSOItem.getResult();

        if (lstSOItem.size() > 0) {
            // tinh gia tri chia deu cho so loai san pham trong don hang
            Double amountPerSOItem = 0d;
            if (totalAmount > 0)
                amountPerSOItem = totalAmount / lstSOItem.size();

            for (int j = 0; j < lstSOItem.size(); j++) {

                int proId = lstSOItem.get(j).getProdId();
                // get product detail
                GetProductParams productParams = new GetProductParams();
                productParams.setProdId(proId);

                DBResponse<List<PDProduct>> dbProd = clProductService.getProduct(SESSION_ID, productParams);
                if (dbProd.getResult().isEmpty())
                    return new ArrayList<>();// khong cho phep tao don hang nay

                GetProductMapping productMapping = new GetProductMapping();
                productMapping.setProductId(proId);
                productMapping.setPartnerId(partnerId);

                DBResponse<List<GetProductMappingResp>> dbProdMapping = clProductService.getProductMapping(SESSION_ID,
                        productMapping);
                if (dbProdMapping.getResult().isEmpty())
                    return new ArrayList<>();// khong cho phep tao don hang nay

                List<PDProduct> lstProd = dbProd.getResult();

                // tinh gia tri discount tinh cho loai san pham
                Double amountPerProduct = 0d;
                if (amountPerSOItem > 0)
                    amountPerProduct = amountPerSOItem / lstProd.size();

                for (int i = 0; i < lstProd.size(); i++) {
                    GetProductMappingResp productMappingResp = dbProdMapping.getResult().get(0);

                    ProductDto product = new ProductDto();
                    product.setProId(proId);
                    // gan nhan la ten san pham lay theo i
                    product.setProName(lstProd.get(i).getName());
                    // lay theo don hang
                    int qty = lstSOItem.get(j).getQuantity();
                    int qtyCombo = 1;
                    Double price = (double) lstSOItem.get(j).getPrice();

                    // check product la combo hay khong
                    if (lstProd.get(i).getProductType() != null && lstProd.get(i).getProductType() == 2) {
                        GetProductCombo getProductCombo = new GetProductCombo();
                        GetProductComboResp comboResps = null;
                        getProductCombo.setComboId(proId);
                        DBResponse<List<GetProductComboResp>> comboDb = clProductService.getProductCombo(SESSION_ID,
                                getProductCombo);
                        if (!comboDb.getResult().isEmpty()) {
                            comboResps = comboDb.getResult().get(0); // 1 combo chi co 1 ban ghi mapping ben product
                            // combo nen chi can get 0
                            qtyCombo = comboResps.getQuantity();
                            price = BigDecimal.valueOf(comboResps.getPrice() / qtyCombo)
                                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
                        }
                    }

                    // tinh gia tri 1 mat hang
                    if (amountPerProduct > 0)
                        price = BigDecimal.valueOf(amountPerProduct / (qty * qtyCombo))
                                .setScale(2, RoundingMode.HALF_UP).doubleValue();

                    product.setPrice(price);
                    product.setQty(qty * qtyCombo);

                    if (productMappingResp.getPartnerProductCode() != null)
                        product.setProPartnerCode(productMappingResp.getPartnerProductCode());

                    product.setProCode(productMappingResp.getPartnerProductId().toString());
                    lst.add(product);
                }
            }
            if (lstSOItem.size() != lst.size())
                return new ArrayList<>();// khong cho phep tao don hang nay

            // TODO need change to list product not only one
//            deliveryOrder.setProdId(lstSOItem.get(0).getProdId());
        }
        return lst;
    }

    private SaleOrder getSaleOrderDetail(Integer soId) throws TMSException {
        GetSOV2 params = new GetSOV2();
        params.setSoId(soId);
        params.setOrgId(snagAsGEO().get(_COUNTRY));
        DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrderV2(SESSION_ID, params);
        if (dbResponseSaleOrder.getResult().isEmpty())
            throw new TMSException(ErrorMessage.NOT_FOUND);
        SaleOrder soData = dbResponseSaleOrder.getResult().get(0);
        return soData;
    }

    private Map<String, Integer> snagAsGEO() {
        Map<String, Integer> map = new HashMap<>();
        map.put("vn", 4);
        map.put("id", 9);
        map.put("th", 10);
        return map;
    }


}
