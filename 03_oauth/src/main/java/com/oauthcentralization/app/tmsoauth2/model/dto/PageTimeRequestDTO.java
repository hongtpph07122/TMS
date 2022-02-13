package com.oauthcentralization.app.tmsoauth2.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageTimeRequestDTO {
    private Date startTime;
    private Date endTime;
    private Date intervalTime;
    private Date intervalTimePrediction;

    public PageTimeRequestDTO() {
    }

    public PageTimeRequestDTO(Date startTime, Date endTime, Date intervalTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Date intervalTime) {
        this.intervalTime = intervalTime;
    }

    public Date getIntervalTimePrediction() {
        return intervalTimePrediction;
    }

    public void setIntervalTimePrediction(Date intervalTimePrediction) {
        this.intervalTimePrediction = intervalTimePrediction;
    }
}
