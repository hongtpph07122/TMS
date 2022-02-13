package com.oauthcentralization.app.tmsoauth2.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.oauthcentralization.app.tmsoauth2.variables.PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN;

public class LoggerUtils {

    private static final Logger logger = LoggerFactory.getLogger(LoggerUtils.class);

    /**
     * @param object - input object
     * @apiNote - transform object to string json
     */
    public static String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    public static JsonNode toJsonNode(Object object) {
        try {
            String json = toJson(object);
            return new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param object - input object
     * @apiNote - transform object to string json
     */
    public static String parseObsToStr(Object object) {
        Gson gson = new GsonBuilder().setDateFormat(BIBLIOGRAPHY_EPOCH_PATTERN).setPrettyPrinting().create();
        return gson.toJson(object);
    }

    /**
     * @param json  - input string json formatted
     * @param clazz - class object
     * @apiNote - transform string json to object class
     */
    public static <T> T parseJsonToObs(String json, Class<T> clazz) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat(BIBLIOGRAPHY_EPOCH_PATTERN);

            Gson gson = builder.create();
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> T parseStrToObs(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
