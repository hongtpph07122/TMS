package com.tms.service.impl;

import java.util.List;

import com.tms.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.config.DBConfig;
import com.tms.dao.impl.CLProductDao;
import com.tms.entity.PDProduct;
import com.tms.entity.ProductInStock;
import com.tms.exception.ConfigNotFoundException;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;

@Service
public class CLProductService extends BaseService {

    private Logger logger = LoggerFactory.getLogger(CLProductService.class);

    private final static String GET_PRODUCT ="get_product";
    private final static String GET_PRODUCT_V2 ="get_product_v2";
    private final static String GET_PRODUCT_COMBO ="get_product_combo";
    private final static String GET_MULTI_PRODUCT ="get_multi_product";
    private final static String GET_PRODUCT_BY_NAME ="getproductbyname";
    private final static String GET_PRODUCT_IN_STOCK ="get_product_instock";
    private final static String GET_PRODUCT_LIST ="getproductlist";
    private final static String GET_PRODUCT_ATTRIBUTE ="get_product_attribute";
    private final static String GET_PRODUCT_MAPPING ="get_product_mapping";
    private final static String GET_PRODUCT_MAPPING_V2 ="get_product_mapping_v2";
    private final static String GET_PARTNER ="get_partner";

    private final static String GET_ORG_PARTNER ="get_org_partner";
    private final static String GET_STOCK_PROVINCE_PRODUCT ="get_stockby_province_product";
    private final static String GET_WARE_HOURSE = "get_warehouse";
    private final static String GET_MAPPING_FFM_LASTSMILE = "get_mapping_ffm_lastsmile";
    private final static String GET_CDR = "get_cdr";
    private final static String GET_CDR_V2 = "get_cdr_v2";
    private final static String GET_CDR_V3 = "get_cdr_v3";


    private final static String GET_PICK_UP = "get_pickup";
    private final static String GET_SALE_ORDER = "get_sale_order";
    private final static String GET_AGENT_RATE = "get_agent_rate";
    private final static String GET_LOG_AGENT_ACTIVITY = "get_log_agent_activity";
    private final static String GET_CP_CALLINGLIST_AGSKILL = "get_cp_callinglist_agskill";
    private final static String GET_CP_CALLINGLIST_AGSKILL_V7 = "get_cp_callinglist_agskill_V7";

    private final static String GET_POSTA_CODE = "get_postalcode";
    private final static String GET_MTK_DATA = "get_mkt_metadata";
    private final static String GET_POSTA_CODE_V2 = "get_postal_code";

    private final static String GET_AGENT_RATE_DAILY = "get_agent_rate_daily";



    @Autowired
    private CLProductDao clProductDao;



