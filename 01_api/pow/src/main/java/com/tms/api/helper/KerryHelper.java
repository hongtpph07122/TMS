package com.tms.api.helper;

import com.tms.api.dto.delivery.DeliveryDto;
import com.tms.api.dto.delivery.ProductDto;
import com.tms.api.entity.OrderResult;
import com.tms.ff.dto.request.kerry.KerryShipment;
import com.tms.ff.dto.request.kerry.KerryShipmentDto;
import com.tms.ff.dto.request.kerry.KerryShippingInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class KerryHelper {
    private static final Logger logger = LoggerFactory.getLogger(KerryHelper.class);

    public static KerryShippingInfoDto createShippingInfoPayload(String conNo, DeliveryDto deliveryDto, List<ProductDto> products) {
        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.KER_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);
        //1. Create DHL label
//        OrderResult dhlResult = CreateDHLDelivery(deliveryDto, products, EnumType.DHL_LABEL_TYPE.BASE64.getValue());
        KerryShippingInfoDto shippingInfoDto = new KerryShippingInfoDto();

        KerryShipmentDto req = new KerryShipmentDto();
        KerryShipment shipment = new KerryShipment();
        shipment.setCon_no(conNo);
        shipment.setAction_code("A");
        shipment.setS_name("Enforte");
        shipment.setS_address("11/10 ถนนพัฒนาชนบท 3");
        shipment.setS_mobile1("0918623446");
        shipment.setS_district("แขวงคลองสองต้นนุ่น");
        shipment.setS_subdistrict("เขตลาดกระบัง");
        shipment.setS_province("กรุงเทพมหานคร");

        //shipment.setS_zipcode(deliveryDto.getsZipCode());
        shipment.setS_zipcode("10520");
        shipment.setDeclare_value(0);


        shipment.setR_contact(deliveryDto.getReceiveName());
        shipment.setR_name(deliveryDto.getReceiveName());
        shipment.setR_address(deliveryDto.getAddress());
        shipment.setR_mobile1(deliveryDto.getPhone());
        shipment.setR_mobile2(deliveryDto.getPhone2());
        try {
            String[] phones = deliveryDto.getPhone().split("\\|");
            for (int i = 0; i < phones.length; i++) {
                switch (i) {
                    case 0:
                        shipment.setR_mobile1(phones[0]);
                        break;
                    case 1:
                        shipment.setR_mobile2(phones[1]);
                        break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        String[] payMethod = {"", "CASH", "CHEQUE"};

        if (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) {
            shipment.setCod_amount(deliveryDto.getCod_money().intValue());
            shipment.setCod_type(payMethod[1]);
        } else if (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.BANKING.getValue()) {
            shipment.setCod_amount(0);
            shipment.setCod_type(payMethod[0]);
        }

//        shipment.setSpecial_note(deliveryDto.getNote());
        shipment.setSpecial_note(deliveryDto.getPackageName());
        shipment.setService_code("ND");


        shipment.setRef_no(deliveryDto.getCustomerId());
        //TODO
        shipment.setR_zipcode(deliveryDto.getsZipCode());
//        shipment.setR_zipcode("13740");

        req.setShipment(shipment);
        shippingInfoDto.setReq(req);
        return shippingInfoDto;
    }
}
