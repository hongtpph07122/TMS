package com.tms.api.service.impl;

import com.tms.api.service.RescueSqlNativeService;
import com.tms.api.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Service(value = "rescueSqlNativeService")
@Transactional
public class RescueSqlNativeServiceImpl implements RescueSqlNativeService {

    private static final Logger logger = LoggerFactory.getLogger(RescueSqlNativeServiceImpl.class);

    @PersistenceContext
    private final EntityManager entityManager;


    @Autowired
    public RescueSqlNativeServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public boolean existByIdWithPreDelivery(Integer id, boolean isPreDelivery) {
        if (!ObjectUtils.allNotNull(id, isPreDelivery)) {
            return false;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(" SELECT COUNT(rescueJob.id) FROM rc_rescue_job rescueJob WHERE rescueJob.id = :id AND rescueJob.is_pre_delivery = :preDelivery ");
        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("id", id);
        query.setParameter("preDelivery", isPreDelivery);
        int result = ((Number) query.getSingleResult()).intValue();
        if (result == 0) {
            return false;
        }
        return result > 0;
    }

}
