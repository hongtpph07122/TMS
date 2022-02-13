package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "trk_aff_subname_mapping")
public class TrkAffSubnameMapping implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "aff_name")
    private String affName;
    @Column(name = "sub_name")
    private String subName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAffName() {
        return affName;
    }

    public void setAffName(String affName) {
        this.affName = affName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }
}
