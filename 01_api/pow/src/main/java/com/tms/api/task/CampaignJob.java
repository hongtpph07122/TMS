package com.tms.api.task;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.tms.api.dto.UpdateCampaignConfigDto;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.Helper;
import com.tms.dto.CPCampaignParams;
import com.tms.dto.CPCampaignResp;
import com.tms.dto.DBResponse;
import com.tms.dto.GetCallbackAll;
import com.tms.dto.GetCampaign;
import com.tms.dto.GetLeadParams;
import com.tms.entity.CLCallback;
import com.tms.entity.CLFresh;
import com.tms.entity.CPCampaign;
import com.tms.entity.log.UpdCampaign;
import com.tms.service.impl.CLCallbackService;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LogService;
import org.springframework.stereotype.Component;

@Component
public class CampaignJob {
    private static final Logger log = LoggerFactory.getLogger(CampaignJob.class);
    
    @Autowired
    CLFreshService freshService;
    
    @Autowired
    CLCallbackService callbackService;
    
    @Autowired
    LogService logService;

    @Value("${config.stop-campaign}")
    private boolean isStopCampaign;
    
    @Scheduled(fixedRate = 3*60*60*1000) // 3h
    public void stopCampaign() {
        if (!isStopCampaign) {
            log.info(" ===== Stop campaign is off! =====");
            return;
        }
        String taskSession = UUID.randomUUID().toString();
        log.info("Starting to process basket stopCampaign session: " + taskSession);
        
        GetCampaign params = new GetCampaign();
        params.setStatus(EnumType.CAMPAIGN_STATUS_ID.STOPPING.getValue());
        DBResponse<List<CPCampaign>> dbResponse = freshService.getCampaign(taskSession, params);
        if (dbResponse.getResult().isEmpty()) {
            return;
        }
        
        for (CPCampaign campaign : dbResponse.getResult()) {
            CPCampaignParams ruleParams = new CPCampaignParams();
            ruleParams.setCapaignId(campaign.getCpId());
            List<CPCampaignResp> cpConfigs = freshService.getCampaignConfig(taskSession, ruleParams).getResult();
            UpdateCampaignConfigDto cpConfigDto = Helper.getCampaignConfig(cpConfigs);
            List<Integer> callingListIds = cpConfigDto.getCallingList();
            for (Integer callingListId : callingListIds) {
                GetLeadParams leadParams = new GetLeadParams();
                leadParams.setCallinglistId(callingListId.toString());
                leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
                List<CLFresh> leads = freshService.getLead(taskSession, leadParams).getResult();
                
                leadParams.setLeadStatus(EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue());
                leads.addAll(freshService.getLead(taskSession, leadParams).getResult());
                
                leadParams.setLeadStatus(EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue());
                leads.addAll(freshService.getLead(taskSession, leadParams).getResult());

                leadParams.setLeadStatus(EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue());
                leads.addAll(freshService.getLead(taskSession, leadParams).getResult());

                GetCallbackAll callbackParams = new GetCallbackAll();
                callbackParams.setCallinglistId(callingListId.toString());
                callbackParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
                List<CLCallback> callbacks = callbackService.getCallbackAll(taskSession, callbackParams).getResult();
                
                callbackParams.setLeadStatus(EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue());
                callbacks.addAll(callbackService.getCallbackAll(taskSession, callbackParams).getResult());
                
                callbackParams.setLeadStatus(EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue());
                callbacks.addAll(callbackService.getCallbackAll(taskSession, callbackParams).getResult());

                callbackParams.setLeadStatus(EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue());
                callbacks.addAll(callbackService.getCallbackAll(taskSession, callbackParams).getResult());
                
                if (leads.isEmpty() && callbacks.isEmpty()) {
                    UpdCampaign campaignParams = new UpdCampaign();
                    campaignParams.setCampaignid(campaign.getCpId());
                    campaignParams.setStatus(EnumType.CAMPAIGN_STATUS_ID.STOPPED.getValue());
                    logService.udpCampaign(taskSession, campaignParams);
                }
            }
        }
        
        log.info("Finished process basket stopCampaign session: " + taskSession);
    }
}
