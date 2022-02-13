package com.tms.api.service;

import com.tms.api.dto.OrderManagermentRespDto;
import com.tms.api.exception.TMSException;
import com.tms.api.response.TMSResponse;
import com.tms.dto.CLInactiveMoveTrash;
import com.tms.dto.GetOrderManagement7;
import com.tms.entity.User;

public interface InactiveService {

	OrderManagermentRespDto searchByPage(GetOrderManagement7 params, boolean isExport);

	TMSResponse moveListTrashToFresh(String SESSION_ID, User _curUser, CLInactiveMoveTrash dto) throws TMSException;

}
