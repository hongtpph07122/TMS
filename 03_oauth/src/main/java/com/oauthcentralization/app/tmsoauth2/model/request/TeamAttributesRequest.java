package com.oauthcentralization.app.tmsoauth2.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamAttributesRequest {

    private Integer teamMemberId; // id
    private Integer teamId;
    private Integer userId;

    public TeamAttributesRequest() {
        setTeamId(null);
        setTeamMemberId(null);
        setUserId(null);
    }

    public Integer getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(Integer teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
