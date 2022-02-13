package com.tms.api.helper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.dto.delivery.DeliveryDto;
import com.tms.api.dto.delivery.ProductDto;
import com.tms.api.entity.OrderResult;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.task.NinjaVanGetToken;
import com.tms.dto.GetDoNewResp;
import com.tms.ff.dto.request.DHL.DHLLoginRequestDto;
import com.tms.ff.dto.request.DHL.Thailand.*;
import com.tms.ff.dto.request.DHL.shipment.*;
import com.tms.ff.dto.request.DHLFFM.DHLFFMCancelOrderRequestDto;
import com.tms.ff.dto.request.DHLFFM.DHLFFMRequestDto;
import com.tms.ff.dto.request.DHLFFM.DHLFFMRequestItemDto;
import com.tms.ff.dto.request.GHTK.GHTKRequestDto;
import com.tms.ff.dto.request.GHTK.shipment.GHTKOrder;
import com.tms.ff.dto.request.GHTK.shipment.GHTKProduct;
import com.tms.ff.dto.request.Haistar.HaistarRequestOrderDto;
import com.tms.ff.dto.request.Haistar.HaistarRequestOrderItemDto;
import com.tms.ff.dto.request.LoginDto;
import com.tms.ff.dto.request.NTL.*;
import com.tms.ff.dto.request.NinjaVan.*;
import com.tms.ff.dto.request.Snappy.SnappyCreateOrderRequest;
import com.tms.ff.dto.request.Snappy.SnappyItemOrderRequest;
import com.tms.ff.dto.request.WFS.*;
import com.tms.ff.dto.request.boxme.*;
import com.tms.ff.dto.request.ghnexpress.GHNExpressRequestDto;
import com.tms.ff.dto.request.kerry.KerryShipment;
import com.tms.ff.dto.request.kerry.KerryShipmentDto;
import com.tms.ff.dto.request.kerry.KerryShippingInfoDto;
import com.tms.ff.dto.request.mycloud.MyCloudCreateOrderDto;
import com.tms.ff.dto.request.mycloud.MyCloudCreateOrderItemDto;
import com.tms.ff.dto.request.sap.CancelOrderRequestDto;
import com.tms.ff.dto.request.sap.SapCreateOrderRequestDto;
import com.tms.ff.dto.request.ship60.DeliveryAddress;
import com.tms.ff.dto.request.ship60.Orders;
import com.tms.ff.dto.request.ship60.Ship60CreateOrderRequest;
import com.tms.ff.dto.request.ship60.Shop;
import com.tms.ff.dto.request.viettel.VtCreateOrderRequestDto;
import com.tms.ff.dto.request.viettel.VtProduct;
import com.tms.ff.dto.request.viettel.VtUpdateOrderDto;
import com.tms.ff.dto.response.DHL.DHLCreateOrderResponse;
import com.tms.ff.dto.response.DHL.DHLDeleteLabelResponse;
import com.tms.ff.dto.response.DHL.DHLLoginResponse;
import com.tms.ff.dto.response.DHLFFM.DHLFFMOrderResponse;
import com.tms.ff.dto.response.GHTK.GHTKResponseDto;
import com.tms.ff.dto.response.Haistar.HaistarOrderResponseDto;
import com.tms.ff.dto.response.NTL.NTLOrderResponseDto;
import com.tms.ff.dto.response.NTL.NTLProductsResponseDto;
import com.tms.ff.dto.response.NinjaVan.NinjaVanResponseCancerDto;
import com.tms.ff.dto.response.NinjaVan.NinjaVanResponseDto;
import com.tms.ff.dto.response.Snappy.SnappyCancelOrderResponse;
import com.tms.ff.dto.response.Snappy.SnappyCreateOrderResponse;
import com.tms.ff.dto.response.Snappy.SnappyLoginResponse;
import com.tms.ff.dto.response.Snappy.SubEntityResponse.SnappyAddressResponse;
import com.tms.ff.dto.response.Snappy.SubEntityResponse.SnappyBusinessesResponse;
import com.tms.ff.dto.response.WFS.*;
import com.tms.ff.dto.response.boxme.BoxmeCancelOrderResponse;
import com.tms.ff.dto.response.boxme.BoxmeCreateOrderResponse;
import com.tms.ff.dto.response.boxme.BoxmeProductsResponse;
import com.tms.ff.dto.response.boxme.BoxmeUpdateLabelResponse;
import com.tms.ff.dto.response.flash.FlashShippingResponse;
import com.tms.ff.dto.response.ghnexpress.GHNExpressResponseDto;
import com.tms.ff.dto.response.kerry.KerryShippingResponse;
import com.tms.ff.dto.response.mycloud.MyCloudCancelOrderResponse;
import com.tms.ff.dto.response.mycloud.MyCloudCreateOrderResponse;
import com.tms.ff.dto.response.sap.SapCancelOrderResponse;
import com.tms.ff.dto.response.sap.SapCreateOrderResponse;
import com.tms.ff.dto.response.ship60.Ship60ResponseMap;
import com.tms.ff.dto.response.viettel.VtCreateOrderResponse;
import com.tms.ff.dto.response.viettel.VtUpdateOrderResponse;
import com.tms.ff.service.DHL.DHLAuthenticationServiceImpl;
import com.tms.ff.service.DHL.DHLOrderServiceImpl;
import com.tms.ff.service.GHTK.impl.GHTKOrderServiceImpl;
import com.tms.ff.service.Haistar.Impl.HaistarOrderServiceImpl;
import com.tms.ff.service.NTL.impl.NTLOrderServiceImpl;
import com.tms.ff.service.NinjaVan.impl.NinjaVanOrderServiceImpl;
import com.tms.ff.service.Snappy.Impl.SnappyOrderServiceImpl;
import com.tms.ff.service.WFS.WfsAuthenticationServiceImpl;
import com.tms.ff.service.WFS.WfsOrderServiceImpl;
import com.tms.ff.service.boxme.BoxmeOrderServiceImpl;
import com.tms.ff.service.boxme.BoxmeProductServiceImpl;
import com.tms.ff.service.boxme.BoxmeUpdateLabelServiceImpl;
import com.tms.ff.service.ghnexpress.GHNExpressServiceImpl;
import com.tms.ff.service.impl.DHLFFM.DHLFFMOrderServiceImpl;
import com.tms.ff.service.kerry.KerryShippingServiceImpl;
import com.tms.ff.service.kerry.KerryThShippingServiceImpl;
import com.tms.ff.service.mycloud.MyCloudFfmServiceImpl;
import com.tms.ff.service.sap.SapOrderServiceImpl;
import com.tms.ff.service.ship60.ShipOrderServiceImpl;
import com.tms.ff.service.viettel.VtOrderServiceImpl;
import com.tms.ff.utils.AppProperties;
import com.tms.ff.utils.DHL.DHLConst;
import com.tms.ff.utils.DHL.DHLHelper;
import com.tms.ff.utils.Haistar.HaistarConst;
import com.tms.ff.utils.WFS.WfsConst;
import com.tms.ff.utils.boxme.BoxmeConst;
import com.tms.ff.utils.mycloud.MyCloudConst.DELIVERY_MODE;
import com.tms.ff.utils.mycloud.MyCloudConst.ORDER_STATUS;
import org.joda.time.LocalDateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by dinhanhthai on 27/07/2019.
 */
public class DeliveryHelper {

    private static final String SHOP_NAME_DEFAULT = "EKIWI";
    static Logger logger = LoggerFactory.getLogger(DeliveryHelper.class);
    private static Map<Integer, String> DHL_TOKEN_MAPS = new HashMap<>();
    private static Map<Integer, Date> DHL_EXPIRE_AT_MAPS = new HashMap<>();//expireAt;
    private static String NinjaVan_TOKEN = null;

    /**
     * Generate EKW DO code
     *
     * @param doId
     * @return
     */
    public static String createEKWDoCode(int doId) {
        String prefix = "EKW";
        String formatCode = "%s%s%s";
        return String.format(formatCode, prefix, returnDateNowformat("yyyyMMdd"), createDoformat(doId));
    }

    /**
     * Generate EKW DO code for INDO
     *
     * @param doId
     * @return
     */
    public static String createIKWDoCode(int doId) {
        String prefix = "IK";
        String formatCode = "%s%s%s";
        return String.format(formatCode, prefix, returnDateNowformat("yyMM"), createIndoDOformat(doId));
    }

    /**
     * For create new DO
     *
     * @param parterId
     * @param deliveryDto
     * @param lst
     * @return
     */
    public static List<OrderResult> createDelivery(int parterId, DeliveryDto deliveryDto, List<ProductDto> lst) {
        List<OrderResult> lstOrder = new ArrayList<>();

        //send to 3pl
        switch (parterId) {
            case Const.WFS_PARTNER_ID:
                //GHN Logistic
//                lstOrder = DeliveryHelper.CreateGHNWarehouseOrderNew(deliveryDto, lst);
                lstOrder = DeliveryHelper.CreateGHNWarehouseOrderV2(deliveryDto, lst);
                break;
            case Const.NinjaVan_PARTNER_ID:
            	OrderResult  orderResult = DeliveryHelper.CreateNinjaVanOrder(deliveryDto, lst);
            	lstOrder.add(orderResult);
                break;
            case 3:
                break;
            case Const.DHL_PARTNER_ID:
                //DHL
                OrderResult result = DeliveryHelper.CreateDHLDelivery(deliveryDto, lst);
                lstOrder.add(result);
                break;
            case Const.DHL_FFM_PARTNER_ID:
                //DHL
                List<OrderResult> resultDHLFFM = DeliveryHelper.CreateDHLFFMDelivery(deliveryDto, lst);
                lstOrder.addAll(resultDHLFFM);
                break;
            case Const.VT_PARTNER_ID:
                //Viettel
                OrderResult resultVT = DeliveryHelper.CreateVTDelivery(deliveryDto, lst);
                lstOrder.add(resultVT);
                break;
            case Const.BM_PARTNER_ID://boxme vn
                List<OrderResult> resultBx = DeliveryHelper.CreateBoxmeDelivery(deliveryDto, lst);
                lstOrder.addAll(resultBx);
                break;
            case Const.BM_INDO_PARTNER_ID:
                //boxme indo
                List<OrderResult> resultBxIndo = DeliveryHelper.CreateBoxmeIndoDelivery(deliveryDto, lst);
                lstOrder.addAll(resultBxIndo);
                break;
            case Const.BM_TH_PARTNER_ID:
                //boxme thailand
                List<OrderResult> rsBmTh = DeliveryHelper.CreateBoxmeThailandDelivery(deliveryDto, lst);
                lstOrder.addAll(rsBmTh);
                break;
            case Const.KERRY_LM_PARTNER_ID://kerry
            case Const.KERRY_FFM_PARTNER_ID://kerry FFM
                List<OrderResult> resultKerry = DeliveryHelper.CreateKerryDelivery(deliveryDto, lst);
                lstOrder.addAll(resultKerry);
                break;
            case Const.SAP_LM_PARTNER_ID://sap
            case Const.SAP_FFM_PARTNER_ID://SAP FFM
                List<OrderResult> resultSAP = DeliveryHelper.CreateSAPDelivery(deliveryDto, lst);
                lstOrder.addAll(resultSAP);
                break;
            case Const.MYCLOUD_FFM_PARTNER_ID:
                //MyCloud
                List<OrderResult> resultMyCloud = DeliveryHelper.CreateMyCloudDelivery(deliveryDto, lst);
                lstOrder.addAll(resultMyCloud);
                break;
            case Const.SICEPAT_PARTNER_ID:
            case Const.HAISTAR_PARTNER_ID:
                List<OrderResult> resultHaistar = DeliveryHelper.CreateHaistarDelivery(deliveryDto, lst);
                lstOrder.addAll(resultHaistar);
                break;
            case Const.NTL_FFM_PARTNER_ID:
                List<OrderResult> resultNTL = DeliveryHelper.createNTLDelivery(deliveryDto, lst);
                lstOrder.addAll(resultNTL);
                break;

            default:
                lstOrder = null;
                break;
        }

        return lstOrder;
    }


    private static List<OrderResult> CreateHaistarDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
    	OrderResult item = null;
        List<OrderResult> lst = new ArrayList<>();
        HaistarOrderServiceImpl service = new HaistarOrderServiceImpl();

        HaistarRequestOrderDto dto = new HaistarRequestOrderDto();
        if(deliveryDto.getLastmileId() == Const.SICEPAT_PARTNER_ID){
        	dto.setCourier_name(HaistarConst.SiCepat3pl);
		}
		/*
		 * else if(deliveryDto.getLastmileId() == Const.SAP_LM_PARTNER_ID){
		 * dto.setCourier_name(HaistarConst.SAP3pl); } else
		 * if(deliveryDto.getLastmileId() == Const.KERRY_LM_PARTNER_ID){
		 * dto.setCourier_name(HaistarConst.Kerry3pl); }
		 */

        String trackingCode = createIKWDoCode(deliveryDto.getDoId());
        dto.setCode(trackingCode);
        dto.setRecipient_postal_code(deliveryDto.getsZipCode());
        dto.setRecipient_name(deliveryDto.getReceiveName());
        dto.setRecipient_phone(deliveryDto.getPhone());
        dto.setRecipient_address(deliveryDto.getAddress());
        dto.setTotal_price(deliveryDto.getAmount().doubleValue());
        dto.setChannel_id("EKIWI");
        dto.setRecipient_country("Indonesia");
        dto.setRecipient_province(deliveryDto.getProvinceName());
        dto.setRecipient_city(deliveryDto.getDistrictName());
        dto.setRecipient_district(deliveryDto.getWardName());
        if(deliveryDto.getCod_money() != null && deliveryDto.getCod_money() > 0){
            dto.setCod_price(deliveryDto.getCod_money().doubleValue());
            dto.setPayment_type(HaistarConst.PAYMENT_TYPE.COD.getType());
		}
        else {
            dto.setCod_price(0.0);
            dto.setPayment_type(HaistarConst.PAYMENT_TYPE.NON_COD.getType());
        }
        dto.setDfod_price(0.0);
        dto.setRecipient_province(deliveryDto.getProvinceName());
        dto.setRecipient_city(deliveryDto.getDistrictName());
        dto.setRecipient_district(deliveryDto.getWardName());
        dto.setShop_name(SHOP_NAME_DEFAULT);
        List<HaistarRequestOrderItemDto> items = new ArrayList<HaistarRequestOrderItemDto>();
        if(products != null && products.size() > 0){
			for(ProductDto product: products){
				HaistarRequestOrderItemDto oi = new HaistarRequestOrderItemDto();
				oi.setItem_code(product.getProPartnerCode());
				oi.setQuantity(product.getQty());
				oi.setUnit_price(product.getPrice());
				items.add(oi);
			}
		}

        dto.setItems(items);

        logger.info("==================HaistarRequestOrderDto==================\r\nHaistarRequestOrderDto: {}", LogHelper.toJson(dto));
		HaistarOrderResponseDto createOrderResponse = service.createOrder(dto);
        logger.info("==================HaistarOrderResponseDto ==================\r\ncreateOrderResponse: {}", LogHelper.toJson(createOrderResponse));

        item = new OrderResult();
        item.setType(String.valueOf(Const.HAISTAR_PARTNER_ID));

        if (createOrderResponse != null && createOrderResponse.getStatus() != null
        		&& StringUtils.hasText(createOrderResponse.getStatus()) && createOrderResponse.getStatus().equals("200")) {
            item.setResult(Const.DO_SUCCESS);
            //Haistar su dung tracking_code minh tao lam code cua order
            item.setMessage(trackingCode);
        } else {
            item.setResult(Const.DO_ERROR);
            item.setErrMessage(String.format("ERROR when create Haistar order| %s",
                    LogHelper.toJson(createOrderResponse.getData())));
        }

