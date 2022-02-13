package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.enums.PermissionType;
import com.oauthcentralization.app.tmsoauth2.model.enums.RoleType;
import com.oauthcentralization.app.tmsoauth2.model.enums.UsersType;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;

import java.util.List;

public interface CommonsService {

    boolean hasRole(UsersResponse usersResponse, RoleType roleType);

    boolean hasPermission(UsersResponse usersResponse, PermissionType permissionType, List<UsersType> usersTypes);
}
