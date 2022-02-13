package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.dto.HttpStatusCodesResponseDTO;
import com.oauthcentralization.app.tmsoauth2.model.enums.*;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupAssigneesFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.TeamMembersFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.UsersFilter;
import com.oauthcentralization.app.tmsoauth2.model.mappers.MappersTo;
import com.oauthcentralization.app.tmsoauth2.model.request.*;
import com.oauthcentralization.app.tmsoauth2.model.response.*;
import com.oauthcentralization.app.tmsoauth2.service.*;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.utils.DocsUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ValidationUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"all"})
@Service(value = "usersBaseService")
@Transactional
public class UsersBaseServiceImpl implements UsersBaseService {

    private static final Logger logger = LoggerFactory.getLogger(UsersBaseServiceImpl.class);

    @Autowired
    private GroupsBaseService groupsBaseService;

    @Autowired
    private TeamsBaseService teamsBaseService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private CommonsService commonsService;

    @Autowired
    private SMGActionTrunkService smgActionTrunkService;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${tms.geo.identify}")
    private int organizationId;

    /**
     * @param usersFilter - user option filtering
     * @apiNote - return sql query with params request
     */
    private QueryRequest buildQueryUsersRequest(UsersFilter usersFilter) {
        QueryRequest queryRequest = new QueryRequest();
        StringBuilder builder = new StringBuilder();

        /* begin::With */
        builder.append(" WITH ");
        /* begin::Team with */
        builder.append(" teams AS ");
        builder.append(" ( ");
        builder.append(" SELECT ");
        builder.append(" team_member_request.user_id, string_agg(team_request.name, ', ') team_list, ");
        builder.append(" string_agg(cast(team_request.id as varchar), ', ') teams_id_list  ");
        builder.append(" FROM or_team_member team_member_request ");
        builder.append(" LEFT JOIN or_team team_request ON team_member_request.team_id = team_request.id ");
        builder.append(" GROUP BY team_member_request.user_id ");
        builder.append(" ORDER BY count(DISTINCT team_member_request.team_id) DESC ");
        builder.append(" ), ");
        /* end::Team with */

        /* begin::Group with */
        builder.append(" groups AS ");
        builder.append(" ( ");
        builder.append(" SELECT ");
        builder.append(" group_agent_request.agent_id, string_agg(group_request.name, ', ') group_list, ");
        builder.append(" string_agg(cast(group_request.group_id as varchar), ', ') groups_id_list ");
        builder.append(" FROM or_group_agent group_agent_request ");
        builder.append(" LEFT JOIN or_group group_request ON group_request.group_id = group_agent_request.group_id ");
        builder.append(" GROUP BY group_agent_request.agent_id ");
        builder.append(" ORDER BY count(DISTINCT group_agent_request.group_id) DESC ");
        builder.append(" ) ");
        /* end::Group with */
        /* end::With */


        builder.append(" SELECT DISTINCT ");

        builder.append(" user_request.user_id, user_request.org_id, ");
        builder.append(" user_request.user_type, user_request.user_name, ");
        builder.append(" user_request.user_lock, user_request.fullname full_name, ");
        builder.append(" user_request.email, user_request.phone sip, ");
        builder.append(" user_request.birthday, user_request.modifydate, ");
        builder.append(" user_request.failed_login_count, user_request.password_update_time, ");
        builder.append(" user_request.is_expired, ");

        /* begin::Roles */
        builder.append(" role_request.role_id, role_request.name, ");
        builder.append(" role_request.label, role_request.dscr, ");
        /* end::Roles */

        /* begin::With */
        builder.append(" team_in_request.team_list, team_in_request.teams_id_list, ");
        builder.append(" group_in_request.group_list, group_in_request.groups_id_list ");
        /* end::With */

        /* begin::From */
        builder.append(" FROM or_user user_request ");
        /* end:From */

        /* begin::Association */
        builder.append(" LEFT JOIN or_user_role user_role_request ON user_request.user_id = user_role_request.user_id ");
        builder.append(" LEFT JOIN or_role role_request ON user_role_request.role_id = role_request.role_id ");
        builder.append(" LEFT JOIN teams team_in_request ON user_request.user_id = team_in_request.user_id ");
        builder.append(" LEFT JOIN groups group_in_request ON user_request.user_id = group_in_request.agent_id ");

        /*
        builder.append(" LEFT JOIN ");
        builder.append(" ( ");
        builder.append(" SELECT ");
        builder.append(" team_member_request.user_id, string_agg(team_request.name, ',') team_list, ");
        builder.append(" string_agg(cast(team_request.id as varchar), ',') teams_id_list  ");
        builder.append(" FROM or_team_member team_member_request ");
        builder.append(" LEFT JOIN or_team team_request ON team_member_request.team_id = team_request.id ");
        builder.append(" GROUP BY team_member_request.user_id ");
        builder.append(" ORDER BY count(DISTINCT team_member_request.team_id) DESC ");
        builder.append(" )  ");
        builder.append(" team_in_request ON user_request.user_id = team_in_request.user_id ");

        builder.append(" LEFT JOIN ");
        builder.append(" ( ");
        builder.append(" SELECT ");
        builder.append(" group_agent_request.agent_id, string_agg(group_request.name, ',') group_list, ");
        builder.append(" string_agg(cast(group_request.group_id as varchar), ',') groups_id_list ");
        builder.append(" FROM or_group_agent group_agent_request ");
        builder.append(" LEFT JOIN or_group group_request ON group_request.group_id = group_agent_request.group_id ");
        builder.append(" GROUP BY group_agent_request.agent_id ");
        builder.append(" ORDER BY count(DISTINCT group_agent_request.group_id) DESC ");
        builder.append(" )  ");
        builder.append(" group_in_request ON user_request.user_id = group_in_request.agent_id ");
        */

        /* end::Association */

        builder.append(" WHERE 1=1 ");

        if (CollectionsUtility.isNotEmpty(usersFilter.getUsersId())) {
            builder.append(" AND user_request.user_id IN :usersId ");
        }

        if (StringUtility.isNotEmpty(usersFilter.getUsername())) {
            builder.append(" AND TRIM(LOWER(user_request.user_name)) = :username ");
        }

        if (StringUtility.isNotEmpty(usersFilter.getFullname())) {
            builder.append(" AND LOWER(user_request.fullname) LIKE :fullName ");
        }

        if (StringUtility.isNotEmpty(usersFilter.getEmail())) {
            builder.append(" AND LOWER(user_request.email) LIKE :email ");
        }

        if (StringUtility.isNotEmpty(usersFilter.getPhone())) {
            builder.append(" AND LOWER(user_request.phone) LIKE :phone ");
        }

        if (ObjectUtils.allNotNull(usersFilter.getActive())) {
            builder.append(" AND user_request.user_lock = :active ");
        }

        if (ObjectUtils.allNotNull(usersFilter.isExpired())) {
            builder.append(" AND user_request.is_expired = :expired ");
        }

        if (ObjectUtils.allNotNull(usersFilter.getOrganizationId())) {
            builder.append(" AND user_request.org_id = :organizationId ");
        }

        if (ObjectUtils.allNotNull(usersFilter.getUserType())) {
            builder.append(" AND TRIM(LOWER(user_request.user_type)) LIKE :userType ");
        }

        if (ObjectUtils.allNotNull(usersFilter.getTopModifiedAt())) {
            builder.append(" AND user_request.modifydate >= :topModifiedAt ");
        }

        if (ObjectUtils.allNotNull(usersFilter.getBottomModifiedAt())) {
            builder.append(" AND user_request.modifydate <= :bottomModifiedAt ");
        }

        if (CollectionsUtility.isNotEmpty(usersFilter.getMultipleUserType())) {
            StringBuilder subBuilder = new StringBuilder();
            UsersType usersTypeLast = CollectionsUtility.lastElement(usersFilter.getMultipleUserType());
            subBuilder.append(" AND ( ");
            for (UsersType usersType : usersFilter.getMultipleUserType()) {
                if (ObjectUtils.allNotNull(usersType)) {
                    subBuilder.append(" LOWER(user_request.user_type) LIKE :userTypeSpecific ".replace(":userTypeSpecific", StringUtility.trimAllWhitespace(":userType".concat(usersType.getName()))));
                    if (ObjectUtils.allNotNull(usersTypeLast)) {
                        if (!(usersType.getValue() == usersTypeLast.getValue())) {
                            subBuilder.append(" OR  ");
                        }
                    }
                }

            }
            subBuilder.append(" ) ");
            builder.append(subBuilder.toString());
        }

        if (CollectionsUtility.isNotEmpty(usersFilter.getMultipleUsername())) {
            StringBuilder subBuilder = new StringBuilder();
            String lastStatusUserMark = CollectionsUtility.lastElement(usersFilter.getMultipleUsername());
            subBuilder.append(" AND ( ");
            for (String username : usersFilter.getMultipleUsername()) {
                if (StringUtility.isNotEmpty(username)) {
                    subBuilder.append(" LOWER(user_request.user_name) LIKE :userSpecific ".replace(":userSpecific", StringUtility.trimAllWhitespace(":userSpec".concat(username))));
                    if (ObjectUtils.allNotNull(lastStatusUserMark)) {
                        if (StringUtility.areNotEqualText(username, lastStatusUserMark)) {
                            subBuilder.append(" OR  ");
                        }
                    }
                }
            }
            subBuilder.append(" ) ");
            builder.append(subBuilder.toString());
        }

        if (CollectionsUtility.isNotEmpty(usersFilter.getMultipleTeam())) {
            StringBuilder subBuilder = new StringBuilder();
            String lastStatusUserMark = CollectionsUtility.lastElement(usersFilter.getMultipleTeam());
            subBuilder.append(" AND ( ");
            for (String team : usersFilter.getMultipleTeam()) {
                if (StringUtility.isNotEmpty(team)) {
                    subBuilder.append(" LOWER(team_in_request.team_list) LIKE :teamSpecific ".replace(":teamSpecific", StringUtility.trimAllWhitespace(":teamSpec".concat(team.replace("_", "").replace("-", "")))));
                    if (ObjectUtils.allNotNull(lastStatusUserMark)) {
                        if (StringUtility.areNotEqualText(team, lastStatusUserMark)) {
                            subBuilder.append(" OR  ");
                        }
                    }
                }
            }
            subBuilder.append(" ) ");
            builder.append(subBuilder.toString());
        }

        if (CollectionsUtility.isNotEmpty(usersFilter.getMultipleGroup())) {
            StringBuilder subBuilder = new StringBuilder();
            String lastStatusUserMark = CollectionsUtility.lastElement(usersFilter.getMultipleGroup());
            subBuilder.append(" AND ( ");
            for (String group : usersFilter.getMultipleGroup()) {
                if (StringUtility.isNotEmpty(group)) {
                    subBuilder.append(" LOWER(group_in_request.group_list) LIKE :groupSpecific ".replace(":groupSpecific", StringUtility.trimAllWhitespace(":groupSpec".concat(group.replace("_", "").replace("-", "")))));
                    if (ObjectUtils.allNotNull(lastStatusUserMark)) {
                        if (StringUtility.areNotEqualText(group, lastStatusUserMark)) {
                            subBuilder.append(" OR  ");
                        }
                    }
                }
            }
            subBuilder.append(" ) ");
            builder.append(subBuilder.toString());
        }

        if (StringUtility.isNotEmpty(usersFilter.getKeywords())) {
            builder.append(" AND ( ");
            builder.append(" LOWER(user_request.user_name) LIKE :keywords ");
            builder.append(" OR ");
            builder.append(" LOWER(user_request.full_name) LIKE :keywords ");
            builder.append(" OR ");
            builder.append(" LOWER(user_request.phone) LIKE :keywords ");
            builder.append(" OR ");
            builder.append(" LOWER(user_request.email) LIKE :keywords ");
            builder.append(" OR ");
            builder.append(" LOWER(user_request.user_type) LIKE :keywords ");
            builder.append(" ) ");
        }

        /* begin:EOF query */
        builder.append(" ORDER BY user_request.modifydate DESC, user_request.user_id DESC");
        /* end:EOF query */

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(usersFilter.getPageSize()) && usersFilter.getPageSize() > 0) {
            builder.append(" limit :pageSize ");
            if (ObjectUtils.allNotNull(usersFilter.getPageOffset())) {
                builder.append(" offset :pageOffset ");
            }
        }
        /* end::Set pagination */

