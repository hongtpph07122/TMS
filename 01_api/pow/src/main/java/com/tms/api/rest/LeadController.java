package com.tms.api.rest;

import com.tms.api.dto.*;
import com.tms.api.dto.Response.StreamsResponseDTO;
import com.tms.api.entity.ClFresh;
import com.tms.api.entity.ClManipulatedFresh;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.*;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.*;
import com.tms.api.task.AutoReloadConfigTeam;
import com.tms.api.task.DBLogWriter;
import com.tms.api.utils.DateUtils;
import com.tms.api.utils.ObjectUtils;
import com.tms.api.utils.StringUtility;
import com.tms.api.variable.PatternEpochVariable;
import com.tms.dto.*;
import com.tms.entity.CLCallback;
import com.tms.entity.CLFresh;
import com.tms.entity.PDProduct;
import com.tms.entity.User;
import com.tms.entity.log.InsCLFreshV11;
import com.tms.entity.log.UpdLeadReassignV3;
import com.tms.entity.log.UpdLeadV5;
import com.tms.service.impl.*;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Scope("prototype")
@RequestMapping("/lead")
public class LeadController extends BaseController {
	private static final int ORG_TH = 10;

	private final CLFreshService freshService;
	private final SOService soService;
	private final ModelMapper modelMapper;
	private final CLCallbackService clCallbackService;
	private final StringRedisTemplate stringRedisTemplate;
	private final LogService logService;
	private final CLActiveService clActiveService;
	private final CLProductService clProductService;
	private final DBLogWriter dbLog;
	private final CDRsService cdrService;
	private final LeadService leadService;
	private final ClManipulatedFreshService clManipulatedFreshService;
	private final UploadService uploadService;
	private final MktDataService mktDataService;
	private final AutoReloadConfigTeam autoReloadConfigTeam;

	@Autowired
	public LeadController(CLCallbackService clCallbackService,
						  CLFreshService freshService,
						  ModelMapper modelMapper,
						  StringRedisTemplate stringRedisTemplate,
						  LogService logService,
						  CLActiveService clActiveService,
						  CLProductService clProductService,
						  DBLogWriter dbLog,
						  CDRsService cdrService,
						  LeadService leadService,
						  UploadService uploadService,
						  ClManipulatedFreshService clManipulatedFreshService,
						  MktDataService mktDataService,
						  SOService soService,
						  AutoReloadConfigTeam autoReloadConfigTeam) {
		this.clCallbackService = clCallbackService;
		this.freshService = freshService;
		this.modelMapper = modelMapper;
		this.stringRedisTemplate = stringRedisTemplate;
		this.logService = logService;
		this.clActiveService = clActiveService;
		this.clProductService = clProductService;
		this.dbLog = dbLog;
		this.cdrService = cdrService;
		this.leadService = leadService;
		this.uploadService = uploadService;
		this.clManipulatedFreshService = clManipulatedFreshService;
		this.mktDataService = mktDataService;
		this.soService = soService;
		this.autoReloadConfigTeam = autoReloadConfigTeam;
	}

	@SuppressWarnings("deprecation")
	private static String getCellValue(Cell cell) {
		if(cell == null)
			return "";
		cell.setCellType(CellType.STRING);
		switch(cell.getCellType()){
		case STRING:
			return cell.getStringCellValue();
		case NUMERIC:
			if(DateUtil.isCellDateFormatted(cell)){
				Date date = cell.getDateCellValue();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				return dateFormat.format(date);
			} else
				return cell.getNumericCellValue() + "";
		case FORMULA:
			return cell.getCellFormula();
		case BLANK:
			return "";
		default:
			return "";
		}
	}

