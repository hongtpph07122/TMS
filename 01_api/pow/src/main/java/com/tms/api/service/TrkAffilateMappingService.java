package com.tms.api.service;

import com.tms.api.entity.TrkAffiliateMapping;

public interface TrkAffilateMappingService {

    TrkAffiliateMapping getByAgcId(Integer agcId);

    TrkAffiliateMapping getBygetByAgcCode(String agcCode);
}
