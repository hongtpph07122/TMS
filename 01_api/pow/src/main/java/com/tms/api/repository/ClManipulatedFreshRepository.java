package com.tms.api.repository;

import com.tms.api.entity.ClManipulatedFresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClManipulatedFreshRepository extends JpaRepository<ClManipulatedFresh, Integer> {

    @Query(value = " from ClManipulatedFresh a where a.leadId = ?1 ")
    List<ClManipulatedFresh> getByLeadId(Integer leadId);
}
