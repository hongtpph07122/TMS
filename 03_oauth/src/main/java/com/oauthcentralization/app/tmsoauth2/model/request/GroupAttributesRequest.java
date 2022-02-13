package com.oauthcentralization.app.tmsoauth2.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupAttributesRequest {

    private Integer groupId; // group to
    private Integer userId;
    private Integer groupAssigneeId;

    public GroupAttributesRequest() {
        setUserId(null);
        setGroupId(null);
        setGroupAssigneeId(null);
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGroupAssigneeId() {
        return groupAssigneeId;
    }

    public void setGroupAssigneeId(Integer groupAssigneeId) {
        this.groupAssigneeId = groupAssigneeId;
    }
}
