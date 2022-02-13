package com.tms.api.dto;

import com.tms.dto.GetProductComboResp;
import com.tms.entity.PDProduct;

import java.util.Date;
import java.util.List;

public class ProdRespDto extends PDProduct {

    public ProdRespDto(PDProduct product){
        prodId = product.getProdId();
        orgId = product.getOrgId();
        code = product.getCode();
        category = product.getCategory();
        name = product.getName();
        price = product.getPrice();
        dscr = product.getDscr();
        dscrForAgent = product.getDscrForAgent();
        status = product.getStatus();
        pathFile = product.getPathFile();
        modifyby = product.getModifyby();
        modifydate = product.getModifydate();
        dscre = product.getDscre();
        listPrice = product.getListPrice();
        discountCash = product.getDiscountCash();
        discountPercent = product.getDiscountPercent();
        productType = product.getProductType();
        productTypeName = product.getProductTypeName();
        unit = product.getUnit();
        unitName = product.getUnitName();
    }
    private List<GetProductComboResp> items;

    public List<GetProductComboResp> getItems() {
        return items;
    }

    public void setItems(List<GetProductComboResp> items) {
        this.items = items;
    }
}

