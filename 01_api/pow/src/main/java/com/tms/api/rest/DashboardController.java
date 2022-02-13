package com.tms.api.rest;

import com.tms.api.dto.dashboard.*;
import com.tms.api.dto.dashboard.ComparationDto;
import com.tms.api.dto.dashboard.DashboardMonitor;
import com.tms.api.dto.dashboard.MySaleTunnelDto;
import com.tms.api.dto.dashboard.ObjectDto;
import com.tms.api.dto.dashboard.PerfomanceCompareDto;
import com.tms.api.dto.dashboard.StaticLeadDto;
import com.tms.api.dto.dashboard.TotalCallDto;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.Helper;
import com.tms.api.helper.RedisHelper;
import com.tms.api.response.TMSResponse;
import com.tms.dto.*;
import com.tms.dto.DBResponse;
import com.tms.dto.GetCdr;
import com.tms.dto.GetCdrResp;
import com.tms.dto.GetDoNew;
import com.tms.dto.GetDoNewResp;
import com.tms.dto.GetGroupAgent;
import com.tms.dto.GetGroupAgentResp;
import com.tms.dto.GetLeadParamsV4;
import com.tms.dto.GetOrderManagement2;
import com.tms.dto.GetOrderManagement2Resp;
import com.tms.dto.GetUserParams;
import com.tms.entity.*;
import com.tms.entity.CLFresh;
import com.tms.entity.User;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.CLProductService;
import com.tms.service.impl.DeliveryOrderService;
import com.tms.service.impl.LCProvinceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by dinhanhthai on 17/07/2019.
 */

@RestController
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    LCProvinceService lcProvinceService;

    @Autowired
    CLFreshService clFreshService;

    @Autowired
    DeliveryOrderService deliveryOrderService;

    @Autowired
    CLProductService clProductService;

    Logger logger = LoggerFactory.getLogger(DashboardController.class);

    private StaticLeadDto calculateLeadStatusByUser(int userId, int orgId) {
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, userId);
        Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
        StaticLeadDto staticLeadDto = new StaticLeadDto();
        int iApproved = 0, iCallback = 0, iRejected = 0, iTrash = 0, total = 0, busy = 0, invalid = 0, noanws = 0, unreach = 0, uncall = 0;

        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
                String fieldValue = leadMap.get(lstLead.get(i));
                int status = Helper.IntergeTryParse(fieldValue);
                total++;
                switch (status) {
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
        staticLeadDto.setApproved(iApproved);
        staticLeadDto.setCallback(iCallback);
        staticLeadDto.setRejected(iRejected);
        staticLeadDto.setTrash(iTrash);
        staticLeadDto.setBusy(busy);
        staticLeadDto.setNoanws(noanws);
        staticLeadDto.setUnreach(unreach);
        staticLeadDto.setTotal(total);
        staticLeadDto.setUnCall(uncall);
        return staticLeadDto;
    }

    private TotalCallDto calculateCallingByUser(int userId, int orgId) {
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_CALLING, orgId, userId);
/*        Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);


        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
//                String fieldValue = leadMap.get(lstLead.get(i));
                iConnected++;
            }
        }*/
        int iBusy = 0, iConnected = 0, iInvalid = 0, iTotal = 0;
        iConnected = RedisHelper.sizeOfKey(stringRedisTemplate, leadKey);
        iTotal = iBusy + iConnected + iInvalid;
        TotalCallDto totalCallDto = new TotalCallDto();
        totalCallDto.setBusy(iBusy);
        totalCallDto.setConnected(iConnected);
        totalCallDto.setInvalid(iInvalid);
        totalCallDto.setTotal(iTotal);

        return totalCallDto;
    }

    private int calculateSO(int userId, int orgId) {
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_SO, orgId, userId);
        /*Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
        int totalSO = 0;
        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
                String fieldValue = leadMap.get(lstLead.get(i));
                totalSO++;
            }
        }
        return totalSO;*/
        return RedisHelper.sizeOfKey(stringRedisTemplate, leadKey);
    }

    private int calculateSO(int orgId) {
        String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_SO, orgId, "*");
        List<String> lstKeys = RedisHelper.getAllKey(stringRedisTemplate, key);
        int totalSO = 0;
        for (int i = 0; i < lstKeys.size(); i++) {
            /*Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, lstKeys.get(i));
            if (!leadMap.isEmpty()) {
                List<String> lstLead = new ArrayList<String>(leadMap.keySet());
                for (int j = 0; j < lstLead.size(); j++) {
                    String fieldValue = leadMap.get(lstLead.get(j));
                    totalSO++;
                }
            }*/
            totalSO += RedisHelper.sizeOfKey(stringRedisTemplate, lstKeys.get(i));
        }
        return totalSO;
    }

    private int calculateLeadAssignByUser(int userId, int orgId) {
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, userId);
        /*Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
        if (leadMap != null)
            return leadMap.size();
        return 0;*/
        return RedisHelper.sizeOfKey(stringRedisTemplate, leadKey);
    }

    private int calculateAmountByUser(int userId, int orgId) {
        int amount = 0;
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_AMOUNT, orgId, userId);
        Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