        queryRequest.setQuery(builder.toString());
        return queryRequest;
    }

    /**
     * @param usersFilter   - body request
     * @param entityManager - factory sql query
     * @apiNote - return sql query response
     */
    private QueryResponse snapQueryUsersRequest(UsersFilter usersFilter, EntityManager entityManager) {
        QueryRequest queryRequest = buildQueryUsersRequest(usersFilter);
        QueryResponse queryResponse = new QueryResponse();
        Query query = entityManager.createNativeQuery(queryRequest.getQuery());

        if (CollectionsUtility.isNotEmpty(usersFilter.getUsersId())) {
            query.setParameter("usersId", usersFilter.getUsersId());
        }

        if (StringUtility.isNotEmpty(usersFilter.getUsername())) {
            query.setParameter("username", StringUtility.trimSingleWhitespace(usersFilter.getUsername()).toLowerCase());
        }

        if (StringUtility.isNotEmpty(usersFilter.getFullname())) {
            query.setParameter("fullName", "%" + StringUtility.trimSingleWhitespace(usersFilter.getFullname()).toLowerCase() + "%");
        }

        if (StringUtility.isNotEmpty(usersFilter.getEmail())) {
            query.setParameter("email", "%" + StringUtility.trimSingleWhitespace(usersFilter.getEmail()).toLowerCase() + "%");
        }

        if (StringUtility.isNotEmpty(usersFilter.getPhone())) {
            query.setParameter("phone", "%" + StringUtility.trimSingleWhitespace(usersFilter.getPhone()).toLowerCase() + "%");
        }

        if (ObjectUtils.allNotNull(usersFilter.isExpired())) {
            query.setParameter("expired", usersFilter.isExpired());
        }

        if (ObjectUtils.allNotNull(usersFilter.getActive())) {
            query.setParameter("active", usersFilter.getActive());
        }

        if (ObjectUtils.allNotNull(usersFilter.getOrganizationId())) {
            query.setParameter("organizationId", usersFilter.getOrganizationId());
        }

        if (ObjectUtils.allNotNull(usersFilter.getUserType())) {
            query.setParameter("userType", "%" + StringUtility.trimSingleWhitespace(usersFilter.getUserType().getName()).toLowerCase() + "%");
        }

        if (ObjectUtils.allNotNull(usersFilter.getTopModifiedAt())) {
            query.setParameter("topModifiedAt", usersFilter.getTopModifiedAt());
        }

        if (ObjectUtils.allNotNull(usersFilter.getBottomModifiedAt())) {
            query.setParameter("bottomModifiedAt", usersFilter.getBottomModifiedAt());
        }

        if (StringUtility.isNotEmpty(usersFilter.getKeywords())) {
            query.setParameter("keywords", "%" + StringUtility.trimSingleWhitespace(usersFilter.getKeywords()).toLowerCase() + "%");
        }

        if (CollectionsUtility.isNotEmpty(usersFilter.getMultipleUserType())) {
            for (UsersType usersType : usersFilter.getMultipleUserType()) {
                if (ObjectUtils.allNotNull(usersType)) {
                    query.setParameter(StringUtility.trimAllWhitespace("userType".concat(usersType.getName())), "%" + StringUtility.trimSingleWhitespace(usersType.getName()).toLowerCase() + "%");
                }
            }
        }

        if (CollectionsUtility.isNotEmpty(usersFilter.getMultipleUsername())) {
            for (String username : usersFilter.getMultipleUsername()) {
                if (StringUtility.isNotEmpty(username)) {
                    query.setParameter(StringUtility.trimAllWhitespace("userSpec".concat(username)), "%" + StringUtility.trimSingleWhitespace(username).toLowerCase() + "%");
                }
            }
        }

        if (CollectionsUtility.isNotEmpty(usersFilter.getMultipleTeam())) {
            for (String team : usersFilter.getMultipleTeam()) {
                if (StringUtility.isNotEmpty(team)) {
                    query.setParameter(StringUtility.trimAllWhitespace("teamSpec".concat(team.replace("_", "").replace("-", ""))), "%" + StringUtility.trimSingleWhitespace(team).toLowerCase() + "%");
                }
            }
        }

        if (CollectionsUtility.isNotEmpty(usersFilter.getMultipleGroup())) {
            for (String group : usersFilter.getMultipleGroup()) {
                if (StringUtility.isNotEmpty(group)) {
                    query.setParameter(StringUtility.trimAllWhitespace("groupSpec".concat(group.replace("_", "").replace("-", ""))), "%" + StringUtility.trimSingleWhitespace(group).toLowerCase() + "%");
                }
            }
        }

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(usersFilter.getPageSize()) && usersFilter.getPageSize() > 0) {
            query.setParameter("pageSize", usersFilter.getPageSize());
            if (ObjectUtils.allNotNull(usersFilter.getPageOffset())) {
                query.setParameter("pageOffset", usersFilter.getPageOffset());
            }
        }
        /* end::Set pagination */

        queryResponse.setQuery(query);
        return queryResponse;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<UsersResponse> findUsersBy(UsersFilter usersFilter) {
        List<UsersResponse> usersResponses = new ArrayList<>();
        usersFilter.setOrganizationId(organizationId);

        try {
            QueryResponse queryResponse = snapQueryUsersRequest(usersFilter, entityManager);
            List<Object[]> rowsIterator = queryResponse.getQuery().getResultList();
            for (Object[] row : rowsIterator) {
                List<GroupAssigneesResponse> groupAssigneesResponses = new ArrayList<>();
                List<RolesResponse> rolesResponses = new ArrayList<>();
                List<GroupResponse> groupResponses = new ArrayList<>();
                List<TeamsResponse> teamsResponses = new ArrayList<>();
                GroupAssigneesFilter groupAssigneesFilter = new GroupAssigneesFilter();
                TeamMembersFilter teamMembersFilter = new TeamMembersFilter();
                UsersResponse usersResponse = new UsersResponse();
                RolesResponse rolesResponse = new RolesResponse();
                GroupResponse groupResponse = new GroupResponse();
                TeamsResponse teamsResponse = new TeamsResponse();

                usersResponse.setUserId((Integer) row[0]);
                usersResponse.setOrganizationId((Integer) row[1]);
                usersResponse.setUserType((String) row[2]);
                usersResponse.setUsername((String) row[3]);
                usersResponse.setActiveStandard((Integer) row[4]);
                usersResponse.setFullName((String) row[5]);
                usersResponse.setEmail((String) row[6]);
                usersResponse.setPhone((String) row[7]);

                if (ObjectUtils.allNotNull(row[8])) {
                    usersResponse.setBirthday((Date) row[8]);
                }

                if (ObjectUtils.allNotNull(row[9])) {
                    usersResponse.setModifiedDate((Timestamp) row[9]);
                }

                usersResponse.setFailedLoginCount((Integer) row[10]);

                if (ObjectUtils.allNotNull(row[11])) {
                    usersResponse.setPasswordUpdateTime((Date) row[11]);
                }

                usersResponse.setExpired((Boolean) row[12]);

                /* begin::Role */
                rolesResponse.setRoleId((Integer) row[13]);
                rolesResponse.setName((String) row[14]);
                rolesResponse.setLabel((String) row[15]);
                rolesResponse.setDescription((String) row[16]);
                /* end::Role */

                /* begin::With */
                usersResponse.setTeamsName((String) row[17]);
                usersResponse.setTeamsId((String) row[18]);
                usersResponse.setGroupsName((String) row[19]);
                usersResponse.setGroupsId((String) row[20]);
                /* end::With */

                if (usersFilter.isViewParentsGroups()) {
                    groupAssigneesFilter.setPageIndex(1);
                    groupAssigneesFilter.setPageSize(usersFilter.getSizeOfGroups());
                    groupAssigneesFilter.setAssigneesId(Collections.singletonList(usersResponse.getUserId()));
                    groupAssigneesResponses.addAll(groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter));
                }

                if (usersFilter.isViewParentsTeams()) {
                    teamMembersFilter.setPageIndex(1);
                    teamMembersFilter.setPageSize(usersFilter.getSizeOfTeams());
                    teamMembersFilter.setUsersId(Collections.singletonList(usersResponse.getUserId()));
                    teamsResponses.addAll(teamsBaseService.findTeamMembersBy(teamMembersFilter));
                }

                if (CollectionsUtility.isNotEmpty(groupAssigneesResponses)) {
                    groupResponses = groupAssigneesResponses.stream().map(groupAssigneesResponse -> groupAssigneesResponse.getGroup()).collect(Collectors.toList());
                    if (CollectionsUtility.isNotEmpty(groupResponses)) {
                        usersResponse.setGroupsName(CollectionsUtility.toString(groupResponses.stream().map(GroupResponse::getName).collect(Collectors.toList()), ","));
                    }
                }

                if (CollectionsUtility.isNotEmpty(teamsResponses)) {
                    usersResponse.setTeamsName(CollectionsUtility.toString(teamsResponses.stream().map(TeamsResponse::getName).collect(Collectors.toList()), ","));
                }

                rolesResponses.add(rolesResponse);

                if (CollectionsUtility.isNotEmpty(rolesResponses)) {
                    usersResponse.setRolesId(rolesResponses.stream().map(RolesResponse::getRoleId).collect(Collectors.toList()));
                }

                usersResponse.setRoles(rolesResponses);
                usersResponse.setGroupsDetail(groupResponses);
                usersResponse.setTeamsDetail(teamsResponses);

                if (ObjectUtils.allNotNull(usersResponse.getPhone())) {
                    if (usersResponse.getPhone().endsWith(".0")) {
                        usersResponse.setPhone(StringUtility.removeLastChar(usersResponse.getPhone(), 2));
                    }
                }

                // add new user
                usersResponses.add(usersResponse);
            }

            return usersResponses;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return usersResponses;
        }
    }

    @Override
    public TMSResponse<?> excludeGroupMembers(GroupAttributesRequest groupAttributesRequest, MyUserDetailsImpl myUserDetails) {

        if (!ObjectUtils.allNotNull(groupAttributesRequest.getGroupId(), groupAttributesRequest.getUserId(), groupAttributesRequest.getGroupAssigneeId())) {
            return TMSResponse.buildTMSResponse("User & Group undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        UsersFilter usersFilter = new UsersFilter();
        usersFilter.setUsersId(Collections.singletonList(groupAttributesRequest.getUserId()));
        usersFilter.setPageIndex(1);
        usersFilter.setPageSize(1);
        List<UsersResponse> users = findUsersBy(usersFilter);

        if (CollectionsUtility.isEmpty(users)) {
            return TMSResponse.buildTMSResponse("User undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!commonsService.hasPermission(myUserDetails, PermissionType.EXCLUDE_USER_GROUP, Collections.singletonList(UsersType.findBy(users.get(0).getUserType())))) {
            return TMSResponse.buildTMSResponse(String.format("You do not have the authority to remove this user %s", users.get(0).getUsername()), null, HttpStatusCodesResponseDTO.METHOD_NOT_ALLOWED);
        }

        GroupAssigneesFilter groupAssigneesFilter = new GroupAssigneesFilter();
        SMGActionTrunkRequest smgActionTrunkRequest = new SMGActionTrunkRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" DELETE FROM or_group_agent ");
        builder.append(" WHERE ");

        /* begin::Where */
        builder.append(" gr_ag_id = :groupAssigneeId ");
        builder.append(" AND ");
        builder.append(" agent_id = :userId ");
        builder.append(" AND ");
        builder.append(" group_id = :groupId ");
        /* end::Where */

        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("groupAssigneeId", groupAttributesRequest.getGroupAssigneeId());
        query.setParameter("userId", groupAttributesRequest.getUserId());
        query.setParameter("groupId", groupAttributesRequest.getGroupId());

        /* begin::Trunk */
        groupAssigneesFilter.setListId(Collections.singletonList(groupAttributesRequest.getGroupAssigneeId()));
        groupAssigneesFilter.setGroupsId(Collections.singletonList(groupAttributesRequest.getGroupId()));
        groupAssigneesFilter.setAssigneesId(Collections.singletonList(groupAttributesRequest.getUserId()));
        List<GroupAssigneesResponse> groupAssigneesResponses = groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter);
        if (CollectionsUtility.isNotEmpty(groupAssigneesResponses)) {
            smgActionTrunkRequest.setOnTable(TablesType.GROUP_AGENT_TABLE);
            smgActionTrunkRequest.setLogJsonBefore(groupAssigneesResponses);
            smgActionTrunkRequest.setMyUserDetails(myUserDetails);
            smgActionTrunkRequest.setRequestJson(groupAttributesRequest);
            smgActionTrunkRequest.setModuleType(TrunkModuleType.OAUTH2_MODULE);
            smgActionTrunkRequest.setActionType(TrunkType.EXCLUDE_GROUP_MEMBERS);
            smgActionTrunkService.saveAsPayload(smgActionTrunkRequest);
        }
        /* end::Trunk */

        return TMSResponse.buildTMSResponse(query.executeUpdate() == 1 ?
                        String.format("User has been removed from group %s", groupAssigneesResponses.get(0).getGroup().getName()) :
                        "Unable to remove user from group.",
                null,
                HttpStatusCodesResponseDTO.OK);
    }

    @Override
    public TMSResponse<?> moveGroupMembers(GroupAttributesRequest groupAttributesRequest, MyUserDetailsImpl myUserDetails) {

        if (!ObjectUtils.allNotNull(groupAttributesRequest.getGroupId(), groupAttributesRequest.getUserId(), groupAttributesRequest.getGroupAssigneeId())) {
            return TMSResponse.buildTMSResponse("User & Group undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        UsersFilter usersFilter = new UsersFilter();
        usersFilter.setUsersId(Collections.singletonList(groupAttributesRequest.getUserId()));
        usersFilter.setPageIndex(1);
        usersFilter.setPageSize(1);
        List<UsersResponse> users = findUsersBy(usersFilter);

        if (CollectionsUtility.isEmpty(users)) {
            return TMSResponse.buildTMSResponse("User undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!commonsService.hasPermission(myUserDetails, PermissionType.MOVE_USER_TO_GROUP, Collections.singletonList(UsersType.findBy(users.get(0).getUserType())))) {
            return TMSResponse.buildTMSResponse(String.format("You do not have the authority to move this user %s", users.get(0).getUsername()), null, HttpStatusCodesResponseDTO.METHOD_NOT_ALLOWED);
        }

        GroupAssigneesFilter groupAssigneesFilter = new GroupAssigneesFilter();
        groupAssigneesFilter.setGroupsId(Collections.singletonList(groupAttributesRequest.getGroupId()));
        groupAssigneesFilter.setAssigneesId(Collections.singletonList(groupAttributesRequest.getUserId()));
        List<GroupAssigneesResponse> groupAssigneesResponses = groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter);

        if (CollectionsUtility.isNotEmpty(groupAssigneesResponses)) {
            return TMSResponse.buildTMSResponse(String.format("User %s already exists in group %s", users.get(0).getUsername(), groupAssigneesResponses.get(0).getGroup().getName()), null, HttpStatusCodesResponseDTO.METHOD_NOT_ALLOWED);
        }

        SMGActionTrunkRequest smgActionTrunkRequest = new SMGActionTrunkRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" UPDATE or_group_agent ");
        builder.append(" SET group_id = :groupId ");
        builder.append(" WHERE ");

        /* begin::Where */
        builder.append(" gr_ag_id = :groupAssigneeId ");
        builder.append(" AND ");
        builder.append(" agent_id = :userId ");
        /* end::Where */

        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("groupId", groupAttributesRequest.getGroupId());
        query.setParameter("groupAssigneeId", groupAttributesRequest.getGroupAssigneeId());
        query.setParameter("userId", groupAttributesRequest.getUserId());

        int result = query.executeUpdate();

        /* begin::Trunk */
        groupAssigneesFilter.setGroupsId(Collections.singletonList(groupAttributesRequest.getGroupId()));
        groupAssigneesFilter.setListId(Collections.singletonList(groupAttributesRequest.getGroupAssigneeId()));
        groupAssigneesFilter.setAssigneesId(Collections.singletonList(groupAttributesRequest.getUserId()));
        groupAssigneesResponses = groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter);
        if (CollectionsUtility.isNotEmpty(groupAssigneesResponses)) {
            smgActionTrunkRequest.setOnTable(TablesType.GROUP_AGENT_TABLE);
            smgActionTrunkRequest.setLogJsonAfter(groupAssigneesResponses);
            smgActionTrunkRequest.setMyUserDetails(myUserDetails);
            smgActionTrunkRequest.setRequestJson(groupAttributesRequest);
            smgActionTrunkRequest.setModuleType(TrunkModuleType.OAUTH2_MODULE);
            smgActionTrunkRequest.setActionType(TrunkType.MOVE_GROUP_MEMBERS);
            smgActionTrunkService.saveAsPayload(smgActionTrunkRequest);
        }
        /* end::Trunk */

        return TMSResponse.buildTMSResponse(result == 1 ?
                        String.format("User has been moved to group %s", groupAssigneesResponses.get(0).getGroup().getName()) :
                        "Unable to move user from group",
                null,
                HttpStatusCodesResponseDTO.OK);
    }

    @Override
    public TMSResponse<?> excludeTeamMembers(TeamAttributesRequest teamAttributesRequest, MyUserDetailsImpl myUserDetails) {

        boolean hasPermissions = commonsService.hasRole(myUserDetails, RoleType.ROLE_MANAGEMENT) || commonsService.hasRole(myUserDetails, RoleType.ROLE_ADMIN);

        if (!hasPermissions) {
            return TMSResponse.buildTMSResponse("You don't have permissions.", null, HttpStatusCodesResponseDTO.NOT_ACCEPTABLE);
        }

        if (!ObjectUtils.allNotNull(teamAttributesRequest.getTeamId(), teamAttributesRequest.getTeamMemberId(), teamAttributesRequest.getUserId())) {
            return TMSResponse.buildTMSResponse("User & Group undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        UsersFilter usersFilter = new UsersFilter();
        usersFilter.setUsersId(Collections.singletonList(teamAttributesRequest.getUserId()));
        usersFilter.setPageIndex(1);
        usersFilter.setPageSize(1);
        List<UsersResponse> users = findUsersBy(usersFilter);

        if (CollectionsUtility.isEmpty(users)) {
            return TMSResponse.buildTMSResponse("User undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!commonsService.hasPermission(myUserDetails, PermissionType.EXCLUDE_USER_TEAM, Collections.singletonList(UsersType.findBy(users.get(0).getUserType())))) {
            return TMSResponse.buildTMSResponse(String.format("You do not have the authority to remove this user %s", users.get(0).getUsername()), null, HttpStatusCodesResponseDTO.METHOD_NOT_ALLOWED);
        }

        TeamMembersFilter teamMembersFilter = new TeamMembersFilter();
        SMGActionTrunkRequest smgActionTrunkRequest = new SMGActionTrunkRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" DELETE FROM or_team_member ");
        builder.append(" WHERE ");

        /* begin::Where */
        builder.append(" id = :teamMemberId ");
        builder.append(" AND ");
        builder.append(" team_id = :teamId ");
        builder.append(" AND ");
        builder.append(" user_id = :userId ");
        /* end::Where */

        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("teamMemberId", teamAttributesRequest.getTeamMemberId());
        query.setParameter("teamId", teamAttributesRequest.getTeamId());
        query.setParameter("userId", teamAttributesRequest.getUserId());

        /* begin::Trunk */
        teamMembersFilter.setListId(Collections.singletonList(teamAttributesRequest.getTeamMemberId()));
        teamMembersFilter.setTeamsId(Collections.singletonList(teamAttributesRequest.getTeamId()));
        teamMembersFilter.setUsersId(Collections.singletonList(teamAttributesRequest.getUserId()));
        List<TeamsResponse> teamsResponses = teamsBaseService.findTeamMembersBy(teamMembersFilter);
        if (CollectionsUtility.isNotEmpty(teamsResponses)) {
            smgActionTrunkRequest.setOnTable(TablesType.TEAM_MEMBERS_TABLE);
            smgActionTrunkRequest.setLogJsonBefore(teamsResponses);
            smgActionTrunkRequest.setMyUserDetails(myUserDetails);
            smgActionTrunkRequest.setRequestJson(teamAttributesRequest);
            smgActionTrunkRequest.setModuleType(TrunkModuleType.OAUTH2_MODULE);
            smgActionTrunkRequest.setActionType(TrunkType.EXCLUDE_TEAM_MEMBERS);
            smgActionTrunkService.saveAsPayload(smgActionTrunkRequest);
        }
        /* end::Trunk */

        return TMSResponse.buildTMSResponse(query.executeUpdate() == 1 ?
                        String.format("User has been removed from team %s", teamsResponses.get(0).getName()) :
                        "Unable to remove user from team",
                null,
                HttpStatusCodesResponseDTO.OK);
    }

    @Override
    public TMSResponse<?> moveTeamMembers(TeamAttributesRequest teamAttributesRequest, MyUserDetailsImpl myUserDetails) {

        boolean hasPermissions = commonsService.hasRole(myUserDetails, RoleType.ROLE_MANAGEMENT) || commonsService.hasRole(myUserDetails, RoleType.ROLE_ADMIN);

        if (!hasPermissions) {
            return TMSResponse.buildTMSResponse("You don't have permissions.", null, HttpStatusCodesResponseDTO.NOT_ACCEPTABLE);
        }

        if (!ObjectUtils.allNotNull(teamAttributesRequest.getTeamId(), teamAttributesRequest.getTeamMemberId(), teamAttributesRequest.getUserId())) {
            return TMSResponse.buildTMSResponse("User & Group undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        UsersFilter usersFilter = new UsersFilter();
        usersFilter.setUsersId(Collections.singletonList(teamAttributesRequest.getUserId()));
        usersFilter.setPageIndex(1);
        usersFilter.setPageSize(1);
        List<UsersResponse> users = findUsersBy(usersFilter);

        if (CollectionsUtility.isEmpty(users)) {
            return TMSResponse.buildTMSResponse("User undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!commonsService.hasPermission(myUserDetails, PermissionType.MOVE_USER_TO_TEAM, Collections.singletonList(UsersType.findBy(users.get(0).getUserType())))) {
            return TMSResponse.buildTMSResponse(String.format("You do not have the authority to move this user %s", users.get(0).getUsername()), null, HttpStatusCodesResponseDTO.METHOD_NOT_ALLOWED);
        }

        TeamMembersFilter teamMembersFilter = new TeamMembersFilter();
        SMGActionTrunkRequest smgActionTrunkRequest = new SMGActionTrunkRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" UPDATE or_team_member ");
        builder.append(" SET team_id = :teamId ");
        builder.append(" WHERE ");

        /* begin::Where */
        builder.append(" id = :teamMemberId ");
        builder.append(" AND ");
        builder.append(" user_id = :userId ");
        /* end::Where */

        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("teamId", teamAttributesRequest.getTeamId());
        query.setParameter("teamMemberId", teamAttributesRequest.getTeamMemberId());
        query.setParameter("userId", teamAttributesRequest.getUserId());

        int result = query.executeUpdate();

        /* begin::Trunk */
        teamMembersFilter.setListId(Collections.singletonList(teamAttributesRequest.getTeamMemberId()));
        teamMembersFilter.setUsersId(Collections.singletonList(teamAttributesRequest.getUserId()));
        List<TeamsResponse> teamsResponses = teamsBaseService.findTeamMembersBy(teamMembersFilter);
        if (CollectionsUtility.isNotEmpty(teamsResponses)) {
            smgActionTrunkRequest.setOnTable(TablesType.TEAM_MEMBERS_TABLE);
            smgActionTrunkRequest.setLogJsonBefore(teamsResponses);
            smgActionTrunkRequest.setMyUserDetails(myUserDetails);
            smgActionTrunkRequest.setRequestJson(teamAttributesRequest);
            smgActionTrunkRequest.setModuleType(TrunkModuleType.OAUTH2_MODULE);
            smgActionTrunkRequest.setActionType(TrunkType.MOVE_TEAM_MEMBERS);
            smgActionTrunkService.saveAsPayload(smgActionTrunkRequest);
        }
        /* end::Trunk */

        return TMSResponse.buildTMSResponse(result == 1 ?
                        String.format("User has been moved to team %s", CollectionsUtility.isNotEmpty(teamsResponses) ? teamsResponses.get(0).getName() : "") :
                        "Unable to move user from team",
                null,
                HttpStatusCodesResponseDTO.OK);
    }

    @Override
    public byte[] exportUserToExcel(UsersFilter usersFilter, String sheetName) {
        List<UsersExcelResponse> list = MappersTo.convertToExcel(findUsersBy(usersFilter));
        return DocsUtils.buildDocsDope(list, UsersExcelResponse.class, sheetName, true, 100);
    }

    @Override
    public List<UsersRequest> importUsers(InputStream inputStream, String sheetName) {
        if (StringUtility.isEmpty(sheetName)) {
            return Collections.emptyList();
        }

        List<UsersRequest> usersRequests = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(StringUtility.trimSingleWhitespace(sheetName));

            Iterator<Row> rows = sheet.iterator();
            int rowNumber = 0;

            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                if (!DocsUtils.isRowEmpty(currentRow)) {

                    Iterator<Cell> cellsInRow = currentRow.iterator();
                    UsersRequest usersRequest = new UsersRequest();

                    int cellIndex = 0;
                    while (cellsInRow.hasNext()) {
                        Cell cellValue = cellsInRow.next();
                        cellIndex = cellValue.getColumnIndex();
                        // logger.info("Row: {} - column: {}", currentRow.getRowNum(), LoggerUtils.toJson(cellValue.getColumnIndex()));

                        switch (cellIndex) {
                            case 0:
                                usersRequest.setUsername(DocsUtils.getCellValue(cellValue));
                                break;

                            case 1:
                                usersRequest.setUsernameDomain(DocsUtils.getCellValue(cellValue));
                                break;

                            case 2:
                                usersRequest.setFullName(DocsUtils.getCellValue(cellValue));
                                break;

                            case 3:
                                usersRequest.setEmail(DocsUtils.getCellValue(cellValue));
                                break;

                            case 4:
                                usersRequest.setPhone(DocsUtils.getCellValue(cellValue));
                                break;

                            case 5:
                                usersRequest.setPassword(DocsUtils.getCellValue(cellValue));
                                break;

                            case 6:
                                usersRequest.setUserType(UsersType.findByName(DocsUtils.getCellValue(cellValue)));
                                break;

                            default:
                                break;
                        }

                        cellIndex++;
                    }

                    if (ObjectUtils.allNotNull(usersRequest.getPhone())) {
                        if (usersRequest.getPhone().endsWith(".0")) {
                            usersRequest.setPhone(StringUtility.removeLastChar(usersRequest.getPhone(), 2));
                        }
                    }

                    usersRequests.add(usersRequest);
                }

            }

            workbook.close();
            return usersRequests;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return usersRequests;
        }
    }

    @Override
    public TMSResponse<?> importUsers(String sheetName, MultipartFile file, MyUserDetailsImpl myUserDetails) {

        boolean hasPermissions = commonsService.hasRole(myUserDetails, RoleType.ROLE_ALL_SUPER);

        if (!hasPermissions) {
            return TMSResponse.buildTMSResponse("You don't have permissions.", null, HttpStatusCodesResponseDTO.NOT_ACCEPTABLE);
        }

        try {
            List<UsersRequest> usersRequests = importUsers(file.getInputStream(), sheetName);

            if (CollectionsUtility.isEmpty(usersRequests)) {
                return TMSResponse.buildTMSResponse("No users to import", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
            }

            OrganizationType organizationType = OrganizationType.findByValue(organizationId);
            List<DomainType> domainTypes = DomainType.findByOrganizationType(organizationType);

            List<UsersRequest> usersValid = new ArrayList<>();
            List<UsersRequest> usersInvalid = new ArrayList<>();
            List<UsersRequest> usersHanlder = new ArrayList<>();

            /* begin::Validation */
            for (UsersRequest usersRequest : usersRequests) {

                if (!ValidationUtils.isVerifiedAsEmail(usersRequest.getUsername())) {
                    if (StringUtility.isNotEmpty(usersRequest.getUsernameDomain())) {
                        if (StringUtility.isNotEmpty(usersRequest.getUsername())) {
                            usersRequest.setUsername(usersRequest.getUsername().concat(usersRequest.getUsernameDomain()));
                        }
                    }
                }

                usersHanlder.add(usersRequest);
            }
            /* end::Validation */

            /* begin::Handle users */
            if (CollectionsUtility.isNotEmpty(usersHanlder)) {
                for (UsersRequest usersRequest : usersHanlder) {
                    TMSResponse<?> response = usersService.validateCreateUser(usersRequest, myUserDetails);

                    if (!response.getHeader().equals(HttpStatusCodesResponseDTO.OK)) {
                        usersRequest.setErrorsReason(response.getMessage());
                        usersInvalid.add(usersRequest);
                    } else {
                        usersValid.add(usersRequest);
                    }
                }
            }
            /* end::Handle users */

            /* begin::Handle users invalid */
            if (CollectionsUtility.isNotEmpty(usersInvalid)) {
                return buildDocsInvalid(usersInvalid, sheetName);
            }
            /* end::Handle users invalid */

            if (CollectionsUtility.isNotEmpty(usersValid)) {
                for (UsersRequest usersRequest : usersValid) {
                    TMSResponse<?> response = usersService.saveAsPayloads(usersRequest, myUserDetails);
                }
            }

            return TMSResponse.buildTMSResponse(String.format("%d user(s) have been imported successfully", usersValid.size()), null, HttpStatusCodesResponseDTO.OK);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return TMSResponse.buildTMSResponse("Can not import this file excel, wrong format file", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

    }

    private TMSResponse<?> buildDocsInvalid(List<UsersRequest> users, String sheetName) {
        List<UsersRawExcelResponse> usersRawExcelResponses = MappersTo.convertToRawUsers(users);
        byte[] usersFailed = DocsUtils.buildDocsDope(usersRawExcelResponses, UsersRawExcelResponse.class, sheetName, true, 100);
        return TMSResponse.buildTMSResponse(usersFailed, HttpStatusCodesResponseDTO.CREATED);
    }

}
