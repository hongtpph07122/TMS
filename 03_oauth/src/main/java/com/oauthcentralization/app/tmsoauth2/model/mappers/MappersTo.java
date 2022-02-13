package com.oauthcentralization.app.tmsoauth2.model.mappers;

import com.oauthcentralization.app.tmsoauth2.model.request.UsersRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.*;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utils.DateUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MappersTo {

    public static UsersExcelResponse convertToExcel(UsersResponse usersResponse) {
        UsersExcelResponse usersExcelResponse = new UsersExcelResponse();
        usersExcelResponse.setFullName(usersResponse.getFullName());
        usersExcelResponse.setUsername(usersResponse.getUsername());
        usersExcelResponse.setPhone(usersResponse.getPhone());
        usersExcelResponse.setGroups(usersResponse.getGroupsName());
        usersExcelResponse.setTeams(usersResponse.getTeamsName());

        if (ObjectUtils.allNotNull(usersResponse.getPasswordUpdateTime())) {
            usersExcelResponse.setPasswordExpiry(DateUtils.snagPatternStage("dd/MM/yyyy", usersResponse.getPasswordUpdateTime()));
        }
        return usersExcelResponse;
    }

    public static List<UsersExcelResponse> convertToExcel(List<UsersResponse> usersResponses) {
        if (CollectionsUtility.isEmpty(usersResponses)) {
            return Collections.emptyList();
        }

        return usersResponses.stream().map(MappersTo::convertToExcel).collect(Collectors.toList());
    }

    public static GroupsMemberExcelResponse convertToExcelGroupsMember(GroupAssigneesResponse groupAssigneesResponse) {
        GroupsMemberExcelResponse groupsMemberExcelResponse = new GroupsMemberExcelResponse();
        groupsMemberExcelResponse.setSkillLevel(groupAssigneesResponse.getAssigneeSkillLevel());

        if (ObjectUtils.allNotNull(groupAssigneesResponse.getUser())) {
            groupsMemberExcelResponse.setFullname(groupAssigneesResponse.getUser().getFullName());
            groupsMemberExcelResponse.setUsername(groupAssigneesResponse.getUser().getUsername());
        }

        if (ObjectUtils.allNotNull(groupAssigneesResponse.getGroup())) {
            groupsMemberExcelResponse.setGroup(groupAssigneesResponse.getGroup().getName());
        }

        return groupsMemberExcelResponse;
    }

    public static List<GroupsMemberExcelResponse> convertToExcelGroupsMember(List<GroupAssigneesResponse> groupAssigneesResponses) {
        if (CollectionsUtility.isEmpty(groupAssigneesResponses)) {
            return Collections.emptyList();
        }

        return groupAssigneesResponses.stream().map(MappersTo::convertToExcelGroupsMember).collect(Collectors.toList());
    }

    public static TeamsMemberExcelResponse convertToExcelTeamsMember(TeamsResponse teamsResponse) {
        TeamsMemberExcelResponse teamsMemberExcelResponse = new TeamsMemberExcelResponse();
        teamsMemberExcelResponse.setTeam(teamsResponse.getName());

        if (ObjectUtils.allNotNull(teamsResponse.getUser())) {
            teamsMemberExcelResponse.setFullname(teamsResponse.getUser().getFullName());
            teamsMemberExcelResponse.setUsername(teamsResponse.getUser().getUsername());
        }

        return teamsMemberExcelResponse;
    }

    public static List<TeamsMemberExcelResponse> convertToExcelTeamsMember(List<TeamsResponse> teamsResponses) {
        if (CollectionsUtility.isEmpty(teamsResponses)) {
            return Collections.emptyList();
        }

        return teamsResponses.stream().map(MappersTo::convertToExcelTeamsMember).collect(Collectors.toList());
    }

    public static UsersRawExcelResponse convertToRawUsers(UsersRequest usersRequest) {
        UsersRawExcelResponse usersRawExcelResponse = new UsersRawExcelResponse();
        usersRawExcelResponse.setUsername(usersRequest.getUsername());
        usersRawExcelResponse.setUsernameDomain(usersRequest.getUsernameDomain());
        usersRawExcelResponse.setFullName(usersRequest.getFullName());
        usersRawExcelResponse.setEmail(usersRequest.getEmail());
        usersRawExcelResponse.setPhone(usersRequest.getPhone());
        usersRawExcelResponse.setPassword(usersRequest.getPassword());
        usersRawExcelResponse.setErrorsReason(usersRequest.getErrorsReason());
        if (ObjectUtils.allNotNull(usersRequest.getUserType())) {
            usersRawExcelResponse.setUserType(usersRequest.getUserType().getName());
        }
        return usersRawExcelResponse;
    }

    public static List<UsersRawExcelResponse> convertToRawUsers(List<UsersRequest> usersRequests) {
        if (CollectionsUtility.isEmpty(usersRequests)) {
            return Collections.emptyList();
        }
        return usersRequests.stream().map(MappersTo::convertToRawUsers).collect(Collectors.toList());
    }

}
