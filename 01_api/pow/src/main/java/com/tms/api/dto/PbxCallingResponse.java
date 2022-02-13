package com.tms.api.dto;


public class PbxCallingResponse {
    private String channelId;
    private Boolean status;
    private String playback;
    private String dialStatus;


    public String getPlayback() {
        return playback;
    }

    public void setPlayback(String playback) {
        this.playback = playback;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDialStatus() {
        return dialStatus;
    }

    public void setDialStatus(String dialStatus) {
        this.dialStatus = dialStatus;
    }
}