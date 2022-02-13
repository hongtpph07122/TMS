package com.tms.api.rest;

import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.api.dto.*;
import com.tms.api.dto.Response.ShippingResponseDTO;
import com.tms.api.entity.*;
import com.tms.api.enums.RescueTypeEnum;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.*;
import com.tms.api.repository.RcActionMappingRepository;
import com.tms.api.repository.RcActivityRepository;
import com.tms.api.repository.RcLastmileStatusRepository;
import com.tms.api.repository.RcRescueJobRepository;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.DOService;
import com.tms.api.service.RescueSqlNativeService;
import com.tms.api.service.impl.RescueServiceImpl;
import com.tms.api.task.DBLogWriter;
import com.tms.dto.*;
import com.tms.entity.CLFresh;
import com.tms.entity.SaleOrder;
import com.tms.entity.log.InsDeliveryOrder;
import com.tms.entity.log.UpdDeliveryOrder;
import com.tms.entity.log.UpdDoNew;
import com.tms.ff.dto.request.GHN.GhnOrderInfoRequestDto;
import com.tms.ff.service.GHN.impl.GhnOrderServiceImpl;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.DeliveryOrderService;
import com.tms.service.impl.LogService;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/DO")
public class DOController extends BaseController {

    Logger logger = LoggerFactory.getLogger(DOController.class);
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    CLFreshService clFreshService;

    @Autowired
    DeliveryOrderService deliveryOrderService;
    @Autowired
    LogService logService;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DBLogWriter dbLog;

    @Autowired
    RcLastmileStatusRepository rcLastmileStatusRepository;

    @Autowired
    RcActionMappingRepository rcActionMappingRepository;

    @Autowired
    RcRescueJobRepository rcRescueJobRepository;

    @Autowired
    RcActivityRepository rcActivityRepository;

    @Autowired
    RescueServiceImpl rescueService;

    @Autowired
    DOService doService;

    private final RescueSqlNativeService rescueSqlNativeService;

    @Autowired
    public DOController(RescueSqlNativeService rescueSqlNativeService) {
        this.rescueSqlNativeService = rescueSqlNativeService;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        cell.setCellType(CellType.STRING);
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    return dateFormat.format(date);
                } else {
                    return cell.getNumericCellValue() + "";
                }
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    @GetMapping
    public @ResponseBody ResponseEntity<?> findDeliveryOrdersShipping(GetDoNewV2 deliveryOrderParams) {
        ObjectRequestDTO objectRequestDTO = new ObjectRequestDTO();
        objectRequestDTO.setUserId(_curUser.getUserId());
        objectRequestDTO.setOrganizationId(getCurOrgId());
        objectRequestDTO.setSessionId(SESSION_ID);
        logger.info("body current user request : {}", LogHelper.toJson(objectRequestDTO));
        ShippingResponseDTO listShippingResponse = doService.snagShippingDeliveryOrders(objectRequestDTO, deliveryOrderParams);
        return new ResponseEntity<>(TMSResponse.buildResponse(listShippingResponse.getListShipping(), listShippingResponse.getCounter()), HttpStatus.OK);
    }

    @PostMapping("/shipping-orders")
    public @ResponseBody ResponseEntity<?> findDeliveryOrdersShippingV2(GetDoNewV2 deliveryOrderParams) {
        return this.findDeliveryOrdersShipping(deliveryOrderParams);
    }

    @GetMapping("/{doTrackingCode}/tracking")
    public TMSResponse getDOTracking(@PathVariable String doTrackingCode) {
        GhnOrderInfoRequestDto orderRequestDto = new GhnOrderInfoRequestDto();
        GhnOrderServiceImpl ghnOrderService = new GhnOrderServiceImpl();
        orderRequestDto.setOrderCode(doTrackingCode);
//        OrderInfoResponseDto orderInfoResponseDto = ghnOrderService.getOrderResponse(GHNConstant.hostName, null, "/api/v1/apiv3/OrderInfo",
//                orderRequestDto);
     /*   if (orderInfoResponseDto.getData() == null) {
            return TMSResponse.buildResponse(orderInfoResponseDto.getMsg());
        }
        return TMSResponse.buildResponse(orderInfoResponseDto.getData().getCurrentStatus());*/
        return null;
    }

