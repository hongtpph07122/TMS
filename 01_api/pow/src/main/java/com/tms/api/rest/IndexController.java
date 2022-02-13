package com.tms.api.rest;

import com.tms.api.helper.EnumType;
import com.tms.api.task.DBLogWriter;
import com.tms.config.DBConfigMap;
import com.tms.dto.*;
import com.tms.entity.log.*;
import com.tms.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @Autowired
    CLActiveService clActiveService;

    @Autowired
    CLInActiveService clInActiveService;

    @Autowired
    CLFreshService clFreshService;

    @Autowired
    CLProductService clProductService;

    @Autowired
    LCProvinceService lcProvinceService;


    @Autowired
    CLCallbackService clCallbackService;

    @Autowired
    DeliveryOrderService deliveryOrderService;

    @Autowired
    UserService userService;

    @Autowired
    LogService logService;

    @Autowired
    DBConfigMap dbConfigMap;

    @Autowired
    DBLogWriter dbLog;

    @GetMapping("/aka")
    public String aka() {
        /*CLActiveStatusTimeParams pr = new CLActiveStatusTimeParams();
        pr.setLeadstatus(1);
        //pr.setAttempt(8);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 3);

        pr.setStarttime(AppUtils.getDate(cal.getTime()));

        cal.set(Calendar.HOUR_OF_DAY, 23);

        pr.setEndtime(AppUtils.getDate(cal.getTime()));
        pr.setNumber(2);

        clActiveService.getLeadCount("xxxx", 1);*/

        //clFreshService.getFreshLead("xxxx", new GetFreshLeadParams());
        //clFreshService.getFreshLeadByNumber("xxxx", 2);
        //clFreshService.getFreshLeadCount("xxxx", 2);
       /* GetCampaign param = new GetCampaign();
        param.setCpId(1);
        param.setLimit(10);
        clFreshService.getCampaign("xxxx", param);*/
       /* GetProductParams getProductParams = new GetProductParams();
        getProductParams.setOrgId(1);
        getProductParams.setLimit(20);
        clProductService.getProduct("xxxx", getProductParams);*/
        //clProductService.getProductByName("xxxx", "afe");
        //clProductService.getProductInStock("xxxx", new GetProductInStockParams());
        //clProductService.getProductList("xxxx", 1);
        //lcProvinceService.getProvince("xxxx", new GetProvince());
        //lcProvinceService.getProvinceList("xxxx");

        /*LogLead logLead = new LogLead();
        logLead.setComment("akkaka");
        logLead.setLeadId(1);
        logLead.setNewValue("inactive");
        logLead.setOnField("status");
        logLead.setUserId(12);
        logLead.setChangeTime(AppUtils.getDate(new Date()));

        logService.logLead("xxxx", logLead);*/

        /*LogCallback callback = new LogCallback();
        callback.setComment("akkaka");
        callback.setLeadId(1);
        callback.setNewValue("inactive");
        callback.setUpdateOnField("status");
        callback.setUserId(12);
        callback.setChangeTime(AppUtils.getDate(new Date()));

        logService.logCallback("xxxx", callback);*/
       /* UpdateCLCallback clCallback = new UpdateCLCallback();
        clCallback.setAddress("akak");
        clCallback.setLeadId(1);
        logService.updateCallback("xxxx", clCallback);
        */

        //logService.delCallback("xxxx", 1);
        //clCallbackService.getCallback("xxxx", new GetCLCallback());

        /*CLActiveLeadParams params = new CLActiveLeadParams();
        params.setLimit(5);
        clActiveService.getActiveLead("xxxx", params);*/
        //clCallbackService.getCallbackByTime("xxxx", new GetCallbackByTimeParams());
        /*CPCampaignParams pr = new CPCampaignParams();
        pr.setCapaignId(2);
        pr.setOrgId(1);
        clFreshService.getCampaignConfig("xxxx", pr);*/

        //clInActiveService.getInActiveLead("xxx", new CLInActiveParams());

        // clFreshService.getLead("xxx", new GetLeadParams());
        /*GetNewestLeadParams getNewestLeadParams = new GetNewestLeadParams();
        getNewestLeadParams.setLimit(0);
        getNewestLeadParams.setOtherName1("awefw");
        clFreshService.getNewestLead("xxx",new GetNewestLeadParams() );*/

        /*GetPrivByUserParams params = new GetPrivByUserParams();
        params.setUserId(101);
        params.setPrivId(1);
        params.setLimit(5);
        params.setOffset(0);
        userService.getPrivByUser("xxxx", params);*/

        /*GetUserParams getUserParams = new GetUserParams();
        getUserParams.setUserName("bhtech");
        userService.getUser("xxx", getUserParams);*/
        /*GetUserPassParams pr = new GetUserPassParams();
        pr.setUserName("admin");
        pr.setPassword("1234561");

        userService.getUserNamePass("xxx", pr);*/
        //deliveryOrderService.getDO("xxx", new GetDOParams());

       /* InsCLBasket basket = new InsCLBasket();
        basket.setAddress("akfwekfw");
        basket.setAgcId(1344);
        basket.setLeadId(1);
        logService.insBasket("xxxx", basket);*/

        /*InsDeliveryOrder ins = new InsDeliveryOrder();
        ins.setCustomerId(1);
        ins.setTrackingCode("22");
        ins.setDoId(1000);
        logService.insDeliveryOrder("xxxx", ins);*/

       /* InsPaymentOrder ins = new InsPaymentOrder();
        ins.setCreateby(1);
        ins.setSoId(33);
        ins.setPoId(1000);
        ins.setAmount(22d);
        logService.insPaymentOrder("xxxx", ins);*/

        /*InsSaleOrder ins = new InsSaleOrder();
        ins.setCreateby(1);
        ins.setOrgid(33);
        ins.setCpId(1000);
        ins.setAmount(22d);
        logService.insSaleOrder("xxxx", ins);*/

       /* InsSaleOrderItem ins = new InsSaleOrderItem();
        ins.setCreateby(1);
        ins.setOiId(1000);
        //ins.setCreateby(1000);
        ins.setAmount(22d);
        logService.insSaleItemOrder("xxxx", ins);*/

        /*LogPaymentOrder ins = new LogPaymentOrder();
        ins.setLogId(121);
        ins.setComment("akaka");
        //ins.setCreateby(1000);
        ins.setPoId(22);
        logService.logPaymentOrder("xxxx", ins);*/

        /*LogSaleOrder ins = new LogSaleOrder();
        ins.setLogId(121);
        ins.setComment("akaka");
        //ins.setCreateby(1000);
        ins.setSoId(22);
        logService.logSaleOrder("xxxx", ins);/*

        /*UpdDeliveryOrder ins = new UpdDeliveryOrder();
        //ins.setCreateby(121);
        ins.setDoId(1001);
        //ins.setCreateby(1000);
        ins.setTrackingCode("cccc");
        logService.updDeliveryOrder("xxxx", ins);*/

        /*UpdSaleOrder ins = new UpdSaleOrder();
        //ins.setCreateby(121);
        ins.setCpId(1001);
        //ins.setCreateby(1000);
        //ins.setTrackingCode("cccc");
        logService.updSaleOrder("xxxx", ins);*/

        /*UpdInActiveLead ins = new UpdInActiveLead();
        ins.setLeadId(121);
        ins.setCpId(1001);
        //ins.setCreateby(1000);
        //ins.setTrackingCode("cccc");
        logService.updInActiveLead("xxxx", ins);*/

       /* UpdLead ins = new UpdLead();
        ins.setLeadId(114520);
        ins.setCpId(1001);
        //ins.setCreateby(1000);
        //ins.setTrackingCode("cccc");
        logService.updLead("xxxx", ins);*/

        /*GetDistributionRuleParams params = new GetDistributionRuleParams();
        params.setDrId(11);
        lcProvinceService.getDistrictRule("xxxx", params);*/


        //lcProvinceService.getDistrict("xxxx", new GetDistrict());
        //lcProvinceService.getDistrictByPartner("xxxx", new GetDistrictByPartner());

        /*GetGroupParams params = new GetGroupParams();
        params.setGroupId(10);
        lcProvinceService.getGroup("xxxx", params);*/

        /*GetCallStrategy params = new GetCallStrategy();
        params.setCsId(10);
        lcProvinceService.getCallStrategy("xxxx", params);*/

        /*GetGroupAgent params = new GetGroupAgent();
        params.setGroupId(10);
        lcProvinceService.getGroupAgent("xxxx", params);*/

       /*InsCampaign campaign = new InsCampaign();
        campaign.setCpId(null);
        campaign.setOrgId(1);
        campaign.setName("yyyyyyyyyy");
        campaign.setOwner(1);
        campaign.setStatus(111);
        campaign.setCreateby(4);
        campaign.setModifyby(1);
        campaign.setCreatedate("20190202");
        campaign.setModifydate("20190202");
        campaign.setStartdate("");
        campaign.setStopdate("");
        logService.insCampaign("xxxx", campaign);*/

        /*InsCLFresh campaign = new InsCLFresh();
        campaign.setCpId(11);
        campaign.setLeadId(11);
        logService.insCLFresh("xxxx", campaign);*/

        /*InsCLActive campaign = new InsCLActive();
        campaign.setCpId(11);
        campaign.setLeadId(11);
        logService.insCLActive("xxxx", campaign);*/

        /*InsCLTrash campaign = new InsCLTrash();
        campaign.setCpId(11);
        campaign.setLeadId(11);
        logService.insCLTrash("xxxx", campaign);*/

        /*InsCLCallback campaign = new InsCLCallback();
        campaign.setCpId(11);
        campaign.setLeadId(11);
        logService.insCLCallback("xxxx", campaign);*/

        /*InsCPConfig campaign = new InsCPConfig();
        campaign.setCpId(11);
        campaign.setCpcfId(11);
        logService.insCPConfig("xxxx", campaign);*/

        /*UdpCPConfig campaign = new UdpCPConfig();
        campaign.setCpId(11);
        campaign.setCpcfId(11);
        logService.updCPConfig("xxxx", campaign);*/

        //clCallbackService.getCallback("xxxx", new GetCLCallback());

        //deliveryOrderService.getDO("xxxx", new GetDOParams());
        //deliveryOrderService.getSaleOrder("xxxx", new GetSOParams());
        //deliveryOrderService.getSaleOrderItem("xxxx", new GetSoItem());
        //deliveryOrderService.getSaleOrderByProduct("xxxx", new GetSOByProductParams());

        //lcProvinceService.getDistributionRule("xxx", new GetDistributionRuleParams());
        //lcProvinceService.getRuleParams("xxx", new GetRuleParams());
        //lcProvinceService.getStrategyParam("xxx", new GetStrategyParam());

      /*  GetProvinceBypartner provinceBypartnerResp = new GetProvinceBypartner();
        provinceBypartnerResp.setPartnerId(1);

        DBResponse<List<GetProvinceBypartnerResp>> dbResponse = lcProvinceService.getProvinceByPartner("xxx", provinceBypartnerResp);*/


        //logService.insSubdistrict("xxx", new InsSubdistrict());
        //logService.insDistrict("xxx", new InsDistrict());
        /*InsProvince insProvince = new InsProvince();
        insProvince.setPrvId(2002);
        logService.insProvince("xxx", insProvince);*/

       /* InsProvinceMap insProvinceMap = new InsProvinceMap();
        insProvinceMap.setPrmapId(221);
        insProvinceMap.setPrvId(22);

        logService.insProvinceMap("xxx",insProvinceMap);*/

        //lcProvinceService.getSubDistrict("xxxx", new GetSubdistrict());
        //lcProvinceService.getSubDistrictByPartner("xxxx", new GetSubDistrictByPartner());


        //logService.udpCampaign("xxx", new UpdCampaign());

        //logService.delCallback("xxx", 11);
        //logService.delMulCallback("xxx", "aa");

        /*InsProductAttribute insProductAttribute = new InsProductAttribute();
        insProductAttribute.setAttributeId(12);
        insProductAttribute.setValue("afae");
        logService.insProductAttribute("xxxx", insProductAttribute);*/

        //clFreshService.getCampaignAgent("xxxx", new GetCampaignAgent());
        //clProductService.getProductAttribute("xxxx", new GetProductAttribute());

        //lcProvinceService.getCpCallStrategy("xxx", new GetCpCallStrategy());
        //lcProvinceService.getCpDistributionRule("xxx", new GetCpDistributionRuleParams());

        //clProductService.getProductMapping("xxxx", new GetProductMapping());

        //clFreshService.getCallingList("xxxx", new GetCallingList());
        //clFreshService.getCpCallingList("xxxx", new GetCpCallingList());

        // lcProvinceService.getBasketLead("xxxx", new GetBasketLeadParams());

        //clProductService.getOrgPartner("xxxx", new GetOrganizationPartner());

        /*GetValidation getValidation = new GetValidation();
        getValidation.setOrgId(1);
        getValidation.setLimit(20);
        clFreshService.getValidation("xxx", getValidation);*/
        /*GetNewestLeadParams getNewestLeadParams = new GetNewestLeadParams();
        getNewestLeadParams.setLeadId(118634);
        clFreshService.getNewestLead("xxx", getNewestLeadParams);*/

        //deliveryOrderService.getDefaultDO("xxx", new GetDefaultDO());

        //clProductService.getStockByProductAndProvince("xx", new GetStockProvinceProduct());

       /* InsDoNew doNew = new InsDoNew();
        doNew.setCustomerDistrict("alo");
        doNew.setTrackingCode("EKIWI_XX21-2ABC1234");
        doNew.setWarehouseId(12);
        doNew.setCarrierId(4);
        doNew.setFfmId(4);
        doNew.setStatus(53);
        logService.insDoNew("xxxx", doNew);

        doNew = new InsDoNew();
        doNew.setCustomerDistrict("alo");
        doNew.setTrackingCode("5219075361486889");
        doNew.setWarehouseId(12);
        doNew.setCarrierId(4);
        doNew.setFfmId(4);
        doNew.setStatus(53);
        logService.insDoNew("xxxx", doNew);*/



       /* GetDoNew getDoNew = new GetDoNew();
        getDoNew.setTrackingCode("10345381626");
        DBResponse<List<GetDoNewResp>> doNew = deliveryOrderService.getDoNew("xxxx", getDoNew);

        UpdDoNew updDoNew = new UpdDoNew();
        updDoNew.setTrackingCode("10345381626");
        updDoNew.setDoId(doNew.getResult().get(0).getDoId());

        logService.updDoNew("xxx", updDoNew);*/

        /*GetStatusMapping mapping = new GetStatusMapping();
        deliveryOrderService.getStatusMapping("xxx", mapping);*/

      /* GetTrackingCode trackingCode = new GetTrackingCode();
        trackingCode.setOrgId(4);
        trackingCode.setCarrierId(4);
        trackingCode.setDoStatus("51,52,54,55,56,57,58,59");
        deliveryOrderService.getTrackingCode("xxx", trackingCode);*/

        InsLastmileStatus lastmileStatus = new InsLastmileStatus();
        lastmileStatus.setTrackingCode("FAX6H7NK");
        lastmileStatus.setCodAmount(118000D);
        lastmileStatus.setConsigneeAddress("HANOI vn");
        lastmileStatus.setConsigneeName("VN KKKKK");
        lastmileStatus.setConsigneePhone("0987654321");

        lastmileStatus.setCurrentWarehouse("Kho Giao nhan Giang Bien HN");
        lastmileStatus.setSubstatus("");
        lastmileStatus.setStatus("Giao thanh cong");
        lastmileStatus.setPackageDescription("118000 joincure");
        logService.insLastmileStatus("xxxxx", lastmileStatus);

        // logService.insDoPostback("", new InsDoPostback());
        //logService.updateCallbackByAssigned("", new UpdateCLCallbackByAssigned());
        //logService.updateCallback("", new UpdateCLCallback());
        return "test";
    }

    @GetMapping("/logasync")
    public String logasync() {
        dbLog.writeLeadStatusLog(1, 116217, EnumType.LEAD_STATUS.NEW.getValue(), "Create Lead From Basket");
        return "test";
    }
}
