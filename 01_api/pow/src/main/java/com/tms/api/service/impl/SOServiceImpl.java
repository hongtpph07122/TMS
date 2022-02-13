package com.tms.api.service.impl;

import com.tms.api.dto.OrderManagement10RespDto;
import com.tms.api.dto.OrderManagement8RespDto;
import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.api.dto.Response.OrderManagementResponseDTO;
import com.tms.api.dto.Response.ValidationResponseDTO;
import com.tms.api.dto.ValidationRespDto;
import com.tms.api.dto.delivery.DeliveryDto;
import com.tms.api.dto.delivery.ProductDto;
import com.tms.api.entity.OdSaleOrder;
import com.tms.api.entity.OrderResult;
import com.tms.api.entity.TrkAffSubnameMapping;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.*;
import com.tms.api.repository.SaleOrderRepository;
import com.tms.api.repository.TrkAffSubnameMappingRepository;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.SOService;
import com.tms.api.task.DBLogWriter;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.*;
import com.tms.entity.*;
import com.tms.entity.log.InsDoNew;
import com.tms.entity.log.UpdDoNew;
import com.tms.service.impl.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SOServiceImpl implements SOService {

    private static final Logger logger = LoggerFactory.getLogger(SOServiceImpl.class);
    private final LogService logService;
    private final StringRedisTemplate stringRedisTemplate;
    private final DBLogWriter dbLog;
	private final DeliveryOrderService deliveryOrderService;
	private final LCProvinceService provinceService;
	private final LCProvinceService lcProvinceService;
	private final CLProductService clProductService;
    private final ModelMapper modelMapper;
    private final CLFreshService clFreshService;
    private final DBLogWriter dbLogWriter;
    private final SaleOrderRepository soRepo;
    private final TrkAffSubnameMappingRepository trkAffSubnameMappingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Value("${config.country}")
    public String _COUNTRY;

    @Value("${config.is-run-subname-aff: false}")
    public Boolean isRunSubnameAff;

    @Autowired
    public SOServiceImpl(ModelMapper modelMapper, CLFreshService clFreshService, DBLogWriter dbLogWriter, LogService logService, StringRedisTemplate stringRedisTemplate, DBLogWriter dbLog, DeliveryOrderService deliveryOrderService, LCProvinceService provinceService, LCProvinceService lcProvinceService, CLProductService clProductService, SaleOrderRepository soRepo
    , TrkAffSubnameMappingRepository trkAffSubnameMappingRepository) {
        this.modelMapper = modelMapper;
        this.clFreshService = clFreshService;
        this.dbLogWriter = dbLogWriter;
        this.logService = logService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.dbLog = dbLog;
        this.deliveryOrderService = deliveryOrderService;
        this.provinceService = provinceService;
        this.lcProvinceService = lcProvinceService;
        this.clProductService = clProductService;
        this.soRepo = soRepo;
        this.trkAffSubnameMappingRepository = trkAffSubnameMappingRepository;
    }

    @Override
    public OrderManagement8RespDto getOrderManagerment(GetOrderManagement8 params, boolean isExport) {

        StringBuilder getOrderSql = new StringBuilder();
        getOrderSql.append(
                " select d.org_id ,a.so_id ,e.do_id ,e.do_code ,d.lead_id ,d.name as lead_name ,d.phone as lead_phone , d.prod_name as product_name,b.product as product_crosssell  ,a.amount ,d.agc_id as source , ");
        getOrderSql.append(
                " coalesce(o.shortname,d.agc_code) as source_name ,d.agc_code as source_code ,d.assigned as ag_id ,p.user_name as ag_name ,d.comment ,coalesce(d.address||'###'||n.name||'-'||m.name||'-'||l.name ,d.address) as address , ");
        getOrderSql.append(
                " e.ffm_id ,f.shortname asffm_name ,e.warehouse_id ,j.warehouse_shortname as warehouse_name , ");
        getOrderSql.append(
                " e.carrier_id as lastmile_id ,k.shortname as lastmile_name ,case a.payment_method when 1 then 'COD' when 2 then 'Banking transfer' when 3 then 'Deposit' when 4 then 'Payment gateway (ESPay)' when 5 then 'Payment gateway (2C2P)' end as payment_method, ");
        getOrderSql.append(
                " d.lead_status as lead_status_id ,g.name as lead_status , a.status as so_status_id ,h.name as order_status , ");
        getOrderSql.append(" e.status as do_status_id ,i.name as delivery_status ,d.lead_type ,null lead_type_name , ");
        getOrderSql.append(" d.createdate, d.modifydate as modify_date ,d.total_call , ");
        getOrderSql.append(
                " d.cp_id ,q.name as cp_name ,d.callinglist_id ,r.cl_name as callinglist_name ,1 group_id, 'null' group_name  ");
        getOrderSql.append(
                " , d.affiliate_id, d.subid1, d.user_defin_05 as reason, d.click_id, d.user_defin_02 as reason_callback, d.agent_note, d.actual_call, d.subid5");
        if (isExport)
            getOrderSql.append(" , cfs.name as lead_postback_status ");
        getOrderSql.append(
                " from (select org_id, lead_id, name, phone,prod_id, prod_name, total_call,lead_status, lead_type, agc_id, agc_code, ");
        getOrderSql.append(
                " comment, address, province, district, subdistrict, assigned, createdate, modifydate, cp_id, callinglist_id,");
        getOrderSql.append(" affiliate_id, subid1, user_defin_05, user_defin_02, click_id, agent_note, actual_call, subid5 from cl_fresh) d ");
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
        if (isExport) {
            getOrderSql.append(" left join cl_postback clpb on clpb.lead_id = d.lead_id ");
            getOrderSql.append(
                    " left join (select name, value from cf_synonym where type_id = 33) cfs on clpb.status_postback = cfs.value");
        }
        getOrderSql.append(" where  1=1 and d.lead_status <> 4 ");

        StringBuilder countOrder = new StringBuilder("select count(lead_id) from ( ");

        if (params.getOrgId() != null)
            getOrderSql.append(" and d.org_id = :orgId ");
        if (params.getSoId() != null)
            getOrderSql.append(" and a.so_id = :soId ");
        if (params.getDoId() != null)
            getOrderSql.append(" and e.do_id = :doId ");
        if (params.getDoCode() != null)
            getOrderSql.append(" and e.do_code = :doCode ");
        if (params.getLeadId() != null)
            getOrderSql.append(" and d.lead_id = :leadId ");
        if (params.getLeadName() != null)
            getOrderSql.append(" and lower(d.name) like :leadName ");
        if (params.getLeadPhone() != null)
            getOrderSql.append(" and lower(d.phone) like :leadPhone ");
        if (params.getProductName() != null)
            getOrderSql.append(" and lower(d.prod_name) like :prodName ");
        if (params.getProductCrosssell() != null)
            getOrderSql.append(" and b.product like :productCrosssell ");
        if (params.getAmount() != null)
            getOrderSql.append(" and a.amount = :amount ");
        if (params.getSource() != null)
            getOrderSql.append(" and d.agc_id = :source ");
        if (params.getSourceName() != null)
            getOrderSql.append(" and LOWER(coalesce(o.shortname,d.agc_code)) like :sourceName ");
        if (params.getAgId() != null)
            getOrderSql.append(" and d.assigned = :agId ");
        if (params.getAgName() != null)
            getOrderSql.append(" and p.user_name like :agName ");
        if (params.getComment() != null)
            getOrderSql.append(" and lower(d.comment) like :comment ");
        if (params.getAddress() != null)
            getOrderSql.append(" and d.address||'-'||n.name||'-'||m.name||'-'||l.name like :address ");
        if (params.getFfmId() != null)
            getOrderSql.append(" and e.ffm_id = :ffmId ");
        if (params.getFfmName() != null)
            getOrderSql.append(" and  lower(f.shortname) = :shortName ");
        if (params.getWarehouseId() != null)
            getOrderSql.append(" and e.warehouse_id = :warehouseId ");
        if (params.getWarehouseName() != null)
            getOrderSql.append(" and lower(j.warehouse_shortname) = :warehouseName ");
        if (params.getLastmileId() != null)
            getOrderSql.append(" and e.carrier_id = :lastmieId ");
        if (params.getLastmileName() != null)
            getOrderSql.append(" and lower(k.shortname) like :lastmieName ");
        if (params.getPaymentMethod() != null)
            getOrderSql.append(" and a.payment_method = :paymentMethod ");
        if (params.getLeadStatusId() != null)
            getOrderSql.append(" and d.lead_status = :leadStatusId ");
        if (params.getLeadStatus() != null)
            getOrderSql.append(" and g.name in :leadStatus ");
        if (params.getSoStatusId() != null)
            getOrderSql.append(" and a.status = :soStatusId ");
        if (params.getOrderStatus() != null)
            getOrderSql.append(" and h.name = :orderStatus ");
        if (params.getDoStatusId() != null)
            getOrderSql.append(" and e.status = :doStatusId ");
        if (params.getDeliveryStatus() != null)
            getOrderSql.append(" and i.name = :deliverStatus ");
        if (params.getLeadType() != null)
            getOrderSql.append(" and d.lead_type = :leadType ");
        if (params.getCreatedate() != null)
            getOrderSql.append(
                    " and d.createdate  >= to_timestamp(split_part(:createDate,'|',1),'yyyymmddhh24miss') and d.createdate   <= to_timestamp(split_part(:createDate,'|',2),'yyyymmddhh24miss') ");
        if (params.getModifyDate() != null)
            getOrderSql.append(
                    " and d.modifydate  >= to_timestamp(split_part(:modifyDate,'|',1),'yyyymmddhh24miss') and d.modifydate   <= to_timestamp(split_part(:modifyDate,'|',2),'yyyymmddhh24miss') ");
        if (params.getTotalCall() != null)
            if(params.getTotalCall() == 0)
                getOrderSql.append(" and total_call is null ");
            else
                getOrderSql.append(" and total_call = :totalCall ");
        if (params.getCpId() != null)
            getOrderSql.append(" and d.cp_id = :cpId ");
        if (params.getCpIds() != null)
            getOrderSql.append(" and d.cp_id in :cpIds ");
        if (params.getCpName() != null)
            getOrderSql.append(" and  lower(q.name) like  :cpName ");
        if (params.getCallinglistId() != null)
            getOrderSql.append(" and d.callinglist_id = :callingListId ");
        if (params.getCallinglistName() != null)
            getOrderSql.append(" and r.cl_name like :callingListName ");
        if (params.getAffiliateId() != null)
            getOrderSql.append(" and d.affiliate_id like :affiliateId ");
        if (params.getSubId1() != null)
            getOrderSql.append(" and d.subid1 like :subId1 ");
        if (params.getReason() != null)
            getOrderSql.append(" and d.user_defin_05 like :reason ");
        if (params.getAgentNote() != null)
            getOrderSql.append(" and lower(d.agent_note) like :agentNote ");
        countOrder.append(getOrderSql);
        countOrder.append(" ) as a");
        getOrderSql.append(" order by d.createdate desc, lead_id desc ");
        if (params.getLimit() != null)
            getOrderSql.append(" limit :limit ");
        if (params.getOffset() != null)
            getOrderSql.append(" offset :offset ");

        Query query = entityManager.createNativeQuery(getOrderSql.toString());
        Query query1 = entityManager.createNativeQuery(countOrder.toString());

        if (params.getOrgId() != null) {
            query.setParameter("orgId", params.getOrgId());
            query1.setParameter("orgId", params.getOrgId());
        }
        if (params.getSoId() != null) {
            query.setParameter("soId", params.getSoId());
            query1.setParameter("soId", params.getSoId());
        }
        if (params.getDoId() != null) {
            query.setParameter("doId", params.getDoId());
            query1.setParameter("doId", params.getDoId());
        }
        if (params.getDoCode() != null) {
            query.setParameter("doCode", params.getDoCode());
            query1.setParameter("doCode", params.getDoCode());
        }
        if (params.getLeadId() != null) {
            query.setParameter("leadId", params.getLeadId());
            query1.setParameter("leadId", params.getLeadId());
        }
        if (params.getLeadName() != null) {
            query.setParameter("leadName", "%" + params.getLeadName().toLowerCase() + "%");
            query1.setParameter("leadName", "%" + params.getLeadName().toLowerCase() + "%");
        }
        if (params.getLeadPhone() != null) {
            query.setParameter("leadPhone", "%" + params.getLeadPhone() + "%");
            query1.setParameter("leadPhone", "%" + params.getLeadPhone() + "%");
        }
        if (params.getProductName() != null) {
            query.setParameter("prodName", "%" + params.getProductName().toLowerCase() + "%");
            query1.setParameter("prodName", "%" + params.getProductName().toLowerCase() + "%");
        }
        if (params.getProductCrosssell() != null) {
            query.setParameter("productCrosssell", "%" + params.getProductCrosssell().toLowerCase() + "%");
            query1.setParameter("productCrosssell", "%" + params.getProductCrosssell().toLowerCase() + "%");
        }
        if (params.getAmount() != null) {
            query.setParameter("amount", params.getAmount());
            query1.setParameter("amount", params.getAmount());
        }
        if (params.getSource() != null) {
            query.setParameter("source", params.getSource());
            query1.setParameter("source", params.getSource());
        }
        if (params.getSourceName() != null) {
            query.setParameter("sourceName", "%" + params.getSourceName().toLowerCase() + "%");
            query1.setParameter("sourceName", "%" + params.getSourceName().toLowerCase() + "%");
        }
        if (params.getAgId() != null) {
            query.setParameter("agId", params.getAgId());
            query1.setParameter("agId", params.getAgId());
        }
        if (params.getAgName() != null) {
            query.setParameter("agName", "%" + params.getAgName().toLowerCase() + "%");
            query1.setParameter("agName", "%" + params.getAgName().toLowerCase() + "%");
        }
        if (params.getComment() != null) {
            query.setParameter("comment", params.getComment().toLowerCase());
            query1.setParameter("comment", params.getComment().toLowerCase());
        }
        if (params.getAddress() != null) {
            query.setParameter("address", "%" + params.getAddress() + "%");
            query1.setParameter("address", "%" + params.getAddress() + "%");
        }
        if (params.getFfmId() != null) {
            query.setParameter("ffmId", params.getFfmId());
            query1.setParameter("ffmId", params.getFfmId());
        }
        if (params.getFfmName() != null) {
            query.setParameter("ffmName", params.getFfmName().toLowerCase());
            query1.setParameter("ffmName", params.getFfmName().toLowerCase());
        }
        if (params.getWarehouseId() != null) {
            query.setParameter("warehouseId", params.getWarehouseId());
            query1.setParameter("warehouseId", params.getWarehouseId());
        }
        if (params.getWarehouseName() != null) {
            query.setParameter("warehouseName", params.getWarehouseName().toLowerCase());
            query1.setParameter("warehouseName", params.getWarehouseName().toLowerCase());
        }
        if (params.getLastmileId() != null) {
            query.setParameter("lastmieId", params.getLastmileId());
            query1.setParameter("lastmieId", params.getLastmileId());
        }
        if (params.getLastmileName() != null) {
            query.setParameter("lastmieName", "%" + params.getLastmileName().toLowerCase() + "%");
            query1.setParameter("lastmieName", "%" + params.getLastmileName().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getPaymentMethod())) {
            if (params.getPaymentMethod().compareTo("COD") == 0) {
                query.setParameter("paymentMethod", 1);
                query1.setParameter("paymentMethod", 1);
            } else if (params.getPaymentMethod().compareTo("Banking transfer") == 0) {
                query.setParameter("paymentMethod", 2);
                query1.setParameter("paymentMethod", 2);
            } else if (params.getPaymentMethod().compareTo("Deposit") == 0) {
                query.setParameter("paymentMethod", 3);
                query1.setParameter("paymentMethod", 3);
            }
            String currentGEO = _COUNTRY.toLowerCase().trim();
            if (currentGEO.equalsIgnoreCase("th") || currentGEO.equalsIgnoreCase("tl")) {
                if (params.getPaymentMethod().compareTo("Payment gateway (2C2P)") == 0 || params.getPaymentMethod().toLowerCase().contains("2C2P".toLowerCase())) {
                    query.setParameter("paymentMethod", 5);
                    query1.setParameter("paymentMethod", 5);
                }
            } else if (currentGEO.equalsIgnoreCase("id") || currentGEO.equalsIgnoreCase("indo")) {
                if (params.getPaymentMethod().compareTo("Payment gateway (ESPay)") == 0 || params.getPaymentMethod().toLowerCase().contains("ESPay".toLowerCase())) {
                    query.setParameter("paymentMethod", 4);
                    query1.setParameter("paymentMethod", 4);
                }
            }
        }

        if (params.getLeadStatusId() != null) {
            query.setParameter("leadStatusId", params.getLeadStatusId());
            query1.setParameter("leadStatusId", params.getLeadStatusId());
        }
        if (params.getLeadStatus() != null) {
            List<String> status = Arrays.asList(params.getLeadStatus().split(","));
            query.setParameter("leadStatus", status);
            query1.setParameter("leadStatus", status);
        }
        if (params.getSoStatusId() != null) {
            query.setParameter("soStatusId", params.getSoStatusId());
            query1.setParameter("soStatusId", params.getSoStatusId());
        }
        if (params.getOrderStatus() != null) {
            query.setParameter("orderStatus", params.getOrderStatus());
            query1.setParameter("orderStatus", params.getOrderStatus());
        }
        if (params.getDoStatusId() != null) {
            query.setParameter("doStatusId", params.getDoStatusId());
            query1.setParameter("doStatusId", params.getDoStatusId());
        }
        if (params.getDeliveryStatus() != null) {
            query.setParameter("deliverStatus", params.getDeliveryStatus());
            query1.setParameter("deliverStatus", params.getDeliveryStatus());
        }
        if (params.getLeadType() != null) {
            query.setParameter("leadType", params.getLeadType());
            query1.setParameter("leadType", params.getLeadType());
        }
        if (params.getCreatedate() != null) {
            query.setParameter("createDate", params.getCreatedate());
            query1.setParameter("createDate", params.getCreatedate());
        }
        if (params.getModifyDate() != null) {
            query.setParameter("modifyDate", params.getModifyDate());
            query1.setParameter("modifyDate", params.getModifyDate());
        }
        if (params.getTotalCall() != null) {
            if (params.getTotalCall() != 0) {
                query.setParameter("totalCall", params.getTotalCall());
                query1.setParameter("totalCall", params.getTotalCall());
            }
        }
        if (params.getCpId() != null) {
            query.setParameter("cpId", params.getCpId());
            query1.setParameter("cpId", params.getCpId());
        }
        if (params.getCpIds() != null) {
            query.setParameter("cpIds", params.getCpIds());
            query1.setParameter("cpIds", params.getCpIds());
        }
        if (params.getCpName() != null) {
            query.setParameter("cpName", "%" + params.getCpName().toLowerCase() + "%");
            query1.setParameter("cpName", "%" + params.getCpName().toLowerCase() + "%");
        }
        if (params.getCallinglistId() != null) {
            query.setParameter("callinglistId", params.getCallinglistId());
            query1.setParameter("callinglistId", params.getCallinglistId());
        }
        if (params.getCallinglistName() != null) {
            query.setParameter("callinglistName", "%" + params.getCallinglistName().toLowerCase() + "%");
            query1.setParameter("callinglistName", "%" + params.getCallinglistName().toLowerCase() + "%");
        }
        if (params.getAffiliateId() != null) {
            query.setParameter("affiliateId", "%" + params.getAffiliateId() + "%");
            query1.setParameter("affiliateId", "%" + params.getAffiliateId() + "%");
        }
        if (params.getSubId1() != null) {
            query.setParameter("subId1", "%" + params.getSubId1() + "%");
            query1.setParameter("subId1", "%" + params.getSubId1() + "%");
        }
        if (params.getReason() != null) {
            query.setParameter("reason", "%" + params.getReason().toLowerCase() + "%");
            query1.setParameter("reason", "%" + params.getReason().toLowerCase() + "%");
        }
        if (params.getAgentNote() != null) {
            query.setParameter("agentNote", params.getAgentNote().toLowerCase());
            query1.setParameter("agentNote", params.getAgentNote().toLowerCase());
        }
        if (params.getLimit() != null)
            query.setParameter("limit", params.getLimit());
        if (params.getOffset() != null)
            query.setParameter("offset", params.getOffset());

        List<GetOrderManagement8Resp> listOrder = new ArrayList<>();
        List<Object[]> rows = query.getResultList();
        for (Object[] row : rows) {
            GetOrderManagement8Resp order = new GetOrderManagement8Resp();
            if (row[0] != null)
                order.setOrgId((Integer) row[0]);
            if (row[1] != null)
                order.setSoId((Integer) row[1]);
            if (row[2] != null)
                order.setDoId((Integer) row[2]);
            if (row[3] != null)
                order.setDoCode((String) row[3]);
            if (row[4] != null)
                order.setLeadId((Integer) row[4]);
            if (row[5] != null)
                order.setLeadName((String) row[5]);
            if (row[6] != null)
                order.setLeadPhone((String) row[6]);
            if (row[7] != null)
                order.setProductName((String) row[7]);
            if (row[8] != null)
                order.setProductCrosssell((String) row[8]);
            if (row[9] != null) {
                BigDecimal amount = (BigDecimal) row[9];
                order.setAmount(amount.doubleValue());
            }
            if (row[10] != null)
                order.setSource((Integer) row[10]);
            if (row[11] != null)
                order.setSourceName((String) row[11]);
            if (row[12] != null)
                order.setSourceCode((String) row[12]);
            if (row[13] != null)
                order.setAgId((Integer) row[13]);
            if (row[14] != null)
                order.setAgName((String) row[14]);
            if (row[15] != null)
                order.setComment((String) row[15]);
            if (row[16] != null)
                order.setAddress((String) row[16]);
            if (row[17] != null)
                order.setFfmId((Integer) row[17]);
            if (row[18] != null)
                order.setFfmName((String) row[18]);
            if (row[19] != null)
                order.setWarehouseId((Integer) row[19]);
            if (row[20] != null)
                order.setWarehouseName((String) row[20]);
            if (row[21] != null)
                order.setLastmileId((Integer) row[21]);
            if (row[22] != null)
                order.setLastmileName((String) row[22]);
            if (row[23] != null)
                order.setPaymentMethod((String) row[23]);
            if (row[24] != null)
                order.setLeadStatusId((Integer) row[24]);
            if (row[25] != null)
                order.setLeadStatus((String) row[25]);
            if (row[26] != null)
                order.setSoStatusId((Integer) row[26]);
            if (row[27] != null)
                order.setOrderStatus((String) row[27]);
            if (row[28] != null)
                order.setDoStatusId((Integer) row[28]);
            if (row[29] != null)
                order.setDeliveryStatus((String) row[29]);
            if (row[30] != null)
                order.setLeadType((String) row[30]);
            if (row[31] != null)
                order.setLeadTypeName((String) row[31]);
            if (row[32] != null)
                order.setCreatedate((Date) row[32]);
            if (row[33] != null)
                order.setModifyDate((Date) row[33]);
            if (row[34] != null)
                order.setTotalCall((Integer) row[34]);
            else
                order.setTotalCall(0);
            if (row[35] != null)
                order.setCpId((Integer) row[35]);
            if (row[36] != null)
                order.setCpName((String) row[36]);
            if (row[37] != null)
                order.setCallinglistId((Integer) row[37]);
            if (row[38] != null)
                order.setCallinglistName((String) row[38]);
            if (row[39] != null)
                order.setGroupId((Integer) row[39]);
            if (row[40] != null)
                order.setGroupName((String) row[40]);
            if (row[41] != null)
                order.setAffiliateId((String) row[41]);
            if (row[42] != null)
                order.setSubId1((String) row[42]);
            if (row[43] != null)
                order.setReason((String) row[43]);
            if (row[44] != null)
                order.setClickId((String) row[44]);

            if (row[45] != null)
                order.setReasonCallback((String) row[45]);
            if (row[46] != null)
                order.setAgentNote((String) row[46]);
            if (ObjectUtils.allNotNull(row[47])) {
                order.setActualCall((Integer) row[47]);
            }
            if (row[48] != null)
                order.setSubid5((String) row[48]);
            if (isExport && ObjectUtils.allNotNull(row[49])) {
                order.setLeadPostbackStatus((String) row[49]);
            }
            listOrder.add(order);
        }
        if (isRunSubnameAff && listOrder.size() > 0) {
            updateSubnameV2(listOrder);
        }

        BigInteger count = (BigInteger) query1.getResultList().get(0);
        Integer rowCount = count.intValue();

        OrderManagement8RespDto result = new OrderManagement8RespDto();
        result.setOrderManagement8Resp(listOrder);
        result.setRowCount(rowCount);

        return result;
    }

    @Override
    public OrderManagement10RespDto getOrderManagement(GetOrderManagement10 params, boolean isExport, String ssId) {
        StringBuilder getOrderSql = new StringBuilder();

        /* begin::Get Info */
        getOrderSql.append(
                " select d.org_id ,a.so_id ,e.do_id ,e.do_code ,d.lead_id ,d.name as lead_name ,d.phone as lead_phone , d.prod_name as product_name,b.product as product_crosssell  ,a.amount ,d.agc_id as source , ");
        getOrderSql.append(
                " coalesce(o.shortname,d.agc_code) as source_name ,d.agc_code as source_code ,d.assigned as ag_id ,p.user_name as ag_name ,d.comment ,coalesce(d.address||'###'||n.name||'-'||m.name||'-'||l.name ,d.address) as address , ");
        getOrderSql.append(
                " e.ffm_id ,f.shortname asffm_name ,e.warehouse_id ,j.warehouse_shortname as warehouse_name , ");
        getOrderSql.append(
                " e.carrier_id as lastmile_id ,k.shortname as lastmile_name ,case a.payment_method when 1 then 'COD' when 2 then 'Banking transfer' when 3 then 'Deposit' when 4 then 'Payment gateway (ESPay)' when 5 then 'Payment gateway (2C2P)' end as payment_method, ");
        getOrderSql.append(
                " d.lead_status as lead_status_id ,g.name as lead_status , a.status as so_status_id ,h.name as order_status , ");
        getOrderSql.append(" e.status as do_status_id ,i.name as delivery_status ,d.lead_type ,null lead_type_name , ");
        getOrderSql.append(" d.createdate, d.modifydate as modify_date ,d.total_call , ");
        getOrderSql.append(
                " d.cp_id ,q.name as cp_name ,d.callinglist_id ,r.cl_name as callinglist_name ,1 group_id, 'null' group_name  ");
        getOrderSql.append(
                " , d.affiliate_id, d.subid1, d.user_defin_05 as reason, d.click_id, d.user_defin_02 as reason_callback, d.agent_note, d.actual_call, d.subid5 ");

        /* begin::Location info */
        getOrderSql.append(", l.name as province_name, m.name as district_name, n.name as ward_name ");
        /* end::Location info */

        /* begin::External Post-back */
        if (isExport) {
            getOrderSql.append(" , cfs.name as lead_postback_status ");
        }
        /* end::External Post-back */

        /* end::Get Info */

        /* begin::From */
        getOrderSql.append(
                " from (select org_id, lead_id, name, phone,prod_id, prod_name, total_call,lead_status, lead_type, agc_id, agc_code, ");
        getOrderSql.append(
                " comment, address, province, district, subdistrict, assigned, createdate, modifydate, cp_id, callinglist_id,");
        getOrderSql.append(" affiliate_id, subid1, user_defin_05, user_defin_02, click_id, agent_note, actual_call, subid5 from cl_fresh) d ");
        /* end::From */

        /* begin::External Association */
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
        if (isExport) {
            getOrderSql.append(" left join cl_postback clpb on clpb.lead_id = d.lead_id ");
            getOrderSql.append(
                    " left join (select name, value from cf_synonym where type_id = 33) cfs on clpb.status_postback = cfs.value");
        }
        /* end::External Association */

        getOrderSql.append(" where  1=1 ");

        StringBuilder countOrder = new StringBuilder("select count(lead_id) from ( ");

        if (params.getOrgId() != null)
            getOrderSql.append(" and d.org_id = :orgId ");
        if (params.getSoId() != null)
            getOrderSql.append(" and a.so_id = :soId ");
        if (params.getDoId() != null)
            getOrderSql.append(" and e.do_id = :doId ");
        if (params.getDoCode() != null)
            getOrderSql.append(" and e.do_code = :doCode ");
        if (params.getLeadId() != null)
            getOrderSql.append(" and d.lead_id = :leadId ");
        if (params.getLeadName() != null)
            getOrderSql.append(" and lower(d.name) like :leadName ");
        if (params.getLeadPhone() != null)
            getOrderSql.append(" and lower(d.phone) like :leadPhone ");
        if (params.getProductName() != null)
            getOrderSql.append(" and lower(d.prod_name) like :prodName ");
        if (params.getProductCrosssell() != null)
            getOrderSql.append(" and b.product like :productCrosssell ");
        if (params.getAmount() != null)
            getOrderSql.append(" and a.amount = :amount ");
        if (params.getSource() != null)
            getOrderSql.append(" and d.agc_id = :source ");
        if (params.getSourceName() != null)
            getOrderSql.append(" and LOWER(coalesce(o.shortname,d.agc_code)) like :sourceName ");
        if (params.getAgId() != null)
            getOrderSql.append(" and d.assigned = :agId ");
        if (params.getAgName() != null)
            getOrderSql.append(" and p.user_name like :agName ");
        if (params.getComment() != null)
            getOrderSql.append(" and lower(d.comment) like :comment ");
        if (params.getAddress() != null)
            getOrderSql.append(" and d.address||'-'||n.name||'-'||m.name||'-'||l.name like :address ");
        if (params.getFfmId() != null)
            getOrderSql.append(" and e.ffm_id = :ffmId ");
        if (params.getFfmName() != null)
            getOrderSql.append(" and  lower(f.shortname) = :shortName ");
        if (params.getWarehouseId() != null)
            getOrderSql.append(" and e.warehouse_id = :warehouseId ");
        if (params.getWarehouseName() != null)
            getOrderSql.append(" and lower(j.warehouse_shortname) = :warehouseName ");
        if (params.getLastmileId() != null)
            getOrderSql.append(" and e.carrier_id = :lastmieId ");
        if (params.getLastmileName() != null)
            getOrderSql.append(" and lower(k.shortname) like :lastmieName ");
        if (params.getPaymentMethod() != null)
            getOrderSql.append(" and a.payment_method = :paymentMethod ");
        if (params.getLeadStatusId() != null)
            getOrderSql.append(" and d.lead_status = :leadStatusId ");
        if (params.getLeadStatus() != null)
            getOrderSql.append(" and g.name in :leadStatus ");
        if (params.getSoStatusId() != null)
            getOrderSql.append(" and a.status = :soStatusId ");
        if (params.getOrderStatus() != null)
            getOrderSql.append(" and h.name = :orderStatus ");
        if (params.getDoStatusId() != null)
            getOrderSql.append(" and e.status = :doStatusId ");
        if (params.getDeliveryStatus() != null)
            getOrderSql.append(" and i.name = :deliverStatus ");
        if (params.getLeadType() != null)
            getOrderSql.append(" and d.lead_type = :leadType ");
        if (params.getCreatedate() != null)
            getOrderSql.append(
                    " and d.createdate  >= to_timestamp(split_part(:createDate,'|',1),'yyyymmddhh24miss') and d.createdate   <= to_timestamp(split_part(:createDate,'|',2),'yyyymmddhh24miss') ");
        if (params.getModifyDate() != null)
            getOrderSql.append(
                    " and d.modifydate  >= to_timestamp(split_part(:modifyDate,'|',1),'yyyymmddhh24miss') and d.modifydate   <= to_timestamp(split_part(:modifyDate,'|',2),'yyyymmddhh24miss') ");
        if (params.getTotalCall() != null)
            if (params.getTotalCall() == 0)
                getOrderSql.append(" and total_call is null ");
            else
                getOrderSql.append(" and total_call = :totalCall ");
        if (params.getCpId() != null)
            getOrderSql.append(" and d.cp_id = :cpId ");
        if (params.getCpIds() != null)
            getOrderSql.append(" and d.cp_id in :cpIds ");
        if (params.getCpName() != null)
            getOrderSql.append(" and  lower(q.name) like  :cpName ");
        if (params.getCallinglistId() != null)
            getOrderSql.append(" and d.callinglist_id = :callingListId ");
        if (params.getCallinglistName() != null)
            getOrderSql.append(" and r.cl_name like :callingListName ");
        if (params.getAffiliateId() != null)
            getOrderSql.append(" and d.affiliate_id like :affiliateId ");
        if (params.getSubId1() != null)
            getOrderSql.append(" and d.subid1 like :subid1 ");
        if (params.getReason() != null)
            getOrderSql.append(" and d.user_defin_05 like :reason ");
        if (params.getAgentNote() != null)
            getOrderSql.append(" and lower(d.agent_note) like :agentNote ");
        countOrder.append(getOrderSql);
        countOrder.append(" ) as a");
        getOrderSql.append(" order by d.createdate desc, lead_id desc ");
        if (params.getLimit() != null)
            getOrderSql.append(" limit :limit ");
        if (params.getOffset() != null)
            getOrderSql.append(" offset :offset ");

        Query query = entityManager.createNativeQuery(getOrderSql.toString());
        Query query1 = entityManager.createNativeQuery(countOrder.toString());

        if (params.getOrgId() != null) {
            query.setParameter("orgId", params.getOrgId());
            query1.setParameter("orgId", params.getOrgId());
        }
        if (params.getSoId() != null) {
            query.setParameter("soId", params.getSoId());
            query1.setParameter("soId", params.getSoId());
        }
        if (params.getDoId() != null) {
            query.setParameter("doId", params.getDoId());
            query1.setParameter("doId", params.getDoId());
        }
        if (params.getDoCode() != null) {
            query.setParameter("doCode", params.getDoCode());
            query1.setParameter("doCode", params.getDoCode());
        }
        if (params.getLeadId() != null) {
            query.setParameter("leadId", params.getLeadId());
            query1.setParameter("leadId", params.getLeadId());
        }
        if (params.getLeadName() != null) {
            query.setParameter("leadName", "%" + params.getLeadName().toLowerCase() + "%");
            query1.setParameter("leadName", "%" + params.getLeadName().toLowerCase() + "%");
        }
        if (params.getLeadPhone() != null) {
            query.setParameter("leadPhone", "%" + params.getLeadPhone() + "%");
            query1.setParameter("leadPhone", "%" + params.getLeadPhone() + "%");
        }
        if (params.getProductName() != null) {
            query.setParameter("prodName", "%" + params.getProductName().toLowerCase() + "%");
            query1.setParameter("prodName", "%" + params.getProductName().toLowerCase() + "%");
        }
        if (params.getProductCrosssell() != null) {
            query.setParameter("productCrosssell", "%" + params.getProductCrosssell().toLowerCase() + "%");
            query1.setParameter("productCrosssell", "%" + params.getProductCrosssell().toLowerCase() + "%");
        }
        if (params.getAmount() != null) {
            query.setParameter("amount", params.getAmount());
            query1.setParameter("amount", params.getAmount());
        }
        if (params.getSource() != null) {
            query.setParameter("source", params.getSource());
            query1.setParameter("source", params.getSource());
        }
        if (params.getSourceName() != null) {
            query.setParameter("sourceName", "%" + params.getSourceName().toLowerCase() + "%");
            query1.setParameter("sourceName", "%" + params.getSourceName().toLowerCase() + "%");
        }
        if (params.getAgId() != null) {
            query.setParameter("agId", params.getAgId());
            query1.setParameter("agId", params.getAgId());
        }
        if (params.getAgName() != null) {
            query.setParameter("agName", "%" + params.getAgName().toLowerCase() + "%");
            query1.setParameter("agName", "%" + params.getAgName().toLowerCase() + "%");
        }
        if (params.getComment() != null) {
            query.setParameter("comment", params.getComment().toLowerCase());
            query1.setParameter("comment", params.getComment().toLowerCase());
        }
        if (params.getAddress() != null) {
            query.setParameter("address", "%" + params.getAddress() + "%");
            query1.setParameter("address", "%" + params.getAddress() + "%");
        }
        if (params.getFfmId() != null) {
            query.setParameter("ffmId", params.getFfmId());
            query1.setParameter("ffmId", params.getFfmId());
        }
        if (params.getFfmName() != null) {
            query.setParameter("ffmName", params.getFfmName().toLowerCase());
            query1.setParameter("ffmName", params.getFfmName().toLowerCase());
        }
        if (params.getWarehouseId() != null) {
            query.setParameter("warehouseId", params.getWarehouseId());
            query1.setParameter("warehouseId", params.getWarehouseId());
        }
        if (params.getWarehouseName() != null) {
            query.setParameter("warehouseName", params.getWarehouseName().toLowerCase());
            query1.setParameter("warehouseName", params.getWarehouseName().toLowerCase());
        }
        if (params.getLastmileId() != null) {
            query.setParameter("lastmieId", params.getLastmileId());
            query1.setParameter("lastmieId", params.getLastmileId());
        }
        if (params.getLastmileName() != null) {
            query.setParameter("lastmieName", "%" + params.getLastmileName().toLowerCase() + "%");
            query1.setParameter("lastmieName", "%" + params.getLastmileName().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getPaymentMethod())) {
            if (params.getPaymentMethod().compareTo("COD") == 0) {
                query.setParameter("paymentMethod", 1);
                query1.setParameter("paymentMethod", 1);
            } else if (params.getPaymentMethod().compareTo("Banking transfer") == 0) {
                query.setParameter("paymentMethod", 2);
                query1.setParameter("paymentMethod", 2);
            } else if (params.getPaymentMethod().compareTo("Deposit") == 0) {
                query.setParameter("paymentMethod", 3);
                query1.setParameter("paymentMethod", 3);
            }
            String currentGEO = _COUNTRY.toLowerCase().trim();
            if (currentGEO.equalsIgnoreCase("th") || currentGEO.equalsIgnoreCase("tl")) {
                if (params.getPaymentMethod().compareTo("Payment gateway (2C2P)") == 0 || params.getPaymentMethod().toLowerCase().contains("2C2P".toLowerCase())) {
                    query.setParameter("paymentMethod", 5);
                    query1.setParameter("paymentMethod", 5);
                }
            } else if (currentGEO.equalsIgnoreCase("id") || currentGEO.equalsIgnoreCase("indo")) {
                if (params.getPaymentMethod().compareTo("Payment gateway (ESPay)") == 0 || params.getPaymentMethod().toLowerCase().contains("ESPay".toLowerCase())) {
                    query.setParameter("paymentMethod", 4);
                    query1.setParameter("paymentMethod", 4);
                }
            }
        }

        if (params.getLeadStatusId() != null) {
            query.setParameter("leadStatusId", params.getLeadStatusId());
            query1.setParameter("leadStatusId", params.getLeadStatusId());
        }
        if (params.getLeadStatus() != null) {
            List<String> status = Arrays.asList(params.getLeadStatus().split(","));
            query.setParameter("leadStatus", status);
            query1.setParameter("leadStatus", status);
        }
        if (params.getSoStatusId() != null) {
            query.setParameter("soStatusId", params.getSoStatusId());
            query1.setParameter("soStatusId", params.getSoStatusId());
        }
        if (params.getOrderStatus() != null) {
            query.setParameter("orderStatus", params.getOrderStatus());
            query1.setParameter("orderStatus", params.getOrderStatus());
        }
        if (params.getDoStatusId() != null) {
            query.setParameter("doStatusId", params.getDoStatusId());
            query1.setParameter("doStatusId", params.getDoStatusId());
        }
        if (params.getDeliveryStatus() != null) {
            query.setParameter("deliverStatus", params.getDeliveryStatus());
            query1.setParameter("deliverStatus", params.getDeliveryStatus());
        }
        if (params.getLeadType() != null) {
            query.setParameter("leadType", params.getLeadType());
            query1.setParameter("leadType", params.getLeadType());
        }
        if (params.getCreatedate() != null) {
            query.setParameter("createDate", params.getCreatedate());
            query1.setParameter("createDate", params.getCreatedate());
        }
        if (params.getModifyDate() != null) {
            query.setParameter("modifyDate", params.getModifyDate());
            query1.setParameter("modifyDate", params.getModifyDate());
        }
        if (params.getTotalCall() != null) {
            if (params.getTotalCall() != 0) {
                query.setParameter("totalCall", params.getTotalCall());
                query1.setParameter("totalCall", params.getTotalCall());
            }
        }
        if (params.getCpId() != null) {
            query.setParameter("cpId", params.getCpId());
            query1.setParameter("cpId", params.getCpId());
        }
        if (params.getCpIds() != null) {
            query.setParameter("cpIds", params.getCpIds());
            query1.setParameter("cpIds", params.getCpIds());
        }
        if (params.getCpName() != null) {
            query.setParameter("cpName", "%" + params.getCpName().toLowerCase() + "%");
            query1.setParameter("cpName", "%" + params.getCpName().toLowerCase() + "%");
        }
        if (params.getCallinglistId() != null) {
            query.setParameter("callinglistId", params.getCallinglistId());
            query1.setParameter("callinglistId", params.getCallinglistId());
        }
        if (params.getCallinglistName() != null) {
            query.setParameter("callinglistName", "%" + params.getCallinglistName().toLowerCase() + "%");
            query1.setParameter("callinglistName", "%" + params.getCallinglistName().toLowerCase() + "%");
        }
        if (params.getAffiliateId() != null) {
            query.setParameter("affiliateId", "%" + params.getAffiliateId() + "%");
            query1.setParameter("affiliateId", "%" + params.getAffiliateId() + "%");
        }
        if (params.getSubId1() != null) {
            query.setParameter("subid1", "%" + params.getSubId1() + "%");
            query1.setParameter("subid1", "%" + params.getSubId1() + "%");
        }
        if (params.getReason() != null) {
            query.setParameter("reason", "%" + params.getReason().toLowerCase() + "%");
            query1.setParameter("reason", "%" + params.getReason().toLowerCase() + "%");
        }
        if (params.getAgentNote() != null) {
            query.setParameter("agentNote", params.getAgentNote().toLowerCase());
            query1.setParameter("agentNote", params.getAgentNote().toLowerCase());
        }
        if (params.getLimit() != null)
            query.setParameter("limit", params.getLimit());
        if (params.getOffset() != null)
            query.setParameter("offset", params.getOffset());

        List<GetOrderManagement10Resp> listOrder = new ArrayList<>();
        List<Object[]> rows = query.getResultList();
        for (Object[] row : rows) {
            GetOrderManagement10Resp order = new GetOrderManagement10Resp();
            if (row[0] != null)
                order.setOrgId((Integer) row[0]);
            if (row[1] != null)
                order.setSoId((Integer) row[1]);
            if (row[2] != null)
                order.setDoId((Integer) row[2]);
            if (row[3] != null)
                order.setDoCode((String) row[3]);
            if (row[4] != null)
                order.setLeadId((Integer) row[4]);
            if (row[5] != null)
                order.setLeadName((String) row[5]);
            if (row[6] != null)
                order.setLeadPhone((String) row[6]);
            if (row[7] != null)
                order.setProductName((String) row[7]);
            if (row[8] != null)
                order.setProductCrosssell((String) row[8]);
            if (row[9] != null) {
                BigDecimal amount = (BigDecimal) row[9];
                order.setAmount(amount.doubleValue());
            }
            if (row[10] != null)
                order.setSource((Integer) row[10]);
            if (row[11] != null)
                order.setSourceName((String) row[11]);
            if (row[12] != null)
                order.setSourceCode((String) row[12]);
            if (row[13] != null)
                order.setAgId((Integer) row[13]);
            if (row[14] != null)
                order.setAgName((String) row[14]);
            if (row[15] != null)
                order.setComment((String) row[15]);
            if (row[16] != null)
                order.setAddress((String) row[16]);
            if (row[17] != null)
                order.setFfmId((Integer) row[17]);
            if (row[18] != null)
                order.setFfmName((String) row[18]);
            if (row[19] != null)
                order.setWarehouseId((Integer) row[19]);
            if (row[20] != null)
                order.setWarehouseName((String) row[20]);
            if (row[21] != null)
                order.setLastmileId((Integer) row[21]);
            if (row[22] != null)
                order.setLastmileName((String) row[22]);
            if (row[23] != null)
                order.setPaymentMethod((String) row[23]);
            if (row[24] != null)
                order.setLeadStatusId((Integer) row[24]);
            if (row[25] != null)
                order.setLeadStatus((String) row[25]);
            if (row[26] != null)
                order.setSoStatusId((Integer) row[26]);
            if (row[27] != null)
                order.setOrderStatus((String) row[27]);
            if (row[28] != null)
                order.setDoStatusId((Integer) row[28]);
            if (row[29] != null)
                order.setDeliveryStatus((String) row[29]);
            if (row[30] != null)
                order.setLeadType((String) row[30]);
            if (row[31] != null)
                order.setLeadTypeName((String) row[31]);
            if (row[32] != null)
                order.setCreatedate((Date) row[32]);
            if (row[33] != null)
                order.setModifyDate((Date) row[33]);
            if (row[34] != null)
                order.setTotalCall((Integer) row[34]);
            else
                order.setTotalCall(0);
            if (row[35] != null)
                order.setCpId((Integer) row[35]);
            if (row[36] != null)
                order.setCpName((String) row[36]);
            if (row[37] != null)
                order.setCallinglistId((Integer) row[37]);
            if (row[38] != null)
                order.setCallinglistName((String) row[38]);
            if (row[39] != null)
                order.setGroupId((Integer) row[39]);
            if (row[40] != null)
                order.setGroupName((String) row[40]);
            if (row[41] != null)
                order.setAffiliateId((String) row[41]);
            if (row[42] != null)
                order.setSubId1((String) row[42]);
            if (row[43] != null)
                order.setReason((String) row[43]);
            if (row[44] != null)
                order.setClickId((String) row[44]);
            if (row[45] != null)
                order.setReasonCallback((String) row[45]);
            if (row[46] != null)
                order.setAgentNote((String) row[46]);
            if (ObjectUtils.allNotNull(row[47])) {
                order.setActualCall((Integer) row[47]);
            }
            if (row[48] != null)
                order.setSubid5((String) row[48]);

            if (ObjectUtils.allNotNull(row[49])) {
                order.setProvinceName((String) row[49]);
            }

            if (ObjectUtils.allNotNull(row[50])) {
                order.setDistrictName((String) row[50]);
            }

            if (ObjectUtils.allNotNull(row[51])) {
                order.setSubDistrictName((String) row[51]);
            }

            if (isExport && ObjectUtils.allNotNull(row[52])) {
                order.setLeadPostbackStatus((String) row[52]);
            }
            listOrder.add(order);
        }

        if (!listOrder.isEmpty())
            this.setCustomerDataFromList(listOrder, ssId);

        if (isRunSubnameAff) {
            updateSubname(listOrder);
        }

        BigInteger count = (BigInteger) query1.getResultList().get(0);
        Integer rowCount = count.intValue();

        OrderManagement10RespDto result = new OrderManagement10RespDto();
        result.setOrderManagementResp(listOrder);
        result.setRowCount(rowCount);

        return result;
    }

    private void updateSubname (List<GetOrderManagement10Resp> listOrder) {
        List<TrkAffSubnameMapping> affSubnameMappings = trkAffSubnameMappingRepository.findAll();
        HashMap<String, String> mapSubname = new HashMap<>();
        for (TrkAffSubnameMapping affSubnameMapping: affSubnameMappings) {
            mapSubname.put(affSubnameMapping.getAffName(), affSubnameMapping.getSubName());
        }
        for (GetOrderManagement10Resp order : listOrder){
            if (!StringUtils.isEmpty(order.getAffiliateId()) && mapSubname.containsKey(order.getAffiliateId())){
                order.setAffiliateId(order.getAffiliateId() + mapSubname.get(order.getAffiliateId()));
            }
        }

    }

    private void updateSubnameV2 (List<GetOrderManagement8Resp> listOrder) {
        List<TrkAffSubnameMapping> affSubnameMappings = trkAffSubnameMappingRepository.findAll();
        HashMap<String, String> mapSubname = new HashMap<>();
        for (TrkAffSubnameMapping affSubnameMapping: affSubnameMappings) {
            mapSubname.put(affSubnameMapping.getAffName(), affSubnameMapping.getSubName());
        }
        for (GetOrderManagement8Resp order : listOrder){
            if (!StringUtils.isEmpty(order.getAffiliateId()) && mapSubname.containsKey(order.getAffiliateId())){
                order.setAffiliateId(order.getAffiliateId() + mapSubname.get(order.getAffiliateId()));
            }
        }

    }

    private void setCustomerDataFromList(List<GetOrderManagement10Resp> listOrder, String ssId) {
        List<Integer> idList = listOrder.stream()
                .map(GetOrderManagement10Resp::getLeadId)
                .collect(Collectors.toList());

        GetMktDataParams getMktData = new GetMktDataParams();
        getMktData.setLeadId(org.apache.commons.lang3.StringUtils.join(idList, ","));
        DBResponse<List<GetMktDataResp>> dbResponse = clFreshService.getMktData(ssId, getMktData);

        if (dbResponse == null || dbResponse.getResult().isEmpty())
            return;

        HashMap<Integer, GetMktDataResp> mapMktData = new HashMap<>();
        for (GetMktDataResp mktData : dbResponse.getResult())
            mapMktData.put(mktData.getLeadId(), mktData);

        DBResponse<List<LCProvince>> responseProvince = lcProvinceService.getProvince(ssId, new GetProvince());
        HashMap<Integer, String> mapProvince = new HashMap<>();
        for (LCProvince province : responseProvince.getResult()) {
            mapProvince.put(province.getPrvId(), province.getName());
        }

        GetSynonym getSynonym = new GetSynonym();
        getSynonym.setTypeId(47);
        DBResponse<List<GetSynonymResp>> responseAgeRange = clFreshService.getSynonym(ssId, getSynonym);
        HashMap<Integer, String> mapAgeRange = new HashMap<>();
        for (GetSynonymResp ageRange : responseAgeRange.getResult()) {
            mapAgeRange.put(ageRange.getValue(), ageRange.getName());
        }

        for (GetOrderManagement10Resp order : listOrder) {
            if (!mapMktData.containsKey(order.getLeadId()))
                continue;

            GetMktDataResp mktData = mapMktData.get(order.getLeadId());
            order.setCustId(mktData.getCustId());
            order.setCustName(mktData.getCustName());
            order.setCustPhone(mktData.getCustPhone());
            order.setCustAge(mapAgeRange.get(mktData.getCustAge()));
            order.setCustJob(mktData.getCustJob());
            order.setCustOtherSymptom(mktData.getCustOtherSymptom());
            order.setCustArea(mapProvince.get(mktData.getCustArea()));
            if (mktData.getCustGender() != null)
                order.setCustGender(getGender(mktData.getCustGender()));
            if (mktData.getCustDob() != null)
                order.setCustDob(DateHelper.convertToLocalDateViaMillisecond(mktData.getCustDob()));
        }
    }

    private String getGender(int gender) {
        if (gender == EnumType.GENDER.FEMALE.getValue())
            return EnumType.GENDER.FEMALE.getName();
        else if (gender == EnumType.GENDER.MALE.getValue())
            return EnumType.GENDER.MALE.getName();
        else if (gender == EnumType.GENDER.ETC.getValue())
            return EnumType.GENDER.ETC.getName();
        return null;
    }

    @Override
    public byte[] exportCSVOrderManagement(GetOrderManagement10 orderManagement,
                                           OrderManagementRequestDTO orderManagementRequestDTO) throws TMSException {
        final String ACTIVITY = "Export Order Management";
        final String OBJECT_TYPE = "ExportOrderManagement";
        final String ON_FIELD = "getOrderManagementV6";
        List<GetOrderManagement10Resp> orderManagementResponseList = new ArrayList<>();
        orderManagement.setOrgId(orderManagementRequestDTO.getOrgId());

        if (orderManagementRequestDTO.isTeamLeader()) {
            GetCampaignAgent campaignAgent = new GetCampaignAgent();
            List<Integer> cpIds = new ArrayList<>();
            campaignAgent.setUserId(orderManagementRequestDTO.getUserId());
            campaignAgent.setOrgId(orderManagementRequestDTO.getOrgId());
            campaignAgent.setStatus(EnumType.CAMPAIGN_STATUS_ID.RUNNING.getValue());
            List<GetCampaignAgentResp> campaigns = clFreshService
                    .getCampaignAgent(orderManagementRequestDTO.getSessionId(), campaignAgent).getResult();
            if (CollectionUtils.isEmpty(campaigns))
                throw new TMSException(ErrorMessage.BAD_REQUEST);
            for (GetCampaignAgentResp campaign : campaigns)
                cpIds.add(campaign.getCpId());
            orderManagement.setCpIds(cpIds);
        }

        if (ObjectUtils.isNull(orderManagement.getModifyDate()) && ObjectUtils.isNull(orderManagement.getCreatedate())) {
            java.time.LocalDateTime beforeDate = java.time.LocalDateTime.now().minusDays(30);
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter
                    .ofPattern("yyyyMMddHHmmss");
            orderManagement.setCreatedate(beforeDate.format(formatter) + "|" + now.format(formatter));
        }
        OrderManagement10RespDto listDBResponse = getOrderManagement(orderManagement, true, orderManagementRequestDTO.getSessionId());
        if (!ObjectUtils.isNull(listDBResponse) && !CollectionUtils.isEmpty(listDBResponse.getOrderManagementResp())) {
            orderManagementResponseList = listDBResponse.getOrderManagementResp();
            dbLogWriter.writeAgentActivityLog(orderManagementRequestDTO.getUserId(), ACTIVITY, OBJECT_TYPE,
                    orderManagementResponseList.size(), ON_FIELD, LogHelper.toJson(orderManagement));
        }
        try {
            return ExcelHelper.createExcelData(
                    OrderManagementResponseDTO.buildMappedToDTO(orderManagementResponseList,
                            orderManagementRequestDTO),
                    OrderManagementResponseDTO.class);
        } catch (TMSException error) {
            logger.error(error.getMessage(), error);
            return new byte[0];
        }
    }

    @Override
    public byte[] exportCSVValidation(GetValidation7 validations, OrderManagementRequestDTO orderManagementRequestDTO) {
        final String ACTIVITY = "Export Validation";
        final String OBJECT_TYPE = "ExportValidation";
        final String ON_FIELD = "getValidationV7";
        List<GetValidation7Resp> dbResponse = new ArrayList<>();
        validations.setOrgId(orderManagementRequestDTO.getOrgId());
        ValidationRespDto validationRespDto = getValidation(validations, true);
        if (!ObjectUtils.isNull(validationRespDto)
                && !CollectionUtils.isEmpty(validationRespDto.getGetValidation7Resps())) {
            dbResponse = validationRespDto.getGetValidation7Resps();
            dbLogWriter.writeAgentActivityLog(orderManagementRequestDTO.getUserId(), ACTIVITY, OBJECT_TYPE,
                    dbResponse.size(), ON_FIELD, LogHelper.toJson(validations));
        }

        try {
            return ExcelHelper.createExcelData(
                    ValidationResponseDTO.buildMappedDTOV7(dbResponse, orderManagementRequestDTO),
                    ValidationResponseDTO.class);
        } catch (TMSException error) {
            logger.error(error.getMessage());
            return new byte[0];
        }
    }

    @Override
    public byte[] exportCommissionData(GetCommissionData commissionData, String sessionId) {
        int orgId = commissionData.getOrgId();

        byte[] result;
        try {
            /* Only TH (orgId = 10) use get_commission_data_v2 */
            if (orgId == 10) {
                List<GetCommissionDataV2Resp> commissionDataV2RespList = new ArrayList<>();
                DBResponse<List<GetCommissionDataV2Resp>> dbResponseV2 = clFreshService.getCommissionDataV2(sessionId, commissionData);
                if (!ObjectUtils.isNull(dbResponseV2) && !CollectionUtils.isEmpty(dbResponseV2.getResult()))
                    commissionDataV2RespList = dbResponseV2.getResult();

                result = ExcelHelper.createExcelData(commissionDataV2RespList, GetCommissionDataV2Resp.class);
            } else {
                List<GetCommissionDataResp> commissionDataRespList = new ArrayList<>();
                DBResponse<List<GetCommissionDataResp>> dbResponse = clFreshService.getCommissionData(sessionId, commissionData);
                if (!ObjectUtils.isNull(dbResponse) && !CollectionUtils.isEmpty(dbResponse.getResult()))
                    commissionDataRespList = dbResponse.getResult();

                result = ExcelHelper.createExcelData(commissionDataRespList, GetCommissionDataResp.class);
            }
        } catch (TMSException error) {
            logger.error(error.getMessage());
            return new byte[0];
        }

        return result;
    }

    @Override
    public byte[] exportCollectionData(GetCollectionData collectionData, String sessionId) {
        List<GetCollectionDataResp> dbResponse = new ArrayList<>();
        DBResponse<List<GetCollectionDataResp>> dbResponseCollectionData = clFreshService.getCollectionData(sessionId,
                collectionData);
        if (!ObjectUtils.isNull(dbResponseCollectionData)
                && !CollectionUtils.isEmpty(dbResponseCollectionData.getResult()))
            dbResponse = dbResponseCollectionData.getResult();
        try {
            return ExcelHelper.createExcelData(dbResponse, GetCollectionDataResp.class);
        } catch (TMSException error) {
            logger.error(error.getMessage());
            return new byte[0];
        }
    }

    @Override
    public ValidationRespDto getValidation(GetValidation7 params, boolean isExport) {
        if (params.getOrgId() == null)
            return null;
        StringBuilder getValidationSql = new StringBuilder();

        /* begin::Get info */
        getValidationSql.append(
                " select  a.org_id ,a.so_id ,e.do_id ,e.do_code ,d.lead_id ,d.name as lead_name ,d.phone as lead_phone ,d.prod_name as product_name,b.product as product_crosssell ,a.amount ,d.agc_id as source , ");
        getValidationSql.append(
                " coalesce(o.shortname, d.agc_code) as source_name ,d.agc_code as source_code ,ag_id ,p.user_name as ag_name ,d.comment ,d.address||'###'||n.name||'-'||m.name||'-'||l.name as address ,"
                        + (params.getOrgId() == 9 ? " x.name as location_status, " : " null as location_status , "));
        getValidationSql.append(
                " e.ffm_id ,f.shortname as ffm_name ,e.warehouse_id ,j.warehouse_shortname as warehouse_name , ");
        getValidationSql.append(
                " e.carrier_id as lastmile_id ,k.shortname as lastmile_name ,case a.payment_method when 1 then 'COD' when 2 then 'Banking transfer' when 3 then 'Deposit' when 4 then 'Payment gateway (ESPay)' when 5 then 'Payment gateway (2C2P)' end as payment_method ");
        getValidationSql.append(
                " ,d.lead_status as lead_status_id, g.name as lead_status ,a.status as so_status_id ,h.name as order_status , ");
        getValidationSql
                .append(" e.status as do_status_id ,i.name as delivery_status ,d.lead_type ,null as lead_type_name , ");
        getValidationSql.append(" a.createdate, coalesce(e.updatedate, a.modifydate) as modify_date ,d.total_call , ");
        getValidationSql.append(
                " d.cp_id ,q.name as cp_name ,d.callinglist_id ,r.cl_name as callinglist_name, d.agent_note, a.creation_date, a.validate_by, z.fullname as validate_by_name, d.affiliate_id, d.subid1, a.reason, a.qa_note, ");

        /* begin::Get locations */
        getValidationSql.append(" l.name as province_name, m.name as district_name, n.name as ward_name ");
        /* end::Get locations */

        /* begin::ZipCode */
        getValidationSql.append( params.getOrgId() == 9 ? ", y.code as lc_zip_code, y.name as neighborhood_name" : "");
        getValidationSql.append( params.getOrgId() == 10 ? ", y.sdt_code as lc_zip_code, y.sdt_name as neighborhood_name" : "");
        /* end::ZipCode */

        /* end::Get info */

        /* begin::From */
        getValidationSql.append(" from od_sale_order a ");
        /* end::From */

        /* begin::External Association */
        getValidationSql.append(
                " left join (select b.so_id, string_agg(c.name||' ('||quantity||')' , E', \\n' order by oi_id ) as product ");
        getValidationSql.append(
                " from od_so_item b join pd_product c on b.prod_id=c.prod_id group by so_id) b on a.so_id=b.so_id ");
        getValidationSql.append(
                " left join (select lead_id, name, phone, total_call,lead_status, lead_type, prod_name, agc_id, agc_code, comment, address, province, district, subdistrict, "
                        + (params.getOrgId() == 9 ? " neighborhood, " : "") + "  cp_id, callinglist_id, agent_note, affiliate_id, subid1 ");
        getValidationSql.append(" from cl_fresh ");

        /*
        getValidationSql.append(
                " union all select lead_id, name, phone, total_call, lead_status,lead_type, prod_name, agc_id, agc_code, comment, address, province, district, subdistrict,  "
                        + (params.getOrgId() == 9 ? " neighborhood, " : "") + "  cp_id, callinglist_id, agent_note ");
        getValidationSql.append(" from cl_active ");
        getValidationSql.append(
                " union all select lead_id, name, phone, total_call,lead_status, lead_type, prod_name, agc_id, agc_code, comment, address, province, district, subdistrict, "
                        + (params.getOrgId() == 9 ? " neighborhood, " : "") + " cp_id, callinglist_id, agent_note from cl_inactive ");
        */


        getValidationSql.append(" ) d on a.lead_id=d.lead_id ");
        getValidationSql.append(" left join od_do_new e on a.so_id=e.so_id ");
        getValidationSql.append(" left join bp_partner f on e.ffm_id = f.pn_id ");
        getValidationSql.append(" left join bp_warehouse j on e.warehouse_id = j.warehouse_id ");
        getValidationSql.append(" left join bp_partner k on e.carrier_id = k.pn_id ");
        getValidationSql.append(" left join bp_partner o on d.agc_id = o.pn_id ");
        getValidationSql
                .append(" left join (select * from cf_synonym where type ='lead status') g on d.lead_status=g.value ");
        getValidationSql
                .append(" left join (select * from cf_synonym where type ='sale order status') h on a.status=h.value ");
        getValidationSql.append(
                " left join (select * from cf_synonym where type ='delivery order status') i on e.status=i.value ");
        getValidationSql.append(" left join or_user p on p.user_id = ag_id ");
        getValidationSql.append(" left join or_user z on z.user_id = a.validate_by ");
        getValidationSql.append(" left join lc_province l on l.prv_id = cast(d.province as integer) ");
        getValidationSql.append(" left join lc_district m on m.dt_id = cast(d.district as integer) ");
        getValidationSql.append(" left join lc_subdistrict n on n.sdt_id = cast(d.subdistrict as integer) ");
        getValidationSql.append(" left join cp_campaign q on q.cp_id = d.cp_id ");
        getValidationSql.append(" left join cl_calling_list r on r.callinglist_id = d.callinglist_id ");

        /* begin::Define neighborhood */
        getValidationSql.append(
                params.getOrgId() == 9 ? " left join lc_neighborhood y on y.id = cast(d.neighborhood as integer) "
                        : " ");
        getValidationSql.append(params.getOrgId() == 9
                ? " left join (select * from cf_synonym where type ='location status') x on y.status=x.value "
                : " ");

        getValidationSql.append(
                params.getOrgId() == 10 ? " left join lc_subdistrict_map y on y.sdt_id = cast(d.subdistrict as integer) AND y.partner_id = 56 "
                        : " ");

        /* begin::Define neighborhood */

        /* end::External Association */

        /* begin::Where Condition */
        getValidationSql.append(" where 1=1 ");
        StringBuilder countValidation = new StringBuilder(" Select count(*) from (");
        if (!StringUtils.isEmpty(params.getOrgId()))
            getValidationSql.append(" and a.org_id = :orgId ");
        if (!StringUtils.isEmpty(params.getSoId()))
            getValidationSql.append(" and a.so_id = :soId ");
        if (!StringUtils.isEmpty(params.getDoId()))
            getValidationSql.append(" and e.do_id = :doId ");
        if (!StringUtils.isEmpty(params.getDoCode()))
            getValidationSql.append(" and e.do_code = doCode ");
        if (!StringUtils.isEmpty(params.getLeadId()))
            getValidationSql.append(" and d.lead_id = :leadId ");
        if (!StringUtils.isEmpty(params.getLeadName()))
            getValidationSql.append(" and lower(d.name) like :leadName ");
        if (!StringUtils.isEmpty(params.getLeadPhone()))
            getValidationSql.append(" and lower(d.phone) like :leadPhone ");
        if (!StringUtils.isEmpty(params.getProductName()))
            getValidationSql.append(" and lower(d.prod_name) like :productName ");
        if (!StringUtils.isEmpty(params.getProductCrosssell()))
            getValidationSql.append(" and lower(b.product) like :productCrosssell ");
        if (!StringUtils.isEmpty(params.getAmount()))
            getValidationSql.append(" and a.amount = :amount ");
        if (!StringUtils.isEmpty(params.getSource()))
            getValidationSql.append(" and d.agc_id = :source ");
        if (!StringUtils.isEmpty(params.getSourceName()))
            getValidationSql.append(" and lower(o.shortname) like :sourceName ");
        if (!StringUtils.isEmpty(params.getSourceCode()))
            getValidationSql.append(" and d.agc_code = :sourceCode ");
        if (!StringUtils.isEmpty(params.getAgId()))
            getValidationSql.append(" and ag_id = :agId ");
        if (!StringUtils.isEmpty(params.getAgName()))
            getValidationSql.append(" and lower(p.user_name) like :agName ");
        if (!StringUtils.isEmpty(params.getComment()))
            getValidationSql.append(" and lower(d.comment) = :comment ");
        if (!StringUtils.isEmpty(params.getAddress()))
            getValidationSql.append(" and d.address||'-'||n.name||'-'||m.name||'-'||l.name like :address ");
        if (!StringUtils.isEmpty(params.getFfmId()))
            getValidationSql.append(" and e.ffm_id = :ffmId ");
        if (!StringUtils.isEmpty(params.getFfmName()))
            getValidationSql.append(" and lower(f.shortname) = :ffmName ");
        if (!StringUtils.isEmpty(params.getWarehouseId()))
            getValidationSql.append(" and e.warehouse_id = :warehouseId ");
        if (!StringUtils.isEmpty(params.getWarehouseName()))
            getValidationSql.append(" and lower(j.warehouse_shortname) = :warehouseName ");
        if (!StringUtils.isEmpty(params.getLastmileId()))
            getValidationSql.append(" and e.carrier_id = :lastmileId ");
        if (!StringUtils.isEmpty(params.getLastmileName()))
            getValidationSql.append(" and lower(k.shortname) like :lastmileName ");
        if (!StringUtils.isEmpty(params.getPaymentMethod()))
            getValidationSql.append(" and a.payment_method = :paymentMethod ");
        if (!StringUtils.isEmpty(params.getLeadStatusId()))
            getValidationSql.append(" and d.lead_status = :leadStatusId ");
        if (!StringUtils.isEmpty(params.getLeadStatus()))
            getValidationSql.append(" and g.name in :leadStatus ");
        if (!StringUtils.isEmpty(params.getSoStatusId()))
            getValidationSql.append(" and a.status = :soStatusId ");
        if (!StringUtils.isEmpty(params.getOrderStatus()))
            getValidationSql.append(" and lower(h.name) = :orderStatus ");
        if (!StringUtils.isEmpty(params.getDoStatusId()))
            getValidationSql.append(" and e.status = :doStatusId ");
        if (!StringUtils.isEmpty(params.getDeliveryStatus()))
            getValidationSql.append(" and lower(i.name) = :deliveryStatus ");
        if (!StringUtils.isEmpty(params.getLocationStatus()) && params.getOrgId() == 9)
            getValidationSql.append(" and lower(x.name) = :locationStatus ");
        if (!StringUtils.isEmpty(params.getLeadType()))
            getValidationSql.append(" and d.lead_type = :leadType ");
        if (!StringUtils.isEmpty(params.getCreatedate())) {
            getValidationSql
                    .append(" and a.createdate  >= to_timestamp(split_part(:createDate,'|',1),'yyyymmddhh24miss') ");
            getValidationSql
                    .append(" and a.createdate  <= to_timestamp(split_part(:createDate,'|',2),'yyyymmddhh24miss') ");
        }
        if (!StringUtils.isEmpty(params.getModifyDate())) {
            getValidationSql.append(
                    " and coalesce(e.updatedate, a.modifydate)  >= to_timestamp(split_part(:modifyDate,'|',1),'yyyymmddhh24miss') ");
            getValidationSql.append(
                    " and coalesce(e.updatedate, a.modifydate)  <= to_timestamp(split_part(:modifyDate,'|',2),'yyyymmddhh24miss') ");
        }
        if (!StringUtils.isEmpty(params.getTotalCall()))
            getValidationSql.append(" and total_call = :totalCall ");
        if (!StringUtils.isEmpty(params.getCpId()))
            getValidationSql.append(" and d.cp_id = :cpId ");
        if (!StringUtils.isEmpty(params.getCpName()))
            getValidationSql.append(" and lower(q.name) like :cpName ");
        if (!StringUtils.isEmpty(params.getCallinglistId()))
            getValidationSql.append(" and d.callinglist_id = callingListId ");
        if (!StringUtils.isEmpty(params.getCallinglistName()))
            getValidationSql.append(" and lower(r.cl_name) like :callingListName ");
        if (!StringUtils.isEmpty(params.getCreationDate())) {
            getValidationSql.append(
                    " and a.creation_date >= to_timestamp(split_part(:creationDate,'|',1),'yyyymmddhh24miss') ");
            getValidationSql.append(
                    " and a.creation_date  <= to_timestamp(split_part(:creationDate,'|',2),'yyyymmddhh24miss') ");
        }
        if (!StringUtils.isEmpty(params.getAffiliateId()))
            getValidationSql.append(" and lower(d.affiliate_id) like :affiliateId");
        if (!StringUtils.isEmpty(params.getSubid1()))
            getValidationSql.append(" and lower(d.subid1) like :subid1");
        if (!StringUtils.isEmpty(params.getReason()))
            getValidationSql.append(" and lower(a.reason) like :reason");
        if (!StringUtils.isEmpty(params.getQaNote()))
            getValidationSql.append(" and lower(a.qa_note) like :qaNote");
        countValidation.append(getValidationSql).append(" ) as a");
        getValidationSql.append(" order by so_id desc ");
        if (!StringUtils.isEmpty(params.getLimit()))
            getValidationSql.append(" limit :limit ");
        if (!StringUtils.isEmpty(params.getOffset()))
            getValidationSql.append(" offset :offset ");

        Query getValidationQuery = entityManager.createNativeQuery(getValidationSql.toString());
        Query countValidationQuery = entityManager.createNativeQuery(countValidation.toString());

        if (!StringUtils.isEmpty(params.getOrgId())) {
            getValidationQuery.setParameter("orgId", params.getOrgId());
            countValidationQuery.setParameter("orgId", params.getOrgId());
        }
        if (!StringUtils.isEmpty(params.getSoId())) {
            getValidationQuery.setParameter("soId", params.getSoId());
            countValidationQuery.setParameter("soId", params.getSoId());
        }
        if (!StringUtils.isEmpty(params.getDoId())) {
            getValidationQuery.setParameter("doId", params.getDoId());
            countValidationQuery.setParameter("doId", params.getDoId());
        }
        if (!StringUtils.isEmpty(params.getDoCode())) {
            getValidationQuery.setParameter("doCode", params.getDoCode());
            countValidationQuery.setParameter("doCode", params.getDoCode());
        }
        if (!StringUtils.isEmpty(params.getLeadId())) {
            getValidationQuery.setParameter("leadId", params.getLeadId());
            countValidationQuery.setParameter("leadId", params.getLeadId());
        }
        if (!StringUtils.isEmpty(params.getLeadName())) {
            getValidationQuery.setParameter("leadName", "%" + params.getLeadName().toLowerCase() + "%");
            countValidationQuery.setParameter("leadName", "%" + params.getLeadName().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getLeadPhone())) {
            getValidationQuery.setParameter("leadPhone", "%" + params.getLeadPhone().toLowerCase() + "%");
            countValidationQuery.setParameter("leadPhone", "%" + params.getLeadPhone().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getProductName())) {
            getValidationQuery.setParameter("productName", "%" + params.getProductName().toLowerCase() + "%");
            countValidationQuery.setParameter("productName", "%" + params.getProductName().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getProductCrosssell())) {
            getValidationQuery.setParameter("productCrosssell", "%" + params.getProductCrosssell().toLowerCase() + "%");
            countValidationQuery.setParameter("productCrosssell",
                    "%" + params.getProductCrosssell().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getAmount())) {
            getValidationQuery.setParameter("amount", params.getAmount());
            countValidationQuery.setParameter("amount", params.getAmount());
        }
        if (!StringUtils.isEmpty(params.getSource())) {
            getValidationQuery.setParameter("source", params.getSource());
            countValidationQuery.setParameter("source", params.getSource());
        }
        if (!StringUtils.isEmpty(params.getSourceName())) {
            getValidationQuery.setParameter("sourceName", "%" + params.getSourceName().toLowerCase() + "%");
            countValidationQuery.setParameter("sourceName", "%" + params.getSourceName().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getSourceCode())) {
            getValidationQuery.setParameter("sourceCode", params.getSourceCode());
            countValidationQuery.setParameter("sourceCode", params.getSourceCode());
        }
        if (!StringUtils.isEmpty(params.getAgId())) {
            getValidationQuery.setParameter("agId", params.getAgId());
            countValidationQuery.setParameter("agId", params.getAgId());
        }
        if (!StringUtils.isEmpty(params.getAgName())) {
            getValidationQuery.setParameter("agName", "%" + params.getAgName().toLowerCase() + "%");
            countValidationQuery.setParameter("agName", "%" + params.getAgName().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getComment())) {
            getValidationQuery.setParameter("comment", params.getComment().toLowerCase());
            countValidationQuery.setParameter("comment", params.getComment().toLowerCase());
        }
        if (!StringUtils.isEmpty(params.getAddress())) {
            getValidationQuery.setParameter("address", "%" + params.getAddress() + "%");
            countValidationQuery.setParameter("address", "%" + params.getAddress() + "%");
        }
        if (!StringUtils.isEmpty(params.getFfmId())) {
            getValidationQuery.setParameter("ffmId", params.getFfmId());
            countValidationQuery.setParameter("ffmId", params.getFfmId());
        }
        if (!StringUtils.isEmpty(params.getFfmName())) {
            getValidationQuery.setParameter("ffmName", params.getFfmName().toLowerCase());
            countValidationQuery.setParameter("ffmName", params.getFfmName().toLowerCase());
        }
        if (!StringUtils.isEmpty(params.getWarehouseId())) {
            getValidationQuery.setParameter("warehouseId", params.getWarehouseId());
            countValidationQuery.setParameter("warehouseId", params.getWarehouseId());
        }
        if (!StringUtils.isEmpty(params.getWarehouseName())) {
            getValidationQuery.setParameter("warehouseName", params.getWarehouseName().toLowerCase());
            countValidationQuery.setParameter("warehouseName", params.getWarehouseName().toLowerCase());
        }
        if (!StringUtils.isEmpty(params.getLastmileId())) {
            getValidationQuery.setParameter("lastmileId", params.getLastmileId());
            countValidationQuery.setParameter("lastmileId", params.getLastmileId());
        }
        if (!StringUtils.isEmpty(params.getLastmileName())) {
            getValidationQuery.setParameter("lastmileName", "%" + params.getLastmileName().toLowerCase() + "%");
            countValidationQuery.setParameter("lastmileName", "%" + params.getLastmileName().toLowerCase() + "%");
        }

        if (!StringUtils.isEmpty(params.getPaymentMethod())) {
            if (params.getPaymentMethod().compareTo("COD") == 0) {
                getValidationQuery.setParameter("paymentMethod", 1);
                countValidationQuery.setParameter("paymentMethod", 1);
            } else if (params.getPaymentMethod().compareTo("Banking transfer") == 0) {
                getValidationQuery.setParameter("paymentMethod", 2);
                countValidationQuery.setParameter("paymentMethod", 2);
            } else if (params.getPaymentMethod().compareTo("Deposit") == 0) {
                getValidationQuery.setParameter("paymentMethod", 3);
                countValidationQuery.setParameter("paymentMethod", 3);
            }
            String currentGEO = _COUNTRY.toLowerCase().trim();
            if (currentGEO.equalsIgnoreCase("th") || currentGEO.equalsIgnoreCase("tl")) {
                if (params.getPaymentMethod().compareTo("Payment gateway (2C2P)") == 0 || params.getPaymentMethod().toLowerCase().contains("2C2P".toLowerCase())) {
                    getValidationQuery.setParameter("paymentMethod", 5);
                    countValidationQuery.setParameter("paymentMethod", 5);
                }
            } else if (currentGEO.equalsIgnoreCase("id") || currentGEO.equalsIgnoreCase("indo")) {
                if (params.getPaymentMethod().compareTo("Payment gateway (ESPay)") == 0 || params.getPaymentMethod().toLowerCase().contains("ESPay".toLowerCase())) {
                    getValidationQuery.setParameter("paymentMethod", 4);
                    countValidationQuery.setParameter("paymentMethod", 4);
                }
            }
        }
        if (!StringUtils.isEmpty(params.getLeadStatusId())) {
            getValidationQuery.setParameter("leadStatusId", params.getLeadStatusId());
            countValidationQuery.setParameter("leadStatusId", params.getLeadStatusId());
        }
        if (!StringUtils.isEmpty(params.getLeadStatus())) {
            List<String> status = Arrays.asList(params.getLeadStatus().split(","));
            getValidationQuery.setParameter("leadStatus", status);
            countValidationQuery.setParameter("leadStatus", status);
        }
        if (!StringUtils.isEmpty(params.getSoStatusId())) {
            getValidationQuery.setParameter("soStatusId", params.getSoStatusId());
            countValidationQuery.setParameter("soStatusId", params.getSoStatusId());
        }
        if (!StringUtils.isEmpty(params.getOrderStatus())) {
            getValidationQuery.setParameter("orderStatus", params.getOrderStatus().toLowerCase());
            countValidationQuery.setParameter("orderStatus", params.getOrderStatus().toLowerCase());
        }
        if (!StringUtils.isEmpty(params.getDoStatusId())) {
            getValidationQuery.setParameter("doStatusId", params.getDoStatusId());
            countValidationQuery.setParameter("doStatusId", params.getDoStatusId());
        }
        if (!StringUtils.isEmpty(params.getDeliveryStatus())) {
            getValidationQuery.setParameter("deliveryStatus", params.getDeliveryStatus().toLowerCase());
            countValidationQuery.setParameter("deliveryStatus", params.getDeliveryStatus().toLowerCase());
        }
        if (!StringUtils.isEmpty(params.getLocationStatus()) && params.getOrgId() == 9) {
            getValidationQuery.setParameter("locationStatus", params.getLocationStatus().toLowerCase());
            countValidationQuery.setParameter("locationStatus", params.getLocationStatus().toLowerCase());
        }
        if (!StringUtils.isEmpty(params.getLeadType())) {
            getValidationQuery.setParameter("leadType", params.getLeadType());
            countValidationQuery.setParameter("leadType", params.getLeadType());
        }
        if (!StringUtils.isEmpty(params.getCreatedate())) {
            getValidationQuery.setParameter("createDate", params.getCreatedate());
            countValidationQuery.setParameter("createDate", params.getCreatedate());
        }
        if (!StringUtils.isEmpty(params.getModifyDate())) {
            getValidationQuery.setParameter("modifyDate", params.getModifyDate());
            countValidationQuery.setParameter("modifyDate", params.getModifyDate());
        }
        if (!StringUtils.isEmpty(params.getTotalCall())) {
            getValidationQuery.setParameter("totalCall", params.getTotalCall());
            countValidationQuery.setParameter("totalCall", params.getTotalCall());
        }
        if (!StringUtils.isEmpty(params.getCpId())) {
            getValidationQuery.setParameter("cpId", params.getCpId());
            countValidationQuery.setParameter("cpId", params.getCpId());
        }
        if (!StringUtils.isEmpty(params.getCpName())) {
            getValidationQuery.setParameter("cpName", "%" + params.getCpName().toLowerCase() + "%");
            countValidationQuery.setParameter("cpName", "%" + params.getCpName().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getCallinglistId())) {
            getValidationQuery.setParameter("callingListId", params.getCallinglistId());
            countValidationQuery.setParameter("callingListId", params.getCallinglistId());
        }
        if (!StringUtils.isEmpty(params.getCallinglistName())) {
            getValidationQuery.setParameter("callingListName", "%" + params.getCallinglistName().toLowerCase() + "%");
            countValidationQuery.setParameter("callingListName", "%" + params.getCallinglistName().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getCreationDate())) {
            getValidationQuery.setParameter("creationDate", params.getCreationDate());
            countValidationQuery.setParameter("creationDate", params.getCreationDate());
        }
        if (!StringUtils.isEmpty(params.getAffiliateId())) {
            getValidationQuery.setParameter("affiliateId", "%" + params.getAffiliateId().toLowerCase() + "%");
            countValidationQuery.setParameter("affiliateId", "%" + params.getAffiliateId().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getSubid1())) {
            getValidationQuery.setParameter("subid1", "%" + params.getSubid1().toLowerCase() + "%");
            countValidationQuery.setParameter("subid1", "%" + params.getSubid1().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getReason())) {
            getValidationQuery.setParameter("reason", "%" + params.getReason().toLowerCase() + "%");
            countValidationQuery.setParameter("reason", "%" + params.getReason().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getQaNote())) {
            getValidationQuery.setParameter("qaNote", "%" + params.getQaNote().toLowerCase() + "%");
            countValidationQuery.setParameter("qaNote", "%" + params.getQaNote().toLowerCase() + "%");
        }
        if (!StringUtils.isEmpty(params.getLimit()))
            getValidationQuery.setParameter("limit", params.getLimit());
        if (!StringUtils.isEmpty(params.getOffset()))
            getValidationQuery.setParameter("offset", params.getOffset());

        List<Object[]> rows = getValidationQuery.getResultList();
        ValidationRespDto result = new ValidationRespDto();
        List<GetValidation7Resp> listValidation = new ArrayList<>();
        for (Object[] row : rows) {
            GetValidation7Resp validationResp = new GetValidation7Resp();
            if (row[0] != null)
                validationResp.setOrgId((Integer) row[0]);
            if (row[1] != null)
                validationResp.setSoId((Integer) row[1]);
            if (row[2] != null)
                validationResp.setDoId((Integer) row[2]);
            if (row[3] != null)
                validationResp.setDoCode((String) row[3]);
            if (row[4] != null)
                validationResp.setLeadId((Integer) row[4]);
            if (row[5] != null)
                validationResp.setLeadName((String) row[5]);
            if (row[6] != null)
                validationResp.setLeadPhone((String) row[6]);
            if (row[7] != null)
                validationResp.setProductName((String) row[7]);
            if (row[8] != null)
                validationResp.setProductCrosssell((String) row[8]);
            if (row[9] != null) {
                BigDecimal amount = (BigDecimal) row[9];
                validationResp.setAmount(amount.doubleValue());
            }
            if (row[10] != null)
                validationResp.setSource((Integer) row[10]);
            if (row[11] != null)
                validationResp.setSourceName((String) row[11]);
            if (row[12] != null)
                validationResp.setSourceCode((String) row[12]);
            if (row[13] != null)
                validationResp.setAgId((Integer) row[13]);
            if (row[14] != null)
                validationResp.setAgName((String) row[14]);
            if (row[15] != null)
                validationResp.setComment((String) row[15]);
            if (row[16] != null)
                validationResp.setAddress((String) row[16]);

            if (row[17] != null)
                validationResp.setLocationStatus((String) row[17]);

            if (row[18] != null)
                validationResp.setFfmId((Integer) row[18]);
            if (row[19] != null)
                validationResp.setFfmName((String) row[19]);
            if (row[20] != null)
                validationResp.setWarehouseId((Integer) row[20]);
            if (row[21] != null)
                validationResp.setWarehouseName((String) row[21]);
            if (row[22] != null)
                validationResp.setLastmileId((Integer) row[22]);
            if (row[23] != null)
                validationResp.setLastmileName((String) row[23]);
            if (row[24] != null)
                validationResp.setPaymentMethod((String) row[24]);
            if (row[25] != null)
                validationResp.setLeadStatusId((Integer) row[25]);
            if (row[26] != null)
                validationResp.setLeadStatus((String) row[26]);
            if (row[27] != null)
                validationResp.setSoStatusId((Integer) row[27]);
            if (row[28] != null)
                validationResp.setOrderStatus((String) row[28]);
            if (row[29] != null)
                validationResp.setDoStatusId((Integer) row[29]);
            if (row[30] != null)
                validationResp.setDeliveryStatus((String) row[30]);
            if (row[31] != null)
                validationResp.setLeadType((String) row[31]);
            if (row[32] != null)
                validationResp.setLeadTypeName((String) row[32]);
            if (row[33] != null)
                validationResp.setCreatedate((Date) row[33]);
            if (row[34] != null)
                validationResp.setModifyDate((Date) row[34]);
            if (row[35] != null)
                validationResp.setTotalCall((Integer) row[35]);
            if (row[36] != null)
                validationResp.setCpId((Integer) row[36]);
            if (row[37] != null)
                validationResp.setCpName((String) row[37]);
            if (row[38] != null)
                validationResp.setCallinglistId((Integer) row[38]);
            if (row[39] != null)
                validationResp.setCallinglistName((String) row[39]);
            if (row[40] != null)
                validationResp.setAgentNote((String) row[40]);
            if (row[41] != null)
                validationResp.setCreationDate((Date) row[41]);
            if (ObjectUtils.allNotNull(row[42])) {
                validationResp.setValidateBy((Integer) row[42]);
            }
            if (ObjectUtils.allNotNull(row[43])) {
                validationResp.setValidateByName((String) row[43]);
            }
            if (row[44] != null)
                validationResp.setAffiliateId((String) row[44]);
            if (row[45] != null)
                validationResp.setSubid1((String) row[45]);
            if (row[46] != null)
                validationResp.setReason((String) row[46]);
            if (row[47] != null)
                validationResp.setQaNote((String) row[47]);

            if (ObjectUtils.allNotNull(row[48])) {
                validationResp.setProvinceName((String) row[48]);
            }

            if (ObjectUtils.allNotNull(row[49])) {
                validationResp.setDistrictName((String) row[49]);
            }

            if (ObjectUtils.allNotNull(row[50])) {
                validationResp.setSubDistrictName((String) row[50]);
            }

            if (params.getOrgId() == 9 || params.getOrgId() == 10) {
                if (ObjectUtils.allNotNull(row[51])) {
                    validationResp.setZipCode((String) row[51]);
                }

                if (ObjectUtils.allNotNull(row[52])) {
                    validationResp.setNeighborhoodName((String) row[52]);
                }
            }

            listValidation.add(validationResp);
        }

        if (isRunSubnameAff && listValidation.size() >0) {
            mappingSubnameValidation(listValidation);
        }

        result.setGetValidation7Resps(listValidation);

        BigInteger rowCount = (BigInteger) countValidationQuery.getResultList().get(0);
        Integer count = rowCount.intValue();
        result.setRowCount(count);

        return result;
    }

    private void mappingSubnameValidation(List<GetValidation7Resp> listValidation) {
        List<TrkAffSubnameMapping> affSubnameMappings = trkAffSubnameMappingRepository.findAll();
        HashMap<String, String> mapSubname = new HashMap<>();
        for (TrkAffSubnameMapping affSubnameMapping: affSubnameMappings) {
            mapSubname.put(affSubnameMapping.getAffName(), affSubnameMapping.getSubName());
        }
        for (GetValidation7Resp order : listValidation){
            if (!StringUtils.isEmpty(order.getAffiliateId()) && mapSubname.containsKey(order.getAffiliateId())){
                order.setAffiliateId(order.getAffiliateId() + mapSubname.get(order.getAffiliateId()));
            }
        }

    }

    @Override
    public String getDefaultCreateDateInCommisionDataOrCollectionData() {
        LocalDate endDate = LocalDate.now();

        LocalDate startDate = endDate.minusDays(60);

        String result = "" + startDate.getYear()
                + (startDate.getMonthValue() >= 10 ? startDate.getMonthValue() : "0" + startDate.getMonthValue())
                + (startDate.getDayOfMonth() >= 10 ? startDate.getDayOfMonth() : "0" + startDate.getDayOfMonth())
                + "000000" + "|" + endDate.getYear()
                + (endDate.getMonthValue() >= 10 ? endDate.getMonthValue() : "0" + endDate.getMonthValue())
                + (endDate.getDayOfMonth() >= 10 ? endDate.getDayOfMonth() : "0" + endDate.getDayOfMonth()) + "235959";

        return result;
    }

    @Override
    public boolean createDOCancel(InsDoNew insDoNew, Integer leadId, int _curOrgId, int userId) throws TMSException {
        String SESSION_ID = UUID.randomUUID().toString();
        StringBuilder errMessage = new StringBuilder();
        // get lead detail
        try {
            GetLeadParamsV8 clFreshParams = new GetLeadParamsV8();
            clFreshParams.setLeadId(leadId);
            clFreshParams.setOrgId(_curOrgId);
            DBResponse<List<CLFresh>> dbfresh = clFreshService.getLeadV8(SESSION_ID, clFreshParams);
            logger.info("--------------------------- {}", leadId);
            if (dbfresh.getResult().isEmpty()) {
                logger.info(ErrorMessage.LEAD_NOT_FOUND.getMessage());
                errMessage.append("LEAD NOT FOUND " + leadId + "  " + _curOrgId);
                insDoNew.setErrorMessage(errMessage.toString());
                logService.insDoNew(SESSION_ID, insDoNew);
                throw new TMSException(ErrorMessage.LEAD_NOT_FOUND);
            }
            CLFresh fresh = dbfresh.getResult().get(0);

            insDoNew.setCustomerName(fresh.getName());
            insDoNew.setCustomerPhone(fresh.getPhone());
            insDoNew.setCustomerAddress(fresh.getAddress());
            insDoNew.setCustomerId(leadId);
            insDoNew.setOrgId(_curOrgId);
            insDoNew.setAmountcod(Double.valueOf(0));

            DBResponse dbResponse = logService.insDoNew(SESSION_ID, insDoNew);

            String doNewId = dbResponse.getErrorMsg().trim();
            int doId = 0;
            try {
                doId = Integer.parseInt(doNewId);
            } catch (Exception e) {
                doId = 0;
                logger.debug(e.getMessage());
            }
            String doCode = DeliveryHelper.createEKWDoCode(doId);
            UpdDoNew updDoNew = new UpdDoNew();
            updDoNew.setDoId(doId);
            updDoNew.setDoCode(doCode);
            updDoNew.setOrgId(_curOrgId);
            updDoNew.setStatus(EnumType.DO_STATUS_ID.CANCEL.getValue());
            logService.updDoNew(SESSION_ID, updDoNew);

            String key = RedisHelper.createRedisKey(Const.REDIS_PRERIX_DO, _curOrgId, userId);
            RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(doNewId), insDoNew.getSoId() + "|" + leadId);
        } catch (Exception e) {
            logger.info("Create DO cancel fail: " + e.getMessage());
            return false;
        }
        return true;
    }
    
    
    public TMSResponse mappingFFMLastsmile() {
    	GetWareHouse params1 = new GetWareHouse();
          String SESSION_ID = UUID.randomUUID().toString();
		DBResponse<List<GetWareHouseResp>> getWareHouseResp = clProductService.getWareHouse(SESSION_ID, params1);

		GetDataMappingWarehouse getData = new GetDataMappingWarehouse();
		List<GetDataFfm> listFfm = new ArrayList<>();
		
		GetMappingFFMLastSmile params = new GetMappingFFMLastSmile();
		DBResponse<List<GetMappingFFMLastSmileResp>> getMappingFFMLastSmileResp = clProductService
				.getMappingFFMLastsmile(SESSION_ID, params);

		HashMap<Integer, GetMappingFFMLastSmileResp> mappingWarehouse = new HashMap<>(); 
		for (GetMappingFFMLastSmileResp getMp : getMappingFFMLastSmileResp.getResult()) {
			mappingWarehouse.put(getMp.getFfmPartnerId(), getMp);
		}
		for (Map.Entry<Integer, GetMappingFFMLastSmileResp> list : mappingWarehouse.entrySet()) {
			GetDataFfm getDataFfm = new GetDataFfm();
			getDataFfm.setFfmPartnerId(list.getKey());
			getDataFfm.setNameFfm(list.getValue().getNameFfm());
			listFfm.add(getDataFfm);
		}
		for (GetDataFfm getDataFFm : listFfm) {
			List<GetDataLastSmile> listLm = new ArrayList<>();
			for (GetMappingFFMLastSmileResp list : getMappingFFMLastSmileResp.getResult()) {
				if(getDataFFm.getFfmPartnerId().intValue() == list.getFfmPartnerId().intValue()) {
					GetDataLastSmile dataLm = new GetDataLastSmile();
					dataLm.setLmPartnerId(list.getLmPartnerId());
					dataLm.setNameLm(list.getNameLm());
					listLm.add(dataLm);
				}
			}
			getDataFFm.setListLastSmile(listLm);
			List<GetWareHouseResp> listWareHouse = new ArrayList<>();
			for (GetWareHouseResp getDataFfm2 : getWareHouseResp.getResult()) {
				if(getDataFfm2.getPartnerId().intValue() == getDataFFm.getFfmPartnerId().intValue()) {
					listWareHouse.add(getDataFfm2);
				}
			}
			getDataFFm.setListWarehouse(listWareHouse);
		}
		getData.setListFfm(listFfm);
		return TMSResponse.buildResponse(getData);
    }
    @Override 
    public boolean CreateDONewWhenChangeWarehouse(String SESSION_ID,Integer orgId,Integer userId,InsDoNew insDoNew, Integer leadId, int _curOrgId, int ffmPartnerIdNew,
			int lmPartnerIdNew, int warehouseIdNew) throws TMSException {
		logger.info("########################################################");
		StringBuilder errMessage = new StringBuilder();
		// get lead detail
		GetLeadParamsV8 clFreshParams = new GetLeadParamsV8();
		clFreshParams.setLeadId(leadId);
		clFreshParams.setOrgId(_curOrgId);
		DBResponse<List<CLFresh>> dbfresh = clFreshService.getLeadV8(SESSION_ID, clFreshParams);
		logger.info("--------------------------- {}", leadId);
		if (dbfresh.getResult().isEmpty()) {
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
		if (!StringHelper.isNullOrEmpty(fresh.getNeighborhood()))
			neighborId = Integer.parseInt(fresh.getNeighborhood());

		if (!StringHelper.isNullOrEmpty(fresh.getPostalCode()))
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
		String mappingType = "1";
		int mappingLocation = provinceId;
		try {
            GetDefaultDOV3 getDefaultDO = new GetDefaultDOV3();
			getDefaultDO.setOrgId(_curOrgId);
			getDefaultDO.setProvinceId(provinceId);
			// TODO need to implement new code
			mappingType = RedisHelper.getGlobalParamValue(stringRedisTemplate, _curOrgId, 9, 2);
			logger.info("mappingType ======={}", mappingType);
			switch (mappingType) {
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

			if (dbDo.getResult().isEmpty())
				parterId = ffmPartnerIdNew;
			else {
				parterId = ffmPartnerIdNew;
				// gan code cua warehouse = warehouse name (DB desinger)
				// String tmpWarehouseId = dbDo.getResult().get(0).getWarehouseName();
				// warehouseId = Integer.parseInt(tmpWarehouseId);
				warehouseId = warehouseIdNew;

				logger.info("warehouseId = {}", warehouseId);

                warehouseShortname = dbDo.getResult().get(0).getWarehouseShortname();
				// TODO need to read from config default value
				lastmileId = lmPartnerIdNew;
				/*
				 * if (warehouseId == Const.KERRY_FFM_PARTNER_ID || warehouseId ==
				 * Const.SAP_FFM_PARTNER_ID) {//neu la warehouse cua SAP FFM thi dat = ma cua
				 * SAP lastmile warehouseId = lastmileId; }
				 */
				pickupId = dbDo.getResult().get(0).getPickupId();
				logger.info("{} Changer Warehouse Lastsmile and ffm", SESSION_ID);
			}
			logger.info("**************************** " + parterId);
			// get list product
			lst = this.getProductListBySoId(SESSION_ID,insDoNew, parterId);
			// send to queue
			if (fresh.getTerms() != null && fresh.getClickId() != null
					&& String.valueOf(EnumType.AGENTCY_TYPE.CPO.getValue()).equals(fresh.getTerms())) {
				String payout = "0";
				if (fresh.getPrice() != null)
					payout = fresh.getPrice();
				String offerId = "0";
				if (fresh.getOfferId() != null)
					offerId = fresh.getOfferId();
				int numOfProd = 1;
				if (fresh.getAgcId() == Const.AGENCY_ADFLEX)
					if (lst.isEmpty())
						numOfProd = 1;
					else {
						numOfProd = 0;
						for (ProductDto productDto : lst)
							numOfProd += productDto.getQty();
					}
				String message = "";
				if (null != fresh.getTrackerId() && 0 != fresh.getTrackerId()) {
                    payout = Integer.toString(numOfProd);
                    message = QueueHelper.createQueueMessage(_curOrgId, fresh.getAgcId(), fresh.getClickId(), 1,
                            offerId, fresh.getLeadId(), "TRACKER", payout, EnumType.AGENTCY_TYPE.CPO.getValue(),
                            fresh.getTrackerId(), fresh.getSubid4());
                } else if (fresh.getAgcId() == Const.AGENCY_ARB)
					message = QueueHelper.createQueueMessage(fresh.getOrgId(), fresh.getAgcId(), fresh.getClickId(), 1,
							offerId, fresh.getLeadId(),
							String.format("%s;%s", fresh.getAffiliateId(), fresh.getSubid5()), payout,
							Integer.valueOf(fresh.getTerms()));
				else
					message = QueueHelper.createQueueMessage(_curOrgId, fresh.getAgcId(), fresh.getClickId(), 1,
							offerId, fresh.getLeadId(), payout, EnumType.AGENTCY_TYPE.CPO.getValue(), numOfProd);

				QueueHelper.sendMessage(message, Const.QUEUE_AGENTCY);
				logger.info("[QUEUE] " + Const.QUEUE_AGENTCY + " sent message " + message);
			}

			GetPickup pickupParam = new GetPickup();
			pickupParam.setOrgId(_curOrgId);
			pickupParam.setPickupId(pickupId != null ? pickupId.toString() : "1");

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
			if (!dbWarehouse.getResult().isEmpty()) {
				GetWareHouseResp wareHouseResp = dbWarehouse.getResult().get(0);
				deliveryDto.setsAddress(wareHouseResp.getAddress());
				logger.info("{} {} warehouseId out 3 = {}", SESSION_ID, insDoNew.getSoId(), wareHouseResp.getAddress());
				deliveryDto.setsDistrictName(wareHouseResp.getDistrictName());
				deliveryDto.setsProvinceName(wareHouseResp.getProvinceName());
				deliveryDto.setsDistrictId(wareHouseResp.getDistrictId());
				deliveryDto.setsProvinceId(wareHouseResp.getProvinceId());
                deliveryDto.setsDistrictCode(wareHouseResp.getDistrictCode());
                deliveryDto.setsProvinceCode(wareHouseResp.getProvinceCode());
                deliveryDto.setsWardName(wareHouseResp.getWardsName());
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
					|| parterId == Const.KERRY_FFM_PARTNER_ID ? lastmileId : parterId;

            if (parterId == Const.NTL_FFM_PARTNER_ID)
                threePl = 1;
			GetProvinceMapResp provinceMapResp = locationHelper.getProvinceMapper(provinceService, threePl,
					deliveryDto.getProvinceId(), SESSION_ID);
			logger.info("parterId ============ ============== " + parterId + " " + threePl + "  " + lastmileId);
			GetDistrictMapResp districtMapResp = locationHelper.getDistrictMapper(provinceService, threePl,
					deliveryDto.getDistrictId(), SESSION_ID);
			GetSubdistrictMapResp subdistrictMapResp = null;
			if (Helper.isIndonesia(_COUNTRY) || Helper.isThailand(_COUNTRY) || parterId == Const.WFS_PARTNER_ID || parterId == Const.NTL_FFM_PARTNER_ID) {
				subdistrictMapResp = locationHelper.getSubDistrictMapper(provinceService, threePl,
						deliveryDto.getWardsId(), SESSION_ID);
                if (subdistrictMapResp != null)
                    deliveryDto.setWardName(subdistrictMapResp.getName());
				if (parterId == Const.WFS_PARTNER_ID)
					try {
						deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
					} catch (Exception e) {
						logger.error("Can not convert subdistrictMapResp.getWardsPartnerCode {}", subdistrictMapResp.getWardsPartnerCode(), e);
					}
			}

			try {
				if (provinceMapResp != null) {
					if (threePl == Const.WFS_PARTNER_ID) {
						int prvId = Integer.parseInt(provinceMapResp.getPrvPartnerCode());
						deliveryDto.setProvinceId(prvId);
					} else
						deliveryDto.setProvinceId(provinceMapResp.getPrvId());
					deliveryDto.setProvinceName(provinceMapResp.getPrvPartnerName());

					String province = threePl != Const.DHL_PARTNER_ID ? provinceMapResp.getPrvPartnerCode() != null
							? provinceMapResp.getPrvPartnerCode().toString()
							: "" : provinceMapResp.getName();
					insDoNew.setCustomerProvince(province);
				}

				if (districtMapResp != null) {
					if (threePl == Const.WFS_PARTNER_ID || threePl == Const.BM_INDO_PARTNER_ID) {
						int dtId = Integer.parseInt(districtMapResp.getDtPartnerCode());
                        deliveryDto.setGhnDistrictCode(districtMapResp.getDtPartnerCode());
						deliveryDto.setDistrictId(dtId);
					} else if (threePl == Const.DHL_FFM_PARTNER_ID) {
						deliveryDto.setDistrictCode(districtMapResp.getDtPartnerCode());
					} else
						deliveryDto.setDistrictId(districtMapResp.getDistrictId());

					deliveryDto.setDistrictName(districtMapResp.getDtPartnerName());
					String district = threePl != Const.DHL_PARTNER_ID
							? districtMapResp.getDtPartnerCode() != null ? districtMapResp.getDtPartnerCode().toString()
									: ""
							: districtMapResp.getDtPartnerName();
					logger.info("Check cau hinh ****************** {}-{}-{} ", threePl, lastmileId, parterId);
					/*
					 * if (lastmileId == Const.DHL_PARTNER_ID)
					 * deliveryDto.setDistrictName(district);
					 */

					insDoNew.setCustomerDistrict(district);
				}
			} catch (Exception e) {
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

			if (lastmileId == Const.DHL_PARTNER_ID && parterId == Const.WFS_PARTNER_ID) {
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService, Const.WFS_PARTNER_ID,
						Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService, Const.WFS_PARTNER_ID,
						Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				if (provMapResp != null) {
					int prvId = Integer.parseInt(provMapResp.getPrvPartnerCode());
					deliveryDto.setProvinceId(prvId);
				}
				if (dtMapResp != null) {
					int dtId = Integer.parseInt(dtMapResp.getDtPartnerCode());
					deliveryDto.setDistrictId(dtId);
				}

			}

			if (lastmileId == Const.DHL_PARTNER_ID) {
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService, Const.DHL_PARTNER_ID,
						Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService, Const.DHL_PARTNER_ID,
						Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				if (provMapResp != null)
					deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
				if (dtMapResp != null)
					deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());

			}
			if (lastmileId == Const.GHTK_LM_PARTNER_VN) {
				GetProvinceMapResp provMapResp = locationHelper.getProvinceMapper(provinceService,
						Const.GHTK_LM_PARTNER_VN, Helper.IntergeTryParse(fresh.getProvince()), SESSION_ID);
				GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
						Const.GHTK_LM_PARTNER_VN, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
				GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
						Const.GHTK_LM_PARTNER_VN, deliveryDto.getWardsId(), SESSION_ID);
				if (provMapResp != null)
					deliveryDto.setProvinceName(provMapResp.getPrvPartnerName());
				if (dtMapResp != null)
					deliveryDto.setDistrictName(dtMapResp.getDtPartnerName());
				if (sdtMapResp != null)
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
                GetDistrictMapResp dtMapResp = locationHelper.getDistrictMapper(provinceService,
                        Const.WFS_PARTNER_ID, Helper.IntergeTryParse(fresh.getDistrict()), SESSION_ID);
                GetSubdistrictMapResp sdtMapResp = locationHelper.getSubDistrictMapper(provinceService,
                        Const.WFS_PARTNER_ID, deliveryDto.getWardsId(), SESSION_ID);
                if(dtMapResp != null)
                    deliveryDto.setGhnDistrictCode(dtMapResp.getDtPartnerCode());
                if(sdtMapResp != null)
                    deliveryDto.setGhnWardCode(sdtMapResp.getWardsPartnerCode());
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

			if (subdistrictMapResp != null && !StringHelper.isNullOrEmpty(subdistrictMapResp.getDcsr()))
				deliveryDto.setLastmileService(subdistrictMapResp.getDcsr());

			// set sZipcode = subdistrict postcode by partner (lc_subdistrict_map.sdt_code)
			if (subdistrictMapResp != null)
				deliveryDto.setsZipCode(subdistrictMapResp.getWardsPartnerCode());

			// ---------------------
			if (Helper.isIndonesia(_COUNTRY) && subdistrictMapResp != null) {// only Indonesia need to get subdistrict
				deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
				deliveryDto.setWardName(subdistrictMapResp.getName());

				insDoNew.setCustomerWards(subdistrictMapResp.getWardsPartnerCode());

				if (threePl == Const.BM_INDO_PARTNER_ID) {

					deliveryDto.setProvinceCode(deliveryDto.getDistrictId().toString());
					deliveryDto.setWardsCode(subdistrictMapResp.getWardsPartnerCode());
					if (!StringHelper.isNullOrEmpty(subdistrictMapResp.getDcsr())) {
						String[] path = subdistrictMapResp.getDcsr().split(",");
						if (path.length == 5)
							deliveryDto.setrZipCode(path[4]);
					}
				} else if (parterId == Const.KERRY_FFM_PARTNER_ID) {
					deliveryDto.setrZipCode(subdistrictMapResp.getWardsPartnerCode());
					GetNeighbordhoodResp nghResp = locationHelper.getNeighborhoodMapper(deliveryOrderService,
							Const.KERRY_LM_PARTNER_ID, neighborId, SESSION_ID);
					if (nghResp != null)
						deliveryDto.setrZipCode(nghResp.getCode());
				}

			}
//            return true;

			// update nghiep vu (lay so luong product)

			logger.info("---------------------- -----------------  {}", lst.size());
			if (lst.size() == 0) {
				hasProdInFFToCreateDO = false;
				prodMessage = "Size of SO Item not equal size of product mapping, getProductListBySoId  SO id = "
						+ insDoNew.getSoId();
				insDoNew.setErrorMessage(prodMessage);
				insDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
			}

			String packageName = "";
			String formatPackage = "%s(%s)";
			for (int i = 0; i < lst.size(); i++)
				packageName += String.format(formatPackage, lst.get(i).getProName(), lst.get(i).getQty()) + ", ";
			insDoNew.setPackageName(packageName);
			deliveryDto.setPackageName(packageName);
			insDoNew.setPackageListItem(LogHelper.toJson(lst));
		} catch (Exception e) {
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
		try {
			doId = Integer.parseInt(doNewId);
			deliveryDto.setDoId(doId);
		} catch (Exception e) {
			doId = 0;
			logger.debug(e.getMessage());
		}

		dbLog.writeDOStatusLog(userId, doId, EnumType.DO_STATUS_ID.NEW.getValue(), "Create DO");
		dbLog.writeAgentActivityLog(userId, "create delivery order", "delivery order", doId, "do_status",
				EnumType.DO_STATUS_ID.NEW.getValue() + "");

		logger.info("---------------------- -----------------  2 {}", hasProdInFFToCreateDO);
		if (!hasProdInFFToCreateDO)
			return true;

		String key = RedisHelper.createRedisKey(Const.REDIS_PRERIX_DO, _curOrgId, userId);
		RedisHelper.saveRedis(stringRedisTemplate, key, String.valueOf(doNewId), insDoNew.getSoId() + "|" + leadId);

		String globalValue = RedisHelper.getGlobalParamValue(stringRedisTemplate, _curOrgId, 4, 1);
		if (!isContinue)// co loi xay ra khi cau hinh, khong cho phep chay tiep
			return false;
		logger.info(globalValue + "  ---------------------- -----------------  3 {}", hasProdInFFToCreateDO);
		if (globalValue == null)
			return true;
		if (globalValue.equals("2"))// run create delivery manual
			return true;
		// #################### kiem tra san pham co con trong kho hay ko?
		// ######################
		logger.info("---------------------- -----------------  4 {}", hasProdInFFToCreateDO);
		if (warehouseId == null) {
			prodMessage = "Not configure warehouse id for partner " + parterId;
			hasProdInFFToCreateDO = false;
		} else
			for (int j = 0; j < lst.size(); j++) {
				Boolean hasProductInFF = DeliveryHelper.hasProductInFullfillment(parterId, lst.get(j),
						String.valueOf(warehouseId));
				if (!hasProductInFF) {
					prodMessage = lst.get(j).getProName() + " has not enough quantity in Fullfillment " + parterId;
					logger.info(
							SESSION_ID + " " + insDoNew.getSoId() + "############### " + doNewId + "|" + prodMessage);
					hasProdInFFToCreateDO = false;
					break;
				}
			}
		if (!hasProdInFFToCreateDO) {
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
		if (deliveryDto.getProvinceId() != null)
			provinceMapWHResp = locationHelper.getProvinceMapper(provinceService, parterId, deliveryDto.getProvinceId(),
					SESSION_ID);
		GetDistrictMapResp districtMapWHResp = null;
		if (deliveryDto.getDistrictId() != null)
			districtMapWHResp = locationHelper.getDistrictMapper(provinceService, parterId, deliveryDto.getDistrictId(),
					SESSION_ID);
		if (parterId == Const.SAP_FFM_PARTNER_ID || parterId == Const.KERRY_FFM_PARTNER_ID) {

		} else if (provinceMapWHResp != null && provinceMapWHResp.getPrvPartnerCode() != null) {
			int prvId = Integer.parseInt(provinceMapWHResp.getPrvPartnerCode());
			deliveryDto.setReicvWhProvinceId(prvId);
		}

		if (parterId == Const.SAP_FFM_PARTNER_ID || parterId == Const.KERRY_FFM_PARTNER_ID) {

		} else if (districtMapWHResp != null && districtMapWHResp.getDtPartnerCode() != null) {
			int dtId = Integer.parseInt(districtMapWHResp.getDtPartnerCode());
			deliveryDto.setReicvWhDistrictId(dtId);
			if (districtMapWHResp.getPnDistrictId() != null) {
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

		// Nếu là DHL FFM (đã có ffm code -> set ffm code -< gọi api update sale order
		// DHL FFM)
		if (parterId == Const.DHL_FFM_PARTNER_ID && insDoNew.getFfmCode() != null) {
			deliveryDto.setFfmCode(insDoNew.getFfmCode());
		}

		int totalAmount = 0;
		try {
			SaleOrder saleOrder = getSaleOrderDetail(SESSION_ID,orgId,insDoNew.getSoId());
			deliveryDto.setPaymentMethod(saleOrder.getPaymentMethod());
			if (deliveryDto.getPhone().contains("|"))
				deliveryDto.setPhone(saleOrder.getLeadPhone());
			totalAmount = saleOrder.getAmount().intValue();
		} catch (Exception e) {
			logger.info(SESSION_ID + " " + doNewId + " $$$$$$$$$ Could not get total amount  " + e.getMessage());
		}
		deliveryDto.setAmount(totalAmount);
		// TODO need to be calculate
		if (_COUNTRY.equals("ID"))
			totalAmount = Helper.RoundToHundred(Double.valueOf(totalAmount));
		if (deliveryDto.getPaymentMethod() != EnumType.PAYMENT_METHOD.BANKING.getValue())
			deliveryDto.setCod_money(totalAmount);
		else// la banking thi set = 0
			deliveryDto.setCod_money(0);

		logger.info("{} {} deliveryDto: {}\r\n======= ========== createDelivery partnerId={}",
                SESSION_ID, doNewId, LogHelper.toJson(deliveryDto), parterId);

		List<OrderResult> lstOrder = DeliveryHelper.createDelivery(parterId, deliveryDto, lst);
		if (lstOrder == null)
			logger.info(SESSION_ID + "[CreateDONew] Could not config Partner ID " + parterId + " " + doNewId);

		// update result
		boolean isAllSuccess = true;
		if (lstOrder != null && lstOrder.size() > 0) {
			logger.info("List Order: " + LogHelper.toJson(lstOrder));
			for (int i = 0; i < lstOrder.size(); i++) {
				OrderResult tmpOrder = lstOrder.get(i);
				UpdDoNew updDoNew = new UpdDoNew();
				updDoNew.setDoId(doId);
				updDoNew.setDoCode(deliveryDto.getDoCode());
				updDoNew.setOrgId(_curOrgId);
				updDoNew.setCustomerPhone(deliveryDto.getPhone());
				updDoNew.setFfmId(parterId);
				// TODO need to be change this method
				updDoNew.setAmountcod(Double.valueOf(deliveryDto.getCod_money()));
				if (isAllSuccess) {
					logger.info(tmpOrder.getType());
					switch (tmpOrder.getResult()) {
					case Const.DO_SUCCESS:
						updDoNew.setErrorMessage(null);
						updDoNew.setStatus(EnumType.DO_STATUS_ID.NEW.getValue());
						/*
						 * if (parterId < 2 || parterId > 5)//kho GHN
						 * updDoNew.setFfmCode(tmpOrder.getMessage()); else//lastmile
						 * updDoNew.setTrackingCode(tmpOrder.getMessage());
						 */
						if (tmpOrder.getType().equals(Const.BM_PARTNER_CODE)
								|| tmpOrder.getType().equals(Const.SAPW_PARTNER_CODE)
								|| tmpOrder.getType().equals(Const.MYCLOUD_PARTNER_CODE))
							updDoNew.setFfmCode(tmpOrder.getMessage());
                        else if (tmpOrder.getType().equals(Const.BM_EXPRESS_CODE)) {
                            updDoNew.setFfmCode(tmpOrder.getMessage());
                            updDoNew.setTrackingCode(tmpOrder.getMessage());
                        }
						else if (tmpOrder.getType().equals(Const.WFS_GHN_PARTNER_CODE)) {
							if (tmpOrder.getCode().compareTo("0") == 0) {

								String[] trackings = tmpOrder.getMessage().split("\\|");
								updDoNew.setFfmCode(trackings[0]);
								updDoNew.setTrackingCode(trackings[1]);
							} else {
								updDoNew.setErrorMessage(tmpOrder.getErrMessage());
								updDoNew.setStatus(EnumType.DO_STATUS_ID.PENDING.getValue());
							}
						} else if (tmpOrder.getType().equals(String.valueOf(Const.DHL_FFM_PARTNER_ID))) {
							if (tmpOrder.getMessage() != null && StringUtils.hasText(tmpOrder.getMessage())) {
								updDoNew.setFfmCode(tmpOrder.getMessage());
							}
						} else if (tmpOrder.getType().equals(Const.KERRY_TH_PARTNER_CODE)) {
							updDoNew.setFfmCode(tmpOrder.getMessage());
							updDoNew.setTrackingCode(tmpOrder.getMessage());
						} else if (tmpOrder.getType().equals(Const.FLASH_TH_PARTNER_CODE)) {
                            String[] trackings = tmpOrder.getMessage().split("\\|");
                            updDoNew.setFfmCode(trackings[0]);
                            updDoNew.setTrackingCode(trackings[1]);
                        } else if(tmpOrder.getType().equals(Const.NTL_EXPRESS_PARTNER_CODE)){
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
			if (isAllSuccess)
				return true;
		}
		return false;
	}

	private List<ProductDto> getProductListBySoId(String SESSION_ID,int soId, int orgId, Integer partnerId) {
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
		if (!resSO.getResult().isEmpty()) {
			SaleOrder so = resSO.getResult().get(0);
			totalAmount = so.getAmount();
		}

		List<GetSoItemResp> lstSOItem = responseSOItem.getResult();

		if (lstSOItem.size() > 0) {
			// tinh gia tri chia deu cho so loai san pham trong don hang
			Double amountPerSOItem = 0d;
			if (totalAmount > 0)
				amountPerSOItem = totalAmount / lstSOItem.size();

			for (int j = 0; j < lstSOItem.size(); j++) {

				int proId = lstSOItem.get(j).getProdId();
				// get product detail
				GetProductParams productParams = new GetProductParams();
				productParams.setProdId(proId);

				DBResponse<List<PDProduct>> dbProd = clProductService.getProduct(SESSION_ID, productParams);
				if (dbProd.getResult().isEmpty())
					return new ArrayList<>();// khong cho phep tao don hang nay

				GetProductMapping productMapping = new GetProductMapping();
				productMapping.setProductId(proId);
				productMapping.setPartnerId(partnerId);

				DBResponse<List<GetProductMappingResp>> dbProdMapping = clProductService.getProductMapping(SESSION_ID,
						productMapping);
				if (dbProdMapping.getResult().isEmpty())
					return new ArrayList<>();// khong cho phep tao don hang nay

				List<PDProduct> lstProd = dbProd.getResult();

				// tinh gia tri discount tinh cho loai san pham
				Double amountPerProduct = 0d;
				if (amountPerSOItem > 0)
					amountPerProduct = amountPerSOItem / lstProd.size();

				for (int i = 0; i < lstProd.size(); i++) {
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
					if (lstProd.get(i).getProductType() != null && lstProd.get(i).getProductType() == 2) {
						GetProductCombo getProductCombo = new GetProductCombo();
						GetProductComboResp comboResps = null;
						getProductCombo.setComboId(proId);
						DBResponse<List<GetProductComboResp>> comboDb = clProductService.getProductCombo(SESSION_ID,
								getProductCombo);
						if (!comboDb.getResult().isEmpty()) {
							comboResps = comboDb.getResult().get(0); // 1 combo chi co 1 ban ghi mapping ben product
																		// combo nen chi can get 0
							qtyCombo = comboResps.getQuantity();
							price = BigDecimal.valueOf(comboResps.getPrice() / qtyCombo)
									.setScale(2, RoundingMode.HALF_UP).doubleValue();
						}
					}

					// tinh gia tri 1 mat hang
					if (amountPerProduct > 0)
						price = BigDecimal.valueOf(amountPerProduct / (qty * qtyCombo))
								.setScale(2, RoundingMode.HALF_UP).doubleValue();

					product.setPrice(price);
					product.setQty(qty * qtyCombo);

					if (productMappingResp.getPartnerProductCode() != null)
						product.setProPartnerCode(productMappingResp.getPartnerProductCode());

					product.setProCode(productMappingResp.getPartnerProductId().toString());
					lst.add(product);
				}
			}
			if (lstSOItem.size() != lst.size())
				return new ArrayList<>();// khong cho phep tao don hang nay

			// TODO need change to list product not only one
//            deliveryOrder.setProdId(lstSOItem.get(0).getProdId());
		}
		return lst;
	}

	private List<ProductDto> getProductListBySoId(String SESSION_ID,InsDoNew insDoNew, Integer partnerId) {
		return getProductListBySoId(SESSION_ID,insDoNew.getSoId(), insDoNew.getOrgId(), partnerId);
	}

	private SaleOrder getSaleOrderDetail(String SESSION_ID,Integer orgId,Integer soId) throws TMSException {
		GetSOV2 params = new GetSOV2();
		params.setSoId(soId);
		params.setOrgId(orgId);
		DBResponse<List<SaleOrder>> dbResponseSaleOrder = deliveryOrderService.getSaleOrderV2(SESSION_ID, params);
		if (dbResponseSaleOrder.getResult().isEmpty())
			throw new TMSException(ErrorMessage.NOT_FOUND);
		SaleOrder soData = dbResponseSaleOrder.getResult().get(0);
		return soData;
	}

    @Override
    @Transactional
    public int deleteCallbackByLeadId(Integer leadId) {
        if (leadId == null) {
            return 0;
        }
        String sqlDeletedCallback = "UPDATE cl_callback SET is_deleted = 1, modifydate = now() WHERE lead_id = :leadId AND is_deleted = 0";
        Query query = entityManager.createNativeQuery(sqlDeletedCallback);
        query.setParameter("leadId", leadId);
        return query.executeUpdate();
    }

    /**
     * @param leadIds list of lead_id
     * @return String: list of leadId delete callback fail split by comma(",").
     * Return String "" when delete success.
     */
    @Override
    @Transactional
    public String deleteCallbackByLeadIds(List<Integer> leadIds) {
        StringJoiner deleteCallbackFail = new StringJoiner(",");
        for (Integer leadId : leadIds) {
            int updatedCallbackCount = deleteCallbackByLeadId(leadId);
            if (updatedCallbackCount == 0) {
                deleteCallbackFail.add(leadId.toString());
            }
        }

        return deleteCallbackFail.toString();
    }

    @Override
    public OdSaleOrder updateSaleOrder(OdSaleOrder saleOrder) {
        OdSaleOrder so = null;
        if (saleOrder != null && saleOrder.getSoId() != null){
            so = soRepo.getSaleOrder(saleOrder.getSoId());
            if (saleOrder.getReason() != null)
                so.setReason(saleOrder.getReason());
            if (saleOrder.getQaNote() != null)
                so.setQaNote(saleOrder.getQaNote());
            if (saleOrder.getModifydate() != null)
                so.setModifydate(saleOrder.getModifydate());
            if (saleOrder.getModifyby() != null)
                so.setModifyby(saleOrder.getModifyby());
            if (saleOrder.getCreationDate() != null)
                so.setCreationDate(saleOrder.getCreationDate());
            if (saleOrder.getValidateBy() != null)
                so.setValidateBy(saleOrder.getValidateBy());
            if (saleOrder.getStatus() != null)
                so.setStatus(saleOrder.getStatus());
            if (so.getStatus() == EnumType.SALE_ORDER_STATUS.VALIDATED.getValue())
                so.setReason(null);
            if (ObjectUtils.allNotNull(saleOrder.getAppointmentDate())) {
                so.setAppointmentDate(saleOrder.getAppointmentDate());
            }
            soRepo.save(so);
        }
        return so;
    }

    @Override
    public List<GetLogSoResp> getLogSO(Integer soId, Integer limit, Integer offset) {

        StringBuilder builder = new StringBuilder();
        builder.append(" select a.log_id, a.so_id, a.new_status, c.name as statusName, b.fullname user_name, a.comment, a.changetime  ");
        builder.append(" from log_sale_order a  ");
        builder.append(" left join or_user b on a.user_id = b.user_id  ");
        builder.append(" left join cf_synonym c on a.new_status = c.value and c.type_id = 4 ");
        builder.append(" where so_id = :soId ");
        builder.append(" order by a.log_id desc ");
        builder.append(" limit :limit offset :offset ");

        Query query = entityManager.createNativeQuery(builder.toString());
        query.setParameter("soId", soId);
        query.setParameter("limit", limit);
        query.setParameter("offset", offset);

        List<GetLogSoResp> result = new ArrayList<>();
        List<Object[]> rows = query.getResultList();

        for (Object[] row : rows) {
            GetLogSoResp logSO = new GetLogSoResp();
            logSO.setLogId((Integer) row[0]);
            logSO.setSoId((Integer) row[1]);
            logSO.setNewStatus((Integer) row[2]);
            logSO.setStatusName((String) row[3]);
            logSO.setUserName((String) row[4]);
            logSO.setComment((String) row[5]);
            logSO.setChangetime((Date) row[6]);

            result.add(logSO);
        }

        return result;
    }


    @Override
    public boolean isNotSaveUncall(CLFresh clFreshNew) {
        Integer leadId = clFreshNew.getLeadId();
        if (EnumType.LEAD_STATUS.isCallback(clFreshNew.getLeadStatus())) {
            return true;
        }
        Query query = entityManager.createNativeQuery("Select count(lead_id) from cl_callback where lead_id = :leadId");
        query.setParameter("leadId", leadId);
        int count = ((BigInteger) query.getSingleResult()).intValue();
        return count > 0;
    }
}
