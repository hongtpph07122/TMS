package com.tms.api.repository;

import com.tms.api.entity.TrkAffiliateMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrkAffilateMappingRepository extends JpaRepository<TrkAffiliateMapping,Integer> {

    @Query(value = "from TrkAffiliateMapping a where a.agcId = ?1 and a.isActive = 1 ")
    TrkAffiliateMapping getByAgcId(Integer agcId);

    @Query(value = "from TrkAffiliateMapping a where a.trackerAffiliateId = ?1 and a.isActive = 1 ")
    TrkAffiliateMapping getByTrackerAffiliateId(String agcId);

    @Query(value = "from TrkAffiliateMapping a where a.agcCode = ?1 and a.isActive = 1 ")
    TrkAffiliateMapping getByAgcCode(String agcCode);
}
