package com.tms.api.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tms.api.response.TMSResponse;
import com.tms.dto.GetCallStrategy;
import com.tms.dto.GetCallStrategyResp;
import com.tms.dto.GetStrategyParam;
import com.tms.dto.GetStrategyParamResp;
import com.tms.service.impl.LCProvinceService;

@RestController
@RequestMapping("/strategy")
@SuppressWarnings("unchecked")
public class CallStrategyController extends BaseController{
	Logger logger = LoggerFactory.getLogger(AgentGroupController.class);

    @Autowired
    LCProvinceService lcProvinceService;
    

}
