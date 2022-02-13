package com.tms.dto;

public class GetLogAgentActivity {

    private Integer logId;
    private Integer userId;
    private String userName;
    private String activity;
    private String objectType;
    private Integer objectId;
    private String onField;
    private String newProperty;
    private String newPropertyName;
    private String activeTime;
    private String jsonLog;
    private Integer limit;
    private Integer offset;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
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

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getOnField() {
        return onField;
    }

    public void setOnField(String onField) {
        this.onField = onField;
    }

    public String getNewProperty() {
        return newProperty;
    }

    public void setNewProperty(String newProperty) {
        this.newProperty = newProperty;
    }

    public String getNewPropertyName() {
        return newPropertyName;
    }

    public void setNewPropertyName(String newPropertyName) {
        this.newPropertyName = newPropertyName;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getJsonLog() {
        return jsonLog;
    }

    public void setJsonLog(String jsonLog) {
        this.jsonLog = jsonLog;
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
