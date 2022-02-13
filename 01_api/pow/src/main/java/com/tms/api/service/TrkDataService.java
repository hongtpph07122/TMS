package com.tms.api.service;

import com.tms.api.entity.TrkData;

public interface TrkDataService {
    TrkData createTrackData(TrkData data);

	
    public Boolean checkLeadIfNotExitsInTrkData(Integer leadId);
}
