package com.tms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class GetLeadReservedParam {
    private Integer orgId;
    private Integer leadId;
    private Integer cpId;
    private Integer assigned;
    private Integer leadStatus;
    private Integer reservedby;
    private String reservedUntil;
    private String listCampaignId;
    private Integer limit;
    private Integer offset;
    @JsonIgnore
    private LocalDateTime reservedDateTime;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getAssigned() {
        return assigned;
    }

    public void setAssigned(Integer assigned) {
        this.assigned = assigned;
    }

    public Integer getReservedby() {
        return reservedby;
    }

    public void setReservedby(Integer reservedby) {
        this.reservedby = reservedby;
    }

    public String getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(String reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
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

    public LocalDateTime getReservedDateTime() {
        return reservedDateTime;
    }

    public void setReservedDateTime(LocalDateTime reservedDateTime) {
        this.reservedDateTime = reservedDateTime;
    }

    public String getListCampaignId() {
        return listCampaignId;
    }

    public void setListCampaignId(String listCampaignId) {
        this.listCampaignId = listCampaignId;
    }
}
