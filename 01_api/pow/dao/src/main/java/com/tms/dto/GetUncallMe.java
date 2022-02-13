package com.tms.dto;

public class GetUncallMe {
    private Integer leadId;
    private Integer cpId;
    private String cpName;
    private String callinglistId;
    private String callinglistName;
    private String source;
    private String agcCode;
    private Integer orgId;
    private String ccCode;
    private String name;
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
    private String callStatusName;
    private Integer dayCall;
    private Integer totalCall;
    private String amount;
    private String leadStatus;
    private String leadStatusName;
    private Integer result;
    private String userDefin01;
    private String userDefin02;
    private String userDefin03;
    private String userDefin04;
    private String userDefin05;
    private String leadType;
    private String agcLeadAddress;
    private String createdate;
    private String modifydate;
    private Integer modifyby;
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
    private String lastCallTime;
    private String nextCallTime;
    private Integer numberOfDay;
    private Integer attemptBusy;
    private Integer attemptNoans;
    private Integer attempUnreachable;
    private Integer attempOther1;
    private Integer attempOther2;
    private Integer attempOther3;
    private String clType;
    private String clTypeName;
    private Integer skillLevel;
    private Integer maxAttempt;
    private Integer limit;
    private Integer offset;

    GetUncallMe(Integer leadId, Integer cpId, String cpName, String callinglistId, String callinglistName, String source, String agcCode, Integer orgId, String ccCode, String name, String phone, Integer prodId, String prodName, Integer assigned, String assignedName, Integer calledby, String calledbyName, String address, String province, String district, String subdistrict, String comment, Integer lastCallStatus, String callStatusName, Integer dayCall, Integer totalCall, String amount, String leadStatus, String leadStatusName, Integer result, String userDefin01, String userDefin02, String userDefin03, String userDefin04, String userDefin05, String leadType, String agcLeadAddress, String createdate, String modifydate, Integer modifyby, String otherName1, String otherPhone1, String otherName2, String otherPhone2, String otherName3, String otherPhone3, String otherName4, String otherPhone4, String otherName5, String otherPhone5, String lastCallTime, String nextCallTime, Integer numberOfDay, Integer attemptBusy, Integer attemptNoans, Integer attempUnreachable, Integer attempOther1, Integer attempOther2, Integer attempOther3, String clType, String clTypeName, Integer skillLevel, Integer maxAttempt, Integer limit, Integer offset) {
        this.leadId = leadId;
        this.cpId = cpId;
        this.cpName = cpName;
        this.callinglistId = callinglistId;
        this.callinglistName = callinglistName;
        this.source = source;
        this.agcCode = agcCode;
        this.orgId = orgId;
        this.ccCode = ccCode;
        this.name = name;
        this.phone = phone;
        this.prodId = prodId;
        this.prodName = prodName;
        this.assigned = assigned;
        this.assignedName = assignedName;
        this.calledby = calledby;
        this.calledbyName = calledbyName;
        this.address = address;
        this.province = province;
        this.district = district;
        this.subdistrict = subdistrict;
        this.comment = comment;
        this.lastCallStatus = lastCallStatus;
        this.callStatusName = callStatusName;
        this.dayCall = dayCall;
        this.totalCall = totalCall;
        this.amount = amount;
        this.leadStatus = leadStatus;
        this.leadStatusName = leadStatusName;
        this.result = result;
        this.userDefin01 = userDefin01;
        this.userDefin02 = userDefin02;
        this.userDefin03 = userDefin03;
        this.userDefin04 = userDefin04;
        this.userDefin05 = userDefin05;
        this.leadType = leadType;
        this.agcLeadAddress = agcLeadAddress;
        this.createdate = createdate;
        this.modifydate = modifydate;
        this.modifyby = modifyby;
        this.otherName1 = otherName1;
        this.otherPhone1 = otherPhone1;
        this.otherName2 = otherName2;
        this.otherPhone2 = otherPhone2;
        this.otherName3 = otherName3;
        this.otherPhone3 = otherPhone3;
        this.otherName4 = otherName4;
        this.otherPhone4 = otherPhone4;
        this.otherName5 = otherName5;
        this.otherPhone5 = otherPhone5;
        this.lastCallTime = lastCallTime;
        this.nextCallTime = nextCallTime;
        this.numberOfDay = numberOfDay;
        this.attemptBusy = attemptBusy;
        this.attemptNoans = attemptNoans;
        this.attempUnreachable = attempUnreachable;
        this.attempOther1 = attempOther1;
        this.attempOther2 = attempOther2;
        this.attempOther3 = attempOther3;
        this.clType = clType;
        this.clTypeName = clTypeName;
        this.skillLevel = skillLevel;
        this.maxAttempt = maxAttempt;
        this.limit = limit;
        this.offset = offset;
    }

