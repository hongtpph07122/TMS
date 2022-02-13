package com.tms.api.rest;

import com.tms.api.dto.AgentInfoDto;
import com.tms.api.dto.AgentMonitorDto;
import com.tms.api.dto.Request.AgentPasswordRequestDto;
import com.tms.api.dto.ThemeDTO;
import com.tms.api.dto.UserCache;
import com.tms.api.entity.*;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.*;
import com.tms.api.repository.TrkAffSubnameMappingRepository;
import com.tms.api.request.AgentCallingRequestDTO;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.*;
import com.tms.api.service.impl.AgentManagermentImpl;
import com.tms.api.task.AutoReloadConfigTeam;
import com.tms.api.task.DBLogWriter;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.*;
import com.tms.entity.*;
import com.tms.entity.log.UpdLeadByAssignedV3;
import com.tms.entity.log.UpdUserV2;
import com.tms.service.impl.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@RestController
@Scope("prototype")
@RequestMapping("/agent")
public class AgentController extends BaseController {
    private final StringRedisTemplate stringRedisTemplate;
    private final UserService userService;
    private final CLFreshService freshService;
    private final  CLCallbackService callBackService;
    private final CLActiveService activeService;
    private final CLInActiveService inactiveService;
    private final CLProductService productService;
    private final LogService logService;
    private final DBLogWriter dbLog;
    private final ModelMapper modelMapper;
    private final AgentManagermentImpl agentManagerment;
    private final AgentService agentService;
    private final JavaMailSender javaMailSender;
    private final UserActionsService userActionsService;
    private final CampaignService campaignService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LeadService leadService;
    private final LogAgentTraceService logAgentTraceService;
    private final AutoReloadConfigTeam autoReloadConfigTeam;
    private final TrkAffSubnameMappingRepository trkAffSubnameMappingRepository;

    private static final String LOGO_PATH = "http://ekiwi.tmsapp.vn/images/logo_tms.png";
    private static final String LOGO = "http://ekiwi.tmsapp.vn/images/Logo_Ecommerce.png";
    private int bufferLeadNum = 50;
    @Value("${config.user-expiretime}")
    private Integer USER_EXPIRE_TIME;

    @Value("${config.email-reset-pass}")
    private String fileResetEmail;

    @Value("${config.is-run-subname-aff: false}")
    private Boolean isRunSubnameAff;

    @Value("${config.org-id}")
    private Integer orgId;

    @Autowired
    public AgentController(UserService userService,
                           StringRedisTemplate stringRedisTemplate,
                           CLFreshService freshService,
                           JavaMailSender javaMailSender,
                           CLCallbackService callBackService,
                           CLActiveService activeService,
                           AgentManagermentImpl agentManagerment,
                           CLInActiveService inactiveService,
                           CLProductService productService,
                           LogService logService,
                           DBLogWriter dbLog,
                           ModelMapper modelMapper,
                           AgentService agentService,
                           UserActionsService userActionsService,
                           CampaignService campaignService,
                           BCryptPasswordEncoder passwordEncoder,
                           LeadService leadService,
                           LogAgentTraceService logAgentTraceService,
                           AutoReloadConfigTeam autoReloadConfigTeam,
                           TrkAffSubnameMappingRepository trkAffSubnameMappingRepository) {
        this.userService = userService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.freshService = freshService;
        this.javaMailSender = javaMailSender;
        this.callBackService = callBackService;
        this.activeService = activeService;
        this.agentManagerment = agentManagerment;
        this.inactiveService = inactiveService;
        this.productService = productService;
        this.logService = logService;
        this.dbLog = dbLog;
        this.modelMapper = modelMapper;
        this.agentService = agentService;
        this.userActionsService = userActionsService;
        this.campaignService = campaignService;
        this.passwordEncoder = passwordEncoder;
        this.leadService = leadService;
        this.logAgentTraceService = logAgentTraceService;
        this.autoReloadConfigTeam = autoReloadConfigTeam;
        this.trkAffSubnameMappingRepository = trkAffSubnameMappingRepository;
    }

    /**
     * get agent by current org id
     */
    @GetMapping
    public TMSResponse<List<User>> getAgent(@ModelAttribute("user") GetUserV2 user) throws TMSException {
        DBResponse<List<User>> lstAgent = null;

        try {
            GetUserV2 userParams = user;
            userParams.setOrgId(getCurrentOriganationId());
            lstAgent = userService.getUserV2(SESSION_ID, userParams);
            List<User> lstUser = lstAgent.getResult();


            return TMSResponse.buildResponse(lstUser, lstAgent.getRowCount());
        } catch (Exception e) {
            logger.error(ErrorMessage.USER_NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.USER_NOT_FOUND);
        }
    }

    @GetMapping("/monitor")
    public TMSResponse<List<AgentMonitorDto>> getMonitor() throws TMSException {
        DBResponse<List<AgentMonitorDto>> lstAgMonitor = null;
        DBResponse<List<User>> lstAgent = null;

        try {
            GetUserParams userParams = new GetUserParams();
            userParams.setOrgId(getCurrentOriganationId());
            lstAgent = userService.getUser(SESSION_ID, userParams);

            List<User> lstUser = lstAgent.getResult();
            for (User user : lstUser) {
                AgentMonitorDto agent = new AgentMonitorDto();
                agent.setAgentName(user.getUserName());
                agent.setAgentStatus(EnumType.AGENT_MONITOR_STATUS.OFFLINE.toString());
                agent.setPhoneExt(Const.phoneExts[Helper.getRandomNumberInRange(0, Const.phoneExts.length - 1)]);
                agent.setDirection("Outbound");
                agent.setDuration("00:00:11");

            }

            return TMSResponse.buildResponse(lstUser, lstAgent.getRowCount());
        } catch (Exception e) {
            logger.error(ErrorMessage.USER_NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.USER_NOT_FOUND);
        }
    }


    @GetMapping("/role")
    public TMSResponse<List<GetPrivByUserResp>> getRole() throws TMSException {
        GetPrivByUserParams params = new GetPrivByUserParams();
        params.setUserId(getCurrentUser().getUserId());

        DBResponse dbResponse = userService.getPrivByUser(SESSION_ID, params);
        return TMSResponse.buildResponse(dbResponse.getResult());
    }

    @GetMapping("/me")
    public AgentInfoDto getAgentDetail() throws TMSException {
        AgentInfoDto agent = new AgentInfoDto();
        User user = getCurrentUser();
        agent.setInfo(userActionsService.findOne(user.getUserId()));

        GetOrganization getOrganization = new GetOrganization();
        getOrganization.setOrgId(user.getOrgId());
        DBResponse<?> resp = freshService.getOrganization(SESSION_ID, getOrganization);

        List<GetOrganizationResp> result = (List<GetOrganizationResp>) resp.getResult();

        String icon = LOGO_PATH;
        String short_icon = LOGO;
        String name = "";

        if (!Helper.isNullOrBlank(result.get(0).getLogoPath01())) {
            icon = result.get(0).getLogoPath01();
        }
        if (!Helper.isNullOrBlank(result.get(0).getLogoPath02())) {
            short_icon = result.get(0).getLogoPath02();
        }
        if (!Helper.isNullOrBlank(result.get(0).getName())) {
            name = result.get(0).getName();
        }
        ThemeDTO theme = new ThemeDTO();
        theme.setLogoPath(icon);
        theme.setLogo(short_icon);
        theme.setCompany(name);
        agent.setTheme(theme);


        GetPrivByUserParams params = new GetPrivByUserParams();
        params.setUserId(getCurrentUser().getUserId());

        DBResponse<?> dbResponse = userService.getPrivByUser(SESSION_ID, params);
        List<Integer> roleList = new ArrayList<>();
        if (dbResponse.getRowCount() > 0) {
            List<GetPrivByUserResp> lstPriv = (List<GetPrivByUserResp>) dbResponse.getResult();

            if (lstPriv != null) {
                for (GetPrivByUserResp getPrivByUserResp : lstPriv) {
                    roleList.add(getPrivByUserResp.getRoleId());
                }
            }
        }
        List<Integer> listWithoutDuplicates = roleList.stream()
                .distinct()
                .collect(Collectors.toList());

        agent.setRoleId(listWithoutDuplicates);
        return agent;
    }

    @GetMapping("/caching/{username}")
    public TMSResponse getAgent(@PathVariable String username) throws TMSException {
        logger.info("[USER]Set user info from DB {}", username);
        DBResponse<List<User>> user = null;
        try {
            Date now = new Date();
            UserCache userCache = new UserCache();

            GetUserParams userParams = new GetUserParams();
            userParams.setUserName(username);
            user = userService.getUser(SESSION_ID, userParams);
            if (user != null && user.getResult().size() > 0) {
                //_curUser = user.getResult().get(0);
                userCache.setUser(user.getResult().get(0));
                userCache.setExprireAt(new Date(now.getTime() + USER_EXPIRE_TIME * 60 * 1000));
                _MAP_USER.put(username, userCache);
            }
            //    return _curUser;
        } catch (Exception e) {
            throw new TMSException(ErrorMessage.USER_NOT_FOUND);
        }

        return TMSResponse.buildResponse(true);

    }

    /**
     * get agent detail by agent id
     *
     * @param agentId
     * @return
     */
    @GetMapping("/{agentId}")
    public TMSResponse<List<User>> getAgent(@PathVariable Integer agentId) throws TMSException {
        GetUserParams userParams = new GetUserParams();
        //TODO
        userParams.setUserId(agentId);
        DBResponse<List<User>> lstAgent = userService.getUser(SESSION_ID, userParams);

        return TMSResponse.buildResponse(lstAgent.getResult());
    }

    //get callinglist of agent
    public List<CLFresh> getCallingListByAgent() throws TMSException {
        return null;
    }


    @GetMapping("/is-expired")
    public TMSResponse getIsExpire() {
        if (!_curUser.getUserType().equals(EnumType.USER_TYPE.AGENT.getValue()))
            return TMSResponse.buildResponse(Boolean.FALSE);
        GetUserParamsV5 params = new GetUserParamsV5();
        params.setOrgId(getCurOrgId());
        params.setUserName(_curUser.getUserName());
        DBResponse<List<User>> dbResponse = userService.getUserV5(SESSION_ID, params);
        if (dbResponse == null || dbResponse.getResult().isEmpty())
            return TMSResponse.buildResponse(Boolean.FALSE, 0, ErrorMessage.USER_NOT_FOUND.getMessage(), ErrorMessage.USER_NOT_FOUND.getCode());

        User user = dbResponse.getResult().get(0);
        return TMSResponse.buildResponse(user.getIsExpired());
    }

