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
@Table(name = "rp36_call_duration")
@IdClass(CallDuration.class)
public class CallDuration implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(columnDefinition = "day")
    private String day;
    @Id
    @Column(columnDefinition = "date")
    private String date;
    @Id
    @Column(columnDefinition = "agent01")
    private Integer agent01;
    @Id
    @Column(columnDefinition = "agent02")
    private Integer agent02;
    @Id
    @Column(columnDefinition = "agent03")
    private Integer agent03;
    @Id
    @Column(columnDefinition = "agent04")
    private Integer agent04;
    @Id
    @Column(columnDefinition = "agent05")
    private Integer agent05;
    @Id
    @Column(columnDefinition = "total_number_calls")
    private Integer total_number_calls;
    @Id
    @Column(columnDefinition = "total_call_duration")
    private String total_call_duration;
    @Id
    @Column(columnDefinition = "average_call_duration")
    private String average_call_duration;

    public String getDay() {
	return day;
    }

    public void setDay(String day) {
	this.day = day;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public Integer getAgent01() {
	return agent01;
    }

    public void setAgent01(Integer agent01) {
	this.agent01 = agent01;
    }

    public Integer getAgent02() {
	return agent02;
    }

    public void setAgent02(Integer agent02) {
	this.agent02 = agent02;
    }

    public Integer getAgent03() {
	return agent03;
    }

    public void setAgent03(Integer agent03) {
	this.agent03 = agent03;
    }

    public Integer getAgent04() {
	return agent04;
    }

    public void setAgent04(Integer agent04) {
	this.agent04 = agent04;
    }

    public Integer getAgent05() {
	return agent05;
    }

    public void setAgent05(Integer agent05) {
	this.agent05 = agent05;
    }

    public Integer getTotal_number_calls() {
	return total_number_calls;
    }

    public void setTotal_number_calls(Integer total_number_calls) {
	this.total_number_calls = total_number_calls;
    }

    public String getTotal_call_duration() {
	return total_call_duration;
    }

    public void setTotal_call_duration(String total_call_duration) {
	this.total_call_duration = total_call_duration;
    }

    public String getAverage_call_duration() {
	return average_call_duration;
    }

    public void setAverage_call_duration(String average_call_duration) {
	this.average_call_duration = average_call_duration;
    }

}
