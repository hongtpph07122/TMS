package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.entities.UsersRoleEntity;
import com.oauthcentralization.app.tmsoauth2.model.request.QueryRequest;
import com.oauthcentralization.app.tmsoauth2.model.request.UsersRoleRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.QueryResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersRoleResponse;
import com.oauthcentralization.app.tmsoauth2.repositories.UsersRoleRepository;
import com.oauthcentralization.app.tmsoauth2.service.UsersRoleService;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service(value = "usersRoleService")
@Transactional
public class UsersRoleServiceImpl implements UsersRoleService {

    private static final Logger logger = LoggerFactory.getLogger(UsersRoleServiceImpl.class);
    private final UsersRoleRepository usersRoleRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    public UsersRoleServiceImpl(
            UsersRoleRepository usersRoleRepository,
            EntityManager entityManager) {
        this.usersRoleRepository = usersRoleRepository;
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UsersRoleResponse> snagUsersRoles(Integer userId) {
        if (!ObjectUtils.allNotNull(userId)) {
            return Collections.emptyList();
        }
        List<UsersRoleResponse> usersRoleResponses = new ArrayList<>();

        try {
            QueryResponse queryResponse = snagAsResultQueryUserRoleRequest(userId);
            List<Object[]> rowsIterator = queryResponse.getQuery().getResultList();
            for (Object[] row : rowsIterator) {
                UsersRoleResponse usersRoleResponse = new UsersRoleResponse();
                usersRoleResponse.setUserId((Integer) row[0]);
                usersRoleResponse.setUsername((String) row[1]);
                usersRoleResponse.setRoleSuffix((String) row[2]);
                usersRoleResponse.setRoleId((Integer) row[3]);
                usersRoleResponses.add(usersRoleResponse);
            }

            return usersRoleResponses;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return usersRoleResponses;
        }
    }

    private QueryRequest buildQueryUserRoleRequest() {
        QueryRequest queryRequest = new QueryRequest();
        StringBuilder builder = new StringBuilder();

        builder.append(" SELECT users.user_id, users.user_name, roles.\"name\", roles.role_id FROM \"or_user\" users ");
        builder.append(" LEFT JOIN \"or_user_role\" users_role ON users_role.user_id = users.user_id ");
        builder.append(" LEFT JOIN \"or_role\" roles ON roles.role_id = users_role.role_id ");
        builder.append(" WHERE users.user_id = :userId ");

        queryRequest.setQuery(builder.toString());
        String counter = "select count(*) from ( " + builder +
                " ) as numberTotal";
        queryRequest.setQueryAsCounters(counter);
        return queryRequest;
    }

    private QueryResponse snagAsResultQueryUserRoleRequest(Integer userId) {
        QueryRequest queryRequest = buildQueryUserRoleRequest();
        QueryResponse queryResponse = new QueryResponse();
        Query query = entityManager.createNativeQuery(queryRequest.getQuery());
        Query queryAsCounter = entityManager.createNativeQuery(queryRequest.getQueryAsCounters());

        if (ObjectUtils.allNotNull(userId)) {
            query.setParameter("userId", userId);
            queryAsCounter.setParameter("userId", userId);
        }

        queryResponse.setQuery(query);
        queryResponse.setQueryAsCounter(queryAsCounter);
        return queryResponse;
    }

    @Override
    public boolean saveAsPayload(UsersRoleRequest usersRoleRequest) {
        if (!ObjectUtils.allNotNull(usersRoleRequest)) {
            return false;
        }

        if (usersRoleRepository.existsByRoleIdAndUserId(
                usersRoleRequest.getRoleId(), usersRoleRequest.getUserId())) {
            return true;
        }

        UsersRoleEntity usersRoleEntity = new UsersRoleEntity();
        usersRoleEntity.setUserId(usersRoleRequest.getUserId());
        usersRoleEntity.setRoleId(usersRoleRequest.getRoleId());
        usersRoleRepository.save(usersRoleEntity);
        return true;
    }
}
