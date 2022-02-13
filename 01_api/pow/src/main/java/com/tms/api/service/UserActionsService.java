package com.tms.api.service;

import com.tms.api.dto.Request.UserActiveRequestDTO;
import com.tms.api.response.TMSResponse;
import com.tms.entity.User;

public interface UserActionsService {

    TMSResponse<?> updateOne(User currentUser, UserActiveRequestDTO userActiveRequestDTO);

    User findOne(Integer id);
}
