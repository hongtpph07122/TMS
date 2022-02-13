package com.tms.api.rest;

import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.response.TMSResponse;
import com.tms.dto.DBResponse;
import com.tms.dto.GetBlacklist;
import com.tms.entity.CFBlacklist;
import com.tms.service.impl.LCProvinceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/blacklist")
public class BlackListController extends BaseController {
	Logger logger = LoggerFactory.getLogger(DNCController.class);

	@Autowired
	LCProvinceService lcProvinceService;

	@GetMapping()
	public TMSResponse<List<CFBlacklist>> getList(GetBlacklist params) throws TMSException {
		params.setOrgId(getCurrentOriganationId());
		if (params.getLimit() == null) {
			params.setLimit(Const.DEFAULT_PAGE_SIZE);
		}
		DBResponse<List<CFBlacklist>> dbResponse = lcProvinceService.getBlacklist(SESSION_ID, params);
		return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
	}
	
	@DeleteMapping("/deleteMultiple")
	public TMSResponse<Boolean> deleteMul(@RequestBody String blacklistIds) throws TMSException {
		// TODO delete nhieu blacklist item khi submit
		return TMSResponse.buildResponse(true);
	}
}
