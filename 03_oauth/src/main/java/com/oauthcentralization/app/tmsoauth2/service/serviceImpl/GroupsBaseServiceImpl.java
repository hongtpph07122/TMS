package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.enums.UsersType;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupAssigneesFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupsFilter;
import com.oauthcentralization.app.tmsoauth2.model.mappers.MappersTo;
import com.oauthcentralization.app.tmsoauth2.model.request.QueryRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.*;
import com.oauthcentralization.app.tmsoauth2.service.GroupsBaseService;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.utils.DocsUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"all"})
@Service(value = "groupsBaseService")
@Transactional
public class GroupsBaseServiceImpl implements GroupsBaseService {

    private static final Logger logger = LoggerFactory.getLogger(GroupsBaseServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${tms.geo.identify}")
    private int organizationId;

    /**
     * @param groupsFilter - user option filtering
     * @apiNote - return sql query with params request
     */
    private QueryRequest buildQueryGroupsRequest(GroupsFilter groupsFilter) {
        QueryRequest queryRequest = new QueryRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT ");

        builder.append(" group_request.group_id, group_request.org_id, ");
        builder.append(" group_request.name, group_request.short_name, ");
        builder.append(" group_request.dscr, group_request.modifydate, ");
        builder.append(" group_request.group_type, group_request.skill_id ");

        builder.append(" FROM \"or_group\" group_request ");
        builder.append(" WHERE 1=1 ");

        if (CollectionsUtility.isNotEmpty(groupsFilter.getListId())) {
            builder.append(" AND group_request.group_id IN :listId ");
        }

        if (CollectionsUtility.isNotEmpty(groupsFilter.getSkillsId())) {
            builder.append(" AND group_request.skill_id IN :skillsId ");
        }

        if (StringUtility.isNotEmpty(groupsFilter.getName())) {
            builder.append(" AND LOWER(group_request.name) LIKE :name ");
        }

        if (StringUtility.isNotEmpty(groupsFilter.getShortName())) {
            builder.append(" AND LOWER(group_request.short_name) LIKE :shortName ");
        }

        if (ObjectUtils.allNotNull(groupsFilter.getTopModifiedAt())) {
            builder.append(" AND group_request.modifydate >= :topModifiedAt ");
        }

        if (ObjectUtils.allNotNull(groupsFilter.getBottomModifiedAt())) {
            builder.append(" AND group_request.modifydate <= :bottomModifiedAt ");
        }

        if (ObjectUtils.allNotNull(groupsFilter.getOrganizationId())) {
            builder.append(" AND group_request.org_id = :organizationId ");
        }

        if (StringUtility.isNotEmpty(groupsFilter.getKeywords())) {
            builder.append(" AND ( ");
            builder.append(" LOWER(group_request.name) LIKE :keywords ");
            builder.append(" OR ");
            builder.append(" LOWER(group_request.short_name) LIKE :keywords ");
            builder.append(" ) ");
        }

        /* begin:EOF query */
        builder.append(" ORDER BY group_request.modifydate DESC, group_request.group_id DESC");
        /* end:EOF query */

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(groupsFilter.getPageSize()) && groupsFilter.getPageSize() > 0) {
            builder.append(" limit :pageSize ");
            if (ObjectUtils.allNotNull(groupsFilter.getPageOffset())) {
                builder.append(" offset :pageOffset ");
            }
        }
        /* end::Set pagination */

        queryRequest.setQuery(builder.toString());
        return queryRequest;
    }

    /**
     * @param groupsFilter  - body request
     * @param entityManager - factory sql query
     * @apiNote - return sql query response
     */
    private QueryResponse snapQueryGroupsRequest(GroupsFilter groupsFilter, EntityManager entityManager) {
        QueryRequest queryRequest = buildQueryGroupsRequest(groupsFilter);
        QueryResponse queryResponse = new QueryResponse();
        Query query = entityManager.createNativeQuery(queryRequest.getQuery());

        if (CollectionsUtility.isNotEmpty(groupsFilter.getListId())) {
            query.setParameter("listId", groupsFilter.getListId());
        }

        if (CollectionsUtility.isNotEmpty(groupsFilter.getSkillsId())) {
            query.setParameter("skillsId", groupsFilter.getSkillsId());
        }

        if (StringUtility.isNotEmpty(groupsFilter.getName())) {
            query.setParameter("name", "%" + StringUtility.trimSingleWhitespace(groupsFilter.getName()).toLowerCase() + "%");
        }

        if (StringUtility.isNotEmpty(groupsFilter.getShortName())) {
            query.setParameter("shortName", "%" + StringUtility.trimSingleWhitespace(groupsFilter.getShortName()).toLowerCase() + "%");
        }

        if (ObjectUtils.allNotNull(groupsFilter.getTopModifiedAt())) {
            query.setParameter("topModifiedAt", groupsFilter.getTopModifiedAt());
        }

        if (ObjectUtils.allNotNull(groupsFilter.getBottomModifiedAt())) {
            query.setParameter("bottomModifiedAt", groupsFilter.getBottomModifiedAt());
        }

        if (ObjectUtils.allNotNull(groupsFilter.getOrganizationId())) {
            query.setParameter("organizationId", groupsFilter.getOrganizationId());
        }

        if (StringUtility.isNotEmpty(groupsFilter.getKeywords())) {
            query.setParameter("keywords", "%" + StringUtility.trimSingleWhitespace(groupsFilter.getKeywords()).toLowerCase() + "%");
        }

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(groupsFilter.getPageSize()) && groupsFilter.getPageSize() > 0) {
            query.setParameter("pageSize", groupsFilter.getPageSize());
            if (ObjectUtils.allNotNull(groupsFilter.getPageOffset())) {
                query.setParameter("pageOffset", groupsFilter.getPageOffset());
            }
        }
        /* end::Set pagination */

