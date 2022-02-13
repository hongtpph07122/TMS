package com.tms.api.dto;

public class UpdateLeadPhoneDto {
	private String phone;
	private String otherPhone1;

	private int index;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getOtherPhone1() {
		return otherPhone1;
	}

	public void setOtherPhone1(String otherPhone1) {
		this.otherPhone1 = otherPhone1;
	}

}
