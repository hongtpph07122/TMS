package com.tms.api.entity;

import com.tms.api.entity.base.ConfigLogDevBaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "config_log_dev")
public class ConfigLogDevEntity extends ConfigLogDevBaseEntity {
    public ConfigLogDevEntity() {
        super();
    }
}
