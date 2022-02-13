package com.tms.api.response;

import java.util.Date;

public class CampaignResponseDTO {

    private Integer cpId;
    private Integer orgId;
    private String orgName;
    private String cpName;
    private String userName;
    private Integer owner;
    private Integer status;
    private String statusName;
    private Date startdate;
    private Date stopdate;
    private Integer createby;
    private Date createdate;
    private Integer modifyby;
    private Integer campaignTypeValue;
    private String campaignTypeName;
    private Integer campaignCategoryValue;
    private String campaignCategoryName;
    private Integer campaignTagValue;
    private String campaignTagName;


    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getStopdate() {
        return stopdate;
    }

    public void setStopdate(Date stopdate) {
        this.stopdate = stopdate;
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

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getCampaignTypeValue() {
        return campaignTypeValue;
    }

    public void setCampaignTypeValue(Integer campaignTypeValue) {
        this.campaignTypeValue = campaignTypeValue;
    }

    public String getCampaignTypeName() {
        return campaignTypeName;
    }

    public void setCampaignTypeName(String campaignTypeName) {
        this.campaignTypeName = campaignTypeName;
    }

    public Integer getCampaignCategoryValue() {
        return campaignCategoryValue;
    }

    public void setCampaignCategoryValue(Integer campaignCategoryValue) {
        this.campaignCategoryValue = campaignCategoryValue;
    }

    public String getCampaignCategoryName() {
        return campaignCategoryName;
    }

    public void setCampaignCategoryName(String campaignCategoryName) {
        this.campaignCategoryName = campaignCategoryName;
    }

    public Integer getCampaignTagValue() {
        return campaignTagValue;
    }

    public void setCampaignTagValue(Integer campaignTagValue) {
        this.campaignTagValue = campaignTagValue;
    }

    public String getCampaignTagName() {
        return campaignTagName;
    }

    public void setCampaignTagName(String campaignTagName) {
        this.campaignTagName = campaignTagName;
    }
}
