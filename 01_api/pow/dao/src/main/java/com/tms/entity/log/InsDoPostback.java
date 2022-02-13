package com.tms.entity.log;

public class InsDoPostback {

    private Integer postbackId;
    private String trackingCode;
    private String source;
    private String pbFieldName;
    private String pbValue;
    private String pbAll;
    private Integer createby;
    private String createdate;

    public Integer getPostbackId() {
        return postbackId;
    }

    public void setPostbackId(Integer postbackId) {
        this.postbackId = postbackId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPbFieldName() {
        return pbFieldName;
    }

    public void setPbFieldName(String pbFieldName) {
        this.pbFieldName = pbFieldName;
    }

    public String getPbValue() {
        return pbValue;
    }

    public void setPbValue(String pbValue) {
        this.pbValue = pbValue;
    }

    public String getPbAll() {
        return pbAll;
    }

    public void setPbAll(String pbAll) {
        this.pbAll = pbAll;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
