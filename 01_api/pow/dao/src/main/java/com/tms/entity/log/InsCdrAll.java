package com.tms.entity.log;

import java.lang.Double;
import java.lang.String;

public class InsCdrAll {

    private String accountCode;
    private String src;
    private String destination;
    private String destinationContext;
    private String callerId;
    private String channel;
    private String destinationChannel;
    private String lastApplication;
    private String lastData;
    private String startTime;
    private String answerTime;
    private String endTime;
    private Double duration;
    private Double billableSeconds;
    private String disposition;
    private String amaFlags;
    private String uniqueId;
    private String userField;
    private String privilege;
    private String dateReceived;
    private String userAnswerTime;
    private Double calledSeconds;

    public Double getCalledSeconds() {
        return calledSeconds;
    }

    public void setCalledSeconds(Double calledSeconds) {
        this.calledSeconds = calledSeconds;
    }

    public String getUserAnswerTime() {
        return userAnswerTime;
    }

    public void setUserAnswerTime(String userAnswerTime) {
        this.userAnswerTime = userAnswerTime;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationContext() {
        return destinationContext;
    }

    public void setDestinationContext(String destinationContext) {
        this.destinationContext = destinationContext;
    }

    public String getCallerId() {
        return callerId;
    }

    public void setCallerId(String callerId) {
        this.callerId = callerId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDestinationChannel() {
        return destinationChannel;
    }

    public void setDestinationChannel(String destinationChannel) {
        this.destinationChannel = destinationChannel;
    }

    public String getLastApplication() {
        return lastApplication;
    }

    public void setLastApplication(String lastApplication) {
        this.lastApplication = lastApplication;
    }

    public String getLastData() {
        return lastData;
    }

    public void setLastData(String lastData) {
        this.lastData = lastData;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getBillableSeconds() {
        return billableSeconds;
    }

    public void setBillableSeconds(Double billableSeconds) {
        this.billableSeconds = billableSeconds;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getAmaFlags() {
        return amaFlags;
    }

    public void setAmaFlags(String amaFlags) {
        this.amaFlags = amaFlags;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getUserField() {
        return userField;
    }

    public void setUserField(String userField) {
        this.userField = userField;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

}
