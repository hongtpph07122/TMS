package com.oauthcentralization.app.tmsoauth2.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersRoleRequest {

    private Integer userId;
    private Integer roleId;

    public UsersRoleRequest() {
        setUserId(null);
        setRoleId(null);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