	@GetMapping("/{leadId}")
	public TMSResponse getFreshListByLeadId(@PathVariable Integer leadId) throws TMSException {
		GetLeadParamsV10 clFreshParams = new GetLeadParamsV10();
		clFreshParams.setLeadId(leadId);
		clFreshParams.setOrgId(getCurOrgId());
		DBResponse<List<CLFresh>> dbResponse = freshService.getLeadV13(SESSION_ID, clFreshParams);
		if(dbResponse.getResult().isEmpty()){
			logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		}
		CLFresh fresh = dbResponse.getResult().get(0);

		if (Helper.isAgent(_curUser) && _curUser.getUserId().intValue() != fresh.getAssigned()){
			return TMSResponse.buildResponse(true, 0, ErrorMessage.CAN_NOT_GET_LEAD.getMessage(), 400);
		}

		// neu lead la callback
		if(EnumType.LEAD_STATUS.isCallback(fresh.getLeadStatus())){
			// return
			// ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION,
			// "/api/v1/callback/" + fresh.getLeadId()).build();
			// TODO need to change this implement, need Redirect to callback
			GetCallbackAllV3 clCallbackParams3 = new GetCallbackAllV3();
			clCallbackParams3.setLeadId(fresh.getLeadId());
			DBResponse<List<CLCallback>> dbResponse3 = clCallbackService.getCallbackAllV3(SESSION_ID, clCallbackParams3);
			if(dbResponse3 == null || dbResponse3.getResult().isEmpty()){
				logger.info(ErrorMessage.NOT_FOUND.getMessage());
				throw new TMSException(ErrorMessage.NOT_FOUND);
			}
			CLCallback clCallback = dbResponse3.getResult().get(0);
			//Nếu là callback thì set thêm otherphone1 từ cl_fresh sang
			if(fresh.getOtherPhone1() != null && StringUtils.hasLength(fresh.getOtherPhone1())){
				clCallback.setOtherPhone1(fresh.getOtherPhone1());
			}
			CLCallbackDto clCallbackDto = modelMapper.map(clCallback, CLCallbackDto.class);
			String comment = fresh.getComment() + clCallbackDto.getComment();
			clCallbackDto.setComment(comment);
			// Nếu lead là call back thì set thêm agent note từ cl_fresh sang
			clCallbackDto.setAgentNote(fresh.getAgentNote());
			clCallbackDto.setFirstCallTime(fresh.getFirstCallTime());
			clCallbackDto.setFirstCallBy(fresh.getFirstCallBy());
			clCallbackDto.setFirstCallStatus(fresh.getFirstCallStatus());
			clCallbackDto.setFcrTime(fresh.getFcrTime());
			clCallbackDto.setFcrBy(fresh.getFcrBy());
			clCallbackDto.setFcrStatus(fresh.getFcrStatus());
			clCallbackDto.setFcrReason(fresh.getFcrReason());
			clCallbackDto.setFcrComment(fresh.getFcrComment());

			clCallbackDto = leadService.setCustomerData(SESSION_ID, fresh.getLeadId(), clCallbackDto);

			return TMSResponse.buildResponse(clCallbackDto, 1);
		}
		CLFreshDto phoneDto = modelMapper.map(fresh, CLFreshDto.class);
		phoneDto = leadService.setCustomerData(SESSION_ID, fresh.getLeadId(), phoneDto);

		return TMSResponse.buildResponse(phoneDto, 1);
	}

	@GetMapping
	public ResponseEntity getFreshList(HttpServletResponse response) throws TMSException {

		return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/api/v1/agent/calling")
				.build();
	}

	@PostMapping
	public TMSResponse<?> createLead(@RequestBody InsCLFreshV11 clFresh) throws TMSException {
		if(Helper.isTeamLeaderOther(_curUser)){
			return TMSResponse.buildResponse("You have not a permission to execute this function.");
		}
		leadService.fixLocation(clFresh);
		clFresh.setOrgId(getCurOrgId());
		clFresh.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
		clFresh.setLeadType("M"); // flag to generate lead_id automatically
		clFresh.setCrmActionType(EnumType.CRM_ACTION_TYPE.NEW_LEAD.getValue());
		DBResponse<?> dbResponse = logService.insCLFreshV11(SESSION_ID, clFresh);
		if(dbResponse == null){
			logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		}

		// Save mkt_data
		try {
			int leadId = Integer.parseInt(dbResponse.getErrorMsg());
			if (leadId > 0)
				mktDataService.save(SESSION_ID, clFresh, leadId, getCurrentUser().getUserId());
		} catch (NumberFormatException e) {
			logger.error("Error save mkt_data in saveLeadInfo manual");
		}

		// insert log
		try{
			int leadId = Integer.parseInt(dbResponse.getErrorMsg());
			if (leadId > 0) {
				ModelMapper modelMapper = new ModelMapper();
				CLFresh lead = modelMapper.map(clFresh, CLFresh.class);
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
				dbLog.writeLeadStatusLog(_curUser.getUserId(), leadId, EnumType.LEAD_STATUS.NEW.getValue(),
						"Create Lead Manually NEW");
//				dbLog.writeLeadStatusLogV2(_curUser.getUserId(), lead, leadId, EnumType.LEAD_STATUS.NEW.getValue(),
//						"Create Lead Manually NEW");
			}
		} catch(Exception ex){

		}
		int HttpStatus = dbResponse.getErrorCode() == 1? 200 : dbResponse.getErrorCode();
		return TMSResponse.buildResponse(dbResponse.getErrorMsg().trim(), 0, "", HttpStatus);
	}

