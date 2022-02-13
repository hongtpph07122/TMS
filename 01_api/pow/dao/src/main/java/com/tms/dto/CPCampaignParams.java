package com.tms.dto;

public class CPCampaignParams {

    private Integer orgId;
    private Integer capaignId;
    private Integer limit;
    private Integer offset;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getCapaignId() {
        return capaignId;
    }

    public void setCapaignId(Integer capaignId) {
        this.capaignId = capaignId;
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
