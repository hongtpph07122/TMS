package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.request.GroupsMemberRequest;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;

public interface GroupsService {

    TMSResponse<?> addMembers(GroupsMemberRequest groupsMemberRequest, MyUserDetailsImpl myUserDetails);
}
