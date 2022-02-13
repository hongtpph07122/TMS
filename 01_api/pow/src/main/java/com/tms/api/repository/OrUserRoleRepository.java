package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.tms.api.entity.OrUserRole;

public interface OrUserRoleRepository extends JpaRepository<OrUserRole, Integer>{
    OrUserRole getByUserId(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "delete from com.tms.api.entity.OrUserRole where userId = ?1")
    void deleteByUserId(Integer userId);
}