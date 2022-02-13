package com.tms.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tms.api.entity.CLInactive;

@Repository
public interface ClInactiveRepository extends JpaRepository<CLInactive, Integer> {

	@Query(value = " from CLInactive a where a.leadId IN (?1) AND a.leadStatus = ?2 ")
	List<CLInactive> getByLeadStatusAndListLeadId(List<Integer> leadIds, int value);

	@Query(value = " from CLInactive a where a.leadId = ?1 ")
	List<CLInactive> getAllByLeadId(Integer leadId);

}
