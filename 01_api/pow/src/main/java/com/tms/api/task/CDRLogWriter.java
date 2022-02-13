package com.tms.api.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.helper.Const;
import com.tms.api.helper.RedisHelper;
import com.tms.api.service.CDRsService;
import com.tms.api.service.PhoneService;
import com.tms.entity.log.InsCdrAll;
import com.tms.service.impl.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class CDRLogWriter {
    private static int MAX_TEXT_MESSAGE_BUFFER_SIZE = 20 * 1024 * 1024;
    private final Logger log = LoggerFactory.getLogger(CDRLogWriter.class);
    private static boolean isRunning = false;
    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Value("${config.cdr-listen}")
    public Boolean isListening;
    @Value("${config.cdr-websocket}")
    public String URL;
    @Value("${config.org-id:0}")
    public int orgId;
    @Value("${config.write-log-call-id:false}")
    public boolean writeLogCallId;

    private final LogService logService;

    private final StringRedisTemplate stringRedisTemplate;

    private static final int TIME_OUT_SAVE_REDIS_LOG_LEAD_UNIQUE_ID = 60 * 60;

    @Autowired
    public CDRLogWriter(LogService logService,
                        StringRedisTemplate stringRedisTemplate) {
        this.logService = logService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 5000)
    public void listenCDRLog() {
        if (!isListening) {
            log.info("LogSocket: Config is off");
            return;
        }
        if (isRunning) {
            log.info("LogSocket: Already Starting");
            return;
        }
        log.info("LogSocket: Starting");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxTextMessageBufferSize(MAX_TEXT_MESSAGE_BUFFER_SIZE);
        WebSocketClient client = new StandardWebSocketClient(container);
        client.doHandshake(new AbstractWebSocketHandler() {

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("LogSocket: Connected to ! " + URL);
                isRunning = true;
            }

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                try {
                    String taskSession = UUID.randomUUID().toString();
                    log.info("LogSocket: Received " + message.getPayload());
                    ObjectMapper mapper = new ObjectMapper();
                    InsCdrAll cdrLog = mapper.readValue((String) message.getPayload(), InsCdrAll.class);
                    SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_TIME_FORMAT);

                    if (writeLogCallId)
                        saveUniqueIdInRedis(cdrLog);

                    if (cdrLog.getStartTime() != null && !cdrLog.getStartTime().isEmpty()) {
                        Date startTime = new SimpleDateFormat(LOG_DATE_FORMAT).parse(cdrLog.getStartTime());
                        cdrLog.setStartTime(dateFormat.format(startTime));
                    }

                    if (cdrLog.getEndTime() != null && !cdrLog.getEndTime().isEmpty()) {
                        Date endTime = new SimpleDateFormat(LOG_DATE_FORMAT).parse(cdrLog.getEndTime());
                        cdrLog.setEndTime(dateFormat.format(endTime));
                    }

                    if (cdrLog.getAnswerTime() != null && !cdrLog.getAnswerTime().isEmpty()) {
                        Date answerTime = new SimpleDateFormat(LOG_DATE_FORMAT).parse(cdrLog.getAnswerTime());
                        cdrLog.setAnswerTime(dateFormat.format(answerTime));
                    }

                    cdrLog.setDateReceived(dateFormat.format(new Date()));

                    log.info("LogSocket: Received SRC " + cdrLog.getSrc() + ", DES: " + cdrLog.getDestination());
                    logService.insCdrAll(taskSession, cdrLog);

                } catch (final Exception ex) {
                    isRunning = false;
                    log.error("CDRLogClient Error: ", ex);
                }

            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                isRunning = false;
            }

        }, URL);
    }

    private void saveUniqueIdInRedis(InsCdrAll cdrAll) {
        if (cdrAll.getDestination() == null || cdrAll.getDestination().length() < 2)
            return;

        /* Phone in pbx add '9' in front of customer phone */
        String phone = cdrAll.getDestination().substring(1);
        String leadId = "";
        try {
            String phoneNumberOfAgent = cdrAll.getChannel().substring(4, 8);
            String keyGetLeadId = RedisHelper.createRedisKey(Const.REDIS_PREFIX_MAKE_CALL, orgId, phone, ":" + phoneNumberOfAgent);
            leadId = RedisHelper.getKey(stringRedisTemplate, keyGetLeadId);

            /* Check uniqueId was in redis:
             * yes: add old and new in uniqueId
             * no: add new in uniqueId
             * Then save in redis again */
            String uniqueId = cdrAll.getUniqueId();
            String keyUniqueId = RedisHelper.createRedisKey(Const.REDIS_PREFIX_CALL_ID, orgId, leadId + ":" + phoneNumberOfAgent);
            String uniqueIdInRedis = RedisHelper.getKey(stringRedisTemplate, keyUniqueId);

            String uniqueIds = getUniqueIds(uniqueId, uniqueIdInRedis);

            RedisHelper.saveRedis(stringRedisTemplate, uniqueIds, keyUniqueId, TIME_OUT_SAVE_REDIS_LOG_LEAD_UNIQUE_ID, true);
        } catch (Exception e) {
            log.error("Error save UniqueId. LeadId = {}", leadId, e);
        }
    }

    private String getUniqueIds(String uniqueId, String uniqueIdInRedis) {
        String uniqueIds;
        if (uniqueIdInRedis != null) {
            if (!uniqueIdInRedis.contains(uniqueId))
                uniqueIds = uniqueIdInRedis + "," + uniqueId;
            else
                uniqueIds = uniqueIdInRedis;
        } else
            uniqueIds = uniqueId;

        return uniqueIds;
    }
}