    @PutMapping("/update")
    public TMSResponse updateUser(@RequestBody AgentPasswordRequestDto agent) {
        if (!StringUtils.hasText(agent.getOldPassword()) || !StringUtils.hasText(agent.getNewPassword()) ||
                !Pattern.matches(Const.REGEX_PASSWORD, agent.getNewPassword()))
            return TMSResponse.buildResponse(Boolean.FALSE, 0, ErrorMessage.INVALID_PARAM.getMessage(), ErrorMessage.INVALID_PARAM.getCode());

        GetUserParamsV5 getUserParams = new GetUserParamsV5();
        getUserParams.setOrgId(getCurOrgId());
        getUserParams.setUserName(_curUser.getUserName());
        DBResponse<List<User>> getUserResponse = userService.getUserV5(SESSION_ID, getUserParams);
        if (getUserResponse == null || getUserResponse.getResult().isEmpty())
            return TMSResponse.buildResponse(Boolean.FALSE, 0, ErrorMessage.USER_NOT_FOUND.getMessage(), ErrorMessage.USER_NOT_FOUND.getCode());
        User user = getUserResponse.getResult().get(0);

        if (!passwordEncoder.matches(agent.getOldPassword(), user.getPassword()))
            return TMSResponse.buildResponse(Boolean.FALSE, 0, ErrorMessage.PASSWORD_NOT_MATCH.getMessage(), ErrorMessage.PASSWORD_NOT_MATCH.getCode());

        UpdUserV2 params = new UpdUserV2();
        params.setUserId(_curUser.getUserId());
        params.setModifyby(_curUser.getUserId());
        params.setPassword(passwordEncoder.encode(agent.getNewPassword()));
        params.setPasswordUpdateTime(DateHelper.toTMSDateFormat(new Date()));
        params.setIsExpired(Boolean.FALSE);
        DBResponse<?> dbResponse = userService.upUserV2(SESSION_ID, params);
        if (dbResponse == null) {
            logger.info(ErrorMessage.USER_NOT_FOUND.getMessage());
            return TMSResponse.buildResponse(Boolean.FALSE, 0, ErrorMessage.USER_NOT_FOUND.getMessage(), ErrorMessage.USER_NOT_FOUND.getCode());
        }

        _MAP_USER.remove(_curUser.getUserName());

        return TMSResponse.buildResponse(Boolean.TRUE, 0, "Success", 200);
    }

