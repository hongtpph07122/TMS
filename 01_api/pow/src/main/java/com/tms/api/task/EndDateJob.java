package com.tms.api.task;

import com.tms.api.helper.*;
import com.tms.api.service.CallbackService;
import com.tms.dto.*;
import com.tms.entity.CLCallback;
import com.tms.entity.CLFresh;
import com.tms.entity.log.UpdLeadV5;
import com.tms.service.impl.CLCallbackService;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dinhanhthai on 27/09/2019.
 */
@Component
public class EndDateJob {
    /**
     * 1: 5 phut 1 lan tinh toan lai so lead tung agent duoc nhan
     * 2: kiem tra da dat max so lead duoc nhan chua (duoc lay new lead)
     */
    private final Logger logger = LoggerFactory.getLogger(EndDateJob.class);
    private static final int DEFAULT_DAY_AFTER_TO_RUN_CLOSE = 6;

    @Value("${config.run-close-lead}")
    public Boolean isRunClose;
    @Value("${config.orgId-close}")
    public Integer closeOrg;
    @Value("${config.month-start-run-close}")
    public Integer monthStartRunClose;
    @Value("${config.day-after-run-close}")
    public Integer dayAfterRunClose;

    @Value("${config.orgId-close-callback: 9}")
    public Integer closeCallbackOrg;
    @Value("${config.run-close-callback-lead: false}")
    public Boolean isRunCloseCallback;

    @Value("${config.is-reload-global-param: false}")
    public Boolean isReloadGlobalParam;

    private final StringRedisTemplate stringRedisTemplate;
    private final CLFreshService clFreshService;
    private final LogService logService;
    private final CLCallbackService clCallbackService;
    private final CallbackService callbackService;
    private final DBLogWriter logWriterService;

    @Autowired
    public EndDateJob(StringRedisTemplate stringRedisTemplate, CLFreshService clFreshService, LogService logService, CLCallbackService clCallbackService, CallbackService callbackService, DBLogWriter logWriterService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.clFreshService = clFreshService;
        this.logService = logService;
        this.clCallbackService = clCallbackService;
        this.callbackService = callbackService;
        this.logWriterService = logWriterService;
    }
    @Scheduled(cron = "0 0 * * * ?")
    public void loadGlobalParamDaily() {
        logger.info("------------------------- LOAD GLOBAL PARAM DAILY -------------------------");

        if (!isReloadGlobalParam) {
            logger.info("------------------------- LOAD GLOBAL PARAM DAILY OFF -------------------------");
            return;
        }

        String taskSession = UUID.randomUUID().toString();

        GetGlobalParamV2 getGlobalParamV2 = new GetGlobalParamV2();
        DBResponse<List<GetGlobalParamResp>> dbGlobal = clFreshService.getGlobalParamV2(taskSession, getGlobalParamV2);
        if (dbGlobal.getResult().isEmpty()) {
            logger.info("No result LOAD GLOBAL PARAM ");
            return;
        }
        List<GetGlobalParamResp> lstGlobalParams = dbGlobal.getResult();
        for (GetGlobalParamResp globalParamResp : lstGlobalParams) {
            if (globalParamResp.getValue() == null || globalParamResp.getType() == null || globalParamResp.getParamId() == null)
                continue;
            String key = Helper.createRedisKey(Const.REDIS_PREFIX_GLOBAL, globalParamResp.getOrgId().toString());
            RedisHelper.saveRedis(stringRedisTemplate, key, globalParamResp.getType() + ":" + (globalParamResp.getParamId()), String.valueOf(globalParamResp.getValue()));
            logger.info("********* {}:{} {}", globalParamResp.getType(), globalParamResp.getParamId(), globalParamResp.getValue());
        }
    }