    public DBResponse<List<PDProduct>> getProduct(String _sessionId, GetProductParams params) {
        DBResponse<List<PDProduct>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<PDProduct>>> memTask = new MemFutureTask<DBResponse<List<PDProduct>>>() {

                @Override
                public DBResponse<List<PDProduct>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<PDProduct>>> dbTask = new DBFutureTask<DBResponse<List<PDProduct>>>() {
                @Override
                public DBResponse<List<PDProduct>> get() {
                    return clProductDao.dbGetProduct(config, params);
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

    public DBResponse<List<PDProduct>> getProductV2(String _sessionId, GetProductV2 params) {
        DBResponse<List<PDProduct>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<PDProduct>>> memTask = new MemFutureTask<DBResponse<List<PDProduct>>>() {

                @Override
                public DBResponse<List<PDProduct>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<PDProduct>>> dbTask = new DBFutureTask<DBResponse<List<PDProduct>>>() {
                @Override
                public DBResponse<List<PDProduct>> get() {
                    return clProductDao.dbGetProductV2(config, params);
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

    public DBResponse<List<GetProductComboResp>> getProductCombo(String _sessionId, GetProductCombo params) {
        DBResponse<List<GetProductComboResp>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT_COMBO);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetProductComboResp>>> memTask = new MemFutureTask<DBResponse<List<GetProductComboResp>>>() {

                @Override
                public DBResponse<List<GetProductComboResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetProductComboResp>>> dbTask = new DBFutureTask<DBResponse<List<GetProductComboResp>>>() {
                @Override
                public DBResponse<List<GetProductComboResp>> get() {
                    return clProductDao.dbGetProductCombo(config, params);
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


    public DBResponse<List<PDProduct>> getMultiProduct(String _sessionId, GetMultiProduct params) {
        DBResponse<List<PDProduct>> response;
        try {
            final DBConfig config = getConfig(GET_MULTI_PRODUCT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<PDProduct>>> memTask = new MemFutureTask<DBResponse<List<PDProduct>>>() {

                @Override
                public DBResponse<List<PDProduct>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<PDProduct>>> dbTask = new DBFutureTask<DBResponse<List<PDProduct>>>() {
                @Override
                public DBResponse<List<PDProduct>> get() {
                    return clProductDao.dbGetMultiProduct(config, params);
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

    public DBResponse<List<PDProduct>> getProductByName(String _sessionId, String name) {
        DBResponse<List<PDProduct>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT_BY_NAME);
            AppUtils.printInput(logger, _sessionId, config, null, name);

            FutureTask<DBResponse<List<PDProduct>>> memTask = new MemFutureTask<DBResponse<List<PDProduct>>>() {

                @Override
                public DBResponse<List<PDProduct>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<PDProduct>>> dbTask = new DBFutureTask<DBResponse<List<PDProduct>>>() {
                @Override
                public DBResponse<List<PDProduct>> get() {
                    return clProductDao.dbGetProductByName(config, name);
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


    public DBResponse<List<ProductInStock>> getProductInStock(String _sessionId, GetProductInStockParams params) {
        DBResponse<List<ProductInStock>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT_IN_STOCK);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<ProductInStock>>> memTask = new MemFutureTask<DBResponse<List<ProductInStock>>>() {

                @Override
                public DBResponse<List<ProductInStock>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<ProductInStock>>> dbTask = new DBFutureTask<DBResponse<List<ProductInStock>>>() {
                @Override
                public DBResponse<List<ProductInStock>> get() {
                    return clProductDao.dbGetProductInStock(config, params);
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

    public DBResponse<List<PDProduct>> getProductList(String _sessionId, Integer campaignId) {

        DBResponse<List<PDProduct>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT_LIST);
            AppUtils.printInput(logger, _sessionId, config, null, campaignId);

            FutureTask<DBResponse<List<PDProduct>>> memTask = new MemFutureTask<DBResponse<List<PDProduct>>>() {

                @Override
                public DBResponse<List<PDProduct>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<PDProduct>>> dbTask = new DBFutureTask<DBResponse<List<PDProduct>>>() {
                @Override
                public DBResponse<List<PDProduct>> get() {
                    return clProductDao.dbGetProductList(config, campaignId);
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

    public DBResponse<List<GetProductAttributeResp>> getProductAttribute(String _sessionId, GetProductAttribute params) {
        DBResponse<List<GetProductAttributeResp>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT_ATTRIBUTE);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetProductAttributeResp>>> memTask = new MemFutureTask<DBResponse<List<GetProductAttributeResp>>>() {

                @Override
                public DBResponse<List<GetProductAttributeResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetProductAttributeResp>>> dbTask = new DBFutureTask<DBResponse<List<GetProductAttributeResp>>>() {
                @Override
                public DBResponse<List<GetProductAttributeResp>> get() {
                    return clProductDao.dbGetProductAttribute(config, params);
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

    public DBResponse<List<GetProductMappingResp>> getProductMapping(String _sessionId, GetProductMapping params) {
        DBResponse<List<GetProductMappingResp>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT_MAPPING);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetProductMappingResp>>> memTask = new MemFutureTask<DBResponse<List<GetProductMappingResp>>>() {

                @Override
                public DBResponse<List<GetProductMappingResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetProductMappingResp>>> dbTask = new DBFutureTask<DBResponse<List<GetProductMappingResp>>>() {
                @Override
                public DBResponse<List<GetProductMappingResp>> get() {
                    return clProductDao.dbGetProductMapping(config, params);
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

    public DBResponse<List<GetProductMappingResp>> getProductMappingV2(String _sessionId, GetProductMappingV2 params) {
        DBResponse<List<GetProductMappingResp>> response;
        try {
            final DBConfig config = getConfig(GET_PRODUCT_MAPPING_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetProductMappingResp>>> memTask = new MemFutureTask<DBResponse<List<GetProductMappingResp>>>() {

                @Override
                public DBResponse<List<GetProductMappingResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetProductMappingResp>>> dbTask = new DBFutureTask<DBResponse<List<GetProductMappingResp>>>() {
                @Override
                public DBResponse<List<GetProductMappingResp>> get() {
                    return clProductDao.dbGetProductMappingV2(config, params);
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


    public DBResponse<List<GetPartnerResp>> getPartner(String _sessionId, GetPartner params) {
        DBResponse<List<GetPartnerResp>> response;
        try {
            final DBConfig config = getConfig(GET_PARTNER);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetPartnerResp>>> memTask = new MemFutureTask<DBResponse<List<GetPartnerResp>>>() {

                @Override
                public DBResponse<List<GetPartnerResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetPartnerResp>>> dbTask = new DBFutureTask<DBResponse<List<GetPartnerResp>>>() {
                @Override
                public DBResponse<List<GetPartnerResp>> get() {
                    return clProductDao.dbGetPartner(config, params);
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

    public DBResponse<List<GetOrganizationPartnerResp>> getOrgPartner(String _sessionId, GetOrganizationPartner params) {
        DBResponse<List<GetOrganizationPartnerResp>> response;
        try {
            final DBConfig config = getConfig(GET_ORG_PARTNER);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetOrganizationPartnerResp>>> memTask = new MemFutureTask<DBResponse<List<GetOrganizationPartnerResp>>>() {

                @Override
                public DBResponse<List<GetOrganizationPartnerResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetOrganizationPartnerResp>>> dbTask = new DBFutureTask<DBResponse<List<GetOrganizationPartnerResp>>>() {
                @Override
                public DBResponse<List<GetOrganizationPartnerResp>> get() {
                    return clProductDao.dbGetOrgPartner(config, params);
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

    public DBResponse<List<GetStockProvinceProductResp>> getStockByProductAndProvince(String _sessionId, GetStockProvinceProduct params) {
        DBResponse<List<GetStockProvinceProductResp>> response;
        try {
            final DBConfig config = getConfig(GET_STOCK_PROVINCE_PRODUCT);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetStockProvinceProductResp>>> memTask = new MemFutureTask<DBResponse<List<GetStockProvinceProductResp>>>() {

                @Override
                public DBResponse<List<GetStockProvinceProductResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetStockProvinceProductResp>>> dbTask = new DBFutureTask<DBResponse<List<GetStockProvinceProductResp>>>() {
                @Override
                public DBResponse<List<GetStockProvinceProductResp>> get() {
                    return clProductDao.dbGetStockByProductAndProvince(config, params);
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

    public DBResponse<List<GetWareHouseResp>> getWareHouse(String _sessionId, GetWareHouse params) {
        DBResponse<List<GetWareHouseResp>> response;
        try {
            final DBConfig config = getConfig(GET_WARE_HOURSE);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetWareHouseResp>>> memTask = new MemFutureTask<DBResponse<List<GetWareHouseResp>>>() {

                @Override
                public DBResponse<List<GetWareHouseResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetWareHouseResp>>> dbTask = new DBFutureTask<DBResponse<List<GetWareHouseResp>>>() {
                @Override
                public DBResponse<List<GetWareHouseResp>> get() {
                    return clProductDao.dbGetWarehouse(config, params);
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
    
    public DBResponse<List<GetMappingFFMLastSmileResp>> getMappingFFMLastsmile(String _sessionId , GetMappingFFMLastSmile params) {
        DBResponse<List<GetMappingFFMLastSmileResp>> response;
        try {
            final DBConfig config = getConfig(GET_MAPPING_FFM_LASTSMILE);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetMappingFFMLastSmileResp>>> memTask = new MemFutureTask<DBResponse<List<GetMappingFFMLastSmileResp>>>() {

                @Override
                public DBResponse<List<GetMappingFFMLastSmileResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetMappingFFMLastSmileResp>>> dbTask = new DBFutureTask<DBResponse<List<GetMappingFFMLastSmileResp>>>() {
                @Override
                public DBResponse<List<GetMappingFFMLastSmileResp>> get() {
                    return clProductDao.dbGetMappingFFMLastSmileResp(config, params);
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

    public DBResponse<List<GetCdrResp>> getCdr(String _sessionId, GetCdr params) {
        DBResponse<List<GetCdrResp>> response;
        try {
            final DBConfig config = getConfig(GET_CDR);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCdrResp>>> memTask = new MemFutureTask<DBResponse<List<GetCdrResp>>>() {

                @Override
                public DBResponse<List<GetCdrResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCdrResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCdrResp>>>() {
                @Override
                public DBResponse<List<GetCdrResp>> get() {
                    return clProductDao.dbGetCdr(config, params);
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

    public DBResponse<List<GetCdrResp>> getCdr(String _sessionId, GetCdrV2 params) {
        DBResponse<List<GetCdrResp>> response;
        try {
            final DBConfig config = getConfig(GET_CDR);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCdrResp>>> memTask = new MemFutureTask<DBResponse<List<GetCdrResp>>>() {

                @Override
                public DBResponse<List<GetCdrResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCdrResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCdrResp>>>() {
                @Override
                public DBResponse<List<GetCdrResp>> get() {
                    return clProductDao.dbGetCdr(config, params);
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

    public DBResponse<List<GetCdrResp>> getCdrV2(String _sessionId, GetCdrV2 params) {
        DBResponse<List<GetCdrResp>> response;
        try {
            final DBConfig config = getConfig(GET_CDR_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCdrResp>>> memTask = new MemFutureTask<DBResponse<List<GetCdrResp>>>() {

                @Override
                public DBResponse<List<GetCdrResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCdrResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCdrResp>>>() {
                @Override
                public DBResponse<List<GetCdrResp>> get() {
                    return clProductDao.dbGetCdrV2(config, params);
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
    
    public DBResponse<List<GetCdrResp>> getCdrV3(String _sessionId, GetCdrV3 params) {
        DBResponse<List<GetCdrResp>> response;
        try {
            final DBConfig config = getConfig(GET_CDR_V3);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCdrResp>>> memTask = new MemFutureTask<DBResponse<List<GetCdrResp>>>() {

                @Override
                public DBResponse<List<GetCdrResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCdrResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCdrResp>>>() {
                @Override
                public DBResponse<List<GetCdrResp>> get() {
                    return clProductDao.dbGetCdrV3(config, params);
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


    public DBResponse<List<GetPickupResp>> getPickup(String _sessionId, GetPickup params) {
        DBResponse<List<GetPickupResp>> response;
        try {
            final DBConfig config = getConfig(GET_PICK_UP);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetPickupResp>>> memTask = new MemFutureTask<DBResponse<List<GetPickupResp>>>() {

                @Override
                public DBResponse<List<GetPickupResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetPickupResp>>> dbTask = new DBFutureTask<DBResponse<List<GetPickupResp>>>() {
                @Override
                public DBResponse<List<GetPickupResp>> get() {
                    return clProductDao.dbGetPickup(config, params);
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

    public DBResponse<List<GetSaleOrderResp>> getSaleOrder(String _sessionId, GetSaleOrder params) {
        DBResponse<List<GetSaleOrderResp>> response;
        try {
            final DBConfig config = getConfig(GET_SALE_ORDER);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetSaleOrderResp>>> memTask = new MemFutureTask<DBResponse<List<GetSaleOrderResp>>>() {

                @Override
                public DBResponse<List<GetSaleOrderResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetSaleOrderResp>>> dbTask = new DBFutureTask<DBResponse<List<GetSaleOrderResp>>>() {
                @Override
                public DBResponse<List<GetSaleOrderResp>> get() {
                    return clProductDao.dbSaleOrder(config, params);
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

    public DBResponse<List<GetPostaCodeResp>> getPostaCode(String _sessionId, GetPostaCode params) {
        DBResponse<List<GetPostaCodeResp>> response;
        try {
            final DBConfig config = getConfig(GET_POSTA_CODE);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetPostaCodeResp>>> memTask = new MemFutureTask<DBResponse<List<GetPostaCodeResp>>>() {

                @Override
                public DBResponse<List<GetPostaCodeResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetPostaCodeResp>>> dbTask = new DBFutureTask<DBResponse<List<GetPostaCodeResp>>>() {
                @Override
                public DBResponse<List<GetPostaCodeResp>> get() {
                    return clProductDao.dbPostaCode(config, params);
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

    public DBResponse<List<GetAgentRateResp>> getAgentRate(String _sessionId, GetAgentRate params) {
        DBResponse<List<GetAgentRateResp>> response;
        try {
            final DBConfig config = getConfig(GET_AGENT_RATE);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetAgentRateResp>>> memTask = new MemFutureTask<DBResponse<List<GetAgentRateResp>>>() {

                @Override
                public DBResponse<List<GetAgentRateResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetAgentRateResp>>> dbTask = new DBFutureTask<DBResponse<List<GetAgentRateResp>>>() {
                @Override
                public DBResponse<List<GetAgentRateResp>> get() {
                    return clProductDao.dbAgentRate(config, params);
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

    public DBResponse<List<GetLogAgentActivityResp>> getLogAgentActivity(String _sessionId, GetLogAgentActivity params) {
        DBResponse<List<GetLogAgentActivityResp>> response;
        try {
            final DBConfig config = getConfig(GET_LOG_AGENT_ACTIVITY);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetLogAgentActivityResp>>> memTask = new MemFutureTask<DBResponse<List<GetLogAgentActivityResp>>>() {

                @Override
                public DBResponse<List<GetLogAgentActivityResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetLogAgentActivityResp>>> dbTask = new DBFutureTask<DBResponse<List<GetLogAgentActivityResp>>>() {
                @Override
                public DBResponse<List<GetLogAgentActivityResp>> get() {
                    return clProductDao.dbLogAgentActivity(config, params);
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

    public DBResponse<List<GetCpCallListSkillResp>> getCpCallListSkill(String _sessionId, GetCpCallListSkill params) {
        DBResponse<List<GetCpCallListSkillResp>> response;
        try {
            final DBConfig config = getConfig(GET_CP_CALLINGLIST_AGSKILL);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCpCallListSkillResp>>> memTask = new MemFutureTask<DBResponse<List<GetCpCallListSkillResp>>>() {

                @Override
                public DBResponse<List<GetCpCallListSkillResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCpCallListSkillResp>>> dbTask = new DBFutureTask<DBResponse<List<GetCpCallListSkillResp>>>() {
                @Override
                public DBResponse<List<GetCpCallListSkillResp>> get() {
                    return clProductDao.dbGetCpCallListSkill(config, params);
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

    public DBResponse<List<GetCpCallListSkillV7Resp>> getCpCallListSkillV7(String _sessionId, GetCpCallListSkillV7 params) {
        DBResponse<List<GetCpCallListSkillV7Resp>> response;
        try {
            final DBConfig config = getConfig(GET_CP_CALLINGLIST_AGSKILL_V7);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetCpCallListSkillV7Resp>>> memTask = new MemFutureTask<DBResponse<List<GetCpCallListSkillV7Resp>>>() {

                @Override
                public DBResponse<List<GetCpCallListSkillV7Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetCpCallListSkillV7Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetCpCallListSkillV7Resp>>>() {
                @Override
                public DBResponse<List<GetCpCallListSkillV7Resp>> get() {
                    return clProductDao.dbGetCpCallListSkill(config, params);
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

    public DBResponse<List<GetMtkDataResp>> getMtkData(String _sessionId, GetMtkData params) {
        DBResponse<List<GetMtkDataResp>> response;
        try {
            final DBConfig config = getConfig(GET_MTK_DATA);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetMtkDataResp>>> memTask = new MemFutureTask<DBResponse<List<GetMtkDataResp>>>() {

                @Override
                public DBResponse<List<GetMtkDataResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetMtkDataResp>>> dbTask = new DBFutureTask<DBResponse<List<GetMtkDataResp>>>() {
                @Override
                public DBResponse<List<GetMtkDataResp>> get() {
                    return clProductDao.dbGetMtkData(config, params);
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

    public DBResponse<List<GetPostaCodeV2Resp>> getPostaCodeV2(String _sessionId, GetPostaCodeV2 params) {
        DBResponse<List<GetPostaCodeV2Resp>> response;
        try {
            final DBConfig config = getConfig(GET_POSTA_CODE_V2);
            AppUtils.printInput(logger, _sessionId, config, null, params);

            FutureTask<DBResponse<List<GetPostaCodeV2Resp>>> memTask = new MemFutureTask<DBResponse<List<GetPostaCodeV2Resp>>>() {

                @Override
                public DBResponse<List<GetPostaCodeV2Resp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetPostaCodeV2Resp>>> dbTask = new DBFutureTask<DBResponse<List<GetPostaCodeV2Resp>>>() {
                @Override
                public DBResponse<List<GetPostaCodeV2Resp>> get() {
                    return clProductDao.dbPostaCode(config, params);
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

    public DBResponse<List<GetAgentRateDailyResp>> getAgentRateDaily (String _sessionId, GetAgentRateDailyParams params){
        DBResponse<List<GetAgentRateDailyResp>> response;
        try{
            final DBConfig config = getConfig(GET_AGENT_RATE_DAILY);
            AppUtils.printInput(logger, _sessionId, config, null, params);
            FutureTask<DBResponse<List<GetAgentRateDailyResp>>> memTask = new MemFutureTask<DBResponse<List<GetAgentRateDailyResp>>>() {

                @Override
                public DBResponse<List<GetAgentRateDailyResp>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<GetAgentRateDailyResp>>> dbTask = new DBFutureTask<DBResponse<List<GetAgentRateDailyResp>>>() {
                @Override
                public DBResponse<List<GetAgentRateDailyResp>> get() {
                    return clProductDao.dbGetAgentRateDaily(config, params);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);
        } catch (ConfigNotFoundException e){
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            logger.error(response.toString());
        }

        return response;
    }

}
