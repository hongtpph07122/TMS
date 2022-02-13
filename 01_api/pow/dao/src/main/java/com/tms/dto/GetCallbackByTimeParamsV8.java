package com.tms.dto;

public class GetCallbackByTimeParamsV8 {

    private Integer cpId;
    private Integer orgId;
    private String name;
    private String phone;
    private String callbackTime;
    private String fromRequestTime;
    private String toRequestTime;
    private Integer skillLevel;
    private Integer limit;
    private Integer offset;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public Integer getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(Integer skillLevel) {
		this.skillLevel = skillLevel;
	}

    public String getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(String callbackTime) {
        this.callbackTime = callbackTime;
    }

    public String getFromRequestTime() {
        return fromRequestTime;
    }

    public void setFromRequestTime(String fromRequestTime) {
        this.fromRequestTime = fromRequestTime;
    }

    public String getToRequestTime() {
        return toRequestTime;
    }

    public void setToRequestTime(String toRequestTime) {
        this.toRequestTime = toRequestTime;
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
