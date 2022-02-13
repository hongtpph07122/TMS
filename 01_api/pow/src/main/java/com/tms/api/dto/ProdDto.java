package com.tms.api.dto;

import com.tms.entity.log.InsProductV2;

public class ProdDto {
	private Integer prdId;
	private Integer qty;
	private Integer index;

	public Integer getPrdId() {
		return prdId;
	}

	public void setPrdId(Integer prdId) {
		this.prdId = prdId;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
}

