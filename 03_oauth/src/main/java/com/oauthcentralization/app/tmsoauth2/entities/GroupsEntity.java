package com.oauthcentralization.app.tmsoauth2.entities;

import com.oauthcentralization.app.tmsoauth2.entities.baseEntity.GroupsBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.oauthcentralization.app.tmsoauth2.variables.TablesVariable.GROUPS;

@Entity
@Table(name = GROUPS)
public class GroupsEntity extends GroupsBaseEntity {

    public GroupsEntity() {
        super();
    }
}
