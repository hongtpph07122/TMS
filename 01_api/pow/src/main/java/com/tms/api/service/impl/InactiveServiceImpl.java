package com.tms.api.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tms.api.dto.OrderManagermentRespDto;
import com.tms.api.entity.CLInactive;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.EnumType;
import com.tms.api.repository.ClInactiveRepository;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.InactiveService;
import com.tms.api.task.DBLogWriter;
import com.tms.dto.CLInactiveMoveTrash;
import com.tms.dto.DBResponse;
import com.tms.dto.GetOrderManagement7;
import com.tms.dto.GetOrderManagement7Resp;
import com.tms.entity.CLFresh;
import com.tms.entity.User;
import com.tms.entity.log.InsCLFreshV6;
import com.tms.service.impl.LogService;

@Service
public class InactiveServiceImpl implements InactiveService {

	private static final Logger logger = LoggerFactory.getLogger(InactiveServiceImpl.class);

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	DBLogWriter dbLog;

	@Autowired
	ClInactiveRepository clInactiveRepository;

	@Autowired
	LogService logService;

	@Override
	public OrderManagermentRespDto searchByPage(GetOrderManagement7 params, boolean isExport) {

		params.setLimit(params.getLimit() != null? params.getLimit() : Const.DEFAULT_PAGE_SIZE);
		if(params.getModifyDate() == null && (params.getCreatedate() == null || params.getCreatedate().isEmpty())){
			java.time.LocalDateTime beforeDate = java.time.LocalDateTime.now().minusDays(30);
			java.time.LocalDateTime now = java.time.LocalDateTime.now();
			java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
					.ofPattern("yyyyMMddHHmmss");
			params.setCreatedate(beforeDate.format(formatter) + "|" + now.format(formatter));
		}

		StringBuilder getOrderSql = new StringBuilder();
		getOrderSql.append(
				" select d.org_id ,a.so_id ,e.do_id ,e.do_code ,d.lead_id ,d.name as lead_name ,d.phone as lead_phone , d.prod_name as product_name,b.product as product_crosssell  ,a.amount ,d.agc_id as source , ");
		getOrderSql.append(
				" coalesce(o.shortname,d.agc_code) as source_name ,d.agc_code as source_code ,d.assigned as ag_id ,p.user_name as ag_name ,d.comment ,coalesce(d.address||'###'||n.name||'-'||m.name||'-'||l.name ,d.address) as address , ");
		getOrderSql.append(
				" e.ffm_id ,f.shortname asffm_name ,e.warehouse_id ,j.warehouse_shortname as warehouse_name , ");
		getOrderSql.append(
				" e.carrier_id as lastmile_id ,k.shortname as lastmile_name ,case a.payment_method when 1 then 'COD' when 2 then 'Banking transfer' end as payment_method, ");
		getOrderSql.append(
				" d.lead_status as lead_status_id ,g.name as lead_status , a.status as so_status_id ,h.name as order_status , ");
		getOrderSql.append(" e.status as do_status_id ,i.name as delivery_status ,d.lead_type ,null lead_type_name , ");
		getOrderSql.append(" d.createdate, d.modifydate as modify_date ,d.total_call , ");
		getOrderSql.append(
				" d.cp_id ,q.name as cp_name ,d.callinglist_id ,r.cl_name as callinglist_name ,1 group_id, 'null' group_name  ");
		getOrderSql.append(" , d.affiliate_id, d.user_defin_05 as reason, d.click_id, d.agent_note ");
		if(isExport)
			getOrderSql.append(" , cfs.name as lead_postback_status ");
		getOrderSql.append(
				" from (select org_id, lead_id, name, phone,prod_id, prod_name, total_call,lead_status, lead_type, agc_id, agc_code, ");
		getOrderSql.append(
				" comment, address, province, district, subdistrict, assigned, createdate, modifydate, cp_id, callinglist_id,");
		getOrderSql.append(" affiliate_id, user_defin_05,click_id,agent_note from cl_inactive) d ");
		getOrderSql.append(" left join od_sale_order a on a.lead_id=d.lead_id ");
		getOrderSql.append(
				" left join (select b.so_id, string_agg(c.name||' ('||quantity||')' , E', \\n' order by oi_id ) as product ");
		getOrderSql.append(
				" from od_so_item b join pd_product c on b.prod_id=c.prod_id group by so_id) b on a.so_id=b.so_id ");
		getOrderSql.append(" left join od_do_new e on a.so_id=e.so_id ");
		getOrderSql.append(" left join bp_partner f on e.ffm_id = f.pn_id ");
		getOrderSql.append(" left join bp_warehouse j on e.warehouse_id = j.warehouse_id ");
		getOrderSql.append(" left join bp_partner k on e.carrier_id = k.pn_id ");
		getOrderSql.append(" left join bp_partner o on d.agc_id = o.pn_id ");
		getOrderSql
				.append(" left join (select * from cf_synonym where type ='lead status') g on d.lead_status=g.value ");
		getOrderSql
				.append(" left join (select * from cf_synonym where type ='sale order status') h on a.status=h.value ");
		getOrderSql.append(
				" left join (select * from cf_synonym where type ='delivery order status') i on e.status=i.value ");
		getOrderSql.append(" left join or_user p on p.user_id=d.assigned ");
		getOrderSql.append(" left join lc_province l on l.prv_id = cast(d.province as integer) ");
		getOrderSql.append(" left join lc_district m on m.dt_id = cast(d.district as integer) ");
		getOrderSql.append(" left join lc_subdistrict n on n.sdt_id = cast(d.subdistrict as integer) ");
		getOrderSql.append(" left join cp_campaign q on q.cp_id = d.cp_id ");
		getOrderSql.append(" left join cl_calling_list r on r.callinglist_id = d.callinglist_id ");
		if(isExport){
			getOrderSql.append(" left join cl_postback clpb on clpb.lead_id = d.lead_id ");
			getOrderSql.append(
					" left join (select name, value from cf_synonym where type_id = 33) cfs on clpb.status_postback = cfs.value");
		}
		getOrderSql.append(" where  1=1 ");

		StringBuilder countOrder = new StringBuilder("select count(lead_id) from ( ");

		if(params.getLeadStatusId() != null)
			getOrderSql.append(" and d.lead_status = :leadStatusId ");

		if(params.getOrgId() != null)
			getOrderSql.append(" and d.org_id = :orgId ");
		if(params.getSoId() != null)
			getOrderSql.append(" and a.so_id = :soId ");
		if(params.getDoId() != null)
			getOrderSql.append(" and e.do_id = :doId ");
		if(params.getDoCode() != null)
			getOrderSql.append(" and e.do_code = :doCode ");
		if(params.getLeadId() != null)
			getOrderSql.append(" and d.lead_id = :leadId ");
		if(params.getLeadName() != null)
			getOrderSql.append(" and lower(d.name) like :leadName ");
		if(params.getLeadPhone() != null)
			getOrderSql.append(" and lower(d.phone) like :leadPhone ");
		if(params.getProductName() != null)
			getOrderSql.append(" and lower(d.prod_name) like :prodName ");
		if(params.getProductCrosssell() != null)
			getOrderSql.append(" and b.product like :productCrosssell ");
		if(params.getAmount() != null)
			getOrderSql.append(" and a.amount = :amount ");
		if(params.getSource() != null)
			getOrderSql.append(" and d.agc_id = :source ");
		if(params.getSourceName() != null)
			getOrderSql.append(" and LOWER(coalesce(o.shortname,d.agc_code)) like :sourceName ");
		if(params.getAgId() != null)
			getOrderSql.append(" and d.assigned = :agId ");
		if(params.getAgName() != null)
			getOrderSql.append(" and p.user_name like :agName ");
		if(params.getComment() != null)
			getOrderSql.append(" and lower(d.comment) like :comment ");
		if(params.getAddress() != null)
			getOrderSql.append(" and d.address||'-'||n.name||'-'||m.name||'-'||l.name like :address ");
		if(params.getFfmId() != null)
			getOrderSql.append(" and e.ffm_id = :ffmId ");
		if(params.getFfmName() != null)
			getOrderSql.append(" and  lower(f.shortname) = :shortName ");
		if(params.getWarehouseId() != null)
			getOrderSql.append(" and e.warehouse_id = :warehouseId ");
		if(params.getWarehouseName() != null)
			getOrderSql.append(" and lower(j.warehouse_shortname) = :warehouseName ");
		if(params.getLastmileId() != null)
			getOrderSql.append(" and e.carrier_id = :lastmieId ");
		if(params.getLastmileName() != null)
			getOrderSql.append(" and lower(k.shortname) like :lastmieName ");
		if(params.getPaymentMethod() != null)
			getOrderSql.append(" and a.payment_method = :paymentMethod ");
		if(params.getLeadStatusId() != null)
			getOrderSql.append(" and d.lead_status = :leadStatusId ");
		if(params.getLeadStatus() != null)
			getOrderSql.append(" and g.name in :leadStatus ");
		if(params.getSoStatusId() != null)
			getOrderSql.append(" and a.status = :soStatusId ");
		if(params.getOrderStatus() != null)
			getOrderSql.append(" and h.name = :orderStatus ");
		if(params.getDoStatusId() != null)
			getOrderSql.append(" and e.status = :doStatusId ");
		if(params.getDeliveryStatus() != null)
			getOrderSql.append(" and i.name = :deliverStatus ");
		if(params.getLeadType() != null)
			getOrderSql.append(" and d.lead_type = :leadType ");
		if(params.getCreatedate() != null)
			getOrderSql.append(
					" and d.createdate  >= to_timestamp(split_part(:createDate,'|',1),'yyyymmddhh24miss') and d.createdate   <= to_timestamp(split_part(:createDate,'|',2),'yyyymmddhh24miss') ");
		if(params.getModifyDate() != null)
			getOrderSql.append(
					" and d.modifydate  >= to_timestamp(split_part(:modifyDate,'|',1),'yyyymmddhh24miss') and d.modifydate   <= to_timestamp(split_part(:modifyDate,'|',2),'yyyymmddhh24miss') ");
		if(params.getTotalCall() != null)
			getOrderSql.append(" and total_call = :totalCall ");
		if(params.getCpId() != null)
			getOrderSql.append(" and d.cp_id = :cpId ");
		if(params.getCpIds() != null)
			getOrderSql.append(" and d.cp_id in :cpIds ");
		if(params.getCpName() != null)
			getOrderSql.append(" and  lower(q.name) like  :cpName ");
		if(params.getCallinglistId() != null)
			getOrderSql.append(" and d.callinglist_id = :callingListId ");
		if(params.getCallinglistName() != null)
			getOrderSql.append(" and r.cl_name like :callingListName ");
		if(params.getAffiliateId() != null)
			getOrderSql.append(" and d.affiliate_id like :affiliateId ");
		if(params.getReason() != null)
			getOrderSql.append(" and d.user_defin_05 like :reason ");
		if(params.getAgentNote() != null)
			getOrderSql.append(" and lower(d.agent_note) like :agentNote ");
		countOrder.append(getOrderSql);
		countOrder.append(" ) as a");
		getOrderSql.append(" order by d.createdate desc, lead_id desc ");
		if(params.getLimit() != null)
			getOrderSql.append(" limit :limit ");
		if(params.getOffset() != null)
			getOrderSql.append(" offset :offset ");

		Query query = entityManager.createNativeQuery(getOrderSql.toString());
		Query query1 = entityManager.createNativeQuery(countOrder.toString());

		if(params.getLeadStatusId() != null){
			query.setParameter("leadStatusId", params.getLeadStatusId());
			query1.setParameter("leadStatusId", params.getLeadStatusId());
		}
		if(params.getOrgId() != null){
			query.setParameter("orgId", params.getOrgId());
			query1.setParameter("orgId", params.getOrgId());
		}
		if(params.getSoId() != null){
			query.setParameter("soId", params.getSoId());
			query1.setParameter("soId", params.getSoId());
		}
		if(params.getDoId() != null){
			query.setParameter("doId", params.getDoId());
			query1.setParameter("doId", params.getDoId());
		}
		if(params.getDoCode() != null){
			query.setParameter("doCode", params.getDoCode());
			query1.setParameter("doCode", params.getDoCode());
		}
		if(params.getLeadId() != null){
			query.setParameter("leadId", params.getLeadId());
			query1.setParameter("leadId", params.getLeadId());
		}
		if(params.getLeadName() != null){
			query.setParameter("leadName", "%" + params.getLeadName().toLowerCase() + "%");
			query1.setParameter("leadName", "%" + params.getLeadName().toLowerCase() + "%");
		}
		if(params.getLeadPhone() != null){
			query.setParameter("leadPhone", "%" + params.getLeadPhone() + "%");
			query1.setParameter("leadPhone", "%" + params.getLeadPhone() + "%");
		}
		if(params.getProductName() != null){
			query.setParameter("prodName", "%" + params.getProductName().toLowerCase() + "%");
			query1.setParameter("prodName", "%" + params.getProductName().toLowerCase() + "%");
		}
		if(params.getProductCrosssell() != null){
			query.setParameter("productCrosssell", "%" + params.getProductCrosssell().toLowerCase() + "%");
			query1.setParameter("productCrosssell", "%" + params.getProductCrosssell().toLowerCase() + "%");
		}
		if(params.getAmount() != null){
			query.setParameter("amount", params.getAmount());
			query1.setParameter("amount", params.getAmount());
		}
		if(params.getSource() != null){
			query.setParameter("source", params.getSource());
			query1.setParameter("source", params.getSource());
		}
		if(params.getSourceName() != null){
			query.setParameter("sourceName", "%" + params.getSourceName().toLowerCase() + "%");
			query1.setParameter("sourceName", "%" + params.getSourceName().toLowerCase() + "%");
		}
		if(params.getAgId() != null){
			query.setParameter("agId", params.getAgId());
			query1.setParameter("agId", params.getAgId());
		}
		if(params.getAgName() != null){
			query.setParameter("agName", "%" + params.getAgName().toLowerCase() + "%");
			query1.setParameter("agName", "%" + params.getAgName().toLowerCase() + "%");
		}
		if(params.getComment() != null){
			query.setParameter("comment", params.getComment().toLowerCase());
			query1.setParameter("comment", params.getComment().toLowerCase());
		}
		if(params.getAddress() != null){
			query.setParameter("address", "%" + params.getAddress() + "%");
			query1.setParameter("address", "%" + params.getAddress() + "%");
		}
		if(params.getFfmId() != null){
			query.setParameter("ffmId", params.getFfmId());
			query1.setParameter("ffmId", params.getFfmId());
		}
		if(params.getFfmName() != null){
			query.setParameter("ffmName", params.getFfmName().toLowerCase());
			query1.setParameter("ffmName", params.getFfmName().toLowerCase());
		}
		if(params.getWarehouseId() != null){
			query.setParameter("warehouseId", params.getWarehouseId());
			query1.setParameter("warehouseId", params.getWarehouseId());
		}
		if(params.getWarehouseName() != null){
			query.setParameter("warehouseName", params.getWarehouseName().toLowerCase());
			query1.setParameter("warehouseName", params.getWarehouseName().toLowerCase());
		}
		if(params.getLastmileId() != null){
			query.setParameter("lastmieId", params.getLastmileId());
			query1.setParameter("lastmieId", params.getLastmileId());
		}
		if(params.getLastmileName() != null){
			query.setParameter("lastmieName", "%" + params.getLastmileName().toLowerCase() + "%");
			query1.setParameter("lastmieName", "%" + params.getLastmileName().toLowerCase() + "%");
		}
		if(params.getPaymentMethod() != null)
			if(params.getPaymentMethod().compareTo("COD") == 0){
				query.setParameter("paymentMethod", 1);
				query1.setParameter("paymentMethod", 1);
			} else if(params.getPaymentMethod().compareTo("Banking transfer") == 0){
				query.setParameter("paymentMethod", 2);
				query1.setParameter("paymentMethod", 2);
			} else if(params.getPaymentMethod().compareTo("Deposit") == 0){
				query.setParameter("paymentMethod", 3);
				query1.setParameter("paymentMethod", 3);
			}
		if(params.getLeadStatusId() != null){
			query.setParameter("leadStatusId", params.getLeadStatusId());
			query1.setParameter("leadStatusId", params.getLeadStatusId());
		}
		if(params.getLeadStatus() != null){
			List<String> status = Arrays.asList(params.getLeadStatus().split(","));
			query.setParameter("leadStatus", status);
			query1.setParameter("leadStatus", status);
		}
		if(params.getSoStatusId() != null){
			query.setParameter("soStatusId", params.getSoStatusId());
			query1.setParameter("soStatusId", params.getSoStatusId());
		}
		if(params.getOrderStatus() != null){
			query.setParameter("orderStatus", params.getOrderStatus());
			query1.setParameter("orderStatus", params.getOrderStatus());
		}
		if(params.getDoStatusId() != null){
			query.setParameter("doStatusId", params.getDoStatusId());
			query1.setParameter("doStatusId", params.getDoStatusId());
		}
		if(params.getDeliveryStatus() != null){
			query.setParameter("deliverStatus", params.getDeliveryStatus());
			query1.setParameter("deliverStatus", params.getDeliveryStatus());
		}
		if(params.getLeadType() != null){
			query.setParameter("leadType", params.getLeadType());
			query1.setParameter("leadType", params.getLeadType());
		}
		if(params.getCreatedate() != null){
			query.setParameter("createDate", params.getCreatedate());
			query1.setParameter("createDate", params.getCreatedate());
		}
		if(params.getModifyDate() != null){
			query.setParameter("modifyDate", params.getModifyDate());
			query1.setParameter("modifyDate", params.getModifyDate());
		}
		if(params.getTotalCall() != null){
			query.setParameter("totalCall", params.getTotalCall());
			query1.setParameter("totalCall", params.getTotalCall());
		}
		if(params.getCpId() != null){
			query.setParameter("cpId", params.getCpId());
			query1.setParameter("cpId", params.getCpId());
		}
		if(params.getCpIds() != null){
			query.setParameter("cpIds", params.getCpIds());
			query1.setParameter("cpIds", params.getCpIds());
		}
		if(params.getCpName() != null){
			query.setParameter("cpName", "%" + params.getCpName().toLowerCase() + "%");
			query1.setParameter("cpName", "%" + params.getCpName().toLowerCase() + "%");
		}
		if(params.getCallinglistId() != null){
			query.setParameter("callinglistId", params.getCallinglistId());
			query1.setParameter("callinglistId", params.getCallinglistId());
		}
		if(params.getCallinglistName() != null){
			query.setParameter("callinglistName", "%" + params.getCallinglistName().toLowerCase() + "%");
			query1.setParameter("callinglistName", "%" + params.getCallinglistName().toLowerCase() + "%");
		}
		if(params.getAffiliateId() != null){
			query.setParameter("affiliateId", "%" + params.getAffiliateId() + "%");
			query1.setParameter("affiliateId", "%" + params.getAffiliateId() + "%");
		}
		if(params.getReason() != null){
			query.setParameter("reason", "%" + params.getReason().toLowerCase() + "%");
			query1.setParameter("reason", "%" + params.getReason().toLowerCase() + "%");
		}
		if(params.getAgentNote() != null){
			query.setParameter("agentNote", params.getAgentNote().toLowerCase());
			query1.setParameter("agentNote", params.getAgentNote().toLowerCase());
		}
		if(params.getLimit() != null)
			query.setParameter("limit", params.getLimit());
		if(params.getOffset() != null)
			query.setParameter("offset", params.getOffset());

		List<GetOrderManagement7Resp> listOrder = new ArrayList<>();
		List<Object[]> rows = query.getResultList();
		for(Object[] row: rows){
			GetOrderManagement7Resp order = new GetOrderManagement7Resp();
			if(row[0] != null)
				order.setOrgId((Integer) row[0]);
			if(row[1] != null)
				order.setSoId((Integer) row[1]);
			if(row[2] != null)
				order.setDoId((Integer) row[2]);
			if(row[3] != null)
				order.setDoCode((String) row[3]);
			if(row[4] != null)
				order.setLeadId((Integer) row[4]);
			if(row[5] != null)
				order.setLeadName((String) row[5]);
			if(row[6] != null)
				order.setLeadPhone((String) row[6]);
			if(row[7] != null)
				order.setProductName((String) row[7]);
			if(row[8] != null)
				order.setProductCrosssell((String) row[8]);
			if(row[9] != null){
				BigDecimal amount = (BigDecimal) row[9];
				order.setAmount(amount.doubleValue());
			}
			if(row[10] != null)
				order.setSource((Integer) row[10]);
			if(row[11] != null)
				order.setSourceName((String) row[11]);
			if(row[12] != null)
				order.setSourceCode((String) row[12]);
			if(row[13] != null)
				order.setAgId((Integer) row[13]);
			if(row[14] != null)
				order.setAgName((String) row[14]);
			if(row[15] != null)
				order.setComment((String) row[15]);
			if(row[16] != null)
				order.setAddress((String) row[16]);
			if(row[17] != null)
				order.setFfmId((Integer) row[17]);
			if(row[18] != null)
				order.setFfmName((String) row[18]);
			if(row[19] != null)
				order.setWarehouseId((Integer) row[19]);
			if(row[20] != null)
				order.setWarehouseName((String) row[20]);
			if(row[21] != null)
				order.setLastmileId((Integer) row[21]);
			if(row[22] != null)
				order.setLastmileName((String) row[22]);
			if(row[23] != null)
				order.setPaymentMethod((String) row[23]);
			if(row[24] != null)
				order.setLeadStatusId((Integer) row[24]);
			if(row[25] != null)
				order.setLeadStatus((String) row[25]);
			if(row[26] != null)
				order.setSoStatusId((Integer) row[26]);
			if(row[27] != null)
				order.setOrderStatus((String) row[27]);
			if(row[28] != null)
				order.setDoStatusId((Integer) row[28]);
			if(row[29] != null)
				order.setDeliveryStatus((String) row[29]);
			if(row[30] != null)
				order.setLeadType((String) row[30]);
			if(row[31] != null)
				order.setLeadTypeName((String) row[31]);
			if(row[32] != null)
				order.setCreatedate((Date) row[32]);
			if(row[33] != null)
				order.setModifyDate((Date) row[33]);
			if(row[34] != null)
				order.setTotalCall((Integer) row[34]);
			if(row[35] != null)
				order.setCpId((Integer) row[35]);
			if(row[36] != null)
				order.setCpName((String) row[36]);
			if(row[37] != null)
				order.setCallinglistId((Integer) row[37]);
			if(row[38] != null)
				order.setCallinglistName((String) row[38]);
			if(row[39] != null)
				order.setGroupId((Integer) row[39]);
			if(row[40] != null)
				order.setGroupName((String) row[40]);
			if(row[41] != null)
				order.setAffiliateId((String) row[41]);
			if(row[42] != null)
				order.setReason((String) row[42]);
			if(row[43] != null)
				order.setClickId((String) row[43]);
			if(row[44] != null)
				order.setAgentNote((String) row[44]);
			if(isExport && row[45] != null)
				order.setLeadPostbackStatus((String) row[45]);
			listOrder.add(order);
		}

		BigInteger count = (BigInteger) query1.getResultList().get(0);
		Integer rowCount = count.intValue();

		OrderManagermentRespDto result = new OrderManagermentRespDto();
		result.setOrderManagement7Resp(listOrder);
		result.setRowCount(rowCount);

		return result;
	}

