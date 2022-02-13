package com.tms.api.dto;

import com.tms.entity.CLFresh;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinhanhthai on 16/07/2019.
 */
public class CLCallbackDto extends CLFresh {
    private List<CLFreshPhoneDto> lstPhone;
    private List<CLFreshPhoneDto> lstPhone2;
    private String closeTime;
    private String maxCloseTime;

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getMaxCloseTime() {
        return maxCloseTime;
    }

    public void setMaxCloseTime(String maxCloseTime) {
        this.maxCloseTime = maxCloseTime;
    }

    public List<CLFreshPhoneDto> getLstPhone() {
        if(lstPhone == null){
            lstPhone = new ArrayList<>();
            CLFreshPhoneDto clFreshPhoneDto = new CLFreshPhoneDto();
            clFreshPhoneDto.setPhoneNumber(super.getPhone());
            clFreshPhoneDto.setType(1);
            lstPhone.add(clFreshPhoneDto);
        }
        return lstPhone;
    }

    public void setLstPhone(List<CLFreshPhoneDto> lstPhone) {
        this.lstPhone = lstPhone;
    }

	public List<CLFreshPhoneDto> getLstPhone2() {
        if (lstPhone2 == null) {
        	lstPhone2 = new ArrayList<>();
            if (super.getOtherPhone1() != null && !super.getOtherPhone1().isEmpty()) {
                String[] phones = super.getOtherPhone1().split("\\|");
                for (int i = 0; i < phones.length; i++) {
                    CLFreshPhoneDto clFreshPhoneDto = new CLFreshPhoneDto();
                    clFreshPhoneDto.setPhoneNumber(phones[i]);
                    clFreshPhoneDto.setType(1);
                    lstPhone2.add(clFreshPhoneDto);
                }
            }
        }
        return lstPhone2;
	}

	public void setLstPhone2(List<CLFreshPhoneDto> lstPhone2) {
		this.lstPhone2 = lstPhone2;
	}
	
}
