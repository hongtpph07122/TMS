package com.tms.api.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdLeadReassignDto {
    @NotEmpty(message="Cannot be empty" )
    List<Integer> leadIds;
    @NotNull(message = "Cannot be null")
    Integer agentId;
    Integer status;

    public List<Integer> getLeadIds() {
        return leadIds;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public Integer getStatus() {
        return status;
    }
   
}
