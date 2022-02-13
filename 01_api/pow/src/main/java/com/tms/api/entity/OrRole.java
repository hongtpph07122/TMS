package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "or_role")
public class OrRole{
    @Id
    @Column(name = "role_id", updatable = false, nullable = false)
    private Integer roleId;
    @Column(name = "name")
    private String name;
    @Column(name = "label")
    private String label;
    @Column(name = "dscr")
    private String dscr;
    @Column(name = "modifyby")
    private Integer modifyby;
    @Column(name = "modifydate")
    private Timestamp modifydate;

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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDscr() {
        return dscr;
    }

    public void setDscr(String dscr) {
        this.dscr = dscr;
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