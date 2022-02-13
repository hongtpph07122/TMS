package com.tms.entity.log;

public class LogDeliveryOrder {

    private Integer logId;
    private Integer doId;
    private Integer newStatus;
    private Integer updateby;
    private String comment;
    private String changetime;
    private String jsonLog;
    private String newStatusInpartner;


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

    public String getNewStatusInpartner() {
        return newStatusInpartner;
    }

    public void setNewStatusInpartner(String newStatusInpartner) {
        this.newStatusInpartner = newStatusInpartner;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getChangetime() {
        return changetime;
    }

    public void setChangetime(String changetime) {
        this.changetime = changetime;
    }

    public String getJsonLog() {
        return jsonLog;
    }

    public void setJsonLog(String jsonLog) {
        this.jsonLog = jsonLog;
    }
}
