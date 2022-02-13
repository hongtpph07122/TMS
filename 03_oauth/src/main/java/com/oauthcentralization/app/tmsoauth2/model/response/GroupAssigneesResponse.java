package com.oauthcentralization.app.tmsoauth2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupAssigneesResponse extends BasesResponse {

    private Integer groupAssigneeId;
    private GroupResponse group;
    private UsersResponse user;
    private Integer assigneeSkillLevel;
    private int userTypeId;


    public GroupAssigneesResponse() {
    }

    public Integer getGroupAssigneeId() {
        return groupAssigneeId;
    }

    public void setGroupAssigneeId(Integer groupAssigneeId) {
        this.groupAssigneeId = groupAssigneeId;
    }

    public GroupResponse getGroup() {
        return group;
    }

    public void setGroup(GroupResponse group) {
        this.group = group;
    }

    public UsersResponse getUser() {
        return user;
    }

    public void setUser(UsersResponse user) {
        this.user = user;
    }

    public Integer getAssigneeSkillLevel() {
        return assigneeSkillLevel;
    }

    public void setAssigneeSkillLevel(Integer assigneeSkillLevel) {
        this.assigneeSkillLevel = assigneeSkillLevel;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }
}
