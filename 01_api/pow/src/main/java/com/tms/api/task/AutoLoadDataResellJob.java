package com.tms.api.task;

import com.tms.api.dto.LeadInfoDto;
import com.tms.api.entity.ClFresh;
import com.tms.api.helper.DateHelper;
import com.tms.api.helper.EnumType;
import com.tms.api.repository.LeadRepository;
import com.tms.api.response.TMSResponse;
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
public class AutoLoadDataResellJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoLoadDataResellJob.class);
    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private LeadService leadService;

    private static final Integer CP_FRESH_ME = 567;
    private static final Integer CP_RESELL_AR = 571;
    private static final Integer CP_RESELL_RJ = 572;
    private static final Integer CP_RESELL_ETC = 573;
    private static final Integer CL_RESELL_AR = 10;
    private static final Integer CL_RESELL_RJ = 11;
    private static final Integer CL_RESELL_ETC = 12;
    private static final Integer CL_RESELL_ME = 4;
    private static final Integer CP_RESELL_ME = 565;

    @Value("${config.auto-load-data}")
    public boolean isAutoLoadData;

    @Value("${config.day-reload}")
    public String dayReload;


    @Scheduled(cron = "0 30 0 * * ?")
    public TMSResponse autoLoadDataResell(){
        LOGGER.info("**********START AUTO LOAD LEAD RESELL AR-RJ-ETC*************");
        if(!isAutoLoadData) {
            LOGGER.info("********AUTO LOAD DATA OFF*********");
            return null;
        }
        LocalDateTime dayGetLeadStart = LocalDateTime.now().minusDays(getReloadDay(0) + 1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime dayGetLeadEnd = LocalDateTime.now().minusDays(getReloadDay(0)).withHour(0).withMinute(0).withSecond(0);

        LocalDateTime checkDuplicateTimeStart = LocalDateTime.now().minusDays(getReloadDay(0)).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime checkduplicateTimeEnd = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dayGetLeadStartStr = dayGetLeadStart.format(formatter);
        String dayGetLeadEndStr = dayGetLeadEnd.format(formatter);
        String checkDuplicateTimeStartStr = checkDuplicateTimeStart.format(formatter);
        String checkduplicateTimeEndStr = checkduplicateTimeEnd.format(formatter);
        List<ClFresh> insFresh = new ArrayList<>();
        int approved = 0;
        int rejected = 0;
        int close = 0;

        LOGGER.info("startTime: " + dayGetLeadStart + " endTime: " + dayGetLeadEnd + " day reload: " + getReloadDay(0));

        TMSResponse response = new TMSResponse();
        List<Integer> campainListAR = new ArrayList<>();
        campainListAR.add(CP_RESELL_ETC);
        campainListAR.add(CP_RESELL_ME);
        campainListAR.add(CP_RESELL_RJ);
        campainListAR.add(CP_FRESH_ME);
        List<Integer> leadStatusAR = new ArrayList<>();
        leadStatusAR.add(EnumType.LEAD_STATUS.APPROVED.getValue());
        List<LeadInfoDto> listLeadAR = new ArrayList<>();
        listLeadAR = leadService.getLeadInfo(leadStatusAR,campainListAR, dayGetLeadStartStr, dayGetLeadEndStr);

        LOGGER.info("get Lead from Resell ME AR: " + listLeadAR.size());
        List<Integer> statusCheckDupAR = new ArrayList<>();
        statusCheckDupAR.add(EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue());
        statusCheckDupAR.add(EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue());
        statusCheckDupAR.add(EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue());
        statusCheckDupAR.add(EnumType.LEAD_STATUS.APPROVED.getValue());
        HashMap<String, String> checkDuplicateAR = leadService.getLeadCheckDuplicate(statusCheckDupAR, campainListAR, checkDuplicateTimeStartStr, checkduplicateTimeEndStr);
        HashMap<String, String> freshARNew = new HashMap<>();
        for (LeadInfoDto dto : listLeadAR){
            ClFresh fresh = new ClFresh();
            String keyCheck = dto.getPhone();
            if (!checkDuplicateAR.containsKey(keyCheck)) {
                fresh = this.createLead(dto, CP_RESELL_AR, CL_RESELL_AR);
                insFresh.add(fresh);
                freshARNew.put(fresh.getPhone(), String.format("%s-%s", fresh.getPhone(), fresh.getName()));
                approved ++;
            }
        }

        List<Integer> campainListRJ = new ArrayList<>();
        campainListRJ.add(CP_FRESH_ME);
        List<Integer> leadStatusRJ = new ArrayList<>();
        leadStatusRJ.add(EnumType.LEAD_STATUS.REJECTED.getValue());

        List<LeadInfoDto> listLeadRJ = leadService.getLeadInfo(leadStatusRJ,campainListRJ, dayGetLeadStartStr, dayGetLeadEndStr);
        LOGGER.info("get Lead from Resell ME RJ: " + listLeadAR.size());
        HashMap<String, String> checkDuplicateRJ = leadService.getLeadCheckDuplicate(leadStatusRJ,campainListRJ, checkDuplicateTimeStartStr, checkduplicateTimeEndStr);
        checkDuplicateRJ.putAll(freshARNew);
        for (LeadInfoDto dto : listLeadRJ){
            ClFresh fresh = new ClFresh();
            String keyCheck = dto.getPhone();
            if (!checkDuplicateRJ.containsKey(keyCheck)) {
                fresh = this.createLead(dto, CP_RESELL_RJ, CL_RESELL_RJ);
                insFresh.add(fresh);
                rejected ++;
            }
        }

        List<Integer> campainListETC = new ArrayList<>();
        campainListETC.add(CP_FRESH_ME);
        List<Integer> leadStatusETC = new ArrayList<>();
        leadStatusETC.add(EnumType.LEAD_STATUS.CLOSED.getValue());

        List<LeadInfoDto> listLeadETC = leadService.getLeadInfo(leadStatusETC,campainListETC, dayGetLeadStartStr, dayGetLeadEndStr);
        LOGGER.info("get Lead from Resell ME ETC: " + listLeadAR.size());
        HashMap<String, String> checkDuplicateETC = leadService.getLeadCheckDuplicate(leadStatusETC, campainListETC, checkDuplicateTimeStartStr, checkduplicateTimeEndStr);
        checkDuplicateETC.putAll(freshARNew);
        for (LeadInfoDto dto : listLeadETC){
            ClFresh fresh = new ClFresh();
            String keyCheck = dto.getPhone();
            if (!checkDuplicateETC.containsKey(keyCheck)) {
                fresh = this.createLead(dto, CP_RESELL_ETC, CL_RESELL_ETC);
                insFresh.add(fresh);
                close ++;
            }
        }

        String message = "Import approved: " + approved + ", rejected: " + rejected + ", close: " + close;

        LOGGER.info("******START INSERT TO CAMPAIN*******");
        if (insFresh != null && insFresh.size() > 0){
            leadRepository.saveAll(insFresh);
        }
        LOGGER.info(message);

        return response.buildResponse(null, insFresh.size(), message, 200);
    }

    private ClFresh createLead(LeadInfoDto dto, Integer cpId, Integer callingListId){

        Date now = DateHelper.nowToTimestamp();
        ClFresh entity = new ClFresh();
        entity.setName(dto.getName());
        entity.setPhone(dto.getPhone());
        entity.setProdName(dto.getProductName());
        entity.setProdId(dto.getProdId());
        String address = dto.getAddress();
        if ( address.length() >= 200){
            address = address.substring(0,199);
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
        String userDefin04 = String.format("%s-%s",dto.getLeadId(),getStatusName(dto.getLeadStatus()));
        entity.setUserDefin04(String.format("%s|%s",dto.getUserDefin04(),userDefin04));
        if (dto.getLeadStatus().compareTo(EnumType.LEAD_STATUS.APPROVED.getValue()) == 0){
            String comment = String.format("%s|%s|%s|%s|%s",dto.getPackageListItem(),dto.getAmount().toString(),dto.getPaymentMethod(),dto.getDoStatus(),dto.getDoAddress());
            if(comment.length() >= 300){
                comment = comment.substring(0,299);
            }
            entity.setComment(comment);
        }
        if (dto.getLeadStatus().compareTo(EnumType.LEAD_STATUS.REJECTED.getValue()) == 0){
            String comment = String.format("%s|%s",dto.getProductName(),dto.getAddress());
            if(comment.length() >= 300){
                comment = comment.substring(0,299);
            }
            entity.setComment(comment);
        }

        return entity;
    }

    private Integer getReloadDay(int index){
        String[] listReloadDay = dayReload.split(",");
        Integer result = Integer.valueOf(listReloadDay[index]);

        return result;
    }

    private String getStatusName(Integer status){
        String result = "";
        switch (status){
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
