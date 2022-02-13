package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.CallDetail;

public interface CallDetailRepository extends JpaRepository<CallDetail, Long>, JpaSpecificationExecutor<CallDetail> {

}
