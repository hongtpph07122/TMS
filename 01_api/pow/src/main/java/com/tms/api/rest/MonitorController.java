package com.tms.api.rest;

import com.tms.api.dto.AgentMonitorDto;
import com.tms.api.dto.CampaignMonitorDto;
import com.tms.api.dto.dashboard.AgentRateMonitorDto;
import com.tms.api.dto.dashboard.AgentRateMonitorResponseDto;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.Helper;
import com.tms.api.helper.RedisHelper;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.CampaignService;
import com.tms.dto.DBResponse;
import com.tms.dto.GetCampaign;
import com.tms.dto.GetUserParams;
import com.tms.entity.CPCampaign;
import com.tms.entity.User;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/monitor")
@SuppressWarnings("unchecked")
public class MonitorController extends BaseController {

    private final UserService userService;
    private final CLFreshService freshService;
    private final StringRedisTemplate stringRedisTemplate;
    private final CampaignService campaignService;

    @Autowired
    public MonitorController(UserService userService,
                             CLFreshService freshService,
                             StringRedisTemplate stringRedisTemplate,
                             CampaignService campaignService) {
        this.userService = userService;
        this.freshService = freshService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.campaignService = campaignService;
    }


    @GetMapping("/agent")
    public TMSResponse<List<AgentMonitorDto>> agentMonitor(AgentMonitorDto agentMonitorDto) throws TMSException {
        List<AgentMonitorDto> lstAgMonitor = new ArrayList<>();
        DBResponse<List<User>> lstAgent = null;

        try {
            GetUserParams userParams = new GetUserParams();
            userParams.setOrgId(getCurrentOriganationId());
            lstAgent = userService.getUser(SESSION_ID, userParams);

            List<User> lstUser = lstAgent.getResult();
            for (User user : lstUser){
                AgentMonitorDto agent = new AgentMonitorDto();
                agent.setAgentName(user.getUserName());
                agent.setAgentStatus(EnumType.AGENT_MONITOR_STATUS.OFFLINE.toString());
                //agent.getAgentStatusId(2);
                //agent.getDuration()
                agent.setPhoneExt(Const.phoneExts[Helper.getRandomNumberInRange(0, Const.phoneExts.length - 1)]);
                agent.setDirection("Outbound");
                agent.setDuration("00:00:11");
                lstAgMonitor.add(agent);
            }

            return TMSResponse.buildResponse(lstAgMonitor, lstAgMonitor.size());
        } catch (Exception e) {
            logger.error(ErrorMessage.USER_NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.USER_NOT_FOUND);
        }
    }
    @GetMapping("/campaign")
    public TMSResponse<List<CampaignMonitorDto>> CampaignMonitor(CampaignMonitorDto campaignMonitorDto) throws TMSException {
        GetCampaign params = new GetCampaign();
        params.setOrgId(getCurrentOriganationId());
        if (params.getLimit() == null) {
            params.setLimit(Const.DEFAULT_PAGE_SIZE);
        }
        DBResponse<List<CPCampaign>> dbResponse = freshService.getCampaign(SESSION_ID, params);
        if (dbResponse.getResult().isEmpty()) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }

