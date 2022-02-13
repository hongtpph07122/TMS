package com.tms.api.dto;

import com.tms.api.helper.Const;
import com.tms.dto.GetPartnerResp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinhanhthai on 16/07/2019.
 */
public class GetPartnerDto {
    private boolean isSelected;
    private List<GetPartnerResp> lst;

    public List<GetPartnerResp> getLst() {
        return lst;
    }

    public void setLst(List<GetPartnerResp> lst) {
        this.lst = lst;
    }

    public GetPartnerDto(){
        lst = new ArrayList<>();
        isSelected = Const._IS_SELECTED_PARTNER_IN_ORDER;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
