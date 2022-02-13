package com.tms.dto;

import com.tms.entity.CLFresh;

public class CLFreshLog extends CLFresh {
    private String callId;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