	/*
	 * @Override public boolean moveOneTrashToFresh(Integer leadId) { // TODO
	 * Auto-generated method stub if (leadId != null && leadId > 0) {
	 * Optional<CLInactive> entity = clInactiveRepository.findById(leadId); if
	 * (entity != null) { return true; } } return false; }
	 */

	@Override
	public TMSResponse moveListTrashToFresh(String SESSION_ID, User _curUser, CLInactiveMoveTrash dto)
			throws TMSException {
		StringBuilder message = new StringBuilder();
		// TODO Auto-generated method stub
		String formatMessage = "%s : %s : %s <br />";
		String reason = "";
		boolean isSuccess = false;

		List<Integer> leadIds = new ArrayList<>();
		if(dto != null && dto.getLeadIds() != null && dto.getLeadIds().size() > 0)
			leadIds = dto.getLeadIds();
		if(leadIds != null && leadIds.size() > 0){
			DBResponse dbResponse = null;
			for(Integer leadId: leadIds){
				isSuccess = true;
				reason = "";
				CLInactive clInactive = getOneByLeadId(leadId);
				if(clInactive != null && clInactive.getLeadId() != null){
					InsCLFreshV6 clFresh = convertCLInactiveToInsCLFreshV6(clInactive);
					dbResponse = logService.insCLFreshV6(SESSION_ID, clFresh);
					if(dbResponse == null){
						reason = ErrorMessage.LEAD_NOT_FOUND.getMessage();
						logger.info(reason);
						isSuccess = false;
						message.append(String.format(formatMessage, leadId, isSuccess, reason));
						logger.info(message.toString());
						throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
					}
					// insert log
					try{
						int lead_id = Integer.parseInt(dbResponse.getErrorMsg());
						if(lead_id > 0){
							CLFresh lead = new CLFresh();
							ModelMapper modelMapper = new ModelMapper();
							lead = modelMapper.map(clFresh, CLFresh.class);
							lead.setLeadId(leadId);

							dbLog.writeLeadStatusLog(_curUser.getUserId(), leadId, EnumType.LEAD_STATUS.NEW.getValue(),
									"Create Lead by move list trash to fresh");
//							dbLog.writeLeadStatusLogV2(_curUser.getUserId(),lead, leadId, EnumType.LEAD_STATUS.NEW.getValue(),
//									"Create Lead by move list trash to fresh");
							message.append(String.format(formatMessage, leadId, isSuccess, reason));
							logger.info(message.toString());

							// delete cl_inactive
							try{
								logService.delCLInactive(SESSION_ID, leadId);
							} catch(Exception e){
								logger.error("[moveListTrashToFresh] Fail to delete cl_inactive " + leadId + " | "
										+ e.getMessage());
							}
						}
					} catch(Exception ex){
						if(dbResponse != null && StringUtils.hasText(dbResponse.getErrorMsg())){
							isSuccess = false;
							reason = dbResponse.getErrorMsg();
							logger.info(reason);
							message.append(String.format(formatMessage, leadId, isSuccess, reason));
						}
					}
				} else{
					reason = "cl_inactive not found";
					isSuccess = false;
					message.append(String.format(formatMessage, leadId, isSuccess, reason));
					logger.info(message.toString());
				}
			}
		}
		return TMSResponse.buildResponse(message.toString());
	}

