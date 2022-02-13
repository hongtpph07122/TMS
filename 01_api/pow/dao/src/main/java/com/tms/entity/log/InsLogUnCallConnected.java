package com.tms.entity.log;

public class InsLogUnCallConnected {
    private String leadId;
    private Integer agentId;
    private String actionTime;
    private Integer status;
    private String playbackUrl;
    private String uniqueId;
    private String modifyDate;
    private Integer isUpdatePlaybackUrl;
    private Integer orgId;

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

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
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

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
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
