package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.enums.PermissionType;
import com.oauthcentralization.app.tmsoauth2.model.enums.RoleType;
import com.oauthcentralization.app.tmsoauth2.model.enums.UsersType;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.service.CommonsService;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service(value = "commonsService")
@Transactional
public class CommonsServiceImpl implements CommonsService {

    private static final Logger logger = LoggerFactory.getLogger(CommonsServiceImpl.class);

    @Override
    public boolean hasRole(UsersResponse usersResponse, RoleType roleType) {
        if (!ObjectUtils.allNotNull(usersResponse, roleType) || CollectionsUtility.isEmpty(usersResponse.getRolesId())) {
            return false;
        }

        switch (roleType) {
            case ROLE_ADMIN:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_ADMIN.getRoleId());
            case ROLE_VALIDATION:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_VALIDATION.getRoleId());
            case ROLE_TEAM_LEADER:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_TEAM_LEADER.getRoleId());
            case ROLE_MANAGEMENT:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_MANAGEMENT.getRoleId());
            case ROLE_MANAGER:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_MANAGER.getRoleId());
            case ROLE_AGENT:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_AGENT.getRoleId());
            case ROLE_LASTMILE:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_LASTMILE.getRoleId());
            case ROLE_LOGISTIC:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_LOGISTIC.getRoleId());
            case ROLE_TL_EFA:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_TL_EFA.getRoleId());
            case ROLE_ACCOUNTANT:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_ACCOUNTANT.getRoleId());
            case ROLE_CS:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_CS.getRoleId());
            case ROLE_AGENT_MKT:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_AGENT_MKT.getRoleId());
            case ROLE_DIRECTOR:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_DIRECTOR.getRoleId());
            case ROLE_ALL_SUPER:
                return CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_ADMIN.getRoleId()) ||
                        CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_MANAGEMENT.getRoleId()) ||
                        CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_DIRECTOR.getRoleId()) ||
                        CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_TEAM_LEADER.getRoleId()) ||
                        CollectionUtils.containsInstance(usersResponse.getRolesId(), RoleType.ROLE_TL_EFA.getRoleId());
            default:
                logger.warn("RoleType not found.");
                return false;
        }

    }

    @Override
    public boolean hasPermission(UsersResponse usersResponse, PermissionType permissionType, List<UsersType> usersTypes) {

        if (!ObjectUtils.allNotNull(usersResponse, permissionType) || CollectionsUtility.isEmpty(usersResponse.getRolesId())) {
            return false;
        }

        if (permissionType.equals(PermissionType.CREATE_USER) ||
                permissionType.equals(PermissionType.UPDATE_USER) ||
                permissionType.equals(PermissionType.EXCLUDE_USER_GROUP) ||
                permissionType.equals(PermissionType.MOVE_USER_TO_GROUP)) {

            if (CollectionsUtility.isEmpty(usersTypes)) {
                return false;
            }

            boolean isTeamLeader = hasRole(usersResponse, RoleType.ROLE_TEAM_LEADER) || hasRole(usersResponse, RoleType.ROLE_TL_EFA);

            if (isTeamLeader) {
                return CollectionUtils.containsInstance(usersTypes, UsersType.AGENT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.AGENT_MKT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.CUSTOMER_SERVICE);
            }

            boolean isManager = hasRole(usersResponse, RoleType.ROLE_MANAGEMENT);

            if (isManager) {
                return CollectionUtils.containsInstance(usersTypes, UsersType.AGENT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.AGENT_MKT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.CUSTOMER_SERVICE) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.TEAM_LEADER) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.VALIDATOR);
            }

            boolean isAdmin = hasRole(usersResponse, RoleType.ROLE_ADMIN);

            if (isAdmin) {
                return CollectionUtils.containsInstance(usersTypes, UsersType.AGENT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.AGENT_MKT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.CUSTOMER_SERVICE) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.TEAM_LEADER) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.VALIDATOR) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.LAST_MILE) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.LOGISTIC) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.DIRECTOR) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.ACCOUNTANT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.MANAGER) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.MANAGEMENT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.MANIPULATE);
            }

            return false;
        }

        if (permissionType.equals(PermissionType.ADD_USER_TEAM) || permissionType.equals(PermissionType.EXCLUDE_USER_TEAM) || permissionType.equals(PermissionType.MOVE_USER_TO_TEAM)) {

            boolean isManager = hasRole(usersResponse, RoleType.ROLE_MANAGEMENT) || hasRole(usersResponse, RoleType.ROLE_MANAGER);

            if (isManager) {
                return CollectionUtils.containsInstance(usersTypes, UsersType.AGENT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.AGENT_MKT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.CUSTOMER_SERVICE) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.TEAM_LEADER) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.VALIDATOR) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.LAST_MILE) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.LOGISTIC) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.ACCOUNTANT);
            }

            boolean isAdmin = hasRole(usersResponse, RoleType.ROLE_ADMIN);

            if (isAdmin) {
                return CollectionUtils.containsInstance(usersTypes, UsersType.AGENT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.AGENT_MKT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.CUSTOMER_SERVICE) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.TEAM_LEADER) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.VALIDATOR) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.LAST_MILE) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.LOGISTIC) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.DIRECTOR) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.ACCOUNTANT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.MANAGER) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.MANAGEMENT) ||
                        CollectionUtils.containsInstance(usersTypes, UsersType.MANIPULATE);
            }

            return false;
        }


        return false;
    }
}
