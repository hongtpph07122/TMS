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
@Table(name = "rp37_call_sumary_bydate")
@IdClass(CallSumaryByDate.class)
public class CallSumaryByDate implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(columnDefinition = "date")
    private String date;
    @Id
    @Column(columnDefinition = "agent_id")
    private Integer agentId;
    @Column(columnDefinition = "calls")
    private Integer calls;
    @Column(columnDefinition = "talktime")
    private Integer talktime;
    @Column(columnDefinition = "answer")
    private Integer answer;
    @Column(columnDefinition = "noanswer")
    private Integer noanswer;
    @Column(columnDefinition = "busy")
    private Integer busy;
    @Column(columnDefinition = "failed_congrestition")
    private Integer failedCongrestition;

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public Integer getAgentId() {
	return agentId;
    }

    public void setAgentId(Integer agentId) {
	this.agentId = agentId;
    }

    public Integer getCalls() {
	return calls;
    }

    public void setCalls(Integer calls) {
	this.calls = calls;
    }

    public Integer getTalktime() {
	return talktime;
    }

    public void setTalktime(Integer talktime) {
	this.talktime = talktime;
    }

    public Integer getAnswer() {
	return answer;
    }

    public void setAnswer(Integer answer) {
	this.answer = answer;
    }

    public Integer getNoanswer() {
	return noanswer;
    }

    public void setNoanswer(Integer noanswer) {
	this.noanswer = noanswer;
    }

    public Integer getBusy() {
	return busy;
    }

    public void setBusy(Integer busy) {
	this.busy = busy;
    }

    public Integer getFailedCongrestition() {
	return failedCongrestition;
    }

    public void setFailedCongrestition(Integer failedCongrestition) {
	this.failedCongrestition = failedCongrestition;
    }
}
