package com.tms.dto;

import com.tms.entity.Subdistrict;

public class GetSubDistrictByPartner extends Subdistrict {

    private Integer sdtId;
    private String name;
    private String shortname;
    private String code;
    private Integer partnerId;
    private String pnCode;
    private String pnName;
    private Integer limit;
    private Integer offset;

    public Integer getSdtId() {
        return sdtId;
    }

    public void setSdtId(Integer sdtId) {
        this.sdtId = sdtId;
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

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getPnCode() {
        return pnCode;
    }

    public void setPnCode(String pnCode) {
        this.pnCode = pnCode;
    }

    public String getPnName() {
        return pnName;
    }

    public void setPnName(String pnName) {
        this.pnName = pnName;
    }
}
