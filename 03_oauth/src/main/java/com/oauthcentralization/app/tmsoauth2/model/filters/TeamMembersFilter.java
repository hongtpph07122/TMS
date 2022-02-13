package com.oauthcentralization.app.tmsoauth2.model.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamMembersFilter extends TeamsFilter {

    private List<Integer> teamsId;
    private List<Integer> usersId;

    public TeamMembersFilter() {
        super();
    }

    public List<Integer> getTeamsId() {
        return teamsId;
    }

    public void setTeamsId(List<Integer> teamsId) {
        this.teamsId = teamsId;
    }

    public List<Integer> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<Integer> usersId) {
        this.usersId = usersId;
    }
}
