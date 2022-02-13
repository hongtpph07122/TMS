package com.tms.api.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class CreateSODto {
	@NotNull(message = "Cannot be null")
	Integer leadId;
	@NotNull(message = "Cannot be null")
	Integer leadStatus;
	String leadType;
	String phone;


	/*public Integer getProdType() {
		return prodType;
	}

	public void setProdType(Integer prodType) {
		this.prodType = prodType;
	}*/

	public String getLeadType() {
		return leadType;
	}

	public void setLeadType(String leadType) {
		this.leadType = leadType;
	}

	public Integer getLeadStatus() {
		return leadStatus;
	}
	public void setLeadStatus(Integer leadStatus) {
		this.leadStatus = leadStatus;
	}

	
	@NotEmpty(message="Product cannot empty" )
	HashMap<Integer, SOProductDTO> products;
	@NotNull(message = "Cannot be null")
	Integer paymentMethod;
	@NotNull(message = "Cannot be null")
	Double amount;

	Integer comboDiscount;
	Integer comboPercent;
	Integer saleDiscount;
	Integer salePercent;

	Integer unit;
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getComboPercent() {
		return comboPercent;
	}

	public void setComboPercent(Integer comboPercent) {
		this.comboPercent = comboPercent;
	}

	public Integer getSalePercent() {
		return salePercent;
	}

	public void setSalePercent(Integer salePercent) {
		this.salePercent = salePercent;
	}

	public Integer getUnit() {
		return unit;
	}

	public void setUnit(Integer unit) {
		this.unit = unit;
	}

	public Integer getComboDiscount() {
		return comboDiscount;
	}

	public void setComboDiscount(Integer comboDiscount) {
		this.comboDiscount = comboDiscount;
	}

	public Integer getSaleDiscount() {
		return saleDiscount;
	}

	public void setSaleDiscount(Integer saleDiscount) {
		this.saleDiscount = saleDiscount;
	}

	public Integer getLeadId() {
		return leadId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public HashMap<Integer,SOProductDTO> getProducts() {
		return products;
	}
	public void setProducts(HashMap<Integer,SOProductDTO> products) {
		this.products = products;
	}
	
    private Integer cpId;

    private Integer agentId;

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }
    
    
}

