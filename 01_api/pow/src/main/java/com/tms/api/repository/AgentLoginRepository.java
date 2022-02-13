package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tms.api.entity.AgentLogin;

public interface AgentLoginRepository extends JpaRepository<AgentLogin, Long>, JpaSpecificationExecutor<AgentLogin> {

}
