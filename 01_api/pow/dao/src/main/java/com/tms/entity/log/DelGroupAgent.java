package com.tms.entity.log;

public class DelGroupAgent {

    private Integer grAgId;
    private Integer groupId;
    private Integer agentId;
    private Integer modifyby;
    private String modifydate;
    private Integer agSkillLevel;

    public Integer getGrAgId() {
        return grAgId;
    }

    public void setGrAgId(Integer grAgId) {
        this.grAgId = grAgId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public Integer getAgSkillLevel() {
        return agSkillLevel;
    }

    public void setAgSkillLevel(Integer agSkillLevel) {
        this.agSkillLevel = agSkillLevel;
    }
}
