package com.tms.api.helper;

import com.tms.ff.utils.AppProperties;

/**
 * Created by dinhanhthai on 20/04/2019.
 */
public class Const {
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int DEFAULT_PAGE_SIZE_CDR_EXPORT = 200;
    public static final int DEFAULT_PAGE = 0;
    public static final String ACCESS_API_PATH_KEY = "product_api";
    public static final String ACCESS_API_ADMIN = "ROLE_ADMIN";
    public static final String ACCESS_API_AGENT = "ROLE_AGENT";
    public static final String ACCESS_API_TEAMLEADER = "ROLE_TEAMLEADER";
    public static final String ACCESS_API_VALIDATOR = "ROLE_VALIDATOR";
    public static final String ACCESS_API_MANAGER = "ROLE_MANAGER";
    public static final String STATUS_ASSIGN = "assigned";
    public static final String STATUS_UNASSIGN = "unassigned";
    public static final String DB_TMS_NULL = "NULL";
    public static final String DB_TMS_NOTNULL = "NOTNULL";
    public static final String REQUEST_USERNAME = "username";
    public static final String DO_SUCCESS = "SUCCESS_CREATE_DO";
    public static final String DO_ERROR = "ERROR WHEN CREATE ORDER";
    public static final String DO_CANCEL_SUCCESS = "DO CANCEL SUCCESS";
    public static final String DO_CANCEL_ERROR = "DO CANCEL ERROR";
    public static final String FAIL_GET_RESPONSE_FROM_3PL = "FAIL GET RESPONSE FROM 3PL";
    public static final String TRASH_LEAD_MKT = "TRASH_MKT";
    public static final String HOLD_LEAD_MKT = "HOLD_MKT";
    public static final int AGENT_CALLING_NUM = 1;
    public static final int BEFORE_CALLBACK_TIME = 5; // minutes
    public static final int CALLBACK_NUM = 1;
    public static final String CREATE_SO_MANUAL_LEAD_TYPE = "M";
    // api method value
    public static final int GET_VALUE = 1;
    public static final int POST_VALUE = 2;
    public static final int PUT_VALUE = 4;
    public static final int DELETE_VALUE = 8;
    public static final int INS_DB_SUCCESS = 0;
    public static final int INS_DB_ERROR = 1;
    public static final boolean _IS_SELECTED_PARTNER_IN_ORDER = true;

    public static final int DHL_PARTNER_ID = 4; 

    public static final int NinjaVan_PARTNER_ID = 123; 
    public static final int WFS_PARTNER_ID = 1; 
    public static final int VT_PARTNER_ID = 5;
    public static final int BM_PARTNER_ID = 8;
    public static final int BM_INDO_PARTNER_ID = 39;
    public static final int BM_TH_PARTNER_ID = 55;
    public static final int SAP_LM_PARTNER_ID = 21;
    public static final int SAP_FFM_PARTNER_ID = 27;
    public static final int KERRY_FFM_PARTNER_ID = 26;
    public static final int KERRY_LM_PARTNER_ID = 20;
    public static final int GHN_PARTNER_ID = 6;
    public static final int MYCLOUD_FFM_PARTNER_ID = 56;
    public static final int ALPHA_LM_PARTNER_ID = 57;
    public static final int SCG_EXPRESS_LM_PARTNER_ID = 60;
    public static final int DHL_TH_PARTNER_ID = 59;
    public static final int KERRY_TH_PARTNER_ID = 121;
    public static final int FLASH_TH_PARTNER_ID = 150;
    public static final int SHIP60_LM_PARTNER_VN = 160;
    public static final int NTL_FFM_PARTNER_ID = 161;
    public static final int DHL_FFM_PARTNER_ID = 103;
    public static final String DHL_FFM_PARTNER_CODE = "DHLF";
    public static final String DHL_PARTNER_CODE = "DHL";
    public static final String DHL_TH_PARTNER_CODE = "DHL_PD";
    public static final String KERRY_TH_PARTNER_CODE = "KERRY_PD";
    public static final String FLASH_TH_PARTNER_CODE = "FLASH_PD";
    public static final int TRUE_TH_PARTNER_ID = 85;
    public static final String TRUE_TH_PARTNER_CODE = "TEL_DEX";
    public static final int HAISTAR_PARTNER_ID = 132;
    public static final int SICEPAT_PARTNER_ID = 133;
    public static final int SNAPPY_PARTNER_ID = 140;