	@PostMapping("/excel/upload/{cpId}/{clId}")
	public TMSResponse createLeadByExcel(@RequestParam("file") MultipartFile file, @PathVariable Integer cpId,
			@PathVariable Integer clId) throws Exception {
//        return TMSResponse.buildResponse(Helper.isValidDate("8/15/2019", "MM/dd/yyyy"));

		if(Helper.isTeamLeaderOther(_curUser)){
			return TMSResponse.buildResponse("You have not a permission to execute this function.");
		}
		User user = _curUser;
		int orgId = user.getOrgId();

		if(orgId == ORG_TH)
			return uploadService.createLeadByExcel(file, cpId, clId, SESSION_ID, user.getUserId(), orgId);

		int count = 0;
		try{
			Workbook book = WorkbookFactory.create(file.getInputStream());
			Sheet sheet = book.getSheetAt(0);
			Map<String, Integer> headerIndexMap = new HashMap<>();
			Row headerRow = sheet.getRow(0);
			short minColIx = headerRow.getFirstCellNum();
			short maxColIx = headerRow.getLastCellNum();
			for(short colIx = minColIx; colIx < maxColIx; colIx++){ // loop from first to last index
				Cell cell = headerRow.getCell(colIx);
				headerIndexMap.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
			}
			GetProductParams getProductParams = new GetProductParams();
			getProductParams.setOrgId(orgId);
			StringBuilder errorSb = new StringBuilder();
			List<InsCLFreshV11> clFreshList = new ArrayList<>();
			for(Row dataRow: sheet){
				if(dataRow.getRowNum() == 0)
					continue;
				if(dataRow.getCell(headerIndexMap.get("Customer Name".toLowerCase())) == null)
					continue;

				String name = getCellValue(dataRow.getCell(headerIndexMap.get("Customer Name".toLowerCase())));
				String phone = getCellValue(dataRow.getCell(headerIndexMap.get("Customer Phone".toLowerCase())));
				String productName = getCellValue(dataRow.getCell(headerIndexMap.get("Products Name".toLowerCase())));
				String address = getCellValue(dataRow.getCell(headerIndexMap.get("Address".toLowerCase())));
				String agcCode = getCellValue(dataRow.getCell(headerIndexMap.get("Status transport".toLowerCase())));
				String updatedDate = getCellValue(dataRow.getCell(headerIndexMap.get("Updated date".toLowerCase())));
				String commnet = getCellValue(dataRow.getCell(headerIndexMap.get("Comment".toLowerCase())));
				String agentNote = getCellValue(dataRow.getCell(headerIndexMap.get("Agent note".toLowerCase())));

				if(name == null || name.isEmpty())
					continue;
				Integer productId = null;
				getProductParams.setName(productName);
				DBResponse<List<PDProduct>> dbResponse = clProductService.getProduct(SESSION_ID, getProductParams);
				if(dbResponse != null && !dbResponse.getResult().isEmpty())
					productId = dbResponse.getResult().get(0).getProdId();
				else
					errorSb.append("Row " + dataRow.getRowNum() + ": Product not found \n");

				if(!updatedDate.isEmpty() && !Helper.isValidDate(updatedDate, "MM/dd/yyyy"))
					errorSb.append("Row " + dataRow.getRowNum() + ": date format is invalid \n");

				InsCLFreshV11 clFresh = new InsCLFreshV11();
				clFresh.setOrgId(orgId);
				clFresh.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
				clFresh.setLeadType("M"); // flag to generate lead_id automatically
				clFresh.setName(name);
				clFresh.setPhone(phone);
				clFresh.setProdId(productId);
				clFresh.setProdName(productName);
				clFresh.setAddress(address);
				clFresh.setAgcCode(agcCode);
				clFresh.setAssigned(0);
				clFresh.setCpId(cpId);
				clFresh.setCallinglistId(clId);
				clFresh.setModifydate(Helper.convertToTMSDate(updatedDate, "yyyyMMdd"));
				clFresh.setComment(commnet);
				clFresh.setAgentNote(agentNote);
				clFresh.setCrmActionType(EnumType.CRM_ACTION_TYPE.NEW_LEAD.getValue());

				clFreshList.add(clFresh);
			}
			if(errorSb.length() > 0)
				return TMSResponse.buildResponse(errorSb.toString(), 0, "", 400);
			for(InsCLFreshV11 clFresh: clFreshList){
				DBResponse<?> dbResponse = logService.insCLFreshV11(SESSION_ID, clFresh);

				// insert log
				try{
					int leadId = Integer.parseInt(dbResponse.getErrorMsg());
					if(leadId > 0){
						count++;
						dbLog.writeLeadStatusLog(_curUser.getUserId(), leadId, EnumType.LEAD_STATUS.NEW.getValue(),
								"Create Lead By Excel NEW");
					}
				} catch(Exception ex){
					logger.error("Error writeLogLeadStatus when create lead by excel: ", ex);
				}
				RedisHelper.incrementLead(stringRedisTemplate, Const.REDIS_PREFIX_STATIC + ":" + orgId);
			}
		} catch(EncryptedDocumentException e){
			logger.error("EncryptedDocumentException: ", e);
			return TMSResponse.buildResponse(e.getMessage(), 0, e.getMessage(), 400);
		} catch(IOException e){
			logger.error("IOException: ", e);
			return TMSResponse.buildResponse(e.getMessage(), 0, e.getMessage(), 400);
		}
		return TMSResponse.buildResponse(true, count, count + " rows import successfully", 200);
	}

