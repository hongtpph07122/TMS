package com.tms.api.rest;

import com.tms.api.dto.*;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.api.dto.delivery.DeliveryDto;
import com.tms.api.dto.delivery.ProductDto;
import com.tms.api.entity.*;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.exception.TMSInvalidInputException;
import com.tms.api.helper.*;
import com.tms.api.request.DeliveriesOrderRequest;
import com.tms.api.request.LeadsRequest;
import com.tms.api.response.AppointmentDateResponse;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.*;
import com.tms.api.task.AutoReloadConfigTeam;
import com.tms.api.task.DBLogWriter;
import com.tms.api.utils.DateUtils;
import com.tms.api.utils.StringUtility;
import com.tms.api.variable.PatternEpochVariable;
import com.tms.dto.*;
import com.tms.entity.CLFresh;
import com.tms.entity.PDProduct;
import com.tms.entity.SaleOrder;
import com.tms.entity.Subdistrict;
import com.tms.entity.log.*;
import com.tms.model.Response.DeliveriesOrderResponseDTOV1;
import com.tms.service.impl.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@Scope("prototype")
@RequestMapping("/SO")
public class SOController extends BaseController {

	Logger logger = LoggerFactory.getLogger(SOController.class);

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private CLFreshService clFreshService;

	@Autowired
	private LCProvinceService provinceService;
	@Autowired
	private DeliveryOrderService deliveryOrderService;

	@Autowired
	private LogService logService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CLProductService clProductService;

	@Autowired
	private LCProvinceService lcProvinceService;

	@Autowired
	private SOService soService;

	@Autowired
	private CfCpCallStrategyService cfCpCallStrategy;

	@Autowired
	private LeadService leadService;

	@Autowired
	private ClManipulatedFreshService clManipulatedFreshService;

	@Autowired
	private DBLogWriter dbLog;

	@Autowired
	private CampaignService campaignService;

	@Autowired
	private MktDataService mktDataService;

	@Autowired
	private SynonymsConfigurationService synonymsService;

	private final AppointmentDateService appointmentDateService;

	private final DeliveryOrderRelatedService deliveryOrderRelatedService;

	private final LogAgentTraceService logAgentTraceService;

	@Value("${config.country}")
	public String _COUNTRY;

	@Value("${config.check-connection:false}")
	private Boolean checkConnection;

	@Autowired
	public SOController(
			AppointmentDateService appointmentDateService,
			DeliveryOrderRelatedService deliveryOrderRelatedService,
			LogAgentTraceService logAgentTraceService) {
		this.appointmentDateService = appointmentDateService;
		this.deliveryOrderRelatedService = deliveryOrderRelatedService;
		this.logAgentTraceService = logAgentTraceService;
	}

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	AutoReloadConfigTeam autoReloadConfigTeam;

