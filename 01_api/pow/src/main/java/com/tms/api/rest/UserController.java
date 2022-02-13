package com.tms.api.rest;

import com.tms.api.dto.Request.UserActiveRequestDTO;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.UserActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController {


    private final UserActionsService userActionsService;

    @Autowired
    public UserController(UserActionsService userActionsService) {
        this.userActionsService = userActionsService;
    }

    @PostMapping("/update")
    public @ResponseBody
    ResponseEntity<TMSResponse<?>> updateOne(@RequestBody UserActiveRequestDTO userActiveRequestDTO) {
        return new ResponseEntity<>(userActionsService.updateOne(_curUser, userActiveRequestDTO), HttpStatus.OK);
    }
}
