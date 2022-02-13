package com.tms.entity.log;

public class UpdDeliveryOrder {

    private Integer doId;
    private Integer soId;
    private Integer partnerId;
    private String trackingCode;
    private Integer customerId;
    private Integer prodId;
    private Integer status;
    private Integer createby;
    private String createdate;
    private Integer modifyby;
    private String modifydate;
    private String issueTime;
    private String approvedTime;
    private String expectedTimeArrival;
    private String serviceType;
    private String couponCode;
    private Integer partnerCurrentStatus;
    private String currentStatusCode;
    private Double extrafee;
    private Double totalservicefee;
    private String expectedDeliveryTime;


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

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(String issueTime) {
        this.issueTime = issueTime;
    }

    public String getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public String getExpectedTimeArrival() {
        return expectedTimeArrival;
    }

    public void setExpectedTimeArrival(String expectedTimeArrival) {
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

    public String getExpectedDeliveryTime() {
        return expectedDeliveryTime;
    }

    public void setExpectedDeliveryTime(String expectedDeliveryTime) {
        this.expectedDeliveryTime = expectedDeliveryTime;
    }
}