	@PutMapping("/saveLeadInfo")
	public TMSResponse<?> saveLeadInfo(@RequestBody CLFresh clFresh) throws TMSException {
		AppointmentDateResponse appointmentDateResponse = appointmentDateService.init();
		String taskSession = UUID.randomUUID().toString();
		int orgId = getCurOrgId();
		String oldComment = "";
		boolean isApproved = false;
		if(clFresh.getLeadStatus() == EnumType.LEAD_STATUS.APPROVED.getValue()){
			if(Helper.isNullOrEmpty(clFresh.getProvince()) && !Helper.isNumeric(clFresh.getProvince()))
				throw new TMSInvalidInputException(ErrorMessage.INVALID_PARAM);
			if(Helper.isNullOrEmpty(clFresh.getDistrict()) && !Helper.isNumeric(clFresh.getDistrict()))
				throw new TMSInvalidInputException(ErrorMessage.INVALID_PARAM);
			if(Helper.isNullOrEmpty(clFresh.getSubdistrict()) && !Helper.isNumeric(clFresh.getSubdistrict()))
				throw new TMSInvalidInputException(ErrorMessage.INVALID_PARAM);
			isApproved = true;
		}
		// TODO validate leadStatus from client, only in SALE_ORDER_STATUS
		if (!EnumType.LEAD_STATUS.isContainsStatus(clFresh.getLeadStatus()) || clFresh.getLeadStatus() < 1){
			throw new TMSInvalidInputException(ErrorMessage.INVALID_PARAM);
		}

		if (appointmentDateResponse.isActive()) {
			if (!StringUtils.isEmpty(clFresh.getAppointmentDate())) {
				if (!appointmentDateService.isValid(clFresh.getAppointmentDate())) {
					return TMSResponse.buildResponse(null, 0, "Appointment date invalid.", 400);
				}
				LeadsRequest leadsRequest = new LeadsRequest();
				leadsRequest.setOverrideOnSaleOrder(true);
				leadsRequest.setOverrideOnDeliveryOrder(true);
				leadsRequest.setAppointmentDate(DateUtils.parse(clFresh.getAppointmentDate(), PatternEpochVariable.SHORT_BIBLIOGRAPHY_EPOCH_PATTERN));
				leadService.updateReminderCalls(clFresh.getLeadId(), leadsRequest);
			}

		}

		CLFresh clFreshNew = null;
		DBResponse<?> response = null;
		int user_id = _curUser.getUserId();
		int leadId = 0;
		boolean isDeleteCallbackSuccess = false;
		String messageDeletedCallbackFail = null;
		// check xem da connect toi customer chua
		if (checkConnection && EnumType.LEAD_STATUS.isUncall(clFresh.getLeadStatus().intValue())){
			String isMakeCallKey = RedisHelper.createRedisKey(Const.REDIS_PREFIX_MAKE_CALL, getCurOrgId(), clFresh.getLeadId().toString());
			String isMakeCallUniqueValue = RedisHelper.getKey(stringRedisTemplate, isMakeCallKey);
			logger.info("UniqueId : " + isMakeCallUniqueValue);
			if (StringUtils.hasText(isMakeCallUniqueValue)){
				InsLogUnCallConnected insLogUnCallConnected = new InsLogUnCallConnected();
				insLogUnCallConnected.setLeadId(clFresh.getLeadId().toString());
				insLogUnCallConnected.setAgentId(user_id);
				insLogUnCallConnected.setUniqueId(isMakeCallUniqueValue);
				insLogUnCallConnected.setOrgId(getCurOrgId());
				insLogUnCallConnected.setIsUpdatePlaybackUrl(0);
				logService.insLogUnCallConnected(taskSession, insLogUnCallConnected);
			}
		}
		// neu la save lead info manual
		if(clFresh.getLeadType() != null && clFresh.getLeadType().equals(Const.CREATE_SO_MANUAL_LEAD_TYPE)){
			if(Helper.isNullOrEmpty(clFresh.getAddress()) || Helper.isNullOrEmpty(clFresh.getName())
					|| Helper.isNullOrEmpty(clFresh.getPhone()) || !Helper.isNumeric(clFresh.getPhone()))
				throw new TMSInvalidInputException(ErrorMessage.INVALID_PARAM);
			// clFreshNew
			clFreshNew = clFresh;
			clFreshNew.setOrgId(orgId);
			/* InsCLFreshV5 insCLFresh = modelMapper.map(clFreshNew, InsCLFreshV5.class); */
			InsCLFreshV11 insCLFresh = modelMapper.map(clFreshNew, InsCLFreshV11.class);
			insCLFresh.setModifyby(user_id);
			insCLFresh.setAgcId(user_id);
			insCLFresh.setUserDefin02(clFresh.getUserDefin02());
			if(StringHelper.isNullOrEmpty(clFreshNew.getProvince()))
				insCLFresh.setProvince(null);
			if(StringHelper.isNullOrEmpty(clFreshNew.getDistrict()))
				insCLFresh.setDistrict(null);
			if(StringHelper.isNullOrEmpty(clFreshNew.getSubdistrict()))
				insCLFresh.setSubdistrict(null);
			if(StringHelper.isNullOrEmpty(clFreshNew.getNeighborhood()))
				insCLFresh.setNeighborhood(null);
			// insert new, TODO need check have insert ProId vs ProdName ...

			Calendar dateNow = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
			SimpleDateFormat df = new SimpleDateFormat(Const.DATE_FORMAT);
			
			// update .1) FristCallTIme .2) FristCallBy .3) FirstCallStatus .4) FirstCallReason .5) FristCallComment
			// === Lan dau tien leads duoc agent make call (total call = 1 va cac truong tren = null )
			if(insCLFresh.getTotalCall() != null && insCLFresh.getTotalCall() == 1
					&& (insCLFresh.getFirstCallTime() == null && insCLFresh.getFirstCallBy() == null
							&& insCLFresh.getFirstCallStatus() == null
							&& StringUtils.isEmpty(insCLFresh.getFirstCallReason())
							&& StringUtils.isEmpty(insCLFresh.getFirstCallComment()))){
				insCLFresh.setFirstCallTime(dateFormat.format(dateNow.getTime()));
				insCLFresh.setFirstCallBy(_curUser.getUserId());
				insCLFresh.setFirstCallStatus(clFresh.getLeadStatus());
				insCLFresh.setFirstCallReason(clFresh.getUserDefin05());
				insCLFresh.setFirstCallComment(clFresh.getComment());
			} else if(insCLFresh.getFirstCallTime() != null){
				Calendar firstCallTime = Calendar.getInstance();
				try{
					firstCallTime.setTime(df.parse(insCLFresh.getFirstCallTime()));
					insCLFresh.setFirstCallTime(dateFormat.format(firstCallTime.getTime()));
				} catch(ParseException e){
				}
			}

			// update 6) FCRTime .7) FCRBy .8) FCRStatus .9) FCRReason .10) FCRComment
			// === Lan dau tien leads duoc agent make call (total call = 1 va cac truong tren = null )
			// === Va lead status la contactable, bao gom: Callback, Approved, Rejected, Trash
			if(insCLFresh.getTotalCall() != null && insCLFresh.getTotalCall() == 1
					&& (insCLFresh.getFcrTime() == null && insCLFresh.getFcrBy() == null
							&& insCLFresh.getFcrStatus() == null && StringUtils.isEmpty(insCLFresh.getFcrReason())
							&& StringUtils.isEmpty(insCLFresh.getFcrComment()))
					&& leadIsContactable(insCLFresh.getLeadStatus())){
				insCLFresh.setFcrTime(dateFormat.format(dateNow.getTime()));
				insCLFresh.setFcrBy(_curUser.getUserId());
				insCLFresh.setFcrStatus(clFresh.getLeadStatus());
				insCLFresh.setFcrReason(clFresh.getUserDefin05());
				insCLFresh.setFcrComment(clFresh.getComment());
			} else if(insCLFresh.getFcrTime() != null){
				Calendar fcrTime = Calendar.getInstance();
				try{
					fcrTime.setTime(df.parse(insCLFresh.getFcrTime()));
					insCLFresh.setFcrTime(dateFormat.format(fcrTime.getTime()));
				} catch(ParseException e){
				}
			}
			if (clFreshNew.getLeadStatus() != null &&
					clFreshNew.getLeadStatus() == EnumType.LEAD_STATUS.APPROVED.getValue())
				insCLFresh.setCrmActionType(EnumType.CRM_ACTION_TYPE.SO_NEW.getValue());

			logger.info("[saveLeadInfo] Create Manual ------");
			response = logService.insCLFreshV11(SESSION_ID, insCLFresh);

			// Save mkt_data
			try {
				leadId = Integer.parseInt(response.getErrorMsg());
				if (leadId > 0) {
					mktDataService.save(SESSION_ID, clFresh, leadId, user_id);
				}
			} catch (Exception e) {
				logger.error("Error save mkt_data in saveLeadInfo MANUAL");
			}

			// insert log
			try{
				leadId = Integer.parseInt(response.getErrorMsg());
				if (leadId > 0) {
//					CLFresh lead = new CLFresh();
//					ModelMapper modelMapper = new ModelMapper();
//					lead = modelMapper.map(insCLFresh, CLFresh.class);

					dbLog.writeLeadStatusLog(_curUser.getUserId(), leadId, EnumType.LEAD_STATUS.NEW.getValue(),
							"Create Lead From saveLeadInfo - NEW" + clFresh.getComment());
//					dbLog.writeLeadStatusLogV2(0, lead, leadId, EnumType.LEAD_STATUS.NEW.getValue(),
//							"Create Lead From saveLeadInfo - NEW" + clFresh.getComment());
				}
			} catch(Exception ex){

			}
		} else{
			if(clFresh.getLeadId() == null || clFresh.getLeadId() <= 0)
				throw new TMSInvalidInputException(ErrorMessage.INVALID_PARAM);
			// TODO need to change to GET_LEAD
			GetLeadParamsV10 getFreshLeadParams = new GetLeadParamsV10();
			getFreshLeadParams.setLeadId(clFresh.getLeadId());
			DBResponse<List<CLFresh>> dbResponse = clFreshService.getLeadV14(SESSION_ID, getFreshLeadParams);
			if(dbResponse.getResult().isEmpty())
				throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
			clFreshNew = dbResponse.getResult().get(0);

			if (clFreshNew.getAssigned().intValue() != _curUser.getUserId().intValue() && Helper.isAgent(_curUser)){
				return TMSResponse.buildResponse(true, 0, ErrorMessage.CANNOT_SAVE_LEAD.getMessage(), 400);
			}

			if (clFreshNew.getLeadStatus().intValue() == EnumType.LEAD_STATUS.APPROVED.getValue()
					&& clFresh.getLeadStatus().intValue() != EnumType.LEAD_STATUS.APPROVED.getValue()) {
				return TMSResponse.buildResponse(true, 0, ErrorMessage.LEAD_IS_APPROVED.getMessage(), 400);
			}

			if (clFreshNew.getLeadStatus().intValue() == EnumType.LEAD_STATUS.APPROVED.getValue() && Helper.isAgent(_curUser)) {
				return TMSResponse.buildResponse(true, 0, ErrorMessage.LEAD_IS_APPROVED.getMessage(), 400);
			}

			// neu fresh da duoc callback thi khong duoc set uncall
			if( EnumType.LEAD_STATUS.isUncall(clFresh.getLeadStatus().intValue()) && soService.isNotSaveUncall(clFreshNew)){
				return TMSResponse.buildResponse(true, 0, ErrorMessage.CANNOT_SAVE_UNCALL.getMessage(), 400);
			}

			if(!StringUtils.isEmpty(clFreshNew.getComment()))
				oldComment = clFreshNew.getComment();

			// neu fresh dang la callback thi xoa callback co lead id tuong ung ben table
			// callback
			int freshLeadStatus = clFreshNew.getLeadStatus();

			clFreshNew.setLeadStatus(clFresh.getLeadStatus());
			clFreshNew.setName(clFresh.getName());
			if(!Helper.isNullOrEmpty(clFresh.getDistrict()))
				clFreshNew.setDistrict(clFresh.getDistrict());
			if(!Helper.isNullOrEmpty(clFresh.getProvince()))
				clFreshNew.setProvince(clFresh.getProvince());
			if(!Helper.isNullOrEmpty(clFresh.getSubdistrict()))
				clFreshNew.setSubdistrict(clFresh.getSubdistrict());
			if(!Helper.isNullOrEmpty(clFresh.getNeighborhood()))
				clFreshNew.setNeighborhood(clFresh.getNeighborhood());

			clFreshNew.setPhone(clFresh.getPhone());
			clFreshNew.setOtherPhone1(clFresh.getOtherPhone1());
			clFreshNew.setAddress(clFresh.getAddress());
//            if (!EnumType.LEAD_STATUS.isCallback(clFresh.getLeadStatus()))
			clFreshNew.setComment(clFresh.getComment());
			clFreshNew.setAgentNote(clFresh.getAgentNote());
			clFreshNew.setUserDefin05(clFresh.getUserDefin05());
			clFreshNew.setCustomerEmail(clFresh.getCustomerEmail());
			clFreshNew.setCustomerAge(clFresh.getCustomerAge());
			clFreshNew.setUserDefin02(clFresh.getUserDefin02());
			if(!StringHelper.isNullOrEmpty(clFresh.getPostalCode()))
				clFreshNew.setPostalCode(clFresh.getPostalCode());
			UpdLeadV9 upLead = modelMapper.map(clFreshNew, UpdLeadV9.class);
			upLead.setModifyby(getCurrentUser().getUserId());

			// tạm thời format các trường date time khác null nếu k cập nhật
			// để tránh việc lỗi convert (ngày 26-11-2020)
			if(clFreshNew.getNextCallTime() != null){
				Calendar nextCallTime = Calendar.getInstance();
				nextCallTime.setTime(clFreshNew.getNextCallTime());
				SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
				upLead.setNextCallTime(dateFormat.format(nextCallTime.getTime()));
			}
			if(clFreshNew.getFirstCallTime() != null){
				Calendar firstCallTime = Calendar.getInstance();
				firstCallTime.setTime(clFreshNew.getFirstCallTime());
				SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
				upLead.setFirstCallTime(dateFormat.format(firstCallTime.getTime()));
			}
			if(clFreshNew.getFcrTime() != null){
				Calendar fcrTime = Calendar.getInstance();
				fcrTime.setTime(clFreshNew.getFcrTime());
				SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
				upLead.setFcrTime(dateFormat.format(fcrTime.getTime()));
			}

			if(EnumType.LEAD_STATUS.isUncall(clFresh.getLeadStatus())){
				// TODO need to implement by call strategy
			}
			upLead = setUpdLeadParamForCallV9(upLead, clFreshNew, clFresh, orgId);
			logger.info("[saveLeadInfo] Update lead ------" + upLead.getLeadId());
			response = logService.updLeadV9(SESSION_ID, upLead);

			// Save mkt_data
			try {
				mktDataService.save(SESSION_ID, clFresh, user_id);
			} catch (Exception e) {
				logger.error("LeadId: {} {}", clFresh.getLeadId(), e.getMessage(), e);
			}

			/*
			 * if (isApproved) {//thong bao rang lead da duoc approve String key =
			 * RedisHelper.createRedisKey(Const.REDIS_PREFIX_CREATE, _curOrgId,
			 * String.valueOf(leadId)); RedisHelper.saveRedis(stringRedisTemplate, key,
			 * String.valueOf(clFresh.getLeadId()), strLogId, true); }
			 */
			try{
				dbLog.writeLeadStatusLogV4(_curUser.getUserId(), _curUser.getPhone(),
						clFresh.getLeadId(), clFresh.getLeadStatus(), clFresh.getComment(), SESSION_ID);
				// send to agency
				// neu lead da manipulate thi k postback nua
				boolean isManipulated = checkManipulated(clFresh.getLeadId());
				if(!isManipulated)
					sendToAgencyV8(clFreshNew, isApproved, upLead, clFresh, orgId);
			} catch(Exception e){
				logger.error("LeadId: {} {}", clFresh.getLeadId(), e.getMessage(), e);
			}

			// Delete callback neu la lead co callback va trang thai moi cua lead ko la callback
			if (leadService.haveCallback(clFresh.getLeadId()) && !EnumType.LEAD_STATUS.isCallback(clFresh.getLeadStatus())) {
				try {
					messageDeletedCallbackFail = soService.deleteCallbackByLeadIds(Collections.singletonList(clFresh.getLeadId()));
					if (StringUtils.hasText(messageDeletedCallbackFail)) {
						logger.error("Fail delete callback {}", messageDeletedCallbackFail);
					} else {
						isDeleteCallbackSuccess = true;
					}
					logger.info("[saveLeadInfo] Delete callback leadId {}: {}", clFresh.getLeadId(), isDeleteCallbackSuccess);
					dbLog.writeAgentActivityLog(_curUser.getUserId(), "delete callback in SaveLead: ", "callback",
							clFresh.getLeadId(), "lead_status", clFresh.getLeadStatus().toString());
				} catch (Exception e) {
					logger.error("[saveLeadInfo] Fail to delete callback {} {}", clFresh.getLeadId(), e.getMessage());
				}
			}
		}
		try {
			if (isDeleteCallbackSuccess)
				dbLog.writeAgentActivityLog(_curUser.getUserId(), "delete callback and update lead in SaveLead",
						"lead and callback", clFresh.getLeadId(), "lead_status", clFresh.getLeadStatus().toString());
			else
				dbLog.writeAgentActivityLog(_curUser.getUserId(), "update lead in SaveLead", "lead", clFresh.getLeadId(),
						"lead_status", clFresh.getLeadStatus().toString());
			String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_LEAD, orgId, _curUser.getUserId());
			/* local tạm thời comment code saveRedis lại */
			RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(clFresh.getLeadId()),
					String.valueOf(clFresh.getLeadStatus()));
			// Log agent state GET LEAD
			// Only set agent state to AVAILABLE when lead state existed
			// Check last state to set new state
			// If agents requested BREAK or BUSY or UNAVAILABLE before updating lead
			// then set their state to BREAK or BUSY or UNAVAILABLE. Otherwise, setting their state to AVAILABLE
			if (EnumType.LEAD_STATUS.isAvailable(clFresh.getLeadStatus())) {
				String redisKey = RedisHelper.createLogAgentTraceRedisKey(_curUser.getOrgId(), Const.LOG_AGENT_STATE, user_id);
				LogAgentTrace lastAgentTrace = logAgentTraceService.getLatestActivity(user_id, Const.LOG_AGENT_STATE, redisKey);
				String value = EnumType.AGENT_STATE.AVAILABLE.getName();
				Integer valueCode = EnumType.AGENT_STATE.AVAILABLE.getValue();
				String message = "";
				if (lastAgentTrace != null && lastAgentTrace.getFlagCode() != null && lastAgentTrace.getFlagCode() != 0) {
					valueCode = lastAgentTrace.getFlagCode();
					message = lastAgentTrace.getFlagMessage();
					value = lastAgentTrace.getFlagValue();
				}
				String lastValue = null;
				Integer lastValueCode = 0;
				UUID sessionId = null;
				if (lastAgentTrace != null) {
					lastValue = lastAgentTrace.getValue();
					sessionId = lastAgentTrace.getSessionId();
					lastValueCode = lastAgentTrace.getLastValueCode();
				}
				LogAgentTrace agentTrace = new LogAgentTrace.LogAgentTraceBuilder(
						user_id, Const.LOG_AGENT_STATE, value, valueCode, new Date())
						.setLastValue(lastValue)
						.setLastValueCode(lastValueCode)
						.setObjectType("lead")
						.setObjectId(clFresh.getLeadId())
						.setOnField("lead_status")
						.setSessionId(sessionId)
						.setMessage(message)
						.build();
				logAgentTraceService.logActivity(agentTrace, redisKey);
			}
		} catch(Exception e){
			logger.error("SaveLeadInfo: " + clFresh.getLeadId() + e.getMessage());
		}

		if (response.getErrorCode() == 0 && StringUtils.hasText(messageDeletedCallbackFail)) {
			return TMSResponse.buildResponse(messageDeletedCallbackFail, 0, "Fail delete callback " + messageDeletedCallbackFail, 400);
		}
		return TMSResponse.buildResponse(response);
	}


	private void sendToAgencyV7(CLFresh clFreshNew, boolean isApproved, UpdLeadV8 upLead, CLFresh clFresh, int orgId) {
		if(clFreshNew.getAgcId() != null && clFreshNew.getClickId() != null){/// /neu la don cua Agency thi gui lai
			/// postback
			String payout = "0", offerId = "0";
			if(clFreshNew.getPrice() != null)
				payout = clFreshNew.getPrice();
			if(clFreshNew.getOfferId() != null)
				offerId = clFreshNew.getOfferId();

			int typeQueue = -1;// reject
			if(isApproved && String.valueOf(EnumType.AGENTCY_TYPE.CPO).equals(clFreshNew.getTerms()))// neu la don
				// Approve thi ko gui lai postback la hold nua (yeu cau ngay 25/04/2020 - mr P)
				return;

			// neu la don tu agencty va ko phai la approve thi gui cho Agency status, neu la
			// Approve thi gui la hold, cho validate de gui approve
			typeQueue = isApproved? EnumType.LEAD_STATUS.APPROVED_TEMP.getValue()
					: Helper.AgencyType(clFresh.getLeadStatus());
			String note = isApproved? "wait for confirmation" : clFresh.getUserDefin05();
			if(clFreshNew.getAgcId() == Const.AGENCY_ADFLEX)
				note = clFresh.getComment();
			if(clFreshNew.getAgcId() == Const.AGENCY_ARB){
				note = String.format("%s;%s",clFreshNew.getAffiliateId(),clFreshNew.getSubid5());
			}

			if(EnumType.LEAD_STATUS.CLOSED.getValue() == upLead.getLeadStatus() && !isApproved){// neu la lead Close
				typeQueue = 2;// trash
				note = "attempt more, not approve";
				logger.info("########## MORE ATTEMPT ########### {}", clFreshNew.getClickId());
			}
			int terms = 0;
			try{
				if(clFresh.getTerms() != null && !clFresh.getTerms().isEmpty())
					terms = Integer.parseInt(clFresh.getTerms());
			} catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			String mes = "";
			if(null != clFreshNew.getTrackerId() && 0 != clFreshNew.getTrackerId())
				mes = QueueHelper.createQueueMessage(orgId, clFreshNew.getAgcId(), clFreshNew.getClickId(), typeQueue,
						offerId, clFreshNew.getLeadId(), note, payout, terms, clFreshNew.getTrackerId(),
						clFreshNew.getSubid4());
			else
				mes = QueueHelper.createQueueMessage(orgId, clFreshNew.getAgcId(), clFreshNew.getClickId(), typeQueue,
						offerId, clFreshNew.getLeadId(), note, payout, terms);

			QueueHelper.sendMessage(mes, Const.QUEUE_AGENTCY);
			logger.info("[QUEUE] " + Const.QUEUE_AGENTCY + " sent message " + mes);

		}
	}

	private void sendToAgencyV8(CLFresh clFreshNew, boolean isApproved, UpdLeadV9 upLead, CLFresh clFresh, int orgId) {
		if(clFreshNew.getAgcId() != null && clFreshNew.getClickId() != null){/// /neu la don cua Agency thi gui lai
			/// postback
			String payout = "0", offerId = "0";
			if(clFreshNew.getPrice() != null)
				payout = clFreshNew.getPrice();
			if(clFreshNew.getOfferId() != null)
				offerId = clFreshNew.getOfferId();

			int typeQueue = -1;// reject
			if(isApproved && String.valueOf(EnumType.AGENTCY_TYPE.CPO).equals(clFreshNew.getTerms()))// neu la don
				// Approve thi ko gui lai postback la hold nua (yeu cau ngay 25/04/2020 - mr P)
				return;

			// neu la don tu agencty va ko phai la approve thi gui cho Agency status, neu la
			// Approve thi gui la hold, cho validate de gui approve
			typeQueue = isApproved? EnumType.LEAD_STATUS.APPROVED_TEMP.getValue()
					: Helper.AgencyType(clFresh.getLeadStatus());
			String note = isApproved? "wait for confirmation" : clFresh.getUserDefin05();
			if(clFreshNew.getAgcId() == Const.AGENCY_ADFLEX)
				note = clFresh.getComment();
			if(clFreshNew.getAgcId() == Const.AGENCY_ARB){
				note = String.format("%s;%s",clFreshNew.getAffiliateId(),clFreshNew.getSubid5());
			}

			if(EnumType.LEAD_STATUS.CLOSED.getValue() == upLead.getLeadStatus() && !isApproved){// neu la lead Close
				typeQueue = -1;// reject
				note = "attempt more, not approve";
				logger.info("########## MORE ATTEMPT ########### {}", clFreshNew.getClickId());
			}
			int terms = 0;
			try{
				if(clFresh.getTerms() != null && !clFresh.getTerms().isEmpty())
					terms = Integer.parseInt(clFresh.getTerms());
			} catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			String mes = "";
			if(null != clFreshNew.getTrackerId() && 0 != clFreshNew.getTrackerId())
				mes = QueueHelper.createQueueMessage(orgId, clFreshNew.getAgcId(), clFreshNew.getClickId(), typeQueue,
						offerId, clFreshNew.getLeadId(), note, payout, terms, clFreshNew.getTrackerId(),
						clFreshNew.getSubid4());
			else
				mes = QueueHelper.createQueueMessage(orgId, clFreshNew.getAgcId(), clFreshNew.getClickId(), typeQueue,
						offerId, clFreshNew.getLeadId(), note, payout, terms);

			QueueHelper.sendMessage(mes, Const.QUEUE_AGENTCY);
			logger.info("[QUEUE] " + Const.QUEUE_AGENTCY + " sent message " + mes);

		}
	}

	private UpdLeadV8 setUpdLeadParamForCallV8(UpdLeadV8 upLead, CLFresh currFresh, CLFresh paramFresh, int orgId) {

		int totalCall = 1;
		if (!Helper.isValidator(_curUser)) {
			totalCall = currFresh.getTotalCall() == null? 1 : currFresh.getTotalCall() + 1;
			upLead.setTotalCall(totalCall);
		}

		if (Helper.isAgent(_curUser)) {
			upLead.setAssigned(_curUser.getUserId());
		}

		upLead.setCrmActionType(EnumType.CRM_ACTION_TYPE.UPDATE_LEAD.getValue());

		Calendar dateNow = Calendar.getInstance();
		SimpleDateFormat dateNowFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
		
		/* First time agent make call - only agent can change (role agent and all field is null):
		1) FirstCallTime .2) FirstCallBy .3) FirstCallStatus .4) FirstCallReason .5) FirstCallComment */
		if(Helper.isAgent(_curUser) && (currFresh.getFirstCallTime() == null && currFresh.getFirstCallBy() == null
				&& currFresh.getFirstCallStatus() == null && StringUtils.isEmpty(currFresh.getFirstCallReason())
				&& StringUtils.isEmpty(currFresh.getFirstCallComment()))){
			upLead.setFirstCallTime(dateNowFormat.format(dateNow.getTime()));
			upLead.setFirstCallBy(_curUser.getUserId());
			upLead.setFirstCallStatus(currFresh.getLeadStatus());
			upLead.setFirstCallReason(currFresh.getUserDefin05());
			upLead.setFirstCallComment(currFresh.getComment());
		}
		
		//update  6) FCRTime .7) FCRBy .8) FCRStatus .9) FCRReason .10) FCRComment
		//=== Lan dau tien leads duoc agent make call (total call = 1 va cac truong tren = null )
		//=== Va lead status la contactable, bao gom: Callback, Approved, Rejected, Trash
		if(totalCall == 1
				&& (currFresh.getFcrTime() == null && currFresh.getFcrBy() == null
						&& currFresh.getFcrStatus() == null && StringUtils.isEmpty(currFresh.getFcrReason())
						&& StringUtils.isEmpty(currFresh.getFcrComment()))
				&& leadIsContactable(currFresh.getLeadStatus())){
			upLead.setFcrTime(dateNowFormat.format(dateNow.getTime()));
			upLead.setFcrBy(_curUser.getUserId());
			upLead.setFcrStatus(currFresh.getLeadStatus());
			upLead.setFcrReason(currFresh.getUserDefin05());
			upLead.setFcrComment(currFresh.getComment());
		}

		/* local comment tránh lỗi */
		if(EnumType.LEAD_STATUS.isUncall(paramFresh.getLeadStatus())){// neu launcall thi moi tinh toi maxatemp
			String globalValueMaxAtempt = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 3, 1);
			int maxAttempt = globalValueMaxAtempt != null? Integer.parseInt(globalValueMaxAtempt) : 10;
			if(totalCall >= maxAttempt)
				upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
		}

		Calendar modifiedTime = Calendar.getInstance();
		modifiedTime.setTime(currFresh.getNextCallTime());
		int modifiedDay = modifiedTime.get(Calendar.DAY_OF_YEAR);
		int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

		int dayCall = modifiedDay == currentDay && currFresh.getDayCall() != null? currFresh.getDayCall() + 1 : 1;
		upLead.setDayCall(dayCall);

		int currentNumOfDay = currFresh.getNumberOfDay() == null? 1 : currFresh.getNumberOfDay();
		int numberOfDay = modifiedDay == currentDay? currentNumOfDay : currentNumOfDay + 1;
		upLead.setNumberOfDay(numberOfDay);

		GetCampaignAgent cpAgentParam = new GetCampaignAgent();
		cpAgentParam.setUserId(_curUser.getUserId());
		cpAgentParam.setOrgId(orgId);
		cpAgentParam.setStatus(EnumType.CAMPAIGN_STATUS_ID.RUNNING.getValue());
		List<GetCampaignAgentResp> campaigns = clFreshService.getCampaignAgent(SESSION_ID, cpAgentParam).getResult();
		logger.info(" setUpdLeadParamForUncall campaign size : " + campaigns.size());
		if(campaigns == null || campaigns.isEmpty())
			return upLead;
		GetCampaignAgentResp campaign = campaigns.get(0);
		logger.info(" setUpdLeadParamForUncall campaign Id : " + campaign.getCpId()
				+ " setUpdLeadParamForUncall getLeadStatus : " + paramFresh.getLeadStatus());
		Integer cpId = campaign.getCpId();
		logger.info("CPID: {}", cpId);
		Integer distributionType = getDistributionRuleType(cpId, orgId);
		logger.info("distributionType: {}\r\nsetUpdLeadParamForUncall lead status: {}", distributionType, paramFresh.getLeadStatus());
		CfCpCallStrategyDefault cfCpCallStrategyDefault = null;

		if (com.tms.api.utils.ObjectUtils.allNotNull(distributionType)) {
			if (distributionType.equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue()) && EnumType.LEAD_STATUS.isUncall(paramFresh.getLeadStatus())) {
				upLead.setAssigned(0);
			}
		}

		if(paramFresh.getLeadStatus() == EnumType.LEAD_STATUS.BUSY.getValue()){
			int attemptBusy = modifiedDay == currentDay && currFresh.getAttemptBusy() != null
					? currFresh.getAttemptBusy() + 1
					: 1;
			upLead.setAttemptBusy(attemptBusy);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyConfig(orgId, cpId,
						1, paramFresh.getLeadStatus());
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}
		else if(paramFresh.getLeadStatus() == EnumType.LEAD_STATUS.NOANSWER.getValue()){
			int attemptNoans = modifiedDay == currentDay && currFresh.getAttemptNoans() != null
					? currFresh.getAttemptNoans() + 1
					: 1;
			upLead.setAttemptNoans(attemptNoans);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyConfig(orgId, cpId,
						1, paramFresh.getLeadStatus());
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}
		else if(paramFresh.getLeadStatus() == EnumType.LEAD_STATUS.UNREACHABLE.getValue()){
			int attemptUnreach = modifiedDay == currentDay && currFresh.getAttempUnreachable() != null
					? currFresh.getAttempUnreachable() + 1
					: 1;
			upLead.setAttempUnreachable(attemptUnreach);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyConfig(orgId, cpId,
						1, paramFresh.getLeadStatus());
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}

		if(cfCpCallStrategyDefault != null){
			logger.info("cfCpCallStrategyDefault: {}\r\nsetUpdLeadParamForUncall cpcs name: {}", cfCpCallStrategyDefault, cfCpCallStrategyDefault.getConfigName());
			Calendar newNextCallTime = Calendar.getInstance();
			newNextCallTime.add(Calendar.MINUTE, cfCpCallStrategyDefault.getDuration());
			SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
			upLead.setNextCallTime(dateFormat.format(newNextCallTime.getTime()));

			if(upLead.getNumberOfDay() == cfCpCallStrategyDefault.getDay()
					&& upLead.getAttemptBusy() == cfCpCallStrategyDefault.getAttempt())
				if(cfCpCallStrategyDefault.getNextAction().equals("1"))
					upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
			return upLead;
		}

		GetCpCallStrategyResp cpCallStrategy= campaignService.getConfigCallStrategies(SESSION_ID, orgId, cpId,
				1, paramFresh.getLeadStatus());
		if (cpCallStrategy == null)
			return upLead;
		logger.info(" setUpdLeadParamForUnCall cpCallStrategy name: " + cpCallStrategy.getConfigName());

		Calendar newNextCallTime = Calendar.getInstance();
		newNextCallTime.add(Calendar.MINUTE, cpCallStrategy.getDuration());
		SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
		upLead.setNextCallTime(dateFormat.format(newNextCallTime.getTime()));

		if(upLead.getNumberOfDay() == cpCallStrategy.getDay() && upLead.getAttemptBusy() == cpCallStrategy.getAttempt())
			if(cpCallStrategy.getNextAction().equals("1"))
				upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
		return upLead;
	}

	private boolean leadIsContactable(Integer leadStatus) {
		if(leadStatus != null && (leadStatus.equals(EnumType.LEAD_STATUS.APPROVED.getValue())
				|| leadStatus.equals(EnumType.LEAD_STATUS.REJECTED.getValue())
				|| leadStatus.equals(EnumType.LEAD_STATUS.INVALID.getValue())
				|| EnumType.LEAD_STATUS.isCallback(leadStatus))
		){
			return true;
		}
		return false;
	}

	private UpdLeadV9 setUpdLeadParamForCallV9(UpdLeadV9 upLead, CLFresh currFresh, CLFresh paramFresh, int orgId) {

		int totalCall = 1;
		if (!Helper.isValidator(_curUser)) {
			totalCall = currFresh.getTotalCall() == null? 1 : currFresh.getTotalCall() + 1;
			upLead.setTotalCall(totalCall);
		}

		if (Helper.isAgent(_curUser)) {
			upLead.setAssigned(_curUser.getUserId());
		}

		upLead.setCrmActionType(EnumType.CRM_ACTION_TYPE.UPDATE_LEAD.getValue());
		upLead.setTeam(autoReloadConfigTeam.getConfigTeam(_curUser.getUserId()));
		upLead.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(_curUser.getUserId()));

		Calendar dateNow = Calendar.getInstance();
		SimpleDateFormat dateNowFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);

		/* First time agent make call - only agent can change (role agent and all field is null):
		1) FirstCallTime .2) FirstCallBy .3) FirstCallStatus .4) FirstCallReason .5) FirstCallComment */
		if(Helper.isAgent(_curUser) && (currFresh.getFirstCallTime() == null && currFresh.getFirstCallBy() == null
				&& currFresh.getFirstCallStatus() == null && StringUtils.isEmpty(currFresh.getFirstCallReason())
				&& StringUtils.isEmpty(currFresh.getFirstCallComment()))){
			upLead.setFirstCallTime(dateNowFormat.format(dateNow.getTime()));
			upLead.setFirstCallBy(_curUser.getUserId());
			upLead.setFirstCallStatus(currFresh.getLeadStatus());
			upLead.setFirstCallReason(currFresh.getUserDefin05());
			upLead.setFirstCallComment(currFresh.getComment());
		}

		//update  6) FCRTime .7) FCRBy .8) FCRStatus .9) FCRReason .10) FCRComment
		//=== Lan dau tien leads duoc agent make call (total call = 1 va cac truong tren = null )
		//=== Va lead status la contactable, bao gom: Callback, Approved, Rejected, Trash
		if(totalCall == 1
				&& (currFresh.getFcrTime() == null && currFresh.getFcrBy() == null
				&& currFresh.getFcrStatus() == null && StringUtils.isEmpty(currFresh.getFcrReason())
				&& StringUtils.isEmpty(currFresh.getFcrComment()))
				&& leadIsContactable(currFresh.getLeadStatus())){
			upLead.setFcrTime(dateNowFormat.format(dateNow.getTime()));
			upLead.setFcrBy(_curUser.getUserId());
			upLead.setFcrStatus(currFresh.getLeadStatus());
			upLead.setFcrReason(currFresh.getUserDefin05());
			upLead.setFcrComment(currFresh.getComment());
		}

		/* local comment tránh lỗi */
		if(EnumType.LEAD_STATUS.isUncall(paramFresh.getLeadStatus())){// neu launcall thi moi tinh toi maxatemp
			String globalValueMaxAtempt = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 3, 1);
			int maxAttempt = globalValueMaxAtempt != null? Integer.parseInt(globalValueMaxAtempt) : 30;
			if(totalCall >= maxAttempt) {
				upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
				logger.info("close lead by totalCall: " + totalCall + ", " + maxAttempt + ", " + upLead.getLeadId());
			}
		}

		Calendar modifiedTime = Calendar.getInstance();
		modifiedTime.setTime(currFresh.getNextCallTime());
		int modifiedDay = modifiedTime.get(Calendar.DAY_OF_YEAR);
		int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

		int dayCall = modifiedDay == currentDay && currFresh.getDayCall() != null? currFresh.getDayCall() + 1 : 1;
		upLead.setDayCall(dayCall);

		int currentNumOfDay = currFresh.getNumberOfDay() == null? 1 : currFresh.getNumberOfDay();
		int numberOfDay = modifiedDay == currentDay? currentNumOfDay : currentNumOfDay + 1;
		upLead.setNumberOfDay(numberOfDay);

		GetCampaignAgent cpAgentParam = new GetCampaignAgent();
		cpAgentParam.setUserId(_curUser.getUserId());
		cpAgentParam.setOrgId(orgId);
		cpAgentParam.setStatus(EnumType.CAMPAIGN_STATUS_ID.RUNNING.getValue());
		List<GetCampaignAgentResp> campaigns = clFreshService.getCampaignAgent(SESSION_ID, cpAgentParam).getResult();
		logger.info(" setUpdLeadParamForUncall campaign size : {}", campaigns.size());
		if(campaigns == null || campaigns.isEmpty())
			return upLead;
		GetCampaignAgentResp campaign = campaigns.get(0);
		logger.info(" setUpdLeadParamForUncall campaign Id : {} setUpdLeadParamForUncall getLeadStatus : {}", campaign.getCpId(), paramFresh.getLeadStatus());
		Integer cpId = campaign.getCpId();
		logger.info("CPID: {}", cpId);
		Integer distributionType = getDistributionRuleType(cpId, orgId);
		assert distributionType != null;
		logger.info("distributionType: {}\r\nsetUpdLeadParamForUncall lead status : {}", distributionType, paramFresh.getLeadStatus());
		CfCpCallStrategyDefault cfCpCallStrategyDefault = null;

		if (com.tms.api.utils.ObjectUtils.allNotNull(distributionType)) {
			if (distributionType.equals(EnumType.DISTRIBUTION_RULE.CIT_UN_CALL.getValue()) && EnumType.LEAD_STATUS.isUncall(paramFresh.getLeadStatus())) {
				upLead.setAssigned(0);
			}
		}

		if(paramFresh.getLeadStatus() == EnumType.LEAD_STATUS.BUSY.getValue()){
			int attemptBusy = modifiedDay == currentDay && currFresh.getAttemptBusy() != null
					? currFresh.getAttemptBusy() + 1
					: 1;
			upLead.setAttemptBusy(attemptBusy);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyConfig(orgId, cpId,
						1, paramFresh.getLeadStatus());
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}
		else if(paramFresh.getLeadStatus() == EnumType.LEAD_STATUS.NOANSWER.getValue()){
			int attemptNoans = modifiedDay == currentDay && currFresh.getAttemptNoans() != null
					? currFresh.getAttemptNoans() + 1
					: 1;
			upLead.setAttemptNoans(attemptNoans);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyConfig(orgId, cpId,
						1, paramFresh.getLeadStatus());
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}
		else if(paramFresh.getLeadStatus() == EnumType.LEAD_STATUS.UNREACHABLE.getValue()){
			int attemptUnreach = modifiedDay == currentDay && currFresh.getAttempUnreachable() != null
					? currFresh.getAttempUnreachable() + 1
					: 1;
			upLead.setAttempUnreachable(attemptUnreach);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyConfig(orgId, cpId,
						1, paramFresh.getLeadStatus());
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}

		if(cfCpCallStrategyDefault != null) {
			logger.info("cfCpCallStrategyDefault: {}\r\nsetUpdLeadParamForUncall cpcs name: {}", cfCpCallStrategyDefault, cfCpCallStrategyDefault.getConfigName());
			Calendar newNextCallTime = Calendar.getInstance();
			newNextCallTime.add(Calendar.MINUTE, cfCpCallStrategyDefault.getDuration());
			SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
			upLead.setNextCallTime(dateFormat.format(newNextCallTime.getTime()));

			if (upLead.getNumberOfDay() == cfCpCallStrategyDefault.getDay()
					&& upLead.getAttemptBusy() == cfCpCallStrategyDefault.getAttempt())
				if (cfCpCallStrategyDefault.getNextAction().equals("1")) {
					upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
					logger.info("close lead by attempBusy: " + upLead.getLeadId());
				}
			return upLead;
		}

		GetCpCallStrategyResp cpCallStrategy= campaignService.getConfigCallStrategies(SESSION_ID, orgId, cpId,
				1, paramFresh.getLeadStatus());
		if (cpCallStrategy == null)
			return upLead;
		logger.info(" setUpdLeadParamForUnCall cpCallStrategy name: " + cpCallStrategy.getConfigName());

		if (Helper.isThailand(_COUNTRY) && distributionType == EnumType.DISTRIBUTION_RULE.UNCALL_ME.getValue() &&
				EnumType.LEAD_STATUS.isUncall(currFresh.getLeadStatus())) {
			return updLeadUncallDistributionRuleUncallMe(cpCallStrategy, upLead, modifiedDay, currentDay, totalCall, currFresh.getAttempOther1());
		}

		Calendar newNextCallTime = Calendar.getInstance();
		newNextCallTime.add(Calendar.MINUTE, cpCallStrategy.getDuration());
		SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
		upLead.setNextCallTime(dateFormat.format(newNextCallTime.getTime()));

		if(upLead.getNumberOfDay() == cpCallStrategy.getDay() && upLead.getAttemptBusy() == cpCallStrategy.getAttempt())
			if(cpCallStrategy.getNextAction().equals("1")) {
				upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
				logger.info("close lead by attempBusy 2: " + upLead.getLeadId());
			}
		return upLead;
	}

	private UpdLeadV9 updLeadUncallDistributionRuleUncallMe(GetCpCallStrategyResp cpCallStrategy, UpdLeadV9 updLead,
															int modifiedDay, int currentDay, int totalCall, Integer attemptOther1) {
		logger.info("Update lead uncall ME. LeadId: {}", updLead.getLeadId());
		int totalCallClose = 18;
		int defaultExtraMinutesNextCallTime = 2;
		int defaultNumberCall = 5;

		int attemptOfDay;
		if (modifiedDay == currentDay) {
			attemptOfDay = attemptOther1 == null ? 1 : attemptOther1 + 1;
		} else {
			attemptOfDay = 1;
		}
		updLead.setAttempOther1(attemptOfDay);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextCallTime;
		if (totalCall <= defaultNumberCall) {
			nextCallTime = now.plusMinutes(defaultExtraMinutesNextCallTime);
		} else {
			nextCallTime = now.plusMinutes(cpCallStrategy.getDuration());
		}
		updLead.setNextCallTime(DateHelper.toTMSDateTime(nextCallTime));

		if (totalCall == totalCallClose) {
			logger.info("Close leadId {} max totalCallClose {}", updLead.getLeadId(), totalCall);
			updLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
		}

		return updLead;
	}

	private void sendToAgencyV6(CLFresh clFreshNew, boolean isApproved, UpdLeadV6 upLead, CLFresh clFresh, int orgId) {
		if(clFreshNew.getAgcId() != null && clFreshNew.getClickId() != null){/// /neu la don cua Agency thi gui lai
			/// postback
			String payout = "0", offerId = "0";
			if(clFreshNew.getPrice() != null)
				payout = clFreshNew.getPrice();
			if(clFreshNew.getOfferId() != null)
				offerId = clFreshNew.getOfferId();

			int typeQueue = -1;// reject
			if(isApproved && String.valueOf(EnumType.AGENTCY_TYPE.CPO).equals(clFreshNew.getTerms()))// neu la don
				// Approve thi ko gui lai postback la hold nua (yeu cau ngay 25/04/2020 - mr P)
				return;

			// neu la don tu agencty va ko phai la approve thi gui cho Agency status, neu la
			// Approve thi gui la hold, cho validate de gui approve
			typeQueue = isApproved? EnumType.LEAD_STATUS.APPROVED_TEMP.getValue()
					: Helper.AgencyType(clFresh.getLeadStatus());
			String note = isApproved? "wait for confirmation" : clFresh.getUserDefin05();
			if(clFreshNew.getAgcId() == Const.AGENCY_ADFLEX)
				note = clFresh.getComment();

			if(EnumType.LEAD_STATUS.CLOSED.getValue() == upLead.getLeadStatus() && !isApproved){// neu la lead Close
				typeQueue = 2;// trash
				note = "attempt more, not approve";
				logger.info("########## MORE ATTEMPT ########### {}", clFreshNew.getClickId());
			}
			int terms = 0;
			try{
				if(clFresh.getTerms() != null && !clFresh.getTerms().isEmpty())
					terms = Integer.parseInt(clFresh.getTerms());
			} catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			String mes = "";
			if(null != clFreshNew.getTrackerId() && 0 != clFreshNew.getTrackerId())
				mes = QueueHelper.createQueueMessage(orgId, clFreshNew.getAgcId(), clFreshNew.getClickId(), typeQueue,
						offerId, clFreshNew.getLeadId(), note, payout, terms, clFreshNew.getTrackerId(),
						clFreshNew.getSubid4());
			else
				mes = QueueHelper.createQueueMessage(orgId, clFreshNew.getAgcId(), clFreshNew.getClickId(), typeQueue,
						offerId, clFreshNew.getLeadId(), note, payout, terms);

			QueueHelper.sendMessage(mes, Const.QUEUE_AGENTCY);
			logger.info("[QUEUE] " + Const.QUEUE_AGENTCY + " sent message " + mes);

		}
	}

	private UpdLeadV6 setUpdLeadParamForCallV6(UpdLeadV6 upLead, CLFresh currFresh, CLFresh paramFresh, int orgId) {
		/*
		 * GetGlobalParam globalDbParams = new GetGlobalParam();
		 * globalDbParams.setOrgId(_curOrgId); // TODO: hardcoded, need to replace
		 * globalDbParams.setName("max attempt"); DBResponse<List<GetGlobalParamResp>>
		 * globalParams = clFreshService.getGlobalParam(SESSION_ID, globalDbParams);
		 */
		int totalCall = 1;
		if (!Helper.isValidator(_curUser)) {
			 totalCall = currFresh.getTotalCall() == null? 1 : currFresh.getTotalCall() + 1;
			upLead.setTotalCall(totalCall);
		}

		/* local comment tránh lỗi */
		if(EnumType.LEAD_STATUS.isUncall(paramFresh.getLeadStatus())){// neu launcall thi moi tinh toi maxatemp
			String globalValueMaxAtempt = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 3, 1);
			int maxAttempt = globalValueMaxAtempt != null? Integer.parseInt(globalValueMaxAtempt) : 10;
			if(totalCall >= maxAttempt)
				upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
		}

		Calendar modifiedTime = Calendar.getInstance();
		modifiedTime.setTime(currFresh.getNextCallTime());
		int modifiedDay = modifiedTime.get(Calendar.DAY_OF_YEAR);
		int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

		int dayCall = modifiedDay == currentDay && currFresh.getDayCall() != null? currFresh.getDayCall() + 1 : 1;
		upLead.setDayCall(dayCall);

		int currentNumOfDay = currFresh.getNumberOfDay() == null? 1 : currFresh.getNumberOfDay();
		int numberOfDay = modifiedDay == currentDay? currentNumOfDay : currentNumOfDay + 1;
		upLead.setNumberOfDay(numberOfDay);

		GetCampaignAgent cpAgentParam = new GetCampaignAgent();
		cpAgentParam.setUserId(_curUser.getUserId());
		cpAgentParam.setOrgId(orgId);
		cpAgentParam.setStatus(EnumType.CAMPAIGN_STATUS_ID.RUNNING.getValue());
		List<GetCampaignAgentResp> campaigns = clFreshService.getCampaignAgent(SESSION_ID, cpAgentParam).getResult();
		logger.info(" setUpdLeadParamForUncall campaign size : " + campaigns.size());
		if(campaigns == null || campaigns.isEmpty())
			return upLead;
		GetCampaignAgentResp campaign = campaigns.get(0);
		logger.info(" setUpdLeadParamForUncall campaign Id : " + campaign.getCpId()
				+ " setUpdLeadParamForUncall getLeadStatus : " + paramFresh.getLeadStatus());
		Integer cpId = campaign.getCpId();
		logger.info("CPID: {}", cpId);
		Integer distributionType = getDistributionRuleType(cpId, orgId);
		logger.info("distributionType: {}\r\nsetUpdLeadParamForUncall lead status: {}", distributionType, paramFresh.getLeadStatus());
		CfCpCallStrategyDefault cfCpCallStrategyDefault = null;
		if(EnumType.LEAD_STATUS.BUSY.getValue() == paramFresh.getLeadStatus()){
			int attemptBusy = modifiedDay == currentDay && currFresh.getAttemptBusy() != null
					? currFresh.getAttemptBusy() + 1
					: 1;
			upLead.setAttemptBusy(attemptBusy);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyDefault(cpId, orgId,
						paramFresh.getLeadStatus(), 1);
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}

		if(EnumType.LEAD_STATUS.NOANSWER.getValue() == paramFresh.getLeadStatus()){
			int attemptNoans = modifiedDay == currentDay && currFresh.getAttemptNoans() != null
					? currFresh.getAttemptNoans() + 1
					: 1;
			upLead.setAttemptNoans(attemptNoans);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyDefault(cpId, orgId,
						paramFresh.getLeadStatus(), 1);
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}

		if(EnumType.LEAD_STATUS.UNREACHABLE.getValue() == paramFresh.getLeadStatus()){
			int attemptUnreach = modifiedDay == currentDay && currFresh.getAttempUnreachable() != null
					? currFresh.getAttempUnreachable() + 1
					: 1;
			upLead.setAttempUnreachable(attemptUnreach);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyDefault(cpId, orgId,
						paramFresh.getLeadStatus(), 1);
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}
		if(cfCpCallStrategyDefault != null){
			logger.info("cfCpCallStrategyDefault: {}\r\nsetUpdLeadParamForUncall cpcs name: {}",
					cfCpCallStrategyDefault, cfCpCallStrategyDefault.getConfigName());
			Calendar newNextCallTime = Calendar.getInstance();
			newNextCallTime.add(Calendar.MINUTE, cfCpCallStrategyDefault.getDuration());
			SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
			upLead.setNextCallTime(dateFormat.format(newNextCallTime.getTime()));

			if(upLead.getNumberOfDay() == cfCpCallStrategyDefault.getDay()
					&& upLead.getAttemptBusy() == cfCpCallStrategyDefault.getAttempt())
				if(cfCpCallStrategyDefault.getNextAction().equals("1"))
					upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
			return upLead;
		}
		GetCpCallStrategy csparam = new GetCpCallStrategy();
		csparam.setOrgId(orgId);
		csparam.setCpId(cpId);
		csparam.setCallStatus(paramFresh.getLeadStatus());
		csparam.setOrderPhoneNumber(1);
		DBResponse<List<GetCpCallStrategyResp>> cpcsList = provinceService.getCpCallStrategy(SESSION_ID, csparam);
		if(cpcsList == null || cpcsList.getResult().isEmpty())
			return upLead;
		GetCpCallStrategyResp cpcs = cpcsList.getResult().get(0);
		logger.info(" setUpdLeadParamForUncall cpcs name: " + cpcs.getConfigName());

		Calendar newNextCallTime = Calendar.getInstance();
		newNextCallTime.add(Calendar.MINUTE, cpcs.getDuration());
		SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
		upLead.setNextCallTime(dateFormat.format(newNextCallTime.getTime()));

		if(upLead.getNumberOfDay() == cpcs.getDay() && upLead.getAttemptBusy() == cpcs.getAttempt())
			if(cpcs.getNextAction().equals("1"))
				upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
		return upLead;
	}

	private boolean checkManipulated(Integer leadId) {
		ClManipulatedFresh manipulatedFresh = clManipulatedFreshService.getByLeadId(leadId);
		return !ObjectUtils.isEmpty(manipulatedFresh);
	}

	private void sendToAgency(CLFresh clFreshNew, boolean isApproved, UpdLeadV5 upLead, CLFresh clFresh, int orgId) {
		if(clFreshNew.getAgcId() != null && clFreshNew.getClickId() != null){/// /neu la don cua Agency thi gui lai
																				/// postback
			String payout = "0", offerId = "0";
			if(clFreshNew.getPrice() != null)
				payout = clFreshNew.getPrice();
			if(clFreshNew.getOfferId() != null)
				offerId = clFreshNew.getOfferId();

			int typeQueue = -1;// reject
			if(isApproved && String.valueOf(EnumType.AGENTCY_TYPE.CPO).equals(clFreshNew.getTerms()))// neu la don
																										// Approve thi
																										// ko gui lai
																										// postback la
																										// hold nua (yeu
																										// cau ngay
																										// 25/04/2020 -
																										// mr P)
				return;

			// neu la don tu agencty va ko phai la approve thi gui cho Agency status, neu la
			// Approve thi gui la hold, cho validate de gui approve
			typeQueue = isApproved? EnumType.LEAD_STATUS.APPROVED_TEMP.getValue()
					: Helper.AgencyType(clFresh.getLeadStatus());
			String note = isApproved? "wait for confirmation" : clFresh.getUserDefin05();
			if(clFreshNew.getAgcId() == Const.AGENCY_ADFLEX)
				note = clFresh.getComment();

			if(EnumType.LEAD_STATUS.CLOSED.getValue() == upLead.getLeadStatus() && !isApproved){// neu la lead Close
				typeQueue = 2;// trash
				note = "attempt more, not approve";
				logger.info("########## MORE ATTEMPT ########### {}", clFreshNew.getClickId());
			}
			int terms = 0;
			try{
				if(clFresh.getTerms() != null && !clFresh.getTerms().isEmpty())
					terms = Integer.parseInt(clFresh.getTerms());
			} catch(Exception e){
				logger.error(e.getMessage(), e);
			}
			String mes = "";
			if(null != clFreshNew.getTrackerId() && 0 != clFreshNew.getTrackerId())
				mes = QueueHelper.createQueueMessage(orgId, clFreshNew.getAgcId(), clFreshNew.getClickId(), typeQueue,
						offerId, clFreshNew.getLeadId(), note, payout, terms, clFreshNew.getTrackerId(),
						clFreshNew.getSubid4());
			else
				mes = QueueHelper.createQueueMessage(orgId, clFreshNew.getAgcId(), clFreshNew.getClickId(), typeQueue,
						offerId, clFreshNew.getLeadId(), note, payout, terms);

			QueueHelper.sendMessage(mes, Const.QUEUE_AGENTCY);
			logger.info("[QUEUE] " + Const.QUEUE_AGENTCY + " sent message " + mes);

		}
	}

	private UpdLeadV5 setUpdLeadParamForCall(UpdLeadV5 upLead, CLFresh currFresh, CLFresh paramFresh, int orgId) {
		/*
		 * GetGlobalParam globalDbParams = new GetGlobalParam();
		 * globalDbParams.setOrgId(_curOrgId); // TODO: hardcoded, need to replace
		 * globalDbParams.setName("max attempt"); DBResponse<List<GetGlobalParamResp>>
		 * globalParams = clFreshService.getGlobalParam(SESSION_ID, globalDbParams);
		 */
		int totalCall = currFresh.getTotalCall() == null? 1 : currFresh.getTotalCall() + 1;
		upLead.setTotalCall(totalCall);

		if(EnumType.LEAD_STATUS.isUncall(paramFresh.getLeadStatus())){// neu la uncall thi moi tinh toi maxatemp
			String globalValueMaxAtempt = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 3, 1);
			int maxAttempt = globalValueMaxAtempt != null? Integer.parseInt(globalValueMaxAtempt) : 10;
			if(totalCall >= maxAttempt)
				upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
		}

		Calendar modifiedTime = Calendar.getInstance();
		modifiedTime.setTime(currFresh.getNextCallTime());
		int modifiedDay = modifiedTime.get(Calendar.DAY_OF_YEAR);
		int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

		int dayCall = modifiedDay == currentDay && currFresh.getDayCall() != null? currFresh.getDayCall() + 1 : 1;
		upLead.setDayCall(dayCall);

		int currentNumOfDay = currFresh.getNumberOfDay() == null? 1 : currFresh.getNumberOfDay();
		int numberOfDay = modifiedDay == currentDay? currentNumOfDay : currentNumOfDay + 1;
		upLead.setNumberOfDay(numberOfDay);

		GetCampaignAgent cpAgentParam = new GetCampaignAgent();
		cpAgentParam.setUserId(_curUser.getUserId());
		cpAgentParam.setOrgId(orgId);
		cpAgentParam.setStatus(EnumType.CAMPAIGN_STATUS_ID.RUNNING.getValue());
		List<GetCampaignAgentResp> campaigns = clFreshService.getCampaignAgent(SESSION_ID, cpAgentParam).getResult();
		logger.info(" setUpdLeadParamForUncall campaign size : " + campaigns.size());
		if(campaigns == null || campaigns.isEmpty())
			return upLead;
		GetCampaignAgentResp campaign = campaigns.get(0);
		logger.info(" setUpdLeadParamForUncall campaign Id : " + campaign.getCpId()
				+ " setUpdLeadParamForUncall getLeadStatus : " + paramFresh.getLeadStatus());
		Integer cpId = campaign.getCpId();
		logger.info("CPID: {}", cpId);
		Integer distributionType = getDistributionRuleType(cpId, orgId);
		logger.info("distributionType: {}\r\nsetUpdLeadParamForUncall lead status: {}", distributionType, paramFresh.getLeadStatus());
		CfCpCallStrategyDefault cfCpCallStrategyDefault = null;
		if(EnumType.LEAD_STATUS.BUSY.getValue() == paramFresh.getLeadStatus()){
			int attemptBusy = modifiedDay == currentDay && currFresh.getAttemptBusy() != null
					? currFresh.getAttemptBusy() + 1
					: 1;
			upLead.setAttemptBusy(attemptBusy);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyDefault(cpId, orgId,
						paramFresh.getLeadStatus(), 1);
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}

		if(EnumType.LEAD_STATUS.NOANSWER.getValue() == paramFresh.getLeadStatus()){
			int attemptNoans = modifiedDay == currentDay && currFresh.getAttemptNoans() != null
					? currFresh.getAttemptNoans() + 1
					: 1;
			upLead.setAttemptNoans(attemptNoans);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyDefault(cpId, orgId,
						paramFresh.getLeadStatus(), 1);
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}

		if(EnumType.LEAD_STATUS.UNREACHABLE.getValue() == paramFresh.getLeadStatus()){
			int attemptUnreach = modifiedDay == currentDay && currFresh.getAttempUnreachable() != null
					? currFresh.getAttempUnreachable() + 1
					: 1;
			upLead.setAttempUnreachable(attemptUnreach);
			if(distributionType == EnumType.DISTRIBUTION_RULE.RUBY.getValue() && currFresh.getClType().equals("3")){
				cfCpCallStrategyDefault = cfCpCallStrategy.getCallStrategyDefault(cpId, orgId,
						paramFresh.getLeadStatus(), 1);
				if(cfCpCallStrategyDefault == null)
					return upLead;
			}
		}
		if(cfCpCallStrategyDefault != null){
			logger.info("cfCpCallStrategyDefault: {}\r\nsetUpdLeadParamForUncall cpcs name: {}",
					cfCpCallStrategyDefault, cfCpCallStrategyDefault.getConfigName());
			Calendar newNextCallTime = Calendar.getInstance();
			newNextCallTime.add(Calendar.MINUTE, cfCpCallStrategyDefault.getDuration());
			SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
			upLead.setNextCallTime(dateFormat.format(newNextCallTime.getTime()));

			if(upLead.getNumberOfDay() == cfCpCallStrategyDefault.getDay()
					&& upLead.getAttemptBusy() == cfCpCallStrategyDefault.getAttempt())
				if(cfCpCallStrategyDefault.getNextAction().equals("1"))
					upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
			return upLead;
		}
		GetCpCallStrategy csparam = new GetCpCallStrategy();
		csparam.setOrgId(orgId);
		csparam.setCpId(cpId);
		csparam.setCallStatus(paramFresh.getLeadStatus());
		csparam.setOrderPhoneNumber(1);
		DBResponse<List<GetCpCallStrategyResp>> cpcsList = provinceService.getCpCallStrategy(SESSION_ID, csparam);
		if(cpcsList == null || cpcsList.getResult().isEmpty())
			return upLead;
		GetCpCallStrategyResp cpcs = cpcsList.getResult().get(0);
		logger.info(" setUpdLeadParamForUncall cpcs name: " + cpcs.getConfigName());

		Calendar newNextCallTime = Calendar.getInstance();
		newNextCallTime.add(Calendar.MINUTE, cpcs.getDuration());
		SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
		upLead.setNextCallTime(dateFormat.format(newNextCallTime.getTime()));

		if(upLead.getNumberOfDay() == cpcs.getDay() && upLead.getAttemptBusy() == cpcs.getAttempt())
			if(cpcs.getNextAction().equals("1"))
				upLead.setLeadStatus(EnumType.LEAD_STATUS.CLOSED.getValue());
		return upLead;
	}

	private Integer getDistributionRuleType(Integer cpId, Integer orgId) {
		//Get campaign from config
		CPCampaignResp campaign = campaignService.getConfigCampaign(SESSION_ID, orgId, cpId);
		if (campaign == null)
			return null;

		//Get distribution rule from config
		GetCpDistributionRuleResp rule = campaignService.getConfigDistributionRules(SESSION_ID, orgId, cpId, campaign.getDistributionRule());
		if (rule == null)
			return null;

		return rule.getDistributionType();
	}

	@GetMapping
	public TMSResponse<List<GetOrderManagement8Resp>> getSoMgnList(GetOrderManagement8 params) throws TMSException {
		params.setLimit(params.getLimit() != null? params.getLimit() : Const.DEFAULT_PAGE_SIZE);
		params.setOrgId(getCurOrgId());
		// neu la teamleader chi cho xem cac order cua campain team do tham gia
		if(Helper.isTeamLeader(_curUser)){
			GetCampaignAgent cpAgentParam = new GetCampaignAgent();
			cpAgentParam.setUserId(_curUser.getUserId());
			cpAgentParam.setOrgId(getCurOrgId());
			cpAgentParam.setStatus(EnumType.CAMPAIGN_STATUS_ID.RUNNING.getValue());
			List<GetCampaignAgentResp> campaigns = clFreshService.getCampaignAgent(SESSION_ID, cpAgentParam)
					.getResult();
			logger.info("getSoMgnList campaign size : " + campaigns.size());
			if(campaigns.isEmpty())
				throw new TMSException(ErrorMessage.NOT_FOUND);
			List<Integer> cpIds = new ArrayList<>();
			for(int i = 0; i < campaigns.size(); i++)
				cpIds.add(campaigns.get(i).getCpId());
//            int cpId = campaigns.get(0).getCpId();
//            params.setCpId(cpId);
			params.setCpIds(cpIds);
		}
		if(params.getModifyDate() == null && (params.getCreatedate() == null || params.getCreatedate().isEmpty())){
			java.time.LocalDateTime beforeDate = java.time.LocalDateTime.now().minusDays(30);
			java.time.LocalDateTime now = java.time.LocalDateTime.now();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern("yyyyMMddHHmmss");
			params.setCreatedate(beforeDate.format(formatter) + "|" + now.format(formatter));
		}

		if(Helper.isAgent(_curUser))
			params.setAgId(_curUser.getUserId());
		OrderManagement8RespDto result = soService.getOrderManagerment(params, false);
//        List<GetOrderManagement5Resp> result = soService.getOrderManagerment(params);
//        Integer rowCount = soService.countOrder(params);
//        DBResponse<List<GetOrderManagement5Resp>> dbResponseSaleOrder = clFreshService.getOrderManagementV5(SESSION_ID, params);
//        if (dbResponseSaleOrder.getResult().isEmpty()) {
//            throw new TMSException(ErrorMessage.NOT_FOUND);
//        }
		if(result.getOrderManagement8Resp().isEmpty()) {
//			throw new TMSException(ErrorMessage.NOT_FOUND);
			TMSResponse.buildResponse(null, 0);
		}
//        return TMSResponse.buildResponse(dbResponseSaleOrder.getResult(), dbResponseSaleOrder.getRowCount());
		return TMSResponse.buildResponse(result.getOrderManagement8Resp(), result.getRowCount());
	}

	@PostMapping("/list")
	public TMSResponse<List<GetOrderManagement8Resp>> getSoMgnListV2(GetOrderManagement8 params) throws TMSException {
		return getSoMgnList(params);
	}

	@RequestMapping(value = "/csv", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> exportOrderManagement(@ModelAttribute("params") GetOrderManagement10 params)
			throws TMSException {
		boolean isTeamLeader = Helper.isTeamLeader(_curUser) || Helper.isTeamLeaderOther(_curUser);
		boolean isDirector = Helper.isDirector(_curUser);
		boolean isAdmin = Helper.isAdmin(_curUser);
		OrderManagementRequestDTO orderManagementRequestDTO = new OrderManagementRequestDTO();
		orderManagementRequestDTO.setSessionId(SESSION_ID);
		orderManagementRequestDTO.setUserId(getCurrentUser().getUserId());
		orderManagementRequestDTO.setOrgId(getCurOrgId());
		orderManagementRequestDTO.setTeamLeader(isTeamLeader);
		orderManagementRequestDTO.setDirector(isDirector);
		orderManagementRequestDTO.setAdmin(isAdmin);
		byte[] csvAsByte = soService.exportCSVOrderManagement(params, orderManagementRequestDTO);
		return TMSResponse.buildExcelFileResponse(csvAsByte, "order_management.csv");
	}

	@GetMapping("/{soId}")
	public TMSResponse<?> getSO(@PathVariable Integer soId) throws TMSException {

		SaleOrder soData = getSaleOrderDetailV2(soId);// dbResponseSaleOrder.getResult().get(0);
		GetLeadParamsV9 getFreshLeadParams = new GetLeadParamsV9();
		getFreshLeadParams.setLeadId(soData.getLeadId());
		getFreshLeadParams.setOrgId(getCurOrgId());
		DBResponse<List<CLFresh>> dbResponseCLFresh = clFreshService.getLeadV9(SESSION_ID, getFreshLeadParams);
		if(dbResponseCLFresh.getResult().isEmpty())
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		CLFresh leadData = dbResponseCLFresh.getResult().get(0);
		if (Helper.isAgent(_curUser) && _curUser.getUserId().intValue() != leadData.getAssigned()){
			return TMSResponse.buildResponse(true, 0, ErrorMessage.CAN_NOT_GET_LEAD.getMessage(), 400);
		}
		leadData = leadService.setCustomerData(SESSION_ID, leadData.getLeadId(), leadData);
		ResponeGetSaleOrderDto response = new ResponeGetSaleOrderDto();
		response.setSoData(soData);
		response.setLeadData(leadData);
		return TMSResponse.buildResponse(response);
	}

	private boolean isUnAllowChangeSOStatus(SaleOrder so) {
		return so.getStatus() == EnumType.SALE_ORDER_STATUS.VALIDATED.getValue()
				|| so.getStatus() == EnumType.SALE_ORDER_STATUS.UNASSIGNED.getValue()
				|| so.getStatus() == EnumType.SALE_ORDER_STATUS.DELAYVALIDATE.getValue();
	}

	@PutMapping("/updateSO")
	public TMSResponse<?> updateSO(@Valid @RequestBody UpdateSODto data) throws TMSException {
		logger.info("Body Sale Order request: {}", LogHelper.toJson(data));
		AppointmentDateResponse appointmentDateResponse = appointmentDateService.init();

		if (appointmentDateResponse.isActive()) {
			if (!StringUtility.isEmpty(data.getAppointmentDate())) {
				if (!appointmentDateService.isValid(data.getAppointmentDate())) {
					return TMSResponse.buildResponse(null, 0, "Appointment date invalid.", 400);
				}
			}
		}

		GetLeadParamsV9 getFreshLeadParams = new GetLeadParamsV9();
		getFreshLeadParams.setLeadId(data.getLeadId());
		DBResponse<List<CLFresh>> dbResponse = clFreshService.getLeadV9(SESSION_ID, getFreshLeadParams);
		if(dbResponse.getResult().isEmpty())
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		SaleOrder soData = getSaleOrderDetail(data.getSoId());

		if (appointmentDateResponse.isActive()) {
			OdSaleOrder odSaleOrder = new OdSaleOrder();
			LeadsRequest leadsRequest = new LeadsRequest();
			odSaleOrder.setSoId(soData.getSoId());
			odSaleOrder.setAppointmentDate(DateUtils.parse(data.getAppointmentDate(), PatternEpochVariable.SHORT_BIBLIOGRAPHY_EPOCH_PATTERN));
			leadsRequest.setAppointmentDate(odSaleOrder.getAppointmentDate());
			soService.updateSaleOrder(odSaleOrder);
			leadService.updateReminderCalls(data.getLeadId(), leadsRequest);
		}

		if(!isUnAllowChangeSOStatus(soData)){
			dbLog.writeSOStatusLog(_curUser.getUserId(), data.getSoId(), EnumType.SALE_ORDER_STATUS.NEW.getValue(),
					"updateSO");
			CLFresh cLFresh = dbResponse.getResult().get(0);
			UpdSaleOrderV2 updSaleOrder = modelMapper.map(cLFresh, UpdSaleOrderV2.class);
			updSaleOrder.setAmount(data.getAmount());
			updSaleOrder.setSoId(data.getSoId());
			updSaleOrder.setPaymentMethod(data.getPaymentMethod());
			// sale order update change to new (not depend on posting status)
			updSaleOrder.setStatus(EnumType.SALE_ORDER_STATUS.NEW.getValue());
			// updSaleOrder.setStatus(data.getStatus());
			updSaleOrder.setModifyby(getCurrentUser().getUserId());
			// update new properties
			if(data.getComboDiscount() != null)
				updSaleOrder.setDiscountCash1(Double.valueOf(data.getComboDiscount()));
			if(data.getUnit() != null)
				updSaleOrder.setUnit1(data.getUnit());
			if(data.getComboPercent() != null)
				updSaleOrder.setDiscountPercent1(Double.valueOf(data.getComboPercent()));
			if(data.getSaleDiscount() != null)
				updSaleOrder.setDiscountCash2(Double.valueOf(data.getSaleDiscount()));
			if(data.getSalePecent() != null)
				updSaleOrder.setDiscountPercent2(Double.valueOf(data.getSalePecent()));

			/*
			 * insSaleOrderV2.setUnit1(unit); insSaleOrderV2.setDiscountType1(discountType);
			 * insSaleOrderV2.setDiscountCash1(discountCash);
			 * insSaleOrderV2.setDiscountPercent1(discountPecent);
			 */

			DBResponse<?> response = logService.updSaleOrderV2(SESSION_ID, updSaleOrder);

			dbLog.writeAgentActivityLog(_curUser.getUserId(), "update sale order", "sale order", data.getSoId(),
					"so_status", EnumType.SALE_ORDER_STATUS.NEW.getValue() + "");

			// delete all SO ITEM
			logService.delSo(SESSION_ID, data.getSoId());
			// createSOItems(data.getProducts(), data.getSoId(), data.getAmount());
			// update SO ko quan tam toi check PRICE nua, 16/08/2019
			List<InsSaleOrderItemV2> lst = createSOItems(data.getProducts(), data.getSoId(), data.getAmount(), true,
					EnumType.PRODUCT_TYPE.NORMAL.getValue());
			// DBResponse responseItems = null;
			// save to DB
			if(lst != null && lst.size() > 0)
				// responseItems = logService.insSaleOrder(SESSION_ID, insSaleOrder);
				// write log history
//            LogHelper.writeLogLead(logService, data.getLeadId(), userId, EnumType.SALE_ORDER_STATUS.NEW.getValue(), "lead_status", "", LogHelper.toJson((Object) insSaleOrder), SESSION_ID);
				if(response.getErrorCode() == Const.INS_DB_SUCCESS){
					int itemno = 1;
					for(InsSaleOrderItemV2 insSaleOrderItem: lst){
						// int so_id = Integer.parseInt(response.getErrorMsg().trim());
						insSaleOrderItem.setSoId(data.getSoId());
						insSaleOrderItem.setItemNo(itemno++);
						logService.insSaleItemOrderV2(SESSION_ID, insSaleOrderItem);
					}
					String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_SO, getCurOrgId(), _curUser.getUserId());

					RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(data.getSoId()),
							String.valueOf(data.getAmount()));

				}
			return TMSResponse.buildResponse(response.getResult());
		}
		// 1 Mean COD
		if(data.getPaymentMethod() == 1){
			// TODO Call COD API here
		}
		return TMSResponse.buildResponse(false, 0, "unallow_update_so", 400);
	}

	@PostMapping
	public TMSResponse createSO(@Valid @RequestBody CreateSODto data) throws TMSException {
		logger.info("[createSO] " + LogHelper.toJson(data));
		int orgId = getCurOrgId();
		GetLeadParamsV4 getFreshLeadParams = new GetLeadParamsV4();
		getFreshLeadParams.setLeadId(data.getLeadId());
		if(data.getLeadStatus() != EnumType.LEAD_STATUS.APPROVED.getValue())
			return TMSResponse.buildResponse(null, 0, "Could not create SO because only Approve can create SO",
					HttpStatus.OK.value());
		Integer paymentType[] = {
				EnumType.PAYMENT_METHOD.BANKING.getValue(), EnumType.PAYMENT_METHOD.COD.getValue(),
				EnumType.PAYMENT_METHOD.DEPOSIT.getValue()
		};
		List<Integer> list = Arrays.asList(paymentType);
		if(!list.contains(data.getPaymentMethod()))
			throw new TMSException("Method payment not configure");

		DBResponse<List<CLFresh>> dbResponse = clFreshService.getLeadV4(SESSION_ID, getFreshLeadParams);
		if(dbResponse.getResult().isEmpty())
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		CLFresh cLFresh = dbResponse.getResult().get(0);
		int oldLeadStatus = cLFresh.getLeadStatus();
		// neu ko phai la tao LEAD manual (vi goi 2 request create lead = Approve vs tao
		// luon SO)
		// TODO check if create manual
		boolean isManual = false;
		if(data.getLeadType() != null && data.getLeadType().equals(Const.CREATE_SO_MANUAL_LEAD_TYPE))
			isManual = true;
		if(!isManual && oldLeadStatus == EnumType.LEAD_STATUS.APPROVED.getValue()){
			logger.info("Try to create SO with lead has been approved!" + data.getLeadId());
			return TMSResponse.buildResponse(null, 0,
					"Could not create SO because OLD LEAD has been 'Approved' ! Please contact Administrator",
					HttpStatus.OK.value());
		}

		GetSOParams soParams = new GetSOParams();
		soParams.setOrgId(orgId);
		soParams.setLeadId(data.getLeadId());
		DBResponse<List<SaleOrder>> dbSaleOrder = deliveryOrderService.getSaleOrder(SESSION_ID, soParams);

		if(dbSaleOrder.getRowCount() > 0){
			List<SaleOrder> lstDbSaleOrder = dbSaleOrder.getResult();
			for(SaleOrder so: lstDbSaleOrder)
				if(so.getStatus() == EnumType.SALE_ORDER_STATUS.PENDING.getValue()
						|| so.getStatus() == EnumType.SALE_ORDER_STATUS.NEW.getValue()){
					logger.info("Could not create SO because EXISTING ACTIVE SO ! Please contact Administrator |"
							+ data.getLeadId());
					return TMSResponse.buildResponse(null, 0,
							"Could not create SO because EXISTING ACTIVE SO ! Please contact Administrator",
							HttpStatus.OK.value());
				}
		}

		/*
		 * if (cLFresh.getResult() != null && oldLeadStatus ==
		 * EnumType.LEAD_STATUS.APPROVED_TEMP.getValue()) {
		 * logger.info("Try to create SO with lead has been approved TEMP!" +
		 * data.getLeadId()); return TMSResponse.buildResponse(null, 0,
		 * "Could not create SO because OLD LEAD has been 'Approved TEMP' ! Please contact Administrator"
		 * , HttpStatus.OK.value()); }
		 */
		// tao moi SO
		InsSaleOrder insSaleOrder = modelMapper.map(cLFresh, InsSaleOrder.class);
		insSaleOrder.setPaymentMethod(data.getPaymentMethod());
		// 0 Mean just created
		insSaleOrder.setStatus(EnumType.SALE_ORDER_STATUS.NEW.getValue());
		int userId = getCurrentUser().getUserId();
		insSaleOrder.setCreateby(userId);
		insSaleOrder.setAmount(data.getAmount());
		if(data.getAmount() <= 0 && data.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue())
			return TMSResponse.buildResponse(null, 0,
					"Could not create SO because COD must have price > 0, current price = " + data.getAmount(),
					HttpStatus.OK.value());
		// modify set cpID & userId from session + lead
		insSaleOrder.setCpId(cLFresh.getCpId());
		insSaleOrder.setAgId(isManual? data.getAgentId() : userId);

		List<InsSaleOrderItemV2> lst = createSOItems(data.getProducts(), 0, data.getAmount());
		DBResponse response = null;
		// save to DB
		if(lst != null && lst.size() > 0){
			response = logService.insSaleOrder(SESSION_ID, insSaleOrder);
			if(response.getErrorCode() == Const.INS_DB_SUCCESS){
				int so_id = Integer.parseInt(response.getErrorMsg().trim());
				int itemNo = 1;
				for(InsSaleOrderItemV2 insSaleOrderItem: lst){
					insSaleOrderItem.setSoId(so_id);
					insSaleOrderItem.setItemNo(itemNo++);
					logService.insSaleItemOrderV2(SESSION_ID, insSaleOrderItem);
				}

				dbLog.writeAgentActivityLog(userId, "create sale order", "sale order", so_id, "so_status",
						EnumType.SALE_ORDER_STATUS.NEW.getValue() + "");
				dbLog.writeSOStatusLog(userId, so_id, EnumType.SALE_ORDER_STATUS.NEW.getValue(), "createSO");

				// write to redis
				String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_SO, orgId, _curUser.getUserId());
				RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(so_id),
						String.valueOf(data.getLeadId()));

				String keyAmount = RedisHelper.createRedisKey(Const.REDIS_PREFIX_AMOUNT, orgId, _curUser.getUserId());
				RedisHelper.saveRedis(stringRedisTemplate, keyAmount, String.valueOf(so_id),
						String.valueOf(data.getAmount().intValue()));

			}

			// update cpid and agent to fresh
			UpdLead updLead = new UpdLead();
			updLead.setLeadId(data.getLeadId());
			updLead.setCpId(data.getCpId());
			updLead.setAssigned(data.getAgentId());
			updLead.setResult(oldLeadStatus);
