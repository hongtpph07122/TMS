package com.tms.service.impl;

import com.tms.config.DBConfig;
import com.tms.dao.impl.RoleDao;
import com.tms.dto.*;
import com.tms.entity.LCProvince;
import com.tms.exception.ConfigNotFoundException;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService extends BaseService {
    private final static Logger LOGGER = LoggerFactory.getLogger(RoleService.class);

    private final static String GET_ROLES= "get_roles";

    @Autowired
    private RoleDao roleDao;


    public DBResponse<List<RoleTMS>> getRoleList(String _sessionId){
        DBResponse<List<RoleTMS>> response;
        try {
            final DBConfig config = getConfig(GET_ROLES);
            AppUtils.printInput(LOGGER, _sessionId, config, null, null);

            FutureTask<DBResponse<List<RoleTMS>>> memTask = new MemFutureTask<DBResponse<List<RoleTMS>>>() {

                @Override
                public DBResponse<List<RoleTMS>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<RoleTMS>>> dbTask = new DBFutureTask<DBResponse<List<RoleTMS>>>() {
                @Override
                public DBResponse<List<RoleTMS>> get() {
                    return roleDao.dbGetRoleList(config);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(LOGGER, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            LOGGER.error(response.toString());
        }
        return response;
    }
}
