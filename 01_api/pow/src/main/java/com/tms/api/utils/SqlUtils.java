package com.tms.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class SqlUtils {
    private static final Logger logger = LoggerFactory.getLogger(SqlUtils.class);

    public static String toRevertCamelCaseLowercase(String str) {
        String[] r = str.split("(?=\\p{Upper})|(?<=\\D)(?=\\d+\\b)");
        return String.join("_", r).toLowerCase();
    }

    public static String upperCaseFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getParamsWithoutLimitOffSet(Class<?> config) {
        Field[] fields = config.getDeclaredFields();
        StringBuilder result = new StringBuilder();

        for (Field field : fields) {
            if (field.getName().equals("limit") || field.getName().equals("offset"))
                continue;

            String name = toRevertCamelCaseLowercase(field.getName());
            result.append(name).append(", ");
        }

        return result.substring(0, result.length() - 2);
    }

    public static String getParams(Class<?> config) {
        Field[] fields = config.getDeclaredFields();
        StringBuilder result = new StringBuilder();

        for (Field field : fields) {
            String name = toRevertCamelCaseLowercase(field.getName());
            result.append(name).append(", ");
        }

        return result.substring(0, result.length() - 2);
    }

    public static <T> boolean checkMethodGetValueOfFieldIsNull(T params, Field field) {
        try {
            Method method = params.getClass().getMethod(
                    "get" + SqlUtils.upperCaseFirst(field.getName()));
            Object value = method.invoke(params);
            return value == null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public static <T> Object getValueFromNameOfObject(String name, T params) {
        try {
            Method method = params.getClass().getMethod(
                    "get" + SqlUtils.upperCaseFirst(name));
            return method.invoke(params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> String getWhereParamsSql(T params) {
        Field[] fields = params.getClass().getDeclaredFields();
        StringBuilder result = new StringBuilder();
        String operator;

        for (Field field : fields) {
            if (field.getName().equals("limit") || field.getName().equals("offset"))
                continue;
            if (SqlUtils.checkMethodGetValueOfFieldIsNull(params, field))
                continue;
            String name = SqlUtils.toRevertCamelCaseLowercase(field.getName());
            if (field.getType().isAssignableFrom(List.class))
                operator = " in :";
            else
                operator = " = :";
            result.append(" and ")
                    .append(name)
                    .append(operator)
                    .append(field.getName());
        }
        return result.toString();
    }

    public static <T> void setQueryParams(T config, Query query, Query queryCount) {
        Field[] fields = config.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (SqlUtils.checkMethodGetValueOfFieldIsNull(config, field))
                continue;
            query.setParameter(field.getName(), getValueFromNameOfObject(field.getName(), config));
            queryCount.setParameter(field.getName(), getValueFromNameOfObject(field.getName(), config));
        }
    }
}
