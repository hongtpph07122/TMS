package com.tms.api.service;

import com.tms.api.entity.ClManipulatedFresh;

public interface ClManipulatedFreshService {

    ClManipulatedFresh saveOrUpdate(ClManipulatedFresh logManipulate);

    ClManipulatedFresh getByLeadId(Integer leadId);
}
