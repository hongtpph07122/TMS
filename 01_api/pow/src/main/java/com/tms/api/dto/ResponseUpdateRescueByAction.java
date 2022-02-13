package com.tms.api.dto;

import com.tms.api.entity.OdDONew;
import com.tms.api.entity.RcActivity;
import com.tms.api.entity.RcRescueJob;

import java.util.List;

public class ResponseUpdateRescueByAction {
    private List<RcRescueJob> rcRescueJobs;
    private List<RcActivity> rcActivities;

    public List<RcRescueJob> getRcRescueJobs() {
        return rcRescueJobs;
    }

    public void setRcRescueJobs(List<RcRescueJob> rcRescueJobs) {
        this.rcRescueJobs = rcRescueJobs;
    }

    public List<RcActivity> getRcActivities() {
        return rcActivities;
    }

    public void setRcActivities(List<RcActivity> rcActivities) {
        this.rcActivities = rcActivities;
    }
}
