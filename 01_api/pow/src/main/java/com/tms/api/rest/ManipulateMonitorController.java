package com.tms.api.rest;

import com.tms.api.dto.Request.ManipulateMonitorRequestDTO;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Response.ManipulateMonitorResponseDTO;
import com.tms.api.dto.Response.StreamsResponseDTO;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.ManipulateMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/manipulate-monitor")
public class ManipulateMonitorController extends BaseController {

    private final ManipulateMonitorService manipulateMonitorService;

    @Autowired
    public ManipulateMonitorController(ManipulateMonitorService manipulateMonitorService) {
        this.manipulateMonitorService = manipulateMonitorService;
    }

    @RequestMapping(value = "/export-excel", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResponseEntity<?> exportExcelManipulateMonitor(@RequestParam(value = "sheetName", defaultValue = "Sheet1", required = false) String sheetName,
                                                   @RequestParam(value = "filename", defaultValue = "Leads-Manipulated.xlsx", required = false) String filename,
                                                   ManipulateMonitorRequestDTO manipulateMonitorRequestDTO) {
        ObjectRequestDTO objectRequestDTO = new ObjectRequestDTO();
        objectRequestDTO.setUserId(_curUser.getUserId());
        objectRequestDTO.setSessionId(SESSION_ID);
        objectRequestDTO.setOrganizationId(getCurOrgId());

        return StreamsResponseDTO.buildScrapeDocsResponse(manipulateMonitorService.exportExcelManipulateMonitor(sheetName, manipulateMonitorRequestDTO, objectRequestDTO), filename, ".xlsx");
    }

    @GetMapping("/leads-manipulated")
    public @ResponseBody
    ResponseEntity<?> snagAllManipulatesMonitor(ManipulateMonitorRequestDTO manipulateMonitorRequestDTO) {
        ObjectRequestDTO objectRequestDTO = new ObjectRequestDTO();
        objectRequestDTO.setUserId(_curUser.getUserId());
        objectRequestDTO.setSessionId(SESSION_ID);
        objectRequestDTO.setOrganizationId(getCurOrgId());
        ManipulateMonitorResponseDTO manipulateMonitorResponseDTO = manipulateMonitorService.snagAllManipulatesMonitor(manipulateMonitorRequestDTO, objectRequestDTO);
        return new ResponseEntity<>(TMSResponse.buildResponse(manipulateMonitorResponseDTO.getManipulateMonitorRequestDTOList(), manipulateMonitorResponseDTO.getCounter()), HttpStatus.OK);
    }
}
