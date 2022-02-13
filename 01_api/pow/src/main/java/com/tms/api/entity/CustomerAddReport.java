// tag::sample[]
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
@Table(name = "rp38_customer_add_report")
@IdClass(CustomerAddReport.class)
public class CustomerAddReport implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(columnDefinition = "date")
    private String date;
	
	@Id
	@Column(columnDefinition = "agent_name01")
    private int agentName01;
	
	@Id
	@Column(columnDefinition = "agent_name02")
    private int agentName02;
	
	@Id
	@Column(columnDefinition = "agent_name03")
    private int agentName03;
	
	@Id
	@Column(columnDefinition = "agent_name04")
    private int agentName04;
	
	@Id
	@Column(columnDefinition = "total_ae")
    private int totalAe;


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getAgentName01() {
		return agentName01;
	}

	public void setAgentName01(int agentName01) {
		this.agentName01 = agentName01;
	}

	public int getAgentName02() {
		return agentName02;
	}

	public void setAgentName02(int agentName02) {
		this.agentName02 = agentName02;
	}

	public int getAgentName03() {
		return agentName03;
	}

	public void setAgentName03(int agentName03) {
		this.agentName03 = agentName03;
	}

	public int getAgentName04() {
		return agentName04;
	}

	public void setAgentName04(int agentName04) {
		this.agentName04 = agentName04;
	}

	public int getTotalAe() {
		return totalAe;
	}

	public void setTotalAe(int totalAe) {
		this.totalAe = totalAe;
	}
	
	
}

