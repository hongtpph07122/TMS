package com.tms.api.service.impl;

import com.tms.api.helper.Helper;
import com.tms.api.helper.LogHelper;
import com.tms.api.service.CampaignService;
import com.tms.dto.*;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LCProvinceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service(value = "campaignService")
public class CampaignServiceImpl implements CampaignService {
    private static HashMap<String, CPCampaignResp> configCampaign = null;
    private static HashMap<String, GetCpDistributionRuleResp> configDistributionRules = null;
    private static HashMap<String, GetCpCallStrategyResp> configCpCallStrategy = null;

    Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

    private final CLFreshService freshService;

    private final LCProvinceService lcProvinceService;

    @Autowired
    public CampaignServiceImpl(CLFreshService freshService, LCProvinceService lcProvinceService) {
        this.freshService = freshService;
        this.lcProvinceService = lcProvinceService;
    }

    @Override
    public boolean refreshConfigCampaign(String SESSION_ID, Integer orgId) {
        if (configCampaign != null) configCampaign.clear();
        else configCampaign = new HashMap<>();
        try {
            readConfigCampaignByOrg(SESSION_ID, orgId);
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    @Override
    public CPCampaignResp getConfigCampaign(String SESSION_ID, Integer orgId, Integer cpId) {
        if (configCampaign == null) {
            configCampaign = new HashMap<>();
            try {
                readConfigCampaignByOrg(SESSION_ID, orgId);
            } catch (Exception e) {
                logger.info(e.getMessage());
                return null;
            }
        }
        return configCampaign.get(String.format("%s:%s", Helper.toStringEmpty(orgId), Helper.toStringEmpty(cpId)));
    }

    @Override
    public boolean refreshConfigDistributionRules(String SESSION_ID, Integer orgId) {
        if (configDistributionRules != null) configDistributionRules.clear();
        else configDistributionRules = new HashMap<>();
        try {
            readConfigDistributionRulesByOrg(SESSION_ID, orgId);
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    @Override
    public GetCpDistributionRuleResp getConfigDistributionRules(String SESSION_ID, Integer orgId, Integer cpId, Integer configValue) {
        if (configDistributionRules == null) {
            configDistributionRules = new HashMap<>();
            try {
                readConfigDistributionRulesByOrg(SESSION_ID, orgId);
            } catch (Exception e) {
                logger.info(e.getMessage());
                return null;
            }
        }
        return configDistributionRules.get(String.format("%s:%s:%s", Helper.toStringEmpty(orgId),
                Helper.toStringEmpty(cpId), Helper.toStringEmpty(configValue)));
    }

    @Override
    public boolean refreshConfigCallStrategies(String SESSION_ID, Integer orgId) {
        if (configCpCallStrategy != null) configCpCallStrategy.clear();
        else configCpCallStrategy = new HashMap<>();
        try {
            readConfigCpCallStrategyByOrg(SESSION_ID, orgId);
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    @Override
    public GetCpCallStrategyResp getConfigCallStrategies(String SESSION_ID, Integer orgId, Integer cpId, Integer orderPhoneNumber, Integer callStatus) {
        if (configCpCallStrategy == null) {
            configCpCallStrategy = new HashMap<>();
            try {
                readConfigCpCallStrategyByOrg(SESSION_ID, orgId);
            } catch (Exception e) {
                logger.info(e.getMessage());
                return null;
            }
        }
        return configCpCallStrategy.get(getCpCallStrategyKey(orgId, cpId, orderPhoneNumber, callStatus));
    }


    private void readConfigCampaignByOrg(String SESSION_ID, Integer orgId) {
        CPCampaignParams params = new CPCampaignParams();
        params.setOrgId(orgId);

        List<CPCampaignResp> cpConfigs = freshService.getCampaignConfig(SESSION_ID, params).getResult();

        logger.info("---------- REFRESH CAMPAIGN CONFIG ----------");
        for (CPCampaignResp cpConfig : cpConfigs) {
            configCampaign.put(String.format("%s:%s", Helper.toStringEmpty(cpConfig.getOrgId()), Helper.toStringEmpty(cpConfig.getCpId())), cpConfig);
            logger.info(String.format("Refresh campaign %s config: ", cpConfig.getCpId()) + LogHelper.toJson(cpConfig));
        }

    }

    private void readConfigDistributionRulesByOrg(String SESSION_ID, Integer orgId) {
        GetCpDistributionRuleV2 params = new GetCpDistributionRuleV2();
        params.setOrgId(orgId);

        List<GetCpDistributionRuleResp> distributionRules = lcProvinceService.getCpDistributionRuleV2(SESSION_ID, params).getResult();

        logger.info("---------- REFRESH CAMPAIGN DISTRIBUTION RULE CONFIG ----------");
        for (GetCpDistributionRuleResp distributionRule : distributionRules) {
            configDistributionRules.put(String.format("%s:%s:%s",
                    Helper.toStringEmpty(distributionRule.getOrgId()), Helper.toStringEmpty(distributionRule.getCpId()),
                    Helper.toStringEmpty(distributionRule.getConfigValue())), distributionRule);
            logger.info(String.format("Refresh campaign-distribution %s-%s config: ",
                    distributionRule.getCpId(), distributionRule.getConfigValue()) + LogHelper.toJson(distributionRule));
        }
    }

    private void readConfigCpCallStrategyByOrg(String SESSION_ID, Integer orgId) {
        GetCpCallStrategy params = new GetCpCallStrategy();
        params.setOrgId(orgId);

        List<GetCpCallStrategyResp> callStrategies = lcProvinceService.getCpCallStrategy(SESSION_ID, params).getResult();

        logger.info("---------- REFRESH CAMPAIGN CALL STRATEGY CONFIG ----------");
        for (GetCpCallStrategyResp callStrategy : callStrategies) {
            configCpCallStrategy.put(getCpCallStrategyKey(callStrategy), callStrategy);
            logger.info(String.format("Refresh campaign-orderPhoneNumber-callStatus %s-%s-%s config: ",
                    callStrategy.getCpId(), callStrategy.getOrderPhoneNumber(), callStrategy.getCallStatus()) + LogHelper.toJson(callStrategy));
        }
    }

    private String getCpCallStrategyKey(GetCpCallStrategyResp callStrategy) {
        return String.format("%s:%s:%s:%s",
                Helper.toStringEmpty(callStrategy.getOrgId()), Helper.toStringEmpty(callStrategy.getCpId()),
                Helper.toStringEmpty(callStrategy.getOrderPhoneNumber()), Helper.toStringEmpty(callStrategy.getCallStatus()));
    }

    private String getCpCallStrategyKey(Integer orgId, Integer cpId, Integer orderPhoneNumber, Integer callStatus) {
        return String.format("%s:%s:%s:%s", Helper.toStringEmpty(orgId), Helper.toStringEmpty(cpId),
                Helper.toStringEmpty(orderPhoneNumber), Helper.toStringEmpty(callStatus));
    }
}
