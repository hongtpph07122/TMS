package com.tms.dto;

public class GetSubdistrictMap {

    private Integer dtId;
    private Integer subdistrictId;
    private String name;
    private String shortname;
    private String code;
    private Integer sdtmapId;
    private Integer partnerId;
    private String wardsPartnerName;
    private String wardsPartnerCode;
    private Integer limit;
    private Integer offset;


    public Integer getDtId() {
        return dtId;
    }

    public void setDtId(Integer dtId) {
        this.dtId = dtId;
    }

    public Integer getSubdistrictId() {
        return subdistrictId;
    }

    public void setSubdistrictId(Integer subdistrictId) {
        this.subdistrictId = subdistrictId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSdtmapId() {
        return sdtmapId;
    }

    public void setSdtmapId(Integer sdtmapId) {
        this.sdtmapId = sdtmapId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getWardsPartnerName() {
        return wardsPartnerName;
    }

    public void setWardsPartnerName(String wardsPartnerName) {
        this.wardsPartnerName = wardsPartnerName;
    }

    public String getWardsPartnerCode() {
        return wardsPartnerCode;
    }

    public void setWardsPartnerCode(String wardsPartnerCode) {
        this.wardsPartnerCode = wardsPartnerCode;
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
}
