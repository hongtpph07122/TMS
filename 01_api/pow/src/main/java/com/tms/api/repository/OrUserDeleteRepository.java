package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tms.api.entity.OrUserDelete;

public interface OrUserDeleteRepository extends JpaRepository<OrUserDelete, Long>{

}