package com.tms.api.rest;

import com.tms.api.response.SynonymsConfigurationResponseDTO;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.SynonymsConfigurationService;
import com.tms.api.utils.LoggerUtils;
import com.tms.dto.*;
import com.tms.service.impl.CLFreshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/synonym")
public class SynonymController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SynonymController.class);

    private final CLFreshService clFreshService;
    private final SynonymsConfigurationService synonymsService;

    @Value("${config.country}")
    public String _COUNTRY;

    @Autowired
    public SynonymController(CLFreshService clFreshService, SynonymsConfigurationService synonymsService) {
        this.clFreshService = clFreshService;
        this.synonymsService = synonymsService;
    }

    @GetMapping
    public TMSResponse<?> geListStatus() {
        return TMSResponse.buildResponse(new DBResponse());
    }

    @GetMapping("/customer")
    public TMSResponse<?> getCustomerListStatus() {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] synonymId = {2, 3, 6};
        int[] value = {2, 3, 6};
        String[] name = {"approved", "rejected", "closed"};
        for (int i = 0; i < synonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(value[i]));
            map.put("name", name[i]);
            map.put("type", "customer status");
            map.put("id", String.valueOf(synonymId[i]));
            listStatus.add(map);
        }

        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/lead")
    public TMSResponse<?> getLeadListStatus() {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] synonymId = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        int[] value = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        String[] name = {"new", "approved", "rejected", "duplicated", "trash", "closed", "unreachable", "callback consulting", "callback not propect", "callback potential", "busy", "noanswer", "urgent"};
        for (int i = 0; i < synonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(value[i]));
            map.put("name", name[i]);
            map.put("type", "lead status");
            map.put("id", String.valueOf(synonymId[i]));
            listStatus.add(map);
        }

        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/role")
    public TMSResponse<?> getRoleListStatus() {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] synonymId = {11, 12, 13, 14, 15, 16};
        int[] value = {11, 12, 13, 14, 15, 16};
        String[] name = {"admin", "agent", "agent_type02", "validation", "team_leader", "management"};
        for (int i = 0; i < synonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(value[i]));
            map.put("name", name[i]);
            map.put("type", "role list");
            map.put("id", String.valueOf(synonymId[i]));
            listStatus.add(map);
        }

        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/payment")
    public @ResponseBody
    ResponseEntity<?> getPaymentTypeList(@RequestParam(value = "typeId", required = false, defaultValue = "18") Integer typeId) {
        /*
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] synonymId = {150, 151, 152};
        int[] value = {1, 2, 3};
        String[] name = {"COD", "Banking transfer", "Deposit"};
        for (int i = 0; i < synonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(value[i]));
            map.put("name", name[i]);
            map.put("type", "payment mothod");
            map.put("id", String.valueOf(synonymId[i]));
            listStatus.add(map);
        }

        return TMSResponse.buildResponse(listStatus, listStatus.size());
        */
        List<SynonymsConfigurationResponseDTO> synonymsConfigurationResponseDTOS = synonymsService.snagSynonymsUnionBaseOnGEO(_COUNTRY, synonymsService.finByTypeId(typeId));
        return new ResponseEntity<>(TMSResponse.buildResponse(synonymsConfigurationResponseDTOS, synonymsConfigurationResponseDTOS.size()), HttpStatus.OK);
    }

    @GetMapping("/campaign")
    public TMSResponse<?> getCampaignListStatus() {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] campaignStatusSynonymId = {31, 32, 33, 34, 35, 36};
        int[] campaignStatusValue = {31, 32, 33, 34, 35, 36};
        String[] campaignStatusName = {"running", "stopped", "paused", "stopping", "deleted", "new"};
        for (int i = 0; i < campaignStatusSynonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(campaignStatusValue[i]));
            map.put("name", campaignStatusName[i]);
            map.put("type", "campaign status");
            map.put("id", String.valueOf(campaignStatusSynonymId[i]));
            listStatus.add(map);
        }
        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/call")
    public TMSResponse<?> getCallStatus() {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] campaignStatusSynonymId = {71, 72, 73, 74};
        int[] campaignStatusValue = {71, 72, 73, 74};
        String[] campaignStatusName = {"answer", "noanwser", "aban", "busy"};
        for (int i = 0; i < campaignStatusSynonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(campaignStatusValue[i]));
            map.put("name", campaignStatusName[i]);
            map.put("type", "call status");
            map.put("id", String.valueOf(campaignStatusSynonymId[i]));
            listStatus.add(map);
        }
        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/order")
    public TMSResponse<?> getSaleOrderStatus() {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] saleOrderStatusSynonymId = {41, 42, 43, 44, 45, 46, 357};
        int[] saleOrderStatusValue = {41, 42, 43, 44, 45, 46, 357};
        String[] saleOrderStatusName = {"new", "pending", "validated", "cancel", "success", "unassigned", "delay"};
        for (int i = 0; i < saleOrderStatusSynonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(saleOrderStatusValue[i]));
            map.put("name", saleOrderStatusName[i]);
            map.put("type", "sale order status");
            map.put("id", String.valueOf(saleOrderStatusSynonymId[i]));
            listStatus.add(map);
        }
        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/orderValidate")
    public TMSResponse<?> getSaleOrderValidateStatus(@RequestParam(value = "isfilter", required = false, defaultValue = "0") String isfilter) {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] saleOrderStatusSynonymId = {41, 42, 43, 44, 46, 357};
        int[] saleOrderStatusValue = {41, 42, 43, 44, 46, 357};
        String[] saleOrderStatusName = {"new", "pending", "validated", "cancel", "unassigned", "delay"};
        int start = 1;
        if (isfilter.equals("1")) {
            start = 0;
        }

        for (int i = start; i < saleOrderStatusSynonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(saleOrderStatusValue[i]));
            map.put("name", saleOrderStatusName[i]);
            map.put("type", "sale order status");
            map.put("id", String.valueOf(saleOrderStatusSynonymId[i]));
            listStatus.add(map);
        }
        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/delivery")
    public TMSResponse<?> getDeliveryStatus() {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] saleOrderStatusSynonymId = {51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 65};
        int[] saleOrderStatusValue = {51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 65};
        String[] saleOrderStatusName = {"new", "ready to pick", "cancel", "picking", "in transit", "returning", "reject", "in preparation", "delivered", "paid", "returned", "delivery fail", "picked up", "pending"};

        for (int i = 0; i < saleOrderStatusSynonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(saleOrderStatusValue[i]));
            map.put("name", saleOrderStatusName[i]);
            map.put("type", "delivery order status");
            map.put("id", String.valueOf(saleOrderStatusSynonymId[i]));
            listStatus.add(map);
        }
        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/combotype")
    public TMSResponse<?> getComboType() {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] saleOrderStatusSynonymId = {311, 312};
        int[] saleOrderStatusValue = {1, 2};
        String[] saleOrderStatusName = {"Normal", "Combo"};

        for (int i = 0; i < saleOrderStatusSynonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(saleOrderStatusValue[i]));
            map.put("name", saleOrderStatusName[i]);
            map.put("type", "product_type");
            map.put("id", String.valueOf(saleOrderStatusSynonymId[i]));
            listStatus.add(map);
        }
        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/currency")
    public TMSResponse<?> getListCurrency(@RequestParam(required = false) Integer orgId) {
        List<Map<String, String>> listStatus = new ArrayList<>();
        int[] saleOrderStatusSynonymId = {313, 314};
        int[] saleOrderStatusValue = {1, 2};
        String[] saleOrderStatusName = {"VND", "USD"};

        for (int i = 0; i < saleOrderStatusSynonymId.length; i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("value", String.valueOf(saleOrderStatusValue[i]));
            map.put("name", saleOrderStatusName[i]);
            map.put("type", "unit price");
            map.put("id", String.valueOf(saleOrderStatusSynonymId[i]));
            if (orgId != null && (orgId == 4 || orgId == 6) && saleOrderStatusName[i].equals("VND")) {
                listStatus.add(map);
            }
        }
        return TMSResponse.buildResponse(listStatus, listStatus.size());
    }

    @GetMapping("/trash")
    public TMSResponse<?> getTrashStatus() {
        GetSynonym getSynonym = new GetSynonym();
        getSynonym.setTypeId(9);
        DBResponse<List<GetSynonymResp>> dbResponse = clFreshService.getSynonym(SESSION_ID, getSynonym);
        if (!CollectionUtils.isEmpty(dbResponse.getResult())) {
            return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
        }
        return TMSResponse.buildResponse(null, 0);
    }

    @GetMapping("/reject")
    public TMSResponse<?> getRejectStatus() {
        GetSynonymV2 getSynonym = new GetSynonymV2();
        getSynonym.setTypeId(21);
        DBResponse<List<GetSynonymRespV2>> dbResponse = clFreshService.getSynonymV2(SESSION_ID, getSynonym);
        if (!CollectionUtils.isEmpty(dbResponse.getResult())) {
            return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
        }
        return TMSResponse.buildResponse(null, 0);
    }

    @GetMapping("/object")
    public @ResponseBody
    ResponseEntity<?> findObjectBy(@RequestParam(value = "typeId") Integer typeId) {
        GetSynonymV2 getSynonym = new GetSynonymV2();
        getSynonym.setTypeId(typeId);
        DBResponse<List<GetSynonymRespV2>> dbResponse = clFreshService.getSynonymV2(SESSION_ID, getSynonym);
        if (CollectionUtils.isEmpty(dbResponse.getResult())) {
            return new ResponseEntity<>(TMSResponse.buildResponse(Collections.emptyList(), 0), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount()), HttpStatus.OK);
    }

    @GetMapping("/age-range")
    public TMSResponse<?> getAgeRange() {
        GetSynonym getSynonym = new GetSynonym();
        getSynonym.setTypeId(47);
        DBResponse<List<GetSynonymResp>> dbResponse = clFreshService.getSynonym(SESSION_ID, getSynonym);
        if (!CollectionUtils.isEmpty(dbResponse.getResult())) {
            return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
        }
        return TMSResponse.buildResponse(null, 0);
    }
}
