package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.AgentActivity;

public interface AgentActivityRepository extends JpaRepository<AgentActivity, Long>, JpaSpecificationExecutor<AgentActivity> {

}
