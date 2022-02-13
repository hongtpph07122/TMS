package com.tms.api.service;

import com.tms.api.request.AgentCallingRequestDTO;
import com.tms.entity.CLFresh;

import java.util.List;

public interface AgentService {

    List<CLFresh> snapDistributionRuleCITUnCall(AgentCallingRequestDTO agentCallingRequest);

    List<CLFresh> snapDistributionRuleAgentRate(AgentCallingRequestDTO agentCallingRequest);
}
