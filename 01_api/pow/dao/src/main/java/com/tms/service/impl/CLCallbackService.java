package com.tms.service.impl;

import com.tms.config.DBConfig;
import com.tms.dao.impl.CLCallbackDao;
import com.tms.dto.*;
import com.tms.entity.CLCallback;
import com.tms.exception.ConfigNotFoundException;
import com.tms.model.Request.LeadStatusCallbackWithTimeRequestDTO;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CLCallbackService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(CLCallbackService.class);

    private final static String GET_CALLBACK ="get_callback";
    private final static String GET_CALLBACK_V4 ="get_callback_v4";
    private final static String GET_CALLBACK_ORDER_BY_LEAD_STATUS ="get_callback_order_by_lead_status";
    private final static String GET_CALLBACK_STATUS_MULTI_CAMPAIGNS ="get_callback_status_multi";
    private final static String GET_CALLBACK_V7 ="get_callback_v7";
    private final static String GET_CALLBACK_ALL ="get_callback_all";
    private final static String GET_CALLBACK_ALL_V3 ="get_callback_all_v3";
    private final static String GET_CALLBACK_BY_TIME ="get_callback_bytime";
    private final static String GET_CALLBACK_BY_TIME_V2 ="get_callback_bytime_v2";
    private final static String GET_CALLBACK_BY_TIME_V7 ="get_callback_bytime_v7";
    private final static String GET_CALLBACK_BY_TIME_V8 ="get_callback_bytime_v8";
    private final static String GET_OR_DEPARTMENT ="get_or_department";
    private final static String GET_CALLBACK_BY_FIRST_CREATE_DATE ="get_callback_by_first_create_date";
    private final static String GET_CALLBACK_BY_LEAD_IDS ="get_callback_by_lead_ids";

    @Autowired
    private CLCallbackDao clCallbackDao;


    public DBResponse<List<CLCallback>> getCallback(String _sessionId, GetCLCallback params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallback(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackV4(String _sessionId, GetCLCallbackV4 params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_V4);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackV4(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackV7(String _sessionId, GetCLCallbackV7 params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_V7);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallback(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackByTimeV8(String _sessionId, GetCallbackByTimeParamsV8 params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_BY_TIME_V8);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackByTimeV8(config, params);
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


    public DBResponse<List<CLCallback>> getCallbackByTime(String _sessionId, GetCallbackByTimeParams params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_BY_TIME);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackByTime(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackByTimeV2(String _sessionId, GetCallbackByTimeV2 params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_BY_TIME_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackByTime(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackByTimeV7(String _sessionId, GetCallbackByTimeV7 params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_BY_TIME_V7);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackByTime(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackAll(String _sessionId, GetCallbackAll params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_ALL);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackAll(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackAllV3(String _sessionId, GetCallbackAllV3 params) {

        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_ALL_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackAllV3(config, params);
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

    public DBResponse<List<GetOrDepartmentResp>> getOrDepartment(String _sessionId, GetOrDepartment params) {

        DBResponse<List<GetOrDepartmentResp>> response;
        try {
            final DBConfig config = getConfig(GET_OR_DEPARTMENT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrDepartmentResp>>> memTask = new MemFutureTask<DBResponse<List<GetOrDepartmentResp>>>() {

                @Override
                public DBResponse<List<GetOrDepartmentResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrDepartmentResp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrDepartmentResp>>>() {
                @Override
                public DBResponse<List<GetOrDepartmentResp>> get() {
                    return clCallbackDao.dbGetOrDepartment(config, params);
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
    public DBResponse<List<CLCallback>> getCallbackOrderByLeadStatus(String _sessionId, GetCallbackByTimeParams params) {
    	  DBResponse<List<CLCallback>> response;
          try {
              final DBConfig config = getConfig(GET_CALLBACK_ORDER_BY_LEAD_STATUS);
              AppUtils.printInput(logger, _sessionId, config, null, params);

              FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                  @Override
                  public DBResponse<List<CLCallback>> get() {
                      return null;
                  }
              };

              FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                  @Override
                  public DBResponse<List<CLCallback>> get() {
                      return clCallbackDao.dbGetCallbackByTime(config, params);
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

    @SuppressWarnings("unchecked")
    public DBResponse<List<CLCallback>> snapStatusCallbacksMulti(String _sessionId, LeadStatusCallbackWithTimeRequestDTO params) {
        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_STATUS_MULTI_CAMPAIGNS);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbSnapStatusCallbacksMulti(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackByFirstCreateDate(String _sessionId, GetCallbackByFirstCreateDate params) {
        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_BY_FIRST_CREATE_DATE);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackByFirstCreateDate(config, params);
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

    public DBResponse<List<CLCallback>> getCallbackByLeadIds(String _sessionId, GetCallbackByLeadIds params) {
        DBResponse<List<CLCallback>> response;
        try {
            final DBConfig config = getConfig(GET_CALLBACK_BY_LEAD_IDS);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLCallback>>> memTask = new MemFutureTask<DBResponse<List<CLCallback>>>() {

                @Override
                public DBResponse<List<CLCallback>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLCallback>>> dbTask = new DBFutureTask<DBResponse<List<CLCallback>>>() {
                @Override
                public DBResponse<List<CLCallback>> get() {
                    return clCallbackDao.dbGetCallbackByLeadIds(config, params);
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
