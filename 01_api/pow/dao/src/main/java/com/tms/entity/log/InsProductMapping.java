package com.tms.entity.log;

public class InsProductMapping {

    private Integer prodMappingId;
    private Integer productId;
    private String productCode;
    private String productName;
    private Integer partnerId;
    private Integer partnerProductId;
    private String partnerProductCode;
    private String partnerProductName;
    private Integer status;
    private Integer modifyby;
    private String modifydate;
    private Integer orgId;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getProdMappingId() {
        return prodMappingId;
    }

    public void setProdMappingId(Integer prodMappingId) {
        this.prodMappingId = prodMappingId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getPartnerProductId() {
        return partnerProductId;
    }

    public void setPartnerProductId(Integer partnerProductId) {
        this.partnerProductId = partnerProductId;
    }

    public String getPartnerProductCode() {
        return partnerProductCode;
    }

    public void setPartnerProductCode(String partnerProductCode) {
        this.partnerProductCode = partnerProductCode;
    }

    public String getPartnerProductName() {
        return partnerProductName;
    }

    public void setPartnerProductName(String partnerProductName) {
        this.partnerProductName = partnerProductName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
