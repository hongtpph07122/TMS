package com.tms.entity;

import java.util.Date;

public class DeliveryOrder {

    private Integer doId;
    private Integer soId;
    private Integer partnerId;
    private String trackingCode;
    private Integer customerId;
    private String prodId;
    private Integer status;
    private Integer createby;
    private Date createdate;
    private Integer modifyby;
    private Date modifydate;
    private Date issueTime;
    private Date approvedTime;
    private Date expectedTimeArrival;
    private String serviceType;
    private String couponCode;
    private Integer partnerCurrentStatus;
    private String currentStatusCode;
    private Double extrafee;
    private Double totalservicefee;
    private Date expectedDeliveryTime;

    public Integer getDoId() {
        return doId;
    }

    public void setDoId(Integer doId) {
        this.doId = doId;
    }

    public Integer getSoId() {
        return soId;
    }

    public void setSoId(Integer soId) {
        this.soId = soId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Date getModifydate() {
        return modifydate;
    }

    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public Date getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    public Date getExpectedTimeArrival() {
        return expectedTimeArrival;
    }

    public void setExpectedTimeArrival(Date expectedTimeArrival) {
        this.expectedTimeArrival = expectedTimeArrival;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getPartnerCurrentStatus() {
        return partnerCurrentStatus;
    }

    public void setPartnerCurrentStatus(Integer partnerCurrentStatus) {
        this.partnerCurrentStatus = partnerCurrentStatus;
    }

    public String getCurrentStatusCode() {
        return currentStatusCode;
    }

    public void setCurrentStatusCode(String currentStatusCode) {
        this.currentStatusCode = currentStatusCode;
    }

    public Double getExtrafee() {
        return extrafee;
    }

    public void setExtrafee(Double extrafee) {
        this.extrafee = extrafee;
    }

    public Double getTotalservicefee() {
        return totalservicefee;
    }

    public void setTotalservicefee(Double totalservicefee) {
        this.totalservicefee = totalservicefee;
    }

    public Date getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(Date expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }
}
