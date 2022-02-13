package com.tms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.tms.api.entity.OrRole;

import java.util.List;

public interface OrRoleRepository extends JpaRepository<OrRole, Integer>{
//    @Query(value = "from com.tms.api.entity.OrRole o where o.name = ?1")
//    List<OrUser> getRoleByName(String name);
    List<OrRole> getByName(String name);

    @Query(value = "from com.tms.api.entity.OrRole")
    List<OrRole> getUserType();
}