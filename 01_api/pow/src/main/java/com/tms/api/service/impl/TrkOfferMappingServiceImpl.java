package com.tms.api.service.impl;

import com.tms.api.entity.TrkOfferMapping;
import com.tms.api.repository.TrkOfferMappingRepository;
import com.tms.api.service.TrkOfferMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrkOfferMappingServiceImpl implements TrkOfferMappingService {

    @Autowired
    TrkOfferMappingRepository repository;

    @Override
    public TrkOfferMapping getByOfferId(Integer offerId) {
        List<TrkOfferMapping> offerMappings = repository.getByOfferId(offerId);
        if (offerMappings != null && offerMappings.size() > 0) {
            return offerMappings.get(0);
        }
        return null;
    }
}
