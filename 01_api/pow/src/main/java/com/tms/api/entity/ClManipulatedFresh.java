package com.tms.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cl_manipulated_fresh")
public class ClManipulatedFresh implements Serializable {

    @Id
    @SequenceGenerator(name="manipulate_generator", sequenceName = "seq_log_manipulate", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manipulate_generator")
    @Column(name = "log_id")
    private Integer logId;
    @Column(name = "lead_id")
    private Integer leadId;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "comment")
    private String comment;
    @Column(name = "manipulate_date")
    private Date manipulateDate;
    @Column(name = "createdate")
    private Date createdate;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getManipulateDate() {
        return manipulateDate;
    }

    public void setManipulateDate(Date manipulateDate) {
        this.manipulateDate = manipulateDate;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

}
