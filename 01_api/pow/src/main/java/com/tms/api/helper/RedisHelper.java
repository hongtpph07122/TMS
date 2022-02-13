package com.tms.api.helper;

import com.tms.api.dto.ObjExpire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by dinhanhthai on 27/08/2019.
 */
public class RedisHelper {
    private static final Logger logger = LoggerFactory.getLogger(RedisHelper.class);

    private static ObjExpire<Integer> _RECENT_EXPIRE_OBJ;
    @Value("${config.redis-suffix}")
    private String REDIS_SUFFIX;
    @Value("${config.redis-suffix}")
    private static String REDIS_MASTER_KEY;

    public static String createRedisKey(String prefix, int orgId, String key) {
        return String.format("%s%s", Helper.createRedisPrefix(prefix, orgId), key);
    }

    public static String createRedisKey(String prefix, int orgId, Integer userId) {
        return String.format("%s%s", Helper.createRedisPrefix(prefix, orgId), userId);
    }

    public static String createLogAgentTraceRedisKey(Integer orgId, Integer activityId, Integer agentId) {
        return String.format("%s:agent.state:org:%s:activity:%s:user:%s", REDIS_MASTER_KEY, orgId, activityId, agentId);
    }

    public static String createAgentRedisKey(String sip, String phone) {
        return String.format("%s:sip:%s:phone:%s", REDIS_MASTER_KEY, sip, phone);
    }

    public static String createRedisKey(String prefix, int orgId, String leadPhone, String channel) {
        return String.format("%s%s%s", Helper.createRedisPrefix(prefix, orgId), leadPhone, channel);
    }

    public static void setExpireAtEndDay(StringRedisTemplate stringRedisTemplate, String key) {
        Date todayEnd = new Date();
        todayEnd.setHours(23);
        todayEnd.setMinutes(59);
        todayEnd.setSeconds(59);
        stringRedisTemplate.expireAt(key, todayEnd);
    }

    public static void setExpire(StringRedisTemplate stringRedisTemplate, int expInSecond, String key) {
        stringRedisTemplate.expire(key, expInSecond, TimeUnit.SECONDS);
    }

    public static String getGlobalParamValue(StringRedisTemplate stringRedisTemplate, int orgId, Integer type, Integer paramId) {
        String key = Helper.createRedisKey(Const.REDIS_PREFIX_GLOBAL, String.valueOf(orgId));
        String value = getKey(stringRedisTemplate, key, String.format("%s:%s", type, paramId));
        return value;
    }

    private static void setRecentExpireObj(int USER_EXPIRE_TIME, int _curOrgId, StringRedisTemplate stringRedisTemplate) {

        _RECENT_EXPIRE_OBJ.setValue(Integer.parseInt(getGlobalParamValue(stringRedisTemplate, _curOrgId, 3, 4)));
        Date now = new Date();
        _RECENT_EXPIRE_OBJ.setExprireAt(new Date(now.getTime() + USER_EXPIRE_TIME * 60 * 1000));
    }

    public static void saveRedisRecent(int _curOrgId, int _userId, int leadId, int USER_EXPIRE_TIME, StringRedisTemplate stringRedisTemplate) {
        if (_RECENT_EXPIRE_OBJ == null) {
            _RECENT_EXPIRE_OBJ = new ObjExpire();
        }

        if (_RECENT_EXPIRE_OBJ.isExprire()) {
            setRecentExpireObj(USER_EXPIRE_TIME, _curOrgId, stringRedisTemplate);
        }
        String key = RedisHelper.createRedisKey(Const.REDIS_RECENT_LEAD, _curOrgId, String.valueOf(leadId));
        logger.info("saveRedisRecent {}", _RECENT_EXPIRE_OBJ.getValue());
        saveRedis(stringRedisTemplate, String.valueOf(_userId), key, _RECENT_EXPIRE_OBJ.getValue());
    }

