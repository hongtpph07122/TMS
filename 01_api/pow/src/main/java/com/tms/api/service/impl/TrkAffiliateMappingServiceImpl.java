package com.tms.api.service.impl;

import com.tms.api.entity.TrkAffiliateMapping;
import com.tms.api.repository.TrkAffilateMappingRepository;
import com.tms.api.service.TrkAffilateMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrkAffiliateMappingServiceImpl implements TrkAffilateMappingService {

    @Autowired
    TrkAffilateMappingRepository repository;

    @Override
    public TrkAffiliateMapping getByAgcId(Integer agcId) {

        TrkAffiliateMapping result = new TrkAffiliateMapping();
        result = repository.getByAgcId(agcId);
        return result;
    }

    @Override
    public TrkAffiliateMapping getBygetByAgcCode(String agcCode) {
        TrkAffiliateMapping result = new TrkAffiliateMapping();
        result = repository.getByAgcCode(agcCode);
        return result;
    }


}
