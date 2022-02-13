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
@Table(name = "rp39_phone_not_contact")
@IdClass(PhoneNotContact.class)
public class PhoneNotContact implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "phone_number")
    private String phoneNumber;
    @Column(columnDefinition = "name")
    private String name;
    @Column(columnDefinition = "assign")
    private String assign;
    @Column(columnDefinition = "dialtype")
    private String dialtype;
    @Column(columnDefinition = "create_time")
    private String createTime;

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getAssign() {
	return assign;
    }

    public void setAssign(String assign) {
	this.assign = assign;
    }

    public String getDialtype() {
	return dialtype;
    }

    public void setDialtype(String dialtype) {
	this.dialtype = dialtype;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

}