    public static void saveRedis(StringRedisTemplate stringRedisTemplate, String key, String properties, String value, boolean isExpire) {
        HashOperations<String, String, String> hash = stringRedisTemplate.opsForHash();

        Map<String, String> empJoeMap = new HashMap<>();
        empJoeMap.put(properties, value);
        //hash.putAll(key, empJoeMap);
        saveRedis(hash, empJoeMap, key);
        if (isExpire)
            setExpireAtEndDay(stringRedisTemplate, key);
    }
    public static void setExpireAt(StringRedisTemplate stringRedisTemplate, String key, Date expireDate){
        stringRedisTemplate.expireAt(key, expireDate);
    }

    public static void saveRedis(StringRedisTemplate stringRedisTemplate, String key, String properties, String value, Date expireAt) {
        HashOperations<String, String, String> hash = stringRedisTemplate.opsForHash();

        Map<String, String> empJoeMap = new HashMap<>();
        empJoeMap.put(properties, value);
        //hash.putAll(key, empJoeMap);
        saveRedis(hash, empJoeMap, key);
        if (expireAt != null)
            setExpireAt(stringRedisTemplate, key, expireAt);
    }



    public static void saveRedis(StringRedisTemplate stringRedisTemplate, String key, String properties, String value) {
        saveRedis(stringRedisTemplate, key, properties, value, true);
    }

    public static Map<String, String> getRedis(StringRedisTemplate stringRedisTemplate, String key) {
        HashOperations<String, String, String> hash = stringRedisTemplate.opsForHash();
        return hash.entries(key);
    }

    public static void saveRedis(StringRedisTemplate stringRedisTemplate, Map<String, String> map, String key) {
        saveRedis(stringRedisTemplate, map, key, true);
    }

    public static void saveRedis(StringRedisTemplate stringRedisTemplate, Map<String, String> map, String key, boolean isExpire) {
        HashOperations<String, String, String> hash = stringRedisTemplate.opsForHash();
        hash.putAll(key, map);
        if (isExpire)
            setExpireAtEndDay(stringRedisTemplate, key);
    }

    //save string - value
    public static void saveRedis(StringRedisTemplate stringRedisTemplate, String value, String key, int second) {
        saveRedis(stringRedisTemplate, value, key, second, true);
    }

