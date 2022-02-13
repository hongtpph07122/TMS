package com.tms.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CreateCallBackDto {
    @NotNull(message = "leadId is not null")
    Integer leadId;
    //@NotEmpty(message = "callbackTime is not null")
    String callbackTime;
    Integer status;
    String note;
    String userDefine5;
    String phone;
    Integer phoneType;
    String name;
    String custJob;
    String custDob;
    Integer custAge;
    Integer custGender;
    String custOtherSymptom;
    String closeTime;
    String maxCloseTime;
    Integer oldStatus;

    public String getUserDefine5() {
        return userDefine5;
    }

    public void setUserDefine5(String userDefine5) {
        this.userDefine5 = userDefine5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getCallbackTime() {
        return callbackTime;
    }

    public void setCallbackTime(String callbackTime) {
        this.callbackTime = callbackTime;
    }

    public String getCustJob() {
        return custJob;
    }

    public void setCustJob(String custJob) {
        this.custJob = custJob;
    }

    public String getCustDob() {
        return custDob;
    }

    public void setCustDob(String custDob) {
        this.custDob = custDob;
    }

    public Integer getCustAge() {
        return custAge;
    }

    public void setCustAge(Integer custAge) {
        this.custAge = custAge;
    }

    public Integer getCustGender() {
        return custGender;
    }

    public void setCustGender(Integer custGender) {
        this.custGender = custGender;
    }

    public String getCustOtherSymptom() {
        return custOtherSymptom;
    }

    public void setCustOtherSymptom(String custOtherSymptom) {
        this.custOtherSymptom = custOtherSymptom;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getMaxCloseTime() {
        return maxCloseTime;
    }

    public void setMaxCloseTime(String maxCloseTime) {
        this.maxCloseTime = maxCloseTime;
    }

    public Integer getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }
}