        queryResponse.setQuery(query);
        return queryResponse;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<GroupResponse> findGroupsBy(GroupsFilter groupsFilter) {
        List<GroupResponse> groupResponses = new ArrayList<>();
        groupsFilter.setOrganizationId(organizationId);

        try {
            QueryResponse queryResponse = snapQueryGroupsRequest(groupsFilter, entityManager);
            List<Object[]> rowsIterator = queryResponse.getQuery().getResultList();
            for (Object[] row : rowsIterator) {
                GroupResponse groupResponse = new GroupResponse();
                groupResponse.setGroupId((Integer) row[0]);
                groupResponse.setOrganizationId((Integer) row[1]);
                groupResponse.setName((String) row[2]);
                groupResponse.setShortName((String) row[3]);
                groupResponse.setDescription((String) row[4]);

                if (ObjectUtils.allNotNull(row[5])) {
                    groupResponse.setModifiedAt((Timestamp) row[5]);
                }

                groupResponse.setGroupType((Integer) row[6]);
                groupResponse.setSkillId((Integer) row[7]);

                groupResponses.add(groupResponse);
            }

            return groupResponses;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return groupResponses;
        }
    }

    /**
     * @param groupAssigneesFilter - user option filtering
     * @apiNote - return sql query with params request
     */
    private QueryRequest buildQueryGroupAssigneesRequest(GroupAssigneesFilter groupAssigneesFilter) {
        QueryRequest queryRequest = new QueryRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT ");

        builder.append(" group_assignee_request.gr_ag_id, group_assignee_request.modifydate group_assignee_modified_at, group_assignee_request.ag_skill_level, ");

        /* begin::Group details */
        builder.append(" group_request.name, group_request.short_name, ");
        builder.append(" group_request.dscr, group_request.modifydate group_modified_at, ");
        builder.append(" group_request.group_type, group_request.skill_id, ");
        builder.append(" group_request.group_id, ");
        /* end::Group details */

        /* begin::User details */
        builder.append(" user_request.user_id, user_request.user_name, ");
        builder.append(" user_request.user_type, user_request.fullname, ");
        builder.append(" user_request.phone, user_request.user_lock ");
        /* end::User details */

        /* begin::From */
        builder.append(" FROM or_group_agent group_assignee_request ");
        /* end::From */

        /* begin::Association */
        builder.append(" LEFT JOIN or_group group_request ON group_assignee_request.group_id = group_request.group_id ");
        builder.append(" LEFT JOIN or_user user_request ON group_assignee_request.agent_id = user_request.user_id ");
        /* end::Association */
        builder.append(" WHERE 1=1 ");

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getListId())) {
            builder.append(" AND group_assignee_request.gr_ag_id IN :listId ");
        }

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getGroupsId())) {
            builder.append(" AND group_assignee_request.group_id IN :groupsId ");
        }

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getAssigneesId())) {
            builder.append(" AND group_assignee_request.agent_id IN :assigneesId ");
        }

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getGroupsAssigneesSkillsLevel())) {
            builder.append(" AND group_assignee_request.ag_skill_level IN :assigneeSkillsLevel ");
        }

        if (ObjectUtils.allNotNull(groupAssigneesFilter.getTopModifiedAt())) {
            builder.append(" AND group_assignee_request.modifydate >= :topModifiedAt ");
        }

        if (ObjectUtils.allNotNull(groupAssigneesFilter.getBottomModifiedAt())) {
            builder.append(" AND group_assignee_request.modifydate <= :bottomModifiedAt ");
        }

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getMultipleUserType())) {
            StringBuilder subBuilder = new StringBuilder();
            UsersType usersTypeLast = CollectionsUtility.lastElement(groupAssigneesFilter.getMultipleUserType());
            subBuilder.append(" AND ( ");
            for (UsersType usersType : groupAssigneesFilter.getMultipleUserType()) {
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

        /* begin:EOF query */
        builder.append(" ORDER BY group_assignee_request.modifydate DESC, group_assignee_request.gr_ag_id DESC");
        /* end:EOF query */

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(groupAssigneesFilter.getPageSize()) && groupAssigneesFilter.getPageSize() > 0) {
            builder.append(" limit :pageSize ");
            if (ObjectUtils.allNotNull(groupAssigneesFilter.getPageOffset())) {
                builder.append(" offset :pageOffset ");
            }
        }
        /* end::Set pagination */

        queryRequest.setQuery(builder.toString());
        return queryRequest;
    }

    /**
     * @param groupAssigneesFilter - body request
     * @param entityManager        - factory sql query
     * @apiNote - return sql query response
     */
    private QueryResponse snapQueryGroupAssigneesRequest(GroupAssigneesFilter groupAssigneesFilter, EntityManager entityManager) {
        QueryRequest queryRequest = buildQueryGroupAssigneesRequest(groupAssigneesFilter);
        QueryResponse queryResponse = new QueryResponse();
        Query query = entityManager.createNativeQuery(queryRequest.getQuery());

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getListId())) {
            query.setParameter("listId", groupAssigneesFilter.getListId());
        }

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getGroupsId())) {
            query.setParameter("groupsId", groupAssigneesFilter.getGroupsId());
        }

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getAssigneesId())) {
            query.setParameter("assigneesId", groupAssigneesFilter.getAssigneesId());
        }

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getGroupsAssigneesSkillsLevel())) {
            query.setParameter("assigneeSkillsLevel", groupAssigneesFilter.getGroupsAssigneesSkillsLevel());
        }

        if (ObjectUtils.allNotNull(groupAssigneesFilter.getTopModifiedAt())) {
            query.setParameter("topModifiedAt", groupAssigneesFilter.getTopModifiedAt());
        }

        if (ObjectUtils.allNotNull(groupAssigneesFilter.getBottomModifiedAt())) {
            query.setParameter("bottomModifiedAt", groupAssigneesFilter.getBottomModifiedAt());
        }

        if (CollectionsUtility.isNotEmpty(groupAssigneesFilter.getMultipleUserType())) {
            for (UsersType usersType : groupAssigneesFilter.getMultipleUserType()) {
                if (ObjectUtils.allNotNull(usersType)) {
                    query.setParameter(StringUtility.trimAllWhitespace("userType".concat(usersType.getName())), "%" + StringUtility.trimSingleWhitespace(usersType.getName()).toLowerCase() + "%");
                }
            }
        }

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(groupAssigneesFilter.getPageSize()) && groupAssigneesFilter.getPageSize() > 0) {
            query.setParameter("pageSize", groupAssigneesFilter.getPageSize());
            if (ObjectUtils.allNotNull(groupAssigneesFilter.getPageOffset())) {
                query.setParameter("pageOffset", groupAssigneesFilter.getPageOffset());
            }
        }
        /* end::Set pagination */

        queryResponse.setQuery(query);
        return queryResponse;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<GroupAssigneesResponse> findGroupAssigneesBy(GroupAssigneesFilter groupAssigneesFilter) {
        List<GroupAssigneesResponse> groupAssigneesResponses = new ArrayList<>();
        try {
            QueryResponse queryResponse = snapQueryGroupAssigneesRequest(groupAssigneesFilter, entityManager);
            List<Object[]> rowsIterator = queryResponse.getQuery().getResultList();
            for (Object[] row : rowsIterator) {
                GroupAssigneesResponse groupAssigneesResponse = new GroupAssigneesResponse();
                GroupResponse groupResponse = new GroupResponse();
                UsersResponse usersResponse = new UsersResponse();

                groupAssigneesResponse.setGroupAssigneeId((Integer) row[0]);
                groupAssigneesResponse.setModifiedAt((Timestamp) row[1]);
                groupAssigneesResponse.setAssigneeSkillLevel((Integer) row[2]);

                /* begin::Groups */
                groupResponse.setName((String) row[3]);
                groupResponse.setShortName((String) row[4]);
                groupResponse.setDescription((String) row[5]);
                groupResponse.setModifiedAt((Timestamp) row[6]);
                groupResponse.setGroupType((Integer) row[7]);
                groupResponse.setSkillId((Integer) row[8]);
                groupResponse.setGroupId((Integer) row[9]);
                /* end::Groups */

                /* begin::Users */
                usersResponse.setUserId((Integer) row[10]);
                usersResponse.setUsername((String) row[11]);
                usersResponse.setUserType((String) row[12]);
                usersResponse.setFullName((String) row[13]);
                usersResponse.setPhone((String) row[14]);
                usersResponse.setActiveStandard((Integer) row[15]);
                /* end::Users */

                groupAssigneesResponse.setUserTypeId(UsersType.findBy(usersResponse.getUserType()).getValue());
                groupAssigneesResponse.setUser(usersResponse);
                groupAssigneesResponse.setGroup(groupResponse);
                groupAssigneesResponses.add(groupAssigneesResponse);
            }

            return groupAssigneesResponses;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return groupAssigneesResponses;
        }
    }

    @Override
    public byte[] exportGroupsMemberToExcel(GroupAssigneesFilter groupAssigneesFilter, String sheetName) {
        List<GroupsMemberExcelResponse> list = MappersTo.convertToExcelGroupsMember(findGroupAssigneesBy(groupAssigneesFilter));
        return DocsUtils.buildDocsDope(list, GroupsMemberExcelResponse.class, sheetName, true, 100);
    }

}
