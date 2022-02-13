package com.tms.api.helper;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.exception.TMSException;
import com.tms.entity.log.LogLead;
import com.tms.service.impl.LogService;

/**
 * Created by dinhanhthai on 18/06/2019.
 */
public class LogHelper {
    private static final Logger logger = LoggerFactory.getLogger(LogHelper.class);


    public static String toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // convert user object to json string and return it
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    public static boolean isJSONValid(String jsonInString ) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void writeLogLead(LogService logService, int lead_id, int userId, int newStatus, String onField, String comment, String json, String sessionId) throws TMSException {
        LogLead logLead = new LogLead();
        logLead.setLeadId(lead_id);
        logLead.setUserId(userId);
        logLead.setComment(comment);
        logLead.setNewValue(String.valueOf(newStatus));
        logLead.setOnField(onField);
        logLead.setJsonLog(json);

//        return logLead;
        logService.logLead(sessionId, logLead);
    }
}
