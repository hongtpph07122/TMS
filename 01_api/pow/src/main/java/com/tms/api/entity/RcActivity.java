package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Immutable;

import java.sql.Timestamp;

@Entity
@Table(name = "rc_activity")
public class RcActivity{
    @Id
    @SequenceGenerator(name="activity_generator", sequenceName = "seq_rc_activity_id", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "activity_generator")
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;
    @Column(name="rc_job_id")
    private Integer rc_job_id;
    @Column(name="activity_type")
    private Integer activity_type;
    @Column(name="new_status")
    private Integer new_status;
    @Column(name="new_substatus")
    private Integer new_substatus;
    @Column(name="new_reason")
    private Integer new_reason;
    @Column(name="new_subreason")
    private Integer new_subreason;
    @Column(name="comment")
    private String comment;
    @Column(name="updateby")
    private Integer updateby;
    @Column(name="act_time")
    private Timestamp act_time;
    @Column(name="json_log")
    private String json_log;
    @Column(name="contact_name")
    private String contact_name;
    @Column(name="contact_phone")
    private String contact_phone;
    @Column(name="owner_id")
    private Integer owner_id;
    @Column(name="lastmile_reason")
    private String lastmile_reason;
    @Column(name="lastmile_reason_detail")
    private String lastmile_reason_detail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRc_job_id() {
        return rc_job_id;
    }

    public void setRc_job_id(Integer rc_job_id) {
        this.rc_job_id = rc_job_id;
    }

    public Integer getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(Integer activity_type) {
        this.activity_type = activity_type;
    }

    public Integer getNew_status() {
        return new_status;
    }

    public void setNew_status(Integer new_status) {
        this.new_status = new_status;
    }

    public Integer getNew_substatus() {
        return new_substatus;
    }

    public void setNew_substatus(Integer new_substatus) {
        this.new_substatus = new_substatus;
    }

    public Integer getNew_reason() {
        return new_reason;
    }

    public void setNew_reason(Integer new_reason) {
        this.new_reason = new_reason;
    }

    public Integer getNew_subreason() {
        return new_subreason;
    }

    public void setNew_subreason(Integer new_subreason) {
        this.new_subreason = new_subreason;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Integer updateby) {
        this.updateby = updateby;
    }

    public Timestamp getAct_time() {
        return act_time;
    }

    public void setAct_time(Timestamp act_time) {
        this.act_time = act_time;
    }

    public String getJson_log() {
        return json_log;
    }

    public void setJson_log(String json_log) {
        this.json_log = json_log;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public Integer getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(Integer owner_id) {
        this.owner_id = owner_id;
    }

    public String getLastmile_reason() {
        return lastmile_reason;
    }

    public void setLastmile_reason(String lastmile_reason) {
        this.lastmile_reason = lastmile_reason;
    }

    public String getLastmile_reason_detail() {
        return lastmile_reason_detail;
    }

    public void setLastmile_reason_detail(String lastmile_reason_detail) {
        this.lastmile_reason_detail = lastmile_reason_detail;
    }
}
