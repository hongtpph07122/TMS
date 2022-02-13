package com.tms.api.service.impl;

import com.tms.api.dto.PbxCallingBody;
import com.tms.api.dto.PbxCallingResponse;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Request.PhoneCallRequestDTO;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.helper.*;
import com.tms.api.repository.LeadRepository;
import com.tms.api.service.LeadService;
import com.tms.api.service.PhoneService;
import com.tms.api.utils.AudioUtils;
import com.tms.api.utils.NetworkingUtils;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.DBResponse;
import com.tms.dto.GetLeadParams;
import com.tms.entity.CLFresh;
import com.tms.entity.log.InsCdrV2;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LogService;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service(value = "phoneService")
@Transactional
public class PhoneServiceImpl implements PhoneService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneServiceImpl.class);

    private final StringRedisTemplate stringRedisTemplate;
    private final LogService logService;
    private final CLFreshService freshService;
    private final LeadService leadService;

    private final LeadRepository leadRepository;

    @Value("${config.ip-local-geo}")
    private String ipLocalGeo;

    @Autowired
    public PhoneServiceImpl(StringRedisTemplate stringRedisTemplate, LogService logService, CLFreshService freshService, LeadService leadService, LeadRepository leadRepository) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.logService = logService;
        this.freshService = freshService;
        this.leadService = leadService;
        this.leadRepository = leadRepository;
    }

    private PbxCallingResponse hookOnSwitchboard(PbxCallingBody pbxCallingBody, String url) {
        JSONObject jsonObject = new JSONObject(pbxCallingBody);
        PbxCallingResponse pbxCallingResponse;
        StringEntity paramsBody;
        try {
            paramsBody = new StringEntity(jsonObject.toString());
            pbxCallingResponse = HttpHelper.postResponse(url, PbxCallingResponse.class, paramsBody);
            if (!StringUtils.isEmpty(pbxCallingResponse.getChannelId())) {
                String channelIdLatest = pbxCallingResponse.getChannelId().replace("SIP/", "SIP_");
                pbxCallingResponse.setChannelId(channelIdLatest);
            }
            return pbxCallingResponse;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public PbxCallingResponse createPhoneCall(PhoneCallRequestDTO phoneCallRequestDTO, ObjectRequestDTO objectRequestDTO) {
        PbxCallingBody pbxCallingBody = new PbxCallingBody();
        pbxCallingBody.setFromNumber(phoneCallRequestDTO.getPhone());
        pbxCallingBody.setToNumber(phoneCallRequestDTO.getNumber());
        PbxCallingResponse pbxCallingResponse = hookOnSwitchboard(pbxCallingBody, phoneCallRequestDTO.getUrlDestination());
        logger.info("Pbx response: {}", LogHelper.toJson(pbxCallingResponse));
        String properties = "nolead";
        if (!ObjectUtils.isNull(phoneCallRequestDTO.getLeadId())) {
            GetLeadParams leadParams = new GetLeadParams();
            leadParams.setLeadId(phoneCallRequestDTO.getLeadId());
            leadParams.setOrgId(phoneCallRequestDTO.getOrgId());

            /* update actual call */
            com.tms.api.entity.ClFresh leadEntity = leadService.getById(phoneCallRequestDTO.getLeadId());
            if (Helper.isAgent(objectRequestDTO.getUser())) {
                if (ObjectUtils.allNotNull(leadEntity)) {
                    BigDecimal valueOfActualCall = leadService.findMaxActualCall(leadEntity.getLeadId());
                    leadEntity.setLeadId(leadEntity.getLeadId());
                    if (ObjectUtils.allNotNull(valueOfActualCall)) {
                        leadEntity.setActualCall(valueOfActualCall.intValue() + 1);
                    } else {
                        leadEntity.setActualCall(1);
                    }
                    leadRepository.save(leadEntity);
                }
            }

            DBResponse<List<CLFresh>> dbResponse = freshService.getLead(phoneCallRequestDTO.getSessionId(), leadParams);
            if (CollectionUtils.isEmpty(dbResponse.getResult())) {
                logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
            }
            assert pbxCallingResponse != null;
            String linkPlayback = pbxCallingResponse.getPlayback();
            CLFresh freshLatest = dbResponse.getResult().get(0);
            InsCdrV2 cdrParams = new InsCdrV2();
            cdrParams.setOrgId(phoneCallRequestDTO.getOrgId());
            cdrParams.setChannel(pbxCallingResponse.getChannelId());
            cdrParams.setUserId(phoneCallRequestDTO.getUserId());
            cdrParams.setLeadId(freshLatest.getLeadId());
            cdrParams.setLeadName(freshLatest.getName());
            cdrParams.setLeadPhone(freshLatest.getPhone());
            cdrParams.setPlaybackUrl(linkPlayback);
            cdrParams.setCallNote(pbxCallingResponse.getDialStatus());
            cdrParams.setDuration(0.00);
            logger.info("Insert CDR Params: {}", LogHelper.toJson(cdrParams));
            logService.insCdrV2(phoneCallRequestDTO.getSessionId(), cdrParams);
            properties = String.valueOf(phoneCallRequestDTO.getLeadId());
        } else {
            properties += phoneCallRequestDTO.getNumber();
        }
        String keys = RedisHelper.createRedisKey(Const.REDIS_PREFIX_CALLING, phoneCallRequestDTO.getOrgId(), String.valueOf(phoneCallRequestDTO.getUserId()));
        RedisHelper.saveRedis(stringRedisTemplate, keys, properties, phoneCallRequestDTO.getNumber() + "|" + Helper.currentEpoch());
        return pbxCallingResponse;
    }

    @Override
    public double snagAsDurations(String linkPlayback) {
        if (StringUtils.isEmpty(linkPlayback)) {
            return 0.00;
        }
        String host = NetworkingUtils.snagPublicIPAsUrl(linkPlayback, "1");
        assert host != null;
        String currentLinkPlayback = linkPlayback.replace(host, ipLocalGeo);
        double duration = AudioUtils.getAudioDuration(currentLinkPlayback);
        return duration;
    }
}
