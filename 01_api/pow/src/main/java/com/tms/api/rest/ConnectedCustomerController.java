package com.tms.api.rest;

import com.tms.api.helper.Const;
import com.tms.api.helper.RedisHelper;
import com.tms.api.response.TMSResponse;
import com.tms.entity.log.InsLogConnectedCustomer;
import com.tms.service.impl.LogService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@Scope("prototype")
@RequestMapping("/public/pbx/callback")
public class ConnectedCustomerController {

    private static final Logger logger = LoggerFactory.getLogger(ConnectedCustomerController.class);
    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Value("${config.orgId-close}")
    private int _curOrgId;

    @Value("${config.check-connection:false}")
    private Boolean checkConnection;


    final LogService logService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    public ConnectedCustomerController(LogService logService) {
        this.logService = logService;
    }


    @PostMapping
    public TMSResponse makeCall(@RequestBody String jsonRequest) {
        if (!checkConnection){
            return TMSResponse.buildResponse(false, 0, "Success", 200);
        }
        String isMakeCallKey = "";
        String taskSession = UUID.randomUUID().toString();
        try {
            JSONObject json = new JSONObject(jsonRequest);

            // make key to get lead id
            String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_MAKE_CALL, _curOrgId, String.valueOf(json.get("phone").toString()), ":" + String.valueOf(json.get("extension").toString()));
            String leadId = RedisHelper.getKey(stringRedisTemplate, key);
            if (leadId != null) {
                // create log entity
                InsLogConnectedCustomer ccLog = new InsLogConnectedCustomer();
                ccLog.setPhone(json.get("phone").toString());
                ccLog.setStatus(json.get("action").toString());
                ccLog.setExtension(json.get("extension").toString());
                ccLog.setUniqueId(json.get("uniqueId").toString());
                ccLog.setJsonLog(jsonRequest);
                ccLog.setLeadId(leadId);

                SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_TIME_FORMAT);
                if (json.get("eventTime").toString() != null && !json.get("eventTime").toString().isEmpty()) {
                    Date eventTime = new SimpleDateFormat(LOG_DATE_FORMAT).parse(json.get("eventTime").toString());
                    ccLog.setEventTime(dateFormat.format(eventTime));
                }

                // create make call key to check in /saveleadinfo
                isMakeCallKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_MAKE_CALL, _curOrgId, leadId);
                String isNullValue = RedisHelper.getKey(stringRedisTemplate, isMakeCallKey);
                if (!StringUtils.hasText(isNullValue))
                    RedisHelper.saveRedis(stringRedisTemplate, String.valueOf(ccLog.getUniqueId()), isMakeCallKey, 900, true);
                else {
                    if (ccLog.getStatus().toLowerCase().equals("connected")){
                        RedisHelper.deleteKey(stringRedisTemplate, isMakeCallKey);
                        RedisHelper.saveRedis(stringRedisTemplate, String.valueOf(ccLog.getUniqueId()), isMakeCallKey, 900, true);
                    } else {
                        RedisHelper.deleteKey(stringRedisTemplate, isMakeCallKey);
                    }
                }
                // insert log entity
                logService.insLogConnectedCustomer(taskSession, ccLog);
            }
        } catch (Exception e) {
            logger.error("[MAKE CALL KEY CHECK ERROR]: {}\r\n[CONNECTED CUSTOMER REQUEST ERR]: {}", e.getMessage(), jsonRequest);
            return TMSResponse.buildResponse(isMakeCallKey, 0, "False", 400);
        }
        return TMSResponse.buildResponse(isMakeCallKey, 0, "Success", 200);
    }

    @GetMapping("/check-connection")
    public TMSResponse checkConnection(@RequestParam("leadId") String leadId) {
        if (!checkConnection){
            return TMSResponse.buildResponse(false, 0, "Success", 200);
        }
        Boolean isUnCall = false;
        try {
            String isConnectionKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_MAKE_CALL, _curOrgId, leadId);
            String isConnectionValue = RedisHelper.getKey(stringRedisTemplate, isConnectionKey);
            if (StringUtils.hasText(isConnectionValue)){
                isUnCall = true;
            }
        } catch (Exception e) {
            logger.error("[UN CALL ERROR]: {}\r\n[Lead is un call err]: {}|{}", e.getMessage(), leadId, isUnCall);
            return TMSResponse.buildResponse(isUnCall, 1, "False", 400);
        }
        return TMSResponse.buildResponse(isUnCall, 0, "Success", 200);
    }
}
