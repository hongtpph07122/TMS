package com.tms.dao.impl;

import com.tms.dao.TMSBeanPropertyRowMapper;
import com.tms.dto.*;
import com.tms.entity.CountMapper;
import com.tms.config.DBConfig;
import com.tms.entity.CLActive;
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
public class CLActiveDao extends BasicDao {

    public DBResponse<List<CLActive>> memGetLeadByStatus(DBConfig config, CLActiveParams params) {
        return null;
    }

    public DBResponse<List<CLActive>> dbGetLeadByStatus(DBConfig config, CLActiveParams params) {

        DBResponse<List<CLActive>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_leadstatus", Types.INTEGER),
                        new SqlParameter("in_attempt", Types.INTEGER),
                        new SqlParameter("in_number", Types.INTEGER),
                        new SqlParameter("inout_rc_name",  Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLActive.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_leadstatus", params.getLeadstatus())
                .addValue("in_attempt", params.getAttempt())
                .addValue("in_number", params.getNumber())
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setResult((List<CLActive>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLActive>> memGetLeadByStatusAndTime(DBConfig config, CLActiveStatusTimeParams params) {
        return null;
    }

    public DBResponse<List<CLActive>> dbGetLeadByStatusAndTime(DBConfig config, CLActiveStatusTimeParams params) {

        DBResponse<List<CLActive>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_starttime", Types.VARCHAR),
                        new SqlParameter("in_endtime", Types.VARCHAR),
                        new SqlParameter("in_leadstatus", Types.INTEGER),
                        new SqlParameter("in_number", Types.INTEGER),
                        new SqlParameter("inout_rc_name",  Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLActive.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_starttime", params.getStarttime())
                .addValue("in_endtime", params.getEndtime())
                .addValue("in_leadstatus", params.getLeadstatus())
                .addValue("in_number", params.getNumber())
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setResult((List<CLActive>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLActive>> memGetLeadByTime(DBConfig config, CLActiveStatusTimeParams params) {
        return null;
    }

    public DBResponse<List<CLActive>> dbGetLeadByTime(DBConfig config, CLActiveStatusTimeParams params) {

        DBResponse<List<CLActive>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_starttime", Types.VARCHAR),
                        new SqlParameter("in_endtime", Types.VARCHAR),
                        new SqlParameter("in_number", Types.INTEGER),
                        new SqlParameter("inout_rc_name",  Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLActive.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_starttime", params.getStarttime())
                .addValue("in_endtime", params.getEndtime())
                .addValue("in_number", params.getNumber())
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setResult((List<CLActive>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<Long> memGetActiveLeadCount(DBConfig config, Integer campainId) {
        return null;
    }

    public DBResponse<Long> dbGetActiveLeadCount(DBConfig config, Integer campainId) {

        DBResponse<Long> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_campaignid", Types.INTEGER),
                        new SqlParameter("inout_rc_name",  Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new CountMapper());

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_campaignid", campainId)
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        List<Long> rsm = (List<Long>)map.get("c_cursor");
        rs.setResult(rsm.get(0));

        return rs;
    }

    public DBResponse<Long> memGetCallbackLeadCount(DBConfig config, Integer campainId) {
        return null;
    }

    public DBResponse<Long> dbGetCallbackLeadCount(DBConfig config, Integer campainId) {

        DBResponse<Long> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_campaignid", Types.INTEGER),
                        new SqlParameter("inout_rc_name",  Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new CountMapper());

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_campaignid", campainId)
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        List<Long> rsm = (List<Long>)map.get("c_cursor");
        rs.setResult(rsm.get(0));

        return rs;
    }


    public DBResponse<Long> memGetLeadCount(DBConfig config, Integer campainId) {
        return null;
    }

    public DBResponse<Long> dbGetLeadCount(DBConfig config, Integer campainId) {

        DBResponse<Long> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_campaignid", Types.INTEGER),
                        new SqlParameter("inout_rc_name",  Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new CountMapper());

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_campaignid", campainId)
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        List<Long> rsm = (List<Long>)map.get("c_cursor");
        rs.setResult(rsm.get(0));

        return rs;
    }

    public DBResponse<List<CLActive>> dbGetActiveLead(DBConfig config, CLActiveLeadParams params) {

        DBResponse<List<CLActive>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLActive.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLActive>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLActive>> dbGetActiveLeadV3(DBConfig config, CLActiveLeadParamsV3 params) {

        DBResponse<List<CLActive>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLActive.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLActive>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLActive>> dbGetActiveLead(DBConfig config, CLActiveLeadParamsV7 params) {

        DBResponse<List<CLActive>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLActive.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLActive>)map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetUnreachableResp>> dbGetUnreachableLead(DBConfig config, GetUnreachable params) {

        DBResponse<List<GetUnreachableResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetUnreachableResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetUnreachableResp>)map.get("c_cursor"));

        return rs;
    }


    public DBResponse<List<GetLogLeadResp>> dbGetLogLead(DBConfig config, GetLogLead params) {

        DBResponse<List<GetLogLeadResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetLogLeadResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetLogLeadResp>)map.get("c_cursor"));

        return rs;
    }



}
