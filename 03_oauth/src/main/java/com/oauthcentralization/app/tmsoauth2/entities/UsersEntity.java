package com.oauthcentralization.app.tmsoauth2.entities;


import com.oauthcentralization.app.tmsoauth2.entities.baseEntity.UsersBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.oauthcentralization.app.tmsoauth2.variables.TablesVariable.USERS;


@Entity
@Table(name = USERS)
public class UsersEntity extends UsersBaseEntity {
    public UsersEntity() {
        super();
    }
}
