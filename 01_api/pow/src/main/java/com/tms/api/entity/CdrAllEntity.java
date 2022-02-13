package com.tms.api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cdr_all")
public class CdrAllEntity implements Serializable {
    @Column(name = "accountcode")
    private String accountCode;
    @Column(name = "src")
    private String src;
    @Column(name = "destination")
    private String destination;
    @Column(name = "destinationcontext")
    private String destinationContext;
    @Column(name = "callerid")
    private String callerId;
    @Column(name = "channel")
    private String channel;
    @Column(name = "destinationchannel")
    private String destinationChannel;
    @Column(name = "lastapplication")
    private String lastApplication;
    @Column(name = "lastdata")
    private String lastData;
    @Column(name = "starttime")
    private Date startTime;
    @Column(name = "answertime")
    private Date answerTime;
    @Column(name = "endtime")
    private Date endTime;
    @Column(name = "duration")
    private Double duration;
    @Column(name = "billableseconds")
    private Double billableSeconds;
    @Column(name = "disposition")
    private String disposition;
    @Column(name = "amaflags")
    private String amaFlags;
    @Column(name = "uniqueid")
    private String uniqueId;
    @Column(name = "userfield")
    private String userField;
    @Column(name = "privilege")
    private String privilege;
    @Column(name = "datereceived")
    private Date dateReceived;
    @Column(name = "url_playback")
    private String urlPlayback;
    @Column(name = "call_id")
    @Id
    @SequenceGenerator(name="cdr_generator", sequenceName = "seq_call_id", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cdr_generator")
    private Integer callId;
    @Column(name = "update_status")
    private Integer updateStatus;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getUrlPlayback() {
        return urlPlayback;
    }

    public void setUrlPlayback(String urlPlayback) {
        this.urlPlayback = urlPlayback;
    }


    public Integer getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(Integer updateStatus) {
        this.updateStatus = updateStatus;
    }

    public Integer getCallId() {
        return callId;
    }

    public void setCallId(Integer callId) {
        this.callId = callId;
    }
}