    public static final int GHTK_LM_PARTNER_VN = 100;
    public static final String WFS_NINJAVAN_PARTNER_CODE = "NIV001";
    public static final String WFS_SNAPPY_PARTNER_CODE = "SNAPPY";
    public static final String WFS_GHTK_PARTNER_CODE = "GHTK001";
    public static final String WFS_DHL_PARTNER_CODE = "DHL001";
    public static final String WFS_GHN_PARTNER_CODE = "GHN001";
    public static final String WFS_S60_PARTNER_CODE = "S60";
    public static final String WFS_GHN_EXPRESS_SERVICE_NHANH = "NHANH";
    public static final String WFS_GHN_EXPRESS_SERVICE_CHUAN = "CHUAN";
    public static final String DHL_COURIER_NAME = "DHL eCommerce";
    public static final String GHTK_COURIER_NAME = "Giao Hàng Tiết Kiệm";
    public static final String GHN_EXPRESS_CODE = "GHN";
    //mac dinh la nguoi ban chiu phi voi Express
    public static final Integer WFS_GHN_EXPRESS_PAYMENT_TYPE_ID = 1;
    //cho xem hang khong thu
    public static final String WFS_GHN_EXPRESS_NOTE_CODE = "CHOXEMHANGKHONGTHU";
    public static final String VT_PARTNER_CODE = "VTP";
    public static final String BM_PARTNER_CODE = "BM";
    public static final String BM_EXPRESS_CODE = "BM_EXPRESS";
    public static final String KER_PARTNER_CODE = "KER";
    public static final String FLASH_PARTNER_CODE = "FLASH";
    public static final String SAP_PARTNER_CODE = "SAP";
    public static final String SAPW_PARTNER_CODE = "SAPW";
    public static final String MYCLOUD_PARTNER_CODE = "MYC";
    public static final String ALPHA_PARTNER_CODE = "ALP";
    public static final String SCG_EXPRESS_PARTNER_CODE = "SCG";
    public static final String NTL_EXPRESS_PARTNER_CODE = "NTX";
    public static final String SHIP60_PARTNER_CODE = "SHIP60";

    public static final String QUEUE_TEST = "test-queue";
    public static final Integer VT_EKIWI_CUS_ID = 6803976;
    public static final String QUEUE_AGENTCY = AppProperties.getPropertyValue("config.queue-agent");
    public static final String QUEUE_LOG = "log";
    public static final int[] phoneExts = {104, 105, 106, 107, 108, 109, 110, 111, 112, 113};
    public static final String REDIS_PREFIX_TEST = "test" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_LEAD = "lead" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_CREATE = "create" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_RECENT_LEAD = "recent" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_SO = "so" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PRERIX_DO = "do" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_AMOUNT = "amount" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_MAKE_CALL = "make_call" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_CALLING = "calling" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_GROUP = "group" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_STATIC = "static" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_RATE = "rate" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_NEW = "NEW" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_FIELD_TOTAL_LEAD = "leadTotal";
    public static final String REDIS_PREFIX_BASKET = "basket" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_USER = "user" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_GLOBAL = "global" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String REDIS_PREFIX_CALL_ID = "call_id" + AppProperties.getPropertyValue("config.redis-suffix");
    public static final String USER_TEAMLEADER_TYPE = "team leader";
    public static final String USER_TEAMLEADER_OTHER = "tl_efa";
    public static final String USER_MANAGER_TYPE = "manager";
    public static final String USER_ADMIN_TYPE = "admin";
    public static final String USER_AGENT_TYPE = "agent";
    public static final String USER_DIRECTOR_TYPE = "director";
    public static final String USER_VALIDATION_TYPE = "validation";
    public static final String DB_DATE_FORMAT = "yyyyMMdd HH:mm:ss";
    public static final String DB_TIME_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_S = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String SPLIT = ";#";
    public static final int LEAD_BASKET_EXPIRE_TRASH = 3 * 60 * 60;// 3 h
    public static final int LEAD_BASKET_MAX_LIFE = 10 * 24 * 60 * 60;//10 day

    public static final int AGENCY_CLICK_DEAL = 18;
    public static final int AGENCY_MOSIAC = 19;
    public static final int AGENCY_MAXXI = 23;
    public static final int AGENCY_TMA = 22;
    public static final int AGENCY_AFSU = 29;
    public static final int AGENCY_ADFLEX = 3;
    public static final int AGENCY_ARB = 130;
    public static final int AGENCY_MKT = 58;

    public static final int COUNTRY_TL = 10;
    public static final String DO_SHIPPING_PENDING = "pending";
    public static final int CAMPAIGN_STATUS_RUNNING = 31;

    public static final int SYSTEM_ID = 0;

    public static final String REGEX_PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z]{8,33}$";

    public static final int LEAD_STATUS_CALLBACK_CONSULTING = 8;
    public static final int LEAD_STATUS_CALLBACK_NOT_PROSPECT = 9;
    public static final int LEAD_STATUS_CALLBACK_POTENTIAL = 14;
    // Using for log agent
    public static final Integer LOG_AGENT_STATE = 53;
    public static final int LOG_AGENT_STATE_CACHE_LIVE_TIME = 8*60*60;
    public static final int LOG_AGENT_LAST_CHECKING_MINUTES = 8*60;
    public static final int LOG_AGENT_CODE_SUCCESS = 200;
    public static final int LOG_AGENT_CODE_FAILED = 500;
    public static final String LOG_AGENT_ERROR_NO_AGENT = "No agent found";
    public static final String LOG_AGENT_ERROR_NOT_ALLOWED = "Action not allowed";
    public static final String LOG_AGENT_ERROR_INTERNAL_SERVER = "Internal server error";
    public static final String LOG_AGENT_REQUEST_STATE = "Requested %s";
    public static final String LOG_AGENT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
}
