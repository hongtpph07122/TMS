package com.tms.dto;

import java.lang.Integer;

public class GetLeadAgencyResp {

    private Integer orgId;
    private Integer agcId;
    private Integer leadId;
    private String clickId;
    private Integer leadStatus;
    private String leadStatusName;
    private String comment;
    private String userDefin05;


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

    public String getClickId() {
        return clickId;
    }

    public void setClickId(String clickId) {
        this.clickId = clickId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
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
}
