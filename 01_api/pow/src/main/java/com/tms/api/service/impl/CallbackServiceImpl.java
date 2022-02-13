package com.tms.api.service.impl;

import com.tms.api.dto.CreateCallBackDto;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.DateHelper;
import com.tms.api.helper.EnumType;
import com.tms.api.helper.Helper;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.CallbackService;
import com.tms.api.service.MktDataService;
import com.tms.api.task.DBLogWriter;
import com.tms.api.utils.LoggerUtils;
import com.tms.api.utils.ObjectUtils;
import com.tms.api.utils.StringUtility;
import com.tms.dto.DBResponse;
import com.tms.dto.GetFreshLeadV4;
import com.tms.dto.GetLeadParamsV4;
import com.tms.entity.CLFresh;
import com.tms.entity.log.InsCLCallbackV6;
import com.tms.entity.log.UpdLeadV7;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.rmi.UnexpectedException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service(value = "callbackService")
public class CallbackServiceImpl implements CallbackService {
    private static final Logger logger = LoggerFactory.getLogger(CallbackServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;
    private final DBLogWriter logWriterService;
    private final LogService logService;
    private final CLFreshService freshService;
    private final ModelMapper modelMapper;
    private final MktDataService mktDataService;

    // C-NP: consulting + not prospect
    // P: potential
    // closeDate_C-NP,maxCloseDate_C-NP,closeDate_P,maxCloseDate_P
    @Value("${config.close-callback-params: 4,4,4,15}")
    private String CLOSE_CALLBACK_PARAMS;

    @Autowired
    public CallbackServiceImpl(CLFreshService freshService, LogService logService, ModelMapper modelMapper, DBLogWriter logWriterService, MktDataService mktDataService) {
        this.freshService = freshService;
        this.logService = logService;
        this.modelMapper = modelMapper;
        this.logWriterService = logWriterService;
        this.mktDataService = mktDataService;
    }

    @Override
    public TMSResponse<?> creatOne(CreateCallBackDto callbackRequest, ObjectRequestDTO objectRequestDTO) {
        int[] phoneTypes = new int[]{EnumType.CALLBACK_PHONE_TYPE.AVAIABLE.getValue(), EnumType.CALLBACK_PHONE_TYPE.NEW.getValue()};
        GetLeadParamsV4 freshLeadParams = new GetLeadParamsV4();
        DBResponse<List<CLFresh>> dbFreshResponse;
        DBResponse<?> resultResponse;
        freshLeadParams.setLeadId(callbackRequest.getLeadId());
        freshLeadParams.setOrgId(objectRequestDTO.getOrganizationId());

        boolean isValidPhoneType = java.util.stream.IntStream.of(phoneTypes).anyMatch(x -> x == callbackRequest.getPhoneType());

        if (!isCallbackStatus(callbackRequest.getStatus())) {
            try {
                throw new TMSException("Status is not valid");
            } catch (TMSException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (!isValidPhoneType) {
            try {
                throw new TMSException("Phone Type is not valid");
            } catch (TMSException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (callbackRequest.getPhoneType() == EnumType.CALLBACK_PHONE_TYPE.NEW.getValue()) {
            if (StringUtils.isEmpty(callbackRequest.getName())) {
                try {
                    throw new TMSException("Name is empty");
                } catch (TMSException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            if (StringUtils.isEmpty(callbackRequest.getPhone())) {
                try {
                    throw new TMSException("Phone is empty");
                } catch (TMSException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        dbFreshResponse = freshService.getLeadV4(objectRequestDTO.getSessionId(), freshLeadParams);
        if (CollectionUtils.isEmpty(dbFreshResponse.getResult())) {
            try {
                throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
            } catch (TMSException e) {
                logger.error(e.getMessage(), e);
            }
        }

        CLFresh freshOne = dbFreshResponse.getResult().get(0);
        logger.info("body fresh lead response: {}", LoggerUtils.snagAsLogJson(freshOne));
        InsCLCallbackV6 callbackOne = modelMapper.map(freshOne, InsCLCallbackV6.class);
        callbackOne.setAssigned(objectRequestDTO.getUserId());
        callbackOne.setLeadStatus(callbackRequest.getStatus());
        callbackOne.setLastCallTime(null);
        callbackOne.setNextCallTime(null);
        callbackOne.setCreatedate(null);
        callbackOne.setModifydate(null);

        if (!StringUtils.isEmpty(freshOne.getAffiliateId())) {
            callbackOne.setAffiliateId(freshOne.getAffiliateId());
        }

        if (!ObjectUtils.isNull(freshOne.getAgcId())) {
            callbackOne.setAgcId(freshOne.getAgcId());
        }

        if (!StringUtils.isEmpty(callbackRequest.getUserDefine5())) {
            callbackOne.setUserDefin05(callbackRequest.getUserDefine5());
        }

        if (!Helper.isValidator(objectRequestDTO.getUser())) {
            if (ObjectUtils.allNotNull(freshOne.getTotalCall())) {
                callbackOne.setTotalCall(freshOne.getTotalCall() + 1);
            } else {
                callbackOne.setTotalCall(1);
            }
        }

        /* set comments */
        if (!StringUtils.isEmpty(freshOne.getComment())) {
            callbackOne.setComment(freshOne.getComment() + "|" + callbackRequest.getNote());
        } else {
            callbackOne.setComment(callbackRequest.getNote());
        }

        /* set leads */
        UpdLeadV7 leadRequestOne = modelMapper.map(freshOne, UpdLeadV7.class);

        /* set comments */
        if (StringUtils.isEmpty(freshOne.getComment())) {
            leadRequestOne.setComment(callbackRequest.getNote());
        } else {
            leadRequestOne.setComment(freshOne.getComment() + "|" + callbackRequest.getNote());
        }


        if (callbackRequest.getPhoneType() == EnumType.CALLBACK_PHONE_TYPE.NEW.getValue()) {
            String commentModified;
            if (!StringUtils.isEmpty(freshOne.getComment())) {
                commentModified = freshOne.getName() + "|" + freshOne.getPhone() + "|" + freshOne.getComment() + "|" + callbackRequest.getNote();
            } else {
                commentModified = freshOne.getName() + "|" + freshOne.getPhone() + "|" + callbackRequest.getNote();
            }
            callbackOne.setPhone(callbackRequest.getPhone());
            callbackOne.setName(callbackRequest.getName());
            callbackOne.setComment(commentModified);
            /* continue set body leadRequestOne */
            leadRequestOne.setName(callbackRequest.getName());
            leadRequestOne.setPhone(callbackRequest.getPhone());
            leadRequestOne.setComment(commentModified);
            logger.info("Comment modified when phone type is new : {}", commentModified);
        }

        /* continue set body leadRequestOne */
        leadRequestOne.setLeadStatus(callbackRequest.getStatus());
        leadRequestOne.setNextCallTime(null);
        leadRequestOne.setCreatedate(null);
        leadRequestOne.setModifydate(null);

        if (!Helper.isValidator(objectRequestDTO.getUser())) {
            if (ObjectUtils.allNotNull(freshOne.getTotalCall())) {
                leadRequestOne.setTotalCall(freshOne.getTotalCall() + 1);
            } else {
                leadRequestOne.setTotalCall(1);
            }
        }

		Calendar dateNow = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(Const.DB_DATE_FORMAT);
		SimpleDateFormat df = new SimpleDateFormat(Const.DATE_FORMAT);
		
		// update .1) FristCallTIme .2) FristCallBy .3) FirstCallStatus .4) FirstCallReason .5) FristCallComment
		// === Lan dau tien leads duoc agent make call (total call = 1 va cac truong tren = null )
		if(callbackOne.getTotalCall() == 1 && (leadRequestOne.getFirstCallTime() == null
				&& leadRequestOne.getFirstCallBy() == null && leadRequestOne.getFirstCallStatus() == null
				&& StringUtils.isEmpty(leadRequestOne.getFirstCallReason())
				&& StringUtils.isEmpty(leadRequestOne.getFirstCallComment()))){
			leadRequestOne.setFirstCallTime(dateFormat.format(dateNow.getTime()));
			leadRequestOne.setFirstCallBy(objectRequestDTO.getUserId());
			leadRequestOne.setFirstCallStatus(callbackOne.getLeadStatus());
			leadRequestOne.setFirstCallReason(callbackOne.getUserDefin05());
			leadRequestOne.setFirstCallComment(callbackOne.getComment());
		} else if(leadRequestOne.getFirstCallTime() != null){
			Calendar firstCallTime = Calendar.getInstance();
			try{
				firstCallTime.setTime(df.parse(leadRequestOne.getFirstCallTime()));
				leadRequestOne.setFirstCallTime(dateFormat.format(firstCallTime.getTime()));
			} catch(ParseException e){
			}
		}

		// update 6) FCRTime .7) FCRBy .8) FCRStatus .9) FCRReason .10) FCRComment
		// === Lan dau tien leads duoc agent make call (total call = 1 va cac truong tren = null )
		// === Va lead status la contactable, bao gom: Callback, Approved, Rejected, Trash
		if(callbackOne.getTotalCall() == 1
				&& (leadRequestOne.getFcrTime() == null && leadRequestOne.getFcrBy() == null
						&& leadRequestOne.getFcrStatus() == null && StringUtils.isEmpty(leadRequestOne.getFcrReason())
						&& StringUtils.isEmpty(leadRequestOne.getFcrComment()))
				&& leadIsContactable(leadRequestOne.getLeadStatus())){
			leadRequestOne.setFcrTime(dateFormat.format(dateNow.getTime()));
			leadRequestOne.setFcrBy(objectRequestDTO.getUserId());
			leadRequestOne.setFcrStatus(callbackOne.getLeadStatus());
			leadRequestOne.setFcrReason(callbackOne.getUserDefin05());
			leadRequestOne.setFcrComment(callbackOne.getComment());
		} else if(leadRequestOne.getFcrTime() != null){
			Calendar fcrTime = Calendar.getInstance();
			try{
				fcrTime.setTime(df.parse(leadRequestOne.getFcrTime()));
				leadRequestOne.setFcrTime(dateFormat.format(fcrTime.getTime()));
			} catch(ParseException e){
			}
		}

        try {
            this.updRequestTime_CloseTime_MaxCloseTime(callbackOne, callbackRequest);
        } catch (DateTimeParseException e) {
            logger.error(e.getMessage(), e);
            return TMSResponse.buildResponse(null, 0, ErrorMessage.INVALID_PARAM.getMessage(), ErrorMessage.INVALID_PARAM.getCode());
        } catch (UnexpectedException e) {
            return TMSResponse.buildResponse(null, 0, e.getMessage(), ErrorMessage.INVALID_PARAM.getCode());
        }

        resultResponse = logService.insCLCallbackV6(objectRequestDTO.getSessionId(), callbackOne);

        if (resultResponse.getErrorCode() != Const.INS_DB_SUCCESS) {
            throw new DataIntegrityViolationException(resultResponse.getErrorMsg());
        }
        DBResponse<?> resultResponseAsLeadUpdated = logService.updLeadV7(objectRequestDTO.getSessionId(), leadRequestOne);
        logger.info("Messages code from leads updated: {} -> {}", resultResponseAsLeadUpdated.getErrorCode(), (resultResponseAsLeadUpdated.getErrorCode() != Const.INS_DB_SUCCESS));

        if (resultResponseAsLeadUpdated.getErrorCode() != Const.INS_DB_SUCCESS) {
            try {
                throw new TMSException(resultResponseAsLeadUpdated.getErrorMsg());
            } catch (TMSException e) {
                logger.error(e.getMessage(), e);
            }
        }

        // Update customer data
        freshOne.setName(callbackRequest.getName());
        freshOne.setPhone(callbackRequest.getPhone());
        freshOne.setCustJob(callbackRequest.getCustJob());
        freshOne.setCustDob(callbackRequest.getCustDob());
        freshOne.setCustAge(callbackRequest.getCustAge());
        freshOne.setCustGender(callbackRequest.getCustGender());
        freshOne.setCustOtherSymptom(callbackRequest.getCustOtherSymptom());
        mktDataService.save(objectRequestDTO.getSessionId(), freshOne, objectRequestDTO.getUserId());

        logWriterService.writeAgentActivityLog(objectRequestDTO.getUserId(), "lead is updated in callback", "lead", callbackRequest.getLeadId(), "lead status", callbackRequest.getStatus().toString());
        return TMSResponse.buildResponse(resultResponse.getResult());
    }

    private void updRequestTime_CloseTime_MaxCloseTime(InsCLCallbackV6 callbackOne, CreateCallBackDto callbackRequest) throws UnexpectedException {
        LocalDate requestTime, closeTime, maxCloseTime;
        LocalDate now = LocalDate.now();
        String requestTimeStr = callbackRequest.getCallbackTime();

        // Xy ly khi status la callback consulting va callbackTime null
        if (callbackRequest.getStatus() == Const.LEAD_STATUS_CALLBACK_CONSULTING && !StringUtility.hasLength(requestTimeStr))
            requestTimeStr = DateHelper.toTMSFullDateFormat(LocalDateTime.now().plusHours(1));

        requestTime = DateHelper.convertFromTMSFullDateFormatToLocalDate(requestTimeStr);

        switch (callbackRequest.getOldStatus()) {
            case Const.LEAD_STATUS_CALLBACK_CONSULTING:
            case Const.LEAD_STATUS_CALLBACK_NOT_PROSPECT:
                if (callbackRequest.getStatus() == Const.LEAD_STATUS_CALLBACK_POTENTIAL) {
                    closeTime = requestTime.plusDays(getCloseCallbackParam(2));
                    maxCloseTime = now.plusDays(getCloseCallbackParam(3));
                } else {
                    closeTime = DateHelper.convertFromTMSFullDateFormatToLocalDate(callbackRequest.getCloseTime());
                    maxCloseTime = DateHelper.convertFromTMSFullDateFormatToLocalDate(callbackRequest.getMaxCloseTime());
                }
                break;
            case Const.LEAD_STATUS_CALLBACK_POTENTIAL:
                if (callbackRequest.getStatus() == Const.LEAD_STATUS_CALLBACK_POTENTIAL) {
                    closeTime = requestTime.plusDays(getCloseCallbackParam(2));
                    maxCloseTime = DateHelper.convertFromTMSFullDateFormatToLocalDate(callbackRequest.getMaxCloseTime());
                } else {
                    closeTime = now.plusDays(getCloseCallbackParam(0));
                    maxCloseTime = now.plusDays(getCloseCallbackParam(1));
                }
                break;
            default:
                if (callbackRequest.getStatus() == Const.LEAD_STATUS_CALLBACK_POTENTIAL) {
                    closeTime =requestTime.plusDays(getCloseCallbackParam(2));
                    maxCloseTime = now.plusDays(getCloseCallbackParam(3));
                } else {
                    closeTime = now.plusDays(getCloseCallbackParam(0));
                    maxCloseTime = now.plusDays(getCloseCallbackParam(1));
                }
                break;
        }

        // RequestTime must be shorter than maxCloseTime
        if (requestTime.isAfter(maxCloseTime) || requestTime.isEqual(maxCloseTime))
            throw new UnexpectedException("RequestTime must be before " + DateHelper.toTMSDateFormatUI(maxCloseTime));

        if (closeTime.isAfter(maxCloseTime))
            closeTime = maxCloseTime;

        callbackOne.setRequestTime(requestTimeStr);
        callbackOne.setCloseTime(DateHelper.toTMSDateFormat(closeTime));
        callbackOne.setMaxCloseTime(DateHelper.toTMSDateFormat(maxCloseTime));
    }

    @Override
    @Transactional
    public Integer delete(List<Integer> leadId, Integer orgId) {
        if (leadId.isEmpty()) {
            return 0;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE cl_callback SET is_deleted = 1, modifydate = :modifydate WHERE lead_id IN :leadId AND org_id = :orgId AND is_deleted <> 1");
        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("leadId", leadId);
        query.setParameter("orgId", orgId);
        query.setParameter("modifydate", new Date());

        return query.executeUpdate();
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

	private boolean isCallbackStatus(int status) {
        List<Integer> callbackStatus = Arrays.asList(EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue(),
                EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue(),
                EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue());

        return callbackStatus.stream().anyMatch(x -> x == status);
    }

    private int getCloseCallbackParam(int index) {
        String[] params = CLOSE_CALLBACK_PARAMS.split(",");
        return Integer.parseInt(params[index]);
    }
}