    @GetMapping("{doId}")
    public TMSResponse getDO(@PathVariable Integer doId) throws TMSException {

       /* GetDOParams doParams = new GetDOParams();
        doParams.setDoId(doId);
        doParams.setOrgId(getCurOrgId());
        DBResponse<List<DeliveryOrder>> dbResponse = deliveryOrderService.getDO(SESSION_ID, doParams);*/
        GetDoNew doParams = new GetDoNew();
        doParams.setDoId(doId);
        doParams.setOrgId(getCurOrgId());
        DBResponse<List<GetDoNewResp>> dbResponse = deliveryOrderService.getDoNew(SESSION_ID, doParams);

        if (dbResponse.getResult().isEmpty())
            throw new TMSException(ErrorMessage.DELIVERY_ORDER_NOT_FOUND);
        GetDoNewResp doData = dbResponse.getResult().get(0);
        if (!StringUtils.isEmpty(doData.getLastmileReturnCode()) && doData.getLastmileReturnCode().toLowerCase().equals("null")) {
            doData.setLastmileReturnCode(null);
        }

        int soId = dbResponse.getResult().get(0).getSoId();
        GetSOParams params = new GetSOParams();
        params.setSoId(soId);
        params.setOrgId(getCurOrgId());
        DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrder(SESSION_ID, params);
        if (dbResponseSaleOrder.getResult().isEmpty()) {
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        SaleOrder soData = dbResponseSaleOrder.getResult().get(0);
        GetFreshLeadParams getFreshLeadParams = new GetFreshLeadParams();
        getFreshLeadParams.setLeadId(soData.getLeadId());
        getFreshLeadParams.setOrgId(getCurOrgId());
        DBResponse<List<CLFresh>> dbResponseCLFresh = clFreshService.getFreshLead(SESSION_ID, getFreshLeadParams);
        if (dbResponseCLFresh.getResult().isEmpty()) {
            throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
        }
        CLFresh leadData = dbResponseCLFresh.getResult().get(0);
        ResponeDeliveryOrderDto response = new ResponeDeliveryOrderDto();
        response.setSoData(soData);
        response.setLeadData(leadData);
        response.setDoData(doData);

        return TMSResponse.buildResponse(response);
//        return TMSResponse.buildResponse(dbResponse.getResult());
    }

    @PutMapping("/cancel/{doId}")
    public TMSResponse cancelDO(@PathVariable Integer doId) throws TMSException {
       /* GhnOrderServiceImpl ghnOrderService = new GhnOrderServiceImpl();
        CancelOrderRequestDto cancelOrderRequestDto = new CancelOrderRequestDto();
        cancelOrderRequestDto.setOrderCode(doTrackingCode);
        CancelOrderResponseDto cancelOrderResponseDto = ghnOrderService.cancelOrder(EnumType.GHN.GHN_HOST.getValue(),
                null, EnumType.GHN.GHN_CANCEL_DO.getValue(), cancelOrderRequestDto);
        // TODO cancel DO
        return cancelOrderResponseDto;*/
//        int do
        GetDoNew getDoNew = new GetDoNew();
        getDoNew.setDoId(doId);
        getDoNew.setOrgId(getCurOrgId());
        DBResponse<List<GetDoNewResp>> dbDoNew = deliveryOrderService.getDoNew(SESSION_ID, getDoNew);
        if (dbDoNew.getResult().isEmpty()) {
            throw new TMSException(ErrorMessage.DELIVERY_ORDER_NOT_FOUND);
        }
        GetDoNewResp doNew = dbDoNew.getResult().get(0);
        int lastMileId = doNew.getCarrierId();
        int ffId = doNew.getFfmId();
        boolean isSuccess = false;
        UpdDoNew updDoNew = new UpdDoNew();
        updDoNew.setDoId(doId);
        OrderResult orderResult = new OrderResult();
        orderResult.setResult(Const.DO_SUCCESS);

        /*switch (carrierId) {
            case Const.VT_PARTNER_ID:
                String trackingId = doNew.getTrackingCode();
                orderResult = DeliveryHelper.CancelVTDelivery(trackingId);
                break;
            case Const.DHL_PARTNER_ID:
                String shipmentId = doNew.getDoCode();
                orderResult = DeliveryHelper.CancelDHLDelivery(shipmentId);
                break;
            case Const.WFS_PARTNER_ID:
                Integer saleOrderId = doNew.getFfmId();
                orderResult = DeliveryHelper.CancelWFSDelivery(String.valueOf(saleOrderId));
                break;
            case Const.GHN_PARTNER_ID:
            default:
                throw new TMSException(ErrorMessage.CARRIER_NOT_FOUND);
        }*/

        //chi cancel lastmile DHL (hien tai co DHL va GHNE nen chi cancel DHL
        if (lastMileId == Const.DHL_PARTNER_ID || lastMileId == Const.DHL_FFM_PARTNER_ID)
            orderResult = DeliveryHelper.cancelDelivery(lastMileId, doNew, getCurOrgId());//cancel lastmile

        OrderResult ffResult = new OrderResult();
        //cancel lastmile OK?
        //need to change in SoControler, function cancelSO(@PathVariable Integer soId)
        if (orderResult != null && orderResult.getResult().equals(Const.DO_SUCCESS)) {
            //cancel FF
            if (ffId == Const.BM_PARTNER_ID || ffId == Const.WFS_PARTNER_ID || ffId == Const.MYCLOUD_FFM_PARTNER_ID)
                ffResult = DeliveryHelper.cancelDelivery(ffId, doNew, getCurOrgId());

            if(orderResult.getType().equals(String.valueOf(Const.DHL_FFM_PARTNER_ID))){
                updDoNew.setStatus(EnumType.DO_STATUS_ID.CANCEL.getValue());
                isSuccess = true;
			}
            else if (ffResult != null && ffResult.getResult().equals(Const.DO_SUCCESS)) {
                updDoNew.setStatus(EnumType.DO_STATUS_ID.CANCEL.getValue());
                isSuccess = true;
            } else {
                updDoNew.setErrorMessage(ffResult.getMessage());
            }

            //TODO write log DO
            dbLog.writeDOStatusLog(_curUser.getUserId(), doId, EnumType.DO_STATUS_ID.CANCEL.getValue(), "Cancel DO");

            //Cancel success update redis, remove order doid

        } else {
            updDoNew.setErrorMessage(ffResult.getMessage());
        }
        logService.updDoNew(SESSION_ID, updDoNew);
        if (isSuccess)
            return TMSResponse.buildResponse(orderResult.getCode(), 1, orderResult.getMessage(), 200);
        return TMSResponse.buildResponse(null, 0, "Failed to cancel DO", 400);

    }

    @PostMapping()
    public TMSResponse create(@RequestBody InsDeliveryOrder deliveryOrder) throws TMSException {

       /* CreateOrderRequestDto createOrderRequestDto = new CreateOrderRequestDto();
        createOrderRequestDto.setPaymentTypeID(1);
        createOrderRequestDto.setFromDistrictID(1455);
        createOrderRequestDto.setToDistrictID(1462);
        createOrderRequestDto.setClientContactName("Giao Hang Nhanh");
        createOrderRequestDto.setClientContactPhone("19001206");
        createOrderRequestDto.setClientAddress("Ha Noi");
        createOrderRequestDto.setShippingAddress("Ha Noi");
        createOrderRequestDto.setNoteCode("CHOTHUHANG");
        createOrderRequestDto.setServiceID(53319);
        createOrderRequestDto.setCouponCode("");
        createOrderRequestDto.setWeight(10200);
        createOrderRequestDto.setLength(10);
        createOrderRequestDto.setWidth(10);
        createOrderRequestDto.setHeight(10);
        createOrderRequestDto.setReturnContactName("Syntel");
        createOrderRequestDto.setReturnContactPhone("19001206");
        createOrderRequestDto.setReturnAddress("Ha Noi");
        createOrderRequestDto.setReturnDistrictID(1455);
        GhnOrderServiceImpl ghnOrderService = new GhnOrderServiceImpl();
        CreateOrderResponseDto createOrderResponseDto = ghnOrderService.createOrder(GHNConstant.hostName, null,
                EnumType.GHN.GHN_CREATE_DO.getValue(), createOrderRequestDto);
        if (createOrderResponseDto.getData().getOrderCode() != null) {
            deliveryOrder.setTrackingCode(createOrderResponseDto.getData().getOrderCode());
        }
        DBResponse dbResponse = logService.insDeliveryOrder(SESSION_ID, deliveryOrder);
        // TODO them
        // properties vao InsDeliveryOrder
        return TMSResponse.buildResponse(dbResponse.getResult());*/
        return null;
    }

    @PutMapping()
    public TMSResponse update(@RequestBody UpdDeliveryOrder deliveryOrder) throws TMSException {
       /* GhnOrderServiceImpl ghnOrderService = new GhnOrderServiceImpl();
        UpdateOrderRequestDto updateOrderRequestDto = new UpdateOrderRequestDto();
        updateOrderRequestDto.setShippingOrderID(556754);
        updateOrderRequestDto.setOrderCode(deliveryOrder.getTrackingCode());
        updateOrderRequestDto.setPaymentTypeID(1);
        updateOrderRequestDto.setFromDistrictID(1452);
        updateOrderRequestDto.setToDistrictID(1452);
        updateOrderRequestDto.setNoteCode("CHOTHUHANG");
        updateOrderRequestDto.setNote("Giao Hang Nhanh");
        updateOrderRequestDto.setClientContactName("client name");
        updateOrderRequestDto.setClientContactPhone("01647826167");
        updateOrderRequestDto.setClientAddress("Ha noi");
        updateOrderRequestDto.setShippingAddress("Ha noi");
        updateOrderRequestDto.setServiceID(53320);
        updateOrderRequestDto.setCouponCode("");
        updateOrderRequestDto.setWeight(10200);
        updateOrderRequestDto.setLength(10);
        updateOrderRequestDto.setWidth(10);
        updateOrderRequestDto.setHeight(10);
        updateOrderRequestDto.setReturnContactName("");
        updateOrderRequestDto.setReturnContactPhone("");
        updateOrderRequestDto.setReturnAddress("");
        updateOrderRequestDto.setReturnDistrictCode("");
        UpdateOrderResponseDto updateOrderResponseDto = ghnOrderService.updateOrder(GHNConstant.hostName, null,
                EnumType.GHN.GHN_UPDATE_DO.getValue(), updateOrderRequestDto);
        // TODO them properties vao UpdDeliveryOrder
        DBResponse dbResponse = logService.updDeliveryOrder(SESSION_ID, deliveryOrder);
        return TMSResponse.buildResponse(updateOrderRequestDto);*/
        return null;
    }

    @GetMapping("/config")
    public TMSResponse getConfig() {
        return TMSResponse.buildResponse(com.tms.ff.utils.DHL.DHLConst.hostName);
    }

    @RequestMapping(value = "/pending", method = {RequestMethod.GET, RequestMethod.POST})
    public TMSResponse<?> findAllDeliveryOrdersPending(GetShipping shippingRequest) {
        shippingRequest.setOrgId(getCurOrgId());
        ShippingPendingRespDto response = doService.snagDeliveryOrdersPending(shippingRequest);
        return TMSResponse.buildResponse(response.getListShippingPending(), response.getRowCount());
    }

    @RequestMapping(value = "/pending/csv", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> exportShippingPendingList(GetShipping getShipping) throws TMSException {
        boolean isTeamLeader = (Helper.isTeamLeader(_curUser) || Helper.isTeamLeaderOther(_curUser));
        boolean isDirector = Helper.isDirector(_curUser);
        OrderManagementRequestDTO orderManagementRequestDTO = new OrderManagementRequestDTO();
        orderManagementRequestDTO.setSessionId(SESSION_ID);
        orderManagementRequestDTO.setUserId(getCurrentUser().getUserId());
        orderManagementRequestDTO.setOrgId(getCurOrgId());
        orderManagementRequestDTO.setTeamLeader(isTeamLeader);
        orderManagementRequestDTO.setDirector(isDirector);
        byte[] csvAsByte = doService.exportCSVShippingPending(getShipping, orderManagementRequestDTO);
        return TMSResponse.buildExcelFileResponse(csvAsByte, "shippingPending.csv");
    }

    private TMSResponse<List<ShippingPendingDto>> getDOPending(GetDoNew getDoNew) throws TMSException {
        int curOrgId = getCurOrgId();

        getDoNew.setOrgId(getCurOrgId());
        getDoNew.setErrorMessage(Const.DB_TMS_NOTNULL);
        //TODO need Function from DB to check trackingcode not null

        if (getDoNew.getLimit() == null) {
            getDoNew.setLimit(Const.DEFAULT_PAGE_SIZE);
        }

        //params.setStatus(EnumType.DO_STATUS_ID.REJECT.getValue());
        //TODO need Function from DB to check trackingcode not null
//        params.setTrackingCode(!null);
        DBResponse<List<GetDoNewResp>> dbResponse = deliveryOrderService.getDoNew(SESSION_ID, getDoNew);
//        Map<List<ShippingPendingDto>, Integer> lstShippingMap = new HashMap<>();
        List<ShippingPendingDto> lstShipping = new ArrayList<>();
        if (!dbResponse.getResult().isEmpty()) {
            List<GetDoNewResp> lst = dbResponse.getResult();
            //TODO can xem lai cho nay, vao DB hoi nhieu
            for (int i = 0; i < lst.size(); i++) {
                GetDoNewResp deliveryOrder = lst.get(i);
                ShippingPendingDto shippingDto = new ShippingPendingDto(deliveryOrder);
                if (shippingDto.getCustomerId() != null && shippingDto.getCustomerId() > 0) {
                    GetLeadParamsV4 getLeadParams = new GetLeadParamsV4();
                    getLeadParams.setLeadId(shippingDto.getCustomerId());
                    getLeadParams.setOrgId(curOrgId);
                    DBResponse<List<CLFresh>> lstFresh = clFreshService.getLeadV4(SESSION_ID, getLeadParams);
                    if (!lstFresh.getResult().isEmpty()) {
                        shippingDto.setCustomer(lstFresh.getResult().get(0));
                    }
                }


                if (shippingDto.getSoId() != null && shippingDto.getSoId() > 0) {
                    GetSOParams soParams = new GetSOParams();
                    soParams.setSoId(shippingDto.getSoId());
                    soParams.setOrgId(curOrgId);
                    DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrder(SESSION_ID, soParams);
                    if (!dbResponseSaleOrder.getResult().isEmpty()) {
                        shippingDto.setSaleOrder(dbResponseSaleOrder.getResult().get(0));
                    }
                }
                lstShipping.add(shippingDto);
            }
        }
//        lstShippingMap.put(lstShipping, dbResponse.getRowCount());
        return TMSResponse.buildResponse(lstShipping, dbResponse.getRowCount());
    }

    private TMSResponse<List<GetShippingResp>> getListShippingPending(GetShipping data, boolean export) throws TMSException {
        int curOrgId = getCurOrgId();
        GetShipping params = new GetShipping();
        if (data != null) {
            params = modelMapper.map(data, GetShipping.class);
        }
        params.setOrgId(curOrgId);
        params.setDoStatus(Const.DO_SHIPPING_PENDING);
        if (data.getLimit() == null && export == false) {
            params.setLimit(Const.DEFAULT_PAGE_SIZE);
        }
        DBResponse<List<GetShippingResp>> dbResponse = deliveryOrderService.getShipping(SESSION_ID, params);
        try {
            if (dbResponse != null || !dbResponse.getResult().isEmpty()) {
                return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
            }
        } catch (Exception e) {
            return TMSResponse.buildResponse(null, 0);
        }
        return null;
    }

    @GetMapping("/pending2")
    public TMSResponse<List<ShippingPendingDto>> getDOPendingList2(GetDoNew getDoNew) throws TMSException {
        int curOrgId = getCurOrgId();

        getDoNew.setOrgId(getCurOrgId());
        getDoNew.setErrorMessage(Const.DB_TMS_NOTNULL);
        //TODO need Function from DB to check trackingcode not null

        if (getDoNew.getLimit() == null) {
            getDoNew.setLimit(Const.DEFAULT_PAGE_SIZE);
        }

        //params.setStatus(EnumType.DO_STATUS_ID.REJECT.getValue());
        //TODO need Function from DB to check trackingcode not null
//        params.setTrackingCode(!null);
        DBResponse<List<GetDoNewResp>> dbResponse = deliveryOrderService.getDoNew(SESSION_ID, getDoNew);
        List<ShippingPendingDto> lstShipping = new ArrayList<>();
        if (!dbResponse.getResult().isEmpty()) {
            List<GetDoNewResp> lst = dbResponse.getResult();
            //TODO can xem lai cho nay, vao DB hoi nhieu
            for (int i = 0; i < lst.size(); i++) {
                GetDoNewResp deliveryOrder = lst.get(i);
                ShippingPendingDto shippingDto = new ShippingPendingDto(deliveryOrder);
                if (shippingDto.getCustomerId() != null && shippingDto.getCustomerId() > 0) {
                    GetLeadParamsV4 getLeadParams = new GetLeadParamsV4();
                    getLeadParams.setLeadId(shippingDto.getCustomerId());
                    getLeadParams.setOrgId(curOrgId);
                    DBResponse<List<CLFresh>> lstFresh = clFreshService.getLeadV4(SESSION_ID, getLeadParams);
                    if (!lstFresh.getResult().isEmpty()) {
                        shippingDto.setCustomer(lstFresh.getResult().get(0));
                    }
                }


                if (shippingDto.getSoId() != null && shippingDto.getSoId() > 0) {
                    GetSOParams soParams = new GetSOParams();
                    soParams.setSoId(shippingDto.getSoId());
                    soParams.setOrgId(curOrgId);
                    DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrder(SESSION_ID, soParams);
                    if (!dbResponseSaleOrder.getResult().isEmpty()) {
                        shippingDto.setSaleOrder(dbResponseSaleOrder.getResult().get(0));
                    }
                }
                lstShipping.add(shippingDto);
            }
        }
        return TMSResponse.buildResponse(lstShipping, dbResponse.getRowCount());
//        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }


    @PutMapping("/update/status")
    public TMSResponse<Boolean> updateDoStatus(@RequestBody UpdateDOStatusDto params) throws TMSException {
        UpdDoNew updDoParams = new UpdDoNew();
        for (Integer doId : params.getDoIds()) {
            if (doId == null) {
                continue;
            }
            dbLog.writeDOStatusLog(_curUser.getUserId(), doId, params.getStatus(), "updateDoStatus");
            updDoParams.setDoId(doId);
            updDoParams.setStatus(params.getStatus());
            DBResponse dbResponse = logService.updDoNew(SESSION_ID, updDoParams);
            if (dbResponse == null) {
                return TMSResponse.buildResponse(false);
            }
        }
        return TMSResponse.buildResponse(true);
    }

    @GetMapping("/dhl")
    public TMSResponse<List<GetDoNewResp>> getDHLList(GetDoNew params) throws TMSException {
//        GetDoNew getDoNew = new GetDoNew();
        params.setOrgId(getCurOrgId());

        params.setCarrierId(EnumType.CARRIER_ID.DHL.getValue());
        params.setStatus(EnumType.DO_STATUS_ID.NEW.getValue());
        if (params.getLimit() == null) {
            params.setLimit(Const.DEFAULT_PAGE_SIZE);
        }

        DBResponse<List<GetDoNewResp>> dbResponse = deliveryOrderService.getDoNew(SESSION_ID, params);
        if (dbResponse == null || dbResponse.getResult().isEmpty()) {
            return TMSResponse.buildResponse(new ArrayList<GetDoNewResp>(), 0);
        }
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @GetMapping("/carrier/{carrier}")
    public TMSResponse<List<GetDoNewResp>> getCarrierList(@PathVariable String carrier, GetDoNew params) throws TMSException {
        if (!carrier.equals("dhl") && !carrier.equals("kerry") && !carrier.equals("sap")) {
            return TMSResponse.buildResponse(false, 0, "Not configure URL", 200);
        } else {
            this.setGetDOParamForCarrier(carrier, params);
        }
        DBResponse<List<GetDoNewResp>> dbResponse = deliveryOrderService.getDoNew(SESSION_ID, params);
        if (dbResponse == null || dbResponse.getResult().isEmpty()) {
            return TMSResponse.buildResponse(new ArrayList<GetDoNewResp>(), 0);
        }
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @GetMapping("/{doId}/history")
    public TMSResponse<List<GetLogDoResp>> getDoHistory(@PathVariable Integer doId, GetLogDo params) throws TMSException {

//        params.setOrgId(getCurOrgId());
        params.setDoId(doId);
        if (params.getLimit() == null) {
            params.setLimit(Const.DEFAULT_PAGE_SIZE);
        }

        DBResponse<List<GetLogDoResp>> dbResponse = clFreshService.getLogDo(SESSION_ID, params);
        if (dbResponse == null || dbResponse.getResult().isEmpty()) {
            return TMSResponse.buildResponse(new ArrayList<GetLogDoResp>(), 0);
        }
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getResult().size());
    }

    @RequestMapping(value = "/shipping/csv", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> exportShippingList(GetDoNewV2 params) throws TMSException {
        boolean isTeamLeader = (Helper.isTeamLeader(_curUser) || Helper.isTeamLeaderOther(_curUser));
        boolean isDirector = Helper.isDirector(_curUser);
        OrderManagementRequestDTO orderManagementRequestDTO = new OrderManagementRequestDTO();
        orderManagementRequestDTO.setSessionId(SESSION_ID);
        orderManagementRequestDTO.setUserId(getCurrentUser().getUserId());
        orderManagementRequestDTO.setOrgId(getCurOrgId());
        orderManagementRequestDTO.setTeamLeader(isTeamLeader);
        orderManagementRequestDTO.setDirector(isDirector);
        byte[] csvAsByte = doService.exportCSVShipping(params, orderManagementRequestDTO);
        return TMSResponse.buildExcelFileResponse(csvAsByte, "shipping.csv");
    }

    @GetMapping("/{carrier}/csv")
    public ResponseEntity exportCarrierList(@PathVariable String carrier, GetDoNew params) throws TMSException {
        logger.info("Export carrier: " + carrier);
        boolean isTeamLeader = (Helper.isTeamLeader(_curUser) || Helper.isTeamLeaderOther(_curUser));
        boolean isDirector = Helper.isDirector(_curUser);
        OrderManagementRequestDTO orderManagementRequestDTO = new OrderManagementRequestDTO();
        orderManagementRequestDTO.setSessionId(SESSION_ID);
        orderManagementRequestDTO.setUserId(getCurrentUser().getUserId());
        orderManagementRequestDTO.setOrgId(getCurOrgId());
        orderManagementRequestDTO.setTeamLeader(isTeamLeader);
        orderManagementRequestDTO.setDirector(isDirector);
        GetDoNewV2 params2 = new GetDoNewV2();

        if (!carrier.equals("dhl") && !carrier.equals("kerry") && !carrier.equals("sap")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Carrier not found");
        } else {
            this.setGetDOParamForCarrier(carrier, params, params2);
        }
//        DBResponse<List<GetDoNewResp>> dbResponseDO = deliveryOrderService.getDoNew(SESSION_ID, params);

        byte[] csvAsByte = doService.exportCSVShipping(params2, orderManagementRequestDTO);

//        List<GetDoNewResp> dbResponse = new ArrayList<GetDoNewResp>();
//        if (dbResponseDO != null && !dbResponseDO.getResult().isEmpty()) {
//            dbResponse = dbResponseDO.getResult();
//            dbLog.writeAgentActivityLog(getCurrentUser().getUserId(), "export CarrierList", "exportCarrierList", dbResponse.size(), "getDoNew", LogHelper.toJson(params));
//        }
//        byte[] out = ExcelHelper.createExcelData(dbResponse, GetDoNewResp.class);
        return TMSResponse.buildExcelFileResponse(csvAsByte, carrier + ".csv");
    }

    @GetMapping("/dhl/ids/csv")
    public ResponseEntity exportDHLListByDoIds(@RequestParam List<Integer> doIds) throws TMSException {
        List<GetDoNewResp> dbResponse = new ArrayList<GetDoNewResp>();
        GetDoNew getDoNew = new GetDoNew();
        getDoNew.setOrgId(getCurOrgId());
        for (Integer doId : doIds) {
            if (doId == null) {
                continue;
            }
            getDoNew.setDoId(doId);
            DBResponse<List<GetDoNewResp>> dbResponseDO = deliveryOrderService.getDoNew(SESSION_ID, getDoNew);
            if (dbResponseDO != null && !dbResponseDO.getResult().isEmpty()) {
                dbResponse.addAll(dbResponseDO.getResult());
                dbLog.writeAgentActivityLog(getCurrentUser().getUserId(), "export DHLListByDoIds", "exportDHLListByDoIds", dbResponse.size(), "getDoNew", LogHelper.toJson(doIds));
            }
        }
        byte[] out = ExcelHelper.createExcelData(dbResponse, GetDoNewResp.class);
        return TMSResponse.buildExcelFileResponse(out, "dhl.csv");
    }

    @GetMapping("/remove")
    public TMSResponse Validate(@RequestBody List<Integer> doIds) throws TMSException {
//        List<SaleOrder> result = new ArrayList<>();
        int userId = getCurrentUser().getUserId();
        //get list SO from DB
        List<Integer> lstWithoutDupDOId = doIds.stream()
                .distinct().collect(java.util.stream.Collectors.toList());

        for (Integer id : lstWithoutDupDOId) {
            GetDoNew doParams = new GetDoNew();
            doParams.setDoId(id);
            doParams.setOrgId(getCurOrgId());
            DBResponse<List<GetDoNewResp>> dbResponse = deliveryOrderService.getDoNew(SESSION_ID, doParams);
            if (dbResponse.getResult().isEmpty()) {
                //   throw new TMSException(ErrorMessage.DELIVERY_ORDER_NOT_FOUND);
                logger.info("Could not found DoID === " + id);
                continue;
            }
            int soId = dbResponse.getResult().get(0).getSoId();

            com.tms.entity.log.UpdSaleOrder updSaleOrder = new com.tms.entity.log.UpdSaleOrder();
            updSaleOrder.setStatus(EnumType.SALE_ORDER_STATUS.PENDING.getValue());
            updSaleOrder.setSoId(soId);
            DBResponse response = logService.updSaleOrder(SESSION_ID, updSaleOrder);

            dbLog.writeAgentActivityLog(userId, "update sale order DO remove " + id, "sale order", soId, "so_status", EnumType.SALE_ORDER_STATUS.PENDING.getValue() + "");

            //delete old DO
            logService.delDoNew(SESSION_ID, String.valueOf(id));

        }
        return TMSResponse.buildResponse(true);
    }

    @PostMapping("/import")
    public TMSResponse excelDataToDB2(@RequestParam("file") MultipartFile readExcelDataFile) throws IOException, ParseException {
        logger.info("START IMPORT RESCUE");
        TMSResponse response = new TMSResponse();
        int updated = 0;
        int created = 0;
        int imported = 0;
        String messages = "";
        Date now = new Date();
        Timestamp nowTimestamp = new Timestamp(now.getTime());
        List<RcLastmileStatus> rcLastmileStatuses = new ArrayList<>();
        List<String> trackingCodes = new ArrayList<>();
        HashMap<String, List<String>> statusMap = new HashMap<>();

        Workbook book = WorkbookFactory.create(readExcelDataFile.getInputStream());
        Sheet sheet = book.getSheetAt(0);
        Map<String, Integer> headerIndexMap = new HashMap<String, Integer>();
        Row headerRow = sheet.getRow(0);
        short minColIx = headerRow.getFirstCellNum();
        short maxColIx = headerRow.getLastCellNum();
        for (short colIx = minColIx; colIx < maxColIx; colIx++) { //loop from first to last index
            Cell cell = headerRow.getCell(colIx);
            headerIndexMap.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
        }
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            Row dataRow = sheet.getRow(i);
            if (dataRow != null) {
                RcLastmileStatus rcLastmineStatus = new RcLastmileStatus();
                if (dataRow.getRowNum() == 0) { // header row
                    continue;
                }
                if (dataRow.getCell(headerIndexMap.get("tracking_code".toLowerCase())) != null) {
                    String tracking_code = getCellValue(dataRow.getCell(headerIndexMap.get("tracking_code".toLowerCase())));
                    rcLastmineStatus.setTracking_code(tracking_code);
                }
                if (dataRow.getCell(headerIndexMap.get("Consignee Address".toLowerCase())) != null) {
                    String address = getCellValue(dataRow.getCell(headerIndexMap.get("Consignee Address".toLowerCase())));
                    rcLastmineStatus.setConsignee_address(address);
                }
                if (dataRow.getCell(headerIndexMap.get("substatus".toLowerCase())) != null) {
                    String subStatus = getCellValue(dataRow.getCell(headerIndexMap.get("substatus".toLowerCase())));
                    if (subStatus.equals("")) {
                        rcLastmineStatus.setSubstatus("No Sub");
                    } else {
                        rcLastmineStatus.setSubstatus(subStatus);
                    }
                } else {
                    rcLastmineStatus.setSubstatus("No Sub");
                }
                if (dataRow.getCell(headerIndexMap.get("expected date".toLowerCase())) != null) {
                    String expected_date = getCellValue(dataRow.getCell(headerIndexMap.get("expected date".toLowerCase())));
                    if (!expected_date.equals("")) {
                        Timestamp expected_timestamp = DateHelper.dateToTimestamp(expected_date);
                        rcLastmineStatus.setExpected_date(expected_timestamp);
                    }

                }
                if (dataRow.getCell(headerIndexMap.get("Attempted Delivery 1 Date".toLowerCase())) != null) {
                    String attDelivery1Date = getCellValue(dataRow.getCell(headerIndexMap.get("Attempted Delivery 1 Date".toLowerCase())));
                    if (!attDelivery1Date.equals("")) {
                        Timestamp att_timestamp = DateHelper.dateToTimestamp(attDelivery1Date);
                        rcLastmineStatus.setAttempted_delivery_1_date(att_timestamp);
                    }
                }
                if (dataRow.getCell(headerIndexMap.get("COD amount".toLowerCase())) != null) {
                    String codAmount = getCellValue(dataRow.getCell(headerIndexMap.get("COD amount".toLowerCase())));
                    if (!codAmount.equals(""))
                        rcLastmineStatus.setCod_amount(Double.parseDouble(codAmount));
                }
                if (dataRow.getCell(headerIndexMap.get("Package Description".toLowerCase())) != null) {
                    String des = getCellValue(dataRow.getCell(headerIndexMap.get("Package Description".toLowerCase())));
                    rcLastmineStatus.setPackage_description(des);
                }
                if (dataRow.getCell(headerIndexMap.get("current warehouse".toLowerCase())) != null) {
                    String warehouse = getCellValue(dataRow.getCell(headerIndexMap.get("current warehouse".toLowerCase())));
                    rcLastmineStatus.setCurrent_warehouse(warehouse);
                }
                if (dataRow.getCell(headerIndexMap.get("status".toLowerCase())) != null) {
                    String status = getCellValue(dataRow.getCell(headerIndexMap.get("status".toLowerCase())));
                    rcLastmineStatus.setStatus(status);
                }
                if (dataRow.getCell(headerIndexMap.get("Consignee Name".toLowerCase())) != null) {
                    String name = getCellValue(dataRow.getCell(headerIndexMap.get("Consignee Name".toLowerCase())));
                    rcLastmineStatus.setConsignee_name(name);
                }
                if (dataRow.getCell(headerIndexMap.get("Consignee Phone".toLowerCase())) != null) {
                    String phone = getCellValue(dataRow.getCell(headerIndexMap.get("Consignee Phone".toLowerCase())));
                    rcLastmineStatus.setConsignee_phone(phone);
                }
                if (rcLastmineStatus != null) {
                    imported += 1;
                    rcLastmileStatuses.add(rcLastmineStatus);
                    trackingCodes.add(rcLastmineStatus.getTracking_code());
                    List<String> status = new ArrayList<>();
                    status.add(rcLastmineStatus.getStatus());
                    status.add(rcLastmineStatus.getSubstatus());
                    statusMap.put(rcLastmineStatus.getTracking_code(), status);
                }
            }

        }
        logger.info("START INSERT");
//        try {
//            rescueService.saveOrUpdateLastmileStatus(rcLastmileStatuses);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        logger.info("INSERT TEMP DONE");
        List<OdDONew> getDos = rescueService.getDOByTrackingcode(trackingCodes);
        List<RcRescueJob> updates = new ArrayList<>();
        List<RcRescueJob> creates = new ArrayList<>();
        List<RcActivity> rcActivities = new ArrayList<>();
        List<String> tracking_update = new ArrayList<>();
        for (OdDONew DO : getDos) {
            List<String> status = statusMap.get(DO.getTracking_code());
            if (status != null) {
                DO.setLastmile_reason(status.get(0));
                DO.setLastmile_reason_detail(status.get(1));
                tracking_update.add(DO.getTracking_code());
            }
        }
        rescueService.saveOrUpdateDO(getDos);
        logger.info("UPDATE STATUS IN DO DONE");
        List<OdDONew> odDONews = rescueService.getDOByAction(tracking_update, 1);
        if (odDONews == null) {
            logger.info("Action = 1, rescue not null, NULL");
        } else {
            for (OdDONew DO : odDONews) {
                RcRescueJob rcRescueJob = new RcRescueJob();
                rcRescueJob.setId(DO.getRescue_id());
                rcRescueJob.setLastmile_reason(DO.getLastmile_reason());
                rcRescueJob.setLastmile_reason_detail(DO.getLastmile_reason_detail());
                rcRescueJob.setPriority(DO.getRcActionMapping().getPriority());
                rcRescueJob.setUpdatedate(nowTimestamp);
                rcRescueJob.setUpdateby(0);
                /* begin::Check Rescue  ID is existed on pre-delivery? */
                if (rescueSqlNativeService.existByIdWithPreDelivery(rcRescueJob.getId(), true)) {
                    rcRescueJob.setJobType(RescueTypeEnum.ALL_RESCUE_TYPE.getJobType());
                    rcRescueJob.setPreDelivery(false);
                }
                /* end::Check Rescue  ID is existed on pre-delivery? */
                updates.add(rcRescueJob);
                RcActivity rcActivity = this.createActivity(DO, nowTimestamp, 1, true, DO.getRescue_id());
                rcActivities.add(rcActivity);
                updated++;
            }
        }

        List<OdDONew> odDONews2 = rescueService.getDOByAction(tracking_update, 2);
        if (odDONews2 == null) {
            logger.info("Action = 2, null");
        } else {
            for (OdDONew DO : odDONews2) {
                RcRescueJob rcRescueJob = new RcRescueJob();
                rcRescueJob.setId(DO.getRescue_id());
                rcRescueJob.setLastmile_reason(DO.getLastmile_reason());
                rcRescueJob.setLastmile_reason_detail(DO.getLastmile_reason_detail());
                rcRescueJob.setPriority(DO.getRcActionMapping().getPriority());
                rcRescueJob.setUpdatedate(nowTimestamp);
                rcRescueJob.setUpdateby(0);
                rcRescueJob.setJob_status(7);
                rcRescueJob.setJob_sub_status(3);
                updates.add(rcRescueJob);
                RcActivity rcActivity = this.createActivity(DO, nowTimestamp, 2, true, DO.getRescue_id());
                rcActivities.add(rcActivity);
                updated++;
            }
        }

        List<OdDONew> odDONews3 = rescueService.getDOByAction(tracking_update, 3);
        if (odDONews3 == null) {
            logger.info("Action = 3, null");
        } else {
            for (OdDONew DO : odDONews3) {
                RcRescueJob rcRescueJob = new RcRescueJob();
                rcRescueJob.setId(DO.getRescue_id());
                rcRescueJob.setLastmile_reason(DO.getLastmile_reason());
                rcRescueJob.setLastmile_reason_detail(DO.getLastmile_reason_detail());
                rcRescueJob.setPriority(DO.getRcActionMapping().getPriority());
                rcRescueJob.setUpdatedate(nowTimestamp);
                rcRescueJob.setUpdateby(0);
                rcRescueJob.setJob_status(7);
                rcRescueJob.setJob_sub_status(2);
                updates.add(rcRescueJob);
                RcActivity rcActivity = this.createActivity(DO, nowTimestamp, 2, true, DO.getRescue_id());
                rcActivities.add(rcActivity);
                updated++;
            }
        }

        List<OdDONew> odDONews1_null = rescueService.getDOByRescueNull(tracking_update, 1);
        HashMap<String, OdDONew> updateRcIds = new HashMap<>();
        if (odDONews1_null == null) {
            logger.info("None to create");
        } else {
            for (OdDONew DO : odDONews1_null) {
                RcRescueJob rcRescueJob = this.createRescue(DO, DO.getRcActionMapping(), nowTimestamp);
                creates.add(rcRescueJob);
                updateRcIds.put(DO.getTracking_code(), DO);
                created++;
            }
        }
        if (!creates.isEmpty()) {
            List<RcRescueJob> rcRescueJobs = rescueService.saveOrUpdateRescueJob(creates);
            List<OdDONew> updateDORcs = new ArrayList<>();
            for (RcRescueJob rc : rcRescueJobs) {
                OdDONew odDONew = updateRcIds.get(rc.getTracking_code());
                if (odDONew != null) {
                    RcActivity rcActivity = this.createActivity(odDONew, nowTimestamp, 1, false, rc.getId());
                    rcActivities.add(rcActivity);
                    odDONew.setRescue_id(rc.getId());
                    updateDORcs.add(odDONew);
                }
            }
            rescueService.saveOrUpdateDO(updateDORcs);
        }
        rescueService.bactchUpdateRescue(updates);
        rescueService.saveOrUpdateActivity(rcActivities);
//        rescueService.updateIsUpdated(trackingCodes);
//        rescueService.updateUpdatedStatus(tracking_update);
        logger.info("IMPORT RESCUE IS DONE!");

        messages = imported + " records imported | " + created + " rescues job created | " + updated + " rescues job updated";
        return response.buildResponse(null, imported, messages, 200);
    }

    @PostMapping("/job-auto-update")
    public TMSResponse<?> updateAndAutomateJob() {
        ResponseRescueFromDO result = doService.createAndUpdateRescue();
        int updated = result.getUpdated();
        int created = result.getCreated();
        int imported = result.getImported();
        String messages = imported + " records imported | " + created + " rescues job created | " + updated + " rescues job updated";
        logger.info(messages);
        return TMSResponse.buildResponse(null, imported, messages, 200);
    }

    private RcRescueJob createRescue(OdDONew DO, RcActionMapping rcActionMapping, Timestamp nowTimestamp) {
        RcRescueJob rcRescueJob = new RcRescueJob();
        rcRescueJob.setLastmile_reason(rcActionMapping.getStatus_name());
        rcRescueJob.setLastmile_reason_detail(rcActionMapping.getSub_status_name());
        rcRescueJob.setPriority(rcActionMapping.getPriority());
        rcRescueJob.setOrg_id(DO.getOrg_id());
        rcRescueJob.setTracking_code(DO.getTracking_code());
        rcRescueJob.setDo_id(DO.getDo_id());
        rcRescueJob.setDo_code(DO.getDo_code());
        rcRescueJob.setAmountcod(DO.getAmountcod());
        rcRescueJob.setCustomer_address(DO.getCustomer_address());
        rcRescueJob.setCustomer_id(DO.getCustomer_id());
        rcRescueJob.setCustomer_name(DO.getCustomer_name());
        rcRescueJob.setCreateby(DO.getCreateby());
        rcRescueJob.setFfm_code(DO.getFfm_code());
        rcRescueJob.setFfm_id(DO.getFfm_id());
        rcRescueJob.setJob_status(1);
        rcRescueJob.setSo_id(DO.getSo_id());
        rcRescueJob.setLastmile_id(DO.getCarrier_id());
        rcRescueJob.setCustomer_phone(DO.getCustomer_phone());
        rcRescueJob.setPackage_products(DO.getPackage_name());
        rcRescueJob.setDo_status(DO.getStatus());
        rcRescueJob.setCreateby(0);
        rcRescueJob.setUpdatedate(nowTimestamp);
        rcRescueJob.setCreatedate(nowTimestamp);
        rcRescueJob.setPreDelivery(false);
        rcRescueJob.setPreDeliveredBefore(false);
        rcRescueJob.setJobType(RescueTypeEnum.ALL_RESCUE_TYPE.getJobType());
        return rcRescueJob;
    }

    private RcActivity createActivity(OdDONew DO, Timestamp nowTimestamp, Integer action, boolean rescue, Integer rescueId) {
        RcActivity rcActivity = new RcActivity();
        rcActivity.setActivity_type(3);
        rcActivity.setUpdateby(0);
        rcActivity.setAct_time(nowTimestamp);
        rcActivity.setLastmile_reason(DO.getLastmile_reason());
        rcActivity.setLastmile_reason_detail(DO.getLastmile_reason_detail());
        if (action == 1 && rescue == false) {
            rcActivity.setRc_job_id(rescueId);
            rcActivity.setComment("System create rescue job");
            rcActivity.setNew_status(1);
            return rcActivity;
        } else if (action == 1 && rescue == true) {
            rcActivity.setRc_job_id(rescueId);
            rcActivity.setComment("System update lastmile reason from excel file");
            return rcActivity;
        } else if (action == 2) {
            rcActivity.setRc_job_id(rescueId);
            rcActivity.setComment("System close rescue job");
            rcActivity.setNew_status(7);
            return rcActivity;
        }
        return null;
    }

    private void setGetDOParamForCarrier(String carrier, GetDoNew params) {
        params.setOrgId(getCurOrgId());
        switch (carrier) {
            case "dhl":
                params.setCarrierId(EnumType.CARRIER_ID.DHL.getValue());
                break;
            case "alpha":
                params.setCarrierId(EnumType.CARRIER_ID.ALPHA.getValue());
                break;
            case "kerry":
                params.setFfmId(EnumType.FFM_ID.KERRY.getValue());
                break;
            case "sap":
                params.setFfmId(EnumType.FFM_ID.SAP.getValue());
                break;
            case "mycloud":
                params.setFfmId(EnumType.FFM_ID.MYCLOUD.getValue());
                break;
            default:
                return;
        }
        params.setStatus(EnumType.DO_STATUS_ID.NEW.getValue());
        if (params.getCreatedate() == null) {
            Date now = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH, -30);
            Date today_30 = cal.getTime();
            today_30.setHours(0);
            today_30.setMinutes(0);
            today_30.setSeconds(0);
            String today = DateHelper.toTMSFullDateFormat(now);
            String aMonthAgo = DateHelper.toTMSFullDateFormat(today_30);
            params.setCreatedate(aMonthAgo + "|" + today);
        }
    }

    private void setGetDOParamForCarrier(String carrier, GetDoNew params, GetDoNewV2 params2) {
        params.setOrgId(getCurOrgId());
        switch (carrier) {
            case "dhl":
//                params.setCarrierId(EnumType.CARRIER_ID.DHL.getValue());
                params2.setCarrierId(EnumType.CARRIER_ID.DHL.getValue());
                params2.setCarrierName(EnumType.CARRIER_ID.DHL.name());
                break;
            case "alpha":
//                params.setCarrierId(EnumType.CARRIER_ID.ALPHA.getValue());
                params2.setCarrierId(EnumType.CARRIER_ID.ALPHA.getValue());
                params2.setCarrierName(EnumType.CARRIER_ID.ALPHA.name());
                break;
            case "kerry":
//                params.setFfmId(EnumType.FFM_ID.KERRY.getValue());
                params2.setFfmId(EnumType.FFM_ID.KERRY.getValue());
                params2.setCarrierName("Ker");
                break;
            case "sap":
//                params.setFfmId(EnumType.FFM_ID.SAP.getValue());
                params2.setFfmId(EnumType.FFM_ID.SAP.getValue());
                params2.setCarrierName("SAP");
                break;
            case "mycloud":
//                params.setFfmId(EnumType.FFM_ID.MYCLOUD.getValue());
                params2.setFfmId(EnumType.FFM_ID.MYCLOUD.getValue());
                params2.setFfmName("MYC");
                break;
            default:
                return;
        }
//        params.setStatus(EnumType.DO_STATUS_ID.NEW.getValue());
        params2.setStatus(EnumType.DO_STATUS_ID.NEW.getValue());
        if (params.getCreatedate() == null) {
            Date now = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(now);
            cal.add(Calendar.DAY_OF_MONTH, -30);
            Date today_30 = cal.getTime();
            today_30.setHours(0);
            today_30.setMinutes(0);
            today_30.setSeconds(0);
            String today = DateHelper.toTMSFullDateFormat(now);
            String aMonthAgo = DateHelper.toTMSFullDateFormat(today_30);
//            params.setCreatedate(aMonthAgo + "|" + today);
            params2.setCreatedate(aMonthAgo + "|" + today);
        } else {
            params2.setCreatedate(params.getCreatedate());
        }
        logger.info("param: " + LogHelper.toJson(params2));
    }

    @PutMapping("/cancel-v2/{doId}")
    public TMSResponse<Boolean> cancelDOV2(@PathVariable Integer doId) {
        return doService.cancelDO(SESSION_ID, _curUser.getOrgId(), _curUser.getUserId(), doId);
    }
}
