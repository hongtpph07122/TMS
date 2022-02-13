package com.tms.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Immutable;

import java.sql.Timestamp;

@Entity
@Table(name = "cf_cp_call_strategy_default")
public class CfCpCallStrategyDefault{
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "cp_id")
    private Integer cpId;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "cp_name")
    private String cpName;
    @Column(name = "cp_status")
    private Integer cpStatus;
    @Column(name = "cpcf_id")
    private Integer cpcfId;
    @Column(name = "config_type")
    private Integer configType;
    @Column(name = "config_value")
    private Integer configValue;
    @Column(name = "config_status")
    private Integer configStatus;
    @Column(name = "config_name")
    private String configName;
    @Column(name = "shortname")
    private String shortName;
    @Column(name = "order_phone_number")
    private Integer orderPhoneNumber;
    @Column(name = "call_status")
    private Integer callStatus;
    @Column(name = "attempt")
    private Integer attempt;
    @Column(name = "duration")
    private Integer duration;
    @Column(name = "day")
    private Integer day;
    @Column(name = "next_action")
    private String nextAction;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public Integer getCpStatus() {
        return cpStatus;
    }

    public void setCpStatus(Integer cpStatus) {
        this.cpStatus = cpStatus;
    }

    public Integer getCpcfId() {
        return cpcfId;
    }

    public void setCpcfId(Integer cpcfId) {
        this.cpcfId = cpcfId;
    }

    public Integer getConfigType() {
        return configType;
    }

    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    public Integer getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Integer configValue) {
        this.configValue = configValue;
    }

    public Integer getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(Integer configStatus) {
        this.configStatus = configStatus;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getOrderPhoneNumber() {
        return orderPhoneNumber;
    }

    public void setOrderPhoneNumber(Integer orderPhoneNumber) {
        this.orderPhoneNumber = orderPhoneNumber;
    }

    public Integer getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(Integer callStatus) {
        this.callStatus = callStatus;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getNextAction() {
        return nextAction;
    }

    public void setNextAction(String nextAction) {
        this.nextAction = nextAction;
    }

    @Override
    public String toString() {
        return "CfCpCallStrategyDefault{" +
                "id=" + id +
                ", cpId=" + cpId +
                ", orgId=" + orgId +
                ", cpName='" + cpName + '\'' +
                ", cpStatus=" + cpStatus +
                ", cpcfId=" + cpcfId +
                ", configType=" + configType +
                ", configValue=" + configValue +
                ", configStatus=" + configStatus +
                ", configName='" + configName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", orderPhoneNumber='" + orderPhoneNumber + '\'' +
                ", callStatus=" + callStatus +
                ", attempt=" + attempt +
                ", duration=" + duration +
                ", day=" + day +
                ", nextAction='" + nextAction + '\'' +
                '}';
    }
}
