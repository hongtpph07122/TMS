package com.tms.api.task;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.tms.api.dto.Request.ScaleoCreateLeadReq;
import com.tms.api.dto.Response.ScaleoCreateLeadRes;
import com.tms.api.entity.TrkAffiliateMapping;
import com.tms.api.entity.TrkData;
import com.tms.api.helper.*;
import com.tms.api.service.TrkAffilateMappingService;
import com.tms.api.service.TrkDataService;
import com.tms.api.service.TrkOfferMappingService;
import com.tms.api.utils.ObjectUtils;
import com.tms.api.utils.StringUtility;
import com.tms.entity.log.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tms.api.helper.Const;
import com.tms.api.helper.Helper;
import com.tms.api.service.BasketProcessorService;
import com.tms.dto.DBResponse;
import com.tms.dto.GetBasketLeadV3;
import com.tms.dto.GetBasketLeadV4;
import com.tms.dto.GetBlacklist;
import com.tms.dto.GetCpCallingList;
import com.tms.dto.GetCpCallingListResp;
import com.tms.dto.GetDnc;
import com.tms.dto.GetFreshLeadParams;
import com.tms.entity.CFBlacklist;
import com.tms.entity.CFDnc;
import com.tms.entity.CLBasket;
import com.tms.entity.CLFresh;
import com.tms.service.impl.CLActiveService;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.CLInActiveService;
import com.tms.service.impl.LCProvinceService;
import com.tms.service.impl.LogService;

@Component
public class BasketProcessor {
	private static final Logger logger = LoggerFactory.getLogger(BasketProcessor.class);

	@Autowired
	LCProvinceService basketService;

	@Autowired
	CLFreshService freshService;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Autowired
	LCProvinceService dncService;

	@Autowired
	LCProvinceService blackListService;

	@Autowired
	CLActiveService activeService;

	@Autowired
	CLInActiveService inactiveService;

	@Autowired
	LogService logService;

	@Autowired
	BasketProcessorService basketProcessorService;

	@Autowired
	TrkDataService trkDataService;

	@Autowired
	TrkOfferMappingService trkOfferMappingService;

	@Autowired
	TrkAffilateMappingService trkAffilateMappingService;

	@Value("${config.run-basket}")
	public Boolean isRunBasket;

	@Value("${config.check-valid-phone-number}")
	public Boolean _CHECK_VALID_PHONE_NUMBER;

	@Value("${config.agency-id-hold}")
	public String agencyIdNeedToHoldPostback;
	@Value("${config.country}")
	public String _COUNTRY;
	@Value("${config.smart-load-sharing-mode}")
	public String _SMART_LOAD_SHARING_MODE;
	public String _SMART_LOAD_SHARING_MODE_LOAD_BALANCING = "load_balancing";
	@Autowired
	DBLogWriter dbLog;

	@Autowired
	ModelMapper modelMapper;

	private static Map<Integer, Integer> MAX_LIFE_TIME = new HashMap<>();// key: org, value: time maximum life (for
																			// duplicate)
	private static Map<Integer, Integer> TRASH_TIME = new HashMap<>();// key: org, value: time sent trash (when
																		// duplicate)
	private static Map<Integer, Integer> DUP_TIME = new HashMap<>();//
	private static Map<String, Integer> CAMPAIGN_CALLING_MAP = new HashMap<>();// key: orgid:callinglist_id => campaign
																				// id

	private static boolean _isLockBasket = false;
	private static final String BASKET_INVALID = "BASKET-INVALID";
	private static final String BASKET = "BASKET";
	private static final String DUPLICATE = "DUPLICATE";
	private static final Integer TRACKER_SCALEO = 102;

