package com.tms.dto;

public class GetSoFulfillment {

    private Integer soFfmId;
    private Integer soId;
    private Integer ffmId;
    private Integer warehouseId;
    private Integer lastmileId;
    private Integer modifyby;
    private String modifydate;
    private Integer limit;
    private Integer offset;

    public Integer getSoFfmId() {
        return soFfmId;
    }

    public void setSoFfmId(Integer soFfmId) {
        this.soFfmId = soFfmId;
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

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getLastmileId() {
        return lastmileId;
    }

    public void setLastmileId(Integer lastmileId) {
        this.lastmileId = lastmileId;
    }

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
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

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }
}
