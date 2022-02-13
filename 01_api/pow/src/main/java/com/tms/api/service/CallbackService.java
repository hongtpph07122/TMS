package com.tms.api.service;

import com.tms.api.dto.CreateCallBackDto;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.response.TMSResponse;

import java.util.List;

public interface CallbackService {

    TMSResponse<?> creatOne(CreateCallBackDto callbackRequest, ObjectRequestDTO objectRequestDTO);

    Integer delete(List<Integer> leadId, Integer orgId);
}
