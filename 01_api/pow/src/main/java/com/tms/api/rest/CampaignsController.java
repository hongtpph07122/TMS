package com.tms.api.rest;

import com.tms.api.dto.UpdateCampaignConfigDto;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.Helper;
import com.tms.api.repository.CpConfigurationRepository;
import com.tms.api.request.CampaignConfigurationRequestDTO;
import com.tms.api.request.CampaignRequestDTO;
import com.tms.api.request.CampaignUpdateRequestDTO;
import com.tms.api.response.CampaignResponseDTO;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.CampaignService;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.*;
import com.tms.entity.CPCampaign;
import com.tms.entity.log.InsCPConfig;
import com.tms.entity.log.UpdCampaign;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LCProvinceService;
import com.tms.service.impl.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@SuppressWarnings("unchecked")
@RequestMapping("/campaign")
public class CampaignsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CampaignsController.class);

    private final CLFreshService freshService;

    private final LCProvinceService provinceService;

    private final LogService logService;

    private final CpConfigurationRepository cpConfigurationRepository;

    private final CampaignService campaignService;

    @Autowired
    public CampaignsController(CLFreshService freshService, LCProvinceService provinceService, LogService logService, CpConfigurationRepository cpConfigurationRepository, CampaignService campaignService) {
        this.freshService = freshService;
        this.provinceService = provinceService;
        this.logService = logService;
        this.cpConfigurationRepository = cpConfigurationRepository;
        this.campaignService = campaignService;
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<?> snagAllCampaigns(CampaignRequestDTO params) {
        params.setOrgId(getCurOrgId());

        if (!ObjectUtils.allNotNull(params.getLimit())) {
            params.setLimit(Const.DEFAULT_PAGE_SIZE);
        }
        DBResponse<List<CampaignResponseDTO>> dbResponse = freshService.snagCampaigns(SESSION_ID, params);
        if (CollectionUtils.isEmpty(dbResponse.getResult())) {
            logger.info(" error - {}", ErrorMessage.NOT_FOUND.getMessage());
            try {
                throw new TMSException(ErrorMessage.NOT_FOUND);
            } catch (TMSException e) {
                logger.error("error [NOT_SHOW_CAMPAIGNS] throw exception when get all campaigns from db which is null: {}", e.getMessage(), e);
            }
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount()), HttpStatus.OK);
    }


    @GetMapping("/progress")
    public TMSResponse<List<GetCampaignProgressResp>> getCampaignProgressList(GetCampaignProgress params) throws TMSException {
        params.setOrgId(getCurOrgId());
        if (params.getLimit() == null) {
            params.setLimit(Const.DEFAULT_PAGE_SIZE);
        }
        DBResponse<List<GetCampaignProgressResp>> dbResponse = freshService.getCampaignProgress(SESSION_ID, params);
        if (dbResponse.getResult().isEmpty()) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }

        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @GetMapping("{cpId}")
    public @ResponseBody
    ResponseEntity<?> findOneCampaign(@PathVariable Integer cpId) {
        CampaignRequestDTO campaignRequest = new CampaignRequestDTO();
        campaignRequest.setOrgId(getCurOrgId());
        campaignRequest.setCpId(cpId);
        DBResponse<List<CampaignResponseDTO>> responseAsDB = freshService.snagCampaigns(SESSION_ID, campaignRequest);
        if (CollectionUtils.isEmpty(responseAsDB.getResult())) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            try {
                throw new TMSException(ErrorMessage.NOT_FOUND);
            } catch (TMSException e) {
                logger.error("error - [findOneCampaign] response null when findOne: {}", e.getMessage(), e);
            }
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(responseAsDB.getResult()), HttpStatus.OK);
    }

    @GetMapping("{cpId}/{type}")
    public TMSResponse getCampaign(@PathVariable Integer cpId, @PathVariable String type) throws TMSException {
        int curOrgId = getCurOrgId();
        switch (type) {
            case "callinglist":
                GetCpCallingList getCpCallingList = new GetCpCallingList();
                getCpCallingList.setCpId(cpId);
                getCpCallingList.setOrgId(curOrgId);
                DBResponse<List<GetCpCallingListResp>> dbCallingRp = freshService.getCpCallingList(SESSION_ID, getCpCallingList);
                return TMSResponse.buildResponse(dbCallingRp.getResult());
            case "callstrategy":
                GetCpCallStrategy cpCallStrategy = new GetCpCallStrategy();
                cpCallStrategy.setCpId(cpId);
                cpCallStrategy.setOrgId(curOrgId);
                DBResponse<List<GetCpCallStrategyResp>> dbStrategyRp = provinceService.getCpCallStrategy(SESSION_ID, cpCallStrategy);
                return TMSResponse.buildResponse(dbStrategyRp.getResult());
            case "rule":
                GetCpDistributionRuleParams cpRule = new GetCpDistributionRuleParams();
                cpRule.setCpId(cpId);
                cpRule.setOrgId(curOrgId);
                DBResponse<List<GetCpDistributionRuleResp>> dbRule = provinceService.getCpDistributionRule(SESSION_ID, cpRule);
                return TMSResponse.buildResponse(dbRule.getResult());
            default:
                break;
        }

        return TMSResponse.buildResponse("Not_suitable_parameter", 0);
    }

    @PutMapping
    public @ResponseBody
    ResponseEntity<?> updateOneCampaign(@RequestBody CampaignUpdateRequestDTO campaignUpdateRequest) {
        DBResponse<?> responseAsDB = logService.udpCampaign(SESSION_ID, campaignUpdateRequest);
        if (!ObjectUtils.allNotNull(responseAsDB)) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            try {
                throw new TMSException(ErrorMessage.NOT_FOUND);
            } catch (TMSException e) {
                logger.error("error - [updateOneCampaign] - response null when updated campaign: {}", e.getMessage(), e);
            }
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(responseAsDB.getResult()), HttpStatus.OK);
    }

    @PutMapping("/changeStatus/{cpId}/{status}")
    public TMSResponse changeStatus(@PathVariable Integer cpId, @PathVariable String status) throws TMSException {
        UpdCampaign campaign = new UpdCampaign();
        campaign.setCampaignid(cpId);
        if (status.equals(EnumType.CAMPAIGN_STATUS_NAME.STOP.getValue())) {
            campaign.setStatus(EnumType.CAMPAIGN_STATUS_ID.STOPPED.getValue());
        }
        if (status.equals(EnumType.CAMPAIGN_STATUS_NAME.START.getValue())) {
            validateStartCampaing(cpId);
            campaign.setStatus(EnumType.CAMPAIGN_STATUS_ID.RUNNING.getValue());
        }

        if (status.equals(EnumType.CAMPAIGN_STATUS_NAME.WAITING.getValue())) {
            campaign.setStatus(EnumType.CAMPAIGN_STATUS_ID.PAUSED.getValue());
        }

        DBResponse dbResponse = logService.udpCampaign(SESSION_ID, campaign);
        if (dbResponse == null) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        return TMSResponse.buildResponse(dbResponse.getResult());
    }

    @DeleteMapping("/{cpId}")
    public TMSResponse deleteCampaign(@PathVariable Integer cpId) throws TMSException {
        GetCampaign params = new GetCampaign();
        params.setOrgId(getCurOrgId());
        params.setCpId(cpId);
        DBResponse<List<CPCampaign>> getCampaignDbResponse = freshService.getCampaign(SESSION_ID, params);
        if (getCampaignDbResponse.getResult().isEmpty()) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        CPCampaign campaign = getCampaignDbResponse.getResult().get(0);

        if (campaign.getStatus() == EnumType.CAMPAIGN_STATUS_ID.STOPPED.getValue()
                || campaign.getStatus() == EnumType.CAMPAIGN_STATUS_ID.NEW.getValue()) {
            UpdCampaign udpCampaign = new UpdCampaign();
            udpCampaign.setCampaignid(cpId);
            udpCampaign.setStatus(EnumType.CAMPAIGN_STATUS_ID.DELETED.getValue());
            DBResponse dbResponse = logService.udpCampaign(SESSION_ID, udpCampaign);
            if (dbResponse == null) {
                logger.info(ErrorMessage.NOT_FOUND.getMessage());
                throw new TMSException(ErrorMessage.NOT_FOUND);
            }
            return TMSResponse.buildResponse(dbResponse.getResult());
        }
        throw new TMSException(ErrorMessage.BAD_REQUEST);
    }

    @PostMapping
    public @ResponseBody
    ResponseEntity<?> createOneCampaign(@RequestBody CampaignConfigurationRequestDTO campaignRequest) {
        campaignRequest.setOrgId(getCurOrgId());
        campaignRequest.setOwner(_curUser.getUserId());
        campaignRequest.setStatus(36);/* status campaign new */
        DBResponse<?> responseAsDB = logService.insCampaign(SESSION_ID, campaignRequest);

        if (!ObjectUtils.allNotNull(responseAsDB)) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            try {
                throw new TMSException(ErrorMessage.NOT_FOUND);
            } catch (TMSException e) {
                logger.error("error - [createOneCampaign] - response null when create campaign: {}", e.getMessage(), e);
            }
        }
        int statusCode = (responseAsDB.getErrorCode() == 1 ? 200 : responseAsDB.getErrorCode());
        return new ResponseEntity<>(TMSResponse.buildResponse(responseAsDB.getErrorMsg().trim(), 0, "", statusCode), HttpStatus.OK);
    }

    @GetMapping("/callstrategy")
    public TMSResponse<List<GetCallStrategyResp>> getCallStrategyList() throws TMSException {
        GetCallStrategy strategy = new GetCallStrategy();
        strategy.setOrgId(getCurOrgId());
        List<GetCallStrategyResp> strategies = provinceService.getCallStrategy(SESSION_ID, strategy)
                .getResult();
        return TMSResponse.buildResponse(strategies);
    }

    @GetMapping("/callstrategy/{strategyId}")
    public TMSResponse<List<GetStrategyParamResp>> getCallback(@PathVariable Integer strategyId) {
        GetStrategyParam params = new GetStrategyParam();
        params.setCsId(strategyId);
        List<GetStrategyParamResp> strategyParams = provinceService.getStrategyParam(SESSION_ID, params).getResult();
        return TMSResponse.buildResponse(strategyParams);
    }

    @GetMapping("/rule")
    public TMSResponse<List<GetDistributionRuleResp>> getCampaignDistributionRuleList() throws TMSException {
        GetDistributionRule rule = new GetDistributionRule();
        rule.setOrgId(getCurOrgId());
        DBResponse<List<GetDistributionRuleResp>> dbResponse = provinceService.getDistributionRule(SESSION_ID, rule);
        return TMSResponse.buildResponse(dbResponse.getResult());
    }

    @GetMapping("/callinglist")
    public TMSResponse<List<GetCallingListResp>> getCampaignCallinglist(GetCallingList param) throws TMSException {
        param.setOrgId(getCurOrgId());
        if (param.getLimit() == null)
            param.setLimit(Const.DEFAULT_PAGE_SIZE);

        DBResponse dbResponse = freshService.getCallingList(SESSION_ID, param);
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @GetMapping("/config/{cpId}")
    public TMSResponse<Boolean> getCampaignConfig(@PathVariable Integer cpId) throws TMSException {
        CPCampaignParams ruleParams = new CPCampaignParams();
        ruleParams.setCapaignId(cpId);
        List<CPCampaignResp> cpConfigs = freshService.getCampaignConfig(SESSION_ID, ruleParams).getResult();
        return TMSResponse.buildResponse(Helper.getCampaignConfig(cpConfigs));
    }

    @PutMapping("/config")
    @Transactional
    public TMSResponse<Boolean> updateCampaignConfig(@RequestBody UpdateCampaignConfigDto configs) throws TMSException {
        validation.validateCampaignId(configs.getCampaignId());

        // remove all old configs
        // TODO: can delete cp_configuration
        cpConfigurationRepository.deleteByCpId(configs.getCampaignId());

        if (configs.getStrategyId() > 0) {
            InsCPConfig cpConfig = new InsCPConfig();
            cpConfig.setCpId(configs.getCampaignId());
            cpConfig.setType(EnumType.CAMPAIGN_CONFIG_TYPE.STRATEGY.getValue());
            cpConfig.setValue(configs.getStrategyId());
            cpConfig.setStatus(EnumType.CAMPAIGN_CONFIG_TYPE.STRATEGY.getValue());
            logService.insCPConfig(SESSION_ID, cpConfig);
        }

        if (configs.getRuleId() > 0) {
            InsCPConfig cpConfig = new InsCPConfig();
            cpConfig.setCpId(configs.getCampaignId());
            cpConfig.setType(EnumType.CAMPAIGN_CONFIG_TYPE.RULE.getValue());
            cpConfig.setValue(configs.getRuleId());
            cpConfig.setStatus(EnumType.CAMPAIGN_CONFIG_TYPE.RULE.getValue());
            logService.insCPConfig(SESSION_ID, cpConfig);
        }

        if (configs.getAgentGroupIdList().size() > 0) {
            List<Integer> uniqueGroupIds = configs.getAgentGroupIdList().stream().distinct().collect(Collectors.toList());
            for (Integer groupId : uniqueGroupIds) {
                if (groupId <= 0) {
                    continue;
                }
                InsCPConfig cpConfig = new InsCPConfig();
                cpConfig.setCpId(configs.getCampaignId());
                cpConfig.setType(EnumType.CAMPAIGN_CONFIG_TYPE.AGENTGROUP.getValue());
                cpConfig.setValue(groupId);
                cpConfig.setStatus(EnumType.CAMPAIGN_CONFIG_TYPE.AGENTGROUP.getValue());
                logService.insCPConfig(SESSION_ID, cpConfig);
            }
        }

        if (configs.getCallingList().size() > 0) {
            List<Integer> uniqueList = configs.getCallingList().stream().distinct().collect(Collectors.toList());
            for (Integer clId : uniqueList) {
                if (clId <= 0) {
                    continue;
                }
                InsCPConfig cpConfig = new InsCPConfig();
                cpConfig.setCpId(configs.getCampaignId());
                cpConfig.setType(EnumType.CAMPAIGN_CONFIG_TYPE.CLGROUP.getValue());
                cpConfig.setValue(clId);
                cpConfig.setStatus(EnumType.CAMPAIGN_CONFIG_TYPE.CLGROUP.getValue());
                logService.insCPConfig(SESSION_ID, cpConfig);
            }
        }
        return TMSResponse.buildResponse(true);
    }

    public void validateStartCampaing(Integer campaignId) throws TMSException {
        CPCampaignParams params = new CPCampaignParams();
        params.setCapaignId(campaignId);
        List<CPCampaignResp> result = freshService.getCampaignConfig(SESSION_ID, params).getResult();

        if (result.isEmpty()) {
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        CPCampaignResp campaignConfig = result.get(0);
        if (campaignConfig.getDistributionRule() == null || campaignConfig.getCallingList() == null
                || campaignConfig.getCallStrategy() == null || campaignConfig.getGroupAgent() == null) {
            throw new TMSException(ErrorMessage.BAD_REQUEST);
        }
    }

    @GetMapping("{cpId}/agents")
    public TMSResponse<List<GetCampaignAgentResp>> getCampaignAgents(@PathVariable Integer cpId)
            throws TMSException {

        GetCampaignAgent params = new GetCampaignAgent();
        params.setOrgId(getCurOrgId());
        params.setUserType(Const.USER_AGENT_TYPE);
//        params.setCpId(cpId);
        DBResponse<List<GetCampaignAgentResp>> dbResponse = freshService.getCampaignAgent(SESSION_ID, params);
        List<GetCampaignAgentResp> lst = new java.util.ArrayList<>();
        if (!dbResponse.getResult().isEmpty()) {
            lst = dbResponse.getResult().stream()
                    .distinct()
                    .collect(Collectors.toList());
        }
        return TMSResponse.buildResponse(lst, lst.size());
    }

    @GetMapping("/refresh/campaign")
    public TMSResponse<Boolean> getCampaignRefresh() {
        Boolean isSuccess = campaignService.refreshConfigCampaign(SESSION_ID, getCurOrgId());
        return TMSResponse.buildResponse(isSuccess);
    }
}
