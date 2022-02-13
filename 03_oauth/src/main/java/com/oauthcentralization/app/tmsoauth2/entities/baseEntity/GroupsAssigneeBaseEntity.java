package com.oauthcentralization.app.tmsoauth2.entities.baseEntity;

import javax.persistence.*;
import java.util.Date;

import static com.oauthcentralization.app.tmsoauth2.variables.ColumnsVariable.*;

@MappedSuperclass
public class GroupsAssigneeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = GROUP_ASSIGNEE_ID, nullable = false)
    private Integer groupAssigneeId;

    @Column(name = GROUP_ID)
    private Integer groupId;

    @Column(name = AGENT_ID)
    private Integer agentId;

    @Column(name = MODIFY_BY)
    private Integer modifiedBy;

    @Column(name = MODIFY_DATE)
    private Date modifiedAt;

    @Column(name = AGENT_SKILL_LEVEL)
    private Integer assigneeSkillsLevel;

    public GroupsAssigneeBaseEntity() {
        setModifiedAt(new Date());
    }

    public Integer getGroupAssigneeId() {
        return groupAssigneeId;
    }

    public void setGroupAssigneeId(Integer groupAssigneeId) {
        this.groupAssigneeId = groupAssigneeId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
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

    public Integer getAssigneeSkillsLevel() {
        return assigneeSkillsLevel;
    }

    public void setAssigneeSkillsLevel(Integer assigneeSkillsLevel) {
        this.assigneeSkillsLevel = assigneeSkillsLevel;
    }
}
