package com.tms.dto;

public class GetProvinceMapResp {

    private Integer prmapId;
    private Integer regionId;
    private Integer prvId;
    private String name;
    private String shortname;
    private String code;
    private Integer partnerId;
    private String prvPartnerName ;
    private String prvPartnerCode ;


    public Integer getPrvId() {
        return prvId;
    }

    public void setPrvId(Integer prvId) {
        this.prvId = prvId;
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

    public Integer getPrmapId() {
        return prmapId;
    }

    public void setPrmapId(Integer prmapId) {
        this.prmapId = prmapId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrvPartnerName() {
        return prvPartnerName;
    }

    public void setPrvPartnerName(String prvPartnerName) {
        this.prvPartnerName = prvPartnerName;
    }

    public String getPrvPartnerCode() {
        return prvPartnerCode;
    }

    public void setPrvPartnerCode(String prvPartnerCode) {
        this.prvPartnerCode = prvPartnerCode;
    }
}
