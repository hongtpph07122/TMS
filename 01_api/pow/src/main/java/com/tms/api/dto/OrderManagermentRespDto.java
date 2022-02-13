package com.tms.api.dto;

import java.util.List;

import com.tms.dto.GetOrderManagement7Resp;

public class OrderManagermentRespDto {
	/* private List<GetOrderManagement5Resp> orderManagement5Resp; */
	private List<GetOrderManagement7Resp> orderManagement7Resp;
	private Integer rowCount;

	public OrderManagermentRespDto() {
	}

	/*
	 * public List<GetOrderManagement5Resp> getOrderManagement5Resp() { return
	 * orderManagement5Resp; } public void
	 * setOrderManagement5Resp(List<GetOrderManagement5Resp> orderManagement5Resp) {
	 * this.orderManagement5Resp = orderManagement5Resp; }
	 */

	public Integer getRowCount() {
		return rowCount;
	}

	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}

	public List<GetOrderManagement7Resp> getOrderManagement7Resp() {
		return orderManagement7Resp;
	}

	public void setOrderManagement7Resp(List<GetOrderManagement7Resp> orderManagement7Resp) {
		this.orderManagement7Resp = orderManagement7Resp;
	}

}
