package com.tms.dto;

public class GetNewestLeadV5 {
    private Integer cpId;
    private String cpName;
    private String callinglistId;
    private String source;
    private String agcCode;
    private Integer orgId;
    private Integer assigned;
    private Integer leadStatus;
    private String leadType;
    private String clType;
    private String clTypeName;
    private Integer skillLevel;
    private Integer limit;
    private Integer offset;

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCallinglistId() {
        return callinglistId;
    }

    public void setCallinglistId(String callinglistId) {
        this.callinglistId = callinglistId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAgcCode() {
        return agcCode;
    }

    public void setAgcCode(String agcCode) {
        this.agcCode = agcCode;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getAssigned() {
        return assigned;
    }

    public void setAssigned(Integer assigned) {
        this.assigned = assigned;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getLeadType() {
        return leadType;
    }

    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    public String getClType() {
        return clType;
    }

    public void setClType(String clType) {
        this.clType = clType;
    }

    public String getClTypeName() {
        return clTypeName;
    }

    public void setClTypeName(String clTypeName) {
        this.clTypeName = clTypeName;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
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
