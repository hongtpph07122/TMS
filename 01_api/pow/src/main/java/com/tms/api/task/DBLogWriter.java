package com.tms.api.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.api.helper.Const;
import com.tms.api.helper.RedisHelper;
import com.tms.dto.*;
import com.tms.entity.CLFresh;
import com.tms.entity.SaleOrder;
import com.tms.entity.log.*;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.DeliveryOrderService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DBLogWriter {
    private static final Logger log = LoggerFactory.getLogger(DBLogWriter.class);
    
    private final LogService logService;
    private final DeliveryOrderService doService;
    private final CLFreshService freshService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ModelMapper modelMapper;

    @Value("${config.run-writelogdb}")
    private Boolean isRunLogDb;
    @Value("${config.org-id:0}")
    private int orgId;
    @Value("${config.write-log-call-id:false}")
    public boolean writeLogCallId;

    @Autowired
    public DBLogWriter(LogService logService,
                       DeliveryOrderService doService,
                       CLFreshService freshService,
                       StringRedisTemplate stringRedisTemplate,
                       ModelMapper modelMapper) {
        this.logService = logService;
        this.doService = doService;
        this.freshService = freshService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.modelMapper = modelMapper;
    }

    @Async
    public void writeDOStatusLog(int userId, int doId, int newStatus, String comment) {
        if(!isRunLogDb)
            return;
        try {
            log.info("Start Executing writeDOStatusLog method asynchronously. " + Thread.currentThread().getName());
            String taskSession = UUID.randomUUID().toString();
            
            String doJson = "{}";
            GetDoNew doParam = new GetDoNew();
            doParam.setDoId(doId);
            DBResponse<List<GetDoNewResp>> doList = doService.getDoNew(taskSession, doParam);
            if (doList != null && !doList.getResult().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                doJson = mapper.writeValueAsString(doList.getResult().get(0));
            }
            
            LogDeliveryOrder dolog = new LogDeliveryOrder();
            dolog.setDoId(doId);
            dolog.setNewStatus(newStatus);
            dolog.setUpdateby(userId);
            dolog.setJsonLog(doJson);
            dolog.setComment(comment);
            logService.logDeliveryOrder(taskSession, dolog);
            
            log.info("Finish Executing writeDOStatusLog method asynchronously. " + Thread.currentThread().getName());
        } catch (final Exception e) {
            log.error("DBLogWriter Error: ", e);
        }
    }
    
   
    
    @Async
    public void writeSOStatusLog(int userId, int soId, int newStatus, String comment) {
        if(!isRunLogDb)
            return;
        try {
            log.info("Start Executing writeSOStatusLog method asynchronously. " + Thread.currentThread().getName());
            String taskSession = UUID.randomUUID().toString();
            
            String doJson = "{}";
            GetSOParams soParam = new GetSOParams();
            soParam.setSoId(soId);
            DBResponse<List<SaleOrder>> soList = doService.getSaleOrder(taskSession, soParam);
            if (soList != null && !soList.getResult().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                doJson = mapper.writeValueAsString(soList.getResult().get(0));
            }
            
            LogSaleOrder solog = new LogSaleOrder();
            solog.setSoId(soId);
            solog.setNewStatus(newStatus);
            solog.setUserId(userId);
            solog.setJsonLog(doJson);
            solog.setComment(comment);
            logService.logSaleOrder(taskSession, solog);
            
            log.info("Finish Executing writeSOStatusLog method asynchronously. " + Thread.currentThread().getName());
        } catch (final Exception e) {
            log.error("DBLogWriter Error: ", e);
        }
    }

    @Async
    public void writeSOStatusLogV2(int userId, int soId, int newStatus, String comment) {
        if(!isRunLogDb)
            return;
        try {
            log.info("Start Executing writeSOStatusLog method asynchronously. " + Thread.currentThread().getName());
            String taskSession = UUID.randomUUID().toString();

            String doJson = "{}";
            GetSOV4 soParam = new GetSOV4();
            soParam.setSoId(soId);
            DBResponse<List<SaleOrder>> soList = doService.getSaleOrderV4(taskSession, soParam);
            if (soList != null && !soList.getResult().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                doJson = mapper.writeValueAsString(soList.getResult().get(0));
            }

            LogSaleOrder solog = new LogSaleOrder();
            solog.setSoId(soId);
            solog.setNewStatus(newStatus);
            solog.setUserId(userId);
            solog.setJsonLog(doJson);
            solog.setComment(comment);
            logService.logSaleOrder(taskSession, solog);

            log.info("Finish Executing writeSOStatusLog method asynchronously. " + Thread.currentThread().getName());
        } catch (final Exception e) {
            log.error("DBLogWriter Error: ", e);
        }
    }

    @Async
    public void writeLeadStatusLog(int userId, int leadId, int newStatus, String comment) {
        if(!isRunLogDb)
            return;
        try {
            log.info("Start Executing writeLeadStatusLog method asynchronously. " + Thread.currentThread().getName());
            String taskSession = UUID.randomUUID().toString();
            
            String leadJson = "{}";
            GetLeadParams clFreshParams = new GetLeadParams();
            clFreshParams.setLeadId(leadId);
            DBResponse<List<CLFresh>> leads = freshService.getLead(taskSession, clFreshParams);
            if (leads != null && !leads.getResult().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                leadJson = mapper.writeValueAsString(leads.getResult().get(0));
            }
            
            LogLead logLead = new LogLead();
            logLead.setLeadId(leadId);
            logLead.setUserId(userId);
            logLead.setComment(comment);
            logLead.setNewValue(String.valueOf(newStatus));
            logLead.setOnField("lead_status");
            logLead.setJsonLog(leadJson);
            logService.logLead(taskSession, logLead);
            
            log.info("Finish Executing writeLeadStatusLog method asynchronously. " + Thread.currentThread().getName());
        } catch (final Exception e) {
            log.error("DBLogWriter Error: ", e.getMessage());
        }
    }
    

    @Async
    public void writeLeadStatusLogV2(int userId, CLFresh lead,Integer leadId, int newStatus, String comment) {
        if(!isRunLogDb)
            return;
        try {
            log.info("Start Executing writeLeadStatusLog method asynchronously. " + Thread.currentThread().getName());
            String taskSession = UUID.randomUUID().toString();
            String leadJson = "{}"; 
            if (lead != null ) {
                ObjectMapper mapper = new ObjectMapper();
                leadJson = mapper.writeValueAsString(lead);
            }
            
            LogLead logLead = new LogLead();
            logLead.setLeadId(leadId);
            logLead.setUserId(userId);
            logLead.setComment(comment);
            logLead.setNewValue(String.valueOf(newStatus));
            logLead.setOnField("lead_status");
            logLead.setJsonLog(leadJson);
            DBResponse res= logService.logLead(taskSession, logLead);
            

            log.info("Finish Executing writeLeadStatusLog method asynchronously. " + Thread.currentThread().getName());
        } catch (final Exception e) {
            log.error("DBLogWriter Error: ", e.getMessage());
        }
    }

    @Async
    public void writeLeadStatusLogV3(int userId, CLFresh lead, int newStatus, String comment, String ssId) {
        if (!isRunLogDb)
            return;
        log.info("Start Executing writeLeadStatusLog method asynchronously. " + Thread.currentThread().getName());
        ObjectMapper mapper = new ObjectMapper();
        String leadJson = null;
        try {
            leadJson = mapper.writeValueAsString(lead);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }

        LogLead logLead = new LogLead();
        logLead.setLeadId(lead.getLeadId());
        logLead.setUserId(userId);
        logLead.setComment(comment);
        logLead.setNewValue(String.valueOf(newStatus));
        logLead.setOnField("lead_status");
        logLead.setJsonLog(leadJson);
        logService.logLead(ssId, logLead);

        log.info("Finish Executing writeLeadStatusLog method asynchronously. " + Thread.currentThread().getName());
    }

    @Async
    public void writeLeadStatusLogV4(int userId, String phone, int leadId, int newStatus, String comment, String ssId) {
        if(!isRunLogDb)
            return;
        try {
            log.info("Start Executing writeLeadStatusLog method asynchronously. " + Thread.currentThread().getName() + " " + leadId);

            String callId = null;
            String callIds = null;
            if (writeLogCallId) {
                String phoneNumberOfAgent = phone.substring(4, 8);
                String keyGetCallId = RedisHelper.createRedisKey(Const.REDIS_PREFIX_CALL_ID, orgId, leadId + ":" + phoneNumberOfAgent);
                callIds = RedisHelper.getKey(stringRedisTemplate, keyGetCallId);
                callId = getFirstCallIdSplitByComma(callIds);

                RedisHelper.deleteKey(stringRedisTemplate, keyGetCallId);
            }

            String leadJson = "{}";
            GetLeadParams clFreshParams = new GetLeadParams();
            clFreshParams.setLeadId(leadId);
            DBResponse<List<CLFresh>> leads = freshService.getLead(ssId, clFreshParams);
            if (leads != null && !leads.getResult().isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                CLFreshLog clFreshLog = modelMapper.map(leads.getResult().get(0), CLFreshLog.class);
                clFreshLog.setCallId(callIds);

                leadJson = mapper.writeValueAsString(clFreshLog);
            }

            LogLeadV3 logLead = new LogLeadV3();
            logLead.setLeadId(leadId);
            logLead.setUserId(userId);
            logLead.setComment(comment);
            logLead.setNewValue(String.valueOf(newStatus));
            logLead.setOnField("lead_status");
            logLead.setJsonLog(leadJson);
            logLead.setCallId(callId);
            logService.logLeadV3(ssId, logLead);

            log.info("Finish Executing writeLeadStatusLog method asynchronously. " + Thread.currentThread().getName() + " " + leadId);
        } catch (Exception e) {
            log.error("DBLogWriter Error leadId = {} {}", leadId, e.getMessage(), e);
        }
    }

    private String getFirstCallIdSplitByComma(String callIds) {
        if (callIds == null)
            return null;

        int firstCommaPosition = callIds.indexOf(",");
        if (firstCommaPosition == -1)
            return callIds;
        else
            return callIds.substring(0, firstCommaPosition);
    }
    
    @Async
    public void writeAgentActivityLog(int userId, String activity, String objType, int objId, String onField, String newValue) {
        if(!isRunLogDb)
            return;
        try {
//            log.info("Start Executing writeAgentActivityLog method asynchronously. " + Thread.currentThread().getName());
            String taskSession = UUID.randomUUID().toString();
            
            String leadJson = "{}";
//            GetLeadParams clFreshParams = new GetLeadParams();
//            clFreshParams.setLeadId(leadId);
//            DBResponse<List<CLFresh>> leads = freshService.getLead(taskSession, clFreshParams);
//            if (leads != null && !leads.getResult().isEmpty()) {
//                ObjectMapper mapper = new ObjectMapper();
//                leadJson = mapper.writeValueAsString(leads.getResult().get(0));
//            }
            
            LogAgentActivity logData = new LogAgentActivity();
            logData.setUserid(userId);
            logData.setActivity(activity);
            logData.setObjectType(objType);
            logData.setObjectId(objId);
            logData.setOnField(onField);
            logData.setNewProperty(newValue);
            logData.setJsonLog(leadJson);
            logService.logAgentActivity(taskSession, logData);
            
//            log.info("Finish Executing writeAgentActivityLog method asynchronously. " + Thread.currentThread().getName());
        } catch (final Exception e) {
            log.error("DBLogWriter Error: ", e);
        }
    }
}
