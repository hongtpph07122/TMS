package com.oauthcentralization.app.tmsoauth2.service;


import com.oauthcentralization.app.tmsoauth2.model.filters.RolesFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.RolesResponse;

import java.util.List;

public interface RolesBaseService {

    List<RolesResponse> findRolesBy(RolesFilter rolesFilter);
}