    //    @Scheduled(cron = "0 21 9 * * ?")
//    @Scheduled(fixedRate = 5 * 1 * 1000)
//    @Async
    public void exportReport() {
        boolean isMaxLeadByUser = RedisHelper.isMaxLeadByUser(stringRedisTemplate, 6, 605);
        logger.info("Running export data {}\r\nisMaxLeadByUser: {}", new Date(), isMaxLeadByUser);
    }

//    @Scheduled(fixedDelay = 1 * 60 * 1000, initialDelay = 5000)
//    @Scheduled(fixedDelayString = "${config.basket-time}
    @Scheduled(cron = "0 10 1 * * ?")
    public void autoCloseUncallLead() {
        logger.info("------------------------- LOAD AUTO CLOSE UNCALL LEAD DAILY -------------------------");
        if (!isRunClose)
            return;

        String taskSession = UUID.randomUUID().toString();

        List<Integer> freshCpIds = getCpIdsByRule(taskSession, closeOrg);
        if (freshCpIds == null || freshCpIds.isEmpty()) {
            logger.info("---------- Don't have any campaign apply rule ----------");
            return;
        }

        List<Integer> listUncallStatus = Arrays.asList(EnumType.LEAD_STATUS.BUSY.getValue(), EnumType.LEAD_STATUS.UNREACHABLE.getValue(), EnumType.LEAD_STATUS.NOANSWER.getValue());
        if (dayAfterRunClose == null) {
        	dayAfterRunClose = DEFAULT_DAY_AFTER_TO_RUN_CLOSE;
        }
        LocalDateTime dayBefore = LocalDateTime.now().minusDays(dayAfterRunClose).withHour(0).withMinute(0).withSecond(0);
        logger.info(dayBefore.toString());

        List<CLFresh> listUncall = new ArrayList<>();
        List<Integer> leadCallbackIds = new ArrayList<>();
        List<Integer> leadIdCallbackCloses = new ArrayList<>();

        for (int status : listUncallStatus) {
            GetLeadParamsV8 getFreshLeadParams = new GetLeadParamsV8();
            getFreshLeadParams.setOrgId(closeOrg);
            getFreshLeadParams.setLeadStatus(status);
            getFreshLeadParams.setCpId(StringUtils.join(freshCpIds, ","));
            DBResponse<List<CLFresh>> dbResponseCLFresh = clFreshService.getLeadV8(taskSession, getFreshLeadParams);
            if (dbResponseCLFresh.getResult().isEmpty())
                continue;

            listUncall.addAll(dbResponseCLFresh.getResult());
        }

        if (listUncall.isEmpty()) {
            logger.info("---------- NO LEAD TO RUN ----------");
            return;
        }

        List<Integer> leadUncallIds = listUncall.stream()
                .map(CLFresh::getLeadId)
                .collect(Collectors.toList());

        GetCallbackByLeadIds getCallbackParams = new GetCallbackByLeadIds();
        getCallbackParams.setOrgId(9);
        getCallbackParams.setLeadId(StringUtils.join(leadUncallIds, ","));
        DBResponse<List<CLCallback>> dbResponseCallback = clCallbackService.getCallbackByLeadIds(taskSession, getCallbackParams);
        if (!dbResponseCallback.getResult().isEmpty()) {
            leadCallbackIds = dbResponseCallback.getResult().stream()
                    .map(CLCallback::getLeadId)
                    .collect(Collectors.toList());
            leadIdCallbackCloses = getLeadIdCallbackByRule(dbResponseCallback.getResult(), dayBefore);
        }

        int countLeadUncallToClose = 0;
        for (CLFresh fresh : listUncall) {
            String message = "attempt more than " + dayAfterRunClose + " days";
            String rejectReason = "Reject: Callback reject";
            UpdLeadV5 updLead = new UpdLeadV5();
            if (leadCallbackIds.contains(fresh.getLeadId())) {
                if (leadIdCallbackCloses.contains(fresh.getLeadId())) {
                    postBackReject(fresh, message);
                    updLead.setLeadId(fresh.getLeadId());
                    updLead.setLeadStatus(EnumType.LEAD_STATUS.REJECTED.getValue());
                    updLead.setComment(fresh.getComment() + "|" + message);
                    updLead.setUserDefin05(rejectReason);
                    logService.updLeadV5(taskSession, updLead);
                    logger.info("Change lead to reject and send Reject to partner: {}", fresh.getLeadId());
                }
            } else {
                if (DateHelper.convertToLocalDateTimeViaMilisecond(fresh.getCreatedate()).isBefore(dayBefore)) {
//                   sendTrashToQueue(fresh, message);
                    postBackReject(fresh, message);
                    updLead.setLeadId(fresh.getLeadId());
                    updLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
                    updLead.setComment(fresh.getComment() + "|" + message);
                    logService.updLeadV5(taskSession, updLead);
                    logger.info("Change lead to close and send Trash to agency: {}", fresh.getLeadId());
                    countLeadUncallToClose++;
                }
            }
        }
        logger.info("CHANGE {} LEAD UNCALL TO CLOSE\r\nCHANGE {} LEAD UNCALL TO REJECTED",
                countLeadUncallToClose, leadIdCallbackCloses.size());
    }

