package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.ActivitySumary;

public interface ActivitySumaryRepository extends JpaRepository<ActivitySumary, Long>, JpaSpecificationExecutor<ActivitySumary> {

}
