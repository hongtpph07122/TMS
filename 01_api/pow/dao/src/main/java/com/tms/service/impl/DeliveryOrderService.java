package com.tms.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.config.DBConfig;
import com.tms.dao.impl.DeliveryOrderDao;
import com.tms.dto.*;
import com.tms.entity.DeliveryOrder;
import com.tms.entity.SaleOrder;
import com.tms.exception.ConfigNotFoundException;
import com.tms.model.Response.DeliveriesOrderResponseDTO;
import com.tms.model.Response.DeliveriesOrderResponseDTOV1;
import com.tms.model.Response.NeighborhoodResponseDTO;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryOrderService extends BaseService {

    private final static String GET_DO = "get_do";
    private final static String GET_DEFAULT_DO = "get_default_do";
    private final static String GET_DEFAULT_DO_V2 = "get_default_do_v2";
    private final static String GET_DEFAULT_DO_V3 = "get_default_do_v3";
    private final static String GET_SO = "get_so";
    private final static String GET_SO_V4 = "get_so_v4";
    private final static String GET_SO_V2 = "get_so_v2";
    private final static String GET_SO_V3 = "get_so_v3";
    private final static String GET_SO_V5 = "get_so_v5";
    private final static String GET_SO_ITEM = "get_so_item";
    private final static String GET_SO_ITEM_V2 = "get_so_item_v2";
    private final static String GET_SO_BY_PRODUCT = "get_so_byProduct";
    private final static String GET_DO_NEW = "get_do_new";
    private final static String GET_DO_NEW_V2 = "get_do_new_v2";
    private final static String GET_DO_NEW_V7 = "get_do_new_v7";
    private final static String GET_DO_NEW_V9 = "get_do_new_v9";
    private final static String GET_DO_NEW_V10 = "get_do_new_v10";
    private final static String GET_DO_NEW_V11 = "get_do_new_v11";
    private final static String GET_DO_NEW_V12 = "get_do_new_v12";
    private final static String GET_SO_FULFILLMENT = "get_so_fulfillment";
    private final static String GET_STATUS_MAPPING = "get_status_mapping";
    private final static String GET_TRACKING_CODE = "get_tracking_code";
    private final static String GET_TRACKING_CODE_V2 = "get_tracking_code_v2";
    private final static String GET_TRACKING_CODE_V3 = "get_tracking_code_v3";
    private final static String GET_SHIPPING_EXPORT_CSV = "get_shipping_exportCSV";
    private final static String GET_DO_FULFILLMENT = "get_do_fulfillment";
    private final static String GET_SHIPPING = "get_shipping";
    private final static String GET_NEIGHBORHOOD = "get_neighborhood";
    private final static String GET_NEIGHBORHOOD_V1 = "get_neighborhood_v1";
    private final static String GET_LOCATION_FULL = "get_location_full";
    private final static String GET_LOG_SEND_SMS_DELIVERY_ORDER = "get_log_send_sms_delivery_order";
    private final static String UPD_DO_NEW = "upd_do_new_v8";

    private Logger logger = LoggerFactory.getLogger(DeliveryOrderService.class);

    @Autowired
    private DeliveryOrderDao deliveryOrderDao;

    public DBResponse<List<DeliveryOrder>> getDO(String _sessionId, GetDOParams params) {
        DBResponse<List<DeliveryOrder>> response;
        try {
            final DBConfig config = getConfig(GET_DO);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<DeliveryOrder>>> memTask = new MemFutureTask<DBResponse<List<DeliveryOrder>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<DeliveryOrder>>> dbTask = new DBFutureTask<DBResponse<List<DeliveryOrder>>>() {
                @Override
                public DBResponse<List<DeliveryOrder>> get() {
                    return deliveryOrderDao.dbGetDO(config, params);
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

    public DBResponse<List<GetDefaultDOResp>> getDefaultDO(String _sessionId, GetDefaultDO params) {
        DBResponse<List<GetDefaultDOResp>> response;
        try {
            final DBConfig config = getConfig(GET_DEFAULT_DO);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDefaultDOResp>>> memTask = new MemFutureTask<DBResponse<List<GetDefaultDOResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDefaultDOResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDefaultDOResp>>>() {
                @Override
                public DBResponse<List<GetDefaultDOResp>> get() {
                    return deliveryOrderDao.dbDefaultGetDO(config, params);
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

    public DBResponse<List<GetDefaultDOResp>> getDefaultDOV2(String _sessionId, GetDefaultDOV2 params) {
        DBResponse<List<GetDefaultDOResp>> response;
        try {
            final DBConfig config = getConfig(GET_DEFAULT_DO_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDefaultDOResp>>> memTask = new MemFutureTask<DBResponse<List<GetDefaultDOResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDefaultDOResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDefaultDOResp>>>() {
                @Override
                public DBResponse<List<GetDefaultDOResp>> get() {
                    return deliveryOrderDao.dbDefaultGetDOV2(config, params);
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

    public DBResponse<List<GetDefaultDOResp>> getDefaultDOV3(String _sessionId, GetDefaultDOV3 params) {
        DBResponse<List<GetDefaultDOResp>> response;
        try {
            final DBConfig config = getConfig(GET_DEFAULT_DO_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDefaultDOResp>>> memTask = new MemFutureTask<DBResponse<List<GetDefaultDOResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDefaultDOResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDefaultDOResp>>>() {
                @Override
                public DBResponse<List<GetDefaultDOResp>> get() {
                    return deliveryOrderDao.dbDefaultGetDOV3(config, params);
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

    public DBResponse<List<SaleOrder>> getSaleOrder(String _sessionId, GetSOParams params) {
        DBResponse<List<SaleOrder>> response;
        try {
            final DBConfig config = getConfig(GET_SO);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<SaleOrder>>> memTask = new MemFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<SaleOrder>>> dbTask = new DBFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse<List<SaleOrder>> get() {
                    return deliveryOrderDao.dbGetSO(config, params);
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

    public DBResponse<List<SaleOrder>> getSaleOrderV2(String _sessionId, GetSOV2 params) {
        DBResponse<List<SaleOrder>> response;
        try {
            final DBConfig config = getConfig(GET_SO_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<SaleOrder>>> memTask = new MemFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<SaleOrder>>> dbTask = new DBFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse<List<SaleOrder>> get() {
                    return deliveryOrderDao.dbGetSO(config, params);
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

    public DBResponse<List<SaleOrder>> getSaleOrderV3(String _sessionId, GetSOV3 params) {
        DBResponse<List<SaleOrder>> response;
        try {
            final DBConfig config = getConfig(GET_SO_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<SaleOrder>>> memTask = new MemFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<SaleOrder>>> dbTask = new DBFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse<List<SaleOrder>> get() {
                    return deliveryOrderDao.dbGetSO(config, params);
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

    public DBResponse<List<SaleOrder>> getSaleOrderV5(String _sessionId, GetSOV3 params) {
        DBResponse<List<SaleOrder>> response;
        try {
            final DBConfig config = getConfig(GET_SO_V5);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<SaleOrder>>> memTask = new MemFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<SaleOrder>>> dbTask = new DBFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse<List<SaleOrder>> get() {
                    return deliveryOrderDao.dbGetSO(config, params);
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

    public DBResponse<List<SaleOrder>> getSaleOrderV4(String _sessionId, GetSOV4 params) {
        DBResponse<List<SaleOrder>> response;
        try {
            final DBConfig config = getConfig(GET_SO_V4);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<SaleOrder>>> memTask = new MemFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<SaleOrder>>> dbTask = new DBFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse<List<SaleOrder>> get() {
                    return deliveryOrderDao.dbGetSO(config, params);
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

    public DBResponse<List<GetSoItemResp>> getSaleOrderItem(String _sessionId, GetSoItem params) {
        DBResponse<List<GetSoItemResp>> response;
        try {
            final DBConfig config = getConfig(GET_SO_ITEM);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetSoItemResp>>> memTask = new MemFutureTask<DBResponse<List<GetSoItemResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetSoItemResp>>> dbTask = new DBFutureTask<DBResponse<List<GetSoItemResp>>>() {
                @Override
                public DBResponse<List<GetSoItemResp>> get() {
                    return deliveryOrderDao.dbGetSOItem(config, params);
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

    public DBResponse<List<GetSoItemResp>> getSaleOrderItemV2(String _sessionId, GetSoItemV2 params) {
        DBResponse<List<GetSoItemResp>> response;
        try {
            final DBConfig config = getConfig(GET_SO_ITEM_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetSoItemResp>>> memTask = new MemFutureTask<DBResponse<List<GetSoItemResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetSoItemResp>>> dbTask = new DBFutureTask<DBResponse<List<GetSoItemResp>>>() {
                @Override
                public DBResponse<List<GetSoItemResp>> get() {
                    return deliveryOrderDao.dbGetSOItem(config, params);
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

    public DBResponse<List<SaleOrder>> getSaleOrderByProduct(String _sessionId, GetSOByProductParams params) {
        DBResponse<List<SaleOrder>> response;
        try {
            final DBConfig config = getConfig(GET_SO_BY_PRODUCT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<SaleOrder>>> memTask = new MemFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<SaleOrder>>> dbTask = new DBFutureTask<DBResponse<List<SaleOrder>>>() {
                @Override
                public DBResponse<List<SaleOrder>> get() {
                    return deliveryOrderDao.dbGetSOByProductId(config, params);
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

    public DBResponse<List<GetDoNewResp>> getDoNew(String _sessionId, GetDoNew params) {
        DBResponse<List<GetDoNewResp>> response;
        try {
            final DBConfig config = getConfig(GET_DO_NEW);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDoNewResp>>> memTask = new MemFutureTask<DBResponse<List<GetDoNewResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDoNewResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDoNewResp>>>() {
                @Override
                public DBResponse<List<GetDoNewResp>> get() {
                    return deliveryOrderDao.dbGetDoNew(config, params);
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

    public DBResponse<List<DeliveriesOrderResponseDTO>> getDoNewV9(String _sessionId, GetDoNewV8 params) {
        DBResponse<List<DeliveriesOrderResponseDTO>> response;
        try {
            final DBConfig config = getConfig(GET_DO_NEW_V9);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>> memTask = new MemFutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>> dbTask = new DBFutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>>() {
                @Override
                public DBResponse<List<DeliveriesOrderResponseDTO>> get() {
                    return deliveryOrderDao.dbGetDoNew(config, params);
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

    public DBResponse<List<DeliveriesOrderResponseDTO>> getDoNewV10(String _sessionId, GetDoNewV10 params) {
        DBResponse<List<DeliveriesOrderResponseDTO>> response;
        try {
            final DBConfig config = getConfig(GET_DO_NEW_V10);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>> memTask = new MemFutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>> dbTask = new DBFutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>>() {
                @Override
                public DBResponse<List<DeliveriesOrderResponseDTO>> get() {
                    return deliveryOrderDao.dbGetDoNewV10(config, params);
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

    public DBResponse<List<DeliveriesOrderResponseDTO>> getDoNewV11(String _sessionId, GetDoNewV11 params) {
        DBResponse<List<DeliveriesOrderResponseDTO>> response;
        try {
            final DBConfig config = getConfig(GET_DO_NEW_V11);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>> memTask = new MemFutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>> dbTask = new DBFutureTask<DBResponse<List<DeliveriesOrderResponseDTO>>>() {
                @Override
                public DBResponse<List<DeliveriesOrderResponseDTO>> get() {
                    return deliveryOrderDao.dbGetDoNewV11(config, params);
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

    public DBResponse<List<DeliveriesOrderResponseDTOV1>> getDoNewV12(String _sessionId, GetDoNewV12 params) {
        DBResponse<List<DeliveriesOrderResponseDTOV1>> response;
        try {
            final DBConfig config = getConfig(GET_DO_NEW_V12);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            MemFutureTask<DBResponse<List<DeliveriesOrderResponseDTOV1>>> memTask = new MemFutureTask<DBResponse<List<DeliveriesOrderResponseDTOV1>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<DeliveriesOrderResponseDTOV1>>> dbTask = new DBFutureTask<DBResponse<List<DeliveriesOrderResponseDTOV1>>>() {
                @Override
                public DBResponse<List<DeliveriesOrderResponseDTOV1>> get() {
                    return deliveryOrderDao.dbGetDoNewV12(config, params);
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

    public DBResponse<List<DeliveriesOrderResponseDTOV1>> updDoNew(String _sessionId, GetDoNewV12 params) {
        DBResponse<List<DeliveriesOrderResponseDTOV1>> response;
        try {
            final DBConfig config = getConfig(UPD_DO_NEW);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            MemFutureTask<DBResponse<List<DeliveriesOrderResponseDTOV1>>> memTask = new MemFutureTask<DBResponse<List<DeliveriesOrderResponseDTOV1>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<DeliveriesOrderResponseDTOV1>>> dbTask = new DBFutureTask<DBResponse<List<DeliveriesOrderResponseDTOV1>>>() {
                @Override
                public DBResponse<List<DeliveriesOrderResponseDTOV1>> get() {
                    return deliveryOrderDao.dbGetDoNewV12(config, params);
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

    public DBResponse<List<GetDoNewResp>> getDoNewV2(String _sessionId, GetDoNewV2 params) {
        DBResponse<List<GetDoNewResp>> response;
        try {
            final DBConfig config = getConfig(GET_DO_NEW_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDoNewResp>>> memTask = new MemFutureTask<DBResponse<List<GetDoNewResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDoNewResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDoNewResp>>>() {
                @Override
                public DBResponse<List<GetDoNewResp>> get() {
                    return deliveryOrderDao.dbGetDoNew(config, params);
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

    public DBResponse<List<GetDoNewResp>> getDoNewV7(String _sessionId, GetDoNewV7 params) {
        DBResponse<List<GetDoNewResp>> response;
        try {
            final DBConfig config = getConfig(GET_DO_NEW_V7);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDoNewResp>>> memTask = new MemFutureTask<DBResponse<List<GetDoNewResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDoNewResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDoNewResp>>>() {
                @Override
                public DBResponse<List<GetDoNewResp>> get() {
                    return deliveryOrderDao.dbGetDoNew(config, params);
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

    public DBResponse<List<GetDoNewResp>> getDoFulfillment(String _sessionId, GetDoFulfillment params) {
        DBResponse<List<GetDoNewResp>> response;
        try {
            final DBConfig config = getConfig(GET_DO_FULFILLMENT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetDoNewResp>>> memTask = new MemFutureTask<DBResponse<List<GetDoNewResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetDoNewResp>>> dbTask = new DBFutureTask<DBResponse<List<GetDoNewResp>>>() {
                @Override
                public DBResponse<List<GetDoNewResp>> get() {
                    return deliveryOrderDao.dbGetDoFulfillment(config, params);
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

    public DBResponse<List<GetSoFulfillmentResp>> getSoFulfillment(String _sessionId, GetSoFulfillment params) {
        DBResponse<List<GetSoFulfillmentResp>> response;
        try {
            final DBConfig config = getConfig(GET_SO_FULFILLMENT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetSoFulfillmentResp>>> memTask = new MemFutureTask<DBResponse<List<GetSoFulfillmentResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetSoFulfillmentResp>>> dbTask = new DBFutureTask<DBResponse<List<GetSoFulfillmentResp>>>() {
                @Override
                public DBResponse<List<GetSoFulfillmentResp>> get() {
                    return deliveryOrderDao.dbGetSoFulfillment(config, params);
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

    public DBResponse<List<GetStatusMappingResp>> getStatusMapping(String _sessionId, GetStatusMapping params) {
        DBResponse<List<GetStatusMappingResp>> response;
        try {
            final DBConfig config = getConfig(GET_STATUS_MAPPING);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetStatusMappingResp>>> memTask = new MemFutureTask<DBResponse<List<GetStatusMappingResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetStatusMappingResp>>> dbTask = new DBFutureTask<DBResponse<List<GetStatusMappingResp>>>() {
                @Override
                public DBResponse<List<GetStatusMappingResp>> get() {
                    return deliveryOrderDao.dbGetStatusMapping(config, params);
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

    public DBResponse<List<GetTrackingCodeResp>> getTrackingCode(String _sessionId, GetTrackingCode params) {
        DBResponse<List<GetTrackingCodeResp>> response;
        try {
            final DBConfig config = getConfig(GET_TRACKING_CODE);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetTrackingCodeResp>>> memTask = new MemFutureTask<DBResponse<List<GetTrackingCodeResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetTrackingCodeResp>>> dbTask = new DBFutureTask<DBResponse<List<GetTrackingCodeResp>>>() {
                @Override
                public DBResponse<List<GetTrackingCodeResp>> get() {
                    return deliveryOrderDao.dbTrackingCode(config, params);
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

    public DBResponse<List<GetTrackingCodeRespV2>> getTrackingCodeV2(String _sessionId, GetTrackingCode params) {
        DBResponse<List<GetTrackingCodeRespV2>> response;
        try {
            final DBConfig config = getConfig(GET_TRACKING_CODE_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetTrackingCodeRespV2>>> memTask = new MemFutureTask<DBResponse<List<GetTrackingCodeRespV2>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetTrackingCodeRespV2>>> dbTask = new DBFutureTask<DBResponse<List<GetTrackingCodeRespV2>>>() {
                @Override
                public DBResponse<List<GetTrackingCodeRespV2>> get() {
                    return deliveryOrderDao.dbTrackingCodeV2(config, params);
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

    public DBResponse<List<GetTrackingCodeRespV3>> getTrackingCodeV3(String _sessionId, GetTrackingCode params) {
        DBResponse<List<GetTrackingCodeRespV3>> response;
        try {
            final DBConfig config = getConfig(GET_TRACKING_CODE_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetTrackingCodeRespV3>>> memTask = new MemFutureTask<DBResponse<List<GetTrackingCodeRespV3>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetTrackingCodeRespV3>>> dbTask = new DBFutureTask<DBResponse<List<GetTrackingCodeRespV3>>>() {
                @Override
                public DBResponse<List<GetTrackingCodeRespV3>> get() {
                    return deliveryOrderDao.dbTrackingCodeV3(config, params);
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

    public DBResponse<List<GetShippingExportResp>> getShippingExportCSV(String _sessionId, GetShippingExport params) {
        DBResponse<List<GetShippingExportResp>> response;
        try {
            final DBConfig config = getConfig(GET_SHIPPING_EXPORT_CSV);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetShippingExportResp>>> memTask = new MemFutureTask<DBResponse<List<GetShippingExportResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetShippingExportResp>>> dbTask = new DBFutureTask<DBResponse<List<GetShippingExportResp>>>() {
                @Override
                public DBResponse<List<GetShippingExportResp>> get() {
                    return deliveryOrderDao.dbShippingExportCSV(config, params);
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

    public DBResponse<List<GetShippingResp>> getShipping(String _sessionId, GetShipping params) {
        DBResponse<List<GetShippingResp>> response;
        try {
            final DBConfig config = getConfig(GET_SHIPPING);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetShippingResp>>> memTask = new MemFutureTask<DBResponse<List<GetShippingResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetShippingResp>>> dbTask = new DBFutureTask<DBResponse<List<GetShippingResp>>>() {
                @Override
                public DBResponse<List<GetShippingResp>> get() {
                    return deliveryOrderDao.dbShipping(config, params);
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

    public DBResponse<List<GetNeighbordhoodResp>> getNeighborhood(String _sessionId, GetNeighbordhood params) {
        DBResponse<List<GetNeighbordhoodResp>> response;
        try {
            final DBConfig config = getConfig(GET_NEIGHBORHOOD);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetNeighbordhoodResp>>> memTask = new MemFutureTask<DBResponse<List<GetNeighbordhoodResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetNeighbordhoodResp>>> dbTask = new DBFutureTask<DBResponse<List<GetNeighbordhoodResp>>>() {
                @Override
                public DBResponse<List<GetNeighbordhoodResp>> get() {
                    return deliveryOrderDao.dbGetNeighborhood(config, params);
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

    public DBResponse<List<NeighborhoodResponseDTO>> getNeighborhoodV1(String _sessionId, GetNeighbordhood params) {
        DBResponse<List<NeighborhoodResponseDTO>> response;
        try {
            final DBConfig config = getConfig(GET_NEIGHBORHOOD_V1);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<NeighborhoodResponseDTO>>> memTask = new MemFutureTask<DBResponse<List<NeighborhoodResponseDTO>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<NeighborhoodResponseDTO>>> dbTask = new DBFutureTask<DBResponse<List<NeighborhoodResponseDTO>>>() {
                @Override
                public DBResponse<List<NeighborhoodResponseDTO>> get() {
                    return deliveryOrderDao.dbGetNeighborhoodV1(config, params);
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

    public DBResponse<List<GetLocationResp>> getLocationFull(String _sessionId, GetLocation params) {
        DBResponse<List<GetLocationResp>> response;
        try {
            final DBConfig config = getConfig(GET_LOCATION_FULL);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetLocationResp>>> memTask = new MemFutureTask<DBResponse<List<GetLocationResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetLocationResp>>> dbTask = new DBFutureTask<DBResponse<List<GetLocationResp>>>() {
                @Override
                public DBResponse<List<GetLocationResp>> get() {
                    return deliveryOrderDao.dbLocationFull(config, params);
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

    public DBResponse<List<GetLogSendSmsDeliveryOrderResp>> getLogSendSmsDeliveryOrder(String _sessionId,
                                                                                       GetLogSendSmsDeliveryOrder params) {
        DBResponse<List<GetLogSendSmsDeliveryOrderResp>> response;
        try {
            final DBConfig config = getConfig(GET_LOG_SEND_SMS_DELIVERY_ORDER);
            try {
                ObjectMapper mapper = new ObjectMapper();
                logger.info("=====================config========================\r\n{}", mapper.writeValueAsString(config));
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage(), e);
            }
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetLogSendSmsDeliveryOrderResp>>> memTask = new MemFutureTask<DBResponse<List<GetLogSendSmsDeliveryOrderResp>>>() {
                @Override
                public DBResponse get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetLogSendSmsDeliveryOrderResp>>> dbTask = new DBFutureTask<DBResponse<List<GetLogSendSmsDeliveryOrderResp>>>() {
                @Override
                public DBResponse<List<GetLogSendSmsDeliveryOrderResp>> get() {
                    return deliveryOrderDao.dbGetLogSendSmsDeliveryOrder(config, params);
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
