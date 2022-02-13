package com.tms.api.dto.dashboard;

public class AgentRateMonitorDto {
    private Integer agentId;
    private String agentName;
    private Double rate;
    private Integer maxlead;
    private Integer leadProcessed;
    private Integer approved;
    private Integer rejected;
    private Integer callback;
    private Integer trash;
    private Integer unCall;
    private Double totalAmount;
    private Double avgOrderValue;
    private Integer newLeadProcessed;

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getMaxlead() {
        return maxlead;
    }

    public void setMaxlead(Integer maxlead) {
        this.maxlead = maxlead;
    }

    public Integer getLeadProcessed() {
        return leadProcessed;
    }

    public void setLeadProcessed(Integer leadProcessed) {
        this.leadProcessed = leadProcessed;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getRejected() {
        return rejected;
    }

    public void setRejected(Integer rejected) {
        this.rejected = rejected;
    }

    public Integer getCallback() {
        return callback;
    }

    public void setCallback(Integer callback) {
        this.callback = callback;
    }

    public Integer getTrash() {
        return trash;
    }

    public void setTrash(Integer trash) {
        this.trash = trash;
    }

    public Integer getUnCall() {
        return unCall;
    }

    public void setUnCall(Integer unCall) {
        this.unCall = unCall;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getAvgOrderValue() {
        return avgOrderValue;
    }

    public void setAvgOrderValue(Double avgOrderValue) {
        this.avgOrderValue = avgOrderValue;
    }

    public Integer getNewLeadProcessed() {
        return newLeadProcessed;
    }

    public void setNewLeadProcessed(Integer newLeadProcessed) {
        this.newLeadProcessed = newLeadProcessed;
    }
}