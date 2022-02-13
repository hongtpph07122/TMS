package com.tms.entity.log;

public class UpdLeadReassignV3 {
    private String leadId;
    private Integer assigned;
    private Integer leadStatus;
    private Integer modifyby;
    private Integer crmActionType;
    private Integer team;
    private Integer teamSupervisor;

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public Integer getAssigned() {
        return assigned;
    }

    public void setAssigned(Integer assigned) {
        this.assigned = assigned;
    }

    public Integer getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(Integer leadStatus) {
        this.leadStatus = leadStatus;
    }

    public Integer getCrmActionType() {
        return crmActionType;
    }

    public void setCrmActionType(Integer crmActionType) {
        this.crmActionType = crmActionType;
    }

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public Integer getTeam() {
        return team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public Integer getTeamSupervisor() {
        return teamSupervisor;
    }

    public void setTeamSupervisor(Integer teamSupervisor) {
        this.teamSupervisor = teamSupervisor;
    }
}
