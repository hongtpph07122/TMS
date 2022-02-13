package com.tms.api.service.impl;

import com.tms.api.entity.OdDONew;
import com.tms.api.repository.OdDONewRepository;
import com.tms.api.request.DeliveriesOrderRequest;
import com.tms.api.service.DeliveryOrderRelatedService;
import com.tms.api.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "DeliveryOrderRelatedService")
@Transactional
public class DeliveryOrderRelatedServiceImpl implements DeliveryOrderRelatedService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryOrderRelatedServiceImpl.class);

    private final OdDONewRepository deliveryOrderRepository;

    @Autowired
    public DeliveryOrderRelatedServiceImpl(
            OdDONewRepository deliveryOrderRepository) {
        this.deliveryOrderRepository = deliveryOrderRepository;
    }


    @Override
    public void updateReminderCalls(Integer deliveryOrderId, DeliveriesOrderRequest deliveriesOrderRequest) {
        if (ObjectUtils.allNotNull(deliveryOrderId)) {
            OdDONew deliveryOrderEntity = deliveryOrderRepository.getOne(deliveryOrderId);
            if (ObjectUtils.allNotNull(deliveryOrderEntity)) {
                deliveryOrderEntity.setAppointmentDate(deliveriesOrderRequest.getAppointmentDate());
                deliveryOrderRepository.save(deliveryOrderEntity);
            }
        }

    }
}
