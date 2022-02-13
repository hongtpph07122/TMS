package com.tms.api.rest;


import com.tms.api.dto.PbxCallingResponse;
import com.tms.api.dto.PbxHangupBody;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Request.PhoneCallRequestDTO;
import com.tms.api.helper.Const;
import com.tms.api.helper.HttpHelper;
import com.tms.api.helper.RedisHelper;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.PhoneService;
import com.tms.entity.CLCallback;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LogService;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Scope("prototype")
@RequestMapping("/phone")
public class PhoneController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PhoneController.class);

    @Value("${pbx.url}")
    private String PBX_WS_URL;
    private String _CALL_PATH = "/call";
    private String _HANGUP_PATH = "/hangup";

    @Value("${config.check-connection:false}")
    private Boolean checkConnection;

    @Value("${config.write-log-call-id:false}")
    public boolean writeLogCallId;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    LogService logService;

    @Autowired
    CLFreshService freshService;

    @Autowired
    PhoneService phoneService;


    @GetMapping("/{number}")
    public TMSResponse<?> getCallback(@PathVariable String number, @RequestParam(required = false) Integer leadId) {
        PhoneCallRequestDTO phoneCallRequestDTO = new PhoneCallRequestDTO();
        String toPhone = "";
        if (number.contains("+") || number.contains("-") || number.contains(" ")){
            toPhone = number.replace("+","").replace("-", "").replace(" ", "");
        } else {
            toPhone = number;
        }
        ObjectRequestDTO objectRequestDTO = new ObjectRequestDTO();
        phoneCallRequestDTO.setLeadId(leadId);
        phoneCallRequestDTO.setNumber(toPhone);
        phoneCallRequestDTO.setOrgId(getCurOrgId());
        phoneCallRequestDTO.setPhone(_curUser.getPhone());
        phoneCallRequestDTO.setUserId(_curUser.getUserId());
        phoneCallRequestDTO.setUrlDestination(PBX_WS_URL.concat(_CALL_PATH));
        phoneCallRequestDTO.setSessionId(SESSION_ID);
        objectRequestDTO.setUser(_curUser);
        PbxCallingResponse pbxCallingResponse = phoneService.createPhoneCall(phoneCallRequestDTO, objectRequestDTO);

        if (checkConnection || writeLogCallId){
            try{
                String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_MAKE_CALL, getCurOrgId(), toPhone, ":" + pbxCallingResponse.getChannelId().substring(4,8));
                RedisHelper.saveRedis(stringRedisTemplate, String.valueOf(leadId), key, 60 * 60, true);
            } catch (Exception e) {
                logger.error("[CAll ERR]: " + leadId + " " + e.getMessage());
            }
        }
        return TMSResponse.buildResponse(pbxCallingResponse, 1, pbxCallingResponse.isStatus() ? "OK" : "False", 200);
    }

    @GetMapping("/hangup/{ext}")
    public TMSResponse<?> hangup(@PathVariable String ext) throws IOException {
        logger.info("---------- {}", ext);
        ext = ext.replace("SIP_", "SIP/");
        PbxHangupBody pbxHangupBody = new PbxHangupBody();
        pbxHangupBody.setChannel(ext);
        String url = PBX_WS_URL + _HANGUP_PATH;
        JSONObject jsBody = new JSONObject(pbxHangupBody);
        StringEntity paramsBody = new StringEntity(jsBody.toString());
        PbxCallingResponse response = HttpHelper.postResponse(url, PbxCallingResponse.class, paramsBody);
        return TMSResponse.buildResponse(response, 1, response.isStatus() ? "OK" : "False", 200);
    }

    @PutMapping("/{number}")
    public CLCallback updateCallback(@PathVariable Integer agentId) {
        return new CLCallback();
    }

}
