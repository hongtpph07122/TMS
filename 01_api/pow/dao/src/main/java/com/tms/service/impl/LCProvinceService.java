package com.tms.service.impl;

import com.tms.config.DBConfig;
import com.tms.dao.impl.LCProvinceDao;
import com.tms.dto.*;
import com.tms.entity.*;
import com.tms.exception.ConfigNotFoundException;
import com.tms.model.Response.ProvinceWithPartnerResponseDTO;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LCProvinceService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(LCProvinceService.class);

    private final static String GET_PROVINCE = "get_province";
    private final static String GET_PROVINCE_LIST = "getprovincelist";
    private final static String GET_DISTRIBUTION_RULE = "get_distribution_rule";
    private final static String GET_DISTRIBUTION_RULE_V2 = "get_distribution_rule_v2";
    private final static String GET_CP_DISTRIBUTION_RULE = "get_cp_distribution_rule";
    private final static String GET_CP_DISTRIBUTION_RULE_V2 = "get_cp_distribution_rule_v2";
    private final static String GET_CP_DISTRIBUTION_RULE_V7 = "get_cp_distribution_rule_v7";
    private final static String GET_RULE_PARAM = "get_rule_param";
    private final static String GET_GROUP = "get_group";
    private final static String GET_CALL_STRATEGY = "get_call_strategy";
    private final static String GET_CP_CALL_STRATEGY = "get_cp_call_strategy";
    private final static String GET_STRATEGY_PARAM = "get_strategy_param";
    private final static String GET_GROUP_AGENT = "get_group_agent";
    private final static String GET_GROUP_AGENT_V2 = "get_group_agent_v2";
    private final static String GET_SUB_DISTRICT = "get_subdistrict";
    private final static String GET_SUB_DISTRICT_BY_PARTNER = "get_subdistrict_bypartner";
    private final static String GET_PROVINCE_BY_PARTNER = "get_province_bypartner";
    private final static String GET_PROVINCE_BY_PARTNER_V1 = "get_province_bypartner_v1";
    private final static String GET_DISTRICT = "get_district";
    private final static String GET_DISTRICT_BY_PARTNER = "get_district_bypartner";
    private final static String GET_BLACKLIST = "get_blacklist";
    private final static String GET_DNC = "get_dnc";
    private final static String GET_BASKET_LEAD = "get_basket_lead";
    private final static String GET_BASKET_LEAD_V2 = "get_basket_lead_v2";
    private final static String GET_BASKET_LEAD_V3 = "get_basket_lead_v3";
    private final static String GET_BASKET_LEAD_V4 = "get_basket_lead_v4";
    private final static String GET_DISTRI_RULE_PARAM = "get_distri_rule_param";

    private final static String GET_PROVINCE_MAP = "get_province_map";
    private final static String GET_SUB_DISTRICT_MAP = "get_subdistrict_map";
    private final static String GET_DISTRICT_MAP = "get_district_map";


    @Autowired
    private LCProvinceDao lcProvinceDao;


    public DBResponse<List<LCProvince>> getProvince(String _sessionId, GetProvince province) {

        DBResponse<List<LCProvince>> response;
        try {
            final DBConfig config = getConfig(GET_PROVINCE);
            AppUtils.printInput(logger, _sessionId, config, null, province);

            FutureTask<DBResponse<List<LCProvince>>> memTask = new MemFutureTask<DBResponse<List<LCProvince>>>() {

                @Override
                public DBResponse<List<LCProvince>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<LCProvince>>> dbTask = new DBFutureTask<DBResponse<List<LCProvince>>>() {
                @Override
                public DBResponse<List<LCProvince>> get() {
                    return lcProvinceDao.dbGetProvince(config, province);
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

    public DBResponse<List<LCProvince>> getProvinceList(String _sessionId) {

        DBResponse<List<LCProvince>> response;
        try {
            final DBConfig config = getConfig(GET_PROVINCE_LIST);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<LCProvince>>> memTask = new MemFutureTask<DBResponse<List<LCProvince>>>() {

                @Override
                public DBResponse<List<LCProvince>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<LCProvince>>> dbTask = new DBFutureTask<DBResponse<List<LCProvince>>>() {
                @Override
                public DBResponse<List<LCProvince>> get() {
                    return lcProvinceDao.dbGetProvinceList(config);
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

    public DBResponse<List<GetDistributionRuleResp>> getDistributionRule(String _sessionId, GetDistributionRule param) {

        DBResponse<List<GetDistributionRuleResp>> response;
        try {
            final DBConfig config = getConfig(GET_DISTRIBUTION_RULE);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetDistributionRuleResp>>> memTask = new MemFutureTask<DBResponse<List<GetDistributionRuleResp>>>() {

                @Override
                public DBResponse<List<GetDistributionRuleResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDistributionRuleResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDistributionRuleResp>>>() {
                @Override
                public DBResponse<List<GetDistributionRuleResp>> get() {
                    return lcProvinceDao.dbDistributionRule(config, param);
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

    public DBResponse<List<GetDistributionRuleResp>> getDistributionRuleV2(String _sessionId, GetDistributionRuleV2 param) {

        DBResponse<List<GetDistributionRuleResp>> response;
        try {
            final DBConfig config = getConfig(GET_DISTRIBUTION_RULE_V2);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetDistributionRuleResp>>> memTask = new MemFutureTask<DBResponse<List<GetDistributionRuleResp>>>() {

                @Override
                public DBResponse<List<GetDistributionRuleResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDistributionRuleResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDistributionRuleResp>>>() {
                @Override
                public DBResponse<List<GetDistributionRuleResp>> get() {
                    return lcProvinceDao.dbDistributionRuleV2(config, param);
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

    public DBResponse<List<GetCpDistributionRuleResp>> getCpDistributionRule(String _sessionId, GetCpDistributionRuleParams param) {

        DBResponse<List<GetCpDistributionRuleResp>> response;
        try {
            final DBConfig config = getConfig(GET_CP_DISTRIBUTION_RULE);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetCpDistributionRuleResp>>> memTask = new MemFutureTask<DBResponse<List<GetCpDistributionRuleResp>>>() {

                @Override
                public DBResponse<List<GetCpDistributionRuleResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCpDistributionRuleResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCpDistributionRuleResp>>>() {
                @Override
                public DBResponse<List<GetCpDistributionRuleResp>> get() {
                    return lcProvinceDao.dbCpDistributionRule(config, param);
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

    public DBResponse<List<GetCpDistributionRuleResp>> getCpDistributionRuleV2(String _sessionId, GetCpDistributionRuleV2 param) {

        DBResponse<List<GetCpDistributionRuleResp>> response;
        try {
            final DBConfig config = getConfig(GET_CP_DISTRIBUTION_RULE_V2);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetCpDistributionRuleResp>>> memTask = new MemFutureTask<DBResponse<List<GetCpDistributionRuleResp>>>() {

                @Override
                public DBResponse<List<GetCpDistributionRuleResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCpDistributionRuleResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCpDistributionRuleResp>>>() {
                @Override
                public DBResponse<List<GetCpDistributionRuleResp>> get() {
                    return lcProvinceDao.dbCpDistributionRuleV2(config, param);
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

    public DBResponse<List<GetCpDistributionRuleV7Resp>> getCpDistributionRuleV7(String _sessionId, GetCpDistributionRuleV7 param) {

        DBResponse<List<GetCpDistributionRuleV7Resp>> response;
        try {
            final DBConfig config = getConfig(GET_CP_DISTRIBUTION_RULE_V7);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetCpDistributionRuleV7Resp>>> memTask = new MemFutureTask<DBResponse<List<GetCpDistributionRuleV7Resp>>>() {

                @Override
                public DBResponse<List<GetCpDistributionRuleV7Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCpDistributionRuleV7Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetCpDistributionRuleV7Resp>>>() {
                @Override
                public DBResponse<List<GetCpDistributionRuleV7Resp>> get() {
                    return lcProvinceDao.dbCpDistributionRule(config, param);
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

    public DBResponse<List<RuleParamResp>> getRuleParams(String _sessionId, GetRuleParams param) {

        DBResponse<List<RuleParamResp>> response;
        try {
            final DBConfig config = getConfig(GET_RULE_PARAM);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<RuleParamResp>>> memTask = new MemFutureTask<DBResponse<List<RuleParamResp>>>() {

                @Override
                public DBResponse<List<RuleParamResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<RuleParamResp>>> dbTask = new DBFutureTask<DBResponse<List<RuleParamResp>>>() {
                @Override
                public DBResponse<List<RuleParamResp>> get() {
                    return lcProvinceDao.dbRuleParam(config, param);
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

    public DBResponse<List<OrGroup>> getGroup(String _sessionId, GetGroupParams param) {

        DBResponse<List<OrGroup>> response;
        try {
            final DBConfig config = getConfig(GET_GROUP);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<OrGroup>>> memTask = new MemFutureTask<DBResponse<List<OrGroup>>>() {

                @Override
                public DBResponse<List<OrGroup>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<OrGroup>>> dbTask = new DBFutureTask<DBResponse<List<OrGroup>>>() {
                @Override
                public DBResponse<List<OrGroup>> get() {
                    return lcProvinceDao.dbGetGroup(config, param);
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


    public DBResponse<List<GetCallStrategyResp>> getCallStrategy(String _sessionId, GetCallStrategy param) {

        DBResponse<List<GetCallStrategyResp>> response;
        try {
            final DBConfig config = getConfig(GET_CALL_STRATEGY);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetCallStrategyResp>>> memTask = new MemFutureTask<DBResponse<List<GetCallStrategyResp>>>() {

                @Override
                public DBResponse<List<GetCallStrategyResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCallStrategyResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCallStrategyResp>>>() {
                @Override
                public DBResponse<List<GetCallStrategyResp>> get() {
                    return lcProvinceDao.dbGetCallstrategy(config, param);
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

    public DBResponse<List<GetCpCallStrategyResp>> getCpCallStrategy(String _sessionId, GetCpCallStrategy param) {

        DBResponse<List<GetCpCallStrategyResp>> response;
        try {
            final DBConfig config = getConfig(GET_CP_CALL_STRATEGY);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetCpCallStrategyResp>>> memTask = new MemFutureTask<DBResponse<List<GetCpCallStrategyResp>>>() {

                @Override
                public DBResponse<List<GetCpCallStrategyResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCpCallStrategyResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCpCallStrategyResp>>>() {
                @Override
                public DBResponse<List<GetCpCallStrategyResp>> get() {
                    return lcProvinceDao.dbCpGetCallstrategy(config, param);
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

    public DBResponse<List<GetStrategyParamResp>> getStrategyParam(String _sessionId, GetStrategyParam param) {

        DBResponse<List<GetStrategyParamResp>> response;
        try {
            final DBConfig config = getConfig(GET_STRATEGY_PARAM);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetStrategyParamResp>>> memTask = new MemFutureTask<DBResponse<List<GetStrategyParamResp>>>() {

                @Override
                public DBResponse<List<GetStrategyParamResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetStrategyParamResp>>> dbTask = new DBFutureTask<DBResponse<List<GetStrategyParamResp>>>() {
                @Override
                public DBResponse<List<GetStrategyParamResp>> get() {
                    return lcProvinceDao.dbGetCallstrategyParams(config, param);
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

    public DBResponse<List<GetGroupAgentResp>> getGroupAgent(String _sessionId, GetGroupAgent param) {

        DBResponse<List<GetGroupAgentResp>> response;
        try {
            final DBConfig config = getConfig(GET_GROUP_AGENT);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetGroupAgentResp>>> memTask = new MemFutureTask<DBResponse<List<GetGroupAgentResp>>>() {

                @Override
                public DBResponse<List<GetGroupAgentResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetGroupAgentResp>>> dbTask = new DBFutureTask<DBResponse<List<GetGroupAgentResp>>>() {
                @Override
                public DBResponse<List<GetGroupAgentResp>> get() {
                    return lcProvinceDao.dbGetGroupAgent(config, param);
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

    public DBResponse<List<GetGroupAgentResp>> getGroupAgentV2(String _sessionId, GetGroupAgentV2 param) {

        DBResponse<List<GetGroupAgentResp>> response;
        try {
            final DBConfig config = getConfig(GET_GROUP_AGENT_V2);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetGroupAgentResp>>> memTask = new MemFutureTask<DBResponse<List<GetGroupAgentResp>>>() {

                @Override
                public DBResponse<List<GetGroupAgentResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetGroupAgentResp>>> dbTask = new DBFutureTask<DBResponse<List<GetGroupAgentResp>>>() {
                @Override
                public DBResponse<List<GetGroupAgentResp>> get() {
                    return lcProvinceDao.dbGetGroupAgentV2(config, param);
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

    public DBResponse<List<Subdistrict>> getSubDistrict(String _sessionId, GetSubdistrict param) {

        DBResponse<List<Subdistrict>> response;
        try {
            final DBConfig config = getConfig(GET_SUB_DISTRICT);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<Subdistrict>>> memTask = new MemFutureTask<DBResponse<List<Subdistrict>>>() {

                @Override
                public DBResponse<List<Subdistrict>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<Subdistrict>>> dbTask = new DBFutureTask<DBResponse<List<Subdistrict>>>() {
                @Override
                public DBResponse<List<Subdistrict>> get() {
                    return lcProvinceDao.dbGetSubDistrict(config, param);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            //AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<GetSubDistrictByPartnerResp>> getSubDistrictByPartner(String _sessionId, GetSubDistrictByPartner param) {

        DBResponse<List<GetSubDistrictByPartnerResp>> response;
        try {
            final DBConfig config = getConfig(GET_SUB_DISTRICT_BY_PARTNER);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetSubDistrictByPartnerResp>>> memTask = new MemFutureTask<DBResponse<List<GetSubDistrictByPartnerResp>>>() {

                @Override
                public DBResponse<List<GetSubDistrictByPartnerResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetSubDistrictByPartnerResp>>> dbTask = new DBFutureTask<DBResponse<List<GetSubDistrictByPartnerResp>>>() {
                @Override
                public DBResponse<List<GetSubDistrictByPartnerResp>> get() {
                    return lcProvinceDao.dbGetSubDistrictByPartner(config, param);
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


    public DBResponse<List<GetProvinceBypartnerResp>> getProvinceByPartner(String _sessionId, GetProvinceBypartner param) {

        DBResponse<List<GetProvinceBypartnerResp>> response;
        try {
            final DBConfig config = getConfig(GET_PROVINCE_BY_PARTNER);
            AppUtils.printInput(logger, _sessionId, config, null, param);

            FutureTask<DBResponse<List<GetProvinceBypartnerResp>>> memTask = new MemFutureTask<DBResponse<List<GetProvinceBypartnerResp>>>() {

                @Override
                public DBResponse<List<GetProvinceBypartnerResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetProvinceBypartnerResp>>> dbTask = new DBFutureTask<DBResponse<List<GetProvinceBypartnerResp>>>() {
                @Override
                public DBResponse<List<GetProvinceBypartnerResp>> get() {
                    return lcProvinceDao.dbGetProvinceBypartner(config, param);
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

    public DBResponse<List<ProvinceWithPartnerResponseDTO>> getProvinceByPartnerV1(String _sessionId, GetProvinceBypartner param) {

        DBResponse<List<ProvinceWithPartnerResponseDTO>> response;
        try {
            final DBConfig config = getConfig(GET_PROVINCE_BY_PARTNER_V1);
            AppUtils.printInput(logger, _sessionId, config, null, param);

            FutureTask<DBResponse<List<ProvinceWithPartnerResponseDTO>>> memTask = new MemFutureTask<DBResponse<List<ProvinceWithPartnerResponseDTO>>>() {

                @Override
                public DBResponse<List<ProvinceWithPartnerResponseDTO>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<ProvinceWithPartnerResponseDTO>>> dbTask = new DBFutureTask<DBResponse<List<ProvinceWithPartnerResponseDTO>>>() {
                @Override
                public DBResponse<List<ProvinceWithPartnerResponseDTO>> get() {
                    return lcProvinceDao.dbGetProvinceByPartnerV1(config, param);
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

    public DBResponse<List<LCDistrict>> getDistrict(String _sessionId, GetDistrict param) {

        DBResponse<List<LCDistrict>> response;
        try {
            final DBConfig config = getConfig(GET_DISTRICT);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<LCDistrict>>> memTask = new MemFutureTask<DBResponse<List<LCDistrict>>>() {

                @Override
                public DBResponse<List<LCDistrict>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<LCDistrict>>> dbTask = new DBFutureTask<DBResponse<List<LCDistrict>>>() {
                @Override
                public DBResponse<List<LCDistrict>> get() {
                    return lcProvinceDao.dbGetDistrict(config, param);
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

    public DBResponse<List<GetDistrictByPartnerResp>> getDistrictByPartner(String _sessionId, GetDistrictByPartner param) {

        DBResponse<List<GetDistrictByPartnerResp>> response;
        try {
            final DBConfig config = getConfig(GET_DISTRICT_BY_PARTNER);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetDistrictByPartnerResp>>> memTask = new MemFutureTask<DBResponse<List<GetDistrictByPartnerResp>>>() {

                @Override
                public DBResponse<List<GetDistrictByPartnerResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDistrictByPartnerResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDistrictByPartnerResp>>>() {
                @Override
                public DBResponse<List<GetDistrictByPartnerResp>> get() {
                    return lcProvinceDao.dbGetDistrictByPartner(config, param);
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

    public DBResponse<List<CFBlacklist>> getBlacklist(String _sessionId, GetBlacklist param) {

        DBResponse<List<CFBlacklist>> response;
        try {
            final DBConfig config = getConfig(GET_BLACKLIST);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<CFBlacklist>>> memTask = new MemFutureTask<DBResponse<List<CFBlacklist>>>() {

                @Override
                public DBResponse<List<CFBlacklist>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CFBlacklist>>> dbTask = new DBFutureTask<DBResponse<List<CFBlacklist>>>() {
                @Override
                public DBResponse<List<CFBlacklist>> get() {
                    return lcProvinceDao.dbGetBlacklist(config, param);
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

    public DBResponse<List<CFDnc>> getDnc(String _sessionId, GetDnc param) {

        DBResponse<List<CFDnc>> response;
        try {
            final DBConfig config = getConfig(GET_DNC);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<CFDnc>>> memTask = new MemFutureTask<DBResponse<List<CFDnc>>>() {

                @Override
                public DBResponse<List<CFDnc>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CFDnc>>> dbTask = new DBFutureTask<DBResponse<List<CFDnc>>>() {
                @Override
                public DBResponse<List<CFDnc>> get() {
                    return lcProvinceDao.dbGetDnc(config, param);
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

    public DBResponse<List<CLBasket>> getBasketLead(String _sessionId, GetBasketLeadParams param) {

        DBResponse<List<CLBasket>> response;
        try {
            final DBConfig config = getConfig(GET_BASKET_LEAD);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<CLBasket>>> memTask = new MemFutureTask<DBResponse<List<CLBasket>>>() {

                @Override
                public DBResponse<List<CLBasket>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLBasket>>> dbTask = new DBFutureTask<DBResponse<List<CLBasket>>>() {
                @Override
                public DBResponse<List<CLBasket>> get() {
                    return lcProvinceDao.dbBasketLead(config, param);
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

    public DBResponse<List<CLBasket>> getBasketLeadV2(String _sessionId, GetBasketLeadV2 param) {

        DBResponse<List<CLBasket>> response;
        try {
            final DBConfig config = getConfig(GET_BASKET_LEAD_V2);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<CLBasket>>> memTask = new MemFutureTask<DBResponse<List<CLBasket>>>() {

                @Override
                public DBResponse<List<CLBasket>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLBasket>>> dbTask = new DBFutureTask<DBResponse<List<CLBasket>>>() {
                @Override
                public DBResponse<List<CLBasket>> get() {
                    return lcProvinceDao.dbBasketLeadV2(config, param);
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

    public DBResponse<List<CLBasket>> getBasketLeadV3(String _sessionId, GetBasketLeadV3 param) {

        DBResponse<List<CLBasket>> response;
        try {
            final DBConfig config = getConfig(GET_BASKET_LEAD_V3);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<CLBasket>>> memTask = new MemFutureTask<DBResponse<List<CLBasket>>>() {

                @Override
                public DBResponse<List<CLBasket>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLBasket>>> dbTask = new DBFutureTask<DBResponse<List<CLBasket>>>() {
                @Override
                public DBResponse<List<CLBasket>> get() {
                    return lcProvinceDao.dbBasketLeadV3(config, param);
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

    public DBResponse<List<CLBasket>> getBasketLeadV4(String _sessionId, GetBasketLeadV4 param) {

        DBResponse<List<CLBasket>> response;
        try {
            final DBConfig config = getConfig(GET_BASKET_LEAD_V4);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<CLBasket>>> memTask = new MemFutureTask<DBResponse<List<CLBasket>>>() {

                @Override
                public DBResponse<List<CLBasket>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<CLBasket>>> dbTask = new DBFutureTask<DBResponse<List<CLBasket>>>() {
                @Override
                public DBResponse<List<CLBasket>> get() {
                    return lcProvinceDao.dbBasketLeadV4(config, param);
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

    public DBResponse<List<GetDistributionRuleParamResp>> getDistributionRuleParam(String _sessionId, GetDistributionRuleParams param) {

        DBResponse<List<GetDistributionRuleParamResp>> response;
        try {
            final DBConfig config = getConfig(GET_DISTRI_RULE_PARAM);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetDistributionRuleParamResp>>> memTask = new MemFutureTask<DBResponse<List<GetDistributionRuleParamResp>>>() {

                @Override
                public DBResponse<List<GetDistributionRuleParamResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDistributionRuleParamResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDistributionRuleParamResp>>>() {
                @Override
                public DBResponse<List<GetDistributionRuleParamResp>> get() {
                    return lcProvinceDao.dbDistributionRuleParam(config, param);
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

    public DBResponse<List<GetProvinceMapResp>> getProvinceMap(String _sessionId, GetProvinceMap province) {

        DBResponse<List<GetProvinceMapResp>> response;
        try {
            final DBConfig config = getConfig(GET_PROVINCE_MAP);
            AppUtils.printInput(logger, _sessionId, config, null, province);

            FutureTask<DBResponse<List<GetProvinceMapResp>>> memTask = new MemFutureTask<DBResponse<List<GetProvinceMapResp>>>() {

                @Override
                public DBResponse<List<GetProvinceMapResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetProvinceMapResp>>> dbTask = new DBFutureTask<DBResponse<List<GetProvinceMapResp>>>() {
                @Override
                public DBResponse<List<GetProvinceMapResp>> get() {
                    return lcProvinceDao.dbGetProvinceMap(config, province);
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

    public DBResponse<List<GetSubdistrictMapResp>> getSubDistrictMap(String _sessionId, GetSubdistrictMap param) {

        DBResponse<List<GetSubdistrictMapResp>> response;
        try {
            final DBConfig config = getConfig(GET_SUB_DISTRICT_MAP);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetSubdistrictMapResp>>> memTask = new MemFutureTask<DBResponse<List<GetSubdistrictMapResp>>>() {

                @Override
                public DBResponse<List<GetSubdistrictMapResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetSubdistrictMapResp>>> dbTask = new DBFutureTask<DBResponse<List<GetSubdistrictMapResp>>>() {
                @Override
                public DBResponse<List<GetSubdistrictMapResp>> get() {
                    return lcProvinceDao.dbGetSubDistrictMap(config, param);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            //AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse<List<GetDistrictMapResp>> getDistrictMap(String _sessionId, GetDistrictMap param) {

        DBResponse<List<GetDistrictMapResp>> response;
        try {
            final DBConfig config = getConfig(GET_DISTRICT_MAP);
            AppUtils.printInput(logger, _sessionId, config, null, null);

            FutureTask<DBResponse<List<GetDistrictMapResp>>> memTask = new MemFutureTask<DBResponse<List<GetDistrictMapResp>>>() {

                @Override
                public DBResponse<List<GetDistrictMapResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDistrictMapResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDistrictMapResp>>>() {
                @Override
                public DBResponse<List<GetDistrictMapResp>> get() {
                    return lcProvinceDao.dbGetDistrictMap(config, param);
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
