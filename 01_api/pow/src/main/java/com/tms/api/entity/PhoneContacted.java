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
@Table(name = "rp40_phone_contacted")
@IdClass(PhoneContacted.class)
public class PhoneContacted implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(columnDefinition = "phone_number")
    private String phone_number;
    @Column(columnDefinition = "direction")
    private String direction;
    @Column(columnDefinition = "name")
    private String name;
    @Column(columnDefinition = "user_extension")
    private String user_extension;
    @Column(columnDefinition = "disposition")
    private String disposition;
    @Column(columnDefinition = "hangedupcall")
    private String hangedupcall;
    @Column(columnDefinition = "notpotential")
    private String notpotential;
    @Column(columnDefinition = "consider")
    private String consider;
    @Column(columnDefinition = "closingtime")
    private String closingtime;
    @Column(columnDefinition = "describe")
    private String describe;
    @Column(columnDefinition = "call_date")
    private String call_date;

    public String getPhone_number() {
	return phone_number;
    }

    public void setPhone_number(String phone_number) {
	this.phone_number = phone_number;
    }

    public String getDirection() {
	return direction;
    }

    public void setDirection(String direction) {
	this.direction = direction;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getUser_extension() {
	return user_extension;
    }

    public void setUser_extension(String user_extension) {
	this.user_extension = user_extension;
    }

    public String getDisposition() {
	return disposition;
    }

    public void setDisposition(String disposition) {
	this.disposition = disposition;
    }

    public String getHangedupcall() {
	return hangedupcall;
    }

    public void setHangedupcall(String hangedupcall) {
	this.hangedupcall = hangedupcall;
    }

    public String getNotpotential() {
	return notpotential;
    }

    public void setNotpotential(String notpotential) {
	this.notpotential = notpotential;
    }

    public String getConsider() {
	return consider;
    }

    public void setConsider(String consider) {
	this.consider = consider;
    }

    public String getClosingtime() {
	return closingtime;
    }

    public void setClosingtime(String closingtime) {
	this.closingtime = closingtime;
    }

    public String getDescribe() {
	return describe;
    }

    public void setDescribe(String describe) {
	this.describe = describe;
    }

    public String getCall_date() {
	return call_date;
    }

    public void setCall_date(String call_date) {
	this.call_date = call_date;
    }

}
