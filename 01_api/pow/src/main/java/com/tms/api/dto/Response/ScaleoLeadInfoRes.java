package com.tms.api.dto.Response;

public class ScaleoLeadInfoRes {

    private String lead_id;
    private String status;
    private String message;
    private Integer affiliate_id;
    private Integer offer_id;
    private Integer goal_id;

    public String getLead_id() {
        return lead_id;
    }

    public void setLead_id(String lead_id) {
        this.lead_id = lead_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getAffiliate_id() {
        return affiliate_id;
    }

    public void setAffiliate_id(Integer affiliate_id) {
        this.affiliate_id = affiliate_id;
    }

    public Integer getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(Integer offer_id) {
        this.offer_id = offer_id;
    }

    public Integer getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(Integer goal_id) {
        this.goal_id = goal_id;
    }
}
