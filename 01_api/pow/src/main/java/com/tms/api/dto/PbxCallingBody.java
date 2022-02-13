package com.tms.api.dto;

/**
 * Created by dinhanhthai on 03/07/2019.
 */
public class PbxCallingBody {
    private String fromNumber;
    private String toNumber;

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    //them 9 de quay so tu tong dai
    public String getToNumber() {
        return 9 + toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }
}
