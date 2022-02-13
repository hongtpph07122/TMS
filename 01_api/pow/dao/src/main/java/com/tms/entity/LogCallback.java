package com.tms.entity;

public class LogCallback {

    private Integer leadId;
    private String updateOnField;
    private String newValue;
    private String comment;
    private Integer userId;
    private String changeTime;

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getUpdateOnField() {
        return updateOnField;
    }

    public void setUpdateOnField(String updateOnField) {
        this.updateOnField = updateOnField;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
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

    public String getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }
}
