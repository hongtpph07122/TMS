package com.tms.api.dto;

import com.tms.dto.GetOrderManagement9Resp;

import java.util.List;

public class OrderManagement9RespDto {
    private List<GetOrderManagement9Resp> orderManagement9Resp;
    private Integer rowCount;

    public List<GetOrderManagement9Resp> getOrderManagement9Resp() {
        return orderManagement9Resp;
    }

    public void setOrderManagement9Resp(List<GetOrderManagement9Resp> orderManagement9Resp) {
        this.orderManagement9Resp = orderManagement9Resp;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
