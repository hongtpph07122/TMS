package com.tms.dto;

public class GetTrackingCode {

    private Integer orgId;
    private Integer doId;
    private String trackingCode;
    private Integer soId;
    private Integer ffmId;
    private String ffmName;
    private Integer warehouseId;
    private String warehouseName;
    private Integer carrierId;
    private String carrierName;
    private String doStatus;
    private Integer limit;
    private Integer offset;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getDoId() {
        return doId;
    }

    public void setDoId(Integer doId) {
        this.doId = doId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public Integer getSoId() {
        return soId;
    }

    public void setSoId(Integer soId) {
        this.soId = soId;
    }

    public Integer getFfmId() {
        return ffmId;
    }

    public void setFfmId(Integer ffmId) {
        this.ffmId = ffmId;
    }

    public String getFfmName() {
        return ffmName;
    }

    public void setFfmName(String ffmName) {
        this.ffmName = ffmName;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getDoStatus() {
        return doStatus;
    }

    public void setDoStatus(String doStatus) {
        this.doStatus = doStatus;
    }
}
