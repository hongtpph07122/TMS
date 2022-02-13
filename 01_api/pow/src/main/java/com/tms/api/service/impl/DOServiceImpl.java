package com.tms.api.service.impl;

import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.api.dto.Request.QueryRequestDTO;
import com.tms.api.dto.Response.QueryResponseDTO;
import com.tms.api.dto.Response.ShippingExportResponseDTOV1;
import com.tms.api.dto.Response.ShippingResponseDTO;
import com.tms.api.dto.ResponseRescueFromDO;
import com.tms.api.dto.ResponseUpdateRescueByAction;
import com.tms.api.dto.ShippingDto;
import com.tms.api.dto.ShippingPendingRespDto;
import com.tms.api.entity.OdDONew;
import com.tms.api.entity.RcActionMapping;
import com.tms.api.entity.RcActivity;
import com.tms.api.entity.RcRescueJob;
import com.tms.api.enums.RescueTypeEnum;
import com.tms.api.entity.*;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.*;
import com.tms.api.repository.OdDONewRepository;
import com.tms.api.request.DeliveriesOrderRequest;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.DOService;
import com.tms.api.service.RescueService;
import com.tms.api.service.RescueSqlNativeService;
import com.tms.api.task.DBLogWriter;
import com.tms.api.utils.DateUtils;
import com.tms.api.utils.ObjectUtils;
import com.tms.api.utils.StringUtility;
import com.tms.dto.*;
import com.tms.entity.CLFresh;
import com.tms.entity.log.UpdDoNewV8;
import com.tms.model.Response.DeliveriesOrderResponseDTO;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.CLProductService;
import com.tms.service.impl.DeliveryOrderService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DOServiceImpl implements DOService {

	private static final Logger logger = LoggerFactory.getLogger(DOServiceImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	private final RescueService rescueService;
	private final OdDONewRepository odDONewRepository;
	private final DeliveryOrderService deliveryOrderService;
	private final DBLogWriter dbLogWriter;
	private final ModelMapper modelMapper;
	private final CLFreshService freshService;
	private final RescueSqlNativeService rescueSqlNativeService;
	private final CLProductService clProductService;
	private final LogService logService;

	@Value("${config.country}")
	public String _COUNTRY;

	@Autowired
	public DOServiceImpl(RescueService rescueService, OdDONewRepository odDONewRepository,
						 DeliveryOrderService deliveryOrderService, DBLogWriter dbLogWriter, ModelMapper modelMapper,
						 CLFreshService freshService, RescueSqlNativeService rescueSqlNativeService,
						 CLProductService clProductService,
						 LogService logService) {
		this.rescueService = rescueService;
		this.odDONewRepository = odDONewRepository;
		this.deliveryOrderService = deliveryOrderService;
		this.dbLogWriter = dbLogWriter;
		this.modelMapper = modelMapper;
		this.freshService = freshService;
		this.rescueSqlNativeService = rescueSqlNativeService;
		this.clProductService = clProductService;
		this.logService = logService;
	}

	private ResponseUpdateRescueByAction updateRescueByAction(List<OdDONew> odDONews, Integer action) {
		ResponseUpdateRescueByAction result = new ResponseUpdateRescueByAction();
		Date now = new Date();
		Timestamp nowTimestamp = new Timestamp(now.getTime());
		List<RcRescueJob> updates = new ArrayList<>();
		List<RcActivity> rcActivities = new ArrayList<>();
		for(OdDONew DO: odDONews){
			RcRescueJob rcRescueJob = new RcRescueJob();
			rcRescueJob.setId(DO.getRescue_id());
			rcRescueJob.setLastmile_reason(DO.getLastmile_reason());
			rcRescueJob.setLastmile_reason_detail(DO.getLastmile_reason_detail());
			rcRescueJob.setPriority(DO.getRcActionMapping().getPriority());
			rcRescueJob.setUpdatedate(nowTimestamp);
			rcRescueJob.setUpdateby(0);
			/* begin::Check Rescue  ID is existed on pre-delivery? */
			if (action == 1) {
				if (rescueSqlNativeService.existByIdWithPreDelivery(rcRescueJob.getId(), true)) {
					rcRescueJob.setPreDelivery(false);
					rcRescueJob.setJobType(RescueTypeEnum.ALL_RESCUE_TYPE.getJobType());
					RcActivity rcActivity = createActivity(DO, nowTimestamp, 1, true, DO.getRescue_id());
					rcActivities.add(rcActivity);
					logger.info("action 1: pre -> rescue: " + LogHelper.toJson(rcRescueJob));
				}
			}
			/* end::Check Rescue  ID is existed on pre-delivery? */
			if(action.intValue() == 2){
				rcRescueJob.setJob_status(7);
				rcRescueJob.setJob_sub_status(3);
				if (rescueSqlNativeService.existByIdWithPreDelivery(rcRescueJob.getId(), true)) {
					rcRescueJob.setPreDelivery(false);
					rcRescueJob.setJobType(RescueTypeEnum.ALL_RESCUE_TYPE.getJobType());
				}
				RcActivity rcActivity = createActivity(DO, nowTimestamp, 2, true, DO.getRescue_id());
				rcActivities.add(rcActivity);

			}
			if(action.intValue() == 3){
				rcRescueJob.setJob_status(7);
				rcRescueJob.setJob_sub_status(2);
				if (rescueSqlNativeService.existByIdWithPreDelivery(rcRescueJob.getId(), true)) {
					rcRescueJob.setPreDelivery(false);
					rcRescueJob.setJobType(RescueTypeEnum.ALL_RESCUE_TYPE.getJobType());
				}
				RcActivity rcActivity = createActivity(DO, nowTimestamp, 2, true, DO.getRescue_id());
				rcActivities.add(rcActivity);
			}
			updates.add(rcRescueJob);
//			RcActivity rcActivity = createActivity(DO, nowTimestamp, 1, true, DO.getRescue_id());
//			rcActivities.add(rcActivity);
		}
		result.setRcRescueJobs(updates);
		result.setRcActivities(rcActivities);

		return result;
	}

	private ResponseUpdateRescueByAction createNewRescue(List<OdDONew> odDONews) {
		ResponseUpdateRescueByAction result = new ResponseUpdateRescueByAction();
		Date now = new Date();
		Timestamp nowTimestamp = new Timestamp(now.getTime());
		List<RcRescueJob> creates = new ArrayList<>();
		List<OdDONew> updateDORcs = new ArrayList<>();
		HashMap<String, OdDONew> updateRcIds = new HashMap<>();
		List<RcActivity> rcActivities = new ArrayList<>();
		for(OdDONew DO: odDONews){
			RcRescueJob rcRescueJob = createRescue(DO, DO.getRcActionMapping(), nowTimestamp);
			creates.add(rcRescueJob);
			updateRcIds.put(DO.getTracking_code(), DO);
		}
		if(!creates.isEmpty()){
			List<RcRescueJob> rcRescueJobs = rescueService.saveOrUpdateRescueJob(creates);
			for(RcRescueJob rc: rcRescueJobs){
				OdDONew odDONew = updateRcIds.get(rc.getTracking_code());
				if(odDONew != null){
					RcActivity rcActivity = createActivity(odDONew, nowTimestamp, 1, false, rc.getId());
					rcActivities.add(rcActivity);
					odDONew.setRescue_id(rc.getId());
					updateDORcs.add(odDONew);
				}
			}
			rescueService.saveOrUpdateDO(updateDORcs);
		}
		result.setRcRescueJobs(creates);
		result.setRcActivities(rcActivities);
		return result;

	}

	private RcRescueJob createRescue(OdDONew DO, RcActionMapping rcActionMapping, Timestamp nowTimestamp) {
		RcRescueJob rcRescueJob = new RcRescueJob();
		rcRescueJob.setLastmile_reason(rcActionMapping.getStatus_name());
		rcRescueJob.setLastmile_reason_detail(rcActionMapping.getSub_status_name());
		rcRescueJob.setPriority(rcActionMapping.getPriority());
		rcRescueJob.setOrg_id(DO.getOrg_id());
		rcRescueJob.setTracking_code(DO.getTracking_code());
		rcRescueJob.setDo_id(DO.getDo_id());
		rcRescueJob.setDo_code(DO.getDo_code());
		rcRescueJob.setAmountcod(DO.getAmountcod());
		rcRescueJob.setCustomer_address(DO.getCustomer_address());
		rcRescueJob.setCustomer_id(DO.getCustomer_id());
		rcRescueJob.setCustomer_name(DO.getCustomer_name());
		rcRescueJob.setCreateby(DO.getCreateby());
		rcRescueJob.setFfm_code(DO.getFfm_code());
		rcRescueJob.setFfm_id(DO.getFfm_id());
		rcRescueJob.setJob_status(1);
		rcRescueJob.setSo_id(DO.getSo_id());
		rcRescueJob.setLastmile_id(DO.getCarrier_id());
		rcRescueJob.setCustomer_phone(DO.getCustomer_phone());
		rcRescueJob.setPackage_products(DO.getPackage_name());
		rcRescueJob.setDo_status(DO.getStatus());
		rcRescueJob.setCreateby(0);
		rcRescueJob.setUpdatedate(nowTimestamp);
		rcRescueJob.setCreatedate(nowTimestamp);
		rcRescueJob.setPreDelivery(false);
		rcRescueJob.setPreDeliveredBefore(false);
		rcRescueJob.setJobType(RescueTypeEnum.ALL_RESCUE_TYPE.getJobType());
		return rcRescueJob;
	}

	private RcActivity createActivity(OdDONew DO, Timestamp nowTimestamp, Integer action, boolean rescue,
			Integer rescueId) {
		RcActivity rcActivity = new RcActivity();
		rcActivity.setActivity_type(3);
		rcActivity.setUpdateby(0);
		rcActivity.setAct_time(nowTimestamp);
		rcActivity.setLastmile_reason(DO.getLastmile_reason());
		rcActivity.setLastmile_reason_detail(DO.getLastmile_reason_detail());
		if(action == 1 && rescue == false){
			rcActivity.setRc_job_id(rescueId);
			rcActivity.setComment("System create rescue job");
			rcActivity.setNew_status(1);
			return rcActivity;
		} else if(action == 1 && rescue == true){
			rcActivity.setRc_job_id(rescueId);
			rcActivity.setComment("System update pre-delivery to rescue!");
			return rcActivity;
		} else if(action == 2){
			rcActivity.setRc_job_id(rescueId);
			rcActivity.setComment("System close rescue job");
			rcActivity.setNew_status(7);
			return rcActivity;
		}
		return null;
	}

	@Override
	public ResponseRescueFromDO createAndUpdateRescue() {
		ResponseRescueFromDO result = new ResponseRescueFromDO();
		try{
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			Calendar cal = Calendar.getInstance();
			cal.setTime(ts);
			cal.add(Calendar.MONTH, -2);
			ts.setTime(cal.getTime().getTime());
			ts = new Timestamp(cal.getTime().getTime());

			List<OdDONew> getDos = odDONewRepository.getAllDOByActionBackTo2Month(ts);
			logger.info("getDos: {}", getDos.size());
			List<RcRescueJob> updates = new ArrayList<>();
			List<RcRescueJob> creates = new ArrayList<>();
			List<RcActivity> rcActivities = new ArrayList<>();
			List<String> tracking_update = new ArrayList<>();
			for(OdDONew DO: getDos)
				tracking_update.add(DO.getTracking_code());
			logger.info("UPDATE STATUS IN DO DONE");
			List<OdDONew> odDONews = rescueService.getDOByAction(tracking_update, 1);
			if(odDONews == null)
				logger.info("Action = 1, rescue not null, NULL");
			else{
				ResponseUpdateRescueByAction responseUpdateRescueByAction = updateRescueByAction(odDONews, 1);
				updates.addAll(responseUpdateRescueByAction.getRcRescueJobs());
				rcActivities.addAll(responseUpdateRescueByAction.getRcActivities());
			}

			List<OdDONew> odDONews2 = rescueService.getDOByAction(tracking_update, 2);
			if(odDONews2 == null)
				logger.info("Action = 2, null");
			else{
				ResponseUpdateRescueByAction responseUpdateRescueByAction = updateRescueByAction(odDONews2, 2);
				updates.addAll(responseUpdateRescueByAction.getRcRescueJobs());
				rcActivities.addAll(responseUpdateRescueByAction.getRcActivities());
			}

			List<OdDONew> odDONews3 = rescueService.getDOByAction(tracking_update, 3);
			if(odDONews3 == null)
				logger.info("Action = 3, null");
			else{
				ResponseUpdateRescueByAction responseUpdateRescueByAction = updateRescueByAction(odDONews3, 3);
				updates.addAll(responseUpdateRescueByAction.getRcRescueJobs());
				rcActivities.addAll(responseUpdateRescueByAction.getRcActivities());
			}

			List<OdDONew> odDONews1_null = rescueService.getDOByRescueNull(tracking_update, 1);
			if(odDONews1_null == null)
				logger.info("None to create");
			else{
				ResponseUpdateRescueByAction responseUpdateRescueByAction = createNewRescue(odDONews1_null);
				rcActivities.addAll(responseUpdateRescueByAction.getRcActivities());
				creates.addAll(responseUpdateRescueByAction.getRcRescueJobs());
			}

			rescueService.bactchUpdateRescue(updates);
			rescueService.saveOrUpdateActivity(rcActivities);
			logger.info("IMPORT RESCUE IS DONE!");

			result.setCreated(creates.size());
			result.setImported(getDos.size());
			result.setUpdated(updates.size());

		} catch(Exception ex){
			logger.error("Error create and update rescue {}", ex.getMessage(), ex);
			return null;
		}
		return result;

	}

	@Override
	public byte[] exportCSVShipping(GetDoNewV2 paramsDo, OrderManagementRequestDTO orderManagementRequestDTO) {
		final String ACTIVITY = "Export Shipping";
		final String OBJECT_TYPE = "ExportShipping";
		final String ON_FIELD = "getShippingExportCSV";
		List<GetShippingExportRespV1> shippingExportRespList = new ArrayList<>();
		GetShippingExportV1 shippingExportParams = new GetShippingExportV1();
		shippingExportParams.setOrgId(orderManagementRequestDTO.getOrgId());

		if(!ObjectUtils.isNull(paramsDo.getLimit()))
			shippingExportParams.setLimit(paramsDo.getLimit());

		if(!ObjectUtils.isNull(paramsDo.getCreatedate()))
			shippingExportParams.setCreatedate(paramsDo.getCreatedate());
		else{
			java.time.LocalDateTime beforeDate = java.time.LocalDateTime.now().minusMonths(1);
			java.time.LocalDateTime monthBefore = java.time.LocalDateTime.of(beforeDate.getYear(),
					beforeDate.getMonth(), 1, 0, 0, 0);
			java.time.LocalDateTime now = java.time.LocalDateTime.now();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern("yyyyMMddHHmmss");
			shippingExportParams.setCreatedate(monthBefore.format(formatter) + "|" + now.format(formatter));
		}

		if(!ObjectUtils.isNull(paramsDo.getSoId()))
			shippingExportParams.setSaleOrder(String.valueOf(paramsDo.getSoId()));

		if(!ObjectUtils.isNull(paramsDo.getCustomerName()))
			shippingExportParams.setCustomerName(paramsDo.getCustomerName());

		if(!ObjectUtils.isNull(paramsDo.getCustomerPhone()))
			shippingExportParams.setCustomerPhone(paramsDo.getCustomerPhone());

		if(!ObjectUtils.isNull(paramsDo.getCustomerAddress()))
			shippingExportParams.setAddress(paramsDo.getCustomerAddress());

		if(!ObjectUtils.isNull(paramsDo.getCarrierName()))
			shippingExportParams.setCarrier(paramsDo.getCarrierName());

		if(!ObjectUtils.isNull(paramsDo.getWarehouseName()))
			shippingExportParams.setWarehouse(paramsDo.getWarehouseName());

		if(!ObjectUtils.isNull(paramsDo.getDoId()))
			shippingExportParams.setDeliveryCode(String.valueOf(paramsDo.getDoId()));

		if(!ObjectUtils.isNull(paramsDo.getStatus()))
			shippingExportParams.setStatusTransport(String.valueOf(paramsDo.getStatus()));

		if (!ObjectUtils.isNull(paramsDo.getLastmileReturnCode())) {
			shippingExportParams.setLastmileReturnCode(paramsDo.getLastmileReturnCode());
		}
        //DBResponse<List<GetShippingExportResp>> listDBResponse = deliveryOrderService.getShippingExportCSV(orderManagementRequestDTO.getSessionId(), shippingExportParams);
		List<GetShippingExportRespV1> listShippingResp = findAllShippingBaseOnGeo(shippingExportParams);

		if(!CollectionUtils.isEmpty(listShippingResp)){
			shippingExportRespList = listShippingResp;
			dbLogWriter.writeAgentActivityLog(orderManagementRequestDTO.getUserId(), ACTIVITY, OBJECT_TYPE,
					shippingExportRespList.size(), ON_FIELD, LogHelper.toJson(shippingExportParams));
		}

		try{
			return ExcelHelper.createExcelData(
					ShippingExportResponseDTOV1.buildMappedToDTO(shippingExportRespList, orderManagementRequestDTO),
					ShippingExportResponseDTOV1.class);
		} catch(TMSException error){
			logger.error(error.getMessage());
			return new byte[0];
		}
	}

	@SuppressWarnings("unchecked")
	private List<GetShippingExportResp> findAllShippingBaseOnGeo(GetShippingExport params) {
		List<GetShippingExportResp> result = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		builder.append(
				" select a.so_id as sale_order, a.do_code as delivery_code, a.customer_name, a.customer_phone, coalesce(j.shortname,i.agc_code) as source, ");
		builder.append(
				" h.user_name as user_name, regexp_replace(i.address, E'[\\n\\r]+', ' ', 'g' ) as address, ");

		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			builder.append(
					" t.name as postal_code, u.code as neighborhood, n.name as wards, m.name as district, l.name as province,  ");
		} else {
			builder.append(
					" u.code as neighborhood, n.name as wards, m.name as district, l.name as province,  ");
		}

		builder.append(
				" e.shortname as carrier,regexp_replace(i.comment, E'[\\n\\r]+', ' ', 'g' ) as comment, p.name as status, o.name as SO_status, d.name as status_transport, ");
		builder.append(
				" a.createdate as createdate, updatedate, i.total_call, case  f.payment_method when 1 then 'COD' when 2 then 'Bank tranfer' end as payment_method , ");
		builder.append(
				" f.amount, product1, qty1, price1,product2, qty2, price2, product3, qty3, price3, product4, qty4, price4, ");
		builder.append(
				" a.customer_id, a.ffm_code, a.tracking_code, b.shortname as FFM_name, a.org_id, s.warehouse_shortname as warehouse, i.agent_note, i.affiliate_id, a.lastmile_return_code ");
		builder.append(" from od_do_new a ");
		builder.append(" left join bp_partner b on a.ffm_id = b.pn_id ");
		builder.append(" left join bp_warehouse c on a.warehouse_id = c.warehouse_id ");
		builder.append(
				" left join (select * from cf_synonym where type = 'delivery order status')d on a.status = d.value ");
		builder.append(" left join bp_partner e on e.pn_id = a.carrier_id ");
		builder.append(" left join od_sale_order f on f.so_id = a.so_id ");
		builder.append(" left join or_user h on f.ag_id = h.user_id ");
		builder.append(" left join cl_fresh i on i.lead_id = a.customer_id ");
		builder.append(" left join bp_partner j on j.pn_id = i.agc_id ");
		builder.append(" left join lc_province l on l.prv_id = cast(i.province as integer) ");
		builder.append(" left join lc_district m on m.dt_id = cast(i.district as integer) ");
		builder.append(" left join lc_subdistrict n on n.sdt_id = cast(i.subdistrict as integer) ");
		builder.append(
				" left join (select * from cf_synonym where type = 'sale order status')o on f.status = o.value ");
		builder.append(
				" left join (select * from cf_synonym where type = 'lead status')p on i.lead_status = p.value ");
		builder.append(" left join (select so_id, ");
		builder.append(" max((case when item_no = 1 then b.name  end)) as product1, ");
		builder.append(" max((case when item_no = 1 then quantity  end)) as Qty1, ");
		builder.append(" max((case when item_no = 1 then a.price  end)) as Price1, ");
		builder.append(" max((case when item_no = 2 then b.name  end)) as product2, ");
		builder.append(" max((case when item_no = 2 then quantity  end)) as Qty2, ");
		builder.append(" max((case when item_no = 2 then a.price  end)) as Price2, ");
		builder.append(" max((case when item_no = 3 then b.name  end)) as product3, ");
		builder.append(" max((case when item_no = 3 then quantity  end)) as Qty3, ");
		builder.append(" max((case when item_no = 3 then a.price  end)) as Price3, ");
		builder.append(" max((case when item_no = 4 then b.name  end)) as product4, ");
		builder.append(" max((case when item_no = 4 then quantity  end)) as Qty4, ");
		builder.append(" max((case when item_no = 4 then a.price  end)) as Price4 ");
		builder.append(" from od_so_item a join pd_product b on a.prod_id=b.prod_id ");
		builder.append(" group by so_id order by so_id desc) q on q.so_id=f.so_id ");
		builder.append(" left join bp_warehouse s on s.warehouse_id = a.warehouse_id ");
		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			builder.append(" left join lc_neighborhood t on t.id = cast(i.neighborhood as integer) ");
		}
		builder.append(" left join lc_postal_code u on cast(u.id as text) = i.postal_code ");
		builder.append(" where a.error_message is null ");

		if (!StringUtils.isEmpty(params.getSaleOrder()))
			builder.append(" and a.so_id in :soId ");
		if (!StringUtils.isEmpty(params.getDeliveryCode()))
			builder.append(" and lower(a.do_code) like :doCode ");
		if (!StringUtils.isEmpty(params.getCustomerName()))
			builder.append(" and lower(a.customer_name) like :customerName ");
		if (!StringUtils.isEmpty(params.getCustomerPhone()))
			builder.append(" and lower(a.customer_phone) like :customerPhone ");
		if (!StringUtils.isEmpty(params.getSource()))
			builder.append(" and lower(coalesce(j.shortname,i.agc_code)) like :source ");
		if (!StringUtils.isEmpty(params.getUserName()))
			builder.append(" and lower(h.user_name) like :userName ");
		if (!StringUtils.isEmpty(params.getAddress()))
			builder.append(" and lower(regexp_replace(i.address, E'[\\n\\r]+', ' ', 'g' )) like :address ");
		if (!StringUtils.isEmpty(params.getWards()))
			builder.append(" and lower(n.name) like :ward ");
		if (!StringUtils.isEmpty(params.getDistrict()))
			builder.append(" and lower(m.name) like :district ");
		if (!StringUtils.isEmpty(params.getProvince()))
			builder.append(" and lower(l.name) like :province ");
		if (!StringUtils.isEmpty(params.getCarrier()))
			builder.append(" and lower(e.shortname) like :carrier ");
		if (!StringUtils.isEmpty(params.getComment()))
			builder.append(" and lower(regexp_replace(i.comment, E'[\\n\\r]+', ' ', 'g' )) like :comment ");
		if (!StringUtils.isEmpty(params.getStatus()))
			builder.append(" and cast(i.lead_status as text) = :leadStatus ");
		if (!StringUtils.isEmpty(params.getSoStatus()))
			builder.append(" and cast(f.status as text) = :soStatus ");
		if (!StringUtils.isEmpty(params.getStatusTransport()))
			builder.append(" and cast(a.status as text) = :statusTransport ");
		if (!StringUtils.isEmpty(params.getCreatedate()))
			builder.append(
					" and a.createdate  >= to_timestamp(split_part(:createdate,'|',1),'yyyymmddhh24miss') and a.createdate  <= to_timestamp(split_part(:createdate,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getUpdatedate()))
			builder.append(
					" and a.updatedate  >= to_timestamp(split_part(:updatedate,'|',1),'yyyymmddhh24miss') and a.updatedate  <= to_timestamp(split_part(:updatedate,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getTotalCall()))
			builder.append(" and i.total_call = :totalCall ");
		if (!StringUtils.isEmpty(params.getPaymentMethod()))
			builder.append(" and f.payment_method = :paymentMethod ");
		if (!StringUtils.isEmpty(params.getAmount()))
			builder.append(" and f.amount = :amount ");
		if (!StringUtils.isEmpty(params.getProduct1()))
			builder.append(" and product1 like :product1 ");
		if (!StringUtils.isEmpty(params.getQty1()))
			builder.append(" and Qty1 = :qty1");
		if (!StringUtils.isEmpty(params.getPrice1()))
			builder.append(" and Price1 = :price1 ");
		if (!StringUtils.isEmpty(params.getProduct2()))
			builder.append(" and product2 like :product2 ");
		if (!StringUtils.isEmpty(params.getQty2()))
			builder.append(" and Qty2 = :qty2");
		if (!StringUtils.isEmpty(params.getPrice2()))
			builder.append(" and Price2 = :price2 ");
		if (!StringUtils.isEmpty(params.getProduct3()))
			builder.append(" and product3 like :product3 ");
		if (!StringUtils.isEmpty(params.getQty3()))
			builder.append(" and Qty3 = :qty3");
		if (!StringUtils.isEmpty(params.getPrice3()))
			builder.append(" and Price3 = :price3 ");
		if (!StringUtils.isEmpty(params.getLeadId()))
			builder.append(" and a.customer_id in :leadId ");
		if (!StringUtils.isEmpty(params.getFfmCode()))
			builder.append(" and lower(a.ffm_code) like :ffmCode ");
		if (!StringUtils.isEmpty(params.getTrackingCode()))
			builder.append(" and lower(a.tracking_code) like :trackingCode ");
		if (!StringUtils.isEmpty(params.getFfmName()))
			builder.append(" and lower(b.shortname) like :ffmName ");
		if (!StringUtils.isEmpty(params.getOrgId()))
			builder.append(" and a.org_id = :orgId ");
		if (!StringUtils.isEmpty(params.getWarehouse()))
			builder.append(" and lower(s.warehouse_shortname) like :warehouse ");
		if (!StringUtils.isEmpty((params.getLastmileReturnCode())))
			builder.append(" and lower(a.lastmile_return_code) like :lastmileReturnCode ");

		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			if (!StringUtils.isEmpty(params.getNeighborhood())) {
				builder.append(" and lower(t.name) like :neighborhood ");
			}
		}

		if (!StringUtils.isEmpty(params.getPostalCode())) {
			builder.append(" and u.code = :postalCode ");
		}
		builder.append(" order by a.createdate  desc ");

		if (!StringUtils.isEmpty(params.getLimit())) {
			builder.append(" limit :limit");
		}
		if (!StringUtils.isEmpty(params.getOffset())) {
			builder.append(" offset :offset ");
		}

		/* set parameters */
		Query query = entityManager.createNativeQuery(builder.toString());
		if (!StringUtils.isEmpty(params.getSaleOrder()))
			query.setParameter("soId", params.getSaleOrder());
		if (!StringUtils.isEmpty(params.getDeliveryCode()))
			query.setParameter("doCode", "%" + params.getDeliveryCode().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getCustomerName()))
			query.setParameter("customerName", "%" + params.getCustomerName().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getCustomerPhone()))
			query.setParameter("customerPhone", "%" + params.getCustomerPhone().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getSource()))
			query.setParameter("source", "%" + params.getSource().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getUserName()))
			query.setParameter("userName", "%" + params.getUserName().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getAddress()))
			query.setParameter("address", "%" + params.getAddress().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getWards()))
			query.setParameter("ward", "%" + params.getWards().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getDistrict()))
			query.setParameter("district", "%" + params.getDistrict().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getProvince()))
			query.setParameter("province", "%" + params.getProvince().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getCarrier())) {
			builder.append(" and lower(e.shortname) like :carrier ");
			query.setParameter("carrier", "%" + params.getCarrier().toLowerCase() + "%");
		}
		if (!StringUtils.isEmpty(params.getComment()))
			query.setParameter("comment", "%" + params.getComment().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getStatus()))
			query.setParameter("leadStatus", params.getStatus());
		if (!StringUtils.isEmpty(params.getSoStatus()))
			query.setParameter("soStatus", params.getSoStatus());
		if (!StringUtils.isEmpty(params.getStatusTransport()))
			query.setParameter("statusTransport", params.getStatusTransport());
		if (!StringUtils.isEmpty(params.getCreatedate()))
			query.setParameter("createdate", params.getCreatedate());
		if (!StringUtils.isEmpty(params.getUpdatedate()))
			query.setParameter("updatedate", params.getUpdatedate());
		if (!StringUtils.isEmpty(params.getTotalCall()))
			query.setParameter("totalCall", params.getTotalCall());
		if (!StringUtils.isEmpty(params.getPaymentMethod()))
			query.setParameter("paymentMethod", params.getPaymentMethod());
		if (!StringUtils.isEmpty(params.getAmount()))
			query.setParameter("amount", params.getAmount());
		if (!StringUtils.isEmpty(params.getProduct1()))
			query.setParameter("product1", "%" + params.getProduct1() + "%");
		if (!StringUtils.isEmpty(params.getQty1()))
			query.setParameter("qty1", params.getQty1());
		if (!StringUtils.isEmpty(params.getPrice1()))
			query.setParameter("price1", params.getPrice1());
		if (!StringUtils.isEmpty(params.getProduct2()))
			query.setParameter("product2", "%" + params.getProduct2() + "%");
		if (!StringUtils.isEmpty(params.getQty2()))
			query.setParameter("qty2", params.getQty2());
		if (!StringUtils.isEmpty(params.getPrice2()))
			query.setParameter("price2", params.getPrice2());
		if (!StringUtils.isEmpty(params.getProduct3()))
			query.setParameter("product3", "%" + params.getProduct3() + "%");
		if (!StringUtils.isEmpty(params.getQty3()))
			query.setParameter("qty3", params.getQty3());
		if (!StringUtils.isEmpty(params.getPrice3()))
			query.setParameter("price3", params.getPrice3());
		if (!StringUtils.isEmpty(params.getLeadId())) {
			List<String> leadIds = Arrays.asList(params.getLeadId().split(","));
			query.setParameter("leadId", leadIds);
		}
		if (!StringUtils.isEmpty(params.getFfmCode()))
			query.setParameter("ffmCode", "%" + params.getFfmCode().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getTrackingCode()))
			query.setParameter("trackingCode", "%" + params.getTrackingCode().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getFfmName()))
			query.setParameter("ffmName", "%" + params.getFfmName().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getOrgId()))
			query.setParameter("orgId", params.getOrgId());
		if (!StringUtils.isEmpty(params.getWarehouse()))
			query.setParameter("warehouse", "%" + params.getWarehouse().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getLastmileReturnCode()))
			query.setParameter("lastmileReturnCode", "%" + params.getLastmileReturnCode().toLowerCase() + "%");

		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			if (!StringUtils.isEmpty(params.getNeighborhood())) {
				query.setParameter("neighborhood", "%" + params.getNeighborhood().toLowerCase() + "%");
			}
		}

		if (!StringUtils.isEmpty(params.getPostalCode()))
			query.setParameter("postalCode", params.getPostalCode());
		if (!StringUtils.isEmpty(params.getLimit()))
			query.setParameter("limit", params.getLimit());
		if (!StringUtils.isEmpty(params.getOffset()))
			query.setParameter("offset", params.getOffset());

		/* set response body */
		List<Object[]> data = query.getResultList();
		for (Object[] row : data) {
			GetShippingExportResp shipping = new GetShippingExportResp();
			logger.info(data.toString());
			if (row[0] != null) {
				Integer soId = (Integer) row[0];
				shipping.setSaleOrder(soId.toString());
			}
			if (row[1] != null) {
				shipping.setDeliveryCode((String) row[1]);
			}
			if (row[2] != null) {
				shipping.setCustomerName((String) row[2]);
			}
			if (row[3] != null) {
				shipping.setCustomerPhone((String) row[3]);
			}
			if (row[4] != null) {
				shipping.setSource((String) row[4]);
			}
			if (row[5] != null) {
				shipping.setUserName((String) row[5]);
			}
			if (row[6] != null) {
				shipping.setAddress((String) row[6]);
			}

			if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
				if (row[7] != null) {
					shipping.setPostalCode((String) row[7]);
				}
				if (row[8] != null) {
					shipping.setNeighborhood((String) row[8]);
				}
				if (row[9] != null) {
					shipping.setWards((String) row[9]);
				}
				if (row[10] != null) {
					shipping.setDistrict((String) row[10]);
				}
				if (row[11] != null) {
					shipping.setProvince((String) row[11]);
				}
				if (row[12] != null) {
					shipping.setCarrier((String) row[12]);
				}
				if (row[13] != null) {
					shipping.setComment((String) row[13]);
				}
				if (row[14] != null) {
					shipping.setStatus((String) row[14]);
				}
				if (row[15] != null) {
					shipping.setSoStatus((String) row[15]);
				}
				if (row[16] != null) {
					shipping.setStatusTransport((String) row[16]);
				}
				if (row[17] != null) {
					shipping.setCreatedate((Date) row[17]);
				}
				if (row[18] != null) {
					shipping.setUpdatedate((Date) row[18]);
				}
				if (row[19] != null) {
					shipping.setTotalCall((Integer) row[19]);
				}
				if (row[20] != null) {
					shipping.setPaymentMethod((String) row[20]);
				}
				if (row[21] != null) {
					BigDecimal amount = (BigDecimal) row[21];
					shipping.setAmount(amount.doubleValue());
				}
				if (row[22] != null) {
					shipping.setProduct1((String) row[22]);
				}
				if (row[23] != null) {
					Integer qty1 = (Integer) row[23];
					shipping.setQty1(qty1.doubleValue());
				}
				if (row[24] != null) {
					Integer price1 = (Integer) row[24];
					shipping.setPrice1(price1.doubleValue());
				}
				if (row[25] != null) {
					shipping.setProduct2((String) row[25]);
				}
				if (row[26] != null) {
					Integer qty2 = (Integer) row[26];
					shipping.setQty2(qty2.doubleValue());
				}
				if (row[27] != null) {
					Integer price2 = (Integer) row[27];
					shipping.setPrice2(price2.doubleValue());
				}
				if (row[28] != null) {
					shipping.setProduct3((String) row[28]);
				}
				if (row[29] != null) {
					Integer qty3 = (Integer) row[29];
					shipping.setQty3(qty3.doubleValue());
				}
				if (row[30] != null) {
					Integer price3 = (Integer) row[30];
					shipping.setPrice3(price3.doubleValue());
				}
				if (row[31] != null) {
					shipping.setProduct4((String) row[31]);
				}
				if (row[32] != null) {
					Integer qty4 = (Integer) row[32];
					shipping.setQty4(qty4.doubleValue());
				}
				if (row[33] != null) {
					Integer price4 = (Integer) row[33];
					shipping.setPrice4(price4.doubleValue());
				}
				if (row[34] != null) {
					shipping.setLeadId((Integer) row[34]);
				}
				if (row[35] != null) {
					shipping.setFfmCode((String) row[35]);
				}
				if (row[36] != null) {
					shipping.setTrackingCode((String) row[36]);
				}
				if (row[37] != null) {
					shipping.setFfmName((String) row[37]);
				}
				if (row[38] != null) {
					shipping.setOrgId((Integer) row[38]);
				}
				if (row[39] != null) {
					shipping.setWarehouse((String) row[39]);
				}
				if (row[40] != null) {
					shipping.setAgentNote((String) row[40]);
				}
				if (row[41] != null) {
					shipping.setAffiliateId((String) row[41]);
				}
				String lastmileReturnCode = (String) row[42];
				if (!StringUtils.isEmpty(lastmileReturnCode) && !lastmileReturnCode.toLowerCase().equals("null"))
					shipping.setLastmileReturnCode(lastmileReturnCode);
			} else {
				if (row[7] != null) {
					shipping.setNeighborhood((String) row[7]);
				}
				if (row[8] != null) {
					shipping.setWards((String) row[8]);
				}
				if (row[9] != null) {
					shipping.setDistrict((String) row[9]);
				}
				if (row[10] != null) {
					shipping.setProvince((String) row[10]);
				}
				if (row[11] != null) {
					shipping.setCarrier((String) row[11]);
				}
				if (row[12] != null) {
					shipping.setComment((String) row[12]);
				}
				if (row[13] != null) {
					shipping.setStatus((String) row[13]);
				}
				if (row[14] != null) {
					shipping.setSoStatus((String) row[14]);
				}
				if (row[15] != null) {
					shipping.setStatusTransport((String) row[15]);
				}
				if (row[16] != null) {
					shipping.setCreatedate((Date) row[16]);
				}
				if (row[17] != null) {
					shipping.setUpdatedate((Date) row[17]);
				}
				if (row[18] != null) {
					shipping.setTotalCall((Integer) row[18]);
				}
				if (row[19] != null) {
					shipping.setPaymentMethod((String) row[19]);
				}
				if (row[20] != null) {
					BigDecimal amount = (BigDecimal) row[20];
					shipping.setAmount(amount.doubleValue());
				}
				if (row[21] != null) {
					shipping.setProduct1((String) row[21]);
				}
				if (row[22] != null) {
					Integer qty1 = (Integer) row[22];
					shipping.setQty1(qty1.doubleValue());
				}
				if (row[23] != null) {
					Integer price1 = (Integer) row[23];
					shipping.setPrice1(price1.doubleValue());
				}
				if (row[24] != null) {
					shipping.setProduct2((String) row[24]);
				}
				if (row[25] != null) {
					Integer qty2 = (Integer) row[25];
					shipping.setQty2(qty2.doubleValue());
				}
				if (row[26] != null) {
					Integer price2 = (Integer) row[26];
					shipping.setPrice2(price2.doubleValue());
				}
				if (row[27] != null) {
					shipping.setProduct3((String) row[27]);
				}
				if (row[28] != null) {
					Integer qty3 = (Integer) row[28];
					shipping.setQty3(qty3.doubleValue());
				}
				if (row[29] != null) {
					Integer price3 = (Integer) row[29];
					shipping.setPrice3(price3.doubleValue());
				}
				if (row[30] != null) {
					shipping.setProduct4((String) row[30]);
				}
				if (row[31] != null) {
					Integer qty4 = (Integer) row[31];
					shipping.setQty4(qty4.doubleValue());
				}
				if (row[32] != null) {
					Integer price4 = (Integer) row[32];
					shipping.setPrice4(price4.doubleValue());
				}
				if (row[33] != null) {
					shipping.setLeadId((Integer) row[33]);
				}
				if (row[34] != null) {
					shipping.setFfmCode((String) row[34]);
				}
				if (row[35] != null) {
					shipping.setTrackingCode((String) row[35]);
				}
				if (row[36] != null) {
					shipping.setFfmName((String) row[36]);
				}
				if (row[37] != null) {
					shipping.setOrgId((Integer) row[37]);
				}
				if (row[38] != null) {
					shipping.setWarehouse((String) row[38]);
				}
				if (row[39] != null) {
					shipping.setAgentNote((String) row[39]);
				}
				if (row[40] != null) {
					shipping.setAffiliateId((String) row[40]);
				}

				String lastmileReturnCode = (String) row[41];
				if (!StringUtils.isEmpty(lastmileReturnCode) && !lastmileReturnCode.toLowerCase().equals("null"))
					shipping.setLastmileReturnCode(lastmileReturnCode);
			}

			result.add(shipping);
		}

		return result;
	}

	private List<GetShippingExportRespV1> findAllShippingBaseOnGeo(GetShippingExportV1 params) {
		List<GetShippingExportRespV1> result = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		builder.append(
				" select a.so_id as sale_order, a.do_code as delivery_code, a.customer_name, a.customer_phone, coalesce(j.shortname,i.agc_code) as source, ");
		builder.append(
				" h.user_name as user_name, regexp_replace(i.address, E'[\\n\\r]+', ' ', 'g' ) as address, ");

		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			builder.append(
					" t.name as postal_code, u.code as neighborhood, n.name as wards, m.name as district, l.name as province,  ");
		} else {
			builder.append(
					" u.code as neighborhood, n.name as wards, m.name as district, l.name as province,  ");
		}

		builder.append(
				" e.shortname as carrier,regexp_replace(i.comment, E'[\\n\\r]+', ' ', 'g' ) as comment, p.name as status, o.name as SO_status, d.name as status_transport, ");
		builder.append(
				" a.createdate as createdate, updatedate, i.total_call, case  f.payment_method when 1 then 'COD' when 2 then 'Bank tranfer' end as payment_method , ");
		builder.append(
				" f.amount, product1, qty1, price1,product2, qty2, price2, product3, qty3, price3, product4, qty4, price4, ");
		builder.append(
				" a.customer_id, a.ffm_code, a.tracking_code, b.shortname as FFM_name, a.org_id, s.warehouse_shortname as warehouse, i.agent_note, i.affiliate_id, a.lastmile_return_code,");
		builder.append(" a.picked_up_date, a.attemp, k.name, a.paid_cod_date, a.firstdelivertime, a.second_delivery_time, a.third_delivery_time, a.returntime, a.closetime, a.picked_time, a.packed_time, a.handovered_time,  cp.\"name\" as cp_name, cate.\"name\" as category ");
		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			builder.append(", i.neighborhood as neighborhood_id, i.subdistrict as wards_id ");
		}
		builder.append(" from od_do_new a ");
		builder.append(" left join bp_partner b on a.ffm_id = b.pn_id ");
		builder.append(" left join bp_warehouse c on a.warehouse_id = c.warehouse_id ");
		builder.append(
				" left join (select * from cf_synonym where type = 'delivery order status')d on a.status = d.value ");
		builder.append(" left join bp_partner e on e.pn_id = a.carrier_id ");
		builder.append(" left join od_sale_order f on f.so_id = a.so_id ");
		builder.append(" left join or_user h on f.ag_id = h.user_id ");
		builder.append(" left join cl_fresh i on i.lead_id = a.customer_id ");
		builder.append(" left join cp_campaign cp on cp.cp_id = i.cp_id ");
		builder.append(" left join (select * from cf_synonym where type_id = 41)cate on cate.value = cp.cp_subcat ");
		builder.append(" left join bp_partner j on j.pn_id = i.agc_id ");
		builder.append(" left join lc_province l on l.prv_id = cast(i.province as integer) ");
		builder.append(" left join lc_district m on m.dt_id = cast(i.district as integer) ");
		builder.append(" left join lc_subdistrict n on n.sdt_id = cast(i.subdistrict as integer) ");
		builder.append(" left join (select * from cf_synonym where type_id = 51)k on k.value = a.paid_cod ");
		builder.append(
				" left join (select * from cf_synonym where type = 'sale order status')o on f.status = o.value ");
		builder.append(
				" left join (select * from cf_synonym where type = 'lead status')p on i.lead_status = p.value ");
		builder.append(" left join (select so_id, ");
		builder.append(" max((case when item_no = 1 then b.name  end)) as product1, ");
		builder.append(" max((case when item_no = 1 then quantity  end)) as Qty1, ");
		builder.append(" max((case when item_no = 1 then a.price  end)) as Price1, ");
		builder.append(" max((case when item_no = 2 then b.name  end)) as product2, ");
		builder.append(" max((case when item_no = 2 then quantity  end)) as Qty2, ");
		builder.append(" max((case when item_no = 2 then a.price  end)) as Price2, ");
		builder.append(" max((case when item_no = 3 then b.name  end)) as product3, ");
		builder.append(" max((case when item_no = 3 then quantity  end)) as Qty3, ");
		builder.append(" max((case when item_no = 3 then a.price  end)) as Price3, ");
		builder.append(" max((case when item_no = 4 then b.name  end)) as product4, ");
		builder.append(" max((case when item_no = 4 then quantity  end)) as Qty4, ");
		builder.append(" max((case when item_no = 4 then a.price  end)) as Price4 ");
		builder.append(" from od_so_item a join pd_product b on a.prod_id=b.prod_id ");
		builder.append(" group by so_id order by so_id desc) q on q.so_id=f.so_id ");
		builder.append(" left join bp_warehouse s on s.warehouse_id = a.warehouse_id ");
		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			builder.append(" left join lc_neighborhood t on t.id = cast(i.neighborhood as integer) ");
		}
		builder.append(" left join lc_postal_code u on cast(u.id as text) = i.postal_code ");
		builder.append(" where a.error_message is null ");

		if (!StringUtils.isEmpty(params.getSaleOrder()))
			builder.append(" and a.so_id in :soId ");
		if (!StringUtils.isEmpty(params.getDeliveryCode()))
			builder.append(" and lower(a.do_code) like :doCode ");
		if (!StringUtils.isEmpty(params.getCustomerName()))
			builder.append(" and lower(a.customer_name) like :customerName ");
		if (!StringUtils.isEmpty(params.getCustomerPhone()))
			builder.append(" and lower(a.customer_phone) like :customerPhone ");
		if (!StringUtils.isEmpty(params.getSource()))
			builder.append(" and lower(coalesce(j.shortname,i.agc_code)) like :source ");
		if (!StringUtils.isEmpty(params.getUserName()))
			builder.append(" and lower(h.user_name) like :userName ");
		if (!StringUtils.isEmpty(params.getAddress()))
			builder.append(" and lower(regexp_replace(i.address, E'[\\n\\r]+', ' ', 'g' )) like :address ");
		if (!StringUtils.isEmpty(params.getWards()))
			builder.append(" and lower(n.name) like :ward ");
		if (!StringUtils.isEmpty(params.getDistrict()))
			builder.append(" and lower(m.name) like :district ");
		if (!StringUtils.isEmpty(params.getProvince()))
			builder.append(" and lower(l.name) like :province ");
		if (!StringUtils.isEmpty(params.getCarrier()))
			builder.append(" and lower(e.shortname) like :carrier ");
		if (!StringUtils.isEmpty(params.getComment()))
			builder.append(" and lower(regexp_replace(i.comment, E'[\\n\\r]+', ' ', 'g' )) like :comment ");
		if (!StringUtils.isEmpty(params.getStatus()))
			builder.append(" and cast(i.lead_status as text) = :leadStatus ");
		if (!StringUtils.isEmpty(params.getSoStatus()))
			builder.append(" and cast(f.status as text) = :soStatus ");
		if (!StringUtils.isEmpty(params.getStatusTransport()))
			builder.append(" and cast(a.status as text) = :statusTransport ");
		if (!StringUtils.isEmpty(params.getCreatedate()))
			builder.append(
					" and a.createdate  >= to_timestamp(split_part(:createdate,'|',1),'yyyymmddhh24miss') and a.createdate  <= to_timestamp(split_part(:createdate,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getUpdatedate()))
			builder.append(
					" and a.updatedate  >= to_timestamp(split_part(:updatedate,'|',1),'yyyymmddhh24miss') and a.updatedate  <= to_timestamp(split_part(:updatedate,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getTotalCall()))
			builder.append(" and i.total_call = :totalCall ");
		if (!StringUtils.isEmpty(params.getPaymentMethod()))
			builder.append(" and f.payment_method = :paymentMethod ");
		if (!StringUtils.isEmpty(params.getAmount()))
			builder.append(" and f.amount = :amount ");
		if (!StringUtils.isEmpty(params.getProduct1()))
			builder.append(" and product1 like :product1 ");
		if (!StringUtils.isEmpty(params.getQty1()))
			builder.append(" and Qty1 = :qty1");
		if (!StringUtils.isEmpty(params.getPrice1()))
			builder.append(" and Price1 = :price1 ");
		if (!StringUtils.isEmpty(params.getProduct2()))
			builder.append(" and product2 like :product2 ");
		if (!StringUtils.isEmpty(params.getQty2()))
			builder.append(" and Qty2 = :qty2");
		if (!StringUtils.isEmpty(params.getPrice2()))
			builder.append(" and Price2 = :price2 ");
		if (!StringUtils.isEmpty(params.getProduct3()))
			builder.append(" and product3 like :product3 ");
		if (!StringUtils.isEmpty(params.getQty3()))
			builder.append(" and Qty3 = :qty3");
		if (!StringUtils.isEmpty(params.getPrice3()))
			builder.append(" and Price3 = :price3 ");
		if (!StringUtils.isEmpty(params.getLeadId()))
			builder.append(" and a.customer_id in :leadId ");
		if (!StringUtils.isEmpty(params.getFfmCode()))
			builder.append(" and lower(a.ffm_code) like :ffmCode ");
		if (!StringUtils.isEmpty(params.getTrackingCode()))
			builder.append(" and lower(a.tracking_code) like :trackingCode ");
		if (!StringUtils.isEmpty(params.getFfmName()))
			builder.append(" and lower(b.shortname) like :ffmName ");
		if (!StringUtils.isEmpty(params.getOrgId()))
			builder.append(" and a.org_id = :orgId ");
		if (!StringUtils.isEmpty(params.getWarehouse()))
			builder.append(" and lower(s.warehouse_shortname) like :warehouse ");
		if (!StringUtils.isEmpty((params.getLastmileReturnCode())))
			builder.append(" and lower(a.lastmile_return_code) like :lastmileReturnCode ");
		if (!StringUtils.isEmpty(params.getPickedUpDate()))
			builder.append(
					" and a.picked_up_date  >= to_timestamp(split_part(:pickedUpDate,'|',1),'yyyymmddhh24miss') and a.picked_up_date  <= to_timestamp(split_part(:pickedUpDate,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getAttemp()))
			builder.append(" and a.attemp = :attemp ");
		if (!StringUtils.isEmpty(params.getPaidCod()))
			builder.append(" and k.name = :paidCod ");
		if (!StringUtils.isEmpty(params.getPaidCodDate()))
			builder.append(
					" and a.paid_cod_date  >= to_timestamp(split_part(:paidCodDate,'|',1),'yyyymmddhh24miss') and a.paid_cod_date  <= to_timestamp(split_part(:paidCodDate,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getFirstDeliveryTime()))
			builder.append(
					" and a.firstdeliverytime  >= to_timestamp(split_part(:firstDeliveryTime,'|',1),'yyyymmddhh24miss') and a.firstdeliverytime <= to_timestamp(split_part(:firstDeliveryTime,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getSecondDeliveryTime()))
			builder.append(
					" and a.second_delivery_time  >= to_timestamp(split_part(:secondDeliveryTime,'|',1),'yyyymmddhh24miss') and a.second_delivery_time  <= to_timestamp(split_part(:secondDeliveryTime,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getThirdDeliveryTime()))
			builder.append(
					" and a.third_delivery_time  >= to_timestamp(split_part(:thirdDeliveryTime,'|',1),'yyyymmddhh24miss') and a.third_delivery_time  <= to_timestamp(split_part(:thirdDeliveryTime,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getReturnTime()))
			builder.append(
					" and a.returntime  >= to_timestamp(split_part(:returnTime,'|',1),'yyyymmddhh24miss') and a.returntime  <= to_timestamp(split_part(:returnTime,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getCloseTime()))
			builder.append(
					" and a.closetime  >= to_timestamp(split_part(:closeTime,'|',1),'yyyymmddhh24miss') and a.closetime  <= to_timestamp(split_part(:closeTime,'|',2),'yyyymmddhh24miss') ");
		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			if (!StringUtils.isEmpty(params.getNeighborhood())) {
				builder.append(" and lower(t.name) like :neighborhood ");
			}
		}

		if (!StringUtils.isEmpty(params.getPostalCode())) {
			builder.append(" and u.code = :postalCode ");
		}
		builder.append(" order by a.createdate  desc ");

		if (!StringUtils.isEmpty(params.getLimit())) {
			builder.append(" limit :limit");
		}
		if (!StringUtils.isEmpty(params.getOffset())) {
			builder.append(" offset :offset ");
		}
		if (!StringUtils.isEmpty(params.getPickedTime()))
			builder.append(
					" and a.picked_time  >= to_timestamp(split_part(:pickedTime,'|',1),'yyyymmddhh24miss') and a.picked_time  <= to_timestamp(split_part(:pickedTime,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getPackedTime()))
			builder.append(
					" and a.packed_time  >= to_timestamp(split_part(:packedTime,'|',1),'yyyymmddhh24miss') and a.packed_time  <= to_timestamp(split_part(:packedTime,'|',2),'yyyymmddhh24miss') ");
		if (!StringUtils.isEmpty(params.getHandoveredTime()))
			builder.append(
					" and a.handovered_time  >= to_timestamp(split_part(:handoveredTime,'|',1),'yyyymmddhh24miss') and a.handovered_time  <= to_timestamp(split_part(:handoveredTime,'|',2),'yyyymmddhh24miss') ");
		/* set parameters */
		Query query = entityManager.createNativeQuery(builder.toString());
		if (!StringUtils.isEmpty(params.getSaleOrder()))
			query.setParameter("soId", params.getSaleOrder());
		if (!StringUtils.isEmpty(params.getDeliveryCode()))
			query.setParameter("doCode", "%" + params.getDeliveryCode().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getCustomerName()))
			query.setParameter("customerName", "%" + params.getCustomerName().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getCustomerPhone()))
			query.setParameter("customerPhone", "%" + params.getCustomerPhone().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getSource()))
			query.setParameter("source", "%" + params.getSource().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getUserName()))
			query.setParameter("userName", "%" + params.getUserName().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getAddress()))
			query.setParameter("address", "%" + params.getAddress().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getWards()))
			query.setParameter("ward", "%" + params.getWards().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getDistrict()))
			query.setParameter("district", "%" + params.getDistrict().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getProvince()))
			query.setParameter("province", "%" + params.getProvince().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getCarrier())) {
			builder.append(" and lower(e.shortname) like :carrier ");
			query.setParameter("carrier", "%" + params.getCarrier().toLowerCase() + "%");
		}
		if (!StringUtils.isEmpty(params.getComment()))
			query.setParameter("comment", "%" + params.getComment().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getStatus()))
			query.setParameter("leadStatus", params.getStatus());
		if (!StringUtils.isEmpty(params.getSoStatus()))
			query.setParameter("soStatus", params.getSoStatus());
		if (!StringUtils.isEmpty(params.getStatusTransport()))
			query.setParameter("statusTransport", params.getStatusTransport());
		if (!StringUtils.isEmpty(params.getCreatedate()))
			query.setParameter("createdate", params.getCreatedate());
		if (!StringUtils.isEmpty(params.getUpdatedate()))
			query.setParameter("updatedate", params.getUpdatedate());
		if (!StringUtils.isEmpty(params.getTotalCall()))
			query.setParameter("totalCall", params.getTotalCall());
		if (!StringUtils.isEmpty(params.getPaymentMethod()))
			query.setParameter("paymentMethod", params.getPaymentMethod());
		if (!StringUtils.isEmpty(params.getAmount()))
			query.setParameter("amount", params.getAmount());
		if (!StringUtils.isEmpty(params.getProduct1()))
			query.setParameter("product1", "%" + params.getProduct1() + "%");
		if (!StringUtils.isEmpty(params.getQty1()))
			query.setParameter("qty1", params.getQty1());
		if (!StringUtils.isEmpty(params.getPrice1()))
			query.setParameter("price1", params.getPrice1());
		if (!StringUtils.isEmpty(params.getProduct2()))
			query.setParameter("product2", "%" + params.getProduct2() + "%");
		if (!StringUtils.isEmpty(params.getQty2()))
			query.setParameter("qty2", params.getQty2());
		if (!StringUtils.isEmpty(params.getPrice2()))
			query.setParameter("price2", params.getPrice2());
		if (!StringUtils.isEmpty(params.getProduct3()))
			query.setParameter("product3", "%" + params.getProduct3() + "%");
		if (!StringUtils.isEmpty(params.getQty3()))
			query.setParameter("qty3", params.getQty3());
		if (!StringUtils.isEmpty(params.getPrice3()))
			query.setParameter("price3", params.getPrice3());
		if (!StringUtils.isEmpty(params.getLeadId())) {
			List<String> leadIds = Arrays.asList(params.getLeadId().split(","));
			query.setParameter("leadId", leadIds);
		}
		if (!StringUtils.isEmpty(params.getFfmCode()))
			query.setParameter("ffmCode", "%" + params.getFfmCode().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getTrackingCode()))
			query.setParameter("trackingCode", "%" + params.getTrackingCode().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getFfmName()))
			query.setParameter("ffmName", "%" + params.getFfmName().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getOrgId()))
			query.setParameter("orgId", params.getOrgId());
		if (!StringUtils.isEmpty(params.getWarehouse()))
			query.setParameter("warehouse", "%" + params.getWarehouse().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getLastmileReturnCode()))
			query.setParameter("lastmileReturnCode", "%" + params.getLastmileReturnCode().toLowerCase() + "%");
		if (!StringUtils.isEmpty(params.getPickedUpDate()))
			query.setParameter("pickedUpDate", params.getPickedUpDate());
		if (!StringUtils.isEmpty(params.getAttemp()))
			query.setParameter("attemp", params.getAttemp());
		if (!StringUtils.isEmpty(params.getPaidCod()))
			query.setParameter("paidCod", params.getPaidCod());
		if (!StringUtils.isEmpty(params.getPaidCodDate()))
			query.setParameter("paidCodDate", params.getPaidCodDate());
		if (!StringUtils.isEmpty(params.getFirstDeliveryTime()))
			query.setParameter("firstDeliveryTime", params.getFirstDeliveryTime());
		if (!StringUtils.isEmpty(params.getSecondDeliveryTime()))
			query.setParameter("secondDeliveryTime", params.getSecondDeliveryTime());
		if (!StringUtils.isEmpty(params.getThirdDeliveryTime()))
			query.setParameter("thirdDeliveryTime", params.getThirdDeliveryTime());
		if (!StringUtils.isEmpty(params.getReturnTime()))
			query.setParameter("returnTime", params.getReturnTime());
		if (!StringUtils.isEmpty(params.getCloseTime()))
			query.setParameter("closeTime", params.getCloseTime());
		if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
			if (!StringUtils.isEmpty(params.getNeighborhood())) {
				query.setParameter("neighborhood", "%" + params.getNeighborhood().toLowerCase() + "%");
			}
		}

		if (!StringUtils.isEmpty(params.getPostalCode()))
			query.setParameter("postalCode", params.getPostalCode());
		if (!StringUtils.isEmpty(params.getLimit()))
			query.setParameter("limit", params.getLimit());
		if (!StringUtils.isEmpty(params.getOffset()))
			query.setParameter("offset", params.getOffset());
		if (!StringUtils.isEmpty(params.getPickedTime()))
			query.setParameter("pickedTime", params.getPickedTime());
		if (!StringUtils.isEmpty(params.getPackedTime()))
			query.setParameter("packedTime", params.getPackedTime());
		if (!StringUtils.isEmpty(params.getHandoveredTime()))
			query.setParameter("handoveredTime", params.getHandoveredTime());

		/* set response body */
		List<Object[]> data = query.getResultList();
		for (Object[] row : data) {
			GetShippingExportRespV1 shipping = new GetShippingExportRespV1();
			if (row[0] != null) {
				Integer soId = (Integer) row[0];
				shipping.setSaleOrder(soId.toString());
			}
			if (row[1] != null) {
				shipping.setDeliveryCode((String) row[1]);
			}
			if (row[2] != null) {
				shipping.setCustomerName((String) row[2]);
			}
			if (row[3] != null) {
				shipping.setCustomerPhone((String) row[3]);
			}
			if (row[4] != null) {
				shipping.setSource((String) row[4]);
			}
			if (row[5] != null) {
				shipping.setUserName((String) row[5]);
			}
			if (row[6] != null) {
				shipping.setAddress((String) row[6]);
			}

			if ("id".equalsIgnoreCase(StringUtility.trimAllWhitespace(_COUNTRY))) {
				if (row[7] != null) {
					shipping.setPostalCode((String) row[7]);
				}
				if (row[8] != null) {
					shipping.setNeighborhood((String) row[8]);
				}
				if (row[9] != null) {
					shipping.setWards((String) row[9]);
				}
				if (row[10] != null) {
					shipping.setDistrict((String) row[10]);
				}
				if (row[11] != null) {
					shipping.setProvince((String) row[11]);
				}
				if (row[12] != null) {
					shipping.setCarrier((String) row[12]);
				}
				if (row[13] != null) {
					shipping.setComment((String) row[13]);
				}
				if (row[14] != null) {
					shipping.setStatus((String) row[14]);
				}
				if (row[15] != null) {
					shipping.setSoStatus((String) row[15]);
				}
				if (row[16] != null) {
					shipping.setStatusTransport((String) row[16]);
				}
				if (row[17] != null) {
					shipping.setCreatedate((Date) row[17]);
				}
				if (row[18] != null) {
					shipping.setUpdatedate((Date) row[18]);
				}
				if (row[19] != null) {
					shipping.setTotalCall((Integer) row[19]);
				}
				if (row[20] != null) {
					shipping.setPaymentMethod((String) row[20]);
				}
				if (row[21] != null) {
					BigDecimal amount = (BigDecimal) row[21];
					shipping.setAmount(amount.doubleValue());
				}
				if (row[22] != null) {
					shipping.setProduct1((String) row[22]);
				}
				if (row[23] != null) {
					Integer qty1 = (Integer) row[23];
					shipping.setQty1(qty1.doubleValue());
				}
				if (row[24] != null) {
					Integer price1 = (Integer) row[24];
					shipping.setPrice1(price1.doubleValue());
				}
				if (row[25] != null) {
					shipping.setProduct2((String) row[25]);
				}
				if (row[26] != null) {
					Integer qty2 = (Integer) row[26];
					shipping.setQty2(qty2.doubleValue());
				}
				if (row[27] != null) {
					Integer price2 = (Integer) row[27];
					shipping.setPrice2(price2.doubleValue());
				}
				if (row[28] != null) {
					shipping.setProduct3((String) row[28]);
				}
				if (row[29] != null) {
					Integer qty3 = (Integer) row[29];
					shipping.setQty3(qty3.doubleValue());
				}
				if (row[30] != null) {
					Integer price3 = (Integer) row[30];
					shipping.setPrice3(price3.doubleValue());
				}
				if (row[31] != null) {
					shipping.setProduct4((String) row[31]);
				}
				if (row[32] != null) {
					Integer qty4 = (Integer) row[32];
					shipping.setQty4(qty4.doubleValue());
				}
				if (row[33] != null) {
					Integer price4 = (Integer) row[33];
					shipping.setPrice4(price4.doubleValue());
				}
				if (row[34] != null) {
					shipping.setLeadId((Integer) row[34]);
				}
				if (row[35] != null) {
					shipping.setFfmCode((String) row[35]);
				}
				if (row[36] != null) {
					shipping.setTrackingCode((String) row[36]);
				}
				if (row[37] != null) {
					shipping.setFfmName((String) row[37]);
				}
				if (row[38] != null) {
					shipping.setOrgId((Integer) row[38]);
				}
				if (row[39] != null) {
					shipping.setWarehouse((String) row[39]);
				}
				if (row[40] != null) {
					shipping.setAgentNote((String) row[40]);
				}
				if (row[41] != null) {
					shipping.setAffiliateId((String) row[41]);
				}
				String lastmileReturnCode = (String) row[42];
				if (!StringUtils.isEmpty(lastmileReturnCode) && !lastmileReturnCode.toLowerCase().equals("null"))
					shipping.setLastmileReturnCode(lastmileReturnCode);
				if (row[43] != null) {
					shipping.setPickedUpDate((Date) row[43]);
				}
				if (row[44] != null) {
					shipping.setAttemp((Integer) row[44]);
				}
				if (row[45] != null) {
					shipping.setPaidCod((String) row[45]);
				}
				if (row[46] != null) {
					shipping.setPaidCodDate((Date) row[46]);
				}
				if (row[47] != null) {
					shipping.setFirstDeliveryTime((Date) row[47]);
				}
				if (row[48] != null) {
					shipping.setSecondDeliveryTime((Date) row[48]);
				}
				if (row[49] != null) {
					shipping.setThirdDeliveryTime((Date) row[49]);
				}
				if (row[50] != null) {
					shipping.setReturnTime((Date) row[50]);
				}
				if (row[51] != null) {
					shipping.setCloseTime((Date) row[51]);
				}
				if (row[52] != null) {
					shipping.setPickedTime((Date) row[52]);
				}
				if (row[53] != null) {
					shipping.setPackedTime((Date) row[53]);
				}
				if (row[54] != null) {
					shipping.setHandoveredTime((Date) row[54]);
				}
				if (row[55] != null) {
					shipping.setCpName((String) row[55]);
				}
				if (row[56] != null) {
					shipping.setCategory((String) row[56]);
				}
				if (row[57] != null) {
					shipping.setNeighborhoodId((String) row[57]);
				}
				if (row[58] != null) {
					shipping.setWardsId((String) row[58]);
				}
			} else {
				if (row[7] != null) {
					shipping.setNeighborhood((String) row[7]);
				}
				if (row[8] != null) {
					shipping.setWards((String) row[8]);
				}
				if (row[9] != null) {
					shipping.setDistrict((String) row[9]);
				}
				if (row[10] != null) {
					shipping.setProvince((String) row[10]);
				}
				if (row[11] != null) {
					shipping.setCarrier((String) row[11]);
				}
				if (row[12] != null) {
					shipping.setComment((String) row[12]);
				}
				if (row[13] != null) {
					shipping.setStatus((String) row[13]);
				}
				if (row[14] != null) {
					shipping.setSoStatus((String) row[14]);
				}
				if (row[15] != null) {
					shipping.setStatusTransport((String) row[15]);
				}
				if (row[16] != null) {
					shipping.setCreatedate((Date) row[16]);
				}
				if (row[17] != null) {
					shipping.setUpdatedate((Date) row[17]);
				}
				if (row[18] != null) {
					shipping.setTotalCall((Integer) row[18]);
				}
				if (row[19] != null) {
					shipping.setPaymentMethod((String) row[19]);
				}
				if (row[20] != null) {
					BigDecimal amount = (BigDecimal) row[20];
					shipping.setAmount(amount.doubleValue());
				}
				if (row[21] != null) {
					shipping.setProduct1((String) row[21]);
				}
				if (row[22] != null) {
					Integer qty1 = (Integer) row[22];
					shipping.setQty1(qty1.doubleValue());
				}
				if (row[23] != null) {
					Integer price1 = (Integer) row[23];
					shipping.setPrice1(price1.doubleValue());
				}
				if (row[24] != null) {
					shipping.setProduct2((String) row[24]);
				}
				if (row[25] != null) {
					Integer qty2 = (Integer) row[25];
					shipping.setQty2(qty2.doubleValue());
				}
				if (row[26] != null) {
					Integer price2 = (Integer) row[26];
					shipping.setPrice2(price2.doubleValue());
				}
				if (row[27] != null) {
					shipping.setProduct3((String) row[27]);
				}
				if (row[28] != null) {
					Integer qty3 = (Integer) row[28];
					shipping.setQty3(qty3.doubleValue());
				}
				if (row[29] != null) {
					Integer price3 = (Integer) row[29];
					shipping.setPrice3(price3.doubleValue());
				}
				if (row[30] != null) {
					shipping.setProduct4((String) row[30]);
				}
				if (row[31] != null) {
					Integer qty4 = (Integer) row[31];
					shipping.setQty4(qty4.doubleValue());
				}
				if (row[32] != null) {
					Integer price4 = (Integer) row[32];
					shipping.setPrice4(price4.doubleValue());
				}
				if (row[33] != null) {
					shipping.setLeadId((Integer) row[33]);
				}
				if (row[34] != null) {
					shipping.setFfmCode((String) row[34]);
				}
				if (row[35] != null) {
					shipping.setTrackingCode((String) row[35]);
				}
				if (row[36] != null) {
					shipping.setFfmName((String) row[36]);
				}
				if (row[37] != null) {
					shipping.setOrgId((Integer) row[37]);
				}
				if (row[38] != null) {
					shipping.setWarehouse((String) row[38]);
				}
				if (row[39] != null) {
					shipping.setAgentNote((String) row[39]);
				}
				if (row[40] != null) {
					shipping.setAffiliateId((String) row[40]);
				}

				String lastmileReturnCode = (String) row[41];
				if (!StringUtils.isEmpty(lastmileReturnCode) && !lastmileReturnCode.toLowerCase().equals("null"))
					shipping.setLastmileReturnCode(lastmileReturnCode);

				if (row[42] != null) {
					shipping.setPickedUpDate((Date) row[42]);
				}
				if (row[43] != null) {
					shipping.setAttemp((Integer) row[43]);
				}
				if (row[44] != null) {
					shipping.setPaidCod((String) row[44]);
				}
				if (row[45] != null) {
					shipping.setPaidCodDate((Date) row[45]);
				}
				if (row[46] != null) {
					shipping.setFirstDeliveryTime((Date) row[46]);
				}
				if (row[47] != null) {
					shipping.setSecondDeliveryTime((Date) row[47]);
				}
				if (row[48] != null) {
					shipping.setThirdDeliveryTime((Date) row[48]);
				}
				if (row[49] != null) {
					shipping.setReturnTime((Date) row[49]);
				}
				if (row[50] != null) {
					shipping.setCloseTime((Date) row[50]);
				}
				if (row[51] != null) {
					shipping.setPickedTime((Date) row[51]);
				}
				if (row[52] != null) {
					shipping.setPackedTime((Date) row[52]);
				}
				if (row[53] != null) {
					shipping.setHandoveredTime((Date) row[53]);
				}
				if (row[54] != null) {
					shipping.setCpName((String) row[54]);
				}
				if (row[55] != null) {
					shipping.setCategory((String) row[55]);
				}
			}

			result.add(shipping);
		}

		return result;
	}

	@Override
	public byte[] exportCSVShippingPending(GetShipping paramsShippingPending,
			OrderManagementRequestDTO orderManagementRequestDTO) {
		final String ACTIVITY = "Export Shipping Pending";
		final String OBJECT_TYPE = "ExportShippingPending";
		final String ON_FIELD = "getListShippingPending";
		TMSResponse<List<GetShippingResp>> shippingPending = finAllShippingPending(orderManagementRequestDTO);
		List<GetShippingPendingExportResp> saleOrders = new ArrayList<>();
		List<GetShippingResp> lstShipping = shippingPending.getData();
		for(GetShippingResp shippingResp: lstShipping){
			GetShippingPendingExportResp exportResponse = new GetShippingPendingExportResp();
			exportResponse.setOrderId(shippingResp.getSoId());
			exportResponse.setAssigned(shippingResp.getAgent());
			exportResponse.setCarrier(shippingResp.getCarrier());
			exportResponse.setTotalPrice(shippingResp.getAmount());
			exportResponse.setUpdatedate(shippingResp.getSoUpdatedate());
			exportResponse.setLeadId(shippingResp.getLeadId());
			exportResponse.setProductName(shippingResp.getProduct());
			exportResponse.setSource(shippingResp.getSource());
			exportResponse.setWarehouse(shippingResp.getFulfillment());
			exportResponse.setResult(shippingResp.getErrorMessage());
			exportResponse.setCustomerName(shippingResp.getCustomerName());

			if(orderManagementRequestDTO.isDirector())
				exportResponse.setCustomerPhone(shippingResp.getCustomerPhone());
			else
				exportResponse.setCustomerPhone(null);

			if(orderManagementRequestDTO.isTeamLeader())
				exportResponse.setAddress(null);
			else
				exportResponse.setAddress(shippingResp.getAddress());
			saleOrders.add(exportResponse);
		}
		dbLogWriter.writeAgentActivityLog(orderManagementRequestDTO.getUserId(), ACTIVITY, OBJECT_TYPE,
				saleOrders.size(), ON_FIELD, LogHelper.toJson(paramsShippingPending));

		try{
			return ExcelHelper.createExcelData(saleOrders, GetShippingPendingExportResp.class);
		} catch(TMSException error){
			logger.error(error.getMessage());
			return new byte[0];
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShippingPendingRespDto snagDeliveryOrdersPending(GetShipping shippingRequest) {
		ShippingPendingRespDto result = new ShippingPendingRespDto();
		shippingRequest.setDoStatus(Const.DO_SHIPPING_PENDING);
		List<GetShippingResp> shippingDeliveryOrdersPending = new ArrayList<>();
		List<Object[]> rowsIterator = snagAsResultQueryDeliveryOrderPending(shippingRequest).getQuery().getResultList();
		for(Object[] row: rowsIterator){
			GetShippingResp shippingResponse = new GetShippingResp();
			shippingResponse.setOrgId((Integer) row[0]);
			shippingResponse.setLeadId(String.valueOf(row[1]));
			shippingResponse.setSoId(String.valueOf(row[2]));
			shippingResponse.setDoId(String.valueOf(row[3]));
			shippingResponse.setDoCode((String) row[4]);
			shippingResponse.setFfmCode((String) row[5]);
			shippingResponse.setTrackingCode((String) row[6]);
			shippingResponse.setCustomerName((String) row[7]);
			shippingResponse.setCustomerPhone((String) row[8]);
			shippingResponse.setProduct((String) row[9]);
			shippingResponse.setSource((String) row[10]);
			shippingResponse.setAgent((String) row[11]);
			shippingResponse.setAddress((String) row[12]);
			shippingResponse.setWards((String) row[13]);
			shippingResponse.setDistrict((String) row[14]);
			shippingResponse.setProvince((String) row[15]);
			shippingResponse.setComment((String) row[16]);
			shippingResponse.setFulfillment((String) row[17]);
			shippingResponse.setCarrier((String) row[18]);
			shippingResponse.setLeadStatus((String) row[19]);
			shippingResponse.setSoStatus((String) row[20]);
			shippingResponse.setDoStatus((String) row[21]);
			shippingResponse.setSoCreatedate(DateUtils.feedStageAsString((Date) row[22]));
			shippingResponse.setSoUpdatedate(DateUtils.feedStageAsString((Date) row[23]));
			shippingResponse.setDoCreatedate(DateUtils.feedStageAsString((Date) row[24]));
			shippingResponse.setDoUpdatedate(DateUtils.feedStageAsString((Date) row[25]));
			shippingResponse.setTotalCall((Integer) row[26]);
			shippingResponse.setPaymentMethod((String) row[27]);
			BigDecimal bigDecimal = (BigDecimal) row[28];
			shippingResponse.setAmount(bigDecimal.doubleValue());
			shippingResponse.setErrorMessage((String) row[29]);

			/* add to list */
			shippingDeliveryOrdersPending.add(shippingResponse);
		}
		BigInteger getRowCount = (BigInteger) snagAsResultQueryDeliveryOrderPending(shippingRequest).getQueryAsCounter()
				.getResultList().get(0);
		Integer rowCount = getRowCount.intValue();
		result.setListShippingPending(shippingDeliveryOrdersPending);
		result.setRowCount(rowCount);

		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShippingResponseDTO snagShippingDeliveryOrders(ObjectRequestDTO objectRequestDTO,
			GetDoNewV2 deliveryOrderRequest) {
		ShippingResponseDTO listShippingResponse = new ShippingResponseDTO();
		List<GetDoNewResp> listDeliveryOrders = new ArrayList<>();
		deliveryOrderRequest.setOrgId(objectRequestDTO.getOrganizationId());
		deliveryOrderRequest.setErrorMessage(Const.DB_TMS_NULL);
		if(ObjectUtils.isNull(deliveryOrderRequest.getLimit()))
			deliveryOrderRequest.setLimit(Const.DEFAULT_PAGE_SIZE);

		QueryResponseDTO queryResponseDTO = snagAsResultQueryShippingDeliveryOrder(deliveryOrderRequest);
		List<Object[]> rowsIterator = queryResponseDTO.getQuery().getResultList();
		for(Object[] row: rowsIterator){
			GetDoNewResp deliveryOrderResponse = new GetDoNewResp();
			deliveryOrderResponse.setOrgId((Integer) row[0]);
			deliveryOrderResponse.setDoId((Integer) row[1]);
			deliveryOrderResponse.setDoCode((String) row[2]);
			deliveryOrderResponse.setFfmCode((String) row[3]);
			deliveryOrderResponse.setTrackingCode((String) row[4]);
			deliveryOrderResponse.setSoId((Integer) row[5]);
			deliveryOrderResponse.setFfmId((Integer) row[6]);
			deliveryOrderResponse.setFfmName((String) row[7]);
			deliveryOrderResponse.setWarehouseId((Integer) row[8]);
			deliveryOrderResponse.setWarehouseName((String) row[9]);
			deliveryOrderResponse.setCarrierId((Integer) row[10]);
			deliveryOrderResponse.setCarrierName((String) row[11]);
			deliveryOrderResponse.setCustomerId((Integer) row[12]);
			deliveryOrderResponse.setCustomerName((String) row[13]);
			deliveryOrderResponse.setCustomerPhone((String) row[14]);
			deliveryOrderResponse.setCustomerAddress((String) row[15]);
			deliveryOrderResponse.setCustomerWards((String) row[16]);
			deliveryOrderResponse.setCustomerDistrict((String) row[17]);
			deliveryOrderResponse.setCustomerProvince((String) row[18]);
			deliveryOrderResponse.setPackageId((String) row[19]);
			deliveryOrderResponse.setPackageName((String) row[20]);
			deliveryOrderResponse.setPackageDescription((String) row[21]);
			BigDecimal amount = (BigDecimal) row[22];
			deliveryOrderResponse.setAmountcod(amount != null ? amount.doubleValue() : 0);
			deliveryOrderResponse.setStatus((Integer) row[23]);
			deliveryOrderResponse.setStatusName((String) row[24]);
			deliveryOrderResponse.setStatusFfm((String) row[25]);
			deliveryOrderResponse.setStatusLastmile((String) row[26]);
			deliveryOrderResponse.setErrorCode((String) row[27]);
			deliveryOrderResponse.setErrorMessage((String) row[28]);
			deliveryOrderResponse.setAttribute1((String) row[29]);
			deliveryOrderResponse.setAttribute2((String) row[30]);
			deliveryOrderResponse.setAttribute3((String) row[31]);
			deliveryOrderResponse.setAttribute4((String) row[32]);
			deliveryOrderResponse.setAttribute5((String) row[33]);
			deliveryOrderResponse.setCreateby((Integer) row[34]);
			deliveryOrderResponse.setCreatedate((Date) row[35]);
			deliveryOrderResponse.setUpdateby((Integer) row[36]);
			deliveryOrderResponse.setUpdatedate((Date) row[37]);
			deliveryOrderResponse.setApprovedTime((Date) row[38]);
			deliveryOrderResponse.setExpectedDeliveryTime((Date) row[39]);
			deliveryOrderResponse.setExpectedArrivalTime((Date) row[40]);
			String lastmileReturnCode = (String) row[41];
			if (!StringUtils.isEmpty(lastmileReturnCode) && !lastmileReturnCode.toLowerCase().equals("null")) {
				deliveryOrderResponse.setLastmileReturnCode(lastmileReturnCode);
			}

			/* add to list */
			listDeliveryOrders.add(deliveryOrderResponse);
		}

		/* find counter */
		BigInteger counter = (BigInteger) queryResponseDTO.getQueryAsCounter().getResultList().get(0);
		/* set result */
		listShippingResponse.setCounter(counter.intValue());
		listShippingResponse.setListShipping(snagAsResultShippingConnectLeads(objectRequestDTO, listDeliveryOrders));
		return listShippingResponse;
	}

	/**
	 * Call function from database : get_lead_v4
	 */
	private List<ShippingDto> snagAsResultShippingConnectLeads(ObjectRequestDTO objectRequestDTO,
			List<GetDoNewResp> listDeliveryOrders) {
		List<ShippingDto> listShippingResponse = new ArrayList<>();
		List<Integer> listFreshId = new ArrayList<>();

		for(GetDoNewResp deliveryOrder : listDeliveryOrders){
			listFreshId.add(deliveryOrder.getCustomerId());
		}

		String leadIds = listFreshId.toString();
		leadIds = leadIds.substring(1, leadIds.length() - 1);
		GetLeadParamsV11 leadParams = new GetLeadParamsV11();
		leadParams.setLeadId(leadIds);
		leadParams.setOrgId(objectRequestDTO.getOrganizationId());
		DBResponse<List<CLFresh>> listFreshLeads = freshService.getLeadV11(objectRequestDTO.getSessionId(),
				leadParams);

		Map<Integer, CLFresh> map = new HashMap<>();
		if(listFreshLeads.getResult().size() > 0){
			for(CLFresh clFresh : listFreshLeads.getResult()){
				map.put(clFresh.getLeadId(), clFresh);
			}
		}

		if(!CollectionUtils.isEmpty(listDeliveryOrders)){
			for(GetDoNewResp deliveryOrder: listDeliveryOrders) {
				ShippingDto shippingObject = new ShippingDto(deliveryOrder);
				if(!ObjectUtils.isNull(shippingObject.getCustomerId())) {
					shippingObject.setCustomer(map.get(deliveryOrder.getCustomerId()));
					listShippingResponse.add(shippingObject);
				}
			}
		}

		return listShippingResponse;
	}

	private QueryResponseDTO snagAsResultQueryShippingDeliveryOrder(GetDoNewV2 deliveryOrderRequest) {
		QueryRequestDTO queryRequestDTO = buildQueryShippingDeliveryOrder(deliveryOrderRequest);
		QueryResponseDTO queryResponseDTO = new QueryResponseDTO();
		Query query = entityManager.createNativeQuery(queryRequestDTO.getQuery());
		Query queryAsCounter = entityManager.createNativeQuery(queryRequestDTO.getQueryAsCounters());

		/* filter here */
		if(!ObjectUtils.isNull(deliveryOrderRequest.getOrgId())){
			query.setParameter("orgId", deliveryOrderRequest.getOrgId());
			queryAsCounter.setParameter("orgId", deliveryOrderRequest.getOrgId());
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getDoId())){
			query.setParameter("doId", deliveryOrderRequest.getDoId());
			queryAsCounter.setParameter("doId", deliveryOrderRequest.getDoId());
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getSoId())){
			query.setParameter("soId", deliveryOrderRequest.getSoId());
			queryAsCounter.setParameter("soId", deliveryOrderRequest.getSoId());
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getFfmId())){
			query.setParameter("ffmId", deliveryOrderRequest.getFfmId());
			queryAsCounter.setParameter("ffmId", deliveryOrderRequest.getFfmId());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getDoCode())){
			query.setParameter("doCode", "%" + deliveryOrderRequest.getDoCode().toLowerCase() + "%");
			queryAsCounter.setParameter("doCode", "%" + deliveryOrderRequest.getDoCode().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getFfmName())){
			query.setParameter("ffmName", "%" + deliveryOrderRequest.getFfmName().toLowerCase() + "%");
			queryAsCounter.setParameter("ffmName", "%" + deliveryOrderRequest.getFfmName().toLowerCase() + "%");
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getWarehouseId())){
			query.setParameter("warehouseId", deliveryOrderRequest.getWarehouseId());
			queryAsCounter.setParameter("warehouseId", deliveryOrderRequest.getWarehouseId());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getWarehouseName())){
			query.setParameter("warehouseName", "%" + deliveryOrderRequest.getWarehouseName().toLowerCase() + "%");
			queryAsCounter.setParameter("warehouseName",
					"%" + deliveryOrderRequest.getWarehouseName().toLowerCase() + "%");
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getCarrierId())){
			query.setParameter("carrierId", deliveryOrderRequest.getCarrierId());
			queryAsCounter.setParameter("carrierId", deliveryOrderRequest.getCarrierId());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCarrierName())){
			query.setParameter("carrierName", "%" + deliveryOrderRequest.getCarrierName().toLowerCase() + "%");
			queryAsCounter.setParameter("carrierName", "%" + deliveryOrderRequest.getCarrierName().toLowerCase() + "%");
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getCustomerId())){
			query.setParameter("customerId", deliveryOrderRequest.getCustomerId());
			queryAsCounter.setParameter("customerId", deliveryOrderRequest.getCustomerId());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerName())){
			query.setParameter("customerName", "%" + deliveryOrderRequest.getCustomerName().toLowerCase() + "%");
			queryAsCounter.setParameter("customerName",
					"%" + deliveryOrderRequest.getCustomerName().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerPhone())){
			query.setParameter("customerPhone", "%" + deliveryOrderRequest.getCustomerPhone().toLowerCase() + "%");
			queryAsCounter.setParameter("customerPhone",
					"%" + deliveryOrderRequest.getCustomerPhone().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerAddress())){
			query.setParameter("customerAddress", "%" + deliveryOrderRequest.getCustomerAddress().toLowerCase() + "%");
			queryAsCounter.setParameter("customerAddress",
					"%" + deliveryOrderRequest.getCustomerAddress().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerWards())){
			query.setParameter("customerWard", "%" + deliveryOrderRequest.getCustomerWards().toLowerCase() + "%");
			queryAsCounter.setParameter("customerWard",
					"%" + deliveryOrderRequest.getCustomerWards().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerDistrict())){
			query.setParameter("customerDistrict",
					"%" + deliveryOrderRequest.getCustomerDistrict().toLowerCase() + "%");
			queryAsCounter.setParameter("customerDistrict",
					"%" + deliveryOrderRequest.getCustomerDistrict().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerProvince())){
			query.setParameter("customerProvince",
					"%" + deliveryOrderRequest.getCustomerProvince().toLowerCase() + "%");
			queryAsCounter.setParameter("customerProvince",
					"%" + deliveryOrderRequest.getCustomerProvince().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getPackageId())){
			query.setParameter("packageId", "%" + deliveryOrderRequest.getPackageId().toLowerCase() + "%");
			queryAsCounter.setParameter("packageId", "%" + deliveryOrderRequest.getPackageId().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getPackageName())){
			query.setParameter("packageName", "%" + deliveryOrderRequest.getPackageName().toLowerCase() + "%");
			queryAsCounter.setParameter("packageName", "%" + deliveryOrderRequest.getPackageName().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getPackageDescription())){
			query.setParameter("packageDescription",
					"%" + deliveryOrderRequest.getPackageDescription().toLowerCase() + "%");
			queryAsCounter.setParameter("packageDescription",
					"%" + deliveryOrderRequest.getPackageDescription().toLowerCase() + "%");
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getAmountcod())){
			query.setParameter("amountCode", deliveryOrderRequest.getAmountcod());
			queryAsCounter.setParameter("amountCode", deliveryOrderRequest.getAmountcod());
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getStatus())){
			query.setParameter("status", deliveryOrderRequest.getStatus());
			queryAsCounter.setParameter("status", deliveryOrderRequest.getStatus());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getStatusName())){
			query.setParameter("statusName", "%" + deliveryOrderRequest.getStatusName().toLowerCase() + "%");
			queryAsCounter.setParameter("statusName", "%" + deliveryOrderRequest.getStatusName().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getStatusFfm())){
			query.setParameter("statusFfm", "%" + deliveryOrderRequest.getStatusFfm().toLowerCase() + "%");
			queryAsCounter.setParameter("statusFfm", "%" + deliveryOrderRequest.getStatusFfm().toLowerCase() + "%");
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getCreateby())){
			query.setParameter("createdBy", deliveryOrderRequest.getCreateby());
			queryAsCounter.setParameter("createdBy", deliveryOrderRequest.getCreateby());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCreatedate())){
			query.setParameter("createdDate", deliveryOrderRequest.getCreatedate());
			queryAsCounter.setParameter("createdDate", deliveryOrderRequest.getCreatedate());
		}

		if(!ObjectUtils.isNull(deliveryOrderRequest.getUpdateby())){
			query.setParameter("updatedBy", deliveryOrderRequest.getUpdateby());
			queryAsCounter.setParameter("updatedBy", deliveryOrderRequest.getUpdateby());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getUpdatedate())){
			query.setParameter("updatedDate", deliveryOrderRequest.getUpdatedate());
			queryAsCounter.setParameter("updatedDate", deliveryOrderRequest.getUpdatedate());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getApprovedTime())){
			query.setParameter("approvedTime", deliveryOrderRequest.getApprovedTime());
			queryAsCounter.setParameter("approvedTime", deliveryOrderRequest.getApprovedTime());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getExpectedDeliveryTime())){
			query.setParameter("expectedDeliveryTime", deliveryOrderRequest.getExpectedDeliveryTime());
			queryAsCounter.setParameter("expectedDeliveryTime", deliveryOrderRequest.getExpectedDeliveryTime());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getExpectedArrivalTime())){
			query.setParameter("expectedArrivalTime", deliveryOrderRequest.getExpectedArrivalTime());
			queryAsCounter.setParameter("expectedArrivalTime", deliveryOrderRequest.getExpectedArrivalTime());
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute1())){
			query.setParameter("attribute1", "%" + deliveryOrderRequest.getAttribute1().toLowerCase() + "%");
			queryAsCounter.setParameter("attribute1", "%" + deliveryOrderRequest.getAttribute1().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute2())){
			query.setParameter("attribute2", "%" + deliveryOrderRequest.getAttribute2().toLowerCase() + "%");
			queryAsCounter.setParameter("attribute2", "%" + deliveryOrderRequest.getAttribute2().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute3())){
			query.setParameter("attribute3", "%" + deliveryOrderRequest.getAttribute3().toLowerCase() + "%");
			queryAsCounter.setParameter("attribute3", "%" + deliveryOrderRequest.getAttribute3().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute4())){
			query.setParameter("attribute4", "%" + deliveryOrderRequest.getAttribute4().toLowerCase() + "%");
			queryAsCounter.setParameter("attribute4", "%" + deliveryOrderRequest.getAttribute4().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute5())){
			query.setParameter("attribute5", "%" + deliveryOrderRequest.getAttribute5().toLowerCase() + "%");
			queryAsCounter.setParameter("attribute5", "%" + deliveryOrderRequest.getAttribute5().toLowerCase() + "%");
		}

		/* special case */
		if(!StringUtils.isEmpty(deliveryOrderRequest.getFfmCode())){
			query.setParameter("ffmCode", "%" + deliveryOrderRequest.getFfmCode().toLowerCase() + "%");
			queryAsCounter.setParameter("ffmCode", "%" + deliveryOrderRequest.getFfmCode().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getTrackingCode())){
			query.setParameter("trackingCode", "%" + deliveryOrderRequest.getTrackingCode().toLowerCase() + "%");
			queryAsCounter.setParameter("trackingCode",
					"%" + deliveryOrderRequest.getTrackingCode().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getStatusLastmile())){
			query.setParameter("statusLastMile", "%" + deliveryOrderRequest.getStatusLastmile().toLowerCase() + "%");
			queryAsCounter.setParameter("statusLastMile",
					"%" + deliveryOrderRequest.getStatusLastmile().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(deliveryOrderRequest.getErrorCode())){
			query.setParameter("errorCode", "%" + deliveryOrderRequest.getErrorCode().toLowerCase() + "%");
			queryAsCounter.setParameter("errorCode", "%" + deliveryOrderRequest.getErrorCode().toLowerCase() + "%");
		}

		if (!StringUtils.isEmpty(deliveryOrderRequest.getLastmileReturnCode())){
			query.setParameter("lastmileReturnCode", "%" + deliveryOrderRequest.getLastmileReturnCode() + "%");
			queryAsCounter.setParameter("lastmileReturnCode", "%" + deliveryOrderRequest.getLastmileReturnCode() + "%");
		}

//        if (!StringUtils.isEmpty(deliveryOrderRequest.getErrorMessage())) {
//            String errorMessage = "";
//            if (deliveryOrderRequest.getErrorMessage() == Const.DB_TMS_NULL){
//                errorMessage = " is null ";
//            } else {
//                errorMessage = " is not null ";
//            }
//            query.setParameter("errorMessage", errorMessage);
//            queryAsCounter.setParameter("errorMessage", errorMessage);
//        }
		/* end case. */

		if(!ObjectUtils.isNull(deliveryOrderRequest.getLimit()))
			query.setParameter("limit", deliveryOrderRequest.getLimit());

		if(!ObjectUtils.isNull(deliveryOrderRequest.getOffset()))
			query.setParameter("offset", deliveryOrderRequest.getOffset());

		queryResponseDTO.setQuery(query);
		queryResponseDTO.setQueryAsCounter(queryAsCounter);
		return queryResponseDTO;
	}

	private QueryRequestDTO buildQueryShippingDeliveryOrder(GetDoNewV2 deliveryOrderRequest) {
		QueryRequestDTO queryRequestDTO = new QueryRequestDTO();
		StringBuilder builder = new StringBuilder();

		builder.append(
				" select a.org_id ,a.do_id ,a.do_code, a.ffm_code ,a.tracking_code ,a.so_id ,a.FFM_id ,b.shortname as FFM_name  ,a.warehouse_id ,c.warehouse_shortname as warehouse_name , ");
		builder.append(" a.carrier_id ,e.shortname as carrier_name, a.customer_id , ");
		builder.append(
				" a.customer_name ,a.customer_phone ,a.customer_address ,a.customer_wards  ,a.customer_district ,a.customer_province  ,a.package_id, ");
		builder.append(
				" a.package_name ,a.package_description ,a.amountCOD ,a.status ,d.name as status_name ,a.status_FFM , ");
		builder.append(
				" a.status_lastmile, a.error_code , a.error_message ,a.attribute1  ,a.attribute2  ,a.attribute3  ,a.attribute4  ,a.attribute5, ");
		builder.append(
				" a.createby ,a.createdate ,a.updateby ,a.updatedate ,a.approved_time ,a.expected_delivery_time ,a.expected_arrival_time, a.lastmile_return_code ");
		builder.append(" from od_do_new a ");
		builder.append(" left join bp_partner b on a.FFM_id=b.pn_id ");
		builder.append(" left join bp_warehouse c on a.warehouse_id=c.warehouse_id ");
		builder.append(
				" left join (select * from cf_synonym where type ='delivery order status')d on a.status = d.value ");
		builder.append(" left join bp_partner e on e.pn_id=a.carrier_id ");
		builder.append(" left join od_sale_order f on f.so_id=a.so_id ");
		builder.append(" where 1=1 ");
		StringBuilder counter = new StringBuilder("select count(*) from ( ");

		/* add filter */
		if(!ObjectUtils.isNull(deliveryOrderRequest.getOrgId()))
			builder.append(" and a.org_id = :orgId ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getDoId()))
			builder.append(" and a.do_id = :doId ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getSoId()))
			builder.append(" and a.so_id = :soId");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getFfmId()))
			builder.append(" and a.FFM_id = :ffmId");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getDoCode()))
			builder.append(" and lower(a.do_code) like :doCode ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getFfmName()))
			builder.append(" and lower(b.shortname) like :ffmName ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getWarehouseId()))
			builder.append(" and a.warehouse_id = :warehouseId ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getWarehouseName()))
			builder.append(" and lower(c.warehouse_shortname) like :warehouseName ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getCarrierId()))
			builder.append(" and a.carrier_id = :carrierId ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCarrierName()))
			builder.append(" and lower(e.shortname) like :carrierName ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getCustomerId()))
			builder.append(" and a.customer_id = :customerId ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerName()))
			builder.append(" and lower(a.customer_name) like :customerName ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerPhone()))
			builder.append(" and a.customer_phone like :customerPhone ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerAddress()))
			builder.append(" and lower(a.customer_address) like :customerAddress ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerWards()))
			builder.append(" and lower(a.customer_wards) like :customerWard ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerDistrict()))
			builder.append(" and lower(a.customer_district) like :customerDistrict ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCustomerProvince()))
			builder.append(" and lower(a.customer_province) like :customerProvince ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getPackageId()))
			builder.append(" and lower(a.package_id) like :packageId ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getPackageName()))
			builder.append(" and lower(a.package_name) like :packageName ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getPackageDescription()))
			builder.append(" and lower(a.package_description) like :packageDescription ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getAmountcod()))
			builder.append(" and a.amountCOD = :amountCode ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getStatus()))
			builder.append(" and a.status = :status ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getStatusName()))
			builder.append(" and lower(d.name) like :statusName ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getStatusFfm()))
			builder.append(" and lower(a.status_FFM) like :statusFfm ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getCreateby()))
			builder.append(" and a.createby = :createdBy ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getCreatedate()))
			builder.append(
					" and ( a.createdate  >= to_timestamp(split_part(:createdDate,'|',1),'yyyymmddhh24miss') and a.createdate <= to_timestamp(split_part(:createdDate,'|',2),'yyyymmddhh24miss')) ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getUpdateby()))
			builder.append(" and a.updateby = :updatedBy ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getUpdatedate()))
			builder.append(
					" and ( a.updatedate  >= to_timestamp(split_part(:updatedDate,'|',1),'yyyymmddhh24miss') and a.updatedate <= to_timestamp(split_part(:updatedDate,'|',2),'yyyymmddhh24miss')) ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getApprovedTime()))
			builder.append(" and a.approved_time = to_timestamp(:approvedTime,'yyyymmddhh24miss') ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getExpectedDeliveryTime()))
			builder.append(" and a.expected_delivery_time = to_timestamp(:expectedDeliveryTime,'yyyymmddhh24miss') ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getExpectedArrivalTime()))
			builder.append(" and a.expected_arrival_time = to_timestamp(:expectedArrivalTime,'yyyymmddhh24miss') ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute1()))
			builder.append(" and lower(a.attribute1) like :attribute1 ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute2()))
			builder.append(" and lower(a.attribute2) like :attribute2 ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute3()))
			builder.append(" and lower(a.attribute3) like :attribute3 ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute4()))
			builder.append(" and lower(a.attribute4) like :attribute4 ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getAttribute5()))
			builder.append(" and lower(a.attribute5) like :attribute5 ");

		/* special case */
		if(!StringUtils.isEmpty(deliveryOrderRequest.getFfmCode()))
			builder.append(" and lower(a.ffm_code) like :ffmCode ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getTrackingCode()))
			builder.append(" and lower(a.tracking_code) like :trackingCode ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getStatusLastmile()))
			builder.append(" and lower(a.status_lastmile) like :statusLastMile ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getErrorCode()))
			builder.append(" and lower(a.error_code) like :errorCode ");

		if(!StringUtils.isEmpty(deliveryOrderRequest.getErrorMessage()))
			if(deliveryOrderRequest.getErrorMessage() == Const.DB_TMS_NULL)
				builder.append(" and a.error_message is null ");
			else
				builder.append(" and a.error_message is not null ");

		if (!StringUtils.isEmpty(deliveryOrderRequest.getLastmileReturnCode()))
			builder.append(" and lower(a.lastmile_return_code) like :lastmileReturnCode ");

		/* end filters */
		counter.append(builder);
		counter.append(" ) as numberTotal");
		builder.append(" order by a.createdate  desc ");
		if(!ObjectUtils.isNull(deliveryOrderRequest.getLimit()))
			builder.append(" limit :limit ");

		if(!ObjectUtils.isNull(deliveryOrderRequest.getOffset()))
			builder.append(" offset :offset ");

		queryRequestDTO.setQuery(builder.toString());
		queryRequestDTO.setQueryAsCounters(counter.toString());
		return queryRequestDTO;
	}

	private TMSResponse<List<GetShippingResp>> finAllShippingPending(
			OrderManagementRequestDTO orderManagementRequestDTO) {
		TMSResponse<List<GetShippingResp>> listTMSResponse = new TMSResponse<>();
		GetShipping params = new GetShipping();

		params.setOrgId(orderManagementRequestDTO.getOrgId());
		ShippingPendingRespDto shippingResponse = snagDeliveryOrdersPending(params);

		listTMSResponse.setData(shippingResponse.getListShippingPending());
		listTMSResponse.setTotal(shippingResponse.getRowCount());
		return listTMSResponse;
	}

	private QueryResponseDTO snagAsResultQueryDeliveryOrderPending(GetShipping shippingRequest) {
		QueryRequestDTO queryRequestDTO = buildQueryDeliveryOrderPending(shippingRequest);
		QueryResponseDTO queryResponseDTO = new QueryResponseDTO();
		Query query = entityManager.createNativeQuery(queryRequestDTO.getQuery());
		Query queryAsCounter = entityManager.createNativeQuery(queryRequestDTO.getQueryAsCounters());

		if(!ObjectUtils.isNull(shippingRequest.getOrgId())){
			query.setParameter("orgId", shippingRequest.getOrgId());
			queryAsCounter.setParameter("orgId", shippingRequest.getOrgId());
		}

		if(!StringUtils.isEmpty(shippingRequest.getLeadId())){
			List<String> leadIds = Arrays.asList(shippingRequest.getLeadId().split(","));
			query.setParameter("leadId", leadIds);
			queryAsCounter.setParameter("leadId", leadIds);
		}

		if(!StringUtils.isEmpty(shippingRequest.getSoId())){
			List<String> soIds = Arrays.asList(shippingRequest.getSoId().split(","));
			List<String> soIdsResult = StringUtility.trimAllWhitesSpace(soIds);
			query.setParameter("soId", soIdsResult.stream().map(Integer::parseInt).collect(Collectors.toList()));
			queryAsCounter.setParameter("soId",
					soIdsResult.stream().map(Integer::parseInt).collect(Collectors.toList()));
		}

		if(!StringUtils.isEmpty(shippingRequest.getDoId())){
			List<String> doIds = Arrays.asList(shippingRequest.getDoId().split(","));
			query.setParameter("doId", doIds);
			queryAsCounter.setParameter("doId", doIds);
		}

		if(!StringUtils.isEmpty(shippingRequest.getDoCode())){
			query.setParameter("doCode", shippingRequest.getDoCode());
			queryAsCounter.setParameter("doCode", shippingRequest.getDoCode());
		}

		if(!StringUtils.isEmpty(shippingRequest.getFfmCode())){
			query.setParameter("ffmCode", shippingRequest.getFfmCode());
			queryAsCounter.setParameter("ffmCode", shippingRequest.getFfmCode());
		}

		if(!StringUtils.isEmpty(shippingRequest.getTrackingCode())){
			query.setParameter("trackingCode", shippingRequest.getTrackingCode().trim());
			queryAsCounter.setParameter("trackingCode", shippingRequest.getTrackingCode().trim());
		}

		if(!StringUtils.isEmpty(shippingRequest.getCustomerName())){
			query.setParameter("customerName", "%" + shippingRequest.getCustomerName().toLowerCase() + "%");
			queryAsCounter.setParameter("customerName", "%" + shippingRequest.getCustomerName().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getCustomerPhone())){
			query.setParameter("customerPhone", "%" + shippingRequest.getCustomerPhone().toLowerCase() + "%");
			queryAsCounter.setParameter("customerPhone", "%" + shippingRequest.getCustomerPhone().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getProduct())){
			query.setParameter("productName", "%" + shippingRequest.getProduct().toLowerCase() + "%");
			queryAsCounter.setParameter("productName", "%" + shippingRequest.getProduct().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getSource())){
			query.setParameter("source", "%" + shippingRequest.getSource().toLowerCase() + "%");
			queryAsCounter.setParameter("source", "%" + shippingRequest.getSource().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getAgent())){
			query.setParameter("agent", "%" + shippingRequest.getAgent().toLowerCase() + "%");
			queryAsCounter.setParameter("agent", "%" + shippingRequest.getAgent().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getAddress())){
			query.setParameter("address", "%" + shippingRequest.getAddress().toLowerCase() + "%");
			queryAsCounter.setParameter("address", "%" + shippingRequest.getAddress().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getWards())){
			query.setParameter("ward", "%" + shippingRequest.getWards().toLowerCase() + "%");
			queryAsCounter.setParameter("ward", "%" + shippingRequest.getWards().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getDistrict())){
			query.setParameter("district", "%" + shippingRequest.getDistrict().toLowerCase() + "%");
			queryAsCounter.setParameter("district", "%" + shippingRequest.getDistrict().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getProvince())){
			query.setParameter("province", "%" + shippingRequest.getProvince().toLowerCase() + "%");
			queryAsCounter.setParameter("province", "%" + shippingRequest.getProvince().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getComment())){
			query.setParameter("comment", "%" + shippingRequest.getComment().toLowerCase() + "%");
			queryAsCounter.setParameter("comment", "%" + shippingRequest.getComment().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getFulfillment())){
			query.setParameter("fulfillment", "%" + shippingRequest.getFulfillment().toLowerCase() + "%");
			queryAsCounter.setParameter("fulfillment", "%" + shippingRequest.getFulfillment().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getCarrier())){
			query.setParameter("carrier", "%" + shippingRequest.getCarrier().toLowerCase() + "%");
			queryAsCounter.setParameter("carrier", "%" + shippingRequest.getCarrier().toLowerCase() + "%");
		}

		if(!StringUtils.isEmpty(shippingRequest.getLeadStatus())){
			List<String> leadStatus = Arrays.asList(shippingRequest.getLeadStatus().split(","));
			query.setParameter("leadStatus", leadStatus);
			queryAsCounter.setParameter("leadStatus", leadStatus);
		}

		if(!StringUtils.isEmpty(shippingRequest.getSoStatus())){
			List<String> soStatus = Arrays.asList(shippingRequest.getSoStatus().split(","));
			query.setParameter("soStatus", soStatus);
			queryAsCounter.setParameter("soStatus", soStatus);
		}

		if(!StringUtils.isEmpty(shippingRequest.getDoStatus())){
			query.setParameter("doStatus", shippingRequest.getDoStatus());
			queryAsCounter.setParameter("doStatus", shippingRequest.getDoStatus());
		}

		if(!StringUtils.isEmpty(shippingRequest.getSoCreatedate())){
			query.setParameter("soCreateDate", shippingRequest.getSoCreatedate());
			queryAsCounter.setParameter("soCreateDate", shippingRequest.getSoCreatedate());
		}

		if(!StringUtils.isEmpty(shippingRequest.getSoUpdatedate())){
			query.setParameter("soUpdateDate", shippingRequest.getSoUpdatedate());
			queryAsCounter.setParameter("soUpdateDate", shippingRequest.getSoUpdatedate());
		}

		if(!StringUtils.isEmpty(shippingRequest.getDoCreatedate())){
			query.setParameter("doCreateDate", shippingRequest.getDoCreatedate());
			queryAsCounter.setParameter("doCreateDate", shippingRequest.getDoCreatedate());
		}

		if(!StringUtils.isEmpty(shippingRequest.getDoUpdatedate())){
			query.setParameter("doUpdateDate", shippingRequest.getDoUpdatedate());
			queryAsCounter.setParameter("doUpdateDate", shippingRequest.getDoUpdatedate());
		}

		if(!ObjectUtils.isNull(shippingRequest.getTotalCall())){
			query.setParameter("totalCall", shippingRequest.getTotalCall());
			queryAsCounter.setParameter("totalCall", shippingRequest.getTotalCall());
		}

		if(!StringUtils.isEmpty(shippingRequest.getPaymentMethod())){
			query.setParameter("paymentMethod", shippingRequest.getPaymentMethod());
			queryAsCounter.setParameter("paymentMethod", shippingRequest.getPaymentMethod());
		}

		if(!ObjectUtils.isNull(shippingRequest.getAmount())){
			query.setParameter("amount", shippingRequest.getAmount());
			queryAsCounter.setParameter("amount", shippingRequest.getAmount());
		}

		if(!StringUtils.isEmpty(shippingRequest.getErrorMessage())){
			query.setParameter("errorMessage", "%" + shippingRequest.getErrorMessage().toLowerCase() + "%");
			queryAsCounter.setParameter("errorMessage", "%" + shippingRequest.getErrorMessage().toLowerCase() + "%");
		}

		if(!ObjectUtils.isNull(shippingRequest.getLimit()))
			query.setParameter("limit", shippingRequest.getLimit());

		if(!ObjectUtils.isNull(shippingRequest.getOffset()))
			query.setParameter("offset", shippingRequest.getOffset());

		queryResponseDTO.setQuery(query);
		queryResponseDTO.setQueryAsCounter(queryAsCounter);
		return queryResponseDTO;
	}

	private QueryRequestDTO buildQueryDeliveryOrderPending(GetShipping shippingRequest) {
		QueryRequestDTO queryRequestDTO = new QueryRequestDTO();
		StringBuilder builder = new StringBuilder();

		builder.append(
				" select a.org_id ,a.customer_id as lead_id ,a.so_id ,a.do_id ,a.do_code ,a.ffm_code ,a.tracking_code , b.name as customer_name , ");
		builder.append(
				" b.phone as customer_phone ,e.product ,d.shortname as source ,f.fullname as agent ,b.address ,i.name as wards ,h.name as district ,g.name as province , ");
		builder.append(" b.comment ,j.shortname as fulfillment ,k.shortname as carrier ,l.name as lead_status , ");
		builder.append(
				" m.name as so_status ,n.name as do_status ,c.createdate as so_createdate ,c.modifydate as so_updatedate ,a.createdate as do_createdate ,a.updatedate as do_updatedate , ");
		builder.append(" b.total_call ,o.name as payment_method , ");
		builder.append(
				" c.amount ,case when a.error_message like '%message%' then replace(replace(split_part(error_message,',',2),'\"message\":',''),'\"','')  else a.error_message end as error_message ");
		builder.append(" from od_do_new a ");
		builder.append(" left join cl_fresh b on a.customer_id=b.lead_id  ");
		builder.append(" left join od_sale_order c on a.so_id=c.so_id ");
		builder.append(" left join bp_partner d on b.agc_id = d.pn_id ");
		builder.append(
				" left join (select x.so_id, string_agg(y.name||' ('||quantity||')' , E', \\n' order by oi_id ) as product ");
		builder.append(
				" from od_so_item x join pd_product y on x.prod_id=y.prod_id group by so_id) e on c.so_id=e.so_id ");
		builder.append(" left join or_user f on f.user_id=c.ag_id  ");
		builder.append(" left join lc_province g on g.prv_id = cast(b.province as integer) ");
		builder.append(" left join lc_district h on h.dt_id = cast(b.district as integer) ");
		builder.append(" left join lc_subdistrict i on i.sdt_id = cast(b.subdistrict as integer) ");
		builder.append(" left join bp_partner j on a.ffm_id = j.pn_id ");
		builder.append(" left join bp_partner k on a.carrier_id = k.pn_id ");
		builder.append(" left join cf_synonym l on l.value = b.lead_status and l.type_id=1 ");
		builder.append(" left join cf_synonym m on m.value = c.status and m.type_id=4 ");
		builder.append(" left join cf_synonym n on n.value = a.status and n.type_id=5 ");
		builder.append(" left join cf_synonym o on o.value = c.payment_method and o.type_id=29 ");
		builder.append(" where  1=1 ");
		StringBuilder counter = new StringBuilder("select count(*) from ( ");

		/* add filters */
		if(!ObjectUtils.isNull(shippingRequest.getOrgId()))
			builder.append(" and a.org_id = :orgId ");

		if(!StringUtils.isEmpty(shippingRequest.getLeadId()))
			builder.append(" and a.customer_id in :leadId ");

		if(!StringUtils.isEmpty(shippingRequest.getSoId()))
			builder.append(" and a.so_id in :soId");

		if(!StringUtils.isEmpty(shippingRequest.getDoId()))
			builder.append(" and a.do_id in :doId ");

		if(!StringUtils.isEmpty(shippingRequest.getDoCode()))
			builder.append(" and a.do_code = :doCode");

		if(!StringUtils.isEmpty(shippingRequest.getFfmCode()))
			builder.append(" and a.ffm_code = :ffmCode");

		if(!StringUtils.isEmpty(shippingRequest.getTrackingCode()))
			builder.append(" and a.tracking_code = :trackingCode ");

		if(!StringUtils.isEmpty(shippingRequest.getCustomerName()))
			builder.append(" and lower(b.name) like :customerName ");

		if(!StringUtils.isEmpty(shippingRequest.getCustomerPhone()))
			builder.append(" and b.phone like :customerPhone ");

		if(!StringUtils.isEmpty(shippingRequest.getProduct()))
			builder.append(" and lower(e.product) like :productName ");

		if(!StringUtils.isEmpty(shippingRequest.getSource()))
			builder.append(" and lower(d.shortname) like :source ");

		if(!StringUtils.isEmpty(shippingRequest.getAgent()))
			builder.append(" and lower(f.fullname) like :agent ");

		if(!StringUtils.isEmpty(shippingRequest.getAddress()))
			builder.append(" and lower(b.address) like :address ");

		if(!StringUtils.isEmpty(shippingRequest.getWards()))
			builder.append(" and lower(i.name) like :ward ");

		if(!StringUtils.isEmpty(shippingRequest.getDistrict()))
			builder.append(" and lower(h.name) like :district ");

		if(!StringUtils.isEmpty(shippingRequest.getProvince()))
			builder.append(" and lower(g.name) like :province ");

		if(!StringUtils.isEmpty(shippingRequest.getComment()))
			builder.append(" and lower(b.comment) like :comment ");

		if(!StringUtils.isEmpty(shippingRequest.getFulfillment()))
			builder.append(" and lower(j.shortname) like :fulfillment ");

		if(!StringUtils.isEmpty(shippingRequest.getCarrier()))
			builder.append(" and lower(k.shortname) like :carrier ");

		if(!StringUtils.isEmpty(shippingRequest.getLeadStatus()))
			builder.append(" and l.name in :leadStatus ");

		if(!StringUtils.isEmpty(shippingRequest.getSoStatus()))
			builder.append(" and m.name in :soStatus ");

		if(!StringUtils.isEmpty(shippingRequest.getDoStatus()))
			builder.append(" and n.name = :doStatus ");

		if(!StringUtils.isEmpty(shippingRequest.getSoCreatedate()))
			builder.append(
					" and ( c.createdate  >= to_timestamp(split_part(:soCreateDate,'|',1),'yyyymmddhh24miss')) and ( c.createdate   <= to_timestamp(split_part(:soCreateDate,'|',2),'yyyymmddhh24miss')) ");

		if(!StringUtils.isEmpty(shippingRequest.getSoUpdatedate()))
			builder.append(
					" and ( c.modifydate  >= to_timestamp(split_part(:soUpdateDate,'|',1),'yyyymmddhh24miss')) and ( c.modifydate   <= to_timestamp(split_part(:soUpdateDate,'|',2),'yyyymmddhh24miss')) ");

		if(!StringUtils.isEmpty(shippingRequest.getDoCreatedate()))
			builder.append(
					" and ( a.createdate  >= to_timestamp(split_part(:doCreateDate,'|',1),'yyyymmddhh24miss')) and ( a.createdate   <= to_timestamp(split_part(:doCreateDate,'|',2),'yyyymmddhh24miss')) ");

		if(!StringUtils.isEmpty(shippingRequest.getDoUpdatedate()))
			builder.append(
					" and ( a.updatedate  >= to_timestamp(split_part(:doUpdateDate,'|',1),'yyyymmddhh24miss')) and ( a.updatedate   <= to_timestamp(split_part(:doUpdateDate,'|',2),'yyyymmddhh24miss')) ");

		if(!ObjectUtils.isNull(shippingRequest.getTotalCall()))
			builder.append(" and b.total_call = :totalCall ");

		if(!StringUtils.isEmpty(shippingRequest.getPaymentMethod()))
			builder.append(" and o.name  = :paymentMethod ");

		if(!ObjectUtils.isNull(shippingRequest.getAmount()))
			builder.append(" and c.amount = :amount ");

		if(!StringUtils.isEmpty(shippingRequest.getErrorMessage()))
			builder.append(" and a.error_message like :errorMessage ");

		counter.append(builder);
		counter.append(" ) as numberTotal");
		builder.append(" order by a.do_id  desc ");
		if(!ObjectUtils.isNull(shippingRequest.getLimit()))
			builder.append(" limit :limit ");

		if(!ObjectUtils.isNull(shippingRequest.getOffset()))
			builder.append(" offset :offset ");

		queryRequestDTO.setQuery(builder.toString());
		queryRequestDTO.setQueryAsCounters(counter.toString());
		return queryRequestDTO;
	}

	@Override
	public TMSResponse<Boolean> cancelDO(String ssId, Integer orgId, Integer userId, Integer doId) {
		GetDoNewV11 getDoNew = new GetDoNewV11();
		getDoNew.setOrgId(orgId);
		getDoNew.setDoId(doId);
		DBResponse<List<DeliveriesOrderResponseDTO>> dbResponse = deliveryOrderService.getDoNewV11(ssId, getDoNew);
		if (CollectionUtils.isEmpty(dbResponse.getResult()))
			return TMSResponse.buildResponse(Boolean.FALSE, ErrorMessage.DELIVERY_ORDER_NOT_FOUND);

		DeliveriesOrderResponseDTO doNew = dbResponse.getResult().get(0);
		if (doNew.getFfmId() == null || doNew.getCarrierId() == null)
			return TMSResponse.buildResponse(Boolean.FALSE, ErrorMessage.DO_HAVE_FFM_ID_OR_CARRIER_ID_NULL);
		if (checkFinalDOStatus(doNew.getStatus()))
			return TMSResponse.buildResponse(Boolean.FALSE, ErrorMessage.CAN_NOT_CANCEL_DO_WITH_FINAL_STATUS);

		OrderResult orderResultFFM = null;
		OrderResult orderResultLM = null;
		Integer lmId = doNew.getCarrierId();
		Integer ffmId = doNew.getFfmId();

		try {
			/* Cancel lastmile first then fulfillment */
			switch (lmId) {
				case Const.BM_PARTNER_ID:
					/* If ffm is BoxMe, just need cancel BoxMe ffm */
					if (ffmId == Const.BM_PARTNER_ID) {
						orderResultFFM = DeliveryHelper.cancelDelivery(ffmId, doNew.getFfmCode(), orgId);
						orderResultLM = orderResultFFM;
						break;
					}
					break;
				case Const.DHL_PARTNER_ID:
					/* If ffm is DHL_FFM, just need cancel DHL_FFM */
					if (ffmId == Const.DHL_FFM_PARTNER_ID) {
						orderResultFFM = DeliveryHelper.cancelDelivery(ffmId, doNew.getFfmCode(), orgId);
						orderResultLM = orderResultFFM;
						break;
					}

					orderResultLM = DeliveryHelper.cancelDelivery(lmId, doNew.getDoCode(), orgId);
					if (orderResultLM.getResult().equals(Const.DO_CANCEL_SUCCESS)) {
						if (ffmId == Const.BM_PARTNER_ID) {
							orderResultFFM = DeliveryHelper.cancelDelivery(ffmId, doNew.getFfmCode(), orgId);
						} else if (ffmId == Const.WFS_PARTNER_ID) {
							String wareHouseCodeInPartner = getWareHouseCodeInPartner(ssId, doNew.getWarehouseId());
							orderResultFFM = DeliveryHelper.cancelDelivery(ffmId, doNew.getDoCode(), wareHouseCodeInPartner, orgId);
						}
					}
					break;
				case Const.SNAPPY_PARTNER_ID:
					orderResultLM = DeliveryHelper.cancelDelivery(lmId, doNew.getTrackingCode(), orgId);
					if (orderResultLM.getResult().equals(Const.DO_CANCEL_SUCCESS)) {
						if (ffmId == Const.WFS_PARTNER_ID) {
							String wareHouseCodeInPartner = getWareHouseCodeInPartner(ssId, doNew.getWarehouseId());
							orderResultFFM = DeliveryHelper.cancelDelivery(ffmId, doNew.getDoCode(), wareHouseCodeInPartner, orgId);
						} else if (ffmId == Const.BM_PARTNER_ID) {
							orderResultFFM = DeliveryHelper.cancelDelivery(ffmId, doNew.getFfmCode(), orgId);
						}
					}
					break;
				case Const.GHN_PARTNER_ID:
					/* If ffm is GHN ffm, just need cancel GHN ffm (WFS is GHN ffm) */
					if (ffmId == Const.WFS_PARTNER_ID) {
						String wareHouseCodeInPartner = getWareHouseCodeInPartner(ssId, doNew.getWarehouseId());
						orderResultFFM = DeliveryHelper.cancelDelivery(ffmId, doNew.getDoCode(), wareHouseCodeInPartner, orgId);
						orderResultLM = orderResultFFM;
						break;
					}
					break;
				case Const.SAP_LM_PARTNER_ID:
					/* If ffm is SAP, just need cancel SAP ffm */
					if (ffmId == Const.SAP_FFM_PARTNER_ID) {
						orderResultFFM = DeliveryHelper.cancelDelivery(lmId, doNew.getTrackingCode(), orgId);
						orderResultLM = orderResultFFM;
						break;
					}
				default:
					return TMSResponse.buildResponse(Boolean.FALSE, ErrorMessage.CARRIER_NOT_FOUND);
			}
		} catch (TMSException e) {
			logger.error(e.getMessage(), e);
			return TMSResponse.buildResponse(Boolean.FALSE, ErrorMessage.THREE_PLS_NOT_FOUND);
		}

		boolean isSuccessCancelLM = false;
		boolean isSuccessCancelFFM = false;
		if (orderResultLM != null && orderResultLM.getResult().equals(Const.DO_CANCEL_SUCCESS))
			isSuccessCancelLM = true;

		if (orderResultFFM != null && orderResultFFM.getResult().equals(Const.DO_CANCEL_SUCCESS)) {
			isSuccessCancelFFM = true;
		}

		StringBuilder message = new StringBuilder();
		if (isSuccessCancelLM) {
			message.append("Success cancel DO in LM");
			if (!isSuccessCancelFFM) {
				message.append(". Error cancel FFM: ");
				if (orderResultFFM != null)
					message.append(orderResultFFM.getMessage());
				else
					message.append(Const.FAIL_GET_RESPONSE_FROM_3PL);
			} else {
				message.append(" and FFM.");
			}
		} else {
			message.append("Error cancel LM: ");
			if (orderResultLM != null)
				message.append(orderResultLM.getMessage());
			else
				message.append(Const.FAIL_GET_RESPONSE_FROM_3PL);
		}

		logger.info("MESSAGE CANCEL ORDER do_id = " + doId + ": " + message);
		if (!isSuccessCancelLM || !isSuccessCancelFFM)
			return TMSResponse.buildResponse(Boolean.FALSE, 0, message.toString(), 200);

		UpdDoNewV8 updDoNew = new UpdDoNewV8();
		updDoNew.setDoId(doId);
		updDoNew.setStatus(EnumType.DO_STATUS_ID.CANCEL.getValue());
		updDoNew.setUpdateby(userId);

		logService.updDoNewV8(ssId, updDoNew);

		return TMSResponse.buildResponse(Boolean.TRUE, 0, message.toString(), 200);
	}

	private boolean checkFinalDOStatus(Integer doStatus) {
		if (doStatus == null)
			return false;
		return doStatus == EnumType.DO_STATUS_ID.DELIVERED.getValue() ||
				doStatus == EnumType.DO_STATUS_ID.RETURNED.getValue();
	}

	private String getWareHouseCodeInPartner(String ssId, Integer warehouseId) {
		GetWareHouse getWareHouse = new GetWareHouse();
		getWareHouse.setWarehouseId(warehouseId.toString());
		DBResponse<List<GetWareHouseResp>> dbResponseGetWareHouse = clProductService.getWareHouse(ssId, getWareHouse);

		if (dbResponseGetWareHouse == null || CollectionUtils.isEmpty(dbResponseGetWareHouse.getResult()))
			return null;

		return dbResponseGetWareHouse.getResult().get(0).getWhCodeInpartner();
	}
}
