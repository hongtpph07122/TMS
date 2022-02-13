package com.tms.api.task;

import com.tms.api.entity.ClFreshEntity;
import com.tms.api.entity.OdDONew;
import com.tms.api.helper.*;
import com.tms.api.repository.OdDONewRepository;
import com.tms.api.service.BasketProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class DeliveryJob {
    private final Logger logger = LoggerFactory.getLogger(DeliveryJob.class);

    @Autowired
    BasketProcessorService basketProcessorService;

    @Value("${config.run-delivery-postback-job}")
    public Boolean isDeliveryPostback;
    @Value("${config.agency-id-need-to-delivery-postback}")
    public String agencyIdNeedToDeliveryPostback;
    @Autowired
    private OdDONewRepository odDONewRepository;
    private static final String DELIVERY = "DELIVERY";
    private static final Integer POSTBACK_TYPE_DELIVERED = 4;

    private void sentMessageToQueue(ClFreshEntity clfresh) {
        String message = clfresh.getAgcId() + "|" + clfresh.getClickId();
        String payout = "0", offerId = "0";
        if (clfresh.getPrice() != null)
            payout = clfresh.getPrice();
        if (clfresh.getOfferId() != null)
            offerId = clfresh.getOfferId();
        int terms = 0;
        try {
            if (clfresh.getTerms() != null && !clfresh.getTerms().isEmpty())
                terms = Integer.parseInt(clfresh.getTerms());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            message = QueueHelper.createQueueMessage(clfresh.getOrgId(), clfresh.getAgcId(), clfresh.getClickId(),
                    POSTBACK_TYPE_DELIVERED, offerId, clfresh.getLeadId(), DELIVERY, payout, terms);
            QueueHelper.sendMessage(message, Const.QUEUE_AGENTCY);
        } catch (Exception e) {
            logger.error("[QUEUE] could not sent {}", clfresh.getLeadId(), e);
        }
    }

    @Scheduled(fixedDelayString = "${config.delivery-postback-job-time}")
    public void postbackDeliveried() {
        try {
            logger.info("delivery-postback-job-time {}", new Date());
            if (!isDeliveryPostback || StringHelper.isNullOrEmpty(agencyIdNeedToDeliveryPostback))
                return;
            List<Integer> agcIds = Arrays.stream(agencyIdNeedToDeliveryPostback.split(";")).map(Integer::parseInt)
                    .collect(Collectors.toList());

            List<OdDONew> listOdDONews = odDONewRepository.getAllUnpostbackDeliveriedWithLeadByAgcId(agcIds);
            for (OdDONew odDONew : listOdDONews) {
                ClFreshEntity lead = odDONew.getClFresh();
                if (lead != null) {
                    sentMessageToQueue(lead);
                    odDONew.setIs_postback(1);
                }
            }
            odDONewRepository.saveAll(listOdDONews);
            logger.info("Finished to process delivery-postback-job");
        } catch (Exception exp) {
            logger.error("Error-delivery-postback|{}", exp.getMessage(), exp);
        }
    }
}
