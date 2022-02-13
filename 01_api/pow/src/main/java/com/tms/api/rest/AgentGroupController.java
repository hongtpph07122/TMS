package com.tms.api.rest;

import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.Helper;
import com.tms.api.helper.LogHelper;
import com.tms.api.helper.RedisHelper;
import com.tms.api.response.TMSResponse;
import com.tms.dto.*;
import com.tms.entity.OrGroup;
import com.tms.entity.User;
import com.tms.entity.log.DelGroupAgent;
import com.tms.entity.log.InsGroupAgent;
import com.tms.entity.log.UpdGroupAgent;
import com.tms.service.impl.LCProvinceService;
import com.tms.service.impl.LogService;
import com.tms.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/agentgroup")
@SuppressWarnings("unchecked")
public class AgentGroupController extends BaseController {

    Logger logger = LoggerFactory.getLogger(AgentGroupController.class);

    @Autowired
    LCProvinceService lcProvinceService;
    @Autowired
    LogService logService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserService userService;

    public static Queue<Integer> QUEUE_UPDATE = new LinkedList<>();

    @GetMapping
    public TMSResponse<List<OrGroup>> getLstAgentGroup() throws TMSException {
        GetGroupParams params = new GetGroupParams();
        params.setOrgId(getCurOrgId());
        DBResponse<List<OrGroup>> dbResponse = lcProvinceService.getGroup(SESSION_ID, params);
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @GetMapping("/{groupId}/agent")
    public TMSResponse<List<GetGroupAgentResp>> getGroupDetail(@PathVariable Integer groupId, GetGroupAgentV2 params) throws TMSException {
        if (params.getLimit() == null)
            params.setLimit(Const.DEFAULT_PAGE_SIZE);

        params.setOrgId(getCurOrgId());
        params.setGroupId(groupId);
//        if (groupId == 0)
//            params.setGroupId(null);
        DBResponse<List<GetGroupAgentResp>> dbResponse = lcProvinceService.getGroupAgentV2(SESSION_ID, params);
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @PutMapping("/{groupId}/agent")
    public TMSResponse updGroupDetail(@RequestBody UpdGroupAgent params) throws TMSException {
//        params.setOrgId(_curOrgId);
        logger.info(LogHelper.toJson(params));
//        params.setOldGroupId()
        DBResponse dbResponse = logService.updGroupAgent(SESSION_ID, params);
        if (dbResponse.getErrorCode() != Const.INS_DB_SUCCESS)
            return TMSResponse.buildResponse(false, 0, dbResponse.getErrorMsg(), 400);
        QUEUE_UPDATE.add(getCurOrgId());
        return TMSResponse.buildResponse(true);
    }

    @PostMapping("/{groupId}/agent")
    public TMSResponse insUserToGroup(@RequestBody InsGroupAgent params) throws TMSException {

//        params.setOrgId(_curOrgId);
        DBResponse dbResponse = logService.insGroupAgent(SESSION_ID, params);
        if (dbResponse.getErrorCode() != Const.INS_DB_SUCCESS)
            return TMSResponse.buildResponse(false, 0, dbResponse.getErrorMsg(), 400);
        QUEUE_UPDATE.add(getCurOrgId());
        return TMSResponse.buildResponse(true);
    }

    @DeleteMapping("/{groupId}/agent")
    public TMSResponse delUserToGroup(@RequestParam("grAgId") Integer grAgId) throws TMSException {
        DelGroupAgent params = new DelGroupAgent();
        params.setGrAgId(grAgId);

        DBResponse dbResponse = logService.delGroupAgent(SESSION_ID, params);
        if (dbResponse.getErrorCode() != Const.INS_DB_SUCCESS)
            return TMSResponse.buildResponse(false, 0, dbResponse.getErrorMsg(), 400);
        QUEUE_UPDATE.add(getCurOrgId());
        return TMSResponse.buildResponse(true);
    }

    @GetMapping("/{groupId}")
    public TMSResponse<List<GetGroupAgentResp>> getAgentByGroup(@PathVariable Integer groupId) {
        GetGroupAgent params = new GetGroupAgent();
        params.setGroupId(groupId);
        DBResponse<List<GetGroupAgentResp>> dbResponse = lcProvinceService.getGroupAgent(SESSION_ID, params);
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    public static TMSResponse CachingGroup(Integer orgId, LCProvinceService lcprovince,  UserService uservice, StringRedisTemplate redis, String session) throws TMSException {
        GetGroupParams params = new GetGroupParams();
        params.setOrgId(orgId);
        DBResponse<List<OrGroup>> dbResponse = lcprovince.getGroup(session, params);
        if (dbResponse.getResult().isEmpty()) {
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        List<OrGroup> lstGroup = dbResponse.getResult();
        for (int i = 0; i < lstGroup.size(); i++) {
            GetGroupAgent params1 = new GetGroupAgent();
            params1.setGroupId(lstGroup.get(i).getGroupId());
            DBResponse<List<GetGroupAgentResp>> dbResponse1 = lcprovince.getGroupAgent(session, params1);
            List<Integer> groupUserIds = new ArrayList<>();

            if (!dbResponse1.getResult().isEmpty()) {
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_GROUP, orgId, lstGroup.get(i).getGroupId());
                Map<String, String> groupAgentMap = new HashMap<>();
                List<GetGroupAgentResp> lstGroupAgent = dbResponse1.getResult();

                for (int j = 0; j < lstGroupAgent.size(); j++) {
                    groupUserIds.add(lstGroupAgent.get(j).getUserId());
//
                }

                //TODO need to turning ...
                GetUserParams userParams = new GetUserParams();
                for (int user_Id : groupUserIds) {
                    userParams.setUserId(user_Id);
                    DBResponse<List<User>> userResponse = uservice.getUser(session, userParams);
                    if (!userResponse.getResult().isEmpty()) {
                        groupAgentMap.put(String.valueOf(user_Id), userResponse.getResult().get(0).getUserType());
                    }
                }
                RedisHelper.saveRedis(redis, groupAgentMap, key, false);
            }
        }
        return TMSResponse.buildResponse(true);
    }

    @GetMapping("/caching")
    public TMSResponse cachingGroupAgent() throws TMSException {
        return CachingGroup(getCurOrgId(), lcProvinceService, userService, stringRedisTemplate, SESSION_ID);
    }

    @GetMapping("/getListAgent")
    public TMSResponse getListAgent() throws TMSException {
        int userId = _curUser.getUserId();
        GetGroupAgent getGroupAgent = new GetGroupAgent();
        DBResponse<List<GetGroupAgentResp>> dbResponse = new DBResponse<List<GetGroupAgentResp>>();
        GetUserParams userParams = new GetUserParams();

        if (Helper.isAdmin(_curUser) || Helper.isManager(_curUser)) {
            userParams.setOrgId(getCurOrgId());
            DBResponse<List<User>> userResponse = userService.getUser(SESSION_ID, userParams);
            return TMSResponse.buildResponse(userResponse.getResult(), userResponse.getRowCount());

        } else if (Helper.isTeamLeader(_curUser) || Helper.isTeamLeaderOther(_curUser)) {
            getGroupAgent.setUserId(userId);
            getGroupAgent.setOrgId(getCurOrgId());
            dbResponse = lcProvinceService.getGroupAgent(SESSION_ID, getGroupAgent);
        }

        List<GetGroupAgentResp> lstGroup = dbResponse.getResult();
        List<Integer> lstUser = new ArrayList<Integer>();
        List<User> listUser = new ArrayList<User>();
        int rowCount = 0;
        if (!lstGroup.isEmpty()) {
            for (GetGroupAgentResp groupAgentResp : lstGroup) {
                int groupId = groupAgentResp.getGroupId();
                lstUser.addAll(this.getListUser(groupId));

            }
            lstUser = lstUser.stream().distinct().collect(Collectors.toList());
            String userIds = lstUser.stream()
                    .map(n -> String.valueOf(n))
                    .collect(Collectors.joining(","));
            GetUserParamV2 getUserParamV2 = new GetUserParamV2();
            getUserParamV2.setOrgId(getCurOrgId());
            getUserParamV2.setUserId(userIds);
            logger.info("get agent list param: " + LogHelper.toJson(getUserParamV2));
            DBResponse<List<User>> getUsers = userService.getUserV3(SESSION_ID, getUserParamV2);
            listUser = getUsers.getResult();
            rowCount = getUsers.getRowCount();

//            //TODO need to turning ...
//            for (int user_Id : lstUser) {
//                userParams.setUserId(user_Id);
//                userParams.setUserType(Const.USER_AGENT_TYPE);
//                DBResponse<List<User>> userResponse = userService.getUser(SESSION_ID, userParams);
//                if (!userResponse.getResult().isEmpty()) {
//                    rowCount += userResponse.getRowCount();
//                    listUser.addAll(userResponse.getResult());
//                }
//            }
        }

        return TMSResponse.buildResponse(listUser, rowCount);
    }

    private List<Integer> getListUser(int groupId) {
        List<Integer> lstUser = new ArrayList();
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_GROUP, getCurOrgId(), groupId);
        Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
//        int totalSO = 0;
        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
                lstUser.add(Helper.IntergeTryParse(lstLead.get(i)));
            }
        }

        return lstUser;
    }

    private List<Integer> getListUser(int groupId, String type) {
        List<Integer> lstUser = new ArrayList();
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_GROUP, getCurOrgId(), groupId);
        Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
//        int totalSO = 0;
        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
                lstUser.add(Helper.IntergeTryParse(lstLead.get(i)));
            }
        }

        return lstUser;
    }


}
