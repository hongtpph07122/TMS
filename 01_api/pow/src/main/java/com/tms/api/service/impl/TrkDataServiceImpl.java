package com.tms.api.service.impl;

import com.tms.api.entity.TrkData;
import com.tms.api.repository.TrkDataRepository;
import com.tms.api.service.TrkDataService;
import com.tms.api.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Service
public class TrkDataServiceImpl implements TrkDataService {
	private final Logger logger = LoggerFactory.getLogger(TrkDataServiceImpl.class);

	@Autowired
	TrkDataRepository trkDataRepository;

	@Autowired
	EntityManager entityManager;

	@Override
	public TrkData createTrackData(TrkData data) {
		Calendar now = Calendar.getInstance();
		if (ObjectUtils.isNull(data.getCreateDate())) {
			data.setCreateDate(now.getTime());
		}
		data.setUpdateDate(now.getTime());
		data = trkDataRepository.save(data);

		return data;
	}

	@Override
	public Boolean checkLeadIfNotExitsInTrkData(Integer leadId) {
		String sql = "select exists(select 1 from trk_data where lead_id=:leadId) AS exists";
		try {
			Query query = entityManager.createNativeQuery(sql);
			query.setParameter("leadId", leadId);
			return (Boolean) query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
