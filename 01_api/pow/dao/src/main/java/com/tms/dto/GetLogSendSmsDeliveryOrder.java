package com.tms.dto;

public class GetLogSendSmsDeliveryOrder {
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
	private String bodySms;
	private Integer limit;
	private Integer offset;

	public Integer getLogId() {
		return logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Integer getDoStatus() {
		return doStatus;
	}

	public void setDoStatus(Integer doStatus) {
		this.doStatus = doStatus;
	}

	public Integer getSoId() {
		return soId;
	}

	public void setSoId(Integer soId) {
		this.soId = soId;
	}

	public String getTrackingCode() {
		return trackingCode;
	}

	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}

	public String getLastmileCode() {
		return lastmileCode;
	}

	public void setLastmileCode(String lastmileCode) {
		this.lastmileCode = lastmileCode;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBodySms() {
		return bodySms;
	}

	public void setBodySms(String bodySms) {
		this.bodySms = bodySms;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public GetLogSendSmsDeliveryOrder() {
		super();
	}

}