        List<CampaignMonitorDto> lstCampaignMonitor = new ArrayList<>();
        for(CPCampaign campaign : dbResponse.getResult()){
            CampaignMonitorDto campaignMonitorDto1 = new CampaignMonitorDto();
            campaignMonitorDto1.setCpName(campaign.getCpName());

            lstCampaignMonitor.add(campaignMonitorDto1);
        }
        return TMSResponse.buildResponse(lstCampaignMonitor, lstCampaignMonitor.size());
    }

    @GetMapping("/agentMonitor")
    public TMSResponse agentMonitor() throws TMSException {
        int orgId = getCurOrgId();
        Map<String, String> rateMapping = this.getRate(orgId);
        logger.info(rateMapping.toString());
        AgentRateMonitorResponseDto responseDto = new AgentRateMonitorResponseDto();
        List<AgentRateMonitorDto> agentRateMonitorDtoList = new ArrayList<>();
        if(rateMapping != null){
            for(String k : rateMapping.keySet()){
                logger.info("{}: {}", k, rateMapping.get(k));
                int userId = Integer.parseInt(k);
                AgentRateMonitorDto agentRateMonitorDto = this.getProcessed(orgId, userId);
                String maxleadByUser = this.getMaxleadByUser(orgId, userId, 469);//indo campain
                if(maxleadByUser != null){
                    agentRateMonitorDto.setMaxlead(Integer.parseInt(maxleadByUser));
                }
                agentRateMonitorDto.setRate(Double.parseDouble(rateMapping.get(k)));
                logger.info("maxlead {} : {}", userId, maxleadByUser);
                Double totalAmount = this.calculateAmountByUser(orgId, userId);
                String userName = this.getAgentName(orgId, userId);
                Integer newLeadProcessed = this.getNewLeadProcessed(orgId, userId);
                logger.info("New lead processed: {}", newLeadProcessed);
                agentRateMonitorDto.setNewLeadProcessed(newLeadProcessed);
                agentRateMonitorDto.setAgentName(userName);
                agentRateMonitorDto.setTotalAmount(totalAmount);
                agentRateMonitorDtoList.add(agentRateMonitorDto);
            }
        }
        String totalLead = this.getTotalleadByUser(orgId, 469);
        if(totalLead != null)
            responseDto.setLeadTotal(Integer.parseInt(totalLead));
        responseDto.setData(agentRateMonitorDtoList);
        return TMSResponse.buildResponse(responseDto, agentRateMonitorDtoList.size());
    }

    private Map<String, String> getRate(int orgId){
        String key = Helper.createRedisKey(Const.REDIS_PREFIX_RATE, String.valueOf(orgId) + ":469");
        logger.info("RATE KEY: {}", key);
        Map<String, String> rateMaping = RedisHelper.getRedis(stringRedisTemplate, key);
        return rateMaping;
    }

    private String getMaxleadByUser(int orgId, int userId, int cpId){
        String key = Helper.createRedisKey(Const.REDIS_PREFIX_STATIC, String.valueOf(orgId) + ":" + cpId);
        String exist = RedisHelper.getKey(stringRedisTemplate, key, String.valueOf(userId));
        return exist;
    }

    private String getTotalleadByUser(int orgId, int cpId){
        String key = Helper.createRedisKey(Const.REDIS_PREFIX_STATIC, String.valueOf(orgId) + ":" + cpId);
        String exist = RedisHelper.getKey(stringRedisTemplate, key, "leadTotal");
        return exist;
    }

    private AgentRateMonitorDto getProcessed(int orgId, int userId){
        String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, userId);
        logger.info(key);
        Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, key);
        int iApproved = 0, iCallback = 0, iRejected = 0, iTrash = 0, iNew = 0, busy = 0, invalid = 0, noanws = 0, unreach = 0, uncall = 0, iTotal = 0;
        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
                String fieldValue = leadMap.get(lstLead.get(i));
                int status = Helper.IntergeTryParse(fieldValue);
                logger.info("{}|{}", lstLead.get(i), fieldValue);
                iTotal++;
                switch (status) {
                    case 1:
                        iNew++;
                        break;
                    case 2:
                        iApproved++;
                        break;
                    case 3:
                        iRejected++;
                        break;
                    case 4:
                    case 5:
                        iTrash++;
                        break;
                    case 6:
                        break;
                    case 7:
                        unreach++;
                        break;
                    case 8:
                        iCallback++;
                        break;
                    case 9:
                        iCallback++;
                        break;
                    case 10:
                        busy++;
                        break;
                    case 11:
                        noanws++;
                        break;
                }

            }
        }
        uncall = unreach + iRejected + noanws;
        AgentRateMonitorDto agentRateMonitorDto = new AgentRateMonitorDto();
        agentRateMonitorDto.setAgentId(userId);
        agentRateMonitorDto.setApproved(iApproved);
        agentRateMonitorDto.setCallback(iCallback);
        agentRateMonitorDto.setRejected(iRejected);
        agentRateMonitorDto.setLeadProcessed(iTotal);
        agentRateMonitorDto.setTrash(iTrash);
        agentRateMonitorDto.setUnCall(uncall);
        return agentRateMonitorDto;
    }

    private Double calculateAmountByUser(int orgId, int userId) {
        double amount = 0;
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_AMOUNT, orgId, userId);
        Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (String s : lstLead) {
                String fieldValue = leadMap.get(s);
                try {
                    double tmp = Double.parseDouble(fieldValue);
                    amount += tmp;
                } catch (Exception e) {
                    logger.error("{} {}", fieldValue, e.getMessage());
                }
            }
        }
        return amount;
    }

    private String getAgentName(int orgId, int userId){
        GetUserParams userParams = new GetUserParams();
        userParams.setOrgId(orgId);
        userParams.setUserId(userId);
        DBResponse<List<User>> dbResponse = userService.getUser(SESSION_ID, userParams);
        if(dbResponse == null || dbResponse.getResult().isEmpty())
            return null;
        String userName = dbResponse.getResult().get(0).getUserName();
        return userName;
    }

    private Integer getNewLeadProcessed(int orgId, int userId){
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_NEW, orgId, userId);
        Integer leadSize = RedisHelper.sizeOfKey(stringRedisTemplate, leadKey);
        return leadSize;
    }

    @GetMapping("/refresh/rule")
    public TMSResponse<Boolean> getCampaignRefresh() {
        Boolean isSuccess = campaignService.refreshConfigDistributionRules(SESSION_ID, getCurOrgId());
        return TMSResponse.buildResponse(isSuccess);
    }

    @GetMapping("/refresh/strategy")
    public TMSResponse<Boolean> getStrategyRefresh() {
        Boolean isSuccess = campaignService.refreshConfigCallStrategies(SESSION_ID, getCurOrgId());
        return TMSResponse.buildResponse(isSuccess);
    }
}
