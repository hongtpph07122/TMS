package com.tms.dto;

import java.lang.Integer;
import java.lang.String;

public class GetLeadAgencyParams {
    private Integer orgId;
    private Integer agcId;
    private String leadId;
    private String clickId;
    private String leadStatus;
    private String leadStatusName;
    private String comment;
    private String userDefin05;
    private Integer limit;
    private Integer offset;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getAgcId() {
        return agcId;
    }

    public void setAgcId(Integer agcId) {
        this.agcId = agcId;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public String getClickId() {
        return clickId;
    }

    public void setClickId(String clickId) {
        this.clickId = clickId;
    }

    public String getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getLeadStatusName() {
        return leadStatusName;
    }

    public void setLeadStatusName(String leadStatusName) {
        this.leadStatusName = leadStatusName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserDefin05() {
        return userDefin05;
    }

    public void setUserDefin05(String userDefin05) {
        this.userDefin05 = userDefin05;
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
}
