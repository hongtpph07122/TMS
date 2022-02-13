package com.tms.dto;

public class GetCpCallingListResp {

    private Integer cpId;
    private Integer orgId;
    private String cpName;
    private Integer cpStatus;
    private Integer cpcfId;
    private Integer configType;
    private Integer configValue;
    private Integer configStatus;
    private Integer callinglistId;
    private String clCode;
    private String clName;
    private String clType;
    private String clTypeName;

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

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public Integer getCpStatus() {
        return cpStatus;
    }

    public void setCpStatus(Integer cpStatus) {
        this.cpStatus = cpStatus;
    }

    public Integer getCpcfId() {
        return cpcfId;
    }

    public void setCpcfId(Integer cpcfId) {
        this.cpcfId = cpcfId;
    }

    public Integer getConfigType() {
        return configType;
    }

    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    public Integer getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Integer configValue) {
        this.configValue = configValue;
    }

    public Integer getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(Integer configStatus) {
        this.configStatus = configStatus;
    }

    public Integer getCallinglistId() {
        return callinglistId;
    }

    public void setCallinglistId(Integer callinglistId) {
        this.callinglistId = callinglistId;
    }

    public String getClCode() {
        return clCode;
    }

    public void setClCode(String clCode) {
        this.clCode = clCode;
    }

    public String getClName() {
        return clName;
    }

    public void setClName(String clName) {
        this.clName = clName;
    }

}
