package com.tms.api.request;

public class CampaignRequestDTO {

    private Integer cpId;
    private Integer orgId;
    private String orgName;
    private String name;
    private Integer owner;
    private String ownerName;
    private Integer status;
    private String statusName;
    private String startdate;
    private String stopdate;
    private Integer createby;
    private String createdate;
    private Integer modifyby;
    private String modifydate;
    private Integer limit;
    private Integer offset;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getStopdate() {
        return stopdate;
    }

    public void setStopdate(String stopdate) {
        this.stopdate = stopdate;
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

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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
