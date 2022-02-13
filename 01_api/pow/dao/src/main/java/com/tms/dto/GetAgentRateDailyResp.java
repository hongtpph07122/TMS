package com.tms.dto;

import java.util.Date;

public class GetAgentRateDailyResp {

    private Integer agRateId;
    private Integer orgId;
    private Integer cpId;
    private Integer groupId;
    private Integer agentId;
    private Date EvaluateDate;
    private Integer rate;
    private Integer createby;
    private Date createdate;
    private Integer updateby;
    private Date updatedate;
    private Integer isDeleted;

    public Integer getAgRateId() {
        return agRateId;
    }

    public void setAgRateId(Integer agRateId) {
        this.agRateId = agRateId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Date getEvaluateDate() {
        return EvaluateDate;
    }

    public void setEvaluateDate(Date evaluateDate) {
        EvaluateDate = evaluateDate;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
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

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
