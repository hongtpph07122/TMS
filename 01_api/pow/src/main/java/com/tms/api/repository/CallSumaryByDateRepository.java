package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.CallSumaryByDate;

public interface CallSumaryByDateRepository extends JpaRepository<CallSumaryByDate, Long>, JpaSpecificationExecutor<CallSumaryByDate> {

}
