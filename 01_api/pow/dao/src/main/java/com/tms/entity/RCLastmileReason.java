package com.tms.entity;

import java.util.Date;

public class RCLastmileReason {
    private Integer id;
    private Integer lastmileId;
    private String lastmileStatus;
    private String lastmileReason;
    private String lastmileSubReason;
    private Integer action;
    private Integer priority;
    private String enabled;
    private Integer createby;
    private Date createdate;
    private Integer updateby;
    private Date updatedate;
    private Integer orgId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLastmileId() {
        return lastmileId;
    }

    public void setLastmileId(Integer lastmileId) {
        this.lastmileId = lastmileId;
    }

    public String getLastmileStatus() {
        return lastmileStatus;
    }

    public void setLastmileStatus(String lastmileStatus) {
        this.lastmileStatus = lastmileStatus;
    }

    public String getLastmileReason() {
        return lastmileReason;
    }

    public void setLastmileReason(String lastmileReason) {
        this.lastmileReason = lastmileReason;
    }

    public String getLastmileSubReason() {
        return lastmileSubReason;
    }

    public void setLastmileSubReason(String lastmileSubReason) {
        this.lastmileSubReason = lastmileSubReason;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Integer getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Integer updateby) {
        this.updateby = updateby;
    }

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}
