package com.tms.api.service;

import com.tms.dto.CPCampaignParams;
import com.tms.dto.CPCampaignResp;
import com.tms.dto.GetCpCallStrategyResp;
import com.tms.dto.GetCpDistributionRuleResp;

public interface CampaignService {
    boolean refreshConfigCampaign(String SESSION_ID, Integer orgId);

    CPCampaignResp getConfigCampaign(String SESSION_ID, Integer orgId, Integer cpId);

    boolean refreshConfigDistributionRules(String SESSION_ID, Integer orgId);

    GetCpDistributionRuleResp getConfigDistributionRules(String SESSION_ID, Integer orgId, Integer cpId, Integer configValue);

    boolean refreshConfigCallStrategies(String SESSION_ID, Integer orgId);

    GetCpCallStrategyResp getConfigCallStrategies(String SESSION_ID, Integer orgId, Integer cpId, Integer orderPhoneNumber, Integer callStatus);

}
