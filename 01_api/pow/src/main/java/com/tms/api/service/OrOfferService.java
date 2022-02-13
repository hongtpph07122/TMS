package com.tms.api.service;

import com.tms.api.dto.CreateUpdateOfferResponse;
import com.tms.dto.DBResponse;
import com.tms.entity.log.InsOffer;
import com.tms.entity.log.UpdOrOffer;

public interface OrOfferService {

    public CreateUpdateOfferResponse insOrOffer(String sessionId, InsOffer offer);

    public DBResponse updOrOffer(String sessionId, UpdOrOffer offer);
}
