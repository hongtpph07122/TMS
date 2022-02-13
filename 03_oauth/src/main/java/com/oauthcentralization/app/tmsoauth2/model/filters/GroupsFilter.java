package com.oauthcentralization.app.tmsoauth2.model.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oauthcentralization.app.tmsoauth2.model.dto.PageRequestDTO;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupsFilter extends PageRequestDTO {

    private List<Integer> listId;
    private List<Integer> skillsId;
    private String name;
    private String shortName;

    public GroupsFilter() {
        super();
    }

    public List<Integer> getListId() {
        return listId;
    }

    public void setListId(List<Integer> listId) {
        this.listId = listId;
    }

    public List<Integer> getSkillsId() {
        return skillsId;
    }

    public void setSkillsId(List<Integer> skillsId) {
        this.skillsId = skillsId;
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

}
