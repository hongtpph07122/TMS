package com.tms.dto;

import java.lang.String;

public class GetDistrictMapResp {

    private Integer prvId;
    private Integer districtId;
    private String name;
    private String shortname;
    private String code;
    private Integer dtmapId;
    private Integer partnerId;
    private String dtPartnerName;
    private String dtPartnerCode;
    private String pnDistrictId;
    private Integer limit;
    private Integer offset;


    public String getPnDistrictId() {
        return pnDistrictId;
    }

    public void setPnDistrictId(String pnDistrictId) {
        this.pnDistrictId = pnDistrictId;
    }

    public Integer getPrvId() {
        return prvId;
    }

    public void setPrvId(Integer prvId) {
        this.prvId = prvId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
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

    public Integer getDtmapId() {
        return dtmapId;
    }

    public void setDtmapId(Integer dtmapId) {
        this.dtmapId = dtmapId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getDtPartnerName() {
        return dtPartnerName;
    }

    public void setDtPartnerName(String dtPartnerName) {
        this.dtPartnerName = dtPartnerName;
    }

    public String getDtPartnerCode() {
        return dtPartnerCode;
    }

    public void setDtPartnerCode(String dtPartnerCode) {
        this.dtPartnerCode = dtPartnerCode;
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
