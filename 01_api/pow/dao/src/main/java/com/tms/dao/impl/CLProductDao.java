package com.tms.dao.impl;

import com.tms.config.DBConfig;
import com.tms.dao.TMSBeanPropertyRowMapper;
import com.tms.dto.*;
import com.tms.entity.CLActive;
import com.tms.entity.PDProduct;
import com.tms.entity.ProductInStock;
import com.tms.entity.User;
import com.tms.utils.AppUtils;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class CLProductDao extends BasicDao {


    public DBResponse<List<PDProduct>> dbGetProduct(DBConfig config, GetProductParams params) {

        DBResponse<List<PDProduct>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(PDProduct.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<PDProduct>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<PDProduct>> dbGetProductV2(DBConfig config, GetProductV2 params) {

        DBResponse<List<PDProduct>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(PDProduct.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<PDProduct>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetProductComboResp>> dbGetProductCombo(DBConfig config, GetProductCombo params) {

        DBResponse<List<GetProductComboResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetProductComboResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetProductComboResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<PDProduct>> dbGetMultiProduct(DBConfig config, GetMultiProduct params) {

        DBResponse<List<PDProduct>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(PDProduct.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<PDProduct>)map.get("c_cursor"));

        return rs;
    }


    public DBResponse<List<PDProduct>> dbGetProductByName(DBConfig config, String name) {

        DBResponse<List<PDProduct>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_name", Types.VARCHAR),
                        new SqlParameter("inout_rc_name",  Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(PDProduct.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_name", name)
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setResult((List<PDProduct>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<ProductInStock>> dbGetProductInStock(DBConfig config, GetProductInStockParams params) {

        DBResponse<List<ProductInStock>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(ProductInStock.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);
        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));

        rs.setResult((List<ProductInStock>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<PDProduct>> dbGetProductList(DBConfig config, Integer campaignId) {

        DBResponse<List<PDProduct>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_campaignid", Types.INTEGER),
                        new SqlParameter("inout_rc_name",  Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(PDProduct.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_campaignid", campaignId)
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setResult((List<PDProduct>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetProductAttributeResp>> dbGetProductAttribute(DBConfig config, GetProductAttribute params) {

        DBResponse<List<GetProductAttributeResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetProductAttributeResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetProductAttributeResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetProductMappingResp>> dbGetProductMapping(DBConfig config, GetProductMapping params) {

        DBResponse<List<GetProductMappingResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetProductMappingResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetProductMappingResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetProductMappingResp>> dbGetProductMappingV2(DBConfig config, GetProductMappingV2 params) {

        DBResponse<List<GetProductMappingResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetProductMappingResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetProductMappingResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetPartnerResp>> dbGetPartner(DBConfig config, GetPartner params) {

        DBResponse<List<GetPartnerResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetPartnerResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetPartnerResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrganizationPartnerResp>> dbGetOrgPartner(DBConfig config, GetOrganizationPartner params) {

        DBResponse<List<GetOrganizationPartnerResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrganizationPartnerResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrganizationPartnerResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetStockProvinceProductResp>> dbGetStockByProductAndProvince(DBConfig config, GetStockProvinceProduct params) {

        DBResponse<List<GetStockProvinceProductResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetStockProvinceProductResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetStockProvinceProductResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetWareHouseResp>> dbGetWarehouse(DBConfig config, GetWareHouse params) {

        DBResponse<List<GetWareHouseResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetWareHouseResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetWareHouseResp>)map.get("c_cursor"));

        return rs;
    }
    
    public DBResponse<List<GetMappingFFMLastSmileResp>> dbGetMappingFFMLastSmileResp(DBConfig config, GetMappingFFMLastSmile params) {

        DBResponse<List<GetMappingFFMLastSmileResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetMappingFFMLastSmileResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetMappingFFMLastSmileResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCdrResp>> dbGetCdr(DBConfig config, GetCdr params) {

        DBResponse<List<GetCdrResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCdrResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCdrResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCdrResp>> dbGetCdr(DBConfig config, GetCdrV2 params) {

        DBResponse<List<GetCdrResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCdrResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCdrResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCdrResp>> dbGetCdrV2(DBConfig config, GetCdrV2 params) {

        DBResponse<List<GetCdrResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCdrResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCdrResp>)map.get("c_cursor"));

        return rs;
    }
    
    public DBResponse<List<GetCdrResp>> dbGetCdrV3(DBConfig config, GetCdrV3 params) {

        DBResponse<List<GetCdrResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCdrResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCdrResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetPickupResp>> dbGetPickup(DBConfig config, GetPickup params) {

        DBResponse<List<GetPickupResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetPickupResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetPickupResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetSaleOrderResp>> dbSaleOrder(DBConfig config, GetSaleOrder params) {

        DBResponse<List<GetSaleOrderResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetSaleOrderResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetSaleOrderResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetAgentRateResp>> dbAgentRate(DBConfig config, GetAgentRate params) {

        DBResponse<List<GetAgentRateResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetAgentRateResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetAgentRateResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetLogAgentActivityResp>> dbLogAgentActivity(DBConfig config, GetLogAgentActivity params) {

        DBResponse<List<GetLogAgentActivityResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetLogAgentActivityResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetLogAgentActivityResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCpCallListSkillResp>> dbGetCpCallListSkill(DBConfig config, GetCpCallListSkill params) {

        DBResponse<List<GetCpCallListSkillResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCpCallListSkillResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCpCallListSkillResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCpCallListSkillV7Resp>> dbGetCpCallListSkill(DBConfig config, GetCpCallListSkillV7 params) {

        DBResponse<List<GetCpCallListSkillV7Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCpCallListSkillV7Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCpCallListSkillV7Resp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetPostaCodeResp>> dbPostaCode(DBConfig config, GetPostaCode params) {

        DBResponse<List<GetPostaCodeResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetPostaCodeResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetPostaCodeResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetMtkDataResp>> dbGetMtkData(DBConfig config, GetMtkData params) {

        DBResponse<List<GetMtkDataResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetMtkDataResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetMtkDataResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetPostaCodeV2Resp>> dbPostaCode(DBConfig config, GetPostaCodeV2 params) {

        DBResponse<List<GetPostaCodeV2Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetPostaCodeV2Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);


        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetPostaCodeV2Resp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetAgentRateDailyResp>> dbGetAgentRateDaily(DBConfig config, GetAgentRateDailyParams params) {

        DBResponse<List<GetAgentRateDailyResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR),
                        new SqlOutParameter("out_loginstatus", Types.INTEGER)
                )
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetAgentRateDailyResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_loginstatus"));
        if (rs.getErrorCode() == 0) {
            rs.setResult((List<GetAgentRateDailyResp>)map.get("c_cursor"));
        }

        return rs;
    }
}
