package com.tms.api.dto;

import javax.validation.constraints.NotNull;

public class SOProductDTO {
    @NotNull(message = "Cannot be null")
    Integer quantity;
    @NotNull(message = "Cannot be null")
    Integer priceIndex;

	Integer prodType;

	public Integer getProdType() {
		return prodType;
	}

	public void setProdType(Integer prodType) {
		this.prodType = prodType;
	}

	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
    public Integer getPriceIndex() {
		return priceIndex;
	}
	public void setPriceIndex(Integer priceIndex) {
		this.priceIndex = priceIndex;
	}
}