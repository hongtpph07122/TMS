package com.tms.dao.impl;

import com.tms.config.DBConfig;
import com.tms.dao.TMSBeanPropertyRowMapper;
import com.tms.dto.RCLastmileReasonParams;
import com.tms.dto.DBResponse;
import com.tms.entity.RCLastmileReason;
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
public class RCLastmileReasonDao extends BasicDao {
    public DBResponse<List<RCLastmileReason>> dbGetRCLastmileReason(DBConfig config, RCLastmileReasonParams params) {

        DBResponse<List<RCLastmileReason>> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("in_org_id", Types.INTEGER),
                        new SqlParameter("inout_rc_name", Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR),
                        new SqlOutParameter("out_countrow", Types.INTEGER)

                ).returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(RCLastmileReason.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("in_org_id", params.getOrdId())
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        rs.setErrorCode((Integer) map.get("out_status"));
        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setRowCount((Integer) map.get("out_countrow"));
        rs.setResult((List<RCLastmileReason>) map.get("c_cursor"));

        return rs;
    }
}
