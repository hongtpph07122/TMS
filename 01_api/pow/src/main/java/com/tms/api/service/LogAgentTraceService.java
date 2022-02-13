package com.tms.api.service;

import com.tms.api.entity.LogAgentTrace;
import com.tms.api.entity.OrUser;
import com.tms.api.response.PBXResponse;

import java.text.ParseException;

public interface LogAgentTraceService {
    // To get activities, agent ID is required
    LogAgentTrace getLatestActivity(Integer agentId, Integer activityId, String redisKey);
    LogAgentTrace getLatestActivity(Integer agentId, Integer activityId, String redisKey, Integer lastMinutes);
    OrUser getAgent(String sip, String phone) throws ParseException;;
    void setAgentState(LogAgentTrace agentTrace);
    LogAgentTrace logActivity(LogAgentTrace agentTrace, String redisKey) throws ParseException;
    LogAgentTrace logPBXActivity(PBXResponse pbxResponse, OrUser agent) throws ParseException;
    LogAgentTrace validateAgentState(LogAgentTrace agentTrace, String redisKey);
    Boolean isAllowBreak(Integer agentId, Integer activityId, String redisKey);
    LogAgentTrace temporaryStateRequest(LogAgentTrace agentTrace, String redisKey);
    LogAgentTrace convertTemporaryStateRequest(LogAgentTrace agentTrace);
    Boolean isAllowBusy(Integer agentId, Integer activityId, String redisKey);
    Boolean isAllowAvailable(Integer agentId, Integer activityId, String redisKey);
    Boolean isAllowUnavailable(Integer agentId, Integer activityId, String redisKey);
    Boolean isAllowOnCall(Integer agentId, Integer activityId, String redisKey);
    Boolean isAllowWrapUp(Integer agentId, Integer activityId, String redisKey);
    Boolean isAllowAssigned(Integer agentId, Integer activityId, String redisKey);
}

