package com.tms.api.task;

import com.tms.api.dto.DurationDTO;
import com.tms.api.entity.CdrAllEntity;
import com.tms.api.entity.CdrEntity;
import com.tms.api.helper.HttpHelper;
import com.tms.api.helper.LogHelper;
import com.tms.api.repository.CdrAllRepository;
import com.tms.api.repository.CdrRepository;
import com.tms.api.service.CdrAllService;
import com.tms.api.service.CDRsService;
import com.tms.api.service.PhoneService;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LoadCDRDurationJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadCDRDurationJob.class);

    final
    CdrAllService cdrAllService;

    final
    CDRsService cdrService;

    final
    PhoneService phoneService;

    final
    CdrAllRepository cdrAllRepository;

    final
    CdrRepository cdrRepository;

    @Value("${config.country}")
    private String currentCountry;
    @Value("${config.url-read-duration}")
    private String urlReadDuration;
    @Value("${config.cdr-autoloadduration}")
    private Boolean isAutoLoadDuration;

    @Autowired
    public LoadCDRDurationJob(CdrAllService cdrAllService, CDRsService cdrService, PhoneService phoneService, CdrAllRepository cdrAllRepository, CdrRepository cdrRepository) {
        this.cdrAllService = cdrAllService;
        this.cdrService = cdrService;
        this.phoneService = phoneService;
        this.cdrAllRepository = cdrAllRepository;
        this.cdrRepository = cdrRepository;
    }

    @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 5000)
    public void processUpdateCdr() throws IOException {
        LOGGER.info("================ Start update duration Cdr ==================");
        if (!isAutoLoadDuration){
            LOGGER.info("============= Auto load duration is off ==============");
            return;
        }
        List<CdrAllEntity> cdrAllEntities = cdrAllService.findByStatus(0);
        String channel = "";
        List<CdrEntity> updateCdrs = new ArrayList<>();
        List<CdrEntity> createCdrs = new ArrayList<>();
        List<String> channels = new ArrayList<>();
        HashMap<String, CdrEntity> mapCdr = new HashMap<>();
        List<String> playbackUrls = new ArrayList<>();

        if (!cdrAllEntities.isEmpty()) {
            for (CdrAllEntity cdrAll : cdrAllEntities) {
                channels.add(cdrAll.getChannel().replace("/", "_"));
            }

            List<CdrEntity> getCdrs = cdrService.getByChanel(channels);
            for (CdrEntity cdrEntity : getCdrs) {
                mapCdr.put(cdrEntity.getChannel(), cdrEntity);
            }
            for (CdrAllEntity cdrAll : cdrAllEntities) {
                cdrAll.setUpdateStatus(1);
                channel = cdrAll.getChannel().replace("/", "_");
                if (mapCdr.containsKey(channel)) {
                    CdrEntity cdr = mapCdr.get(channel);
                    cdr.setCallNote(cdrAll.getDisposition());
                    updateCdrs.add(cdr);
                    playbackUrls.add(cdr.getPlaybackUrl());
                } else {
                    CdrEntity cdrEntity = this.createCdrByCdrAll(cdrAll);
                    createCdrs.add(cdrEntity);
                    playbackUrls.add(cdrEntity.getPlaybackUrl());
                }
            }

            HashMap<String, Double> durations = snagAsDurationPlayback(playbackUrls);
            for (CdrEntity entity : updateCdrs) {
                Double duration = durations.get(entity.getPlaybackUrl());
                entity.setDuration(duration);
                LOGGER.info("Cdr Update Entity: " + LogHelper.toJson(entity));
            }

            for (CdrEntity entity : createCdrs) {
                Double duration = durations.get(entity.getPlaybackUrl());
                entity.setDuration(duration);
                LOGGER.info("Cdr Create Entity: " + LogHelper.toJson(entity));
            }

            LOGGER.info(String.format("CdrAll update: %s, Cdr update: %s, Cdr create: %s", cdrAllEntities.size(), updateCdrs.size(), createCdrs.size()));
            cdrAllRepository.saveAll(cdrAllEntities);
            cdrRepository.saveAll(createCdrs);
            cdrRepository.saveAll(updateCdrs);
        }

    }

    private CdrEntity createCdrByCdrAll(CdrAllEntity cdrAll) {
        CdrEntity entity = new CdrEntity();
        entity.setCallId(cdrAll.getCallId());
        entity.setCallNote(cdrAll.getDisposition());
        entity.setChannel(cdrAll.getChannel().replace("/", "_"));
        entity.setCreatetime(cdrAll.getDateReceived());
        entity.setStarttime(cdrAll.getStartTime());
        entity.setOrgId(snagAsGEO().get(currentCountry.trim().toLowerCase()));
        if (cdrAll.getDestination().length() > 1)
            entity.setLeadPhone(cdrAll.getDestination().substring(1));
        entity.setPlaybackUrl(cdrAll.getUrlPlayback());

        return entity;
    }

    private Map<String, Integer> snagAsGEO() {
        Map<String, Integer> map = new HashMap<>();
        map.put("vn", 4);
        map.put("id", 9);
        map.put("th", 10);
        return map;
    }

    private HashMap<String, Double> snagAsDurationPlayback(List<String> playbackUrls) throws IOException {
        HashMap<String, Double> result = new HashMap<>();
        JSONArray json = new JSONArray(playbackUrls);
        StringEntity params = new StringEntity(json.toString(), ContentType.APPLICATION_JSON);
        List<DurationDTO> durationDTOS = HttpHelper.postResponseDuration(urlReadDuration, params);
        for (DurationDTO dto : durationDTOS) {
            result.put(dto.getFileName(), dto.getDuration());
        }

        return result;
    }
}
