package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;

@Entity
@DynamicUpdate
@Transactional
@Table(name = "pd_category")
public class PdCategory {
    @Id
    @SequenceGenerator(name = "seq_category_id", sequenceName = "seq_category_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_category_id")
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "dscr")
    private String dscr;
    @Column(name = "status")
    private Integer status;
    @Column(name = "modifyby")
    private Integer modifyby;
    @Column(name = "modifydate")
    private Timestamp modifydate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public Timestamp getModifydate() {
        return modifydate;
    }

    public void setModifydate(Timestamp modifydate) {
        this.modifydate = modifydate;
    }
}