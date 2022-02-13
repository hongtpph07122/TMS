package com.oauthcentralization.app.tmsoauth2.entities;

import com.oauthcentralization.app.tmsoauth2.entities.baseEntity.TeamMembersBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.oauthcentralization.app.tmsoauth2.variables.TablesVariable.TEAM_MEMBERS;

@Entity
@Table(name = TEAM_MEMBERS)
public class TeamMembersEntity extends TeamMembersBaseEntity {

    public TeamMembersEntity() {
        super();
    }
}
