package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.DialledListSumary;

public interface DialledListSumaryRepository extends JpaRepository<DialledListSumary, Long>, JpaSpecificationExecutor<DialledListSumary> {

}
