package com.tms.dto;

import java.util.List;

public class GetDataFfm {
	private Integer ffmPartnerId;
	private String nameFfm;
	private List<GetDataLastSmile> listLastSmile;
	private List<GetWareHouseResp> listWarehouse;
	
	

	public List<GetWareHouseResp> getListWarehouse() {
		return listWarehouse;
	}

	public void setListWarehouse(List<GetWareHouseResp> listWarehouse) {
		this.listWarehouse = listWarehouse;
	}

	public List<GetDataLastSmile> getListLastSmile() {
		return listLastSmile;
	}

	public void setListLastSmile(List<GetDataLastSmile> listLastSmile) {
		this.listLastSmile = listLastSmile;
	}

	public Integer getFfmPartnerId() {
		return ffmPartnerId;
	}

	public void setFfmPartnerId(Integer ffmPartnerId) {
		this.ffmPartnerId = ffmPartnerId;
	}

	public String getNameFfm() {
		return nameFfm;
	}

	public void setNameFfm(String nameFfm) {
		this.nameFfm = nameFfm;
	}

}
