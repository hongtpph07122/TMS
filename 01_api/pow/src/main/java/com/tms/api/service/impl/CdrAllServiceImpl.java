package com.tms.api.service.impl;

import com.tms.api.entity.CdrAllEntity;
import com.tms.api.repository.CdrAllRepository;
import com.tms.api.service.CdrAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CdrAllServiceImpl implements CdrAllService {

    @Autowired
    CdrAllRepository cdrAllRepository;

    @Override
    public List<CdrAllEntity> findByStatus(Integer status) {
        return cdrAllRepository.findByStatus(status);
    }

    @Override
    public void saveOrUpdateCdrAlls(List<CdrAllEntity> cdrAlls) {
        cdrAllRepository.saveAll(cdrAlls);
    }
}