//        int totalSO = 0;
        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
                String fieldValue = leadMap.get(lstLead.get(i));
                try {
                    int tmp = Helper.IntergeTryParse(fieldValue);
                    amount += tmp;
                } catch (Exception e) {
                    logger.error(fieldValue + e.getMessage());
                }
            }
        }
        return amount;
    }

    private int calculateDelivery(int userId, int orgId) {
        int countDo = 0;
        String leadKey = RedisHelper.createRedisKey(Const.REDIS_PRERIX_DO, orgId, userId);
       /* Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);

        if (!leadMap.isEmpty()) {
            List<String> lstLead = new ArrayList<String>(leadMap.keySet());
            for (int i = 0; i < lstLead.size(); i++) {
                String fieldValue = leadMap.get(lstLead.get(i));
                countDo++;
            }
        }
        return countDo;*/
        return RedisHelper.sizeOfKey(stringRedisTemplate, leadKey);
    }

    private MySaleTunnelDto calculateMySale(int userId, int orgId) {
        int iLead = 0, iDelivery = 0, iPaid = 0, iSaleOrder = 0;
        iDelivery = calculateDelivery(userId, orgId);
        iSaleOrder = calculateSO(userId,orgId);
        MySaleTunnelDto mySaleTunnelDto = new MySaleTunnelDto();
        mySaleTunnelDto.setLead(iLead);
        mySaleTunnelDto.setDelivered(iDelivery);
        mySaleTunnelDto.setPaid(iPaid);
        mySaleTunnelDto.setSaleOrder(iSaleOrder);

        return mySaleTunnelDto;
    }

    private PerfomanceCompareDto caclulatePerfomance(int userId, StaticLeadDto staticLeadDto, MySaleTunnelDto mySaleTunnelDto, int orgId) {
        int iLeadAssign = 0, iAvgLead = 0, iAgentSaleValue = 0, iAvgSaleTeam = 0, iSoTotal = 0,
                iSuccessOrder = 0, iAvgSuccessOrder = 0, iSo = 0, iLeadTotal = 0, iSumAmount = 0;

        iLeadAssign = (calculateLeadAssignByUser(userId, orgId));
        int iLeadApprove = staticLeadDto.getApproved();
        double amount = this.calculateAmountByUser(userId, orgId);

        ObjectDto ob1 = new ObjectDto();
        ob1.setLabel("lead_assign");
        ob1.setValue((double) staticLeadDto.getTotal());

        ObjectDto ob2 = new ObjectDto();
        ob2.setLabel("avg_call_lead");
        ob2.setValue((double) iAvgLead);

        ObjectDto ob3 = new ObjectDto();
        ob3.setLabel("agent_sale_value");
        ob3.setValue(amount);

        ObjectDto ob4 = new ObjectDto();
        ob4.setLabel("avg_sale_value_team");
        ob4.setValue((double) iAvgSaleTeam);

        iSuccessOrder = calculateSO(userId, orgId);
        ObjectDto ob5 = new ObjectDto();
        ob5.setLabel("success_order");
        ob5.setValue((double) mySaleTunnelDto.getSaleOrder());

        ObjectDto ob6 = new ObjectDto();
        ob6.setLabel("avg_success_order/team");
        ob6.setValue((double) iAvgSuccessOrder);
        //iSo = calculateSO(userId);

        ObjectDto ob7 = new ObjectDto();
        ob7.setLabel("rate_SO");
        ob7.setValue(Helper.convertToPercent(iLeadApprove, iLeadAssign));

        ObjectDto ob8 = new ObjectDto();
        ob8.setLabel("avg_rate_SO_team");
        ob8.setValue((double) iLeadTotal);

        ObjectDto ob9 = new ObjectDto();
        ob9.setLabel("avg_amount_user/order");
        ob9.setValue(Helper.formatNumber(amount/(double) mySaleTunnelDto.getSaleOrder()));
        ObjectDto ob10 = new ObjectDto();
        ob10.setLabel("avg_amount_team");
        ob10.setValue((double) mySaleTunnelDto.getSaleOrder());

        List<ComparationDto> lst = new ArrayList<>();

        ComparationDto orderProcess = new ComparationDto();
        orderProcess.setLabel("order_process");
        /*orderProcess.setObj1(ob1);
        orderProcess.setObj2(ob2);*/
        List<ObjectDto> lstObj = new ArrayList<>();
        lstObj.add(ob1);
        lstObj.add(ob2);
        orderProcess.setLst(lstObj);
        lst.add(orderProcess);

        ComparationDto saleValue = new ComparationDto();
        saleValue.setLabel("sale_value");
        List<ObjectDto> lstObj1 = new ArrayList<>();
        lstObj1.add(ob3);
        lstObj1.add(ob4);
        saleValue.setLst(lstObj1);
        lst.add(saleValue);

        ComparationDto saleOrder = new ComparationDto();
        saleOrder.setLabel("sale_order");
        List<ObjectDto> lstObj2 = new ArrayList<>();
        lstObj2.add(ob5);
        lstObj2.add(ob6);
        saleOrder.setLst(lstObj2);
        lst.add(saleOrder);

        ComparationDto rate = new ComparationDto();
        rate.setLabel("rate");
        List<ObjectDto> lstObj3 = new ArrayList<>();
        lstObj3.add(ob7);
        lstObj3.add(ob8);
        rate.setLst(lstObj3);


        ComparationDto avg = new ComparationDto();
        avg.setLabel("avg_sale_value");
        List<ObjectDto> lstObj4 = new ArrayList<>();
        lstObj4.add(ob9);
        lstObj4.add(ob10);
        avg.setLst(lstObj4);


        lst.add(rate);
        lst.add(avg);
        PerfomanceCompareDto perfomanceCompareDto = new PerfomanceCompareDto();
        perfomanceCompareDto.setLst(lst);

        return perfomanceCompareDto;
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

    private DashboardMonitor getDashboardMonitor(int userId, int orgId) {
        StaticLeadDto staticLeadDto = new StaticLeadDto();
        TotalCallDto totalCallDto = new TotalCallDto();
        MySaleTunnelDto mySaleTunnelDto = new MySaleTunnelDto();
        PerfomanceCompareDto perfomanceCompareDto = new PerfomanceCompareDto();


        staticLeadDto = this.calculateLeadStatusByUser(userId, orgId);

        totalCallDto = new TotalCallDto();
        totalCallDto.setInvalid(staticLeadDto.getTrash());
        totalCallDto.setConnected(staticLeadDto.getCallingConnected());
        totalCallDto.setBusy(staticLeadDto.getCallingBusy());
        totalCallDto.setTotal(staticLeadDto.getCallingTotal());

        mySaleTunnelDto = this.calculateMySale(userId, orgId);
        perfomanceCompareDto = this.caclulatePerfomance(userId, staticLeadDto, mySaleTunnelDto, orgId);
        mySaleTunnelDto.setLead(staticLeadDto.getTotal());

        DashboardMonitor dashboardMonitor = new DashboardMonitor();
        dashboardMonitor.setLead(staticLeadDto);
        dashboardMonitor.setTotalCall(totalCallDto);
        dashboardMonitor.setCompare(perfomanceCompareDto);
        dashboardMonitor.setMySale(mySaleTunnelDto);

        return dashboardMonitor;
    }

    private StaticLeadDto addStaticDto(StaticLeadDto staticLeadDto1, StaticLeadDto staticLeadDto2) {
        StaticLeadDto nStaticDto = new StaticLeadDto();
        nStaticDto.setBusy(staticLeadDto1.getBusy() + staticLeadDto2.getBusy());
        nStaticDto.setApproved(staticLeadDto1.getApproved() + staticLeadDto2.getApproved());
        nStaticDto.setRejected(staticLeadDto1.getRejected() + staticLeadDto2.getRejected());
        nStaticDto.setTrash(staticLeadDto1.getTrash() + staticLeadDto2.getTrash());
        nStaticDto.setNoanws(staticLeadDto1.getNoanws() + staticLeadDto2.getNoanws());
        nStaticDto.setUnreach(staticLeadDto1.getUnreach() + staticLeadDto2.getUnreach());
        nStaticDto.setTotal(staticLeadDto1.getTotal() + staticLeadDto2.getTotal());
        nStaticDto.setCallback(staticLeadDto1.getCallback() + staticLeadDto2.getCallback());

        return nStaticDto;
    }

    private TotalCallDto addTotalCallDto(TotalCallDto totalCallDto1, TotalCallDto totalCallDto2) {
        TotalCallDto nTotalCallDto = new TotalCallDto();
        nTotalCallDto.setBusy(totalCallDto1.getBusy() + totalCallDto2.getBusy());
        nTotalCallDto.setConnected(totalCallDto1.getConnected() + totalCallDto2.getConnected());
        nTotalCallDto.setTotal(totalCallDto1.getTotal() + totalCallDto2.getTotal());
        nTotalCallDto.setInvalid(totalCallDto1.getInvalid() + totalCallDto2.getInvalid());

        return nTotalCallDto;
    }

    private MySaleTunnelDto addMySaleTunnelDto(MySaleTunnelDto mySaleTunnelDto1, MySaleTunnelDto mySaleTunnelDto2) {
        MySaleTunnelDto mySaleTunnelDto = new MySaleTunnelDto();
        mySaleTunnelDto.setLead(mySaleTunnelDto1.getLead() + mySaleTunnelDto2.getLead());
        mySaleTunnelDto.setSaleOrder(mySaleTunnelDto1.getSaleOrder() + mySaleTunnelDto2.getSaleOrder());
        mySaleTunnelDto.setDelivered(mySaleTunnelDto1.getDelivered() + mySaleTunnelDto2.getDelivered());
        mySaleTunnelDto.setPaid(mySaleTunnelDto1.getPaid() + mySaleTunnelDto2.getPaid());
        return mySaleTunnelDto;
    }

    private DashboardMonitor calculateMyDashboardMonitor(List<DashboardMonitor> lst) {
        DashboardMonitor dashboardMonitor = new DashboardMonitor();
        StaticLeadDto staticLeadDto = new StaticLeadDto();
        TotalCallDto totalCallDto = new TotalCallDto();
        MySaleTunnelDto mySaleTunnelDto = new MySaleTunnelDto();
        PerfomanceCompareDto perfomanceCompareDto = new PerfomanceCompareDto();

        for (int i = 0; i < lst.size(); i++) {
            staticLeadDto = this.addStaticDto(staticLeadDto, lst.get(i).getLead());
            totalCallDto = this.addTotalCallDto(totalCallDto, lst.get(i).getTotalCall());
            mySaleTunnelDto = this.addMySaleTunnelDto(mySaleTunnelDto, lst.get(i).getMySale());

        }
        perfomanceCompareDto.setLst(new ArrayList<>());
        dashboardMonitor.setLead(staticLeadDto);
        dashboardMonitor.setTotalCall(totalCallDto);
        dashboardMonitor.setMySale(mySaleTunnelDto);
        dashboardMonitor.setCompare(perfomanceCompareDto);
        return dashboardMonitor;
    }

//    @GetMapping("/monitor")
    public TMSResponse getMonitor() throws TMSException {

        int userId = _curUser.getUserId();
        DashboardMonitor dashboardMonitor = new DashboardMonitor();

        if (Helper.isTeamLeader(_curUser)) {
            GetGroupAgent getGroupAgent = new GetGroupAgent();
            getGroupAgent.setUserId(userId);
            getGroupAgent.setOrgId(getCurOrgId());
            DBResponse<List<GetGroupAgentResp>> dbResponse = lcProvinceService.getGroupAgent(SESSION_ID, getGroupAgent);
            if (!dbResponse.getResult().isEmpty()) {
                List<GetGroupAgentResp> lstGroup = dbResponse.getResult();
                List<DashboardMonitor> lstDashboard = new ArrayList<>();

                for (GetGroupAgentResp groupAgentResp : lstGroup) {
                    int groupId = groupAgentResp.getGroupId();
                    List<Integer> lstUser = this.getListUser(groupId, getCurOrgId());
                    for (int j = 0; j < lstUser.size(); j++) {
                        DashboardMonitor dbmonitor = this.getDashboardMonitor(lstUser.get(j), getCurOrgId());
                        lstDashboard.add(dbmonitor);
                    }
                }
                return TMSResponse.buildResponse(this.calculateMyDashboardMonitor(lstDashboard), lstDashboard.size());

            } else {

            }
        } else {
/*            staticLeadDto = this.calculateLeadStatusByUser(userId);
//        TotalCallDto totalCallDto = this.calculateCallingByUser(userId);

            totalCallDto = new TotalCallDto();
            totalCallDto.setInvalid(staticLeadDto.getTrash());
            totalCallDto.setConnected(staticLeadDto.getCallingConnected());
            totalCallDto.setBusy(staticLeadDto.getCallingBusy());
            totalCallDto.setTotal(staticLeadDto.getCallingTotal());

            mySaleTunnelDto = this.calculateMySale(userId);
            perfomanceCompareDto = this.caclulatePerfomance(userId, staticLeadDto, mySaleTunnelDto);
            mySaleTunnelDto.setLead(staticLeadDto.getTotal());*/
            dashboardMonitor = this.getDashboardMonitor(userId, getCurOrgId());
        }

        return TMSResponse.buildResponse(dashboardMonitor, 1);
    }

    @GetMapping("/monitor")
    public TMSResponse getPerfomaceCompare() throws TMSException {

        int userId = _curUser.getUserId();
        int approved = 0;
        int totalLeadAssigned = 0;
        int totalSO = 0;
        int totalAmount = 0;
        int countAgent = 0;
        double conversionRate = 0;
        double avgAmountSO = 0;
        double avgLeadCalled = 0;
        double avgAmountAgent = 0;
        double avgSO = 0;
        int orgId = getCurOrgId();
        DashboardMonitor dashboardMonitor = new DashboardMonitor();
        List<DashboardMonitor> lstDashboard = new ArrayList<>();
        GetGroupAgent getGroupAgent = new GetGroupAgent();
        getGroupAgent.setUserId(userId);
        getGroupAgent.setOrgId(orgId);
        DBResponse<List<GetGroupAgentResp>> dbResponse = lcProvinceService.getGroupAgent(SESSION_ID, getGroupAgent);
        List<GetGroupAgentResp> lstGroup = dbResponse.getResult();
        if (Helper.isAdmin(_curUser) || Helper.isManager(_curUser)) {
            GetUserParams userParams = new GetUserParams();
            userParams.setOrgId(orgId);
            DBResponse<List<User>> userResponse = userService.getUser(SESSION_ID, userParams);
            List<User> listUser = userResponse.getResult();
            if (!listUser.isEmpty()) {
                for (User user : listUser) {
                    countAgent++;
                    int user_Id = user.getUserId();
                    DashboardMonitor dbmonitor = this.getDashboardMonitor(user_Id, orgId);
                    StaticLeadDto staticLeadDto = dbmonitor.getLead();
                    approved += staticLeadDto.getApproved();
                    totalLeadAssigned += staticLeadDto.getTotal();
                    totalSO += calculateSO(user_Id, orgId);
                    totalAmount += calculateAmountByUser(user_Id, orgId);
                    lstDashboard.add(dbmonitor);
                }
            }
            dashboardMonitor = this.calculateMyDashboardMonitor(lstDashboard);
        } else if(Helper.isTeamLeader(_curUser)){
            if (!lstGroup.isEmpty()) {
                for (GetGroupAgentResp groupAgentResp : lstGroup) {
                    int groupId = groupAgentResp.getGroupId();
                    List<Integer> lstUser = this.getListUser(groupId, orgId);
                    for (int j = 0; j < lstUser.size(); j++) {
                        countAgent++;
                        DashboardMonitor dbmonitor = this.getDashboardMonitor(lstUser.get(j), orgId);
                        StaticLeadDto staticLeadDto = dbmonitor.getLead();
                        approved += staticLeadDto.getApproved();
                        totalLeadAssigned += staticLeadDto.getTotal();
                        totalSO += calculateSO(lstUser.get(j), orgId);
                        totalAmount += calculateAmountByUser(lstUser.get(j), orgId);
                        lstDashboard.add(dbmonitor);
                    }
                }

            }
            dashboardMonitor = this.calculateMyDashboardMonitor(lstDashboard);
        }else{
            dashboardMonitor = this.getDashboardMonitor(userId, orgId);
            if (!lstGroup.isEmpty()) {
                for (GetGroupAgentResp groupAgentResp : lstGroup) {
                    int groupId = groupAgentResp.getGroupId();
                    List<Integer> lstUser = this.getListUser(groupId, orgId);
                    for (int j = 0; j < lstUser.size(); j++) {
                        countAgent++;
                        StaticLeadDto staticLeadDto = this.calculateLeadStatusByUser(lstUser.get(j), orgId);
                        approved += staticLeadDto.getApproved();
                        totalLeadAssigned += staticLeadDto.getTotal();
                        totalSO += calculateSO(lstUser.get(j),orgId);
                        totalAmount += calculateAmountByUser(lstUser.get(j), orgId);
                    }
                }
            }
        }
        if (totalLeadAssigned != 0) {
            conversionRate = ((double) approved / totalLeadAssigned) * 100;
        }
        if (totalSO != 0) {
            avgAmountSO = Helper.formatNumber((double) totalAmount / totalSO);
        }
        if (countAgent != 0) {
            avgLeadCalled = Helper.formatNumber((double) totalLeadAssigned / countAgent);
            avgAmountAgent = Helper.formatNumber((double) totalAmount / countAgent);
            avgSO = Helper.formatNumber((double) totalSO / countAgent);
        }
        PerfomanceCompareDto perfomanceCompareDto = new PerfomanceCompareDto();
        if (Helper.isTeamLeader(_curUser) || Helper.isAdmin(_curUser) || Helper.isManager(_curUser)) {
            perfomanceCompareDto = this.caclulatePerfomance(userId, dashboardMonitor.getLead(), dashboardMonitor.getMySale(), orgId);
            List<ComparationDto> rateDtos = perfomanceCompareDto.getLst();
            for (ComparationDto dto : rateDtos) {
                if (dto.getLabel().equals("rate")) {
                    List<ObjectDto> obDtos = dto.getLst();
                    obDtos.get(1).setValue(Helper.formatNumber(conversionRate));
                }
                if (dto.getLabel().equals("avg_sale_value")) {
                    List<ObjectDto> obDtos = dto.getLst();
                    obDtos.get(0).setValue(0.0);
                    obDtos.get(1).setValue(Helper.formatNumber(avgAmountSO));
                }
                if (dto.getLabel().equals("order_process")) {
                    List<ObjectDto> obDtos = dto.getLst();
                    obDtos.get(0).setValue((double) totalLeadAssigned);
                    obDtos.get(1).setValue(avgLeadCalled);
                }
                if (dto.getLabel().equals("sale_value")) {
                    List<ObjectDto> obDtos = dto.getLst();
                    obDtos.get(0).setValue(Helper.formatNumber(totalAmount));
                    obDtos.get(1).setValue(Helper.formatNumber(avgAmountAgent));
                }
                if (dto.getLabel().equals("sale_order")) {
                    List<ObjectDto> obDtos = dto.getLst();
                    obDtos.get(0).setValue((double) totalSO);
                    obDtos.get(1).setValue(avgSO);
                }
            }
        } else {
            perfomanceCompareDto = dashboardMonitor.getCompare();
            List<ComparationDto> rateDtos = perfomanceCompareDto.getLst();
            for (ComparationDto dto : rateDtos) {
                if (dto.getLabel().equals("rate")) {
                    List<ObjectDto> obDtos = dto.getLst();
                    obDtos.get(1).setValue(Helper.formatNumber(conversionRate));
                }
                if (dto.getLabel().equals("avg_sale_value")) {
                    List<ObjectDto> obDtos = dto.getLst();
                    double userAmount = 0;
                    if (this.calculateSO(userId, orgId) != 0) {
                        userAmount = this.calculateAmountByUser(userId, orgId) / this.calculateSO(userId, orgId);
                    }
                    obDtos.get(0).setValue(Helper.formatNumber(userAmount));
                    obDtos.get(1).setValue(Helper.formatNumber(avgAmountSO));
                }
                if (dto.getLabel().equals("order_process")) {
                    List<ObjectDto> obDtos = dto.getLst();
//            		obDtos.get(0).setValue((double)totalLeadAssigned);
                    obDtos.get(1).setValue(avgLeadCalled);
                }
                if (dto.getLabel().equals("sale_value")) {
                    List<ObjectDto> obDtos = dto.getLst();
//            		obDtos.get(0).setValue((double)totalAmount);
                    double amount = this.calculateAmountByUser(userId, orgId);
                    obDtos.get(0).setValue(Helper.formatNumber(amount));
                    obDtos.get(1).setValue(Helper.formatNumber(avgAmountAgent));
                }
                if (dto.getLabel().equals("sale_order")) {
                    List<ObjectDto> obDtos = dto.getLst();
//            		obDtos.get(0).setValue((double)totalSO);
                    obDtos.get(1).setValue(avgSO);
                }
            }
        }
//        perfomanceCompareDto.setLst(rateDtos);
        dashboardMonitor.setCompare(perfomanceCompareDto);
        return TMSResponse.buildResponse(dashboardMonitor, 1);
    }

    private Integer cutNumber(int totalAmount, double avgAmount) {
        String total = Integer.toString(totalAmount);
        String avg = Integer.toString((int) avgAmount);
        int n1 = total.length();
        int n2 = avg.length();
        int n = n2 - (n1 - n2) - 2;

        return n;

    }

    private String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHMMss");
        Date now = new Date();
        now.setHours(0);
        now.setMinutes(00);
        now.setSeconds(0);
        Date tomorrow = new Date(now.getTime() + (1000 * 60 * 60 * 24));
        tomorrow.setHours(0);
        tomorrow.setMinutes(00);
        tomorrow.setSeconds(0);
        String currentDate = formatter.format(now);
        String endDate = formatter.format(tomorrow);
        String date = currentDate + "|" + endDate;

        return date;
    }

    private Integer getTodaySoList(int orgId, int userId, String date) throws TMSException {

        GetOrderManagement2 params = new GetOrderManagement2();

        params.setLimit(params.getLimit() != null ? params.getLimit() : Const.DEFAULT_PAGE_SIZE);
        params.setOrgId(orgId);
        params.setCreatedate(date);

//        if (!Helper.isTeamLeader(_curUser)) {
//            params.setAgId(userId);
//        }
        DBResponse<List<GetOrderManagement2Resp>> dbResponseSaleOrder = clFreshService.getOrderManagementV2(SESSION_ID, params);
        if (dbResponseSaleOrder.getResult().isEmpty()) {
            return 0;
        }

        List<GetOrderManagement2Resp> listSaleOrders = dbResponseSaleOrder.getResult();
        for (GetOrderManagement2Resp SaleOrder : listSaleOrders) {
            int agId = SaleOrder.getAgId();
            String soKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_SO, orgId, agId);
            Map<String, String> soMap = RedisHelper.getRedis(stringRedisTemplate, soKey);
            RedisHelper.saveRedis(stringRedisTemplate, soMap, soKey);

            String amountKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_AMOUNT, orgId, agId);
            Map<String, String> amountMap = RedisHelper.getRedis(stringRedisTemplate, amountKey);
            RedisHelper.saveRedis(stringRedisTemplate, amountMap, amountKey);
        }

        return listSaleOrders.size();
    }

    private Integer getTodayDOList(int orgId, String date) throws TMSException {

        GetDoNew params = new GetDoNew();

        params.setOrgId(orgId);
        params.setCreatedate(date);
//        if (params.getLimit() == null) {
//            params.setLimit(Const.DEFAULT_PAGE_SIZE);
//        }

        DBResponse<List<GetDoNewResp>> dbResponse = deliveryOrderService.getDoNew(SESSION_ID, params);
        if (dbResponse == null || dbResponse.getResult().isEmpty()) {
            return 0;
        }
        List<GetDoNewResp> deliveryOrders = dbResponse.getResult();
        for (GetDoNewResp deliveryOrder : deliveryOrders) {
            int createby = deliveryOrder.getCreateby();
            String doKey = RedisHelper.createRedisKey(Const.REDIS_PRERIX_DO, orgId, createby);
            Map<String, String> doMap = RedisHelper.getRedis(stringRedisTemplate, doKey);
            RedisHelper.saveRedis(stringRedisTemplate, doMap, doKey);
        }
        return deliveryOrders.size();
    }

    private Integer getTodayCdrList(int orgId, String date) throws TMSException {

        GetCdr params = new GetCdr();

        params.setOrgId(orgId);
        params.setCreatetime(date);
//        if (params.getLimit() == null) {
//            params.setLimit(Const.DEFAULT_PAGE_SIZE);
//        }

        DBResponse<List<GetCdrResp>> dbResponse = clProductService.getCdr(SESSION_ID, params);
        if (dbResponse == null || dbResponse.getResult().isEmpty()) {
            return 0;
        }
        List<GetCdrResp> cdrs = dbResponse.getResult();
        for (GetCdrResp cdr : cdrs) {
            int userId = cdr.getUserId();
            String cdrKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_CALLING, orgId, userId);
            Map<String, String> cdrMap = RedisHelper.getRedis(stringRedisTemplate, cdrKey);
            RedisHelper.saveRedis(stringRedisTemplate, cdrMap, cdrKey);
        }
        return cdrs.size();
    }

    private Integer getTodayLeadList(int orgId, String date) throws TMSException {

        GetLeadParamsV4 params = new GetLeadParamsV4();

        params.setOrgId(orgId);
        params.setAssigned(-1);
        params.setCreatedate(date);
//        if (params.getLimit() == null) {
//            params.setLimit(Const.DEFAULT_PAGE_SIZE);
//        }

        DBResponse<List<CLFresh>> dbResponse = clFreshService.getLeadV4(SESSION_ID, params);
        if (dbResponse == null || dbResponse.getResult().isEmpty()) {
            return 0;
        }
        List<CLFresh> leads = dbResponse.getResult();
        for (CLFresh lead : leads) {
            int assigned = lead.getAssigned();
            String leadKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, assigned);
            Map<String, String> leadMap = RedisHelper.getRedis(stringRedisTemplate, leadKey);
            RedisHelper.saveRedis(stringRedisTemplate, leadMap, leadKey);
        }
        return leads.size();
    }

    @GetMapping("/getTodayInfo")
    public String sendRequest(@RequestParam(name = "type", required = true) String type) throws TMSException {

        User user = getCurrentUser();
        int userId = user.getUserId();
        int orgId = user.getOrgId();
        String date = getDate();
        String resp = "";

        if (type.equals("so")) {
            int soResp = getTodaySoList(orgId, userId, date);
            resp = "SO " + soResp;
        } else if (type.equals("do")) {
            int doResp = getTodayDOList(orgId, date);
            resp = "DO " + doResp;
        } else if (type.equals("cdr")) {
            int cdrResp = getTodayCdrList(orgId, date);
            resp = "Cdr " + cdrResp;
        } else if (type.equals("lead")) {
            int leadResp = getTodayLeadList(orgId, date);
            resp = "Lead " + leadResp;
        } else {
            resp = "null";
        }
        return resp;
    }

}
