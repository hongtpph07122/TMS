package com.tms.api.service.impl;

import com.tms.api.entity.OdDONew;
import com.tms.api.entity.RcActivity;
import com.tms.api.entity.RcLastmileStatus;
import com.tms.api.entity.RcRescueJob;
import com.tms.api.params.ConstParams;
import com.tms.api.repository.*;
import com.tms.api.service.RescueService;
import com.tms.api.utils.DateUtils;
import com.tms.api.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RescueServiceImpl implements RescueService {

    private static final Logger logger = LoggerFactory.getLogger(RescueServiceImpl.class);

    @Autowired
    private RcLastmileStatusRepository rcLastmileStatusRepository;

    @Autowired
    private RcActionMappingRepository rcActionMappingRepository;

    @Autowired
    private RcRescueJobRepository rcRescueJobRepository;

    @Autowired
    private RcActivityRepository rcActivityRepository;

    @Autowired
    private OdDONewRepository odDONewRepository;


    @Override
    public void saveOrUpdateLastmileStatus(List<RcLastmileStatus> rcLastmileStatus) {
        rcLastmileStatusRepository.saveAll(rcLastmileStatus);
    }

    @Override
    public void saveOrUpdateActivity(List<RcActivity> rcActivities) {
        rcActivityRepository.saveAll(rcActivities);
    }

    @Override
    public List<RcRescueJob> saveOrUpdateRescueJob(List<RcRescueJob> rescueJobs) {
        List<RcRescueJob> rcRescueJobs = rcRescueJobRepository.saveAll(rescueJobs);
        return rcRescueJobs;
    }

    @Override
    public List<OdDONew> getDOByTrackingcode(List<String> trackingCodes) {
        List<OdDONew> odDONews = odDONewRepository.getDOByTrackingCode(trackingCodes);
        if (odDONews != null) {
            return odDONews;
        }
        return null;
    }

    @Override
    public void updateDOByTrackingcode(List<OdDONew> odDONews) {
        odDONewRepository.saveAll(odDONews);
    }


    @Override
    public void updateIsUpdated(List<String> trackingCodes) {
        rcLastmileStatusRepository.updateIsUpdated(trackingCodes);
    }

    @Override
    public void updateUpdatedStatus(List<String> trackingCodes) {
        rcLastmileStatusRepository.updateUpdatedStatus(trackingCodes);
    }

    @Override
    public List<OdDONew> getDOByAction(List<String> tracking_code, Integer action) {
        List<OdDONew> odDONews = odDONewRepository.getDOByAction(tracking_code, action);
        if (odDONews != null && !odDONews.isEmpty()) {
            return odDONews;
        }
        return null;
    }

    @Override
    public List<OdDONew> getDOByRescueNull(List<String> tracking_code, Integer action) {
        List<OdDONew> odDONews = odDONewRepository.getDOByRescueNull(tracking_code, action);
        if (odDONews != null && !odDONews.isEmpty()) {
            return odDONews;
        }
        return null;
    }

    @Override
    public void saveOrUpdateDO(List<OdDONew> odDONews) {
        odDONewRepository.saveAll(odDONews);
    }

    @PersistenceContext
    EntityManager entityManager;


    private boolean runBatchOneRescue(RcRescueJob rescueJob) {
        StringBuilder builder = new StringBuilder();
        builder.append(" UPDATE rc_rescue_job ");
        builder.append(" SET lastmile_reason = :lastMileReason ");
        builder.append(" , lastmile_reason_detail = :lastMileReasonDetail ");
        builder.append(" , updatedate = :modifiedTime ");
        builder.append(" , updateby = :modifiedBy ");
        builder.append(" , priority = :priority ");

        if (ObjectUtils.allNotNull(rescueJob.getPreDelivery())) {
            builder.append(" , is_pre_delivery = :preDelivery ");
        }

        if (ObjectUtils.allNotNull(rescueJob.getJob_status())) {
            builder.append(" , job_status = :jobStatus ");
        }

        if (ObjectUtils.allNotNull(rescueJob.getJob_sub_status())) {
            builder.append(" , job_sub_status = :jobSubStatus ");
        }

        builder.append(" WHERE id = :id ");

        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("id", rescueJob.getId());
        query.setParameter("lastMileReason", rescueJob.getLastmile_reason());
        query.setParameter("lastMileReasonDetail", rescueJob.getLastmile_reason_detail());
        query.setParameter("modifiedTime", rescueJob.getUpdatedate());
        query.setParameter("modifiedBy", rescueJob.getUpdateby());
        query.setParameter("priority", rescueJob.getPriority());

        if (ObjectUtils.allNotNull(rescueJob.getPreDelivery())) {
            query.setParameter("preDelivery", rescueJob.getPreDelivery());
        }

        if (ObjectUtils.allNotNull(rescueJob.getJob_status()) || rescueJob.getJob_status().equals(7)) {
            query.setParameter("jobStatus", rescueJob.getJob_status());
        }

        if (ObjectUtils.allNotNull(rescueJob.getJob_sub_status())) {
            query.setParameter("jobSubStatus", rescueJob.getJob_sub_status());
        }

        int result = query.executeUpdate();
        return result == 1;
    }

    @Override
    @Transactional
    public void bactchUpdateRescue(List<RcRescueJob> rcRescueJobs) {
        logger.info("Start update rescue at: {}", DateUtils.feedStageAsString(new Date()));
        /*
        int countSucceeded = 0;
        for (RcRescueJob rescueJob : rcRescueJobs) {
            if (runBatchOneRescue(rescueJob)) {
                countSucceeded++;
            }
        }
        logger.info("No. of rescue update successfully: {} - at: {}", countSucceeded, DateUtils.feedStageAsString(new Date()));
        */

        String query2 = "UPDATE com.tms.api.entity.RcRescueJob rc set rc.lastmile_reason = ?1, rc.lastmile_reason_detail = ?2, rc.priority = ?3, rc.updatedate = ?4, rc.updateby = ?5 where rc.id = ?6";
        String query3 = "UPDATE com.tms.api.entity.RcRescueJob rc set rc.lastmile_reason = ?1, rc.lastmile_reason_detail = ?2, rc.priority = ?3, rc.updatedate = ?4, rc.updateby = ?5, rc.job_status = ?6, rc.job_sub_status = ?7 where rc.id = ?8";
        String query4 = "UPDATE com.tms.api.entity.RcRescueJob rc set rc.lastmile_reason = ?1, rc.lastmile_reason_detail = ?2, rc.priority = ?3, rc.updatedate = ?4, rc.updateby = ?5, rc.job_status = ?6, rc.job_sub_status = ?7, rc.isPreDelivery = ?9, rc.jobType = ?10 where rc.id = ?8";

        for (RcRescueJob rc : rcRescueJobs) {
            if (rc.getJob_status() == null && !ObjectUtils.allNotNull(rc.getPreDelivery())) {
                entityManager.createQuery(query2)
                        .setParameter(1, rc.getLastmile_reason())
                        .setParameter(2, rc.getLastmile_reason_detail())
                        .setParameter(3, rc.getPriority())
                        .setParameter(4, rc.getUpdatedate())
                        .setParameter(5, rc.getUpdateby())
                        .setParameter(6, rc.getId())
                        .executeUpdate();
            } else if (rc.getJob_status() == null && ObjectUtils.allNotNull(rc.getPreDelivery())) {
                entityManager.createQuery(query4)
                        .setParameter(1, rc.getLastmile_reason())
                        .setParameter(2, rc.getLastmile_reason_detail())
                        .setParameter(3, rc.getPriority())
                        .setParameter(4, rc.getUpdatedate())
                        .setParameter(5, rc.getUpdateby())
                        .setParameter(6, ConstParams.RescueStatus.NEW.getValue())
                        .setParameter(7, null)
                        .setParameter(8, rc.getId())
                        .setParameter(9, rc.getPreDelivery())
                        .setParameter(10, rc.getJobType())
                        .executeUpdate();
            } else if (rc.getJob_status() == 7) {
                entityManager.createQuery(query3)
                        .setParameter(1, rc.getLastmile_reason())
                        .setParameter(2, rc.getLastmile_reason_detail())
                        .setParameter(3, rc.getPriority())
                        .setParameter(4, rc.getUpdatedate())
                        .setParameter(5, rc.getUpdateby())
                        .setParameter(6, rc.getJob_status())
                        .setParameter(7, rc.getJob_sub_status())
                        .setParameter(8, rc.getId())
                        .executeUpdate();
            }
        }

    }

    @Override
    public List<OdDONew> getDOByTime(Timestamp createDate) {
        List<OdDONew> odDONews = odDONewRepository.getDOByTime(createDate);
        if (odDONews != null && !odDONews.isEmpty()) {
            return odDONews;
        }
        return null;
    }

    @Override
    public List<RcLastmileStatus> getLastmileStatus() {
        List<RcLastmileStatus> rcLastmileStatuses = rcLastmileStatusRepository.getLastmileStatus();
        if (rcLastmileStatuses == null || rcLastmileStatuses.isEmpty()) {
            return null;
        }
        return rcLastmileStatuses;
    }

}
