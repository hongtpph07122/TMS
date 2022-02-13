package com.tms.api.service;

import com.tms.api.dto.GetCdrResponse;
import com.tms.api.entity.CdrEntity;
import com.tms.dto.GetCdrResp;

import java.util.List;

public interface CDRsService {

    List<GetCdrResponse> findAllCdr(List<GetCdrResp> cdrRequestList);

    CdrEntity findOneByChannel(String channel);

    void updateOne(CdrEntity cdrEntity);

    void createOne(String channel);

    void saveOrUpdate(List<CdrEntity> entities);

    List<CdrEntity> getByChanel(List<String> channels);

    byte[] exportCDRsExcel(String sheetName, List<GetCdrResponse> cdrResponses);
}
