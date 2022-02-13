package com.oauthcentralization.app.tmsoauth2.entities.baseEntity;

import javax.persistence.*;
import java.util.Date;

import static com.oauthcentralization.app.tmsoauth2.variables.ColumnsVariable.*;

@MappedSuperclass
public class GroupsBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = GROUP_ID, nullable = false)
    private Integer groupId;

    @Column(name = ORG_ID)
    private Integer organizationId;

    @Column(name = NAME)
    private String name;

    @Column(name = SHORT_NAME)
    private String shortName;

    @Column(name = DESCRIPTION)
    private String description;

    @Column(name = MODIFY_BY)
    private Integer modifiedBy;

    @Column(name = MODIFY_DATE)
    private Date modifiedAt;

    @Column(name = GROUP_TYPE)
    private Integer groupType;

    @Column(name = SKILL_ID)
    private Integer skillId;

    public GroupsBaseEntity() {
        setModifiedAt(new Date());
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer orgId) {
        this.organizationId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }
}
