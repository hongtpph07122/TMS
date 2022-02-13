package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "trk_affiliate_mapping")
public class TrkAffiliateMapping implements Serializable {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "agc_id")
    private Integer agcId;
    @Column(name = "tracker_affiliate_id")
    private Integer trackerAffiliateId;
    @Column(name = "createdate")
    private Date createdate;
    @Column(name = "is_active")
    private Integer isActive;
    @Column(name = "tracker_id")
    private Integer trackerId;
    @Column(name = "agc_code")
    private String agcCode;

    public String getAgcCode() {
        return agcCode;
    }

    public void setAgcCode(String agcCode) {
        this.agcCode = agcCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getAgcId() {
        return agcId;
    }

    public void setAgcId(Integer agcId) {
        this.agcId = agcId;
    }

    public Integer getTrackerAffiliateId() {
        return trackerAffiliateId;
    }

    public void setTrackerAffiliateId(Integer trackerAffiliateId) {
        this.trackerAffiliateId = trackerAffiliateId;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }
}
