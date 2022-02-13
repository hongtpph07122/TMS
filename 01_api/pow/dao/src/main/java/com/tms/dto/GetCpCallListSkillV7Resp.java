package com.tms.dto;

public class GetCpCallListSkillV7Resp {

    private Integer orgId;
    private String cpId;
    private Integer userId;
    private String userName;
    private String callinglist;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCallinglist() {
        return callinglist;
    }

    public void setCallinglist(String callinglist) {
        this.callinglist = callinglist;
    }


}
