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
@Table(name = "rp28_agent_activity")
@IdClass(AgentActivity.class)
public class AgentActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(columnDefinition = "datetime")
    private String datetime;
    @Id
    @Column(columnDefinition = "agent_id")
    private Integer agentId;
    @Column(columnDefinition = "agent_name")
    private String agentName;
    @Column(columnDefinition = "avaiable")
    private Integer avaiable;
    @Column(columnDefinition = "unavaiable")
    private Integer unavaiable;
    @Column(columnDefinition = "oncall")
    private Integer oncall;
    @Column(columnDefinition = "acw")
    private Integer acw;

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

    public Integer getAvaiable() {
	return avaiable;
    }

    public void setAvaiable(Integer avaiable) {
	this.avaiable = avaiable;
    }

    public Integer getUnavaiable() {
	return unavaiable;
    }

    public void setUnavaiable(Integer unavaiable) {
	this.unavaiable = unavaiable;
    }

    public Integer getOncall() {
	return oncall;
    }

    public void setOncall(Integer oncall) {
	this.oncall = oncall;
    }

    public Integer getAcw() {
	return acw;
    }

    public void setAcw(Integer acw) {
	this.acw = acw;
    }

}