    public static GetUncallMe.GetUncallMeBuilder builder() {
        return new GetUncallMe.GetUncallMeBuilder();
    }

    public Integer getLeadId() {
        return this.leadId;
    }

    public Integer getCpId() {
        return this.cpId;
    }

    public String getCpName() {
        return this.cpName;
    }

    public String getCallinglistId() {
        return this.callinglistId;
    }

    public String getCallinglistName() {
        return this.callinglistName;
    }

    public String getSource() {
        return this.source;
    }

    public String getAgcCode() {
        return this.agcCode;
    }

    public Integer getOrgId() {
        return this.orgId;
    }

    public String getCcCode() {
        return this.ccCode;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public Integer getProdId() {
        return this.prodId;
    }

    public String getProdName() {
        return this.prodName;
    }

    public Integer getAssigned() {
        return this.assigned;
    }

    public String getAssignedName() {
        return this.assignedName;
    }

    public Integer getCalledby() {
        return this.calledby;
    }

    public String getCalledbyName() {
        return this.calledbyName;
    }

    public String getAddress() {
        return this.address;
    }

    public String getProvince() {
        return this.province;
    }

    public String getDistrict() {
        return this.district;
    }

    public String getSubdistrict() {
        return this.subdistrict;
    }

    public String getComment() {
        return this.comment;
    }

    public Integer getLastCallStatus() {
        return this.lastCallStatus;
    }

    public String getCallStatusName() {
        return this.callStatusName;
    }

    public Integer getDayCall() {
        return this.dayCall;
    }

    public Integer getTotalCall() {
        return this.totalCall;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getLeadStatus() {
        return this.leadStatus;
    }

    public String getLeadStatusName() {
        return this.leadStatusName;
    }

    public Integer getResult() {
        return this.result;
    }

    public String getUserDefin01() {
        return this.userDefin01;
    }

    public String getUserDefin02() {
        return this.userDefin02;
    }

    public String getUserDefin03() {
        return this.userDefin03;
    }

    public String getUserDefin04() {
        return this.userDefin04;
    }

    public String getUserDefin05() {
        return this.userDefin05;
    }

    public String getLeadType() {
        return this.leadType;
    }

    public String getAgcLeadAddress() {
        return this.agcLeadAddress;
    }

    public String getCreatedate() {
        return this.createdate;
    }

    public String getModifydate() {
        return this.modifydate;
    }

    public Integer getModifyby() {
        return this.modifyby;
    }

    public String getOtherName1() {
        return this.otherName1;
    }

    public String getOtherPhone1() {
        return this.otherPhone1;
    }

    public String getOtherName2() {
        return this.otherName2;
    }

    public String getOtherPhone2() {
        return this.otherPhone2;
    }

    public String getOtherName3() {
        return this.otherName3;
    }

    public String getOtherPhone3() {
        return this.otherPhone3;
    }

    public String getOtherName4() {
        return this.otherName4;
    }

    public String getOtherPhone4() {
        return this.otherPhone4;
    }

    public String getOtherName5() {
        return this.otherName5;
    }

    public String getOtherPhone5() {
        return this.otherPhone5;
    }

    public String getLastCallTime() {
        return this.lastCallTime;
    }

    public String getNextCallTime() {
        return this.nextCallTime;
    }

    public Integer getNumberOfDay() {
        return this.numberOfDay;
    }

    public Integer getAttemptBusy() {
        return this.attemptBusy;
    }

    public Integer getAttemptNoans() {
        return this.attemptNoans;
    }

    public Integer getAttempUnreachable() {
        return this.attempUnreachable;
    }

    public Integer getAttempOther1() {
        return this.attempOther1;
    }

    public Integer getAttempOther2() {
        return this.attempOther2;
    }

    public Integer getAttempOther3() {
        return this.attempOther3;
    }

    public String getClType() {
        return this.clType;
    }

    public String getClTypeName() {
        return this.clTypeName;
    }

    public Integer getSkillLevel() {
        return this.skillLevel;
    }

    public Integer getMaxAttempt() {
        return this.maxAttempt;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public static class GetUncallMeBuilder {
        private Integer leadId;
        private Integer cpId;
        private String cpName;
        private String callinglistId;
        private String callinglistName;
        private String source;
        private String agcCode;
        private Integer orgId;
        private String ccCode;
        private String name;
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
        private String callStatusName;
        private Integer dayCall;
        private Integer totalCall;
        private String amount;
        private String leadStatus;
        private String leadStatusName;
        private Integer result;
        private String userDefin01;
        private String userDefin02;
        private String userDefin03;
        private String userDefin04;
        private String userDefin05;
        private String leadType;
        private String agcLeadAddress;
        private String createdate;
        private String modifydate;
        private Integer modifyby;
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
        private String lastCallTime;
        private String nextCallTime;
        private Integer numberOfDay;
        private Integer attemptBusy;
        private Integer attemptNoans;
        private Integer attempUnreachable;
        private Integer attempOther1;
        private Integer attempOther2;
        private Integer attempOther3;
        private String clType;
        private String clTypeName;
        private Integer skillLevel;
        private Integer maxAttempt;
        private Integer limit;
        private Integer offset;

        GetUncallMeBuilder() {
        }

        public GetUncallMe.GetUncallMeBuilder leadId(Integer leadId) {
            this.leadId = leadId;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder cpId(Integer cpId) {
            this.cpId = cpId;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder cpName(String cpName) {
            this.cpName = cpName;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder callinglistId(String callinglistId) {
            this.callinglistId = callinglistId;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder callinglistName(String callinglistName) {
            this.callinglistName = callinglistName;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder source(String source) {
            this.source = source;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder agcCode(String agcCode) {
            this.agcCode = agcCode;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder orgId(Integer orgId) {
            this.orgId = orgId;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder ccCode(String ccCode) {
            this.ccCode = ccCode;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder name(String name) {
            this.name = name;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder prodId(Integer prodId) {
            this.prodId = prodId;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder prodName(String prodName) {
            this.prodName = prodName;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder assigned(Integer assigned) {
            this.assigned = assigned;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder assignedName(String assignedName) {
            this.assignedName = assignedName;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder calledby(Integer calledby) {
            this.calledby = calledby;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder calledbyName(String calledbyName) {
            this.calledbyName = calledbyName;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder address(String address) {
            this.address = address;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder province(String province) {
            this.province = province;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder district(String district) {
            this.district = district;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder subdistrict(String subdistrict) {
            this.subdistrict = subdistrict;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder lastCallStatus(Integer lastCallStatus) {
            this.lastCallStatus = lastCallStatus;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder callStatusName(String callStatusName) {
            this.callStatusName = callStatusName;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder dayCall(Integer dayCall) {
            this.dayCall = dayCall;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder totalCall(Integer totalCall) {
            this.totalCall = totalCall;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder amount(String amount) {
            this.amount = amount;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder leadStatus(String leadStatus) {
            this.leadStatus = leadStatus;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder leadStatusName(String leadStatusName) {
            this.leadStatusName = leadStatusName;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder result(Integer result) {
            this.result = result;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder userDefin01(String userDefin01) {
            this.userDefin01 = userDefin01;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder userDefin02(String userDefin02) {
            this.userDefin02 = userDefin02;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder userDefin03(String userDefin03) {
            this.userDefin03 = userDefin03;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder userDefin04(String userDefin04) {
            this.userDefin04 = userDefin04;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder userDefin05(String userDefin05) {
            this.userDefin05 = userDefin05;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder leadType(String leadType) {
            this.leadType = leadType;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder agcLeadAddress(String agcLeadAddress) {
            this.agcLeadAddress = agcLeadAddress;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder createdate(String createdate) {
            this.createdate = createdate;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder modifydate(String modifydate) {
            this.modifydate = modifydate;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder modifyby(Integer modifyby) {
            this.modifyby = modifyby;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherName1(String otherName1) {
            this.otherName1 = otherName1;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherPhone1(String otherPhone1) {
            this.otherPhone1 = otherPhone1;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherName2(String otherName2) {
            this.otherName2 = otherName2;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherPhone2(String otherPhone2) {
            this.otherPhone2 = otherPhone2;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherName3(String otherName3) {
            this.otherName3 = otherName3;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherPhone3(String otherPhone3) {
            this.otherPhone3 = otherPhone3;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherName4(String otherName4) {
            this.otherName4 = otherName4;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherPhone4(String otherPhone4) {
            this.otherPhone4 = otherPhone4;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherName5(String otherName5) {
            this.otherName5 = otherName5;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder otherPhone5(String otherPhone5) {
            this.otherPhone5 = otherPhone5;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder lastCallTime(String lastCallTime) {
            this.lastCallTime = lastCallTime;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder nextCallTime(String nextCallTime) {
            this.nextCallTime = nextCallTime;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder numberOfDay(Integer numberOfDay) {
            this.numberOfDay = numberOfDay;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder attemptBusy(Integer attemptBusy) {
            this.attemptBusy = attemptBusy;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder attemptNoans(Integer attemptNoans) {
            this.attemptNoans = attemptNoans;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder attempUnreachable(Integer attempUnreachable) {
            this.attempUnreachable = attempUnreachable;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder attempOther1(Integer attempOther1) {
            this.attempOther1 = attempOther1;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder attempOther2(Integer attempOther2) {
            this.attempOther2 = attempOther2;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder attempOther3(Integer attempOther3) {
            this.attempOther3 = attempOther3;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder clType(String clType) {
            this.clType = clType;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder clTypeName(String clTypeName) {
            this.clTypeName = clTypeName;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder skillLevel(Integer skillLevel) {
            this.skillLevel = skillLevel;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder maxAttempt(Integer maxAttempt) {
            this.maxAttempt = maxAttempt;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder limit(Integer limit) {
            this.limit = limit;
            return this;
        }

        public GetUncallMe.GetUncallMeBuilder offset(Integer offset) {
            this.offset = offset;
            return this;
        }

        public GetUncallMe build() {
            return new GetUncallMe(this.leadId, this.cpId, this.cpName, this.callinglistId, this.callinglistName, this.source, this.agcCode, this.orgId, this.ccCode, this.name, this.phone, this.prodId, this.prodName, this.assigned, this.assignedName, this.calledby, this.calledbyName, this.address, this.province, this.district, this.subdistrict, this.comment, this.lastCallStatus, this.callStatusName, this.dayCall, this.totalCall, this.amount, this.leadStatus, this.leadStatusName, this.result, this.userDefin01, this.userDefin02, this.userDefin03, this.userDefin04, this.userDefin05, this.leadType, this.agcLeadAddress, this.createdate, this.modifydate, this.modifyby, this.otherName1, this.otherPhone1, this.otherName2, this.otherPhone2, this.otherName3, this.otherPhone3, this.otherName4, this.otherPhone4, this.otherName5, this.otherPhone5, this.lastCallTime, this.nextCallTime, this.numberOfDay, this.attemptBusy, this.attemptNoans, this.attempUnreachable, this.attempOther1, this.attempOther2, this.attempOther3, this.clType, this.clTypeName, this.skillLevel, this.maxAttempt, this.limit, this.offset);
        }

        public String toString() {
            return "GetUncallMe.GetUncallMeBuilder(leadId=" + this.leadId + ", cpId=" + this.cpId + ", cpName=" + this.cpName + ", callinglistId=" + this.callinglistId + ", callinglistName=" + this.callinglistName + ", source=" + this.source + ", agcCode=" + this.agcCode + ", orgId=" + this.orgId + ", ccCode=" + this.ccCode + ", name=" + this.name + ", phone=" + this.phone + ", prodId=" + this.prodId + ", prodName=" + this.prodName + ", assigned=" + this.assigned + ", assignedName=" + this.assignedName + ", calledby=" + this.calledby + ", calledbyName=" + this.calledbyName + ", address=" + this.address + ", province=" + this.province + ", district=" + this.district + ", subdistrict=" + this.subdistrict + ", comment=" + this.comment + ", lastCallStatus=" + this.lastCallStatus + ", callStatusName=" + this.callStatusName + ", dayCall=" + this.dayCall + ", totalCall=" + this.totalCall + ", amount=" + this.amount + ", leadStatus=" + this.leadStatus + ", leadStatusName=" + this.leadStatusName + ", result=" + this.result + ", userDefin01=" + this.userDefin01 + ", userDefin02=" + this.userDefin02 + ", userDefin03=" + this.userDefin03 + ", userDefin04=" + this.userDefin04 + ", userDefin05=" + this.userDefin05 + ", leadType=" + this.leadType + ", agcLeadAddress=" + this.agcLeadAddress + ", createdate=" + this.createdate + ", modifydate=" + this.modifydate + ", modifyby=" + this.modifyby + ", otherName1=" + this.otherName1 + ", otherPhone1=" + this.otherPhone1 + ", otherName2=" + this.otherName2 + ", otherPhone2=" + this.otherPhone2 + ", otherName3=" + this.otherName3 + ", otherPhone3=" + this.otherPhone3 + ", otherName4=" + this.otherName4 + ", otherPhone4=" + this.otherPhone4 + ", otherName5=" + this.otherName5 + ", otherPhone5=" + this.otherPhone5 + ", lastCallTime=" + this.lastCallTime + ", nextCallTime=" + this.nextCallTime + ", numberOfDay=" + this.numberOfDay + ", attemptBusy=" + this.attemptBusy + ", attemptNoans=" + this.attemptNoans + ", attempUnreachable=" + this.attempUnreachable + ", attempOther1=" + this.attempOther1 + ", attempOther2=" + this.attempOther2 + ", attempOther3=" + this.attempOther3 + ", clType=" + this.clType + ", clTypeName=" + this.clTypeName + ", skillLevel=" + this.skillLevel + ", maxAttempt=" + this.maxAttempt + ", limit=" + this.limit + ", offset=" + this.offset + ")";
        }
    }
}
