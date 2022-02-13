package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tms.api.entity.RcActivity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RcActivityRepository extends JpaRepository<RcActivity, Integer>{
//    @Query(value = "from com.tms.api.entity.RcActionMapping rc where rc.status_name = ?1 and rc.sub_status_name = ?2")
//    List<RcActionMapping> getRcActionMappingStatus(String status_name, String sub_status_name);
}