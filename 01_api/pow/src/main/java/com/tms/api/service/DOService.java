package com.tms.api.service;


import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.api.dto.Response.ShippingResponseDTO;
import com.tms.api.dto.ResponseRescueFromDO;
import com.tms.api.dto.ShippingPendingRespDto;
import com.tms.api.response.TMSResponse;
import com.tms.dto.GetDoNewV2;
import com.tms.dto.GetShipping;

public interface DOService {

    ResponseRescueFromDO createAndUpdateRescue();

    byte[] exportCSVShipping(GetDoNewV2 paramsDo, OrderManagementRequestDTO orderManagementRequestDTO);

    byte[] exportCSVShippingPending(GetShipping paramsShippingPending, OrderManagementRequestDTO orderManagementRequestDTO);

    ShippingPendingRespDto snagDeliveryOrdersPending(GetShipping shippingRequest);

    ShippingResponseDTO snagShippingDeliveryOrders(ObjectRequestDTO objectRequestDTO, GetDoNewV2 deliveryOrderRequest);

    TMSResponse<Boolean> cancelDO(String ssId, Integer orgId, Integer userId, Integer doId);
}
