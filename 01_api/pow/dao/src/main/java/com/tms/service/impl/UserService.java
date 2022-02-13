package com.tms.service.impl;

import com.tms.config.DBConfig;
import com.tms.dao.impl.LogDao;
import com.tms.dao.impl.UserDao;
import com.tms.dto.*;
import com.tms.entity.User;
import com.tms.entity.log.UpdUserV2;
import com.tms.exception.ConfigNotFoundException;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends BaseService {

    private final static String GET_PRIV_BY_USER ="get_priv_byuser";
    private final static String GET_USER ="get_user";
    private final static String GET_USER_V2 ="get_user_v2";
    private final static String GET_USER_V3 ="get_user_v3";
    private final static String GET_USER_V5 ="get_user_v5";
    private final static String GET_USER_NAME_PASS ="get_user_namepass";

    private final static String UPD_USER_V2 ="upd_user_v2";

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserDao userDao;

    private final LogDao logDao;

    @Autowired
    public UserService(UserDao userDao, LogDao logDao) {
        this.userDao = userDao;
        this.logDao = logDao;
    }

    public DBResponse<List<GetPrivByUserResp>> getPrivByUser(String _sessionId, GetPrivByUserParams params) {
        DBResponse<List<GetPrivByUserResp>> response;
        try {
            final DBConfig config = getConfig(GET_PRIV_BY_USER);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetPrivByUserResp>>> memTask = new MemFutureTask<DBResponse<List<GetPrivByUserResp>>>() {

                @Override
                public DBResponse<List<GetPrivByUserResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetPrivByUserResp>>> dbTask = new DBFutureTask<DBResponse<List<GetPrivByUserResp>>>() {
                @Override
                public DBResponse<List<GetPrivByUserResp>> get() {
                    return userDao.dbGetPrivByUser(config, params);
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

    public DBResponse<List<User>> getUser(String _sessionId, GetUserParams params) {
        DBResponse<List<User>> response;
        try {
            final DBConfig config = getConfig(GET_USER);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<User>>> memTask = new MemFutureTask<DBResponse<List<User>>>() {

                @Override
                public DBResponse<List<User>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<User>>> dbTask = new DBFutureTask<DBResponse<List<User>>>() {
                @Override
                public DBResponse<List<User>> get() {
                    return userDao.dbGetUser(config, params);
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

    public DBResponse<List<User>> getUserV3(String _sessionId, GetUserParamV2 params) {
        DBResponse<List<User>> response;
        try {
            final DBConfig config = getConfig(GET_USER_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<User>>> memTask = new MemFutureTask<DBResponse<List<User>>>() {

                @Override
                public DBResponse<List<User>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<User>>> dbTask = new DBFutureTask<DBResponse<List<User>>>() {
                @Override
                public DBResponse<List<User>> get() {
                    return userDao.dbGetUser(config, params);
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

    public DBResponse<List<User>> getUserV2(String _sessionId, GetUserV2 params) {
        DBResponse<List<User>> response;
        try {
            final DBConfig config = getConfig(GET_USER_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<User>>> memTask = new MemFutureTask<DBResponse<List<User>>>() {

                @Override
                public DBResponse<List<User>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<User>>> dbTask = new DBFutureTask<DBResponse<List<User>>>() {
                @Override
                public DBResponse<List<User>> get() {
                    return userDao.dbGetUser(config, params);
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

    public DBResponse<List<User>> getUserV5(String _sessionId, GetUserParamsV5 params) {
        DBResponse<List<User>> response;
        try {
            final DBConfig config = getConfig(GET_USER_V5);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<User>>> memTask = new MemFutureTask<DBResponse<List<User>>>() {

                @Override
                public DBResponse<List<User>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<User>>> dbTask = new DBFutureTask<DBResponse<List<User>>>() {
                @Override
                public DBResponse<List<User>> get() {
                    return userDao.dbGetUserV5(config, params);
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

    public DBResponse<List<User>> getUserNamePass(String _sessionId, GetUserPassParams params) {
        DBResponse<List<User>> response;
        try {
            final DBConfig config = getConfig(GET_USER_NAME_PASS);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<User>>> memTask = new MemFutureTask<DBResponse<List<User>>>() {

                @Override
                public DBResponse<List<User>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<User>>> dbTask = new DBFutureTask<DBResponse<List<User>>>() {
                @Override
                public DBResponse<List<User>> get() {
                    return userDao.dbGetUserNamPass(config, params);
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

    public DBResponse<?> upUserV2(String _sessionId, UpdUserV2 updLead) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPD_USER_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdUserV2(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdUserV2(config, updLead);
                }
            };
            response = insert(_sessionId, config, fileTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }
}
