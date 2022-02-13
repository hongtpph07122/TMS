package com.tms.model.Response;

public class ProvinceWithPartnerResponseDTO {

    private Integer prvId;
    private Integer groupId;
    private String name;
    private String shortname;
    private String code;
    private Integer partnerId;
    private String pnCode;
    private String pnName;
    private Integer lastMileId;
    private String lastMileName;

    public Integer getPrvId() {
        return prvId;
    }

    public void setPrvId(Integer prvId) {
        this.prvId = prvId;
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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getPnCode() {
        return pnCode;
    }

    public void setPnCode(String pnCode) {
        this.pnCode = pnCode;
    }

    public String getPnName() {
        return pnName;
    }

    public void setPnName(String pnName) {
        this.pnName = pnName;
    }

    public Integer getLastMileId() {
        return lastMileId;
    }

    public void setLastMileId(Integer lastMileId) {
        this.lastMileId = lastMileId;
    }

    public String getLastMileName() {
        return lastMileName;
    }

    public void setLastMileName(String lastMileName) {
        this.lastMileName = lastMileName;
    }
}
