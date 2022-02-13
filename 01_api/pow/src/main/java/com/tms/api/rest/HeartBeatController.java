package com.tms.api.rest;

import com.tms.api.exception.ErrorMessage;
import com.tms.api.helper.*;
import com.tms.api.repository.OrUserRepository;
import com.tms.api.response.TMSResponse;
import com.tms.ff.utils.AppProperties;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static java.lang.Thread.sleep;

@RestController
@RequestMapping("/heartbeat")
public class HeartBeatController {

    private Logger logger = LoggerFactory.getLogger(HeartBeatController.class);

    private final OrUserRepository orUserRepository;
    private final StringRedisTemplate stringRedisTemplate;



    public String OAUTH_SERVER_GET_TOKEN = AppProperties.getPropertyValue("heartbeat.auth.url");
    public String OAUTH_GRANT_TYPE = AppProperties.getPropertyValue("heartbeat.auth.granttype");
    public String OAUTH_USERNAME = AppProperties.getPropertyValue("heartbeat.auth.username");
    public String OAUTH_PASSWORD = AppProperties.getPropertyValue("heartbeat.auth.password");

    @Value("${config.org-id}")
    private Integer orgId;

    @Autowired
    public HeartBeatController(OrUserRepository orUserRepository,
                               StringRedisTemplate stringRedisTemplate) {
        this.orUserRepository = orUserRepository;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("/{apiKey}")
    public TMSResponse checkConnection(@PathVariable String apiKey){
        if (!apiKey.equals("X7ubImiHnWRp3YewXuG3yvF3t7HPNdag")) {
            return TMSResponse.buildResponse(ErrorMessage.APIKEY_INVALID);
        }
        // check connect db
        try {
            Integer countUser = orUserRepository.testConnection();
            if (CollectionUtils.isEmpty(Collections.singleton(countUser))) {
                logger.error("Error get data from database: ");
                return TMSResponse.buildResponse(ErrorMessage.CONNECTION_DATABASE_FAIL);
            }
        } catch (Exception e) {
            logger.error("Error connection database: " + e.getMessage());
            return TMSResponse.buildResponse(ErrorMessage.CONNECTION_DATABASE_FAIL);
        }

        // check connect redis
        try {
            String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_TEST, orgId, "connect");
            RedisHelper.saveRedis(stringRedisTemplate, key, "1", "1");

            Map<String, String> testMap = RedisHelper.getRedis(stringRedisTemplate, key);
            if (CollectionUtils.isEmpty(testMap)) {
                logger.error("Error get data from redis: ");
                return TMSResponse.buildResponse(ErrorMessage.CONNECTION_REDIS_FAIL);
            }
        } catch (Exception e) {
            logger.error("Error connection redis: " + e.getMessage());
            return TMSResponse.buildResponse(ErrorMessage.CONNECTION_REDIS_FAIL);
        }

        // check queue
        try {
            String mes = "Test connect queue!";
            QueueHelper.sendMessage(mes, Const.QUEUE_TEST);
            logger.info("Send message: {} to queue: {}", mes, Const.QUEUE_TEST);
        } catch (Exception e) {
            logger.error("Connect queue fail: " + e.getMessage());
            return TMSResponse.buildResponse(ErrorMessage.QUEUE_FAIL);
        }

        //check authen
        try {
            String checkAuthen = getToken();
            JSONObject bodyMap = new JSONObject(checkAuthen);
            String token = bodyMap.optString("access_token");
            if (CollectionUtils.isEmpty(Collections.singleton(token))) {
                logger.error("get token fail!");
                return TMSResponse.buildResponse(ErrorMessage.AUTHEN_FAIL);
            }

        } catch (Exception e) {
            logger.error("authen fail: " + e.getMessage());
            return TMSResponse.buildResponse(ErrorMessage.AUTHEN_FAIL);
        }


        return TMSResponse.buildResponse(ErrorMessage.SUCCESS);
    }

    public String getToken() throws IOException {

        String url = OAUTH_SERVER_GET_TOKEN + "?grant_type=password&username=" + OAUTH_USERNAME + "&password=" + OAUTH_PASSWORD;
        JSONObject json = new JSONObject();
        StringEntity params = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
        String checkAuthen = HttpHelper.postResponse(url,params);

        return checkAuthen;
    }

}
