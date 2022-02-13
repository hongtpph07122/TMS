package com.tms.api.dto;

import java.util.List;

import com.tms.dto.GetValidation7Resp;

public class ValidationRespDto {

    private List<GetValidation7Resp> getValidation7Resps;
    private Integer rowCount;

    public List<GetValidation7Resp> getGetValidation7Resps() {
        return getValidation7Resps;
    }

    public void setGetValidation7Resps(List<GetValidation7Resp> getValidation7Resps) {
        this.getValidation7Resps = getValidation7Resps;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }
}
