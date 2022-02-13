package com.tms.api.dto.dashboard;

/**
 * Created by dinhanhthai on 17/07/2019.
 */
public class StaticLeadDto {
    private Integer approved;
    private Integer rejected;
    private Integer callback;
    private Integer trash;
    private Integer noanws;
    private Integer busy;
    private Integer unreach;
    private Integer total;
    private Integer unCall;

	public StaticLeadDto(){
        approved = 0;
        rejected = 0;
        callback = 0;
        trash = 0;
        noanws = 0;
        busy = 0;
        unreach = 0;
        total = 0;
    }

    public Integer getCallingConnected(){
        return approved + rejected + callback;
    }
    public Integer getCallingBusy(){
        return busy + noanws + unreach;
    }
    public Integer getCallingTotal(){
        return getCallingConnected() + getCallingBusy() + trash;
    }

    public Integer getBusy() {
        return busy;
    }

    public void setBusy(Integer busy) {
        this.busy = busy;
    }

    public Integer getUnreach() {
        return unreach;
    }

    public void setUnreach(Integer unreach) {
        this.unreach = unreach;
    }

    public Integer getNoanws() {
        return noanws;
    }

    public void setNoanws(Integer noanws) {
        this.noanws = noanws;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getApproved() {
        return approved;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public Integer getRejected() {
        return rejected;
    }

    public void setRejected(Integer rejected) {
        this.rejected = rejected;
    }

    public Integer getCallback() {
        return callback;
    }

    public void setCallback(Integer callback) {
        this.callback = callback;
    }

    public Integer getTrash() {
        return trash;
    }

    public void setTrash(Integer trash) {
        this.trash = trash;
    }
    public Integer getUnCall() {
		return unreach + busy + noanws;
	}

	public void setUnCall(Integer unCall) {
		this.unCall = unCall;
	}
}
