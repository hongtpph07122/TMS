package com.tms.entity.log;

public class InsCPConfig {

    private Integer cpcfId;
    private Integer cpId;
    private Integer type;
    private Integer value;
    private String dscr;
    private Integer status;
    private Integer modifyby;
    private String modifydate;


    public Integer getCpcfId() {
        return cpcfId;
    }

    public void setCpcfId(Integer cpcfId) {
        this.cpcfId = cpcfId;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDscr() {
        return dscr;
    }

    public void setDscr(String dscr) {
        this.dscr = dscr;
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
