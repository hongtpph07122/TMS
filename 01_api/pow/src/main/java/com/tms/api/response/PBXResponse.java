package com.tms.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/*
{
   "eventType":"AgentStatus",
   "action":"End",
   "uniqueId":"1622620889.1668",
   "extension":"8101",
   "phone":"0586337315",
   "direction":"Outbound",
   "startTime":"2021-06-02 15:01:30",
   "endTime":"2021-06-02 15:01:43",
   "status":"ANSWERED",
   "eventTime":"2021-06-02 15:01:43",
   "playbackUrl":"http://27.71.226.238:8858/playback/out-90586337315-8101-20210602-1622620889.1668.wav",
   "channelType":"SIP"
}
*/
@JsonIgnoreProperties(ignoreUnknown = true)
public class PBXResponse {
    private String eventType;
    private String action;
    private String uniqueId;
    private String extension;
    private String phone;
    private String direction;
    private String startTime;
    private String endTime;
    private String status;
    private String eventTime;
    private String playbackUrl;
    private String channelType;

    public PBXResponse() {
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getPlaybackUrl() {
        return playbackUrl;
    }

    public void setPlaybackUrl(String playbackUrl) {
        this.playbackUrl = playbackUrl;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
}
