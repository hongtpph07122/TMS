package com.tms.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jcsv.writer.CSVEntryConverter;
import com.tms.config.DBConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class AppUtils {

    protected static ObjectMapper mapper = new ObjectMapper();
    private static Logger logger = LoggerFactory.getLogger(AppUtils.class);

    public static boolean isNotEmpty(String val) {
        return val != null && !"".equals(val);
    }
    public static void printLine(Logger log, String sessionId, String text) {
        log.debug(String.format(" - %s : === %s ===", sessionId, text));
    }

    public static void printInput(Logger log, String sessionId, DBConfig cf, List<String> excludeFields, Object obj) {

        log.debug(String.format(" - %s : ===== Calling: <%s> =====", sessionId, cf.getFuncName()));
        //log.debug(String.format(" - %s : === DB Server: %s ===", sessionId, cf.getConnectStr()));
        log.debug(String.format(" - %s : === MemTimeout: %s milisecs, DBTimeout: %s milisecs", sessionId, cf.getMemTimeout(), cf.getDBTimeout()));
        
        String json = "";
        try {
            //json =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        	json =  mapper.writeValueAsString(obj);
        	log.debug(String.format(" - %s : === Input: === %s", sessionId, json));
        } catch (Exception e) {
        }
        
//        //Map<String, Object> mapFields = AISLogUtil.ob2Map(obj);
//        log.debug(String.format(" - %s : === Input: ===", sessionId));
//        if (isNotEmpty(json)) {
//            String[] split = json.split("\\n");
//            if (excludeFields != null && excludeFields.size() > 0) {
//                for (String key : split) {
//                    if (!excludeFields.contains(key)) {
//                        log.debug(String.format(" - %s : <-- %s", sessionId, key));
//                    }
//                }
//            } else {
//                for (String key : split) {
//                    log.debug(String.format(" - %s : <-- %s", sessionId, key));
//                }
//            }
//        }

    }

    public static void printOutput(Logger log, String sessionId, DBConfig cf, List<String> excludeFields, Object obj) {
        //Map<String, Object> mapFields = AISLogUtil.ob2Map(obj);
        String json = "";
        try {
            json =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
        }
        log.debug(String.format(" - %s : === Output: <%s>  ===", sessionId, cf.getFuncName()));
        if (isNotEmpty(json)) {
            String[] split = json.split("\\n");
            if (excludeFields != null && excludeFields.size() > 0) {
                for (String key : split) {
                    if (!excludeFields.contains(key)) {
                        log.debug(String.format(" - %s : --> %s", sessionId, key));
                    }
                }
            } else {
                for (String key : split) {
                    log.debug(String.format(" - %s : --> %s", sessionId, key));
                }
            }
        }
    }

    public static void printInsert(Logger log, String sessionId, DBConfig cf, List<String> excludeFields, Object obj) {

        log.debug(String.format(" - %s : ===== Calling: <%s> =====", sessionId, cf.getFuncName()));
        String json = "";
        try {
            json =  mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
        }
        //Map<String, Object> mapFields = AISLogUtil.ob2Map(obj);
        log.debug(String.format(" - %s : === Input: ===", sessionId));
        if (isNotEmpty(json)) {
            String[] split = json.split("\\n");
            if (excludeFields != null && excludeFields.size() > 0) {
                for (String key : split) {
                    if (!excludeFields.contains(key)) {
                        log.debug(String.format(" - %s : <-- %s", sessionId, key));
                    }
                }
            } else {
                for (String key : split) {
                    log.debug(String.format(" - %s : <-- %s", sessionId, key));
                }
            }
        }

    }



    private static String toRevertCamelCase(String str) {
        String camelCaseString = "";
        if (isNotEmpty(str)) {
            String[] r = str.split("(?=\\p{Upper})");
            return camelCaseString = String.join("-", r).toUpperCase();
        }
        return null;
    }

    private static String toRevertCamelCaseLowercase(String str) {
        if (isNotEmpty(str)) {
            String[] r = str.split("(?=\\p{Upper})|(?<=\\D)(?=\\d+\\b)");
            return  String.join("_", r).toLowerCase();
        }
        return null;
    }


    private static String lowserCaseFirst(String str) {
        if (isNotEmpty(str)) {
            return str.substring(0, 1).toLowerCase() + str.substring(1);
        }
        return "";
    }

    private static String upperCaseFirst(String str) {
        if (isNotEmpty(str)) {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
        return "";
    }


    public static <T> CSVEntryConverter<T> ob2Converter(T config) {
        Field[] fields = config.getClass().getDeclaredFields();
        String[] res = new String[fields.length];
        Method method;
        int i = 0;
        for (Field f : fields) {
            try {
                method = config.getClass().getMethod(
                        "get" + upperCaseFirst(f.getName()));

                Object invoke = method.invoke(config);
                if (invoke != null) {
                    res[i++] = String.valueOf(invoke);
                } else {
                    res[i++] = "";
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        final String[] result = res;
        return new CSVEntryConverter<T>() {
            @Override
            public String[] convertEntry(T t) {
                return result;
            }
        };
    }

    public static void close(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {

            }
        }
    }

    public static String getDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(date);
    }

    public static <T> MapSqlParameterSource ob2SqlSource(T config) {
        return ob2SqlSource(config, false);
    }

    public static <T> MapSqlParameterSource ob2SqlSource(T config, boolean cursor) {
        MapSqlParameterSource in = new MapSqlParameterSource();
        Field[] fields = config.getClass().getDeclaredFields();
        Method method;
        for (Field f : fields) {
            String name = f.getName();
            String reName = "in_" + toRevertCamelCaseLowercase(name);
            if (isNotEmpty(reName)) {
                try {
                    method = config.getClass().getMethod(
                            "get" + upperCaseFirst(name));

                    Object invoke = method.invoke(config);
                    in.addValue(reName, invoke);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        if (cursor) {
            in.addValue("inout_rc_name", "c_cursor");
        }
        return in;
    }

    public static <T> List<SqlParameter> ob2SqlInParams(T config, boolean inOut) {
        return ob2SqlInParams(config,inOut, false);
    }

    public static <T> List<SqlParameter> ob2SqlInParams(T config, boolean inOut, boolean out) {
        Field[] fields = config.getClass().getDeclaredFields();
        List<SqlParameter> inParams = new ArrayList<>();
        for (Field f : fields) {
            String name = f.getName();
            String reName = "in_" + toRevertCamelCaseLowercase(name);
            if (isNotEmpty(reName)) {
                try {

                    SqlParameter sqlParameter = null;
                    if(f.getType().isAssignableFrom(Integer.class)){
                        sqlParameter = new SqlParameter(reName, Types.INTEGER);
                    } else if(f.getType().isAssignableFrom(String.class) && (name.equals("attribute") || name.equals("jsonLog")
                            || name.equals("packageListItem")|| name.equals("chatId")|| name.equals("pbAll")|| name.equals("requestJson")|| name.equals("responseJson"))){
                        sqlParameter = new SqlParameter(reName, Types.OTHER);
                    } else if(f.getType().isAssignableFrom(String.class)){
                        sqlParameter = new SqlParameter(reName, Types.VARCHAR);
                    } else if(f.getType().isAssignableFrom(Double.class)){
                        sqlParameter = new SqlParameter(reName, Types.NUMERIC);
                    } else if(f.getType().isAssignableFrom(Boolean.class)){
                        sqlParameter = new SqlParameter(reName, Types.BOOLEAN);
                    }
                    if (sqlParameter != null) {
                        inParams.add(sqlParameter);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        if (inOut) {
            inParams.add(new SqlParameter("inout_rc_name", Types.OTHER));
        }

        if (out) {
            inParams.add(new SqlOutParameter("out_status", Types.INTEGER));
            inParams.add(new SqlOutParameter("out_failmessage", Types.VARCHAR));
            inParams.add(new SqlOutParameter("out_countrow", Types.INTEGER));
        }

        return inParams;
    }



    public static <T> CSVEntryConverter<T> ob2Strr(T config) {
        Field[] fields = config.getClass().getDeclaredFields();
        String[] res = new String[fields.length];
        Method method;
        int i = 0;
        for (Field f : fields) {
            try {
                method = config.getClass().getMethod(
                        "get" + upperCaseFirst(f.getName()));

                Object invoke = method.invoke(config);
                if (invoke != null) {
                    res[i++] = String.valueOf(invoke);
                } else {
                    res[i++] = "";
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        final String[] result = res;
        return new CSVEntryConverter<T>() {
            @Override
            public String[] convertEntry(T t) {
                return result;
            }
        };
    }

}
