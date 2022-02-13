package com.tms.api.component;

import com.google.gson.Gson;
import com.tms.api.entity.OrUser;
import com.tms.api.helper.Const;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.RedisHelper;
import com.tms.api.response.PBXResponse;
import com.tms.api.service.LogAgentTraceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    @Autowired
    private LogAgentTraceService logAgentTraceService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${config.kafka.phone-prefix}")
    private String phonePrefix;
    @Value("${config.org-id}")
    private Integer orgId;

    @KafkaListener(topics = "${config.kafka.consumer.topic}", groupId = "${config.kafka.consumer.group-id}",
            autoStartup = "${config.kafka.consumer.auto-startup}")
    public void consume(String message) {
        try {
            Gson gson = new Gson();
            PBXResponse pbxResponse = gson.fromJson(message, PBXResponse.class);
            if (pbxResponse != null) {
                String direction = pbxResponse.getDirection();
                String action = pbxResponse.getAction();
                if (direction != null && action != null && direction.equals(EnumType.AGENT_STATE.OUTBOUND.getName())) {
                    if (action.equals(EnumType.AGENT_STATE.RINGING.getName()) ||
                            action.equals(EnumType.AGENT_STATE.END.getName()) ||
                            action.equals(EnumType.AGENT_STATE.CONNECTED.getName())) {
                        // Get agent info and cache
                        String sip = pbxResponse.getChannelType() + "/" + pbxResponse.getExtension();
                        String phone = this.parsePhoneNumber(pbxResponse.getPhone().trim());
                        String redisKey = RedisHelper.createAgentRedisKey(sip, phone);
                        String agentString = RedisHelper.getKey(stringRedisTemplate, redisKey);
                        OrUser agent;
                        if (agentString != null && !agentString.equals("")) {
                            agent = gson.fromJson(agentString, OrUser.class);
                        } else {
                            agent = this.logAgentTraceService.getAgent(sip, phone);
                            if (agent != null && agent.getUser_id() > 0) {
                                String jsonAgent = gson.toJson(agent);
                                RedisHelper.saveRedis(stringRedisTemplate, jsonAgent, redisKey, Const.LOG_AGENT_STATE_CACHE_LIVE_TIME);
                            }
                        }
                        if (agent != null && agent.getUser_id() > 0) {
                            this.logAgentTraceService.logPBXActivity(pbxResponse, agent);
                        }
                    }
                }
            }
        } catch(Exception ex) {
            logger.error("kafka get exception" + ex.getMessage(), ex);
        }
    }
    private String parsePhoneNumber(String phone) {
        if (orgId == 9 && phone != null && phonePrefix != null && !phonePrefix.equals("")
                && phone.startsWith(phonePrefix) && phone.length() > 10) {
            return phone.replaceFirst(phonePrefix, "");
        }
        return phone;
    }
}