    private List<Integer> getLeadIdCallbackByRule(List<CLCallback> clCallbackList, LocalDateTime dayBefore) {
        List<Integer> result = new ArrayList<>();
        List<CLCallback> finalList = new ArrayList<>();
        List<CLCallback> firstlyList = new ArrayList<>();

        CLCallback finalCLCallback = clCallbackList.get(0);
        finalList.add(finalCLCallback);
        for (int i = 1; i < clCallbackList.size(); i++) {
            if (clCallbackList.get(i).getLeadId().intValue() == finalCLCallback.getLeadId().intValue())
                continue;
            finalCLCallback = clCallbackList.get(i);

            finalList.add(finalCLCallback);
            firstlyList.add(clCallbackList.get(i - 1));
        }
        firstlyList.add(clCallbackList.get(clCallbackList.size() - 1));

        for (int i = 0; i < finalList.size(); i++) {
            if (finalList.get(i).getLeadStatus() == EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue()) {
                LocalDateTime requestTime = DateHelper.convertToLocalDateTimeViaMilisecond(finalList.get(i).getRequestTime());
                if (requestTime.isBefore(dayBefore))
                    result.add(finalList.get(i).getLeadId());
            } else {
                LocalDateTime requestTime = DateHelper.convertToLocalDateTimeViaMilisecond(firstlyList.get(i).getRequestTime());
                if (requestTime.isBefore(dayBefore))
                    result.add(firstlyList.get(i).getLeadId());
            }
        }

        return result;
    }

    @Scheduled(cron = "0 2 0 * * ?")
    public void autoCloseCallbackLead() {
        logger.info("------------------------- LOAD autoCloseCallbackLead DAILY -------------------------");
        if(!isRunCloseCallback)
            return;

        String taskSession = UUID.randomUUID().toString();

        List<Integer> freshCpIds = getCpIdsByRule(taskSession, closeCallbackOrg);
        if (freshCpIds == null) {
            logger.info("---------- Don't have any campaign apply rule ----------");
            return;
        }

        LocalDate dayEnd = LocalDate.now().plusDays(1);

        GetCallbackAllV3 getCallbackParams = new GetCallbackAllV3();
        getCallbackParams.setOrgId(closeCallbackOrg);
        getCallbackParams.setCloseTime("|"+DateHelper.toTMSDateFormat(dayEnd));
        if (!freshCpIds.isEmpty())
            getCallbackParams.setCpId(StringUtils.join(freshCpIds, ","));
        DBResponse<List<CLCallback>> dbRespCallback = clCallbackService.getCallbackAllV3(taskSession, getCallbackParams);
        if (dbRespCallback == null || dbRespCallback.getResult().isEmpty()) {
            logger.info("------------------ NO LEAD TO RUN CLOSE CALLBACK------------------");
            return;
        }

        List<Integer> leadIds = dbRespCallback.getResult().stream()
                .map(CLCallback::getLeadId)
                .collect(Collectors.toList());

        GetLeadParamsV12 getLeadParams = new GetLeadParamsV12();
        getLeadParams.setOrgId(closeCallbackOrg);
        getLeadParams.setLeadId(StringUtils.join(leadIds, ","));
        DBResponse<List<CLFresh>> dbResponseLead = clFreshService.getLeadV12(taskSession, getLeadParams);

        for (CLFresh fresh : dbResponseLead.getResult()) {
            String message = "auto close callback";
            UpdLeadV5 updLead = new UpdLeadV5();
            postBackReject(fresh, message);
            updLead.setLeadId(fresh.getLeadId());
            updLead.setLeadStatus(EnumType.LEAD_STATUS.REJECTED.getValue());
            updLead.setComment(fresh.getComment() + "|" + message);
            updLead.setUserDefin05(message);
            logService.updLeadV5(taskSession, updLead);
            logger.info("Change lead to reject - {}", updLead.getLeadId());

            int systemId = 0;
            logWriterService.writeLeadStatusLogV3(systemId, fresh,
                    EnumType.LEAD_STATUS.REJECTED.getValue(), message, taskSession);
        }

        int numberCallbackDeleted = callbackService.delete(leadIds, closeCallbackOrg);
        logger.info("Delete callback result: {}", numberCallbackDeleted);
    }

