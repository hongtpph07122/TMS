package com.tms.api.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.dto.delivery.DeliveryDto;
import com.tms.api.dto.delivery.ProductDto;
import com.tms.api.entity.OrderResult;
import com.tms.ff.dto.request.flash.FlashShippingInfoDto;
import com.tms.ff.dto.response.flash.FlashNotifyResponse;
import com.tms.ff.dto.response.flash.FlashShippingResponse;
import com.tms.ff.dto.response.flash.FlashWarehouseResponse;
import com.tms.ff.service.flash.FlashThShippingService;
import com.tms.ff.service.flash.impl.FlashThShippingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.List;

public class FlashHelper {
    static Logger logger = LoggerFactory.getLogger(FlashHelper.class);


    public static FlashShippingResponse createShippingInfo(String conNo, DeliveryDto deliveryDto, List<ProductDto> products, OrderResult orderResult) {
        FlashShippingResponse response;
        FlashShippingInfoDto shippingInfoDto = createShippingInfoPayload(conNo, deliveryDto, products);
        FlashThShippingService shippingService = new FlashThShippingServiceImpl();


        orderResult.setType(Const.FLASH_TH_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);
        logger.info("Create Flash TH. {}\r\n{} {}", conNo, deliveryDto.getSESSION_ID(), LogHelper.toJson(shippingInfoDto));
        response = shippingService.getShippingInfo(shippingInfoDto);
        logger.info(deliveryDto.getSESSION_ID() + " " + LogHelper.toJson(response));

        if (!ObjectUtils.isEmpty(response) && FlashThShippingService.CODE_SUCCESS.equals(response.getCode())) {
            orderResult.setResult(Const.DO_SUCCESS);
            orderResult.setCode("200");
            orderResult.setMessage(String.format("%s|%s", conNo, response.getData().getPno()));
            orderResult.setErrMessage(response.getMessage());

        } else {
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage(conNo);
            logger.info("Cancel MYC order!");
            orderResult.setCode(String.valueOf(response.getCode()));
            orderResult.setErrMessage(response.getMessage());
        }

        return response;
    }

    public static FlashShippingInfoDto createShippingInfoPayload(String conNo, DeliveryDto deliveryDto, List<ProductDto> products) {
        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.FLASH_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);
        FlashShippingInfoDto req = new FlashShippingInfoDto();
        double weight = 0d;

        for (ProductDto prd : products) {
            weight += ObjectUtils.isEmpty(prd.getWeight()) ? 0d : prd.getWeight();
        }

        req.setArticleCategory(FlashThShippingService.ARTICLE_CATEGORY_COMMODITY);

        if (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) {
            req.setCodEnabled(FlashThShippingService.COD_ENABLE);
            req.setCodAmount(deliveryDto.getCod_money() * 100); //cent
        } else {
            req.setCodEnabled(FlashThShippingService.COD_DISABLE);
            req.setCodAmount(0);
        }

        req.setDstName(deliveryDto.getReceiveName());
        req.setDstDetailAddress(deliveryDto.getAddress());
        req.setDstPostalCode(deliveryDto.getWardsCode());
        req.setDstPhone(deliveryDto.getPhone());
        req.setDstProvinceName(deliveryDto.getProvinceName());
        req.setDstCityName(deliveryDto.getDistrictName());
        req.setDstDistrictName(deliveryDto.getWardName());

        try {
            String[] phones = deliveryDto.getPhone().split("\\|");
            for (int i = 0; i < phones.length; i++) {
                switch (i) {
                    case 0:
                        req.setDstPhone(phones[0]);
                        break;
//                    case 1:
//                        shipment.setR_mobile2(phones[1]);
//                        break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


        req.setExpressCategory(FlashThShippingService.EXPRESS_CATEGORY_STANDARD);
        req.setInsureDeclareValue(0);
        req.setInsured(0);
        req.setNonceStr(conNo);
        req.setOutTradeNo(conNo);
        req.setRemark(deliveryDto.getPackageName());

//        req.setSrcName(deliveryDto.getPickupName());
//        req.setSrcDetailAddress(deliveryDto.getsAddress());
//        req.setSrcPhone(deliveryDto.getsPhone());
//        req.setSrcProvinceName(deliveryDto.getsProvinceName());
//        req.setSrcCityName(deliveryDto.getsDistrictName());
//        req.setSrcPostalCode(deliveryDto.getsWardCode());
        req.setSrcName("Enforte");
        req.setSrcDetailAddress("11/10 ถนนพัฒนาชนบท 3");
        req.setSrcPhone("0918623446");
        req.setSrcProvinceName("กรุงเทพ");
        req.setSrcCityName("ลาดกระบัง");
        req.setSrcDistrictName("คลองสองต้นนุ่น");
        req.setSrcPostalCode("10520");
        req.setWeight(new Double(weight).intValue());
        try {
            logger.info(new ObjectMapper().writeValueAsString(req));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }

        return req;
    }

    public static File getSmallLabel(String pno) {
        File rslt = null;

        try {
            FlashThShippingService shippingService = new FlashThShippingServiceImpl();
            rslt = shippingService.getSmallLabel(pno);
        } catch (Exception ex) {
            logger.error("FlashHelper.getSmallLabel Error {}", ex);
        }

        return rslt;
    }

    public static FlashNotifyResponse sendNotification(int size) {
        FlashNotifyResponse resp = null;
        try {
            FlashThShippingService srv = new FlashThShippingServiceImpl();
            FlashWarehouseResponse whs = srv.getAllWarehouses();
            FlashWarehouseResponse.Data wh = whs.getData().get(0);
            String remark = "Pickup at 16:00.";

            resp = srv.sendNotifyInfo(wh, size, remark);
        } catch (Exception ex) {
            logger.error("FlashHelper.sendNotification {}", ex);
        }

        return resp;
    }

    public static void cancel(String pno) {
        try {
            FlashThShippingService srv = new FlashThShippingServiceImpl();
            srv.cancel(pno);
        } catch (Exception ex) {
            logger.error("FlashHelper.sendNotification {}", ex);
        }
    }
}
