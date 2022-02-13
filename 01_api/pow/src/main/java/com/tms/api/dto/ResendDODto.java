package com.tms.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ResendDODto {
	@NotEmpty(message="Cannot be empty" )
	List<Integer> doIds;
	@NotNull(message = "Cannot be null")
	Integer status;
	Boolean isChangeWareHouse;
	Integer ffmPartnerId;
	Integer lmPartnerId;
	Integer warehouseId;

	 
	public Boolean getIsChangeWareHouse() {
		return isChangeWareHouse;
	}

	public void setIsChangeWareHouse(Boolean isChangeWareHouse) {
		this.isChangeWareHouse = isChangeWareHouse;
	}

	public Integer getFfmPartnerId() {
		return ffmPartnerId;
	}

	public void setFfmPartnerId(Integer ffmPartnerId) {
		this.ffmPartnerId = ffmPartnerId;
	}

	public Integer getLmPartnerId() {
		return lmPartnerId;
	}

	public void setLmPartnerId(Integer lmPartnerId) {
		this.lmPartnerId = lmPartnerId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public List<Integer> getDoIds() {
		return doIds;
	}

	public void setDoIds(List<Integer> doIds) {
		this.doIds = doIds;
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
