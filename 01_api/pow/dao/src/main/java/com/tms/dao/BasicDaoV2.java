package com.tms.dao;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.writer.CSVEntryConverter;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;
import com.tms.config.DBConfig;
import com.tms.config.DBConfigMap;
import com.tms.config.DataSourceConfig;
import com.tms.dto.DBResponse;
import com.tms.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Repository
public class BasicDaoV2 {

    @Autowired
    @Qualifier(DataSourceConfig.DB_JDBC_TEMPLATE)
    protected JdbcTemplate dbTemplate;

    @Autowired
    @Qualifier(DataSourceConfig.MM_JDBC_TEMPLATE)
    protected JdbcTemplate mmTemplate;

    @Autowired
    protected DBConfigMap configMap;

    protected String getPath(String funcName) {
        return configMap.getFilePath() + File.separator + getFileName(funcName);
    }

    private String getFileName(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String nowStr = (LocalDateTime.now()).format(formatter);
        return String.format("%s.csv.%s", fileName, nowStr);
    }

    /* Begin get */
    @Transactional(readOnly = true)
    public <PARAM, RESULT> DBResponse<RESULT> dbGet(DBConfig config, PARAM params, Class<?> resultClass) {
        DBResponse<RESULT> result = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, true, true);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate)
                .withFunctionName(config.getFuncName()).withoutProcedureColumnMetaDataAccess()
                .declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .returningResultSet("c_cursor", new TMSBeanPropertyRowMapper<>(resultClass));

        SqlParameterSource in = AppUtils.ob2SqlSource(params, true);

        Map<String, Object> map = simpleJdbcCall.execute(in);
        result.setErrorMsg((String) map.get("out_failmessage"));
        result.setErrorCode((Integer) map.get("out_status"));
        result.setRowCount((Integer) map.get("out_countrow"));
        @SuppressWarnings("unchecked")
        RESULT cursor = (RESULT) map.get("c_cursor");
        result.setResult(cursor);

        return result;
    }
    /* End get */

    /* Begin insert or update */
    public <T> DBResponse<String> insOrUpdFile(DBConfig config, T params) {
        DBResponse<String> result = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<T> converter = AppUtils.ob2Strr(params);
            CSVWriter<T> csvWriter = new CSVWriterBuilder<T>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            result.setErrorMsg(e.getMessage());
            result.setErrorCode(1);
        }
        return result;
    }

    public <T> DBResponse<String> insOrUpdDb(DBConfig config, T params) {
        DBResponse<String> result = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        result.setErrorMsg((String) map.get("out_failmessage"));
        result.setErrorCode((Integer) map.get("out_status"));

        return result;
    }
    /* End insert or update */
}
