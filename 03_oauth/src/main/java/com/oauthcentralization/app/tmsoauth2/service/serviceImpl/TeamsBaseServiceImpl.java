package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.filters.TeamMembersFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.TeamsFilter;
import com.oauthcentralization.app.tmsoauth2.model.mappers.MappersTo;
import com.oauthcentralization.app.tmsoauth2.model.request.QueryRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.*;
import com.oauthcentralization.app.tmsoauth2.service.TeamsBaseService;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.utils.DocsUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service(value = "teamsBaseService")
@Transactional
public class TeamsBaseServiceImpl implements TeamsBaseService {

    private static final Logger logger = LoggerFactory.getLogger(TeamsBaseServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param teamsFilter - user option filtering
     * @apiNote - return sql query with params request
     */
    private QueryRequest buildQueryTeamsRequest(TeamsFilter teamsFilter) {
        QueryRequest queryRequest = new QueryRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT ");

        builder.append(" team_request.id, team_request.name team_name, ");
        builder.append(" team_request.enable, team_request.team_type, ");

        /* begin::Users */
        builder.append(" user_request.user_id, user_request.user_name, ");
        builder.append(" user_request.fullname, user_request.user_type, ");
        builder.append(" user_request.phone, user_request.org_id, ");
        /* end::Users */

        /* begin::synonym */
        builder.append(" synonym_request.synonym_id, synonym_request.type, ");
        builder.append(" synonym_request.name synonym_name, synonym_request.value, ");
        builder.append(" synonym_request.dscr, synonym_request.type_id ");
        /* end::synonym */

        /* begin::From */
        builder.append(" FROM or_team team_request ");
        /* end::From */

        /* begin::Association */
        builder.append(" LEFT JOIN or_user user_request ON team_request.manager_id = user_request.user_id ");
        builder.append(" LEFT JOIN cf_synonym synonym_request ON team_request.team_type = synonym_request.synonym_id ");
        /* end::Association */
        builder.append(" WHERE 1=1 ");

        if (CollectionsUtility.isNotEmpty(teamsFilter.getListId())) {
            builder.append(" AND team_request.id IN :listId ");
        }

        if (CollectionsUtility.isNotEmpty(teamsFilter.getTeamTypeId())) {
            builder.append(" AND team_request.team_type IN :teamsTypeId ");
        }

        if (CollectionsUtility.isNotEmpty(teamsFilter.getManagersId())) {
            builder.append(" AND team_request.manager_id IN :managersId ");
        }

        if (StringUtility.isNotEmpty(teamsFilter.getName())) {
            builder.append(" AND LOWER(team_request.name) LIKE :name ");
        }

        if (ObjectUtils.allNotNull(teamsFilter.isActive())) {
            builder.append(" AND team_request.enable = :active ");
        }

        if (StringUtility.isNotEmpty(teamsFilter.getKeywords())) {
            builder.append(" AND ( ");
            builder.append(" LOWER(team_request.name) LIKE :keywords ");
            builder.append(" ) ");
        }

        /* begin:EOF query */
        builder.append(" ORDER BY team_request.id DESC");
        /* end:EOF query */

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(teamsFilter.getPageSize()) && teamsFilter.getPageSize() > 0) {
            builder.append(" limit :pageSize ");
            if (ObjectUtils.allNotNull(teamsFilter.getPageOffset())) {
                builder.append(" offset :pageOffset ");
            }
        }
        /* end::Set pagination */

        queryRequest.setQuery(builder.toString());
        return queryRequest;
    }

