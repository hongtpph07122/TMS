package com.tms.dto;

public class GetGroupAgentV2 {

    private Integer orgId;
    private Integer grAgId;
    private Integer groupId;
    private String name;
    private String shortName;
    private Integer groupType;
    private String groupTypeName;
    private Integer userId ;
    private String userName;
    private String fullname;
    private Integer agSkillLevel;
    private Integer limit;
    private Integer offset;

    public Integer getGrAgId() {
        return grAgId;
    }

    public void setGrAgId(Integer grAgId) {
        this.grAgId = grAgId;
    }

    public Integer getAgSkillLevel() {
        return agSkillLevel;
    }

    public void setAgSkillLevel(Integer agSkillLevel) {
        this.agSkillLevel = agSkillLevel;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public String getGroupTypeName() {
        return groupTypeName;
    }

    public void setGroupTypeName(String groupTypeName) {
        this.groupTypeName = groupTypeName;
    }
}
