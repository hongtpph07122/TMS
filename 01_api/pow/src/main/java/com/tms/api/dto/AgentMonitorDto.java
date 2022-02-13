package com.tms.api.dto;

import com.tms.entity.User;

import java.util.List;

public class AgentMonitorDto {
    int PhoneExt;
	String agentName;
	String agentStatus;
	int agentStatusId;
	String duration;
	String phoneStatus;
	int phoneStatusId;
	String direction;
	String phoneNumber;


	public int getPhoneExt() {
		return PhoneExt;
	}

	public void setPhoneExt(int phoneExt) {
		PhoneExt = phoneExt;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentStatus() {
		return agentStatus;
	}

	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}

	public int getAgentStatusId() {
		return agentStatusId;
	}

	public void setAgentStatusId(int agentStatusId) {
		this.agentStatusId = agentStatusId;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getPhoneStatus() {
		return phoneStatus;
	}

	public void setPhoneStatus(String phoneStatus) {
		this.phoneStatus = phoneStatus;
	}

	public int getPhoneStatusId() {
		return phoneStatusId;
	}

	public void setPhoneStatusId(int phoneStatusId) {
		this.phoneStatusId = phoneStatusId;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}