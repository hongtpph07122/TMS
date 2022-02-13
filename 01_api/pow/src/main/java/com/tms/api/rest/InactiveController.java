package com.tms.api.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tms.api.dto.OrderManagermentRespDto;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.EnumType;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.InactiveService;
import com.tms.dto.CLInactiveMoveTrash;
import com.tms.dto.GetOrderManagement7;
import com.tms.dto.GetOrderManagement7Resp;

@RestController
@RequestMapping("/inactive")
public class InactiveController extends BaseController {

	@Autowired
	InactiveService inactiveService;

	@RequestMapping(value = "/trash", method = {RequestMethod.GET, RequestMethod.POST})
	public TMSResponse<List<GetOrderManagement7Resp>> getListTrashSearchByPage(GetOrderManagement7 params)
			throws TMSException {
		if(params == null)
			params = new GetOrderManagement7();
		params.setOrgId(getCurOrgId());
		params.setLeadStatusId(EnumType.LEAD_STATUS.INVALID.getValue());

		OrderManagermentRespDto result = inactiveService.searchByPage(params, false);
		if(result.getOrderManagement7Resp().isEmpty())
			throw new TMSException(ErrorMessage.NOT_FOUND);
		return TMSResponse.buildResponse(result.getOrderManagement7Resp(), result.getRowCount());
	}

	@PutMapping("/trash/move")
	public TMSResponse moveListTrashToFresh(@RequestBody CLInactiveMoveTrash dto) throws TMSException {
		return inactiveService.moveListTrashToFresh(SESSION_ID, _curUser, dto);
	}

}
