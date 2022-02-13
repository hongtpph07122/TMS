package com.tms.dto;

public class GetDefaultDOResp {

    private Integer orgId;
    private Integer provinceId;
    private String provName;
    private Integer warehouseId;
    private String warehouseName;
    private String warehouseShortname;
    private Integer hubId;
    private String hubName;
    private Integer lastmileId;
    private String lastmileName;
    private Integer pickupId;

    private Integer partnerId;

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getPickupId() {
        return pickupId;
    }

    public void setPickupId(Integer pickupId) {
        this.pickupId = pickupId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
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

    public Integer getHubId() {
        return hubId;
    }

    public void setHubId(Integer hubId) {
        this.hubId = hubId;
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public Integer getLastmileId() {
        return lastmileId;
    }

    public void setLastmileId(Integer lastmileId) {
        this.lastmileId = lastmileId;
    }

    public String getLastmileName() {
        return lastmileName;
    }

    public void setLastmileName(String lastmileName) {
        this.lastmileName = lastmileName;
    }

    public String getWarehouseShortname() {
        return warehouseShortname;
    }

    public void setWarehouseShortname(String warehouseShortname) {
        this.warehouseShortname = warehouseShortname;
    }
}
