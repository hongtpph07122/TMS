package com.tms.api.service.impl;

import com.tms.api.dto.Request.ManipulateMonitorRequestDTO;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Request.QueryRequestDTO;
import com.tms.api.dto.Response.ManipulateMonitorResponseDTO;
import com.tms.api.dto.Response.QueryResponseDTO;
import com.tms.api.service.ManipulateMonitorService;
import com.tms.api.utils.DateUtils;
import com.tms.api.utils.DocsUtils;
import com.tms.api.utils.LoggerUtils;
import com.tms.api.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service(value = "manipulateMonitorService")
public class ManipulateMonitorServiceImpl implements ManipulateMonitorService {

    private static final Logger logger = LoggerFactory.getLogger(ManipulateMonitorServiceImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public ManipulateMonitorResponseDTO snagAllManipulatesMonitor(ManipulateMonitorRequestDTO manipulateMonitorRequestDTO, ObjectRequestDTO objectRequestDTO) {
        logger.info("Manipulate on monitor request : {}", LoggerUtils.snagAsLogJson(manipulateMonitorRequestDTO));
        logger.info("Current object request : {}", LoggerUtils.snagAsLogJson(objectRequestDTO));
        ManipulateMonitorResponseDTO result = new ManipulateMonitorResponseDTO();
        QueryResponseDTO queryResponseDTO = snagAsResultQueryManipulateMonitorRequest(manipulateMonitorRequestDTO, objectRequestDTO);
        List<ManipulateMonitorRequestDTO> manipulateMonitorRequestDTOList = new ArrayList<>();
        List<Object[]> rowsIterator = queryResponseDTO.getQuery().getResultList();
        for (Object[] row : rowsIterator) {
            ManipulateMonitorRequestDTO manipulateMonitorResponse = new ManipulateMonitorRequestDTO();
            manipulateMonitorResponse.setLeadId((Integer) row[0]);
            manipulateMonitorResponse.setAgencyId((Integer) row[1]);
            manipulateMonitorResponse.setSource((String) row[2]);
            manipulateMonitorResponse.setName((String) row[3]);
            manipulateMonitorResponse.setProductName((String) row[4]);
            manipulateMonitorResponse.setManipulatedDate(DateUtils.feedStageAsString((Date) row[5]));
            manipulateMonitorResponse.setCreatedDate(DateUtils.feedStageAsString((Date) row[6]));
            manipulateMonitorResponse.setModifiedDate(DateUtils.feedStageAsString((Date) row[7]));
            manipulateMonitorResponse.setClickedId((String) row[8]);
            /* add list */
            manipulateMonitorRequestDTOList.add(manipulateMonitorResponse);
        }

        /* body response */
        BigInteger counter = (BigInteger) queryResponseDTO.getQueryAsCounter().getResultList().get(0);
        result.setManipulateMonitorRequestDTOList(manipulateMonitorRequestDTOList);
        result.setCounter(counter.intValue());

        return result;
    }

    @Override
    public byte[] exportExcelManipulateMonitor(String sheetName, ManipulateMonitorRequestDTO manipulateMonitorRequestDTO, ObjectRequestDTO objectRequestDTO) {
        List<ManipulateMonitorRequestDTO> manipulateMonitorRequestDTOList = snagAllManipulatesMonitor(manipulateMonitorRequestDTO, objectRequestDTO).getManipulateMonitorRequestDTOList();
        return DocsUtils.buildDocsDope(manipulateMonitorRequestDTOList, ManipulateMonitorRequestDTO.class, sheetName, 100);
    }


    private QueryResponseDTO snagAsResultQueryManipulateMonitorRequest(ManipulateMonitorRequestDTO manipulateMonitorRequestDTO, ObjectRequestDTO objectRequestDTO) {
        QueryRequestDTO queryRequestDTO = buildQueryManipulateMonitorRequest(manipulateMonitorRequestDTO, objectRequestDTO);
        QueryResponseDTO queryResponseDTO = new QueryResponseDTO();
        Query query = entityManager.createNativeQuery(queryRequestDTO.getQuery());
        Query queryAsCounter = entityManager.createNativeQuery(queryRequestDTO.getQueryAsCounters());

        if (ObjectUtils.allNotNull(objectRequestDTO.getOrganizationId())) {
            query.setParameter("orgId", objectRequestDTO.getOrganizationId());
            queryAsCounter.setParameter("orgId", objectRequestDTO.getOrganizationId());
        }

        if (ObjectUtils.allNotNull(manipulateMonitorRequestDTO.getLeadId())) {
            query.setParameter("leadId", manipulateMonitorRequestDTO.getLeadId());
            queryAsCounter.setParameter("leadId", manipulateMonitorRequestDTO.getLeadId());
        }

        if (ObjectUtils.allNotNull(manipulateMonitorRequestDTO.getAgencyId())) {
            query.setParameter("agencyId", manipulateMonitorRequestDTO.getAgencyId());
            queryAsCounter.setParameter("agencyId", manipulateMonitorRequestDTO.getAgencyId());
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getSource())) {
            query.setParameter("source", "%" + manipulateMonitorRequestDTO.getSource().toLowerCase().trim() + "%");
            queryAsCounter.setParameter("source", "%" + manipulateMonitorRequestDTO.getSource().toLowerCase().trim() + "%");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getName())) {
            query.setParameter("name", "%" + manipulateMonitorRequestDTO.getName().toLowerCase().trim() + "%");
            queryAsCounter.setParameter("name", "%" + manipulateMonitorRequestDTO.getName().toLowerCase().trim() + "%");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getProductName())) {
            query.setParameter("productName", "%" + manipulateMonitorRequestDTO.getProductName().toLowerCase().trim() + "%");
            queryAsCounter.setParameter("productName", "%" + manipulateMonitorRequestDTO.getProductName().toLowerCase().trim() + "%");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getClickedId())) {
            query.setParameter("clickedId", "%" + manipulateMonitorRequestDTO.getClickedId().trim() + "%");
            queryAsCounter.setParameter("clickedId", "%" + manipulateMonitorRequestDTO.getClickedId().trim() + "%");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getCreatedDate())) {
            query.setParameter("createdDate", manipulateMonitorRequestDTO.getCreatedDate());
            queryAsCounter.setParameter("createdDate", manipulateMonitorRequestDTO.getCreatedDate());
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getModifiedDate())) {
            query.setParameter("modifiedDate", manipulateMonitorRequestDTO.getModifiedDate());
            queryAsCounter.setParameter("modifiedDate", manipulateMonitorRequestDTO.getModifiedDate());
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getManipulatedDate())) {
            query.setParameter("manipulatedDate", manipulateMonitorRequestDTO.getManipulatedDate());
            queryAsCounter.setParameter("manipulatedDate", manipulateMonitorRequestDTO.getManipulatedDate());
        }

        if (ObjectUtils.allNotNull(manipulateMonitorRequestDTO.getLimit())) {
            query.setParameter("limit", manipulateMonitorRequestDTO.getLimit());
        }

        if (ObjectUtils.allNotNull(manipulateMonitorRequestDTO.getOffset())) {
            query.setParameter("offset", manipulateMonitorRequestDTO.getOffset());
        }
        queryResponseDTO.setQuery(query);
        queryResponseDTO.setQueryAsCounter(queryAsCounter);
        return queryResponseDTO;

    }

    private QueryRequestDTO buildQueryManipulateMonitorRequest(ManipulateMonitorRequestDTO manipulateMonitorRequestDTO, ObjectRequestDTO objectRequestDTO) {
        QueryRequestDTO queryRequestDTO = new QueryRequestDTO();
        StringBuilder builder = new StringBuilder();

        builder.append(" select a.lead_id, a.agc_id, b.shortname as source, a.\"name\", ");
        builder.append(" a.prod_name, c.manipulate_date, a.createdate, a.modifydate, a.click_id from cl_fresh a ");
        builder.append(" LEFT JOIN bp_partner b on a.agc_id = b.pn_id ");
        builder.append(" RIGHT JOIN cl_manipulated_fresh c ON a.lead_id = c.lead_id ");
        builder.append(" where 1=1 ");
        StringBuilder counter = new StringBuilder("select count(*) from ( ");

        /* add filter */

        if (ObjectUtils.allNotNull(objectRequestDTO.getOrganizationId())) {
            builder.append(" and a.org_id = :orgId ");
        }

        if (ObjectUtils.allNotNull(manipulateMonitorRequestDTO.getLeadId())) {
            builder.append(" and a.lead_id = :leadId ");
        }

        if (ObjectUtils.allNotNull(manipulateMonitorRequestDTO.getAgencyId())) {
            builder.append(" and a.agc_id = :agencyId ");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getSource())) {
            builder.append(" and lower(b.shortname) like :source ");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getName())) {
            builder.append(" and lower(a.name) like :name ");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getProductName())) {
            builder.append(" and lower(a.prod_name) like :productName ");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getClickedId())) {
            builder.append(" and (a.click_id) like :clickedId ");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getCreatedDate())) {
            builder.append(" and ( a.createdate  >= to_timestamp(split_part(:createdDate,'|',1),'yyyymmddhh24miss')) and ( a.createdate   <= to_timestamp(split_part(:createdDate,'|',2),'yyyymmddhh24miss')) ");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getModifiedDate())) {
            builder.append(" and ( a.modifydate  >= to_timestamp(split_part(:modifiedDate,'|',1),'yyyymmddhh24miss')) and ( a.modifydate   <= to_timestamp(split_part(:modifiedDate,'|',2),'yyyymmddhh24miss')) ");
        }

        if (!StringUtils.isEmpty(manipulateMonitorRequestDTO.getManipulatedDate())) {
            builder.append(" and ( c.manipulate_date  >= to_timestamp(split_part(:manipulatedDate,'|',1),'yyyymmddhh24miss')) and ( c.manipulate_date  <= to_timestamp(split_part(:manipulatedDate,'|',2),'yyyymmddhh24miss')) ");
        }
        /* end filters */

        counter.append(builder);
        counter.append(" ) as numberTotal");
        builder.append(" order by a.lead_id  desc ");

        if (ObjectUtils.allNotNull(manipulateMonitorRequestDTO.getLimit())) {
            builder.append(" limit :limit ");
        }

        if (ObjectUtils.allNotNull(manipulateMonitorRequestDTO.getOffset())) {
            builder.append(" offset :offset ");
        }
        queryRequestDTO.setQuery(builder.toString());
        queryRequestDTO.setQueryAsCounters(counter.toString());
        return queryRequestDTO;

    }
}