	@GetMapping("/{leadId}/cdr")
	public TMSResponse getCdrListByLeadId(@PathVariable Integer leadId) throws TMSException {
		GetCdrV2 cdrParam = new GetCdrV2();
		cdrParam.setOrgId(getCurOrgId());
		cdrParam.setLeadId(leadId);
		DBResponse<List<GetCdrResp>> cdrList = clProductService.getCdrV2(SESSION_ID, cdrParam);
		if(cdrList == null)
			return TMSResponse.buildResponse(new ArrayList<GetCdrResp>(), 0, "Success", 200);
		return TMSResponse.buildResponse(cdrList.getResult(), cdrList.getRowCount(), "Success", 200);
	}

	@GetMapping("/cdr/download/{callId}")
	public TMSResponse downloadCDRByCallId(@PathVariable Integer callId) throws TMSException {

		DBResponse<List<GetCdrResp>> cdrList = new DBResponse<>();

		if(Helper.isTeamLeader(_curUser)){
			GetCdrV2 cdrParam = new GetCdrV2();
			cdrParam.setOrgId(getCurOrgId());
			cdrParam.setCallId(callId);
			cdrList = clProductService.getCdrV2(SESSION_ID, cdrParam);
			if(!cdrList.getResult().isEmpty()){
				String filename = cdrList.getResult().get(0).getCallId() + ".wav";
				logger.info("filename: {}\r\nURL: {}", filename, cdrList.getResult().get(0).getPlaybackUrl());
				try(BufferedInputStream in = new BufferedInputStream(
						new URL(cdrList.getResult().get(0).getPlaybackUrl()).openStream());
						FileOutputStream fileOutputStream = new FileOutputStream(filename)){
					byte dataBuffer[] = new byte[1024];
					int bytesRead;
					while((bytesRead = in.read(dataBuffer, 0, 1024)) != -1)
						fileOutputStream.write(dataBuffer, 0, bytesRead);
				} catch(IOException e){
					logger.error("Exception write cdrList to byte: ", e);
					return TMSResponse.buildResponse(null, 0, "file not found", 400);
				}

			}
		} else
			return TMSResponse.buildResponse(null, 0, "Required teamleader only", 400);
		return TMSResponse.buildResponse(cdrList.getResult(), cdrList.getRowCount(), "OK", 200);
	}

