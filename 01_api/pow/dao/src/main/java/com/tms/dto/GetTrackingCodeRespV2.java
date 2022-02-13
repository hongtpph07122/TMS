package com.tms.dto;

public class GetTrackingCodeRespV2 extends GetTrackingCodeResp {

    private String lastmileStatus;
    private String attribute4;

	public String getLastmileStatus() {
		return lastmileStatus;
	}
	public void setLastmileStatus(String lastmileStatus) {
		this.lastmileStatus = lastmileStatus;
	}
	public String getAttribute4() {
		return attribute4;
	}
	public void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}
    
}
