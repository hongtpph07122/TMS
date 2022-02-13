package com.tms.service.impl;

import com.tms.api.request.CampaignRequestDTO;
import com.tms.api.response.CampaignResponseDTO;
import com.tms.config.DBConfig;
import com.tms.dao.impl.CLFreshDao;
import com.tms.dto.*;
import com.tms.entity.CLFresh;
import com.tms.entity.CPCampaign;
import com.tms.entity.FreshLead;
import com.tms.exception.ConfigNotFoundException;
import com.tms.model.Request.LeadParamsRequestDTO;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CLFreshService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(CLFreshService.class);

    private final static String GET_FRESH_LEAD = "get_freshlead";
    private final static String GET_FRESH_LEAD_V2 = "get_freshlead_v2";
    private final static String GET_FRESH_LEAD_V3 = "get_freshlead_v3";
    private final static String GET_FRESH_LEAD_V4 = "get_freshlead_v4";
    private final static String GET_LEAD = "get_lead";
    private final static String GET_LEAD_V3 = "get_lead_v3";
    private final static String GET_LEAD_V4 = "get_lead_v4";
    public  final static String GET_LEAD_MULTI_CAMPAIGNS = "get_lead_multi";
    private final static String GET_LEAD_V5 = "get_lead_v5";
    private final static String GET_LEAD_V7 = "get_lead_v7";
    private final static String GET_LEAD_V8 = "get_lead_v8";
    private final static String GET_LEAD_V9 = "get_lead_v9";
    private final static String GET_LEAD_V10 = "get_lead_v10";
    private final static String GET_LEAD_V11 = "get_lead_v11";
    private final static String GET_LEAD_V12 = "get_lead_v12";
    private final static String GET_LEAD_V13 = "get_lead_v13";
    private final static String GET_LEAD_V14 = "get_lead_v14";
    private final static String GET_LEAD_AGENCY = "get_lead_agency";
    private final static String GET_LEAD_CALLBACK = "get_lead_callback";
    private final static String GET_NEWEST_LEAD = "get_newest_lead";
    private final static String GET_NEWEST_LEAD_V2 = "get_newest_lead_v2";
    private final static String GET_NEWEST_LEAD_V3 = "get_newest_lead_v3";
    private final static String GET_NEWEST_LEAD_V4 = "get_newest_lead_v4";
    private final static String GET_NEWEST_LEAD_V5 = "get_newest_lead_v5";
    private final static String GET_NEWEST_LEAD_V7 = "get_newest_lead_v7";
    private final static String GET_UNCALL_LEAD = "get_uncall_lead";
    private final static String GET_UNCALL_LEAD_V2 = "get_uncall_lead_v2";
    private final static String GET_UNCALL_LEAD_V3 = "get_uncall_lead_v3";
    private final static String GET_UNCALL_LEAD_LIFO_02 = "get_uncall_lead_LIFO_02";
    private final static String GET_UNCALL_LEAD_ME = "get_uncall_lead_me";

    private final static String GET_FRESH_LEAD_BY_NUMBER = "getfreshleadbynumber";
    private final static String GET_FRESH_LEAD_COUNT = "getfreshleadcount";
    private final static String GET_CAMPAIGN = "get_campaign";
    private final static String GET_CAMPAIGN_V1 = "get_campaign_v1";
    private final static String GET_CAMPAIGN_V2 = "get_campaign_v2";
    private final static String GET_CAMPAIGN_AGENT = "get_campaign_agent";
    private final static String GET_CAMPAIGN_CONFIG = "get_capaign_config";
    private final static String GET_CALLING_LIST = "get_calling_list";
    private final static String GET_CALLING_LIST_V2 = "get_calling_list_v2";
    private final static String GET_CP_CALLING_LIST = "get_cp_callinglist";
    private final static String GET_CP_CALLING_LIST_V3 = "get_cp_callinglist_v3";
    private final static String GET_ORGANIZATION = "get_organization";
    private final static String GET_LOG_SO = "get_log_so";
    private final static String GET_LOG_DO = "get_log_do";

    private final static String GET_CAMPAIGN_PROGRESS = "get_campaign_progress";

    private final static String GET_VALIDATION = "get_validation";
    private final static String GET_VALIDATION_V2 = "get_validation_v2";
    private final static String GET_VALIDATION_V3 = "get_validation_v3";
    private final static String GET_VALIDATION_V4 = "get_validation_v4";
    private final static String GET_VALIDATION_V5 = "get_validation_v5";

    private final static String GET_ORDER_MANAGEMENT = "get_order_management";
    private final static String GET_ORDER_MANAGEMENT_V2 = "get_order_management_v2";
    private final static String GET_ORDER_MANAGEMENT_V3 = "get_order_management_v3";
    private final static String GET_ORDER_MANAGEMENT_V4 = "get_order_management_v4";
    private final static String GET_ORDER_MANAGEMENT_V5 = "get_order_management_v5";
    private final static String GET_ORDER_MANAGEMENT_V6 = "get_order_management_v6";
    private final static String GET_ORDER_DHL = "get_order_dhl";
    private final static String GET_GLOBAL_PARAM = "get_global_parameter";
    private final static String GET_GLOBAL_PARAM_V2 = "get_global_parameter_v2";

    private final static String GET_DASHBOARD_AGENT = "dashboard_agent_performance";
    private final static String GET_DASHBOARD_AVG_GROUP = "dashboard_avggroup_performance";

    private final static String GET_OFFER = "get_offer";

    private final static String GET_SYNONYM = "get_synonym";
    private final static String GET_SYNONYM_V2 = "get_synonym_v2";
    private final static String GET_COMMISSION_DATA = "get_commission_data";
    private final static String GET_COMMISSION_DATA_V2 = "get_commission_data_v2";
    private final static String GET_COLLECTION_DATA = "get_collection_data";

    private final static String GET_RESERVED_LEAD = "get_reserved_lead";
    private final static String GET_RESERVE_NEW_LEAD = "get_reserved_new_lead";

    @Autowired
    private CLFreshDao clFreshDao;

    public DBResponse<List<CLFresh>> getFreshLead(String _sessionId, GetFreshLeadParams params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_FRESH_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetFreshLead(config, params);
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

    public DBResponse<List<CLFresh>> getFreshLeadV2(String _sessionId, GetFreshLeadV2 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_FRESH_LEAD_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetFreshLeadV2(config, params);
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

    public DBResponse<List<CLFresh>> getFreshLeadV3(String _sessionId, GetFreshLeadV3 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_FRESH_LEAD_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetFreshLeadV3(config, params);
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

    public DBResponse<List<CLFresh>> getLead(String _sessionId, GetLeadParams params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV3(String _sessionId, GetLeadParamsV3 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLeadV3(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV4(String _sessionId, GetLeadParamsV4 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V4);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLeadV4(config, params);
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
    public DBResponse<List<CLFresh>> snapCluesMultiCampaigns(String _sessionId, LeadParamsRequestDTO params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_MULTI_CAMPAIGNS);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbSnapCluesMultiCampaigns(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV11(String _sessionId, GetLeadParamsV11 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V11);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLeadV11(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV12(String _sessionId, GetLeadParamsV12 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V12);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLeadV12(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV5(String _sessionId, GetLeadParamsV5 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V5);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV7(String _sessionId, GetLeadParamsV7 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V7);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV8(String _sessionId, GetLeadParamsV8 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V8);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

    public DBResponse<List<CLFresh>> getLeadCallback(String _sessionId, GetLeadParamsV8 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_CALLBACK);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

    public DBResponse<List<GetLeadAgencyResp>> getLeadAgency(String _sessionId, GetLeadAgencyParams params) {
        DBResponse<List<GetLeadAgencyResp>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_AGENCY);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetLeadAgencyResp>>> memTask = new MemFutureTask<DBResponse<List<GetLeadAgencyResp>>>() {

                @Override
                public DBResponse<List<GetLeadAgencyResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetLeadAgencyResp>>> dbTask = new DBFutureTask<DBResponse<List<GetLeadAgencyResp>>>() {
                @Override
                public DBResponse<List<GetLeadAgencyResp>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

    public DBResponse<List<CLFresh>> getNewestLead(String _sessionId, GetNewestLeadParams params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_NEWEST_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetNewestLead(config, params);
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

    public DBResponse<List<CLFresh>> getNewestLeadV2(String _sessionId, GetNewestLeadV2 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_NEWEST_LEAD_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetNewestLeadV2(config, params);
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

    public DBResponse<List<CLFresh>> getNewestLeadV3(String _sessionId, GetNewestLeadV3 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_NEWEST_LEAD_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetNewestLeadV3(config, params);
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

    public DBResponse<List<CLFresh>> getNewestLeadV4(String _sessionId, GetNewestLeadV4 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_NEWEST_LEAD_V4);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetNewestLead(config, params);
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

    public DBResponse<List<CLFresh>> getNewestLeadV5(String _sessionId, GetNewestLeadV5 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_NEWEST_LEAD_V5);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetNewestLead(config, params);
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

    public DBResponse<List<CLFresh>> getNewestLeadV7(String _sessionId, GetNewestLeadV7 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_NEWEST_LEAD_V7);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetNewestLead(config, params);
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

    public DBResponse<List<CLFresh>> getUncallLead(String _sessionId, GetUncallLead params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_UNCALL_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetUncallLead(config, params);
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

    public DBResponse<List<CLFresh>> getUncallLeadV2(String _sessionId, GetUncallLeadV2 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_UNCALL_LEAD_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetUncallLead(config, params);
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
    public DBResponse<List<CLFresh>> snapUnCalls(String _sessionId, GetUncallLeadV2 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_UNCALL_LEAD_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetUncallLead(config, params);
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

    public DBResponse<List<CLFresh>> getUncallLeadLIFO(String _sessionId, GetUncallLeadLifo params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_UNCALL_LEAD_LIFO_02);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetUncallLead(config, params);
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

    public DBResponse<List<CLFresh>> getUncallLeadMe(String sessionId, GetUncallMe params) {
        return dbGet(sessionId, GET_UNCALL_LEAD_ME, params, CLFresh.class);
    }

    public DBResponse<List<FreshLead>> getFreshLeadByNumber(String _sessionId, Integer params) {
        DBResponse<List<FreshLead>> response;
        try {
            final DBConfig config = getConfig(GET_FRESH_LEAD_BY_NUMBER);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<FreshLead>>> memTask = new MemFutureTask<DBResponse<List<FreshLead>>>() {

                @Override
                public DBResponse<List<FreshLead>> get() {
                    return clFreshDao.memGetFreshLeadByNumber(config, params);
                }
            };

            FutureTask<DBResponse<List<FreshLead>>> dbTask = new DBFutureTask<DBResponse<List<FreshLead>>>() {
                @Override
                public DBResponse<List<FreshLead>> get() {
                    return clFreshDao.dbGetFreshLeadByNumber(config, params);
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

    public DBResponse<Long> getFreshLeadCount(String _sessionId, Integer campainId) {
        DBResponse<Long> response;
        try {
            final DBConfig config = getConfig(GET_FRESH_LEAD_COUNT);
            AppUtils.printInput(logger, _sessionId, config, null, campainId);

            FutureTask<DBResponse<Long>> memTask = new MemFutureTask<DBResponse<Long>>() {

                @Override
                public DBResponse<Long> get() {
                    return clFreshDao.memGetFreshCount(config, campainId);
                }
            };

            FutureTask<DBResponse<Long>> dbTask = new DBFutureTask<DBResponse<Long>>() {
                @Override
                public DBResponse<Long> get() {
                    return clFreshDao.dbGetFreshCount(config, campainId);
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

    public DBResponse<List<CPCampaign>> getCampaign(String _sessionId, GetCampaign campaign) {

        DBResponse<List<CPCampaign>> response;
        try {
            final DBConfig config = getConfig(GET_CAMPAIGN);
            AppUtils.printInput(logger, _sessionId, config, null, campaign);

            FutureTask<DBResponse<List<CPCampaign>>> memTask = new MemFutureTask<DBResponse<List<CPCampaign>>>() {

                @Override
                public DBResponse<List<CPCampaign>> get() {
                    return clFreshDao.memGetCampaign(config, campaign);
                }
            };

            FutureTask<DBResponse<List<CPCampaign>>> dbTask = new DBFutureTask<DBResponse<List<CPCampaign>>>() {
                @Override
                public DBResponse<List<CPCampaign>> get() {
                    return clFreshDao.dbGetCampaign(config, campaign);
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

    public DBResponse<List<GetCampaignRespV2>> getCampaignV2(String _sessionId, GetCampaignParamsV2 params) {

        DBResponse<List<GetCampaignRespV2>> response;
        try {
            final DBConfig config = getConfig(GET_CAMPAIGN_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCampaignRespV2>>> memTask = new MemFutureTask<DBResponse<List<GetCampaignRespV2>>>() {

                @Override
                public DBResponse<List<GetCampaignRespV2>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCampaignRespV2>>> dbTask = new DBFutureTask<DBResponse<List<GetCampaignRespV2>>>() {
                @Override
                public DBResponse<List<GetCampaignRespV2>> get() {
                    return clFreshDao.dbGetCampaignV2(config, params);
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

    public DBResponse<List<CampaignResponseDTO>> snagCampaigns(String sessionId, CampaignRequestDTO campaign) {

        DBResponse<List<CampaignResponseDTO>> response;
        try {
            final DBConfig config = getConfig(GET_CAMPAIGN_V1);
            AppUtils.printInput(logger, sessionId, config, null, campaign);

            FutureTask<DBResponse<List<CampaignResponseDTO>>> memTask = new MemFutureTask<DBResponse<List<CampaignResponseDTO>>>() {

                @Override
                public DBResponse<List<CampaignResponseDTO>> get() {
                    return clFreshDao.memGetCampaign(config, campaign);
                }
            };

            FutureTask<DBResponse<List<CampaignResponseDTO>>> dbTask = new DBFutureTask<DBResponse<List<CampaignResponseDTO>>>() {
                @Override
                public DBResponse<List<CampaignResponseDTO>> get() {
                    return clFreshDao.dbGetCampaign(config, campaign);
                }
            };
            response = query(sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<GetCampaignAgentResp>> getCampaignAgent(String _sessionId, GetCampaignAgent campaign) {

        DBResponse<List<GetCampaignAgentResp>> response;
        try {
            final DBConfig config = getConfig(GET_CAMPAIGN_AGENT);
            AppUtils.printInput(logger, _sessionId, config, null, campaign);

            FutureTask<DBResponse<List<GetCampaignAgentResp>>> memTask = new MemFutureTask<DBResponse<List<GetCampaignAgentResp>>>() {

                @Override
                public DBResponse<List<GetCampaignAgentResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCampaignAgentResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCampaignAgentResp>>>() {
                @Override
                public DBResponse<List<GetCampaignAgentResp>> get() {
                    return clFreshDao.dbGetCampaignAgent(config, campaign);
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

    public DBResponse<List<CPCampaignResp>> getCampaignConfig(String _sessionId, CPCampaignParams params) {

        DBResponse<List<CPCampaignResp>> response;
        try {
            final DBConfig config = getConfig(GET_CAMPAIGN_CONFIG);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CPCampaignResp>>> memTask = new MemFutureTask<DBResponse<List<CPCampaignResp>>>() {

                @Override
                public DBResponse<List<CPCampaignResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CPCampaignResp>>> dbTask = new DBFutureTask<DBResponse<List<CPCampaignResp>>>() {
                @Override
                public DBResponse<List<CPCampaignResp>> get() {
                    return clFreshDao.dbGetCampaignConfig(config, params);
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

    public DBResponse<List<GetCallingListResp>> getCallingList(String _sessionId, GetCallingList params) {

        DBResponse<List<GetCallingListResp>> response;
        try {
            final DBConfig config = getConfig(GET_CALLING_LIST);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCallingListResp>>> memTask = new MemFutureTask<DBResponse<List<GetCallingListResp>>>() {

                @Override
                public DBResponse<List<GetCallingListResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCallingListResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCallingListResp>>>() {
                @Override
                public DBResponse<List<GetCallingListResp>> get() {
                    return clFreshDao.dbGetCallingList(config, params);
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

    public DBResponse<List<GetCallingListResp>> getCallingListV2(String _sessionId, GetCallingListV2 params) {

        DBResponse<List<GetCallingListResp>> response;
        try {
            final DBConfig config = getConfig(GET_CALLING_LIST_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCallingListResp>>> memTask = new MemFutureTask<DBResponse<List<GetCallingListResp>>>() {

                @Override
                public DBResponse<List<GetCallingListResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCallingListResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCallingListResp>>>() {
                @Override
                public DBResponse<List<GetCallingListResp>> get() {
                    return clFreshDao.dbGetCallingList(config, params);
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

    public DBResponse<List<GetCpCallingListResp>> getCpCallingList(String _sessionId, GetCpCallingList params) {

        DBResponse<List<GetCpCallingListResp>> response;
        try {
            final DBConfig config = getConfig(GET_CP_CALLING_LIST);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCpCallingListResp>>> memTask = new MemFutureTask<DBResponse<List<GetCpCallingListResp>>>() {

                @Override
                public DBResponse<List<GetCpCallingListResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCpCallingListResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCpCallingListResp>>>() {
                @Override
                public DBResponse<List<GetCpCallingListResp>> get() {
                    return clFreshDao.dbCpCallingList(config, params);
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

    public DBResponse<List<GetCpCallingListResp>> getCpCallingListV3(String _sessionId, GetCpCallingListV3 params) {

        DBResponse<List<GetCpCallingListResp>> response;
        try {
            final DBConfig config = getConfig(GET_CP_CALLING_LIST_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCpCallingListResp>>> memTask = new MemFutureTask<DBResponse<List<GetCpCallingListResp>>>() {

                @Override
                public DBResponse<List<GetCpCallingListResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCpCallingListResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCpCallingListResp>>>() {
                @Override
                public DBResponse<List<GetCpCallingListResp>> get() {
                    return clFreshDao.dbCpCallingList(config, params);
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

    public DBResponse<List<GetOrganizationResp>> getOrganization(String _sessionId, GetOrganization params) {

        DBResponse<List<GetOrganizationResp>> response;
        try {
            final DBConfig config = getConfig(GET_ORGANIZATION);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrganizationResp>>> memTask = new MemFutureTask<DBResponse<List<GetOrganizationResp>>>() {

                @Override
                public DBResponse<List<GetOrganizationResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrganizationResp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrganizationResp>>>() {
                @Override
                public DBResponse<List<GetOrganizationResp>> get() {
                    return clFreshDao.dbOrganization(config, params);
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

    public DBResponse<List<GetLogSoResp>> getLogSo(String _sessionId, GetLogSo params) {

        DBResponse<List<GetLogSoResp>> response;
        try {
            final DBConfig config = getConfig(GET_LOG_SO);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetLogSoResp>>> memTask = new MemFutureTask<DBResponse<List<GetLogSoResp>>>() {

                @Override
                public DBResponse<List<GetLogSoResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetLogSoResp>>> dbTask = new DBFutureTask<DBResponse<List<GetLogSoResp>>>() {
                @Override
                public DBResponse<List<GetLogSoResp>> get() {
                    return clFreshDao.dbGetLogSo(config, params);
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

    public DBResponse<List<GetLogDoResp>> getLogDo(String _sessionId, GetLogDo params) {

        DBResponse<List<GetLogDoResp>> response;
        try {
            final DBConfig config = getConfig(GET_LOG_DO);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetLogDoResp>>> memTask = new MemFutureTask<DBResponse<List<GetLogDoResp>>>() {

                @Override
                public DBResponse<List<GetLogDoResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetLogDoResp>>> dbTask = new DBFutureTask<DBResponse<List<GetLogDoResp>>>() {
                @Override
                public DBResponse<List<GetLogDoResp>> get() {
                    return clFreshDao.dbGetLogDo(config, params);
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

    public DBResponse<List<GetCampaignProgressResp>> getCampaignProgress(String _sessionId,
                                                                         GetCampaignProgress campaign) {

        DBResponse<List<GetCampaignProgressResp>> response;
        try {
            final DBConfig config = getConfig(GET_CAMPAIGN_PROGRESS);
            AppUtils.printInput(logger, _sessionId, config, null, campaign);

            FutureTask<DBResponse<List<GetCampaignProgressResp>>> memTask = new MemFutureTask<DBResponse<List<GetCampaignProgressResp>>>() {

                @Override
                public DBResponse<List<GetCampaignProgressResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCampaignProgressResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCampaignProgressResp>>>() {
                @Override
                public DBResponse<List<GetCampaignProgressResp>> get() {
                    return clFreshDao.dbGetCampaignProgress(config, campaign);
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

    public DBResponse<List<GetValidationResp>> getValidation(String _sessionId, GetValidation params) {
        DBResponse<List<GetValidationResp>> response;
        try {
            final DBConfig config = getConfig(GET_VALIDATION);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetValidationResp>>> memTask = new MemFutureTask<DBResponse<List<GetValidationResp>>>() {

                @Override
                public DBResponse<List<GetValidationResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetValidationResp>>> dbTask = new DBFutureTask<DBResponse<List<GetValidationResp>>>() {
                @Override
                public DBResponse<List<GetValidationResp>> get() {
                    return clFreshDao.dbGetValidation(config, params);
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

    public DBResponse<List<GetValidation2Resp>> getValidationV2(String _sessionId, GetValidation2 params) {
        DBResponse<List<GetValidation2Resp>> response;
        try {
            final DBConfig config = getConfig(GET_VALIDATION_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetValidation2Resp>>> memTask = new MemFutureTask<DBResponse<List<GetValidation2Resp>>>() {

                @Override
                public DBResponse<List<GetValidation2Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetValidation2Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetValidation2Resp>>>() {
                @Override
                public DBResponse<List<GetValidation2Resp>> get() {
                    return clFreshDao.dbGetValidation2(config, params);
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

    public DBResponse<List<GetValidation3Resp>> getValidationV3(String _sessionId, GetValidation3 params) {
        DBResponse<List<GetValidation3Resp>> response;
        try {
            final DBConfig config = getConfig(GET_VALIDATION_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetValidation3Resp>>> memTask = new MemFutureTask<DBResponse<List<GetValidation3Resp>>>() {

                @Override
                public DBResponse<List<GetValidation3Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetValidation3Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetValidation3Resp>>>() {
                @Override
                public DBResponse<List<GetValidation3Resp>> get() {
                    return clFreshDao.dbGetValidation3(config, params);
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

    public DBResponse<List<GetValidation4Resp>> getValidationV4(String _sessionId, GetValidation4 params) {
        DBResponse<List<GetValidation4Resp>> response;
        try {
            final DBConfig config = getConfig(GET_VALIDATION_V4);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetValidation4Resp>>> memTask = new MemFutureTask<DBResponse<List<GetValidation4Resp>>>() {

                @Override
                public DBResponse<List<GetValidation4Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetValidation4Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetValidation4Resp>>>() {
                @Override
                public DBResponse<List<GetValidation4Resp>> get() {
                    return clFreshDao.dbGetValidation4(config, params);
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

    public DBResponse<List<GetValidation4Resp>> getValidationV5(String _sessionId, GetValidation5 params) {
        DBResponse<List<GetValidation4Resp>> response;
        try {
            final DBConfig config = getConfig(GET_VALIDATION_V5);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetValidation4Resp>>> memTask = new MemFutureTask<DBResponse<List<GetValidation4Resp>>>() {

                @Override
                public DBResponse<List<GetValidation4Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetValidation4Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetValidation4Resp>>>() {
                @Override
                public DBResponse<List<GetValidation4Resp>> get() {
                    return clFreshDao.dbGetValidation(config, params);
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

    public DBResponse<List<GetOrderManagementResp>> getOrderManagement(String _sessionId, GetOrderManagement params) {
        DBResponse<List<GetOrderManagementResp>> response;
        try {
            final DBConfig config = getConfig(GET_ORDER_MANAGEMENT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrderManagementResp>>> memTask = new MemFutureTask<DBResponse<List<GetOrderManagementResp>>>() {

                @Override
                public DBResponse<List<GetOrderManagementResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrderManagementResp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrderManagementResp>>>() {
                @Override
                public DBResponse<List<GetOrderManagementResp>> get() {
                    return clFreshDao.dbOrderManagement(config, params);
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

    public DBResponse<List<GetOrderManagement2Resp>> getOrderManagementV2(String _sessionId,
                                                                          GetOrderManagement2 params) {
        DBResponse<List<GetOrderManagement2Resp>> response;
        try {
            final DBConfig config = getConfig(GET_ORDER_MANAGEMENT_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrderManagement2Resp>>> memTask = new MemFutureTask<DBResponse<List<GetOrderManagement2Resp>>>() {

                @Override
                public DBResponse<List<GetOrderManagement2Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrderManagement2Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrderManagement2Resp>>>() {
                @Override
                public DBResponse<List<GetOrderManagement2Resp>> get() {
                    return clFreshDao.dbOrderManagement2(config, params);
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

    public DBResponse<List<GetOrderManagement3Resp>> getOrderManagementV3(String _sessionId,
                                                                          GetOrderManagement3 params) {
        DBResponse<List<GetOrderManagement3Resp>> response;
        try {
            final DBConfig config = getConfig(GET_ORDER_MANAGEMENT_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrderManagement3Resp>>> memTask = new MemFutureTask<DBResponse<List<GetOrderManagement3Resp>>>() {

                @Override
                public DBResponse<List<GetOrderManagement3Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrderManagement3Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrderManagement3Resp>>>() {
                @Override
                public DBResponse<List<GetOrderManagement3Resp>> get() {
                    return clFreshDao.dbOrderManagement3(config, params);
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

    public DBResponse<List<GetOrderManagement4Resp>> getOrderManagementV4(String _sessionId,
                                                                          GetOrderManagement4 params) {
        DBResponse<List<GetOrderManagement4Resp>> response;
        try {
            final DBConfig config = getConfig(GET_ORDER_MANAGEMENT_V4);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrderManagement4Resp>>> memTask = new MemFutureTask<DBResponse<List<GetOrderManagement4Resp>>>() {

                @Override
                public DBResponse<List<GetOrderManagement4Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrderManagement4Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrderManagement4Resp>>>() {
                @Override
                public DBResponse<List<GetOrderManagement4Resp>> get() {
                    return clFreshDao.dbOrderManagement4(config, params);
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

    public DBResponse<List<GetOrderManagement5Resp>> getOrderManagementV5(String _sessionId,
                                                                          GetOrderManagement5 params) {
        DBResponse<List<GetOrderManagement5Resp>> response;
        try {
            final DBConfig config = getConfig(GET_ORDER_MANAGEMENT_V5);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrderManagement5Resp>>> memTask = new MemFutureTask<DBResponse<List<GetOrderManagement5Resp>>>() {

                @Override
                public DBResponse<List<GetOrderManagement5Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrderManagement5Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrderManagement5Resp>>>() {
                @Override
                public DBResponse<List<GetOrderManagement5Resp>> get() {
                    return clFreshDao.dbOrderManagement5(config, params);
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

    public DBResponse<List<GetOrderManagement6Resp>> getOrderManagementV6(String _sessionId,
                                                                          GetOrderManagement6 params) {
        DBResponse<List<GetOrderManagement6Resp>> response;
        try {
            final DBConfig config = getConfig(GET_ORDER_MANAGEMENT_V6);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrderManagement6Resp>>> memTask = new MemFutureTask<DBResponse<List<GetOrderManagement6Resp>>>() {

                @Override
                public DBResponse<List<GetOrderManagement6Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrderManagement6Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrderManagement6Resp>>>() {
                @Override
                public DBResponse<List<GetOrderManagement6Resp>> get() {
                    return clFreshDao.dbOrderManagement(config, params);
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

    public DBResponse<List<GetOrderDHLResp>> getOrderDHL(String _sessionId, GetOrderDHL params) {
        DBResponse<List<GetOrderDHLResp>> response;
        try {
            final DBConfig config = getConfig(GET_ORDER_DHL);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrderDHLResp>>> memTask = new MemFutureTask<DBResponse<List<GetOrderDHLResp>>>() {

                @Override
                public DBResponse<List<GetOrderDHLResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrderDHLResp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrderDHLResp>>>() {
                @Override
                public DBResponse<List<GetOrderDHLResp>> get() {
                    return clFreshDao.dbOrderDHL(config, params);
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

    public DBResponse<List<GetGlobalParamResp>> getGlobalParam(String _sessionId, GetGlobalParam params) {
        DBResponse<List<GetGlobalParamResp>> response;
        try {
            final DBConfig config = getConfig(GET_GLOBAL_PARAM);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetGlobalParamResp>>> memTask = new MemFutureTask<DBResponse<List<GetGlobalParamResp>>>() {

                @Override
                public DBResponse<List<GetGlobalParamResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetGlobalParamResp>>> dbTask = new DBFutureTask<DBResponse<List<GetGlobalParamResp>>>() {
                @Override
                public DBResponse<List<GetGlobalParamResp>> get() {
                    return clFreshDao.dbGlobalParam(config, params);
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

    public DBResponse<List<GetGlobalParamResp>> getGlobalParamV2(String _sessionId, GetGlobalParamV2 params) {
        DBResponse<List<GetGlobalParamResp>> response;
        try {
            final DBConfig config = getConfig(GET_GLOBAL_PARAM_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetGlobalParamResp>>> memTask = new MemFutureTask<DBResponse<List<GetGlobalParamResp>>>() {

                @Override
                public DBResponse<List<GetGlobalParamResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetGlobalParamResp>>> dbTask = new DBFutureTask<DBResponse<List<GetGlobalParamResp>>>() {
                @Override
                public DBResponse<List<GetGlobalParamResp>> get() {
                    return clFreshDao.dbGlobalParamV2(config, params);
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

    public DBResponse<List<GetDashboardAgentResp>> getDashboardAgent(String _sessionId, GetDashboardAgent params) {
        DBResponse<List<GetDashboardAgentResp>> response;
        try {
            final DBConfig config = getConfig(GET_DASHBOARD_AGENT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDashboardAgentResp>>> memTask = new MemFutureTask<DBResponse<List<GetDashboardAgentResp>>>() {

                @Override
                public DBResponse<List<GetDashboardAgentResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDashboardAgentResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDashboardAgentResp>>>() {
                @Override
                public DBResponse<List<GetDashboardAgentResp>> get() {
                    return clFreshDao.dbGetDashboardAgent(config, params);
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

    public DBResponse<List<GetDashboardAvggoupResp>> getDashboardAvgGroup(String _sessionId,
                                                                          GetDashboardAvggroup params) {
        DBResponse<List<GetDashboardAvggoupResp>> response;
        try {
            final DBConfig config = getConfig(GET_DASHBOARD_AVG_GROUP);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDashboardAvggoupResp>>> memTask = new MemFutureTask<DBResponse<List<GetDashboardAvggoupResp>>>() {

                @Override
                public DBResponse<List<GetDashboardAvggoupResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDashboardAvggoupResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDashboardAvggoupResp>>>() {
                @Override
                public DBResponse<List<GetDashboardAvggoupResp>> get() {
                    return clFreshDao.dbGetDashboardAvgGroup(config, params);
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

    public DBResponse<List<GetOfferResp>> getOffer(String _sessionId, GetOffer params) {
        DBResponse<List<GetOfferResp>> response;
        try {
            final DBConfig config = getConfig(GET_OFFER);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOfferResp>>> memTask = new MemFutureTask<DBResponse<List<GetOfferResp>>>() {

                @Override
                public DBResponse<List<GetOfferResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOfferResp>>> dbTask = new DBFutureTask<DBResponse<List<GetOfferResp>>>() {
                @Override
                public DBResponse<List<GetOfferResp>> get() {
                    return clFreshDao.dbGetOffer(config, params);
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

    public DBResponse<List<GetSynonymResp>> getSynonym(String _sessionId, GetSynonym params) {
        DBResponse<List<GetSynonymResp>> response;
        try {
            final DBConfig config = getConfig(GET_SYNONYM);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetSynonymResp>>> memTask = new MemFutureTask<DBResponse<List<GetSynonymResp>>>() {

                @Override
                public DBResponse<List<GetSynonymResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetSynonymResp>>> dbTask = new DBFutureTask<DBResponse<List<GetSynonymResp>>>() {
                @Override
                public DBResponse<List<GetSynonymResp>> get() {
                    return clFreshDao.dbGetSynonym(config, params);
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

    public DBResponse<List<GetSynonymRespV2>> getSynonymV2(String _sessionId, GetSynonymV2 params) {
        DBResponse<List<GetSynonymRespV2>> response;
        try {
            final DBConfig config = getConfig(GET_SYNONYM_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetSynonymRespV2>>> memTask = new MemFutureTask<DBResponse<List<GetSynonymRespV2>>>() {

                @Override
                public DBResponse<List<GetSynonymRespV2>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetSynonymRespV2>>> dbTask = new DBFutureTask<DBResponse<List<GetSynonymRespV2>>>() {
                @Override
                public DBResponse<List<GetSynonymRespV2>> get() {
                    return clFreshDao.dbGetSynonymV2(config, params);
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

    public DBResponse<List<GetCommissionDataResp>> getCommissionData(String _sessionId, GetCommissionData params) {
        DBResponse<List<GetCommissionDataResp>> response;
        try {
            final DBConfig config = getConfig(GET_COMMISSION_DATA);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCommissionDataResp>>> memTask = new MemFutureTask<DBResponse<List<GetCommissionDataResp>>>() {

                @Override
                public DBResponse<List<GetCommissionDataResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCommissionDataResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCommissionDataResp>>>() {
                @Override
                public DBResponse<List<GetCommissionDataResp>> get() {
                    return clFreshDao.dbGetCommissionData(config, params);
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

    public DBResponse<List<GetCommissionDataV2Resp>> getCommissionDataV2(String _sessionId, GetCommissionData params) {
        DBResponse<List<GetCommissionDataV2Resp>> response;
        try {
            final DBConfig config = getConfig(GET_COMMISSION_DATA_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCommissionDataV2Resp>>> memTask = new MemFutureTask<DBResponse<List<GetCommissionDataV2Resp>>>() {

                @Override
                public DBResponse<List<GetCommissionDataV2Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCommissionDataV2Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetCommissionDataV2Resp>>>() {
                @Override
                public DBResponse<List<GetCommissionDataV2Resp>> get() {
                    return clFreshDao.dbGetCommissionDataV2(config, params);
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

    public DBResponse<List<GetCollectionDataResp>> getCollectionData(String _sessionId, GetCollectionData params) {
        DBResponse<List<GetCollectionDataResp>> response;
        try {
            final DBConfig config = getConfig(GET_COLLECTION_DATA);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCollectionDataResp>>> memTask = new MemFutureTask<DBResponse<List<GetCollectionDataResp>>>() {

                @Override
                public DBResponse<List<GetCollectionDataResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCollectionDataResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCollectionDataResp>>>() {
                @Override
                public DBResponse<List<GetCollectionDataResp>> get() {
                    return clFreshDao.dbGetCollectionData(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV9(String _sessionId, GetLeadParamsV9 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V9);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

	public DBResponse<List<CLFresh>> getLeadV10(String _sessionId, GetLeadParamsV10 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V10);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV13(String _sessionId, GetLeadParamsV10 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_LEAD_V13);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetLead(config, params);
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

    public DBResponse<List<CLFresh>> getLeadV14(String sessionId, GetLeadParamsV10 params) {
        return dbGet(sessionId, GET_LEAD_V14, params, CLFresh.class);
    }

	public DBResponse<List<CLFresh>> getFreshLeadV4(String _sessionId, GetFreshLeadV4 params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_FRESH_LEAD_V4);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetFreshLead(config, params);
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

    public DBResponse<List<CLFresh>> getReservedLead(String _sessionId, GetLeadReservedParam params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_RESERVED_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetReservedLead(config, params);
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

    public DBResponse<List<CLFresh>> getReserveNewLead(String _sessionId, GetLeadReservedParam params) {
        DBResponse<List<CLFresh>> response;
        try {
            final DBConfig config = getConfig(GET_RESERVE_NEW_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<CLFresh>>> memTask = new MemFutureTask<DBResponse<List<CLFresh>>>() {

                @Override
                public DBResponse<List<CLFresh>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLFresh>>> dbTask = new DBFutureTask<DBResponse<List<CLFresh>>>() {
                @Override
                public DBResponse<List<CLFresh>> get() {
                    return clFreshDao.dbGetReserveNewLead(config, params);
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

    public DBResponse<List<GetMktDataResp>> getMktData(String _sessionId, GetMktDataParams params) {
        DBResponse<List<GetMktDataResp>> response;
        try {
            final DBConfig config = getConfig("get_mkt_data");
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetMktDataResp>>> memTask = new MemFutureTask<DBResponse<List<GetMktDataResp>>>() {

                @Override
                public DBResponse<List<GetMktDataResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetMktDataResp>>> dbTask = new DBFutureTask<DBResponse<List<GetMktDataResp>>>() {
                @Override
                public DBResponse<List<GetMktDataResp>> get() {
                    return clFreshDao.dbGetMktData(config, params);
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
