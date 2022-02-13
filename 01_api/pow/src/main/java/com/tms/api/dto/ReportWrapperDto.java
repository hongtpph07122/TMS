package com.tms.api.dto;

import java.util.ArrayList;
import java.util.List;

import com.tms.api.entity.SearchCriteria;
import com.tms.api.helper.Const;

public class ReportWrapperDto {
    List<SearchCriteria> conditions;
    Integer limit;
    Integer offset;

    public List<SearchCriteria> getConditions() {
        if (conditions == null) {
            conditions = new ArrayList<SearchCriteria>();
        }
        return conditions;
    }

    public void setConditions(List<SearchCriteria> conditions) {
        this.conditions = conditions;
    }

    public int getLimit() {
        if (limit == null || limit <= 0) {
            limit = Const.DEFAULT_PAGE_SIZE;
        }
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public int getOffset() {
        if (offset == null || offset < 0) {
            offset = Const.DEFAULT_PAGE;
        }
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

}
