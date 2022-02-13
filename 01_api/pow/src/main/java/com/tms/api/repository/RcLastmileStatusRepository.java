package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;


import com.tms.api.entity.RcLastmileStatus;

import java.util.List;

public interface RcLastmileStatusRepository extends JpaRepository<RcLastmileStatus, Integer>{

    @Query(value = "from com.tms.api.entity.RcLastmileStatus rc where rc.is_updated = 1")
    List<RcLastmileStatus> getLastmileStatus();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE com.tms.api.entity.RcLastmileStatus rc set rc.is_updated = 0 where rc.tracking_code in ?1")
    void updateIsUpdated(List<String> trackingCodes);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE com.tms.api.entity.RcLastmileStatus rc set rc.update_status = 1 where rc.tracking_code in ?1")
    void updateUpdatedStatus(List<String> trackingCodes);
}