	@GetMapping("/{leadId}/history")
	public TMSResponse getLeadHistoryByLeadId(@PathVariable Integer leadId,
			@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
			@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) throws TMSException {
		GetLogLead logLead = new GetLogLead();
		// logLead.setOrgId(_curOrgId);
		logLead.setLeadId(leadId);
		logLead.setLimit(limit);
		logLead.setOffset(offset);
		DBResponse<List<GetLogLeadResp>> cdrList = clActiveService.getLogLead(SESSION_ID, logLead);
		if(cdrList == null)
			return TMSResponse.buildResponse(new ArrayList<GetLogLeadResp>(), 0, "Success", 200);
		return TMSResponse.buildResponse(cdrList.getResult(), cdrList.getRowCount(), "Success", 200);
	}

	@RequestMapping(value = "/cdr", method = {RequestMethod.GET, RequestMethod.POST})
	public TMSResponse<?> getCdrListByLeadId(GetCdrV3 cdrParam) {

		if(ObjectUtils.isNull(cdrParam.getLimit()))
			cdrParam.setLimit(Const.DEFAULT_PAGE_SIZE);
		cdrParam.setOrgId(getCurOrgId());
		if (cdrParam.getStarttime() == null) {
			java.time.LocalDateTime beforeDate = java.time.LocalDateTime.now().minusDays(30);
			java.time.LocalDateTime now = java.time.LocalDateTime.now();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern("yyyyMMddHHmmss");
			cdrParam.setStarttime(beforeDate.format(formatter) + "|" + now.format(formatter));
		}
		if (cdrParam.getCreatetime() == null) {
			java.time.LocalDateTime beforeDate = java.time.LocalDateTime.now().minusDays(30);
			java.time.LocalDateTime now = java.time.LocalDateTime.now();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern("yyyyMMddHHmmss");
			cdrParam.setCreatetime(beforeDate.format(formatter) + "|" + now.format(formatter));
		}
		logger.info("Get CDR param: " + LogHelper.toJson(cdrParam));
		DBResponse<List<GetCdrResp>> cdrList = clProductService.getCdrV3(SESSION_ID, cdrParam);
		List<GetCdrResponse> cdrRespList = cdrService.findAllCdr(cdrList.getResult());

		if(CollectionUtils.isEmpty(cdrRespList))
			return TMSResponse.buildResponse(new ArrayList<GetCdrResp>(), 0, "Success", 200);
		return TMSResponse.buildResponse(cdrRespList, cdrList.getRowCount(), "Success", 200);
	}

