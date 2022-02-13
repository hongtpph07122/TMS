package com.tms.dto;

public class GetCampaignProgress {

    private Integer orgId;
    private Integer cpId;
    private String name;
    private String startdate;
    private String stopdate;
    private Integer status;
    private String statusName;
    private Double progress;
    private Double outboundAgent;
    private Integer leadInCampaign;
    private Integer leadInProgress;
    private Integer limit;
    private Integer offset;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getStopdate() {
        return stopdate;
    }

    public void setStopdate(String stopdate) {
        this.stopdate = stopdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public Double getOutboundAgent() {
        return outboundAgent;
    }

    public void setOutboundAgent(Double outboundAgent) {
        this.outboundAgent = outboundAgent;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLeadInCampaign() {
        return leadInCampaign;
    }

    public void setLeadInCampaign(Integer leadInCampaign) {
        this.leadInCampaign = leadInCampaign;
    }

    public Integer getLeadInProgress() {
        return leadInProgress;
    }

    public void setLeadInProgress(Integer leadInProgress) {
        this.leadInProgress = leadInProgress;
    }
}
