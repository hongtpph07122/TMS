package com.tms.api.dto;

import java.util.ArrayList;
import java.util.List;

public class UpdateCampaignConfigDto {
	private int campaignId;
	private int strategyId;
	private int ruleId;
	private List<Integer> agentGroupIdList;
	private List<Integer> callingList;
	private int freshRatio;
	private int oldRatio;
	
	public UpdateCampaignConfigDto() {
	    agentGroupIdList = new ArrayList<Integer>();
	    callingList = new ArrayList<Integer>();
	}
	
    public int getCampaignId() {
        return campaignId;
    }
    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }
    
    public int getStrategyId() {
        return strategyId;
    }
    public void setStrategyId(int strategyId) {
        this.strategyId = strategyId;
    }
    
    public int getRuleId() {
        return ruleId;
    }
    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }
    
    public List<Integer> getAgentGroupIdList() {
        return agentGroupIdList;
    }
    public void setAgentGroupIdList(List<Integer> agentGroupIdList) {
        this.agentGroupIdList = agentGroupIdList;
    }
    
    public List<Integer> getCallingList() {
        return callingList;
    }
    public void setCallingList(List<Integer> callingList) {
        this.callingList = callingList;
    }
    
    public int getFreshRatio() {
        return freshRatio;
    }
    public void setFreshRatio(int freshRatio) {
        this.freshRatio = freshRatio;
    }
    
    public int getOldRatio() {
        return oldRatio;
    }
    public void setOldRatio(int oldRatio) {
        this.oldRatio = oldRatio;
    }
}
