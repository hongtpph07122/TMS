package com.tms.api.dto;

import com.tms.entity.CLFresh;
import com.tms.entity.SaleOrder;

	public class ResponeGetSaleOrderDto {
	SaleOrder soData;
	CLFresh leadData;
	public SaleOrder getSoData() {
		return soData;
	}
	public void setSoData(SaleOrder soData) {
		this.soData = soData;
	}
	public CLFresh getLeadData() {
		return leadData;
	}
	public void setLeadData(CLFresh leadData) {
		this.leadData = leadData;
	}
}
