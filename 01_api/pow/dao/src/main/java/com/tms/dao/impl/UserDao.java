package com.tms.dao.impl;

import com.tms.config.DBConfig;
import com.tms.dao.TMSBeanPropertyRowMapper;
import com.tms.dto.*;
import com.tms.entity.User;
import com.tms.utils.AppUtils;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
@Transactional(readOnly = true)
public class UserDao extends BasicDao {

    public DBResponse<List<GetPrivByUserResp>> dbGetPrivByUser(DBConfig config, GetPrivByUserParams params) {

        DBResponse<List<GetPrivByUserResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetPrivByUserResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetPrivByUserResp>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<User>> dbGetUser(DBConfig config, GetUserParams params) {

        DBResponse<List<User>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(User.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<User>)map.get("c_cursor"));

        return rs;
    }
    public DBResponse<List<User>> dbGetUser(DBConfig config, GetUserParamV2 params) {

        DBResponse<List<User>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(User.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<User>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<User>> dbGetUser(DBConfig config, GetUserV2 params) {

        DBResponse<List<User>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(User.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<User>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<User>> dbGetUserV5(DBConfig config, GetUserParamsV5 params) {

        DBResponse<List<User>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(User.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map<String, Object> map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<User>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<User>> dbGetUserNamPass(DBConfig config, GetUserPassParams params) {

        DBResponse<List<User>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(
                    new SqlOutParameter("out_status", Types.INTEGER),
                    new SqlOutParameter("out_failmessage", Types.VARCHAR),
                    new SqlOutParameter("out_loginstatus", Types.INTEGER)
                )
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(User.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_loginstatus"));
        if (rs.getErrorCode() == 0) {
            rs.setResult((List<User>)map.get("c_cursor"));
        }

        return rs;
    }



}
