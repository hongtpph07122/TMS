package com.tms.api.dto;

public class TeamConfig {
    private Integer userId;
    private Integer team;
    private Integer teamSupervisor;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
