package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.request.UsersRoleRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersRoleResponse;

import java.util.List;

public interface UsersRoleService {

    List<UsersRoleResponse> snagUsersRoles(Integer userId);

    boolean saveAsPayload(UsersRoleRequest usersRoleRequest);
}
