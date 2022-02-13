package com.tms.api.dto.dashboard;

import java.util.List;
import com.tms.api.dto.dashboard.AgentRateMonitorDto;

public class AgentRateMonitorResponseDto{
    private List<AgentRateMonitorDto> data;
    private Integer leadTotal;

    public List<AgentRateMonitorDto> getData() {
        return data;
    }

    public void setData(List<AgentRateMonitorDto> data) {
        this.data = data;
    }

    public Integer getLeadTotal() {
        return leadTotal;
    }

    public void setLeadTotal(Integer leadTotal) {
        this.leadTotal = leadTotal;
    }
}