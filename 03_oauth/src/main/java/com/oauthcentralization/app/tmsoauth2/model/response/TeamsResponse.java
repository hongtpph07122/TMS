package com.oauthcentralization.app.tmsoauth2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamsResponse {

    private Integer id;
    private Integer teamMemberId;
    private String name;
    private Boolean active;
    private UsersResponse user;
    private SynonymsResponse teamType;
    private Integer teamTypeId;


    public TeamsResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UsersResponse getUser() {
        return user;
    }

    public void setUser(UsersResponse user) {
        this.user = user;
    }

    public SynonymsResponse getTeamType() {
        return teamType;
    }

    public void setTeamType(SynonymsResponse teamType) {
        this.teamType = teamType;
    }

    public Integer getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(Integer teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public Integer getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(Integer teamMemberId) {
        this.teamMemberId = teamMemberId;
    }
}
