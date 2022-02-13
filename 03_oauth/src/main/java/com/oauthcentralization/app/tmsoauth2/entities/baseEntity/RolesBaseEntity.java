package com.oauthcentralization.app.tmsoauth2.entities.baseEntity;

import javax.persistence.*;
import java.util.Date;

import static com.oauthcentralization.app.tmsoauth2.variables.ColumnsVariable.*;


@MappedSuperclass
public class RolesBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ROLE_ID, nullable = false)
    private Integer roleId;

    @Column(name = NAME)
    private String name;

    @Column(name = LABEL)
    private String label;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = MODIFY_BY)
    private Integer modifyBy;

    @Column(name = MODIFY_DATE)
    private Date modifyDate;

    public RolesBaseEntity() {
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Integer modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
