package com.tms.dao.impl;

import java.sql.Types;
import java.util.List;
import java.util.Map;

import com.tms.dto.*;
import com.tms.model.Request.LeadParamsRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.request.CampaignRequestDTO;
import com.tms.api.response.CampaignResponseDTO;
import com.tms.config.DBConfig;
import com.tms.dao.TMSBeanPropertyRowMapper;
import com.tms.entity.CLFresh;
import com.tms.entity.CPCampaign;
import com.tms.entity.CountMapper;
import com.tms.entity.FreshLead;
import com.tms.utils.AppUtils;

@Repository
@Transactional(readOnly = true)
public class CLFreshDao extends BasicDao {

    public DBResponse<List<CLFresh>> dbGetLead(DBConfig config, GetLeadParams params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetLeadV3(DBConfig config, GetLeadParamsV3 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetLeadV4(DBConfig config, GetLeadParamsV4 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetLeadV11(DBConfig config, GetLeadParamsV11 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    @SuppressWarnings("unchecked")
    public DBResponse<List<CLFresh>> dbSnapCluesMultiCampaigns(DBConfig config, LeadParamsRequestDTO params) {

        DBResponse<List<CLFresh>> response = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> sqlParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)
                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(sqlParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper<>(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);
        Map<?,?> map = simpleJdbcCall.execute(in);

        response.setErrorMsg((String) map.get("out_failmessage"));
        response.setErrorCode((Integer) map.get("out_status"));
        response.setRowCount((Integer) map.get("out_countrow"));
        response.setResult((List<CLFresh>) map.get("c_cursor"));
        return response;
    }

    public DBResponse<List<CLFresh>> dbGetLead(DBConfig config, GetLeadParamsV5 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetLead(DBConfig config, GetLeadParamsV7 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetLead(DBConfig config, GetLeadParamsV8 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetLeadAgencyResp>> dbGetLead(DBConfig config, GetLeadAgencyParams params) {

        DBResponse<List<GetLeadAgencyResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetLeadAgencyResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetLeadAgencyResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetFreshLead(DBConfig config, GetFreshLeadParams params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetFreshLeadV2(DBConfig config, GetFreshLeadV2 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetFreshLeadV3(DBConfig config, GetFreshLeadV3 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetNewestLead(DBConfig config, GetNewestLeadParams params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetNewestLeadV2(DBConfig config, GetNewestLeadV2 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetNewestLeadV3(DBConfig config, GetNewestLeadV3 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetNewestLead(DBConfig config, GetNewestLeadV4 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetNewestLead(DBConfig config, GetNewestLeadV5 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetNewestLead(DBConfig config, GetNewestLeadV7 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetUncallLead(DBConfig config, GetUncallLead params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetUncallLead(DBConfig config, GetUncallLeadV2 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetUncallLead(DBConfig config, GetUncallLeadLifo params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<FreshLead>> memGetFreshLeadByNumber(DBConfig config, Integer params) {
        return null;
    }

    public DBResponse<List<FreshLead>> dbGetFreshLeadByNumber(DBConfig config, Integer params) {

        DBResponse<List<FreshLead>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess().declareParameters(
                        new SqlParameter("in_number", Types.INTEGER), new SqlParameter("inout_rc_name", Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(FreshLead.class));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_number", params).addValue("inout_rc_name",
                "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setResult((List<FreshLead>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<Long> memGetFreshCount(DBConfig config, Integer campainId) {
        return null;
    }

    public DBResponse<Long> dbGetFreshCount(DBConfig config, Integer campainId) {

        DBResponse<Long> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess().declareParameters(
                        new SqlParameter("in_campaignid", Types.INTEGER),
                        new SqlParameter("inout_rc_name", Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new CountMapper());

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_campaignid", campainId)
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        List<Long> rsm = (List<Long>) map.get("c_cursor");
        rs.setResult(rsm.get(0));

        return rs;
    }

    public DBResponse<List<CPCampaign>> memGetCampaign(DBConfig config, GetCampaign params) {
        return null;
    }

	public DBResponse<List<CampaignResponseDTO>> memGetCampaign(DBConfig config, CampaignRequestDTO params) {
		return null;
	}

    public DBResponse<List<CPCampaign>> dbGetCampaign(DBConfig config, GetCampaign params) {

        DBResponse<List<CPCampaign>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CPCampaign.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CPCampaign>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CampaignResponseDTO>> dbGetCampaign(DBConfig config, CampaignRequestDTO params) {

        DBResponse<List<CampaignResponseDTO>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CampaignResponseDTO.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CampaignResponseDTO>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCampaignRespV2>> dbGetCampaignV2(DBConfig config, GetCampaignParamsV2 params) {

        DBResponse<List<GetCampaignRespV2>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCampaignRespV2.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map<String, Object> map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCampaignRespV2>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCampaignAgentResp>> dbGetCampaignAgent(DBConfig config, GetCampaignAgent params) {

        DBResponse<List<GetCampaignAgentResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCampaignAgentResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCampaignAgentResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CPCampaignResp>> dbGetCampaignConfig(DBConfig config, CPCampaignParams params) {

        DBResponse<List<CPCampaignResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CPCampaignResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CPCampaignResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCallingListResp>> dbGetCallingList(DBConfig config, GetCallingList params) {

        DBResponse<List<GetCallingListResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCallingListResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCallingListResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCallingListResp>> dbGetCallingList(DBConfig config, GetCallingListV2 params) {

        DBResponse<List<GetCallingListResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCallingListResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCallingListResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCpCallingListResp>> dbCpCallingList(DBConfig config, GetCpCallingList params) {

        DBResponse<List<GetCpCallingListResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCpCallingListResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCpCallingListResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCpCallingListResp>> dbCpCallingList(DBConfig config, GetCpCallingListV3 params) {

        DBResponse<List<GetCpCallingListResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCpCallingListResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCpCallingListResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrganizationResp>> dbOrganization(DBConfig config, GetOrganization params) {

        DBResponse<List<GetOrganizationResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrganizationResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrganizationResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetLogSoResp>> dbGetLogSo(DBConfig config, GetLogSo params) {

        DBResponse<List<GetLogSoResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetLogSoResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetLogSoResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetLogDoResp>> dbGetLogDo(DBConfig config, GetLogDo params) {

        DBResponse<List<GetLogDoResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetLogDoResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetLogDoResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCampaignProgressResp>> dbGetCampaignProgress(DBConfig config,
                                                                           GetCampaignProgress params) {

        DBResponse<List<GetCampaignProgressResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCampaignProgressResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCampaignProgressResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetValidationResp>> dbGetValidation(DBConfig config, GetValidation params) {

        DBResponse<List<GetValidationResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetValidationResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetValidationResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetValidation2Resp>> dbGetValidation2(DBConfig config, GetValidation2 params) {

        DBResponse<List<GetValidation2Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetValidation2Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetValidation2Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetValidation3Resp>> dbGetValidation3(DBConfig config, GetValidation3 params) {

        DBResponse<List<GetValidation3Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetValidation3Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetValidation3Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetValidation4Resp>> dbGetValidation4(DBConfig config, GetValidation4 params) {

        DBResponse<List<GetValidation4Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetValidation4Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetValidation4Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetValidation4Resp>> dbGetValidation(DBConfig config, GetValidation5 params) {

        DBResponse<List<GetValidation4Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetValidation4Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetValidation4Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrderManagementResp>> dbOrderManagement(DBConfig config, GetOrderManagement params) {

        DBResponse<List<GetOrderManagementResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrderManagementResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrderManagementResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrderManagement2Resp>> dbOrderManagement2(DBConfig config, GetOrderManagement2 params) {

        DBResponse<List<GetOrderManagement2Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrderManagement2Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrderManagement2Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrderManagement3Resp>> dbOrderManagement3(DBConfig config, GetOrderManagement3 params) {

        DBResponse<List<GetOrderManagement3Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrderManagement3Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrderManagement3Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrderManagement4Resp>> dbOrderManagement4(DBConfig config, GetOrderManagement4 params) {

        DBResponse<List<GetOrderManagement4Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrderManagement4Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrderManagement4Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrderManagement5Resp>> dbOrderManagement5(DBConfig config, GetOrderManagement5 params) {

        DBResponse<List<GetOrderManagement5Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrderManagement5Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrderManagement5Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrderManagement6Resp>> dbOrderManagement(DBConfig config, GetOrderManagement6 params) {

        DBResponse<List<GetOrderManagement6Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrderManagement6Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrderManagement6Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOrderDHLResp>> dbOrderDHL(DBConfig config, GetOrderDHL params) {

        DBResponse<List<GetOrderDHLResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOrderDHLResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOrderDHLResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetGlobalParamResp>> dbGlobalParam(DBConfig config, GetGlobalParam params) {

        DBResponse<List<GetGlobalParamResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetGlobalParamResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetGlobalParamResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetGlobalParamResp>> dbGlobalParamV2(DBConfig config, GetGlobalParamV2 params) {

        DBResponse<List<GetGlobalParamResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetGlobalParamResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetGlobalParamResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDashboardAgentResp>> dbGetDashboardAgent(DBConfig config, GetDashboardAgent params) {

        DBResponse<List<GetDashboardAgentResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDashboardAgentResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDashboardAgentResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetDashboardAvggoupResp>> dbGetDashboardAvgGroup(DBConfig config,
                                                                            GetDashboardAvggroup params) {

        DBResponse<List<GetDashboardAvggoupResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetDashboardAvggoupResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetDashboardAvggoupResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetOfferResp>> dbGetOffer(DBConfig config, GetOffer params) {

        DBResponse<List<GetOfferResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetOfferResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetOfferResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetSynonymResp>> dbGetSynonym(DBConfig config, GetSynonym params) {

        DBResponse<List<GetSynonymResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetSynonymResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetSynonymResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetSynonymRespV2>> dbGetSynonymV2(DBConfig config, GetSynonymV2 params) {

        DBResponse<List<GetSynonymRespV2>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetSynonymRespV2.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetSynonymRespV2>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCommissionDataResp>> dbGetCommissionData(DBConfig config, GetCommissionData params) {

        DBResponse<List<GetCommissionDataResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCommissionDataResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCommissionDataResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCommissionDataV2Resp>> dbGetCommissionDataV2(DBConfig config, GetCommissionData params) {

        DBResponse<List<GetCommissionDataV2Resp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCommissionDataV2Resp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map<String, Object> map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCommissionDataV2Resp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<GetCollectionDataResp>> dbGetCollectionData(DBConfig config, GetCollectionData params) {

        DBResponse<List<GetCollectionDataResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetCollectionDataResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetCollectionDataResp>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetLead(DBConfig config, GetLeadParamsV9 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

	public DBResponse<List<CLFresh>> dbGetLead(DBConfig config, GetLeadParamsV10 params) {
        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
	}

    public DBResponse<List<CLFresh>> dbGetLeadV12(DBConfig config, GetLeadParamsV12 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map<String, Object> map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

	public DBResponse<List<CLFresh>> dbGetFreshLead(DBConfig config, GetFreshLeadV4 params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
	}

    public DBResponse<List<CLFresh>> dbGetReservedLead(DBConfig config, GetLeadReservedParam params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
    }

    public DBResponse<List<CLFresh>> dbGetReserveNewLead(DBConfig config, GetLeadReservedParam params) {

        DBResponse<List<CLFresh>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(CLFresh.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<CLFresh>) map.get("c_cursor"));

        return rs;
	}

    public DBResponse<List<GetMktDataResp>> dbGetMktData(DBConfig config, GetMktDataParams params) {

        DBResponse<List<GetMktDataResp>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(GetMktDataResp.class));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map<String, Object> map = simpleJdbcCall.execute(in);
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<GetMktDataResp>) map.get("c_cursor"));

        return rs;
    }
}