	@RequestMapping(value = "/export-cdr/excel", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<?> exportCDRsExcel(@RequestParam(value = "sheetName", defaultValue = "Sheet1", required = false) String sheetName,
									  @RequestParam(value = "filename", defaultValue = "CDRs.xlsx", required = false) String filename,
									  GetCdrV2 cdrParam) {

		cdrParam.setOrgId(getCurOrgId());

//		if (!ObjectUtils.allNotNull(cdrParam.getLimit())) {
//			cdrParam.setLimit(Const.DEFAULT_PAGE_SIZE_CDR_EXPORT);
//		}

		if (!StringUtils.isEmpty(cdrParam.getStarttime())) {
			cdrParam.setStarttime(cdrParam.getStarttime());
		} else {
			LocalDate now = LocalDate.now();
			String endTime = StringUtility.trimAllWhitespace(DateUtils.snagPatternStage(PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN, DateUtils.feedStageAsDate(now)).replace("-", "").replace(":", ""));
			String startTime = StringUtility.trimAllWhitespace(DateUtils.snagPatternStage(PatternEpochVariable.BIBLIOGRAPHY_EPOCH_PATTERN, DateUtils.feedStageAsDate(DateUtils.subtractDate(now, 10))).replace("-", "").replace(":", ""));
			cdrParam.setStarttime(startTime.concat("|").concat(endTime));
		}

		DBResponse<List<GetCdrResp>> listOfCDRs = clProductService.getCdr(SESSION_ID, cdrParam);
		List<GetCdrResponse> CDRsResponse = cdrService.findAllCdr(listOfCDRs.getResult());
		return StreamsResponseDTO.buildScrapeDocsResponse(cdrService.exportCDRsExcel(sheetName, CDRsResponse), filename, ".xlsx");
	}

	@PutMapping("/{leadId}/phone")
	public TMSResponse updatePhone(@PathVariable Integer leadId, UpdateLeadPhoneDto phoneDto) throws TMSException {
		GetLeadParamsV4 clFreshParams = new GetLeadParamsV4();
		clFreshParams.setLeadId(leadId);
		int orgId = getCurOrgId();
		clFreshParams.setOrgId(orgId);
		DBResponse<List<CLFresh>> dbResponse = freshService.getLeadV4(SESSION_ID, clFreshParams);
		if(dbResponse == null || dbResponse.getResult().isEmpty()){
			logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		}
		
		CLFresh clFresh = dbResponse.getResult().get(0);
		UpdLeadV5 updLead = new UpdLeadV5();
		updLead.setLeadId(leadId);
		String newPhones = null;
		String phone2 = null;
		if(phoneDto.getPhone() != null && StringUtils.hasText(phoneDto.getPhone())){
			String[] curPhones = clFresh.getPhone().split("\\|");
			if(phoneDto.getIndex() > curPhones.length || phoneDto.getIndex() < 0)
				throw new TMSException(ErrorMessage.INVALID_PARAM);
			else
				curPhones[phoneDto.getIndex()] = phoneDto.getPhone();
			newPhones = String.join("\\|", curPhones);
			updLead.setPhone(newPhones);
		}
		else if(phoneDto.getOtherPhone1() != null && StringUtils.hasText(phoneDto.getOtherPhone1())){
			String[] curPhones = (clFresh.getOtherPhone1() != null) ? clFresh.getOtherPhone1().split("\\|") : new String[] {""};
			if(phoneDto.getIndex() > curPhones.length || phoneDto.getIndex() < 0)
				throw new TMSException(ErrorMessage.INVALID_PARAM);
			else
				curPhones[phoneDto.getIndex()] = phoneDto.getOtherPhone1();
			phone2 = String.join("\\|", curPhones);
			updLead.setOtherPhone1(phone2);
		}else {
			return TMSResponse.buildResponse(false, 0, "error_update_phone", 400);
		}
		
		DBResponse result = logService.updLeadV5(SESSION_ID, updLead);

		// lead la callback thi update ca so dt o callback
		if(Helper.isCallback(clFresh.getLeadStatus())){
			UpdateCLCallback updateCLCallback = new UpdateCLCallback();
			updateCLCallback.setLeadId(leadId);
//            updateCLCallback.setOrgId(orgId);
			if(newPhones != null && StringUtils.hasText(newPhones)){
				updateCLCallback.setPhone(newPhones);
			}
			if(phone2 != null && StringUtils.hasText(phone2)){
				updateCLCallback.setOtherPhone1(phone2);
			}
			DBResponse result2 = logService.updateCallback(SESSION_ID, updateCLCallback);
		}
		if(result.getErrorCode() == 0)
			return TMSResponse.buildResponse(true, 1, "Success", 200);
		return TMSResponse.buildResponse(false, 0, "error_update_phone", 400);
	}

	@PostMapping("/reassign")
	public TMSResponse updLeadReassign(@RequestBody UpdLeadReassignDto updLeadReassignDto) throws TMSException {
		int userId = _curUser.getUserId();
		List<Integer> lstLeadId = updLeadReassignDto.getLeadIds();
		List<Integer> leadsSecures = new ArrayList<>();
		List<Integer> reassignLead = new ArrayList<>();
		String key = "";
		int orgId = getCurOrgId();
		for(int i = 0; i < lstLeadId.size(); i++){
			key = RedisHelper.createRedisKey(Const.REDIS_RECENT_LEAD, orgId, String.valueOf(lstLeadId.get(i)));
			if(stringRedisTemplate.hasKey(key))
				leadsSecures.add(lstLeadId.get(i));
			else
				reassignLead.add(lstLeadId.get(i));
		}

		if(reassignLead.size() <= 0)
			return TMSResponse.buildResponse(true, 0, "Lead is in processing by other agent", 400);

		String leadIds = reassignLead.toString();
		leadIds = leadIds.substring(1, leadIds.length() - 1);

		int agentId = updLeadReassignDto.getAgentId();
		UpdLeadReassignV3 updLeadReassign = new UpdLeadReassignV3();
		updLeadReassign.setAssigned(agentId);
		updLeadReassign.setLeadId(leadIds);
		updLeadReassign.setModifyby(userId);
		updLeadReassign.setCrmActionType(EnumType.CRM_ACTION_TYPE.ASSIGN_LEAD.getValue());
		updLeadReassign.setTeam(autoReloadConfigTeam.getConfigTeam(userId));
		updLeadReassign.setTeamSupervisor(autoReloadConfigTeam.getConfigTeamSupervisor(userId));

		if(updLeadReassignDto.getStatus() != null && updLeadReassignDto.getStatus() == 1)
			updLeadReassign.setLeadStatus(EnumType.LEAD_STATUS.URGENT.getValue());

		List<CLFresh> lstFresh = new ArrayList<>();
		// get current lead status
		List<Integer> callbackList = new ArrayList<>();
		GetLeadParamsV11 paramFresh = new GetLeadParamsV11();
		paramFresh.setOrgId(orgId);
		paramFresh.setLeadId(leadIds);
		DBResponse<List<CLFresh>> dbFresh = freshService.getLeadV11(SESSION_ID, paramFresh);
//		logger.info("get Fresh: " + dbFresh.getResult().size());
//		for(int i = 0; i < reassignLead.size(); i++){
//			paramFresh.setLeadId(reassignLead.get(i));
//
//			DBResponse<List<CLFresh>> dbFresh = freshService.getLeadV4(SESSION_ID, paramFresh);
//
//			// neu ko tim duoc lead thi bao request loi
//			if(dbFresh.getResult().isEmpty())
//				throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
//
//			lstFresh.add(dbFresh.getResult().get(0));
//			if(EnumType.LEAD_STATUS.isCallback(dbFresh.getResult().get(0).getLeadStatus()))
//				callbackList.add(reassignLead.get(i));
//
//		}
		if(dbFresh == null){
			logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
			throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
		} else {
			lstFresh = dbFresh.getResult();
		}

		for(CLFresh fresh: lstFresh) {
			if (fresh.getLeadStatus().intValue() == EnumType.LEAD_STATUS.APPROVED.getValue())
				return TMSResponse.buildResponse(true, 0, String.format("Lead %s is in approved!",fresh.getLeadId()), 409);
		}

		DBResponse dbResponse = logService.updLeadReassignV3(SESSION_ID, updLeadReassign);

		Map<Integer,CLFresh> map = new HashMap<>();

		for(CLFresh fresh: lstFresh) {
			RedisHelper.checkDuplicateAssignToAgent(stringRedisTemplate, fresh.getOrgId(), fresh.getProdName(),
					fresh.getName(), fresh.getPhone(), fresh.getLeadId(), agentId);
			if(EnumType.LEAD_STATUS.isCallback(fresh.getLeadStatus()))
				callbackList.add(fresh.getLeadId());
			fresh.setAssigned(updLeadReassign.getAssigned());
			fresh.setModifyby(userId);
			if (updLeadReassign.getLeadStatus() != null) {
				fresh.setLeadStatus(updLeadReassign.getLeadStatus());
			}
			map.put(fresh.getLeadId(), fresh);
		}

		if (!CollectionUtils.isEmpty(reassignLead)) {
			for (Integer leadIdItem : reassignLead) {
				CLFresh lead = new ModelMapper().map(map.get(leadIdItem), CLFresh.class);
				lead.setLeadId(leadIdItem);

				if(ObjectUtils.allNotNull(updLeadReassignDto.getStatus()) && updLeadReassignDto.getStatus() == 1) {
					String comment = String.format("Reassign Lead Id: %d to status: %s, previous status: %s", lead.getLeadId(),  "Urgent", lead.getLeadStatusName());
					dbLog.writeLeadStatusLog(_curUser.getUserId(), lead.getLeadId(), EnumType.LEAD_STATUS.URGENT.getValue(), comment);
				} else {
					String comment = String.format("Reassign Lead Id: %d with status: %s", lead.getLeadId(),  lead.getLeadStatusName());
					dbLog.writeLeadStatusLog(_curUser.getUserId(), lead.getLeadId(), lead.getLeadStatus(), comment);
				}
			}
		}

		dbLog.writeAgentActivityLog(userId, "Reassign Lead", "Reassign Lead", agentId, "assigned", leadIds);

		String messageDeletedCallbackFail = null;
		// If list of reassign has lead status callback, then delete it
		if(callbackList.size() > 0){
			if(updLeadReassignDto.getStatus() != null && updLeadReassignDto.getStatus() == 1){
				String callbackIds = callbackList.stream()
						.map(String::valueOf)
						.collect(Collectors.joining(","));
				messageDeletedCallbackFail = soService.deleteCallbackByLeadIds(callbackList);
				dbLog.writeAgentActivityLog(userId, "Delete callback - Reassign Lead", "Reassign Lead", agentId, "assigned",
						callbackIds);

				logger.info("[updLeadReassign] Delete multi callback " + userId);
				if (StringUtils.hasText(messageDeletedCallbackFail)) {
					logger.error("Fail delete callback {}", messageDeletedCallbackFail);
					return TMSResponse.buildResponse(messageDeletedCallbackFail, 0, "Fail delete callback " + messageDeletedCallbackFail, 400);
				}
			}
			else{
				UpdateCLCallback updateCLCallback = new UpdateCLCallback();
				updateCLCallback.setLeadId(Integer.parseInt(leadIds));
				updateCLCallback.setAssigned(agentId);
				updateCLCallback.setModifyby(userId);
				DBResponse resultUpdCallback = logService.updateCallback(SESSION_ID, updateCLCallback);

				logger.info("[updateCLCallback] Update callback {}", userId);
			}
		}

		if(leadsSecures.size() > 0){
			StringBuilder leadsBuilder = new StringBuilder();
			for (Integer leadId : leadsSecures) {
				leadsBuilder.append(leadId).append(" <br />");
			}

			logger.info("[LOCK-REASSIGN] {}", leadsBuilder);
			return TMSResponse.buildResponse(leadsSecures, leadsSecures.size(),
					leadsBuilder.append(" is in processing...").toString(), 200);
		}

		return TMSResponse.buildResponse(reassignLead, reassignLead.size());
	}

	@PutMapping("/manipulate")
	public TMSResponse manipulateLeads(@RequestBody ManipulateDto manipulateDto) throws TMSException {
		int userId = getCurrentUser().getUserId();
		int orgId = getCurOrgId();
		java.time.LocalDateTime now = java.time.LocalDateTime.now();
		StringBuilder message = new StringBuilder();
		String formatMessage = "%s : %s : %s <br />";

		List<Integer> manipulateIdList = manipulateDto.getLeadIds().stream().distinct()
				.collect(java.util.stream.Collectors.toList());

		for(Integer leadId: manipulateIdList){
			ClFresh clFresh = leadService.getById(leadId);
			String reason = "";
			boolean isSuccess = true;
			if(clFresh != null){
				ClManipulatedFresh manipulatedFresh = clManipulatedFreshService.getByLeadId(leadId);
				if(manipulatedFresh != null){
					isSuccess = false;
					reason = "Lead was manipulated!";
					message.append(String.format(formatMessage, leadId, isSuccess, reason));
					logger.info(message.toString());
				} else{
					sendToAgency(clFresh, orgId);
					ClManipulatedFresh clManipulatedFresh = new ClManipulatedFresh();
					clManipulatedFresh.setLeadId(clFresh.getLeadId());
					clManipulatedFresh.setUserId(userId);
					clManipulatedFresh.setManipulateDate(Timestamp.valueOf(now));
					clManipulatedFresh.setCreatedate(Timestamp.valueOf(now));
					clManipulatedFreshService.saveOrUpdate(clManipulatedFresh);
					message.append(String.format(formatMessage, leadId, isSuccess, reason));
					logger.info(message.toString());
				}
			} else{
				reason = "Lead not found!";
				isSuccess = false;
				message.append(String.format(formatMessage, leadId, isSuccess, reason));
				logger.info(message.toString());
			}
		}
		return TMSResponse.buildResponse(message.toString());

	}

	private void sendToAgency(ClFresh clFreshNew, int orgId) {
		if(clFreshNew.getAgcId() != null && clFreshNew.getClickId() != null){/// /neu la don cua Agency thi gui lai
																				/// postback
			String payout = "0", offerId = "0";
			if(clFreshNew.getPrice() != null)
				payout = clFreshNew.getPrice();
			if(clFreshNew.getOfferId() != null)
				offerId = clFreshNew.getOfferId();

			int typeQueue = -1;// reject

			typeQueue = 1;
			String note = "just want to refer";
			if(clFreshNew.getAgcId() == Const.AGENCY_ADFLEX)
				note = clFreshNew.getComment();

			int terms = 0;
			try{
				if(clFreshNew.getTerms() != null && !clFreshNew.getTerms().isEmpty())
					terms = Integer.parseInt(clFreshNew.getTerms());
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
			logger.info("[QUEUE] {} sent message {}", Const.QUEUE_AGENTCY, mes);

		}
	}
}
