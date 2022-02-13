package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.request.TeamsMemberRequest;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;

public interface TeamsService {

    TMSResponse<?> addMembers(TeamsMemberRequest teamsMemberRequest, MyUserDetailsImpl myUserDetails);
}
