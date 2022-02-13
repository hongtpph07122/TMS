package com.tms.dto;

public class GetTrackingCodeRespV3 extends GetTrackingCodeRespV2 {

    private String trackingCode;

	@Override
	public String getTrackingCode() {
		return trackingCode;
	}

	@Override
	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}
}
