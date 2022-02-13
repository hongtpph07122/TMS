package com.tms.api.dto.Request;

public class ManipulateMonitorRequestDTO {

    private Integer leadId;
    private Integer agencyId;
    private String source;
    private String name;
    private String productName;
    private String createdDate;
    private String modifiedDate;
    private String manipulatedDate;
    private String clickedId;
    private Integer limit;
    private Integer offset;

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getClickedId() {
        return clickedId;
    }

    public void setClickedId(String clickedId) {
        this.clickedId = clickedId;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getManipulatedDate() {
        return manipulatedDate;
    }

    public void setManipulatedDate(String manipulatedDate) {
        this.manipulatedDate = manipulatedDate;
    }
}
