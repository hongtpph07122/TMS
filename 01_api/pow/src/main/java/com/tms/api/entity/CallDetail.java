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
@Table(name = "rp26_call_detail")
@IdClass(CallDetail.class)
public class CallDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "agent_name")
    private String agentName;
    @Id
    @Column(columnDefinition = "call_start_time")
    private String callStartTime;
    @Id
    @Column(columnDefinition = "call_end_time")
    private String callEndTime;
    @Id
    @Column(columnDefinition = "call_type")
    private String callType;
    @Id
    @Column(columnDefinition = "caller")
    private String caller;
    @Id
    @Column(columnDefinition = "number_called")
    private String numberCalled;
    @Id
    @Column(columnDefinition = "disposition_code")
    private String dispositionCode;

    public String getAgentName() {
	return agentName;
    }

    public void setAgentName(String agentName) {
	this.agentName = agentName;
    }

    public String getCallStartTime() {
	return callStartTime;
    }

    public void setCallStartTime(String callStartTime) {
	this.callStartTime = callStartTime;
    }

    public String getCallEndTime() {
	return callEndTime;
    }

    public void setCallEndTime(String callEndTime) {
	this.callEndTime = callEndTime;
    }

    public String getCallType() {
	return callType;
    }

    public void setCallType(String callType) {
	this.callType = callType;
    }

    public String getCaller() {
	return caller;
    }

    public void setCaller(String caller) {
	this.caller = caller;
    }

    public String getNumberCalled() {
	return numberCalled;
    }

    public void setNumberCalled(String numberCalled) {
	this.numberCalled = numberCalled;
    }

    public String getDispositionCode() {
	return dispositionCode;
    }

    public void setDispositionCode(String dispositionCode) {
	this.dispositionCode = dispositionCode;
    }

}
