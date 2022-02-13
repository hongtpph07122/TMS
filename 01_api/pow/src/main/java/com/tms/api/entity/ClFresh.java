package com.tms.api.entity;

import io.swagger.models.parameters.SerializableParameter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cl_fresh")
public class ClFresh implements Serializable {

    @Id
    @SequenceGenerator(name="clfresh_generator", sequenceName = "seq_lead_id", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clfresh_generator")
    @Column(name = "lead_id")
    private Integer leadId;
    @Column(name = "agc_id")
    private Integer agcId;
    @Column(name = "agc_code")
    private String agcCode;
    @Column(name = "org_id")
    private Integer orgId;
    @Column(name = "cc_code")
    private String ccCode;
    @Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "prod_id")
    private Integer prodId;
    @Column(name = "prod_name")
    private String prodName;
    @Column(name = "assigned")
    private Integer assigned;
    @Column(name = "calledby")
    private Integer calledby;
    @Column(name = "address")
    private String address;
    @Column(name = "province")
    private String province;
    @Column(name = "district")
    private String district;
    @Column(name = "subdistrict")
    private String subdistrict;
    @Column(name = "comment")
    private String comment;
    @Column(name = "last_call_status")
    private Integer lastCallStatus;
    @Column(name = "day_call")
    private Integer dayCall;
    @Column(name = "total_call")
    private Integer totalCall;
    @Column(name = "amount")
    private String amount;
    @Column(name = "lead_status")
    private Integer leadStatus;
    @Column(name = "result")
    private Integer result;
    @Column(name = "user_defin_01")
    private String userDefin01;
    @Column(name = "user_defin_02")
    private String userDefin02;
    @Column(name = "user_defin_03")
    private String userDefin03;
    @Column(name = "user_defin_04")
    private String userDefin04;
    @Column(name = "user_defin_05")
    private String userDefin05;
    @Type(type = "jsonb")
    @Column(name = "attribute")
    private String attribute;
    @Column(name = "createdate")
    private Date createdate;
    @Column(name = "modifydate")
    private Date modifydate;
    @Column(name = "modifyby")
    private Integer modifyby;
    @Column(name = "cp_id")
    private Integer cpId;
    @Column(name = "callinglist_id")
    private Integer callingListId;
    @Column(name = "lead_type")
    private String leadType;
    @Column(name = "agc_lead_address")
    private String agcLeadAddress;
    @Column(name = "other_name1")
    private String otherName1;
    @Column(name = "other_phone1")
    private String otherPhone1;
    @Column(name = "other_name2")
    private String otherName2;
    @Column(name = "other_phone2")
    private String otherPhone2;
    @Column(name = "other_name3")
    private String otherName3;
    @Column(name = "other_phone3")
    private String otherPhone3;
    @Column(name = "other_name4")
    private String otherName4;
    @Column(name = "other_phone4")
    private String otherPhone4;
    @Column(name = "other_name5")
    private String otherName5;
    @Column(name = "other_phone5")
    private String otherPhone5;
    @Column(name = "last_call_time")
    private Date lastCallTime;
    @Column(name = "next_call_time")
    private Date nextCallTime;
    @Column(name = "number_of_day")
    private Integer numberOfDay;
    @Column(name = "attempt_busy")
    private Integer attemptBusy;
    @Column(name = "attempt_noans")
    private Integer attemptNoans;
    @Column(name = "attemp_unreachable")
    private Integer attempUnreachable;
    @Column(name = "attemp_other1")
    private Integer attempOther1;
    @Column(name = "attemp_other2")
    private Integer attempOther2;
    @Column(name = "attemp_other3")
    private Integer attempOther3;
    @Column(name = "click_id")
    private String clickId;
    @Column(name = "affiliate_id")
    private String affiliateId;
    @Column(name = "subid1")
    private String subid1;
    @Column(name = "subid2")
    private String subid2;
    @Column(name = "subid3")
    private String subid3;
    @Column(name = "subid4")
    private String subid4;
    @Column(name = "subid5")
    private String subid5;
    @Column(name = "networkid")
    private String networkid;
    @Column(name = "pid")
    private String pid;
    @Column(name = "tracking_url_id")
    private String trackingUrlId;
    @Column(name = "offer_id")
    private String offerId;
    @Column(name = "agc_offer_id")
    private String agcOfferId;
    @Column(name = "terms")
    private String terms;
    @Column(name = "price")
    private String price;
    @Column(name = "unit")
    private String unit;
    @Column(name = "customer_age")
    private Integer customerAge;
    @Column(name = "customer_email")
    private String customerEmail;
    @Column(name = "customer_comment")
    private String customerComment;
    @Column(name = "internal_comment")
    private String internalComment;
    @Column(name = "carrier_comment")
    private String carrierComment;
    @Column(name = "cl_group")
    private Integer clGroup;
    @Column(name = "agcoffer_id")
    private String agcofferId;
    @Column(name = "assigned_name")
    private String assignedName;
    @Column(name = "calledby_name")
    private String calledByName;
    @Column(name = "callinglist_name")
    private String callingListName;
    @Column(name = "campaign_name")
    private String campainName;
    @Column(name = "lastcall_status_name")
    private String lastCallStatusName;
    @Column(name = "lead_status_name")
    private String leadStatusName;
    @Column(name = "source")
    private String source;
    @Column(name = "userdefin_03")
    private String userdefin03;
    @Column(name = "neighborhood")
    private String neighborhood;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "tracker_id")
    private Integer trackerId;
    @Column(name = "actual_call")
    private Integer actualCall;
    @Column(name = "appointment_date")
    private Date appointmentDate;

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public Integer getAgcId() {
        return agcId;
    }

    public void setAgcId(Integer agcId) {
        this.agcId = agcId;
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

    public Integer getCallingListId() {
        return callingListId;
    }

    public void setCallingListId(Integer callingListId) {
        this.callingListId = callingListId;
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

    public Integer getClGroup() {
        return clGroup;
    }

    public void setClGroup(Integer clGroup) {
        this.clGroup = clGroup;
    }

    public String getAgcofferId() {
        return agcofferId;
    }

    public void setAgcofferId(String agcofferId) {
        this.agcofferId = agcofferId;
    }

    public String getAssignedName() {
        return assignedName;
    }

    public void setAssignedName(String assignedName) {
        this.assignedName = assignedName;
    }

    public String getCalledByName() {
        return calledByName;
    }

    public void setCalledByName(String calledByName) {
        this.calledByName = calledByName;
    }

    public String getCallingListName() {
        return callingListName;
    }

    public void setCallingListName(String callingListName) {
        this.callingListName = callingListName;
    }

    public String getCampainName() {
        return campainName;
    }

    public void setCampainName(String campainName) {
        this.campainName = campainName;
    }

    public String getLastCallStatusName() {
        return lastCallStatusName;
    }

    public void setLastCallStatusName(String lastCallStatusName) {
        this.lastCallStatusName = lastCallStatusName;
    }

    public String getLeadStatusName() {
        return leadStatusName;
    }

    public void setLeadStatusName(String leadStatusName) {
        this.leadStatusName = leadStatusName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUserdefin03() {
        return userdefin03;
    }

    public void setUserdefin03(String userdefin03) {
        this.userdefin03 = userdefin03;
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

    public Integer getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(Integer trackerId) {
        this.trackerId = trackerId;
    }

    public Integer getActualCall() {
        return actualCall;
    }

    public void setActualCall(Integer actualCall) {
        this.actualCall = actualCall;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
