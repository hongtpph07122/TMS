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
@Table(name = "rp30_activity_sumary")
@IdClass(ActivitySumary.class)
public class ActivitySumary implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "datetime")
    private String datetime;
    @Id
    @Column(columnDefinition = "agent_id")
    private Integer agentId;
    @Column(columnDefinition = "agent_name")
    private String agentName;
    @Column(columnDefinition = "avg_avaiable_time")
    private String avgAvaiableTime;
    @Column(columnDefinition = "avg_unavaiable_time")
    private String avgUnavaiableTime;
    @Column(columnDefinition = "avg_oncall_time")
    private String avgOncallTime;
    @Column(columnDefinition = "avg_acw_time")
    private String avgAcwTime;

    public String getDatetime() {
	return datetime;
    }

    public void setDatetime(String datetime) {
	this.datetime = datetime;
    }

    public Integer getAgentId() {
	return agentId;
    }

    public void setAgentId(Integer agentId) {
	this.agentId = agentId;
    }

    public String getAgentName() {
	return agentName;
    }

    public void setAgentName(String agentName) {
	this.agentName = agentName;
    }

    public String getAvgAvaiableTime() {
	return avgAvaiableTime;
    }

    public void setAvgAvaiableTime(String avgAvaiableTime) {
	this.avgAvaiableTime = avgAvaiableTime;
    }

    public String getAvgUnavaiableTime() {
	return avgUnavaiableTime;
    }

    public void setAvgUnavaiableTime(String avgUnavaiableTime) {
	this.avgUnavaiableTime = avgUnavaiableTime;
    }

    public String getAvgOncallTime() {
	return avgOncallTime;
    }

    public void setAvgOncallTime(String avgOncallTime) {
	this.avgOncallTime = avgOncallTime;
    }

    public String getAvgAcwTime() {
	return avgAcwTime;
    }

    public void setAvgAcwTime(String avgAcwTime) {
	this.avgAcwTime = avgAcwTime;
    }

}
