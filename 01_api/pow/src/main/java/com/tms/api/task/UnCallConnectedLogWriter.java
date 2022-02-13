package com.tms.api.task;

import com.tms.api.entity.CdrAllEntity;
import com.tms.api.entity.UnCallConnected;
import com.tms.api.repository.CdrAllRepository;
import com.tms.api.repository.UnCallConnectedRepository;
import com.tms.service.impl.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UnCallConnectedLogWriter {
    private final Logger logger = LoggerFactory.getLogger(UnCallConnectedLogWriter.class);

    @Value("${config.orgId-close}")
    private int _curOrgId;

    @Value("${config.check-connection:false}")
    private Boolean checkConnection;

    public String taskSession = UUID.randomUUID().toString();

    @Autowired
    private LogService logService;

    @Autowired
    private UnCallConnectedRepository connectedRepository;

    @Autowired
    private CdrAllRepository cdrAllRepository;

//        @Scheduled(fixedDelayString = "${config.basket-time}")
    @Scheduled(cron = "* * 3 * *  ?")
    public void updatePlaybackUrl() {
        if (!checkConnection){
            return;
        }
        try {
            logger.info("*** Start update playback url ***");
            List<UnCallConnected> response = connectedRepository.findUnCallConnectedsByIsUpdatePlaybackUrlAndOrgId(0, _curOrgId);
            Map<String, UnCallConnected> map = new HashMap<>();

            for (UnCallConnected e : response) {
                map.put(e.getUniqueId(),e);
            }

            if (response != null) {
                List<String> lstUniqueId = new ArrayList<>();
                for (int i = 0; i < response.size(); i++) {
                    lstUniqueId.add(response.get(i).getUniqueId());
                }

                List<CdrAllEntity> cdrAllResponse = cdrAllRepository.findByUniqueId(lstUniqueId);
                for (CdrAllEntity res : cdrAllResponse) {
                    UnCallConnected unCallConnected = map.get(res.getUniqueId());
                    unCallConnected.setPlaybackUrl(res.getUrlPlayback());
                    unCallConnected.setIsUpdatePlaybackUrl(1);
                    unCallConnected.setModifyDate(new Date());
                    connectedRepository.save(unCallConnected);
                }
            }
        } catch (Exception e) {
            logger.error("Update playback url err: " + e.getMessage());
        }
    }
}
