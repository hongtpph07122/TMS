package com.tms.api.service;

import com.tms.api.entity.CfCpCallStrategyDefault;

public interface CfCpCallStrategyService {
    CfCpCallStrategyDefault getCallStrategyDefault(Integer cpId, Integer orgId, Integer callStatus, Integer orderPhoneNumber);

    CfCpCallStrategyDefault getCallStrategyConfig(Integer orgId, Integer cpId, Integer orderPhoneNumber, Integer callStatus);

    boolean refreshCfCpCallStrategy();
}