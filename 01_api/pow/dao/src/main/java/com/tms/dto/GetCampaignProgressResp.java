package com.tms.dto;

import java.util.Date;

public class GetCampaignProgressResp {

    private Integer orgId;
    private Integer cpId;
    private String name;
    private Date startdate;
    private Date stopdate;
    private Integer status;
    private String statusName;
    private Double progress;
    private Double outboundAgent;
    private Integer leadInCampaign;
    private Integer leadInProgress;

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

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getStopdate() {
        return stopdate;
    }

    public void setStopdate(Date stopdate) {
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

}
