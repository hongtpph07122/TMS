package com.oauthcentralization.app.tmsoauth2.entities;


import com.oauthcentralization.app.tmsoauth2.entities.baseEntity.RolesBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.oauthcentralization.app.tmsoauth2.variables.TablesVariable.ROLE;


@Entity
@Table(name = ROLE)
public class RolesEntity extends RolesBaseEntity {

    public RolesEntity() {
        super();
    }
}
