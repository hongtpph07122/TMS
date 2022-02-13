package com.tms.dao.impl;

import com.tms.config.DBConfig;
import com.tms.dao.TMSBeanPropertyRowMapper;
import com.tms.dto.DBResponse;
import com.tms.dto.RoleTMS;
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
public class RoleDao extends BasicDao {

    public DBResponse<List<RoleTMS>> dbGetRoleList(DBConfig dbConfig) {
        DBResponse<List<RoleTMS>> listDBResponse = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)

                .withFunctionName(dbConfig.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("inout_rc_name", Types.OTHER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR)

                ).returningResultSet("c_cursor", new TMSBeanPropertyRowMapper(RoleTMS.class));

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("inout_rc_name", "c_cursor");

        Map map = simpleJdbcCall.execute(in);
        listDBResponse.setErrorMsg((String) map.get("out_failmessage"));
        listDBResponse.setErrorCode((Integer) map.get("out_status"));
        listDBResponse.setResult((List<RoleTMS>) map.get("c_cursor"));

        return listDBResponse;
    }
}
