package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tms.api.entity.RcActionMapping;
import org.springframework.data.jpa.repository.Query;
import com.tms.api.entity.OdDONew;

import java.util.List;

public interface RcActionMappingRepository extends JpaRepository<RcActionMapping, Integer>{
    @Query(value = "from com.tms.api.entity.RcActionMapping rc where rc.status_name = ?1 and rc.sub_status_name = ?2")
    List<RcActionMapping> getRcActionMappingStatus(String status_name, String sub_status_name);

    @Query(value = "from com.tms.api.entity.RcActionMapping rc where rc.status_name = ?1 and rc.sub_status_name = ?2 and rc.action = ?3")
    List<RcActionMapping> getRcAction(String status_name, String sub_status_name, Integer action);

    @Query(value = "from com.tms.api.entity.RcActionMapping rc where rc.action = ?1")
    List<RcActionMapping> getRcActionByAction(Integer action);

//    @Query(value = "from com.tms.api.entity.RcActionMapping rc inner join com.tms.api.entity.OdDONew d on d.lastmile_reason = rc.status_name " +
//            "and d.lastmile_reason_detail = rc.sub_status_name where d.tracking_code in ?1 and rc.action = ?2 and d.rescue_id is null")
//    List<RcActionMapping> getRcActionByRescueNull(List<String> tracking_code, Integer action);
//
//    @Query(value = "from com.tms.api.entity.RcActionMapping rc inner join com.tms.api.entity.OdDONew d on d.lastmile_reason = rc.status_name " +
//            "and d.lastmile_reason_detail = rc.sub_status_name where d.tracking_code in ?1 and rc.action = ?2 and d.rescue_id is not null")
//    List<RcActionMapping> getRcActionByRescueNotNull(List<String> tracking_code, Integer action);

//    @Query(value = "from com.tms.api.entity.RcActionMapping rc where (rc.status_name, rc.sub_status_name) in ?1")
//    List<RcActionMapping> getRcActionByListStatus(List<List<String>> status);

//    @Query(value = "from com.tms.api.entity.RcActionMapping rc where (rc.status_name, rc.sub_status_name) in ?1")
//    List<RcActionMapping> findByStatusnameAndSubstatusname(String status_name, String sub_status_name);
}