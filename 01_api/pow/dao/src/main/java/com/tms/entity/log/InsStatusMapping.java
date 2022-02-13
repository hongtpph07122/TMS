package com.tms.entity.log;

public class InsStatusMapping {

    private Integer sttMappingId;
    private Integer orgId;
    private Integer partnerId;
    private String pnStatusCode;
    private String pnStatusName;
    private String pnStatusDescription;
    private Integer tmsStatus;
    private String tmsStatusName;
    private Integer limit;
    private Integer offset;

    public Integer getSttMappingId() {
        return sttMappingId;
    }

    public void setSttMappingId(Integer sttMappingId) {
        this.sttMappingId = sttMappingId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getPnStatusCode() {
        return pnStatusCode;
    }

    public void setPnStatusCode(String pnStatusCode) {
        this.pnStatusCode = pnStatusCode;
    }

    public String getPnStatusName() {
        return pnStatusName;
    }

    public void setPnStatusName(String pnStatusName) {
        this.pnStatusName = pnStatusName;
    }

    public String getPnStatusDescription() {
        return pnStatusDescription;
    }

    public void setPnStatusDescription(String pnStatusDescription) {
        this.pnStatusDescription = pnStatusDescription;
    }

    public Integer getTmsStatus() {
        return tmsStatus;
    }

    public void setTmsStatus(Integer tmsStatus) {
        this.tmsStatus = tmsStatus;
    }

    public String getTmsStatusName() {
        return tmsStatusName;
    }

    public void setTmsStatusName(String tmsStatusName) {
        this.tmsStatusName = tmsStatusName;
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
