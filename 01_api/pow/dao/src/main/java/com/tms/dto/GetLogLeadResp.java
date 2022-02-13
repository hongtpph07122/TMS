package com.tms.dto;


import java.util.Date;

public class GetLogLeadResp {

    private Integer logId;
    private Integer leadId;
    private String onField;
    private String newValue;
    private String statusName;
    private Integer userId;
    private String userName ;
    private String comment;
    private Date changetime;
    private String jsonLog;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getOnField() {
        return onField;
    }

    public void setOnField(String onField) {
        this.onField = onField;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getChangetime() {
        return changetime;
    }

    public void setChangetime(Date changetime) {
        this.changetime = changetime;
    }

    public String getJsonLog() {
        return jsonLog;
    }

    public void setJsonLog(String jsonLog) {
        this.jsonLog = jsonLog;
    }

}
