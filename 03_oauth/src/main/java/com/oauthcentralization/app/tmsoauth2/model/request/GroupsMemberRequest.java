package com.oauthcentralization.app.tmsoauth2.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupsMemberRequest {

    private List<Integer> usersId;
    private Integer groupId;
    private List<Integer> assigneesSkillLevel;

    public GroupsMemberRequest() {
    }

    public List<Integer> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<Integer> usersId) {
        this.usersId = usersId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public List<Integer> getAssigneesSkillLevel() {
        return assigneesSkillLevel;
    }

    public void setAssigneesSkillLevel(List<Integer> assigneesSkillLevel) {
        this.assigneesSkillLevel = assigneesSkillLevel;
    }
}
