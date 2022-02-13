package com.tms.api.rest;

import com.tms.api.dto.CreateCallBackDto;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.UpdateCallbackDto;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.Helper;
import com.tms.api.helper.RedisHelper;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.CallbackService;
import com.tms.api.task.DBLogWriter;
import com.tms.api.utils.LoggerUtils;
import com.tms.config.DBConfigMap;
import com.tms.dto.*;
import com.tms.entity.CLCallback;
import com.tms.service.impl.CLCallbackService;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LCProvinceService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@Scope("prototype")
@SuppressWarnings("unchecked")
@RequestMapping("/callback")
public class CallbackController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    private final
    CLCallbackService clCallbackService;

    private final
    CLFreshService clFreshService;

    private final
    LogService logService;

    private final
    LCProvinceService lcProvinceService;

    private final
    DBConfigMap dbConfigMap;

    private final
    ModelMapper modelMapper;

    private final
    StringRedisTemplate stringRedisTemplate;

    private final
    DBLogWriter dbLog;

    private final CallbackService callbackService;

    @Autowired
    public CallbackController(CLCallbackService clCallbackService, CLFreshService clFreshService, LogService logService, LCProvinceService lcProvinceService, DBConfigMap dbConfigMap, ModelMapper modelMapper, StringRedisTemplate stringRedisTemplate, DBLogWriter dbLog, CallbackService callbackService) {
        this.clCallbackService = clCallbackService;
        this.clFreshService = clFreshService;
        this.logService = logService;
        this.lcProvinceService = lcProvinceService;
        this.dbConfigMap = dbConfigMap;
        this.modelMapper = modelMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.dbLog = dbLog;
        this.callbackService = callbackService;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public TMSResponse<List<CLCallback>> getCallbackList(GetCallbackAll params,
                                                         @RequestParam(value = "status", required = false, defaultValue = "assigned") String status)
            throws TMSException {

        int orgId = getCurOrgId();
        int userId = _curUser.getUserId();

        params.setOrgId(orgId);
        params.setAssigned("0");

        if (status.equals(Const.STATUS_ASSIGN)) {
            if (Helper.isTeamLeader(_curUser)) {
                GetGroupAgent getGroupAgent = new GetGroupAgent();
                getGroupAgent.setUserId(userId);
                getGroupAgent.setOrgId(getCurOrgId());
                DBResponse<List<GetGroupAgentResp>> dbResponse = lcProvinceService.getGroupAgent(SESSION_ID, getGroupAgent);
                if (!dbResponse.getResult().isEmpty()) {
                    List<GetGroupAgentResp> lstGroup = dbResponse.getResult();
                    List<CLCallback> callback = new ArrayList<>();
                    int rowCount = 0;
                    List<Integer> lstUser = new ArrayList<Integer>();
                    for (GetGroupAgentResp groupAgentResp : lstGroup) {
                        int groupId = groupAgentResp.getGroupId();
                        lstUser.addAll(this.getListUser(groupId, orgId));
                    }

                    lstUser = lstUser.stream().distinct().collect(Collectors.toList());
                    String lstAssigned = lstUser.toString().substring(1, lstUser.toString().length() - 1);
                    params.setAssigned(lstAssigned);

                }
            } else if (Helper.isManager(_curUser)) {
                params.setAssigned(null);
            } else {
                params.setAssigned(getCurrentUser().getUserId().toString());
            }

        }

        if (params.getLimit() == null)
            params.setLimit(Const.DEFAULT_PAGE_SIZE);

        DBResponse<List<CLCallback>> cbResponse = clCallbackService.getCallbackAll(SESSION_ID, params);
        if (cbResponse == null || cbResponse.getResult().isEmpty()) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        return TMSResponse.buildResponse(cbResponse.getResult(), cbResponse.getRowCount());
    }

    private List<Integer> getListUser(int groupId, int orgId) {
        List<Integer> lstUser = new ArrayList();
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_GROUP, orgId, groupId);
        Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
                lstUser.add(Helper.IntergeTryParse(lstLead.get(i)));
            }
        }

        return lstUser;
    }

    @GetMapping("/{leadId}")
    public TMSResponse<CLCallback> getCallback(@PathVariable("leadId") Integer leadId) throws TMSException {
        GetCallbackAll clCallbackParams = new GetCallbackAll();
        clCallbackParams.setLeadId(leadId);
        DBResponse<List<CLCallback>> dbResponse = clCallbackService.getCallbackAll(SESSION_ID, clCallbackParams);
        if (dbResponse == null || dbResponse.getResult().isEmpty()) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @PutMapping("/assign")
    public TMSResponse<Boolean> assignCallbackToAgent(@RequestBody Integer[] leadIds) throws TMSException {
        UpdateCLCallback clCallback = new UpdateCLCallback();
        for (int i = 0; i < leadIds.length; i++) {
            clCallback.setLeadId(leadIds[i]);
            clCallback.setAssigned(getCurrentUser().getUserId());
            DBResponse dbResponse = logService.updateCallback(SESSION_ID, clCallback);
            if (dbResponse == null) {
                return TMSResponse.buildResponse(false);
            }
        }
        return TMSResponse.buildResponse(true);
    }

    @PutMapping("/unassign")
    public TMSResponse<Boolean> unassignCallback(@RequestBody Integer[] leadIds) throws TMSException {
        UpdateCLCallback clCallback = new UpdateCLCallback();
        for (int i = 0; i < leadIds.length; i++) {
            clCallback.setLeadId(leadIds[i]);
            clCallback.setAssigned(0);
            logService.updateCallback(SESSION_ID, clCallback);
        }
        return TMSResponse.buildResponse(true);
    }

    @DeleteMapping
    public TMSResponse<Boolean> deleteMultiple(@RequestBody String leadIds) {
        logService.delMulCallback(SESSION_ID, leadIds);
        return TMSResponse.buildResponse(true);
    }

    @PostMapping("/create")
    public TMSResponse<?> createCallback(@Valid @RequestBody CreateCallBackDto callbackRequest) {
        ObjectRequestDTO objectRequestDTO = new ObjectRequestDTO();
        objectRequestDTO.setUserId(_curUser.getUserId());
        objectRequestDTO.setOrganizationId(getCurOrgId());
        objectRequestDTO.setSessionId(SESSION_ID);
        objectRequestDTO.setUser(_curUser);
        logger.info("Body callback request: {}", LoggerUtils.snagAsLogJson(callbackRequest));
        logger.info("Body object request: {}", LoggerUtils.snagAsLogJson(objectRequestDTO));
        return callbackService.creatOne(callbackRequest, objectRequestDTO);
    }

    @PutMapping("/{leadId}")
    public TMSResponse<CLCallback> updateCallback(@PathVariable("leadId") Integer leadId, @Valid @RequestBody UpdateCallbackDto wrappedData) throws TMSException {
        UpdateCLCallback clCallback = new UpdateCLCallback();
        clCallback.setLeadId(leadId);
        clCallback.setRequestTime(wrappedData.getRequestTime());
        logService.updateCallback(SESSION_ID, clCallback);
        return TMSResponse.buildResponse(true);
    }
}
