package com.tms.entity.log;

public class LogAgentActivity {

    private Integer logId;
    private Integer userid;
    private String activity;
    private String objectType;
    private Integer objectId;
    private String onField;
    private String newProperty;
    private String activeTime;
    private String jsonLog;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
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
}
