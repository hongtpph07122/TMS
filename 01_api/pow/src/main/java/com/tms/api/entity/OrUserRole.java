package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@DynamicUpdate
@Transactional
@Table(name = "or_user_role")
public class OrUserRole {
    @Id
    @SequenceGenerator(name = "seq_user_role_id", sequenceName = "seq_user_role_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_role_id")
    @Column(name = "ur_id", updatable = false, nullable = false)
    private Integer urId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "modifyby")
    private Integer modifyby;
    @Column(name = "modifydate")
    private Timestamp modifydate;


    public Integer getUrId() {
        return urId;
    }

    public void setUrId(Integer urId) {
        this.urId = urId;
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

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public Timestamp getModifydate() {
        return modifydate;
    }

    public void setModifydate(Timestamp modifydate) {
        this.modifydate = modifydate;
    }
}