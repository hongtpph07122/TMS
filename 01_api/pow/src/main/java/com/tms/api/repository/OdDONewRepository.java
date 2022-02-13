package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

import com.tms.api.entity.OdDONew;
import com.tms.api.entity.RcActionMapping;

import java.sql.Timestamp;
import java.util.List;

public interface OdDONewRepository extends JpaRepository<OdDONew, Integer>{
    @Query(value = "from com.tms.api.entity.OdDONew d where d.tracking_code in ?1 ")
    List<OdDONew> getDOByTrackingCode(List<String> trackingCode);

    @Query(value = "from com.tms.api.entity.OdDONew d inner join com.tms.api.entity.RcActionMapping rc on d.lastmile_reason = rc.status_name " +
            "and d.lastmile_reason_detail = rc.sub_status_name and d.org_id = rc.org_id where d.tracking_code in ?1 and rc.action = ?2 and d.rescue_id is not null")
    List<OdDONew> getDOByAction(List<String> tracking_code, Integer action);

    @Query(value = "from com.tms.api.entity.OdDONew d inner join com.tms.api.entity.RcActionMapping rc on d.lastmile_reason = rc.status_name " +
            "and d.lastmile_reason_detail = rc.sub_status_name and d.org_id = rc.org_id where d.tracking_code in ?1 and rc.action = ?2 and d.rescue_id is null")
    List<OdDONew> getDOByRescueNull(List<String> tracking_code, Integer action);

    @Query(value = "from com.tms.api.entity.OdDONew d where d.carrier_id = 6 and d.is_active = 1 and d.createdate >= ?1")
    List<OdDONew> getDOByTime(Timestamp createDate);

    @Query(value = "from com.tms.api.entity.OdDONew d inner join com.tms.api.entity.RcActionMapping rc on d.lastmile_reason = rc.status_name and d.org_id = rc.org_id")
    List<OdDONew> getAllDOByAction();

    @Query(value = "from com.tms.api.entity.OdDONew d inner join com.tms.api.entity.ClFreshEntity cf on d.customer_id = cf.leadId where cf.agcId in ?1 and (d.is_postback != 1 or d.is_postback is null) and d.status = 59")
    List<OdDONew> getAllUnpostbackDeliveriedWithLeadByAgcId(List<Integer> agcId);

    @Query(value = "from com.tms.api.entity.OdDONew d inner join com.tms.api.entity.RcActionMapping rc on d.lastmile_reason = rc.status_name and d.org_id = rc.org_id AND d.createdate >= ?1 ")
	List<OdDONew> getAllDOByActionBackTo2Month(Timestamp ts);

    @Query(value = "from com.tms.api.entity.OdDONew d where d.so_id = ?1 ")
    OdDONew findBySaleOrderId(Integer salesOrderId);
}
