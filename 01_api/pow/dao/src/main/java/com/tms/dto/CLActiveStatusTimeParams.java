package com.tms.dto;

public class CLActiveStatusTimeParams {

    private Integer leadstatus;
    private String starttime;
    private String endtime;
    private Integer number;

    public Integer getLeadstatus() {
        return leadstatus;
    }

    public void setLeadstatus(Integer leadstatus) {
        this.leadstatus = leadstatus;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