    public static void saveRedis(StringRedisTemplate stringRedisTemplate, String value, String key, int second, boolean isExpire) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key, value);
        if (isExpire)
            setExpire(stringRedisTemplate, second, key);
    }

    public static void saveRedis(HashOperations<String, String, String> hash, Map<String, String> map, String key) {
        hash.putAll(key, map);

    }

    public static List<String> getAllKey(StringRedisTemplate stringRedisTemplate, String key) {
        Set<String> keys = stringRedisTemplate.keys(key);
        Iterator iterator = keys.iterator();
        List<String> lst = new ArrayList<>();
        while (iterator.hasNext()) {
            String element = (String) iterator.next();
            lst.add(element);
        }
        return lst;
    }

    public static Long getTTLKey(StringRedisTemplate stringRedisTemplate, String key) {
        return stringRedisTemplate.getExpire(key);
    }

    public static void caculateRateByTotalLead(StringRedisTemplate stringRedisTemplate) {
        //1. lay so luong lead
        String key = Helper.createRedisKey(Const.REDIS_PREFIX_STATIC, "*");
        List<String> lstKeys = RedisHelper.getAllKey(stringRedisTemplate, key);
        for (String lstKey : lstKeys) {
            logger.info("Current Key is {}", lstKey);

            String curTotalLead = RedisHelper.getKey(stringRedisTemplate, lstKey, Const.REDIS_FIELD_TOTAL_LEAD);
            String curOrg = lstKey.replace(Const.REDIS_PREFIX_STATIC + ":", "");

            if (curTotalLead == null || curTotalLead == "0")
                continue;

            double totalLead = Double.parseDouble(curTotalLead);

            //2. lay toan bo agent trong cf_agent_rate_daily
            String rateKey = Helper.createRedisKey(Const.REDIS_PREFIX_RATE, curOrg);
            Map<String, String> map = RedisHelper.getRedis(stringRedisTemplate, rateKey);

            Map<String, String> staticMap = RedisHelper.getRedis(stringRedisTemplate, Helper.createRedisKey(Const.REDIS_PREFIX_STATIC, curOrg));

            double totalScore = 0D;
            for (String keyMap : map.keySet()) {
                if (staticMap.containsKey(keyMap)) {
                    double tmp = Double.parseDouble(map.get(keyMap));
                    totalScore += tmp;
                }
            }

            Map<String, String> maxLeadMap = new HashMap<>();
            for (String keyMap : map.keySet()) {
                if (staticMap.containsKey(keyMap)) {
                    double rate = Math.ceil((Double.parseDouble(map.get(keyMap)) / totalScore) * totalLead);
                    maxLeadMap.put(keyMap, String.valueOf((int) rate));
                    logger.info("Org :{} - {} -- {}", curOrg, keyMap, rate);
                }
            }
            if (maxLeadMap.size() > 0) {
                RedisHelper.saveRedis(stringRedisTemplate, maxLeadMap, lstKey, false);//ko expire key
            }

        }
    }

    public static String getKey(StringRedisTemplate stringRedisTemplate, String key, String field) {
        Object obj = (stringRedisTemplate.opsForHash().get(key, field));
        return (obj == null ? null : String.valueOf(obj));
    }

    public static String getKey(StringRedisTemplate stringRedisTemplate, String key) {
        Object obj = (stringRedisTemplate.opsForValue().get(key));
        return (obj == null ? null : String.valueOf(obj));
    }

    public static void deleteRedis(StringRedisTemplate stringRedisTemplate, String key) {
        stringRedisTemplate.opsForHash().delete(key);
    }

    public static Double increase(StringRedisTemplate stringRedisTemplate, String key, String hashkey) {
        return stringRedisTemplate.opsForHash().increment(key, hashkey, 1D);
    }

    public static Double incrementLead(StringRedisTemplate stringRedisTemplate, String key) {
        return increase(stringRedisTemplate, key, Const.REDIS_FIELD_TOTAL_LEAD);
    }

    public static Integer sizeOfKey(StringRedisTemplate stringRedisTemplate, String key) {
        return stringRedisTemplate.opsForHash().size(key).intValue();
    }


    public static void saveListToRedis(StringRedisTemplate stringRedisTemplate, List<String> lst, int userId, int orgId) {
        saveListToRedis(stringRedisTemplate, lst, userId, orgId, EnumType.LEAD_TYPE.NEW);
    }

    /**
     * Default is lead type = new
     *
     * @param stringRedisTemplate
     * @param value
     * @param userId
     * @param orgId
     */
    public static void saveListToRedis(StringRedisTemplate stringRedisTemplate, String value, int userId, int orgId) {
        //String key = String.format("%s:%s:%s", Const.REDIS_PREFIX_USER, orgId, userId);
        saveListToRedis(stringRedisTemplate, value, userId, orgId, EnumType.LEAD_TYPE.NEW);
    }

    /**
     * Only NEW or UNCALL
     *
     * @param stringRedisTemplate
     * @param lst
     * @param userId
     * @param orgId
     * @param leadType
     */
    public static void saveListToRedis(StringRedisTemplate stringRedisTemplate, List<String> lst, int userId, int orgId, EnumType.LEAD_TYPE leadType) {
        String key = String.format("%s:%s:%s", leadType.getType(), orgId, userId);
        saveListToRedis(stringRedisTemplate, lst, key);
    }

    public static void saveListToRedis(StringRedisTemplate stringRedisTemplate, String value, int userId, int orgId, EnumType.LEAD_TYPE leadType) {
        String key = String.format("%s:%s:%s", leadType.getType(), orgId, userId);
        saveListToRedis(stringRedisTemplate, value, key);
    }

    public static void saveListToRedis(StringRedisTemplate stringRedisTemplate, List<String> lst, String key) {
        stringRedisTemplate.opsForList().leftPushAll(key, lst);
    }

    public static void saveListToRedis(StringRedisTemplate stringRedisTemplate, String value, String key) {
        stringRedisTemplate.opsForList().leftPush(key, value);
    }

    public static Integer getSizeListRedisKey(StringRedisTemplate stringRedisTemplate, String key) {
        /*if(stringRedisTemplate.opsForList().hasKey(key)){

        }*/
        Long size = stringRedisTemplate.opsForList().size(key);
        return ((size == null) ? 0 : size.intValue());
    }

    public static boolean isMaxLeadByUser(StringRedisTemplate stringRedisTemplate, int org_id, int userId) {
        /*String key = Helper.createRedisKey(Const.REDIS_PREFIX_STATIC, String.valueOf(org_id));
        String maxLeadUser = getKey(stringRedisTemplate, key, String.valueOf(userId));
        if (maxLeadUser != null) {
            int maxLead = Integer.parseInt(maxLeadUser);
            //get current new lead by user by key: {leadType}:org:userId => new: list of new lead, uncall : list of uncall lead
            String newKey = String.format("%s:%s:%s", EnumType.LEAD_TYPE.NEW.getType(), org_id, userId);
//            Integer curCountLead = getSizeListRedisKey(stringRedisTemplate, newKey);
            Integer curCountLead = sizeOfKey(stringRedisTemplate, newKey);
            if (curCountLead < maxLead)
                return false;
        }

        return true;*/
        return isMaxLeadByUser(stringRedisTemplate, org_id, userId, 0);
    }

    public static boolean isMaxLeadByUser(StringRedisTemplate stringRedisTemplate, int org_id, int userId, int cpId) {
        String key = Helper.createRedisKey(Const.REDIS_PREFIX_STATIC, String.valueOf(org_id));
        if (cpId > 0) {
            key = String.format("%s:%s", key, cpId);
        }

        String maxLeadUser = getKey(stringRedisTemplate, key, String.valueOf(userId));
        if (maxLeadUser != null) {
            int maxLead = Integer.parseInt(maxLeadUser);
            //get current new lead by user by key: {leadType}:org:userId => new: list of new lead, uncall : list of uncall lead
            String newKey = String.format("%s:%s:%s", EnumType.LEAD_TYPE.NEW.getType(), org_id, userId);
           /* if(cpId > 0)
                newKey = String.format("%s:%s", newKey, cpId);*/

//            Integer curCountLead = getSizeListRedisKey(stringRedisTemplate, newKey);
            Integer curCountLead = sizeOfKey(stringRedisTemplate, newKey);
            if (curCountLead < maxLead)
                return false;
        }

        return true;
    }

    public static boolean isMaxUncallByUser(StringRedisTemplate stringRedisTemplate, int org_id, int userId) {
        //TODO need to read from db
        int maxUncall = 40;

        String uncall = String.format("%s:%s:%s", EnumType.LEAD_TYPE.UNCALL.getType(), org_id, userId);
//        Integer curCountLead = getSizeListRedisKey(stringRedisTemplate, uncall);
        Integer curCountLead = sizeOfKey(stringRedisTemplate, uncall);
        return curCountLead >= maxUncall;
    }

    public static boolean checkDuplicateAssignToAgent(StringRedisTemplate stringRedisTemplate, int orgId, String prodName, String name, String phone, int leadId, int agentId) {
        int hash = StringHelper.createHashkeyFromString(StringHelper.createHashkey(prodName, name, phone));
        String keyHash = RedisHelper.createRedisKey(Const.REDIS_PREFIX_BASKET, (orgId), String.valueOf(hash));
        String value = RedisHelper.getKey(stringRedisTemplate, keyHash);
        if (value != null) {
            if (value.contains(Const.SPLIT)) {//neu da duoc assign
                logger.info("HAS BEEN ASSIGNED {}", leadId);
            } else {
                value = agentId + Const.SPLIT + value;
                RedisHelper.saveRedis(stringRedisTemplate, value, keyHash, Const.LEAD_BASKET_MAX_LIFE, false);
                return true;
            }
        }
        return false;
    }

    public static Boolean deleteKey(StringRedisTemplate stringRedisTemplate, String key){
        return stringRedisTemplate.delete(key);
    }
}
