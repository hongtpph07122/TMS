package com.tms.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "cp_configuration")
public class CpConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "cpcf_id")
    private Integer cpcfId;
    
    @Column(columnDefinition = "cp_id")
    private Integer cpId;
    
    @Column(columnDefinition = "type")
    private Integer type;
    
    @Column(columnDefinition = "value")
    private Integer value;
    
    @Column(columnDefinition = "dscr")
    private String dscr;
    
    @Column(columnDefinition = "status")
    private Integer status;

    public Integer getCpcfId() {
        return cpcfId;
    }

    public void setCpcfId(Integer cpcfId) {
        this.cpcfId = cpcfId;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDscr() {
        return dscr;
    }

    public void setDscr(String dscr) {
        this.dscr = dscr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