        lst.add(item);
        /* } */
        return lst;
	}

	/**
     * create order for DHL FFM VN, just create order in warehouse
     *
     * @param deliveryDto
     * @param products
     * @return
     */
    private static List<OrderResult> CreateDHLFFMDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        boolean isNew = (deliveryDto.getFfmCode() != null && StringUtils.hasText(deliveryDto.getFfmCode())) ? false
                : true;
        List<OrderResult> lst = new ArrayList<>();
        DHLFFMOrderServiceImpl service = new DHLFFMOrderServiceImpl();
        ArrayList<String> extraServices = new ArrayList<String>() {
            {
                add("OBOX");
            }
        };

        if (deliveryDto.getPaymentMethod().intValue() == EnumType.PAYMENT_METHOD.COD.getValue()){
            extraServices.add("COD");
        }

        DHLFFMRequestDto dto = new DHLFFMRequestDto();

        dto.setSaleOrderCode(deliveryDto.getDoCode());
        dto.setWarehouseCode(deliveryDto.getGroupAddressId());
        //hiện tại hệ thống không có customer_code nên truyền name, phone, district_code
        dto.setCustomerName(deliveryDto.getReceiveName());
        dto.setCustomerPhone(deliveryDto.getPhone());
        dto.setShippingDistrictCode(deliveryDto.getDistrictCode());
        dto.setShippingWardCode(deliveryDto.getWardsCode());
        dto.setShippingAddressNo(deliveryDto.getAddress());
        dto.setCod(Double.valueOf(deliveryDto.getCod_money()));
        dto.setPackingNote(deliveryDto.getNote());
        dto.setShippingNote(deliveryDto.getNote());
        dto.setTotalAmount(Double.valueOf(deliveryDto.getAmount()));
        if (deliveryDto.getLastmileId().equals(Const.DHL_PARTNER_ID)) {
            dto.setTplCode(Const.DHL_PARTNER_CODE);
            dto.setExtraServices(extraServices);
        }

        if (deliveryDto.getLastmileId().equals(Const.GHN_PARTNER_ID)) {
            dto.setTplCode(Const.GHN_EXPRESS_CODE);
            OrderResult createOrderGHNExpress = createOrderGHNExpress(deliveryDto, products);
            lst.add(createOrderGHNExpress);
        }

        List<DHLFFMRequestItemDto> items = new ArrayList<DHLFFMRequestItemDto>();
        if (products != null && products.size() > 0) {
            for (ProductDto product : products) {
                DHLFFMRequestItemDto item = new DHLFFMRequestItemDto();
                item.setSku(product.getProPartnerCode());
                item.setOrderQty(Double.valueOf(product.getQty()));
                item.setTotalAmount(product.getPrice());

                items.add(item);
            }
        }
        dto.setItems(items);

        ObjectMapper mapper = new ObjectMapper();
        logger.info("==================DHLFFMRequestDto==================\r\nDHLFFMRequestDto: {}", LogHelper.toJson(dto));

        DHLFFMOrderResponse createOrderResponse = null;
        if (isNew) {
            createOrderResponse = service.createOrder(dto);
            logger.info("==================CreateDHL_FFMDelivery==================\r\ncreateOrderResponse: {}", LogHelper.toJson(createOrderResponse));
        } else {
            createOrderResponse = service.updateOrder(dto);
            logger.info("==================UpdateDHL_FFMDelivery==================\r\nupdateOrderResponse: {}", LogHelper.toJson(createOrderResponse));
        }

        /* if(deliveryDto.getLastmileId().equals(Const.DHL_PARTNER_ID)){ */
        OrderResult item = new OrderResult();
        item.setType(String.valueOf(Const.DHL_FFM_PARTNER_ID));

        if (createOrderResponse != null && createOrderResponse.isSuccess()) {
            item.setResult(Const.DO_SUCCESS);
            if (isNew) {
                item.setMessage(createOrderResponse.getResult().getCode());
            }
        } else {
            item.setResult(Const.DO_ERROR);
            item.setErrMessage(String.format("ERROR when create DHL FFM order| %s",
                    LogHelper.toJson(createOrderResponse.getError())));
        }

        lst.add(item);
        /* } */
        return lst;
    }

    /**
     * for Cancel DO
     *
     * @param carrierId
     * @param doNew
     * @param orgId
     * @return
     * @throws TMSException
     */
    public static OrderResult cancelDelivery(int carrierId, GetDoNewResp doNew, Integer orgId) throws TMSException {
        OrderResult orderResult = null;
        switch (carrierId) {
            case Const.VT_PARTNER_ID:
                String trackingId = doNew.getTrackingCode();
                orderResult = DeliveryHelper.CancelVTDelivery(trackingId);
                break;
            case Const.DHL_PARTNER_ID:
                //nếu là hủy đơn của dhl ffm thì gọi api của dhl ffm
                if (doNew.getFfmId() != null && doNew.getFfmId().equals(Const.DHL_FFM_PARTNER_ID)) {
                    String orderCode = doNew.getFfmCode();
                    orderResult = DeliveryHelper.CancelDHLFFMDelivery(doNew);
                } else {
                    String shipmentId = doNew.getDoCode();
                    orderResult = DeliveryHelper.CancelDHLDelivery(shipmentId, orgId);
                }
                break;
            case Const.WFS_PARTNER_ID:
                String saleOrderId = doNew.getFfmCode();
                orderResult = DeliveryHelper.CancelWFSDelivery((saleOrderId));
                break;
            case Const.BM_PARTNER_ID:
                String bmCode = doNew.getFfmCode();
                orderResult = DeliveryHelper.CancelBoxmeDelivery((bmCode));
                break;
            case Const.MYCLOUD_FFM_PARTNER_ID:
                String myCode = doNew.getFfmCode();
                orderResult = DeliveryHelper.CancelMyCloudDelivery(myCode);
                break;
            case Const.GHN_PARTNER_ID:
            case Const.DHL_FFM_PARTNER_ID:
            default:
                throw new TMSException(ErrorMessage.CARRIER_NOT_FOUND);
        }
        return orderResult;
    }

    public static OrderResult cancelDelivery(int partnerId, String code, Integer orgId) throws TMSException {
        OrderResult orderResult;
        switch (partnerId) {
            /* CANCEL FULFILLMENT */
            case Const.BM_PARTNER_ID:
                orderResult = DeliveryHelper.CancelBoxmeDelivery(code);
                break;
            case Const.DHL_FFM_PARTNER_ID:
                orderResult = DeliveryHelper.cancelDHLFFMDelivery(code);
                break;

            /* CANCEL LASTMILE */
            case Const.DHL_PARTNER_ID:
                orderResult = DeliveryHelper.CancelDHLDelivery(code, orgId);
                break;
            case Const.SNAPPY_PARTNER_ID:
                orderResult = DeliveryHelper.CancelSnappy(code);
                break;
            case Const.SAP_LM_PARTNER_ID:
                orderResult = DeliveryHelper.CancelSAPDelivery(code);
                break;

            default:
                throw new TMSException(ErrorMessage.THREE_PLS_NOT_FOUND);
        }
        return orderResult;
    }

    /* Some 3pls need warehouseCode to cancel */
    public static OrderResult cancelDelivery(int partnerId, String code, String warehouseCodeInPartner, Integer orgId) throws TMSException {
        if (partnerId == Const.WFS_PARTNER_ID) {
            return DeliveryHelper.cancelWFSDelivery(code, warehouseCodeInPartner);
        } else {
            throw new TMSException(ErrorMessage.THREE_PLS_NOT_FOUND);
        }
    }

    private static OrderResult CancelDHLFFMDelivery(GetDoNewResp doNew) {
        DHLFFMOrderServiceImpl service = new DHLFFMOrderServiceImpl();
        // TODO Auto-generated method stub
        DHLFFMCancelOrderRequestDto dto = new DHLFFMCancelOrderRequestDto();
        dto.setSaleOrderCode(doNew.getFfmCode());
        dto.setNote("");
        DHLFFMOrderResponse cancelOrderResponse = (DHLFFMOrderResponse) service.cancelOrder(dto);
        logger.info("CANCEL DHLFFM " + doNew.getFfmCode() + " " + LogHelper.toJson(cancelOrderResponse));
        OrderResult orderResult = new OrderResult();
        orderResult.setType(String.valueOf(Const.DHL_FFM_PARTNER_ID));

        if (cancelOrderResponse != null && cancelOrderResponse.isSuccess() && cancelOrderResponse.getResult() != null && cancelOrderResponse.getResult().getIsUpdated()) {
            orderResult.setResult(Const.DO_SUCCESS);
        } else {
            orderResult.setResult(Const.DO_ERROR);
            String error = "";
            if (cancelOrderResponse != null && cancelOrderResponse.getError() != null) {
                error += LogHelper.toJson(cancelOrderResponse.getError());
            }
            orderResult.setMessage("Cannot cancel order=" + doNew.getFfmCode() + ": " + error);
        }
        return orderResult;
    }

    /**
     * check DO result status (for cancel DO)
     *
     * @param orderResult
     * @return
     */
    public static boolean isSuccessCode(OrderResult orderResult) {
        return (orderResult.getCode().equals("200") || orderResult.getCode().equals("0"));
    }

    /**
     * check product in inventory of FFM
     *
     * @param partnerId
     * @param productDto
     * @param warehouseId
     * @return
     */
    public static boolean hasProductInFullfillment(int partnerId, ProductDto productDto, String warehouseId) {
        boolean hasProduct = true;
        switch (partnerId) {
            case 1://kho GHN
//                hasProduct = checkProductInWFS(productDto.getProPartnerCode(), warehouseId, productDto.getQty());
                break;
            case 8://kho Boxme
                hasProduct = checkProductInBoxme(productDto.getProPartnerCode(), productDto.getQty());
                break;
            case 55: //boxme TH
                hasProduct = checkProductInBoxme(productDto.getProPartnerCode(), productDto.getQty());
                break;
            case 161 :
                hasProduct = checkProductInNTX(productDto.getProPartnerCode(), productDto.getQty());
                break;
        }
        return hasProduct;
    }

    /* comment not use
    private static boolean checkProductInWFS(String gCode, String warehouseId, int prodQty) {
        WfsProductServiceImpl productService = new WfsProductServiceImpl();
        WfsProductQtyRequestDto wfsProductRequestDto = new WfsProductQtyRequestDto();
        wfsProductRequestDto.setLocationId(warehouseId);
        wfsProductRequestDto.setGCode(gCode);
//        wfsProductRequestDto.setsCode("8938526491113");
        WfsProductQtyResponse response = productService.ProductQtyDetail(wfsProductRequestDto);
        if (response != null && response.getData() != null) {
            return (response.getData().getAvailableQuantity() - prodQty) > 0;
//                return true;

        }
        return false;
    }*/

    private static boolean checkProductInBoxme(String prodIdCode, int prodQty) {
        BoxmeProductServiceImpl productService = new BoxmeProductServiceImpl();
        BMProductDto productDto = new BMProductDto();
        productDto.setSku(prodIdCode);
        logger.info("SKU: " + prodIdCode);
        BoxmeProductsResponse response = (BoxmeProductsResponse) productService.getProduct(productDto);
        logger.info("Response check SKU: " + LogHelper.toJson(response));
        if (response != null && response.getData() != null && response.getData().getResults() != null && response.getData().getResults().size() > 0) {
            if (response.getData().getResults().get(0).getStatistic() != null && response.getData().getResults().get(0).getStatistic().size() > 0) {
                int quanInStock = response.getData().getResults().get(0).getStatistic().get(0).getQuantity_in_stock();
                int waitOutbound = response.getData().getResults().get(0).getStatistic().get(0).getWaiting_outbound();
                return ((quanInStock - waitOutbound - prodQty) > 0);
            }
        }
        return false;
    }

    private static boolean checkProductInNTX(String prodIdCode, int prodQty) {
        NTLOrderServiceImpl productService = new NTLOrderServiceImpl();
        NTLGetProductByCodeDto productDto = new NTLGetProductByCodeDto();
        productDto.setCode(prodIdCode);

        logger.info("CODE: " + prodIdCode);
        NTLProductsResponseDto response = productService.getProduct(productDto);
        logger.info("Response check inventory: " + LogHelper.toJson(response));

        if (response != null && response.getData() != null && response.getData().getItemList() != null && response.getData().getItemList().size() > 0) {
            int quanInStock = 0;
            for (int i = 0; i < response.getData().getItemList().size(); i++) {
                quanInStock += response.getData().getItemList().get(i).getQuantity().intValue();
            }
                return ((quanInStock - prodQty) > 0);
        }
        return false;
    }

    private static String getDHLToken(Integer orgId) {
        DHLAuthenticationServiceImpl service = new DHLAuthenticationServiceImpl();
        DHLLoginRequestDto loginRequestDto = new DHLLoginRequestDto();
        loginRequestDto.setClientId(AppProperties.getPropertyValue(Helper.getKeyConfigByOrg(orgId, "api.DHL.username")));
        loginRequestDto.setPassword(AppProperties.getPropertyValue(Helper.getKeyConfigByOrg(orgId, "api.DHL.password")));

        DHLLoginResponse loginResponse = (DHLLoginResponse) service.login(DHLConst.hostName, null, DHLConst.LOGIN_PATH, loginRequestDto);
        if (loginResponse.getAccessTokenResponse() != null) {
            DHL_TOKEN_MAPS.put(orgId, loginResponse.getAccessTokenResponse().getToken());
            Integer expire = Integer.valueOf(String.valueOf(loginResponse.getAccessTokenResponse().getExpires_in_seconds()));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.SECOND, expire);

            DHL_EXPIRE_AT_MAPS.put(orgId, calendar.getTime());
            return loginResponse.getAccessTokenResponse().getToken();
        }
        return null;

    }
    private static String getNinjaVanToken() {

    	NinjaVanGetToken nj = new NinjaVanGetToken() ;
    	NinjaVan_TOKEN = nj.NinjaVan_TOKEN;
		return nj.getTokenToCreateOrder();

    }

    private static List<OrderResult> CreateGHNWarehouseOrderNew(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lstOrder = new ArrayList<>();
        OrderResult orderResult = new OrderResult();

        WfsOrderServiceImpl orderService = new WfsOrderServiceImpl();
        WfsAuthenticationServiceImpl service = new WfsAuthenticationServiceImpl();
        LoginDto loginRequestDto = new WfsLoginRequestDto();
        WfsLoginResponse l = (WfsLoginResponse) service.login(WfsConst.hostName, null, WfsConst.LOGIN_PATH, loginRequestDto);

        logger.info("Message: {}\r\nToken: {}", l.getMessage(), l.getData().getToken());

        WfsCreateOrderRequestDto wfsCreateOrderRequestDto = new WfsCreateOrderRequestDto();
        wfsCreateOrderRequestDto.setToken(l.getData().getToken());
        wfsCreateOrderRequestDto.setCustomerAddress(deliveryDto.getAddress());
        wfsCreateOrderRequestDto.setCustomerDistrict(deliveryDto.getDistrictId());
        wfsCreateOrderRequestDto.setCustomerProvince(deliveryDto.getProvinceId());
//        wfsCreateOrderRequestDto.setCustomerWard(deliveryDto.getWardsId());
        wfsCreateOrderRequestDto.setCustomerName(deliveryDto.getReceiveName());
        wfsCreateOrderRequestDto.setCustomerPhone(deliveryDto.getPhone());
//        wfsCreateOrderRequestDto.setNoteCode(deliveryDto.getNote());
        Integer codValue = 0;
        if (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue())
            codValue = deliveryDto.getCod_money();

        wfsCreateOrderRequestDto.setCoDAmount(codValue);
        wfsCreateOrderRequestDto.setNote(String.valueOf(codValue));
        //TODO need to change
        wfsCreateOrderRequestDto.setOrigin(deliveryDto.getDoCode());

        wfsCreateOrderRequestDto.setLocation(deliveryDto.getWarehouseId());//39
        //van chuyen noi tinh doi voi GHN Express thi dung CHUAN, field only used by GHN Express by GHN Logistic
        //wfsCreateOrderRequestDto.setServiceID(deliveryDto.isLocalProvince() ? Const.WFS_GHN_EXPRESS_SERVICE_CHUAN : Const.WFS_GHN_EXPRESS_SERVICE_NHANH);
        //Only require by GHN Express
        wfsCreateOrderRequestDto.setServiceID(Const.WFS_GHN_EXPRESS_SERVICE_CHUAN);
        wfsCreateOrderRequestDto.setPaymentTypeID(Const.WFS_GHN_EXPRESS_PAYMENT_TYPE_ID);
        wfsCreateOrderRequestDto.setNoteCode(Const.WFS_GHN_EXPRESS_NOTE_CODE);
        wfsCreateOrderRequestDto.setInsuranceFee(0);
        String transporter = (deliveryDto.getLastmileId() == Const.DHL_PARTNER_ID ? Const.WFS_DHL_PARTNER_CODE : Const.WFS_GHN_PARTNER_CODE);
        wfsCreateOrderRequestDto.setTransporter(transporter);
        wfsCreateOrderRequestDto.setServicePackage("chuan");

        //TODO need to be clear --- SOLUTION???/
        wfsCreateOrderRequestDto.setHeight(10);
        wfsCreateOrderRequestDto.setLength(10);
        wfsCreateOrderRequestDto.setWeight(100);
        wfsCreateOrderRequestDto.setWidth(10);
        List<WfsCreateOrderRequestDto.ProductOrder> lst = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ProductDto pd = products.get(i);
            WfsCreateOrderRequestDto.ProductOrder pO = wfsCreateOrderRequestDto.new ProductOrder();
            int proPartnerId = Integer.parseInt(pd.getProCode());
            pO.setId(proPartnerId);
            pO.setQuantity(pd.getQty());
            lst.add(pO);
        }
        wfsCreateOrderRequestDto.setProducts(lst);
        try {
            boolean isCreateFF = true;
            if (transporter == Const.WFS_DHL_PARTNER_CODE) {//tao don hang Lastmile sang DHL
                //################ tao don hang sang DHL ###########################
                OrderResult dhlOrder = CreateDHLDelivery(deliveryDto, products);
                dhlOrder.setType(Const.WFS_DHL_PARTNER_CODE);
                if (dhlOrder.getResult() == Const.DO_ERROR) {
                    orderResult.setErrMessage(LogHelper.toJson(dhlOrder));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                }
                lstOrder.add(dhlOrder);
            }
            if (isCreateFF) {
                logger.info("[CREATE ORDER WFS] {} {}\r\n{}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(wfsCreateOrderRequestDto));
                WfsCreateOrderResponse wfsCreateOrderResponse = (WfsCreateOrderResponse) orderService.CreateOrder(wfsCreateOrderRequestDto, l.getData().getToken());
                //String wfsMessage = wfsCreateOrderResponse.getMessage();
                logger.info("{} {} {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(wfsCreateOrderResponse));
                if (wfsCreateOrderResponse != null && wfsCreateOrderResponse.getErrorCode() != null && wfsCreateOrderResponse.getErrorCode().equals("0")) {
                    orderResult.setResult(Const.DO_SUCCESS);
                    orderResult.setCode(wfsCreateOrderResponse.getErrorCode());
                    orderResult.setMessage(wfsCreateOrderResponse.getData().getCode());
                    orderResult.setType(Const.WFS_GHN_PARTNER_CODE);

                } else {//co loi xay ra khi tao WFS order
                    orderResult.setErrMessage(LogHelper.toJson(wfsCreateOrderResponse));
                    orderResult.setCode(wfsCreateOrderResponse.getErrorCode());
                    orderResult.setResult(Const.DO_ERROR);
                    //TODO cancel lastmile khi tao FF fail
                }
                lstOrder.add(orderResult);
                logger.info("{} {}============================= {}",deliveryDto.getSESSION_ID(), deliveryDto.getDoId() , LogHelper.toJson(lstOrder));
                return lstOrder;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            orderResult.setMessage(e.getMessage());
        }
        orderResult.setResult(Const.DO_ERROR);
        orderResult.setCode("WFS");
        lstOrder.add(orderResult);
        return lstOrder;
    }

    private static List<OrderResult> CreateGHNWarehouseOrderV2(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lstOrder = new ArrayList<>();
        OrderResult orderResult = new OrderResult();

        WfsOrderServiceImpl orderService = new WfsOrderServiceImpl();

        String transporter = "";
        if (deliveryDto.getLastmileId() == Const.DHL_PARTNER_ID) {
            transporter = Const.WFS_DHL_PARTNER_CODE;
        } else if (deliveryDto.getLastmileId() == Const.GHTK_LM_PARTNER_VN) {
            transporter = Const.WFS_GHTK_PARTNER_CODE;
        } else if (deliveryDto.getLastmileId() == Const.NinjaVan_PARTNER_ID){
            transporter = Const.WFS_NINJAVAN_PARTNER_CODE;
        } else if (deliveryDto.getLastmileId() == Const.SNAPPY_PARTNER_ID){
            transporter = Const.WFS_SNAPPY_PARTNER_CODE;
        } else if (deliveryDto.getLastmileId() == Const.SHIP60_LM_PARTNER_VN){
            transporter = Const.WFS_S60_PARTNER_CODE;
        } else {
            transporter = Const.WFS_GHN_PARTNER_CODE;
        }

        WfsCreateOrderV2RequestDto requestDto = new WfsCreateOrderV2RequestDto();
        requestDto.setClientOrderCode(deliveryDto.getDoCode());
        //TODO need to read
        //requestDto.setWarehouseID("HCM-Q12-1");
        requestDto.setWarehouseID(deliveryDto.getGroupAddressId());
        requestDto.setOrderType("B2C1D");
        requestDto.setToProvinceID(deliveryDto.getProvinceId().toString());
        requestDto.setToDistrictID(deliveryDto.getGhnDistrictCode());
        requestDto.setToWardID(deliveryDto.getWardsCode());
        requestDto.setTplOutbound(transporter);

        requestDto.setShippingOrderValue(1000000);

        requestDto.setToName(deliveryDto.getReceiveName());
        requestDto.setToPhone(deliveryDto.getPhone());
        requestDto.setToAddress(deliveryDto.getAddress().length() > 99 ? deliveryDto.getAddress().substring(0, 99) : deliveryDto.getAddress());//GHN chi cho phep 100 ky tu

        requestDto.setShippingPaymentTypeId(WfsConst.WFS2_PAYMENT_FREESHIP);
        requestDto.setShippingServiceTypeId(2);
        Integer codValue = 0;
        if (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue())
            codValue = deliveryDto.getCod_money();

        requestDto.setShipmentInsuranceValue(deliveryDto.getAmount());
        requestDto.setShippingCodAmount(codValue);
        requestDto.setShippingContent(deliveryDto.getPackageName());
        requestDto.setShippingNote("Khách hàng từ chối nhận vui lòng gọi hotline 1900234500");
        requestDto.setShippingRequiredNote("CHOXEMHANGKHONGTHU");
        requestDto.setCheckAvailable("Y");


        List<WfsCreateOrderV2RequestDto.ItemOrder> lst = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            WfsCreateOrderV2RequestDto.ItemOrder item = requestDto.new ItemOrder();
            item.setSku(products.get(i).getProPartnerCode());
            item.setLineNo(i + 1);
            item.setQuantity(products.get(i).getQty());
            lst.add(item);
        }

        requestDto.setItems(lst);
        try {
            boolean isCreateFF = true;
            String lastmile_code = "";
            String lastmileLabelUrl  = "";

            if (transporter == Const.WFS_NINJAVAN_PARTNER_CODE) {//tao don hang Lastmile sang NinjaVan
                //################ tao don hang sang DHL ###########################
                OrderResult dhlOrder = CreateNinjaVanOrder(deliveryDto, products);
                dhlOrder.setType(Const.WFS_NINJAVAN_PARTNER_CODE);
                if (dhlOrder.getResult() == Const.DO_ERROR) {
                    logger.info("*****************************************************************************");
                    orderResult.setErrMessage(LogHelper.toJson(dhlOrder));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                } else {
                    logger.info("##############################################################################{}", LogHelper.toJson(dhlOrder));
                    lastmile_code = dhlOrder.getMessage();
                    lastmileLabelUrl = dhlOrder.getLabel();
                }

                lstOrder.add(dhlOrder);
            }

            if (transporter == Const.WFS_S60_PARTNER_CODE) {//tao don hang Lastmile sang S60
                //################ tao don hang sang S60 ###########################
                OrderResult s60Result = createShip60Delivery(deliveryDto, products);
                s60Result.setType(Const.WFS_S60_PARTNER_CODE);
                if (s60Result.getResult() == Const.DO_ERROR) {
                    logger.info("*****************************************************************************");
                    orderResult.setErrMessage(LogHelper.toJson(s60Result));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                } else {
                    logger.info("##############################################################################{}", LogHelper.toJson(s60Result));
                    lastmile_code = s60Result.getMessage();
                    lastmileLabelUrl = s60Result.getLabel();
                }

                lstOrder.add(s60Result);
            }

            if (transporter == Const.WFS_DHL_PARTNER_CODE) {//tao don hang Lastmile sang DHL
                //################ tao don hang sang DHL ###########################
                OrderResult dhlOrder = CreateDHLDelivery(deliveryDto, products);
                dhlOrder.setType(Const.WFS_DHL_PARTNER_CODE);
                if (dhlOrder.getResult() == Const.DO_ERROR) {
                    logger.info("*****************************************************************************");
                    orderResult.setErrMessage(LogHelper.toJson(dhlOrder));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                } else {
                    logger.info("##############################################################################{}", LogHelper.toJson(dhlOrder));
                    lastmile_code = dhlOrder.getMessage();
                    lastmileLabelUrl = dhlOrder.getLabel();
                }

                lstOrder.add(dhlOrder);
            }
            if (transporter == Const.WFS_GHTK_PARTNER_CODE) {
                OrderResult ghtkOrder = CreateGHTKOrder(deliveryDto, products);
                ghtkOrder.setType(Const.WFS_GHTK_PARTNER_CODE);
                if (ghtkOrder.getResult() == Const.DO_ERROR) {
                    logger.info("*************Create GHTK order error***********" + LogHelper.toJson(ghtkOrder));
                    orderResult.setErrMessage(String.format("%s|%s", ghtkOrder.getMessage(), ghtkOrder.getErrMessage()));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                } else {
                    logger.info("Create ghtk order success: " + LogHelper.toJson(ghtkOrder));
                    lastmile_code = ghtkOrder.getCode();
                }
            }

            if (transporter == Const.WFS_SNAPPY_PARTNER_CODE) {
                OrderResult snappyOrder = CreateSnappyOrder(deliveryDto, products);
                snappyOrder.setType(Const.WFS_GHTK_PARTNER_CODE);
                if (snappyOrder.getResult() == Const.DO_ERROR) {
                    logger.info("*************Create Snappy order error***********" + LogHelper.toJson(snappyOrder));
                    orderResult.setErrMessage(String.format("%s|%s", snappyOrder.getMessage(), snappyOrder.getErrMessage()));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                } else {
                    logger.info("Create snappy order success: " + LogHelper.toJson(snappyOrder));
                    lastmile_code = snappyOrder.getMessage();
                }
            }

            if (isCreateFF) {
                logger.info("[CREATE ORDER WFS] " + deliveryDto.getSESSION_ID() + " " + deliveryDto.getDoId());

                if (lastmile_code!= null && !lastmile_code.isEmpty())
                    requestDto.setShippingOrderCode(lastmile_code);
                if (!lastmileLabelUrl.isEmpty())
                    requestDto.setBillUrl(lastmileLabelUrl);

                logger.info(LogHelper.toJson(requestDto));

                WfsCreateOrderV2Response wfsCreateOrderResponse = (WfsCreateOrderV2Response) orderService.CreateOrderV2(requestDto);
                logger.info("{} {}\r\nresponse GHN: {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(wfsCreateOrderResponse));
                if (wfsCreateOrderResponse != null && wfsCreateOrderResponse.getCode() != null) {
                    if (wfsCreateOrderResponse.getCode() == 0) {
                        orderResult.setResult(Const.DO_SUCCESS);
                        orderResult.setCode(String.valueOf(wfsCreateOrderResponse.getCode()));
                        orderResult.setMessage(String.format("%s|%s", wfsCreateOrderResponse.getData().getClientOrderCode(), wfsCreateOrderResponse.getData().getShippingOrderCode()));
                        orderResult.setType(Const.WFS_GHN_PARTNER_CODE);
                    } else {
                        logger.info("GHN error: " + LogHelper.toJson(wfsCreateOrderResponse.getData()));
                        if (wfsCreateOrderResponse.getCode() == 40007) {
                            orderResult.setErrMessage(String.format("%s|%s", wfsCreateOrderResponse.getMsg(), wfsCreateOrderResponse.getData().getSku()));
                        } else {
                            orderResult.setErrMessage(String.format("%s|%s|%s", wfsCreateOrderResponse.getMsg(), wfsCreateOrderResponse.getData().getField(), wfsCreateOrderResponse.getData().getMsg()));
                        }
                        orderResult.setCode(String.valueOf(wfsCreateOrderResponse.getCode()));
                        orderResult.setResult(Const.DO_ERROR);
                        if (transporter == Const.WFS_SNAPPY_PARTNER_CODE) {// cancel don hang Lastmile sang Snappy
                            CancelSnappy(lastmile_code);
                        }
                    }
                } else {//co loi xay ra khi tao WFS order
                    orderResult.setErrMessage(wfsCreateOrderResponse.getResponseJson());
//                    orderResult.setCode(String.valueOf(wfsCreateOrderResponse.getCode()));
					orderResult.setResult(Const.DO_ERROR);
					// TODO cancel lastmile khi tao FF fail
					if (transporter == Const.WFS_NINJAVAN_PARTNER_CODE) {// cancel don hang Lastmile sang NinjaVan
						CancelNinjaVan(lastmile_code);
					}
                }
                lstOrder.add(orderResult);
                logger.info("{} {}============================= {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(lstOrder));
                return lstOrder;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            orderResult.setMessage(e.getMessage());
        }
        orderResult.setResult(Const.DO_ERROR);
        orderResult.setCode("WFS");
        lstOrder.add(orderResult);
        return lstOrder;
    }

    private static OrderResult createShip60Delivery(DeliveryDto deliveryDto, List<ProductDto> lst) {

        OrderResult orderResult = new OrderResult();
        ShipOrderServiceImpl shipOrderService = new ShipOrderServiceImpl();
        Ship60CreateOrderRequest ship60CreateOrderRequest = new Ship60CreateOrderRequest();

        Shop shop = new Shop();
        shop.setShopName("Tele247 - HOTLINE: 1900234500");
        shop.setRawAddress(deliveryDto.getsAddress());
        shop.setWardCode(deliveryDto.getsWardCode());
        shop.setWard(deliveryDto.getsWardName());
        shop.setDistrictCode(deliveryDto.getsDistrictCode());
        shop.setCityCode(deliveryDto.getsProvinceCode());
        shop.setCity(deliveryDto.getsProvinceName());
        shop.setDistrict(deliveryDto.getsDistrictName());
        shop.setSenderPhone(deliveryDto.getsPhone());
        shop.setSenderName("Tele247 - HOTLINE: 1900234500");
        ship60CreateOrderRequest.setShop(shop);
        Orders orders = new Orders();
        orders.setOrderCode(deliveryDto.getDoCode());
        orders.setReceiverPhone(deliveryDto.getPhone());
        orders.setReceiverName(deliveryDto.getReceiveName());

        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setRawAddress(deliveryDto.getAddress());
        deliveryAddress.setWard(deliveryDto.getWardName());
        deliveryAddress.setWardCode(deliveryDto.getWardsCode());
        deliveryAddress.setDistrict(deliveryDto.getDistrictName());
        deliveryAddress.setDistrictCode(deliveryDto.getDistrictCode());
        deliveryAddress.setCityCode(deliveryDto.getProvinceCode());
        deliveryAddress.setCity(deliveryDto.getProvinceName());
        orders.setDeliveryAddress(deliveryAddress);

        ProductDto productDto = lst.get(0);
        orders.setPackageDescription("Sản phẩm hỗ trợ sức khỏe");
        orders.setNote("Cho xem hàng không thử | " + deliveryDto.getComment());
        double valueManifest =  productDto.getPrice();
        orders.setCod(deliveryDto.getCod_money());
        orders.setValueManifest((int) valueManifest);
        double weight = productDto.getWeight();
        orders.setWeight((int) weight);
        Orders[] orders1 = new Orders[1];
        orders1[0] = orders;
        ship60CreateOrderRequest.setOrders(orders1);
        JSONObject jsson = new JSONObject(ship60CreateOrderRequest);
        logger.info("Request: {}", jsson);

        try {
            orderResult.setType(Const.WFS_S60_PARTNER_CODE);
            Ship60ResponseMap ship60ResponseMap = shipOrderService.createOrderShip60(ship60CreateOrderRequest);
            JSONObject jssonResult = new JSONObject(ship60ResponseMap);
            logger.info("Result: {}", jssonResult);
            if(ship60ResponseMap!=null && ship60ResponseMap.getResponseCode()== 20010){
                orderResult.setCode("200");
                orderResult.setLabel("SUCCESS");
                orderResult.setResult(Const.DO_SUCCESS);
                orderResult.setMessage(ship60ResponseMap.getDto().getOrders()[0].getShip60OrderCode());
            }else {
                orderResult.setResult(Const.DO_ERROR);
                orderResult.setErrMessage(ship60ResponseMap.getMessage());
            }
        }catch (Exception e){
            logger.error("{} {}[SHIP60]{}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), e.getMessage(), e);
            orderResult.setMessage(e.getMessage());
            orderResult.setResult(Const.DO_ERROR);
        }

        return orderResult;
    }

    private static OrderResult CreateGHTKOrder(DeliveryDto deliveryDto, List<ProductDto> products) {
        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.WFS_GHTK_PARTNER_CODE);
        List<GHTKProduct> lstProduct = new ArrayList<>();
        GHTKRequestDto request = new GHTKRequestDto();
        GHTKOrder order = new GHTKOrder();
        GHTKOrderServiceImpl ghtkOrderService = new GHTKOrderServiceImpl(null);
        int quantity = 0;
        int price = 0;
        for (ProductDto dto : products) {
            quantity += dto.getQty();
            price += dto.getPrice().intValue();
        }
        GHTKProduct product = new GHTKProduct();
        product.setName("HÃ€NG HÃ“A");
        product.setQuantity(quantity);
        product.setPrice(price);
        product.setWeight(0.3);
        lstProduct.add(product);
        request.setProducts(lstProduct);

        //set doCode
        order.setId(deliveryDto.getDoCode());
        //set info warehouse
        order.setPick_name(deliveryDto.getPickupName());
        order.setPick_address(deliveryDto.getsAddress());
        order.setPick_province(deliveryDto.getsProvinceName());
        order.setPick_district(deliveryDto.getsDistrictName());
        order.setPick_tel(deliveryDto.getsPhone());
        if (deliveryDto.getPaymentMethod().intValue() == EnumType.PAYMENT_METHOD.COD.getValue()) {
            order.setPick_money(deliveryDto.getAmount());
        } else {
            order.setPick_money(0);
        }
        //set info customer
        order.setName(deliveryDto.getReceiveName());
        order.setTel(deliveryDto.getPhone());
        order.setAddress(deliveryDto.getAddress());
        order.setProvince(deliveryDto.getProvinceName());
        order.setDistrict(deliveryDto.getDistrictName());
        order.setWard(deliveryDto.getWardName());
        order.setHamlet("KhÃ¡c");
        Integer isFreeShip = checkIsFreeShip(products);
        order.setIs_freeship(1);

        order.setUse_return_address(0);
        order.setValue(deliveryDto.getAmount());
        request.setOrder(order);
        try {
            logger.info(deliveryDto.getSESSION_ID() + " " + deliveryDto.getDoId() + "[CREATE ORDER GHTK] Request: " + LogHelper.toJson(request));
            GHTKResponseDto response = ghtkOrderService.createOrder(request);

            if (response.getSuccess().compareTo("true") == 0) {

                orderResult.setResult(Const.DO_SUCCESS);
                orderResult.setCode(response.getOrder().getLabel());
                orderResult.setLabel(response.getOrder().getLabel());
                orderResult.setMessage(response.getOrder().getLabel());
                return orderResult;
            } else {
                orderResult.setErrMessage(LogHelper.toJson(response));
                orderResult.setMessage("ERROR when compare code GHTK");
                orderResult.setResult(Const.DO_ERROR);
                return orderResult;
            }
        } catch (Exception e) {
            logger.error("{} {}[GHTK]{}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), e.getMessage(), e);
            orderResult.setMessage(e.getMessage());
        }
        orderResult.setCode("GHTK");
        orderResult.setMessage("ERROR when create GHTK");
        orderResult.setResult(Const.DO_ERROR);

        return orderResult;

    }


    private static OrderResult CreateNinjaVanOrder(DeliveryDto deliveryDto, List<ProductDto> products){
    	 OrderResult orderResult = new OrderResult();
    	 orderResult.setType(Const.WFS_NINJAVAN_PARTNER_CODE);
    	 NinjaVanOrder request = new NinjaVanOrder();
    	 NinjaVanOrderServiceImpl ninjaVanOrderServiceImpl = new NinjaVanOrderServiceImpl(null);
    	 List<NinjaVanItems> listNinjaVanItems = new ArrayList<>();
    	 NinjaVanDimesions ninjaVanDimesions = new NinjaVanDimesions();


    	 request.setService_type("Parcel");
    	 request.setService_level("Express");
         //set tracking number cho ninjaVan, lay tu do code cua minh
         request.setRequested_tracking_number(deliveryDto.getDoId()+"");

    	 double weight = 0;
    	 double length = 0;
    	 double width = 0;
    	 double height = 0;
    	 String note = "Sản phẩm: ";
         for (ProductDto dto : products){
             NinjaVanItems ninjaVanItem = new NinjaVanItems();
             ninjaVanItem.setIs_dangerous_good(false);
             ninjaVanItem.setItem_description(dto.getProName());
             ninjaVanItem.setQuantity(dto.getQty());
             weight += dto.getWeight();
             length += dto.getLength();
             width += dto.getWidth();
             height += dto.getHeight();
             listNinjaVanItems.add(ninjaVanItem);
             note += dto.getProName()+", số lượng: "+dto.getQty()+".";
         }
         ninjaVanDimesions.setHeight(height);
         ninjaVanDimesions.setWeight(weight);
         ninjaVanDimesions.setWidth(width);
         ninjaVanDimesions.setLength(length);


    	 NinjaVanParcelJob ninjaVanParcelJob = new NinjaVanParcelJob();
    	 ninjaVanParcelJob.setDimensions(ninjaVanDimesions);

         //set info warehouse
         NinjaVanFromTo from = new NinjaVanFromTo();
         from.setName(deliveryDto.getPickupName());
         from.setPhone_number(deliveryDto.getsPhone());
         NinjaVanAddress fromAddress = new NinjaVanAddress();
         fromAddress.setAddress1(deliveryDto.getsAddress());
         fromAddress.setProvince(deliveryDto.getsProvinceName());
         fromAddress.setDistrict(deliveryDto.getsDistrictName());
         fromAddress.setCountry("VN");
         from.setAddress(fromAddress);
         //set info customer
         NinjaVanFromTo to = new NinjaVanFromTo();
         to.setName(deliveryDto.getReceiveName());
         to.setPhone_number(deliveryDto.getPhone());
         NinjaVanAddress toAddress = new NinjaVanAddress();
         toAddress.setAddress1(deliveryDto.getAddress());
         toAddress.setProvince(deliveryDto.getProvinceName());
         toAddress.setDistrict(deliveryDto.getDistrictName());
         toAddress.setCountry("VN");
         to.setAddress(toAddress);
         //set payment method
         if (deliveryDto.getPaymentMethod().intValue() == EnumType.PAYMENT_METHOD.COD.getValue()){
        	 ninjaVanParcelJob.setCash_on_delivery(Double.parseDouble(deliveryDto.getAmount().toString()));
         } else {
        	 ninjaVanParcelJob.setCash_on_delivery((double) 0);
         }

         ninjaVanParcelJob.setIs_pickup_required(true);
         ninjaVanParcelJob.setPickup_service_type("Scheduled");
         ninjaVanParcelJob.setPickup_service_level("Standard");
         ninjaVanParcelJob.setPickup_instructions("Pickup with care!");
         note+= "Lưu ý: "+deliveryDto.getNote();
         ninjaVanParcelJob.setDelivery_instructions(note);

         //set time pickup
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         ninjaVanParcelJob.setPickup_date(sdf.format(new LocalDateTime().plusDays(1).toDate()));
         NinjaVanPickUpTimeSlot pickup_timeslot = new NinjaVanPickUpTimeSlot();
         pickup_timeslot.setStart_time("09:00");
         pickup_timeslot.setEnd_time("12:00");
         pickup_timeslot.setTimezone("Asia/Ho_Chi_Minh");
    	 ninjaVanParcelJob.setPickup_timeslot(pickup_timeslot);

    	 //set time delivery
         ninjaVanParcelJob.setDelivery_start_date(sdf.format(new LocalDateTime().plusDays(3).toDate()));
         NinjaVanPickUpTimeSlot delivery_timeslot = new NinjaVanPickUpTimeSlot();
         delivery_timeslot.setStart_time("09:00");
         delivery_timeslot.setEnd_time("18:00");
         delivery_timeslot.setTimezone("Asia/Ho_Chi_Minh");
    	 ninjaVanParcelJob.setDelivery_timeslot(delivery_timeslot);

    	 ninjaVanParcelJob.setItems(listNinjaVanItems);
    	 request.setParcel_job(ninjaVanParcelJob);
    	 request.setFrom(from);
    	 request.setTo(to);

    	 String token = "";


			 token = getNinjaVanToken();



    	  try {
              logger.info(deliveryDto.getSESSION_ID() + " " + deliveryDto.getDoId() + "[CREATE ORDER NinjaVan] Request: " + LogHelper.toJson(request));
              NinjaVanResponseDto response =  ninjaVanOrderServiceImpl.createOrder(request, token);

				if (response.getTracking_number() != null && response.getTracking_number().length() > 0) {
					orderResult.setResult(Const.DO_SUCCESS);
					orderResult.setCode(200+"");
					orderResult.setLabel("SUCCESS");
					orderResult.setMessage(response.getTracking_number());
					return orderResult;
				}  else {
					if (response.getError().getTitle() != null) {
						orderResult.setErrMessage(LogHelper.toJson(response.getError().getTitle()));
						orderResult.setMessage("ERROR when compare code NinjaVan");
						orderResult.setResult(Const.DO_ERROR);
						return orderResult;
					}
              }
          } catch (Exception e) {
              logger.error("{} {}[NinjaVan]{}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), e.getMessage(), e);
              orderResult.setMessage(e.getMessage());
          }

        return orderResult;

    }

    public static Integer checkIsFreeShip(List<ProductDto> productDtos) {
        if (productDtos.size() > 1) {
            return 1;
        }
        if (productDtos.get(0).getQty() > 1) {
            return 1;
        }
        return 0;
    }

    private static List<OrderResult> CreateGHNWarehouseOrder(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lstOrder = new ArrayList<>();
        OrderResult orderResult = new OrderResult();

        WfsOrderServiceImpl orderService = new WfsOrderServiceImpl();
        WfsAuthenticationServiceImpl service = new WfsAuthenticationServiceImpl();
        LoginDto loginRequestDto = new WfsLoginRequestDto();
        WfsLoginResponse l = (WfsLoginResponse) service.login(WfsConst.hostName, null, WfsConst.LOGIN_PATH, loginRequestDto);

        logger.info("Message: {}\r\nToken: {}", l.getMessage(), l.getData().getToken());

        WfsCreateOrderRequestDto wfsCreateOrderRequestDto = new WfsCreateOrderRequestDto();
        wfsCreateOrderRequestDto.setToken(l.getData().getToken());
        wfsCreateOrderRequestDto.setCustomerAddress(deliveryDto.getAddress());
        wfsCreateOrderRequestDto.setCustomerDistrict(deliveryDto.getDistrictId());
        wfsCreateOrderRequestDto.setCustomerProvince(deliveryDto.getProvinceId());
//        wfsCreateOrderRequestDto.setCustomerWard(deliveryDto.getWardsId());
        wfsCreateOrderRequestDto.setCustomerName(deliveryDto.getReceiveName());
        wfsCreateOrderRequestDto.setCustomerPhone(deliveryDto.getPhone());
//        wfsCreateOrderRequestDto.setNoteCode(deliveryDto.getNote());
        wfsCreateOrderRequestDto.setCoDAmount(deliveryDto.getCod_money());
        wfsCreateOrderRequestDto.setNote(String.valueOf(deliveryDto.getCod_money()));
        //TODO need to change
        wfsCreateOrderRequestDto.setOrigin(deliveryDto.getDoCode());

        wfsCreateOrderRequestDto.setLocation(deliveryDto.getWarehouseId());//39
        //van chuyen noi tinh doi voi GHN Express thi dung CHUAN, field only used by GHN Express by GHN Logistic
        //wfsCreateOrderRequestDto.setServiceID(deliveryDto.isLocalProvince() ? Const.WFS_GHN_EXPRESS_SERVICE_CHUAN : Const.WFS_GHN_EXPRESS_SERVICE_NHANH);
        //Only require by GHN Express
        wfsCreateOrderRequestDto.setServiceID(Const.WFS_GHN_EXPRESS_SERVICE_CHUAN);
        wfsCreateOrderRequestDto.setPaymentTypeID(Const.WFS_GHN_EXPRESS_PAYMENT_TYPE_ID);
        wfsCreateOrderRequestDto.setNoteCode(Const.WFS_GHN_EXPRESS_NOTE_CODE);
        wfsCreateOrderRequestDto.setInsuranceFee(0);
        String transporter = (deliveryDto.getLastmileId() == Const.DHL_PARTNER_ID ? Const.WFS_DHL_PARTNER_CODE : Const.WFS_GHN_PARTNER_CODE);
        wfsCreateOrderRequestDto.setTransporter(transporter);
        wfsCreateOrderRequestDto.setServicePackage("chuan");

        //TODO need to be clear --- SOLUTION???/
        wfsCreateOrderRequestDto.setHeight(10);
        wfsCreateOrderRequestDto.setLength(10);
        wfsCreateOrderRequestDto.setWeight(100);
        wfsCreateOrderRequestDto.setWidth(10);
        List<WfsCreateOrderRequestDto.ProductOrder> lst = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            ProductDto pd = products.get(i);
            WfsCreateOrderRequestDto.ProductOrder pO = wfsCreateOrderRequestDto.new ProductOrder();
            int proPartnerId = Integer.parseInt(pd.getProCode());
            pO.setId(proPartnerId);
            pO.setQuantity(pd.getQty());
            lst.add(pO);
        }
        wfsCreateOrderRequestDto.setProducts(lst);
        try {
            logger.info("[CREATE ORDER WFS] {} {} {}",
                    deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(wfsCreateOrderRequestDto));
            WfsCreateOrderResponse wfsCreateOrderResponse = (WfsCreateOrderResponse) orderService.CreateOrder(wfsCreateOrderRequestDto, l.getData().getToken());
            logger.info("[RESPONSE WFS] {} {} {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(wfsCreateOrderResponse));
            if (wfsCreateOrderResponse != null && wfsCreateOrderResponse.getErrorCode() != null && wfsCreateOrderResponse.getErrorCode().equals("0")) {
                orderResult.setResult(Const.DO_SUCCESS);
                orderResult.setCode(wfsCreateOrderResponse.getErrorCode());
                orderResult.setMessage(wfsCreateOrderResponse.getData().getCode());
                orderResult.setType(Const.WFS_GHN_PARTNER_CODE);
                lstOrder.add(orderResult);
                //################ tao don hang sang DHL ###########################
                if (transporter == Const.WFS_DHL_PARTNER_CODE) {
                    OrderResult dhlOrder = CreateDHLDelivery(deliveryDto, products);
                    dhlOrder.setType(Const.WFS_DHL_PARTNER_CODE);
                    lstOrder.add(dhlOrder);

                    if (dhlOrder.getResult() == Const.DO_ERROR) {
                        orderResult.setErrMessage(LogHelper.toJson(dhlOrder));
                        orderResult.setResult(Const.DO_ERROR);
                    }
                }
            } else {//co loi xay ra khi tao WFS order
                orderResult.setErrMessage(LogHelper.toJson(wfsCreateOrderResponse));
                orderResult.setCode(wfsCreateOrderResponse.getErrorCode());
                orderResult.setResult(Const.DO_ERROR);
                lstOrder.add(orderResult);
            }
            logger.info("{} {}============================= {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(lstOrder));
            return lstOrder;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            orderResult.setMessage(e.getMessage());
        }
        orderResult.setResult(Const.DO_ERROR);
        orderResult.setCode("WFS");
        lstOrder.add(orderResult);
        return lstOrder;
    }

    private static List<OrderResult> CreateBoxmeDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lst = new ArrayList<>();

        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.BM_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);

        if (deliveryDto.getLastmileId().intValue() == Const.BM_PARTNER_ID) {
            return createOrderBoxmeWithVNP(deliveryDto, products);
        }

        OrderResult lastmileResult = new OrderResult();
        //1. Create DHL label
        if (deliveryDto.getLastmileId() == Const.DHL_PARTNER_ID) {
            lastmileResult = CreateDHLDelivery(deliveryDto, products, EnumType.DHL_LABEL_TYPE.BASE64.getValue());
            lst.add(lastmileResult);
        }

        if (deliveryDto.getLastmileId() == Const.SHIP60_LM_PARTNER_VN) {
            lastmileResult = createShip60Delivery(deliveryDto, products);
            lst.add(lastmileResult);
        }

        if (deliveryDto.getLastmileId() == Const.GHTK_LM_PARTNER_VN) {
            lastmileResult = CreateGHTKOrder(deliveryDto, products);
            lst.add(lastmileResult);
        }
        if (deliveryDto.getLastmileId() == Const.NinjaVan_PARTNER_ID) {
            lastmileResult = CreateNinjaVanOrder(deliveryDto, products);
            lst.add(lastmileResult);
        }
        if (deliveryDto.getLastmileId() == Const.SNAPPY_PARTNER_ID) {
            lastmileResult = CreateSnappyOrder(deliveryDto, products);
            lst.add(lastmileResult);
        }
        logger.info("Lastmile Result: " + LogHelper.toJson(lastmileResult));
        if (lastmileResult.getLabel() != null && !lastmileResult.getLabel().isEmpty()) {//tao don DHL thanh cong
            //2. Create Boxme order
            BoxmeOrderServiceImpl service = new BoxmeOrderServiceImpl();

            BoxmeCreateOrderRequestDto requestDto = new BoxmeCreateOrderRequestDto();
            //tracking dhl
            lst.add(lastmileResult);

            ShipFromDto shipFromDto = new ShipFromDto();
            shipFromDto.setCountry("VN");
            //change from Integer to Array Integer, special case support by BOXME

            shipFromDto.setPickup_id(Arrays.asList(BoxmeConst.BOXME_PICKUP_ID));
            requestDto.setShip_from(shipFromDto);

            ShipToDto shipToDto = new ShipToDto();
            shipToDto.setAddress(deliveryDto.getAddress());
            shipToDto.setContact_name(deliveryDto.getReceiveName());
            shipToDto.setPhone(deliveryDto.getPhone());
            shipToDto.setPhone2(deliveryDto.getPhone2());
            shipToDto.setCountry("VN");
            //TODO

            shipToDto.setDistrict(deliveryDto.getReicvWhDistrictId());
            shipToDto.setProvince(deliveryDto.getReicvWhProvinceId());
            requestDto.setShip_to(shipToDto);

            ShipmentDto shipmentDto = new ShipmentDto();
            if (products.size() > 0)
                shipmentDto.setContent(products.get(0).getProName());
            else
                shipmentDto.setContent("No Product name");

            shipmentDto.setTotal_amount(deliveryDto.getCod_money());
            //TODO
            shipmentDto.setTotal_parcel(1);
            shipmentDto.setChargeable_weight(10);
            shipmentDto.setDescription(deliveryDto.getNote());

            List<ParacelDto> paracelDtoList = new ArrayList();
            ParacelDto paracelDto = new ParacelDto();
            paracelDto.setWeight(10);
            paracelDto.setDescription(deliveryDto.getNote());
            List<ItemsDto> itemsDtoList = new ArrayList();
            for (int i = 0; i < products.size(); i++) {
                ProductDto pd = products.get(i);
                ItemsDto itemsDto = new ItemsDto();
                itemsDto.setSku(pd.getProPartnerCode());
                itemsDto.setOrigin_country("VN");
                itemsDto.setName(pd.getProName());
                itemsDto.setDesciption(pd.getProName());
                itemsDto.setWeight(10);
                itemsDto.setValue(pd.getPrice().intValue());
                itemsDto.setQuantity(pd.getQty());
                itemsDtoList.add(itemsDto);
            }
            paracelDto.setItems(itemsDtoList);
            paracelDtoList.add(paracelDto);

            shipmentDto.setParcels(paracelDtoList);

            requestDto.setShipments(shipmentDto);

            ConfigDto configDto = new ConfigDto();
            configDto.setCurrency("VND");
            configDto.setUnit_metric("metric");
            configDto.setDocument("Y");
            configDto.setInsurance("N");
            configDto.setReturn_mode(1);
//            configDto.setOrderType();//da thiet lap mang dinh = fulfill
            configDto.setSort_mode("best_price");//best_price: giao cháº­m
            configDto.setOrder_type("fulfill");
            requestDto.setConfig(configDto);

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setFee_paid_by("sender");
//            paymentDto.setCod_amount(deliveryDto.getCod_money());
            //boxme khong thu ho tien nen COD = 0
            paymentDto.setCod_amount(deliveryDto.getCod_money());
            requestDto.setPayment(paymentDto);

            ReferralDto referralDto = new ReferralDto();
            referralDto.setOrder_number(deliveryDto.getDoCode());
            requestDto.setReferral(referralDto);

            logger.info("{} {} BOXME {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(requestDto));
            BoxmeCreateOrderResponse createOrderResponse = (BoxmeCreateOrderResponse) service.CreateOrder(requestDto);
            logger.info("{} {}message: {}",deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), createOrderResponse.getMessages());


            if (!createOrderResponse.isError()) {
                //3. Update label from DHL to Boxme
                BoxmeUpdateLabelServiceImpl updateLabelService = new BoxmeUpdateLabelServiceImpl();
                logger.info("BOXME UPDATE LABEL ");
//                UpdateLabelRequestDto updateLabelRequestDto = new UpdateLabelRequestDto();
//                updateLabelRequestDto.setLabel(lastmileResult.getLabel());
//                updateLabelRequestDto.setTracking_number(createOrderResponse.getData().getTracking_number());//BM81164925529
//                updateLabelRequestDto.setCourier_tracking_code(lastmileResult.getMessage());//5019095370028869
                UpdateLabelRequestDtoV2 updateLabelRequestDto = new UpdateLabelRequestDtoV2();
                if (deliveryDto.getLastmileId() == Const.DHL_PARTNER_ID) {
                    updateLabelRequestDto.setCourier_name(Const.DHL_COURIER_NAME);
                    updateLabelRequestDto.setCourier_tracking_code(lastmileResult.getMessage());
                    updateLabelRequestDto.setLabel(lastmileResult.getLabel());
                    updateLabelRequestDto.setLabel_type("PNG");
                    updateLabelRequestDto.setTracking_code(createOrderResponse.getData().getTracking_number());
                }
                if (deliveryDto.getLastmileId() == Const.GHTK_LM_PARTNER_VN) {
                    updateLabelRequestDto.setCourier_name(Const.GHTK_COURIER_NAME);
                    List<String> trackingCodes = Arrays.asList(lastmileResult.getMessage().replace(".", ",").split(","));
                    updateLabelRequestDto.setCourier_tracking_code(trackingCodes.get(trackingCodes.size() - 1));
                    updateLabelRequestDto.setLabel("");
                    updateLabelRequestDto.setLabel_type("PNG");
                    updateLabelRequestDto.setTracking_code(createOrderResponse.getData().getTracking_number());
                }
                if (deliveryDto.getLastmileId() == Const.NinjaVan_PARTNER_ID){
                    updateLabelRequestDto.setCourier_name(Const.WFS_NINJAVAN_PARTNER_CODE);
                    List<String> trackingCodes = Arrays.asList(lastmileResult.getMessage().replace(".", ",").split(","));
                    updateLabelRequestDto.setCourier_tracking_code(lastmileResult.getMessage());
                    updateLabelRequestDto.setLabel("");
                    updateLabelRequestDto.setLabel_type("PNG");
                    updateLabelRequestDto.setTracking_code(createOrderResponse.getData().getTracking_number());
                }
                if (deliveryDto.getLastmileId() == Const.SNAPPY_PARTNER_ID){
                    updateLabelRequestDto.setCourier_name(Const.WFS_SNAPPY_PARTNER_CODE);
                    List<String> trackingCodes = Arrays.asList(lastmileResult.getMessage().replace(".", ",").split(","));
                    updateLabelRequestDto.setCourier_tracking_code(lastmileResult.getMessage());
                    updateLabelRequestDto.setLabel("");
                    updateLabelRequestDto.setLabel_type("PNG");
                    updateLabelRequestDto.setTracking_code(createOrderResponse.getData().getTracking_number());
                }
                logger.info(deliveryDto.getSESSION_ID() + " " + deliveryDto.getDoId() + "BOXME Create " + LogHelper.toJson(updateLabelRequestDto));
                BoxmeUpdateLabelResponse boxmeUpdateLabelResponse = (BoxmeUpdateLabelResponse) updateLabelService.updateLabelV2(updateLabelRequestDto);
                logger.info(deliveryDto.getSESSION_ID() + " " + deliveryDto.getDoId() + "BOXME Update label result " + LogHelper.toJson(boxmeUpdateLabelResponse));
                if (boxmeUpdateLabelResponse != null && !boxmeUpdateLabelResponse.isError()) {
                    orderResult.setResult(Const.DO_SUCCESS);
                    orderResult.setMessage(createOrderResponse.getData().getTracking_number());
                    orderResult.setCode("");
                    logger.info("Boxme Order Result: " + LogHelper.toJson(orderResult));
                    lst.add(orderResult);
                    return lst;
                }

            }

            //cancel don ninjaVan
            if (deliveryDto.getLastmileId() == Const.NinjaVan_PARTNER_ID){// cancel don hang Lastmile sang NinjaVan
				CancelNinjaVan(lastmileResult.getMessage());
			}
            if (deliveryDto.getLastmileId() == Const.SNAPPY_PARTNER_ID){// cancel don hang Lastmile sang Snappy
                CancelSnappy(lastmileResult.getMessage());
            }
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("BOXME");
            orderResult.setErrMessage(LogHelper.toJson(createOrderResponse));
            orderResult.setCode(createOrderResponse.getErrorCode());
            lst.add(orderResult);
        } /*else {
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("BOXME");
            orderResult.setErrMessage(LogHelper.toJson(createOrderResponse));
            orderResult.setCode(createOrderResponse.getErrorCode());
            lst.add(orderResult);
        }*/

        return lst;
    }

    /**
     * Su dung dich vu boxme express
     *
     * @param deliveryDto
     * @param products
     * @return
     */
    private static List<OrderResult> CreateBoxmeExpressDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lst = new ArrayList<>();

        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.BM_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);

        //2. Create Boxme order
        BoxmeOrderServiceImpl service = new BoxmeOrderServiceImpl();

        BoxmeCreateOrderRequestDto requestDto = new BoxmeCreateOrderRequestDto();


        ShipFromDto shipFromDto = new ShipFromDto();
        shipFromDto.setCountry("VN");
        //change from Integer to Array Integer, special case support by BOXME
        List<Integer> pickupIds = convertToArrayPickup(deliveryDto.getGroupAddressId());
        if (pickupIds.isEmpty()) {
            logger.error("FAIL TO CONVERT WAREHOUSE PARTNER - SYS");
            orderResult.setErrMessage("FAIL TO CONVERT WAREHOUSE PARTNER");
            lst.add(orderResult);
            return lst;
        }
        shipFromDto.setPickup_id(pickupIds);
        requestDto.setShip_from(shipFromDto);

        ShipToDto shipToDto = new ShipToDto();
        shipToDto.setAddress(deliveryDto.getAddress());
        shipToDto.setContact_name(deliveryDto.getReceiveName());
        shipToDto.setPhone(deliveryDto.getPhone());
        shipToDto.setCountry("VN");
        //TODO

        shipToDto.setDistrict(deliveryDto.getReicvWhDistrictId());
        shipToDto.setProvince(deliveryDto.getReicvWhProvinceId());
        requestDto.setShip_to(shipToDto);

        ShipmentDto shipmentDto = new ShipmentDto();
        if (products.size() > 0)
            shipmentDto.setContent(products.get(0).getProName());
        else
            shipmentDto.setContent("No Product name");

        shipmentDto.setTotal_amount(deliveryDto.getCod_money());
        //TODO
        shipmentDto.setTotal_parcel(1);
        shipmentDto.setChargeable_weight(10);
        shipmentDto.setDescription(deliveryDto.getNote());

        List<ParacelDto> paracelDtoList = new ArrayList();
        ParacelDto paracelDto = new ParacelDto();
        paracelDto.setWeight(10);
        paracelDto.setDescription(deliveryDto.getNote());
        List<ItemsDto> itemsDtoList = new ArrayList();
        for (int i = 0; i < products.size(); i++) {
            ProductDto pd = products.get(i);
            ItemsDto itemsDto = new ItemsDto();
            itemsDto.setSku(pd.getProCode());
            itemsDto.setOrigin_country("VN");
            itemsDto.setName(pd.getProName());
            itemsDto.setDesciption(pd.getProName());
            itemsDto.setWeight(10);
            itemsDto.setValue(pd.getPrice().intValue());
            itemsDto.setQuantity(pd.getQty());
            itemsDtoList.add(itemsDto);
        }
        paracelDto.setItems(itemsDtoList);
        paracelDtoList.add(paracelDto);

        shipmentDto.setParcels(paracelDtoList);

        requestDto.setShipments(shipmentDto);

        ConfigDto configDto = new ConfigDto();
        configDto.setCurrency("VND");
        configDto.setUnit_metric("metric");
        configDto.setDocument("Y");
        configDto.setInsurance("N");
        configDto.setReturn_mode(1);
        configDto.setAuto_approve("Y");
        configDto.setDelivery_service("BM_EXP");
//            configDto.setOrderType();//da thiet lap mang dinh = fulfill
//            configDto.setSort_mode("best_price");//best_price: giao chậm
        requestDto.setConfig(configDto);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setFee_paid_by("sender");
//            paymentDto.setCod_amount(deliveryDto.getCod_money());
        //boxme khong thu ho tien nen COD = 0
        paymentDto.setCod_amount((deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) ? deliveryDto.getCod_money() : 0);
        requestDto.setPayment(paymentDto);

        ReferralDto referralDto = new ReferralDto();
        referralDto.setOrder_number(deliveryDto.getDoCode());
        requestDto.setReferral(referralDto);

        logger.info("{} {} BOXME {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(requestDto));
        BoxmeCreateOrderResponse createOrderResponse = (BoxmeCreateOrderResponse) service.CreateOrder(requestDto);
        logger.info("{} {}message: {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), createOrderResponse.getMessages());


        if (!createOrderResponse.isError()) {
            orderResult.setResult(Const.DO_SUCCESS);
            orderResult.setMessage(createOrderResponse.getData().getTracking_number());
            orderResult.setCode("");
            lst.add(orderResult);
            return lst;
        }
        orderResult.setResult(Const.DO_ERROR);
        orderResult.setMessage("BOXME");
        orderResult.setErrMessage(LogHelper.toJson(createOrderResponse));
        orderResult.setCode(createOrderResponse.getErrorCode());
        lst.add(orderResult);


        return lst;
    }

    /**
     * @param groupAddressId indo format Indo 126914|126918
     * @return
     */
    private static List<Integer> convertToArrayPickup(String groupAddressId) {
        List<Integer> lst = new ArrayList<>();
        logger.info("groupid       dddd {}", groupAddressId);
        if (groupAddressId.isEmpty())
            return lst;
        try {
            String[] tmpPickup = groupAddressId.split("\\|");
            if (tmpPickup.length > 0) {

                for (int i = 0; i < tmpPickup.length; i++) {
                    lst.add(Integer.parseInt(tmpPickup[i]));
                }
            }
        } catch (Exception e) {
            logger.error("Error when convert warehouse ID convertToArrayPickup {}", groupAddressId, e);
        }
        return lst;
    }

    /**
     * create order for boxme indonesia, just create order in warehouse
     *
     * @param deliveryDto
     * @param products
     * @return
     */
    private static List<OrderResult> CreateBoxmeIndoDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lst = new ArrayList<>();
        String countryCode = "ID";
        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.BM_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);
        logger.info("FAIL TO CONVERT WAREHOUSE PARTNER - SYS 4");
        String pickUpId = AppProperties.getPropertyValue(Helper.getKeyConfigByOrg(deliveryDto.getOrgId(), "api.BOXME.pickupid"));
        logger.info("FAIL TO CONVERT WAREHOUSE PARTNER - SYS 5");
        {
            //2. Create Boxme order
            BoxmeOrderServiceImpl service = new BoxmeOrderServiceImpl();

            BoxmeCreateOrderRequestDto requestDto = new BoxmeCreateOrderRequestDto();
            //tracking dhl

            ShipFromDto shipFromDto = new ShipFromDto();
            shipFromDto.setCountry(countryCode);
            //cot pickup_id cua boxme co the la Interger hoac la 1 array, fix = cach chuyen ca VN va ID sang Array
            //Indo co dang 126914|126918
            List<Integer> pickupIds = convertToArrayPickup(deliveryDto.getGroupAddressId());
            if (pickupIds.isEmpty()) {
                logger.error("FAIL TO CONVERT WAREHOUSE PARTNER - SYS");
                orderResult.setErrMessage("FAIL TO CONVERT WAREHOUSE PARTNER");
                lst.add(orderResult);
                return lst;
            }

            logger.info("FAIL TO CONVERT WAREHOUSE PARTNER - SYS ------2");
            shipFromDto.setPickup_id(pickupIds);
            //shipFromDto.setPickup_id(Integer.parseInt(deliveryDto.getGroupAddressId()));
            requestDto.setShip_from(shipFromDto);

            ShipToDto shipToDto = new ShipToDto();
            shipToDto.setAddress(deliveryDto.getAddress());
            shipToDto.setContact_name(deliveryDto.getReceiveName());
            shipToDto.setPhone(deliveryDto.getPhone());
            shipToDto.setPhone2(deliveryDto.getPhone2());
            shipToDto.setCountry(countryCode);
            //TODO xxxxx

            shipToDto.setDistrict(Integer.parseInt(deliveryDto.getWardsCode()));
            shipToDto.setProvince(Integer.parseInt(deliveryDto.getProvinceCode()));
            shipToDto.setZipcode(deliveryDto.getrZipCode());
            requestDto.setShip_to(shipToDto);

            ShipmentDto shipmentDto = new ShipmentDto();
            if (products.size() > 0)
                //shipmentDto.setContent(products.get(0).getProName());
                shipmentDto.setContent(deliveryDto.getPackageName());
            else
                shipmentDto.setContent("No Product name");

            shipmentDto.setTotal_amount(deliveryDto.getCod_money());
            //TODO
            shipmentDto.setTotal_parcel(1);
            shipmentDto.setChargeable_weight(10);
            shipmentDto.setDescription(deliveryDto.getNote());

            List<ParacelDto> paracelDtoList = new ArrayList();
            ParacelDto paracelDto = new ParacelDto();
            paracelDto.setWeight(10);
            paracelDto.setDescription(deliveryDto.getNote());
            List<ItemsDto> itemsDtoList = new ArrayList();
            for (int i = 0; i < products.size(); i++) {
                ProductDto pd = products.get(i);
                ItemsDto itemsDto = new ItemsDto();
                itemsDto.setSku(pd.getProPartnerCode());
                itemsDto.setOrigin_country(countryCode);
                itemsDto.setName(pd.getProName());
                itemsDto.setDesciption(pd.getProName());
                itemsDto.setWeight(10);
                itemsDto.setValue(pd.getPrice().intValue());
                itemsDto.setQuantity(pd.getQty());
                itemsDtoList.add(itemsDto);
            }
            paracelDto.setItems(itemsDtoList);
            paracelDtoList.add(paracelDto);

            shipmentDto.setParcels(paracelDtoList);

            requestDto.setShipments(shipmentDto);

            ConfigDto configDto = new ConfigDto();
            configDto.setCurrency("IDR");
            configDto.setUnit_metric("metric");
            configDto.setDocument("Y");
            configDto.setInsurance("N");
            configDto.setReturn_mode(1);
            configDto.setAuto_approve("Y");
            configDto.setSort_mode("best_rate");//best_price: giao cham, best_rate
            configDto.setDelivery_service("");//su dung giao hang cua BOXME
            configDto.setOrder_type("fulfill");
            requestDto.setConfig(configDto);

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setFee_paid_by("sender");
            paymentDto.setCod_amount(deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue() ? deliveryDto.getCod_money() : 0);
           /* //boxme khong thu ho tien nen COD = 0
            paymentDto.setCod_amount(0);*/
            requestDto.setPayment(paymentDto);

            ReferralDto referralDto = new ReferralDto();
            referralDto.setOrder_number(deliveryDto.getDoCode());
            requestDto.setReferral(referralDto);

            logger.info("{} {} BOXME {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(requestDto));
            BoxmeCreateOrderResponse createOrderResponse = (BoxmeCreateOrderResponse) service.CreateOrder(requestDto);
            logger.info("{} {}message: {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), createOrderResponse.getMessages());


            if (!createOrderResponse.isError()) {
                logger.info("{} {}BOXME success result ", deliveryDto.getSESSION_ID(), deliveryDto.getDoId());
                orderResult.setResult(Const.DO_SUCCESS);
                orderResult.setMessage(createOrderResponse.getData().getTracking_number());
                orderResult.setCode("");
                lst.add(orderResult);
                return lst;
            }
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("BOXME");
            orderResult.setErrMessage(LogHelper.toJson(createOrderResponse));
            orderResult.setCode(createOrderResponse.getErrorCode());
            lst.add(orderResult);
        }

        return lst;
    }

    /**
     * create order for boxme thailand, just create order in warehouse
     *
     * @param deliveryDto
     * @param products
     * @return
     */
    private static List<OrderResult> CreateBoxmeThailandDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lst = new ArrayList<>();
        String countryCode = "TH";
        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.BM_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);
        logger.info("FAIL TO CONVERT WAREHOUSE PARTNER - SYS 4");
        String pickUpId = AppProperties.getPropertyValue(Helper.getKeyConfigByOrg(deliveryDto.getOrgId(), "api.BOXME.pickupid"));
        logger.info("FAIL TO CONVERT WAREHOUSE PARTNER - SYS 5");
        {
            //2. Create Boxme order
            BoxmeOrderServiceImpl service = new BoxmeOrderServiceImpl();

            BoxmeCreateOrderRequestDto requestDto = new BoxmeCreateOrderRequestDto();
            //tracking dhl

            ShipFromDto shipFromDto = new ShipFromDto();
            shipFromDto.setCountry(countryCode);
            //cot pickup_id cua boxme co the la Interger hoac la 1 array, fix = cach chuyen ca VN va ID sang Array
            //Indo co dang 126914|126918
            List<Integer> pickupIds = convertToArrayPickup(deliveryDto.getGroupAddressId());
            if (pickupIds.isEmpty()) {
                logger.error("FAIL TO CONVERT WAREHOUSE PARTNER - SYS");
                orderResult.setErrMessage("FAIL TO CONVERT WAREHOUSE PARTNER");
                lst.add(orderResult);
                return lst;
            }

            logger.info("FAIL TO CONVERT WAREHOUSE PARTNER - SYS ------2");
            shipFromDto.setPickup_id(pickupIds);
            //shipFromDto.setPickup_id(Integer.parseInt(deliveryDto.getGroupAddressId()));
            requestDto.setShip_from(shipFromDto);

            ShipToDto shipToDto = new ShipToDto();
            shipToDto.setAddress(deliveryDto.getAddress());
            shipToDto.setContact_name(deliveryDto.getReceiveName());
            shipToDto.setPhone(deliveryDto.getPhone());
            shipToDto.setPhone2(deliveryDto.getPhone2());
            shipToDto.setCountry(countryCode);
            //TODO xxxxx

            /*shipToDto.setDistrict(Integer.parseInt(deliveryDto.getWardsCode()));
            shipToDto.setProvince(Integer.parseInt(deliveryDto.getProvinceCode()));*/
            shipToDto.setDistrict(deliveryDto.getReicvWhDistrictId());
            shipToDto.setProvince(deliveryDto.getReicvWhProvinceId());
            shipToDto.setZipcode(deliveryDto.getrZipCode());
            requestDto.setShip_to(shipToDto);

            ShipmentDto shipmentDto = new ShipmentDto();
            if (products.size() > 0)
                //shipmentDto.setContent(products.get(0).getProName());
                shipmentDto.setContent(deliveryDto.getPackageName());
            else
                shipmentDto.setContent("No Product name");

            shipmentDto.setTotal_amount(deliveryDto.getCod_money());
            //TODO
            shipmentDto.setTotal_parcel(1);
            shipmentDto.setChargeable_weight(10);
            shipmentDto.setDescription(deliveryDto.getNote());

            List<ParacelDto> paracelDtoList = new ArrayList();
            ParacelDto paracelDto = new ParacelDto();
            paracelDto.setWeight(10);
            paracelDto.setDescription(deliveryDto.getNote());
            List<ItemsDto> itemsDtoList = new ArrayList();
            for (int i = 0; i < products.size(); i++) {
                ProductDto pd = products.get(i);
                ItemsDto itemsDto = new ItemsDto();
                itemsDto.setSku(pd.getProPartnerCode());
                itemsDto.setOrigin_country(countryCode);
                itemsDto.setName(pd.getProName());
                itemsDto.setDesciption(pd.getProName());
                itemsDto.setWeight(10);
                itemsDto.setValue(pd.getPrice().intValue());
                itemsDto.setQuantity(pd.getQty());
                itemsDtoList.add(itemsDto);
            }
            paracelDto.setItems(itemsDtoList);
            paracelDtoList.add(paracelDto);

            shipmentDto.setParcels(paracelDtoList);

            requestDto.setShipments(shipmentDto);

            ConfigDto configDto = new ConfigDto();
            configDto.setCurrency("THB");
            configDto.setUnit_metric("metric");
            configDto.setDocument("Y");
            configDto.setInsurance("N");
            configDto.setReturn_mode(1);
            configDto.setAuto_approve("Y");
            configDto.setSort_mode("best_price");//best_price: giao cham, best_rate
            if (deliveryDto.getLastmileId() == Const.DHL_TH_PARTNER_ID) {
                configDto.setDelivery_service(Const.DHL_TH_PARTNER_CODE);
            } else if (deliveryDto.getLastmileId() == Const.KERRY_TH_PARTNER_ID) {
                configDto.setDelivery_service(Const.KERRY_TH_PARTNER_CODE);
            } else if (deliveryDto.getLastmileId() == Const.TRUE_TH_PARTNER_ID) {
                configDto.setDelivery_service(Const.TRUE_TH_PARTNER_CODE);
            } else {
                configDto.setDelivery_service("");//su dung giao hang cua BOXME
            }
            configDto.setOrder_type("fulfill");
            requestDto.setConfig(configDto);

            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setFee_paid_by("sender");
            paymentDto.setCod_amount(deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue() ? deliveryDto.getCod_money() : 0);
           /* //boxme khong thu ho tien nen COD = 0
            paymentDto.setCod_amount(0);*/
            requestDto.setPayment(paymentDto);

            ReferralDto referralDto = new ReferralDto();
            referralDto.setOrder_number(deliveryDto.getDoCode());
            requestDto.setReferral(referralDto);

            logger.info("{} {} BOXME {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(requestDto));
            BoxmeCreateOrderResponse createOrderResponse = (BoxmeCreateOrderResponse) service.CreateOrder(requestDto);
            logger.info("{} {}message: {}\r\nResponse: {}", deliveryDto.getSESSION_ID(),
                    deliveryDto.getDoId(), createOrderResponse.getMessages(), LogHelper.toJson(createOrderResponse));


            if (!createOrderResponse.isError()) {
                logger.info(deliveryDto.getSESSION_ID() + " " + deliveryDto.getDoId() + "BOXME success result ");
                orderResult.setResult(Const.DO_SUCCESS);
                orderResult.setMessage(createOrderResponse.getData().getTracking_number());
                orderResult.setCode("");
                lst.add(orderResult);
                return lst;
            }
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("BOXME");
            orderResult.setErrMessage(LogHelper.toJson(createOrderResponse));
            orderResult.setCode(createOrderResponse.getErrorCode());
            lst.add(orderResult);
        } /*else {
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("BOXME");
            orderResult.setErrMessage(LogHelper.toJson(createOrderResponse));
            orderResult.setCode(createOrderResponse.getErrorCode());
            lst.add(orderResult);
        }*/

        return lst;
    }
    /* comment - not use
    private static void CreateGHNDelivery() {
        GhnCreateOrderRequestDto createOrderRequestDto = new GhnCreateOrderRequestDto();
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
        //GhnCreateOrderResponse createOrderResponseDto = ghnOrderService.createOrder(GHNConstant.hostName, null,
        //        EnumType.GHN.GHN_CREATE_DO.getValue(), createOrderRequestDto);
        GhnCreateOrderResponse createOrderResponseDto = (GhnCreateOrderResponse) ghnOrderService.CreateOrder(createOrderRequestDto);
    }
    */

    private static OrderResult CreateVTDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.VT_PARTNER_CODE);

        VtOrderServiceImpl service = new VtOrderServiceImpl();
        VtCreateOrderRequestDto createOrderDto = new VtCreateOrderRequestDto();
        //TODO need to change
        createOrderDto.setORDER_NUMBER(deliveryDto.getDoCode());
        createOrderDto.setRECEIVER_FULLNAME((deliveryDto.getReceiveName()));
        createOrderDto.setRECEIVER_PROVINCE(deliveryDto.getProvinceId());
        createOrderDto.setRECEIVER_DISTRICT(deliveryDto.getDistrictId());
//        createOrderDto.setRECEIVER_WARD(deliveryDto.getWardsId());
        createOrderDto.setRECEIVER_PHONE(deliveryDto.getPhone());
        createOrderDto.setRECEIVER_ADDRESS((deliveryDto.getAddress()));
        createOrderDto.setORDER_NOTE((deliveryDto.getNote()));

       /* createOrderDto.setSENDER_DISTRICT(595);
        createOrderDto.setSENDER_WARD(10205);
        createOrderDto.setSENDER_PROVINCE(53);*/
        createOrderDto.setSENDER_DISTRICT(deliveryDto.getsDistrictId());
        createOrderDto.setSENDER_WARD(deliveryDto.getsWardsId());
        createOrderDto.setSENDER_PROVINCE(deliveryDto.getsProvinceId());

        createOrderDto.setORDER_PAYMENT(3);
        createOrderDto.setPRODUCT_TYPE("HH");
        createOrderDto.setORDER_SERVICE("VBS");
//        createOrderDto.setGROUPADDRESS_ID(5818802);
//        createOrderDto.setGROUPADDRESS_ID(6655047);
        createOrderDto.setGROUPADDRESS_ID(Integer.parseInt(deliveryDto.getGroupAddressId()));
        createOrderDto.setCUS_ID(Const.VT_EKIWI_CUS_ID);
        createOrderDto.setPRODUCT_NAME(deliveryDto.getPackageName());
        createOrderDto.setPRODUCT_QUANTITY(1);

        int total = 0;
        List<VtProduct> lst = new ArrayList<>();
        for (ProductDto product : products) {

            VtProduct vtProduct = new VtProduct();

            ProductDto productDto = product;
            logger.info("PDO PRO--------------------------------------{}", productDto.getProName());
            vtProduct.setPRODUCT_PRICE(productDto.getPrice());
            vtProduct.setPRODUCT_NAME(productDto.getProName());
            vtProduct.setPRODUCT_WEIGHT(productDto.getWeight());
            vtProduct.setPRODUCT_QUANTITY(productDto.getQty());
            total += productDto.getPrice() * productDto.getQty();
            lst.add(vtProduct);
        }
//        createOrderDto.setMONEY_FEECOD(Double.valueOf(deliveryDto.getAmount()));
        createOrderDto.setMONEY_COLLECTION(Double.valueOf(deliveryDto.getCod_money()));
        createOrderDto.setPRODUCT_PRICE(Double.valueOf(deliveryDto.getAmount()));
        createOrderDto.setLIST_ITEM(lst);
        ObjectMapper Obj = new ObjectMapper();
        try {
            // get Oraganisation object as a json string
            String jsonStr = Obj.writeValueAsString(createOrderDto);

            // Displaying JSON String
            logger.info(jsonStr);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            logger.info("[CREATE ORDER VT] " + deliveryDto.getDoId());
            VtCreateOrderResponse vtCreateOrderResponse = (VtCreateOrderResponse) service.CreateOrder(createOrderDto);
            orderResult.setResult(Const.DO_SUCCESS);
            orderResult.setCode(vtCreateOrderResponse.getStatus().toString());
            orderResult.setMessage(vtCreateOrderResponse.getData().getORDER_NUMBER());
            return orderResult;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            orderResult.setMessage(e.getMessage());
        }
        orderResult.setCode("VT");
        orderResult.setResult(Const.DO_ERROR);
        return orderResult;
    }

    //Default return URL label
    private static OrderResult CreateDHLDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        return CreateDHLDelivery(deliveryDto, products, EnumType.DHL_LABEL_TYPE.URL.getValue());
    }

    private static OrderResult CreateDHLDelivery(DeliveryDto deliveryDto, List<ProductDto> products, String labelType) {

        if (!DHL_TOKEN_MAPS.containsKey(deliveryDto.getOrgId()) || !DHL_EXPIRE_AT_MAPS.containsKey(deliveryDto.getOrgId()) || DHL_EXPIRE_AT_MAPS.get(deliveryDto.getOrgId()).before(new Date())) {
            getDHLToken(deliveryDto.getOrgId());
            logger.info("<<<< DHL token get new for " + deliveryDto.getOrgId());
            if (DHL_TOKEN_MAPS == null || !DHL_TOKEN_MAPS.containsKey(deliveryDto.getOrgId()))
                return null;
        } else {
            logger.info(">>>> DHL token still valid for " + deliveryDto.getOrgId());
        }

        DHLOrderServiceImpl dhlservice = new DHLOrderServiceImpl();
        com.tms.ff.dto.request.DHL.shipment.DHLCreateLabelRequestDto dhlCreateOrderRequestDto = new com.tms.ff.dto.request.DHL.shipment.DHLCreateLabelRequestDto();
        com.tms.ff.dto.request.DHL.shipment.DHLLabelRequest mainfest = new com.tms.ff.dto.request.DHL.shipment.DHLLabelRequest();
        dhlCreateOrderRequestDto.setLabelRequest(mainfest);

        HdrRequest hdrRequest = new HdrRequest();
        hdrRequest.setMessageLanguage("en");
        hdrRequest.setMessageVersion("1.4");
        hdrRequest.setMessageType("LABEL");
        //TODO need accesstoken
        //hdrRequest.setAccessToken("9624676ea5914144af6b5eb2682adce8");
        hdrRequest.setAccessToken(DHL_TOKEN_MAPS.get(deliveryDto.getOrgId()));

        mainfest.setHdr(hdrRequest);
        com.tms.ff.dto.request.DHL.shipment.BodyLabel body = new com.tms.ff.dto.request.DHL.shipment.BodyLabel();
        mainfest.setBd(body);
        body.setInlineLabelReturn(labelType);
        body.setPickupAccountId(deliveryDto.getPickupId());
        body.setSoldToAccountId(deliveryDto.getSoldToAccountId());
        body.setHandoverMethod(2);
        com.tms.ff.dto.request.DHL.shipment.Label label = new com.tms.ff.dto.request.DHL.shipment.Label();
        label.setFormat("PNG");
        label.setLayout("1x1");
        label.setPageSize("400x600");
        body.setLabel(label);

        Address pickup = new Address();
        pickup.setName(deliveryDto.getPickupName());
//        pickup.setCity("Hà Nội");
        pickup.setDistrict(deliveryDto.getsDistrictName());
        pickup.setAddress1(deliveryDto.getsAddress());
        pickup.setState(deliveryDto.getsProvinceName());
        body.setShipperAddress(pickup);
        //body.setPickupDateTime();
/*
        Address shipper = new Address();
        shipper.setAddress1("3106 VP6 Hoang Liet Hoang Mai");
        shipper.setCity("Hà Nội");
        shipper.setName("Đinh ANh Thái");
        shipper.setPhone("0985787096");*/
        // body.setShipperAddress(shipper);

        ConsigneeAddress consigneeAddress = new ConsigneeAddress();
        consigneeAddress.setName(deliveryDto.getReceiveName());
        consigneeAddress.setPhone(deliveryDto.getPhone());
        consigneeAddress.setAddress1(deliveryDto.getAddress());
        //cho ghi chu vao address 3 de co the hien thi thong tin len nhan (Duy Ngo - DHL -> confirm 21/08/2019)
        consigneeAddress.setAddress3(" (THỰC PHẨM CHỨC NĂNG)");
//        consigneeAddress.setCity("Hà Nội");
        consigneeAddress.setDistrict(deliveryDto.getDistrictName());
        consigneeAddress.setState(deliveryDto.getProvinceName());
        /*try {
            consigneeAddress.setState(DHLHelper.convertToUTF8("Hồ Chí Minh"));
            consigneeAddress.setDistrict(DHLHelper.convertToUTF8("Quận 1"));
        }catch (Exception e){
            e.printStackTrace();
        }*/
//        consigneeAddress.setPostCode("100000");
//        consigneeAddress.setIdNumber("125281467");
//        consigneeAddress.setIdType("4");

        Items items = new Items();
        items.setConsigneeAddress(consigneeAddress);
//        -------------------
        //TODO need to change doid
        items.setShipmentID(DHLHelper.createShipmmentId(deliveryDto.getDoCode()));
        items.setPackageDesc(deliveryDto.getPackageName());
        items.setProductCode(DHLConst.DHL_SERVICE_CODE);
        //TODO product dimision
        items.setTotalWeight(100);
        items.setHeight(10D);
        items.setWidth(10D);
        items.setLength(10D);
        //items.setProductCode(DHLConst);
        Double total = (deliveryDto.getCod_money() != null ? Double.valueOf(deliveryDto.getCod_money()) : 10000D);
        //items.setTotalValue(total);
        items.setCodValue((deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) ? total : null);

        //Added service DHL
        AddedService addedService = new AddedService();
        com.tms.ff.dto.request.DHL.shipment.Service vasService = new com.tms.ff.dto.request.DHL.shipment.Service();
        vasService.setVasCode(DHLConst.DHL_ADDED_SERVICE_CODE);
        List<com.tms.ff.dto.request.DHL.shipment.Service> lstService = new ArrayList<>();
        lstService.add(vasService);
        addedService.setValueAddedService(lstService);

        items.setValueAddedServices(addedService);
        items.setReturnMode("02");


        List<Items> lstItems = new ArrayList<>();
        lstItems.add(items);
        body.setShipmentItems(lstItems);

        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.WFS_DHL_PARTNER_CODE);
        try {
            logger.info("{} {}[CREATE ORDER DHL] {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(dhlCreateOrderRequestDto));
            com.tms.ff.dto.response.DHL.DHLCreateLabelResponse response = dhlservice.CreateLabel(dhlCreateOrderRequestDto, "123");
            logger.info("{} {}{}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(response));
            if (response.getLabelResponse().getBd().getResponseStatus().getCode().equals("200")) {
                orderResult.setResult(Const.DO_SUCCESS);
                orderResult.setCode(response.getLabelResponse().getBd().getResponseStatus().getCode());
                orderResult.setMessage(response.getLabelResponse().getBd().getLabels().get(0).getDeliveryConfirmationNo());

                if (labelType.equals(EnumType.DHL_LABEL_TYPE.URL.getValue())) {
                    orderResult.setLabel(response.getLabelResponse().getBd().getLabels().get(0).getLabelURL());
                } else
                    orderResult.setLabel(response.getLabelResponse().getBd().getLabels().get(0).getContent());
            } else {
                orderResult.setErrMessage(LogHelper.toJson(response));
                orderResult.setMessage("ERROR when compare code");
                orderResult.setCode(response.getLabelResponse().getBd().getResponseStatus().getCode());
                orderResult.setResult(Const.DO_ERROR);
            }
            return orderResult;
        } catch (Exception e) {
            logger.error("{} {}[DHL]{}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), e.getMessage(), e);
            orderResult.setMessage(e.getMessage());
        }
        orderResult.setCode("DHL");
        orderResult.setMessage("ERROR when create DHL");
        orderResult.setResult(Const.DO_ERROR);
        return orderResult;
    }

    private static MyCloudFfmServiceImpl myCloudService = new MyCloudFfmServiceImpl();

    private static List<OrderResult> CreateMyCloudDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lst = new ArrayList<>();
        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.MYCLOUD_PARTNER_CODE);
        //neu khong co dia chi (address) hoac zipcode thi chuyen don DO sang pending
        if (StringUtils.isEmpty(deliveryDto.getAddress()) || StringUtils.isEmpty(deliveryDto.getsZipCode())) {
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("MYCLOUD");
            orderResult.setErrMessage(String.format("No Zipcode found: partnerId=%s (%s) districtId=%s", deliveryDto.getPartnerId(), deliveryDto.getWardsId()));
            orderResult.setCode("");
            lst.add(orderResult);
            return lst;
        }

        //2. Create MyCloud order
        //2.1 create list products (order items)
        List<MyCloudCreateOrderItemDto> orderItems = new ArrayList<MyCloudCreateOrderItemDto>();
        for (ProductDto product : products) {
            MyCloudCreateOrderItemDto orderItem = new MyCloudCreateOrderItemDto(product.getProPartnerCode(), product.getPrice().toString(), product.getQty().toString());
            orderItems.add(orderItem);
        }
        //2.2 Choose Delivery Mode Id (shipper)
        int deliveryModeId = DELIVERY_MODE.DEFAULT.getId();
        if (deliveryDto.getLastmileId() == Const.ALPHA_LM_PARTNER_ID)
            deliveryModeId = (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) ? DELIVERY_MODE.ALPHA_COD.getId() : DELIVERY_MODE.ALPHA.getId();
        else if (deliveryDto.getLastmileId() == Const.SCG_EXPRESS_LM_PARTNER_ID)
            deliveryModeId = (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) ? DELIVERY_MODE.SCG_EXPRESS_COD.getId() : DELIVERY_MODE.SCG_EXPRESS.getId();
        else if (deliveryDto.getLastmileId() == Const.DHL_TH_PARTNER_ID)
            deliveryModeId = (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) ? DELIVERY_MODE.DHL_COD.getId() : DELIVERY_MODE.DHL.getId();
        else if (deliveryDto.getLastmileId() == Const.KERRY_TH_PARTNER_ID)
            deliveryModeId = (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) ? DELIVERY_MODE.KERRY_COD.getId() : DELIVERY_MODE.KERRY.getId();
        else if (deliveryDto.getLastmileId() == Const.FLASH_TH_PARTNER_ID)
            deliveryModeId = (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) ? DELIVERY_MODE.FLASH_COD.getId() : DELIVERY_MODE.FLASH.getId();

        //2.3 create order delivery
        MyCloudCreateOrderDto createOrderDto = new MyCloudCreateOrderDto(deliveryModeId, orderItems, deliveryDto.getDoCode(), ORDER_STATUS.APPROVED.getName());
        createOrderDto.setInterfaceType("INTERFACE_TYPE");
        createOrderDto.setTotalPrice(deliveryDto.getAmount());
        createOrderDto.setName(deliveryDto.getReceiveName());
        createOrderDto.setAddress(deliveryDto.getAddress());
        createOrderDto.setPhoneNumber(deliveryDto.getPhone());
        createOrderDto.setPostcode(deliveryDto.getsZipCode()); //get subdistrict zipcode mapping with partner (lc_subdistrict_map.sdt_code)
        createOrderDto.setNote(deliveryDto.getNote());

        FlashShippingResponse flashShippingInfoDto = null;

        if (deliveryDto.getLastmileId() == Const.FLASH_TH_PARTNER_ID) {
            flashShippingInfoDto = FlashHelper.createShippingInfo(String.valueOf(deliveryDto.getDoCode()), deliveryDto, products, orderResult);

            if(Const.DO_SUCCESS.equals(orderResult.getResult())) {
                final String pno = flashShippingInfoDto.getData().getPno();
                File smallPrint = FlashHelper.getSmallLabel(pno);
                createOrderDto.setAttachName(new String[]{pno});
                createOrderDto.setAttachFile(new File[]{smallPrint});
            } else {
                lst.add(orderResult);
                return lst;
            }
        }
        logger.info("{} {} MyCloud {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(createOrderDto));
        MyCloudCreateOrderResponse createOrderResponse = (MyCloudCreateOrderResponse) myCloudService.CreateOrder(createOrderDto);
        logger.info("{} {} MyCloud message: {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), createOrderResponse.getMessage());

        //create order successful
        if (createOrderResponse != null && createOrderResponse.isSuccess()) {
            if (deliveryDto.getLastmileId() == Const.DHL_TH_PARTNER_ID) {
                ManifestRequest manifestRequest = new ManifestRequest();
                DHLRequestTh dhlRequestTh = new DHLRequestTh();
                HdrTh hdr = new HdrTh();
                BdTh bd = new BdTh();
                Address pickupAddress = new Address();
                Address consigneeAddress = new Address();
                Address returnAddress = new Address();

                //Header
                //Constructor assign value from DHLConst
                dhlRequestTh.setHdr(hdr);

                //Body
                //Constructor assign value from DHLConst
                //Pickup Address
                pickupAddress.setAddress1(deliveryDto.getsAddress());
                pickupAddress.setAddress2("");
                pickupAddress.setAddress3("");
                pickupAddress.setCity("-");
                pickupAddress.setDistrict(deliveryDto.getsDistrictName());
                pickupAddress.setPostCode(deliveryDto.getsWardCode());
                pickupAddress.setCountry("TH");
                pickupAddress.setName(deliveryDto.getPickupName());
                pickupAddress.setState(deliveryDto.getsProvinceName());
                pickupAddress.setPhone(deliveryDto.getsPhone());
                pickupAddress.setEmail("hello@mycloudfulfillment.com");
                bd.setPickupAddress(pickupAddress);
                //Shipment Items
                List<ItemsTh> shipmentItems = new ArrayList<>();
                ItemsTh items = new ItemsTh();
                items.setShipmentID(createOrderResponse.getData().getAttributes().getMcNumber());
                items.setTotalWeight(8);
                if (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) {
                    items.setCodValue(deliveryDto.getCod_money().doubleValue());
                } else {
                    items.setCodValue(null);
                }
                //Shipment Consignee Address
                consigneeAddress.setAddress1(deliveryDto.getAddress());
                consigneeAddress.setAddress2("");
                consigneeAddress.setAddress3("");
                consigneeAddress.setCity("-");
                consigneeAddress.setDistrict(deliveryDto.getDistrictName());
                consigneeAddress.setPostCode(deliveryDto.getsZipCode());
                consigneeAddress.setCountry("TH");
                consigneeAddress.setName(deliveryDto.getReceiveName());
                consigneeAddress.setState(deliveryDto.getProvinceName());
                consigneeAddress.setPhone(deliveryDto.getPhone());
                consigneeAddress.setEmail(deliveryDto.getEmail());
                items.setConsigneeAddress(consigneeAddress);
                //Shipment Return Address
                returnAddress.setName(deliveryDto.getPickupName());
                returnAddress.setAddress1(deliveryDto.getsAddress());
                returnAddress.setCity("-");
                returnAddress.setDistrict(deliveryDto.getsDistrictName());
                returnAddress.setCountry("TH");
                returnAddress.setPostCode(deliveryDto.getsWardCode());
                returnAddress.setPhone(deliveryDto.getsPhone());
                returnAddress.setEmail("hello@mycloudfulfillment.com");
                items.setReturnAddress(returnAddress);
                shipmentItems.add(items);
                bd.setShipmentItems(shipmentItems);
                dhlRequestTh.setBd(bd);

                //Call API DHL Thailand at 3rd project
                manifestRequest.setManifestRequest(dhlRequestTh);
                try {
                    logger.info("Create order DHL {}", LogHelper.toJson(manifestRequest));
                    DHLCreateOrderResponse dhlCreateOrderResponse = myCloudService.CreateOrderDHLThailand(manifestRequest);
                    if (dhlCreateOrderResponse != null && dhlCreateOrderResponse.getManifestResponse().getBd().getResponseStatus().getCode().equals("200")) {
                        OrderResult orderResultDhl = new OrderResult();
                        orderResultDhl.setResult(Const.DO_SUCCESS);
                        orderResultDhl.setType(Const.DHL_TH_PARTNER_CODE);
                        orderResultDhl.setMessage(dhlCreateOrderResponse.getManifestResponse().getBd().getShipmentItems().get(0).getDeliveryConfirmationNo());
                        orderResultDhl.setCode("1");
                        lst.add(orderResultDhl);
                        logger.info("DHL Shipment Created{}", dhlCreateOrderResponse.getManifestResponse().getBd().getShipmentItems().get(0).getDeliveryConfirmationNo());
                    } else {
                        logger.info("Can not create DHL Shipment");
                        CancelMyCloudDelivery(createOrderResponse.getData().getAttributes().getMcNumber());
                        logger.info("Cancel MYC order!");
                        orderResult.setResult(Const.DO_ERROR);
                        orderResult.setMessage("DHL");
                        orderResult.setErrMessage(String.format("ERROR when create DHL order|%s", LogHelper.toJson(dhlCreateOrderResponse)));
                        lst.add(orderResult);
                        return lst;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    CancelMyCloudDelivery(createOrderResponse.getData().getAttributes().getMcNumber());
                    logger.info("Cancel MYC order!");
                    orderResult.setErrMessage(e.getMessage());
                    orderResult.setResult(Const.DO_ERROR);
                    lst.add(orderResult);
                    return lst;
                }
            } else if (deliveryDto.getLastmileId() == Const.KERRY_TH_PARTNER_ID) {
                orderResult.setType(Const.KERRY_TH_PARTNER_CODE);
                orderResult.setResult(Const.DO_ERROR);
                KerryThShippingServiceImpl shippingService = new KerryThShippingServiceImpl();
                //1. Create KERRY label
                String conNo = createOrderResponse.getData().getAttributes().getMcNumber();
                KerryShippingInfoDto shippingInfoDto = KerryHelper.createShippingInfoPayload(conNo, deliveryDto, products);

                logger.info("Create Kerry TH. {}\r\n{} {}", conNo, deliveryDto.getSESSION_ID(), LogHelper.toJson(shippingInfoDto));
                KerryShippingResponse response = shippingService.getShippingInfo(shippingInfoDto);
                logger.info("{} {}", deliveryDto.getSESSION_ID(), LogHelper.toJson(response));
                if (response != null && response.getData().getShipment().getStatusCode().equals("000")) {
                    orderResult.setResult(Const.DO_SUCCESS);
                    orderResult.setCode("200");
                    orderResult.setMessage(conNo);
                    orderResult.setErrMessage(response.getData().getShipment().getStatusDesc());

                } else {
                    orderResult.setResult(Const.DO_ERROR);
                    orderResult.setMessage(conNo);
                    CancelMyCloudDelivery(createOrderResponse.getData().getAttributes().getMcNumber());
                    logger.info("Cancel MYC order!");
                    orderResult.setCode(response.getData().getShipment().getStatusCode());
                    orderResult.setErrMessage(response.getData().getShipment().getStatusDesc());
                }

                lst.add(orderResult);
                return lst;
            } else if (deliveryDto.getLastmileId() == Const.FLASH_TH_PARTNER_ID) {
                lst.add(orderResult);
                return lst;
            }
            orderResult.setType(Const.MYCLOUD_PARTNER_CODE);
            orderResult.setResult(Const.DO_SUCCESS);
            orderResult.setMessage(createOrderResponse.getData().getAttributes().getMcNumber());
            orderResult.setCode(String.valueOf(createOrderResponse.isSuccess()));
            lst.add(orderResult);
            return lst;
        }
        else {
            if(!ObjectUtils.isEmpty(flashShippingInfoDto) &&
                    !ObjectUtils.isEmpty(flashShippingInfoDto.getData()) &&
                    deliveryDto.getLastmileId() == Const.FLASH_TH_PARTNER_ID) {
                FlashHelper.cancel(flashShippingInfoDto.getData().getPno());
            }

            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("MYCLOUD");
            orderResult.setErrMessage(createOrderResponse.getMessage());
            orderResult.setCode("");
            lst.add(orderResult);
        }
        return lst;
    }

    /**
     * KERRY
     */
    private static List<OrderResult> CreateKerryDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lst = new ArrayList<>();

        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.KER_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);
        KerryShippingServiceImpl shippingService = new KerryShippingServiceImpl();
        //1. Create DHL label
        KerryShippingInfoDto shippingInfoDto = new KerryShippingInfoDto();

        KerryShipmentDto req = new KerryShipmentDto();
        KerryShipment shipment = new KerryShipment();
        String conNo = createIKWDoCode(deliveryDto.getDoId());
        shipment.setCon_no(conNo);
        shipment.setAction_code("A");
        shipment.setS_name("KIWI INDONESIA");
        shipment.setS_address("WH Jakarta - Cipayung 13880, Jl. Harapan II No.5, RT.2/RW.5, Setu, Kec. Cipayung, DKI Jakarta, Jakarta Timur");
        shipment.setS_mobile1("021 - 2280 6611");
        shipment.setS_subdistrict(deliveryDto.getsWardName());
        shipment.setS_district(deliveryDto.getsDistrictName());
        shipment.setS_province(deliveryDto.getsProvinceName());
        shipment.setS_zipcode("13880");
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

        shipment.setSpecial_note(deliveryDto.getPackageName());
        shipment.setService_code("D1");
        shipment.setRef_no(deliveryDto.getCustomerId());
        shipment.setR_zipcode(deliveryDto.getrZipCode());

        req.setShipment(shipment);
        shippingInfoDto.setReq(req);

        logger.info("{} {}", deliveryDto.getSESSION_ID(), LogHelper.toJson(shippingInfoDto));
        KerryShippingResponse response = shippingService.getShippingInfo(shippingInfoDto);
        if (response != null && response.getData().getShipment().getStatusCode().equals("000")) {
            orderResult.setResult(Const.DO_SUCCESS);
            orderResult.setCode("200");
            orderResult.setMessage(conNo);
            orderResult.setErrMessage(response.getData().getShipment().getStatusDesc());

        } else {
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage(conNo);
            orderResult.setCode(response.getData().getShipment().getStatusCode());
            orderResult.setErrMessage(response.getData().getShipment().getStatusDesc());
        }

        lst.add(orderResult);
        return lst;
    }

    private static int[] SAPpayMethods = {0, 2, 1};//2: COD, 1: Non COD

    /**
     * SAP
     * for cod  customer_code: CGK012795
     * for non cod  customer_code: CGK012796
     */
    private static List<OrderResult> CreateSAPDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
//        for cod  customer_code: CGK012795
//        for non cod  customer_code: CGK012796
        logger.info("--------------------- SAP ------------------------");
        List<OrderResult> lst = new ArrayList<>();

        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.SAP_PARTNER_CODE);
        orderResult.setResult(Const.DO_ERROR);
        SapOrderServiceImpl service = new SapOrderServiceImpl();
        SapCreateOrderRequestDto requestDto = new SapCreateOrderRequestDto();
        String ref_no = createIKWDoCode(deliveryDto.getDoId());
        requestDto.setReference_no(ref_no);

        requestDto.setPickup_name(!StringHelper.isNullOrEmpty(deliveryDto.getsName()) ? deliveryDto.getsName() : "wh sap express");
        requestDto.setPickup_address(!StringHelper.isNullOrEmpty(deliveryDto.getsAddress()) ? deliveryDto.getsAddress() : "jl komodor halim no 28");
        requestDto.setPickup_phone(!StringHelper.isNullOrEmpty(deliveryDto.getsPhone()) ? deliveryDto.getsPhone() : "021 - 2280 6611");
        requestDto.setPickup_district_code(!StringHelper.isNullOrEmpty(deliveryDto.getsDistrictName()) ? deliveryDto.getsDistrictName() : "JK000");
//        requestDto.setPickup_phone("");
        //requestDto.setPickup_postal_code(12430);
        requestDto.setPickup_postal_code(0);
        String serviceCode = ("ODS".equals(deliveryDto.getLastmileService()) ? "UDRONS" : "UDRREG");//ODS: one day service using UDRONS, normal using UDRREG
        requestDto.setService_type_code(serviceCode);
        requestDto.setQuantity(1);
        requestDto.setDescription_item(deliveryDto.getPackageName());
        int totalItem = 0;
        for (int i = 0; i < products.size(); i++) {
            totalItem += products.get(i).getQty();
        }
        requestDto.setTotal_item(totalItem);
        requestDto.setWeight(1);
        requestDto.setVolumetric("10x10x10");
        requestDto.setShipment_type_code("SHTPC");//package, if document use SHTDC
        requestDto.setShipment_content_code("SHTPC");//package, if document use SHTDC
        requestDto.setInsurance_flag(2);//2: include insurance, 1: non insurance
        if (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue())
            requestDto.setCod_flag(2);//2: COD, 1: Non COD
        else
            requestDto.setCod_flag(1);
//        requestDto.setCod_flag(SAPpayMethods[deliveryDto.getPaymentMethod()]);
        String codWarehouseCode = "CGK012795";
        String nCodWarehouseCode = "CGK012796";
        if (!StringHelper.isNullOrEmpty(deliveryDto.getGroupAddressId())) {
            String[] warehouseCode = deliveryDto.getGroupAddressId().trim().split("\\|");
            if (warehouseCode != null && warehouseCode.length == 2) {
                codWarehouseCode = warehouseCode[0];
                nCodWarehouseCode = warehouseCode[1];
            }
        }
        if (deliveryDto.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) {
            requestDto.setCustomer_code(codWarehouseCode);
            requestDto.setCod_value(Double.valueOf(deliveryDto.getCod_money().intValue()));
        } else {//non COD
            requestDto.setCustomer_code(nCodWarehouseCode);
            requestDto.setCod_value(0D);
        }
        //TODO need more
        requestDto.setShipper_name("PT Ekiwi Koneksi Perdagangan");
        requestDto.setShipper_address("Jl. TB Simatupang Kav.36, Jakarta 12430");
        requestDto.setShipper_phone("0812-9049-940");
        //only indo need using ward
        requestDto.setDestination_district_code(deliveryDto.getWardsCode());
        requestDto.setReceiver_name(deliveryDto.getReceiveName());
        requestDto.setReceiver_address(deliveryDto.getAddress());
        requestDto.setReceiver_phone(deliveryDto.getPhone());

        logger.info(LogHelper.toJson(requestDto));
        SapCreateOrderResponse response = (SapCreateOrderResponse) service.CreateOrder(requestDto);

        logger.info(LogHelper.toJson(response));
        try {
            if (response.getStatus().equals("success")) {
                orderResult.setCode("200");
                orderResult.setResult(Const.DO_SUCCESS);
                orderResult.setMessage(ref_no);
                orderResult.setErrMessage(response.getMsg());

            } else {
                orderResult.setResult(Const.DO_ERROR);
                orderResult.setCode(String.valueOf(response.getResponse()));
                orderResult.setErrMessage(response.getMsg());
            }
            lst.add(orderResult);
        } catch (Exception e) {
            logger.error("[ERROR] {} {}", ref_no, e.getMessage(), e);
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setErrMessage(ref_no + " " + e.getMessage());
            lst.add(orderResult);
        }
        return lst;
    }

    private static OrderResult CreateSnappyOrder(DeliveryDto deliveryDto, List<ProductDto> products){
        OrderResult orderResult = new OrderResult();
        SnappyCreateOrderRequest request = new SnappyCreateOrderRequest();
        SnappyOrderServiceImpl snappyOrderService = new SnappyOrderServiceImpl();
        SnappyLoginResponse response = snappyOrderService.getToken();
        List<SnappyBusinessesResponse>  buss = response.getBusinesses();
        List<SnappyAddressResponse> lstAddRes = buss.get(0).getAddresses();
        HashMap<String,UUID> map = new HashMap<>();

        for (int i = 0; i < lstAddRes.size(); i++) {
            SnappyAddressResponse addRes = lstAddRes.get(i);
            map.put(addRes.getName(),addRes.getId());
        }

        List<SnappyItemOrderRequest> listItemRequest = new ArrayList<>();

        for (ProductDto dto : products) {
            SnappyItemOrderRequest orderRequest = new SnappyItemOrderRequest();
            orderRequest.setName(dto.getProName());
            orderRequest.setWeight(dto.getWeight().intValue());
            orderRequest.setQuantity(dto.getQty());
            listItemRequest.add(orderRequest);
        }

        for (SnappyBusinessesResponse bus : response.getBusinesses()) {
            if (bus.getIsDefault())
                request.setBusinessId(bus.getId());
        }

        if (map.containsKey(deliveryDto.getWarehouseShortname())){
            request.setBusinessAddressId(map.get(deliveryDto.getWarehouseShortname()));
        }

        request.setCustomerTrackingId(deliveryDto.getDoCode());
        request.setItems(listItemRequest);
        request.setCod(deliveryDto.getCod_money());
        request.setValue(deliveryDto.getAmount());
        request.setAccessToken(response.getAccessToken());
        request.setReceiverName(deliveryDto.getReceiveName());
        request.setReceiverPhoneNumber(deliveryDto.getPhone());
        request.setReceiverAddress(deliveryDto.getAddress());
        request.setReceiverProvinceId(deliveryDto.getLmProvinceId().toString());
        request.setReceiverDistrictId(deliveryDto.getLmDistrictId().toString());
        request.setReceiverFullAddress(deliveryDto.getAddress());
        request.setNote(deliveryDto.getNote());
        request.setShopNote(deliveryDto.getNote());
        request.setDeliveryNote("Cho xem hàng, không cho thử");
        request.setIsAllowCheckingGood(true);

        String token = "";
        token = response.getAccessToken();
        try {
            orderResult.setType(Const.WFS_SNAPPY_PARTNER_CODE);
            SnappyCreateOrderResponse orderResponse = snappyOrderService.createOrder(request, token);

            if (orderResponse != null && orderResponse.getTracking() != null && orderResponse.getTracking().getId() != null) {
                orderResult.setResult(Const.DO_SUCCESS);
                orderResult.setCode("200");
                orderResult.setLabel("SUCCESS");
                orderResult.setMessage(orderResponse.getTracking().getId());
                return orderResult;
            } else {
                orderResult.setErrMessage(orderResponse.getMessage());
                orderResult.setMessage("ERROR when compare code SNAPPY");
                orderResult.setResult(Const.DO_ERROR);
                return orderResult;
            }
        } catch (Exception e) {
            logger.error("{} {}[SNAPPY]{}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), e.getMessage(), e);
            orderResult.setMessage(e.getMessage());
            orderResult.setResult(Const.DO_ERROR);
        }

        return orderResult;

    }


    /***
     * CANCEL DO
     ***/
    private static OrderResult CancelVTDelivery(String trackingId) {
        VtOrderServiceImpl service = new VtOrderServiceImpl();

        VtUpdateOrderDto updateOrderDto = new VtUpdateOrderDto();
        updateOrderDto.setORDER_NUMBER(trackingId);
        updateOrderDto.setNOTE("Cancel Order from TMS");
        VtUpdateOrderResponse vtUpdateOrderResponse = (VtUpdateOrderResponse) service.CancelOrder(updateOrderDto);
        logger.info(LogHelper.toJson(vtUpdateOrderResponse));

        OrderResult orderResult = new OrderResult();
        orderResult.setCode(vtUpdateOrderResponse.getStatus().toString());
        orderResult.setMessage(vtUpdateOrderResponse.getMessage());
        orderResult.setType(Const.VT_PARTNER_CODE);

        return orderResult;
    }

	private static OrderResult CancelNinjaVan(String trackingId) {
		OrderResult orderResult = new OrderResult();
		NinjaVanOrderServiceImpl service = new NinjaVanOrderServiceImpl(null);

		String token = "";

		token = getNinjaVanToken();
		 NinjaVanResponseCancerDto result = new NinjaVanResponseCancerDto();
		result = service.cancerOrder(token, trackingId);
		orderResult.setCode("200");
		return orderResult;

	}

    private static OrderResult CancelDHLDelivery(String shipmentId, Integer orgId) {
        if (!DHL_TOKEN_MAPS.containsKey(orgId) || DHL_EXPIRE_AT_MAPS.containsKey(orgId) || DHL_EXPIRE_AT_MAPS.get(orgId).before(new Date())) {
            getDHLToken(orgId);
            logger.info("<<<< DHL token get new for {}", orgId);
        } else {
            logger.info(">>>> DHL token still valid {}", orgId);
        }
        DHLOrderServiceImpl dhlService = new DHLOrderServiceImpl();

        DHLDeleteLabelRequestDto dhlDeleteLabelRequestDto = createDhlDeleteLabelRequestDto(shipmentId, orgId);

        logger.info("CANCEL DHL REQ: {} {}", shipmentId, LogHelper.toJson(dhlDeleteLabelRequestDto));
        DHLDeleteLabelResponse response = dhlService.CancelOrder(dhlDeleteLabelRequestDto);
        logger.info("CANCEL DHL RES: {} {}", shipmentId, LogHelper.toJson(response));

        OrderResult orderResult = new OrderResult();
        if (response == null) {
            orderResult.setResult(Const.FAIL_GET_RESPONSE_FROM_3PL);
            return orderResult;
        }

        if (response.getDeleteShipmentResp().getBd().getResponseStatus().getCode().equals("200"))
            orderResult.setResult(Const.DO_CANCEL_SUCCESS);
        else
            orderResult.setResult(Const.DO_CANCEL_ERROR);

        orderResult.setCode(response.getDeleteShipmentResp().getBd().getResponseStatus().getCode());
        orderResult.setMessage(response.getDeleteShipmentResp().getBd().getShipmentItems().get(0).getResponseStatus().getMessageDetails().get(0).getMessageDetail());
        orderResult.setType(Const.WFS_DHL_PARTNER_CODE);
        return orderResult;
    }

    private static DHLDeleteLabelRequestDto createDhlDeleteLabelRequestDto(String shipmentId, Integer orgId) {
        DHLDeleteLabelRequestDto dhlDeleteLabelRequestDto = new DHLDeleteLabelRequestDto();
        DHLDeleteLabelRequest mainFest = new DHLDeleteLabelRequest();

        HdrRequest hdr = new HdrRequest();
        hdr.setMessageLanguage("en");
        hdr.setMessageVersion("1.0");
        hdr.setMessageType("DELETESHIPMENT");
        hdr.setAccessToken(DHL_TOKEN_MAPS.get(orgId));

        DeleteItem deleteItem = new DeleteItem();
        deleteItem.setShipmentID(DHLHelper.createShipmmentId(shipmentId));
        List<DeleteItem> lstDelItem = new ArrayList<>();
        lstDelItem.add(deleteItem);

        BodyDeleteLabel bd = new BodyDeleteLabel();
        bd.setPickupAccountId(DHLConst.PICKUP_ACCOUNT);
        bd.setSoldToAccountId(DHLConst.SOLD_ACCOUNT);
        bd.setShipmentItems(lstDelItem);

        mainFest.setHdr(hdr);
        mainFest.setBd(bd);
        dhlDeleteLabelRequestDto.setDeleteShipmentReq(mainFest);

        return dhlDeleteLabelRequestDto;
    }

    private static OrderResult cancelDHLFFMDelivery(String doCode) {
        DHLFFMOrderServiceImpl dhlFFMOrderService = new DHLFFMOrderServiceImpl();

        DHLFFMCancelOrderRequestDto dhlFFMCancelOrderRequest = createDHLFFMCancelOrderRequest(doCode);

        logger.info("CANCEL DHL FFM REQ: {} {}", doCode, LogHelper.toJson(dhlFFMCancelOrderRequest));
        DHLFFMOrderResponse dhlFFMCancelOrderResponse = dhlFFMOrderService.cancelOrder(dhlFFMCancelOrderRequest);
        logger.info("CANCEL DHL FFM RES: {} {}", doCode, LogHelper.toJson(dhlFFMCancelOrderResponse));

        OrderResult orderResult = new OrderResult();
        if (dhlFFMCancelOrderResponse == null) {
            orderResult.setResult(Const.FAIL_GET_RESPONSE_FROM_3PL);
            return orderResult;
        }

        if (dhlFFMCancelOrderResponse.isSuccess())
            orderResult.setResult(Const.DO_CANCEL_SUCCESS);
        else
            orderResult.setResult(Const.DO_CANCEL_ERROR);

        orderResult.setMessage(LogHelper.toJson(dhlFFMCancelOrderResponse.getError()));
        orderResult.setType(Const.DHL_FFM_PARTNER_CODE);
        return orderResult;
    }

    private static DHLFFMCancelOrderRequestDto createDHLFFMCancelOrderRequest(String doCode) {
        DHLFFMCancelOrderRequestDto dhlFFMCancelOrderRequest = new DHLFFMCancelOrderRequestDto();
        dhlFFMCancelOrderRequest.setSaleOrderCode(doCode);
        dhlFFMCancelOrderRequest.setNote("CANCEL ORDER");

        return dhlFFMCancelOrderRequest;
    }

    private static OrderResult cancelWFSDelivery(String clientOrderCode, String warehouseCodeInPartner) {
        WfsOrderServiceImpl wfsOrderService = new WfsOrderServiceImpl();

        WfsCancelOrderV2RequestDto wfsCancelOrderV2Request = createWFSCancelOrderRequest(clientOrderCode, warehouseCodeInPartner);

        logger.info("CANCEL WFS REQ: {} {}", clientOrderCode, LogHelper.toJson(wfsCancelOrderV2Request));
        WfsCancelOrderV2Response wfsCancelOrderV2Response = wfsOrderService.cancelOrderV2(wfsCancelOrderV2Request);
        logger.info("CANCEL WFS RES: {} {}", clientOrderCode, LogHelper.toJson(wfsCancelOrderV2Response));

        OrderResult orderResult = new OrderResult();
        if (wfsCancelOrderV2Response == null) {
            orderResult.setResult(Const.FAIL_GET_RESPONSE_FROM_3PL);
            return orderResult;
        }

        if (wfsCancelOrderV2Response.getCode() == 0)
            orderResult.setResult(Const.DO_CANCEL_SUCCESS);
        else
            orderResult.setResult(Const.DO_CANCEL_ERROR);

        orderResult.setMessage(wfsCancelOrderV2Response.getMsg());
        orderResult.setType(Const.WFS_GHN_PARTNER_CODE);
        return orderResult;
    }

    private static WfsCancelOrderV2RequestDto createWFSCancelOrderRequest(String clientOrderCode, String warehouseCodeInPartner) {
        WfsCancelOrderV2RequestDto wfsCancelOrderV2Request = new WfsCancelOrderV2RequestDto();
        wfsCancelOrderV2Request.setClientOrderCode(clientOrderCode);
        wfsCancelOrderV2Request.setWarehouseId(warehouseCodeInPartner);

        return wfsCancelOrderV2Request;
    }

    private static OrderResult CancelWFSDelivery(String saleOrderId) {
        WfsAuthenticationServiceImpl service = new WfsAuthenticationServiceImpl();
        WfsOrderServiceImpl orderService = new WfsOrderServiceImpl();

        LoginDto loginRequestDto = new WfsLoginRequestDto();
        WfsLoginResponse login = (WfsLoginResponse) service.login(WfsConst.hostName, null, WfsConst.LOGIN_PATH, loginRequestDto);
        logger.info("Message: {}\r\nToken: {}", login.getMessage(), login.getData().getToken());

        WfsCancelOrderRequestDto wfsCancelOrderRequestDto = new WfsCancelOrderRequestDto();
        wfsCancelOrderRequestDto.setOrderId(saleOrderId);
        wfsCancelOrderRequestDto.setToken(login.getData().getToken());

        WfsCancelResponse response = orderService.CancelOrder(wfsCancelOrderRequestDto, login.getData().getToken());
        logger.info("CANCEL WFS] {} {}", saleOrderId, LogHelper.toJson(response));

        OrderResult orderResult = new OrderResult();
        if (response != null && response.getErrorCode() != null && response.getErrorCode().equals("0"))
            orderResult.setResult(Const.DO_SUCCESS);
        else
            orderResult.setResult(Const.DO_ERROR);

        orderResult.setCode(response.getErrorCode());
        orderResult.setMessage(response.getMessage());
        orderResult.setType(Const.WFS_GHN_PARTNER_CODE);

        return orderResult;
    }

    private static OrderResult CancelBoxmeDelivery(String bmCode) {
        BoxmeOrderServiceImpl service = new BoxmeOrderServiceImpl();
        BoxmeCancelOrderResponse cancelOrderResponse = service.CancelOrder(bmCode);
        logger.info("CANCEL BOXME: {} {}", bmCode, LogHelper.toJson(cancelOrderResponse));

        OrderResult orderResult = new OrderResult();
        if (cancelOrderResponse == null) {
            orderResult.setResult(Const.FAIL_GET_RESPONSE_FROM_3PL);
            return orderResult;
        }

        if (cancelOrderResponse.isError())
            orderResult.setResult(Const.DO_CANCEL_ERROR);
        else
            orderResult.setResult(Const.DO_CANCEL_SUCCESS);

        orderResult.setCode(cancelOrderResponse.getErrorCode());
        orderResult.setMessage(cancelOrderResponse.getMessages());
        orderResult.setType(Const.BM_PARTNER_CODE);

        return orderResult;
    }

    private static OrderResult CancelMyCloudDelivery(String myCode) {
        MyCloudCancelOrderResponse cancelOrderResponse = (MyCloudCancelOrderResponse) myCloudService.cancelOrder(myCode);
        logger.info("CANCEL MYCLOUD] {} {}", myCode, LogHelper.toJson(cancelOrderResponse));
        OrderResult orderResult = new OrderResult();
        if (cancelOrderResponse == null) {
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("Cannot cancel order=" + myCode);
        } else
            orderResult.setResult(Const.DO_SUCCESS);

        orderResult.setType(Const.MYCLOUD_PARTNER_CODE);

        return orderResult;
    }

    private static OrderResult CancelSnappy(String trackingId) {
        SnappyOrderServiceImpl service = new SnappyOrderServiceImpl();
        String token = service.getToken().getAccessToken();

        SnappyCancelOrderResponse snappyCancelOrderResponse = service.cancelOrder(trackingId, token);

        OrderResult orderResult = new OrderResult();
        if (snappyCancelOrderResponse.getSuccess())
            orderResult.setResult(Const.DO_CANCEL_SUCCESS);
        else
            orderResult.setResult(Const.DO_CANCEL_ERROR);

        orderResult.setMessage(snappyCancelOrderResponse.getMessage());
        orderResult.setType(Const.WFS_SNAPPY_PARTNER_CODE);

        return orderResult;

    }

    private static CancelOrderRequestDto createSAPCancelRequest(String trackingCode){
        CancelOrderRequestDto cancelOrderRequestDto = new CancelOrderRequestDto();
        cancelOrderRequestDto.setAwb_no(trackingCode);
        String strDate = DateHelper.toTMSGlobalDateTime(java.time.LocalDateTime.now());
        cancelOrderRequestDto.setCancel_date(strDate);
        cancelOrderRequestDto.setDesc("CANCEL ORDER");

        return cancelOrderRequestDto;
    }

    private static OrderResult CancelSAPDelivery(String trackingCode) {
        SapOrderServiceImpl service = new SapOrderServiceImpl();
        CancelOrderRequestDto requestCancelOrder = DeliveryHelper.createSAPCancelRequest(trackingCode);

        logger.info("CANCEL SAP REQ: {} {}", trackingCode, LogHelper.toJson(requestCancelOrder));
        SapCancelOrderResponse cancelOrderResponse = service.CancelOrder(requestCancelOrder);
        logger.info("CANCEL SAP RES: {} {}" + trackingCode, LogHelper.toJson(cancelOrderResponse));

        OrderResult orderResult = new OrderResult();
        if (cancelOrderResponse == null) {
            orderResult.setResult(Const.FAIL_GET_RESPONSE_FROM_3PL);
            return orderResult;
        }

        if (cancelOrderResponse.getResponse() == 200)
            orderResult.setResult(Const.DO_CANCEL_SUCCESS);
        else
            orderResult.setResult(Const.DO_CANCEL_ERROR);

        orderResult.setCode(String.valueOf(cancelOrderResponse.getResponse()));
        orderResult.setMessage(cancelOrderResponse.getMsg());
        orderResult.setType(Const.SAP_PARTNER_CODE);

        return orderResult;
    }

    /**
     * DO format Id
     *
     * @param doId
     * @return
     */
    private static String createDoformat(int doId) {
        //8 chu so, bao gom ca so 0, ex: 00033448 khi doId = 22448
        return String.format("%08d", doId);
    }

    /**
     * Indonesia create with 7 number
     *
     * @param doId
     * @return
     */
    private static String createIndoDOformat(int doId) {
        return String.format("%07d", doId);
    }

    private static String returnDateNowformat(String formatDate) {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(formatDate);
        String dateString = format.format(new Date());
        return dateString;
    }

    private static List<OrderResult> createOrderBoxmeWithVNP(DeliveryDto deliveryDto, List<ProductDto> products) {
        List<OrderResult> lst = new ArrayList<>();

        logger.info("Create Order Boxme with VNPost!");
        OrderResult orderResult = new OrderResult();
        orderResult.setType(Const.BM_EXPRESS_CODE);

        BoxmeOrderServiceImpl service = new BoxmeOrderServiceImpl();

        BoxmeCreateOrderRequestDto requestDto = new BoxmeCreateOrderRequestDto();
        ShipFromDto shipFromDto = new ShipFromDto();
        shipFromDto.setCountry("VN");
        //change from Integer to Array Integer, special case support by BOXME

        shipFromDto.setPickup_id(Arrays.asList(BoxmeConst.BOXME_PICKUP_ID));
        requestDto.setShip_from(shipFromDto);

        ShipToDto shipToDto = new ShipToDto();
        String address = deliveryDto.getAddress();
        if (address.length() > 150) {
            address = address.substring(0,149);
        }
        shipToDto.setAddress(address);
        shipToDto.setContact_name(deliveryDto.getReceiveName());
        shipToDto.setPhone(deliveryDto.getPhone());
        shipToDto.setPhone2(deliveryDto.getPhone2());
        shipToDto.setCountry("VN");
        //TODO

        shipToDto.setDistrict(deliveryDto.getReicvWhDistrictId());
        shipToDto.setProvince(deliveryDto.getReicvWhProvinceId());
        requestDto.setShip_to(shipToDto);

        ShipmentDto shipmentDto = new ShipmentDto();
        if (products.size() > 0)
            shipmentDto.setContent(products.get(0).getProName());
        else
            shipmentDto.setContent("No Product name");

        shipmentDto.setTotal_amount(deliveryDto.getCod_money());
        //TODO
        shipmentDto.setTotal_parcel(1);
        shipmentDto.setChargeable_weight(10);
        shipmentDto.setDescription(deliveryDto.getNote());

        List<ParacelDto> paracelDtoList = new ArrayList();
        ParacelDto paracelDto = new ParacelDto();
        paracelDto.setWeight(10);
        paracelDto.setDescription(deliveryDto.getNote());
        List<ItemsDto> itemsDtoList = new ArrayList();
        for (int i = 0; i < products.size(); i++) {
            ProductDto pd = products.get(i);
            ItemsDto itemsDto = new ItemsDto();
            itemsDto.setSku(pd.getProPartnerCode());
            itemsDto.setOrigin_country("VN");
            itemsDto.setName(pd.getProName());
            itemsDto.setDesciption(pd.getProName());
            itemsDto.setWeight(10);
            itemsDto.setValue(pd.getPrice().intValue());
            itemsDto.setQuantity(pd.getQty());
            itemsDtoList.add(itemsDto);
        }
        paracelDto.setItems(itemsDtoList);
        paracelDtoList.add(paracelDto);

        shipmentDto.setParcels(paracelDtoList);

        requestDto.setShipments(shipmentDto);

        ConfigDto configDto = new ConfigDto();
        configDto.setCurrency("VND");
        configDto.setUnit_metric("metric");
        configDto.setDocument("Y");
        configDto.setInsurance("N");
        configDto.setReturn_mode(1);
        configDto.setSort_mode("best_price");//best_price: giao cháº­m
        configDto.setOrder_type("fulfill");
        configDto.setAuto_approve("Y");
        configDto.setDelivery_service("STD_TMDT");
        requestDto.setConfig(configDto);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setFee_paid_by("sender");
        paymentDto.setCod_amount(deliveryDto.getCod_money());
        requestDto.setPayment(paymentDto);

        ReferralDto referralDto = new ReferralDto();
        referralDto.setOrder_number(deliveryDto.getDoCode());
        requestDto.setReferral(referralDto);

        logger.info("{} {} BOXME {}", deliveryDto.getSESSION_ID(), deliveryDto.getDoId(), LogHelper.toJson(requestDto));
        BoxmeCreateOrderResponse createOrderResponse = (BoxmeCreateOrderResponse) service.CreateOrder(requestDto);
        logger.info("Boxme response: {}", LogHelper.toJson(createOrderResponse));

        if (!createOrderResponse.isError()) {
            orderResult.setResult(Const.DO_SUCCESS);
            orderResult.setMessage(createOrderResponse.getData().getTracking_number());
            orderResult.setCode("");
            logger.info("Boxme Order Result: {}", LogHelper.toJson(orderResult));
            lst.add(orderResult);
        } else {
            orderResult.setResult(Const.DO_ERROR);
            orderResult.setMessage("BOXME");
            orderResult.setErrMessage(LogHelper.toJson(createOrderResponse));
            orderResult.setCode(createOrderResponse.getErrorCode());
            lst.add(orderResult);
        }

        return lst;
    }

    private static OrderResult createOrderGHNExpress (DeliveryDto deliveryDto, List<ProductDto> products) {
        OrderResult result = new OrderResult();
        result.setType(Const.GHN_EXPRESS_CODE);

        GHNExpressServiceImpl ghnExpressService = new GHNExpressServiceImpl();

        GHNExpressRequestDto requestDto = new GHNExpressRequestDto();
        requestDto.setClient_order_code(deliveryDto.getDoCode());
        requestDto.setCod_amount(deliveryDto.getCod_money());
        requestDto.setContent(deliveryDto.getNote());
        requestDto.setInsurance_value(deliveryDto.getAmount());
        requestDto.setHeight(20);
        requestDto.setLength(20);
        requestDto.setWeight(200);
        requestDto.setLength(20);
        requestDto.setRequired_note(Const.WFS_GHN_EXPRESS_NOTE_CODE);
        requestDto.setService_id(0);
        requestDto.setService_type_id(2);
        requestDto.setTo_address(deliveryDto.getAddress());
        requestDto.setTo_district_id(Integer.valueOf(deliveryDto.getGhnDistrictCode()));
        requestDto.setTo_name(deliveryDto.getReceiveName());
        requestDto.setTo_phone(deliveryDto.getPhone());
        requestDto.setTo_ward_code(deliveryDto.getGhnWardCode());
        requestDto.setPayment_type_id(1); // Default is 1 - Choose who pay shipping fee: Shop/Seller.

        List<com.tms.ff.dto.request.ghnexpress.Items> items = new ArrayList<>();
        for (ProductDto dto : products) {
            com.tms.ff.dto.request.ghnexpress.Items item = new com.tms.ff.dto.request.ghnexpress.Items();
            item.setName(dto.getProName());
            item.setQuantity(dto.getQty());
            items.add(item);
        }
        requestDto.setItems(items);

        logger.info("GHNEXPRESS create Order Request: {}", LogHelper.toJson(requestDto));
        GHNExpressResponseDto response = ghnExpressService.createOrder(requestDto);
        logger.info("GHNEXPRESS create Order Response: {}", LogHelper.toJson(response));

        if (response.getCode() == 200) {
            result.setResult(Const.DO_SUCCESS);
            result.setMessage(deliveryDto.getDoCode());
        } else {
            result.setResult(Const.DO_ERROR);
            result.setMessage(response.getMessage());
            result.setCode(response.getCode_message());
        }
        logger.info("GHNEXPRESS Order Result: {}", LogHelper.toJson(result));

        return result;
    }

    private static List<OrderResult> createNTLDelivery(DeliveryDto deliveryDto, List<ProductDto> products) {
        OrderResult orderResult = new OrderResult();
        List<OrderResult> lstOrder = new ArrayList<>();
        NTLOrderServiceImpl service = new NTLOrderServiceImpl();

        NTLRequestOrderDto createOrderDto = new NTLRequestOrderDto();
        NTLDataRequestOrderDto dataRequestOrderDto = new NTLDataRequestOrderDto();
        NTLReceiverRequestOrderDto ntlReceiverRequestOrderDto = new NTLReceiverRequestOrderDto();
        ArrayList<NTLItemRequestOrderDto> ntlItemRequestOrderDtoArrayList = new ArrayList<>();

        String transporter = "";
        if (deliveryDto.getLastmileId() == Const.SNAPPY_PARTNER_ID) {
            transporter = Const.WFS_SNAPPY_PARTNER_CODE;
        } else if (deliveryDto.getLastmileId() == Const.GHN_PARTNER_ID) {
            transporter = Const.GHN_EXPRESS_CODE;
        } else if (deliveryDto.getLastmileId() == Const.SHIP60_LM_PARTNER_VN){
            transporter = Const.SHIP60_PARTNER_CODE;
        }  else {
            transporter = Const.NTL_EXPRESS_PARTNER_CODE;
        }

        createOrderDto.setClientKey("webpartner");
        dataRequestOrderDto.setWarehouseNumber(deliveryDto.getGroupAddressId());
        dataRequestOrderDto.setOrderTime(System.currentTimeMillis());
        dataRequestOrderDto.setDatePromised(System.currentTimeMillis());
        dataRequestOrderDto.setOrderNumber(deliveryDto.getDoCode());
        dataRequestOrderDto.setServiceType("B2C1D");
        dataRequestOrderDto.setTransporter(transporter);
        dataRequestOrderDto.setDesc(deliveryDto.getNote());
        dataRequestOrderDto.setOrderNumberRef("");

        ntlReceiverRequestOrderDto.setReceiverAddress(deliveryDto.getAddress());
        ntlReceiverRequestOrderDto.setReceiverName(deliveryDto.getReceiveName());
        ntlReceiverRequestOrderDto.setReceiverPhone(deliveryDto.getPhone());
        ntlReceiverRequestOrderDto.setCity(deliveryDto.getProvinceName());
        ntlReceiverRequestOrderDto.setDistrict(deliveryDto.getDistrictName());
        ntlReceiverRequestOrderDto.setZipCode(deliveryDto.getWardName());

        if(products != null && products.size() > 0){
            for(ProductDto product: products){
                NTLItemRequestOrderDto ntlItemRequestOrderDto = new NTLItemRequestOrderDto();
                ntlItemRequestOrderDto.setSku(null);
                ntlItemRequestOrderDto.setsCode(product.getProPartnerCode());
                ntlItemRequestOrderDto.setUom("");
                ntlItemRequestOrderDto.setQtyOrdered(product.getQty());
                ntlItemRequestOrderDto.setQtyReleased(product.getQty());

                ntlItemRequestOrderDtoArrayList.add(ntlItemRequestOrderDto);
            }
        }

        dataRequestOrderDto.setReceiverInfo(ntlReceiverRequestOrderDto);
        dataRequestOrderDto.setOrderLineItem(ntlItemRequestOrderDtoArrayList);

        createOrderDto.setData(dataRequestOrderDto);

        try {
            boolean isCreateFF = true;
            String lastmile_code = "";

            if (transporter == Const.WFS_SNAPPY_PARTNER_CODE) {
                OrderResult snappyOrder = CreateSnappyOrder(deliveryDto, products);
                snappyOrder.setType(Const.WFS_GHTK_PARTNER_CODE);
                if (snappyOrder.getResult() == Const.DO_ERROR) {
                    logger.info("*************Create Snappy order error***********\n %s", LogHelper.toJson(snappyOrder));
                    orderResult.setErrMessage(String.format("%s|%s", snappyOrder.getMessage(), snappyOrder.getErrMessage()));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                } else {
                    logger.info("Create snappy order success: %s", LogHelper.toJson(snappyOrder));
                    lastmile_code = snappyOrder.getMessage();
                }

                lstOrder.add(snappyOrder);
            }

            if (transporter == Const.GHN_EXPRESS_CODE) {
                OrderResult ghnOrder = createOrderGHNExpress(deliveryDto, products);
                ghnOrder.setType(Const.GHN_EXPRESS_CODE);
                if (ghnOrder.getResult() == Const.DO_ERROR) {
                    logger.info("*************Create ghn express order error***********\n %s", LogHelper.toJson(ghnOrder));
                    orderResult.setErrMessage(String.format("%s|%s", ghnOrder.getMessage(), ghnOrder.getErrMessage()));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                } else {
                    logger.info("Create ghn express order success: %s", LogHelper.toJson(ghnOrder));
                    lastmile_code = ghnOrder.getMessage();
                }

                lstOrder.add(ghnOrder);
            }

            if (transporter == Const.SHIP60_PARTNER_CODE) {
                OrderResult ship60Order = createShip60Delivery(deliveryDto, products);
                ship60Order.setType(Const.GHN_EXPRESS_CODE);
                if (ship60Order.getResult() == Const.DO_ERROR) {
                    logger.info("*************Create ship60 order error***********\n %s", LogHelper.toJson(ship60Order));
                    orderResult.setErrMessage(String.format("%s|%s", ship60Order.getMessage(), ship60Order.getErrMessage()));
                    orderResult.setResult(Const.DO_ERROR);
                    isCreateFF = false;
                } else {
                    logger.info("Create ship60 order success: %s" + LogHelper.toJson(ship60Order));
                    lastmile_code = ship60Order.getMessage();
                }

                lstOrder.add(ship60Order);
            }

            if (isCreateFF) {
                logger.info("[CREATE ORDER NTL] " + deliveryDto.getSESSION_ID() + " " + deliveryDto.getDoId());

                if (lastmile_code != null && !lastmile_code.isEmpty())
                    createOrderDto.getData().setBillNumber(lastmile_code);

                System.out.println("==================NTLRequestOrderDto==================");
                logger.info("NTLRequestOrderDto: {}", LogHelper.toJson(createOrderDto));
                NTLOrderResponseDto createOrderResponse = service.createOrder(createOrderDto);
                System.out.println("==================NTLOrderResponseDto ==================");
                logger.info("createOrderResponse: {}", LogHelper.toJson(createOrderResponse));


                if (createOrderResponse != null && createOrderResponse.getReturnCode() != null) {
                    if (createOrderResponse.getReturnCode().equals("200")) {
                        orderResult.setResult(Const.DO_SUCCESS);
                        orderResult.setCode(String.valueOf(createOrderResponse.getReturnCode()));
                        orderResult.setMessage(String.format("%s", deliveryDto.getDoCode()));
                        orderResult.setType(Const.NTL_EXPRESS_PARTNER_CODE);
                    } else {
                        logger.info("NTL error: {}", LogHelper.toJson(createOrderDto.getData()));
                        orderResult.setErrMessage(String.format("%s", createOrderResponse.getData().getBillNumber()));
                        orderResult.setCode(String.valueOf(createOrderResponse.getReturnCode()));
                        orderResult.setResult(Const.DO_ERROR);
                        if (transporter == Const.WFS_SNAPPY_PARTNER_CODE) {// cancel order to snappy
                            CancelSnappy(lastmile_code);
                        }
                    }
                } else {//Error happen when create NTL order
                    orderResult.setResult(Const.DO_ERROR);
                    orderResult.setErrMessage(createOrderResponse.getReturnMessage());

                }
                lstOrder.add(orderResult);
                System.out.println(deliveryDto.getSESSION_ID() + " " + deliveryDto.getDoId() + "============================= " + LogHelper.toJson(lstOrder));
                return lstOrder;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            orderResult.setMessage(e.getMessage());
        }
//        item = new OrderResult();
        lstOrder.add(orderResult);
//
//        if (createOrderResponse != null && createOrderResponse.getReturnCode() != null
//                && StringUtils.hasText(createOrderResponse.getReturnCode()) && createOrderResponse.getReturnCode().equals("200")) {
//            item.setResult(Const.DO_SUCCESS);
//            item.setMessage(deliveryDto.getDoCode());
//        } else {
//            item.setResult(Const.DO_ERROR);
//            item.setErrMessage(String.format("ERROR when create NTL order| %s",
//                    LogHelper.toJson(createOrderResponse.getData())));
//        }
//
//        lst.add(item);
//        return lst;
        return  lstOrder;
    }
}
