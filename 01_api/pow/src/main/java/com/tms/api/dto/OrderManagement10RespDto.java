package com.tms.api.dto;

import com.tms.dto.GetOrderManagement10Resp;

import java.util.List;

public class OrderManagement10RespDto {
    private List<GetOrderManagement10Resp> orderManagementResp;
    private Integer rowCount;

    public List<GetOrderManagement10Resp> getOrderManagementResp() {
        return orderManagementResp;
    }

    public void setOrderManagementResp(List<GetOrderManagement10Resp> orderManagementResp) {
        this.orderManagementResp = orderManagementResp;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
