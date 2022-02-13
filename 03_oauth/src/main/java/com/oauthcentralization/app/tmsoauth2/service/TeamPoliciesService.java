package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.filters.TeamsFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.TeamsResponse;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;

import java.util.List;

public interface TeamPoliciesService {

    List<TeamsResponse> findTeamsBy(TeamsFilter teamsFilter, MyUserDetailsImpl myUserDetails);
}
