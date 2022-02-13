package com.tms.api.service.impl;

import com.tms.api.entity.CfCpCallStrategyDefault;
import com.tms.api.helper.Helper;
import com.tms.api.helper.LogHelper;
import com.tms.api.repository.CfCpCallStrategyDefaultRepository;
import com.tms.api.service.CfCpCallStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CfCpCallStrategyImpl implements CfCpCallStrategyService {
    private final CfCpCallStrategyDefaultRepository cfCpCallStrategyDefaultRepository;

    private static HashMap<String, CfCpCallStrategyDefault> configCpCallStrategy = null;

    private final Logger logger = LoggerFactory.getLogger(CampaignServiceImpl.class);

    @Autowired
    public CfCpCallStrategyImpl(CfCpCallStrategyDefaultRepository cfCpCallStrategyDefaultRepository) {
        this.cfCpCallStrategyDefaultRepository = cfCpCallStrategyDefaultRepository;
    }

    @Override
    public CfCpCallStrategyDefault getCallStrategyDefault(Integer cpId, Integer orgId, Integer callStatus, Integer orderPhoneNumber){
        List<CfCpCallStrategyDefault> cfCpCallStrategyDefaults = cfCpCallStrategyDefaultRepository.getCallStrategy(cpId, orgId, callStatus, orderPhoneNumber);
        if(cfCpCallStrategyDefaults == null || cfCpCallStrategyDefaults.isEmpty()){
            return null;
        }
        return cfCpCallStrategyDefaults.get(0);
    }

    @Override
    public CfCpCallStrategyDefault getCallStrategyConfig(Integer orgId, Integer cpId, Integer orderPhoneNumber, Integer callStatus) {
        if (configCpCallStrategy == null) {
            configCpCallStrategy = new HashMap<>();
            try {
                readCfCpCallStrategyFromDB();
            } catch (Exception e) {
                logger.info(e.getMessage());
                return null;
            }
        }
        return configCpCallStrategy.get(getStrategyConfigKey(orgId, cpId, orderPhoneNumber, callStatus));
    }

    @Override
    public boolean refreshCfCpCallStrategy() {
        if(configCpCallStrategy != null) configCpCallStrategy.clear();
        else configCpCallStrategy = new HashMap<>();

        try {
            readCfCpCallStrategyFromDB();
            return true;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    private void readCfCpCallStrategyFromDB() {
        List<CfCpCallStrategyDefault> strategyConfigs = cfCpCallStrategyDefaultRepository.findAll();

        logger.info("---------- REFRESH CAMPAIGN CALL STRATEGY CONFIG ----------");
        for (CfCpCallStrategyDefault strategyConfig:strategyConfigs) {
            configCpCallStrategy.put(getStrategyConfigKey(strategyConfig), strategyConfig);
            logger.info(String.format("Refresh campaign-callStatus %s-%s config: ",
                    strategyConfig.getCpId(), strategyConfig.getCallStatus()) + LogHelper.toJson(strategyConfig));
        }
    }

    private String getStrategyConfigKey(CfCpCallStrategyDefault strategyConfig) {
        return String.format("%s:%s:%s:%s",
                Helper.toStringEmpty(strategyConfig.getOrgId()), Helper.toStringEmpty(strategyConfig.getCpId()),
                Helper.toStringEmpty(strategyConfig.getOrderPhoneNumber()), Helper.toStringEmpty(strategyConfig.getCallStatus()));
    }

    private String getStrategyConfigKey(Integer orgId, Integer cpId, Integer orderPhoneNumber, Integer callStatus) {
        return String.format("%s:%s:%s:%s", Helper.toStringEmpty(orgId), Helper.toStringEmpty(cpId),
                Helper.toStringEmpty(orderPhoneNumber), Helper.toStringEmpty(callStatus));
    }
}