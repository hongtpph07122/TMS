package com.oauthcentralization.app.tmsoauth2.entities.baseEntity;

import javax.persistence.*;
import java.util.Date;

import static com.oauthcentralization.app.tmsoauth2.variables.ColumnsVariable.*;

@MappedSuperclass
public class UsersRoleBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = USER_ROLE_ID, nullable = false)
    private Integer userRoleId;

    @Column(name = ROLE_ID)
    private Integer roleId;

    @Column(name = USER_ID)
    private Integer userId;

    @Column(name = MODIFY_BY)
    private Integer modifiedBy;

    @Column(name = MODIFY_DATE)
    private Date modifiedAt;

    public UsersRoleBaseEntity() {
        setModifiedAt(new Date());
    }

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
