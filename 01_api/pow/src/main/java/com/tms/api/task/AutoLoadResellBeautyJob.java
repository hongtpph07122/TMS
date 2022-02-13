package com.tms.api.task;

import com.tms.api.dto.LeadInfoDto;
import com.tms.api.entity.ClFresh;
import com.tms.api.helper.DateHelper;
import com.tms.api.helper.EnumType;
import com.tms.api.repository.LeadRepository;
import com.tms.api.service.LeadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class AutoLoadResellBeautyJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoLoadResellBeautyJob.class);
    private final LeadRepository leadRepository;

    private final LeadService leadService;

    private static final Integer CP_RESELL_BEAUTY_AR = 611;
    private static final Integer CP_RESELL_BEAUTY_RJ = 612;
    private static final Integer CP_RESELL_BEAUTY_ETC = 613;
    private static final Integer CL_RESELL_BEAUTY_AR = 53;
    private static final Integer CL_RESELL_BEAUTY_RJ = 54;
    private static final Integer CL_RESELL_BEAUTY_ETC = 55;

    @Value("${config.auto-load-data-beauty}")
    public boolean isAutoLoadData;

    @Value("${config.day-reload}")
    public String dayReload;

    @Autowired
    public AutoLoadResellBeautyJob(LeadRepository leadRepository, LeadService leadService) {
        this.leadRepository = leadRepository;
        this.leadService = leadService;
    }

    @Scheduled(cron = "0 40 0 * * ?")
    public void autoLoadDataResellBeauty() {
        LOGGER.info("**********START AUTO LOAD LEAD RESELL BEAUTY AR-RJ-ETC*************");
        if (!isAutoLoadData) {
            LOGGER.info("********AUTO LOAD DATA OFF*********");
            return;
        }
        LocalDateTime startDateOfCreate = LocalDateTime.of(2021, 2, 1, 0, 0, 0);
        LocalDateTime endDateOfModify = LocalDateTime.now().minusDays(getReloadDay(0)).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDateOfModifyV3 = LocalDateTime.now().minusDays(getReloadDay(0)+1).withHour(0).withMinute(0).withSecond(0);

        LocalDateTime endDateOfModifyV2 = LocalDateTime.now().minusDays(getReloadDay(1)).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDateOfModifyV4 = LocalDateTime.now().minusDays(getReloadDay(1)+1).withHour(0).withMinute(0).withSecond(0);

        LocalDateTime checkDuplicateStartDate = LocalDateTime.now().minusDays(getReloadDay(0)).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime checkDuplicateStartDateV2 = LocalDateTime.now().minusDays(getReloadDay(1)).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime checkDuplicateEndDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String startDateOfCreateStr = startDateOfCreate.format(formatter);
        String endDateOfModifyStr = endDateOfModify.format(formatter);
        String endDateOfModifyV3Str = endDateOfModifyV3.format(formatter);

        String endDateOfModifyV2Str = endDateOfModifyV2.format(formatter);
        String endDateOfModifyV4Str = endDateOfModifyV4.format(formatter);

        String checkDuplicateStartDateStr = checkDuplicateStartDate.format(formatter);
        String checkDuplicateStartDateV2Str = checkDuplicateStartDateV2.format(formatter);
        String checkDuplicateEndDateStr = checkDuplicateEndDate.format(formatter);

        List<ClFresh> insFresh = new ArrayList<>();
        int approvedBeauty = 0;
        int rejectedBeauty = 0;
        int etcBeauty = 0;

        LOGGER.info("-----RESELL BEAUTY AR-----");
        LOGGER.info("startDateOfCreate: " + startDateOfCreateStr + " endDateOfModify: " + endDateOfModifyStr + " day reload: " + getReloadDay(0));
        LOGGER.info("-----RESELL BEAUTY RJ, ETC-----");
        LOGGER.info("startDateOfCreate: " + startDateOfCreateStr + " endDateOfModify: " + endDateOfModifyV2Str + " day reload: " + getReloadDay(1));

        //Get lead resell beauty AR
        List<Integer> campaignListAR = new ArrayList<>();
        campaignListAR.add(EnumType.CAMPAIGN_TH.FRESH_EVERLIFT.getCpId());
        campaignListAR.add(EnumType.CAMPAIGN_TH.FRESH_LAVITE.getCpId());
        List<Integer> leadStatusAR = new ArrayList<>();
        leadStatusAR.add(EnumType.LEAD_STATUS.APPROVED.getValue());

        List<LeadInfoDto> listLeadAR = leadService.getLeadInfoV3(leadStatusAR, campaignListAR, startDateOfCreateStr, endDateOfModifyStr, endDateOfModifyV3Str);

        LOGGER.info("get Lead from Resell Beauty AR: " + listLeadAR.size());
        HashMap<String, String> checkDuplicateBeautyAR = leadService.getLeadCheckDuplicateV3(CP_RESELL_BEAUTY_AR, checkDuplicateStartDateStr, checkDuplicateEndDateStr);
        HashMap<String, String> freshARNew = new HashMap<>();
        for (LeadInfoDto dto : listLeadAR) {
            if (!checkDuplicateBeautyAR.containsKey(dto.getPhone())) {
                ClFresh fresh = this.createLead(dto, CP_RESELL_BEAUTY_AR, CL_RESELL_BEAUTY_AR);
                insFresh.add(fresh);
                approvedBeauty++;
                freshARNew.put(fresh.getPhone(), String.format("%s-%s", fresh.getPhone(), fresh.getName()));
            }
        }

        //Get lead resell beauty RJ
        List<Integer> campaignListRJ = new ArrayList<>();
        campaignListRJ.add(EnumType.CAMPAIGN_TH.FRESH_EVERLIFT.getCpId());
        campaignListRJ.add(EnumType.CAMPAIGN_TH.FRESH_LAVITE.getCpId());
        List<Integer> leadStatusRJ = new ArrayList<>();
        leadStatusRJ.add(EnumType.LEAD_STATUS.REJECTED.getValue());
        List<LeadInfoDto> listLeadRJ = leadService.getLeadInfoV3(leadStatusRJ, campaignListRJ, startDateOfCreateStr, endDateOfModifyV2Str, endDateOfModifyV4Str);

        LOGGER.info("get Lead from Resell Beauty RJ: " + listLeadRJ.size());
        HashMap<String, String> checkDuplicateBeautyRJ = leadService.getLeadCheckDuplicateV3(CP_RESELL_BEAUTY_RJ, checkDuplicateStartDateV2Str, checkDuplicateEndDateStr);
        checkDuplicateBeautyRJ.putAll(freshARNew);
        for (LeadInfoDto dto : listLeadRJ) {
            if (!checkDuplicateBeautyRJ.containsKey(dto.getPhone())) {
                ClFresh fresh = this.createLead(dto, CP_RESELL_BEAUTY_RJ, CL_RESELL_BEAUTY_RJ);
                insFresh.add(fresh);
                rejectedBeauty++;
            }
        }

        //Get lead resell beauty ETC
        List<Integer> campaignListETC = new ArrayList<>();
        campaignListETC.add(EnumType.CAMPAIGN_TH.FRESH_EVERLIFT.getCpId());
        campaignListETC.add(EnumType.CAMPAIGN_TH.FRESH_LAVITE.getCpId());

        List<Integer> leadStatusETC = new ArrayList<>();
        leadStatusETC.add(EnumType.LEAD_STATUS.NEW.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.DUPLICATED.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.CLOSED.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.UNREACHABLE.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.BUSY.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.NOANSWER.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.URGENT.getValue());
        leadStatusETC.add(EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue());

        List<LeadInfoDto> listLeadETC = leadService.getLeadInfoV3(leadStatusETC, campaignListETC, startDateOfCreateStr, endDateOfModifyV2Str, endDateOfModifyV4Str);

        LOGGER.info("get Lead from Resell Beauty ETC: " + listLeadETC.size());
        HashMap<String, String> checkDuplicateBeautyETC = leadService.getLeadCheckDuplicateV3(CP_RESELL_BEAUTY_ETC, checkDuplicateStartDateV2Str, checkDuplicateEndDateStr);
        checkDuplicateBeautyETC.putAll(freshARNew);
        for (LeadInfoDto dto : listLeadETC) {
            if (!checkDuplicateBeautyETC.containsKey(dto.getPhone())) {
                ClFresh fresh = this.createLead(dto, CP_RESELL_BEAUTY_ETC, CL_RESELL_BEAUTY_ETC);
                insFresh.add(fresh);
                etcBeauty++;
            }
        }

        //Import lead beauty
        String messageBeauty = String.format("Import Beauty, approve: %s, rejected: %s, etc: %s", approvedBeauty, rejectedBeauty, etcBeauty);
        LOGGER.info("******START INSERT TO CAMPAIGN*******");
        if (!insFresh.isEmpty()) {
            leadRepository.saveAll(insFresh);
            LOGGER.info(messageBeauty);
        } else
            LOGGER.info("There is nothing to import");
    }

    private ClFresh createLead(LeadInfoDto dto, Integer cpId, Integer callingListId) {

        Date now = DateHelper.nowToTimestamp();
        ClFresh entity = new ClFresh();
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setProdName(dto.getProductName());
        entity.setProdId(dto.getProdId());
        String address = dto.getAddress();
        if (address != null && address.length() >= 200) {
            address = address.substring(0, 199);
        }
        entity.setSource("M");
        entity.setAddress(address);
        entity.setAssigned(0);
        entity.setLeadType("M");
        entity.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
        entity.setOrgId(10);
        entity.setCreatedate(now);
        entity.setNextCallTime(now);
        entity.setModifydate(now);
        entity.setCpId(cpId);
        entity.setCallingListId(callingListId);
        String userDefin04 = String.format("%s-%s", dto.getLeadId(), getStatusName(dto.getLeadStatus()));
        entity.setUserDefin04(String.format("%s|%s", dto.getUserDefin04(), userDefin04));
        if (dto.getLeadStatus().compareTo(EnumType.LEAD_STATUS.APPROVED.getValue()) == 0) {
            String comment = String.format("%s|%s|%s|%s|%s", dto.getPackageListItem(), dto.getAmount().toString(), dto.getPaymentMethod(), dto.getDoStatus(), dto.getDoAddress());
            if (comment.length() >= 300) {
                comment = comment.substring(0, 299);
            }
            entity.setComment(comment);
        }
        if (dto.getLeadStatus().compareTo(EnumType.LEAD_STATUS.REJECTED.getValue()) == 0) {
            String comment = String.format("%s|%s", dto.getProductName(), dto.getAddress());
            if (comment.length() >= 300) {
                comment = comment.substring(0, 299);
            }
            entity.setComment(comment);
        }

        return entity;
    }

    private Integer getReloadDay(int index) {
        String[] listReloadDay = dayReload.split(",");
        return Integer.valueOf(listReloadDay[index]);
    }

    private String getStatusName(Integer status) {
        String result = "";
        switch (status) {
            case 1:
                result = "new";
                break;
            case 2:
                result = "approved";
                break;
            case 3:
                result = "rejected";
                break;
            case 4:
                result = "duplicated";
                break;
            case 5:
                result = "trash";
                break;
            case 6:
                result = "closed";
                break;
            case 7:
                result = "unreachable";
                break;
            case 8:
                result = "callback consult";
                break;
            case 9:
                result = "callback propect";
                break;
            case 10:
                result = "busy";
                break;
            case 11:
                result = "noanswer";
                break;
            case 12:
                result = "urgent";
                break;

        }

        return result;
    }
}
