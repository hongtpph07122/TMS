package com.tms.api.dto;

import com.tms.entity.CLFresh;
import com.tms.entity.SaleOrder;
import com.tms.dto.GetDoNewResp;

public class ResponeDeliveryOrderDto extends ResponeGetSaleOrderDto{
	GetDoNewResp doData;

	public GetDoNewResp getDoData() {
		return doData;
	}

	public void setDoData(GetDoNewResp doData) {
		this.doData = doData;
	}
}
