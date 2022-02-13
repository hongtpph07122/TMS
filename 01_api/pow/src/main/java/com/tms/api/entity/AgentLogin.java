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
@Table(name = "rp29_agent_login")
@IdClass(AgentLogin.class)
public class AgentLogin implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "agent_id")
    private Integer agentId;
    @Column(columnDefinition = "agent_name")
    private String agentName;
    @Column(columnDefinition = "signin_time")
    private String signinTime;
    @Column(columnDefinition = "signout_time")
    private String signoutTime;
    @Column(columnDefinition = "duration")
    private String duration;

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

    public String getSigninTime() {
	return signinTime;
    }

    public void setSigninTime(String signinTime) {
	this.signinTime = signinTime;
    }

    public String getSignoutTime() {
	return signoutTime;
    }

    public void setSignoutTime(String signoutTime) {
	this.signoutTime = signoutTime;
    }

    public String getDuration() {
	return duration;
    }

    public void setDuration(String duration) {
	this.duration = duration;
    }

}
