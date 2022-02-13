package com.tms.dto;

import java.util.Date;

public class GetOrganizationResp {

    private Integer orgId;
    private String name;
    private String shortName;
    private String dscr;
    private String director;
    private String phone;
    private String address;
    private Integer modifyby ;
    private Date modifydate ;
    private String orgType ;
    private Integer parentOrgId;
    private String logoPath01;
    private String logoPath02;
    private Integer level ;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDscr() {
        return dscr;
    }

    public void setDscr(String dscr) {
        this.dscr = dscr;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public Date getModifydate() {
        return modifydate;
    }

    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public Integer getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Integer parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getLogoPath01() {
        return logoPath01;
    }

    public void setLogoPath01(String logoPath01) {
        this.logoPath01 = logoPath01;
    }

    public String getLogoPath02() {
        return logoPath02;
    }

    public void setLogoPath02(String logoPath02) {
        this.logoPath02 = logoPath02;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
