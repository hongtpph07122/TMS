package com.tms.service.impl;

import com.tms.config.DBConfig;
import com.tms.dao.impl.CLActiveDao;
import com.tms.dto.*;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import com.tms.entity.CLActive;
import com.tms.exception.ConfigNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CLActiveService extends BaseService {

    private final static String GET_ACTIVE_LEAD ="get_activelead";
    private final static String GET_ACTIVE_LEAD_V3 ="get_activelead_v3";
    private final static String GET_ACTIVE_LEAD_V7 ="get_activelead_v7";
    private final static String GET_LEAD_BY_STATUS ="getactiveleadbystatus";
    private final static String GET_LEAD_BY_STATUS_AND_TIME ="getactiveleadbystatusandtime";
    private final static String GET_LEAD_BY_TIME ="getactiveleadbytime";
    private final static String GET_ACTIVE_LEAD_COUNT ="getactiveleadcount";
    private final static String GET_CALL_BACK_LEAD_COUNT ="getcallbackleadcount";
    private final static String GET_LEAD_COUNT ="getleadcount";
    private final static String GET_UNREACHABLE_LEAD ="get_unreachable_lead";
    private final static String GET_LOG_LEAD ="get_log_lead";

    //private final static String FUNC_NAME ="getleadbyid";
    private Logger logger = LoggerFactory.getLogger(CLActiveService.class);


    @Autowired
    private CLActiveDao clActiveDao;

    public DBResponse<List<CLActive>> getActiveLead(String _sessionId, CLActiveLeadParams activeLead) {
        DBResponse<List<CLActive>> response;
        try {
            final DBConfig config = getConfig(GET_ACTIVE_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, activeLead);

            FutureTask<DBResponse<List<CLActive>>> memTask = new MemFutureTask<DBResponse<List<CLActive>>>() {

                @Override
                public DBResponse<List<CLActive>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLActive>>> dbTask = new DBFutureTask<DBResponse<List<CLActive>>>() {
                @Override
                public DBResponse<List<CLActive>> get() {
                    return clActiveDao.dbGetActiveLead(config, activeLead);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<CLActive>> getActiveLeadV3(String _sessionId, CLActiveLeadParamsV3 activeLead) {
        DBResponse<List<CLActive>> response;
        try {
            final DBConfig config = getConfig(GET_ACTIVE_LEAD_V3);
            AppUtils.printInput(logger, _sessionId, config, null, activeLead);

            FutureTask<DBResponse<List<CLActive>>> memTask = new MemFutureTask<DBResponse<List<CLActive>>>() {

                @Override
                public DBResponse<List<CLActive>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLActive>>> dbTask = new DBFutureTask<DBResponse<List<CLActive>>>() {
                @Override
                public DBResponse<List<CLActive>> get() {
                    return clActiveDao.dbGetActiveLeadV3(config, activeLead);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<CLActive>> getActiveLeadV7(String _sessionId, CLActiveLeadParamsV7 activeLead) {
        DBResponse<List<CLActive>> response;
        try {
            final DBConfig config = getConfig(GET_ACTIVE_LEAD_V7);
            AppUtils.printInput(logger, _sessionId, config, null, activeLead);

            FutureTask<DBResponse<List<CLActive>>> memTask = new MemFutureTask<DBResponse<List<CLActive>>>() {

                @Override
                public DBResponse<List<CLActive>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLActive>>> dbTask = new DBFutureTask<DBResponse<List<CLActive>>>() {
                @Override
                public DBResponse<List<CLActive>> get() {
                    return clActiveDao.dbGetActiveLead(config, activeLead);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<CLActive>> getLeadByStatus(String _sessionId, CLActiveParams param) {
        DBResponse<List<CLActive>> response;
        try {

            final DBConfig config = getConfig(GET_LEAD_BY_STATUS);
            AppUtils.printInput(logger, _sessionId, config, null, param);
            FutureTask<DBResponse<List<CLActive>>> memTask = new MemFutureTask<DBResponse<List<CLActive>>>() {
                @Override
                public DBResponse<List<CLActive>> get() {
                    return clActiveDao.memGetLeadByStatus(config, param);
                }
            };
            FutureTask<DBResponse<List<CLActive>>> dbTask = new DBFutureTask<DBResponse<List<CLActive>>>() {
                @Override
                public DBResponse get() {
                    return clActiveDao.dbGetLeadByStatus(config, param);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);
            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<CLActive>> getLeadByStatusAndTime(String _sessionId, CLActiveStatusTimeParams param) {
        DBResponse<List<CLActive>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_BY_STATUS_AND_TIME);
            AppUtils.printInput(logger, _sessionId, config, null, param);

            FutureTask<DBResponse<List<CLActive>>> memTask = new MemFutureTask<DBResponse<List<CLActive>>>() {

                @Override
                public DBResponse<List<CLActive>> get() {
                    return clActiveDao.memGetLeadByStatusAndTime(config, param);
                }
            };

            FutureTask<DBResponse<List<CLActive>>> dbTask = new DBFutureTask<DBResponse<List<CLActive>>>() {
                @Override
                public DBResponse get() {
                    return clActiveDao.dbGetLeadByStatusAndTime(config, param);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<CLActive>> getLeadByTime(String _sessionId, CLActiveStatusTimeParams param) {
        DBResponse<List<CLActive>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_BY_TIME);
            AppUtils.printInput(logger, _sessionId, config, null, param);

            FutureTask<DBResponse<List<CLActive>>> memTask = new MemFutureTask<DBResponse<List<CLActive>>>() {

                @Override
                public DBResponse<List<CLActive>> get() {
                    return clActiveDao.memGetLeadByTime(config, param);
                }
            };

            FutureTask<DBResponse<List<CLActive>>> dbTask = new DBFutureTask<DBResponse<List<CLActive>>>() {
                @Override
                public DBResponse get() {
                    return clActiveDao.dbGetLeadByTime(config, param);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<Long> getActiveLeadCount(String _sessionId, Integer campainId) {
        DBResponse<Long> response;
        try {
            final DBConfig config = getConfig(GET_ACTIVE_LEAD_COUNT);
            AppUtils.printInput(logger, _sessionId, config, null, campainId);

            FutureTask<DBResponse<Long>> memTask = new MemFutureTask<DBResponse<Long>>() {

                @Override
                public DBResponse<Long> get() {
                    return clActiveDao.memGetLeadCount(config, campainId);
                }
            };

            FutureTask<DBResponse<Long>> dbTask = new DBFutureTask<DBResponse<Long>>() {
                @Override
                public DBResponse<Long> get() {
                    return clActiveDao.dbGetLeadCount(config, campainId);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<Long> getCallBackLeadCount(String _sessionId, Integer campainId) {
        DBResponse<Long> response;
        try {
            final DBConfig config = getConfig(GET_CALL_BACK_LEAD_COUNT);
            AppUtils.printInput(logger, _sessionId, config, null, campainId);

            FutureTask<DBResponse<Long>> memTask = new MemFutureTask<DBResponse<Long>>() {

                @Override
                public DBResponse<Long> get() {
                    return clActiveDao.memGetCallbackLeadCount(config, campainId);
                }
            };

            FutureTask<DBResponse<Long>> dbTask = new DBFutureTask<DBResponse<Long>>() {
                @Override
                public DBResponse<Long> get() {
                    return clActiveDao.dbGetCallbackLeadCount(config, campainId);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<Long> getLeadCount(String _sessionId, Integer campainId) {
        DBResponse<Long> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_COUNT);
            AppUtils.printInput(logger, _sessionId, config, null, campainId);

            FutureTask<DBResponse<Long>> memTask = new MemFutureTask<DBResponse<Long>>() {

                @Override
                public DBResponse<Long> get() {
                    return clActiveDao.memGetLeadCount(config, campainId);
                }
            };

            FutureTask<DBResponse<Long>> dbTask = new DBFutureTask<DBResponse<Long>>() {
                @Override
                public DBResponse<Long> get() {
                    return clActiveDao.dbGetLeadCount(config, campainId);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<GetUnreachableResp>> getUnreachableLead(String _sessionId, GetUnreachable activeLead) {
        DBResponse<List<GetUnreachableResp>> response;
        try {
            final DBConfig config = getConfig(GET_UNREACHABLE_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, activeLead);

            FutureTask<DBResponse<List<GetUnreachableResp>>> memTask = new MemFutureTask<DBResponse<List<GetUnreachableResp>>>() {

                @Override
                public DBResponse<List<GetUnreachableResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetUnreachableResp>>> dbTask = new DBFutureTask<DBResponse<List<GetUnreachableResp>>>() {
                @Override
                public DBResponse<List<GetUnreachableResp>> get() {
                    return clActiveDao.dbGetUnreachableLead(config, activeLead);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<GetLogLeadResp>> getLogLead(String _sessionId, GetLogLead activeLead) {
        DBResponse<List<GetLogLeadResp>> response;
        try {
            final DBConfig config = getConfig(GET_LOG_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, activeLead);

            FutureTask<DBResponse<List<GetLogLeadResp>>> memTask = new MemFutureTask<DBResponse<List<GetLogLeadResp>>>() {

                @Override
                public DBResponse<List<GetLogLeadResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetLogLeadResp>>> dbTask = new DBFutureTask<DBResponse<List<GetLogLeadResp>>>() {
                @Override
                public DBResponse<List<GetLogLeadResp>> get() {
                    return clActiveDao.dbGetLogLead(config, activeLead);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId , e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

}
