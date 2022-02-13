package com.tms.api.dto.Request;

public class ScaleoCreateClickIdReq {

    private String offer_id;
    private Integer affiliate_id;
    private String aff_click_id;
    private String sub_id5;

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public Integer getAffiliate_id() {
        return affiliate_id;
    }

    public void setAffiliate_id(Integer affiliate_id) {
        this.affiliate_id = affiliate_id;
    }

    public String getAff_click_id() {
        return aff_click_id;
    }

    public void setAff_click_id(String aff_click_id) {
        this.aff_click_id = aff_click_id;
    }

    public String getSub_id5() {
        return sub_id5;
    }

    public void setSub_id5(String sub_id5) {
        this.sub_id5 = sub_id5;
    }
}
