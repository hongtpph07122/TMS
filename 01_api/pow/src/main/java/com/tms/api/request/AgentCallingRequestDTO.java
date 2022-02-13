package com.tms.api.request;

import com.tms.dto.GetCpDistributionRuleResp;

import java.util.List;

public class AgentCallingRequestDTO {
    private int userId;
    private int campaignId; // current campaign, agent joined
    private int orgId;
    private int agentSkillLevel;
    private String callingListStr;
    private GetCpDistributionRuleResp distributionRule;
    private String sessionId;
    private List<Integer> listCampaignsId;
    private int leadRemain;
    private int unCallType;
    private int conditionalTerms;

    public AgentCallingRequestDTO() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getAgentSkillLevel() {
        return agentSkillLevel;
    }

    public void setAgentSkillLevel(int agentSkillLevel) {
        this.agentSkillLevel = agentSkillLevel;
    }

    public String getCallingListStr() {
        return callingListStr;
    }

    public void setCallingListStr(String callingListStr) {
        this.callingListStr = callingListStr;
    }

    public GetCpDistributionRuleResp getDistributionRule() {
        return distributionRule;
    }

    public void setDistributionRule(GetCpDistributionRuleResp distributionRule) {
        this.distributionRule = distributionRule;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<Integer> getListCampaignsId() {
        return listCampaignsId;
    }

    public void setListCampaignsId(List<Integer> listCampaignsId) {
        this.listCampaignsId = listCampaignsId;
    }

    public int getLeadRemain() {
        return leadRemain;
    }

    public void setLeadRemain(int leadRemain) {
        this.leadRemain = leadRemain;
    }

    public int getUnCallType() {
        return unCallType;
    }

    public void setUnCallType(int unCallType) {
        this.unCallType = unCallType;
    }

    public int getConditionalTerms() {
        return conditionalTerms;
    }

    public void setConditionalTerms(int conditionalTerms) {
        this.conditionalTerms = conditionalTerms;
    }
}
