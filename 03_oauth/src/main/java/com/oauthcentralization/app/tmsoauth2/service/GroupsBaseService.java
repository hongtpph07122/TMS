package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.filters.GroupAssigneesFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupsFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.GroupAssigneesResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.GroupResponse;

import java.util.List;

public interface GroupsBaseService {

    List<GroupResponse> findGroupsBy(GroupsFilter groupsFilter);

    List<GroupAssigneesResponse> findGroupAssigneesBy(GroupAssigneesFilter groupsFilter);

    byte[] exportGroupsMemberToExcel(GroupAssigneesFilter groupAssigneesFilter, String sheetName);
}
