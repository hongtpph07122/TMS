package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.CallDuration;

public interface CallDurationRepository extends JpaRepository<CallDuration, Long>, JpaSpecificationExecutor<CallDuration> {

}
