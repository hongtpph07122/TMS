package com.tms.api.dto.dashboard;

import java.util.List;

/**
 * Created by dinhanhthai on 17/07/2019.
 */
public class PerfomanceCompareDto {
    List<ComparationDto> lst;

    public List<ComparationDto> getLst() {
        return lst;
    }

    public void setLst(List<ComparationDto> lst) {
        this.lst = lst;
    }
}
