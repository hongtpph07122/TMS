package com.tms.api.dto;

public class CreateUpdateOfferResponse {

    private String leadDestinationUrl;
    private String offerId;

    public String getLeadDestinationUrl() {
        return leadDestinationUrl;
    }

    public void setLeadDestinationUrl(String leadDestinationUrl) {
        this.leadDestinationUrl = leadDestinationUrl;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

}
