package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.CustomerAddReport;

public interface CustomerAddReportRepository extends JpaRepository<CustomerAddReport, Long>, JpaSpecificationExecutor<CustomerAddReport> {

}
