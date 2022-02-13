package com.tms.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.writer.CSVEntryConverter;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;
import com.tms.api.request.CampaignConfigurationRequestDTO;
import com.tms.api.request.CampaignUpdateRequestDTO;
import com.tms.config.DBConfig;
import com.tms.dto.*;
import com.tms.entity.log.*;
import com.tms.model.Request.SaleOrderRequestDTO;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository

public class LogDao extends BasicDao {

    private static final Logger logger = LoggerFactory.getLogger(LogDao.class);

    public DBResponse fileFreshLog(DBConfig config, LogLead params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogLead> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogLead> csvWriter = new CSVWriterBuilder<LogLead>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbFreshLog(DBConfig config, LogLead params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileLogLeadV3(DBConfig config, LogLeadV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogLeadV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogLeadV3> csvWriter = new CSVWriterBuilder<LogLeadV3>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbLogLeadV3(DBConfig config, LogLeadV3 params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileCallbackLog(DBConfig config, LogCallback params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogCallback> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogCallback> csvWriter = new CSVWriterBuilder<LogCallback>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbCallbackLog(DBConfig config, LogCallback params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpCallbackLog(DBConfig config, UpdateCLCallback params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdateCLCallback> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdateCLCallback> csvWriter = new CSVWriterBuilder<UpdateCLCallback>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpCallbackLog(DBConfig config, UpdateCLCallback params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdCallbackByAssigned(DBConfig config, UpdateCLCallbackByAssigned params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdateCLCallbackByAssigned> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdateCLCallbackByAssigned> csvWriter = new CSVWriterBuilder<UpdateCLCallbackByAssigned>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdCallbackByAssigned(DBConfig config, UpdateCLCallbackByAssigned params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse delCallbackLog(DBConfig config, Integer leadId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_lead_id", Types.INTEGER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_lead_id", leadId);

        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsBasket(DBConfig config, InsCLBasket params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLBasket> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLBasket> csvWriter = new CSVWriterBuilder<InsCLBasket>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsBasket(DBConfig config, InsCLBasket params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsBasketV2(DBConfig config, InsCLBasketV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLBasketV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLBasketV2> csvWriter = new CSVWriterBuilder<InsCLBasketV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsBasketV2(DBConfig config, InsCLBasketV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsBasketV3(DBConfig config, InsCLBasketV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLBasketV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLBasketV3> csvWriter = new CSVWriterBuilder<InsCLBasketV3>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsBasketV3(DBConfig config, InsCLBasketV3 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsBasketV4(DBConfig config, InsCLBasketV4 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLBasketV4> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLBasketV4> csvWriter = new CSVWriterBuilder<InsCLBasketV4>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsBasketV4(DBConfig config, InsCLBasketV4 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdBasket(DBConfig config, UpdCLBasket params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdCLBasket> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdCLBasket> csvWriter = new CSVWriterBuilder<UpdCLBasket>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdBasket(DBConfig config, UpdCLBasket params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdBasketV2(DBConfig config, UpdCLBasketV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdCLBasketV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdCLBasketV2> csvWriter = new CSVWriterBuilder<UpdCLBasketV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdBasketV2(DBConfig config, UpdCLBasketV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsDeliveryOrder(DBConfig config, InsDeliveryOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsDeliveryOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsDeliveryOrder> csvWriter = new CSVWriterBuilder<InsDeliveryOrder>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsDeliveryOrder(DBConfig config, InsDeliveryOrder params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsPaymentOrder(DBConfig config, InsPaymentOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsPaymentOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsPaymentOrder> csvWriter = new CSVWriterBuilder<InsPaymentOrder>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsPaymentOrder(DBConfig config, InsPaymentOrder params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsSaleOrder(DBConfig config, InsSaleOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsSaleOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsSaleOrder> csvWriter = new CSVWriterBuilder<InsSaleOrder>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsSaleOrder(DBConfig config, InsSaleOrder params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsSaleOrder(DBConfig config, InsSaleOrderV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsSaleOrderV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsSaleOrderV2> csvWriter = new CSVWriterBuilder<InsSaleOrderV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsSaleOrder(DBConfig config, InsSaleOrderV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsSaleItemOrder(DBConfig config, InsSaleOrderItem params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsSaleOrderItem> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsSaleOrderItem> csvWriter = new CSVWriterBuilder<InsSaleOrderItem>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsSaleItemOrder(DBConfig config, InsSaleOrderItem params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsSaleItemOrder(DBConfig config, InsSaleOrderItemV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsSaleOrderItemV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsSaleOrderItemV2> csvWriter = new CSVWriterBuilder<InsSaleOrderItemV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsSaleItemOrder(DBConfig config, InsSaleOrderItemV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileLogPaymentOrder(DBConfig config, LogPaymentOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogPaymentOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogPaymentOrder> csvWriter = new CSVWriterBuilder<LogPaymentOrder>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbLogPaymentItemOrder(DBConfig config, LogPaymentOrder params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileLogSaleOrder(DBConfig config, LogSaleOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogSaleOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogSaleOrder> csvWriter = new CSVWriterBuilder<LogSaleOrder>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbLogSaleOrder(DBConfig config, LogSaleOrder params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdDeliveryOrder(DBConfig config, UpdDeliveryOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDeliveryOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDeliveryOrder> csvWriter = new CSVWriterBuilder<UpdDeliveryOrder>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdDeliveryOrder(DBConfig config, UpdDeliveryOrder params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdSaleOrder(DBConfig config, UpdSaleOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdSaleOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdSaleOrder> csvWriter = new CSVWriterBuilder<UpdSaleOrder>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdSaleOrder(DBConfig config, UpdSaleOrder params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdSaleOrder(DBConfig config, UpdSaleOrderV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdSaleOrderV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdSaleOrderV2> csvWriter = new CSVWriterBuilder<UpdSaleOrderV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileUpdSaleOrder(DBConfig config, SaleOrderRequestDTO params) {
        DBResponse response = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer writer = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<SaleOrderRequestDTO> converter = AppUtils.ob2Strr(params);
            CSVWriter<SaleOrderRequestDTO> csvWriter = new CSVWriterBuilder<SaleOrderRequestDTO>(writer)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(writer);
        } catch (IOException e) {
            response.setErrorMsg(e.getMessage());
            response.setErrorCode(1);
        }
        return response;
    }

    public DBResponse fileUpdSaleOrder(DBConfig config, UpdSaleOrderV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdSaleOrderV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdSaleOrderV3> csvWriter = new CSVWriterBuilder<UpdSaleOrderV3>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileUpdSaleOrder(DBConfig config, UpdSaleOrderV4 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdSaleOrderV4> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdSaleOrderV4> csvWriter = new CSVWriterBuilder<UpdSaleOrderV4>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdSaleOrder(DBConfig config, UpdSaleOrderV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbUpdSaleOrder(DBConfig config, SaleOrderRequestDTO params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbUpdSaleOrder(DBConfig config, UpdSaleOrderV3 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbUpdSaleOrder(DBConfig config, UpdSaleOrderV4 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdInactiveLead(DBConfig config, UpdInActiveLead params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdInActiveLead> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdInActiveLead> csvWriter = new CSVWriterBuilder<UpdInActiveLead>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdInactiveLead(DBConfig config, UpdInActiveLead params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdLead(DBConfig config, UpdLead params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLead> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLead> csvWriter = new CSVWriterBuilder<UpdLead>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLead(DBConfig config, UpdLead params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdLeadV4(DBConfig config, UpdLeadV4 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadV4> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadV4> csvWriter = new CSVWriterBuilder<UpdLeadV4>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLeadV4(DBConfig config, UpdLeadV4 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdLead(DBConfig config, UpdLeadV5 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadV5> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadV5> csvWriter = new CSVWriterBuilder<UpdLeadV5>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLead(DBConfig config, UpdLeadV5 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdLeadByAssigned(DBConfig config, UpdLeadByAssigned params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadByAssigned> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadByAssigned> csvWriter = new CSVWriterBuilder<UpdLeadByAssigned>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLeadByAssigned(DBConfig config, UpdLeadByAssigned params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse<String> fileUpdLeadByAssignedV2(DBConfig config, UpdLeadByAssignedV2 params) {
        DBResponse<String> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadByAssignedV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadByAssignedV2> csvWriter = new CSVWriterBuilder<UpdLeadByAssignedV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbUpdLeadByAssignedV2(DBConfig config, UpdLeadByAssignedV2 params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse<String> fileUpdLeadByAssignedV3(DBConfig config, UpdLeadByAssignedV3 params) {
        DBResponse<String> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadByAssignedV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadByAssignedV3> csvWriter = new CSVWriterBuilder<UpdLeadByAssignedV3>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbUpdLeadByAssignedV3(DBConfig config, UpdLeadByAssignedV3 params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCampaign(DBConfig config, InsCampaign params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCampaign> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCampaign> csvWriter = new CSVWriterBuilder<InsCampaign>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileInsCampaign(DBConfig config, CampaignConfigurationRequestDTO params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<CampaignConfigurationRequestDTO> converter = AppUtils.ob2Strr(params);
            CSVWriter<CampaignConfigurationRequestDTO> csvWriter = new CSVWriterBuilder<CampaignConfigurationRequestDTO>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCampaign(DBConfig config, InsCampaign params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbInsCampaign(DBConfig config, CampaignConfigurationRequestDTO params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLFresh(DBConfig config, InsCLFresh params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFresh> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFresh> csvWriter = new CSVWriterBuilder<InsCLFresh>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLFresh(DBConfig config, InsCLFresh params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLFreshV2(DBConfig config, InsCLFreshV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV2> csvWriter = new CSVWriterBuilder<InsCLFreshV2>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLFreshV2(DBConfig config, InsCLFreshV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLFreshV3(DBConfig config, InsCLFreshV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV3> csvWriter = new CSVWriterBuilder<InsCLFreshV3>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLFreshV3(DBConfig config, InsCLFreshV3 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLFreshV4(DBConfig config, InsCLFreshV4 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV4> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV4> csvWriter = new CSVWriterBuilder<InsCLFreshV4>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLFreshV4(DBConfig config, InsCLFreshV4 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLFresh(DBConfig config, InsCLFreshV5 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV5> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV5> csvWriter = new CSVWriterBuilder<InsCLFreshV5>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileInsCLFreshV9(DBConfig config, InsCLFreshV9 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV9> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV9> csvWriter = new CSVWriterBuilder<InsCLFreshV9>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileInsCLFreshV11(DBConfig config, InsCLFreshV11 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV11> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV11> csvWriter = new CSVWriterBuilder<InsCLFreshV11>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileInsCLFreshV6(DBConfig config, InsCLFreshV6 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV6> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV6> csvWriter = new CSVWriterBuilder<InsCLFreshV6>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLFresh(DBConfig config, InsCLFreshV5 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbInsCLFreshV9(DBConfig config, InsCLFreshV9 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbInsCLFreshV11(DBConfig config, InsCLFreshV11 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLActive(DBConfig config, InsCLActive params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLActive> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLActive> csvWriter = new CSVWriterBuilder<InsCLActive>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLActive(DBConfig config, InsCLActive params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLActiveV2(DBConfig config, InsCLActiveV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLActiveV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLActiveV2> csvWriter = new CSVWriterBuilder<InsCLActiveV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLActiveV2(DBConfig config, InsCLActiveV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLActiveV3(DBConfig config, InsCLActiveV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLActiveV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLActiveV3> csvWriter = new CSVWriterBuilder<InsCLActiveV3>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLActiveV3(DBConfig config, InsCLActiveV3 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLInActive(DBConfig config, InsCLInActive params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLInActive> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLInActive> csvWriter = new CSVWriterBuilder<InsCLInActive>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLInActive(DBConfig config, InsCLInActive params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLInActiveV2(DBConfig config, InsCLInActiveV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLInActiveV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLInActiveV2> csvWriter = new CSVWriterBuilder<InsCLInActiveV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLInActiveV2(DBConfig config, InsCLInActiveV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLInActiveV3(DBConfig config, InsCLInActiveV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLInActiveV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLInActiveV3> csvWriter = new CSVWriterBuilder<InsCLInActiveV3>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLInActiveV3(DBConfig config, InsCLInActiveV3 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLTrash(DBConfig config, InsCLTrash params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLTrash> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLTrash> csvWriter = new CSVWriterBuilder<InsCLTrash>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLTrash(DBConfig config, InsCLTrash params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLTrashV2(DBConfig config, InsCLTrashV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLTrashV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLTrashV2> csvWriter = new CSVWriterBuilder<InsCLTrashV2>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLTrashV2(DBConfig config, InsCLTrashV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLTrashV3(DBConfig config, InsCLTrashV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLTrashV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLTrashV3> csvWriter = new CSVWriterBuilder<InsCLTrashV3>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLTrashV3(DBConfig config, InsCLTrashV3 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLCallback(DBConfig config, InsCLCallback params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLCallback> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLCallback> csvWriter = new CSVWriterBuilder<InsCLCallback>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLCallback(DBConfig config, InsCLCallback params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCLCallbackV4(DBConfig config, InsCLCallbackV4 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLCallbackV4> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLCallbackV4> csvWriter = new CSVWriterBuilder<InsCLCallbackV4>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileInsCLCallbackV6(DBConfig config, InsCLCallbackV6 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLCallbackV6> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLCallbackV6> csvWriter = new CSVWriterBuilder<InsCLCallbackV6>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLCallbackV4(DBConfig config, InsCLCallbackV4 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbInsCLCallbackV6(DBConfig config, InsCLCallbackV6 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }



    public DBResponse fileInsCPConfig(DBConfig config, InsCPConfig params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCPConfig> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCPConfig> csvWriter = new CSVWriterBuilder<InsCPConfig>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCPConfig(DBConfig config, InsCPConfig params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUdpCPConfig(DBConfig config, UdpCPConfig params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UdpCPConfig> converter = AppUtils.ob2Strr(params);
            CSVWriter<UdpCPConfig> csvWriter = new CSVWriterBuilder<UdpCPConfig>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUdpCPConfig(DBConfig config, UdpCPConfig params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsProvince(DBConfig config, InsProvince params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsProvince> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsProvince> csvWriter = new CSVWriterBuilder<InsProvince>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsProvince(DBConfig config, InsProvince params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsDistrict(DBConfig config, InsDistrict params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsDistrict> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsDistrict> csvWriter = new CSVWriterBuilder<InsDistrict>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsDistrict(DBConfig config, InsDistrict params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsSubDistrict(DBConfig config, InsSubdistrict params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsSubdistrict> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsSubdistrict> csvWriter = new CSVWriterBuilder<InsSubdistrict>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsSubDistrict(DBConfig config, InsSubdistrict params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsProvinceMap(DBConfig config, InsProvinceMap params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsProvinceMap> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsProvinceMap> csvWriter = new CSVWriterBuilder<InsProvinceMap>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsProvinceMap(DBConfig config, InsProvinceMap params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsDistrictMap(DBConfig config, InsDistrictMap params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsDistrictMap> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsDistrictMap> csvWriter = new CSVWriterBuilder<InsDistrictMap>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsDistrictMap(DBConfig config, InsDistrictMap params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsSubDistrictMap(DBConfig config, InsSubDistrictMap params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsSubDistrictMap> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsSubDistrictMap> csvWriter = new CSVWriterBuilder<InsSubDistrictMap>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsSubDistrictMap(DBConfig config, InsSubDistrictMap params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdCampaign(DBConfig config, UpdCampaign params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdCampaign> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdCampaign> csvWriter = new CSVWriterBuilder<UpdCampaign>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileUpdCampaign(DBConfig config, CampaignUpdateRequestDTO params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<CampaignUpdateRequestDTO> converter = AppUtils.ob2Strr(params);
            CSVWriter<CampaignUpdateRequestDTO> csvWriter = new CSVWriterBuilder<CampaignUpdateRequestDTO>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdCampaign(DBConfig config, UpdCampaign params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbUpdCampaign(DBConfig config, CampaignUpdateRequestDTO params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelCallback(DBConfig config, Integer leadId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_lead_id", Types.INTEGER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_lead_id", leadId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelSo(DBConfig config, Integer soId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_so_id", Types.INTEGER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_so_id", soId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbMulDelCallback(DBConfig config, String leadId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_lead_id", Types.VARCHAR),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_lead_id", leadId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsProduct(DBConfig config, InsProduct params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsProduct> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsProduct> csvWriter = new CSVWriterBuilder<InsProduct>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsProduct(DBConfig config, InsProduct params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsProduct(DBConfig config, InsProductV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsProductV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsProductV2> csvWriter = new CSVWriterBuilder<InsProductV2>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsProduct(DBConfig config, InsProductV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsProductCombo(DBConfig config, InsProductCombo params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsProductCombo> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsProductCombo> csvWriter = new CSVWriterBuilder<InsProductCombo>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsProductCombo(DBConfig config, InsProductCombo params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdProduct(DBConfig config, UpdProduct params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdProduct> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdProduct> csvWriter = new CSVWriterBuilder<UpdProduct>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdProduct(DBConfig config, UpdProduct params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdProduct(DBConfig config, UpdProductV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdProductV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdProductV2> csvWriter = new CSVWriterBuilder<UpdProductV2>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdProduct(DBConfig config, UpdProductV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsProductAttribute(DBConfig config, InsProductAttribute params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsProductAttribute> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsProductAttribute> csvWriter = new CSVWriterBuilder<InsProductAttribute>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsProductAttribute(DBConfig config, InsProductAttribute params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdProductAttribute(DBConfig config, InsProductAttribute params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsProductAttribute> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsProductAttribute> csvWriter = new CSVWriterBuilder<InsProductAttribute>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdProductAttribute(DBConfig config, InsProductAttribute params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsStock(DBConfig config, InsStock params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsStock> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsStock> csvWriter = new CSVWriterBuilder<InsStock>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsStock(DBConfig config, InsStock params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdStock(DBConfig config, UpdStock params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdStock> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdStock> csvWriter = new CSVWriterBuilder<UpdStock>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdStock(DBConfig config, UpdStock params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsUnreachable(DBConfig config, InsUnreachable params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsUnreachable> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsUnreachable> csvWriter = new CSVWriterBuilder<InsUnreachable>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsUnreachable(DBConfig config, InsUnreachable params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdUnreachable(DBConfig config, UpdUnreachable params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdUnreachable> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdUnreachable> csvWriter = new CSVWriterBuilder<UpdUnreachable>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdUnreachable(DBConfig config, UpdUnreachable params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelUnreachable(DBConfig config, Integer leadId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_lead_id", Types.INTEGER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_lead_id", leadId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelProductCombo(DBConfig config, Integer productComboId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("in_combo_id", Types.INTEGER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_combo_id", productComboId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelMulUnreachable(DBConfig config, String leadId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_lead_id", Types.VARCHAR),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_lead_id", leadId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdSoItem(DBConfig config, UpdSoItem params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdSoItem> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdSoItem> csvWriter = new CSVWriterBuilder<UpdSoItem>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdSoItem(DBConfig config, UpdSoItem params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsOrgPartner(DBConfig config, InsOrganizationPartner params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsOrganizationPartner> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsOrganizationPartner> csvWriter = new CSVWriterBuilder<InsOrganizationPartner>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsOrgPartner(DBConfig config, InsOrganizationPartner params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdOrgPartner(DBConfig config, UpdOrganizationPartner params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdOrganizationPartner> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdOrganizationPartner> csvWriter = new CSVWriterBuilder<UpdOrganizationPartner>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdOrgPartner(DBConfig config, UpdOrganizationPartner params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileLogDeliveryOrder(DBConfig config, LogDeliveryOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogDeliveryOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogDeliveryOrder> csvWriter = new CSVWriterBuilder<LogDeliveryOrder>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbLoDeliveryOrder(DBConfig config, LogDeliveryOrder params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileLogAgent(DBConfig config, LogAgentActivity params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogAgentActivity> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogAgentActivity> csvWriter = new CSVWriterBuilder<LogAgentActivity>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbLogAgent(DBConfig config, LogAgentActivity params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelAllFresh(DBConfig config, Integer leadId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_lead_id", Types.INTEGER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_lead_id", leadId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelMultipleFresh(DBConfig config, String leadId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_lead_id", Types.VARCHAR),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_lead_id", leadId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsWarehouse(DBConfig config, InsWareHouse params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsWareHouse> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsWareHouse> csvWriter = new CSVWriterBuilder<InsWareHouse>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsWarehouse(DBConfig config, InsWareHouse params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdWarehouse(DBConfig config, UpdWareHouse params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdWareHouse> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdWareHouse> csvWriter = new CSVWriterBuilder<UpdWareHouse>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdWarehouse(DBConfig config, UpdWareHouse params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelWarehouse(DBConfig config, String warehouseId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("in_warehouse_id", Types.VARCHAR),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_warehouse_id", warehouseId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsDoNew(DBConfig config, InsDoNew params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsDoNew> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsDoNew> csvWriter = new CSVWriterBuilder<InsDoNew>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsDoNew(DBConfig config, InsDoNew params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsDoNew(DBConfig config, InsDoNewV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsDoNewV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsDoNewV2> csvWriter = new CSVWriterBuilder<InsDoNewV2>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsDoNew(DBConfig config, InsDoNewV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdDoNew(DBConfig config, UpdDoNew params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDoNew> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDoNew> csvWriter = new CSVWriterBuilder<UpdDoNew>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdDoNew(DBConfig config, UpdDoNew params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdDoNew(DBConfig config, UpdDoNewV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDoNewV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDoNewV2> csvWriter = new CSVWriterBuilder<UpdDoNewV2>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileUpdDoNew(DBConfig config, UpdDoNewV5 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDoNewV5> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDoNewV5> csvWriter = new CSVWriterBuilder<UpdDoNewV5>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }


    public DBResponse dbUpdDoNew(DBConfig config, UpdDoNewV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbUpdDoNew(DBConfig config, UpdDoNewV5 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdDoNew(DBConfig config, UpdDoNewV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDoNewV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDoNewV3> csvWriter = new CSVWriterBuilder<UpdDoNewV3>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdDoNew(DBConfig config, UpdDoNewV3 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdDoNew(DBConfig config, UpdDoNewV4 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDoNewV4> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDoNewV4> csvWriter = new CSVWriterBuilder<UpdDoNewV4>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdDoNew(DBConfig config, UpdDoNewV4 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdDoNew(DBConfig config, UpdDoNewV8 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDoNewV8> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDoNewV8> csvWriter = new CSVWriterBuilder<UpdDoNewV8>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdDoNew(DBConfig config, UpdDoNewV8 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));


        SqlParameterSource in = AppUtils.ob2SqlSource(params);

        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdDoNew(DBConfig config, UpdDoNewV9 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDoNewV9> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDoNewV9> csvWriter = new CSVWriterBuilder<UpdDoNewV9>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdDoNew(DBConfig config, UpdDoNewV9 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));


        SqlParameterSource in = AppUtils.ob2SqlSource(params);

        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }
    
    public DBResponse fileUpdDoNew(DBConfig config, UpdDoNewByTrackingCode params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdDoNewByTrackingCode> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdDoNewByTrackingCode> csvWriter = new CSVWriterBuilder<UpdDoNewByTrackingCode>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdDoNew(DBConfig config, UpdDoNewByTrackingCode params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelDoNew(DBConfig config, String doId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_do_id", Types.VARCHAR),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_do_id", doId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdSoFulfillment(DBConfig config, UpdSoFulfillment params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdSoFulfillment> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdSoFulfillment> csvWriter = new CSVWriterBuilder<UpdSoFulfillment>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdSoFulfillment(DBConfig config, UpdSoFulfillment params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsStatusMapping(DBConfig config, InsStatusMapping params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsStatusMapping> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsStatusMapping> csvWriter = new CSVWriterBuilder<InsStatusMapping>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdStatusMapping(DBConfig config, InsStatusMapping params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsDoPostback(DBConfig config, InsDoPostback params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsDoPostback> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsDoPostback> csvWriter = new CSVWriterBuilder<InsDoPostback>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsDoPostback(DBConfig config, InsDoPostback params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelProviceMap(DBConfig config, Integer partnerId, String prvId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("in_partner_id", Types.INTEGER),
                        new SqlParameter("in_prv_id", Types.VARCHAR), new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_partner_id", partnerId).addValue("in_prv_id",
                prvId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelDistrictMap(DBConfig config, Integer partnerId, String prvId, String dtId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(
                        new SqlParameter("in_partner_id", Types.INTEGER), new SqlParameter("in_prv_id", Types.VARCHAR),
                        new SqlParameter("in_dt_id", Types.VARCHAR), new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_partner_id", partnerId)
                .addValue("in_prv_id", prvId).addValue("in_dt_id", dtId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelSubDistrictMap(DBConfig config, Integer partnerId, String prvId, String dtId, String sdtId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("in_partner_id", Types.INTEGER),
                        new SqlParameter("in_prv_id", Types.VARCHAR), new SqlParameter("in_dt_id", Types.VARCHAR),
                        new SqlParameter("in_sdt_id", Types.VARCHAR), new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_partner_id", partnerId)
                .addValue("in_prv_id", prvId).addValue("in_dt_id", dtId).addValue("in_sdt_id", sdtId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsProductMapping(DBConfig config, InsProductMapping params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsProductMapping> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsProductMapping> csvWriter = new CSVWriterBuilder<InsProductMapping>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsProductMapping(DBConfig config, InsProductMapping params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelProductMapping(DBConfig config, Integer partnerId, String productId,
                                          String partnerProductId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("in_partner_id", Types.INTEGER),
                        new SqlParameter("in_product_id", Types.VARCHAR),
                        new SqlParameter("in_partner_product_id", Types.VARCHAR),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_partner_id", partnerId)
                .addValue("in_product_id", productId).addValue("in_partner_product_id", partnerProductId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCdr(DBConfig config, InsCdr params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCdr> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCdr> csvWriter = new CSVWriterBuilder<InsCdr>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCdr(DBConfig config, InsCdr params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCdrV2(DBConfig config, InsCdrV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCdrV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCdrV2> csvWriter = new CSVWriterBuilder<InsCdrV2>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCdrV2(DBConfig config, InsCdrV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdCdr(DBConfig config, UpdCdr params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdCdr> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdCdr> csvWriter = new CSVWriterBuilder<UpdCdr>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdCdr(DBConfig config, UpdCdr params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelCdr(DBConfig config, String callId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_call_id", Types.VARCHAR),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_call_id", callId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsPickup(DBConfig config, InsPickup params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsPickup> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsPickup> csvWriter = new CSVWriterBuilder<InsPickup>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsPickup(DBConfig config, InsPickup params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsOffer(DBConfig config, InsOffer params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsOffer> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsOffer> csvWriter = new CSVWriterBuilder<InsOffer>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsOffer(DBConfig config, InsOffer params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdPickup(DBConfig config, UpdPickup params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdPickup> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdPickup> csvWriter = new CSVWriterBuilder<UpdPickup>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdPickup(DBConfig config, UpdPickup params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelPickup(DBConfig config, String pickupId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(new SqlParameter("in_pickup_id", Types.VARCHAR),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_pickup_id", pickupId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsAgentRate(DBConfig config, InsAgentRate params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsAgentRate> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsAgentRate> csvWriter = new CSVWriterBuilder<InsAgentRate>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsAgentRate(DBConfig config, InsAgentRate params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsCdrAll(DBConfig config, InsCdrAll params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCdrAll> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCdrAll> csvWriter = new CSVWriterBuilder<InsCdrAll>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileInsLogConnectedCustomer(DBConfig config, InsLogConnectedCustomer params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsLogConnectedCustomer> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsLogConnectedCustomer> csvWriter = new CSVWriterBuilder<InsLogConnectedCustomer>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse fileInsLogUnCallConnected(DBConfig config, InsLogUnCallConnected params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsLogUnCallConnected> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsLogUnCallConnected> csvWriter = new CSVWriterBuilder<InsLogUnCallConnected>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCdrAll(DBConfig config, InsCdrAll params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbInsLogConnectedCustomer(DBConfig config, InsLogConnectedCustomer params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbInsLogUnCallConnected(DBConfig config, InsLogUnCallConnected params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdLeadAsign(DBConfig config, UpdLeadReasign params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadReasign> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadReasign> csvWriter = new CSVWriterBuilder<UpdLeadReasign>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLeadAsign(DBConfig config, UpdLeadReasign params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdLeadAssignV2(DBConfig config, UpdLeadReassignV2 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadReassignV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadReassignV2> csvWriter = new CSVWriterBuilder<UpdLeadReassignV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLeadAssignV2(DBConfig config, UpdLeadReassignV2 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdLeadAssignV3(DBConfig config, UpdLeadReassignV3 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadReassignV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadReassignV3> csvWriter = new CSVWriterBuilder<UpdLeadReassignV3>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLeadAssignV3(DBConfig config, UpdLeadReassignV3 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    /* #begin: update un-call */
    @SuppressWarnings("rawtypes")
    public DBResponse<?> fileUpdateUnCall(DBConfig config, GetUncallLeadV2 params) {
        DBResponse<?> response = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<GetUncallLeadV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<GetUncallLeadV2> csvWriter = new CSVWriterBuilder<GetUncallLeadV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            response.setErrorMsg(e.getMessage());
            response.setErrorCode(1);
        }
        return response;
    }

    @SuppressWarnings("rawtypes")
    public DBResponse<?> dbUpdateUnCall(DBConfig config, GetUncallLeadV2 params) {

        DBResponse<?> response = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> sqlParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(sqlParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<?, ?> map = simpleJdbcCall.execute(in);

        response.setErrorMsg((String) map.get("out_failmessage"));
        response.setErrorCode((Integer) map.get("out_status"));

        return response;
    }
    /* #end: update un-call */

    public DBResponse<String> fileUpdateUnCallV2(DBConfig config, UpdUncallV2 params) {
        DBResponse<String> response = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdUncallV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdUncallV2> csvWriter = new CSVWriterBuilder<UpdUncallV2>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            response.setErrorMsg(e.getMessage());
            response.setErrorCode(1);
        }
        return response;
    }

    public DBResponse<?> dbUpdateUnCallV2(DBConfig config, UpdUncallV2 params) {

        DBResponse<?> response = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> sqlParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(sqlParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<?, ?> map = simpleJdbcCall.execute(in);

        response.setErrorMsg((String) map.get("out_failmessage"));
        response.setErrorCode((Integer) map.get("out_status"));

        return response;
    }

    public DBResponse<String> fileUpdateUnCallV3(DBConfig config, UpdUncallV3 params) {
        DBResponse<String> response = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdUncallV3> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdUncallV3> csvWriter = new CSVWriterBuilder<UpdUncallV3>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            response.setErrorMsg(e.getMessage());
            response.setErrorCode(1);
        }
        return response;
    }

    public DBResponse<?> dbUpdateUnCallV3(DBConfig config, UpdUncallV3 params) {

        DBResponse<?> response = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> sqlParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(sqlParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<?, ?> map = simpleJdbcCall.execute(in);

        response.setErrorMsg((String) map.get("out_failmessage"));
        response.setErrorCode((Integer) map.get("out_status"));

        return response;
    }

    public DBResponse fileInsGroupAgent(DBConfig config, InsGroupAgent params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsGroupAgent> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsGroupAgent> csvWriter = new CSVWriterBuilder<InsGroupAgent>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsGroupAgent(DBConfig config, InsGroupAgent params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelGroupAgent(DBConfig config, DelGroupAgent params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdGroupAgent(DBConfig config, UpdGroupAgent params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdGroupAgent> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdGroupAgent> csvWriter = new CSVWriterBuilder<UpdGroupAgent>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdGroupAgent(DBConfig config, UpdGroupAgent params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsIntegratePartner(DBConfig config, InsLogIntegratePartner params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsLogIntegratePartner> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsLogIntegratePartner> csvWriter = new CSVWriterBuilder<InsLogIntegratePartner>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsIntegratePartner(DBConfig config, InsLogIntegratePartner params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsLogLastPostbackPartner(DBConfig config, LogLastPostbackPartner params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogLastPostbackPartner> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogLastPostbackPartner> csvWriter = new CSVWriterBuilder<LogLastPostbackPartner>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsLogLastPostbackPartner(DBConfig config, LogLastPostbackPartner params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdLogLastPostbackPartner(DBConfig config, LogLastPostbackPartner params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogLastPostbackPartner> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogLastPostbackPartner> csvWriter = new CSVWriterBuilder<LogLastPostbackPartner>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLogLastPostbackPartner(DBConfig config, LogLastPostbackPartner params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsLastmileStatus(DBConfig config, InsLastmileStatus params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsLastmileStatus> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsLastmileStatus> csvWriter = new CSVWriterBuilder<InsLastmileStatus>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsLastmileStatus(DBConfig config, InsLastmileStatus params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsOrDepartment(DBConfig config, InsOrDepartment params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsOrDepartment> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsOrDepartment> csvWriter = new CSVWriterBuilder<InsOrDepartment>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsOrDepartment(DBConfig config, InsOrDepartment params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdOrDepartment(DBConfig config, UpdOrDepartment params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdOrDepartment> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdOrDepartment> csvWriter = new CSVWriterBuilder<UpdOrDepartment>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdOrDepartment(DBConfig config, UpdOrDepartment params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsOrOffer(DBConfig config, InsOffer params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsOffer> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsOffer> csvWriter = new CSVWriterBuilder<InsOffer>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsOrOffer(DBConfig config, InsOffer params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileUpdOrOffer(DBConfig config, UpdOrOffer params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdOrOffer> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdOrOffer> csvWriter = new CSVWriterBuilder<UpdOrOffer>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdOrOffer(DBConfig config, UpdOrOffer params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsLcNeighborhood(DBConfig config, InsLcNeighborhood params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsLcNeighborhood> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsLcNeighborhood> csvWriter = new CSVWriterBuilder<InsLcNeighborhood>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsLcNeighborhood(DBConfig config, InsLcNeighborhood params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsLcPostaCode(DBConfig config, InsLcPostaCode params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsLcPostaCode> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsLcPostaCode> csvWriter = new CSVWriterBuilder<InsLcPostaCode>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsLcPostaCode(DBConfig config, InsLcPostaCode params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelNeighborhood(DBConfig config, DelLcNeighborhood params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileInsBpPartner(DBConfig config, InsBpPartner params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsBpPartner> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsBpPartner> csvWriter = new CSVWriterBuilder<InsBpPartner>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsBpPartner(DBConfig config, InsBpPartner params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SqlParameter[] inParametersArray = inParameters.toArray(new SqlParameter[]{});
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParametersArray)
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbInsCLFreshV6(DBConfig config, InsCLFreshV6 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse dbDelCLInactive(DBConfig config, Integer leadId) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(new SqlParameter("in_lead_id", Types.INTEGER),
                        new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = new MapSqlParameterSource().addValue("in_lead_id", leadId);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse fileLogSendSmsDeliveryOrder(DBConfig config, LogSendSmsDeliveryOrder params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<LogSendSmsDeliveryOrder> converter = AppUtils.ob2Strr(params);
            CSVWriter<LogSendSmsDeliveryOrder> csvWriter = new CSVWriterBuilder<LogSendSmsDeliveryOrder>(out)
                    .strategy(CSVStrategy.UK_DEFAULT).entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbLogSendSmsDeliveryOrder(DBConfig config, LogSendSmsDeliveryOrder params) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            logger.info(mapper.writeValueAsString(params));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        try {
            logger.info(mapper.writeValueAsString(inParameters));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));
        try {
            logger.info("===========================================\r\n{}", mapper.writeValueAsString(rs));
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return rs;
    }

    public DBResponse<String> fileInsCLFreshV7(DBConfig config, InsCLFreshV7 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV7> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV7> csvWriter = new CSVWriterBuilder<InsCLFreshV7>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbInsCLFreshV7(DBConfig config, InsCLFreshV7 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse<String> fileInsCLFreshV10(DBConfig config, InsCLFreshV10 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV10> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV10> csvWriter = new CSVWriterBuilder<InsCLFreshV10>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbInsCLFreshV10(DBConfig config, InsCLFreshV10 params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse<String> fileUpdLead(DBConfig config, UpdLeadV6 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadV6> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadV6> csvWriter = new CSVWriterBuilder<UpdLeadV6>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLead(DBConfig config, UpdLeadV6 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

	public DBResponse<String> fileInsCLFreshV8(DBConfig config, InsCLFreshV8 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsCLFreshV8> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsCLFreshV8> csvWriter = new CSVWriterBuilder<InsCLFreshV8>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
	}

	public DBResponse dbInsCLFreshV8(DBConfig config, InsCLFreshV8 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
	}

    public DBResponse<String> fileInsMtkData(DBConfig config, InsMtkData params) {
        DBResponse<String> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsMtkData> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsMtkData> csvWriter = new CSVWriterBuilder<InsMtkData>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbInsMtkData(DBConfig config, InsMtkData params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse<String> fileInsMktData(DBConfig config, InsMktData params) {
        DBResponse<String> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<InsMktData> converter = AppUtils.ob2Strr(params);
            CSVWriter<InsMktData> csvWriter = new CSVWriterBuilder<InsMktData>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbInsMktData(DBConfig config, InsMktData params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

	public DBResponse<String> fileUpdLead(DBConfig config, UpdLeadV7 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadV7> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadV7> csvWriter = new CSVWriterBuilder<UpdLeadV7>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
	}

	public DBResponse dbUpdLead(DBConfig config, UpdLeadV7 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
	}

    public DBResponse<String> fileUpdLead(DBConfig config, UpdLeadV8 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadV8> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadV8> csvWriter = new CSVWriterBuilder<UpdLeadV8>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLead(DBConfig config, UpdLeadV8 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse<String> fileUpdLead(DBConfig config, UpdLeadV9 params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadV9> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadV9> csvWriter = new CSVWriterBuilder<UpdLeadV9>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLead(DBConfig config, UpdLeadV9 params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse<String> fileUpdMktData(DBConfig config, UpdMktData params) {
        DBResponse<String> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdMktData> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdMktData> csvWriter = new CSVWriterBuilder<UpdMktData>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbUpdMktData(DBConfig config, UpdMktData params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
	}

    public DBResponse<String> fileUpdUserV2(DBConfig config, UpdUserV2 params) {
        DBResponse<String> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdUserV2> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdUserV2> csvWriter = new CSVWriterBuilder<UpdUserV2>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbUpdUserV2(DBConfig config, UpdUserV2 params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
	}

    public DBResponse fileUpdLeadReleaseReserved(DBConfig config, UpdLeadReleaseReserved params) {
        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadReleaseReserved> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadReleaseReserved> csvWriter = new CSVWriterBuilder<UpdLeadReleaseReserved>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse dbUpdLeadReleaseReserved(DBConfig config, UpdLeadReleaseReserved params) {

        DBResponse rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }

    public DBResponse<String> fileUpdLeadCrmActionType(DBConfig config, UpdLeadCrmActionType params) {
        DBResponse<String> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_FILE);
        try {
            Writer out = new FileWriter(getPath(config.getFuncName()), true);
            CSVEntryConverter<UpdLeadCrmActionType> converter = AppUtils.ob2Strr(params);
            CSVWriter<UpdLeadCrmActionType> csvWriter = new CSVWriterBuilder<UpdLeadCrmActionType>(out).strategy(CSVStrategy.UK_DEFAULT)
                    .entryConverter(converter).build();
            csvWriter.write(params);
            csvWriter.flush();
            AppUtils.close(out);
        } catch (IOException e) {
            rs.setErrorMsg(e.getMessage());
            rs.setErrorCode(1);
        }
        return rs;
    }

    public DBResponse<?> dbUpdLeadCrmActionType(DBConfig config, UpdLeadCrmActionType params) {

        DBResponse<?> rs = new DBResponse<>(DBResponse.DB_CONN_TYPE_DB);
        List<SqlParameter> inParameters = AppUtils.ob2SqlInParams(params, false);
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(dbTemplate).withFunctionName(config.getFuncName())
                .withoutProcedureColumnMetaDataAccess().declareParameters(inParameters.toArray(new SqlParameter[]{}))
                .declareParameters(new SqlOutParameter("out_status", Types.INTEGER),
                        new SqlOutParameter("out_failmessage", Types.VARCHAR));

        SqlParameterSource in = AppUtils.ob2SqlSource(params);
        Map<String, Object> map = simpleJdbcCall.execute(in);

        rs.setErrorMsg((String) map.get("out_failmessage"));
        rs.setErrorCode((Integer) map.get("out_status"));

        return rs;
    }
}