//            updLead.setLeadStatus(EnumType.LEAD_STATUS.APPROVED_TEMP.getValue());
			logService.updLead(SESSION_ID, updLead);

			dbLog.writeAgentActivityLog(_curUser.getUserId(), "update lead in createSO", "lead", data.getLeadId(),
					"lead_status", data.getLeadStatus().toString());
//			CLFresh lead = new CLFresh();
//			ModelMapper modelMapper = new ModelMapper();
//			lead = modelMapper.map(cLFresh, CLFresh.class);
//			lead.setLeadId(data.getLeadId());

			dbLog.writeLeadStatusLog(_curUser.getUserId(), data.getLeadId(), oldLeadStatus,
					cLFresh.getComment());
//			dbLog.writeLeadStatusLogV2(_curUser.getUserId(),lead, data.getLeadId(), oldLeadStatus, cLFresh.getComment());
			
			
		}

		// 1 Mean COD
		if(data.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()){
			// TODO Call COD API here
		}

		// neu la callback thi xoa lead tuong ung ben callback
		// if (cLFresh.getLeadStatus() ==
		// EnumType.LEAD_STATUS.CALLBACK_PROPECT.getValue() || cLFresh.getLeadStatus()
		// == EnumType.LEAD_STATUS.CALLBACK_CONSULT.getValue()) {
		if(Helper.isCallback(oldLeadStatus)){
			logService.delCallback(SESSION_ID, cLFresh.getLeadId());
			dbLog.writeAgentActivityLog(_curUser.getUserId(), "delete callback in createSO", "lead", data.getLeadId(),
					"lead_status", data.getLeadStatus().toString());
//			CLFresh lead = new CLFresh();
//			ModelMapper modelMapper = new ModelMapper();
//			lead = modelMapper.map(cLFresh, CLFresh.class);
//			lead.setLeadId(data.getLeadId());

			dbLog.writeLeadStatusLog(_curUser.getUserId(), data.getLeadId(), oldLeadStatus,
					cLFresh.getComment());
//			dbLog.writeLeadStatusLogV2(_curUser.getUserId(),lead, data.getLeadId(), oldLeadStatus, cLFresh.getComment());
		}
		return TMSResponse.buildResponse(response.getResult());
	}

	private InsSaleOrderV2 setSaleOrderLevelValue(InsSaleOrderV2 insSaleOrderV2, int level, CreateSODto data) {
		switch(level){
		case 0:
			break;
		case 1:
			insSaleOrderV2 = this.setSaleOrderLevelValue(insSaleOrderV2, 1, 1, data.getUnit(),
					Double.valueOf(data.getComboDiscount() != null? data.getComboDiscount() : 0),
					Double.valueOf(data.getComboPercent() != null? data.getComboPercent() : 0));
			break;
		case 2:
			insSaleOrderV2 = this.setSaleOrderLevelValue(insSaleOrderV2, 2, 2, data.getUnit(),
					Double.valueOf(data.getSaleDiscount() != null? data.getSaleDiscount() : 0),
					Double.valueOf(data.getSalePercent() != null? data.getSalePercent() : 0));
			break;
		case 3:
			break;
		case 4:
			break;
		default:
			break;
		}
		return insSaleOrderV2;
	}

	private InsSaleOrderV2 setSaleOrderLevelValue(InsSaleOrderV2 insSaleOrderV2, int level, Integer discountType,
			Integer unit, Double discountCash, Double discountPecent) {
		logger.info("------------ +++++++++ {}|{}|{}|{}|{}", discountCash, discountPecent, unit, discountType, level);
		switch(level){
		case 0:
			insSaleOrderV2.setUnit1(unit);
			insSaleOrderV2.setDiscountType1(discountType);
			insSaleOrderV2.setDiscountCash1(discountCash);
			insSaleOrderV2.setDiscountPercent1(discountPecent);

			insSaleOrderV2.setUnit2(unit);
			insSaleOrderV2.setDiscountType2(discountType);
			insSaleOrderV2.setDiscountCash2(discountCash);
			insSaleOrderV2.setDiscountPercent2(discountPecent);

			insSaleOrderV2.setUnit3(unit);
			insSaleOrderV2.setDiscountType3(discountType);
			insSaleOrderV2.setDiscountCash3(discountCash);
			insSaleOrderV2.setDiscountPercent3(discountPecent);

			insSaleOrderV2.setUnit4(unit);
			insSaleOrderV2.setDiscountType4(discountType);
			insSaleOrderV2.setDiscountCash4(discountCash);
			insSaleOrderV2.setDiscountPercent4(discountPecent);
			break;
		case 1:
			insSaleOrderV2.setUnit1(unit);
			insSaleOrderV2.setDiscountType1(discountType);
			insSaleOrderV2.setDiscountCash1(discountCash);
			insSaleOrderV2.setDiscountPercent1(discountPecent);
			break;
		case 2:
			insSaleOrderV2.setUnit2(unit);
			insSaleOrderV2.setDiscountType2(discountType);
			insSaleOrderV2.setDiscountCash2(discountCash);
			insSaleOrderV2.setDiscountPercent2(discountPecent);
			break;
		case 3:
			insSaleOrderV2.setUnit3(unit);
			insSaleOrderV2.setDiscountType3(discountType);
			insSaleOrderV2.setDiscountCash3(discountCash);
			insSaleOrderV2.setDiscountPercent3(discountPecent);
			break;
		case 4:
			insSaleOrderV2.setUnit4(unit);
			insSaleOrderV2.setDiscountType4(discountType);
			insSaleOrderV2.setDiscountCash4(discountCash);
			insSaleOrderV2.setDiscountPercent4(discountPecent);
			break;
		default:
			break;
		}

		return insSaleOrderV2;
	}

	@PostMapping("/v2")
	public TMSResponse createSO2(@Valid @RequestBody CreateSODto data) throws TMSException {
		AppointmentDateResponse appointmentDateResponse = appointmentDateService.init();
		logger.info("[createSO2] " + LogHelper.toJson(data));
		int orgId = getCurOrgId();
		GetLeadParamsV4 getFreshLeadParams = new GetLeadParamsV4();
		getFreshLeadParams.setLeadId(data.getLeadId());
		if(data.getLeadStatus() != EnumType.LEAD_STATUS.APPROVED.getValue())
			return TMSResponse.buildResponse(null, 0, "Could not create SO because only Approve can create SO",
					HttpStatus.OK.value());
		Integer[] paymentType = {
				EnumType.PAYMENT_METHOD.BANKING.getValue(), EnumType.PAYMENT_METHOD.COD.getValue(),
				EnumType.PAYMENT_METHOD.DEPOSIT.getValue(), EnumType.PAYMENT_METHOD.ESPAY.getValue(),
				EnumType.PAYMENT_METHOD._2C2P.getValue()
		};
		List<Integer> list = Arrays.asList(paymentType);
		if(!list.contains(data.getPaymentMethod()))
			throw new TMSException("Method payment not configure");

		DBResponse<List<CLFresh>> dbResponse = clFreshService.getLeadV4(SESSION_ID, getFreshLeadParams);
		if(dbResponse.getResult().isEmpty())
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		CLFresh cLFresh = dbResponse.getResult().get(0);
		int oldLeadStatus = cLFresh.getLeadStatus();
		// neu ko phai la tao LEAD manual (vi goi 2 request create lead = Approve vs tao
		// luon SO)
		// TODO check if create manual
		boolean isManual = false;
		if(data.getLeadType() != null && data.getLeadType().equals(Const.CREATE_SO_MANUAL_LEAD_TYPE))
			isManual = true;
		if(!isManual && oldLeadStatus == EnumType.LEAD_STATUS.APPROVED.getValue()){
			logger.info("Try to create SO with lead has been approved!" + data.getLeadId());
			return TMSResponse.buildResponse(null, 0,
					"Could not create SO because OLD LEAD has been 'Approved' ! Please contact Administrator",
					HttpStatus.OK.value());
		}

		GetSOParams soParams = new GetSOParams();
		soParams.setOrgId(orgId);
		soParams.setLeadId(data.getLeadId());
		DBResponse<List<SaleOrder>> dbSaleOrder = deliveryOrderService.getSaleOrder(SESSION_ID, soParams);

		if(dbSaleOrder.getRowCount() > 0){
			List<SaleOrder> lstDbSaleOrder = dbSaleOrder.getResult();
			for(SaleOrder so: lstDbSaleOrder)
				if(so.getStatus() == EnumType.SALE_ORDER_STATUS.PENDING.getValue()
						|| so.getStatus() == EnumType.SALE_ORDER_STATUS.NEW.getValue()){
					logger.info("Could not create SO because EXISTING ACTIVE SO ! Please contact Administrator |"
							+ data.getLeadId());
					return TMSResponse.buildResponse(null, 0,
							"Could not create SO because EXISTING ACTIVE SO ! Please contact Administrator",
							HttpStatus.OK.value());
				}
		}
		// tao moi SO
		InsSaleOrderV2 insSaleOrder = modelMapper.map(cLFresh, InsSaleOrderV2.class);
		insSaleOrder.setPaymentMethod(data.getPaymentMethod());
		// 0 Mean just created
		insSaleOrder.setStatus(EnumType.SALE_ORDER_STATUS.NEW.getValue());
		int userId = getCurrentUser().getUserId();
		insSaleOrder.setCreateby(userId);
		insSaleOrder.setAmount(data.getAmount());
		if(!StringHelper.isNullOrEmpty(data.getPhone()))
			insSaleOrder.setLeadPhone(data.getPhone());
		if(data.getAmount() <= 0 && data.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()) {
			logger.info("Could not create SO because COD must have price > 0, current price = " + data.getAmount()
					+ " | " + data.getLeadId());
			return TMSResponse.buildResponse(null, 0,
					"Could not create SO because COD must have price > 0, current price = " + data.getAmount(),
					HttpStatus.OK.value());
		}
		// modify set cpID & userId from session + lead
		insSaleOrder.setCpId(cLFresh.getCpId());
		insSaleOrder.setAgId(isManual? data.getAgentId() : userId);

		// set all value = 0
		insSaleOrder = this.setSaleOrderLevelValue(insSaleOrder, 0, 0, 0, 0D, 0D);
		String globalLevel = "0";
		int level = 0;
		int prdType = 1;
		boolean hasCombo = true;
		// bo check combo
		/*
		 * for (Integer productId : data.getProducts().keySet()) { if
		 * (data.getProducts().get(productId).getProdType() != null &&
		 * data.getProducts().get(productId).getProdType() ==
		 * EnumType.PRODUCT_TYPE.COMBO.getValue()) hasCombo = true; }
		 */
		if(hasCombo){
			// add more properties for combo product
			// 1) get discount LEVEL
			globalLevel = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 7, 1);

			if(globalLevel == null)
				globalLevel = "0";// mac dinh khong co discount
			// neu co discount thi set property phu thuoc vao level
			if(!globalLevel.equals("0")){// 2) set property depend on level
				level = Integer.parseInt(globalLevel);
				for(int i = 1; i < level + 1; i++)
					insSaleOrder = this.setSaleOrderLevelValue(insSaleOrder, i, data);
			}
		}
		insSaleOrder.setDiscountLevel(level);

		List<InsSaleOrderItemV2> lst = createSOItems(data.getProducts(), 0, data.getAmount(), false, prdType,
				data.getSaleDiscount());
		DBResponse response = null;
		// save to DB
		if(lst != null && lst.size() > 0){
			response = logService.insSaleOrderV2(SESSION_ID, insSaleOrder);
			if(response.getErrorCode() == Const.INS_DB_SUCCESS){
				int so_id = Integer.parseInt(response.getErrorMsg().trim());
				int itemNo = 1;
				for(InsSaleOrderItemV2 insSaleOrderItem: lst){
					insSaleOrderItem.setSoId(so_id);
					insSaleOrderItem.setItemNo(itemNo++);
					insSaleOrderItem.setUnit(data.getUnit());
					logService.insSaleItemOrderV2(SESSION_ID, insSaleOrderItem);
				}

				dbLog.writeAgentActivityLog(userId, "create sale order", "sale order", so_id, "so_status",
						EnumType.SALE_ORDER_STATUS.NEW.getValue() + "");
				dbLog.writeSOStatusLog(userId, so_id, EnumType.SALE_ORDER_STATUS.NEW.getValue(), "createSO");

				// write to redis
				String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_SO, orgId, _curUser.getUserId());
				RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(so_id),
						String.valueOf(data.getLeadId()));

				String keyAmount = RedisHelper.createRedisKey(Const.REDIS_PREFIX_AMOUNT, orgId, _curUser.getUserId());
				RedisHelper.saveRedis(stringRedisTemplate, keyAmount, String.valueOf(so_id),
						String.valueOf(data.getAmount().intValue()));

				if (appointmentDateResponse.isActive()) {
					ClFresh lead = leadService.findOneByLeadId(cLFresh.getLeadId());
					OdSaleOrder saleOrderRequest = new OdSaleOrder();
					saleOrderRequest.setSoId(so_id);
					saleOrderRequest.setAppointmentDate(lead.getAppointmentDate());
					soService.updateSaleOrder(saleOrderRequest);
				}
			}

			// update cpid and agent to fresh
			UpdLead updLead = new UpdLead();
			updLead.setLeadId(data.getLeadId());
			updLead.setCpId(data.getCpId());
			updLead.setAssigned(data.getAgentId());
			logService.updLead(SESSION_ID, updLead);

			dbLog.writeAgentActivityLog(_curUser.getUserId(), "update lead in createSO", "lead", data.getLeadId(),
					"lead_status", data.getLeadStatus().toString());