    @GetMapping("/calling")
    public TMSResponse<?> getAgentCallingNew() throws Exception {
        List<CLFresh> emptyList = new ArrayList<CLFresh>();
        List<CLFresh> callingList = new ArrayList<CLFresh>();
        User user = _curUser;
        int userId = user.getUserId();
        int orgId = getCurOrgId();
        // Check agent state is AVAILABLE
        Integer agentId = _curUser.getUserId();
        String redisKey = RedisHelper.createLogAgentTraceRedisKey(_curUser.getOrgId(), Const.LOG_AGENT_STATE, agentId);
        LogAgentTrace agentTrace = logAgentTraceService.getLatestActivity(agentId, Const.LOG_AGENT_STATE, redisKey);
        UUID sessionId = UUID.randomUUID();

        //chi AGENT moi duoc lay lead
        if (!_curUser.getUserType().equals(Const.USER_AGENT_TYPE))
            return createCallingListRes(emptyList, orgId, agentTrace, sessionId);

        if (!Objects.equals(agentTrace.getValueCode(), EnumType.AGENT_STATE.AVAILABLE.getValue()) &&
                !Objects.equals(agentTrace.getValueCode(), EnumType.AGENT_STATE.ASSIGNED.getValue()) &&
                !Objects.equals(agentTrace.getValueCode(), EnumType.AGENT_STATE.ON_CALL.getValue()) &&
                !Objects.equals(agentTrace.getValueCode(), EnumType.AGENT_STATE.WRAP_UP.getValue())) {
            logger.info("agent status not ready {} | {}", agentId, sessionId);
            return createCallingListRes(emptyList, orgId, agentTrace, sessionId);
        }
        GetCampaignAgent cpAgentParam = new GetCampaignAgent();
        cpAgentParam.setUserId(userId);
        cpAgentParam.setOrgId(orgId);
        cpAgentParam.setStatus(EnumType.CAMPAIGN_STATUS_ID.RUNNING.getValue());
        List<GetCampaignAgentResp> campaigns = freshService.getCampaignAgent(SESSION_ID, cpAgentParam).getResult();
        if (campaigns.isEmpty()) {
            logger.error("campaign size null!");
            return createCallingListRes(emptyList, orgId, agentTrace, sessionId);
        }

        int cpId = campaigns.get(0).getCpId();

        //Get campaign from config
        CPCampaignResp campaign = campaignService.getConfigCampaign(SESSION_ID, orgId, cpId);
        if (campaign == null) {
            logger.error("Get campaign config null!");
            return createCallingListRes(emptyList, orgId, agentTrace, sessionId);
        }
        //Get distribution rule from config
        GetCpDistributionRuleResp rule = campaignService.getConfigDistributionRules(SESSION_ID, orgId, cpId, campaign.getDistributionRule());
        if (rule == null) {
            logger.error("Get rule config null");
            return createCallingListRes(emptyList, orgId, agentTrace, sessionId);
        }
        GetCpCallListSkill callingListParam = new GetCpCallListSkill();
        callingListParam.setCpId(cpId);
        callingListParam.setOrgId(orgId);
        callingListParam.setUserId(userId);
        List<GetCpCallListSkillResp> callingListRes = productService.getCpCallListSkill(SESSION_ID, callingListParam).getResult();
        if (callingListRes.isEmpty()) {
            return createCallingListRes(emptyList, orgId, agentTrace, sessionId);
        }
        String callingListStr = callingListRes.get(0).getCallinglist();
        Integer agentSkillLevel = 0;
        if (callingListRes.get(0).getAgSkillLevel() != null) {
            agentSkillLevel = Arrays.stream(Arrays.stream(callingListRes.get(0).getAgSkillLevel().split(","))
                    .mapToInt(Integer::parseInt)
                    .toArray()).max().getAsInt();
        }
        if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            logger.info("getCallingListByAgent 2+++++++++ {}|{}|{}", userId, cpId, 1212121212);
            // get urgent leads
            callingList = getOrAssignUrgentLead(userId, cpId, orgId, agentSkillLevel);
            if (callingList != null && !callingList.isEmpty()) {
                logger.info("get urgent list count $$$$$$$$$$ {}", callingList.size());
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            List<CLCallback> callbacks = getOrAssignCallback(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.RUBY.getValue(), "A", agentSkillLevel);
            if (callbacks != null && !callbacks.isEmpty()) {
                logger.info("getCallingListByAgent +++++++++ {}{}", userId, cpId);
                for (CLCallback callback : callbacks) {
                    callingList.add(modelMapper.map(callback, CLFresh.class));
                }
            }
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            //get current lead type A
            GetLeadParamsV5 currLeadParams = new GetLeadParamsV5();
            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);
            currLeadParams.setClType(EnumType.CALLING_LIST_TYPE.REALTIME.getValue());
            callingList.addAll(freshService.getLeadV5(SESSION_ID, currLeadParams).getResult());
            logger.info("getCallingListByAgent ********** {}", callingList.size());

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            GetNewestLeadV5 leadParams = new GetNewestLeadV5();
            leadParams.setLimit(remainCount + bufferLeadNum);
            leadParams.setOrgId(orgId);
            leadParams.setAssigned(0);
            leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadParams.setCallinglistId(callingListStr);
            leadParams.setClType(EnumType.CALLING_LIST_TYPE.REALTIME.getValue());
            leadParams.setSkillLevel(agentSkillLevel);
            List<CLFresh> newFreshList = freshService.getNewestLeadV5(SESSION_ID, leadParams).getResult();
            List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
            callingList.addAll(assignedLeads);

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            //get uncall Auto
            callingList.addAll(getUncallList(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "1", agentSkillLevel));

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            List<CLCallback> callbacksManual = getOrAssignCallback(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.RUBY.getValue(), "M", agentSkillLevel);
            if (callbacksManual != null && !callbacksManual.isEmpty()) {
                logger.info("getCallingListByAgent +++++++++ {}{}", userId, cpId);
                for (CLCallback callback : callbacksManual) {
                    callingList.add(modelMapper.map(callback, CLFresh.class));
                }
            }

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            //get current lead type M
            GetLeadParamsV5 currLeadParamsManual = new GetLeadParamsV5();
            currLeadParamsManual.setAssigned(userId);
            currLeadParamsManual.setOrgId(orgId);
            currLeadParamsManual.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParamsManual.setLimit(remainCount);
            currLeadParamsManual.setClType(EnumType.CALLING_LIST_TYPE.RESERVER.getValue());
            callingList.addAll(freshService.getLeadV5(SESSION_ID, currLeadParams).getResult());
            logger.info("getCallingListByAgent ********** {}", callingList.size());

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            GetNewestLeadV4 leadParamsManual = new GetNewestLeadV4();
            leadParamsManual.setLimit(remainCount + bufferLeadNum);
            leadParamsManual.setOrgId(orgId);
            leadParamsManual.setAssigned(0);
            leadParamsManual.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadParamsManual.setCallinglistId(callingListStr);
            leadParamsManual.setClType(EnumType.CALLING_LIST_TYPE.RESERVER.getValue());
            List<CLFresh> newFreshListManual = freshService.getNewestLeadV4(SESSION_ID, leadParamsManual).getResult();
            List<CLFresh> assignedLeadsManual = assignFreshLead(userId, cpId, remainCount, newFreshListManual, orgId);
            callingList.addAll(assignedLeadsManual);
            // get uncall type M
            if (callingList.isEmpty()) {
                callingList = getUncallList(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "3", agentSkillLevel);
            }

        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.LIFO.getValue()) {
            callingList = this.getUrgentAndCallback(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.LIFO.getValue(), "", agentSkillLevel);
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("get lead urget + callback $$$$$$$$$$ {}", callingList.get(0).getLeadId());
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();
            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);
            callingList.addAll(freshService.getLeadV4(SESSION_ID, currLeadParams).getResult());

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                leadService.updLeadsCrmActionType(SESSION_ID, callingList,
                        EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), userId);

                logger.info("get lead new assigned {} $$$$$$$$$$ {}", userId, callingList.get(0).getLeadId());
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetNewestLeadV5 leadParams = new GetNewestLeadV5();
            leadParams.setLimit(remainCount + bufferLeadNum);
            leadParams.setOrgId(orgId);
            leadParams.setAssigned(0);
            leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadParams.setCallinglistId(callingListStr);
            leadParams.setSkillLevel(agentSkillLevel);
            List<CLFresh> newFreshList = freshService.getNewestLeadV5(SESSION_ID, leadParams).getResult();
            List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
            callingList.addAll(assignedLeads);
            // get uncall
            if (callingList.isEmpty()) {
                callingList = getUncallList(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "", agentSkillLevel);
            }
        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.FIFO.getValue()) {
            callingList = this.getUrgentAndCallback(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.FIFO.getValue(), "", agentSkillLevel);
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();
            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);
            callingList.addAll(freshService.getLeadV4(SESSION_ID, currLeadParams).getResult());
            logger.info("getCallingListByAgent **** {}", callingList.size());

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            GetNewestLeadV5 leadParams = new GetNewestLeadV5();
            leadParams.setLimit(remainCount + bufferLeadNum);
            leadParams.setAssigned(0);
            leadParams.setOrgId(orgId);
            leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadParams.setCallinglistId(callingListStr);
            List<CLFresh> newFreshList = freshService.getNewestLeadV5(SESSION_ID, leadParams).getResult();
            List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
            callingList.addAll(assignedLeads);
            // get uncall
            if (callingList.isEmpty()) {
                callingList = getUncallList(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "", agentSkillLevel);
            }
        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.MIX.getValue()) {
            callingList = this.getUrgentAndCallback(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.MIX.getValue(), "", agentSkillLevel);
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();
            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);
            callingList.addAll(freshService.getLeadV4(SESSION_ID, currLeadParams).getResult());
            logger.info("getCallingListByAgent ****** {}", callingList.size());

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            int newRatio = 2;
            int oldRatio = 1;
            if (rule.getParamValue() != null && !rule.getParamValue().isEmpty()) {
                try {
                    String[] ratios = rule.getParamValue().split(":");
                    newRatio = Integer.parseInt(ratios[0]);
                    oldRatio = Integer.parseInt(ratios[1]);
                } catch (Exception ex) {

                }
            }
            int newCount = Math.round(remainCount * newRatio / ((newRatio + oldRatio) * 1f));
            int oldCount = remainCount - newCount;
            if (newCount > 0) {
                GetNewestLeadV4 leadParams = new GetNewestLeadV4();
                leadParams.setLimit(remainCount + bufferLeadNum);
                leadParams.setAssigned(0);
                leadParams.setOrgId(orgId);
                leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
                leadParams.setCallinglistId(callingListStr);
                List<CLFresh> newFreshList = freshService.getNewestLeadV4(SESSION_ID, leadParams).getResult();
                List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
                callingList.addAll(assignedLeads);
            }
            if (oldCount > 0) {
                List<CLFresh> tmpCallingList = new ArrayList<CLFresh>();
                CLActiveLeadParams activeLeadParams = new CLActiveLeadParams();
                activeLeadParams.setAssigned(0);
                activeLeadParams.setOrgId(orgId);
                activeLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
                activeLeadParams.setLimit(oldCount);
                activeLeadParams.setCallinglistId(callingListStr);
                List<CLActive> activeLeads = activeService.getActiveLead(SESSION_ID, activeLeadParams).getResult();
                for (CLActive activeLead : activeLeads) {
                    tmpCallingList.add(modelMapper.map(activeLead, CLFresh.class));
                }

                CLInActiveParams inactiveLeadParams = new CLInActiveParams();
                inactiveLeadParams.setAssigned(0);
                inactiveLeadParams.setOrgId(orgId);
                inactiveLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
                inactiveLeadParams.setLimit(oldCount);
                inactiveLeadParams.setCallinglistId(callingListStr);
                List<CLInActive> inactiveLeads = inactiveService.getInActiveLead(SESSION_ID, inactiveLeadParams).getResult();
                for (CLInActive inactiveLead : inactiveLeads) {
                    tmpCallingList.add(modelMapper.map(inactiveLead, CLFresh.class));
                }

                Collections.sort(tmpCallingList, (a, b) -> {
                    return a.getLeadId() - b.getLeadId();
                });

                List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, oldCount, tmpCallingList, orgId);
                callingList.addAll(assignedLeads);
            }
            // get uncall
            if (callingList.isEmpty()) {
                callingList = getUncallList(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "", agentSkillLevel);
            }
        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.RATE.getValue()) {
            callingList = this.getUrgentAndCallback(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.RATE.getValue(), "", agentSkillLevel);
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();
            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);
            callingList.addAll(freshService.getLeadV4(SESSION_ID, currLeadParams).getResult());
            logger.info("getCallingListByAgent ******** {}", callingList.size());

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            //1. check user is online lan dau trong ngay (kiem tra da duoc tinh static:org:userId lead hay chua?
            //neu da duoc tinh thi bo qua, neu chua duoc tinh thi add them user va tinh lai redis static
            String key = Helper.createRedisKey(Const.REDIS_PREFIX_STATIC, String.valueOf(orgId) + ":" + cpId);
            String exist = RedisHelper.getKey(stringRedisTemplate, key, String.valueOf(userId));
            logger.info("RATE EXIST: {}", exist);
            if (exist == null) {
                Map<String, String> map = new HashMap<>();
                map.put(String.valueOf(userId), "0");
                RedisHelper.saveRedis(stringRedisTemplate, map, key);
                //re-calculate score
                RedisHelper.caculateRateByTotalLead(stringRedisTemplate);
            }
            if (!RedisHelper.isMaxLeadByUser(stringRedisTemplate, orgId, userId, cpId)) {
                GetNewestLeadV4 leadParams = new GetNewestLeadV4();
                leadParams.setLimit(remainCount + bufferLeadNum);
                leadParams.setOrgId(orgId);
                leadParams.setAssigned(0);
                leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
                leadParams.setCallinglistId(callingListStr);
                List<CLFresh> newFreshList = freshService.getNewestLeadV4(SESSION_ID, leadParams).getResult();
                List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
                callingList.addAll(assignedLeads);
            }
            // get uncall
            if (callingList.isEmpty()) {
                callingList = getUncallList(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "", agentSkillLevel);
            }
        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.LIFO2.getValue()) {
            callingList = this.getUrgentAndCallback(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.LIFO2.getValue(), "", agentSkillLevel);
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();
            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);
            callingList.addAll(freshService.getLeadV4(SESSION_ID, currLeadParams).getResult());

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                leadService.updLeadsCrmActionType(SESSION_ID, callingList,
                        EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), userId);

                logger.info("getCallingListByAgent $$$$$$$$$$ {}", remainCount);
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetNewestLeadV5 leadParams = new GetNewestLeadV5();
            leadParams.setLimit(remainCount + bufferLeadNum);
            leadParams.setOrgId(orgId);
            leadParams.setAssigned(0);
            leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadParams.setCallinglistId(callingListStr);
            leadParams.setSkillLevel(agentSkillLevel);
            List<CLFresh> newFreshList = freshService.getNewestLeadV5(SESSION_ID, leadParams).getResult();
            List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
            callingList.addAll(assignedLeads);
            // get uncall
            if (callingList.isEmpty()) {
                callingList = getUncallListLifo(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "", agentSkillLevel);
            }
        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.LIFO_V2.getValue()) {

            /* 1. Get lead with status URGENT */
            callingList = getOrAssignUrgentLead(userId, cpId, orgId, agentSkillLevel);
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            List<CLCallback> clCallbackList = getCallbackOrderByLeadStatus(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.LIFO_V2.getValue(), null);
/*
            for(CLCallback callback : clCallbackList){
                if(callback.get)
            }*/

            /* 2. Get lead with status CALLBACK - PROSPECT */
            callingList = getOrAssignCallbackProspectOrConsulting(clCallbackList, EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue(), userId, cpId, remainCount);

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            /* 3. Get lead with status NEW, which is assigned to agent */
            GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();

            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);

            callingList.addAll(freshService.getLeadV4(SESSION_ID, currLeadParams).getResult());
            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            /* 4. Get lead with status NEW, which no assign to agent */
            GetLeadParamsV4 leadParams = new GetLeadParamsV4();

            leadParams.setLimit(remainCount + bufferLeadNum);
            leadParams.setAssigned(0);
            leadParams.setOrgId(orgId);
            leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadParams.setCallinglistId(callingListStr);

            /* Assign lead with status NEW to agent */
            List<CLFresh> newFreshList = freshService.getLeadV4(SESSION_ID, leadParams).getResult();
            List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
            callingList.addAll(assignedLeads);
            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            /* 5. Get lead with status CALLBACK - CONSULTING */
            callingList = getOrAssignCallbackProspectOrConsulting(clCallbackList, EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue(), userId, cpId, remainCount);

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            /* 6. Get lead with status UN-CALL */
            if (callingList.isEmpty()) {
                callingList = getUncallList(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "", agentSkillLevel);
            }
        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.LIFO2_V2.getValue()) {

            /* 1. Get lead with status URGENT */
            callingList = getOrAssignUrgentLead(userId, cpId, orgId, agentSkillLevel);
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            List<CLCallback> clCallbackList = getCallbackOrderByLeadStatus(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.LIFO2_V2.getValue(), null);

            /* 2. Get lead with status CALLBACK - PROSPECT */
            callingList = getOrAssignCallbackProspectOrConsulting(clCallbackList, EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue(), userId, cpId, remainCount);
            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            /* 3. Get lead with status NEW, which is assigned to agent */
            GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();

            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);

            callingList.addAll(freshService.getLeadV4(SESSION_ID, currLeadParams).getResult());
            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            /* 4. Get lead with status NEW, which no assign to agent */
            GetLeadParamsV4 leadParams = new GetLeadParamsV4();

            leadParams.setLimit(remainCount + bufferLeadNum);
            leadParams.setAssigned(0);
            leadParams.setOrgId(orgId);
            leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadParams.setCallinglistId(callingListStr);

            /* Assign lead with status NEW to agent */
            List<CLFresh> newFreshList = freshService.getLeadV4(SESSION_ID, leadParams).getResult();
            List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
            callingList.addAll(assignedLeads);
            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            /* 5. Get lead with status CALLBACK - CONSULTING */
            callingList = getOrAssignCallbackProspectOrConsulting(clCallbackList, EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue(), userId, cpId, remainCount);
            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }

            /* 6. Get lead with status UN-CALL */
            if (callingList.isEmpty()) {
                callingList = getUncallList(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "", agentSkillLevel);
            }

        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue()) {
            AgentCallingRequestDTO agentCallingRequest = new AgentCallingRequestDTO();
            agentCallingRequest.setUserId(userId);
            agentCallingRequest.setOrgId(orgId);
            agentCallingRequest.setAgentSkillLevel(agentSkillLevel);
            agentCallingRequest.setCallingListStr(callingListStr);
            agentCallingRequest.setDistributionRule(rule);
            agentCallingRequest.setSessionId(SESSION_ID);
            agentCallingRequest.setCampaignId(cpId);
            callingList = agentService.snapDistributionRuleCITUnCall(agentCallingRequest);
            return createCallingListRes(callingList, orgId, agentTrace, sessionId);
        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.AGENT_RATE_3_ID.getValue()) {
            List<Integer> listCampaignIdRunning = new ArrayList<>();
            for (GetCampaignAgentResp campaignOne : campaigns) {
                listCampaignIdRunning.add(campaignOne.getCpId());
            }
            AgentCallingRequestDTO agentCallingRequest = new AgentCallingRequestDTO();
            agentCallingRequest.setUserId(userId);
            agentCallingRequest.setOrgId(orgId);
            agentCallingRequest.setAgentSkillLevel(agentSkillLevel);
            agentCallingRequest.setCallingListStr(callingListStr);
            agentCallingRequest.setDistributionRule(rule);
            agentCallingRequest.setSessionId(SESSION_ID);
            agentCallingRequest.setListCampaignsId(listCampaignIdRunning);
            callingList = agentService.snapDistributionRuleAgentRate(agentCallingRequest);
            return createCallingListRes(callingList, orgId, agentTrace, sessionId);
        } else if (rule.getDistributionType() == EnumType.DISTRIBUTION_RULE.UNCALL_ME.getValue()) {
            callingList = this.getUrgentAndCallback(userId, cpId, orgId, callingListStr, EnumType.DISTRIBUTION_RULE.LIFO.getValue(), "", agentSkillLevel);
            int remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                logger.info("get lead urget + callback $$$$$$$$$$ {}", callingList.get(0).getLeadId());
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();
            currLeadParams.setAssigned(userId);
            currLeadParams.setOrgId(orgId);
            currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            currLeadParams.setLimit(remainCount);
            callingList.addAll(freshService.getLeadV4(SESSION_ID, currLeadParams).getResult());

            remainCount = Const.AGENT_CALLING_NUM - callingList.size();
            if (remainCount <= 0) {
                leadService.updLeadsCrmActionType(SESSION_ID, callingList,
                        EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), userId);

                logger.info("get lead new assigned {} $$$$$$$$$$ {}", userId, callingList.get(0).getLeadId());
                return createCallingListRes(callingList, orgId, agentTrace, sessionId);
            }
            GetNewestLeadV5 leadParams = new GetNewestLeadV5();
            leadParams.setLimit(remainCount + bufferLeadNum);
            leadParams.setOrgId(orgId);
            leadParams.setAssigned(0);
            leadParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
            leadParams.setCallinglistId(callingListStr);
            leadParams.setSkillLevel(agentSkillLevel);
            List<CLFresh> newFreshList = freshService.getNewestLeadV5(SESSION_ID, leadParams).getResult();
            List<CLFresh> assignedLeads = assignFreshLead(userId, cpId, remainCount, newFreshList, orgId);
            callingList.addAll(assignedLeads);
            // get uncall
            if (callingList.isEmpty()) {
                callingList = getUncallListME(userId, orgId, cpId, callingListStr, rule.getDistributionType(), "", agentSkillLevel);
            }
        }
        return createCallingListRes(callingList, orgId, agentTrace, sessionId);

    }

    private List<CLFresh> getOrAssignCallbackProspectOrConsulting(List<CLCallback> clCallbackList, int leadStatus, int agentId, int cpId, int callbackNum) throws Exception {

        List<CLFresh> calling = new ArrayList<>();
        boolean hasAssigned = false;
        if (!clCallbackList.isEmpty()) {
            List<CLCallback> listCallback = new ArrayList<>();
            for (CLCallback callback : clCallbackList) {
                if (callback.getLeadStatus() == leadStatus) {
                    listCallback.add(callback);
                }
            }
            if (!listCallback.isEmpty()) {
//                List<CLCallback> callbacks = (hasAssigned ? listCallback : assignCallBackLead(agentId, cpId, callbackNum, listCallback));
                List<CLCallback> callbacks = (assignCallBackLeadStatus(agentId, cpId, callbackNum, listCallback));
                if (callbacks != null && !callbacks.isEmpty()) {
                    logger.info("getCallbackProspectOrConsulting  {} +++++++++ {}{}", leadStatus, agentId, cpId);
                    for (CLCallback callback : callbacks) {
                        calling.add(modelMapper.map(callback, CLFresh.class));
                    }
                }

            }
        }
        return calling;
    }

    private List<CLFresh> getUrgentAndCallback(int userId, int cpId, int orgId, String callingListStr, int distributionType, String leadType, Integer agentSkillLevel) throws IOException {
        List<CLFresh> callingList = getOrAssignUrgentLead(userId, cpId, orgId, agentSkillLevel);
        if (!CollectionUtils.isEmpty(callingList)) {
            logger.info("get urgent list count $$$$$$$$$$ {}", callingList.size());
            return callingList;
        }
        List<CLCallback> callbacks = getOrAssignCallback(userId, cpId, orgId, callingListStr, distributionType, leadType, agentSkillLevel);
        if (!CollectionUtils.isEmpty(callbacks)) {
            logger.info("getCallingListByAgent +++++++++ {}{}", userId, cpId);
            for (CLCallback callback : callbacks) {
                callingList.add(modelMapper.map(callback, CLFresh.class));
            }

            leadService.updLeadsCrmActionType(SESSION_ID, callingList,
                    EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), userId);
        }
        return callingList;
    }

    private TMSResponse<?> createCallingListRes(List<CLFresh> listCallings, int orgId, LogAgentTrace lastAgentTrace, UUID sessionId) throws ParseException {
        String key;
        Integer curUserId = _curUser.getUserId();
        if (!CollectionUtils.isEmpty(listCallings)){
            for (CLFresh fresh : listCallings) {
                EnumType.LEAD_TYPE leadType = null;
                if (EnumType.LEAD_STATUS.isUncall(fresh.getLeadStatus()) || EnumType.LEAD_STATUS.isCallback(fresh.getLeadStatus())){
                    leadType = EnumType.LEAD_TYPE.UNCALL;
                }
                else if (fresh.getLeadStatus() == EnumType.LEAD_STATUS.NEW.getValue()){
                    leadType = EnumType.LEAD_TYPE.NEW;
                }
                //xử lý name? config true/false
                if (isRunSubnameAff && !StringUtils.isEmpty(fresh.getAffiliateId())) {
                    List<TrkAffSubnameMapping> affSubnameMappings = trkAffSubnameMappingRepository.getByAffName(fresh.getAffiliateId());
                    if (!CollectionUtils.isEmpty(affSubnameMappings)) {
                        TrkAffSubnameMapping affSubnameMapping = affSubnameMappings.get(0);
                        fresh.setAffiliateId(affSubnameMapping.getAffName() + affSubnameMapping.getSubName());
                    }

                }
                // Log agent state GET LEAD
                // Create new session when getting new lead
                Integer lastValueCode = 0;
                if (lastAgentTrace != null) {{
                    lastValueCode = lastAgentTrace.getValueCode();
                }}
                Integer leadId = fresh.getLeadId();
                if (lastValueCode != 0) {
                    if (lastValueCode.equals(EnumType.AGENT_STATE.AVAILABLE.getValue())
                            || lastValueCode.equals(EnumType.AGENT_STATE.ASSIGNED.getValue())) {
                        String redisKey = RedisHelper.createLogAgentTraceRedisKey(_curUser.getOrgId(), Const.LOG_AGENT_STATE, curUserId);
                        if (leadId.equals(lastAgentTrace.getObjectId())) {
                            sessionId = lastAgentTrace.getSessionId();
                        }
                        LogAgentTrace agentTrace = new LogAgentTrace.LogAgentTraceBuilder(
                                curUserId, Const.LOG_AGENT_STATE, EnumType.AGENT_STATE.ASSIGNED.getName(),
                                EnumType.AGENT_STATE.ASSIGNED.getValue(), new Date())
                                .setLastValue(lastAgentTrace.getValue())
                                .setLastValueCode(lastValueCode)
                                .setObjectType("lead")
                                .setObjectId(leadId)
                                .setOnField("lead_status")
                                .setSessionId(sessionId)
                                .setFlagCode(lastAgentTrace.getFlagCode())
                                .setFlagValue(lastAgentTrace.getFlagValue())
                                .setFlagMessage(lastAgentTrace.getFlagMessage())
                                .build();
                        logAgentTraceService.logActivity(agentTrace, redisKey);
                    }
                }
                if (!ObjectUtils.allNotNull(leadType)) {
                    continue;
                }
                dbLog.writeAgentActivityLog(curUserId, "get lead", "lead", fresh.getLeadId(), "lead_status", fresh.getLeadStatus().toString());
               if (!StringUtils.isEmpty(leadType.getType())){
                    key = RedisHelper.createRedisKey(leadType.getType(), orgId, curUserId);
                    RedisHelper.saveRedis(stringRedisTemplate, key, fresh.getLeadId().toString(), new Date().toString());
                }
            }
        }
        logger.info("lead {} | {}", listCallings, sessionId);
        return TMSResponse.buildResponse(listCallings, listCallings.size());
    }

    private List<CLFresh> getUncallList(int userId, int orgId, int cpId, String callingListStr, int distributionType, String leadType, Integer agentSkillLevel) throws IOException {
//        GetGlobalParam globalDbParams = new GetGlobalParam();
//        globalDbParams.setOrgId(_curOrgId);
//        globalDbParams.setType(5);
//        globalDbParams.setName("get uncall lead by assign");
//        DBResponse<List<GetGlobalParamResp>> globalParams = freshService.getGlobalParam(SESSION_ID, globalDbParams);

        String globalUncallType = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 5, 2);

        int uncallType = 1;
        try {
            uncallType = Integer.parseInt(globalUncallType);
        } catch (Exception ex) {
            logger.error("Fail parse global uncall type to int: {}", globalUncallType, ex);
        }
        if (uncallType == 1) {
            logger.info("Get uncall is assigned");
            return getMyselfUncallList(userId, orgId, cpId, callingListStr, distributionType, uncallType, leadType, agentSkillLevel);
        }
        logger.info("Get uncall is unassigned");
        return getNewUncallList(userId, orgId, cpId, callingListStr, distributionType, uncallType, leadType, agentSkillLevel);
    }

    private List<CLFresh> getMyselfUncallList(int userId, int orgId, int cpId, String callingListStr, int distributionType, int uncallType, String leadType, Integer agentSkillLevel) throws IOException {
        GetUncallLeadV2 uncallParams = new GetUncallLeadV2();
        uncallParams.setOrgId(orgId);
        uncallParams.setAssigned(userId);
        uncallParams.setCpId(cpId);
        uncallParams.setLimit(Const.AGENT_CALLING_NUM);
        uncallParams.setCallinglistId(callingListStr);
        if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            uncallParams.setClType(leadType);
        }
        DBResponse<List<CLFresh>> uncallLeads = freshService.getUncallLeadV2(SESSION_ID, uncallParams);
        if (uncallLeads != null && !uncallLeads.getResult().isEmpty()) {
            leadService.updLeadsCrmActionType(SESSION_ID, uncallLeads.getResult(),
                    EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), userId);

            return uncallLeads.getResult();
        }
        boolean isRate = distributionType == EnumType.DISTRIBUTION_RULE.RATE.getValue();
        boolean isMaxUncall = RedisHelper.isMaxUncallByUser(stringRedisTemplate, orgId, userId);
        if (!isRate || !isMaxUncall) {
            return assignNewUncallLeads(userId, 0, cpId, callingListStr, distributionType, uncallType, leadType, orgId, agentSkillLevel);
        }
        return new ArrayList<CLFresh>();
    }

    private List<CLFresh> getNewUncallList(int userId, int orgId, int cpId, String callingListStr, int distributionType, int uncallType, String leadType, Integer agentSkillLevel) throws IOException {
        GetUncallLeadV2 uncallParams = new GetUncallLeadV2();
        uncallParams.setOrgId(orgId);
        uncallParams.setAssigned(userId);
        uncallParams.setCpId(cpId);
        uncallParams.setUserDefin03("1"); // get lead locked for user
        uncallParams.setLimit(Const.AGENT_CALLING_NUM);
        uncallParams.setCallinglistId(callingListStr);
        if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            uncallParams.setClType(leadType);
        }
        DBResponse<List<CLFresh>> uncallLeads = freshService.getUncallLeadV2(SESSION_ID, uncallParams);
        if (uncallLeads != null && !uncallLeads.getResult().isEmpty()) {
            leadService.updLeadsCrmActionType(SESSION_ID, uncallLeads.getResult(),
                    EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), userId);

            return uncallLeads.getResult();
        }
        // get new uncall (with assigned = 0)
        List<CLFresh> uncalls = assignNewUncallLeads(userId, 0, cpId, callingListStr, distributionType, uncallType, leadType, orgId, agentSkillLevel);
        if (!CollectionUtils.isEmpty(uncalls)) {
            return uncalls;
        }
        // get uncall from other agents (with assigned != 0)
        return assignNewUncallLeads(userId, -1, cpId, callingListStr, distributionType, uncallType, leadType, orgId, agentSkillLevel); // -1 means assigned != 0 in db
    }

    private List<CLFresh> getUncallListLifo(int userId, int orgId, int cpId, String callingListStr, int distributionType, String leadType, Integer agentSkillLevel) throws IOException {
        String globalUncallType = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 5, 2);

        int uncallType = 1;
        try {
            uncallType = Integer.parseInt(globalUncallType);
        } catch (Exception ex) {
            logger.error("Fail parse global uncall type to int: {}", globalUncallType, ex);
        }
        logger.info("Get uncallType: {}", uncallType);
        if (uncallType == 1) {
            return getMyselfUncallListLifo(userId, orgId, cpId, callingListStr, distributionType, uncallType, leadType, agentSkillLevel);
        }
        return getNewUncallListLifo(userId, orgId, cpId, callingListStr, distributionType, uncallType, leadType, agentSkillLevel);
    }

    private List<CLFresh> getNewUncallListLifo(int userId, int orgId, int cpId, String callingListStr, int distributionType, int uncallType, String leadType, Integer agentSkillLevel) throws IOException {
        GetUncallLeadLifo uncallParams = new GetUncallLeadLifo();
        uncallParams.setOrgId(orgId);
        uncallParams.setAssigned(userId);
        uncallParams.setCpId(cpId);
        uncallParams.setUserDefin03("1"); // get lead locked for user
        uncallParams.setLimit(Const.AGENT_CALLING_NUM);
        uncallParams.setCallinglistId(callingListStr);
        if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            uncallParams.setClType(leadType);
        }
        DBResponse<List<CLFresh>> uncallLeads = freshService.getUncallLeadLIFO(SESSION_ID, uncallParams);
        if (uncallLeads != null && !uncallLeads.getResult().isEmpty()) {
            leadService.updLeadsCrmActionType(SESSION_ID, uncallLeads.getResult(),
                    EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), userId);

            return uncallLeads.getResult();
        }
        // get new uncall (with assigned = 0)
        List<CLFresh> uncalls = assignNewUncallLeadsLifo(userId, 0, cpId, callingListStr, distributionType, uncallType, leadType, orgId, agentSkillLevel);
        if (!CollectionUtils.isEmpty(uncalls)) {
            return uncalls;
        }
        // get uncall from other agents (with assigned != 0)
        return assignNewUncallLeadsLifo(userId, -1, cpId, callingListStr, distributionType, uncallType, leadType, orgId, agentSkillLevel); // -1 means assigned != 0 in db
    }

    private List<CLFresh> getMyselfUncallListLifo(int userId, int orgId, int cpId, String callingListStr, int distributionType, int uncallType, String leadType, Integer agentSkillLevel) throws IOException {
        GetUncallLeadLifo uncallParams = new GetUncallLeadLifo();
        uncallParams.setOrgId(orgId);
        uncallParams.setAssigned(userId);
        uncallParams.setCpId(cpId);
        uncallParams.setLimit(Const.AGENT_CALLING_NUM);
        uncallParams.setCallinglistId(callingListStr);
        if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            uncallParams.setClType(leadType);
        }
        DBResponse<List<CLFresh>> uncallLeads = freshService.getUncallLeadLIFO(SESSION_ID, uncallParams);
        if (uncallLeads != null && !uncallLeads.getResult().isEmpty()) {
            return uncallLeads.getResult();
        }
        boolean isRate = distributionType == EnumType.DISTRIBUTION_RULE.RATE.getValue();
        boolean isMaxUncall = RedisHelper.isMaxUncallByUser(stringRedisTemplate, orgId, userId);
        if (!isRate || !isMaxUncall) {
            return assignNewUncallLeadsLifo(userId, 0, cpId, callingListStr, distributionType, uncallType, leadType, orgId, agentSkillLevel);
        }
        return new ArrayList<CLFresh>();
    }

    private List<CLFresh> getUncallListME(int userId, int orgId, int cpId, String callingListStr, int distributionType, String leadType, Integer agentSkillLevel) throws IOException {
        int uncallType = 2;
        String leadLockedForUser = "1";
        int defaultMaxAttempt = 10;

        // Get uncall agent assigned
        GetUncallMe uncallParams = GetUncallMe.builder()
                .orgId(orgId)
                .assigned(userId)
                .cpId(cpId)
                .userDefin03(leadLockedForUser)
                .limit(Const.AGENT_CALLING_NUM)
                .callinglistId(callingListStr)
                .maxAttempt(defaultMaxAttempt)
                .build();
        List<CLFresh> uncallLeads = freshService.getUncallLeadMe(SESSION_ID, uncallParams).getResult();
        if (!CollectionUtils.isEmpty(uncallLeads)) {
            leadService.updLeadsCrmActionType(SESSION_ID, uncallLeads, EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), userId);

            return uncallLeads;
        }

        // Get uncall free (assigned = 0)
        uncallLeads = assignNewUncallLeadsMe(userId, 0, cpId, callingListStr, uncallType, orgId, agentSkillLevel, defaultMaxAttempt);
        if (!CollectionUtils.isEmpty(uncallLeads)) {
            return uncallLeads;
        }

        // Get uncall from other agents (assigned != 0)
        // -1 means assigned != 0 in db
        return assignNewUncallLeadsMe(userId, -1, cpId, callingListStr, uncallType, orgId, agentSkillLevel, defaultMaxAttempt);
    }

    private List<CLCallback> getOrAssignCallback(int agentId, int cpId, int orgId, String callingListStr, int distributionType, String leadType, Integer agentSkillLevel) throws IOException {
        int callbackNum = Const.CALLBACK_NUM;
        // check if have an existing callback
        logger.info("getOrAssignCallback {}|{}", agentId, cpId);
        GetCLCallback getCurrCbParams = new GetCLCallback();
        getCurrCbParams.setAssigned(agentId);
        getCurrCbParams.setOrgId(orgId);
//        getCurrCbParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
        getCurrCbParams.setLimit(callbackNum);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -(Const.BEFORE_CALLBACK_TIME));
        if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            getCurrCbParams.setLeadType(leadType);
        }
        List<CLCallback> currCallbacks = callBackService.getCallback(SESSION_ID, getCurrCbParams).getResult();
        if (currCallbacks == null || currCallbacks.isEmpty()) { // find new callback
            String pattern = "yyyyMMdd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            GetCallbackByTimeParamsV8 getNewCbParams = new GetCallbackByTimeParamsV8();
            logger.info("**************** {}", now.getTime());
            getNewCbParams.setFromRequestTime(simpleDateFormat.format(now.getTime()));
            getNewCbParams.setLimit(callbackNum + bufferLeadNum);
            getNewCbParams.setOrgId(orgId);
            getNewCbParams.setCpId(cpId);
            getNewCbParams.setSkillLevel(agentSkillLevel);
            if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
                getCurrCbParams.setLeadType(leadType);
            }
            List<CLCallback> newCallbacks = callBackService.getCallbackByTimeV8(SESSION_ID, getNewCbParams).getResult();
            if (newCallbacks != null && !newCallbacks.isEmpty()) {
                return assignCallBackLead(agentId, cpId, callbackNum, newCallbacks);
            }
            return new ArrayList<CLCallback>();
        } else {//check xem co phai thoi gian goi lai khong?
            now = Calendar.getInstance();
            now.add(Calendar.MINUTE, (Const.BEFORE_CALLBACK_TIME));
            List<CLCallback> validCallbacks = new ArrayList<CLCallback>();
            for (CLCallback clCallback : currCallbacks) {
                if (now.getTime().after(clCallback.getRequestTime())) {
                    validCallbacks.add(clCallback);
                }
            }
            logger.info("Run into this line ++++++++++++++++++++++++++++++");
            return validCallbacks;
        }
    }

    private List<CLCallback> getCallbackOrderByLeadStatus(int agentId, int cpId, int orgId, String callingListStr, int distributionType, String leadType) throws IOException {
        int callbackNum = Const.CALLBACK_NUM;
        // check if have an existing callback
        logger.info("getCallbackOrderByLeadStatus {}|{}", agentId, cpId);
        GetCallbackByTimeParams getCurrCbParams = new GetCallbackByTimeParams();
        getCurrCbParams.setAssigned(agentId);
        getCurrCbParams.setOrgId(orgId);
//        getCurrCbParams.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
        getCurrCbParams.setLimit(callbackNum);
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -(Const.BEFORE_CALLBACK_TIME));
        if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            getCurrCbParams.setLeadType(leadType);
        }
        //danh sach callback cua agent
        List<CLCallback> currCallbacks = callBackService.getCallbackOrderByLeadStatus(SESSION_ID, getCurrCbParams).getResult();
        //agent ko co callback nao, tim danh sach callback chua assign cho ai
        if (currCallbacks == null || currCallbacks.isEmpty()) { // find new callback
            String pattern = "yyyyMMdd HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            GetCallbackByTimeParams getNewCbParams = new GetCallbackByTimeParams();
            logger.info("**************** {}", now.getTime());
            getNewCbParams.setFromRequestTime(simpleDateFormat.format(now.getTime()));
            getNewCbParams.setLimit(callbackNum + bufferLeadNum);
            getNewCbParams.setOrgId(orgId);
            getNewCbParams.setCpId(cpId);
            if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
                getCurrCbParams.setLeadType(leadType);
            }
            List<CLCallback> newCallbacks = callBackService.getCallbackOrderByLeadStatus(SESSION_ID, getNewCbParams).getResult();
            if (newCallbacks != null && !newCallbacks.isEmpty()) {//chua assign
                return newCallbacks;
            }
            return new ArrayList<CLCallback>();
        } else {//check xem co phai thoi gian goi lai khong?
            now = Calendar.getInstance();
            now.add(Calendar.MINUTE, (Const.BEFORE_CALLBACK_TIME));
            List<CLCallback> validCallbacks = new ArrayList<CLCallback>();
            for (CLCallback clCallback : currCallbacks) {
                if (now.getTime().after(clCallback.getRequestTime())) {
                    validCallbacks.add(clCallback);
                }
            }
            logger.info("Run into this line ++++++++++++++++++++++++++++++");
            return validCallbacks;
        }
    }

    private List<CLCallback> assignCallBackLeadStatus(int agentId, int cpId, int leadNum, List<CLCallback> callbackList) throws IOException {
        int count = 0;
        List<CLCallback> assignedLeads = new ArrayList<CLCallback>();
        for (CLCallback callback : callbackList) {
            if (count == leadNum) {
                break;
            }
            if (!"0".equals(callback.getAssigned().trim()) && String.valueOf(agentId).equals(callback.getAssigned())) {//callback da duoc asign cho chinh agent thi ko assign lai
                count++;
                assignedLeads.add(callback);
                continue;
            }

            UpdateCLCallbackByAssigned updCallbackByAssigned = new UpdateCLCallbackByAssigned();
            updCallbackByAssigned.setLeadId(callback.getLeadId());
            updCallbackByAssigned.setAssigned(agentId);
            updCallbackByAssigned.setCpId(cpId);
            updCallbackByAssigned.setConditionAssigned(0);
            DBResponse result = logService.updateCallbackByAssigned(SESSION_ID, updCallbackByAssigned);
            int affectedRows = Integer.parseInt(result.getErrorMsg());
            if (affectedRows > 0) {
                count++;
                assignedLeads.add(callback);
            }
        }
        return assignedLeads;
    }


    private List<CLCallback> assignCallBackLead(int agentId, int cpId, int leadNum, List<CLCallback> callbackList) throws IOException {
        int count = 0;
        List<CLCallback> assignedLeads = new ArrayList<CLCallback>();
        for (CLCallback callback : callbackList) {
            if (count == leadNum) {
                break;
            }

            UpdateCLCallbackByAssigned updCallbackByAssigned = new UpdateCLCallbackByAssigned();
            updCallbackByAssigned.setLeadId(callback.getLeadId());
            updCallbackByAssigned.setAssigned(agentId);
            updCallbackByAssigned.setCpId(cpId);
            updCallbackByAssigned.setConditionAssigned(0);
            DBResponse result = logService.updateCallbackByAssigned(SESSION_ID, updCallbackByAssigned);
            int affectedRows = Integer.parseInt(result.getErrorMsg());
            if (affectedRows > 0) {
                count++;
                assignedLeads.add(callback);
            }
        }
        return assignedLeads;
    }

    private List<CLFresh> assignFreshLead(int agentId, int cpId, int leadNum, List<CLFresh> freshList, int orgId) throws IOException {
        int count = 0;
        List<CLFresh> assignedLeads = new ArrayList<CLFresh>();
        for (CLFresh fresh : freshList) {
            if (count == leadNum) {
                break;
            }

            UpdLeadByAssignedV3 updLeadByAssigned = new UpdLeadByAssignedV3();
            updLeadByAssigned.setLeadId(fresh.getLeadId());
            updLeadByAssigned.setAssigned(agentId);
            updLeadByAssigned.setCpId(cpId);
            updLeadByAssigned.setConditionAssigned(0);
            updLeadByAssigned.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
            updLeadByAssigned.setModifyby(agentId);
            updLeadByAssigned.setTeam(autoReloadConfigTeam.getConfigTeam(agentId));
            updLeadByAssigned.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentId));
            DBResponse<?> result = logService.updLeadByAssignedV3(SESSION_ID, updLeadByAssigned);
            int affectedRows = Integer.parseInt(result.getErrorMsg());
            if (affectedRows > 0) {
                count++;
                assignedLeads.add(fresh);

                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, String.valueOf(_curUser.getUserId()));
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                RedisHelper.saveRedisRecent(orgId, _curUser.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
                RedisHelper.checkDuplicateAssignToAgent(stringRedisTemplate, fresh.getOrgId(), fresh.getProdName(), fresh.getName(), fresh.getPhone(), fresh.getLeadId(), agentId);
            }
        }
        return assignedLeads;
    }


    private List<CLFresh> getOrAssignUrgentLead(int agentId, int cpId, int orgId, Integer agentSkillLevel) {
        // get current urgent leads
        GetLeadParamsV4 currLeadParams = new GetLeadParamsV4();
        currLeadParams.setAssigned(agentId);
        currLeadParams.setOrgId(orgId);
        currLeadParams.setLeadStatus(EnumType.LEAD_STATUS.URGENT.getValue());
        currLeadParams.setLimit(Const.AGENT_CALLING_NUM);
        currLeadParams.setCpId(cpId);
        List<CLFresh> urgentLeads = freshService.getLeadV4(SESSION_ID, currLeadParams).getResult();
        if (urgentLeads != null && !urgentLeads.isEmpty()) {
            leadService.updLeadsCrmActionType(SESSION_ID, urgentLeads,
                    EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue(), agentId);
            return urgentLeads;
        }

        // get new urgent leads
        int leadNum = Const.AGENT_CALLING_NUM;
        currLeadParams.setAssigned(0);
        currLeadParams.setLimit(leadNum + bufferLeadNum);
        currLeadParams.setCpId(cpId);
        currLeadParams.setOrgId(orgId);
        currLeadParams.setSkillLevel(agentSkillLevel);
//        currLeadParams.setCallinglistId(callingListStr);
        List<CLFresh> newUrgentLeads = freshService.getLeadV4(SESSION_ID, currLeadParams).getResult();
        int count = 0;
        List<CLFresh> assignedLeads = new ArrayList<CLFresh>();
        for (CLFresh fresh : newUrgentLeads) {
            if (count == leadNum) {
                break;
            }

            UpdLeadByAssignedV3 updLeadByAssigned = new UpdLeadByAssignedV3();
            updLeadByAssigned.setLeadId(fresh.getLeadId());
            updLeadByAssigned.setAssigned(agentId);
            updLeadByAssigned.setCpId(cpId);
            updLeadByAssigned.setConditionAssigned(0);
            updLeadByAssigned.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
            updLeadByAssigned.setTeam(autoReloadConfigTeam.getConfigTeam(agentId));
            updLeadByAssigned.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentId));
            DBResponse<?> result = logService.updLeadByAssignedV3(SESSION_ID, updLeadByAssigned);
            int affectedRows = Integer.parseInt(result.getErrorMsg());
            if (affectedRows > 0) {
                count++;
                assignedLeads.add(fresh);
                //RedisHelper.saveRedis(stringRedisTemplate, );
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, String.valueOf(_curUser.getUserId()));
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");

                RedisHelper.saveRedisRecent(orgId, _curUser.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);

            }
        }
        return assignedLeads;
    }

    private List<CLFresh> assignNewUncallLeads(int agentId, int assigned, int cpId, String callingListStr,
                                               int distributionType, int uncallType, String leadType, int orgId, Integer agentSkillLevel) throws IOException {
        List<CLFresh> assignedLeads = new ArrayList<CLFresh>();
        // get new uncall leads
        int leadNum = Const.AGENT_CALLING_NUM;
        GetUncallLeadV2 uncallParams = new GetUncallLeadV2();
        uncallParams.setOrgId(orgId);
        uncallParams.setAssigned(assigned);
        uncallParams.setCpId(cpId);
        uncallParams.setLimit(leadNum + bufferLeadNum);
        uncallParams.setCallinglistId(callingListStr);
        uncallParams.setSkillLevel(agentSkillLevel);
        if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            uncallParams.setClType(leadType);
        }
        DBResponse<List<CLFresh>> uncallLeads = freshService.getUncallLeadV2(SESSION_ID, uncallParams);
        if (uncallLeads == null || uncallLeads.getResult().isEmpty()) {
            return assignedLeads;
        }

        int count = 0;
        for (CLFresh fresh : uncallLeads.getResult()) {
            if (count == leadNum) {
                break;
            }

            UpdLeadByAssignedV3 updLeadByAssigned = new UpdLeadByAssignedV3();
            updLeadByAssigned.setLeadId(fresh.getLeadId());
            updLeadByAssigned.setAssigned(agentId);
            updLeadByAssigned.setCpId(cpId);
            updLeadByAssigned.setConditionAssigned(fresh.getAssigned());
            if (uncallType == 2) {
                updLeadByAssigned.setUserDefin03("1"); // get lead locked for user
            }
            updLeadByAssigned.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
            updLeadByAssigned.setTeam(autoReloadConfigTeam.getConfigTeam(agentId));
            updLeadByAssigned.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentId));
            DBResponse<?> result = logService.updLeadByAssignedV3(SESSION_ID, updLeadByAssigned);
            int affectedRows = Integer.parseInt(result.getErrorMsg());
            if (affectedRows > 0) {
                count++;
                assignedLeads.add(fresh);
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, String.valueOf(_curUser.getUserId()));
                logger.info("SAVE REDIS KEY: {}", key);
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                logger.info("SAVE OK");

                RedisHelper.saveRedisRecent(orgId, _curUser.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
            }
        }
        return assignedLeads;
    }

    private List<CLFresh> assignNewUncallLeadsMe(int agentId, int assigned, int cpId, String callingListStr,
                                                 int uncallType, int orgId, Integer agentSkillLevel, int maxAttempt) {
        List<CLFresh> assignedLeads = new ArrayList<>();
        // Get new uncall leads
        int leadNum = Const.AGENT_CALLING_NUM;
        GetUncallMe uncallParams = GetUncallMe.builder()
                .orgId(orgId)
                .assigned(assigned)
                .cpId(cpId)
                .limit(Const.AGENT_CALLING_NUM + bufferLeadNum)
                .callinglistId(callingListStr)
                .skillLevel(agentSkillLevel)
                .maxAttempt(maxAttempt)
                .build();
        List<CLFresh> uncallLeads = freshService.getUncallLeadMe(SESSION_ID, uncallParams).getResult();

        if (CollectionUtils.isEmpty(uncallLeads)) {
            return assignedLeads;
        }

        int count = 0;
        for (CLFresh fresh : uncallLeads) {
            if (count == leadNum) {
                break;
            }

            UpdLeadByAssignedV3 updLeadByAssigned = new UpdLeadByAssignedV3();
            updLeadByAssigned.setLeadId(fresh.getLeadId());
            updLeadByAssigned.setAssigned(agentId);
            updLeadByAssigned.setCpId(cpId);
            updLeadByAssigned.setConditionAssigned(fresh.getAssigned());
            if (uncallType == 2) {
                updLeadByAssigned.setUserDefin03("1"); // get lead locked for user
            }
            updLeadByAssigned.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
            updLeadByAssigned.setTeam(autoReloadConfigTeam.getConfigTeam(agentId));
            updLeadByAssigned.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentId));
            DBResponse<?> result = logService.updLeadByAssignedV3(SESSION_ID, updLeadByAssigned);
            int affectedRows = Integer.parseInt(result.getErrorMsg());
            if (affectedRows > 0) {
                count++;
                assignedLeads.add(fresh);
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, String.valueOf(_curUser.getUserId()));
                logger.info("SAVE REDIS KEY: {}", key);
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                logger.info("SAVE OK");

                RedisHelper.saveRedisRecent(orgId, _curUser.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
            }
        }
        return assignedLeads;
    }

    private List<CLFresh> assignNewUncallLeadsLifo(int agentId, int assigned, int cpId, String callingListStr,
                                                   int distributionType, int uncallType, String leadType, int orgId, Integer agentSkillLevel) throws IOException {
        List<CLFresh> assignedLeads = new ArrayList<CLFresh>();
        // get new uncall leads
        int leadNum = Const.AGENT_CALLING_NUM;
        GetUncallLeadLifo uncallParams = new GetUncallLeadLifo();
        uncallParams.setOrgId(orgId);
        uncallParams.setAssigned(assigned);
        uncallParams.setCpId(cpId);
        uncallParams.setLimit(leadNum + bufferLeadNum);
        uncallParams.setCallinglistId(callingListStr);
        uncallParams.setSkillLevel(agentSkillLevel);
        if (distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue()) {
            uncallParams.setClType(leadType);
        }
        DBResponse<List<CLFresh>> uncallLeads = freshService.getUncallLeadLIFO(SESSION_ID, uncallParams);
        if (uncallLeads == null || uncallLeads.getResult().isEmpty()) {
            return assignedLeads;
        }

        int count = 0;
        for (CLFresh fresh : uncallLeads.getResult()) {
            if (count == leadNum) {
                break;
            }

            UpdLeadByAssignedV3 updLeadByAssigned = new UpdLeadByAssignedV3();
            updLeadByAssigned.setLeadId(fresh.getLeadId());
            updLeadByAssigned.setAssigned(agentId);
            updLeadByAssigned.setCpId(cpId);
            updLeadByAssigned.setConditionAssigned(fresh.getAssigned());
            if (uncallType == 2) {
                updLeadByAssigned.setUserDefin03("1"); // get lead locked for user
            }
            updLeadByAssigned.setCrmActionType(EnumType.CRM_ACTION_TYPE.GET_LEAD.getValue());
            updLeadByAssigned.setTeam(autoReloadConfigTeam.getConfigTeam(agentId));
            updLeadByAssigned.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(agentId));
            DBResponse<?> result = logService.updLeadByAssignedV3(SESSION_ID, updLeadByAssigned);
            int affectedRows = Integer.parseInt(result.getErrorMsg());
            if (affectedRows > 0) {
                count++;
                assignedLeads.add(fresh);
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, String.valueOf(_curUser.getUserId()));
                logger.info("SAVE REDIS KEY: {}", key);
                RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(fresh.getLeadId()), "1");
                logger.info("SAVE OK");

                RedisHelper.saveRedisRecent(orgId, _curUser.getUserId(), fresh.getLeadId(), USER_EXPIRE_TIME, stringRedisTemplate);
            }
        }
        return assignedLeads;
    }

    @PostMapping("/changepass")
    public TMSResponse changepass_process(@RequestParam("password") String
                                                  password, @RequestParam("oldpassword") String oldPassword) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrUser> orUsers = agentManagerment.getUserByName(userName);
        logger.info(orUsers.toString());
        if (orUsers != null || !orUsers.isEmpty()) {
            OrUser orUser = orUsers.get(0);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(oldPassword, orUser.getPassword())) {
                orUser.setPassword(passwordEncoder.encode(password));
                orUser.setFailed_login_count(0);
                orUser.setUser_lock(0);
                agentManagerment.saveUser(orUser);
                return TMSResponse.buildResponse(null, 0, "Change password successful", 200);
            } else {
                return TMSResponse.buildResponse(null, 0, "Old password not found", 400);
            }
        }
        return TMSResponse.buildResponse(null, 0, "User not found", 400);
    }

    @GetMapping("/resetpass")
    public TMSResponse resetpass_process(@RequestParam("userId") Integer userId) throws MessagingException, IOException {
        if (_curUser.getUserType().equals(Const.USER_MANAGER_TYPE) || _curUser.getUserType().equals(Const.USER_ADMIN_TYPE)) {
            List<OrUser> orUsers = agentManagerment.getUserById(userId);
            logger.info(orUsers.toString());
            try {
                if (orUsers != null || !orUsers.isEmpty()) {
                    OrUser orUser = orUsers.get(0);
                    String new_pass = alphaNumericString(8);
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    orUser.setPassword(passwordEncoder.encode(new_pass));
                    orUser.setForce_change_password(true);
                    orUser.setFailed_login_count(0);
                    orUser.setUser_lock(0);
                    agentManagerment.saveUser(orUser);
                    String email = orUser.getEmail();
                    try {
                        if (email != null || !email.equals("")) {
                            logger.info("Start send email");
                            sendEmail(email, new_pass, orUser.getUser_name());
                            return TMSResponse.buildResponse(null, 0, "Reset password is successful, please check your email to get new password", 200);
                        }
                    } catch (Exception e) {
                        return TMSResponse.buildResponse(null, 0, orUser.getUser_name() + "'s  email not found", 400);
                    }
                }
            } catch (Exception e) {
                return TMSResponse.buildResponse(null, 0, "User not found", 400);
            }
        }
        return TMSResponse.buildResponse(null, 0, "Only Manager or Admin can reset password", 400);
    }

    private String alphaNumericString(int len) {
        String AB = "0123456789abcdefghiklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    private void sendEmail(String email, String password, String userName) throws MessagingException, IOException {

        String html = readContentFromFile(fileResetEmail);
        html = html.replace("**Uyen**", userName);
        html = html.replace("**newPass**", password);
        MimeMessage msg = javaMailSender.createMimeMessage();
        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(new String[]{email, "uyendn@tmssolutions.net"});
        helper.setFrom("<no-reply@tmssolutions.net>");
        helper.setSubject("TMS reset password");
        helper.setText(html, true);
        logger.info("Create email ok");
        javaMailSender.send(msg);
        logger.info("Send email");

    }

    private String readContentFromFile(String fileName) {
        StringBuffer contents = new StringBuffer();

        try {
            //use buffering, reading one line at a time
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                reader.close();
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return contents.toString();
    }

    @PostMapping
    public TMSResponse createUser(@RequestBody OrUser input) throws TMSException {
        if (input.getPhone() != null && !Helper.isNumeric(input.getPhone())) {
            return TMSResponse.buildResponse(null, 0, "Phone number is invalid", 400);
        }
        if (input.getUser_type() == null || input.getUser_type().equals("")) {
            return TMSResponse.buildResponse(null, 0, "User type can not be null", 400);
        }
        if (input.getUser_id() != null) {
            return TMSResponse.buildResponse(null, 0, "User Id must be null when create user", 400);
        }
        if (_curUser.getUserType().equals(Const.USER_MANAGER_TYPE) || _curUser.getUserType().equals(Const.USER_ADMIN_TYPE)) {
            int quota_user = this.getQuotarNumber();
            if (quota_user != 0) {
                List<OrUser> userList = agentManagerment.getUserByOrgId(getCurOrgId());
                logger.info("{}|{}", quota_user, userList.size());
                if (userList.size() < quota_user) {
                    OrUser params = modelMapper.map(input, OrUser.class);
                    params.setOrg_id(getCurOrgId());
                    params.setModifyby(_curUser.getUserId());
                    params.setModifydate(DateHelper.nowToTimestamp());
                    if (input.getPassword() != null) {
                        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                        params.setPassword(passwordEncoder.encode(input.getPassword()));
                    }
                    if (input.getPhone() != null) {
                        params.setPhone("SIP/" + input.getPhone());
                    }
                    params.setForce_change_password(true);
                    OrUser user = agentManagerment.saveUser(params);
                    List<OrRole> role = agentManagerment.getRoleByName(input.getUser_type());
                    if (role.isEmpty()) {
                        agentManagerment.deleteById(user.getUser_id());
                        return TMSResponse.buildResponse(null, 0, "User type is not found,  please contact System Administrator", 400);
                    }
                    Integer roleId = role.get(0).getRoleId();
                    OrUserRole userRole = new OrUserRole();
                    userRole.setRoleId(roleId);
                    userRole.setModifyby(_curUser.getUserId());
                    userRole.setModifydate(DateHelper.nowToTimestamp());
                    userRole.setUserId(user.getUser_id());
                    agentManagerment.saveUserRole(userRole);
                    return TMSResponse.buildResponse(null, 0, "Create user is successful", 200);
                }
                return TMSResponse.buildResponse(null, 0, "Number of users is limited, please send request to System Administrator", 400);
            }
            return TMSResponse.buildResponse(null, 0, "Quota user value is not found", 400);
        }
        return TMSResponse.buildResponse(null, 0, "Only Manager or Admin can create user", 400);
    }

    @PutMapping
    public TMSResponse updateUser(@RequestBody OrUser input) throws TMSException {
        if (input.getPhone() != null && !Helper.isNumeric(input.getPhone())) {
            return TMSResponse.buildResponse(null, 0, "Phone number is invalid", 400);
        }
        if (input.getUser_id() == null) {
            return TMSResponse.buildResponse(null, 0, "User id can not be null", 400);
        }
        logger.info(_curUser.getUserType());
        if (_curUser.getUserType().equals(Const.USER_MANAGER_TYPE) || _curUser.getUserType().equals(Const.USER_ADMIN_TYPE)) {
            List<OrUser> orUsers = agentManagerment.getUserById(input.getUser_id());
            if (orUsers != null || !orUsers.isEmpty()) {
                OrUser orUser = orUsers.get(0);
                if (input.getUser_type() != null) {
                    orUser.setUser_type(input.getUser_type());
                    List<OrRole> roles = agentManagerment.getRoleByName(input.getUser_type());
                    if (roles.isEmpty()) {
                        return TMSResponse.buildResponse(null, 0, "User type is not found", 400);
                    }
                    OrUserRole userRole = agentManagerment.getUserRoleByUserId(orUser.getUser_id());
                    userRole.setRoleId(roles.get(0).getRoleId());
                    userRole.setModifyby(_curUser.getUserId());
                    userRole.setModifydate(DateHelper.nowToTimestamp());
                    agentManagerment.saveUserRole(userRole);
                }
                if (input.getUser_name() != null) {
                    orUser.setUser_name(input.getUser_name());
                }
                if (input.getEmail() != null) {
                    orUser.setEmail(input.getEmail());
                }
                if (input.getBirthday() != null) {
                    orUser.setBirthday(input.getBirthday());
                }
                if (input.getFullname() != null) {
                    orUser.setFullname(input.getFullname());
                }
                if (input.getPhone() != null) {
                    orUser.setPhone("SIP/" + input.getPhone());
                }
                if (input.getUser_lock() != null) {
                    orUser.setUser_lock(input.getUser_lock());
                }
                if (input.getHome_phone_1() != null) {
                    orUser.setHome_phone_1(input.getHome_phone_1());
                }
                if (input.getHome_phone_2() != null) {
                    orUser.setHome_phone_2(input.getHome_phone_2());
                }
                if (input.getPersonal_mail() != null) {
                    orUser.setPersonal_mail(input.getPersonal_mail());
                }
                if (input.getPersonal_phone_1() != null) {
                    orUser.setPersonal_phone_1(input.getPersonal_phone_1());
                }
                if (input.getPersonal_phone_2() != null) {
                    orUser.setPersonal_phone_2(input.getPersonal_phone_2());
                }
                if (input.getHome_address() != null) {
                    orUser.setHome_address(input.getHome_address());
                }
                if (input.getWork_mail() != null) {
                    orUser.setWork_mail(input.getWork_mail());
                }
                if (input.getChat_id() != null) {
                    orUser.setChat_id(input.getChat_id());
                }
                orUser.setModifyby(_curUser.getUserId());
                orUser.setModifydate(DateHelper.nowToTimestamp());
                agentManagerment.saveUser(orUser);
                return TMSResponse.buildResponse(null, 0, "Update user is successful", 200);

            }
            return TMSResponse.buildResponse(null, 0, "User id is not found", 400);
        }
        return TMSResponse.buildResponse(null, 0, "Only Manager or Admin can update user", 400);
    }

    @DeleteMapping("/{userId}")
    public TMSResponse<?> deleteUser(@PathVariable Integer userId) {
        logger.info("Delete user with userId {}", userId);
        if (_curUser.getUserType().equals(Const.USER_MANAGER_TYPE) || _curUser.getUserType().equals(Const.USER_ADMIN_TYPE)) {
            List<OrUser> users = agentManagerment.getUserById(userId);
            try {
                OrUserDelete delete = modelMapper.map(users.get(0), OrUserDelete.class);
                agentManagerment.saveUserDelete(delete);
                agentManagerment.deleteUserRoleByUserId(userId);
                agentManagerment.deleteById(userId);
                return TMSResponse.buildResponse(null, 0, "Delete user is successful", 200);
            } catch (Exception e) {
                return TMSResponse.buildResponse(null, 0, "User id not found", 400);
            }
        }
        return TMSResponse.buildResponse(null, 0, "Only Manager or Admin can delete user", 400);
    }

    @PostMapping("/{userId}/{userLock}")
    public TMSResponse<?> activeUser(@PathVariable Integer userId, @PathVariable String userLock) {
        logger.info("Active user with userId: {}", userId);
        if (_curUser.getUserType().equals(Const.USER_MANAGER_TYPE) || _curUser.getUserType().equals(Const.USER_ADMIN_TYPE)) {
            List<OrUser> users = agentManagerment.getUserById(userId);
            if (!users.isEmpty()) {
                OrUser orUser = users.get(0);
                if (userLock.equals("active")) {
                    orUser.setUser_lock(0);
                } else if (userLock.equals("deactive")) {
                    orUser.setUser_lock(1);
                }
                agentManagerment.saveUser(orUser);
                return TMSResponse.buildResponse(null, 0, "User " + orUser.getUser_name() + " is " + userLock, 200);
            }
            return TMSResponse.buildResponse(null, 0, "User id not found", 400);

        }
        return TMSResponse.buildResponse(null, 0, "Only Manager or Admin can delete user", 400);
    }

    @GetMapping("/quota")
    public TMSResponse getQuota() {
        Integer quota = this.getQuotarNumber();
        if (quota != 0) {
            return TMSResponse.buildResponse(null, quota, "ok", 200);
        }
        return TMSResponse.buildResponse(null, 0, "not found quota", 400);
    }

    private Integer getQuotarNumber() {
        GetGlobalParamV2 globalParamV2 = new GetGlobalParamV2();
        globalParamV2.setOrgId(getCurOrgId());
        globalParamV2.setType(8);
        globalParamV2.setParamId(1);
        DBResponse<List<GetGlobalParamResp>> dbResponse = freshService.getGlobalParamV2(SESSION_ID, globalParamV2);
        try {
            int quota = Integer.parseInt(dbResponse.getResult().get(0).getValue());
            return quota;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
        }
//        return null;
    }

    @GetMapping("/userType")
    public TMSResponse getListUserType() {
        try {
            List<OrRole> userTypes = agentManagerment.getUserType();
            List<UserType> userTypeList = new ArrayList<>();
            for (OrRole role : userTypes) {
                UserType type = new UserType();
                type.setId(role.getRoleId());
                type.setName(role.getName());
                userTypeList.add(type);
            }
            return TMSResponse.buildResponse(userTypeList, userTypeList.size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return TMSResponse.buildResponse(null, 0);
        }
    }

    @GetMapping("/by-id/{agentId}/activity/instant")
    public TMSResponse<?> getAgentActivity(@PathVariable Integer agentId, @RequestParam Integer activityId) {
        LogAgentTrace data = new LogAgentTrace();
        try {
            String redisKey = RedisHelper.createLogAgentTraceRedisKey(orgId, Const.LOG_AGENT_STATE, agentId);
            data = logAgentTraceService.getLatestActivity(agentId, activityId, redisKey, Const.LOG_AGENT_LAST_CHECKING_MINUTES);
            if (data !=null && data.getFlagCode() != null && data.getFlagCode() != 0) {
                return TMSResponse.buildResponse(data, 0, String.format(Const.LOG_AGENT_REQUEST_STATE, data.getFlagValue()), Const.LOG_AGENT_CODE_SUCCESS);
            }
        }  catch (Exception e) {
            logger.error(e.getMessage(), e);
            return TMSResponse.buildResponse(data, 0, Const.LOG_AGENT_ERROR_INTERNAL_SERVER, Const.LOG_AGENT_CODE_FAILED);
        }
        return TMSResponse.buildResponse(data, 0, "", Const.LOG_AGENT_CODE_SUCCESS);
    }

    @PostMapping("/by-id/{agentId}/activity")
    public TMSResponse<?> updateAgentActivity(@PathVariable Integer agentId, @RequestBody LogAgentTrace agentTrace) throws TMSException {
        try {
            List<OrUser> agents = agentManagerment.getUserById(agentId);
            if (CollectionUtils.isEmpty(agents)) {
                return TMSResponse.buildResponse(null, 0, Const.LOG_AGENT_ERROR_NO_AGENT, Const.LOG_AGENT_CODE_SUCCESS);
            }
            agentTrace.setActionTime(new Date());
            agentTrace.setAgentId(agentId);
            OrUser agent = agents.get(0);
            Integer activityId = agentTrace.getActivityId();
            String redisKey = RedisHelper.createLogAgentTraceRedisKey(agent.getOrg_id(), activityId, agentId);
            LogAgentTrace validatedAgentTrace = agentTrace;
            if (activityId.equals(Const.LOG_AGENT_STATE)) {
                validatedAgentTrace = logAgentTraceService.validateAgentState(agentTrace, redisKey);
            }
            if (validatedAgentTrace != null) {
                agentTrace = logAgentTraceService.logActivity(validatedAgentTrace, redisKey);
                if (validatedAgentTrace.getTemporary() != null && validatedAgentTrace.getTemporary()) {
                    return TMSResponse.buildResponse(logAgentTraceService.convertTemporaryStateRequest(agentTrace), 0, String.format(Const.LOG_AGENT_REQUEST_STATE, agentTrace.getFlagValue()), Const.LOG_AGENT_CODE_SUCCESS);
                }
            } else {
                return TMSResponse.buildResponse(agentTrace, 0, Const.LOG_AGENT_ERROR_NOT_ALLOWED, Const.LOG_AGENT_CODE_FAILED);
            }
        }  catch (Exception e) {
            logger.error(e.getMessage());
            return TMSResponse.buildResponse(agentTrace, 0, Const.LOG_AGENT_ERROR_INTERNAL_SERVER, Const.LOG_AGENT_CODE_FAILED);
        }
        return TMSResponse.buildResponse(agentTrace, 0, "", Const.LOG_AGENT_CODE_SUCCESS);
    }
}
