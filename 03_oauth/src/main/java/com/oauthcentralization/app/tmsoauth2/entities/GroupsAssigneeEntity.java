package com.oauthcentralization.app.tmsoauth2.entities;

import com.oauthcentralization.app.tmsoauth2.entities.baseEntity.GroupsAssigneeBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.oauthcentralization.app.tmsoauth2.variables.TablesVariable.GROUP_ASSIGNEES;

@Entity
@Table(name = GROUP_ASSIGNEES)
public class GroupsAssigneeEntity extends GroupsAssigneeBaseEntity {

    public GroupsAssigneeEntity() {
        super();
    }
}
