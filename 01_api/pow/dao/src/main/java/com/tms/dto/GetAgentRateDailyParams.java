package com.tms.dto;

public class GetAgentRateDailyParams {
    private Integer orgId;
    private Integer cpId;
    private Integer groupId;
    private Integer agentId;
    private Integer rate;
    private String listCampaignId;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getListCampaignId() {
        return listCampaignId;
    }

    public void setListCampaignId(String listCampaignId) {
        this.listCampaignId = listCampaignId;
    }
}
