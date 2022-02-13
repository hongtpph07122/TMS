package com.tms.api.service;

import com.tms.api.request.DeliveriesOrderRequest;

public interface DeliveryOrderRelatedService {

    void updateReminderCalls(Integer deliveryOrderId, DeliveriesOrderRequest deliveriesOrderRequest);

}
