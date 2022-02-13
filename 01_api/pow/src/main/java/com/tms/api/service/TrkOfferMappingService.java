package com.tms.api.service;

import com.tms.api.entity.TrkOfferMapping;

public interface TrkOfferMappingService {

    TrkOfferMapping getByOfferId(Integer offerId);
}
