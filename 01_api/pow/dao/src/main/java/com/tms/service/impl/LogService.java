package com.tms.service.impl;

import com.tms.api.request.CampaignConfigurationRequestDTO;
import com.tms.api.request.CampaignUpdateRequestDTO;
import com.tms.config.DBConfig;
import com.tms.dao.impl.LogDao;
import com.tms.dto.*;
import com.tms.entity.log.*;
import com.tms.exception.ConfigNotFoundException;
import com.tms.model.Request.SaleOrderRequestDTO;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class LogService extends BaseService {

    private final static String LOG_LEAD = "log_lead";
    private final static String LOG_LEAD_V3 = "log_lead_v3";
    private final static String UPD_LEAD = "upd_lead";
    private final static String UPD_LEAD_V4 = "upd_lead_v4";
    private final static String UPD_LEAD_V5 = "upd_lead_v5";
    private final static String UPD_LEAD_V6 = "upd_lead_v6";
    private final static String UPD_LEAD_V7 = "upd_lead_v7";
    private final static String UPD_LEAD_V8 = "upd_lead_v8";
    private final static String UPD_LEAD_V9 = "upd_lead_v9";
    private final static String UPD_LEAD_BY_ASSIGNED = "upd_lead_byAssigned";
    private final static String UPD_LEAD_BY_ASSIGNED_V2 = "upd_lead_by_assigned_v2";
    private final static String UPD_LEAD_BY_ASSIGNED_V3 = "upd_lead_by_assigned_v3";
    private final static String UPD_IN_ACTIVE_LEAD = "upd_inactive_lead";
    private final static String LOG_CALL_BACK = "log_callback";
    private final static String UPD_CALL_BACK = "upd_callback";
    private final static String UPD_CALL_BACK_BY_ASSIGNED = "upd_callback_byassigned";
    private final static String DEL_CALL_BACK = "del_Callack";
    private final static String INS_BASKET = "ins_cl_basket";
    private final static String INS_BASKET_V2 = "ins_cl_basket_v2";
    private final static String INS_BASKET_V3 = "ins_cl_basket_v3";
    private final static String INS_BASKET_V4 = "ins_cl_basket_v4";
    private final static String UPD_BASKET = "upd_basket";
    private final static String UPD_BASKET_V2 = "upd_basket_v2";
    private final static String INS_DELIVERY_ORDER = "ins_do";
    private final static String UPD_DELIVERY_ORDER = "upd_do";
    private final static String INS_PAYMENT_ORDER = "ins_po";
    private final static String INS_SALE_ORDER = "ins_so";
    private final static String INS_SALE_ORDER_V2 = "ins_so_v2";
    private final static String DEL_SALE_ORDER = "del_so_item";
    private final static String UPD_SALE_ORDER = "upd_so";
    private final static String UPD_SALE_ORDER_V2 = "upd_so_v2";
    private final static String UPD_SALE_ORDER_V3 = "upd_so_v3";
    private final static String UPD_SALE_ORDER_V4 = "upd_so_v4";
    private final static String UPD_SALE_ORDER_V5 = "upd_so_v5";
    private final static String INS_SALE_ITEM_ORDER = "ins_so_item";
    private final static String INS_SALE_ITEM_ORDER_V2 = "ins_so_item_v2";
    private final static String LOG_PAYMENT_ORDER = "log_po";
    private final static String LOG_SALE_ORDER = "log_so";
    private final static String LOG_DELIVERY_ORDER = "log_do";
    private final static String LOG_AGENT_ACTIVITY = "log_agent_activity";
    private final static String LOG_SEND_SMS_DELIVERY_ORDER = "log_send_sms_delivery_order";

    private final static String INS_CAMPAIGN = "ins_campaign";
    private final static String INS_CAMPAIGN_V1 = "ins_campaign_v1";
    private final static String UPD_CAMPAIGN = "upd_campaign";
    private final static String UPD_CAMPAIGN_V1 = "upd_campaign_v1";
    private final static String INS_CL_FRESH = "ins_cl_fresh";
    private final static String INS_CL_FRESH_V2 = "ins_cl_fresh_v2";
    private final static String INS_CL_FRESH_V3 = "ins_cl_fresh_v3";
    private final static String INS_CL_FRESH_V4 = "ins_cl_fresh_v4";
    private final static String INS_CL_FRESH_V5 = "ins_cl_fresh_v5";
    private final static String INS_CL_FRESH_V6 = "ins_cl_fresh_v6";
    private final static String INS_CL_FRESH_V7 = "ins_cl_fresh_v7";
    private final static String INS_CL_FRESH_V8 = "ins_cl_fresh_v8";
    private final static String INS_CL_FRESH_V9 = "ins_cl_fresh_v9";
    private final static String INS_CL_FRESH_V10 = "ins_cl_fresh_v10";
    private final static String INS_CL_FRESH_V11 = "ins_cl_fresh_v11";
    private final static String DEL_CL_INACTIVE = "del_cl_inactive";
    private final static String DEL_ALL_FRESH = "del_all_fresh";
    private final static String DEL_MUL_FRESH = "del_multiple_fresh";
    private final static String INS_CL_ACTIVE = "ins_cl_active";
    private final static String INS_CL_ACTIVE_V2 = "ins_cl_active_v2";
    private final static String INS_CL_ACTIVE_V3 = "ins_cl_active_v3";
    private final static String INS_CL_INACTIVE = "ins_cl_inactive";
    private final static String INS_CL_INACTIVE_V2 = "ins_cl_inactive_v2";
    private final static String INS_CL_INACTIVE_V3 = "ins_cl_inactive_v3";
    private final static String INS_CL_TRASH = "ins_cl_trash";
    private final static String INS_CL_TRASH_V2 = "ins_cl_trash_v2";
    private final static String INS_CL_TRASH_V3 = "ins_cl_trash_v3";
    private final static String INS_CL_CALLBACK = "ins_cl_callback";
    private final static String INS_CL_CALLBACK_V4 = "ins_cl_callback_v4";
    private final static String INS_CL_CALLBACK_V6 = "ins_cl_callback_v6";
    private final static String DEL_CL_CALLBACK = "del_cl_callback";
    private final static String DEL_MUL_CL_CALLBACK = "del_multiple_callback";
    private final static String INS_CP_CONFIG = "ins_cp_config";
    private final static String UDP_CP_CONFIG = "upd_CP_config";

    private final static String INS_PROVINCE = "ins_province";
    private final static String INS_DISTRICT = "ins_district";
    private final static String INS_SUB_DISTRICT = "ins_subdistrict";

    private final static String INS_PROVINCE_MAP = "ins_province_map";
    private final static String INS_DISTRICT_MAP = "ins_district_map";
    private final static String INS_SUB_DISTRICT_MAP = "ins_subdistrict_map";

    private final static String INS_PRODUCT = "ins_product";
    private final static String INS_PRODUCT_V2 = "ins_product_v2";
    private final static String INS_PRODUCT_COMBO = "ins_product_combo";
    private final static String DEL_PRODUCT_COMBO = "del_product_combo";
    private final static String UPD_PRODUCT = "upd_product";
    private final static String UPD_PRODUCT_V2 = "upd_product_v2";
    private final static String INS_PRODUCT_ATTR = "ins_product_attribute";
    private final static String UPD_PRODUCT_ATTR = "upd_product_attribute";

    private final static String INS_STOCK = "ins_stock";
    private final static String UPD_STOCK = "upd_stock";
    private final static String INS_UNREACHABLE = "ins_cl_unreachable";
    private final static String UPD_UNREACHABLE = "upd_unreachable";
    private final static String DEL_UNREACHABLE = "del_unreachable";
    private final static String DEL_MULT_UNREACHABLE = "del_multiple_unreachable";

    private final static String UPD_SO_ITEM = "udp_so_item";
    private final static String INS_ORG_PARTNER = "ins_organization_partner";
    private final static String UPD_ORG_PARTNER = "udp_org_partner";

    private final static String INS_WARE_HOURSE = "ins_warehouse";
    private final static String UPD_WARE_HOURSE = "upd_warehouse";
    private final static String DEL_WARE_HOURSE = "del_warehouse";

    private final static String INS_DO_NEW = "ins_do_new";
    private final static String INS_DO_NEW_V2 = "ins_do_new_v2";
    private final static String UPD_DO_NEW = "upd_do_new";
    private final static String UPD_DO_NEW_V2 = "upd_do_new_v2";
    private final static String UPD_DO_NEW_V3 = "upd_do_new_v3";
    private final static String UPD_DO_NEW_V4 = "upd_do_new_v4";
    private final static String UPD_DO_NEW_V5 = "upd_do_new_v5";
    private final static String UPD_DO_NEW_V6 = "upd_do_new_v6";
    private final static String UPD_DO_NEW_V7 = "upd_do_new_v7";
    private final static String UPD_DO_NEW_V8 = "upd_do_new_v8";
    private final static String UPD_DO_NEW_V9 = "upd_do_new_v9";
    private final static String UPD_DO_NEW_BY_TCODE = "upd_do_new_bytrackingCode";
    private final static String DEL_DO_NEW = "del_do_new";

    private final static String UPD_SO_FULFILLMENT = "upd_so_fulfillment";

    private final static String INS_STATUS_MAPPING = "ins_status_mapping";
    private final static String INS_DO_POSTBACK = "ins_do_postback";

    private final static String DEL_PROVINCE_MAP = "del_province_map";
    private final static String DEL_DISTRICT_MAP = "del_district_map";
    private final static String DEL_SUB_DISTRICT_MAP = "del_subdistrict_map";

    private final static String INS_PRODUCT_MAPPING = "ins_product_mapping";
    private final static String DEL_PRODUCT_MAPPING = "del_product_mapping";

    private final static String INS_CDR = "ins_cdr";
    private final static String INS_CDR_V2 = "ins_cdr_v2";
    private final static String UPD_CDR = "upd_cdr";
    private final static String DEL_CDR = "del_cdr";

    private final static String INS_PICK_UP = "ins_pickup";
    private final static String UPD_PICK_UP = "upd_pickup";
    private final static String DEL_PICK_UP = "del_pickup";

    private final static String INS_OFFER = "ins_offer";

    private final static String INS_AGENT_RATE = "ins_agent_rate";

    private final static String INS_CDR_ALL = "ins_cdr_all";
    private final static String INS_LOG_CONNECTED_CUSTOMER = "ins_log_connected_customer";
    private final static String INS_LOG_UNCALL_CONNECTED = "ins_log_uncall_connected";

    private final static String UPD_LEAD_REASSIGN = "upd_lead_reassign";
    private final static String UPD_LEAD_REASSIGN_V2 = "upd_lead_reassign_v2";
    private final static String UPD_LEAD_REASSIGN_V3 = "upd_lead_reassign_v3";
    private final static String UPDATE_UN_CALL = "upd_un_call";
    private final static String UPDATE_UN_CALL_V2 = "upd_un_call_v2";
    private final static String UPDATE_UN_CALL_V3 = "upd_un_call_v3";

    private final static String INS_GROUP_AGENT = "ins_group_agent";
    private final static String DEL_GROUP_AGENT = "del_group_agent";
    private final static String UPD_GROUP_AGENT = "upd_group_agent";
    private final static String INS_INTEGRATE_PARTNER = "log_integrate_partner";
    private final static String INS_CL_POSTBACK = "ins_cl_postback";
    private final static String UPD_CL_POSTBACK = "upd_cl_postback";
    private final static String INS_LASTMILE_STATUS = "ins_rc_lastmile_status";

    private final static String INS_MTK_DATA = "ins_mtk_data";
    private final static String INS_MKT_DATA = "ins_mkt_data";
    private final static String UPD_MKT_DATA = "upd_mkt_data";


    private final static String INS_OR_DEPARTMENT = "ins_or_department";
    private final static String UPD_OR_DEPARTMENT = "udp_or_department";
    private final static String INS_OR_OFFER = "ins_or_offer";
    private final static String UPD_OR_OFFER = "upd_or_offer";
    private final static String INS_LC_NEIGHBORHOOD = "ins_lc_neighborhood";
    private final static String INS_LC_POSTA_CODE = "ins_lc_postal_code";
    private final static String DEL_LC_NEIGHBORHOOD_MAP = "del_lc_neighborhood_map";
    private final static String INS_BP_PARTNER = "ins_bp_partner";

    private final static String UPD_LEAD_RELEASE_RESERVED = "upd_lead_release_reserved";
    private final static String UPD_LEAD_CRM_ACTION_TYPE = "upd_lead_crm_action_type";

    private Logger logger = LoggerFactory.getLogger(LogService.class);

    @Autowired
    private LogDao logDao;

    public DBResponse logLead(String _sessionId, LogLead logLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(LOG_LEAD);
            AppUtils.printInsert(logger, _sessionId, config, null, logLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileFreshLog(config, logLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbFreshLog(config, logLead);
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

    public DBResponse logLeadV3(String _sessionId, LogLeadV3 logLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(LOG_LEAD_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, logLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileLogLeadV3(config, logLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbLogLeadV3(config, logLead);
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

    public DBResponse logCallback(String _sessionId, LogCallback logCallback) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(LOG_CALL_BACK);
            AppUtils.printInsert(logger, _sessionId, config, null, logCallback);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileCallbackLog(config, logCallback);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbCallbackLog(config, logCallback);
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

    public DBResponse updateCallback(String _sessionId, UpdateCLCallback clCallback) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_CALL_BACK);
            AppUtils.printInsert(logger, _sessionId, config, null, clCallback);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpCallbackLog(config, clCallback);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpCallbackLog(config, clCallback);
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

    public DBResponse updateCallbackByAssigned(String _sessionId, UpdateCLCallbackByAssigned clCallback) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_CALL_BACK_BY_ASSIGNED);
            AppUtils.printInsert(logger, _sessionId, config, null, clCallback);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdCallbackByAssigned(config, clCallback);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdCallbackByAssigned(config, clCallback);
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

    public DBResponse insBasket(String _sessionId, InsCLBasket basket) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_BASKET);
            AppUtils.printInsert(logger, _sessionId, config, null, basket);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsBasket(config, basket);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsBasket(config, basket);
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

    public DBResponse insBasketV2(String _sessionId, InsCLBasketV2 basket) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_BASKET_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, basket);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsBasketV2(config, basket);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsBasketV2(config, basket);
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

    public DBResponse insBasketV3(String _sessionId, InsCLBasketV3 basket) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_BASKET_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, basket);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsBasketV3(config, basket);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsBasketV3(config, basket);
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

    public DBResponse insBasketV4(String _sessionId, InsCLBasketV4 basket) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_BASKET_V4);
            AppUtils.printInsert(logger, _sessionId, config, null, basket);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsBasketV4(config, basket);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsBasketV4(config, basket);
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

    public DBResponse updBasket(String _sessionId, UpdCLBasket basket) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_BASKET);
            AppUtils.printInsert(logger, _sessionId, config, null, basket);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdBasket(config, basket);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdBasket(config, basket);
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

    public DBResponse updBasketV2(String _sessionId, UpdCLBasketV2 basket) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_BASKET_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, basket);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdBasketV2(config, basket);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdBasketV2(config, basket);
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

    public DBResponse insDeliveryOrder(String _sessionId, InsDeliveryOrder deliveryOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_DELIVERY_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, deliveryOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsDeliveryOrder(config, deliveryOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsDeliveryOrder(config, deliveryOrder);
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

    public DBResponse insPaymentOrder(String _sessionId, InsPaymentOrder paymentOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PAYMENT_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, paymentOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsPaymentOrder(config, paymentOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsPaymentOrder(config, paymentOrder);
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

    public DBResponse insSaleOrder(String _sessionId, InsSaleOrder saleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_SALE_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, saleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsSaleOrder(config, saleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsSaleOrder(config, saleOrder);
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

    public DBResponse insSaleOrderV2(String _sessionId, InsSaleOrderV2 saleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_SALE_ORDER_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, saleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsSaleOrder(config, saleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsSaleOrder(config, saleOrder);
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

    public DBResponse insSaleItemOrder(String _sessionId, InsSaleOrderItem saleOrderItem) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_SALE_ITEM_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, saleOrderItem);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsSaleItemOrder(config, saleOrderItem);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsSaleItemOrder(config, saleOrderItem);
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

    public DBResponse insSaleItemOrderV2(String _sessionId, InsSaleOrderItemV2 saleOrderItem) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_SALE_ITEM_ORDER_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, saleOrderItem);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsSaleItemOrder(config, saleOrderItem);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsSaleItemOrder(config, saleOrderItem);
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

    public DBResponse logPaymentOrder(String _sessionId, LogPaymentOrder paymentOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(LOG_PAYMENT_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, paymentOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileLogPaymentOrder(config, paymentOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbLogPaymentItemOrder(config, paymentOrder);
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

    public DBResponse logSaleOrder(String _sessionId, LogSaleOrder saleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(LOG_SALE_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, saleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileLogSaleOrder(config, saleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbLogSaleOrder(config, saleOrder);
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

    public DBResponse updDeliveryOrder(String _sessionId, UpdDeliveryOrder updDeliveryOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DELIVERY_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, updDeliveryOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDeliveryOrder(config, updDeliveryOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDeliveryOrder(config, updDeliveryOrder);
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

    public DBResponse updSaleOrder(String _sessionId, UpdSaleOrder updSaleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_SALE_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, updSaleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdSaleOrder(config, updSaleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdSaleOrder(config, updSaleOrder);
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

    public DBResponse updSaleOrderV2(String _sessionId, UpdSaleOrderV2 updSaleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_SALE_ORDER_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, updSaleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdSaleOrder(config, updSaleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdSaleOrder(config, updSaleOrder);
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

    public DBResponse updSaleOrderV5(String sessionId, SaleOrderRequestDTO params) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_SALE_ORDER_V5);
            AppUtils.printInsert(logger, sessionId, config, null, params);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdSaleOrder(config, params);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdSaleOrder(config, params);
                }
            };
            response = insert(sessionId, config, fileTask, dbTask);

            AppUtils.printOutput(logger, sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }

    public DBResponse updSaleOrderV3(String _sessionId, UpdSaleOrderV3 updSaleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_SALE_ORDER_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, updSaleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdSaleOrder(config, updSaleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdSaleOrder(config, updSaleOrder);
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

    public DBResponse updSaleOrderV4(String _sessionId, UpdSaleOrderV4 updSaleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_SALE_ORDER_V4);
            AppUtils.printInsert(logger, _sessionId, config, null, updSaleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdSaleOrder(config, updSaleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdSaleOrder(config, updSaleOrder);
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

    public DBResponse updInActiveLead(String _sessionId, UpdInActiveLead updInActiveLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_IN_ACTIVE_LEAD);
            AppUtils.printInsert(logger, _sessionId, config, null, updInActiveLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdInactiveLead(config, updInActiveLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdInactiveLead(config, updInActiveLead);
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

    public DBResponse updLead(String _sessionId, UpdLead updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLead(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLead(config, updLead);
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

    public DBResponse updLeadV4(String _sessionId, UpdLeadV4 updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_V4);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLeadV4(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadV4(config, updLead);
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

    public DBResponse updLeadV5(String _sessionId, UpdLeadV5 updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_V5);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLead(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLead(config, updLead);
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

    public DBResponse updLeadByAssigned(String _sessionId, UpdLeadByAssigned updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_BY_ASSIGNED);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLeadByAssigned(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadByAssigned(config, updLead);
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

    public DBResponse<?> updLeadByAssignedV2(String _sessionId, UpdLeadByAssignedV2 updLead) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_BY_ASSIGNED_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLeadByAssignedV2(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadByAssignedV2(config, updLead);
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

    public DBResponse<?> updLeadByAssignedV3(String _sessionId, UpdLeadByAssignedV3 updLead) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_BY_ASSIGNED_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLeadByAssignedV3(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadByAssignedV3(config, updLead);
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

    public DBResponse insCampaign(String _sessionId, InsCampaign campaign) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CAMPAIGN);
            AppUtils.printInsert(logger, _sessionId, config, null, campaign);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCampaign(config, campaign);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCampaign(config, campaign);
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

    public DBResponse<?> insCampaign(String _sessionId, CampaignConfigurationRequestDTO campaign) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(INS_CAMPAIGN_V1);
            AppUtils.printInsert(logger, _sessionId, config, null, campaign);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCampaign(config, campaign);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCampaign(config, campaign);
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

    public DBResponse udpCampaign(String _sessionId, UpdCampaign campaign) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_CAMPAIGN);
            AppUtils.printInsert(logger, _sessionId, config, null, campaign);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdCampaign(config, campaign);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdCampaign(config, campaign);
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

    public DBResponse<?> udpCampaign(String _sessionId, CampaignUpdateRequestDTO campaign) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPD_CAMPAIGN_V1);
            AppUtils.printInsert(logger, _sessionId, config, null, campaign);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdCampaign(config, campaign);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdCampaign(config, campaign);
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

    public DBResponse insCLFresh(String _sessionId, InsCLFresh fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFresh(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFresh(config, fresh);
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

    public DBResponse insCLFreshV2(String _sessionId, InsCLFreshV2 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV2(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV2(config, fresh);
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

    public DBResponse insCLFreshV3(String _sessionId, InsCLFreshV3 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV3(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV3(config, fresh);
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

    public DBResponse insCLFreshV4(String _sessionId, InsCLFreshV4 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V4);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV4(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV4(config, fresh);
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

    public DBResponse insCLFreshV5(String _sessionId, InsCLFreshV5 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V5);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFresh(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFresh(config, fresh);
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

    public DBResponse insCLFreshV9(String _sessionId, InsCLFreshV9 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V9);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV9(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV9(config, fresh);
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

    public DBResponse insCLFreshV11(String _sessionId, InsCLFreshV11 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V11);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV11(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV11(config, fresh);
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

    public DBResponse delAllFresh(String _sessionId, Integer leadId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_ALL_FRESH);
            AppUtils.printInsert(logger, _sessionId, config, null, leadId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelAllFresh(config, leadId);
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

    public DBResponse delMultipleFresh(String _sessionId, String leadId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_MUL_FRESH);
            AppUtils.printInsert(logger, _sessionId, config, null, leadId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelMultipleFresh(config, leadId);
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

    public DBResponse insCLActive(String _sessionId, InsCLActive clActive) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_ACTIVE);
            AppUtils.printInsert(logger, _sessionId, config, null, clActive);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLActive(config, clActive);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLActive(config, clActive);
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

    public DBResponse insCLActiveV2(String _sessionId, InsCLActiveV2 clActive) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_ACTIVE_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, clActive);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLActiveV2(config, clActive);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLActiveV2(config, clActive);
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

    public DBResponse insCLActiveV3(String _sessionId, InsCLActiveV3 clActive) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_ACTIVE_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, clActive);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLActiveV3(config, clActive);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLActiveV3(config, clActive);
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

    public DBResponse insCLInActive(String _sessionId, InsCLInActive clInActive) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_INACTIVE);
            AppUtils.printInsert(logger, _sessionId, config, null, clInActive);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLInActive(config, clInActive);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLInActive(config, clInActive);
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

    public DBResponse insCLInActiveV2(String _sessionId, InsCLInActiveV2 clInActive) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_INACTIVE_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, clInActive);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLInActiveV2(config, clInActive);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLInActiveV2(config, clInActive);
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

    public DBResponse insCLInActiveV3(String _sessionId, InsCLInActiveV3 clInActive) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_INACTIVE_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, clInActive);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLInActiveV3(config, clInActive);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLInActiveV3(config, clInActive);
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

    public DBResponse insCLTrash(String _sessionId, InsCLTrash clTrash) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_TRASH);
            AppUtils.printInsert(logger, _sessionId, config, null, clTrash);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLTrash(config, clTrash);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLTrash(config, clTrash);
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

    public DBResponse insCLTrashV2(String _sessionId, InsCLTrashV2 clTrash) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_TRASH_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, clTrash);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLTrashV2(config, clTrash);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLTrashV2(config, clTrash);
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

    public DBResponse insCLTrashV3(String _sessionId, InsCLTrashV3 clTrash) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_TRASH_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, clTrash);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLTrashV3(config, clTrash);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLTrashV3(config, clTrash);
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

    public DBResponse insCLCallback(String _sessionId, InsCLCallback clCallback) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_CALLBACK);
            AppUtils.printInsert(logger, _sessionId, config, null, clCallback);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLCallback(config, clCallback);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLCallback(config, clCallback);
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

    public DBResponse insCLCallbackV4(String _sessionId, InsCLCallbackV4 clCallback) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_CALLBACK_V4);
            AppUtils.printInsert(logger, _sessionId, config, null, clCallback);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLCallbackV4(config, clCallback);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLCallbackV4(config, clCallback);
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

    public DBResponse insCLCallbackV6(String _sessionId, InsCLCallbackV6 clCallback) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_CALLBACK_V6);
            AppUtils.printInsert(logger, _sessionId, config, null, clCallback);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLCallbackV6(config, clCallback);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLCallbackV6(config, clCallback);
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

    public DBResponse insCPConfig(String _sessionId, InsCPConfig cpConfig) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CP_CONFIG);
            AppUtils.printInsert(logger, _sessionId, config, null, cpConfig);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCPConfig(config, cpConfig);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCPConfig(config, cpConfig);
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

    public DBResponse updCPConfig(String _sessionId, UdpCPConfig cpConfig) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UDP_CP_CONFIG);
            AppUtils.printInsert(logger, _sessionId, config, null, cpConfig);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUdpCPConfig(config, cpConfig);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUdpCPConfig(config, cpConfig);
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

    public DBResponse insProvince(String _sessionId, InsProvince insProvince) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PROVINCE);
            AppUtils.printInsert(logger, _sessionId, config, null, insProvince);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsProvince(config, insProvince);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsProvince(config, insProvince);
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

    public DBResponse insDistrict(String _sessionId, InsDistrict district) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_DISTRICT);
            AppUtils.printInsert(logger, _sessionId, config, null, district);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsDistrict(config, district);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsDistrict(config, district);
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

    public DBResponse insSubdistrict(String _sessionId, InsSubdistrict subdistrict) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_SUB_DISTRICT);
            AppUtils.printInsert(logger, _sessionId, config, null, subdistrict);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsSubDistrict(config, subdistrict);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsSubDistrict(config, subdistrict);
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

    public DBResponse insProvinceMap(String _sessionId, InsProvinceMap provinceMap) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PROVINCE_MAP);
            AppUtils.printInsert(logger, _sessionId, config, null, provinceMap);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsProvinceMap(config, provinceMap);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsProvinceMap(config, provinceMap);
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

    public DBResponse insDistrictMap(String _sessionId, InsDistrictMap districtMap) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_DISTRICT_MAP);
            AppUtils.printInsert(logger, _sessionId, config, null, districtMap);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsDistrictMap(config, districtMap);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsDistrictMap(config, districtMap);
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

    public DBResponse insSubdistrictMap(String _sessionId, InsSubDistrictMap subdistrict) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_SUB_DISTRICT_MAP);
            AppUtils.printInsert(logger, _sessionId, config, null, subdistrict);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsSubDistrictMap(config, subdistrict);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsSubDistrictMap(config, subdistrict);
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

    public DBResponse delCallback(String _sessionId, Integer leadId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_CL_CALLBACK);
            AppUtils.printInsert(logger, _sessionId, config, null, leadId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelCallback(config, leadId);
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

    public DBResponse delSo(String _sessionId, Integer soId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_SALE_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, soId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelSo(config, soId);
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

    public DBResponse delMulCallback(String _sessionId, String leadId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_MUL_CL_CALLBACK);
            AppUtils.printInsert(logger, _sessionId, config, null, leadId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbMulDelCallback(config, leadId);
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

    public DBResponse insProduct(String _sessionId, InsProduct attribute) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PRODUCT);
            AppUtils.printInsert(logger, _sessionId, config, null, attribute);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsProduct(config, attribute);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsProduct(config, attribute);
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

    public DBResponse insProductV2(String _sessionId, InsProductV2 attribute) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PRODUCT_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, attribute);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsProduct(config, attribute);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsProduct(config, attribute);
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

    public DBResponse insProductCombo(String _sessionId, InsProductCombo attribute) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PRODUCT_COMBO);
            AppUtils.printInsert(logger, _sessionId, config, null, attribute);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsProductCombo(config, attribute);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsProductCombo(config, attribute);
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

    public DBResponse delProductCombo(String _sessionId, Integer comboId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_PRODUCT_COMBO);
            AppUtils.printInsert(logger, _sessionId, config, null, comboId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelProductCombo(config, comboId);
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

    public DBResponse updProduct(String _sessionId, UpdProduct product) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_PRODUCT);
            AppUtils.printInsert(logger, _sessionId, config, null, product);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdProduct(config, product);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdProduct(config, product);
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

    public DBResponse updProductV2(String _sessionId, UpdProductV2 product) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_PRODUCT_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, product);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdProduct(config, product);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdProduct(config, product);
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

    public DBResponse insProductAttribute(String _sessionId, InsProductAttribute attribute) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PRODUCT_ATTR);
            AppUtils.printInsert(logger, _sessionId, config, null, attribute);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsProductAttribute(config, attribute);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsProductAttribute(config, attribute);
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

    public DBResponse updProductAttribute(String _sessionId, InsProductAttribute attribute) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_PRODUCT_ATTR);
            AppUtils.printInsert(logger, _sessionId, config, null, attribute);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdProductAttribute(config, attribute);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdProductAttribute(config, attribute);
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

    public DBResponse insStock(String _sessionId, InsStock stock) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_STOCK);
            AppUtils.printInsert(logger, _sessionId, config, null, stock);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsStock(config, stock);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsStock(config, stock);
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

    public DBResponse updStock(String _sessionId, UpdStock stock) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_STOCK);
            AppUtils.printInsert(logger, _sessionId, config, null, stock);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdStock(config, stock);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdStock(config, stock);
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

    public DBResponse insUnreachable(String _sessionId, InsUnreachable stock) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_UNREACHABLE);
            AppUtils.printInsert(logger, _sessionId, config, null, stock);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsUnreachable(config, stock);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsUnreachable(config, stock);
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

    public DBResponse updUnreachable(String _sessionId, UpdUnreachable stock) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_UNREACHABLE);
            AppUtils.printInsert(logger, _sessionId, config, null, stock);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdUnreachable(config, stock);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdUnreachable(config, stock);
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

    public DBResponse delUnreachable(String _sessionId, Integer leadId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_UNREACHABLE);
            AppUtils.printInsert(logger, _sessionId, config, null, leadId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelUnreachable(config, leadId);
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

    public DBResponse delMulUnreachable(String _sessionId, String leadId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_MULT_UNREACHABLE);
            AppUtils.printInsert(logger, _sessionId, config, null, leadId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelMulUnreachable(config, leadId);
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

    public DBResponse updSoItem(String _sessionId, UpdSoItem soItem) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_SO_ITEM);
            AppUtils.printInsert(logger, _sessionId, config, null, soItem);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdSoItem(config, soItem);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdSoItem(config, soItem);
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

    public DBResponse insOrganizationPartner(String _sessionId, InsOrganizationPartner partner) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_ORG_PARTNER);
            AppUtils.printInsert(logger, _sessionId, config, null, partner);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsOrgPartner(config, partner);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsOrgPartner(config, partner);
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

    public DBResponse updOrganizationPartner(String _sessionId, UpdOrganizationPartner partner) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_ORG_PARTNER);
            AppUtils.printInsert(logger, _sessionId, config, null, partner);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdOrgPartner(config, partner);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdOrgPartner(config, partner);
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

    public DBResponse logDeliveryOrder(String _sessionId, LogDeliveryOrder saleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(LOG_DELIVERY_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, saleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileLogDeliveryOrder(config, saleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbLoDeliveryOrder(config, saleOrder);
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

    public DBResponse logAgentActivity(String _sessionId, LogAgentActivity saleOrder) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(LOG_AGENT_ACTIVITY);
            AppUtils.printInsert(logger, _sessionId, config, null, saleOrder);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileLogAgent(config, saleOrder);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbLogAgent(config, saleOrder);
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

    public DBResponse insWarehouse(String _sessionId, InsWareHouse wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_WARE_HOURSE);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsWarehouse(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsWarehouse(config, wareHouse);
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

    public DBResponse updWareHouse(String _sessionId, UpdWareHouse wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_WARE_HOURSE);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdWarehouse(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdWarehouse(config, wareHouse);
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

    public DBResponse delWareHouse(String _sessionId, String warehouseId) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_WARE_HOURSE);
            AppUtils.printInsert(logger, _sessionId, config, null, warehouseId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelWarehouse(config, warehouseId);
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

    public DBResponse insDoNew(String _sessionId, InsDoNew doNew) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_DO_NEW);
            AppUtils.printInsert(logger, _sessionId, config, null, doNew);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsDoNew(config, doNew);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsDoNew(config, doNew);
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

    public DBResponse insDoNewV2(String _sessionId, InsDoNewV2 doNew) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_DO_NEW_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, doNew);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsDoNew(config, doNew);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsDoNew(config, doNew);
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

    public DBResponse updDoNew(String _sessionId, UpdDoNew wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDoNew(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDoNew(config, wareHouse);
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

    public DBResponse updDoNewV2(String _sessionId, UpdDoNewV2 wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDoNew(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDoNew(config, wareHouse);
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

    public DBResponse updDoNewV3(String _sessionId, UpdDoNewV3 wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDoNew(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDoNew(config, wareHouse);
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

    public DBResponse updDoNewV4(String _sessionId, UpdDoNewV4 wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDoNew(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDoNew(config, wareHouse);
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

    public DBResponse updDoNewV6(String _sessionId, UpdDoNewV5 wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW_V6);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDoNew(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDoNew(config, wareHouse);
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

    public DBResponse updDoNewV8(String _sessionId, UpdDoNewV8 wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW_V8);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDoNew(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDoNew(config, wareHouse);
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

    public DBResponse updDoNewV9(String _sessionId, UpdDoNewV9 wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW_V9);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDoNew(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDoNew(config, wareHouse);
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

    public DBResponse updDoNewByTrackingCode(String _sessionId, UpdDoNewByTrackingCode wareHouse) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW_BY_TCODE);
            AppUtils.printInsert(logger, _sessionId, config, null, wareHouse);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdDoNew(config, wareHouse);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdDoNew(config, wareHouse);
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

    public DBResponse delDoNew(String _sessionId, String doId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_DO_NEW);
            AppUtils.printInsert(logger, _sessionId, config, null, new HashMap<String, Object>() {
                {
                    put("doId", doId);
                }
            });

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelDoNew(config, doId);
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

    public DBResponse updSoFulfillment(String _sessionId, UpdSoFulfillment fulfillment) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_SO_FULFILLMENT);
            AppUtils.printInsert(logger, _sessionId, config, null, fulfillment);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdSoFulfillment(config, fulfillment);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdSoFulfillment(config, fulfillment);
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

    public DBResponse insStatusMapping(String _sessionId, InsStatusMapping statusMapping) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_STATUS_MAPPING);
            AppUtils.printInsert(logger, _sessionId, config, null, statusMapping);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsStatusMapping(config, statusMapping);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdStatusMapping(config, statusMapping);
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

    public DBResponse insDoPostback(String _sessionId, InsDoPostback statusMapping) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_DO_POSTBACK);
            AppUtils.printInsert(logger, _sessionId, config, null, statusMapping);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsDoPostback(config, statusMapping);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsDoPostback(config, statusMapping);
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

    public DBResponse delProvinceMap(String _sessionId, Integer partnerId, String prvId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_PROVINCE_MAP);
            AppUtils.printInsert(logger, _sessionId, config, null, new HashMap<String, Object>() {
                {
                    put("partnerId", partnerId);
                    put("prvId", prvId);
                }
            });

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelProviceMap(config, partnerId, prvId);
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

    public DBResponse delDistrictMap(String _sessionId, Integer partnerId, String prvId, String dtId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_DISTRICT_MAP);
            AppUtils.printInsert(logger, _sessionId, config, null, new HashMap<String, Object>() {
                {
                    put("partnerId", partnerId);
                    put("prvId", prvId);
                    put("dtId", dtId);
                }
            });

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelDistrictMap(config, partnerId, prvId, dtId);
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

    public DBResponse delSubDistrictMap(String _sessionId, Integer partnerId, String prvId, String dtId, String sdtId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_SUB_DISTRICT_MAP);
            AppUtils.printInsert(logger, _sessionId, config, null, new HashMap<String, Object>() {
                {
                    put("partnerId", partnerId);
                    put("prvId", prvId);
                    put("dtId", dtId);
                    put("sdtId", sdtId);
                }
            });

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelSubDistrictMap(config, partnerId, prvId, dtId, sdtId);
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

    public DBResponse insProductMapping(String _sessionId, InsProductMapping productMapping) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PRODUCT_MAPPING);
            AppUtils.printInsert(logger, _sessionId, config, null, productMapping);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsProductMapping(config, productMapping);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsProductMapping(config, productMapping);
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

    public DBResponse delProductMapping(String _sessionId, Integer partnerId, String productId,
                                        String partnerProductId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_PRODUCT_MAPPING);
            AppUtils.printInsert(logger, _sessionId, config, null, new HashMap<String, Object>() {
                {
                    put("partnerId", partnerId);
                    put("productId", productId);
                    put("partnerProductId", partnerProductId);
                }
            });

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelProductMapping(config, partnerId, productId, partnerProductId);
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

    public DBResponse insCdr(String _sessionId, InsCdr cdr) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CDR);
            AppUtils.printInsert(logger, _sessionId, config, null, cdr);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCdr(config, cdr);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCdr(config, cdr);
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

    public DBResponse insCdrV2(String _sessionId, InsCdrV2 cdr) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CDR_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, cdr);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCdrV2(config, cdr);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCdrV2(config, cdr);
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

    public DBResponse updCdr(String _sessionId, UpdCdr cdr) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_CDR);
            AppUtils.printInsert(logger, _sessionId, config, null, cdr);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdCdr(config, cdr);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdCdr(config, cdr);
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

    public DBResponse delCdr(String _sessionId, String callId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_CDR);
            AppUtils.printInsert(logger, _sessionId, config, null, new HashMap<String, Object>() {
                {
                    put("callId", callId);
                }
            });

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelCdr(config, callId);
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

    public DBResponse insPickup(String _sessionId, InsPickup pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_PICK_UP);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsPickup(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsPickup(config, pickup);
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

    public DBResponse updPickup(String _sessionId, UpdPickup pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_PICK_UP);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdPickup(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdPickup(config, pickup);
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

    public DBResponse delPickup(String _sessionId, String pickupId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_PICK_UP);
            AppUtils.printInsert(logger, _sessionId, config, null, new HashMap<String, Object>() {
                {
                    put("pickupId", pickupId);
                }
            });

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelPickup(config, pickupId);
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

    public DBResponse insOffer(String _sessionId, InsOffer pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_OFFER);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsOffer(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsOffer(config, pickup);
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

    public DBResponse insAgentRate(String _sessionId, InsAgentRate pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_AGENT_RATE);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsAgentRate(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsAgentRate(config, pickup);
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

    public DBResponse updLeadReasign(String _sessionId, UpdLeadReasign cdr) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_REASSIGN);
            AppUtils.printInsert(logger, _sessionId, config, null, cdr);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLeadAsign(config, cdr);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadAsign(config, cdr);
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

    public DBResponse updLeadReassignV2(String _sessionId, UpdLeadReassignV2 cdr) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_REASSIGN_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, cdr);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLeadAssignV2(config, cdr);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadAssignV2(config, cdr);
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

    public DBResponse updLeadReassignV3(String _sessionId, UpdLeadReassignV3 cdr) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_REASSIGN_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, cdr);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLeadAssignV3(config, cdr);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadAssignV3(config, cdr);
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

    /* #begin: update un-call */
    @SuppressWarnings({"rawtypes"})
    public DBResponse<?> updateUnCall(String _sessionId, GetUncallLeadV2 params) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPDATE_UN_CALL);
            AppUtils.printInsert(logger, _sessionId, config, null, params);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.fileUpdateUnCall(config, params);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdateUnCall(config, params);
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
    /* #end: update un-call */

    public DBResponse<?> updateUnCallV2(String _sessionId, UpdUncallV2 params) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPDATE_UN_CALL_V2);
            AppUtils.printInsert(logger, _sessionId, config, null, params);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.fileUpdateUnCallV2(config, params);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdateUnCallV2(config, params);
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

    public DBResponse<?> updateUnCallV3(String _sessionId, UpdUncallV3 params) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPDATE_UN_CALL_V3);
            AppUtils.printInsert(logger, _sessionId, config, null, params);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.fileUpdateUnCallV3(config, params);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdateUnCallV3(config, params);
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

    public DBResponse insCdrAll(String _sessionId, InsCdrAll pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CDR_ALL);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCdrAll(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCdrAll(config, pickup);
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

    public DBResponse insLogConnectedCustomer(String _sessionId, InsLogConnectedCustomer pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_LOG_CONNECTED_CUSTOMER);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsLogConnectedCustomer(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsLogConnectedCustomer(config, pickup);
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

    public DBResponse insLogUnCallConnected(String _sessionId, InsLogUnCallConnected pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_LOG_UNCALL_CONNECTED);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsLogUnCallConnected(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsLogUnCallConnected(config, pickup);
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

    public DBResponse insGroupAgent(String _sessionId, InsGroupAgent pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_GROUP_AGENT);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsGroupAgent(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsGroupAgent(config, pickup);
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

    public DBResponse delGroupAgent(String _sessionId, DelGroupAgent groupAgent) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_GROUP_AGENT);
            AppUtils.printInsert(logger, _sessionId, config, null, groupAgent);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelGroupAgent(config, groupAgent);
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

    public DBResponse updGroupAgent(String _sessionId, UpdGroupAgent groupAgent) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_GROUP_AGENT);
            AppUtils.printInsert(logger, _sessionId, config, null, groupAgent);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdGroupAgent(config, groupAgent);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdGroupAgent(config, groupAgent);
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

    public DBResponse insLogIntegratePartner(String _sessionId, InsLogIntegratePartner pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_INTEGRATE_PARTNER);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsIntegratePartner(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsIntegratePartner(config, pickup);
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

    public DBResponse insLogLastPostbackPartner(String _sessionId, LogLastPostbackPartner logLastPostbackPartner) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_POSTBACK);
            AppUtils.printInsert(logger, _sessionId, config, null, logLastPostbackPartner);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsLogLastPostbackPartner(config, logLastPostbackPartner);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsLogLastPostbackPartner(config, logLastPostbackPartner);
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

    public DBResponse updLogLastPostbackPartner(String _sessionId, LogLastPostbackPartner logLastPostbackPartner) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_CL_POSTBACK);
            AppUtils.printInsert(logger, _sessionId, config, null, logLastPostbackPartner);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLogLastPostbackPartner(config, logLastPostbackPartner);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLogLastPostbackPartner(config, logLastPostbackPartner);
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

    public DBResponse insLastmileStatus(String _sessionId, InsLastmileStatus pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_LASTMILE_STATUS);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsLastmileStatus(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsLastmileStatus(config, pickup);
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

    public DBResponse<?> insMtkData(String _sessionId, InsMtkData pickup) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(INS_MTK_DATA);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsMtkData(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsMtkData(config, pickup);
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

    public DBResponse<?> insMktData(String _sessionId, InsMktData pickup) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(INS_MKT_DATA);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsMktData(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsMktData(config, pickup);
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

    public DBResponse insOrDepartment(String _sessionId, InsOrDepartment pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_OR_DEPARTMENT);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsOrDepartment(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsOrDepartment(config, pickup);
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

    public DBResponse updOrDepartment(String _sessionId, UpdOrDepartment pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_OR_DEPARTMENT);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdOrDepartment(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdOrDepartment(config, pickup);
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

    public DBResponse insOrOffer(String _sessionId, InsOffer offer) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_OR_OFFER);
            AppUtils.printInsert(logger, _sessionId, config, null, offer);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsOrOffer(config, offer);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsOrOffer(config, offer);
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

    public DBResponse updOrOffer(String _sessionId, UpdOrOffer offer) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_OR_OFFER);
            AppUtils.printInsert(logger, _sessionId, config, null, offer);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdOrOffer(config, offer);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdOrOffer(config, offer);
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

    public DBResponse insLcNeighborhood(String _sessionId, InsLcNeighborhood pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_LC_NEIGHBORHOOD);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsLcNeighborhood(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsLcNeighborhood(config, pickup);
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

    public DBResponse insLcPostaCode(String _sessionId, InsLcPostaCode pickup) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_LC_POSTA_CODE);
            AppUtils.printInsert(logger, _sessionId, config, null, pickup);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsLcPostaCode(config, pickup);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsLcPostaCode(config, pickup);
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

    public DBResponse delLcNeighborhood(String _sessionId, DelLcNeighborhood param) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_LC_NEIGHBORHOOD_MAP);
            AppUtils.printInsert(logger, _sessionId, config, null, param);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelNeighborhood(config, param);
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

    public DBResponse insBpPartner(String _sessionId, InsBpPartner bpPartner) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_BP_PARTNER);
            AppUtils.printInsert(logger, _sessionId, config, null, bpPartner);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsBpPartner(config, bpPartner);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsBpPartner(config, bpPartner);
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

    public DBResponse insCLFreshV6(String _sessionId, InsCLFreshV6 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V6);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV6(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV6(config, fresh);
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

    public DBResponse delCLInactive(String _sessionId, Integer leadId) {

        DBResponse response;
        try {
            final DBConfig config = getConfig(DEL_CL_INACTIVE);
            AppUtils.printInsert(logger, _sessionId, config, null, leadId);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return null;
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbDelCLInactive(config, leadId);
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

    public DBResponse logSendSmsDeliveryOrder(String _sessionId, LogSendSmsDeliveryOrder dto) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(LOG_SEND_SMS_DELIVERY_ORDER);
            AppUtils.printInsert(logger, _sessionId, config, null, dto);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileLogSendSmsDeliveryOrder(config, dto);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbLogSendSmsDeliveryOrder(config, dto);
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

    public DBResponse<?> insCLFreshV7(String _sessionId, InsCLFreshV7 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V7);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV7(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV7(config, fresh);
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

    public DBResponse<?> insCLFreshV10(String _sessionId, InsCLFreshV10 fresh) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V10);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV10(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV10(config, fresh);
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

    public DBResponse<?> updLeadV6(String _sessionId, UpdLeadV6 updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_V6);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLead(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLead(config, updLead);
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

	public DBResponse<?> insCLFreshV8(String _sessionId, InsCLFreshV8 fresh) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(INS_CL_FRESH_V8);
            AppUtils.printInsert(logger, _sessionId, config, null, fresh);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileInsCLFreshV8(config, fresh);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbInsCLFreshV8(config, fresh);
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

	public DBResponse<?> updLeadV7(String _sessionId, UpdLeadV7 updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_V7);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLead(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLead(config, updLead);
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

    public DBResponse<?> updLeadV8(String _sessionId, UpdLeadV8 updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_V8);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLead(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLead(config, updLead);
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

    public DBResponse<?> updLeadV9(String _sessionId, UpdLeadV9 updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_V9);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLead(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLead(config, updLead);
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

    public DBResponse<?> updMktData(String _sessionId, UpdMktData updMktData) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPD_MKT_DATA);
            AppUtils.printInsert(logger, _sessionId, config, null, updMktData);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdMktData(config, updMktData);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdMktData(config, updMktData);
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

    public DBResponse<?> updLeadReleaseReserved(String _sessionId, UpdLeadReleaseReserved updLead) {
        DBResponse response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_RELEASE_RESERVED);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse<String> get() {
                    return logDao.fileUpdLeadReleaseReserved(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadReleaseReserved(config, updLead);
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

    @Async
    public DBResponse<?> updLeadCrmActionType(String _sessionId, UpdLeadCrmActionType updLead) {
        DBResponse<?> response;
        try {
            final DBConfig config = getConfig(UPD_LEAD_CRM_ACTION_TYPE);
            AppUtils.printInsert(logger, _sessionId, config, null, updLead);

            FutureTask<DBResponse> fileTask = new FileFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.fileUpdLeadCrmActionType(config, updLead);
                }
            };

            FutureTask<DBResponse> dbTask = new DBFutureTask<DBResponse>() {
                @Override
                public DBResponse get() {
                    return logDao.dbUpdLeadCrmActionType(config, updLead);
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
