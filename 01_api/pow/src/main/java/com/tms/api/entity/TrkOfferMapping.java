package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "trk_offer_mapping")
public class TrkOfferMapping implements Serializable {

    @Id
    @Column(name = "mapping_id")
    private Integer mappingId;
    @Column(name = "offer_id")
    private Integer offerId;
    @Column(name = "tracker_offer_id")
    private Integer trackerOfferId;
    @Column(name = "tracker_affiliate_id")
    private String trackerAffiliateId;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "tracker_id")
    private Integer trackerId;


    public Integer getMappingId() {
        return mappingId;
    }

    public void setMappingId(Integer mappingId) {
        this.mappingId = mappingId;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
        this.offerId = offerId;
    }

    public Integer getTrackerOfferId() {
        return trackerOfferId;
    }

    public void setTrackerOfferId(Integer trackerOfferId) {
        this.trackerOfferId = trackerOfferId;
    }

    public String getTrackerAffiliateId() {
        return trackerAffiliateId;
    }

    public void setTrackerAffiliateId(String trackerAffiliateId) {
        this.trackerAffiliateId = trackerAffiliateId;
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
