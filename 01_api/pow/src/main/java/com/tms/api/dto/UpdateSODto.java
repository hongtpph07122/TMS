package com.tms.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

public class UpdateSODto {
    @NotNull(message = "Cannot be null")
    Integer leadId;
    @NotEmpty(message = "Product cannot empty")
    HashMap<Integer, SOProductDTO> products;
    @NotNull(message = "Cannot be null")
    Integer paymentMethod;
    @NotNull(message = "Cannot be null")
    Integer soId;
    @NotNull(message = "Cannot be null")
    Double amount;

    @NotNull(message = "Cannot be null")
    Integer status;

    Integer comboDiscount;
    Integer comboPercent;
    Integer saleDiscount;
    Integer salePecent;
    Integer unit;
    @JsonAlias({"appointment_schedule", "appointment_date"})
    private String appointmentDate;

    public Integer getComboDiscount() {
        return comboDiscount;
    }

    public void setComboDiscount(Integer comboDiscount) {
        this.comboDiscount = comboDiscount;
    }

    public Integer getComboPercent() {
        return comboPercent;
    }

    public void setComboPercent(Integer comboPercent) {
        this.comboPercent = comboPercent;
    }

    public Integer getSaleDiscount() {
        return saleDiscount;
    }

    public void setSaleDiscount(Integer saleDiscount) {
        this.saleDiscount = saleDiscount;
    }

    public Integer getSalePecent() {
        return salePecent;
    }

    public void setSalePecent(Integer salePecent) {
        this.salePecent = salePecent;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getSoId() {
        return soId;
    }

    public void setSoId(Integer soId) {
        this.soId = soId;
    }

    public Integer getLeadId() {
        return leadId;
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

    public HashMap<Integer, SOProductDTO> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Integer, SOProductDTO> products) {
        this.products = products;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
