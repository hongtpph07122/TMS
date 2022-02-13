package com.tms.api.dto.dashboard;

/**
 * Created by dinhanhthai on 17/07/2019.
 */
public class DashboardMonitor {
    private MySaleTunnelDto mySale;
    private StaticLeadDto lead;
    private PerfomanceCompareDto compare;
    private TotalCallDto totalCall;

    public MySaleTunnelDto getMySale() {
        return mySale;
    }

    public void setMySale(MySaleTunnelDto mySale) {
        this.mySale = mySale;
    }

    public StaticLeadDto getLead() {
        return lead;
    }

    public void setLead(StaticLeadDto lead) {
        this.lead = lead;
    }

    public PerfomanceCompareDto getCompare() {
        return compare;
    }

    public void setCompare(PerfomanceCompareDto compare) {
        this.compare = compare;
    }

    public TotalCallDto getTotalCall() {
        return totalCall;
    }

    public void setTotalCall(TotalCallDto totalCall) {
        this.totalCall = totalCall;
    }
}
