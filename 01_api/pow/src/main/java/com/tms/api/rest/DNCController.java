package com.tms.api.rest;

import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.response.TMSResponse;
import com.tms.dto.DBResponse;
import com.tms.dto.GetDnc;
import com.tms.entity.CFDnc;
import com.tms.service.impl.LCProvinceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/DNC")
public class DNCController extends BaseController {

	Logger logger = LoggerFactory.getLogger(DNCController.class);

	@Autowired
	LCProvinceService lcProvinceService;

	@GetMapping()
	public TMSResponse<List<CFDnc>> getList(GetDnc params) throws TMSException {
		params.setOrgId(getCurrentOriganationId());
		if (params.getLimit() == null) {
			params.setLimit(Const.DEFAULT_PAGE_SIZE);
		}
		DBResponse<List<CFDnc>> dbResponse = lcProvinceService.getDnc(SESSION_ID, params);
		return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
	}
	
	@DeleteMapping("/deleteMultiple")
	public TMSResponse<Boolean> deleteMul(@RequestBody String dncIds) throws TMSException {
		// TODO delete nhieu dnc khi submit
		return TMSResponse.buildResponse(true);
	}
	
}
