package com.tms.api.task;

import com.tms.api.dto.ResponseRescueFromDO;
import com.tms.api.service.DOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TLGetRescueJob {

    private final Logger logger = LoggerFactory.getLogger(TLGetRescueJob.class);

    @Autowired
    private DOService doService;

    @Value("${config.auto-rescue}")
    public Boolean isOrderJob;

    @Scheduled(cron = "${config.auto-rescue-cron:0 0 8,14 * * ?}")
    public void fromApiToRescueTL() {
        if (!isOrderJob)
            return;

        logger.info("START TLGetRescueJob ******************* START");
        ResponseRescueFromDO result = doService.createAndUpdateRescue();
        logger.info("DONE TLGetRescueJob ************************** DONE");

        String messages = result.getImported() + " records imported | " +
                result.getCreated() + " rescues job created | " +
                result.getUpdated() + " rescues job updated";
        logger.info(messages);
    }
}
