package com.oauthcentralization.app.tmsoauth2.model.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oauthcentralization.app.tmsoauth2.model.enums.UsersType;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupAssigneesFilter extends GroupsFilter {

    private List<Integer> groupsId;
    private List<Integer> assigneesId;
    private List<Integer> groupsAssigneesSkillsLevel;
    private List<UsersType> multipleUserType;

    public GroupAssigneesFilter() {
        super();
        setGroupsId(Collections.emptyList());
        setAssigneesId(Collections.emptyList());
        setGroupsAssigneesSkillsLevel(Collections.emptyList());
        setMultipleUserType(Collections.emptyList());
    }

    public List<Integer> getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(List<Integer> groupsId) {
        this.groupsId = groupsId;
    }

    public List<Integer> getAssigneesId() {
        return assigneesId;
    }

    public void setAssigneesId(List<Integer> assigneesId) {
        this.assigneesId = assigneesId;
    }

    public List<Integer> getGroupsAssigneesSkillsLevel() {
        return groupsAssigneesSkillsLevel;
    }

    public void setGroupsAssigneesSkillsLevel(List<Integer> groupsAssigneesSkillsLevel) {
        this.groupsAssigneesSkillsLevel = groupsAssigneesSkillsLevel;
    }

    public List<UsersType> getMultipleUserType() {
        return multipleUserType;
    }

    public void setMultipleUserType(List<UsersType> multipleUserType) {
        this.multipleUserType = multipleUserType;
    }
}
