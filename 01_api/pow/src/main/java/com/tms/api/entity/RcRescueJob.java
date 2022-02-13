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

import org.hibernate.annotations.Immutable;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@DynamicUpdate
@Transactional
@Table(name = "rc_rescue_job")
public class RcRescueJob implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rcJob_generator")
    @SequenceGenerator(name="rcJob_generator", sequenceName = "seq_rc_job_id", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name="org_id")
    private Integer org_id;
    @Column(name="do_id")
    private Integer do_id;
    @Column(name="do_code")
    private String do_code;
    @Column(name="ffm_code")
    private String ffm_code;
    @Column(name="tracking_code")
    private String tracking_code;
    @Column(name="ffm_id")
    private Integer ffm_id;
    @Column(name="lastmile_id")
    private Integer lastmile_id;
    @Column(name="customer_id")
    private Integer customer_id;
    @Column(name="customer_name")
    private String customer_name;
    @Column(name="customer_phone")
    private String customer_phone;
    @Column(name="customer_address")
    private String customer_address;
    @Column(name="package_products")
    private String package_products;
    @Column(name="amountcod")
    private Double amountcod;
    @Column(name="do_status")
    private Integer do_status;
    @Column(name="ffm_status")
    private String ffm_status;
    @Column(name="ffm_return_code")
    private String ffm_return_code;
    @Column(name="ffm_reason")
    private String ffm_reason;
    @Column(name="ffm_reason_detail")
    private String ffm_reason_detail;
    @Column(name="lastmile_status")
    private String lastmile_status;
    @Column(name="lastmile_return_code")
    private String lastmile_return_code;
    @Column(name="lastmile_reason")
    private String lastmile_reason;
    @Column(name="lastmile_reason_detail")
    private String lastmile_reason_detail;
    @Column(name="agent_id")
    private Integer agent_id;
    @Column(name="assigned")
    private Integer assigned;
    @Column(name="priority")
    private Integer priority;
    @Column(name="job_status")
    private Integer job_status;
    @Column(name="job_sub_status")
    private Integer job_sub_status;
    @Column(name="job_reason")
    private Integer job_reason;
    @Column(name="job_sub_reason")
    private Integer job_sub_reason;
    @Column(name="job_comment")
    private String job_comment;
    @Column(name="user_note")
    private String user_note;
    @Column(name="total_call")
    private Integer total_call;
    @Column(name="day_call")
    private Integer day_call;
    @Column(name="createby")
    private Integer createby;
    @Column(name="createdate")
    private Timestamp createdate;
    @Column(name="updateby")
    private Integer updateby;
    @Column(name="updatedate")
    private Timestamp updatedate;
    @Column(name="so_id")
    private Integer so_id;
    @Column(name="is_pre_delivery")
    private Boolean isPreDelivery;
    @Column(name="is_pre_delivered_before", columnDefinition = "tinyint(0) default 0")
    private Boolean isPreDeliveredBefore;
    @Column(name = "job_type")
    private Integer jobType;

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

    public Integer getDo_id() {
        return do_id;
    }

    public void setDo_id(Integer do_id) {
        this.do_id = do_id;
    }

    public String getDo_code() {
        return do_code;
    }

    public void setDo_code(String do_code) {
        this.do_code = do_code;
    }

    public String getFfm_code() {
        return ffm_code;
    }

    public void setFfm_code(String ffm_code) {
        this.ffm_code = ffm_code;
    }

    public String getTracking_code() {
        return tracking_code;
    }

    public void setTracking_code(String tracking_code) {
        this.tracking_code = tracking_code;
    }

    public Integer getFfm_id() {
        return ffm_id;
    }

    public void setFfm_id(Integer ffm_id) {
        this.ffm_id = ffm_id;
    }

    public Integer getLastmile_id() {
        return lastmile_id;
    }

    public void setLastmile_id(Integer lastmile_id) {
        this.lastmile_id = lastmile_id;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getPackage_products() {
        return package_products;
    }

    public void setPackage_products(String package_products) {
        this.package_products = package_products;
    }

    public Double getAmountcod() {
        return amountcod;
    }

    public void setAmountcod(Double amountcod) {
        this.amountcod = amountcod;
    }

    public Integer getDo_status() {
        return do_status;
    }

    public void setDo_status(Integer do_status) {
        this.do_status = do_status;
    }

    public String getFfm_status() {
        return ffm_status;
    }

    public void setFfm_status(String ffm_status) {
        this.ffm_status = ffm_status;
    }

    public String getFfm_return_code() {
        return ffm_return_code;
    }

    public void setFfm_return_code(String ffm_return_code) {
        this.ffm_return_code = ffm_return_code;
    }

    public String getFfm_reason() {
        return ffm_reason;
    }

    public void setFfm_reason(String ffm_reason) {
        this.ffm_reason = ffm_reason;
    }

    public String getFfm_reason_detail() {
        return ffm_reason_detail;
    }

    public void setFfm_reason_detail(String ffm_reason_detail) {
        this.ffm_reason_detail = ffm_reason_detail;
    }

    public String getLastmile_status() {
        return lastmile_status;
    }

    public void setLastmile_status(String lastmile_status) {
        this.lastmile_status = lastmile_status;
    }

    public String getLastmile_return_code() {
        return lastmile_return_code;
    }

    public void setLastmile_return_code(String lastmile_return_code) {
        this.lastmile_return_code = lastmile_return_code;
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

    public Integer getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(Integer agent_id) {
        this.agent_id = agent_id;
    }

    public Integer getAssigned() {
        return assigned;
    }

    public void setAssigned(Integer assigned) {
        this.assigned = assigned;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getJob_status() {
        return job_status;
    }

    public void setJob_status(Integer job_status) {
        this.job_status = job_status;
    }

    public Integer getJob_sub_status() {
        return job_sub_status;
    }

    public void setJob_sub_status(Integer job_sub_status) {
        this.job_sub_status = job_sub_status;
    }

    public Integer getJob_reason() {
        return job_reason;
    }

    public void setJob_reason(Integer job_reason) {
        this.job_reason = job_reason;
    }

    public Integer getJob_sub_reason() {
        return job_sub_reason;
    }

    public void setJob_sub_reason(Integer job_sub_reason) {
        this.job_sub_reason = job_sub_reason;
    }

    public String getJob_comment() {
        return job_comment;
    }

    public void setJob_comment(String job_comment) {
        this.job_comment = job_comment;
    }

    public String getUser_note() {
        return user_note;
    }

    public void setUser_note(String user_note) {
        this.user_note = user_note;
    }

    public Integer getTotal_call() {
        return total_call;
    }

    public void setTotal_call(Integer total_call) {
        this.total_call = total_call;
    }

    public Integer getDay_call() {
        return day_call;
    }

    public void setDay_call(Integer day_call) {
        this.day_call = day_call;
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

    public Integer getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Integer updateby) {
        this.updateby = updateby;
    }

    public Timestamp getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Timestamp updatedate) {
        this.updatedate = updatedate;
    }

    public Integer getSo_id() {
        return so_id;
    }

    public void setSo_id(Integer so_id) {
        this.so_id = so_id;
    }

    public Boolean getPreDelivery() {
        return isPreDelivery;
    }

    public void setPreDelivery(Boolean preDelivery) {
        isPreDelivery = preDelivery;
    }

    public Boolean getPreDeliveredBefore() {
        return isPreDeliveredBefore;
    }

    public void setPreDeliveredBefore(Boolean preDeliveredBefore) {
        isPreDeliveredBefore = preDeliveredBefore;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }
}
