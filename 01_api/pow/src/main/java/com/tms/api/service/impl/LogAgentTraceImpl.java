package com.tms.api.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tms.api.entity.LogAgentTrace;
import com.tms.api.entity.OrUser;
import com.tms.api.helper.Const;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.RedisHelper;
import com.tms.api.repository.LogAgentTraceRepository;
import com.tms.api.repository.OrUserRepository;
import com.tms.api.response.PBXResponse;
import com.tms.api.service.LogAgentTraceService;
import com.tms.api.service.SynonymsConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LogAgentTraceImpl implements LogAgentTraceService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    LogAgentTraceRepository logAgentTraceRepository;

    @Autowired
    OrUserRepository orUserRepository;

    @Autowired
    SynonymsConfigurationService synonymsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public LogAgentTrace getLatestActivity(Integer agentId, Integer activityId, String redisKey) {
        String lastJsonAgentTrace = RedisHelper.getKey(stringRedisTemplate, redisKey);
        LogAgentTrace logAgentTrace;
        if (lastJsonAgentTrace != null && !lastJsonAgentTrace.equals("")) {
            Gson gson = new GsonBuilder().setDateFormat(Const.LOG_AGENT_TIME_FORMAT).create();
            logAgentTrace = gson.fromJson(lastJsonAgentTrace, LogAgentTrace.class);
        } else {
            logAgentTrace = logAgentTraceRepository.getLatestActivity(agentId, activityId);
            if (logAgentTrace != null && logAgentTrace.getId() != 0) {
                Gson gson = new GsonBuilder().setDateFormat(Const.LOG_AGENT_TIME_FORMAT).create();
                String jsonAgentTrace = gson.toJson(logAgentTrace);
                RedisHelper.saveRedis(stringRedisTemplate, jsonAgentTrace, redisKey, Const.LOG_AGENT_STATE_CACHE_LIVE_TIME);
            }
        }
        return logAgentTrace;
    }

    @Override
    public LogAgentTrace getLatestActivity(Integer agentId, Integer activityId, String redisKey, Integer lastMinutes) {
        LogAgentTrace logAgentTrace = getLatestActivity(agentId, activityId, redisKey);
        if (logAgentTrace != null && (logAgentTrace.getValueCode().equals(EnumType.AGENT_STATE.WRAP_UP.getValue()) ||
                logAgentTrace.getValueCode().equals(EnumType.AGENT_STATE.ON_CALL.getValue()))) {
            logAgentTrace = new LogAgentTrace.LogAgentTraceBuilder(agentId, activityId,
                    EnumType.AGENT_STATE.ASSIGNED.getName(), EnumType.AGENT_STATE.ASSIGNED.getValue(), new Date())
                    .setObjectId(logAgentTrace.getObjectId())
                    .setObjectType(logAgentTrace.getObjectType())
                    .setObjectValue(logAgentTrace.getObjectValue())
                    .setOnField(logAgentTrace.getOnField())
                    .setLastValueCode(logAgentTrace.getLastValueCode())
                    .setLastValue(logAgentTrace.getLastValue())
                    .setMessage(logAgentTrace.getMessage())
                    .setSessionId(logAgentTrace.getSessionId())
                    .setFlagCode(logAgentTrace.getFlagCode())
                    .setFlagValue(logAgentTrace.getFlagValue())
                    .setFlagMessage(logAgentTrace.getMessage())
                    .build();
        }
        return logAgentTrace;
    }

    @Override
    public OrUser getAgent(String sip, String phone) throws ParseException {
        String getAgentQuery = "SELECT u.user_id, u.org_id FROM or_user u " +
                "LEFT JOIN cl_fresh cf ON cf.assigned = u.user_id " +
                "WHERE u.phone LIKE :sip AND u.user_lock <> 1 AND cf.phone LIKE :phone";
        Query query = entityManager.createNativeQuery(getAgentQuery);
        query.setParameter("sip", "%" + sip + "%");
        query.setParameter("phone", "%" + phone + "%");
        List<Object[]> rows = query.getResultList();
        List<OrUser> list = new ArrayList<>();
        for (Object[] row : rows) {
            OrUser user = new OrUser();
            if (row[0] != null) {
                user.setUser_id((Integer) row[0]);
            }
            if (row[1] != null) {
                user.setOrg_id((Integer) row[1]);
            }
            list.add(user);
        }
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void setAgentState(LogAgentTrace agentTrace) {
        orUserRepository.updateStatusById(agentTrace.getValue(), agentTrace.getValueCode(), agentTrace.getMessage(), agentTrace.getActionTime(), agentTrace.getAgentId());
    }

    @Override
    public LogAgentTrace logActivity(LogAgentTrace agentTrace, String redisKey) throws ParseException {
        logAgentTraceRepository.save(agentTrace);
        if (agentTrace.getId() != 0) {
            SimpleDateFormat df = new SimpleDateFormat(Const.LOG_AGENT_TIME_FORMAT);
            agentTrace.setActionTime(df.parse(df.format(agentTrace.getActionTime())));
            Gson gson = new GsonBuilder().setDateFormat(Const.LOG_AGENT_TIME_FORMAT).create();
            String jsonAgentTrace = gson.toJson(agentTrace);
            RedisHelper.saveRedis(stringRedisTemplate, jsonAgentTrace, redisKey, Const.LOG_AGENT_STATE_CACHE_LIVE_TIME);
            setAgentState(agentTrace);
        }
        return agentTrace;
    }

    @Override
    public LogAgentTrace logPBXActivity(PBXResponse pbxResponse, OrUser agent) throws ParseException {
        String redisKey = RedisHelper.createLogAgentTraceRedisKey(agent.getOrg_id(), Const.LOG_AGENT_STATE, agent.getUser_id());
        Date actionTime = new SimpleDateFormat(Const.LOG_AGENT_TIME_FORMAT).parse(pbxResponse.getEventTime());
        String value;
        Integer valueCode;
        if (EnumType.AGENT_STATE.END.getName().equals(pbxResponse.getAction())) {
            if (!isAllowWrapUp(agent.getUser_id(), Const.LOG_AGENT_STATE, redisKey)) {
                return null;
            }
            valueCode = EnumType.AGENT_STATE.WRAP_UP.getValue();
            value = EnumType.AGENT_STATE.WRAP_UP.getName();
        } else {
            if (!isAllowOnCall(agent.getUser_id(), Const.LOG_AGENT_STATE, redisKey)) {
                return null;
            }
            valueCode = EnumType.AGENT_STATE.ON_CALL.getValue();
            value = EnumType.AGENT_STATE.ON_CALL.getName();
        }
        // Get last value
        // Prioritize last value input, if empty, get last value và REDIS
        LogAgentTrace lastAgentTrace = this.getLatestActivity(agent.getUser_id(), Const.LOG_AGENT_STATE, redisKey);
        String lastValue = null;
        UUID sessionId = null;
        Integer lastValueCode = 0;
        Integer flagCode = null;
        String flagValue = null;
        String flagMessage = null;
        if (lastAgentTrace != null) {
            lastValue = lastAgentTrace.getValue();
            sessionId = lastAgentTrace.getSessionId();
            lastValueCode = lastAgentTrace.getLastValueCode();
            flagCode = lastAgentTrace.getFlagCode();
            flagValue = lastAgentTrace.getFlagValue();
            flagMessage = lastAgentTrace.getFlagMessage();
        }
        LogAgentTrace agentTrace = new LogAgentTrace.LogAgentTraceBuilder(
                agent.getUser_id(), Const.LOG_AGENT_STATE, value, valueCode, actionTime)
                .setLastValue(lastValue)
                .setLastValueCode(lastValueCode)
                .setObjectType(pbxResponse.getDirection())
                .setObjectValue(pbxResponse.getUniqueId())
                .setOnField(pbxResponse.getStatus())
                .setSessionId(sessionId)
                .setFlagCode(flagCode)
                .setFlagValue(flagValue)
                .setFlagMessage(flagMessage)
                .build();
        logAgentTraceRepository.save(agentTrace);
        if (agentTrace.getId() != 0) {
            Gson gson = new GsonBuilder().setDateFormat(Const.LOG_AGENT_TIME_FORMAT).create();
            String jsonAgentTrace = gson.toJson(agentTrace);
            RedisHelper.saveRedis(stringRedisTemplate, jsonAgentTrace, redisKey, Const.LOG_AGENT_STATE_CACHE_LIVE_TIME);
            setAgentState(agentTrace);
        }
        return agentTrace;
    }

    @Override
    public LogAgentTrace validateAgentState(LogAgentTrace agentTrace, String redisKey) {
        Integer valueCode = agentTrace.getValueCode();
        if (Objects.equals(valueCode, EnumType.AGENT_STATE.AVAILABLE.getValue())) {
            if (this.isAllowAvailable(agentTrace.getAgentId(), agentTrace.getActivityId(), redisKey)) {
                return agentTrace;
            }
        } else if (Objects.equals(valueCode, EnumType.AGENT_STATE.UNAVAILABLE.getValue())) {
            if (this.isAllowUnavailable(agentTrace.getAgentId(), agentTrace.getActivityId(), redisKey)) {
                return agentTrace;
            }
        } else if (Objects.equals(valueCode, EnumType.AGENT_STATE.BREAK.getValue())) {
            if (this.isAllowBreak(agentTrace.getAgentId(), agentTrace.getActivityId(), redisKey)) {
                return agentTrace;
            }
        } else if (Objects.equals(valueCode, EnumType.AGENT_STATE.BUSY.getValue())) {
            if (this.isAllowBusy(agentTrace.getAgentId(), agentTrace.getActivityId(), redisKey)) {
                return agentTrace;
            }
        } else if (Objects.equals(valueCode, EnumType.AGENT_STATE.ASSIGNED.getValue())) {
            if (this.isAllowAssigned(agentTrace.getAgentId(), agentTrace.getActivityId(), redisKey)) {
                return agentTrace;
            }
        } else if (Objects.equals(valueCode, EnumType.AGENT_STATE.LOGOUT.getValue())) {
            return agentTrace;
        }
        return temporaryStateRequest(agentTrace, redisKey);
    }

    @Override
    public Boolean isAllowBreak(Integer agentId, Integer activityId, String redisKey) {
        LogAgentTrace lastLogAgentTrace = this.getLatestActivity(agentId, activityId, redisKey);
        if (lastLogAgentTrace != null) {
            return Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.AVAILABLE.getValue());
        }
        return false;
    }

    @Override
    public LogAgentTrace temporaryStateRequest(LogAgentTrace agentTrace, String redisKey) {
        Integer agentId = agentTrace.getAgentId();
        Integer activityId = agentTrace.getActivityId();
        Integer valueCode = agentTrace.getValueCode();
        if (!valueCode.equals(EnumType.AGENT_STATE.BREAK.getValue()) &&
                !valueCode.equals(EnumType.AGENT_STATE.UNAVAILABLE.getValue()) &&
                !valueCode.equals(EnumType.AGENT_STATE.BUSY.getValue())) {
            return null;
        }
        LogAgentTrace lastLogAgentTrace = this.getLatestActivity(agentId, activityId, redisKey);
        if (lastLogAgentTrace == null) {
            return agentTrace;
        }
        Integer lastValueCode = lastLogAgentTrace.getValueCode();
        if (lastValueCode.equals(valueCode)) {
            return null;
        }
        if (agentTrace.getOnField().equals(EnumType.AGENT_STATE.TOKEN.getName()) &&
                valueCode.equals(EnumType.AGENT_STATE.UNAVAILABLE.getValue())) {
            return lastLogAgentTrace;
        }
        if (Objects.equals(lastValueCode, EnumType.AGENT_STATE.ON_CALL.getValue()) ||
                Objects.equals(lastValueCode, EnumType.AGENT_STATE.ASSIGNED.getValue()) ||
                Objects.equals(lastValueCode, EnumType.AGENT_STATE.WRAP_UP.getValue())) {
            agentTrace = new LogAgentTrace.LogAgentTraceBuilder(agentId, activityId,
                    lastLogAgentTrace.getValue(), lastValueCode, new Date())
                    .setId(agentTrace.getId())
                    .setObjectId(lastLogAgentTrace.getObjectId())
                    .setObjectType(lastLogAgentTrace.getObjectType())
                    .setObjectValue(lastLogAgentTrace.getObjectValue())
                    .setOnField(lastLogAgentTrace.getOnField())
                    .setLastValueCode(lastLogAgentTrace.getLastValueCode())
                    .setLastValue(lastLogAgentTrace.getLastValue())
                    .setMessage(lastLogAgentTrace.getMessage())
                    .setSessionId(lastLogAgentTrace.getSessionId())
                    .setFlagCode(valueCode)
                    .setFlagMessage(agentTrace.getMessage())
                    .setFlagValue(agentTrace.getValue())
                    .build();
            agentTrace.setTemporary(true);
            return agentTrace;
        }
        return null;
    }

    @Override
    public LogAgentTrace convertTemporaryStateRequest(LogAgentTrace agentTrace) {
        String value = agentTrace.getFlagValue();
        String lastValue= agentTrace.getValue();
        Integer valueCode = agentTrace.getFlagCode();
        Integer lastValueCode = agentTrace.getValueCode();
        if (valueCode.equals(EnumType.AGENT_STATE.UNAVAILABLE.getValue())) {
            value = EnumType.AGENT_STATE.REQUESTED_UNAVAILABLE.getName();
            valueCode = EnumType.AGENT_STATE.REQUESTED_UNAVAILABLE.getValue();
        } else if (valueCode.equals(EnumType.AGENT_STATE.BREAK.getValue())) {
            value = EnumType.AGENT_STATE.REQUESTED_BREAK.getName();
            valueCode = EnumType.AGENT_STATE.REQUESTED_BREAK.getValue();
        } else if (valueCode.equals(EnumType.AGENT_STATE.BUSY.getValue())) {
            value = EnumType.AGENT_STATE.REQUESTED_BUSY.getName();
            valueCode = EnumType.AGENT_STATE.REQUESTED_BUSY.getValue();
        }
        agentTrace = new LogAgentTrace.LogAgentTraceBuilder(agentTrace.getAgentId(), agentTrace.getActivityId(),
                value, valueCode, new Date())
                .setId(agentTrace.getId())
                .setObjectId(agentTrace.getObjectId())
                .setObjectType(agentTrace.getObjectType())
                .setObjectValue(agentTrace.getObjectValue())
                .setOnField(agentTrace.getOnField())
                .setLastValueCode(lastValueCode)
                .setLastValue(lastValue)
                .setMessage(agentTrace.getFlagMessage())
                .setSessionId(agentTrace.getSessionId())
                .build();
        return agentTrace;
    }

    @Override
    public Boolean isAllowBusy(Integer agentId, Integer activityId, String redisKey) {
        LogAgentTrace lastLogAgentTrace = this.getLatestActivity(agentId, activityId, redisKey);
        if (lastLogAgentTrace != null) {
            return Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.BREAK.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.AVAILABLE.getValue());
        }
        return false;
    }

    @Override
    public Boolean isAllowAvailable(Integer agentId, Integer activityId, String redisKey) {
        LogAgentTrace lastLogAgentTrace = this.getLatestActivity(agentId, activityId, redisKey);
        if (lastLogAgentTrace != null) {
            return Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.UNAVAILABLE.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.BREAK.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.BUSY.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValue(), "") ||
                    Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.LOGOUT.getValue());
        }
        return false;
    }

    @Override
    public Boolean isAllowUnavailable(Integer agentId, Integer activityId, String redisKey) {
        LogAgentTrace lastLogAgentTrace = this.getLatestActivity(agentId, activityId, redisKey);
        if (lastLogAgentTrace != null) {
            return Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.AVAILABLE.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.BREAK.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.BUSY.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValue(), "") ||
                    Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.LOGOUT.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.LOGIN.getValue()) ||
                    Objects.equals(lastLogAgentTrace.getValue(), EnumType.AGENT_STATE.LOGIN.getName());
        }
        return false;
    }

    @Override
    public Boolean isAllowOnCall(Integer agentId, Integer activityId, String redisKey) {
        LogAgentTrace lastLogAgentTrace = this.getLatestActivity(agentId, activityId, redisKey);
        if (lastLogAgentTrace != null) {
            return Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.ASSIGNED.getValue());
        }
        return false;
    }

    @Override
    public Boolean isAllowWrapUp(Integer agentId, Integer activityId, String redisKey) {
        LogAgentTrace lastLogAgentTrace = this.getLatestActivity(agentId, activityId, redisKey);
        if (lastLogAgentTrace != null) {
            return Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.ON_CALL.getValue());
        }
        return false;
    }

    @Override
    public Boolean isAllowAssigned(Integer agentId, Integer activityId, String redisKey) {
        LogAgentTrace lastLogAgentTrace = this.getLatestActivity(agentId, activityId, redisKey);
        if (lastLogAgentTrace != null) {
            return Objects.equals(lastLogAgentTrace.getValueCode(), EnumType.AGENT_STATE.AVAILABLE.getValue());
        }
        return false;
    }
}
