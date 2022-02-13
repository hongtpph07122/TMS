package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.filters.UsersFilter;
import com.oauthcentralization.app.tmsoauth2.model.request.GroupAttributesRequest;
import com.oauthcentralization.app.tmsoauth2.model.request.TeamAttributesRequest;
import com.oauthcentralization.app.tmsoauth2.model.request.UsersRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface UsersBaseService {

    byte[] exportUserToExcel(UsersFilter usersFilter, String sheetName);

    List<UsersResponse> findUsersBy(UsersFilter usersFilter);

    List<UsersRequest> importUsers(InputStream inputStream, String sheetName);

    TMSResponse<?> excludeGroupMembers(GroupAttributesRequest groupAttributesRequest, MyUserDetailsImpl myUserDetails);

    TMSResponse<?> moveGroupMembers(GroupAttributesRequest groupAttributesRequest, MyUserDetailsImpl myUserDetails);

    TMSResponse<?> excludeTeamMembers(TeamAttributesRequest teamAttributesRequest, MyUserDetailsImpl myUserDetails);

    TMSResponse<?> moveTeamMembers(TeamAttributesRequest teamAttributesRequest, MyUserDetailsImpl myUserDetails);

    TMSResponse<?> importUsers(String sheetName, MultipartFile file, MyUserDetailsImpl myUserDetails);

}
