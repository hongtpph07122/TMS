package com.tms.api.dto;

import com.tms.api.utils.DateUtils;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.GetCdrResp;

import java.util.Date;

public class GetCdrResponse {
    private Integer callId;
    private Integer orgId;
    private Integer partnerId;
    private Integer direction;
    private String channel;
    private Integer userId;
    private String userName;
    private Integer leadId;
    private String leadName;
    private String leadPhone;
    private Integer callStatus;
    private String callStatusName;
    private String callNote;
    private Date starttime;
    private Date stoptime;
    private Integer status;
    private Date createtime;
    private String playbackUrl;
    private String duration;
	private String leadStatus;

    public static GetCdrResponse buildMappedToDTO(GetCdrResp getCdrResp) {
        GetCdrResponse cdrRespV2 = new GetCdrResponse();
        cdrRespV2.setCallId(getCdrResp.getCallId());
        cdrRespV2.setOrgId(getCdrResp.getOrgId());
        cdrRespV2.setPartnerId(getCdrResp.getPartnerId());
        cdrRespV2.setDirection(getCdrResp.getDirection());
        cdrRespV2.setChannel(getCdrResp.getChannel());
        cdrRespV2.setUserId(getCdrResp.getUserId());
        cdrRespV2.setUserName(getCdrResp.getUserName());
        cdrRespV2.setLeadId(getCdrResp.getLeadId());
        cdrRespV2.setLeadName(getCdrResp.getLeadName());
        cdrRespV2.setLeadPhone(getCdrResp.getLeadPhone());
        cdrRespV2.setCallStatus(getCdrResp.getCallStatus());
        cdrRespV2.setCallStatusName(getCdrResp.getCallStatusName());
        cdrRespV2.setCallNote(getCdrResp.getCallNote());
        cdrRespV2.setStarttime(getCdrResp.getStarttime());
        cdrRespV2.setStoptime(getCdrResp.getStoptime());
        cdrRespV2.setStatus(getCdrResp.getStatus());
        cdrRespV2.setCreatetime(getCdrResp.getCreatetime());
        cdrRespV2.setPlaybackUrl(getCdrResp.getPlaybackUrl());
        if (!ObjectUtils.isNull(getCdrResp.getDuration())) {
            cdrRespV2.setDuration(DateUtils.exchangeSecondToTimeHHMMSSFormat(Math.round(Math.round(getCdrResp.getDuration()))));
        } else {
            cdrRespV2.setDuration("00:00:00");
        }
        cdrRespV2.setLeadStatus(getCdrResp.getLeadStatus());
        return cdrRespV2;
    }

    public String getLeadStatus() {
		return leadStatus;
	}

	public void setLeadStatus(String leadStatus) {
		this.leadStatus = leadStatus;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getCallStatusName() {
        return callStatusName;
    }

    public void setCallStatusName(String callStatusName) {
        this.callStatusName = callStatusName;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
