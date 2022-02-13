package com.tms.api.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumns;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.OneToMany;
import javax.persistence.ManyToMany;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.FetchType;
import javax.persistence.*;

import org.springframework.transaction.annotation.Transactional;
import com.tms.api.entity.OdDONew;

import org.hibernate.annotations.Immutable;

@Entity
//@Immutable
@Transactional
@Table(name = "rc_action_mapping")
//@IdClass(RcActionMapping.class)
public class RcActionMapping implements Serializable {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "org_id")
    private Integer org_id;
    @Column(name = "status_code")
    private String status_code;
    @Column(name = "status_name", nullable = false)
    private String status_name;
    @Column(name = "sub_status_code")
    private String sub_status_code;
    @Column(name = "sub_status_name", nullable = false)
    private String sub_status_name;
    @Column(name = "action")
    private Integer action;
    @Column(name = "priority")
    private Integer priority;
    @Column(name = "createby")
    private Integer createby;
    @Column(name = "createdate")
    private Timestamp createdate;
//    @Access(AccessType.PROPERTY)
//    @OneToMany(fetch=FetchType.LAZY)
//    @JoinColumns(
//            {
//                    @JoinColumn(updatable=false,insertable=false, name="status_name", referencedColumnName="lastmile_reason"),
//                    @JoinColumn(updatable=false,insertable=false, name="sub_status_name", referencedColumnName="lastmile_reason_detail")
//            }
//    )
//    private List<OdDONew> odDONew;



//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumns({
//            @JoinColumn(updatable = false, insertable = false, name = "status_name", referencedColumnName = "lastmile_reason"),
//            @JoinColumn(updatable = false, insertable = false, name = "sub_status_name", referencedColumnName = "lastmile_reason_detail")
//    })
//    private OdDONew odDONew;
//    public OdDONew getOdDONew() {
//        return odDONew;
//    }


//    @ManyToMany
//    @JoinTable(
//            name = "od_do_new",
//            joinColumns = @JoinColumn(updatable = false, insertable = false, name = "status_name"),
//                @JoinColumn(updatable = false, insertable = false, name = "sub_status_name"),
//            inverseJoinColumns =
//                    @JoinColumn(updatable = false, insertable = false, name = "lastmile_reason"),
//                    @JoinColumn(updatable = false, insertable = false, name = "lastmile_reason_detail")
//    )
//    private List<OdDONew> odDONew;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrg_id() {
        return org_id;
    }

    public void setOrg_id(Integer org_id) {
        this.org_id = org_id;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getSub_status_code() {
        return sub_status_code;
    }

    public void setSub_status_code(String sub_status_code) {
        this.sub_status_code = sub_status_code;
    }

    public String getSub_status_name() {
        return sub_status_name;
    }

    public void setSub_status_name(String sub_status_name) {
        this.sub_status_name = sub_status_name;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public Timestamp getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Timestamp createdate) {
        this.createdate = createdate;
    }

    @Override
    public String toString() {
        return "RcActionMapping{" +
                "id=" + id +
                ", org_id=" + org_id +
                ", status_code='" + status_code + '\'' +
                ", status_name='" + status_name + '\'' +
                ", sub_status_code='" + sub_status_code + '\'' +
                ", sub_status_name='" + sub_status_name + '\'' +
                ", action=" + action +
                ", priority=" + priority +
                ", createby=" + createby +
                ", createdate=" + createdate +
                '}';
    }

    //    public void setOdDONew(OdDONew odDONew) {
//        this.odDONew = odDONew;
//    }
}
