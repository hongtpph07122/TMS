package com.tms.api.rest;

import com.tms.api.dto.Request.OrderManagementRequestDTO;
import com.tms.api.dto.*;
import com.tms.api.entity.*;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.*;
import com.tms.api.repository.RcActionMappingRepository;
import com.tms.api.repository.RcActivityRepository;
import com.tms.api.repository.RcLastmileStatusRepository;
import com.tms.api.repository.RcRescueJobRepository;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.DOService;
import com.tms.api.service.OrOfferService;
import com.tms.api.service.impl.RescueServiceImpl;
import com.tms.api.task.DBLogWriter;
import com.tms.dto.*;
import com.tms.entity.CLFresh;
import com.tms.entity.SaleOrder;
import com.tms.entity.log.InsCLFreshV5;
import com.tms.entity.log.InsDeliveryOrder;
import com.tms.entity.log.InsOffer;
import com.tms.entity.log.UpdDeliveryOrder;
import com.tms.entity.log.UpdDoNew;
import com.tms.entity.log.UpdOrOffer;
import com.tms.ff.dto.request.GHN.GhnOrderInfoRequestDto;
import com.tms.ff.service.GHN.impl.GhnOrderServiceImpl;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.DeliveryOrderService;
import com.tms.service.impl.LogService;
import org.apache.poi.ss.usermodel.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    OrOfferService orOfferService;
    @PostMapping("/OrOffer")
    public TMSResponse createOrOffer(@RequestBody InsOffer offer) throws TMSException {
        // insert log
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        CreateUpdateOfferResponse response = orOfferService.insOrOffer(sessionId, offer);
        int HttpStatus = (null == response) ? 500 : 200;
        return TMSResponse.buildResponse(response, 0, "", HttpStatus);
    }
    @PutMapping("/OrOffer")
    public TMSResponse updateOrOffer(@RequestBody UpdOrOffer offer) throws TMSException {
        // insert log
        DBResponse dbResponse = null;
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        dbResponse =  orOfferService.updOrOffer(sessionId, offer);
        int HttpStatus = (null == dbResponse ? 500 : 200);
        return TMSResponse.buildResponse(dbResponse.getErrorMsg().trim(), 0, "", HttpStatus);
    }

}
