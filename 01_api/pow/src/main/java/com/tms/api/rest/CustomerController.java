package com.tms.api.rest;

import com.tms.config.DBConfigMap;
import com.tms.entity.CLCallback;
import com.tms.service.impl.CLActiveService;
import com.tms.service.impl.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController{

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CLActiveService clActiveService;



    @Autowired
    LogService logService;

    @Autowired
    DBConfigMap dbConfigMap;

    @GetMapping("/{agentId}")
    public CLCallback getCallback(@PathVariable Integer agentId) {
        return new CLCallback();
    }

    @PutMapping("/{agentId}")
    public CLCallback updateCallback(@PathVariable Integer agentId) {
        return new CLCallback();
    }


}
