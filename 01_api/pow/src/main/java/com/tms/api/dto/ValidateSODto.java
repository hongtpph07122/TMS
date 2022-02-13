package com.tms.api.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ValidateSODto {
	@NotEmpty(message="Cannot be empty" )
	List<Integer> soIds;
	@NotNull(message = "Cannot be null")
	Integer status;
	String creationDate;

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public List<Integer> getSoIds() {
		return soIds;
	}
	public void setSoIds(List<Integer> soIds) {
		this.soIds = soIds;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
