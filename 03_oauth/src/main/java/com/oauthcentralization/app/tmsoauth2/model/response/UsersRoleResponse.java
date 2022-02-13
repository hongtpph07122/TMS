package com.oauthcentralization.app.tmsoauth2.model.response;

public class UsersRoleResponse {

    private Integer userId;
    private String username;
    private String roleSuffix; /* not is ROLE_ */
    private Integer roleId;

    public UsersRoleResponse(Integer userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UsersRoleResponse() {
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoleSuffix() {
        return roleSuffix;
    }

    public void setRoleSuffix(String roleSuffix) {
        this.roleSuffix = roleSuffix;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