    /**
     * @param teamsFilter   - body request
     * @param entityManager - factory sql query
     * @apiNote - return sql query response
     */
    private QueryResponse snapQueryTeamsRequest(TeamsFilter teamsFilter, EntityManager entityManager) {
        QueryRequest queryRequest = buildQueryTeamsRequest(teamsFilter);
        QueryResponse queryResponse = new QueryResponse();
        Query query = entityManager.createNativeQuery(queryRequest.getQuery());

        if (CollectionsUtility.isNotEmpty(teamsFilter.getListId())) {
            query.setParameter("listId", teamsFilter.getListId());
        }

        if (CollectionsUtility.isNotEmpty(teamsFilter.getTeamTypeId())) {
            query.setParameter("teamsTypeId", teamsFilter.getTeamTypeId());
        }

        if (CollectionsUtility.isNotEmpty(teamsFilter.getManagersId())) {
            query.setParameter("managersId", teamsFilter.getManagersId());
        }

        if (StringUtility.isNotEmpty(teamsFilter.getName())) {
            query.setParameter("name", "%" + StringUtility.trimSingleWhitespace(teamsFilter.getName()).toLowerCase() + "%");
        }

        if (ObjectUtils.allNotNull(teamsFilter.isActive())) {
            query.setParameter("active", teamsFilter.isActive());
        }

        if (StringUtility.isNotEmpty(teamsFilter.getKeywords())) {
            query.setParameter("keywords", "%" + StringUtility.trimSingleWhitespace(teamsFilter.getKeywords()).toLowerCase() + "%");
        }

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(teamsFilter.getPageSize()) && teamsFilter.getPageSize() > 0) {
            query.setParameter("pageSize", teamsFilter.getPageSize());
            if (ObjectUtils.allNotNull(teamsFilter.getPageOffset())) {
                query.setParameter("pageOffset", teamsFilter.getPageOffset());
            }
        }
        /* end::Set pagination */

        queryResponse.setQuery(query);
        return queryResponse;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<TeamsResponse> findTeamsBy(TeamsFilter teamsFilter) {
        List<TeamsResponse> teamsResponses = new ArrayList<>();

        try {
            QueryResponse queryResponse = snapQueryTeamsRequest(teamsFilter, entityManager);
            List<Object[]> rowsIterator = queryResponse.getQuery().getResultList();
            for (Object[] row : rowsIterator) {
                TeamsResponse teamsResponse = new TeamsResponse();
                UsersResponse usersResponse = new UsersResponse();
                SynonymsResponse synonymsResponse = new SynonymsResponse();

                teamsResponse.setId((Integer) row[0]);
                teamsResponse.setName((String) row[1]);
                teamsResponse.setActive((Boolean) row[2]);
                teamsResponse.setTeamTypeId((Integer) row[3]);

                usersResponse.setUserId((Integer) row[4]);
                usersResponse.setUsername((String) row[5]);
                usersResponse.setFullName((String) row[6]);
                usersResponse.setUserType((String) row[7]);
                usersResponse.setPhone((String) row[8]);
                usersResponse.setOrganizationId((Integer) row[9]);

                synonymsResponse.setId((Integer) row[10]);
                synonymsResponse.setType((String) row[11]);
                synonymsResponse.setName((String) row[12]);
                synonymsResponse.setValue((Integer) row[13]);
                synonymsResponse.setDescription((String) row[14]);
                synonymsResponse.setTypeId((Integer) row[15]);

                teamsResponse.setUser(usersResponse);
                teamsResponse.setTeamType(synonymsResponse);
                teamsResponses.add(teamsResponse);
            }

            return teamsResponses;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return teamsResponses;
        }
    }

    /**
     * @param teamMembersFilter - user option filtering
     * @apiNote - return sql query with params request
     */
    private QueryRequest buildQueryTeamMembersRequest(TeamMembersFilter teamMembersFilter) {
        QueryRequest queryRequest = new QueryRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT ");

        builder.append(" team_request.id, team_request.name team_name, ");
        builder.append(" team_request.enable, team_request.team_type, ");

        /* begin::Users */
        builder.append(" user_request.user_id, user_request.user_name, ");
        builder.append(" user_request.fullname, user_request.user_type, ");
        builder.append(" user_request.phone, user_request.org_id, ");
        /* end::Users */

        builder.append(" team_member_request.enabled team_member_enable, team_member_request.id team_member_id ");

        /* begin::From */
        builder.append(" FROM or_team_member team_member_request ");
        /* end::From */

        /* begin::Association */
        builder.append(" LEFT JOIN or_team team_request ON team_member_request.team_id = team_request.id ");
        builder.append(" LEFT JOIN or_user user_request ON team_member_request.user_id = user_request.user_id ");
        /* end::Association */
        builder.append(" WHERE 1=1 ");

        if (CollectionsUtility.isNotEmpty(teamMembersFilter.getListId())) {
            builder.append(" AND team_member_request.id IN :listId ");
        }

        if (CollectionsUtility.isNotEmpty(teamMembersFilter.getTeamsId())) {
            builder.append(" AND team_member_request.team_id IN :teamsId ");
        }

        if (CollectionsUtility.isNotEmpty(teamMembersFilter.getUsersId())) {
            builder.append(" AND team_member_request.user_id IN :usersId ");
        }

        if (ObjectUtils.allNotNull(teamMembersFilter.isActive())) {
            builder.append(" AND team_member_request.enabled = :active ");
        }

        /* begin:EOF query */
        builder.append(" ORDER BY team_member_request.id DESC");
        /* end:EOF query */

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(teamMembersFilter.getPageSize()) && teamMembersFilter.getPageSize() > 0) {
            builder.append(" limit :pageSize ");
            if (ObjectUtils.allNotNull(teamMembersFilter.getPageOffset())) {
                builder.append(" offset :pageOffset ");
            }
        }
        /* end::Set pagination */

