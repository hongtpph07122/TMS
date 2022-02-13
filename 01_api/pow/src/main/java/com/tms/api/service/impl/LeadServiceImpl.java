package com.tms.api.service.impl;

import com.tms.api.dto.LeadInfoDto;
import com.tms.api.entity.ClFresh;
import com.tms.api.entity.OdDONew;
import com.tms.api.entity.OdSaleOrder;
import com.tms.api.helper.DateHelper;
import com.tms.api.helper.EnumType;
import com.tms.api.repository.LeadRepository;
import com.tms.api.repository.OdDONewRepository;
import com.tms.api.repository.SaleOrderRepository;
import com.tms.api.request.LeadsRequest;
import com.tms.api.service.LeadService;
import com.tms.api.utils.ObjectUtils;
import com.tms.dto.DBResponse;
import com.tms.dto.GetMktDataParams;
import com.tms.dto.GetMktDataResp;
import com.tms.entity.CLFresh;
import com.tms.entity.log.InsCLFreshV11;
import com.tms.entity.log.UpdLeadCrmActionType;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeadServiceImpl implements LeadService {

    private final Logger logger = LoggerFactory.getLogger(LeadServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final LeadRepository leadRepository;
    private final CLFreshService clFreshService;
    private final SaleOrderRepository saleOrderRepository;
    private final OdDONewRepository deliveryOrderRepository;
    private final LogService logService;

    @Autowired
    public LeadServiceImpl(
            LeadRepository leadRepository,
            CLFreshService clFreshService,
            SaleOrderRepository saleOrderRepository,
            OdDONewRepository deliveryOrderRepository,
            LogService logService) {
        this.leadRepository = leadRepository;
        this.clFreshService = clFreshService;
        this.saleOrderRepository = saleOrderRepository;
        this.deliveryOrderRepository = deliveryOrderRepository;
        this.logService = logService;
    }




    @Override
    public List<LeadInfoDto> getLeadInfo(List<Integer> status, List<Integer> cpId, String startTime, String endTime) {

        StringBuilder sql = new StringBuilder();
        sql.append(" select d.lead_id, d.name, d.phone, coalesce(d.address||'###'||n.name||'-'||m.name||'-'||l.name ,d.address) as Address, ");
        sql.append(" e.name as delivery_status, package_name, r.amountcod, q.name as payment_method, a.\"name\" as product_name, d.lead_status, ");
        sql.append(" r.customer_address as delivery_address, d.prod_id, d.user_defin_04 ");
        sql.append(" from cl_fresh d ");
        sql.append(" left join lc_province l on l.prv_id = cast(d.province as integer) ");
        sql.append(" left join lc_district m on m.dt_id = cast(d.district as integer) ");
        sql.append(" left join lc_subdistrict n on n.sdt_id = cast(d.subdistrict as integer) ");
        sql.append(" left join od_sale_order o on o.lead_id = d.lead_id ");
        sql.append(" left join (select * from cf_synonym where type = 'payment mothod') q on o.payment_method = q.value ");
        sql.append(" left join od_do_new r on r.so_id = o.so_id ");
        sql.append(" left join (select * from cf_synonym where type = 'delivery order status') e on e.value = r.status ");
        sql.append(" left join pd_product a on d.prod_id = a.prod_id ");
        sql.append(" where d.modifydate >=  to_timestamp(:startTime,'yyyymmddhh24miss') ");
        sql.append(" and d.modifydate  < to_timestamp(:endTime,'yyyymmddhh24miss') ");
        if (status.get(0).compareTo(EnumType.LEAD_STATUS.APPROVED.getValue()) == 0){
            sql.append(" and r.status in (61, 59) ");
        }
        sql.append(" and d.lead_status in :status ");
        sql.append(" and d.cp_id in :cpId ");
        sql.append(" order by d.createdate desc ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        query.setParameter("status", status);
        query.setParameter("cpId", cpId);

        List<LeadInfoDto> result = new ArrayList<>();
        List<Object[]> rows = query.getResultList();
        for(Object[] row : rows){
            LeadInfoDto leadInfoDto = new LeadInfoDto();
            if (row[0] != null){
                leadInfoDto.setLeadId((Integer) row[0]);
            }
            if (row[1] != null){
                leadInfoDto.setName((String) row[1]);
            }
            if (row[2] != null){
                leadInfoDto.setPhone((String) row[2]);
            }
            if (row[3] != null){
                leadInfoDto.setAddress((String) row[3]);
            }
            if (row[4] != null){
                leadInfoDto.setDoStatus((String) row[4]);
            }
            if (row[5] != null){
                leadInfoDto.setPackageListItem((String) row[5]);
            }
            if (row[6] != null){
                BigDecimal amount =  (BigDecimal) row[6];
                leadInfoDto.setAmount(amount.doubleValue());
            }
            if (row[7] != null){
                leadInfoDto.setPaymentMethod((String) row[7]);
            }
            if (row[8] != null){
                leadInfoDto.setProductName((String) row[8]);
            }
            if (row[9] != null){
                leadInfoDto.setLeadStatus((Integer) row[9]);
            }
            if (row[10] != null){
                leadInfoDto.setDoAddress((String) row[10]);
            }
            if (row[11] != null){
                leadInfoDto.setProdId((Integer) row[11]);
            }
            if (row[12] != null){
                leadInfoDto.setUserDefin04((String) row[12]);
            }

            result.add(leadInfoDto);
        }

        return result;
    }

    @Override
    public List<LeadInfoDto> getLeadInfoV2(List<Integer> status, List<Integer> cpId, String startTime, String endTime) {

        StringBuilder sql = new StringBuilder();
        sql.append(" select d.lead_id, d.name, d.phone, coalesce(d.address||'###'||n.name||'-'||m.name||'-'||l.name ,d.address) as Address, ");
        sql.append(" e.name as delivery_status, package_name, r.amountcod, q.name as payment_method, a.\"name\" as product_name, d.lead_status, ");
        sql.append(" r.customer_address as delivery_address, d.prod_id, d.user_defin_04 ");
        sql.append(" from cl_fresh d ");
        sql.append(" left join lc_province l on l.prv_id = cast(d.province as integer) ");
        sql.append(" left join lc_district m on m.dt_id = cast(d.district as integer) ");
        sql.append(" left join lc_subdistrict n on n.sdt_id = cast(d.subdistrict as integer) ");
        sql.append(" left join od_sale_order o on o.lead_id = d.lead_id ");
        sql.append(" left join (select * from cf_synonym where type = 'payment mothod') q on o.payment_method = q.value ");
        sql.append(" left join od_do_new r on r.so_id = o.so_id ");
        sql.append(" left join (select * from cf_synonym where type = 'delivery order status') e on e.value = r.status ");
        sql.append(" left join pd_product a on d.prod_id = a.prod_id ");
        sql.append(" where d.modifydate >=  to_timestamp(:startTime,'yyyymmddhh24miss') ");
        sql.append(" and d.modifydate  < to_timestamp(:endTime,'yyyymmddhh24miss') ");
        sql.append(" and r.status = 59 ");
        sql.append(" and d.lead_status in :status ");
        sql.append(" and d.cp_id in :cpId ");
        sql.append(" order by d.createdate desc ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("startTime", startTime);
        query.setParameter("endTime", endTime);
        query.setParameter("status", status);
        query.setParameter("cpId", cpId);

        List<LeadInfoDto> result = new ArrayList<>();
        List<Object[]> rows = query.getResultList();
        for(Object[] row : rows){
            LeadInfoDto leadInfoDto = new LeadInfoDto();
            if (row[0] != null){
                leadInfoDto.setLeadId((Integer) row[0]);
            }
            if (row[1] != null){
                leadInfoDto.setName((String) row[1]);
            }
            if (row[2] != null){
                leadInfoDto.setPhone((String) row[2]);
            }
            if (row[3] != null){
                leadInfoDto.setAddress((String) row[3]);
            }
            if (row[4] != null){
                leadInfoDto.setDoStatus((String) row[4]);
            }
            if (row[5] != null){
                leadInfoDto.setPackageListItem((String) row[5]);
            }
            if (row[6] != null){
                BigDecimal amount =  (BigDecimal) row[6];
                leadInfoDto.setAmount(amount.doubleValue());
            }
            if (row[7] != null){
                leadInfoDto.setPaymentMethod((String) row[7]);
            }
            if (row[8] != null){
                leadInfoDto.setProductName((String) row[8]);
            }
            if (row[9] != null){
                leadInfoDto.setLeadStatus((Integer) row[9]);
            }
            if (row[10] != null){
                leadInfoDto.setDoAddress((String) row[10]);
            }
            if (row[11] != null){
                leadInfoDto.setProdId((Integer) row[11]);
            }
            if (row[12] != null){
                leadInfoDto.setUserDefin04((String) row[12]);
            }

            result.add(leadInfoDto);
        }

        return result;
    }

    @Override
    public List<LeadInfoDto> getLeadInfoV3(List<Integer> status, List<Integer> cpId, String startDateOfCreate, String endDateOfModify, String endDateModify2) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.lead_id, d.name, d.phone, coalesce(d.address||'###'||n.name||'-'||m.name||'-'||l.name ,d.address) as Address, ");
        sql.append(" e.name as delivery_status, package_name, r.amountcod, q.name as payment_method, a.name as product_name, d.lead_status, ");
        sql.append(" r.customer_address as delivery_address, d.prod_id, d.user_defin_04 ");
        sql.append(" from cl_fresh d ");
        sql.append(" left join lc_province l on l.prv_id = cast(d.province as integer) ");
        sql.append(" left join lc_district m on m.dt_id = cast(d.district as integer) ");
        sql.append(" left join lc_subdistrict n on n.sdt_id = cast(d.subdistrict as integer) ");
        sql.append(" left join od_sale_order o on o.lead_id = d.lead_id ");
        sql.append(" left join (select * from cf_synonym where type = 'payment mothod') q on o.payment_method = q.value ");
        sql.append(" left join od_do_new r on r.so_id = o.so_id ");
        sql.append(" left join (select * from cf_synonym where type = 'delivery order status') e on e.value = r.status ");
        sql.append(" left join pd_product a on d.prod_id = a.prod_id ");
        sql.append(" where d.createdate >=  to_timestamp(:startDateOfCreate,'yyyymmddhh24miss') ");
        sql.append(" and d.modifydate  < to_timestamp(:endDateOfModify,'yyyymmddhh24miss') ");
        sql.append(" and d.modifydate  > to_timestamp(:endDateOfModify2,'yyyymmddhh24miss') ");
        if (status.get(0).compareTo(EnumType.LEAD_STATUS.APPROVED.getValue()) == 0){
            sql.append(" and r.status in (61, 59) ");
        }
        sql.append(" and d.lead_status in :status ");
        sql.append(" and d.cp_id in :cpId ");
        sql.append(" order by d.createdate desc ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("startDateOfCreate", startDateOfCreate);
        query.setParameter("endDateOfModify", endDateOfModify);
        query.setParameter("endDateOfModify2", endDateModify2);
        query.setParameter("status", status);
        query.setParameter("cpId", cpId);

        return getLeadResellOtherFromQuery(query);
    }

    @Override
    public List<LeadInfoDto> getLeadDuplicateInfoV3(Integer cpId, String checkDuplicateStartDateStr, String checkDuplicateEndDateStr) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select d.lead_id, d.name, d.phone, coalesce(d.address||'###'||n.name||'-'||m.name||'-'||l.name ,d.address) as Address, ");
        sql.append(" e.name as delivery_status, package_name, r.amountcod, q.name as payment_method, a.name as product_name, d.lead_status, ");
        sql.append(" r.customer_address as delivery_address, d.prod_id, d.user_defin_04 ");
        sql.append(" from cl_fresh d ");
        sql.append(" left join lc_province l on l.prv_id = cast(d.province as integer) ");
        sql.append(" left join lc_district m on m.dt_id = cast(d.district as integer) ");
        sql.append(" left join lc_subdistrict n on n.sdt_id = cast(d.subdistrict as integer) ");
        sql.append(" left join od_sale_order o on o.lead_id = d.lead_id ");
        sql.append(" left join (select * from cf_synonym where type = 'payment mothod') q on o.payment_method = q.value ");
        sql.append(" left join od_do_new r on r.so_id = o.so_id ");
        sql.append(" left join (select * from cf_synonym where type = 'delivery order status') e on e.value = r.status ");
        sql.append(" left join pd_product a on d.prod_id = a.prod_id ");
        sql.append(" where d.createdate >=  to_timestamp(:startDateOfCreate,'yyyymmddhh24miss') ");
        sql.append(" and d.createdate  < to_timestamp(:endDateOfCreate,'yyyymmddhh24miss') ");
        sql.append(" and d.cp_id = :cpId ");
        sql.append(" order by d.createdate desc ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("startDateOfCreate", checkDuplicateStartDateStr);
        query.setParameter("endDateOfCreate", checkDuplicateEndDateStr);
        query.setParameter("cpId", cpId);

        return getLeadResellOtherFromQuery(query);
    }

    @Override
    public HashMap<String, String> getLeadCheckDuplicateV3(Integer cpId, String checkDuplicateStartDateStr, String checkDuplicateEndDateStr) {
        List<LeadInfoDto> listFresh = getLeadDuplicateInfoV3(cpId, checkDuplicateStartDateStr, checkDuplicateEndDateStr);
        HashMap<String, String> result = new HashMap<>();
        if (listFresh != null && !listFresh.isEmpty()) {
            for (LeadInfoDto dto : listFresh) {
                result.put(dto.getPhone(), String.format("%s-%s", dto.getPhone(), dto.getName()));
            }
        }
        return result;
    }

    @Override
    public HashMap<String, String> getLeadCheckDuplicate(List<Integer> status, List<Integer> cpId, String startTime, String endTime) {

        List<LeadInfoDto> listFresh = getLeadInfo(status,cpId,startTime,endTime);
        HashMap<String, String> result = new HashMap<>();
        if (null != listFresh && listFresh.size() > 0) {
            for (LeadInfoDto dto : listFresh) {
                result.put(dto.getPhone(), String.format("%s-%s", dto.getPhone(), dto.getName()));
            }
        }
        return result;
    }

    @Override
    public ClFresh getById(Integer leadId) {
        Optional<ClFresh> result = leadRepository.findById(leadId);

        if (result != null){
            return result.get();
        }
        return null;
    }

    @Override
    public ClFresh saveOrUpdate(ClFresh clFresh) {
        return leadRepository.save(clFresh);
    }

    @Override
    public BigDecimal findMaxActualCall(Integer leadId) {
        return leadRepository.findMaxActualCall(leadId);
    }

    @Override
    public <T extends CLFresh> T setCustomerData(String ssId, Integer leadId, T clFresh) {
        GetMktDataParams getMktDataParams = new GetMktDataParams();
        getMktDataParams.setLeadId(leadId.toString());
        DBResponse<List<GetMktDataResp>> responseMktData = clFreshService.getMktData(ssId, getMktDataParams);
        if (responseMktData != null && !responseMktData.getResult().isEmpty()) {
            GetMktDataResp mktData = responseMktData.getResult().get(0);
            clFresh.setCustAge(mktData.getCustAge());
            clFresh.setCustGender(mktData.getCustGender());
            if (mktData.getCustDob() != null)
                clFresh.setCustDob(DateHelper.toTMSDateFormat(mktData.getCustDob()));
            clFresh.setCustJob(mktData.getCustJob());
            clFresh.setCustOtherSymptom(mktData.getCustOtherSymptom());
        }

        return  clFresh;
    }

    @Override
    public void updateReminderCalls(Integer leadId, LeadsRequest leadsRequest) {
        if (leadId != null) {
            ClFresh leadEntity = leadRepository.getOne(leadId);
            if (ObjectUtils.allNotNull(leadEntity)) {
               if (leadsRequest.isOverrideOnSaleOrder()) {
                   OdSaleOrder saleOrderEntity = saleOrderRepository.findByLeadId(leadEntity.getLeadId());
                   if (saleOrderEntity != null) {
                       saleOrderEntity.setAppointmentDate(leadsRequest.getAppointmentDate());

                       if (leadsRequest.isOverrideOnDeliveryOrder()) {
                           OdDONew deliveryOrderEntity = deliveryOrderRepository.findBySaleOrderId(saleOrderEntity.getSoId());
                           if (ObjectUtils.allNotNull(deliveryOrderEntity)) {
                               deliveryOrderEntity.setAppointmentDate(leadsRequest.getAppointmentDate());
                               deliveryOrderRepository.save(deliveryOrderEntity);
                           }
                       }

                       saleOrderRepository.save(saleOrderEntity);
                   }
               }
                leadEntity.setAppointmentDate(leadsRequest.getAppointmentDate());
                leadRepository.save(leadEntity);
            }
        }

    }

    @Override
    public ClFresh findOneByLeadId(Integer leadId) {
        if (!ObjectUtils.allNotNull(leadId)) {
            return null;
        }
        return leadRepository.findByLeadId(leadId);
    }

    private List<LeadInfoDto> getLeadResellOtherFromQuery(Query query) {
        List<Object[]> rows = query.getResultList();
        List<LeadInfoDto> result = new ArrayList<>();
        for(Object[] row : rows){
            LeadInfoDto leadInfoDto = new LeadInfoDto();
            if (row[0] != null)
                leadInfoDto.setLeadId((Integer) row[0]);
            if (row[1] != null)
                leadInfoDto.setName((String) row[1]);
            if (row[2] != null)
                leadInfoDto.setPhone((String) row[2]);
            if (row[3] != null)
                leadInfoDto.setAddress((String) row[3]);
            if (row[4] != null)
                leadInfoDto.setDoStatus((String) row[4]);
            if (row[5] != null)
                leadInfoDto.setPackageListItem((String) row[5]);
            if (row[6] != null){
                BigDecimal amount =  (BigDecimal) row[6];
                leadInfoDto.setAmount(amount.doubleValue());
            }
            if (row[7] != null)
                leadInfoDto.setPaymentMethod((String) row[7]);
            if (row[8] != null)
                leadInfoDto.setProductName((String) row[8]);
            if (row[9] != null)
                leadInfoDto.setLeadStatus((Integer) row[9]);
            if (row[10] != null)
                leadInfoDto.setDoAddress((String) row[10]);
            if (row[11] != null)
                leadInfoDto.setProdId((Integer) row[11]);
            if (row[12] != null)
                leadInfoDto.setUserDefin04((String) row[12]);

            result.add(leadInfoDto);
        }

        return result;
    }

    @Override
    public void updLeadsCrmActionType(String ssid, List<CLFresh> callingList, Integer crmActionType, Integer userId) {
        try {
            String leadIdsStr = callingList.stream()
                    .map(CLFresh::getLeadId)
                    .map(Object::toString)
                    .collect(Collectors.joining(","));

            UpdLeadCrmActionType params = new UpdLeadCrmActionType();
            params.setLeadId(leadIdsStr);
            params.setCrmActionTYpe(crmActionType);
            params.setModifiedBy(userId);
            logService.updLeadCrmActionType(ssid, params);
        } catch (Exception e) {
            logger.error("---------- CAN'T SAVE CRM ACTION TYPE ----------", e);
        }
    }

    @Override
    public void fixLocation(InsCLFreshV11 clFresh) {
        if (!StringUtils.hasText(clFresh.getProvince())) {
            clFresh.setProvince(null);
            clFresh.setDistrict(null);
            clFresh.setSubdistrict(null);
            clFresh.setNeighborhood(null);

            return;
        }

        if (!StringUtils.hasText(clFresh.getDistrict())) {
            clFresh.setDistrict(null);
            clFresh.setSubdistrict(null);
            clFresh.setNeighborhood(null);

            return;
        }

        if (!StringUtils.hasText(clFresh.getSubdistrict())) {
            clFresh.setSubdistrict(null);
            clFresh.setNeighborhood(null);

            return;
        }

        if (!StringUtils.hasText(clFresh.getNeighborhood())) {
            clFresh.setNeighborhood(null);
        }
    }

    @Override
    public boolean haveCallback(int leadId) {
        String sqlCheckHaveCallback = "SELECT count(lead_id) FROM cl_callback WHERE lead_id = :leadId AND is_deleted = 0";
        Query query = entityManager.createNativeQuery(sqlCheckHaveCallback);
        query.setParameter("leadId", leadId);
        int count = ((BigInteger) query.getSingleResult()).intValue();
        return count > 0;
    }
}
