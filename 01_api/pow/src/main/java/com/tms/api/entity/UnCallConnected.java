package com.tms.api.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "log_uncall_connected")
public class UnCallConnected {

    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "lead_id")
    private String leadId;
    @Column(name = "agent_id")
    private Integer agentId;
    @Column(name = "action_time")
    private Date actionTime;
    @Column(name = "status")
    private Integer status;
    @Column(name = "playback_url")
    private String playbackUrl;
    @Column(name = "unique_id")
    private String uniqueId;
    @Column(name = "modify_date")
    private Date modifyDate;
    @Column(name = "is_update_playback_url")
    private Integer isUpdatePlaybackUrl;
    @Column(name = "org_id")
    private Integer orgId;

    public UnCallConnected() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getIsUpdatePlaybackUrl() {
        return isUpdatePlaybackUrl;
    }

    public void setIsUpdatePlaybackUrl(Integer isUpdatePlaybackUrl) {
        this.isUpdatePlaybackUrl = isUpdatePlaybackUrl;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}
