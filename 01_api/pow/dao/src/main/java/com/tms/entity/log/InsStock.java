package com.tms.entity.log;

public class InsStock {

    private Integer whProdId;
    private Integer prodId;
    private Integer warehouseId;
    private String warehouseProdCode;
    private Integer quantityAvailable;
    private Integer quantityTotal;
    private Integer modifyby;
    private String modifydate;

    public Integer getWhProdId() {
        return whProdId;
    }

    public void setWhProdId(Integer whProdId) {
        this.whProdId = whProdId;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseProdCode() {
        return warehouseProdCode;
    }

    public void setWarehouseProdCode(String warehouseProdCode) {
        this.warehouseProdCode = warehouseProdCode;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Integer getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(Integer quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }
}
