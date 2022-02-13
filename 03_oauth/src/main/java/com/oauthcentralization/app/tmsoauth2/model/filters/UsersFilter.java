package com.oauthcentralization.app.tmsoauth2.model.filters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oauthcentralization.app.tmsoauth2.model.dto.PageRequestDTO;
import com.oauthcentralization.app.tmsoauth2.model.enums.UsersType;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersFilter extends PageRequestDTO {

    private List<Integer> usersId;
    private List<Integer> usersGroupsId;
    private UsersType userType;
    private String username;
    private String fullname;
    private String email;
    private String phone;
    private Integer active;
    private Boolean expired;
    @JsonIgnore
    private boolean viewParentsGroups;
    @JsonIgnore
    private boolean viewParentsTeams;
    @JsonIgnore
    private int sizeOfGroups;
    @JsonIgnore
    private int sizeOfTeams;
    private List<UsersType> multipleUserType;
    private List<String> multipleUsername;
    private List<String> multipleTeam;
    private List<String> multipleGroup;

    public UsersFilter() {
        super();
        setExpired(null);
        setViewParentsGroups(false);
        setViewParentsTeams(false);
        setSizeOfGroups(2);
        setSizeOfTeams(2);
        setUserType(null);
        setMultipleUserType(Collections.emptyList());
        setMultipleUsername(Collections.emptyList());
        setMultipleTeam(Collections.emptyList());
        setMultipleGroup(Collections.emptyList());
    }

    public List<Integer> getUsersId() {
        return usersId;
    }

    public void setUsersId(List<Integer> usersId) {
        this.usersId = usersId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public UsersType getUserType() {
        return userType;
    }

    public void setUserType(UsersType userType) {
        this.userType = userType;
    }

    public Boolean isExpired() {
        return expired;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public boolean isViewParentsGroups() {
        return viewParentsGroups;
    }

    public void setViewParentsGroups(boolean viewParentsGroups) {
        this.viewParentsGroups = viewParentsGroups;
    }

    public boolean isViewParentsTeams() {
        return viewParentsTeams;
    }

    public void setViewParentsTeams(boolean viewParentsTeams) {
        this.viewParentsTeams = viewParentsTeams;
    }

    public int getSizeOfGroups() {
        return sizeOfGroups;
    }

    public void setSizeOfGroups(int sizeOfGroups) {
        this.sizeOfGroups = sizeOfGroups;
    }

    public int getSizeOfTeams() {
        return sizeOfTeams;
    }

    public void setSizeOfTeams(int sizeOfTeams) {
        this.sizeOfTeams = sizeOfTeams;
    }

    public List<UsersType> getMultipleUserType() {
        return multipleUserType;
    }

    public void setMultipleUserType(List<UsersType> multipleUserType) {
        this.multipleUserType = multipleUserType;
    }

    public List<String> getMultipleUsername() {
        return multipleUsername;
    }

    public void setMultipleUsername(List<String> multipleUsername) {
        this.multipleUsername = multipleUsername;
    }

    public List<String> getMultipleTeam() {
        return multipleTeam;
    }

    public void setMultipleTeam(List<String> multipleTeam) {
        this.multipleTeam = multipleTeam;
    }

    public List<String> getMultipleGroup() {
        return multipleGroup;
    }

    public void setMultipleGroup(List<String> multipleGroup) {
        this.multipleGroup = multipleGroup;
    }

    public List<Integer> getUsersGroupsId() {
        return usersGroupsId;
    }

    public void setUsersGroupsId(List<Integer> usersGroupsId) {
        this.usersGroupsId = usersGroupsId;
    }
}
