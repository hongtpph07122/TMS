package com.oauthcentralization.app.tmsoauth2.entities;

import com.oauthcentralization.app.tmsoauth2.entities.baseEntity.SmgActionLogBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.oauthcentralization.app.tmsoauth2.variables.TablesVariable.SMG_ACTION_LOG;

@Entity
@Table(name = SMG_ACTION_LOG)
public class SMGActionTrunkEntity extends SmgActionLogBaseEntity {

    public SMGActionTrunkEntity() {
        super();
    }
}
