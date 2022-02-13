package com.tms.dto;

import java.util.Date;

public class GetLogDoResp {

    private Integer logId;
    private Integer doId;
    private Integer newStatus;
    private String statusName;
    private Integer updateby;
    private String userName;
    private String comment;
    private Date changetime;
    private String jsonLog;
    private String newStatusInpartner;

    public String getNewStatusInpartner() {
        return newStatusInpartner;
    }

    public void setNewStatusInpartner(String newStatusInpartner) {
        this.newStatusInpartner = newStatusInpartner;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getDoId() {
        return doId;
    }

    public void setDoId(Integer doId) {
        this.doId = doId;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Integer updateby) {
        this.updateby = updateby;
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
