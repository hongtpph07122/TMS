package com.tms.api.dto;

/**
 * Created by dinhanhthai on 16/07/2019.
 */
public class CLFreshPhoneDto {
    private String phoneNumber;
    private Integer type;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