        queryRequest.setQuery(builder.toString());
        return queryRequest;
    }

    /**
     * @param teamMembersFilter - body request
     * @param entityManager     - factory sql query
     * @apiNote - return sql query response
     */
    private QueryResponse snapQueryTeamMembersRequest(TeamMembersFilter teamMembersFilter, EntityManager entityManager) {
        QueryRequest queryRequest = buildQueryTeamMembersRequest(teamMembersFilter);
        QueryResponse queryResponse = new QueryResponse();
        Query query = entityManager.createNativeQuery(queryRequest.getQuery());

        if (CollectionsUtility.isNotEmpty(teamMembersFilter.getListId())) {
            query.setParameter("listId", teamMembersFilter.getListId());
        }

        if (CollectionsUtility.isNotEmpty(teamMembersFilter.getTeamsId())) {
            query.setParameter("teamsId", teamMembersFilter.getTeamsId());
        }

        if (CollectionsUtility.isNotEmpty(teamMembersFilter.getUsersId())) {
            query.setParameter("usersId", teamMembersFilter.getUsersId());
        }

        if (ObjectUtils.allNotNull(teamMembersFilter.isActive())) {
            query.setParameter("active", teamMembersFilter.isActive());
        }

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(teamMembersFilter.getPageSize()) && teamMembersFilter.getPageSize() > 0) {
            query.setParameter("pageSize", teamMembersFilter.getPageSize());
            if (ObjectUtils.allNotNull(teamMembersFilter.getPageOffset())) {
                query.setParameter("pageOffset", teamMembersFilter.getPageOffset());
            }
        }
        /* end::Set pagination */

        queryResponse.setQuery(query);
        return queryResponse;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public List<TeamsResponse> findTeamMembersBy(TeamMembersFilter teamMembersFilter) {
        List<TeamsResponse> teamsResponses = new ArrayList<>();

        try {
            QueryResponse queryResponse = snapQueryTeamMembersRequest(teamMembersFilter, entityManager);
            List<Object[]> rowsIterator = queryResponse.getQuery().getResultList();
            for (Object[] row : rowsIterator) {
                TeamsResponse teamsResponse = new TeamsResponse();
                UsersResponse usersResponse = new UsersResponse();

                teamsResponse.setId((Integer) row[0]);
                teamsResponse.setName((String) row[1]);
                teamsResponse.setActive((Boolean) row[2]);
                teamsResponse.setTeamTypeId((Integer) row[3]);

                usersResponse.setUserId((Integer) row[4]);
                usersResponse.setUsername((String) row[5]);
                usersResponse.setFullName((String) row[6]);
                usersResponse.setUserType((String) row[7]);
                usersResponse.setPhone((String) row[8]);
                usersResponse.setOrganizationId((Integer) row[9]);

                teamsResponse.setActive((Boolean) row[10]);

                if (ObjectUtils.allNotNull(row[11])) {
                    teamsResponse.setTeamMemberId((Integer) row[11]);
                }

                teamsResponse.setUser(usersResponse);
                teamsResponses.add(teamsResponse);
            }

            return teamsResponses;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return teamsResponses;
        }
    }

    @Override
    public byte[] exportTeamsMembersToExcel(TeamMembersFilter teamMembersFilter, String sheetName) {
        List<TeamsMemberExcelResponse> list = MappersTo.convertToExcelTeamsMember(findTeamMembersBy(teamMembersFilter));
        return DocsUtils.buildDocsDope(list, TeamsMemberExcelResponse.class, sheetName, true, 100);
    }

}
