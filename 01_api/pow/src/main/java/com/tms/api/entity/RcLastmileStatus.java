package com.tms.api.entity;
import java.io.Serializable;
import java.sql.Timestamp;

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

@Entity
@DynamicUpdate
@Transactional
@Table(name = "rc_lastmile_status")
public class RcLastmileStatus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lastmile_generator")
    @SequenceGenerator(name="lastmile_generator", sequenceName = "seq_rc_lastmile_status_id", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name="submission_date")
    private Timestamp submission_date;
    @Column(name="tracking_code")
    private String tracking_code;
    @Column(name="consignee_address")
    private String consignee_address;
    @Column(name="substatus")
    private String substatus;
    @Column(name="expected_date")
    private Timestamp expected_date;
    @Column(name="attempted_delivery_1_date")
    private Timestamp attempted_delivery_1_date;
    @Column(name="cod_amount")
    private double cod_amount;
    @Column(name="package_description")
    private String package_description;
    @Column(name="current_warehouse")
    private String current_warehouse;
    @Column(name="status")
    private String status;
    @Column(name="consignee_name")
    private String consignee_name;
    @Column(name="consignee_phone")
    private String consignee_phone;
    @Column(name="is_updated", nullable=false)
    private Integer is_updated;
    @Column(name="update_status")
    private Integer update_status;
    @Column(name="upload_date")
    private Timestamp upload_date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getSubmission_date() {
        return submission_date;
    }

    public void setSubmission_date(Timestamp submission_date) {
        this.submission_date = submission_date;
    }

    public String getTracking_code() {
        return tracking_code;
    }

    public void setTracking_code(String tracking_code) {
        this.tracking_code = tracking_code;
    }

    public String getConsignee_address() {
        return consignee_address;
    }

    public void setConsignee_address(String consignee_address) {
        this.consignee_address = consignee_address;
    }

    public String getSubstatus() {
        return substatus;
    }

    public void setSubstatus(String substatus) {
        this.substatus = substatus;
    }

    public Timestamp getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(Timestamp expected_date) {
        this.expected_date = expected_date;
    }

    public Timestamp getAttempted_delivery_1_date() {
        return attempted_delivery_1_date;
    }

    public void setAttempted_delivery_1_date(Timestamp attempted_delivery_1_date) {
        this.attempted_delivery_1_date = attempted_delivery_1_date;
    }

    public double getCod_amount() {
        return cod_amount;
    }

    public void setCod_amount(double cod_amount) {
        this.cod_amount = cod_amount;
    }

    public String getPackage_description() {
        return package_description;
    }

    public void setPackage_description(String package_description) {
        this.package_description = package_description;
    }

    public String getCurrent_warehouse() {
        return current_warehouse;
    }

    public void setCurrent_warehouse(String current_warehouse) {
        this.current_warehouse = current_warehouse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsignee_name() {
        return consignee_name;
    }

    public void setConsignee_name(String consignee_name) {
        this.consignee_name = consignee_name;
    }

    public String getConsignee_phone() {
        return consignee_phone;
    }

    public void setConsignee_phone(String consignee_phone) {
        this.consignee_phone = consignee_phone;
    }

    public Integer getIs_updated() {
        return is_updated;
    }

    public void setIs_updated(Integer is_updated) {
        this.is_updated = is_updated;
    }

    public Integer getUpdate_status() {
        return update_status;
    }

    public void setUpdate_status(Integer update_status) {
        this.update_status = update_status;
    }

    public Timestamp getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Timestamp upload_date) {
        this.upload_date = upload_date;
    }

    @Override
    public String toString() {
        return "RcLastmileStatus{" +
                "id=" + id +
                ", submission_date=" + submission_date +
                ", tracking_code='" + tracking_code + '\'' +
                ", consignee_address='" + consignee_address + '\'' +
                ", substatus='" + substatus + '\'' +
                ", expected_date=" + expected_date +
                ", attempted_delivery_1_date=" + attempted_delivery_1_date +
                ", cod_amount=" + cod_amount +
                ", package_description='" + package_description + '\'' +
                ", current_warehouse='" + current_warehouse + '\'' +
                ", status='" + status + '\'' +
                ", consignee_name='" + consignee_name + '\'' +
                ", consignee_phone='" + consignee_phone + '\'' +
                ", is_updated=" + is_updated +
                ", update_status=" + update_status +
                ", upload_date=" + upload_date +
                '}';
    }
}
