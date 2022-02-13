package com.tms.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.config.DBConfig;
import com.tms.dao.TMSBeanPropertyRowMapper;
import com.tms.dto.*;
import com.tms.entity.DeliveryOrder;
import com.tms.entity.SaleOrder;
import com.tms.model.Response.DeliveriesOrderResponseDTO;
import com.tms.model.Response.DeliveriesOrderResponseDTOV1;
import com.tms.model.Response.NeighborhoodResponseDTO;
import com.tms.utils.AppUtils;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class DeliveryOrderDao extends BasicDao {

    public DBResponse<List<DeliveryOrder>> dbGetDO(DBConfig config, GetDOParams params) {

        DBResponse<List<DeliveryOrder>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(DeliveryOrder.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<DeliveryOrder>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDefaultDOResp>> dbDefaultGetDO(DBConfig config, GetDefaultDO params) {

        DBResponse<List<GetDefaultDOResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDefaultDOResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDefaultDOResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDefaultDOResp>> dbDefaultGetDOV2(DBConfig config, GetDefaultDOV2 params) {

        DBResponse<List<GetDefaultDOResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDefaultDOResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDefaultDOResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDefaultDOResp>> dbDefaultGetDOV3(DBConfig config, GetDefaultDOV3 params) {

        DBResponse<List<GetDefaultDOResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDefaultDOResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDefaultDOResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<SaleOrder>> dbGetSO(DBConfig config, GetSOParams params) {

        DBResponse<List<SaleOrder>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(SaleOrder.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<SaleOrder>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<SaleOrder>> dbGetSO(DBConfig config, GetSOV2 params) {

        DBResponse<List<SaleOrder>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(SaleOrder.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<SaleOrder>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<SaleOrder>> dbGetSO(DBConfig config, GetSOV3 params) {

        DBResponse<List<SaleOrder>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(SaleOrder.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<SaleOrder>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<SaleOrder>> dbGetSO(DBConfig config, GetSOV4 params) {

        DBResponse<List<SaleOrder>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(SaleOrder.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<SaleOrder>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetSoItemResp>> dbGetSOItem(DBConfig config, GetSoItem params) {

        DBResponse<List<GetSoItemResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetSoItemResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetSoItemResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetSoItemResp>> dbGetSOItem(DBConfig config, GetSoItemV2 params) {

        DBResponse<List<GetSoItemResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetSoItemResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetSoItemResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<SaleOrder>> dbGetSOByProductId(DBConfig config, GetSOByProductParams params) {

        DBResponse<List<SaleOrder>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(SaleOrder.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<SaleOrder>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDoNewResp>> dbGetDoNew(DBConfig config, GetDoNew params) {

        DBResponse<List<GetDoNewResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDoNewResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDoNewResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<DeliveriesOrderResponseDTO>> dbGetDoNew(DBConfig config, GetDoNewV8 params) {

        DBResponse<List<DeliveriesOrderResponseDTO>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(DeliveriesOrderResponseDTO.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<DeliveriesOrderResponseDTO>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<DeliveriesOrderResponseDTO>> dbGetDoNewV10(DBConfig config, GetDoNewV10 params) {

        DBResponse<List<DeliveriesOrderResponseDTO>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(DeliveriesOrderResponseDTO.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<DeliveriesOrderResponseDTO>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<DeliveriesOrderResponseDTO>> dbGetDoNewV11(DBConfig config, GetDoNewV11 params) {

        DBResponse<List<DeliveriesOrderResponseDTO>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(DeliveriesOrderResponseDTO.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<DeliveriesOrderResponseDTO>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<DeliveriesOrderResponseDTOV1>> dbGetDoNewV12(DBConfig config, GetDoNewV12 params) {

        DBResponse<List<DeliveriesOrderResponseDTOV1>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(DeliveriesOrderResponseDTOV1.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<DeliveriesOrderResponseDTOV1>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDoNewResp>> dbGetDoNew(DBConfig config, GetDoNewV2 params) {

        DBResponse<List<GetDoNewResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDoNewResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDoNewResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDoNewResp>> dbGetDoNew(DBConfig config, GetDoNewV7 params) {

        DBResponse<List<GetDoNewResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDoNewResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDoNewResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDoNewResp>> dbGetDoFulfillment(DBConfig config, GetDoFulfillment params) {

        DBResponse<List<GetDoNewResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDoNewResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDoNewResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetSoFulfillmentResp>> dbGetSoFulfillment(DBConfig config, GetSoFulfillment params) {

        DBResponse<List<GetSoFulfillmentResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetSoFulfillmentResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetSoFulfillmentResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetStatusMappingResp>> dbGetStatusMapping(DBConfig config, GetStatusMapping params) {

        DBResponse<List<GetStatusMappingResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetStatusMappingResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetStatusMappingResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetTrackingCodeResp>> dbTrackingCode(DBConfig config, GetTrackingCode params) {

        DBResponse<List<GetTrackingCodeResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetTrackingCodeResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetTrackingCodeResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetTrackingCodeRespV2>> dbTrackingCodeV2(DBConfig config, GetTrackingCode params) {

        DBResponse<List<GetTrackingCodeRespV2>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetTrackingCodeRespV2.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetTrackingCodeRespV2>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetShippingExportResp>> dbShippingExportCSV(DBConfig config, GetShippingExport params) {

        DBResponse<List<GetShippingExportResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetShippingExportResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetShippingExportResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetShippingResp>> dbShipping(DBConfig config, GetShipping params) {

        DBResponse<List<GetShippingResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetShippingResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetShippingResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetNeighbordhoodResp>> dbGetNeighborhood(DBConfig config, GetNeighbordhood params) {

        DBResponse<List<GetNeighbordhoodResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetNeighbordhoodResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetNeighbordhoodResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<NeighborhoodResponseDTO>> dbGetNeighborhoodV1(DBConfig config, GetNeighbordhood params) {

        DBResponse<List<NeighborhoodResponseDTO>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(NeighborhoodResponseDTO.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<NeighborhoodResponseDTO>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetLocationResp>> dbLocationFull(DBConfig config, GetLocation params) {

        DBResponse<List<GetLocationResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetLocationResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetLocationResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetLogSendSmsDeliveryOrderResp>> dbGetLogSendSmsDeliveryOrder(DBConfig config,
                                                                                         GetLogSendSmsDeliveryOrder params) {
        ObjectMapper mapper = new ObjectMapper();

        DBResponse<List<GetLogSendSmsDeliveryOrderResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetLogSendSmsDeliveryOrderResp.class));

        try {
            logger.info("=====================params========================\r\n{}", mapper.writeValueAsString(params));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);
        try {
            logger.info("=====================in========================\r\n{}", mapper.writeValueAsString(in));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        Map map = simpleJdbcCall.execute(in);
        try {
            logger.info("=====================map========================\r\n{}", mapper.writeValueAsString(map));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetLogSendSmsDeliveryOrderResp>) map.get("c_cursor"));
        return rs;
    }

    public DBResponse<List<GetTrackingCodeRespV3>> dbTrackingCodeV3(DBConfig config, GetTrackingCode params) {

        DBResponse<List<GetTrackingCodeRespV3>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetTrackingCodeRespV3.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetTrackingCodeRespV3>) map.get("c_cursor"));

        return rs;
    }

}
