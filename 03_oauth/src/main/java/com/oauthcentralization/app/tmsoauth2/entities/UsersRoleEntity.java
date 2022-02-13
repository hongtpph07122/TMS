package com.oauthcentralization.app.tmsoauth2.entities;

import com.oauthcentralization.app.tmsoauth2.entities.baseEntity.UsersRoleBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.oauthcentralization.app.tmsoauth2.variables.TablesVariable.USER_ROLE;

@Entity
@Table(name = USER_ROLE)
public class UsersRoleEntity extends UsersRoleBaseEntity {

    public UsersRoleEntity() {
        super();
    }
}
