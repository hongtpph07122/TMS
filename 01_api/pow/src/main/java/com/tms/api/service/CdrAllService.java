package com.tms.api.service;

import com.tms.api.entity.CdrAllEntity;

import java.util.List;

public interface CdrAllService {

    List<CdrAllEntity> findByStatus(Integer status);

    void saveOrUpdateCdrAlls(List<CdrAllEntity> cdrAlls);
}
