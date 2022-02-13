package com.tms.api.service;

import com.tms.api.entity.*;
import com.tms.api.entity.RcActionMapping;
import com.tms.api.entity.RcActivity;
import com.tms.api.entity.RcLastmileStatus;
import com.tms.api.entity.RcRescueJob;
import com.tms.api.entity.OdDONew;

import java.sql.Timestamp;
import java.util.List;

public interface RescueService {
    void saveOrUpdateLastmileStatus(List<RcLastmileStatus> rcLastmileStatus);

    void saveOrUpdateActivity(List<RcActivity> rcActivities);

    List<RcRescueJob> saveOrUpdateRescueJob(List<RcRescueJob> rescueJobs);

    void updateDOByTrackingcode(List<OdDONew> odDONews);

    List<OdDONew> getDOByTrackingcode(List<String> trackingCodes);

    void updateIsUpdated(List<String> trackingCodes);

    void updateUpdatedStatus(List<String> trackingCodes);

    List<OdDONew> getDOByAction(List<String> tracking_code, Integer action);

    List<OdDONew> getDOByRescueNull(List<String> tracking_code, Integer action);

    void saveOrUpdateDO(List<OdDONew> odDONews);

    void bactchUpdateRescue(List<RcRescueJob> rcRescueJobs);

    List<OdDONew> getDOByTime(Timestamp createDate);

    List<RcLastmileStatus> getLastmileStatus();

}
