package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.DialledListDetail;

public interface DialledListDetailRepository extends JpaRepository<DialledListDetail, Long>, JpaSpecificationExecutor<DialledListDetail> {

}
