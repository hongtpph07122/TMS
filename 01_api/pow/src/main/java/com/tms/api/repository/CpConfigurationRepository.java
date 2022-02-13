package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.tms.api.entity.CpConfiguration;
import com.tms.api.entity.CustomerAddReport;

public interface CpConfigurationRepository extends JpaRepository<CpConfiguration, Integer> {

    long deleteByCpId(Integer cpId);
}
