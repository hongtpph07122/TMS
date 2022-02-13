package com.tms.api.service.impl;

import com.tms.api.entity.ClManipulatedFresh;
import com.tms.api.repository.ClManipulatedFreshRepository;
import com.tms.api.service.ClManipulatedFreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ClManipulatedFreshServiceImpl implements ClManipulatedFreshService {

    @Autowired
    ClManipulatedFreshRepository clManipulatedFreshRepository;

    @Override
    public ClManipulatedFresh saveOrUpdate(ClManipulatedFresh clManipulatedFresh) {
        return clManipulatedFreshRepository.save(clManipulatedFresh);
    }

    @Override
    public ClManipulatedFresh getByLeadId(Integer leadId) {
        List<ClManipulatedFresh> clManipulatedFreshList = clManipulatedFreshRepository.getByLeadId(leadId);
        if(!CollectionUtils.isEmpty(clManipulatedFreshList)){
            return clManipulatedFreshList.get(0);
        }
        return null;
    }
}
