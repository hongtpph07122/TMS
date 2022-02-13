package com.tms.api.task;

import com.tms.api.helper.Const;
import com.tms.api.helper.QueueHelper;
import com.tms.api.rest.AgentGroupController;
import com.tms.service.impl.LCProvinceService;
import com.tms.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
/**
 * Created by dinhanhthai on 27/09/2019.
 */
@Component
public class CachingJob {
    /**
     * 1: 5 phut 1 lan tinh toan lai so lead tung agent duoc nhan
     * 2: kiem tra da dat max so lead duoc nhan chua (duoc lay new lead)
     */

    private final Logger logger = LoggerFactory.getLogger(CachingJob.class);

    @Autowired
    LCProvinceService lcProvinceService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserService userService;


    @Value("${config.caching-group-agent}")
    public Boolean cachingGroupAgent;


    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void StartingCachingGroupAgent() {
        logger.info("------------------------------- Start calculate StartingCachingGroupAgent -------------------------------");
        if(!cachingGroupAgent)
            return;
        if (!AgentGroupController.QUEUE_UPDATE.isEmpty()) {
            String taskSession = UUID.randomUUID().toString();
            for (int i = 0; i < AgentGroupController.QUEUE_UPDATE.size(); i++) {
                Integer orgId = AgentGroupController.QUEUE_UPDATE.poll();
                if (orgId != null) {
                    try {
                        AgentGroupController.CachingGroup(orgId, lcProvinceService, userService, stringRedisTemplate, taskSession);
                    } catch (Exception e) {
                        logger.error("------------------------------- Start calculate StartingCachingGroupAgent ------------------------------- {}", e.getMessage(), e);
                    }

                }
            }

        }
    }

    @Autowired
    JavaMailSender javaMailSender;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void StartingMonitorQueue() {
        logger.info("------------------------------- StartingMonitorQueue -------------------------------");
        if(!cachingGroupAgent)
            return;

        logger.info("------------------------------- StartingMonitorQueue JOB -------------------------------");
        QueueHelper.countMessage(Const.QUEUE_AGENTCY, javaMailSender);
    }
    @Scheduled(cron = "0 1 * * * ?")
    public void scheduleTaskUsingCronExpression() {
        logger.info("------------------------------- Start scheduleTaskUsingCronExpression -------------------------------");
        if(!cachingGroupAgent)
            return;
        AgentGroupController.QUEUE_UPDATE.add(4);
        AgentGroupController.QUEUE_UPDATE.add(6);
        AgentGroupController.QUEUE_UPDATE.add(9);
        AgentGroupController.QUEUE_UPDATE.add(10);

        if (!AgentGroupController.QUEUE_UPDATE.isEmpty()) {
            String taskSession = UUID.randomUUID().toString();

            for (int i = 0; i < AgentGroupController.QUEUE_UPDATE.size(); i++) {
                Integer orgId = AgentGroupController.QUEUE_UPDATE.poll();
                if (orgId != null) {
                    try {
                        AgentGroupController.CachingGroup(orgId, lcProvinceService, userService, stringRedisTemplate, taskSession);
                    } catch (Exception e) {
                        logger.error("------------------------------- Exception scheduleTaskUsingCronExpression ------------------------------- {}", e.getMessage(), e);
                    }
                }
            }

        }
    }
}
