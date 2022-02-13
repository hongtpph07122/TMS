package com.tms.entity.log;

public class LogSaleOrder {

    private Integer logId;
    private Integer soId;
    private Integer newStatus;
    private Integer userId;
    private String comment;
    private String changetime;
    private String jsonLog;


    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getSoId() {
        return soId;
    }

    public void setSoId(Integer soId) {
        this.soId = soId;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
