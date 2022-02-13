package com.tms.api.dto;

import com.tms.dto.GetOrderManagement8Resp;

import java.util.List;

public class OrderManagement8RespDto {
    private List<GetOrderManagement8Resp> orderManagement8Resp;
    private Integer rowCount;

    public OrderManagement8RespDto() {
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public List<GetOrderManagement8Resp> getOrderManagement8Resp() {
        return orderManagement8Resp;
    }

    public void setOrderManagement8Resp(List<GetOrderManagement8Resp> orderManagement8Resp) {
        this.orderManagement8Resp = orderManagement8Resp;
    }

}
