package com.tms.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "trk_data")
public class TrkData implements Serializable {

    @Id
    @SequenceGenerator(name="trkdata_generator", sequenceName = "seq_trk_data", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trkdata_generator")
    @Column(name = "id")
    private Integer id;
    @Column(name = "lead_id")
    private Integer leadId;
    @Column(name = "agc_id")
    private Integer agcId;
    @Column(name = "click_id")
    private String clickId;
    @Column(name = "tracker_aff_id")
    private String trackerAffId;
    @Column(name = "tracker_offer_id")
    private Integer trackerOfferId;
    @Column(name = "tracker_goal_id")
    private String trackerGoalId;
    @Column(name = "tracker_adv_click_id")
    private String trackerAdvClickId;
    @Column(name = "tracker_adv_track_id")
    private String trackerAdvTrackId;
    @Column(name = "tracker_adv_order_id")
    private String trackerAdvOrderId;
    @Column(name = "tracker_adv_user_id")
    private Integer trackerAdvUserId;
    @Column(name = "tracker_adv_param1")
    private String trackerAdvParam1;
    @Column(name = "tracker_adv_param2")
    private String trackerAdvParam2;
    @Column(name = "tracker_adv_param3")
    private String trackerAdvParam3;
    @Column(name = "tracker_adv_param4")
    private String trackerAdvParam4;
    @Column(name = "tracker_adv_param5")
    private String trackerAdvParam5;
    @Column(name = "offer_id")
    private Integer offerId;
    @Column(name = "createdate")
    private Date createDate;
    @Column(name = "updatedate")
    private Date updateDate;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "tracker_id")
    private Integer trackerId;

    public Integer getAgcId() {
        return agcId;
    }

    public void setAgcId(Integer agcId) {
        this.agcId = agcId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getClickId() {
        return clickId;
    }

    public void setClickId(String clickId) {
        this.clickId = clickId;
    }

    public String getTrackerAffId() {
        return trackerAffId;
    }

    public void setTrackerAffId(String trackerAffId) {
        this.trackerAffId = trackerAffId;
    }

    public Integer getTrackerOfferId() {
        return trackerOfferId;
    }

    public void setTrackerOfferId(Integer trackerOfferId) {
        this.trackerOfferId = trackerOfferId;
    }

    public String getTrackerGoalId() {
        return trackerGoalId;
    }

    public void setTrackerGoalId(String trackerGoalId) {
        this.trackerGoalId = trackerGoalId;
    }

    public String getTrackerAdvClickId() {
        return trackerAdvClickId;
    }

    public void setTrackerAdvClickId(String trackerAdvClickId) {
        this.trackerAdvClickId = trackerAdvClickId;
    }

    public String getTrackerAdvTrackId() {
        return trackerAdvTrackId;
    }

    public void setTrackerAdvTrackId(String trackerAdvTrackId) {
        this.trackerAdvTrackId = trackerAdvTrackId;
    }

    public String getTrackerAdvOrderId() {
        return trackerAdvOrderId;
    }

    public void setTrackerAdvOrderId(String trackerAdvOrderId) {
        this.trackerAdvOrderId = trackerAdvOrderId;
    }

    public Integer getTrackerAdvUserId() {
        return trackerAdvUserId;
    }

    public void setTrackerAdvUserId(Integer trackerAdvUserId) {
        this.trackerAdvUserId = trackerAdvUserId;
    }

    public String getTrackerAdvParam1() {
        return trackerAdvParam1;
    }

    public void setTrackerAdvParam1(String trackerAdvParam1) {
        this.trackerAdvParam1 = trackerAdvParam1;
    }

    public String getTrackerAdvParam2() {
        return trackerAdvParam2;
    }

    public void setTrackerAdvParam2(String trackerAdvParam2) {
        this.trackerAdvParam2 = trackerAdvParam2;
    }

    public String getTrackerAdvParam3() {
        return trackerAdvParam3;
    }

    public void setTrackerAdvParam3(String trackerAdvParam3) {
        this.trackerAdvParam3 = trackerAdvParam3;
    }

    public String getTrackerAdvParam4() {
        return trackerAdvParam4;
    }

    public void setTrackerAdvParam4(String trackerAdvParam4) {
        this.trackerAdvParam4 = trackerAdvParam4;
    }

    public String getTrackerAdvParam5() {
        return trackerAdvParam5;
    }

    public void setTrackerAdvParam5(String trackerAdvParam5) {
        this.trackerAdvParam5 = trackerAdvParam5;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }
}