//			CLFresh lead = new CLFresh();
//			ModelMapper modelMapper = new ModelMapper();
//			lead = modelMapper.map(cLFresh, CLFresh.class);
//			lead.setLeadId(data.getLeadId());

			dbLog.writeLeadStatusLog(_curUser.getUserId(), data.getLeadId(), oldLeadStatus,
					cLFresh.getComment());
//			dbLog.writeLeadStatusLogV2(_curUser.getUserId(), lead,data.getLeadId(), oldLeadStatus, cLFresh.getComment());
		}

		// 1 Mean COD
		if(data.getPaymentMethod() == EnumType.PAYMENT_METHOD.COD.getValue()){
			// TODO Call COD API here
		}

		// neu la callback thi xoa lead tuong ung ben callback
		// if (cLFresh.getLeadStatus() ==
		// EnumType.LEAD_STATUS.CALLBACK_PROPECT.getValue() || cLFresh.getLeadStatus()
		// == EnumType.LEAD_STATUS.CALLBACK_CONSULT.getValue()) {
		if(Helper.isCallback(oldLeadStatus)){
			logService.delCallback(SESSION_ID, cLFresh.getLeadId());
			dbLog.writeAgentActivityLog(_curUser.getUserId(), "delete callback in createSO", "lead", data.getLeadId(),
					"lead_status", data.getLeadStatus().toString());
//			CLFresh lead = new CLFresh();
//			ModelMapper modelMapper = new ModelMapper();
//			lead = modelMapper.map(cLFresh, CLFresh.class);
//			lead.setLeadId(data.getLeadId());

			dbLog.writeLeadStatusLog(_curUser.getUserId(), data.getLeadId(), oldLeadStatus,
					cLFresh.getComment());
//			dbLog.writeLeadStatusLogV2(_curUser.getUserId(), lead,data.getLeadId(), oldLeadStatus, cLFresh.getComment());
		}
		return TMSResponse.buildResponse(response.getResult());
	}

	public void deleteOldSOItems(Integer soId) {

	}

	public List<InsSaleOrderItemV2> createSOItems(HashMap<Integer, SOProductDTO> products, Integer soId,
			Double expectedAmount, Integer prodType) throws TMSException {
		return createSOItems(products, soId, expectedAmount, false, prodType);
	}

	public List<InsSaleOrderItemV2> createSOItems(HashMap<Integer, SOProductDTO> products, Integer soId,
			Double expectedAmount) throws TMSException {
		return createSOItems(products, soId, expectedAmount, false, EnumType.PRODUCT_TYPE.NORMAL.getValue());
	}

	public List<InsSaleOrderItemV2> createSOItems(HashMap<Integer, SOProductDTO> products, Integer soId,
			Double expectedAmount, Boolean isUpdate, Integer prodType, Integer saleDiscountValue) throws TMSException {
		Double amount = (double) 0;
		Boolean hasCombo = false;

		List<InsSaleOrderItemV2> lst = new ArrayList<>();

		for(Integer productId: products.keySet()){
			int prdType = EnumType.PRODUCT_TYPE.NORMAL.getValue();
			GetProductV2 getProductParams = new GetProductV2();
			getProductParams.setProdId(productId);
			DBResponse<List<PDProduct>> dbGetProductResponse = clProductService.getProductV2(SESSION_ID,
					getProductParams);
			if(dbGetProductResponse.getResult().isEmpty())
				// TODO Change to product not found here
				throw new TMSException(ErrorMessage.NOT_FOUND);

			// TODO need to get product from onather call????? not get from DB
			PDProduct productFromDB = dbGetProductResponse.getResult().get(0);
			InsSaleOrderItemV2 insSaleOrderItem = new InsSaleOrderItemV2();
			insSaleOrderItem.setSoId(soId);
			insSaleOrderItem.setProdId(productId);
			insSaleOrderItem.setQuantity(products.get(productId).getQuantity());
			if(productFromDB.getProductType() != null
					&& productFromDB.getProductType() == EnumType.PRODUCT_TYPE.COMBO.getValue()){
				if(productFromDB.getDiscountCash() != null)
					insSaleOrderItem.setDiscountCash(productFromDB.getDiscountCash());
				if(productFromDB.getDiscountPercent() != null)
					insSaleOrderItem.setDiscountPercent(productFromDB.getDiscountPercent());
				if(productFromDB.getUnit() != null)
					insSaleOrderItem.setUnit(productFromDB.getUnit());
				if(productFromDB.getListPrice() != null)
					insSaleOrderItem.setListPrice(productFromDB.getListPrice());
			} else if(productFromDB.getProductType() == EnumType.PRODUCT_TYPE.NORMAL.getValue()){
				/*
				 * if (saleDiscountValue != null)
				 * insSaleOrderItem.setDiscountCash((double)saleDiscountValue);
				 */
			}
			String[] prices = productFromDB.getPrice().split("\\|");
			if(products.get(productId).getPriceIndex() > prices.length - 1)
				throw new TMSException(ErrorMessage.BAD_REQUEST);
			Double productPrice = Double.parseDouble(prices[products.get(productId).getPriceIndex()]);
			insSaleOrderItem.setPrice(productPrice);
			insSaleOrderItem.setAmount(products.get(productId).getQuantity() * productPrice);
			insSaleOrderItem.setCreateby(getCurrentUser().getUserId());
			if(products.get(productId).getProdType() != null){
				prdType = products.get(productId).getProdType();
				if(prdType == EnumType.PRODUCT_TYPE.COMBO.getValue())
					hasCombo = true;
			}
			insSaleOrderItem.setProductType(prdType);
			amount += insSaleOrderItem.getAmount();

			lst.add(insSaleOrderItem);
		}
		logger.info("{}|**************************************{}|{} {}", prodType, amount, expectedAmount, lst.size());
		return lst;
	}

	public List<InsSaleOrderItemV2> createSOItems(HashMap<Integer, SOProductDTO> products, Integer soId,
			Double expectedAmount, Boolean isUpdate, Integer prodType) throws TMSException {
		return createSOItems(products, soId, expectedAmount, false, prodType, null);
	}

	private SaleOrder getSaleOrderDetail(Integer soId) throws TMSException {
		GetSOV2 params = new GetSOV2();
		params.setSoId(soId);
		params.setOrgId(getCurrentOriganationId());
		DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrderV2(SESSION_ID, params);
		if(dbResponseSaleOrder.getResult().isEmpty())
			throw new TMSException(ErrorMessage.NOT_FOUND);
		SaleOrder soData = dbResponseSaleOrder.getResult().get(0);
		return soData;
	}

	private SaleOrder getSaleOrderDetailV2(Integer soId) throws TMSException {
		GetSOV3 params = new GetSOV3();
		params.setSoId(soId);
		params.setOrgId(getCurrentOriganationId());
		DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrderV5(SESSION_ID, params);
		if(dbResponseSaleOrder.getResult().isEmpty())
			throw new TMSException(ErrorMessage.NOT_FOUND);
		SaleOrder soData = dbResponseSaleOrder.getResult().get(0);
		return soData;
	}

	private List<ProductDto> getProductListBySoId(int soId, int orgId, Integer partnerId) {
		List<ProductDto> lst = new ArrayList<>();
		GetSoItem getSoItem = new GetSoItem();
		// getSoItem.setSoId(insDoNew.getSoId());

		getSoItem.setSoId(soId);
		DBResponse<List<GetSoItemResp>> responseSOItem = deliveryOrderService.getSaleOrderItem(SESSION_ID, getSoItem);

		// lay tong gia tri don hang
		Double totalAmount = 0d;

		GetSOV2 params = new GetSOV2();
		params.setSoId(soId);
		params.setOrgId(orgId);
		DBResponse<List<SaleOrder>> resSO = deliveryOrderService.getSaleOrderV2(SESSION_ID, params);
		if(!resSO.getResult().isEmpty()){
			SaleOrder so = resSO.getResult().get(0);
			totalAmount = so.getAmount();
		}

		List<GetSoItemResp> lstSOItem = responseSOItem.getResult();

		if(lstSOItem.size() > 0){
			// tinh gia tri chia deu cho so loai san pham trong don hang
			Double amountPerSOItem = 0d;
			if(totalAmount > 0)
				amountPerSOItem = totalAmount / lstSOItem.size();

			for(int j = 0; j < lstSOItem.size(); j++){

				int proId = lstSOItem.get(j).getProdId();
				// get product detail
				GetProductParams productParams = new GetProductParams();
				productParams.setProdId(proId);

				DBResponse<List<PDProduct>> dbProd = clProductService.getProduct(SESSION_ID, productParams);
				if(dbProd.getResult().isEmpty())
					return new ArrayList<>();// khong cho phep tao don hang nay

				GetProductMapping productMapping = new GetProductMapping();
				productMapping.setProductId(proId);
				productMapping.setPartnerId(partnerId);

				DBResponse<List<GetProductMappingResp>> dbProdMapping = clProductService.getProductMapping(SESSION_ID,
						productMapping);
				if(dbProdMapping.getResult().isEmpty())
					return new ArrayList<>();// khong cho phep tao don hang nay

				List<PDProduct> lstProd = dbProd.getResult();

				// tinh gia tri discount tinh cho loai san pham
				Double amountPerProduct = 0d;
				if(amountPerSOItem > 0)
					amountPerProduct = amountPerSOItem / lstProd.size();

				for(int i = 0; i < lstProd.size(); i++){
					GetProductMappingResp productMappingResp = dbProdMapping.getResult().get(0);

					ProductDto product = new ProductDto();
					product.setProId(proId);
					// gan nhan la ten san pham lay theo i
					product.setProName(lstProd.get(i).getName());
					// lay theo don hang
					int qty = lstSOItem.get(j).getQuantity();
					int qtyCombo = 1;
					Double price = (double) lstSOItem.get(j).getPrice();

					// check product la combo hay khong
					if(lstProd.get(i).getProductType() != null && lstProd.get(i).getProductType() == 2){
						GetProductCombo getProductCombo = new GetProductCombo();
						GetProductComboResp comboResps = null;
						getProductCombo.setComboId(proId);
						DBResponse<List<GetProductComboResp>> comboDb = clProductService.getProductCombo(SESSION_ID,
								getProductCombo);
						if(!comboDb.getResult().isEmpty()){
							comboResps = comboDb.getResult().get(0); // 1 combo chi co 1 ban ghi mapping ben product
																		// combo nen chi can get 0
							qtyCombo = comboResps.getQuantity();
							price = BigDecimal.valueOf(comboResps.getPrice() / qtyCombo)
									.setScale(2, RoundingMode.HALF_UP).doubleValue();
						}
					}

					// tinh gia tri 1 mat hang
					if(amountPerProduct > 0)
						price = BigDecimal.valueOf(amountPerProduct / (qty * qtyCombo))
								.setScale(2, RoundingMode.HALF_UP).doubleValue();

					product.setPrice(price);
					product.setQty(qty * qtyCombo);

					if(productMappingResp.getPartnerProductCode() != null)
						product.setProPartnerCode(productMappingResp.getPartnerProductCode());

					product.setProCode(productMappingResp.getPartnerProductId().toString());
					lst.add(product);
				}
			}
			if(lstSOItem.size() != lst.size())
				return new ArrayList<>();// khong cho phep tao don hang nay

			// TODO need change to list product not only one
//            deliveryOrder.setProdId(lstSOItem.get(0).getProdId());
		}
		return lst;
	}

	private List<ProductDto> getProductListBySoId(InsDoNew insDoNew, Integer partnerId) {
		return getProductListBySoId(insDoNew.getSoId(), insDoNew.getOrgId(), partnerId);
	}

	@GetMapping("/GetSoValidationList")
	public TMSResponse getSONeedValidate(@RequestParam(required = false) Map<String, String> requestParams)
			throws TMSException {
		GetSOParams params = new GetSOParams();
		if(requestParams.containsKey("campaignId"))
			params.setCpId(Integer.parseInt(requestParams.get("campaignId")));
		if(requestParams.containsKey("agentId"))
			params.setAgId(Integer.parseInt(requestParams.get("agentId")));
		params.setOrgId(getCurrentUser().getOrgId());
		// TODO need to change to need validate status
		params.setPaymentMethod(1);
		DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrder(SESSION_ID, params);
		return TMSResponse.buildResponse(dbResponseSaleOrder.getResult());
	}

	@RequestMapping(value = "/ValidationList", method = {RequestMethod.GET, RequestMethod.POST})
	public TMSResponse<?> getValidationList(@ModelAttribute("params") GetValidation7 params) throws TMSException {
		if(params.getLimit() == null){
			params.setLimit(Const.DEFAULT_PAGE_SIZE);
		}

		params.setOrgId(getCurrentUser().getOrgId());
		if(params.getModifyDate() == null && (params.getCreatedate() == null || params.getCreatedate().isEmpty())){
			java.time.LocalDateTime beforeDate = java.time.LocalDateTime.now().minusDays(30);
			java.time.LocalDateTime now = java.time.LocalDateTime.now();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern("yyyyMMddHHmmss");
			params.setCreatedate(beforeDate.format(formatter) + "|" + now.format(formatter));
		}
		// TODO need to change to need validate status
		ValidationRespDto validationRespDto = soService.getValidation(params, false);

		return TMSResponse.buildResponse(validationRespDto.getGetValidation7Resps(), validationRespDto.getRowCount());
	}

	@RequestMapping(value = "/ValidationList/csv", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> exportValidation(@ModelAttribute("params") GetValidation7 params) throws TMSException {
		if(!Helper.hasExportPermission(_curUser))
			return TMSResponse.buildDeninedPermission();
		boolean isTeamLeader = Helper.isTeamLeader(_curUser) || Helper.isTeamLeaderOther(_curUser);
		boolean isDirector = Helper.isDirector(_curUser);
		OrderManagementRequestDTO orderManagementRequestDTO = new OrderManagementRequestDTO();
		orderManagementRequestDTO.setSessionId(SESSION_ID);
		orderManagementRequestDTO.setUserId(getCurrentUser().getUserId());
		orderManagementRequestDTO.setOrgId(getCurOrgId());
		orderManagementRequestDTO.setTeamLeader(isTeamLeader);
		orderManagementRequestDTO.setDirector(isDirector);
		byte[] csvAsByte = soService.exportCSVValidation(params, orderManagementRequestDTO);
		return TMSResponse.buildExcelFileResponse(csvAsByte, "so_validation.csv");
	}

	private boolean CreateDONew(InsDoNew insDoNew, Integer leadId, int _curOrgId) throws TMSException {
		AppointmentDateResponse appointmentDateResponse = appointmentDateService.init();
		logger.info("########################################################");
		StringBuilder errMessage = new StringBuilder();
		// get lead detail
		GetLeadParamsV8 clFreshParams = new GetLeadParamsV8();
		clFreshParams.setLeadId(leadId);
		clFreshParams.setOrgId(_curOrgId);
		DBResponse<List<CLFresh>> dbfresh = clFreshService.getLeadV8(SESSION_ID, clFreshParams);
		logger.info("--------------------------- {}", leadId);
		if(dbfresh.getResult().isEmpty()){
			logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
			errMessage.append("LEAD NOT FOUND " + leadId + "  " + _curOrgId);
			insDoNew.setErrorMessage(errMessage.toString());
			logService.insDoNew(SESSION_ID, insDoNew);
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		}

		CLFresh fresh = dbfresh.getResult().get(0);
		int provinceId = Integer.parseInt(fresh.getProvince());
		int subDistId = Integer.parseInt(fresh.getSubdistrict());
		int neighborId = 0;
		String zipcode = "";
		if(!StringHelper.isNullOrEmpty(fresh.getNeighborhood()))
			neighborId = Integer.parseInt(fresh.getNeighborhood());

		if(!StringHelper.isNullOrEmpty(fresh.getPostalCode()))
			zipcode = fresh.getPostalCode();

		boolean hasProdInFFToCreateDO = true;
		String prodMessage = "";
		// TODO need to read from config default value
		Integer parterId = 1;
		Integer warehouseId = 1;
		Integer lastmileId = 1;
		Integer threePl = 1;
		Integer pickupId = 1;
		String warehouseShortname = "";
		boolean isContinue = true;
		List<ProductDto> lst = new ArrayList<>();
		// Tiet kiem call toi Fresh, gui toi QUEUE o day
		// String message = fresh.getAgcId() + "|" + fresh.getClickId();
		logger.info("123 ####################################");
		// TODO need get agc_id

		LocationHelper locationHelper = new LocationHelper();
		DeliveryDto deliveryDto = new DeliveryDto(fresh);
		deliveryDto.setSESSION_ID(SESSION_ID);
		deliveryDto.setComment(fresh.getComment());
		String mappingType = "1";
		int mappingLocation = provinceId;
		try{
			GetDefaultDOV3 getDefaultDO = new GetDefaultDOV3();
			getDefaultDO.setOrgId(_curOrgId);
			getDefaultDO.setProvinceId(provinceId);
			// TODO need to implement new code
			mappingType = RedisHelper.getGlobalParamValue(stringRedisTemplate, _curOrgId, 9, 2);
			logger.info("mappingType ======= {}", mappingType);
			switch(mappingType){
			case "1":
				mappingLocation = provinceId;
				break;
			case "2":
				mappingLocation = Integer.parseInt(fresh.getDistrict());
				break;
			case "3":
				mappingLocation = subDistId;
				break;
			case "4":
				mappingLocation = Integer.parseInt(fresh.getNeighborhood());
				break;
			}

			/*
			 * if (_COUNTRY.equals("ID"))//neu la Indo thi mapping default do theo
			 * subdistrict de tim warehouse and lastmile
			 * getDefaultDO.setProvinceId(subDistId);
			 */
			getDefaultDO.setProvinceId(mappingLocation);
			DBResponse<List<GetDefaultDOResp>> dbDo = deliveryOrderService.getDefaultDOV3(SESSION_ID, getDefaultDO);
			logger.info("provinceId = {}", provinceId);

			if(dbDo.getResult().isEmpty())
				parterId = 1;
			else{
				parterId = dbDo.getResult().get(0).getPartnerId();
				// gan code cua warehouse = warehouse name (DB desinger)
				// String tmpWarehouseId = dbDo.getResult().get(0).getWarehouseName();
				// warehouseId = Integer.parseInt(tmpWarehouseId);
				warehouseId = dbDo.getResult().get(0).getWarehouseId();

				logger.info("warehouseId = {}", warehouseId);

				warehouseShortname = dbDo.getResult().get(0).getWarehouseShortname();
				// TODO need to read from config default value
				lastmileId = dbDo.getResult().get(0).getLastmileId();
				/*
				 * if (warehouseId == Const.KERRY_FFM_PARTNER_ID || warehouseId ==
				 * Const.SAP_FFM_PARTNER_ID) {//neu la warehouse cua SAP FFM thi dat = ma cua
				 * SAP lastmile warehouseId = lastmileId; }
				 */
				pickupId = dbDo.getResult().get(0).getPickupId();
				logger.info("{} {} {}", SESSION_ID, insDoNew.getSoId(), LogHelper.toJson(dbDo.getResult().get(0)));
			}
			logger.info("**************************** " + parterId);
			// get list product
			lst = this.getProductListBySoId(insDoNew, parterId);
			// send to queue
			if(fresh.getTerms() != null && fresh.getClickId() != null
					&& String.valueOf(EnumType.AGENTCY_TYPE.CPO.getValue()).equals(fresh.getTerms())){
				String payout = "0";
				if(fresh.getPrice() != null)
					payout = fresh.getPrice();
				String offerId = "0";
				if(fresh.getOfferId() != null)
					offerId = fresh.getOfferId();
				int numOfProd = 1;
//				if(fresh.getAgcId() == Const.AGENCY_ADFLEX)
				if (lst.isEmpty())
					numOfProd = 1;
				else {
					numOfProd = 0;
					for (ProductDto productDto : lst)
						numOfProd += productDto.getQty();
				}
				String message = "";
				if(null != fresh.getTrackerId() && 0 != fresh.getTrackerId()) {
					payout = Integer.toString(numOfProd);
					message = QueueHelper.createQueueMessage(_curOrgId, fresh.getAgcId(), fresh.getClickId(), 1,
							offerId, fresh.getLeadId(), "TRACKER", payout, EnumType.AGENTCY_TYPE.CPO.getValue(),
							fresh.getTrackerId(), fresh.getSubid4());
				}
				else if (fresh.getAgcId() == Const.AGENCY_ARB)
					message = QueueHelper.createQueueMessage(fresh.getOrgId(), fresh.getAgcId(), fresh.getClickId(),
							1, offerId, fresh.getLeadId(), String.format("%s;%s",fresh.getAffiliateId(),fresh.getSubid5()), payout, Integer.valueOf(fresh.getTerms()));
				else
					message = QueueHelper.createQueueMessage(_curOrgId, fresh.getAgcId(), fresh.getClickId(), 1,
							offerId, fresh.getLeadId(), payout, EnumType.AGENTCY_TYPE.CPO.getValue(), numOfProd);

				QueueHelper.sendMessage(message, Const.QUEUE_AGENTCY);
				logger.info("[QUEUE] " + Const.QUEUE_AGENTCY + " sent message " + message);
			}

			GetPickup pickupParam = new GetPickup();
			pickupParam.setOrgId(_curOrgId);
			pickupParam.setPickupId(pickupId != null? pickupId.toString() : "1");

			DBResponse<List<GetPickupResp>> dbPickup = clProductService.getPickup(SESSION_ID, pickupParam);
			// luon co pickupid = 1 nen ko can check null
			GetPickupResp pickupResp = dbPickup.getResult().get(0);
			// set pickup
			deliveryDto.setPickupId(pickupResp.getPickupCodeInpartner().toString());
			deliveryDto.setSoldToAccountId(pickupResp.getSoldId().toString());
			deliveryDto.setPickupName(pickupResp.getPickupName());

			insDoNew.setCarrierId(lastmileId);
			insDoNew.setWarehouseId(warehouseId);
			insDoNew.setCustomerName(fresh.getName());
			insDoNew.setCustomerPhone(fresh.getPhone());
			insDoNew.setCustomerAddress(fresh.getAddress());
			insDoNew.setCustomerId(leadId);
			logger.info("{} {} warehouseId out = {}", SESSION_ID, insDoNew.getSoId(), warehouseId);
			// mapping data

			deliveryDto.setDoId(insDoNew.getSoId());
			deliveryDto.setWarehouseId(warehouseId);
			deliveryDto.setLastmileId(lastmileId);
			deliveryDto.setOrgId(_curOrgId);

			// get sender address from warehouse id
			GetWareHouse getWareHouse = new GetWareHouse();
			getWareHouse.setWarehouseId(String.valueOf(warehouseId));
			DBResponse<List<GetWareHouseResp>> dbWarehouse = clProductService.getWareHouse(SESSION_ID, getWareHouse);
			logger.info("warehouseId out2 = {}", warehouseId);
			if(!dbWarehouse.getResult().isEmpty()){
				GetWareHouseResp wareHouseResp = dbWarehouse.getResult().get(0);
				deliveryDto.setsAddress(wareHouseResp.getAddress());
				logger.info("{} {} warehouseId out 3 = {}", SESSION_ID, insDoNew.getSoId(), wareHouseResp.getAddress());
				deliveryDto.setsDistrictName(wareHouseResp.getDistrictName());
				deliveryDto.setsProvinceName(wareHouseResp.getProvinceName());
				deliveryDto.setsDistrictId(wareHouseResp.getDistrictId());
				deliveryDto.setsDistrictCode(wareHouseResp.getDistrictCode());
				deliveryDto.setsProvinceCode(wareHouseResp.getProvinceCode());
				deliveryDto.setsWardName(wareHouseResp.getWardsName());
				deliveryDto.setsProvinceId(wareHouseResp.getProvinceId());
				deliveryDto.setGroupAddressId(wareHouseResp.getWhCodeInpartner());
				deliveryDto.setsPhone(wareHouseResp.getContactPhone());
				deliveryDto.setsWardCode(wareHouseResp.getWardsCode());
			}

			// get pickup
			logger.info("parterId ============ ============== " + parterId);
			/*
			 * switch (parterId) { case Const.SAP_FFM_PARTNER_ID: case Const.BM_PARTNER_ID:
			 * case Const.KERRY_FFM_PARTNER_ID: case Const.WFS_PARTNER_ID: threePl =
			 * lastmileId; break; default: threePl = parterId; break; }
			 */
			threePl = parterId == Const.BM_PARTNER_ID || parterId == Const.SAP_FFM_PARTNER_ID
					|| parterId == Const.KERRY_FFM_PARTNER_ID? lastmileId : parterId;

			if (parterId == Const.NTL_FFM_PARTNER_ID)
				threePl = 1;

			GetProvinceMapResp provinceMapResp = locationHelper.getProvinceMapper(provinceService, threePl,
					deliveryDto.getProvinceId(), SESSION_ID);
			logger.info("parterId ============ ============== " + parterId + " " + threePl + "  " + lastmileId);
			GetDistrictMapResp districtMapResp = locationHelper.getDistrictMapper(provinceService, threePl,
					deliveryDto.getDistrictId(), SESSION_ID);
			GetSubdistrictMapResp subdistrictMapResp = null;
			if(Helper.isIndonesia(_COUNTRY) || Helper.isThailand(_COUNTRY) || parterId == Const.WFS_PARTNER_ID || parterId == Const.NTL_FFM_PARTNER_ID){
				subdistrictMapResp = locationHelper.getSubDistrictMapper(provinceService, threePl,
						deliveryDto.getWardsId(), SESSION_ID);
				if (subdistrictMapResp != null)
					deliveryDto.setWardName(subdistrictMapResp.getName());
				if(parterId == Const.WFS_PARTNER_ID)
					try{
						deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
					} catch(Exception e){
						logger.error("Can not convert subdistrictMapResp.getWardsPartnerCode {}", subdistrictMapResp.getWardsPartnerCode(), e);
					}
			}

			try{
				if(provinceMapResp != null){
					if(threePl == Const.WFS_PARTNER_ID){
						int prvId = Integer.parseInt(provinceMapResp.getPrvPartnerCode());
						deliveryDto.setProvinceId(prvId);
					} else
						deliveryDto.setProvinceId(provinceMapResp.getPrvId());
					deliveryDto.setProvinceName(provinceMapResp.getPrvPartnerName());

					String province = threePl != Const.DHL_PARTNER_ID? provinceMapResp.getPrvPartnerCode() != null
							? provinceMapResp.getPrvPartnerCode().toString()
							: "" : provinceMapResp.getName();
					insDoNew.setCustomerProvince(province);
				}

				if(districtMapResp != null){
					if(threePl == Const.WFS_PARTNER_ID || threePl == Const.BM_INDO_PARTNER_ID){
						int dtId = Integer.parseInt(districtMapResp.getDtPartnerCode());
						deliveryDto.setGhnDistrictCode(districtMapResp.getDtPartnerCode());
						deliveryDto.setDistrictId(dtId);
					}
					else if(threePl == Const.DHL_FFM_PARTNER_ID){
						deliveryDto.setDistrictCode(districtMapResp.getDtPartnerCode());
					}
					else
						deliveryDto.setDistrictId(districtMapResp.getDistrictId());

					deliveryDto.setDistrictName(districtMapResp.getDtPartnerName());
					String district = threePl != Const.DHL_PARTNER_ID
							? districtMapResp.getDtPartnerCode() != null? districtMapResp.getDtPartnerCode().toString()
									: ""
							: districtMapResp.getDtPartnerName();
					logger.info("Check cau hinh ****************** {}-{}-{} ", threePl, lastmileId, parterId);

					insDoNew.setCustomerDistrict(district);
				}
			} catch(Exception e){
				prodMessage = "Not mapping province and district ";
				logger.error(e.getMessage(), e);
			}

			// --------------------- NEW
			if(lastmileId == Const.SHIP60_LM_PARTNER_VN){
				subdistrictMapResp = locationHelper.getSubDistrictMapper(provinceService, threePl,
						deliveryDto.getWardsId(), SESSION_ID);
				if (provinceMapResp != null){
					deliveryDto.setProvinceCode(provinceMapResp.getPrvPartnerCode());
					deliveryDto.setProvinceName(provinceMapResp.getName());
				}
				if (districtMapResp != null){
					deliveryDto.setDistrictCode(districtMapResp.getDtPartnerCode());
					deliveryDto.setDistrictName(districtMapResp.getName());
				}
				if (subdistrictMapResp !=null){
					deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
					deliveryDto.setWardName(subdistrictMapResp.getName());
				}
			}

			if(lastmileId == Const.DHL_PARTNER_ID && parterId == Const.WFS_PARTNER_ID){
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService, Const.WFS_PARTNER_ID,
						Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService, Const.WFS_PARTNER_ID,
						Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				if(provMapResp != null){
					int prvId = Integer.parseInt(provMapResp.getPrvPartnerCode());
					deliveryDto.setProvinceId(prvId);
				}
				if(dtMapResp != null){
					int dtId = Integer.parseInt(dtMapResp.getDtPartnerCode());
					deliveryDto.setDistrictId(dtId);
				}

			}

			if(lastmileId == Const.DHL_PARTNER_ID){
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService, Const.DHL_PARTNER_ID,
						Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService, Const.DHL_PARTNER_ID,
						Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				if(provMapResp != null)
					deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
				if(dtMapResp != null)
					deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());

			}
			if(lastmileId == Const.GHTK_LM_PARTNER_VN){
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
						Const.GHTK_LM_PARTNER_VN, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
						Const.GHTK_LM_PARTNER_VN, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
						Const.GHTK_LM_PARTNER_VN, deliveryDto.getWardsId(), SESSION_ID);
				if(provMapResp != null)
					deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
				if(dtMapResp != null)
					deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());
				if(sdtMapResp != null)
					deliveryDto.setWardName(sdtMapResp.getName());
			}
			if(lastmileId == Const.NinjaVan_PARTNER_ID){
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
						Const.NinjaVan_PARTNER_ID, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
						Const.NinjaVan_PARTNER_ID, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
						Const.NinjaVan_PARTNER_ID, deliveryDto.getWardsId(), SESSION_ID);
				if(provMapResp != null)
					deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
				if(dtMapResp != null)
					deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());
				if(sdtMapResp != null)
					deliveryDto.setWardName(sdtMapResp.getName());
			}

			if(lastmileId == Const.GHN_PARTNER_ID && parterId == Const.DHL_FFM_PARTNER_ID){
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
						Const.WFS_PARTNER_ID, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
						Const.WFS_PARTNER_ID, deliveryDto.getWardsId(), SESSION_ID);
				if(dtMapResp != null)
					deliveryDto.setGhnDistrictCode(dtMapResp.getDtPartnerCode());
				if(sdtMapResp != null)
					deliveryDto.setGhnWardCode(sdtMapResp.getWardsPartnerCode());
			}

			if(lastmileId == Const.GHN_PARTNER_ID){
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
						Const.WFS_PARTNER_ID, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
						Const.WFS_PARTNER_ID, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
						Const.WFS_PARTNER_ID, deliveryDto.getWardsId(), SESSION_ID);
				if(provMapResp != null)
					deliveryDto.setProvinceName(provinceMapResp.getPrvPartnerName());
				if(dtMapResp != null) {
					deliveryDto.setGhnDistrictCode(dtMapResp.getDtPartnerCode());
					deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());
				}
				if(sdtMapResp != null) {
					deliveryDto.setGhnWardCode(sdtMapResp.getWardsPartnerCode());
					deliveryDto.setWardName(sdtMapResp.getName());
				}
			}

			if(lastmileId == Const.SNAPPY_PARTNER_ID){
				deliveryDto.setWarehouseShortname(warehouseShortname);
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
						Const.SNAPPY_PARTNER_ID, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
						Const.SNAPPY_PARTNER_ID, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				if(provMapResp != null)
					deliveryDto.setLmProvinceId(Integer.parseInt(provMapResp.getPrvPartnerCode()));
				if(dtMapResp != null)
					deliveryDto.setLmDistrictId(Integer.parseInt(dtMapResp.getDtPartnerCode()));
			}
			if(lastmileId == Const.FLASH_TH_PARTNER_ID){
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
						lastmileId, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
						lastmileId, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
						lastmileId, Helper.IntergeTryParse(fresh.getSubdistrict()), SESSION_ID);
				if(provMapResp != null) {
					deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
				}
				if(dtMapResp != null) {
					deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());
				}
				if(sdtMapResp != null) {
					deliveryDto.setWardName(sdtMapResp.getWardsPartnerId());
					deliveryDto.setWardsCode(sdtMapResp.getWardsPartnerCode());
				}
			}

			if(subdistrictMapResp != null && !StringHelper.isNullOrEmpty(subdistrictMapResp.getDcsr()))
				deliveryDto.setLastmileService(subdistrictMapResp.getDcsr());

			// set sZipcode = subdistrict postcode by partner (lc_subdistrict_map.sdt_code)
			if(subdistrictMapResp != null)
				deliveryDto.setsZipCode(subdistrictMapResp.getWardsPartnerCode());

			// ---------------------
			if(Helper.isIndonesia(_COUNTRY) && subdistrictMapResp != null){// only Indonesia need to get subdistrict
				deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
				deliveryDto.setWardName(subdistrictMapResp.getName());

				insDoNew.setCustomerWards(subdistrictMapResp.getWardsPartnerCode());

				if(threePl == Const.BM_INDO_PARTNER_ID){

					deliveryDto.setProvinceCode(deliveryDto.getDistrictId().toString());
					deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
					if(!StringHelper.isNullOrEmpty(subdistrictMapResp.getDcsr())){
						String[] path = subdistrictMapResp.getDcsr().split(",");
						if(path.length == 5)
							deliveryDto.setrZipCode(path[4]);
					}
				} else if(parterId == Const.KERRY_FFM_PARTNER_ID){
					deliveryDto.setrZipCode(subdistrictMapResp.getWardsPartnerCode());
					GetNeighbordhoodResp nghResp = locationHelper.getNeighborhoodMapper(deliveryOrderService,
							Const.KERRY_LM_PARTNER_ID, neighborId, SESSION_ID);
					if(nghResp != null)
						deliveryDto.setrZipCode(nghResp.getCode());
				}

			}
