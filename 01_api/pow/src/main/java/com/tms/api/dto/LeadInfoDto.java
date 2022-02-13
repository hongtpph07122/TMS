package com.tms.api.dto;

public class LeadInfoDto {

    private Integer leadId;
    private String name;
    private String phone;
    private String address;
    private String doStatus;
    private String packageListItem;
    private Double amount;
    private String paymentMethod;
    private String productName;
    private Integer leadStatus;
    private String doAddress;
    private Integer prodId;
    private String userDefin04;

    public String getUserDefin04() {
        return userDefin04;
    }

    public void setUserDefin04(String userDefin04) {
        this.userDefin04 = userDefin04;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public String getDoAddress() {
        return doAddress;
    }

    public void setDoAddress(String doAddress) {
        this.doAddress = doAddress;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoStatus() {
        return doStatus;
    }

    public void setDoStatus(String doStatus) {
        this.doStatus = doStatus;
    }

    public String getPackageListItem() {
        return packageListItem;
    }

    public void setPackageListItem(String packageListItem) {
        this.packageListItem = packageListItem;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
