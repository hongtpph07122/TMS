package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import com.tms.api.entity.CfCpCallStrategyDefault;

import java.util.List;

public interface CfCpCallStrategyDefaultRepository extends JpaRepository<CfCpCallStrategyDefault, Integer> {
    @Query(value = "from com.tms.api.entity.CfCpCallStrategyDefault a where a.cpId = ?1 and a.orgId = ?2 and a.callStatus = ?3 and a.orderPhoneNumber = ?4")
    List<CfCpCallStrategyDefault> getCallStrategy(Integer cpId, Integer orgId, Integer callStatus, Integer orderPhoneNumber);
}