//            return true;

			// update nghiep vu (lay so luong product)

			logger.info("---------------------- -----------------  {}", lst.size());
			if(lst.size() == 0){
				hasProdInFFToCreateDO = false;
				prodMessage = "Size of SO Item not equal size of product mapping, getProductListBySoId  SO id = "
						+ insDoNew.getSoId();
				insDoNew.setErrorMessage(prodMessage);
				insDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
			}

			String packageName = "";
			String formatPackage = "%s(%s)";
			for(int i = 0; i < lst.size(); i++)
				packageName += String.format(formatPackage, lst.get(i).getProName(), lst.get(i).getQty()) + ", ";
			insDoNew.setPackageName(packageName);
			deliveryDto.setPackageName(packageName);
			insDoNew.setPackageListItem(LogHelper.toJson(lst));
		} catch(Exception e){
			isContinue = false;
			logger.error(e.getMessage(), e);
			errMessage.append(e.getMessage());
			insDoNew.setErrorMessage(errMessage.toString());
			insDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
		}
		// INSERT TO donew
		DBResponse dbResponse = logService.insDoNew(SESSION_ID, insDoNew);
		String doNewId = dbResponse.getErrorMsg().trim();

		int doId = 0;
		try{
			doId = Integer.parseInt(doNewId);
			deliveryDto.setDoId(doId);
		} catch(Exception e){
			doId = 0;
			logger.debug(e.getMessage());
		}

		dbLog.writeDOStatusLog(_curUser.getUserId(), doId, EnumType.DO_STATUS_ID.NEW.getValue(), "Create DO");
		dbLog.writeAgentActivityLog(_curUser.getUserId(), "create delivery order", "delivery order", doId, "do_status",
				EnumType.DO_STATUS_ID.NEW.getValue() + "");

		logger.info("---------------------- -----------------  2 {}", hasProdInFFToCreateDO);
		if(!hasProdInFFToCreateDO)
			return true;

		String key = RedisHelper.createRedisKey(Const.REDIS_PRERIX_DO, _curOrgId, _curUser.getUserId());
		RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(doNewId), insDoNew.getSoId() + "|" + leadId);

		String globalValue = RedisHelper.getGlobalParamValue(stringRedisTemplate, _curOrgId, 4, 1);
		if(!isContinue)// co loi xay ra khi cau hinh, khong cho phep chay tiep
			return false;
		if(globalValue == null)
			return true;
		if(globalValue.equals("2"))// run create delivery manual
			return true;
		// #################### kiem tra san pham co con trong kho hay ko?
		// ######################
		logger.info("---------------------- -----------------  4 {}", hasProdInFFToCreateDO);
		if(warehouseId == null){
			prodMessage = "Not configure warehouse id for partner " + parterId;
			hasProdInFFToCreateDO = false;
		} else
			for(int j = 0; j < lst.size(); j++){
				Boolean hasProductInFF = DeliveryHelper.hasProductInFullfillment(parterId, lst.get(j),
						String.valueOf(warehouseId));
				if(!hasProductInFF){
					prodMessage = lst.get(j).getProName() + " has not enough quantity in Fullfillment " + parterId;
					logger.info(
							SESSION_ID + " " + insDoNew.getSoId() + "############### " + doNewId + "|" + prodMessage);
					hasProdInFFToCreateDO = false;
					break;
				}
			}
		if(!hasProdInFFToCreateDO){
			UpdDoNew updDoNew = new UpdDoNew();
			updDoNew.setDoId(doId);
			updDoNew.setOrgId(_curOrgId);
			updDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
			updDoNew.setErrorMessage(prodMessage);
			logService.updDoNew(SESSION_ID, updDoNew);

			return false;
		}
		logger.info("---------------------- -----------------  5 {}", hasProdInFFToCreateDO);
		GetProvinceMapResp provinceMapWHResp = null;
		if(deliveryDto.getProvinceId() != null)
			provinceMapWHResp = locationHelper.getProvinceMapper(provinceService, parterId, deliveryDto.getProvinceId(),
					SESSION_ID);
		GetDistrictMapResp districtMapWHResp = null;
		if(deliveryDto.getDistrictId() != null)
			districtMapWHResp = locationHelper.getDistrictMapper(provinceService, parterId, deliveryDto.getDistrictId(),
					SESSION_ID);
		if(parterId == Const.SAP_FFM_PARTNER_ID || parterId == Const.KERRY_FFM_PARTNER_ID || parterId == Const.HAISTAR_PARTNER_ID){

		} else if(provinceMapWHResp != null && provinceMapWHResp.getPrvPartnerCode() != null){
			int prvId = Integer.parseInt(provinceMapWHResp.getPrvPartnerCode());
			deliveryDto.setReicvWhProvinceId(prvId);
		}

		if(parterId == Const.SAP_FFM_PARTNER_ID || parterId == Const.KERRY_FFM_PARTNER_ID || parterId == Const.HAISTAR_PARTNER_ID){

		} else if(districtMapWHResp != null && districtMapWHResp.getDtPartnerCode() != null){
			int dtId = Integer.parseInt(districtMapWHResp.getDtPartnerCode());
			deliveryDto.setReicvWhDistrictId(dtId);
			if(districtMapWHResp.getPnDistrictId() != null){
				GetSubdistrict paramGetSubdistrict = new GetSubdistrict();
				paramGetSubdistrict.setSdtId(subDistId);
				DBResponse<List<Subdistrict>> lstSubdistrictRes = lcProvinceService.getSubDistrict(SESSION_ID,
						paramGetSubdistrict);
				Subdistrict subdistrict = lstSubdistrictRes.getResult().get(0);
				deliveryDto.setrZipCode(subdistrict.getCode());
//                deliveryDto.setrZipCode(districtMapWHResp.getPnDistrictId());
			}
		}

		deliveryDto.setPartnerId(parterId);
		String doCode = DeliveryHelper.createEKWDoCode(doId);
		deliveryDto.setDoCode(doCode);
		
		//Nếu là DHL FFM (đã có ffm code -> set ffm code -< gọi api update sale order DHL FFM)
		if(parterId == Const.DHL_FFM_PARTNER_ID && insDoNew.getFfmCode() != null){
			deliveryDto.setFfmCode(insDoNew.getFfmCode());
		}

		int totalAmount = 0;
		try{
			SaleOrder saleOrder = getSaleOrderDetailV2(insDoNew.getSoId());
			deliveryDto.setPaymentMethod(saleOrder.getPaymentMethod());
			if (appointmentDateResponse.isActive()) {
				if (com.tms.api.utils.ObjectUtils.allNotNull(saleOrder.getAppointmentDate())) {
					DeliveriesOrderRequest deliveriesOrderRequest = new DeliveriesOrderRequest();
					String remindTime = DateUtils.snagPatternStage(PatternEpochVariable.VIETNAMESE_BIBLIOGRAPHY_EPOCH_PATTERN, saleOrder.getAppointmentDate());
					deliveryDto.setNote("Appointment date: ".concat(remindTime).concat(", ").concat(deliveryDto.getNote()));
					deliveriesOrderRequest.setAppointmentDate(saleOrder.getAppointmentDate());
					deliveryOrderRelatedService.updateReminderCalls(doId, deliveriesOrderRequest);
				}
			}
			if(deliveryDto.getPhone().contains("|"))
				deliveryDto.setPhone(saleOrder.getLeadPhone());
			totalAmount = saleOrder.getAmount().intValue();
		} catch(Exception e){
			logger.info(SESSION_ID + " " + doNewId + " $$$$$$$$$ Could not get total amount  " + e.getMessage());
		}
		deliveryDto.setAmount(totalAmount);
		// TODO need to be calculate
		if(_COUNTRY.equals("ID"))
			totalAmount = Helper.RoundToHundred(Double.valueOf(totalAmount));
		if(deliveryDto.getPaymentMethod() != EnumType.PAYMENT_METHOD.BANKING.getValue())
			deliveryDto.setCod_money(totalAmount);
		else// la banking thi set = 0
			deliveryDto.setCod_money(0);

		logger.info("{} {}  deliveryDto: {}\r\n======= ========== createDelivery partnerId={}",
				SESSION_ID, doNewId, LogHelper.toJson(deliveryDto), parterId);

		List<OrderResult> lstOrder = DeliveryHelper.createDelivery(parterId, deliveryDto, lst);
		if(lstOrder == null)
			logger.info(SESSION_ID + "[CreateDONew] Could not config Partner ID " + parterId + " " + doNewId);

		// update result
		boolean isAllSuccess = true;
		if(lstOrder != null && lstOrder.size() > 0){
			logger.info("List Order: " + LogHelper.toJson(lstOrder));
			for(int i = 0; i < lstOrder.size(); i++){
				OrderResult tmpOrder = lstOrder.get(i);
				UpdDoNew updDoNew = new UpdDoNew();
				updDoNew.setDoId(doId);
				updDoNew.setDoCode(deliveryDto.getDoCode());
				updDoNew.setOrgId(_curOrgId);
				updDoNew.setCustomerPhone(deliveryDto.getPhone());
				updDoNew.setFfmId(parterId);
				// TODO need to be change this method
				updDoNew.setAmountcod(Double.valueOf(deliveryDto.getCod_money()));
				if(isAllSuccess){
					logger.info(tmpOrder.getType());
					switch(tmpOrder.getResult()){
					case Const.DO_SUCCESS:
						updDoNew.setErrorMessage(null);
						updDoNew.setStatus(EnumType.DO_STATUS_ID.NEW.getValue());
						/*
						 * if (parterId < 2 || parterId > 5)//kho GHN
						 * updDoNew.setFfmCode(tmpOrder.getMessage()); else//lastmile
						 * updDoNew.setTrackingCode(tmpOrder.getMessage());
						 */
						if(tmpOrder.getType().equals(Const.BM_PARTNER_CODE)
								|| tmpOrder.getType().equals(Const.SAPW_PARTNER_CODE)
								|| tmpOrder.getType().equals(Const.MYCLOUD_PARTNER_CODE)
							)
							updDoNew.setFfmCode(tmpOrder.getMessage());
						else if (tmpOrder.getType().equals(Const.BM_EXPRESS_CODE)) {
							updDoNew.setFfmCode(tmpOrder.getMessage());
							updDoNew.setTrackingCode(tmpOrder.getMessage());
						}
						else if(tmpOrder.getType().equals(Const.WFS_GHN_PARTNER_CODE)){
							if(tmpOrder.getCode().compareTo("0") == 0){

								String[] trackings = tmpOrder.getMessage().split("\\|");
								updDoNew.setFfmCode(trackings[0]);
								updDoNew.setTrackingCode(trackings[1]);
							} else{
								updDoNew.setErrorMessage(tmpOrder.getErrMessage());
								updDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
							}
						}
						else if(tmpOrder.getType().equals(String.valueOf(Const.DHL_FFM_PARTNER_ID))){
							if(tmpOrder.getMessage() != null && StringUtils.hasText(tmpOrder.getMessage())){
								updDoNew.setFfmCode(tmpOrder.getMessage());
							}
						} else if (tmpOrder.getType().equals(Const.KERRY_TH_PARTNER_CODE)) {
                            updDoNew.setFfmCode(tmpOrder.getMessage());
                            updDoNew.setTrackingCode(tmpOrder.getMessage());
                        } else if (tmpOrder.getType().equals(Const.FLASH_TH_PARTNER_CODE)) {
							String[] trackings = tmpOrder.getMessage().split("\\|");
							updDoNew.setFfmCode(trackings[0]);
							updDoNew.setTrackingCode(trackings[1]);
                        } else if (tmpOrder.getType().equals(String.valueOf(Const.HAISTAR_PARTNER_ID))) {
							updDoNew.setTrackingCode(tmpOrder.getMessage());
						} else if(tmpOrder.getType().equals(String.valueOf(Const.NTL_EXPRESS_PARTNER_CODE))){
							if(tmpOrder.getMessage() != null && StringUtils.hasText(tmpOrder.getMessage())){
								updDoNew.setFfmCode(tmpOrder.getMessage());
							}
						} else
							updDoNew.setTrackingCode(tmpOrder.getMessage());

						break;
					case Const.DO_ERROR:
						isAllSuccess = false;
						updDoNew.setErrorMessage(tmpOrder.getMessage() + "|" + tmpOrder.getErrMessage());
						updDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
						break;
					default:
						isAllSuccess = false;
						updDoNew.setErrorMessage(tmpOrder.getCode() + "|" + tmpOrder.getErrMessage());
						break;
					}
					logService.updDoNew(SESSION_ID, updDoNew);
				}
			}
			if(isAllSuccess)
				return true;
		}
		return false;
	}


	@PutMapping("/Validate")
	public TMSResponse<?> Validate(@Valid @RequestBody ValidateSODtoV2 validateData) throws TMSException {
		if(!Helper.hasExportPermission(_curUser)) {
			return TMSResponse.buildResponse("You do not have permission!");
		}

		AppointmentDateResponse appointmentDateResponse = appointmentDateService.init();

		if (appointmentDateResponse.isActive()) {
			if (!StringUtility.isEmpty(validateData.getAppointmentDate())) {
				if (!appointmentDateService.isValid(validateData.getAppointmentDate())) {
					return TMSResponse.buildResponse(null, 0, "Appointment date invalid.", 400);
				}
			}
		}

		List<SaleOrder> result = new ArrayList<>();
		int userId = getCurrentUser().getUserId();
		int orgId = getCurOrgId();
		// get list SO from DB
		List<Integer> lstWithoutDupSOId = validateData.getSoIds().stream().distinct()
				.collect(java.util.stream.Collectors.toList());
		boolean isAllowChangeStatus = true;
		for(Integer id: lstWithoutDupSOId){
			GetSOV3 params = new GetSOV3();
			params.setSoId(id);
			params.setOrgId(orgId);
			DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrderV3(SESSION_ID, params);
			if(dbResponseSaleOrder.getResult().isEmpty())
				throw new TMSException(ErrorMessage.NOT_FOUND);
			SaleOrder so = dbResponseSaleOrder.getResult().get(0);
			logger.info("Go to line+++++++++++++++++++++++++");
			if (EnumType.SALE_ORDER_STATUS.VALIDATED.getValue() == so.getStatus()
					|| EnumType.SALE_ORDER_STATUS.UNASSIGNED.getValue() == so.getStatus()) {// neu la don da validate
				logger.info("Go to line ======================");
				isAllowChangeStatus = false;
				// chi duoc chuyen sang Unassign
				if (EnumType.SALE_ORDER_STATUS.UNASSIGNED.getValue() == validateData.getStatus())
					isAllowChangeStatus = true;
			}
//			if (EnumType.SALE_ORDER_STATUS.DELAYVALIDATE.getValue() == so.getStatus()) {
//				isAllowChangeStatus = false;
//			}
			result.add(so);
		}

		if(!isAllowChangeStatus)
			return TMSResponse.buildResponse(false, 0, "do_not_allow_change_so", 400);
		boolean isSucces = false;
		DBResponse response = null;

		// neu ko la unassign thi update status
		if(validateData.getStatus() != EnumType.SALE_ORDER_STATUS.UNASSIGNED.getValue())
			for(SaleOrder saleOrder: result){
				logger.info("validation param: " + LogHelper.toJson(validateData));

				UpdSaleOrderV2 updSaleOrder = modelMapper.map(saleOrder, UpdSaleOrderV2.class);
				updSaleOrder.setStatus(validateData.getStatus());
				updSaleOrder.setModifyby(userId);
				response = logService.updSaleOrderV2(SESSION_ID, updSaleOrder);
				isSucces = true;
				dbLog.writeAgentActivityLog(_curUser.getUserId(), "update sale order", "sale order",
						saleOrder.getSoId(), "so_status", validateData.getStatus() + "");
				// TODO need to clean-code, because this call db in loop
				// neu la VALIDATED, tao moi DO
				if(validateData.getStatus() == EnumType.SALE_ORDER_STATUS.VALIDATED.getValue()){
					// neu lead la CPO thi chuyen POSTBACK toi Agentcy khi la VALIDATE SO success,
					// le ra se xu ly o day nhung tam thoi de xu ly trong create DO new (tiet kiem
					// call to FRESH
					OdSaleOrder soValidate = new OdSaleOrder();
					soValidate.setSoId(saleOrder.getSoId());
					soValidate.setValidated(true);
					soValidate.setValidateBy(userId);
					soValidate.setReason(null);
					soValidate.setModifyby(userId);
					soValidate.setModifydate(new Date());

					if (appointmentDateResponse.isActive()) {
						LeadsRequest leadsRequest = new LeadsRequest();
						soValidate.setAppointmentDate(DateUtils.parse(validateData.getAppointmentDate(), PatternEpochVariable.SHORT_BIBLIOGRAPHY_EPOCH_PATTERN));
						leadsRequest.setAppointmentDate(soValidate.getAppointmentDate());
						leadService.updateReminderCalls(saleOrder.getLeadId(), leadsRequest);
					}
					soService.updateSaleOrder(soValidate);
					// get all so item
					GetSoItemV2 getSoItem = new GetSoItemV2();
					getSoItem.setSoId(saleOrder.getSoId());
					DBResponse<List<GetSoItemResp>> responseSOItem = deliveryOrderService.getSaleOrderItemV2(SESSION_ID,
							getSoItem);
					if(responseSOItem == null || responseSOItem.getErrorCode() != Const.INS_DB_SUCCESS)
						throw new TMSException(ErrorMessage.NOT_FOUND);

					try{
						UpdLeadV5 upLead = new UpdLeadV5();
						upLead.setLeadId(saleOrder.getLeadId());
						logService.updLeadV5(SESSION_ID, upLead);
					} catch(Exception ex){
						logger.error("SO Validate Lead " + SESSION_ID + " " + ex.getMessage());
					}

					List<GetSoItemResp> lst = responseSOItem.getResult();

					String prodId = "";
					for(int j = 0; j < lst.size(); j++)
						prodId += lst.get(j).getProdId() + ",";

					// change from DELIVERY ORDER ==> DO NEW
					InsDoNew doNew = new InsDoNew();
					doNew.setOrgId(orgId);
					doNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
					doNew.setSoId(saleOrder.getSoId());
					doNew.setCreateby(userId);
					doNew.setCustomerId(saleOrder.getLeadId());

					isSucces = CreateDONew(doNew, saleOrder.getLeadId(), saleOrder.getOrgId());
//                    isSucces = true;
				} else if(validateData.getStatus() == EnumType.SALE_ORDER_STATUS.CANCEL.getValue()){
					// TODO check cac dieu kien lien quan den CANCEL
					if (validateData.getReason() == null){
						logger.info("Cancel missing reason!!" + LogHelper.toJson(validateData));
						return TMSResponse.buildResponse(true, 0, ErrorMessage.MISSING_REASON.getMessage(), 200);
					}
					if (saleOrder.getStatus() == EnumType.SALE_ORDER_STATUS.DELAYVALIDATE.getValue()) {
						OdSaleOrder soDelay = new OdSaleOrder();
						soDelay.setSoId(saleOrder.getSoId());
						soDelay.setStatus(EnumType.SALE_ORDER_STATUS.VALIDATED.getValue());
						soDelay.setCreationDate(new Date(validateData.getCreationDate()));
						soDelay.setModifyby(_curUser.getModifyby());
						soDelay.setModifydate(new Date());

						soService.updateSaleOrder(soDelay);

						InsDoNew doNew = new InsDoNew();
						doNew.setOrgId(saleOrder.getOrgId());
						doNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
						doNew.setSoId(saleOrder.getSoId());
						doNew.setCreateby(userId);
						doNew.setCustomerId(saleOrder.getLeadId());
						Boolean isSucess = soService.createDOCancel(doNew, saleOrder.getLeadId(), orgId, userId);

						logger.info("[Cancel delay order ] " + saleOrder.getSoId());

					} else {
						GetLeadParamsV8 clFreshParams = new GetLeadParamsV8();
						clFreshParams.setLeadId(saleOrder.getLeadId());
						clFreshParams.setOrgId(orgId);
						DBResponse<List<CLFresh>> dbfresh = clFreshService.getLeadV8(SESSION_ID, clFreshParams);
						if (dbfresh.getResult().isEmpty()) {
							logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
							throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
						}
						CLFresh fresh = dbfresh.getResult().get(0);
						int numOfProd = 1;
						String message = "";
						if (fresh.getTerms() != null && fresh.getAgcId() != null && fresh.getClickId() != null
								&& fresh.getOfferId() != null) {
							String price = "0";
							if (fresh.getPrice() != null) {
								price = fresh.getPrice();
							}
							if (null != fresh.getTrackerId() && 0 != fresh.getTrackerId()) {
								price = Integer.toString(numOfProd);
								message = QueueHelper.createQueueMessage(orgId, fresh.getAgcId(), fresh.getClickId(), -1,
										fresh.getOfferId(), fresh.getLeadId(), "cancel order", price, EnumType.AGENTCY_TYPE.CPO.getValue(), fresh.getTrackerId(),
										fresh.getSubid4());
							} else if (fresh.getAgcId() == Const.AGENCY_ARB)
								message = QueueHelper.createQueueMessage(fresh.getOrgId(), fresh.getAgcId(), fresh.getClickId(),
										-1, fresh.getOfferId(), fresh.getLeadId(), String.format("%s;%s",fresh.getAffiliateId(),fresh.getSubid5()), price, Integer.valueOf(fresh.getTerms()));
							else
								message = QueueHelper.createQueueMessage(orgId, fresh.getAgcId(), fresh.getClickId(), -1,
										fresh.getOfferId(), fresh.getLeadId(), price,
										EnumType.AGENTCY_TYPE.CPO.getValue(), numOfProd);

							QueueHelper.sendMessage(message, Const.QUEUE_AGENTCY);
						}
						logger.info("[QUEUE Cancel order ] " + Const.QUEUE_AGENTCY + " sent message " + message);
						/* cancel D.O as validate_by */
						OdSaleOrder soCancel = new OdSaleOrder();
						soCancel.setSoId(saleOrder.getSoId());
						soCancel.setValidateBy(userId);
						soCancel.setStatus(EnumType.SALE_ORDER_STATUS.CANCEL.getValue());
						soCancel.setReason(validateData.getReason());
						soCancel.setQaNote(validateData.getQaNote());
						soCancel.setModifyby(_curUser.getModifyby());
						soCancel.setModifydate(new Date());

						soService.updateSaleOrder(soCancel);
					}
				} else if(validateData.getStatus() == EnumType.SALE_ORDER_STATUS.PENDING.getValue()){
					if (validateData.getReason() == null){
						logger.info("Pending missing reason!! " + LogHelper.toJson(validateData));
						return TMSResponse.buildResponse(true, 0, ErrorMessage.MISSING_REASON.getMessage(), 200);
					}
					OdSaleOrder soPending = new OdSaleOrder();
					soPending.setSoId(saleOrder.getSoId());
					soPending.setValidateBy(userId);
					soPending.setStatus(EnumType.SALE_ORDER_STATUS.PENDING.getValue());
					soPending.setReason(validateData.getReason());
					soPending.setQaNote(validateData.getQaNote());
					soPending.setModifyby(_curUser.getModifyby());
					soPending.setModifydate(new Date());

					soService.updateSaleOrder(soPending);

				} else if (validateData.getStatus() == EnumType.SALE_ORDER_STATUS.DELAYVALIDATE.getValue()){
					logger.info("Delay valitate: " + LogHelper.toJson(validateData));
					if (ObjectUtils.isEmpty(validateData.getCreationDate())){
						logger.info((ErrorMessage.INVALID_PARAM.getMessage()));
						throw new TMSException(ErrorMessage.INVALID_PARAM);
					}

					/*
					UpdSaleOrderV4 updSaleOrderV4 = new UpdSaleOrderV4();
					updSaleOrderV4.setSoId(saleOrder.getSoId());
					updSaleOrderV4.setCreationDate(validateData.getCreationDate());
					updSaleOrderV4.setModifyby(_curUser.getUserId());
					updSaleOrderV4.setValidateBy(userId);
					logService.updSaleOrderV4(SESSION_ID, updSaleOrderV4);
					*/

					OdSaleOrder soValidate = new OdSaleOrder();
					soValidate.setSoId(saleOrder.getSoId());
					soValidate.setCreationDate(DateUtils.convertTime(validateData.getCreationDate()));
					soValidate.setModifyby(_curUser.getUserId());
					soValidate.setValidateBy(userId);

					if (appointmentDateResponse.isActive()) {
						LeadsRequest leadsRequest = new LeadsRequest();
						soValidate.setAppointmentDate(DateUtils.parse(validateData.getAppointmentDate(), PatternEpochVariable.SHORT_BIBLIOGRAPHY_EPOCH_PATTERN));
						leadsRequest.setAppointmentDate(soValidate.getAppointmentDate());
						leadService.updateReminderCalls(saleOrder.getLeadId(), leadsRequest);
					}
					soService.updateSaleOrder(soValidate);

					GetLeadParamsV8 clFreshParams = new GetLeadParamsV8();
					clFreshParams.setLeadId(saleOrder.getLeadId());
					clFreshParams.setOrgId(orgId);
					DBResponse<List<CLFresh>> dbfresh = clFreshService.getLeadV8(SESSION_ID, clFreshParams);
					if(dbfresh.getResult().isEmpty()){
						logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
						throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
					}
					CLFresh fresh = dbfresh.getResult().get(0);
					int numOfProd = 1;
					HashMap<Integer,Integer> mapPartner = new HashMap<>();
					mapPartner.put(4,8);
					mapPartner.put(9,39);
					mapPartner.put(10,55);
					List<ProductDto> lst =  this.getProductListBySoId(soValidate.getSoId(),orgId, mapPartner.get(orgId));
//					if(fresh.getAgcId() == Const.AGENCY_ADFLEX)
					if (lst.isEmpty())
						numOfProd = 1;
					else {
						numOfProd = 0;
						for (ProductDto productDto : lst)
							numOfProd += productDto.getQty();
					}
					String message = "";
					if(fresh.getTerms() != null && fresh.getAgcId() != null && fresh.getClickId() != null
							&& fresh.getOfferId() != null){
						String price = "0";
						if (!StringUtils.isEmpty(fresh.getPrice())){
							price = fresh.getPrice();
						}
						String note = "approve";

						if (null != fresh.getTrackerId() && 0 != fresh.getTrackerId()) {
							price = Integer.toString(numOfProd);
							message = QueueHelper.createQueueMessage(orgId, fresh.getAgcId(), fresh.getClickId(), 1,
									fresh.getOfferId(), fresh.getLeadId(), note, price, EnumType.AGENTCY_TYPE.CPO.getValue(), fresh.getTrackerId(),
									fresh.getSubid4());
						} else if (fresh.getAgcId() == Const.AGENCY_ARB)
							message = QueueHelper.createQueueMessage(fresh.getOrgId(), fresh.getAgcId(), fresh.getClickId(),
									1, fresh.getOfferId(), fresh.getLeadId(), String.format("%s;%s", fresh.getAffiliateId(), fresh.getSubid5()), fresh.getPrice(), Integer.valueOf(fresh.getTerms()));
						else
							message = QueueHelper.createQueueMessage(orgId, fresh.getAgcId(), fresh.getClickId(), 1,
									fresh.getOfferId(), fresh.getLeadId(), price,
									EnumType.AGENTCY_TYPE.CPO.getValue(), numOfProd);

						QueueHelper.sendMessage(message, Const.QUEUE_AGENTCY);
					}
					logger.info("[QUEUE delay order ] " + Const.QUEUE_AGENTCY + " sent message " + message);
				}
				dbLog.writeSOStatusLogV2(userId, saleOrder.getSoId(), validateData.getStatus(), "ValidateSO");
			}
		else
			// TODO thaida need to upgrade DB function to use less connection to DB and
			// modify redis store
			// List<GetSoItemResp> lstSoItemResp = new ArrayList<>();
			for(SaleOrder saleOrder: result){
				// 1. change SO old to Unassigned
				dbLog.writeSOStatusLog(userId, saleOrder.getSoId(), EnumType.SALE_ORDER_STATUS.UNASSIGNED.getValue(),
						"ValidateSO change to Unassigned");

				UpdSaleOrderV2 updSaleOrder = modelMapper.map(saleOrder, UpdSaleOrderV2.class);
				updSaleOrder.setStatus(EnumType.SALE_ORDER_STATUS.UNASSIGNED.getValue());
				updSaleOrder.setModifyby(userId);
				response = logService.updSaleOrderV2(SESSION_ID, updSaleOrder);
				// 2. create SO: clone old SO
				// 2.1 get all so item
				GetSoItemV2 getSoItem = new GetSoItemV2();
				getSoItem.setSoId(saleOrder.getSoId());
				DBResponse<List<GetSoItemResp>> responseSOItem = deliveryOrderService.getSaleOrderItemV2(SESSION_ID,
						getSoItem);
				if(responseSOItem == null || responseSOItem.getErrorCode() != Const.INS_DB_SUCCESS)
					throw new TMSException(ErrorMessage.NOT_FOUND);

				// update lai status DO thanh cancel
				GetDoNewV12 doNew = new GetDoNewV12();
				doNew.setSoId(saleOrder.getSoId());
				DBResponse<List<DeliveriesOrderResponseDTOV1>> doNewResps = deliveryOrderService.getDoNewV12(SESSION_ID,
						doNew);
				if (doNewResps.getErrorCode() == 0 && !doNewResps.getResult().isEmpty()) {
					DeliveriesOrderResponseDTOV1 currentDo = doNewResps.getResult().get(0);
					Integer doId = currentDo.getDoId();
					UpdDoNewV8 updDoNew = new UpdDoNewV8();
					updDoNew.setDoId(doId);
					updDoNew.setStatus(EnumType.SALE_ORDER_STATUS.CANCEL.getValue());
					logService.updDoNewV8(SESSION_ID, updDoNew);
				}

				// 2.2: insert SO
				InsSaleOrderV2 insSaleOrder = modelMapper.map(saleOrder, InsSaleOrderV2.class);
				insSaleOrder.setSoId(null);
				insSaleOrder.setStatus(EnumType.SALE_ORDER_STATUS.NEW.getValue());
				insSaleOrder.setCreateby(userId);
				insSaleOrder.setModifyby(userId);
				DBResponse dbResponseSO = logService.insSaleOrderV2(SESSION_ID, insSaleOrder);
				if(dbResponseSO == null || dbResponseSO.getErrorCode() != Const.INS_DB_SUCCESS)
					throw new TMSException("Insert sale order failed - when clone SO after validate");
				int so_id = Integer.parseInt(dbResponseSO.getErrorMsg().trim());
				dbLog.writeAgentActivityLog(userId, "create sale order", "sale order", so_id, "so_status",
						EnumType.SALE_ORDER_STATUS.NEW.getValue() + "");
				dbLog.writeSOStatusLog(userId, so_id, EnumType.SALE_ORDER_STATUS.NEW.getValue(),
						"ValidateSO - insert SO");
				// 2.3: insert SO Item
				List<GetSoItemResp> lst = responseSOItem.getResult();
				int itemNo = 1;
				for(GetSoItemResp getSoItemResp: lst){
					logger.info("####### ############|||||||||||||||||||{}", so_id);
					modelMapper.getConfiguration().setAmbiguityIgnored(true);
					InsSaleOrderItemV2 insSaleOrderItem = modelMapper.map(getSoItemResp, InsSaleOrderItemV2.class);
					insSaleOrderItem.setModifyby(userId);
					insSaleOrderItem.setCreateby(userId);
					insSaleOrderItem.setSoId(so_id);
					insSaleOrderItem.setItemNo(itemNo++);
					insSaleOrderItem.setAmount(getSoItemResp.getProdAmount());
//                    insSaleOrderItem.setAmount()
					logService.insSaleItemOrderV2(SESSION_ID, insSaleOrderItem);
				}
				// update lai date modify of lead
				UpdLeadV4 updLeadV4 = new UpdLeadV4();
				updLeadV4.setLeadId(saleOrder.getLeadId());
				updLeadV4.setOrgId(orgId);
				logService.updLeadV4(SESSION_ID, updLeadV4);

				// neu la Unassign thi tim SO trong DO new va set DO_new thanh in active
				UpdDoNewV3 updDoNewV3 = new UpdDoNewV3();
				updDoNewV3.setConditionSoId(saleOrder.getSoId());
//                updDoNewV3.setOrgId(_curOrgId);
				updDoNewV3.setIsActive(0);
				logService.updDoNewV3(SESSION_ID, updDoNewV3);

				isSucces = true;
			}

		return TMSResponse.buildResponse(isSucces, 0,
				isSucces? "Sucesss" : "Success create DO but shipment not created!!", isSucces? 200 : 400);
	}

	@PutMapping("/resend")
	public TMSResponse Validate(@RequestBody @Valid ResendDODto resendDODto) throws TMSException {
//        List<SaleOrder> result = new ArrayList<>();
		int userId = getCurrentUser().getUserId();
		// get list SO from DB
		List<Integer> lstWithoutDupDOId = resendDODto.getDoIds().stream().distinct()
				.collect(java.util.stream.Collectors.toList());
		boolean isSucces = true;
		StringBuilder mesage = new StringBuilder();
		int orgId = getCurOrgId();
		String formatMessage = "%s : %s : %s <br />";
		for(Integer id: lstWithoutDupDOId){
			GetDoNew getDoNew = new GetDoNew();
			getDoNew.setDoId(id);
			getDoNew.setOrgId(orgId);
			DBResponse<List<GetDoNewResp>> dbDoNew = deliveryOrderService.getDoNew(SESSION_ID, getDoNew);
			if(dbDoNew.getResult().isEmpty())
				throw new TMSException(ErrorMessage.DELIVERY_ORDER_NOT_FOUND);
			GetDoNewResp doNew = dbDoNew.getResult().get(0);
//            int carrierId = doNew.getCarrierId();
			int soId = doNew.getSoId();
			int leadId = doNew.getCustomerId();
			int doUserId = doNew.getCreateby();
			// delete old DO
			logService.delDoNew(SESSION_ID, String.valueOf(id));

			InsDoNew insDoNew = new InsDoNew();
			insDoNew.setOrgId(orgId);
			insDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
			insDoNew.setSoId(soId);
			insDoNew.setCreateby(doUserId);
			insDoNew.setUpdateby(_curUser.getUserId());
			insDoNew.setCustomerId(leadId);

			
			if(resendDODto.getIsChangeWareHouse() != null && resendDODto.getIsChangeWareHouse()) {
				isSucces = soService.CreateDONewWhenChangeWarehouse(SESSION_ID,getCurrentOriganationId(),_curUser.getUserId(),insDoNew, leadId, orgId,resendDODto.getFfmPartnerId(),resendDODto.getLmPartnerId(),resendDODto.getWarehouseId());
			}else {
				isSucces = CreateDONew(insDoNew, leadId, orgId);
			}
			
			mesage.append(String.format(formatMessage, soId, leadId, isSucces));
		}
		return TMSResponse.buildResponse(mesage.toString());
	}

	@GetMapping("/getSimilarOrder/{productId}")
	public TMSResponse getSimilarOrder(@RequestParam(value = "offset", required = false) Integer offset,
			@RequestParam(value = "limit", required = false) Integer limit, @PathVariable Integer productId)
			throws TMSException {
		GetSOByProductParams params = new GetSOByProductParams();
		params.setProductId(productId);
		params.setLimit(limit);
		params.setOffset(offset);
		DBResponse<List<SaleOrder>> response = deliveryOrderService.getSaleOrderByProduct(SESSION_ID, params);
		return TMSResponse.buildResponse(response.getResult(), response.getRowCount());
	}

	@GetMapping("/getRecent/{phoneNumber}")
	public TMSResponse getRecentOrder(@PathVariable String phoneNumber) throws TMSException {
		GetSOParams params = new GetSOParams();
		params.setLeadPhone(phoneNumber);
		DBResponse<List<SaleOrder>> response = deliveryOrderService.getSaleOrder(SESSION_ID, params);
		List<SaleOrder> result = response.getResult();
		Collections.sort(result);
		return TMSResponse.buildResponse(result, result.size());
	}

	@GetMapping("/Search")
	public TMSResponse search(GetSOParams params) throws TMSException {
		DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrder(SESSION_ID, params);
		if(dbResponseSaleOrder.getResult().isEmpty())
			throw new TMSException(ErrorMessage.NOT_FOUND);
		return TMSResponse.buildResponse(dbResponseSaleOrder.getResult());
	}

	@PutMapping("/cancelSO/{soId}")
	public TMSResponse cancelSO(@PathVariable Integer soId) throws TMSException {
		GetDoNew params = new GetDoNew();
		params.setOrgId(getCurOrgId());
		params.setSoId(soId);
		DBResponse<List<GetDoNewResp>> dbDoResp = deliveryOrderService.getDoNew(SESSION_ID, params);
		boolean isCancelSo = true;
		if(!dbDoResp.getResult().isEmpty()){
			// cancel DO
			GetDoNewResp doNew = dbDoResp.getResult().get(0);
			int carrierId = doNew.getCarrierId();
			int doId = doNew.getDoId();
			OrderResult orderResult = DeliveryHelper.cancelDelivery(carrierId, doNew, getCurOrgId());
			// need to change in SoControler, function cancelSO(@PathVariable Integer soId)
			if(orderResult != null && DeliveryHelper.isSuccessCode(orderResult)){
				UpdDoNew updDoNew = new UpdDoNew();
				updDoNew.setDoId(doId);
				// updDoNew.setOrgId(_curOrgId);
				updDoNew.setStatus(EnumType.DO_STATUS_ID.CANCEL.getValue());
				logService.updDoNew(SESSION_ID, updDoNew);
				// TODO write log DO

				// Cancel success update redis, remove order doid

				// return TMSResponse.buildResponse(orderResult.getCode(), 1,
				// orderResult.getMessage(), 200);
			} else
				isCancelSo = false;

		}

		if(isCancelSo){
			dbLog.writeSOStatusLog(_curUser.getUserId(), soId, EnumType.SALE_ORDER_STATUS.CANCEL.getValue(),
					"cancelSO");

			UpdSaleOrder updSaleOrder = new UpdSaleOrder();
			updSaleOrder.setStatus(EnumType.SALE_ORDER_STATUS.CANCEL.getValue());
			updSaleOrder.setSoId(soId);
			DBResponse response = logService.updSaleOrder(SESSION_ID, updSaleOrder);
			if(response == null)
				throw new TMSException(ErrorMessage.NOT_FOUND);

			dbLog.writeAgentActivityLog(_curUser.getUserId(), "update sale order", "sale order", soId, "so_status",
					EnumType.SALE_ORDER_STATUS.CANCEL.getValue() + "");

			// TODO redis need to remove SO:id
			// return TMSResponse.buildResponse(response.getResult());
			return TMSResponse.buildResponse(true, 1, "cancel_so_success", 200);
		}

		return TMSResponse.buildResponse(false, 0, "cancel_so_fail", 400);
	}

	private void cancelSOBySoId(Integer soId) throws TMSException {

	}

	@GetMapping("/SaleOrderItems/{soId}")
	public TMSResponse getSOItems(@PathVariable Integer soId) throws TMSException {
		GetSoItemV2 params = new GetSoItemV2();
		params.setSoId(soId);
		DBResponse<List<GetSoItemResp>> dbResponseSaleOrderItems = deliveryOrderService.getSaleOrderItemV2(SESSION_ID,
				params);
		if(dbResponseSaleOrderItems.getResult().isEmpty())
			throw new TMSException(ErrorMessage.NOT_FOUND);
		return TMSResponse.buildResponse(dbResponseSaleOrderItems.getResult());
	}

	@GetMapping("/queueProcess")
	public TMSResponse queueProcessing(@RequestParam String message) throws TMSException {
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader("D:\\Work\\Source Project\\reject.txt"));
			String line = reader.readLine();
			while(line != null){
				logger.info(line);
				QueueHelper.sendMessage(line.trim(), Const.QUEUE_AGENTCY);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch(IOException e){
			logger.error(e.getMessage(), e);
		}
		return TMSResponse.buildResponse(true);
	}

	@GetMapping("/test")
	public TMSResponse testSO(@RequestParam String message) throws TMSException {

		return TMSResponse.buildResponse(true);
	}

	@GetMapping("/CommissionData/export")
	public ResponseEntity<?> exportCommissionData(@ModelAttribute("params") GetCommissionData params) {
		params.setOrgId(_curUser.getOrgId());
		if(params.getCreatedate() == null || !StringUtils.hasText(params.getCreatedate())){
			String createDate = soService.getDefaultCreateDateInCommisionDataOrCollectionData();
			if(createDate != null)
				params.setCreatedate(createDate);
		}
		byte[] csvAsByte = soService.exportCommissionData(params, SESSION_ID);
		return TMSResponse.buildExcelFileResponse(csvAsByte, "CommissionData.xlsx");
	}

	@GetMapping("/CollectionData/export")
	public ResponseEntity<?> exportCollectionData(@ModelAttribute("params") GetCollectionData params)
			throws TMSException {
		params.setOrgId(_curUser.getOrgId());
		if(params.getCreatedate() == null || !StringUtils.hasText(params.getCreatedate())){
			String createDate = soService.getDefaultCreateDateInCommisionDataOrCollectionData();
			if(createDate != null)
				params.setCreatedate(createDate);
		}
		byte[] csvAsByte = soService.exportCollectionData(params, SESSION_ID);
		return TMSResponse.buildExcelFileResponse(csvAsByte, "CollectionData.xlsx");
	}
	
	@GetMapping("/mappingFFMLastsmile")
	public TMSResponse mappingFFMLastsmile() throws TMSException {
		 

		return soService.mappingFFMLastsmile();
	}

	@GetMapping("/{soId}/history")
	public TMSResponse getLogSO(@PathVariable Integer soId,
								@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
								@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {
		List<GetLogSoResp> result = soService.getLogSO(soId, limit, offset);
		return TMSResponse.buildResponse(result, result.size(),"Success", 200);

	}

}
