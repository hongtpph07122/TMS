package com.tms.dto;

public class GetPrivByUserResp {

    private Integer userId;
    private Integer privId;
    private Integer roleId;
    private Integer privValue;
    private String name;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPrivId() {
        return privId;
    }

    public void setPrivId(Integer privId) {
        this.privId = privId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrivValue() {
        return privValue;
    }

    public void setPrivValue(Integer privValue) {
        this.privValue = privValue;
    }
}
