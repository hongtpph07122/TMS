package com.tms.api.service;

import com.tms.api.dto.LeadInfoDto;
import com.tms.api.entity.ClFresh;
import com.tms.api.request.LeadsRequest;
import com.tms.entity.CLFresh;
import com.tms.entity.log.InsCLFreshV11;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public interface LeadService {

    List<LeadInfoDto> getLeadInfo(List<Integer> status, List<Integer> cpId, String startTime, String endTime);

    List<LeadInfoDto> getLeadInfoV2(List<Integer> status, List<Integer> cpId, String startTime, String endTime);

    List<LeadInfoDto> getLeadInfoV3(List<Integer> status, List<Integer> cpId, String startDateOfCreate, String endDateOfModify, String endDateModify2);

    List<LeadInfoDto> getLeadDuplicateInfoV3(Integer cpId, String checkDuplicateStartDateStr, String checkDuplicateEndDateStr);

    HashMap<String, String> getLeadCheckDuplicateV3(Integer cpId, String checkDuplicateStartDateStr, String checkDuplicateEndDateStr);

    HashMap<String, String> getLeadCheckDuplicate(List<Integer> status, List<Integer> cpId, String startTime, String endTime);

    ClFresh getById(Integer leadId);

    ClFresh saveOrUpdate(ClFresh clFresh);

    BigDecimal findMaxActualCall(Integer leadId);

    <T extends CLFresh> T setCustomerData(String ssId, Integer leadId, T clFresh);

    void updateReminderCalls(Integer leadId, LeadsRequest leadsRequest);

    ClFresh findOneByLeadId(Integer leadId);

    void updLeadsCrmActionType(String ssid, List<CLFresh> callingList, Integer crmActionType, Integer userId);

    void fixLocation(InsCLFreshV11 clFresh);

    boolean haveCallback(int leadId);
}
