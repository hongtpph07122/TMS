package com.tms.api.service;

import com.tms.api.dto.MktDataDto;
import com.tms.dto.GetMktData;
import com.tms.entity.CLFresh;

import java.util.List;

public interface MktDataService {
    List<MktDataDto> get(GetMktData params);

    void save(String ssid, CLFresh clFresh, Integer userId);

    <T> void save(String ssid, T clFresh, Integer leadId, Integer userId);

}
