package com.tms.api.dto.dashboard;

/**
 * Created by dinhanhthai on 17/07/2019.
 */
public class TotalCallDto {
    private Integer connected;
    private Integer busy;
    private Integer invalid;
    private Integer total;

    public TotalCallDto(){
        connected = 0;
        busy = 0;
        invalid = 0;
        total = 0;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getConnected() {
        return connected;
    }

    public void setConnected(Integer connected) {
        this.connected = connected;
    }

    public Integer getBusy() {
        return busy;
    }

    public void setBusy(Integer busy) {
        this.busy = busy;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }
}
