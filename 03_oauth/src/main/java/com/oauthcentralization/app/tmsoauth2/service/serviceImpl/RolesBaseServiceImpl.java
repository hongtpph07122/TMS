package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.filters.RolesFilter;
import com.oauthcentralization.app.tmsoauth2.model.request.QueryRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.QueryResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.RolesResponse;
import com.oauthcentralization.app.tmsoauth2.service.RolesBaseService;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service(value = "rolesService")
@Transactional
public class RolesBaseServiceImpl implements RolesBaseService {

    private static final Logger logger = LoggerFactory.getLogger(RolesBaseServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param rolesFilter - user option filtering
     * @apiNote - return sql query with params request
     */
    private QueryRequest buildQueryRolesRequest(RolesFilter rolesFilter) {
        QueryRequest queryRequest = new QueryRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT ");

        builder.append(" role_request.role_id, role_request.name, ");
        builder.append(" role_request.label, role_request.dscr, ");
        builder.append(" role_request.modifyby, role_request.modifydate ");

        builder.append(" FROM or_role role_request ");
        builder.append(" WHERE 1=1 ");

        if (CollectionsUtility.isNotEmpty(rolesFilter.getListId())) {
            builder.append(" AND role_request.role_id IN :listId ");
        }

        if (StringUtility.isNotEmpty(rolesFilter.getName())) {
            builder.append(" AND LOWER(role_request.name) LIKE :name ");
        }

        if (StringUtility.isNotEmpty(rolesFilter.getLabel())) {
            builder.append(" AND LOWER(role_request.label) LIKE :label ");
        }

        if (ObjectUtils.allNotNull(rolesFilter.getTopModifiedAt())) {
            builder.append(" AND role_request.modifydate >= :topModifiedAt ");
        }

        if (ObjectUtils.allNotNull(rolesFilter.getBottomModifiedAt())) {
            builder.append(" AND role_request.modifydate <= :bottomModifiedAt ");
        }

        if (StringUtility.isNotEmpty(rolesFilter.getKeywords())) {
            builder.append(" AND ( ");
            builder.append(" LOWER(role_request.name) LIKE :keywords ");
            builder.append(" OR ");
            builder.append(" LOWER(role_request.label) LIKE :keywords ");
            builder.append(" ) ");
        }

        /* begin:EOF query */
        builder.append(" ORDER BY role_request.modifydate DESC, role_request.role_id DESC");
        /* end:EOF query */

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(rolesFilter.getPageSize()) && rolesFilter.getPageSize() > 0) {
            builder.append(" limit :pageSize ");
            if (ObjectUtils.allNotNull(rolesFilter.getPageOffset())) {
                builder.append(" offset :pageOffset ");
            }
        }
        /* end::Set pagination */

        queryRequest.setQuery(builder.toString());
        return queryRequest;
    }

    /**
     * @param rolesFilter   - body request
     * @param entityManager - factory sql query
     * @apiNote - return sql query response
     */
    private QueryResponse snapQueryRolesRequest(RolesFilter rolesFilter, EntityManager entityManager) {
        QueryRequest queryRequest = buildQueryRolesRequest(rolesFilter);
        QueryResponse queryResponse = new QueryResponse();
        Query query = entityManager.createNativeQuery(queryRequest.getQuery());

        if (CollectionsUtility.isNotEmpty(rolesFilter.getListId())) {
            query.setParameter("listId", rolesFilter.getListId());
        }

        if (StringUtility.isNotEmpty(rolesFilter.getName())) {
            query.setParameter("name", "%" + StringUtility.trimSingleWhitespace(rolesFilter.getName()).toLowerCase() + "%");
        }

        if (StringUtility.isNotEmpty(rolesFilter.getLabel())) {
            query.setParameter("label", "%" + StringUtility.trimSingleWhitespace(rolesFilter.getLabel()).toLowerCase() + "%");
        }

        if (ObjectUtils.allNotNull(rolesFilter.getTopModifiedAt())) {
            query.setParameter("topModifiedAt", rolesFilter.getTopModifiedAt());
        }

        if (ObjectUtils.allNotNull(rolesFilter.getBottomModifiedAt())) {
            query.setParameter("bottomModifiedAt", rolesFilter.getBottomModifiedAt());
        }

        if (StringUtility.isNotEmpty(rolesFilter.getKeywords())) {
            query.setParameter("keywords", "%" + StringUtility.trimSingleWhitespace(rolesFilter.getKeywords()).toLowerCase() + "%");
        }

        /* begin::Set pagination */
        if (ObjectUtils.allNotNull(rolesFilter.getPageSize()) && rolesFilter.getPageSize() > 0) {
            query.setParameter("pageSize", rolesFilter.getPageSize());
            if (ObjectUtils.allNotNull(rolesFilter.getPageOffset())) {
                query.setParameter("pageOffset", rolesFilter.getPageOffset());
            }
        }
        /* end::Set pagination */

        queryResponse.setQuery(query);
        return queryResponse;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RolesResponse> findRolesBy(RolesFilter rolesFilter) {
        List<RolesResponse> rolesResponses = new ArrayList<>();

        try {
            QueryResponse queryResponse = snapQueryRolesRequest(rolesFilter, entityManager);
            List<Object[]> rowsIterator = queryResponse.getQuery().getResultList();
            for (Object[] row : rowsIterator) {
                RolesResponse rolesResponse = new RolesResponse();
                rolesResponse.setRoleId((Integer) row[0]);
                rolesResponse.setName((String) row[1]);
                rolesResponse.setLabel((String) row[2]);
                rolesResponse.setDescription((String) row[3]);
                rolesResponse.setModifiedBy((Integer) row[4]);

                if (ObjectUtils.allNotNull(row[5])) {
                    rolesResponse.setModifiedDate((Timestamp) row[5]);
                }

                rolesResponses.add(rolesResponse);
            }

            return rolesResponses;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return rolesResponses;
        }
    }

}
