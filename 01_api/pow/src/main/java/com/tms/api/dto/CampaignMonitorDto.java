package com.tms.api.dto;

public class CampaignMonitorDto {
    int PhoneExt;
	String cpName;
	String startDate;
	String endDate;
	int actual;
	String lead;
	String campaignStatus;
	int campaignStatusId;
	int totalCall;
	int totalAns;

	public String getCpName() {
		return cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	public int getPhoneExt() {
		return PhoneExt;
	}

	public void setPhoneExt(int phoneExt) {
		PhoneExt = phoneExt;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getActual() {
		return actual;
	}

	public void setActual(int actual) {
		this.actual = actual;
	}

	public String getLead() {
		return lead;
	}

	public void setLead(String lead) {
		this.lead = lead;
	}

	public String getCampaignStatus() {
		return campaignStatus;
	}

	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}

	public int getCampaignStatusId() {
		return campaignStatusId;
	}

	public void setCampaignStatusId(int campaignStatusId) {
		this.campaignStatusId = campaignStatusId;
	}

	public int getTotalCall() {
		return totalCall;
	}

	public void setTotalCall(int totalCall) {
		this.totalCall = totalCall;
	}

	public int getTotalAns() {
		return totalAns;
	}

	public void setTotalAns(int totalAns) {
		this.totalAns = totalAns;
	}
}