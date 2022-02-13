package com.tms.dto;

public class GetCpCallListSkillResp {

    private Integer orgId;
    private Integer cpId;
    private Integer userId;
    private String userName;
    private String callinglist;
    private String agSkillLevel;
    
    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
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

    public String getCallinglist() {
        return callinglist;
    }

    public void setCallinglist(String callinglist) {
        this.callinglist = callinglist;
    }

	public String getAgSkillLevel() {
		return agSkillLevel;
	}

	public void setAgSkillLevel(String agSkillLevel) {
		this.agSkillLevel = agSkillLevel;
	}


}
