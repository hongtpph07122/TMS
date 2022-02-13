package com.tms.api.repository;

import com.tms.api.entity.CdrAllEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CdrAllRepository extends JpaRepository<CdrAllEntity, Integer> {

    @Query(value = " from CdrAllEntity a where a.updateStatus =?1 ")
    List<CdrAllEntity> findByStatus(Integer status);

    @Query(value = " from CdrAllEntity a where a.uniqueId in ?1")
    List<CdrAllEntity> findByUniqueId(List<String> uniqueIds);
}
