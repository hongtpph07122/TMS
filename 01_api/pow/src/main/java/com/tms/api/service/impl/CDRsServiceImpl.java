package com.tms.api.service.impl;

import com.tms.api.dto.GetCdrResponse;
import com.tms.api.dto.Response.CDRsResponseDTO;
import com.tms.api.entity.CdrEntity;
import com.tms.api.helper.LogHelper;
import com.tms.api.repository.CdrRepository;
import com.tms.api.service.CDRsService;
import com.tms.api.service.PhoneService;
import com.tms.api.utils.DocsUtils;
import com.tms.dto.DBResponse;
import com.tms.dto.GetCdrResp;
import com.tms.dto.GetCdrV2;
import com.tms.service.impl.CLProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class CDRsServiceImpl implements CDRsService {

    private static final Logger logger = LoggerFactory.getLogger(CDRsServiceImpl.class);

    private final CdrRepository cdrRepository;
    private final PhoneService phoneService;
    private final CLProductService clProductService;

    @Value("${config.country}")
    private String currentCountry;

    @Autowired
    public CDRsServiceImpl(CdrRepository cdrRepository, PhoneService phoneService, CLProductService clProductService) {
        this.cdrRepository = cdrRepository;
        this.phoneService = phoneService;
        this.clProductService = clProductService;
    }

    @Override
    public List<GetCdrResponse> findAllCdr(List<GetCdrResp> cdrRequestList) {
        List<GetCdrResponse> cdrRespList = new ArrayList<>();
        if (CollectionUtils.isEmpty(cdrRequestList)) {
            return new ArrayList<>();
        }
        for (GetCdrResp cdrResp : cdrRequestList) {
            GetCdrResponse cdrRespV2 = GetCdrResponse.buildMappedToDTO(cdrResp);
            cdrRespList.add(cdrRespV2);
        }
        return cdrRespList;
    }

    @Override
    public CdrEntity findOneByChannel(String channel) {
        if (StringUtils.isEmpty(channel)) {
            return null;
        }
        java.time.LocalDateTime nowday = java.time.LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<CdrEntity> cdrs = cdrRepository.findOneByChannel(channel, Timestamp.valueOf(nowday));
        if (cdrs.isEmpty()) {
            return null;
        } else {
            return cdrs.get(0);
        }

    }

    @Override
    public void updateOne(CdrEntity cdrEntity) {
        cdrRepository.save(cdrEntity);
    }

    @Override
    public void createOne(String channel) {
        CdrEntity cdrEntity = new CdrEntity();
        Integer organizationId = snagAsGEO().get(currentCountry.toLowerCase().trim());

        GetCdrV2 cdrParam = new GetCdrV2();
        cdrParam.setOrgId(organizationId);
        cdrParam.setChannel(channel);
        cdrParam.setLimit(1);
        DBResponse<List<GetCdrResp>> cdrList = clProductService.getCdrV2(null, cdrParam);
        GetCdrResp cdrResp = cdrList.getResult().get(0);

        logger.info("Log CDR: current phone number: {}\r\nLog CDR: current orgId: {}\r\nLog CDR: current channel: {}",
                cdrResp.getLeadPhone(), organizationId, cdrResp.getChannel());

        cdrEntity.setLeadId(cdrResp.getLeadId());
        cdrEntity.setLeadName(cdrResp.getLeadName());
        cdrEntity.setLeadPhone(cdrResp.getLeadPhone().substring(1).trim());

        cdrEntity.setCallId(cdrResp.getCallId());
        cdrEntity.setOrgId(organizationId);
        cdrEntity.setChannel(channel.replace("SIP/", "SIP_"));
        cdrEntity.setCallNote(cdrResp.getCallStatusName());
        cdrEntity.setStarttime(cdrResp.getStarttime());
        cdrEntity.setCreatetime(cdrResp.getCreatetime());
        cdrEntity.setPlaybackUrl(cdrResp.getPlaybackUrl());
        cdrEntity.setDuration(phoneService.snagAsDurations(cdrResp.getPlaybackUrl()));

        cdrRepository.save(cdrEntity);
        logger.info("Log CDR: body cdr: {}", LogHelper.toJson(cdrEntity));

    }

    @Override
    public void saveOrUpdate(List<CdrEntity> entities) {
        cdrRepository.saveAll(entities);
    }

    @Override
    public List<CdrEntity> getByChanel(List<String> channels) {
        java.time.LocalDateTime nowday = java.time.LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        List<CdrEntity> results = cdrRepository.findByChannel(channels, Timestamp.valueOf(nowday));
        return results;
    }

    @Override
    public byte[] exportCDRsExcel(String sheetName, List<GetCdrResponse> cdrResponses) {
        List<CDRsResponseDTO> cdRsResponseDTOS = cdrResponses.stream().map(CDRsResponseDTO::buildMapped).collect(Collectors.toList());
        return DocsUtils.buildDocsDope(cdRsResponseDTOS, CDRsResponseDTO.class, sheetName, 100);
    }

    private Map<String, Integer> snagAsGEO() {
        Map<String, Integer> map = new HashMap<>();
        map.put("vn", 4);
        map.put("id", 9);
        map.put("th", 10);
        return map;
    }

    private static String snapGenerateSession() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }
}
