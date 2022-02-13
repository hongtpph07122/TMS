package com.tms.model;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "rc_lastmile_status")
public class RcLastmileStatus{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq_rc_lastmile_status_id")
    private Long id;

    @Column(name = "submission_date")
    private Date submissionDate;

    @Column(name = "tracking_code")
    private String trackingCode;

    @Column(name = "consignee_address")
    private String consigneeAddress;

    private String substatus;

    @Column(name = "expected_date")
    private Date expectedDate;

    @Column(name = "attempted_delivery_1_date")
    private Date attemptedDeliveryDate;

    @Column(name = "cod_amount")
    private Double codAmount;

    @Column(name = "package_description")
    private String packageDescription;


    @Column(name = "current_warehouse")
    private String currentWarehouse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getSubstatus() {
        return substatus;
    }

    public void setSubstatus(String substatus) {
        this.substatus = substatus;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public Date getAttemptedDeliveryDate() {
        return attemptedDeliveryDate;
    }

    public void setAttemptedDeliveryDate(Date attemptedDeliveryDate) {
        this.attemptedDeliveryDate = attemptedDeliveryDate;
    }

    public Double getCodAmount() {
        return codAmount;
    }

    public void setCodAmount(Double codAmount) {
        this.codAmount = codAmount;
    }

    public String getPackageDescription() {
        return packageDescription;
    }

    public void setPackageDescription(String packageDescription) {
        this.packageDescription = packageDescription;
    }

    public String getCurrentWarehouse() {
        return currentWarehouse;
    }

    public void setCurrentWarehouse(String currentWarehouse) {
        this.currentWarehouse = currentWarehouse;
    }
}
