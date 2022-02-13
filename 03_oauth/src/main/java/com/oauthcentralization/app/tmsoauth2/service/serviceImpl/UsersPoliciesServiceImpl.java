package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.enums.RoleType;
import com.oauthcentralization.app.tmsoauth2.model.enums.UsersType;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupAssigneesFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.UsersFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.GroupAssigneesResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersTypeResponse;
import com.oauthcentralization.app.tmsoauth2.service.CommonsService;
import com.oauthcentralization.app.tmsoauth2.service.GroupsBaseService;
import com.oauthcentralization.app.tmsoauth2.service.UsersBaseService;
import com.oauthcentralization.app.tmsoauth2.service.UsersPoliciesService;
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
import java.util.stream.Collectors;

@SuppressWarnings({"FieldCanBeLocal"})
@Service(value = "usersPoliciesService")
@Transactional
public class UsersPoliciesServiceImpl implements UsersPoliciesService {

    private static final Logger logger = LoggerFactory.getLogger(UsersPoliciesServiceImpl.class);

    private final UsersBaseService usersBaseService;
    private final CommonsService commonsService;
    private final GroupsBaseService groupsBaseService;

    @Autowired
    public UsersPoliciesServiceImpl(
            UsersBaseService usersBaseService,
            CommonsService commonsService,
            GroupsBaseService groupsBaseService) {
        this.usersBaseService = usersBaseService;
        this.commonsService = commonsService;
        this.groupsBaseService = groupsBaseService;
    }

    @Override
    public List<UsersResponse> findUsersByPolicies(UsersFilter usersFilter, MyUserDetailsImpl myUserDetails) {
        if (commonsService.hasRole(myUserDetails, RoleType.ROLE_TEAM_LEADER)) {
            GroupAssigneesFilter groupAssigneesFilter = new GroupAssigneesFilter();
            List<Integer> usersId = new ArrayList<>();
            List<Integer> groupsId = new ArrayList<>();

            groupAssigneesFilter.setAssigneesId(Collections.singletonList(myUserDetails.getUserId()));

            List<GroupAssigneesResponse> groupAssigneesResponses = groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter);

            if (CollectionsUtility.isNotEmpty(groupAssigneesResponses)) {
                for (GroupAssigneesResponse groupAssigneesResponse : groupAssigneesResponses) {
                    if (ObjectUtils.allNotNull(groupAssigneesResponse.getGroup()) && ObjectUtils.allNotNull(groupAssigneesResponse.getGroup().getGroupId())) {
                        groupsId.add(groupAssigneesResponse.getGroup().getGroupId());
                    }
                }
            }

            if (CollectionsUtility.isNotEmpty(groupsId)) {
                groupAssigneesFilter.setAssigneesId(Collections.emptyList());
                groupAssigneesFilter.setGroupsId(groupsId);
                groupAssigneesResponses = groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter);

                if (CollectionsUtility.isNotEmpty(groupAssigneesResponses)) {
                    for (GroupAssigneesResponse groupAssigneesResponse : groupAssigneesResponses) {
                        if (ObjectUtils.allNotNull(groupAssigneesResponse.getUser()) && ObjectUtils.allNotNull(groupAssigneesResponse.getUser().getUserId())) {
                            usersId.add(groupAssigneesResponse.getUser().getUserId());
                        }
                    }
                }
            }

            usersFilter.setMultipleUserType(Arrays.asList(UsersType.AGENT, UsersType.CUSTOMER_SERVICE));
            usersFilter.setUsersId(CollectionsUtility.removeElementsAsDuplicated(usersId));
        }

        return usersBaseService.findUsersBy(usersFilter);
    }

    @Override
    public List<UsersResponse> filterUsersByPolicies(UsersFilter usersFilter, MyUserDetailsImpl myUserDetails) {
        if (commonsService.hasRole(myUserDetails, RoleType.ROLE_TEAM_LEADER)) {
            usersFilter.setMultipleUserType(Arrays.asList(UsersType.AGENT, UsersType.CUSTOMER_SERVICE));
        }
        return usersBaseService.findUsersBy(usersFilter);
    }

    @Override
    public List<UsersTypeResponse> findAllUsersType(MyUserDetailsImpl myUserDetails) {
        if (commonsService.hasRole(myUserDetails, RoleType.ROLE_TEAM_LEADER)) {
            List<UsersType> usersTypes = Arrays.asList(UsersType.AGENT, UsersType.CUSTOMER_SERVICE);
            return usersTypes.stream().map(usersType -> UsersType.findBy(usersType.getValue())).collect(Collectors.toList());
        }
        return UsersType.findAll();
    }

    @Override
    public byte[] exportUserToExcel(UsersFilter usersFilter, MyUserDetailsImpl myUserDetails, String sheetName) {
        if (commonsService.hasRole(myUserDetails, RoleType.ROLE_TEAM_LEADER)) {
            if (CollectionsUtility.isEmpty(usersFilter.getMultipleUserType())) {
                List<UsersType> usersTypes = Arrays.asList(UsersType.AGENT, UsersType.CUSTOMER_SERVICE);
                usersFilter.setMultipleUserType(usersTypes);
            }
        }
        return usersBaseService.exportUserToExcel(usersFilter, sheetName);
    }
}
