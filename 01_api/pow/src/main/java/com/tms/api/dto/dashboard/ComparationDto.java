package com.tms.api.dto.dashboard;

import java.util.List;

/**
 * Created by dinhanhthai on 17/07/2019.
 */
public class ComparationDto {
    private String label;

    private List<ObjectDto> lst;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<ObjectDto> getLst() {
        return lst;
    }

    public void setLst(List<ObjectDto> lst) {
        this.lst = lst;
    }
}
