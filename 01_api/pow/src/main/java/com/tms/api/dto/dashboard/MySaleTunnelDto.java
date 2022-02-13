package com.tms.api.dto.dashboard;

/**
 * Created by dinhanhthai on 17/07/2019.
 */
public class MySaleTunnelDto {
    private Integer lead;
    private Integer saleOrder;
    private Integer delivered;
    private Integer paid;


    public MySaleTunnelDto(){
        lead = 0;
        saleOrder = 0;
        delivered = 0;
        paid = 0;
    }
    public Integer getLead() {
        return lead;
    }

    public void setLead(Integer lead) {
        this.lead = lead;
    }

    public Integer getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(Integer saleOrder) {
        this.saleOrder = saleOrder;
    }

    public Integer getDelivered() {
        return delivered;
    }

    public void setDelivered(Integer delivered) {
        this.delivered = delivered;
    }

    public Integer getPaid() {
        return paid;
    }

    public void setPaid(Integer paid) {
        this.paid = paid;
    }
}
