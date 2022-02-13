package com.tms.api.dto;

import com.tms.entity.CLFresh;
import com.tms.entity.DeliveryOrder;
import com.tms.entity.SaleOrder;
import com.tms.dto.GetDoNewResp;

/**
 * Created by dinhanhthai on 10/07/2019.
 */
public class ShippingPendingDto extends ShippingDto {

    private SaleOrder saleOrder;

    public SaleOrder getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(SaleOrder saleOrder) {
        this.saleOrder = saleOrder;
    }

    public ShippingPendingDto(){
        super();
    }
    public ShippingPendingDto(GetDoNewResp doNewResp){
        super(doNewResp);
    }

}
