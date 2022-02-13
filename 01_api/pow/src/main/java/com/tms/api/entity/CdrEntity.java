package com.tms.api.entity;


import com.tms.dto.GetCdrResp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "cdr")
public class CdrEntity {
    @Id
    @Column(name = "call_id")
    private Integer callId;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "partner_id")
    private Integer partnerId;
    @Column(name = "direction")
    private Integer direction;
    @Column(name = "channel")
    private String channel;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "lead_id")
    private Integer leadId;
    @Column(name = "lead_name")
    private String leadName;
    @Column(name = "lead_phone")
    private String leadPhone;
    @Column(name = "call_status")
    private Integer callStatus;
    @Column(name = "call_note")
    private String callNote;
    @Column(name = "starttime")
    private Date starttime;
    @Column(name = "stoptime")
    private Date stoptime;
    @Column(name = "status")
    private Integer status;
    @Column(name = "createtime")
    private Date createtime;
    @Column(name = "playback_url")
    private String playbackUrl;
    @Column(name = "duration")
    private Double duration;

    public static CdrEntity buildEntity(GetCdrResp insCdrV2) {
        CdrEntity cdrEntity = new CdrEntity();
        cdrEntity.setCallId(insCdrV2.getCallId());
        cdrEntity.setOrgId(insCdrV2.getOrgId());
        cdrEntity.setPartnerId(insCdrV2.getPartnerId());
        cdrEntity.setDirection(insCdrV2.getDirection());
        cdrEntity.setChannel(insCdrV2.getChannel());
        cdrEntity.setUserId(insCdrV2.getUserId());
        cdrEntity.setLeadId(insCdrV2.getLeadId());
        cdrEntity.setLeadName(insCdrV2.getLeadName());
        cdrEntity.setLeadPhone(insCdrV2.getLeadPhone());
        cdrEntity.setCallStatus(insCdrV2.getCallStatus());
        cdrEntity.setCallNote(insCdrV2.getCallNote());
        cdrEntity.setStarttime(insCdrV2.getStarttime());
        cdrEntity.setStoptime(insCdrV2.getStoptime());
        cdrEntity.setStatus(insCdrV2.getStatus());
        cdrEntity.setCreatetime(insCdrV2.getCreatetime());
        cdrEntity.setPlaybackUrl(insCdrV2.getPlaybackUrl());
        cdrEntity.setDuration(insCdrV2.getDuration());
        return cdrEntity;
    }

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getLeadName() {
        return leadName;
    }

    public void setLeadName(String leadName) {
        this.leadName = leadName;
    }

    public String getLeadPhone() {
        return leadPhone;
    }

    public void setLeadPhone(String leadPhone) {
        this.leadPhone = leadPhone;
    }

    public Integer getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Integer callStatus) {
        this.callStatus = callStatus;
    }

    public String getCallNote() {
        return callNote;
    }

    public void setCallNote(String callNote) {
        this.callNote = callNote;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getStoptime() {
        return stoptime;
    }

    public void setStoptime(Date stoptime) {
        this.stoptime = stoptime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }
}