	private CLInactive getOneByLeadId(Integer leadId) {
		// TODO Auto-generated method stub
		if(leadId != null && leadId > 0){
			List<CLInactive> list = clInactiveRepository.getAllByLeadId(leadId);
			if(list != null && list.size() > 0 && list.get(0).getLeadId() != null)
				return list.get(0);
		}
		return null;
	}

	private InsCLFreshV6 convertCLInactiveToInsCLFreshV6(CLInactive clInactive) {
		// TODO Auto-generated method stub
		InsCLFreshV6 entity = null;
		if(clInactive != null && clInactive.getLeadId() != null){
			entity = new InsCLFreshV6();
			entity.setOrgId(clInactive.getOrgId());
			entity.setLeadStatus(EnumType.LEAD_STATUS.NEW.getValue());
			entity.setLeadType("A");

			entity.setLeadId(clInactive.getLeadId());
			entity.setAgcId(clInactive.getAgcId());
			entity.setAgcCode(clInactive.getAgcCode());
			entity.setCcCode(clInactive.getCcCode());
			entity.setName(clInactive.getName());
			entity.setPhone(clInactive.getPhone());
			entity.setProdId(clInactive.getProdId());
			entity.setProdName(clInactive.getProdName());
			entity.setAssigned(clInactive.getAssigned());
			entity.setCalledby(clInactive.getCalledby());
			entity.setAddress(clInactive.getAddress());
			entity.setProvince(clInactive.getProvince());
			entity.setDistrict(clInactive.getDistrict());
			entity.setSubdistrict(clInactive.getSubdistrict());
			entity.setComment(clInactive.getComment());
			entity.setLastCallStatus(clInactive.getLastCallStatus());
			entity.setDayCall(clInactive.getDayCall());
			entity.setTotalCall(clInactive.getTotalCall());
			entity.setAmount(clInactive.getAmount());

			entity.setResult(clInactive.getResult());
			entity.setUserDefin01(clInactive.getUserDefin01());
			entity.setUserDefin02(clInactive.getUserDefin02());
			entity.setUserDefin03(clInactive.getUserDefin03());
			entity.setUserDefin04(clInactive.getUserDefin04());
			entity.setUserDefin05(clInactive.getUserDefin05());
			entity.setAttribute(clInactive.getAttribute());

			entity.setCpId(clInactive.getCpId());
			entity.setCallinglistId(clInactive.getCallingListId());
			entity.setAgcLeadAddress(clInactive.getAgcLeadAddress());
			entity.setLastCallTime(
					clInactive.getLastCallTime() != null? clInactive.getLastCallTime().toString() : null);
			entity.setNextCallTime(
					clInactive.getNextCallTime() != null? clInactive.getNextCallTime().toString() : null);
			entity.setNumberOfDay(clInactive.getNumberOfDay());
			entity.setAttemptBusy(clInactive.getAttemptBusy());
			entity.setAttemptNoans(clInactive.getAttemptNoans());
			entity.setAttempUnreachable(clInactive.getAttempUnreachable());
			entity.setAttempOther1(clInactive.getAttempOther1());
			entity.setAttempOther2(clInactive.getAttempOther2());
			entity.setAttempOther3(clInactive.getAttempOther3());
			entity.setClickId(clInactive.getClickId());
			entity.setAffiliateId(clInactive.getAffiliateId());
			entity.setSubid1(clInactive.getSubid1());
			entity.setSubid2(clInactive.getSubid2());
			entity.setSubid3(clInactive.getSubid3());
			entity.setSubid4(clInactive.getSubid4());
			entity.setSubid5(clInactive.getSubid5());
			entity.setNetworkid(clInactive.getNetworkid());
			entity.setPid(clInactive.getPid());
			entity.setTrackingUrlId(clInactive.getTrackingUrlId());
			entity.setOfferId(clInactive.getOfferId());
			entity.setAgcOfferId(clInactive.getAgcOfferId());
			entity.setTerms(clInactive.getTerms());
			entity.setPrice(clInactive.getPrice());
			entity.setUnit(clInactive.getUnit());
			entity.setCustomerAge(clInactive.getCustomerAge());
			entity.setCustomerEmail(clInactive.getCustomerEmail());
			entity.setCustomerComment(clInactive.getCustomerComment());
			entity.setInternalComment(clInactive.getInternalComment());
			entity.setCarrierComment(clInactive.getCarrierComment());
			entity.setNeighborhood(clInactive.getNeighborhood());
			entity.setPostalCode(clInactive.getPostalCode());
			entity.setTrackerId(clInactive.getTrackerId());
		}
		return entity;
	}

}