	private void sentMessageToQueue(CLBasket basket, String note, Boolean ignoreOfferType) {
		// TODO neu basket la CPL hoac la AGENCY CLICK DEAL thi goi lai postback
		try{
			boolean isTracker = checkIsTracker(basket);

			if(agencyIdNeedToHoldPostback.indexOf(";" + basket.getAgcId() + ";") >= 0 || ignoreOfferType
					|| basket.getAgcId() != null && basket.getClickId() != null
							&& String.valueOf(EnumType.AGENTCY_TYPE.CPL.getValue()).equals(basket.getTerms())
					|| isTracker){
				String message = basket.getAgcId() + "|" + basket.getClickId();
				boolean isSend = true;
				String payout = "0", offerId = "0";
				boolean isInvalid = false;
				if(basket.getPrice() != null)
					payout = basket.getPrice();
				if(basket.getOfferId() != null)
					offerId = basket.getOfferId();
				if(basket.getStatus() == EnumType.LEAD_STATUS.INVALID.getValue()
						|| basket.getStatus() == EnumType.LEAD_STATUS.DUPLICATED.getValue())
					isInvalid = true;
				String _command = BASKET;
				if(basket.getStatus() == EnumType.LEAD_STATUS.DUPLICATED.getValue())
					_command = DUPLICATE;

				if(basket.getAgcId() == Const.AGENCY_ARB)
					_command = String.format("%s;%s;%s",basket.getAffiliateId(),basket.getSubid5(),_command);
				int terms = 0;
				try{
					if(basket.getTerms() != null && !basket.getTerms().isEmpty())
						terms = Integer.parseInt(basket.getTerms());
				} catch(Exception e){
					logger.error(e.getMessage(), e);
				}
				if(null != note && note.equals(Const.TRASH_LEAD_MKT))
					isInvalid = true;
				if(null != basket.getTrackerId() && 0 != basket.getTrackerId())
					message = QueueHelper.createQueueMessage(basket.getOrgId(), basket.getAgcId(), basket.getClickId(),
							isInvalid? 2 : 0, offerId, basket.getLeadId(), _command, payout, terms,
							basket.getTrackerId(), basket.getSubid4());
				else
					message = QueueHelper.createQueueMessage(basket.getOrgId(), basket.getAgcId(), basket.getClickId(),
							isInvalid? 2 : 0, offerId, basket.getLeadId(), _command, payout, terms);
				if(basket.getAgcId() == Const.AGENCY_MKT){
					isSend = false;
					switch(note){
					case Const.TRASH_LEAD_MKT:
					case Const.HOLD_LEAD_MKT:
						isSend = true;
						break;
					}
				}
				if(isSend)
					QueueHelper.sendMessage(message, Const.QUEUE_AGENTCY);
				logger.info("[QUEUE] " + Const.QUEUE_AGENTCY + " sent message " + message);
			}
		} catch(Exception e){
			logger.error("[QUEUE] could not sent {}", basket.getLeadId(), e);
		}
	}

	private boolean checkIsTracker(CLBasket basket) {
		if(basket.getTrackerId() == null || basket.getTrackerId().intValue() == 0)
			return false;
		else
			return true;
	}

	private void sentMessageToQueue(CLBasket basket, String note) {
		sentMessageToQueue(basket, note, false);
	}

