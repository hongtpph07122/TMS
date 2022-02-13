package com.tms.api.task;

import com.tms.api.helper.Const;
import com.tms.api.helper.DateHelper;
import com.tms.api.helper.RedisHelper;
import com.tms.dto.DBResponse;
import com.tms.dto.GetAgentRate;
import com.tms.dto.GetAgentRateResp;
import com.tms.service.impl.CLProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by dinhanhthai on 27/09/2019.
 */
@Component
public class AgentRateJob {
    /**
     * 1: 5 phut 1 lan tinh toan lai so lead tung agent duoc nhan
     * 2: kiem tra da dat max so lead duoc nhan chua (duoc lay new lead)
     */

    private static final Logger logger = LoggerFactory.getLogger(AgentRateJob.class);
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    CLProductService clProductService;

    @Value("${config.run-rate}")
    public Boolean isRunJob;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void processAgentRate() {
        if (!isRunJob)
            return;
        logger.info("------------------------------- Start calculate RATE -------------------------------");
        RedisHelper.caculateRateByTotalLead(stringRedisTemplate);
    }

    @Scheduled(fixedRate = 5 * 60 * 1000, initialDelay = 5000)
    public void loadAgentRateDaily() {
        if (!isRunJob)
            return;
        GetAgentRate getAgentRate = new GetAgentRate();
        getAgentRate.setEvaluateDate(DateHelper.toTMSDateFormat());
        String taskSession = UUID.randomUUID().toString();
        DBResponse<List<GetAgentRateResp>> dbResponse = clProductService.getAgentRate(taskSession, getAgentRate);
        if (dbResponse.getResult().isEmpty()) {
            logger.info("No result RATE");
            return;
        }

        List<GetAgentRateResp> lstRates = dbResponse.getResult();
        for (GetAgentRateResp rate : lstRates) {
            String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_RATE, rate.getOrgId(), rate.getCpId());
            RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(rate.getAgentId()), String.valueOf(rate.getRate()));
            logger.info("********* {} {}", rate.getAgentId(), rate.getRate());
        }
    }
}
