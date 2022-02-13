package com.tms.api.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "rp32_dialled_list_sumary")
@IdClass(DialledListSumary.class)
public class DialledListSumary implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(columnDefinition = "campaign_name")
    private String campaignName;
    @Id
    @Column(columnDefinition = "product_name")
    private String productName;
    @Id
    @Column(columnDefinition = "agent_name")
    private String agentName;
    @Id
    @Column(columnDefinition = "date")
    private String date;
    @Id
    @Column(columnDefinition = "total_calls")
    private String totalCalls;
    @Id
    @Column(columnDefinition = "answer")
    private String answer;
    @Id
    @Column(columnDefinition = "noanswer")
    private String noanswer;
    @Id
    @Column(columnDefinition = "busy")
    private String busy;

    public String getCampaignName() {
	return campaignName;
    }

    public void setCampaignName(String campaignName) {
	this.campaignName = campaignName;
    }

    public String getProductName() {
	return productName;
    }

    public void setProductName(String productName) {
	this.productName = productName;
    }

    public String getAgentName() {
	return agentName;
    }

    public void setAgentName(String agentName) {
	this.agentName = agentName;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public String getTotalCalls() {
	return totalCalls;
    }

    public void setTotalCalls(String totalCalls) {
	this.totalCalls = totalCalls;
    }

    public String getAnswer() {
	return answer;
    }

    public void setAnswer(String answer) {
	this.answer = answer;
    }

    public String getNoanswer() {
	return noanswer;
    }

    public void setNoanswer(String noanswer) {
	this.noanswer = noanswer;
    }

    public String getBusy() {
	return busy;
    }

    public void setBusy(String busy) {
	this.busy = busy;
    }

}
