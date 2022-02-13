package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.filters.GroupsFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.GroupResponse;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;

import java.util.List;

public interface GroupPoliciesService {

    List<GroupResponse> findGroupsBy(GroupsFilter groupsFilter, MyUserDetailsImpl myUserDetails);
}
