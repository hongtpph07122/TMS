package com.tms.api.repository;

import com.tms.api.entity.TrkAffSubnameMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrkAffSubnameMappingRepository extends JpaRepository<TrkAffSubnameMapping, Integer> {

    @Query(value = "from TrkAffSubnameMapping a where a.affName = ?1 ")
    List<TrkAffSubnameMapping> getByAffName(String affName);
}
