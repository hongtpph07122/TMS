package com.oauthcentralization.app.tmsoauth2.entities;

import com.oauthcentralization.app.tmsoauth2.entities.baseEntity.TeamsBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.oauthcentralization.app.tmsoauth2.variables.TablesVariable.TEAMS;

@Entity
@Table(name = TEAMS)
public class TeamsEntity extends TeamsBaseEntity {

    public TeamsEntity() {
        super();
    }
}
