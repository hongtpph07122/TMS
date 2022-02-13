package com.tms.api.rest;

import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.response.TMSResponse;
import com.tms.dto.DBResponse;
import com.tms.dto.GetOrDepartment;
import com.tms.dto.GetOrDepartmentResp;
import com.tms.entity.log.InsOrDepartment;
import com.tms.entity.log.UpdOrDepartment;
import com.tms.service.impl.CLCallbackService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController extends BaseController {
    Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    CLCallbackService clCallbackService;

    @Autowired
    LogService logService;
    @Autowired
    ModelMapper modelMapper;

    @PostMapping
    public TMSResponse createDepartment(@RequestBody InsOrDepartment input) throws TMSException {
        InsOrDepartment insOrDepartment = new InsOrDepartment();
        insOrDepartment.setOrgId(getCurOrgId());
        insOrDepartment.setDistrict(input.getDistrict());
        insOrDepartment.setAddress(input.getAddress());
        insOrDepartment.setName(input.getName());
        insOrDepartment.setShortName(input.getShortName());
        insOrDepartment.setProvince(input.getProvince());
        insOrDepartment.setSubdistrict(input.getSubdistrict());
        insOrDepartment.setDscr(input.getDscr());
        insOrDepartment.setManager(input.getManager());
        insOrDepartment.setLevel(input.getLevel());

        DBResponse response = logService.insOrDepartment(SESSION_ID, insOrDepartment);
        int HttpStatus = (response.getErrorCode() == 1 ? 200 : response.getErrorCode());
        return TMSResponse.buildResponse(response.getErrorMsg().trim(), 0, "", HttpStatus);

    }

    @GetMapping("/{depId}")
    public TMSResponse department(@PathVariable Integer depId) throws TMSException {
        GetOrDepartment orDepartment = new GetOrDepartment();
        orDepartment.setDepId(depId);
        orDepartment.setOrgId(getCurOrgId());
        DBResponse<List<GetOrDepartmentResp>> dbResponse = clCallbackService.getOrDepartment(SESSION_ID, orDepartment);
        if (dbResponse.getResult().isEmpty()) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @GetMapping
    public TMSResponse lstDepartment(GetOrDepartment params) throws TMSException{
        int orgId = getCurOrgId();
        params.setOrgId(orgId);
        DBResponse<List<GetOrDepartmentResp>> dbResponse = clCallbackService.getOrDepartment(SESSION_ID, params);
        if (dbResponse.getResult().isEmpty()) {
            logger.info(ErrorMessage.NOT_FOUND.getMessage());
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        return TMSResponse.buildResponse(dbResponse.getResult(), dbResponse.getRowCount());
    }

    @PutMapping
    public TMSResponse updateDepartment(@RequestBody UpdOrDepartment input) throws TMSException {
        if (input.getDepId() == null) {
            return TMSResponse.buildResponse(null, 0, "Department ID is null", 400);
        }
        GetOrDepartment params = new GetOrDepartment();
        params.setDepId(input.getDepId());

        DBResponse<List<GetOrDepartmentResp>> response = clCallbackService.getOrDepartment(SESSION_ID, params);
        if (response.getResult().isEmpty()) {
            throw new TMSException(ErrorMessage.NOT_FOUND);
        }
        try {
            // if (response != null || !response.getResult().isEmpty()) {
            GetOrDepartmentResp resp = response.getResult().get(0);
            UpdOrDepartment update = modelMapper.map(resp, UpdOrDepartment.class);
                if (input.getAddress() != null) {
                    update.setAddress(input.getAddress());
                }
                if (input.getDistrict() != null) {
                    update.setDistrict(input.getDistrict());
                }
                if (input.getProvince() != null) {
                    update.setProvince(input.getProvince());
                }
                if (input.getSubdistrict() != null) {
                    update.setSubdistrict(input.getSubdistrict());
                }
                if (input.getManager() != null) {
                    update.setManager(input.getManager());
                }
                if (input.getLevel() != null) {
                    update.setLevel(input.getLevel());
                }
                if (input.getName() != null) {
                    update.setName(input.getName());
                }
                if (input.getShortName() != null) {
                    update.setShortName(input.getShortName());
                }
            DBResponse dbResponse = logService.updOrDepartment(SESSION_ID, update);
            return TMSResponse.buildResponse(dbResponse);
            //}
        } catch (Exception e) {
            return TMSResponse.buildResponse(null, 0, "Department is not found", 400);
        }

        //  return TMSResponse.buildResponse(null, 0, "Department is not found", 400);
    }

    @DeleteMapping
    public TMSResponse deleteDepartment(Integer deptId) {

        return null;
    }

}
