package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.filters.UsersFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersTypeResponse;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;

import java.util.List;

public interface UsersPoliciesService {

    List<UsersResponse> findUsersByPolicies(UsersFilter usersFilter, MyUserDetailsImpl myUserDetails);

    List<UsersResponse> filterUsersByPolicies(UsersFilter usersFilter, MyUserDetailsImpl myUserDetails);

    List<UsersTypeResponse> findAllUsersType(MyUserDetailsImpl myUserDetails);

    byte[] exportUserToExcel(UsersFilter usersFilter, MyUserDetailsImpl myUserDetails, String sheetName);
}
