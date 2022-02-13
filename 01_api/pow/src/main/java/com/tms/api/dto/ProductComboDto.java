package com.tms.api.dto;

import com.tms.entity.log.InsProductV2;

import java.util.List;

public class ProductComboDto {
	private InsProductV2 comboInfo;
	private List<ProdDto> prods;

	public InsProductV2 getComboInfo() {
		return comboInfo;
	}

	public void setComboInfo(InsProductV2 comboInfo) {
		this.comboInfo = comboInfo;
	}

	public List<ProdDto> getProds() {
		return prods;
	}

	public void setProds(List<ProdDto> prods) {
		this.prods = prods;
	}
}

