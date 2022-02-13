package com.tms.api.task;

import com.tms.api.entity.*;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.impl.RescueServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class GHNGetOrderJob {
    private final Logger logger = LoggerFactory.getLogger(GHNGetOrderJob.class);

    @Autowired
    RescueServiceImpl rescueService;

    @Value("${config.ghn-order}")
    public Boolean isGHNOrderJob;

    @Scheduled(cron = "0 0 8,14 * * ?")
    public TMSResponse fromApiToRescueGHN() throws IOException, ParseException {
        logger.info("START*******************START");
        if(!isGHNOrderJob)
            return null;
        TMSResponse response = new TMSResponse();
        int updated = 0;
        int created = 0;
        int imported = 0;
        String messages = "";
        Date now = new Date();
        Timestamp nowTimestamp = new Timestamp(now.getTime());
        List<RcLastmileStatus> rcLastmileStatusList = rescueService.getLastmileStatus();
        if(rcLastmileStatusList == null || rcLastmileStatusList.isEmpty()){
            logger.info("0 imported | 0 create | 0 update");
            return response.buildResponse(null, imported, "0 imported | 0 create | 0 update", 200);
        }
        List<String> trackingCodes = new ArrayList<>();
        HashMap<String, List<String>> statusMap = new HashMap<>();
        for(RcLastmileStatus rc : rcLastmileStatusList){
            imported += 1;
            trackingCodes.add(rc.getTracking_code());
            List<String> status = new ArrayList<>();
            status.add(rc.getStatus());
            status.add(rc.getSubstatus());
            statusMap.put(rc.getTracking_code(), status);
        }
        List<OdDONew> odDONewList = rescueService.getDOByTrackingcode(trackingCodes);
        List<RcRescueJob> updates = new ArrayList<>();
        List<RcRescueJob> creates = new ArrayList<>();
        List<com.tms.api.entity.RcActivity> rcActivities = new ArrayList<>();
        List<String> tracking_update = new ArrayList<>();
        for (OdDONew DO : odDONewList) {
            List<String> status = statusMap.get(DO.getTracking_code());
            if (status != null) {
                DO.setLastmile_reason(status.get(0));
                DO.setLastmile_reason_detail(status.get(1));
                tracking_update.add(DO.getTracking_code());
            }
        }
        rescueService.saveOrUpdateDO(odDONewList);
        List<OdDONew> odDONews = rescueService.getDOByAction(tracking_update, 1);
        if (odDONews == null) {
            logger.info("Action = 1, rescue not null, NULL");
        } else {
            for (OdDONew DO : odDONews) {
                RcRescueJob rcRescueJob = new RcRescueJob();
                rcRescueJob.setId(DO.getRescue_id());
                rcRescueJob.setLastmile_reason(DO.getLastmile_reason());
                rcRescueJob.setLastmile_reason_detail(DO.getLastmile_reason_detail());
                rcRescueJob.setPriority(DO.getRcActionMapping().getPriority());
                rcRescueJob.setUpdatedate(nowTimestamp);
                rcRescueJob.setUpdateby(0);
//                rcRescueJob.setJob_status(5);
                updates.add(rcRescueJob);
                RcActivity rcActivity = this.createActivity(DO, nowTimestamp, 1, true, DO.getRescue_id());
                rcActivities.add(rcActivity);
                updated++;
            }
        }

        List<OdDONew> odDONews2 = rescueService.getDOByAction(tracking_update, 2);
        if (odDONews2 == null) {
            logger.info("Action = 2, null");
        } else {
            for (OdDONew DO : odDONews2) {
                RcRescueJob rcRescueJob = new RcRescueJob();
                rcRescueJob.setId(DO.getRescue_id());
                rcRescueJob.setLastmile_reason(DO.getLastmile_reason());
                rcRescueJob.setLastmile_reason_detail(DO.getLastmile_reason_detail());
                rcRescueJob.setPriority(DO.getRcActionMapping().getPriority());
                rcRescueJob.setUpdatedate(nowTimestamp);
                rcRescueJob.setUpdateby(0);
                rcRescueJob.setJob_status(7);
                rcRescueJob.setJob_sub_status(3);
                updates.add(rcRescueJob);
                RcActivity rcActivity = this.createActivity(DO, nowTimestamp, 2, true, DO.getRescue_id());
                rcActivities.add(rcActivity);
                updated++;
            }
        }

        List<OdDONew> odDONews3 = rescueService.getDOByAction(tracking_update, 3);
        if (odDONews3 == null) {
            logger.info("Action = 3, null");
        } else {
            for (OdDONew DO : odDONews3) {
                RcRescueJob rcRescueJob = new RcRescueJob();
                rcRescueJob.setId(DO.getRescue_id());
                rcRescueJob.setLastmile_reason(DO.getLastmile_reason());
                rcRescueJob.setLastmile_reason_detail(DO.getLastmile_reason_detail());
                rcRescueJob.setPriority(DO.getRcActionMapping().getPriority());
                rcRescueJob.setUpdatedate(nowTimestamp);
                rcRescueJob.setUpdateby(0);
                rcRescueJob.setJob_status(7);
                rcRescueJob.setJob_sub_status(2);
                updates.add(rcRescueJob);
                RcActivity rcActivity = this.createActivity(DO, nowTimestamp, 2, true, DO.getRescue_id());
                rcActivities.add(rcActivity);
                updated++;
            }
        }

        List<OdDONew> odDONews1_null = rescueService.getDOByRescueNull(tracking_update, 1);
        HashMap<String, OdDONew> updateRcIds = new HashMap<>();
        if (odDONews1_null == null) {
            logger.info("None to create");
        } else {
            for (OdDONew DO : odDONews1_null) {
                RcRescueJob rcRescueJob = this.createRescue(DO, DO.getRcActionMapping(), nowTimestamp);
                creates.add(rcRescueJob);
                updateRcIds.put(DO.getTracking_code(), DO);
                created++;
            }
        }
        if (!creates.isEmpty()) {
            List<RcRescueJob> rcRescueJobs = rescueService.saveOrUpdateRescueJob(creates);
            List<OdDONew> updateDORcs = new ArrayList<>();
            for (RcRescueJob rc : rcRescueJobs) {
                OdDONew odDONew = updateRcIds.get(rc.getTracking_code());
                if (odDONew != null) {
                    RcActivity rcActivity = this.createActivity(odDONew, nowTimestamp, 1, false, rc.getId());
                    rcActivities.add(rcActivity);
                    odDONew.setRescue_id(rc.getId());
                    updateDORcs.add(odDONew);
                }
            }
            rescueService.saveOrUpdateDO(updateDORcs);
        }
        rescueService.saveOrUpdateActivity(rcActivities);
        rescueService.bactchUpdateRescue(updates);
        rescueService.updateIsUpdated(trackingCodes);
        rescueService.updateUpdatedStatus(tracking_update);
        logger.info("DONE**************************DONE");
        messages = imported + " records imported | " + created + " rescues job created | " + updated + " rescues job updated";
        logger.info(messages);
        return response.buildResponse(null, imported, messages, 200);
    }

    private RcRescueJob createRescue(OdDONew DO, RcActionMapping rcActionMapping, Timestamp nowTimestamp) {
        RcRescueJob rcRescueJob = new RcRescueJob();
        rcRescueJob.setLastmile_reason(rcActionMapping.getStatus_name());
        rcRescueJob.setLastmile_reason_detail(rcActionMapping.getSub_status_name());
        rcRescueJob.setPriority(rcActionMapping.getPriority());
        rcRescueJob.setOrg_id(DO.getOrg_id());
        rcRescueJob.setTracking_code(DO.getTracking_code());
        rcRescueJob.setDo_id(DO.getDo_id());
        rcRescueJob.setDo_code(DO.getDo_code());
        rcRescueJob.setAmountcod(DO.getAmountcod());
        rcRescueJob.setCustomer_address(DO.getCustomer_address());
        rcRescueJob.setCustomer_id(DO.getCustomer_id());
        rcRescueJob.setCustomer_name(DO.getCustomer_name());
        rcRescueJob.setCreateby(DO.getCreateby());
        rcRescueJob.setFfm_code(DO.getFfm_code());
        rcRescueJob.setFfm_id(DO.getFfm_id());
        rcRescueJob.setJob_status(1);
        rcRescueJob.setSo_id(DO.getSo_id());
        rcRescueJob.setLastmile_id(DO.getCarrier_id());
        rcRescueJob.setCustomer_phone(DO.getCustomer_phone());
        rcRescueJob.setPackage_products(DO.getPackage_name());
        rcRescueJob.setDo_status(DO.getStatus());
        rcRescueJob.setCreateby(0);
        rcRescueJob.setUpdatedate(nowTimestamp);
        rcRescueJob.setCreatedate(nowTimestamp);
        return rcRescueJob;
    }

    private RcActivity createActivity(OdDONew DO, Timestamp nowTimestamp, Integer action, boolean rescue, Integer rescueId) {
        RcActivity rcActivity = new RcActivity();
        rcActivity.setActivity_type(3);
        rcActivity.setUpdateby(0);
        rcActivity.setAct_time(nowTimestamp);
        rcActivity.setLastmile_reason(DO.getLastmile_reason());
        rcActivity.setLastmile_reason_detail(DO.getLastmile_reason_detail());
        if (action == 1 && rescue == false) {
            rcActivity.setRc_job_id(rescueId);
            rcActivity.setComment("System create rescue job");
            rcActivity.setNew_status(1);
            return rcActivity;
        } else if (action == 1 && rescue == true) {
            rcActivity.setRc_job_id(rescueId);
            rcActivity.setComment("System update lastmile reason from excel file");
            return rcActivity;
        } else if (action == 2) {
            rcActivity.setRc_job_id(rescueId);
            rcActivity.setComment("System close rescue job");
            rcActivity.setNew_status(7);
            return rcActivity;
        }
        return null;
    }
}