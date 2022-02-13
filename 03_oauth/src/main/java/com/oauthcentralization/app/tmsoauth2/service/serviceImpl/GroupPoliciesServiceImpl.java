package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.enums.RoleType;
import com.oauthcentralization.app.tmsoauth2.model.enums.UsersType;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupAssigneesFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupsFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.GroupAssigneesResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.GroupResponse;
import com.oauthcentralization.app.tmsoauth2.service.CommonsService;
import com.oauthcentralization.app.tmsoauth2.service.GroupPoliciesService;
import com.oauthcentralization.app.tmsoauth2.service.GroupsBaseService;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal"})
@Service(value = "groupPoliciesService")
@Transactional
public class GroupPoliciesServiceImpl implements GroupPoliciesService {

    private static final Logger logger = LoggerFactory.getLogger(GroupPoliciesServiceImpl.class);

    private final GroupsBaseService groupsBaseService;
    private final CommonsService commonsService;

    @Autowired
    public GroupPoliciesServiceImpl(
            GroupsBaseService groupsBaseService,
            CommonsService commonsService) {
        this.groupsBaseService = groupsBaseService;
        this.commonsService = commonsService;
    }

    @Override
    public List<GroupResponse> findGroupsBy(GroupsFilter groupsFilter, MyUserDetailsImpl myUserDetails) {

        if (commonsService.hasRole(myUserDetails, RoleType.ROLE_TEAM_LEADER)) {
            List<Integer> groupsId = new ArrayList<>();
            GroupAssigneesFilter groupAssigneesFilter = new GroupAssigneesFilter();
            groupAssigneesFilter.setAssigneesId(Collections.singletonList(myUserDetails.getUserId()));
            groupAssigneesFilter.setMultipleUserType(Arrays.asList(UsersType.AGENT, UsersType.CUSTOMER_SERVICE, UsersType.AGENT_MKT));
            List<GroupAssigneesResponse> groupAssigneesResponses = groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter);

            if (CollectionsUtility.isNotEmpty(groupAssigneesResponses)) {

                for (GroupAssigneesResponse groupAssigneesResponse : groupAssigneesResponses) {
                    if (ObjectUtils.allNotNull(groupAssigneesResponse.getGroup())) {
                        groupsId.add(groupAssigneesResponse.getGroup().getGroupId());
                    }
                }
            }

            groupsFilter.setListId(groupsId);
        }

        return groupsBaseService.findGroupsBy(groupsFilter);
    }
}
