package com.tms.api.dto;

import java.util.List;

public class UpdateDOStatusDto {
    private List<Integer> doIds;
    private Integer status;
    
    public List<Integer> getDoIds() {
        return doIds;
    }
    public void setDoIds(List<Integer> doIds) {
        this.doIds = doIds;
    }
    
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