	 @Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 5000)
	@Scheduled(fixedDelayString = "${config.basket-time}")
	public void processBasketData() {
		try{
			logger.info("processBasketData --------------- {}", new Date());
			if(!isRunBasket)
				return;

			if(_isLockBasket)
				return;
			String taskSession = UUID.randomUUID().toString();
			logger.info("Starting to process basket data with session: {}", taskSession);
			GetBasketLeadV3 getBasketLeadParams = new GetBasketLeadV3();
			getBasketLeadParams.setStatus(1);
			// mac dinh khi chen vao basket thi attribute3 la 1, khi do sang lead thi se
			// update lai = 0
			getBasketLeadParams.setAttribute3("1");

			DBResponse<List<CLBasket>> dbBaskets = basketService.getBasketLeadV3(taskSession, getBasketLeadParams);
			List<CLBasket> baskets = dbBaskets.getResult();
			if (baskets == null || baskets.isEmpty()) {
				logger.info("Don't have lead in basket to run!");
				return;
			}

			baskets.sort(Comparator.comparingInt(CLBasket::getLeadId));

			DBResponse<List<CFBlacklist>> dbBlackList = blackListService.getBlacklist(taskSession, new GetBlacklist());
			Map<String, CFBlacklist> blackListMap = new HashMap<>();
			for(CFBlacklist blockedFresh: dbBlackList.getResult()){
				String hashKey = blockedFresh.getPhone();
				blackListMap.put(hashKey, blockedFresh);
			}

			DBResponse<List<CFDnc>> dbDncList = dncService.getDnc(taskSession, new GetDnc());
			Map<String, CFDnc> dncListMap = new HashMap<>();
			for(CFDnc dncFresh: dbDncList.getResult()){
				String hashKey = dncFresh.getPhone();
				dncListMap.put(hashKey, dncFresh);
			}

			Map<String, CLBasket> freshBasketMap = new HashMap<>();
			for(CLBasket basket: baskets){
				// check neu so dt khong hop le thi cho vao inactive luon
				if(basket.getOrgId() == 6){ // Nori = 6 yeu cau xoa bo cac ky tu dac biet chi de lai cac so can thiet
					String phone = Helper.formatPhoneNumber(basket.getPhone());
					basket.setPhone(phone);
				}

				String note = "BASKET";
				/*
				 * if(basket.getOrgId() == 4 || basket.getOrgId() == 9){ }
				 */
				// if (!Helper.isPhoneValid(_COUNTRY, (basket.getPhone().split("\\|")))) {
//                if (basket.getOrgId() == Const.COUNTRY_TL){
//                    isValidBasketTL(basket);
//                }

				if("LEAD_ERROR".equalsIgnoreCase(basket.getAttribute1())){
					logger.info("[INVALID-LEAD-ERROR] " + basket.getPhone() + " " + taskSession);
					basket.setStatus(EnumType.LEAD_STATUS.INVALID.getValue());
					// insert invalid
					InsCLInActiveV3 insClActive = modelMapper.map(basket, InsCLInActiveV3.class);
					logService.insCLInActiveV3(taskSession, insClActive);
					updBasketToInactive(taskSession, basket);
					continue;
				} else
					sentMessageToQueue(basket, note);
				if(blackListMap.containsKey(basket.getPhone()))
					// TODO: update basket status here
					updBasketToInactive(taskSession, basket);
				else if(dncListMap.containsKey(basket.getPhone()))
					// TODO: update basket status here
					updBasketToInactive(taskSession, basket);
				else{
					// check duplicate
					/**/
					int mode = 2;// default mode ekiwi

					String hashKey = "";
					try{
						String configMode = RedisHelper.getGlobalParamValue(stringRedisTemplate, basket.getOrgId(), 6,
								12);// config basket mode
						if(configMode != null)
							mode = Integer.parseInt(configMode);
					} catch(Exception e){
						logger.error(e.getMessage(), e);
					}

					CLBasket tmp = null;
					String hash = "0";

					switch(mode){
					case 1:// nori

						if(!MAX_LIFE_TIME.containsKey(basket.getOrgId()))
							readConfig(basket.getOrgId());
						break;
					case 2:// kiwi
							// String hashKey = basket.getPhone() + basket.getProdId();
						if(basket.getAgcId() != null)
							hashKey += basket.getAgcId();
						else
							hashKey += "MANUAL";
						if(!MAX_LIFE_TIME.containsKey(basket.getOrgId()))
							readConfig(basket.getOrgId());
						break;
					}

					if (StringUtils.isEmpty(basket.getPhone())){
						basket.setComment("PHONE NUMBER IS NULL!");
						basket.setStatus(EnumType.LEAD_STATUS.INVALID.getValue());
						updBasketToInactive(taskSession, basket);
						InsCLInActiveV3 insClActive = modelMapper.map(basket, InsCLInActiveV3.class);
						logService.insCLInActiveV3(taskSession, insClActive);
						sentMessageToQueue(basket, basket.getComment(), true);
					}


					if (!StringUtils.isEmpty(basket.getPhone()) && !StringUtils.isEmpty(basket.getName()) && !StringUtils.isEmpty(basket.getProdName())) {
						hash = String.valueOf(
								StringHelper.createHashkeyFromString(hashKey + "-" + StringHelper.createHashkey(basket)));
						tmp = createFreshCheckDupRedis(basket, taskSession, hash);

						if (basket.getAgcId() != null && basket.getAgcId() == Const.AGENCY_MKT
								&& tmp.getStatus() != EnumType.LEAD_STATUS.DUPLICATED.getValue())
							tmp = createFreshMKTCheckDupRedis(basket, taskSession);
						if (tmp != null)
							if (!freshBasketMap.containsKey(hash))// ko chua key
								freshBasketMap.put(hash, tmp);
							else {// co chua roi sent as duplicate
								basket.setStatus(EnumType.LEAD_STATUS.DUPLICATED.getValue());
								if (basket.getAgcId() != 102) {
									sentMessageToQueue(basket, basket.getComment(), true);
								}
							}
					}

				}
			}

			for (Map.Entry<String, CLBasket> basketEntry : freshBasketMap.entrySet()) {
				CLBasket basket = basketEntry.getValue();
				// TODO: update basket status here
				updBasketToInactive(taskSession, basket);
				logger.info("___________ {} ------  {}", basket.getLeadId(), basket.getStatus());

				Integer callingId = -1;
				try{
					if(_SMART_LOAD_SHARING_MODE.equalsIgnoreCase(_SMART_LOAD_SHARING_MODE_LOAD_BALANCING))
						callingId = basketProcessorService.getSmartLoadCallingList(basket);
					else
						callingId = basketProcessorService.getDefaultCallingList(basket.getAttribute2());
				} catch(Exception e){
					callingId = 0;
					logger.error(e.getMessage(), e);
				}

				String tmpKey = basket.getOrgId() + ":" + callingId;
				int campaignId = getCampaignFromCallingList(tmpKey, taskSession);

				if (basket.getStatus() == EnumType.LEAD_STATUS.INVALID.getValue()) {
					sentMessageToQueue(basket, basket.getComment(), true);
					logger.info("___________ TRASHED ___________");
					InsCLInActiveV3 insClActive = modelMapper.map(basket, InsCLInActiveV3.class);
					insClActive.setAttribute("" + callingId);
					insClActive.setCallinglistId(callingId);
					insClActive.setCpId(campaignId);
					logService.insCLInActiveV3(taskSession, insClActive);
					continue;
				} else if (basket.getStatus() == EnumType.LEAD_STATUS.INVALID.getValue() && !basket.getComment().equals("Incorrect phone number")) {
					sentMessageToQueue(basket, basket.getComment(), true);
				} else if (basket.getStatus() == EnumType.LEAD_STATUS.DUPLICATED.getValue()) {
					if (basket.getAgcId() != 102) {
						sentMessageToQueue(basket, basket.getComment(), true);
					} else {
						if (basket.getSubid3() != null && basket.getSubid3().equals("trackingLink")) {
							logger.info("send postback insert lead duplicate: " + basket.getLeadId());
							sentMessageToQueue(basket, basket.getComment(), true);
						}
					}
				}
				// modify phone number to valid phone
				String phoneNumber = StringUtility.filterPhoneNumber(basket.getPhone());
				boolean isValidPhoneNumber = checkValidPhoneNumber(basket.getOrgId(), phoneNumber);

				if(_CHECK_VALID_PHONE_NUMBER){
					if(isValidPhoneNumber)
						basket.setPhone(phoneNumber);
					else{
						basket.setComment("INCORRECT PHONE NUMBER!");
						basket.setStatus(EnumType.LEAD_STATUS.INVALID.getValue());
						sentMessageToQueue(basket, basket.getComment(), true);
					}
				} else {
					basket.setPhone(phoneNumber);
				}

				InsCLFreshV11 fresh = modelMapper.map(basket, InsCLFreshV11.class);
				if (basket.getStatus() == EnumType.LEAD_STATUS.DUPLICATED.getValue())
					fresh.setLeadStatus(EnumType.LEAD_STATUS.DUPLICATED.getValue());
				else if (basket.getStatus() == EnumType.LEAD_STATUS.INVALID.getValue())
					fresh.setLeadStatus(EnumType.LEAD_STATUS.INVALID.getValue());
				else
					fresh.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
				fresh.setLeadType("A");
				if(basket.getAttribute1() != null && !basket.getAttribute1().isEmpty()
						&& basket.getAttribute1().contains(Const.SPLIT))
					fresh.setAssigned(Integer.parseInt(basket.getAttribute1().replace(Const.SPLIT, "")));
				if(basket.getClickId() != null && !basket.getClickId().isEmpty())
					fresh.setClickId(basket.getClickId());
				fresh.setModifydate(null);

				if (basket.getClickId2() != null && !basket.getClickId2().isEmpty()){
					fresh.setSubid2(basket.getClickId2());
				}
				
				//set phone2 to phone2 neu co
				String phone = null;
				String phone2 = null;
				if (fresh.getPhone() != null && StringUtils.hasText(fresh.getPhone())) {
					int indexOfPhone2 = fresh.getPhone().indexOf("|");
					if (indexOfPhone2 > 0) {
						phone = fresh.getPhone().substring(0, indexOfPhone2);
						phone2 = fresh.getPhone().substring(indexOfPhone2 + 1);
					}
				}

				if (StringUtils.hasText(phone) && StringUtils.hasText(phone2)) {
					fresh.setPhone(phone);
					fresh.setOtherPhone1(phone2);
				}

				// set lai attribute
				fresh.setAttribute("" + callingId);
				fresh.setCallinglistId(callingId);

				if(fresh.getProdName().equals("tensicure-id"))
					fresh.setProdName("tensilab-id");
				fresh.setCpId(campaignId);
				fresh.setCrmActionType(EnumType.CRM_ACTION_TYPE.NEW_LEAD.getValue());
				DBResponse insResult = logService.insCLFreshV11(taskSession, fresh);
				// insert log
				try{
					int leadId = Integer.parseInt(insResult.getErrorMsg());
					if (leadId > 0) {
						ModelMapper modelMapper = new ModelMapper();
						CLFresh lead = modelMapper.map(fresh, CLFresh.class);
						lead.setLeadId(leadId);
						lead.setModifydate(new Date());
						lead.setNextCallTime(new Date()); 
						lead.setLeadId(leadId);  
						lead.setCreatedate(new Date());
						if (lead.getAssigned()== null) {
							lead.setAssigned(0);
						}
						if(lead.getLeadStatus() == null && lead.getLeadStatusName() == null) {
							lead.setLeadStatusName("new");
						}

						dbLog.writeLeadStatusLog(0, leadId, EnumType.LEAD_STATUS.NEW.getValue(),
								"Create Lead From Basket NEW");
//						dbLog.writeLeadStatusLogV2(0, lead, leadId, EnumType.LEAD_STATUS.NEW.getValue(),
//								"Create Lead From Basket NEW");
					}
				} catch(Exception ex){
					logger.error(ex.getMessage(), ex);
				}

				// create clickId to tracker
				if (fresh.getTrackerId() != null) {
					Boolean check = trkDataService.checkLeadIfNotExitsInTrkData(fresh.getLeadId());
					if (!check) {
						this.createLeadTracker(fresh);
					}
				}
				// increase counter
				if(campaignId > 0){
					String callingKey = Helper.createRedisKey(Const.REDIS_PREFIX_STATIC,
							fresh.getOrgId().toString() + ":" + campaignId);
					RedisHelper.incrementLead(stringRedisTemplate, callingKey);
				}
			}
			logger.info("Finished to process basket data with session: " + taskSession);
		} catch(Exception exp){
			logger.info("Error-Basket|{}", exp.getMessage(), exp);
		}
	}

	@Scheduled(cron = "0 2 * * * ?")
	public void processUncall() {
		java.util.Date now = new java.util.Date();

		/*
		 * GetLeadParamsV5 leadParamsV5 = new GetLeadParamsV5(); leadParamsV5
		 */
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void resetRoundRobinRule() {
		basketProcessorService.resetRoundRobinRule();
	}

	private Integer getCampaignFromCallingList(String callingKey, String session) {
		int campainId = 0;
		logger.info("-------- GET CALLINGLIST - CAMPAIGN -------");
		if(CAMPAIGN_CALLING_MAP.isEmpty()){
			GetCpCallingList cpCallingList = new GetCpCallingList();
			DBResponse<List<GetCpCallingListResp>> campaignList = freshService.getCpCallingList(session, cpCallingList);
			if(campaignList.getResult().isEmpty())
				return -1;

			for(GetCpCallingListResp campaign: campaignList.getResult()){
				String key = campaign.getOrgId() + ":" + campaign.getCallinglistId();
				CAMPAIGN_CALLING_MAP.put(key, campaign.getCpId());
			}
		}

		if(CAMPAIGN_CALLING_MAP.containsKey(callingKey))
			campainId = CAMPAIGN_CALLING_MAP.get(callingKey);
		logger.info("-------- GET CALLINGLIST - CAMPAIGN -------  {}", campainId);
		return campainId;
	}

	private void readConfig(int orgId) {
		if(!MAX_LIFE_TIME.containsKey(orgId)){
			String configTrashTime = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 6, 7);
			String configDupTime = RedisHelper.getGlobalParamValue(stringRedisTemplate, orgId, 6, 5);

			logger.info("{} ######################################################    {}", configTrashTime, configDupTime);
			int dupTime = -1;
			int trashTime = -1;
			int maxDupTime = -1;
			if(configTrashTime != null){
				String[] tmp = configTrashTime.split(":");
				try{
					dupTime = Integer.parseInt(tmp[1]);
				} catch(Exception e){
					dupTime = 14400;// 10 * 24 * 60 (10 ngay - don vi phut)
					logger.error(e.getMessage(), e);
				}
				try{
					trashTime = Integer.parseInt(tmp[0]);
				} catch(Exception e){
					trashTime = 180;// 3 * 60 (3 hour, don vi phut)
					logger.error(e.getMessage(), e);
				}
			}

			if(configDupTime != null)
				try{
					maxDupTime = Integer.parseInt(configDupTime);
				} catch(Exception e){
					maxDupTime = 180; //
					logger.error(e.getMessage(), e);
				}
			logger.info("{}  ############  duptime {}  ----------  trash {}    -------- {}  maxDupTime ----------------",
					orgId, dupTime, trashTime, maxDupTime);
			MAX_LIFE_TIME.put(orgId, dupTime);
			TRASH_TIME.put(orgId, trashTime);
			DUP_TIME.put(orgId, maxDupTime);
		}
	}

	private CLBasket createFreshMKTCheckDupRedis(CLBasket basket, String taskSession) {
		int tnzDupTime = 1440;// 24hours
		int tnzDupContact = 3;
		// int classicDupMinutes = 180; // check duplicate based on name, phone, product
		try{
			// config TNZ duplitcate time
			String configTnzTime = RedisHelper.getGlobalParamValue(stringRedisTemplate, basket.getOrgId(), 6, 8);
			if(configTnzTime != null)
				tnzDupTime = Integer.parseInt(configTnzTime);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}

		try{
			// config TNZ duplitcate time
			String configTnzDupContact = RedisHelper.getGlobalParamValue(stringRedisTemplate, basket.getOrgId(), 6, 13);
			if(configTnzDupContact != null)
				tnzDupContact = Integer.parseInt(configTnzDupContact);
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}

		String ip = Helper.getMktParam(basket.getClickId(), "ip");
		String click = Helper.getMktParam(basket.getClickId(), "clickid");
		String contact = StringHelper.createContact(basket);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, tnzDupTime);
		Date expireAt = cal.getTime();

		if(null != click){
			// check duplicate clickid
			String redisKey = String.format("%s:%d:%d:%d:%s", Const.REDIS_PREFIX_BASKET, basket.getOrgId(),
					basket.getAgcId(), basket.getProdId(), click);
			Map<String, String> clickIdContacts = RedisHelper.getRedis(stringRedisTemplate, redisKey);

			if(clickIdContacts != null && clickIdContacts.size() > 0){

				int sameContacts = 0, differentContacts = 0;

				if(clickIdContacts.get("contact").equals(contact)){
					sameContacts = Integer.parseInt(clickIdContacts.get("same")) + 1;
					clickIdContacts.put("same", String.valueOf(sameContacts));
				} else{
					differentContacts = Integer.parseInt(clickIdContacts.get("different")) + 1;
					clickIdContacts.put("different", String.valueOf(differentContacts));
				}

				RedisHelper.saveRedis(stringRedisTemplate, clickIdContacts, redisKey, false);

				if(sameContacts >= tnzDupContact || differentContacts >= tnzDupContact){
					logger.info("[MKT] SENT TO TRASH contact {} {}", basket.getLeadId(), contact);
					sentMessageToQueue(basket, Const.TRASH_LEAD_MKT, true);
					basket.setAttribute1("Duplicate >=3 clickID MKT sameContacts: " + (sameContacts >= tnzDupContact)
							+ " differentContacts: " + (differentContacts >= tnzDupContact));
					basket.setStatus(EnumType.LEAD_STATUS.DUPLICATED.getValue());
					updBasketToInactive(taskSession, basket);
					return null;
				}

				if(sameContacts == 2){
					logger.info("[MKT] DO NOTHING contact {} {}", basket.getLeadId(), contact);
					basket.setAttribute1("Duplicate = 2 clickID MKT");
					basket.setStatus(4);
					basket.setStatus(EnumType.LEAD_STATUS.DUPLICATED.getValue());
					updBasketToInactive(taskSession, basket);
					return null;
				}

			} else{ // new clickid

				clickIdContacts = new HashMap<>();
				clickIdContacts.put("contact", contact);
				clickIdContacts.put("same", "1");
				clickIdContacts.put("different", "1");
				RedisHelper.saveRedis(stringRedisTemplate, clickIdContacts, redisKey, false);
				RedisHelper.setExpireAt(stringRedisTemplate, redisKey, expireAt);

			}

			logger.info("[MKT] SENT TO HOLD contact {} {}", basket.getLeadId(), contact);
			sentMessageToQueue(basket, Const.HOLD_LEAD_MKT, true);
			return basket;
		}

		if(null != ip){
			// check trung IP
			String ipKey = String.format("%s:%d:%d:%d:%s", Const.REDIS_PREFIX_BASKET, basket.getOrgId(),
					basket.getAgcId(), basket.getProdId(), ip);
			Map<String, String> ips = RedisHelper.getRedis(stringRedisTemplate, ipKey);
			if(ips != null && ips.size() > 0){
				// TODO send trash
				logger.info("[MKT] SENT TO TRASH");
				int sameContact = 1;
				for(String key: ips.keySet())
					if(ips.get(key).equals(StringHelper.createContact(basket)))
						sameContact++;

				if(sameContact >= tnzDupContact){
					logger.info("[MKT] SENT TO TRASH SAME CONTACT ");
					// postback trash ve volumn
					sentMessageToQueue(basket, Const.TRASH_LEAD_MKT);
				} else if(sameContact >= 2){

				}
				ips.put(String.valueOf(sameContact), StringHelper.createContact(basket));
				RedisHelper.saveRedis(stringRedisTemplate, ips, ipKey);

				if(sameContact == 1)
					// TODO ko gui hold ve volumn, gui lead ve sale
					return basket;

				// sentMessageToQueue(basket, Const.TRASH_LEAD_MKT);
				updBasketToInactive(taskSession, basket);
				return null;
			} else{
				logger.info("[MKT] SENT TO HOLD {}", basket.getLeadId());
				RedisHelper.saveRedis(stringRedisTemplate, ipKey, click, StringHelper.createContact(basket), expireAt);
				// TODO send hold
				// sentMessageToQueue();
			}
		}

		sentMessageToQueue(basket, Const.HOLD_LEAD_MKT);
		return basket;
	}

	private CLBasket createFreshCheckDupRedis(CLBasket basket, String taskSession, String hash) {

		String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_BASKET, basket.getOrgId(), hash);
		Long ttl = RedisHelper.getTTLKey(stringRedisTemplate, key);
		int dupTime = MAX_LIFE_TIME.containsKey(basket.getOrgId())? MAX_LIFE_TIME.get(basket.getOrgId()) * 60
				: Const.LEAD_BASKET_MAX_LIFE;
		int trashTime = TRASH_TIME.containsKey(basket.getOrgId())? TRASH_TIME.get(basket.getOrgId()) * 60
				: Const.LEAD_BASKET_EXPIRE_TRASH;
		int maxDupTime = DUP_TIME.containsKey(basket.getOrgId())? DUP_TIME.get(basket.getOrgId()) * 60
				: Const.LEAD_BASKET_MAX_LIFE;
		if(basket.getAgcId() != null && basket.getAgcId() == 13)
			return basket;
		if(ttl != null)
			if(ttl == -2L){// khong ton tai key trong redis
				RedisHelper.saveRedis(stringRedisTemplate, StringHelper.createHashkey(basket), key, maxDupTime, true);
				logger.info("*******************   Save into basket NEW {} *******************  ", basket.getLeadId());
				// freshBasketMap.put(String.valueOf(hash), basket);
				return basket;
			} else if(ttl == -1L){// key ton tai nhung ko dat Expire
				// ko xu ly truong hop nay

			} else if(ttl >= 0){// duoc set expire
				if(ttl > maxDupTime - trashTime)
					// set basket la trash
					basket = updBasketDuplicateToTrash(taskSession, basket);
				else{// trong khoang tu Const.LEAD_BASKET_EXPIRE_TRASH den Const.LEAD_BASKET_MAX_LIFE
						// tao moi lead va assign lead nay cho agent da xu ly truoc do

					String value = RedisHelper.getKey(stringRedisTemplate, key);
					if(value != null){
						logger.info("The value is {}", value);
						if(value.contains(Const.SPLIT)){// neu da duoc assign
							String[] arr = value.split(Const.SPLIT);
							int assignUserId = 0;
							try{
								assignUserId = Integer.parseInt(arr[0]);
							} catch(Exception e){
								logger.error("$$$$$$$$$$$$$$$ FAIL TO convert assignuser {}", value);
							}
							if(assignUserId > 0)
								basket.setAttribute1(String.valueOf(assignUserId) + Const.SPLIT);
							// freshBasketMap.put(String.valueOf(hash), basket);
							return basket;
						} else
							// Const.LEAD_BASKET_EXPIRE_TRASH
							basket = updBasketDuplicateToTrash(taskSession, basket);
					}
				}
				return basket;
			}
		return null;
	}

	private boolean updateBasket(String SESSION, CLBasket clBasket, int status) {
		clBasket.setCreatedate(null);
		com.tms.entity.log.UpdCLBasket updCLBasket = modelMapper.map(clBasket, com.tms.entity.log.UpdCLBasket.class);
		updCLBasket.setModifydate(null);
		updCLBasket.setAttribute3(String.valueOf(status));
		try{
			logService.updBasket(SESSION, updCLBasket);
			return true;
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	// 0 mean inactive, default basket has atribute3 = 1
	private CLBasket updBasketDuplicateToTrash(String taskSession, CLBasket basket) {
		basket.setStatus(EnumType.LEAD_STATUS.DUPLICATED.getValue());
		basket.setAttribute1("LEAD DUPLICATE ( < " + Const.LEAD_BASKET_EXPIRE_TRASH + ")");
		return basket;
	}

	private TrkData createLeadTracker(InsCLFreshV11 fresh) throws IOException {
		TrkData result = new TrkData();
		int trackerId = fresh.getTrackerId();
		if (trackerId == TRACKER_SCALEO.intValue()) {
			TrkAffiliateMapping affiliateMapping = trkAffilateMappingService.getBygetByAgcCode(fresh.getAffiliateId());
			if (!ObjectUtils.isNull(affiliateMapping)) {
				ScaleoCreateLeadReq request = new ScaleoCreateLeadReq();
				request.setAffiliate_id(affiliateMapping.getTrackerAffiliateId());
				if (fresh.getLeadStatus().intValue() == (EnumType.LEAD_STATUS.DUPLICATED.getValue())) {
					request.setAdv_param4("trash");
				} else {
					request.setAdv_param4("pending");
				}
				request.setAdv_user_id(fresh.getLeadId().toString());
				request.setSub_id1(fresh.getAffiliateId());
				request.setSub_id5(fresh.getSubid5());
				request.setGoal_id(Integer.valueOf(fresh.getOfferId()));
				request.setAff_click_id(fresh.getClickId());
				logger.info(" affiliateId: {}", affiliateMapping.getTrackerAffiliateId());
				ScaleoHelper scaleoHelper = new ScaleoHelper();
				ScaleoCreateLeadRes response = scaleoHelper.createLead(request);
				logger.info("create lead to scaleo response: {}", LogHelper.toJson(response));
				if (response.getStatus().equals("success")) {
					TrkData trkData = new TrkData();
					trkData.setLeadId(fresh.getLeadId());
					trkData.setClickId(fresh.getClickId());
					trkData.setOfferId(Integer.valueOf(fresh.getOfferId()));
					trkData.setTrackerAffId(affiliateMapping.getTrackerAffiliateId().toString());
					trkData.setTrackerAdvClickId(response.getInfo().getLead_id());
					trkData.setTrackerOfferId(Integer.valueOf(fresh.getOfferId()));
					trkData.setAgcId(fresh.getAgcId());
					trkData.setTrackerId(trackerId);
					trkData.setOrgId(fresh.getOrgId());
					logger.info("create trkData param: {}", LogHelper.toJson(trkData));
					result = trkDataService.createTrackData(trkData);
				} else {
					logger.info("Create clickId Scaleo fail!");
				}
			} else {
				logger.info("No offer of Scaleo!");
			}
		}
		return result;

	}

	// 0 mean inactive, default basket has atribute3 = 1
	private boolean updBasketToInactive(String SESSION, CLBasket clBasket) {
		return updateBasket(SESSION, clBasket, 0);
	}

	// @Scheduled(fixedRate = 10*60*1000)
	public void processFreshData() {
		String taskSession = UUID.randomUUID().toString();
		logger.info("Starting to process fresh data with session: {}", taskSession);
		DBResponse<List<CLFresh>> freshList = freshService.getFreshLead(taskSession, new GetFreshLeadParams());
		int count = 0;
		for(CLFresh fresh: freshList.getResult()){
			count++;
			if(fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.NEW.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.UNREACHABLE.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.BUSY.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.NOANSWER.getValue())){
				InsCLActive insClActive = modelMapper.map(fresh, InsCLActive.class);
				logService.insCLActive(taskSession, insClActive);
			} else if(fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.REJECTED.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.APPROVED.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.CLOSED.getValue())){
				InsCLInActiveV3 clInactive = modelMapper.map(fresh, InsCLInActiveV3.class);
				logService.insCLInActiveV3(taskSession, clInactive);
			} else if(fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.DUPLICATED.getValue())
					|| fresh.getLeadStatus().equals(EnumType.LEAD_STATUS.INVALID.getValue())){
				InsCLTrash clTrash = modelMapper.map(fresh, InsCLTrash.class);
				logService.insCLTrash(taskSession, clTrash);

			}
			// TODO: delete fresh record

			if(count % 100 == 0)
				try{
					Thread.sleep(100);
				} catch(InterruptedException e){
					logger.error(e.getMessage(), e);
				}
		}

		logger.info("Finished to process fresh data with session: {}", taskSession);
	}

	// @Scheduled(fixedDelay = 1 * 60 * 1000, initialDelay = 4000)
	private void loadDataToRedis() {
		try{
			_isLockBasket = true;
			Date now = new Date();
			String taskSession = UUID.randomUUID().toString();
			GetBasketLeadV4 getBasketLeadParams = new GetBasketLeadV4();
			getBasketLeadParams.setStatus(1);
			getBasketLeadParams.setAttribute3("0");

			getBasketLeadParams.setCreatedate(DateHelper.toTMSFullDateFormat(DateHelper.getDateBefore(3)) + "|"
					+ DateHelper.toTMSFullDateFormat(now));
			DBResponse<List<CLBasket>> dbBaskets = basketService.getBasketLeadV4(taskSession, getBasketLeadParams);
			if(dbBaskets.getResult().isEmpty())
				return;

			for(CLBasket basket: dbBaskets.getResult()){
				int hash = StringHelper.createHashkeyFromString(StringHelper.createHashkey(basket));
				String key = RedisHelper.createRedisKey(Const.REDIS_PREFIX_BASKET, basket.getOrgId(),
						String.valueOf(hash));
				// RedisHelper.saveRedis(stringRedisTemplate, String.valueOf(hash), key, ((int)
				// DateHelper.calculateDifferenceTime(basket, now)) , true);
				RedisHelper.saveRedis(stringRedisTemplate, StringHelper.createHashkey(basket), key,
						Const.LEAD_BASKET_MAX_LIFE - (int) DateHelper.calculateDifferenceTime(basket, now), true);
			}
		} catch(Exception e){
			logger.error(e.getMessage(), e);
		} finally{
			_isLockBasket = false;
		}
	}

	private void isValidBasketTL(CLBasket basket) {
		if(basket.getName().toLowerCase().contains("test")){
			basket.setStatus(EnumType.LEAD_STATUS.INVALID.getValue());
			basket.setAttribute1("LEAD TEST");
		}
		String phone = basket.getPhone().replace(" ", "").replace("-", "").replace("+", "").trim();
		if(!isValidPhone(phone)){
			basket.setStatus(EnumType.LEAD_STATUS.INVALID.getValue());
			basket.setAttribute1("PHONE IS INVALID");
		}
	}

	public boolean isValidPhone(String phone) {
		if(phone.startsWith("0") && phone.length() == 10 || phone.startsWith("66") && phone.length() == 11)
			return true;
		return false;
	}

	public static boolean checkValidPhoneNumber(Integer orgId, String phoneNumber){
		String defaultRegion;
		switch (orgId) {
			case 4:
				defaultRegion = "VN";
				break;

			case 9:
				defaultRegion = "ID";
				break;

			case 10:
				defaultRegion = "TH";
				break;

			default:
				defaultRegion = null;
				break;
		}

		boolean checkFormatPhoneNumber = checkFormatPhoneNumber(phoneNumber);
		if (!checkFormatPhoneNumber) {
			return false;
		}
		boolean isValidPhoneNumberByRegion = isValidPhoneNumberByRegion(defaultRegion, phoneNumber);

		if (isValidPhoneNumberByRegion)
			return true;
		logger.info("region: {}, isValidPhoneNumber: {}, checkFormatPhoneNumber: {}", defaultRegion, isValidPhoneNumberByRegion, checkFormatPhoneNumber);
		return false;

	}

	public static boolean isValidPhoneNumberByRegion (String defaultRegion, String phoneNumber) {
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		Phonenumber.PhoneNumber thePhoneNumber = null;

		try {
			thePhoneNumber = phoneUtil.parse(phoneNumber, defaultRegion);
		} catch (NumberParseException e) {
			logger.info("Cannot parse the given phone number string {}", phoneNumber);
			e.printStackTrace();
		}

		return phoneUtil.isValidNumber(thePhoneNumber);
	}

	public static boolean checkFormatPhoneNumber(String in){
		Pattern pattern = Pattern.compile("^[0-9]{9,11}$");
		Matcher matcher = pattern.matcher(in);
		if(matcher.matches())
			return true;
		return false;
	}
}
