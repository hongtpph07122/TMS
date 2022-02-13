package com.tms.api.dto.Request;

public class ScaleoCreateLeadReq {
    private Integer affiliate_id;
    private String adv_user_id;
    private String adv_param4;
    private String sub_id1;
    private Integer goal_id;
    private String sub_id5;
    private String aff_click_id;


    public Integer getAffiliate_id() {
        return affiliate_id;
    }

    public void setAffiliate_id(Integer affiliate_id) {
        this.affiliate_id = affiliate_id;
    }

    public String getSub_id1() {
        return sub_id1;
    }

    public void setSub_id1(String sub_id1) {
        this.sub_id1 = sub_id1;
    }

    public String getAdv_param4() {
        return adv_param4;
    }

    public void setAdv_param4(String adv_param4) {
        this.adv_param4 = adv_param4;
    }

    public Integer getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(Integer goal_id) {
        this.goal_id = goal_id;
    }

    public String getSub_id5() {
        return sub_id5;
    }

    public void setSub_id5(String sub_id5) {
        this.sub_id5 = sub_id5;
    }

    public String getAff_click_id() {
        return aff_click_id;
    }

    public void setAff_click_id(String aff_click_id) {
        this.aff_click_id = aff_click_id;
    }

    public String getAdv_user_id() {
        return adv_user_id;
    }

    public void setAdv_user_id(String adv_user_id) {
        this.adv_user_id = adv_user_id;
    }
}
