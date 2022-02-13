package com.tms.api.rest;

import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.response.TMSResponse;
import com.tms.dto.DBResponse;
import com.tms.dto.RoleTMS;
import com.tms.service.impl.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

@RestController
@RequestMapping(value = "/roles")
public class RoleTMSController {

    @Autowired
    RoleService roleService;


    private String getSession(){
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    @GetMapping("/all")
    public TMSResponse<List<RoleTMS>> getRoles() throws TMSException{
        DBResponse<List<RoleTMS>> listDBResponse = roleService.getRoleList(getSession());
        if(listDBResponse.getResult().isEmpty()){
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        return TMSResponse.buildResponse(listDBResponse.getResult(), listDBResponse.getResult().size());
    }

}
