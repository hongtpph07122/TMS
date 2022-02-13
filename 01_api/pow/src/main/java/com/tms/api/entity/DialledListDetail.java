package com.tms.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "rp31_dialled_list_detail")
@IdClass(DialledListDetail.class)
public class DialledListDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(columnDefinition = "product_name")
    private String productName;
    @Id
    @Column(columnDefinition = "phone")
    private String phone;
    @Id
    @Column(columnDefinition = "owner_id")
    private String ownerId;
    @Id
    @Column(columnDefinition = "owner_name")
    private String ownerName;
    @Id
    @Column(columnDefinition = "disposition")
    private String disposition;
    @Id
    @Column(columnDefinition = "max_try_count")
    private String maxTryCount;
    @Id
    @Column(columnDefinition = "phone_number")
    private String phoneNumber;
    @Id
    @Column(columnDefinition = "address")
    private String address;
    @Id
    @Column(columnDefinition = "email")
    private String email;
    @Id
    @Column(columnDefinition = "description")
    private String description;
    @Id
    @Column(columnDefinition = "province")
    private String province;
    @Id
    @Column(columnDefinition = "district")
    private String district;
    @Id
    @Column(columnDefinition = "subdistrict")
    private String subdistrict;
    @Id
    @Column(columnDefinition = "so_id")
    private String soId;
    @Id
    @Column(columnDefinition = "amount_cod")
    private String amountCod;
    @Id
    @Column(columnDefinition = "payment_info")
    private String paymentInfo;
    @Id
    @Column(columnDefinition = "gcode")
    private String gcode;
    @Id
    @Column(columnDefinition = "amount")
    private String amount;
    @Id
    @Column(columnDefinition = "call_status")
    private String callStatus;

    public String getProductName() {
	return productName;
    }

    public void setProductName(String productName) {
	this.productName = productName;
    }

    public String getPhone() {
	return phone;
    }

    public void setPhone(String phone) {
	this.phone = phone;
    }

    public String getOwnerId() {
	return ownerId;
    }

    public void setOwnerId(String ownerId) {
	this.ownerId = ownerId;
    }

    public String getOwnerName() {
	return ownerName;
    }

    public void setOwnerName(String ownerName) {
	this.ownerName = ownerName;
    }

    public String getDisposition() {
	return disposition;
    }

    public void setDisposition(String disposition) {
	this.disposition = disposition;
    }

    public String getMaxTryCount() {
	return maxTryCount;
    }

    public void setMaxTryCount(String maxTryCount) {
	this.maxTryCount = maxTryCount;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getProvince() {
	return province;
    }

    public void setProvince(String province) {
	this.province = province;
    }

    public String getDistrict() {
	return district;
    }

    public void setDistrict(String district) {
	this.district = district;
    }

    public String getSubdistrict() {
	return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
	this.subdistrict = subdistrict;
    }

    public String getSoId() {
	return soId;
    }

    public void setSoId(String soId) {
	this.soId = soId;
    }

    public String getAmountCod() {
	return amountCod;
    }

    public void setAmountCod(String amountCod) {
	this.amountCod = amountCod;
    }

    public String getPaymentInfo() {
	return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
	this.paymentInfo = paymentInfo;
    }

    public String getGcode() {
	return gcode;
    }

    public void setGcode(String gcode) {
	this.gcode = gcode;
    }

    public String getAmount() {
	return amount;
    }

    public void setAmount(String amount) {
	this.amount = amount;
    }

    public String getCallStatus() {
	return callStatus;
    }

    public void setCallStatus(String callStatus) {
	this.callStatus = callStatus;
    }

    public static long getSerialversionuid() {
	return serialVersionUID;
    }

}
