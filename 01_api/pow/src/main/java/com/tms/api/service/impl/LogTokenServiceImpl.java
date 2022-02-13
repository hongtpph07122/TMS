package com.tms.api.service.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tms.api.entity.LogToken;
import com.tms.api.repository.LogTokenRepository;
import com.tms.api.service.LogTokenService;
import com.tms.api.utils.ObjectUtils;

@Service
public class LogTokenServiceImpl implements LogTokenService {
	
	@Autowired
	LogTokenRepository logTokenRepos;

	@Override
	public LogToken createLogToken(LogToken data) {
		// TODO Auto-generated method stub
		Calendar now = Calendar.getInstance();
		if (ObjectUtils.isNull(data.getCreateDate())) {
			data.setCreateDate(now.getTime());
		}
		data = logTokenRepos.save(data);
		return data;
	}

}
