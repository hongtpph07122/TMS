package com.tms.entity.log;

public class LogSendSmsDeliveryOrder {

	private Integer logId;
	private String createDate;
	private Integer status;
	private Integer doId;
	private String doCode;
	private Integer doStatus;
	private Integer soId;
	private String trackingCode;
	private String lastmileCode;
	private String ffmCode;
	private String ffmStatus;
	private String ffmStatusName;
	private String lastmileStatus;
	private String lastmileStatusName;
	private String phoneNumber;
	private Integer warehouseId;
	private String comment;
	private String responseJson;
	private String jsonLog;
	private String bodySms;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public Integer getDoStatus() {
		return doStatus;
	}

	public void setDoStatus(Integer doStatus) {
		this.doStatus = doStatus;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getJsonLog() {
		return jsonLog;
	}

	public void setJsonLog(String jsonLog) {
		this.jsonLog = jsonLog;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getDoId() {
		return doId;
	}

	public void setDoId(Integer doId) {
		this.doId = doId;
	}

	public String getDoCode() {
		return doCode;
	}

	public void setDoCode(String doCode) {
		this.doCode = doCode;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public String getFfmCode() {
		return ffmCode;
	}

	public void setFfmCode(String ffmCode) {
		this.ffmCode = ffmCode;
	}

	public String getFfmStatus() {
		return ffmStatus;
	}

	public void setFfmStatus(String ffmStatus) {
		this.ffmStatus = ffmStatus;
	}

	public String getFfmStatusName() {
		return ffmStatusName;
	}

	public void setFfmStatusName(String ffmStatusName) {
		this.ffmStatusName = ffmStatusName;
	}

	public String getLastmileCode() {
		return lastmileCode;
	}

	public void setLastmileCode(String lastmileCode) {
		this.lastmileCode = lastmileCode;
	}

	public String getLastmileStatus() {
		return lastmileStatus;
	}

	public void setLastmileStatus(String lastmileStatus) {
		this.lastmileStatus = lastmileStatus;
	}

	public String getLastmileStatusName() {
		return lastmileStatusName;
	}

	public void setLastmileStatusName(String lastmileStatusName) {
		this.lastmileStatusName = lastmileStatusName;
	}

	public Integer getSoId() {
		return soId;
	}

	public void setSoId(Integer soId) {
		this.soId = soId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getBodySms() {
		return bodySms;
	}

	public void setBodySms(String bodySms) {
		this.bodySms = bodySms;
	}

	public LogSendSmsDeliveryOrder() {
		super();
	}

}
