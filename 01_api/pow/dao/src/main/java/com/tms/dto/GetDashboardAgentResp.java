package com.tms.dto;

public class GetDashboardAgentResp {

    private Integer orgId;
    private Integer agent;
    private String fromdate;
    private String todate;
    private Integer approved;
    private Integer rejected;
    private Integer callback;
    private Integer trash;
    private Integer totalCall;
    private Integer totalSaleOrder;
    private Double totalOrderValue;
    private Integer totalLead;
    private Double rateSaleOrder;
    private Double rateDeliveryOrder;
    private Double ratePaidOrder;
    private Double conversionRate;
    private Double avgSalesValue;
    private Integer orderProcess;
    private Integer saleOrders;
    private Double saleValues;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getAgent() {
        return agent;
    }

    public void setAgent(Integer agent) {
        this.agent = agent;
    }

    public String getFromdate() {
        return fromdate;
    }

    public void setFromdate(String fromdate) {
        this.fromdate = fromdate;
    }

    public String getTodate() {
        return todate;
    }

    public void setTodate(String todate) {
        this.todate = todate;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getRejected() {
        return rejected;
    }

    public void setRejected(Integer rejected) {
        this.rejected = rejected;
    }

    public Integer getCallback() {
        return callback;
    }

    public void setCallback(Integer callback) {
        this.callback = callback;
    }

    public Integer getTrash() {
        return trash;
    }

    public void setTrash(Integer trash) {
        this.trash = trash;
    }

    public Integer getTotalCall() {
        return totalCall;
    }

    public void setTotalCall(Integer totalCall) {
        this.totalCall = totalCall;
    }

    public Integer getTotalSaleOrder() {
        return totalSaleOrder;
    }

    public void setTotalSaleOrder(Integer totalSaleOrder) {
        this.totalSaleOrder = totalSaleOrder;
    }

    public Double getTotalOrderValue() {
        return totalOrderValue;
    }

    public void setTotalOrderValue(Double totalOrderValue) {
        this.totalOrderValue = totalOrderValue;
    }

    public Integer getTotalLead() {
        return totalLead;
    }

    public void setTotalLead(Integer totalLead) {
        this.totalLead = totalLead;
    }

    public Double getRateSaleOrder() {
        return rateSaleOrder;
    }

    public void setRateSaleOrder(Double rateSaleOrder) {
        this.rateSaleOrder = rateSaleOrder;
    }

    public Double getRateDeliveryOrder() {
        return rateDeliveryOrder;
    }

    public void setRateDeliveryOrder(Double rateDeliveryOrder) {
        this.rateDeliveryOrder = rateDeliveryOrder;
    }

    public Double getRatePaidOrder() {
        return ratePaidOrder;
    }

    public void setRatePaidOrder(Double ratePaidOrder) {
        this.ratePaidOrder = ratePaidOrder;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(Double conversionRate) {
        this.conversionRate = conversionRate;
    }

    public Double getAvgSalesValue() {
        return avgSalesValue;
    }

    public void setAvgSalesValue(Double avgSalesValue) {
        this.avgSalesValue = avgSalesValue;
    }

    public Integer getOrderProcess() {
        return orderProcess;
    }

    public void setOrderProcess(Integer orderProcess) {
        this.orderProcess = orderProcess;
    }

    public Integer getSaleOrders() {
        return saleOrders;
    }

    public void setSaleOrders(Integer saleOrders) {
        this.saleOrders = saleOrders;
    }

    public Double getSaleValues() {
        return saleValues;
    }

    public void setSaleValues(Double saleValues) {
        this.saleValues = saleValues;
    }
}
