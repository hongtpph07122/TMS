package com.tms.api.repository;

import com.tms.api.entity.TrkOfferMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrkOfferMappingRepository extends JpaRepository<TrkOfferMapping, Integer> {
    @Query(value = "from TrkOfferMapping a where a.offerId = ?1 ")
    List<TrkOfferMapping> getByOfferId(Integer offerId);
}
