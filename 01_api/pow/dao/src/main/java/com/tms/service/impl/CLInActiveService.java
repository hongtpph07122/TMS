package com.tms.service.impl;

import com.tms.config.DBConfig;
import com.tms.dao.impl.CLActiveDao;
import com.tms.dao.impl.CLInActiveDao;
import com.tms.dto.*;
import com.tms.entity.CLActive;
import com.tms.entity.CLInActive;
import com.tms.entity.FreshLead;
import com.tms.exception.ConfigNotFoundException;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CLInActiveService extends BaseService {

    private final static String GET_INACTIVE_LEAD ="get_inactivelead";
    private final static String GET_INACTIVE_LEAD_V3 ="get_inactivelead_v3";
    private final static String GET_INACTIVE_LEAD_V7 ="get_inactivelead_v7";


    private Logger logger = LoggerFactory.getLogger(CLInActiveService.class);


    @Autowired
    private CLInActiveDao clInActiveDao;

    public DBResponse<List<CLInActive>> getInActiveLead(String _sessionId, CLInActiveParams params) {
        DBResponse<List<CLInActive>> response;
        try {
            final DBConfig config = getConfig(GET_INACTIVE_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLInActive>>> memTask = new MemFutureTask<DBResponse<List<CLInActive>>>() {

                @Override
                public DBResponse<List<CLInActive>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLInActive>>> dbTask = new DBFutureTask<DBResponse<List<CLInActive>>>() {
                @Override
                public DBResponse<List<CLInActive>> get() {
                    return clInActiveDao.dbGetInActiveLead(config, params);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<CLInActive>> getInActiveLeadV3(String _sessionId, CLInActiveParamsV3 params) {
        DBResponse<List<CLInActive>> response;
        try {
            final DBConfig config = getConfig(GET_INACTIVE_LEAD_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLInActive>>> memTask = new MemFutureTask<DBResponse<List<CLInActive>>>() {

                @Override
                public DBResponse<List<CLInActive>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLInActive>>> dbTask = new DBFutureTask<DBResponse<List<CLInActive>>>() {
                @Override
                public DBResponse<List<CLInActive>> get() {
                    return clInActiveDao.dbGetInActiveLeadV3(config, params);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<CLInActive>> getInActiveLeadV7(String _sessionId, CLInActiveParamsV7 params) {
        DBResponse<List<CLInActive>> response;
        try {
            final DBConfig config = getConfig(GET_INACTIVE_LEAD_V7);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLInActive>>> memTask = new MemFutureTask<DBResponse<List<CLInActive>>>() {

                @Override
                public DBResponse<List<CLInActive>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLInActive>>> dbTask = new DBFutureTask<DBResponse<List<CLInActive>>>() {
                @Override
                public DBResponse<List<CLInActive>> get() {
                    return clInActiveDao.dbGetInActiveLead(config, params);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

}