    private List<Integer> getCpIdsByRule(String ssId, int _ORG) {
        String key = Helper.createRedisKey(Const.REDIS_PREFIX_GLOBAL, String.valueOf(_ORG));
        String hasResell = RedisHelper.getKey(stringRedisTemplate, key, "11:1");
        logger.info("key: {}, value: {}", key, hasResell);
        String cpCat = "" + EnumType.CAMPAIGN_TYPE.FRESH.getCpType();
        if (hasResell == null || hasResell.equals(String.valueOf(EnumType.BOOLEAN_INTEGER.FALSE.getValue())))
            logger.info("----- ONLY FRESH -----");
        else
            cpCat += "," + EnumType.CAMPAIGN_TYPE.RESELL.getCpType();

        GetCampaignParamsV2 getCampaignFreshParams = new GetCampaignParamsV2();
        getCampaignFreshParams.setCpCat(cpCat);
        DBResponse<List<GetCampaignRespV2>> dbRespCampaignFresh = clFreshService.getCampaignV2(ssId, getCampaignFreshParams);
        if (dbRespCampaignFresh == null || dbRespCampaignFresh.getResult() == null || dbRespCampaignFresh.getResult().isEmpty())
            return null;

        return dbRespCampaignFresh.getResult().stream()
                .map(GetCampaignRespV2::getCpId)
                .collect(Collectors.toList());

    }

    private void sendTrashToQueue(CLFresh basket, String note) {
        try {

            if ((basket.getAgcId() != null && basket.getClickId() != null)) {
                String message = basket.getAgcId() + "|" + basket.getClickId();

                String payout = "0", offerId = "0";

                if (basket.getPrice() != null)
                    payout = basket.getPrice();
                if (basket.getOfferId() != null)
                    offerId = basket.getOfferId();

                int terms = 0;
                try {
                    if (basket.getTerms() != null && !basket.getTerms().isEmpty())
                        terms = Integer.parseInt(basket.getTerms());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                if (null != basket.getTrackerId() && 0 != basket.getTrackerId()) {
                    message = QueueHelper.createQueueMessage(basket.getOrgId(), basket.getAgcId(), basket.getClickId(),
                            2, offerId, basket.getLeadId(), note, payout, terms, basket.getTrackerId(), basket.getSubid4());
                } else {
                    message = QueueHelper.createQueueMessage(basket.getOrgId(), basket.getAgcId(), basket.getClickId(),
                            2, offerId, basket.getLeadId(), note, payout, terms);
                }
                QueueHelper.sendMessage(message, Const.QUEUE_AGENTCY);
                logger.info("[QUEUE] {} sent message to close {}", Const.QUEUE_AGENTCY, message);
            }
        } catch (Exception e) {
            logger.error("[QUEUE] could not send to close {}", basket.getLeadId(), e);
        }
    }

    private void postBackReject(CLFresh basket, String note) {
        try {

            if ((basket.getAgcId() != null && basket.getClickId() != null)) {
                String message;

                String payout = "0", offerId = "0";

                if (basket.getPrice() != null)
                    payout = basket.getPrice();
                if (basket.getOfferId() != null)
                    offerId = basket.getOfferId();

                int terms = 0;
                try {
                    if (basket.getTerms() != null && !basket.getTerms().isEmpty())
                        terms = Integer.parseInt(basket.getTerms());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                if (null != basket.getTrackerId() && 0 != basket.getTrackerId()) {
                    message = QueueHelper.createQueueMessage(basket.getOrgId(), basket.getAgcId(), basket.getClickId(),
                            -1, offerId, basket.getLeadId(), note, payout, terms, basket.getTrackerId(), basket.getSubid4());
                } else {
                    message = QueueHelper.createQueueMessage(basket.getOrgId(), basket.getAgcId(), basket.getClickId(),
                            -1, offerId, basket.getLeadId(), note, payout, terms);
                }
                QueueHelper.sendMessage(message, Const.QUEUE_AGENTCY);
                logger.info("[QUEUE] {} sent message to reject {}", Const.QUEUE_AGENTCY, message);
            }
        } catch (Exception e) {
            logger.error("[QUEUE] could not send to reject {}", basket.getLeadId(), e);
        }
    }
}
