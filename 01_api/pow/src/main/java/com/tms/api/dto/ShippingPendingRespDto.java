package com.tms.api.dto;

import com.tms.dto.GetShippingResp;

import java.util.List;

public class ShippingPendingRespDto {

    private List<GetShippingResp> listShippingPending;
    private Integer rowCount;

    public List<GetShippingResp> getListShippingPending() {
        return listShippingPending;
    }

    public void setListShippingPending(List<GetShippingResp> listShippingPending) {
        this.listShippingPending = listShippingPending;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
