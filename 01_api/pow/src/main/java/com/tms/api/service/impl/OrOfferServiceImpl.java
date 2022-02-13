package com.tms.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tms.api.dto.CreateUpdateOfferResponse;
import com.tms.api.service.OrOfferService;
import com.tms.dto.DBResponse;
import com.tms.entity.log.InsOffer;
import com.tms.entity.log.UpdOrOffer;
import com.tms.service.impl.LogService;

@Service
public class OrOfferServiceImpl implements OrOfferService {
    @Autowired
    LogService logService; 
    
    private String LEAD_DESTINATION = com.tms.ff.utils.AppProperties.getPropertyValue("trk.api.LEAD_DESTINATION"); 

    @Override
    public CreateUpdateOfferResponse insOrOffer(String sessionId, InsOffer offer) {
        DBResponse dbResponse = new DBResponse();
        CreateUpdateOfferResponse response = null;
        try {
            dbResponse = logService.insOrOffer(sessionId, offer);
            if (0 == dbResponse.getErrorCode()) {
                response = new CreateUpdateOfferResponse();
                response.setLeadDestinationUrl(LEAD_DESTINATION);
                response.setOfferId(dbResponse.getErrorMsg().trim());
            }
        } catch (Exception ex) {

        }
        return response;
    }

    @Override
    public DBResponse updOrOffer(String sessionId, UpdOrOffer offer) {
        DBResponse dbResponse = null;
        try {
            dbResponse = logService.updOrOffer(sessionId, offer);
        } catch (Exception ex) {

        }
        return dbResponse;
    }
 
}
