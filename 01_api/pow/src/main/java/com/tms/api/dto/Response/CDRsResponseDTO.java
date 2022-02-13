package com.tms.api.dto.Response;

import com.tms.api.dto.GetCdrResponse;
import com.tms.api.utils.DateUtils;
import com.tms.api.utils.ExchangeUtils;
import com.tms.api.utils.MathUtils;
import com.tms.api.utils.StringUtility;
import com.tms.api.variable.PatternEpochVariable;
import org.springframework.util.StringUtils;

public class CDRsResponseDTO {

    private Integer callId;
    private Integer orgId;
    private Integer partnerId;
    private String channel;
    private Integer userId;
    private String userName;
    private Integer leadId;
    private String leadName;
    private String leadPhone;
    private Integer callStatus;
    private String callStatusName;
    private String callNote;
    private String starttime;
    private String stoptime;
    private Integer status;
    private String createtime;
    private String playbackUrl;
    private String duration;
    private Integer direction;

    public CDRsResponseDTO() {
    }

    public static CDRsResponseDTO buildMapped(GetCdrResponse CDRsResponse) {
        CDRsResponseDTO cdRsResponseDTO = new CDRsResponseDTO();
        cdRsResponseDTO.setCallId(CDRsResponse.getCallId());
        cdRsResponseDTO.setOrgId(CDRsResponse.getOrgId());
        cdRsResponseDTO.setPartnerId(CDRsResponse.getPartnerId());
        cdRsResponseDTO.setDirection(CDRsResponse.getDirection());
        cdRsResponseDTO.setChannel(CDRsResponse.getChannel());
        cdRsResponseDTO.setUserId(CDRsResponse.getUserId());
        cdRsResponseDTO.setUserName(CDRsResponse.getUserName());
        cdRsResponseDTO.setLeadId(CDRsResponse.getLeadId());
        cdRsResponseDTO.setLeadName(CDRsResponse.getLeadName());
        cdRsResponseDTO.setLeadPhone(CDRsResponse.getLeadPhone());
        cdRsResponseDTO.setCallStatus(CDRsResponse.getCallStatus());
        cdRsResponseDTO.setCallStatusName(CDRsResponse.getCallStatusName());
        cdRsResponseDTO.setCallNote(CDRsResponse.getCallNote());
        cdRsResponseDTO.setStarttime(DateUtils.snagPatternStage(PatternEpochVariable.ASIA_BIBLIOGRAPHY_EPOCH_PATTERN, CDRsResponse.getStarttime()));
        /* custom here stop time */
        if (StringUtils.isEmpty(CDRsResponse.getDuration())) {
            cdRsResponseDTO.setStoptime(cdRsResponseDTO.getStarttime());
        } else {
            String[] startTime = StringUtility.trimSingleWhitespace(cdRsResponseDTO.getStarttime()).split("\\s");
            String[] first = startTime[1].split(":");
            String[] second = CDRsResponse.getDuration().split(":");
            int[] noFirst = ExchangeUtils.exchangeStringArrayToIntegerArray(first);

            int[] noSecond = ExchangeUtils.exchangeStringArrayToIntegerArray(second);
            int[] result = MathUtils.add(noFirst, noSecond);
            String[] elements = ExchangeUtils.exchangeIntegerArrayToStringArray(result);
            String line = elements[0] + ":" + elements[1] + ":" + elements[2];
            long seconds = DateUtils.feedAsNumberSecondOfHHMMSS(line);
            cdRsResponseDTO.setStoptime(startTime[0].concat(" ").concat(DateUtils.feedAsSecondInHHMMSS(seconds)));
        }

        cdRsResponseDTO.setStatus(CDRsResponse.getStatus());
        cdRsResponseDTO.setCreatetime(DateUtils.snagPatternStage(PatternEpochVariable.ASIA_BIBLIOGRAPHY_EPOCH_PATTERN, CDRsResponse.getCreatetime()));
        cdRsResponseDTO.setPlaybackUrl(CDRsResponse.getPlaybackUrl());
        cdRsResponseDTO.setDuration(CDRsResponse.getDuration());
        return cdRsResponseDTO;
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

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
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
