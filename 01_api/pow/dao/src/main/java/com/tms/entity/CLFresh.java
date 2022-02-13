package com.tms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CLFresh {

	private Integer leadId;
	private String source; //
	private String agcCode;
	private Integer orgId;
	private String ccCode;
	private String name;
	private String campaignName;
	private String callinglistName;
	private String callinglistId;
	private Integer agcId;
	private String phone;
	private Integer prodId;
	private String prodName;
	private Integer assigned;
	private String assignedName;
	private Integer calledby;
	private String calledbyName;
	private String address;
	private String province;
	private String district;
	private String subdistrict;
	private String comment;
	private Integer lastCallStatus;
	private String lastcallStatusName;
	private Integer dayCall;
	private Integer totalCall;
	private String amount;
	private Integer leadStatus;
	private String leadStatusName;
	private Integer result;
	private String userDefin01;
	private String userDefin02;
	private String userDefin03;
	private String userDefin04;
	private String userDefin05;
	private String attribute;
	private Date createdate;
	private Date modifydate;
	private Integer modifyby;
	private String leadType;
	private String agcLeadAddress;
	private Integer cpId;
	private Integer clGroup; //
	private String otherName1;
	private String otherPhone1;
	private String otherName2;
	private String otherPhone2;
	private String otherName3;
	private String otherPhone3;
	private String otherName4;
	private String otherPhone4;
	private String otherName5;
	private String otherPhone5;
	private Date lastCallTime;
	private Date nextCallTime;
	private Integer numberOfDay;
	private Integer attemptBusy;
	private Integer attemptNoans;
	private Integer attempUnreachable;
	private Integer attempOther1;
	private Integer attempOther2;
	private Integer attempOther3;
	private String clickId;
	private String affiliateId;
	private String subid1;
	private String subid2;
	private String subid3;
	private String subid4;
	private String subid5;
	private String networkid;
	private String pid;
	private String trackingUrlId;
	private String offerId;
	private String agcOfferId;
	private String terms;
	private String price;
	private String unit;
	private Integer customerAge;
	private String customerEmail;
	private String customerComment;
	private String internalComment;
	private String carrierComment;
	private String clType;
	private String clTypeName;
	private String neighborhood; //
	private String postalCode; //
	private Integer trackerId; //
	private String agentNote; //
	private Date firstCallTime; //
	private Integer firstCallBy; //
	private Integer firstCallStatus; //
	private String firstCallReason; //
	private String firstCallComment; //
	private Date fcrTime; //
	private Integer fcrBy; //
	private Integer fcrStatus; //
	private String fcrReason; //
	private String fcrComment; //
	private Integer custAge;
	private Integer custGender;
	private String custDob;
	private String custJob;
	private String custOtherSymptom;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String appointmentDate;

	public static List<CLFresh> mapCallbackToFresh(List<CLCallback> callbacks) {
		List<CLFresh> list = new ArrayList<>();
		if (!CollectionUtils.isEmpty(callbacks)) {
			for (CLCallback callback : callbacks) {
				CLFresh fresh = new CLFresh();
				fresh.setLeadId(callback.getLeadId());
				fresh.setAgcCode(callback.getAgcCode());
				fresh.setOrgId(callback.getOrgId());
				fresh.setCcCode(callback.getCcCode());
				fresh.setName(callback.getName());
				fresh.setCampaignName(callback.getCampaignName());
				fresh.setCallinglistName(callback.getCallinglistName());
				fresh.setCallinglistId(callback.getCallinglistId());
				fresh.setAgcId(callback.getAgcId());
				fresh.setPhone(callback.getPhone());
				fresh.setProdId(callback.getProdId());
				fresh.setProdName(callback.getProdName());
				fresh.setAssigned(Integer.parseInt(callback.getAssigned()));
				fresh.setAssignedName(callback.getAssignedName());
				fresh.setCalledby(callback.getCalledby());
				fresh.setCalledbyName(callback.getCalledbyName());
				fresh.setAddress(callback.getAddress());
				fresh.setProvince(callback.getProvince());
				fresh.setDistrict(callback.getDistrict());
				fresh.setSubdistrict(callback.getSubdistrict());
				fresh.setComment(callback.getComment());
				fresh.setLastCallStatus(callback.getLastCallStatus());
				fresh.setLastcallStatusName(callback.getLastcallStatusName());
				fresh.setDayCall(callback.getDayCall());
				fresh.setTotalCall(callback.getTotalCall());
				fresh.setAmount(callback.getAmount());
				fresh.setLeadStatus(callback.getLeadStatus());
				fresh.setLeadStatusName(callback.getLeadStatusName());
				fresh.setResult(callback.getResult());
				fresh.setUserDefin01(callback.getUserDefin01());
				fresh.setUserDefin02(callback.getUserDefin02());
				fresh.setUserDefin03(callback.getUserDefin03());
				fresh.setUserDefin04(callback.getUserDefin04());
				fresh.setUserDefin05(callback.getUserDefin05());
				fresh.setAttribute(callback.getAttribute());
				fresh.setCreatedate(callback.getCreatedate());
				fresh.setModifydate(callback.getModifydate());
				fresh.setModifyby(callback.getModifyby());
				fresh.setLeadType(callback.getLeadType());
				fresh.setAgcLeadAddress(callback.getAgcLeadAddress());
				fresh.setCpId(callback.getCpId());
				fresh.setOtherName1(callback.getOtherName1());
				fresh.setOtherPhone1(callback.getOtherPhone1());
				fresh.setOtherName2(callback.getOtherName2());
				fresh.setOtherPhone2(callback.getOtherPhone2());
				fresh.setOtherName3(callback.getOtherName3());
				fresh.setOtherPhone3(callback.getOtherPhone3());
				fresh.setOtherName4(callback.getOtherName4());
				fresh.setOtherPhone4(callback.getOtherPhone4());
				fresh.setOtherName5(callback.getOtherName5());
				fresh.setOtherPhone5(callback.getOtherPhone5());
				fresh.setLastCallTime(callback.getLastCallTime());
				fresh.setNextCallTime(callback.getNextCallTime());
				fresh.setNumberOfDay(callback.getNumberOfDay());
				fresh.setAttemptBusy(callback.getAttemptBusy());
				fresh.setAttemptNoans(callback.getAttemptNoans());
				fresh.setAttempUnreachable(callback.getAttempUnreachable());
				fresh.setAttempOther1(callback.getAttempOther1());
				fresh.setAttempOther2(callback.getAttempOther2());
				fresh.setAttempOther3(callback.getAttempOther3());
				fresh.setClickId(callback.getClickId());
				fresh.setAffiliateId(callback.getAffiliateId());
				fresh.setSubid1(callback.getSubid1());
				fresh.setSubid2(callback.getSubid2());
				fresh.setSubid3(callback.getSubid3());
				fresh.setSubid4(callback.getSubid4());
				fresh.setSubid5(callback.getSubid5());
				fresh.setNetworkid(callback.getNetworkid());
				fresh.setPid(callback.getPid());
				fresh.setTrackingUrlId(callback.getTrackingUrlId());
				fresh.setOfferId(callback.getOfferId());
				fresh.setAgcOfferId(callback.getAgcOfferId());
				fresh.setTerms(callback.getTerms());
				fresh.setPrice(callback.getPrice());
				fresh.setUnit(callback.getUnit());
				fresh.setCustomerAge(callback.getCustomerAge());
				fresh.setCustomerComment(callback.getCustomerComment());
				fresh.setCustomerEmail(callback.getCustomerEmail());
				fresh.setInternalComment(callback.getInternalComment());
				fresh.setCarrierComment(callback.getCarrierComment());
				fresh.setClType(callback.getClType());
				fresh.setClTypeName(callback.getClTypeName());

				list.add(fresh);
			}
		}
		return list;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getClType() {
		return clType;
	}

	public void setClType(String clType) {
		this.clType = clType;
	}

	public String getClTypeName() {
		return clTypeName;
	}

	public void setClTypeName(String clTypeName) {
		this.clTypeName = clTypeName;
	}

	public Integer getCustomerAge() {
		return customerAge;
	}

	public void setCustomerAge(Integer customerAge) {
		this.customerAge = customerAge;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerComment() {
		return customerComment;
	}

	public void setCustomerComment(String customerComment) {
		this.customerComment = customerComment;
	}

	public String getInternalComment() {
		return internalComment;
	}

	public void setInternalComment(String internalComment) {
		this.internalComment = internalComment;
	}

	public String getCarrierComment() {
		return carrierComment;
	}

	public void setCarrierComment(String carrierComment) {
		this.carrierComment = carrierComment;
	}

	public String getAgcOfferId() {
		return agcOfferId;
	}

	public void setAgcOfferId(String agcOfferId) {
		this.agcOfferId = agcOfferId;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getAgcId() {
		return agcId;
	}

	public void setAgcId(Integer agcId) {
		this.agcId = agcId;
	}

	public Integer getLeadId() {
		return leadId;
	}

	public void setLeadId(Integer leadId) {
		this.leadId = leadId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAgcCode() {
		return agcCode;
	}

	public void setAgcCode(String agcCode) {
		this.agcCode = agcCode;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getCcCode() {
		return ccCode;
	}

	public void setCcCode(String ccCode) {
		this.ccCode = ccCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getProdId() {
		return prodId;
	}

	public void setProdId(Integer prodId) {
		this.prodId = prodId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public Integer getAssigned() {
		return assigned;
	}

	public void setAssigned(Integer assigned) {
		this.assigned = assigned;
	}

	public Integer getCalledby() {
		return calledby;
	}

	public void setCalledby(Integer calledby) {
		this.calledby = calledby;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getSubdistrict() {
		return subdistrict;
	}

	public void setSubdistrict(String subdistrict) {
		this.subdistrict = subdistrict;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getLastCallStatus() {
		return lastCallStatus;
	}

	public void setLastCallStatus(Integer lastCallStatus) {
		this.lastCallStatus = lastCallStatus;
	}

	public Integer getDayCall() {
		return dayCall;
	}

	public void setDayCall(Integer dayCall) {
		this.dayCall = dayCall;
	}

	public Integer getTotalCall() {
		return totalCall;
	}

	public void setTotalCall(Integer totalCall) {
		this.totalCall = totalCall;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Integer getLeadStatus() {
		return leadStatus;
	}

	public void setLeadStatus(Integer leadStatus) {
		this.leadStatus = leadStatus;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getUserDefin01() {
		return userDefin01;
	}

	public void setUserDefin01(String userDefin01) {
		this.userDefin01 = userDefin01;
	}

	public String getUserDefin02() {
		return userDefin02;
	}

	public void setUserDefin02(String userDefin02) {
		this.userDefin02 = userDefin02;
	}

	public String getUserDefin03() {
		return userDefin03;
	}

	public void setUserDefin03(String userDefin03) {
		this.userDefin03 = userDefin03;
	}

	public String getUserDefin04() {
		return userDefin04;
	}

	public void setUserDefin04(String userDefin04) {
		this.userDefin04 = userDefin04;
	}

	public String getUserDefin05() {
		return userDefin05;
	}

	public void setUserDefin05(String userDefin05) {
		this.userDefin05 = userDefin05;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public Integer getModifyby() {
		return modifyby;
	}

	public void setModifyby(Integer modifyby) {
		this.modifyby = modifyby;
	}

	public Integer getCpId() {
		return cpId;
	}

	public void setCpId(Integer cpId) {
		this.cpId = cpId;
	}

	public Integer getClGroup() {
		return clGroup;
	}

	public void setClGroup(Integer clGroup) {
		this.clGroup = clGroup;
	}

	public String getCampaignName() {
		return campaignName;
	}

	public void setCampaignName(String campaignName) {
		this.campaignName = campaignName;
	}

	public String getCallinglistName() {
		return callinglistName;
	}

	public void setCallinglistName(String callinglistName) {
		this.callinglistName = callinglistName;
	}

	public String getAssignedName() {
		return assignedName;
	}

	public void setAssignedName(String assignedName) {
		this.assignedName = assignedName;
	}

	public String getCalledbyName() {
		return calledbyName;
	}

	public void setCalledbyName(String calledbyName) {
		this.calledbyName = calledbyName;
	}

	public String getLastcallStatusName() {
		return lastcallStatusName;
	}

	public void setLastcallStatusName(String lastcallStatusName) {
		this.lastcallStatusName = lastcallStatusName;
	}

	public String getLeadStatusName() {
		return leadStatusName;
	}

	public void setLeadStatusName(String leadStatusName) {
		this.leadStatusName = leadStatusName;
	}

	public String getLeadType() {
		return leadType;
	}

	public void setLeadType(String leadType) {
		this.leadType = leadType;
	}

	public String getAgcLeadAddress() {
		return agcLeadAddress;
	}

	public void setAgcLeadAddress(String agcLeadAddress) {
		this.agcLeadAddress = agcLeadAddress;
	}

	public String getOtherName1() {
		return otherName1;
	}

	public void setOtherName1(String otherName1) {
		this.otherName1 = otherName1;
	}

	public String getOtherPhone1() {
		return otherPhone1;
	}

	public void setOtherPhone1(String otherPhone1) {
		this.otherPhone1 = otherPhone1;
	}

	public String getOtherName2() {
		return otherName2;
	}

	public void setOtherName2(String otherName2) {
		this.otherName2 = otherName2;
	}

	public String getOtherPhone2() {
		return otherPhone2;
	}

	public void setOtherPhone2(String otherPhone2) {
		this.otherPhone2 = otherPhone2;
	}

	public String getOtherName3() {
		return otherName3;
	}

	public void setOtherName3(String otherName3) {
		this.otherName3 = otherName3;
	}

	public String getOtherPhone3() {
		return otherPhone3;
	}

	public void setOtherPhone3(String otherPhone3) {
		this.otherPhone3 = otherPhone3;
	}

	public String getOtherName4() {
		return otherName4;
	}

	public void setOtherName4(String otherName4) {
		this.otherName4 = otherName4;
	}

	public String getOtherPhone4() {
		return otherPhone4;
	}

	public void setOtherPhone4(String otherPhone4) {
		this.otherPhone4 = otherPhone4;
	}

	public String getOtherName5() {
		return otherName5;
	}

	public void setOtherName5(String otherName5) {
		this.otherName5 = otherName5;
	}

	public String getOtherPhone5() {
		return otherPhone5;
	}

	public void setOtherPhone5(String otherPhone5) {
		this.otherPhone5 = otherPhone5;
	}

	public Date getLastCallTime() {
		return lastCallTime;
	}

	public void setLastCallTime(Date lastCallTime) {
		this.lastCallTime = lastCallTime;
	}

	public Date getNextCallTime() {
		return nextCallTime;
	}

	public void setNextCallTime(Date nextCallTime) {
		this.nextCallTime = nextCallTime;
	}

	public Integer getNumberOfDay() {
		return numberOfDay;
	}

	public void setNumberOfDay(Integer numberOfDay) {
		this.numberOfDay = numberOfDay;
	}

	public Integer getAttemptBusy() {
		return attemptBusy;
	}

	public void setAttemptBusy(Integer attemptBusy) {
		this.attemptBusy = attemptBusy;
	}

	public Integer getAttemptNoans() {
		return attemptNoans;
	}

	public void setAttemptNoans(Integer attemptNoans) {
		this.attemptNoans = attemptNoans;
	}

	public Integer getAttempUnreachable() {
		return attempUnreachable;
	}

	public void setAttempUnreachable(Integer attempUnreachable) {
		this.attempUnreachable = attempUnreachable;
	}

	public Integer getAttempOther1() {
		return attempOther1;
	}

	public void setAttempOther1(Integer attempOther1) {
		this.attempOther1 = attempOther1;
	}

	public Integer getAttempOther2() {
		return attempOther2;
	}

	public void setAttempOther2(Integer attempOther2) {
		this.attempOther2 = attempOther2;
	}

	public Integer getAttempOther3() {
		return attempOther3;
	}

	public void setAttempOther3(Integer attempOther3) {
		this.attempOther3 = attempOther3;
	}

	public String getCallinglistId() {
		return callinglistId;
	}

	public void setCallinglistId(String callinglistId) {
		this.callinglistId = callinglistId;
	}

	public String getClickId() {
		return clickId;
	}

	public void setClickId(String clickId) {
		this.clickId = clickId;
	}

	public String getAffiliateId() {
		return affiliateId;
	}

	public void setAffiliateId(String affiliateId) {
		this.affiliateId = affiliateId;
	}

	public String getSubid1() {
		return subid1;
	}

	public void setSubid1(String subid1) {
		this.subid1 = subid1;
	}

	public String getSubid2() {
		return subid2;
	}

	public void setSubid2(String subid2) {
		this.subid2 = subid2;
	}

	public String getSubid3() {
		return subid3;
	}

	public void setSubid3(String subid3) {
		this.subid3 = subid3;
	}

	public String getSubid4() {
		return subid4;
	}

	public void setSubid4(String subid4) {
		this.subid4 = subid4;
	}

	public String getSubid5() {
		return subid5;
	}

	public void setSubid5(String subid5) {
		this.subid5 = subid5;
	}

	public String getNetworkid() {
		return networkid;
	}

	public void setNetworkid(String networkid) {
		this.networkid = networkid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getTrackingUrlId() {
		return trackingUrlId;
	}

	public void setTrackingUrlId(String trackingUrlId) {
		this.trackingUrlId = trackingUrlId;
	}

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public Integer getTrackerId() {
		return trackerId;
	}

	public void setTrackerId(Integer trackerId) {
		this.trackerId = trackerId;
	}

	public String getAgentNote() {
		return agentNote;
	}

	public void setAgentNote(String agentNote) {
		this.agentNote = agentNote;
	}

	public Date getFirstCallTime() {
		return firstCallTime;
	}

	public void setFirstCallTime(Date firstCallTime) {
		this.firstCallTime = firstCallTime;
	}

	public Integer getFirstCallBy() {
		return firstCallBy;
	}

	public void setFirstCallBy(Integer firstCallBy) {
		this.firstCallBy = firstCallBy;
	}

	public Integer getFirstCallStatus() {
		return firstCallStatus;
	}

	public void setFirstCallStatus(Integer firstCallStatus) {
		this.firstCallStatus = firstCallStatus;
	}

	public String getFirstCallReason() {
		return firstCallReason;
	}

	public void setFirstCallReason(String firstCallReason) {
		this.firstCallReason = firstCallReason;
	}

	public String getFirstCallComment() {
		return firstCallComment;
	}

	public void setFirstCallComment(String firstCallComment) {
		this.firstCallComment = firstCallComment;
	}

	public Date getFcrTime() {
		return fcrTime;
	}

	public void setFcrTime(Date fcrTime) {
		this.fcrTime = fcrTime;
	}

	public Integer getFcrBy() {
		return fcrBy;
	}

	public void setFcrBy(Integer fcrBy) {
		this.fcrBy = fcrBy;
	}

	public Integer getFcrStatus() {
		return fcrStatus;
	}

	public void setFcrStatus(Integer fcrStatus) {
		this.fcrStatus = fcrStatus;
	}

	public String getFcrReason() {
		return fcrReason;
	}

	public void setFcrReason(String fcrReason) {
		this.fcrReason = fcrReason;
	}

	public String getFcrComment() {
		return fcrComment;
	}

	public void setFcrComment(String fcrComment) {
		this.fcrComment = fcrComment;
	}

	public Integer getCustAge() {
		return custAge;
	}

	public void setCustAge(Integer custAge) {
		this.custAge = custAge;
	}

	public Integer getCustGender() {
		return custGender;
	}

	public void setCustGender(Integer custGender) {
		this.custGender = custGender;
	}

	public String getCustDob() {
		return custDob;
	}

	public void setCustDob(String custDob) {
		this.custDob = custDob;
	}

	public String getCustJob() {
		return custJob;
	}

	public void setCustJob(String custJob) {
		this.custJob = custJob;
	}

	public String getCustOtherSymptom() {
		return custOtherSymptom;
	}

	public void setCustOtherSymptom(String custOtherSymptom) {
		this.custOtherSymptom = custOtherSymptom;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public static int compareWithTotalCall(CLFresh lhs, CLFresh rhs) {
		if (!lhs.getTotalCall().equals(rhs.getTotalCall())) {
			return Long.compare(lhs.getTotalCall(), rhs.getTotalCall());
		} else {
			if (lhs.getCreatedate().after(rhs.getCreatedate())) {
				return -1;
			}
			return 1;
		}
	}

	public static int compareWithStatus(CLFresh lhs, CLFresh rhs) {
		return -Long.compare(lhs.getLeadStatus(), rhs.getLeadStatus());
	}
}
