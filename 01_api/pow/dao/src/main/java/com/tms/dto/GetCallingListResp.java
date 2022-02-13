package com.tms.dto;

import java.util.Date;

public class GetCallingListResp {

    private Integer callinglistId;
    private Integer orgId;
    private String clType;
    private String clTypeName;
    private String clCode;
    private String clName;
    private String dscr;
    private Integer productId;
    private Integer createby;
    private Date createdate;
    private Integer modifyby;
    private Date modifydate;

    public String getClType() {
        return clType;
    }

    public void setClType(String clType) {
        this.clType = clType;
    }

    public String getClTypeName() {
        return clTypeName;
    }

    public void setClTypeName(String clTypeName) {
        this.clTypeName = clTypeName;
    }

    public Integer getCallinglistId() {
        return callinglistId;
    }

    public void setCallinglistId(Integer callinglistId) {
        this.callinglistId = callinglistId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getClCode() {
        return clCode;
    }

    public void setClCode(String clCode) {
        this.clCode = clCode;
    }

    public String getClName() {
        return clName;
    }

    public void setClName(String clName) {
        this.clName = clName;
    }

    public String getDscr() {
        return dscr;
    }

    public void setDscr(String dscr) {
        this.dscr = dscr;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
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
}
