package com.tms.entity.log;

public class InsSubDistrictMap {

    private Integer sdtmapId;
    private Integer sdtId;
    private Integer partnerId;
    private String pnCode;
    private String pnName;
    private String dcsr;

    public Integer getSdtmapId() {
        return sdtmapId;
    }

    public void setSdtmapId(Integer sdtmapId) {
        this.sdtmapId = sdtmapId;
    }

    public Integer getSdtId() {
        return sdtId;
    }

    public void setSdtId(Integer sdtId) {
        this.sdtId = sdtId;
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

    public String getDcsr() {
        return dcsr;
    }

    public void setDcsr(String dcsr) {
        this.dcsr = dcsr;
    }
}
