package com.oauthcentralization.app.tmsoauth2.model.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oauthcentralization.app.tmsoauth2.model.dto.PageRequestDTO;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamsFilter extends PageRequestDTO {

    private List<Integer> listId;
    private List<Integer> teamTypeId;
    private List<Integer> managersId;
    private String name;
    private boolean active;

    public TeamsFilter() {
        super();
        setActive(true);
    }

    public List<Integer> getListId() {
        return listId;
    }

    public void setListId(List<Integer> listId) {
        this.listId = listId;
    }

    public List<Integer> getTeamTypeId() {
        return teamTypeId;
    }

    public void setTeamTypeId(List<Integer> teamTypeId) {
        this.teamTypeId = teamTypeId;
    }

    public List<Integer> getManagersId() {
        return managersId;
    }

    public void setManagersId(List<Integer> managersId) {
        this.managersId = managersId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
