package com.tms.dao.impl;

import com.tms.config.DBConfig;
import com.tms.dao.TMSBeanPropertyRowMapper;
import com.tms.dto.*;
import com.tms.entity.CLCallback;
import com.tms.model.Request.LeadStatusCallbackWithTimeRequestDTO;
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
public class CLCallbackDao extends BasicDao {


    public DBResponse<List<CLCallback>> dbGetCallback(DBConfig config, GetCLCallback params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }


    public DBResponse<List<CLCallback>> dbGetCallbackV4(DBConfig config, GetCLCallbackV4 params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLCallback>> dbGetCallback(DBConfig config, GetCLCallbackV7 params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }


    public DBResponse<List<CLCallback>> dbGetCallbackByTime(DBConfig config, GetCallbackByTimeParams params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }
    public DBResponse<List<CLCallback>> dbGetCallbackByTimeV8(DBConfig config, GetCallbackByTimeParamsV8 params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLCallback>> dbGetCallbackByTime(DBConfig config, GetCallbackByTimeV2 params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLCallback>> dbGetCallbackByTime(DBConfig config, GetCallbackByTimeV7 params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLCallback>> dbGetCallbackAll(DBConfig config, GetCallbackAll params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLCallback>> dbGetCallbackAllV3(DBConfig config, GetCallbackAllV3 params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrDepartmentResp>> dbGetOrDepartment(DBConfig config, GetOrDepartment params) {

        DBResponse<List<GetOrDepartmentResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrDepartmentResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrDepartmentResp>)map.get("c_cursor"));

        return rs;
    }


    @SuppressWarnings("unchecked")
    public DBResponse<List<CLCallback>> dbSnapStatusCallbacksMulti(DBConfig config, LeadStatusCallbackWithTimeRequestDTO params) {

        DBResponse<List<CLCallback>> response = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> sqlParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)
                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(sqlParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper<>(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);
        Map<?, ?> map = simpleJdbcCall.execute(in);

        response.setErrorMsg((String) map.get("out_failmessage"));
        response.setErrorCode((Integer) map.get("out_status"));
        response.setRowCount((Integer) map.get("out_countrow"));
        response.setResult((List<CLCallback>)map.get("c_cursor"));
        return response;
    }

    public DBResponse<List<CLCallback>> dbGetCallbackByFirstCreateDate(DBConfig config, GetCallbackByFirstCreateDate params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        if (map.get("out_countrow") != null)
            rs.setRowCount((Integer) map.get("out_countrow"));
        else
            rs.setRowCount(0);
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLCallback>> dbGetCallbackByLeadIds(DBConfig config, GetCallbackByLeadIds params) {

        DBResponse<List<CLCallback>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLCallback.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        if (map.get("out_countrow") != null)
            rs.setRowCount((Integer) map.get("out_countrow"));
        else
            rs.setRowCount(0);
        rs.setResult((List<CLCallback>)map.get("c_cursor"));

        return rs;
    }
}
