package com.tms.dto;

public class CPCampaignResp {

    private Integer cpId;
    private Integer orgId;
    private String name;
    private Integer callStrategy;
    private Integer distributionRule;
    private Integer groupAgent;
    private Integer callingList;

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCallStrategy() {
        return callStrategy;
    }

    public void setCallStrategy(Integer callStrategy) {
        this.callStrategy = callStrategy;
    }

    public Integer getDistributionRule() {
        return distributionRule;
    }

    public void setDistributionRule(Integer distributionRule) {
        this.distributionRule = distributionRule;
    }

    public Integer getGroupAgent() {
        return groupAgent;
    }

    public void setGroupAgent(Integer groupAgent) {
        this.groupAgent = groupAgent;
    }

    public Integer getCallingList() {
        return callingList;
    }

    public void setCallingList(Integer callingList) {
        this.callingList = callingList;
    }
}
