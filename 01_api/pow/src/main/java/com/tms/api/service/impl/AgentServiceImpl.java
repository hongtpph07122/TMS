package com.tms.api.service.impl;

import com.tms.api.helper.Const;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.RedisHelper;
import com.tms.api.request.AgentCallingRequestDTO;
import com.tms.api.response.ConfigLogDevResponseDTO;
import com.tms.api.service.AgentService;
import com.tms.api.service.ConfigLogDevService;
import com.tms.api.service.LeadService;
import com.tms.api.task.AutoReloadConfigTeam;
import com.tms.api.utils.DateUtils;
import com.tms.api.utils.ExchangeUtils;
import com.tms.api.utils.LoggerUtils;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.*;
import com.tms.entity.CLCallback;
import com.tms.entity.CLFresh;
import com.tms.entity.log.*;
import com.tms.model.Request.LeadParamsRequestDTO;
import com.tms.model.Request.LeadStatusCallbackWithTimeRequestDTO;
import com.tms.service.impl.CLCallbackService;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.CLProductService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service(value = "agentService")
@Transactional
public class AgentServiceImpl implements AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);
    private static final DateTimeFormatter FORM_FORMAT_TIME = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final StringRedisTemplate stringRedisTemplate;
    private final CLCallbackService callbackService;
    private final CLProductService productService;
    private final CLFreshService freshService;
    private final LogService logService;
    private final ModelMapper modelMapper;
    private final ConfigLogDevService configDevService;
    private final LeadService leadService;

    private ConfigLogDevResponseDTO configLogDevResponse;
    private final AutoReloadConfigTeam autoReloadConfigTeam;

    @PersistenceContext
    private final EntityManager entityManager;

    @Value("${config.user-expiretime}")
    private Integer USER_EXPIRE_TIME;

    @Value("${config.buffer-lead-limit}")
    private Integer BUFFER_LEAD_LIMIT;

    @Autowired
    public AgentServiceImpl(
            StringRedisTemplate stringRedisTemplate,
            CLCallbackService callbackService,
            CLProductService productService,
            CLFreshService freshService,
            LogService logService,
            ModelMapper modelMapper, EntityManager entityManager,
            ConfigLogDevService configDevService,
            LeadService leadService,
            AutoReloadConfigTeam autoReloadConfigTeam) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.callbackService = callbackService;
        this.productService = productService;
        this.freshService = freshService;
        this.logService = logService;
        this.modelMapper = modelMapper;
        this.configDevService = configDevService;
        this.entityManager = entityManager;
        this.leadService = leadService;
        this.autoReloadConfigTeam = autoReloadConfigTeam;
        if (!ObjectUtils.allNotNull(BUFFER_LEAD_LIMIT)) {
            this.BUFFER_LEAD_LIMIT = 50;
        }
    }

    @PostConstruct
    private void configDev() {
        this.configLogDevResponse = configDevService.findOneByValue(1);
    }


    @Override
    public List<CLFresh> snapDistributionRuleCITUnCall(AgentCallingRequestDTO agentCallingRequest) {

        if (ObjectUtils.allNotNull(this.configLogDevResponse)) {
            if (this.configLogDevResponse.getActive()) {
                logger.info("Agent Calling Request To Get list calling CIT_UnCall: {}", LoggerUtils.snagAsLogJson(agentCallingRequest));
            }
        }

        List<CLFresh> listCallings;
        int limitRemains = 1;
        GetLeadParamsV4 leadStatusNew = new GetLeadParamsV4();
        GetNewestLeadV5 leadStatusFreedom = new GetNewestLeadV5();

        /* 1. FIND OUT LEAD STATUS: URGENT AND CALLBACK */
        listCallings = snapUrgentWithCallback(agentCallingRequest, "");

        if (ObjectUtils.allNotNull(this.configLogDevResponse)) {
            if (this.configLogDevResponse.getActive()) {
                logger.info("1. LIST 'LEAD' URGENT , CALLBACK: {}", LoggerUtils.snagAsLogJson(listCallings));
            }
        }

        if (!CollectionUtils.isEmpty(listCallings)) {
            limitRemains = Const.AGENT_CALLING_NUM - listCallings.size();
            if (limitRemains <= 0) {
                return listCallings;
            }
        }

        /* 2. FIND OUT LEAD STATUS: NEW ASSIGNED */
        leadStatusNew.setAssigned(agentCallingRequest.getUserId());
        leadStatusNew.setOrgId(agentCallingRequest.getOrgId());
        leadStatusNew.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
        leadStatusNew.setLimit(limitRemains);
        listCallings = freshService.getLeadV4(agentCallingRequest.getSessionId(), leadStatusNew).getResult();

        if (ObjectUtils.allNotNull(this.configLogDevResponse)) {
            if (this.configLogDevResponse.getActive()) {
                logger.info("2. LIST 'LEAD' NEW ASSIGNED: {}", LoggerUtils.snagAsLogJson(listCallings));
            }
        }

        if (!CollectionUtils.isEmpty(listCallings)) {
            limitRemains = Const.AGENT_CALLING_NUM - listCallings.size();
            if (limitRemains <= 0) {
                leadService.updLeadsCrmActionType(agentCallingRequest.getSessionId(), listCallings,
                        EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentCallingRequest.getUserId());

                return listCallings;
            }
        }

        /* 3. FIND OUT LEAD STATUS: NEW NOT ASSIGNED */
        leadStatusFreedom.setLimit(limitRemains + BUFFER_LEAD_LIMIT);
        leadStatusFreedom.setOrgId(agentCallingRequest.getOrgId());
        leadStatusFreedom.setAssigned(0);
        leadStatusFreedom.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
        leadStatusFreedom.setCallinglistId(agentCallingRequest.getCallingListStr());
        leadStatusFreedom.setSkillLevel(agentCallingRequest.getAgentSkillLevel());
        List<CLFresh> leadsStatusFreedom = freshService.getNewestLeadV5(agentCallingRequest.getSessionId(), leadStatusFreedom).getResult();
        agentCallingRequest.setLeadRemain(limitRemains);
        listCallings = setClueToAgent(agentCallingRequest, leadsStatusFreedom);

        if (ObjectUtils.allNotNull(this.configLogDevResponse)) {
            if (this.configLogDevResponse.getActive()) {
                logger.info("3. LIST 'LEAD' NEW NOT ASSIGNED: {}", LoggerUtils.snagAsLogJson(listCallings));
            }
        }

        if (!CollectionUtils.isEmpty(listCallings)) {
            limitRemains = Const.AGENT_CALLING_NUM - listCallings.size();
            if (limitRemains <= 0) {
                return listCallings;
            }
        }

        /* 4. FIND OUT LEAD STATUS: Un-call */
        listCallings = snapUnCall(agentCallingRequest, "");

        if (ObjectUtils.allNotNull(this.configLogDevResponse)) {
            if (this.configLogDevResponse.getActive()) {
                logger.info("4. LIST 'LEAD' UN-CALL: {}", LoggerUtils.snagAsLogJson(listCallings));
            }
        }

        if (!CollectionUtils.isEmpty(listCallings)) {
            listCallings.sort(CLFresh::compareWithTotalCall);

            if (ObjectUtils.allNotNull(this.configLogDevResponse)) {
                if (this.configLogDevResponse.getActive()) {
                    logger.info("4.1 LIST 'LEAD' UN-CALL AFTER SORT: {}", LoggerUtils.snagAsLogJson(listCallings));
                }
            }

            limitRemains = Const.AGENT_CALLING_NUM - listCallings.size();
            if (limitRemains <= 0) {
                return Collections.singletonList(listCallings.get(0));
            }
        }
        return listCallings;
    }

    /**
     * @param agentCallingRequest - body request - to get urgent
     */
    private List<CLFresh> snapUrgent(AgentCallingRequestDTO agentCallingRequest) {
        GetLeadParamsV4 leadStatusUrgent = new GetLeadParamsV4();
        List<CLFresh> leadsStatusUrgent;
        int leadRemain = Const.AGENT_CALLING_NUM;
        int count = 0;
        /* 1. GET URGENT EXISTED */
        leadStatusUrgent.setAssigned(agentCallingRequest.getUserId());
        leadStatusUrgent.setOrgId(agentCallingRequest.getOrgId());
        leadStatusUrgent.setLeadStatus(EnumType.LEAD_STATUS.URGENT.getValue());
        leadStatusUrgent.setLimit(leadRemain);
        leadStatusUrgent.setCpId(agentCallingRequest.getCampaignId());
        leadsStatusUrgent = freshService.getLeadV4(agentCallingRequest.getSessionId(), leadStatusUrgent).getResult();

        if (!CollectionUtils.isEmpty(leadsStatusUrgent)) {
            leadService.updLeadsCrmActionType(agentCallingRequest.getSessionId(), leadsStatusUrgent,
                    EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentCallingRequest.getUserId());

            return leadsStatusUrgent;
        }

        /* 2. GET URGENT FREEDOM */
        leadStatusUrgent.setAssigned(0);
        leadStatusUrgent.setLimit(leadRemain + BUFFER_LEAD_LIMIT);
        leadStatusUrgent.setCpId(agentCallingRequest.getCampaignId());
        leadStatusUrgent.setOrgId(agentCallingRequest.getOrgId());
        leadStatusUrgent.setSkillLevel(agentCallingRequest.getAgentSkillLevel());
        List<CLFresh> leadsPreStatusUrgent = freshService.getLeadV4(agentCallingRequest.getSessionId(), leadStatusUrgent).getResult();

        for (CLFresh fresh : leadsPreStatusUrgent) {
            if (count == leadRemain) {
                break;
            }
            UpdLeadByAssignedV3 leadPayload = new UpdLeadByAssignedV3();
            leadPayload.setLeadId(fresh.getLeadId());
            leadPayload.setAssigned(agentCallingRequest.getUserId());
            leadPayload.setCpId(agentCallingRequest.getCampaignId());
            leadPayload.setConditionAssigned(0);
            leadPayload.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
            leadPayload.setTeam(autoReloadConfigTeam.getConfigTeam(agentCallingRequest.getUserId()));
            leadPayload.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentCallingRequest.getUserId()));

            DBResponse<?> result = logService.updLeadByAssignedV3(agentCallingRequest.getSessionId(), leadPayload);
            int conditionalAffected = Integer.parseInt(result.getErrorMsg());
            if (conditionalAffected > 0) {
                count++;
                leadsStatusUrgent.add(fresh);
                /*
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, agentCallingRequest.getOrgId(), String.valueOf(agentCallingRequest.getUserId()));
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                RedisHelper.saveRedisRecent(agentCallingRequest.getOrgId(), agentCallingRequest.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
                */
            }
        }

        if (!CollectionUtils.isEmpty(leadsStatusUrgent)) {
            return leadsStatusUrgent;
        }
        return Collections.emptyList();

    }

    /**
     * @param agentCallingRequest - body request - to get urgent & callback
     * @param leadType            - set lead type to get lead auto or manual
     */
    private List<CLFresh> snapUrgentWithCallback(AgentCallingRequestDTO agentCallingRequest, String leadType) {
        List<CLFresh> list;
        List<CLFresh> listCallbacks = new ArrayList<>();
        list = snapUrgent(agentCallingRequest);

        if (ObjectUtils.allNotNull(this.configLogDevResponse)) {
            if (this.configLogDevResponse.getActive()) {
                logger.info("LIST 'LEAD' URGENT: {}", LoggerUtils.snagAsLogJson(list));
            }
        }

        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }

        List<CLCallback> listOfCallbacks = snapCallback(agentCallingRequest, leadType);

        if (ObjectUtils.allNotNull(this.configLogDevResponse)) {
            if (this.configLogDevResponse.getActive()) {
                logger.info("LIST 'LEAD' CALLBACK: {}", LoggerUtils.snagAsLogJson(listOfCallbacks));
            }
        }

        if (!CollectionUtils.isEmpty(listOfCallbacks)) {
            /*
             return CLFresh.mapCallbackToFresh(listOfCallbacks);
            */
            list = CLFresh.mapCallbackToFresh(listOfCallbacks);
            list.sort(CLFresh::compareWithStatus);

            listCallbacks = (list.stream().filter(callback -> callback.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue())).collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(listCallbacks)) {
                leadService.updLeadsCrmActionType(agentCallingRequest.getSessionId(), Collections.singletonList(listCallbacks.get(0)),
                        EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentCallingRequest.getUserId());

                return Collections.singletonList(listCallbacks.get(0));
            }

            listCallbacks = (list.stream().filter(callback -> callback.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue())).collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(listCallbacks)) {
                leadService.updLeadsCrmActionType(agentCallingRequest.getSessionId(), Collections.singletonList(listCallbacks.get(0)),
                        EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentCallingRequest.getUserId());

                return Collections.singletonList(listCallbacks.get(0));
            }

            listCallbacks = (list.stream().filter(callback -> callback.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue())).collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(listCallbacks)) {
                leadService.updLeadsCrmActionType(agentCallingRequest.getSessionId(), Collections.singletonList(listCallbacks.get(0)),
                        EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentCallingRequest.getUserId());

                return Collections.singletonList(listCallbacks.get(0));
            }
        }

        if (!CollectionUtils.isEmpty(list)) {
            leadService.updLeadsCrmActionType(agentCallingRequest.getSessionId(), list,
                    EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentCallingRequest.getUserId());

            return list;
        }
        return Collections.emptyList();
    }

    /**
     * @param agentCallingRequest - body request - to get callback
     */
    private List<CLCallback> snapCallback(AgentCallingRequestDTO agentCallingRequest, String leadType) {
        SimpleDateFormat DATE_FORMAT_REQ = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        GetCallbackByTimeParamsV8 leadCallbackReqTime = new GetCallbackByTimeParamsV8();
        GetCLCallback leadCallbackReq = new GetCLCallback();
        List<CLCallback> leadsCallbackOnTime = new ArrayList<>();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -(Const.BEFORE_CALLBACK_TIME));
        int callbackRemain = Const.CALLBACK_NUM;

        /* Get existed callback */
        leadCallbackReq.setAssigned(agentCallingRequest.getUserId());
        leadCallbackReq.setOrgId(agentCallingRequest.getOrgId());
        // leadCallbackReq.setLimit(callbackRemain);

        if (agentCallingRequest.getDistributionRule().getDistributionType() == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            leadCallbackReq.setLeadType(leadType);
        }
        List<CLCallback> leadsCallbackBefore = callbackService.getCallback(agentCallingRequest.getSessionId(), leadCallbackReq).getResult();
        if (CollectionUtils.isEmpty(leadsCallbackBefore)) {
            leadCallbackReqTime.setFromRequestTime(DATE_FORMAT_REQ.format(now.getTime()));
            leadCallbackReqTime.setLimit(callbackRemain + BUFFER_LEAD_LIMIT);
            leadCallbackReqTime.setOrgId(agentCallingRequest.getOrgId());
            leadCallbackReqTime.setCpId(agentCallingRequest.getCampaignId());
            leadCallbackReqTime.setSkillLevel(agentCallingRequest.getAgentSkillLevel());

            if (agentCallingRequest.getDistributionRule().getDistributionType() == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
                leadCallbackReq.setLeadType(leadType);
            }
            List<CLCallback> leadsCallbackReqTime = callbackService.getCallbackByTimeV8(agentCallingRequest.getSessionId(), leadCallbackReqTime).getResult();

            if (!CollectionUtils.isEmpty(leadsCallbackReqTime)) {
                agentCallingRequest.setLeadRemain(callbackRemain);
                return setCallbackToAgent(agentCallingRequest, leadsCallbackReqTime);
            }
            return Collections.emptyList();
        } else {
            /*
             * Get all callback on time base on user_id
             * */
            now = Calendar.getInstance();
            now.add(Calendar.MINUTE, (Const.BEFORE_CALLBACK_TIME));

            for (CLCallback callback : leadsCallbackBefore) {
                if (now.getTime().after(callback.getRequestTime())) {
                    leadsCallbackOnTime.add(callback);
                }
            }
            if (!CollectionUtils.isEmpty(leadsCallbackOnTime)) {
                return leadsCallbackOnTime;
            } else {
                return Collections.emptyList();
            }
        }
    }

    /**
     * @param agentCallingRequest - body request - set callback to agent
     * @param list                - list callback will be assigned to agent one by one
     */
    private List<CLCallback> setCallbackToAgent(AgentCallingRequestDTO agentCallingRequest, List<CLCallback> list) {
        int breakOut = 0;
        List<CLCallback> leadsStatusCallback = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            for (CLCallback callback : list) {
                if (breakOut == agentCallingRequest.getLeadRemain()) {
                    break;
                }
                UpdateCLCallbackByAssigned leadPayload = new UpdateCLCallbackByAssigned();
                leadPayload.setLeadId(callback.getLeadId());
                leadPayload.setAssigned(agentCallingRequest.getUserId());
                leadPayload.setCpId(agentCallingRequest.getCampaignId());
                leadPayload.setConditionAssigned(0);

                DBResponse<?> result = logService.updateCallbackByAssigned(agentCallingRequest.getSessionId(), leadPayload);

                int conditionalAffected = Integer.parseInt(result.getErrorMsg());
                if (conditionalAffected > 0) {
                    breakOut++;
                    leadsStatusCallback.add(callback);
                }
            }
        }
        return leadsStatusCallback;
    }

    /**
     * @param agentCallingRequest   - body request - set lead to agent
     * @param leadsStatusNewFreedom - list fresh leads with status new not assign to anyone
     * @apiNote - set assign lead (with status new) to agent
     */
    private List<CLFresh> setClueToAgent(AgentCallingRequestDTO agentCallingRequest, List<CLFresh> leadsStatusNewFreedom) {
        List<CLFresh> list = new ArrayList<>();
        int breakOut = 0;
        if (!CollectionUtils.isEmpty(leadsStatusNewFreedom)) {
            for (CLFresh fresh : leadsStatusNewFreedom) {
                if (breakOut == agentCallingRequest.getLeadRemain()) {
                    break;
                }

                UpdLeadByAssignedV3 leadPayload = new UpdLeadByAssignedV3();
                leadPayload.setLeadId(fresh.getLeadId());
                leadPayload.setAssigned(agentCallingRequest.getUserId());
                leadPayload.setCpId(agentCallingRequest.getCampaignId());
                leadPayload.setConditionAssigned(0);
                leadPayload.setModifyby(agentCallingRequest.getUserId());
                leadPayload.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
                leadPayload.setTeam(autoReloadConfigTeam.getConfigTeam(agentCallingRequest.getUserId()));
                leadPayload.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentCallingRequest.getUserId()));

                boolean isUpdate = setUpdateClues(leadPayload);

                if (isUpdate) {
                    breakOut++;
                    list.add(fresh);
                    String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, agentCallingRequest.getOrgId(), String.valueOf(agentCallingRequest.getUserId()));
                    RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                    RedisHelper.saveRedisRecent(agentCallingRequest.getOrgId(), agentCallingRequest.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
                    RedisHelper.checkDuplicateAssignToAgent(stringRedisTemplate, fresh.getOrgId(), fresh.getProdName(), fresh.getName(), fresh.getPhone(), fresh.getLeadId(), agentCallingRequest.getUserId());
                     /*
                     String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, agentCallingRequest.getOrgId(), String.valueOf(agentCallingRequest.getUserId()));
                     RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                     RedisHelper.saveRedisRecent(agentCallingRequest.getOrgId(), agentCallingRequest.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
                     RedisHelper.checkDuplicateAssignToAgent(stringRedisTemplate, fresh.getOrgId(), fresh.getProdName(), fresh.getName(), fresh.getPhone(), fresh.getLeadId(), agentCallingRequest.getUserId());
                     */
                }

            /*
            DBResponse<?> result = logService.updLeadByAssigned(agentCallingRequest.getSessionId(), leadPayload);
                int affectedRows = Integer.parseInt(result.getErrorMsg());
                if (affectedRows > 0) {
                    breakOut++;
                    list.add(fresh);
                    String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, agentCallingRequest.getOrgId(), String.valueOf(agentCallingRequest.getUserId()));
                    RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                    RedisHelper.saveRedisRecent(agentCallingRequest.getOrgId(), agentCallingRequest.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
                    RedisHelper.checkDuplicateAssignToAgent(stringRedisTemplate, fresh.getOrgId(), fresh.getProdName(), fresh.getName(), fresh.getPhone(), fresh.getLeadId(), agentCallingRequest.getUserId());
                }
            */
            }
            return list;
        }
        return list;
    }

    /**
     * @param agentCallingRequest - body request
     * @apiNote - get all lead status urgent from multi campaigns
     */
    private List<CLFresh> snapUrgentMulti(AgentCallingRequestDTO agentCallingRequest) {
        int leadRemain = Const.AGENT_CALLING_NUM;
        int count = 0;
        UpdLeadByAssigned leadPayload;
        LeadParamsRequestDTO leadStatusUrgent = new LeadParamsRequestDTO();
        List<CLFresh> leadsStatusUrgentResponse = new ArrayList<>();

        /* 1. GET LEAD STATUS: EXISTED URGENT */
        leadStatusUrgent.setAssigned(agentCallingRequest.getUserId());
        leadStatusUrgent.setOrgId(agentCallingRequest.getOrgId());
        leadStatusUrgent.setLeadStatus(EnumType.LEAD_STATUS.URGENT.getValue());
        leadStatusUrgent.setLimit(Const.AGENT_CALLING_NUM);
        leadStatusUrgent.setListCampaignId(ExchangeUtils.exchangeListStringToStringWithJoins(",", ExchangeUtils.exchangeListIntegerToListString(agentCallingRequest.getListCampaignsId())));

        List<CLFresh> leadsStatusUrgent = freshService.snapCluesMultiCampaigns(agentCallingRequest.getSessionId(), leadStatusUrgent).getResult();
        if (!CollectionUtils.isEmpty(leadsStatusUrgent)) {
            return leadsStatusUrgent;
        }

        /* 2. GET LEAD STATUS: NEW URGENT */
        leadStatusUrgent.setAssigned(0);
        leadStatusUrgent.setLimit(leadRemain + BUFFER_LEAD_LIMIT);
        leadStatusUrgent.setOrgId(agentCallingRequest.getOrgId());
        leadStatusUrgent.setSkillLevel(agentCallingRequest.getAgentSkillLevel());
        List<CLFresh> leadsStatusUrgentFreedom = freshService.snapCluesMultiCampaigns(agentCallingRequest.getSessionId(), leadStatusUrgent).getResult();

        if (CollectionUtils.isEmpty(leadsStatusUrgentFreedom)) {
            return Collections.emptyList();
        } else {
            for (CLFresh fresh : leadsStatusUrgentFreedom) {
                if (count == leadRemain) {
                    break;
                }
                leadPayload = new UpdLeadByAssigned();
                leadPayload.setLeadId(fresh.getLeadId());
                leadPayload.setAssigned(agentCallingRequest.getUserId());
                leadPayload.setConditionAssigned(0);

                DBResponse<?> result = logService.updLeadByAssigned(agentCallingRequest.getSessionId(), leadPayload);
                int conditionalAffectedResult = Integer.parseInt(result.getErrorMsg());

                if (conditionalAffectedResult > 0) {
                    count++;
                    leadsStatusUrgentResponse.add(fresh);
                    String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, agentCallingRequest.getOrgId(), String.valueOf(agentCallingRequest.getUserId()));
                    RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                    RedisHelper.saveRedisRecent(agentCallingRequest.getOrgId(), agentCallingRequest.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
                }
            }
            if (CollectionUtils.isEmpty(leadsStatusUrgentResponse)) {
                return Collections.emptyList();
            }
            return leadsStatusUrgentResponse;
        }
    }


    /**
     * @param agentCallingRequest - find out calling list belong to { campaign_id, user_id }
     */
    @Override
    public List<CLFresh> snapDistributionRuleAgentRate(AgentCallingRequestDTO agentCallingRequest) {
        GetAgentRateDailyParams agentRatePayload = new GetAgentRateDailyParams();
        LeadParamsRequestDTO leadStatusWithNew = new LeadParamsRequestDTO();
        GetLeadReservedParam leadStatusNewReserveParams = new GetLeadReservedParam();
        GetLeadReservedParam leadReservedOverdueParams = new GetLeadReservedParam();
        List<CLFresh> listFilterStatusCallings = new ArrayList<>();
        List<CLCallback> listCallbackWithStatus;
        List<CLFresh> listCallings;
        LocalDateTime now = LocalDateTime.now();
        int limitRemains = 1;

        logger.info("BUFFER LEAD: {}", BUFFER_LEAD_LIMIT);
        logger.info("AGENT CALLING REQ: {}", LoggerUtils.snagAsLogJson(agentCallingRequest));

        /* Set required */
        String[] currentDistributionRule = agentCallingRequest.getDistributionRule().getParamValue().split(":");
        int rateTio = Integer.parseInt(currentDistributionRule[0]);
        int durationDistributionRule = Integer.parseInt(currentDistributionRule[1]);
        LocalDateTime reservedTime = LocalDateTime.now().plusMinutes(durationDistributionRule);

        logger.info("PARAMS REQ: rateTio = {} - durationDistributionRule = {} - reservedTime = {}", rateTio, durationDistributionRule, reservedTime);

        /* Set body request */
        agentRatePayload.setOrgId(agentCallingRequest.getOrgId());
        agentRatePayload.setAgentId(agentCallingRequest.getUserId());
        agentRatePayload.setListCampaignId(ExchangeUtils.exchangeListStringToStringWithJoins(",", ExchangeUtils.exchangeListIntegerToListString(agentCallingRequest.getListCampaignsId())));

        DBResponse<List<GetAgentRateDailyResp>> listAgents = productService.getAgentRateDaily(agentCallingRequest.getSessionId(), agentRatePayload);
        if (ObjectUtils.allNotNull(listAgents) && !CollectionUtils.isEmpty(listAgents.getResult())) {
            int valueOfRate = 0;

            if (!CollectionUtils.isEmpty(listAgents.getResult())) {
                valueOfRate = rateTio * listAgents.getResult().get(0).getRate();
            }

            /*======= 1. FIND OUT LEAD STATUS: 'Urgent' =======*/
            listCallings = snapUrgentMulti(agentCallingRequest);
            logger.info("LEAD URGENT RESPONSE: {}", LoggerUtils.snagAsLogJson(listCallings));

            if (!CollectionUtils.isEmpty(listCallings)) {
                listFilterStatusCallings = listCallings.stream().filter(fresh -> fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.URGENT.getValue())).collect(Collectors.toList());
            }

            if (!CollectionUtils.isEmpty(listFilterStatusCallings)) {
                return Collections.singletonList(listFilterStatusCallings.get(0));
            }

            /* Get all lead status callback */
            limitRemains = Const.AGENT_CALLING_NUM - listFilterStatusCallings.size();
            listCallbackWithStatus = snapStatusCallbackMulti(agentCallingRequest, null);

            if (!CollectionUtils.isEmpty(listCallbackWithStatus)) {

                /*====== 2. FIND OUT LEAD STATUS: 'Potential' ========*/
                agentCallingRequest.setLeadRemain(limitRemains);
                listCallings = snapCallbacks(listCallbackWithStatus, agentCallingRequest, EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue());

                logger.info("LEAD POTENTIAL RESPONSE: {}", LoggerUtils.snagAsLogJson(listCallings));

                if (!CollectionUtils.isEmpty(listCallings)) {
                    listFilterStatusCallings = listCallings.stream().filter(fresh -> fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue())).collect(Collectors.toList());
                }

                if (!CollectionUtils.isEmpty(listFilterStatusCallings)) {
                    return Collections.singletonList(listFilterStatusCallings.get(0));
                }

                /*====== 3. FIND OUT LEAD STATUS: 'Not Prospect' ========*/
                limitRemains = Const.AGENT_CALLING_NUM - listFilterStatusCallings.size();
                agentCallingRequest.setLeadRemain(limitRemains);
                listCallings = snapCallbacks(listCallbackWithStatus, agentCallingRequest, EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue());

                logger.info("LEAD NOT-PROSPECT RESPONSE: {}", LoggerUtils.snagAsLogJson(listCallings));

                if (!CollectionUtils.isEmpty(listCallings)) {
                    listFilterStatusCallings = listCallings.stream().filter(fresh -> fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue())).collect(Collectors.toList());
                }

                if (!CollectionUtils.isEmpty(listFilterStatusCallings)) {
                    return Collections.singletonList(listFilterStatusCallings.get(0));
                }
            }

            /*======= 4. FIND OUT LEAD STATUS: 'New', which is assigned to agent ========*/
            leadStatusWithNew.setAssigned(agentCallingRequest.getUserId());
            leadStatusWithNew.setOrgId(agentCallingRequest.getOrgId());
            leadStatusWithNew.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadStatusWithNew.setListCampaignId(ExchangeUtils.exchangeListStringToStringWithJoins(",", ExchangeUtils.exchangeListIntegerToListString(agentCallingRequest.getListCampaignsId())));
            leadStatusWithNew.setLimit(limitRemains);
            listCallings.addAll(freshService.snapCluesMultiCampaigns(agentCallingRequest.getSessionId(), leadStatusWithNew).getResult());

            logger.info("LEAD NEW ASSIGNED RESPONSE: {}", LoggerUtils.snagAsLogJson(listCallings));

            if (!CollectionUtils.isEmpty(listCallings)) {
                listFilterStatusCallings = listCallings.stream().filter(fresh -> fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.NEW.getValue())).collect(Collectors.toList());
            }

            if (!CollectionUtils.isEmpty(listFilterStatusCallings)) {
                return Collections.singletonList(listFilterStatusCallings.get(0));
            }


            /*====== 5. FIND OUT LEAD STATUS: 'New', which is reserved ========*/
            leadStatusNewReserveParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadStatusNewReserveParams.setAssigned(0);
            leadStatusNewReserveParams.setReservedby(agentCallingRequest.getUserId());
            leadStatusNewReserveParams.setOrgId(agentCallingRequest.getOrgId());
            List<CLFresh> leadsStatusNewAfter = freshService.getReservedLead(agentCallingRequest.getSessionId(), leadStatusNewReserveParams).getResult();

            logger.info("LEAD RESERVED RESPONSE: {}", LoggerUtils.snagAsLogJson(leadsStatusNewAfter));

            leadReservedOverdueParams.setOrgId(agentCallingRequest.getOrgId());
            leadReservedOverdueParams.setListCampaignId(ExchangeUtils.exchangeListStringToStringWithJoins(",", ExchangeUtils.exchangeListIntegerToListString(agentCallingRequest.getListCampaignsId())));
            leadReservedOverdueParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadReservedOverdueParams.setReservedUntil(now.format(FORM_FORMAT_TIME));

            /* Get lead reserved is over due */
            DBResponse<List<CLFresh>> leadsStatusNewOverdue = freshService.getReservedLead(agentCallingRequest.getSessionId(), leadReservedOverdueParams);

            logger.info("LEADS RESERVED: {}", LoggerUtils.snagAsLogJson(leadsStatusNewOverdue.getResult()));

            /* Release leads reserved is over due */
            if (!CollectionUtils.isEmpty(leadsStatusNewOverdue.getResult()) && ObjectUtils.allNotNull(leadsStatusNewOverdue)) {
                releaseLeadsReserved(agentCallingRequest.getSessionId(), leadsStatusNewOverdue.getResult());
            }

            if (CollectionUtils.isEmpty(leadsStatusNewAfter)) {
                GetLeadReservedParam leadNewReserved = new GetLeadReservedParam();
                leadNewReserved.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
                leadNewReserved.setOrgId(agentCallingRequest.getOrgId());
                leadNewReserved.setReservedby(agentCallingRequest.getUserId());
                leadNewReserved.setReservedUntil(reservedTime.format(FORM_FORMAT_TIME));
                leadNewReserved.setReservedDateTime(reservedTime);
                leadNewReserved.setAssigned(0);
                leadNewReserved.setLimit(valueOfRate);
                updateReservedLeads(leadNewReserved);

                leadsStatusNewAfter = freshService.getReservedLead(agentCallingRequest.getSessionId(), leadStatusNewReserveParams).getResult();
            }

            logger.info("LEAD NEW REVERSED RESPONSE: {}", LoggerUtils.snagAsLogJson(leadsStatusNewAfter));

            if (!CollectionUtils.isEmpty(leadsStatusNewAfter)) {
                limitRemains = Const.AGENT_CALLING_NUM - listCallings.size();
                agentCallingRequest.setLeadRemain(limitRemains);
                listCallings = setClueToAgent(agentCallingRequest, leadsStatusNewAfter);

                logger.info("LEAD RESERVED ASSIGNED RESPONSE: {}", LoggerUtils.snagAsLogJson(listCallings));

                if (!CollectionUtils.isEmpty(listCallings)) {
                    listFilterStatusCallings = listCallings.stream().filter(fresh -> fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.NEW.getValue())).collect(Collectors.toList());
                }

                if (!CollectionUtils.isEmpty(listFilterStatusCallings)) {
                    return Collections.singletonList(listFilterStatusCallings.get(0));
                }
            }


            /*========= 6. FIND OUT LEAD STATUS: 'Consulting' =========*/
            if (!CollectionUtils.isEmpty(listCallbackWithStatus)) {
                limitRemains = Const.AGENT_CALLING_NUM - listCallings.size();
                agentCallingRequest.setLeadRemain(limitRemains);
                listCallings = snapCallbacks(listCallbackWithStatus, agentCallingRequest, EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue());

                logger.info("LEAD CONSULTING RESPONSE: {}", LoggerUtils.snagAsLogJson(listCallings));

                if (!CollectionUtils.isEmpty(listCallings)) {
                    listFilterStatusCallings = listCallings.stream().filter(fresh -> fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue())).collect(Collectors.toList());
                }

                if (!CollectionUtils.isEmpty(listFilterStatusCallings)) {
                    return Collections.singletonList(listFilterStatusCallings.get(0));
                }
            }

            /*======== 7. FIND OUT LEAD STATUS: 'Un-call' ========*/
            if (CollectionUtils.isEmpty(listCallings)) {
                limitRemains = Const.AGENT_CALLING_NUM - listCallings.size();
                agentCallingRequest.setLeadRemain(limitRemains);
                listCallings = snapUnCall(agentCallingRequest, "");
            }

            logger.info("LEAD UN-CALL RESPONSE: {}", LoggerUtils.snagAsLogJson(listCallings));

            if (!CollectionUtils.isEmpty(listCallings)) {
                return Collections.singletonList(listCallings.get(0));
            }
        }
        return listFilterStatusCallings;
    }

    /**
     * @param listCallbacks       - list status callback available
     * @param agentCallingRequest - body request filter
     * @param leadStatus          - lead status
     * @apiNote - get list callback base one lead status
     */
    private List<CLFresh> snapCallbacks(List<CLCallback> listCallbacks, AgentCallingRequestDTO agentCallingRequest, int leadStatus) {
        if (CollectionUtils.isEmpty(listCallbacks)) {
            return Collections.emptyList();
        }
        List<CLFresh> leadsStatusCallbackResponse = new ArrayList<>();
        List<CLCallback> leadsStatusCallbackRequest = listCallbacks.stream().filter(callback -> callback.getLeadStatus().equals(leadStatus)).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(leadsStatusCallbackRequest)) {
            List<CLCallback> listCurrentCallbacks = setCallbacksToAgent(leadsStatusCallbackRequest, agentCallingRequest);

            if (!CollectionUtils.isEmpty(listCurrentCallbacks)) {
                leadsStatusCallbackResponse = CLFresh.mapCallbackToFresh(listCurrentCallbacks);
            }
        }
        return leadsStatusCallbackResponse;
    }

    /**
     * @param listCallbacks       - list status callback available
     * @param agentCallingRequest - body request filter
     * @apiNote - set lead callback to agent
     */
    private List<CLCallback> setCallbacksToAgent(List<CLCallback> listCallbacks, AgentCallingRequestDTO agentCallingRequest) {
        if (CollectionUtils.isEmpty(listCallbacks)) {
            return Collections.emptyList();
        }
        int count = 0;
        List<CLCallback> leadsStatusCallback = new ArrayList<>();
        UpdateCLCallbackByAssigned leadPayload;

        if (!ObjectUtils.allNotNull(agentCallingRequest.getLeadRemain())) {
            agentCallingRequest.setLeadRemain(1);
        }

        for (CLCallback callback : listCallbacks) {
            if (count == agentCallingRequest.getLeadRemain()) {
                break;
            }
            if (!"0".equals(StringUtils.trimAllWhitespace(callback.getAssigned())) && String.valueOf(agentCallingRequest.getUserId()).equals(callback.getAssigned())) {
                count++;
                leadsStatusCallback.add(callback);
                continue;
            }
            leadPayload = new UpdateCLCallbackByAssigned();
            leadPayload.setLeadId(callback.getLeadId());
            leadPayload.setAssigned(agentCallingRequest.getUserId());
            leadPayload.setConditionAssigned(0);
            DBResponse<?> result = logService.updateCallbackByAssigned(agentCallingRequest.getSessionId(), leadPayload);
            int conditionalAffectedResult = Integer.parseInt(result.getErrorMsg());

            if (conditionalAffectedResult > 0) {
                count++;
                leadsStatusCallback.add(callback);
            }
        }
        return leadsStatusCallback;
    }

    /**
     * @param agentCallingRequest - body request
     * @param leadType            - get lead by type : auto or manual
     * @apiNote - get list lead status callback from table cl_callback
     */
    private List<CLCallback> snapStatusCallbackMulti(AgentCallingRequestDTO agentCallingRequest, String leadType) {
        GetCallbackByTimeParams leadStatusCallback = new GetCallbackByTimeParams();
        LeadStatusCallbackWithTimeRequestDTO leadCallbackPayload = new LeadStatusCallbackWithTimeRequestDTO();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -(Const.BEFORE_CALLBACK_TIME));
        int callbackRemain = Const.CALLBACK_NUM;
        String pattern = "yyyyMMdd HH:mm:ss";
        SimpleDateFormat FORMAT_DATE_REQ = new SimpleDateFormat(pattern);
        List<CLCallback> leadsStatusCallbackResponse = new ArrayList<>();

        /* 1. GET LEAD STATUS: EXISTED CALLBACK */
        leadStatusCallback.setAssigned(agentCallingRequest.getUserId());
        leadStatusCallback.setOrgId(agentCallingRequest.getOrgId());
        leadStatusCallback.setLimit(callbackRemain);
        if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.RUBY.getValue())) {
            leadStatusCallback.setLeadType(leadType);
        }

        List<CLCallback> leadsStatusCallback = callbackService.getCallbackOrderByLeadStatus(agentCallingRequest.getSessionId(), leadStatusCallback).getResult();
        if (CollectionUtils.isEmpty(leadsStatusCallback)) {

            leadCallbackPayload.setFromRequestTime(FORMAT_DATE_REQ.format(now.getTime()));
            leadCallbackPayload.setLimit(callbackRemain + BUFFER_LEAD_LIMIT);
            leadCallbackPayload.setOrgId(agentCallingRequest.getOrgId());
            leadCallbackPayload.setListCampaignId(ExchangeUtils.exchangeListStringToStringWithJoins(",", ExchangeUtils.exchangeListIntegerToListString(agentCallingRequest.getListCampaignsId())));

            if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.RUBY.getValue())) {
                leadStatusCallback.setLeadType(leadType);
            }

            List<CLCallback> leadsStatusCallbackFreedom = callbackService.snapStatusCallbacksMulti(agentCallingRequest.getSessionId(), leadCallbackPayload).getResult();
            if (!CollectionUtils.isEmpty(leadsStatusCallbackFreedom)) {
                return leadsStatusCallbackFreedom;
            }

        } else {
            now = Calendar.getInstance();
            now.add(Calendar.MINUTE, (Const.BEFORE_CALLBACK_TIME));
            for (CLCallback callback : leadsStatusCallback) {
                if (now.getTime().after(callback.getRequestTime())) {
                    leadsStatusCallbackResponse.add(callback);
                }
            }
            if (!CollectionUtils.isEmpty(leadsStatusCallbackResponse)) {
                return leadsStatusCallbackResponse;
            }
        }
        return Collections.emptyList();
    }

    /**
     * @param agentCallingRequest - body request
     * @param leadType            - lead type
     * @apiNote - get all un-call
     **/
    private List<CLFresh> snapUnCall(AgentCallingRequestDTO agentCallingRequest, String leadType) {
        List<CLFresh> list;
        String unCallTypeUnion = RedisHelper.getGlobalParamValue(stringRedisTemplate, agentCallingRequest.getOrgId(), 5, 2);
        int unCallType = 1;
        try {
            if (!StringUtils.isEmpty(unCallTypeUnion)) {
                unCallType = Integer.parseInt(unCallTypeUnion);
            }
        } catch (Exception ex) {
            logger.error("[ERROR] Can not get global un-call type: {} - {}", ex, ex.getMessage());
        }
        agentCallingRequest.setUnCallType(unCallType);

        if (!agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
            /*
                if (unCallType == 1) {
                    return snapSelfUnCall(agentCallingRequest, leadType);
                } else {
                    return snapUnCallAbility(agentCallingRequest, leadType);
                }
                */
            list = snapSelfUnCall(agentCallingRequest, leadType);
            if (!CollectionUtils.isEmpty(list)) {
                return list;
            } else {
                list = snapUnCallAbility(agentCallingRequest, leadType);
                if (!CollectionUtils.isEmpty(list)) {
                    return list;
                } else {
                    return Collections.emptyList();
                }
            }
        } else {
            list = snapSelfUnCall(agentCallingRequest, leadType);
            if (!CollectionUtils.isEmpty(list)) {
                return list;
            }
            list = snapUnCallAbility(agentCallingRequest, leadType);
            if (!CollectionUtils.isEmpty(list)) {
                return list;
            } else {
                return Collections.emptyList();
            }
        }

    }

    /**
     * @param agentCallingRequest - body request
     * @param leadType            - lead type
     * @apiNote - get un-assign/ other assigned: un-call
     **/
    private List<CLFresh> snapUnCallAbility(AgentCallingRequestDTO agentCallingRequest, String leadType) {
        GetUncallLeadV2 leadStatusUnCall = new GetUncallLeadV2();
        DBResponse<List<CLFresh>> leadsStatusUnCall;

        /* 1. Get lead status: self Un-call, excepted DR is CIT_UnCall */
        if (!agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
            leadStatusUnCall.setOrgId(agentCallingRequest.getOrgId());
            leadStatusUnCall.setAssigned(agentCallingRequest.getUserId());
            if (!agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.AGENT_RATE_3_ID.getValue())) {
                leadStatusUnCall.setCpId(agentCallingRequest.getCampaignId());
            }
            leadStatusUnCall.setUserDefin03("1");
            leadStatusUnCall.setLimit(Const.AGENT_CALLING_NUM);
            leadStatusUnCall.setCallinglistId(agentCallingRequest.getCallingListStr());

            if (agentCallingRequest.getDistributionRule().getDistributionType() == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
                leadStatusUnCall.setClType(leadType);
            }

            if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
                leadsStatusUnCall = freshService.snapUnCalls(agentCallingRequest.getSessionId(), leadStatusUnCall);
            } else {
                leadsStatusUnCall = freshService.getUncallLeadV2(agentCallingRequest.getSessionId(), leadStatusUnCall);
            }
            if (ObjectUtils.allNotNull(leadsStatusUnCall) && !CollectionUtils.isEmpty(leadsStatusUnCall.getResult())) {
                leadService.updLeadsCrmActionType(agentCallingRequest.getSessionId(), leadsStatusUnCall.getResult(),
                        EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentCallingRequest.getUserId());

                return leadsStatusUnCall.getResult();
            }
        }

        /* 2. Get lead status: new Un-call, with assigned = 0 which is not assign to someone  */
        agentCallingRequest.setConditionalTerms(0);
        List<CLFresh> leadsStatusUnCallUnAssigned = setCluesUnCallToAgent(agentCallingRequest, leadType);
        if (!CollectionUtils.isEmpty(leadsStatusUnCallUnAssigned)) {
            if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
                leadsStatusUnCallUnAssigned.sort(CLFresh::compareWithTotalCall);
            }
            return leadsStatusUnCallUnAssigned;
        }
        /* 3. Get lead status: new Un-call, with assigned != 0 which assigned to someone, excepted DR is CIT_UnCall  */
        if (!agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
            agentCallingRequest.setConditionalTerms(-1); /* -1 means assigned != 0 in db */
            List<CLFresh> leadsStatusUnCallOtherAssigned = setCluesUnCallToAgent(agentCallingRequest, leadType);
            if (!CollectionUtils.isEmpty(leadsStatusUnCallOtherAssigned)) {
                if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
                    leadsStatusUnCallOtherAssigned.sort(CLFresh::compareWithTotalCall);
                }
                return leadsStatusUnCallOtherAssigned;
            }
        }
        return Collections.emptyList();
    }

    /**
     * @param agentCallingRequest - body request
     * @param leadType            - lead type
     * @apiNote - get yourself un-call with user_id
     **/
    private List<CLFresh> snapSelfUnCall(AgentCallingRequestDTO agentCallingRequest, String leadType) {
        GetUncallLeadV2 leadStatusSelfUnCall = new GetUncallLeadV2();
        DBResponse<List<CLFresh>> leadsStatusSelfUnCall;

        leadStatusSelfUnCall.setOrgId(agentCallingRequest.getOrgId());
        leadStatusSelfUnCall.setAssigned(agentCallingRequest.getUserId());
        if (!agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.AGENT_RATE_3_ID.getValue())) {
            leadStatusSelfUnCall.setCpId(agentCallingRequest.getCampaignId());
        }
        leadStatusSelfUnCall.setLimit(Const.AGENT_CALLING_NUM);
        leadStatusSelfUnCall.setCallinglistId(agentCallingRequest.getCallingListStr());

        if (agentCallingRequest.getDistributionRule().getDistributionType() == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            leadStatusSelfUnCall.setClType(leadType);
        }

        if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
            leadsStatusSelfUnCall = freshService.snapUnCalls(agentCallingRequest.getSessionId(), leadStatusSelfUnCall);
        } else {
            leadsStatusSelfUnCall = freshService.getUncallLeadV2(agentCallingRequest.getSessionId(), leadStatusSelfUnCall);
        }

        if (ObjectUtils.allNotNull(leadsStatusSelfUnCall) && !CollectionUtils.isEmpty(leadsStatusSelfUnCall.getResult())) {
            if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
                leadsStatusSelfUnCall.getResult().sort(CLFresh::compareWithTotalCall);
            }
            leadService.updLeadsCrmActionType(agentCallingRequest.getSessionId(), leadsStatusSelfUnCall.getResult(),
                    EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentCallingRequest.getUserId());

            return leadsStatusSelfUnCall.getResult();
        }

        if (!agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
            boolean isRate = agentCallingRequest.getDistributionRule().getDistributionType() == EnumType.DISTRIBUTION_RULE.RATE.getValue();
            if (!isRate) {
                return setCluesUnCallToAgent(agentCallingRequest, leadType);
            }
        }

        /*
        boolean isMaxUnCall = RedisHelper.isMaxUncallByUser(stringRedisTemplate, agentCallingRequest.getOrgId(), agentCallingRequest.getUserId());
        if (!isRate || !isMaxUnCall) {
            return setCluesUnCallToAgent(agentCallingRequest, leadType);
        }
        */
        return Collections.emptyList();
    }

    /**
     * @param agentCallingRequest - body request: org_id, user_id, cp_id, calling_list_str, agent_skill_level, session
     * @param leadType            - lead type : auto or manual
     * @apiNote - assign lead status un-call to agent
     */
    private List<CLFresh> setCluesUnCallToAgent(AgentCallingRequestDTO agentCallingRequest, String leadType) {
        List<CLFresh> list = new ArrayList<>();
        UpdUncallV3 updUncall = new UpdUncallV3();
        DBResponse<List<CLFresh>> leadsStatusUnCall;
        DBResponse<?> leadsUnCalls;
        int leadRemain = Const.AGENT_CALLING_NUM;
        int breakOut = 0;
        /* 1. Get lead status un-call existed before */
        updUncall.setOrgId(agentCallingRequest.getOrgId());
        if (ObjectUtils.allNotNull(agentCallingRequest.getConditionalTerms())) {
            updUncall.setAssigned(agentCallingRequest.getConditionalTerms());
        }
        if (!agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.AGENT_RATE_3_ID.getValue())) {
            updUncall.setCpId(agentCallingRequest.getCampaignId());
        }

        if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
            updUncall.setLimit(leadRemain);
        } else {
            updUncall.setLimit(leadRemain + BUFFER_LEAD_LIMIT);
        }

        updUncall.setCallinglistId(agentCallingRequest.getCallingListStr());
        updUncall.setSkillLevel(agentCallingRequest.getAgentSkillLevel());

        if (agentCallingRequest.getDistributionRule().getDistributionType() == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            updUncall.setClType(leadType);
        }

        if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
            list = snapSelfUnCall(agentCallingRequest, leadType);
            if (!CollectionUtils.isEmpty(list)) {
                return list;
            }
            updUncall.setModifyby(agentCallingRequest.getUserId());
            updUncall.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
            updUncall.setTeam(autoReloadConfigTeam.getConfigTeam(agentCallingRequest.getUserId()));
            updUncall.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentCallingRequest.getUserId()));
            DBResponse<?> getuncallResponse = logService.updateUnCallV3(agentCallingRequest.getSessionId(), updUncall);
            logger.info("Result update un-call unit: {}", LoggerUtils.snagAsLogJson(getuncallResponse));
            if (ObjectUtils.allNotNull(getuncallResponse) && getuncallResponse.getErrorCode() != 0) {
                GetLeadParamsV4 leadParams = new GetLeadParamsV4();

                leadParams.setOrgId(agentCallingRequest.getOrgId());
                leadParams.setLeadId(getuncallResponse.getErrorCode());

                /* Assign lead with status NEW to agent */
                list = freshService.getLeadV4(agentCallingRequest.getSessionId(), leadParams).getResult();
//                list = snapSelfUnCall(agentCallingRequest, leadType);
                logger.info("Result list un-call after update: {}", LoggerUtils.snagAsLogJson(list));
            }
            if (CollectionUtils.isEmpty(list)) {
                return Collections.emptyList();
            }
            return list;
        } else {
            GetUncallLeadV2 leadStatusUnCall = modelMapper.map(updUncall, GetUncallLeadV2.class);
            leadsStatusUnCall = freshService.getUncallLeadV2(agentCallingRequest.getSessionId(), leadStatusUnCall);
        }

        if (!ObjectUtils.allNotNull(leadsStatusUnCall) && CollectionUtils.isEmpty(leadsStatusUnCall.getResult())) {
            return Collections.emptyList();
        }

        /*
         if (agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue())) {
            leadsStatusUnCall.getResult().sort(CLFresh::compareWithTotalCall);
        }
        */

        for (CLFresh fresh : leadsStatusUnCall.getResult()) {
            if (breakOut == leadRemain) {
                break;
            }
            UpdLeadByAssignedV3 leadPayload = new UpdLeadByAssignedV3();
            leadPayload.setLeadId(fresh.getLeadId());
            leadPayload.setAssigned(agentCallingRequest.getUserId());
            if (!agentCallingRequest.getDistributionRule().getDistributionType().equals(EnumType.DISTRIBUTION_RULE.AGENT_RATE_3_ID.getValue())) {
                leadPayload.setCpId(agentCallingRequest.getCampaignId());
            }
            leadPayload.setConditionAssigned(fresh.getAssigned());
            leadPayload.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
            leadPayload.setTeam(autoReloadConfigTeam.getConfigTeam(agentCallingRequest.getUserId()));
            leadPayload.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentCallingRequest.getUserId()));

            if (ObjectUtils.allNotNull(agentCallingRequest.getUnCallType())) {
                if (agentCallingRequest.getUnCallType() == 2) {
                    leadPayload.setUserDefin03("1"); /* get lead locked for user */
                }
            }

            boolean isUpdate = setUpdateClues(leadPayload);
            if (isUpdate) {
                breakOut++;
                list.add(fresh);
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, agentCallingRequest.getOrgId(), String.valueOf(agentCallingRequest.getUserId()));
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                RedisHelper.saveRedisRecent(agentCallingRequest.getOrgId(), agentCallingRequest.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate); // recent:orgId:leadId
                /*
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, agentCallingRequest.getOrgId(), String.valueOf(agentCallingRequest.getUserId()));
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                RedisHelper.saveRedisRecent(agentCallingRequest.getOrgId(), agentCallingRequest.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
                */
            }
            /*
            DBResponse<?> result = logService.updLeadByAssigned(agentCallingRequest.getSessionId(), leadPayload);
            int conditionalAffected = Integer.parseInt(result.getErrorMsg());
            if (conditionalAffected > 0) {
                breakOut++;
                list.add(fresh);
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, agentCallingRequest.getOrgId(), String.valueOf(agentCallingRequest.getUserId()));
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                RedisHelper.saveRedisRecent(agentCallingRequest.getOrgId(), agentCallingRequest.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
            }
            */
        }
        return list;
    }

    /**
     * @param leadPayload - lead request parameters
     */
    private boolean setUpdateClues(UpdLeadByAssignedV2 leadPayload) {
        StringBuilder builder = new StringBuilder();
        builder.append(" UPDATE cl_fresh SET assigned = :assigned ")
                .append(" , modifydate = now() at time zone 'Asia/Ho_Chi_Minh' ");
        if (!StringUtils.isEmpty(leadPayload.getUserDefin03())) {
            builder.append(" , user_defin_03 = :userDef03 ");
        }
        if (leadPayload.getCrmActionType() != null) {
            builder.append(" , crm_action_type = :crmActionType ");
        }
        if (leadPayload.getModifyby() != null) {
            builder.append(" , modifyby = :modifyBy ");
        }
        builder.append(" WHERE lead_id  = :leadId AND assigned = :inConditionAssigned ");
        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("assigned", leadPayload.getAssigned());
        query.setParameter("leadId", leadPayload.getLeadId());
        query.setParameter("inConditionAssigned", leadPayload.getConditionAssigned());
        if (!StringUtils.isEmpty(leadPayload.getUserDefin03())) {
            query.setParameter("userDef03", leadPayload.getUserDefin03());
        }
        if (leadPayload.getCrmActionType() != null) {
            query.setParameter("crmActionType", leadPayload.getCrmActionType());
        }
        if (leadPayload.getCrmActionType() != null) {
            query.setParameter("modifyBy", leadPayload.getModifyby());
        }
        int result = query.executeUpdate();
        logger.info("Update fresh to assign lead: {}", result == 1 ? "success" : "failed");
        return result == 1;
    }

    private boolean setUpdateClues(UpdLeadByAssignedV3 leadPayload) {
        StringBuilder builder = new StringBuilder();
        builder.append(" UPDATE cl_fresh SET assigned = :assigned ")
                .append(" , modifydate = now() at time zone 'Asia/Ho_Chi_Minh' ");
        if (!StringUtils.isEmpty(leadPayload.getUserDefin03())) {
            builder.append(" , user_defin_03 = :userDef03 ");
        }
        if (leadPayload.getCrmActionType() != null) {
            builder.append(" , crm_action_type = :crmActionType ");
        }
        if (leadPayload.getModifyby() != null) {
            builder.append(" , modifyby = :modifyBy ");
        }
        if (leadPayload.getTeam() != null) {
            builder.append(" , team = :team ");
        }
        if (leadPayload.getTeamSupervisor() != null) {
            builder.append(" , team_supervisor = :teamSupervisor ");
        }
        builder.append(" WHERE lead_id  = :leadId AND assigned = :inConditionAssigned ");
        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("assigned", leadPayload.getAssigned());
        query.setParameter("leadId", leadPayload.getLeadId());
        query.setParameter("inConditionAssigned", leadPayload.getConditionAssigned());
        if (!StringUtils.isEmpty(leadPayload.getUserDefin03())) {
            query.setParameter("userDef03", leadPayload.getUserDefin03());
        }
        if (leadPayload.getCrmActionType() != null) {
            query.setParameter("crmActionType", leadPayload.getCrmActionType());
        }
        if (leadPayload.getCrmActionType() != null) {
            query.setParameter("modifyBy", leadPayload.getModifyby());
        }
        if (leadPayload.getTeam() != null) {
            query.setParameter("team", leadPayload.getTeam());
        }
        if (leadPayload.getTeamSupervisor() != null) {
            query.setParameter("teamSupervisor", leadPayload.getTeamSupervisor());
        }
        int result = query.executeUpdate();
        logger.info("Update fresh to assign lead: {}", result == 1 ? "success" : "failed");
        return result == 1;
    }

    private void releaseLeadsReserved(String sessionId, List<CLFresh> list) {
        StringBuilder leadIds = new StringBuilder(list.get(0).getLeadId().toString());
        for (int i = 1; i < list.size(); i++) {
            leadIds.append(",").append(list.get(i).getLeadId());
        }
        UpdLeadReleaseReserved updLeadReleaseReserved = new UpdLeadReleaseReserved();
        updLeadReleaseReserved.setLeadId(leadIds.toString());
        logService.updLeadReleaseReserved(sessionId, updLeadReleaseReserved);
    }

    private void updateReservedLeads(GetLeadReservedParam leadReservedParams) {
        StringBuilder builder = new StringBuilder();
        builder.append(" UPDATE cl_fresh SET reservedby = :reservedBy, reserved_until = :reservedUntil ");
        builder.append(" WHERE lead_id  in ( ");
        builder.append(" SELECT lead_id FROM cl_fresh WHERE assigned = 0 AND reservedby = 0 ");
        if (ObjectUtils.allNotNull(leadReservedParams.getLeadId())) {
            builder.append(" AND lead_id = :leadId ");
        }

        if (ObjectUtils.allNotNull(leadReservedParams.getCpId())) {
            builder.append(" AND cp_id = :cpId ");
        }

        if (ObjectUtils.allNotNull(leadReservedParams.getOrgId())) {
            builder.append(" AND org_id = :orgId ");
        }

        if (ObjectUtils.allNotNull(leadReservedParams.getLeadStatus())) {
            builder.append(" AND lead_status = :leadStatus ");
        }

        builder.append(" ORDER BY next_call_time DESC, lead_id DESC ");

        if (ObjectUtils.allNotNull(leadReservedParams.getLimit())) {
            builder.append(" limit :limit ");
        }

        builder.append(" ) ");

        Query query = entityManager.createNativeQuery(builder.toString());

        query.setParameter("reservedBy", leadReservedParams.getReservedby());
        query.setParameter("reservedUntil", DateUtils.feedStageAsDate(leadReservedParams.getReservedDateTime()), TemporalType.TIMESTAMP);

        if (ObjectUtils.allNotNull(leadReservedParams.getLeadId())) {
            query.setParameter("leadId", leadReservedParams.getLeadId());
        }

        if (ObjectUtils.allNotNull(leadReservedParams.getCpId())) {
            query.setParameter("cpId", leadReservedParams.getCpId());
        }

        if (ObjectUtils.allNotNull(leadReservedParams.getOrgId())) {
            query.setParameter("orgId", leadReservedParams.getOrgId());
        }

        if (ObjectUtils.allNotNull(leadReservedParams.getLeadStatus())) {
            query.setParameter("leadStatus", leadReservedParams.getLeadStatus());
        }

        if (ObjectUtils.allNotNull(leadReservedParams.getLimit())) {
            query.setParameter("limit", leadReservedParams.getLimit());
        }

        int result = query.executeUpdate();
        logger.info("Update lead reserved: {}", result == 1 ? "success" : "failed");
    }
}
