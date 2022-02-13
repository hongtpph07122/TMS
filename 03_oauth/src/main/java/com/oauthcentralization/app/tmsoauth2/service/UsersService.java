package com.oauthcentralization.app.tmsoauth2.service;


import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.request.UsersRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;

import java.util.List;

public interface UsersService {

    boolean existsByUsername(String username);

    UsersResponse findOne(String username);

    UsersResponse findOne(Integer userId);

    List<UsersResponse> findUsersActive();

    void updateOneByIsExpired(Integer userId, Integer isExpired);

    void updateOneByPWDIsExpired(Integer userId, String password);

    void updatePWDUpdateTimeIsNull(Integer userId, int days);

    void countUserSignInFailed(Integer userId);

    TMSResponse<?> validateCreateUser(UsersRequest usersRequest, UsersResponse usersResponse);

    TMSResponse<?> saveAsPayloads(UsersRequest usersRequest, UsersResponse usersResponse);

    TMSResponse<?> updateAsPayloads(Integer userId, UsersResponse usersResponse, MyUserDetailsImpl myUserDetails);
}
