package com.tms.api.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tms.api.dto.ReportWrapperDto;
import com.tms.api.entity.*;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.ExcelHelper;
import com.tms.api.helper.SpecificationsBuilder;
import com.tms.api.repository.*;
import com.tms.api.response.TMSResponse;
import com.tms.service.impl.UserService;

@RestController
@RequestMapping("/report")
@SuppressWarnings("rawtypes")
public class ReportController extends BaseController {
    Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    UserService userService;

    @Autowired
    CustomerAddReportRepository customerRepository;

    @Autowired
    ActivitySumaryRepository activitySumaryRepository;

    @Autowired
    AgentActivityRepository agentActivityRepository;

    @Autowired
    AgentLoginRepository agentLoginRepository;

    @Autowired
    CallDetailRepository CallDetailRepository;

    @Autowired
    CallDurationRepository callDurationRepository;

    @Autowired
    CallSumaryByDateRepository callSumaryByDateRepository;

    @Autowired
    DialledListDetailRepository dialledListDetailRepository;

    @Autowired
    DialledListSumaryRepository dialledListSumaryRepository;

    @Autowired
    PhoneContactedRepository phoneContactedRepository;

    @Autowired
    PhoneNotContactRepository phoneNotContactRepository;

    @PostMapping("/customeradd/file")
    public ResponseEntity getCustomerAddReportFile(@RequestBody ReportWrapperDto reportWrapperDto) throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), CustomerAddReport.class);
        List<CustomerAddReport> response = customerRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, CustomerAddReport.class);
        return TMSResponse.buildExcelFileResponse(out, "customer_add_report.xlsx");
    }

    @PostMapping("/customeradd")
    public TMSResponse<List<CustomerAddReport>> getCustomerAddReport(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), CustomerAddReport.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<CustomerAddReport> response = customerRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/ActivitySumary/file")
    public ResponseEntity getActivitySumaryReportFile(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), ActivitySumary.class);
        Iterable<ActivitySumary> response = activitySumaryRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, ActivitySumary.class);
        return TMSResponse.buildExcelFileResponse(out, "activity_sumary.xlsx");
    }

    @PostMapping("/ActivitySumary")
    public TMSResponse getActivitySumaryReport(@RequestBody ReportWrapperDto reportWrapperDto) throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), ActivitySumary.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<ActivitySumary> response = activitySumaryRepository.findAll(spec, pagging);
        byte[] out = ExcelHelper.createExcelData(response, ActivitySumary.class);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/AgentActivity/file")
    public ResponseEntity getAgentActivityReportFile(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), AgentActivity.class);
        Iterable<AgentActivity> response = agentActivityRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, AgentActivity.class);
        return TMSResponse.buildExcelFileResponse(out, "agent_activity.xlsx");
    }

    @PostMapping("/AgentActivity")
    public TMSResponse<List<CustomerAddReport>> getAgentActivityReport(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), AgentActivity.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<AgentActivity> response = agentActivityRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/AgentLogin/file")
    public ResponseEntity getAgentLoginReportFile(@RequestBody ReportWrapperDto reportWrapperDto) throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), AgentLogin.class);
        Iterable<AgentLogin> response = agentLoginRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, AgentLogin.class);
        return TMSResponse.buildExcelFileResponse(out, "agent_login.xlsx");
    }

    @PostMapping("/AgentLogin")
    public TMSResponse<List<CustomerAddReport>> getAgentLoginReport(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), AgentLogin.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<AgentLogin> response = agentLoginRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/CallDetail/file")
    public ResponseEntity getCallDetailReportFile(@RequestBody ReportWrapperDto reportWrapperDto) throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), CallDetail.class);
        Iterable<CallDetail> response = CallDetailRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, CallDetail.class);
        return TMSResponse.buildExcelFileResponse(out, "call_detail.xlsx");
    }

    @PostMapping("/CallDetail")
    public TMSResponse<List<CustomerAddReport>> getCallDetailReport(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), CallDetail.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<CallDetail> response = CallDetailRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/CallDuration/file")
    public ResponseEntity getCallDurationReportFile(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), CallDuration.class);
        Iterable<CallDuration> response = callDurationRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, CallDuration.class);
        return TMSResponse.buildExcelFileResponse(out, "call_duration.xlsx");
    }

    @PostMapping("/CallDuration")
    public TMSResponse<List<CustomerAddReport>> getCallDurationReport(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), CallDuration.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<CallDuration> response = callDurationRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/CallSumaryByDate/file")
    public ResponseEntity getCallSumaryByDateReportFile(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), CallSumaryByDate.class);
        Iterable<CallSumaryByDate> response = callSumaryByDateRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, CallSumaryByDate.class);
        return TMSResponse.buildExcelFileResponse(out, "call_sumary_bydate.xlsxx");
    }

    @PostMapping("/CallSumaryByDate")
    public TMSResponse<List<CustomerAddReport>> getCallSumaryByDateReport(
            @RequestBody ReportWrapperDto reportWrapperDto) throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), CallSumaryByDate.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<CallSumaryByDate> response = callSumaryByDateRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/DialledListDetail/file")
    public ResponseEntity getDialledListDetailReportFile(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), DialledListDetail.class);
        Iterable<DialledListDetail> response = dialledListDetailRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, DialledListDetail.class);
        return TMSResponse.buildExcelFileResponse(out, "dialled_list_detail.xlsx");
    }

    @PostMapping("/DialledListDetail")
    public TMSResponse<List<CustomerAddReport>> getDialledListDetailReport(
            @RequestBody ReportWrapperDto reportWrapperDto) throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), DialledListDetail.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<DialledListDetail> response = dialledListDetailRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/DialledListSumary/file")
    public ResponseEntity getDialledListSumaryReportFile(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), DialledListSumary.class);
        Iterable<DialledListSumary> response = dialledListSumaryRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, DialledListSumary.class);
        return TMSResponse.buildExcelFileResponse(out, "dialled_list_sumary.xlsx");
    }

    @PostMapping("/DialledListSumary")
    public TMSResponse<List<CustomerAddReport>> getDialledListSumaryReport(
            @RequestBody ReportWrapperDto reportWrapperDto) throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), DialledListSumary.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<DialledListSumary> response = dialledListSumaryRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/PhoneContacted/file")
    public ResponseEntity getPhoneContactedReportFile(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), PhoneContacted.class);
        Iterable<PhoneContacted> response = phoneContactedRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, PhoneContacted.class);
        return TMSResponse.buildExcelFileResponse(out, "phone_contacted.xlsx");
    }

    @PostMapping("/PhoneContacted")
    public TMSResponse<List<CustomerAddReport>> getPhoneContactedReport(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), PhoneContacted.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<PhoneContacted> response = phoneContactedRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }

    @PostMapping("/PhoneNotContact/file")
    public ResponseEntity getPhoneNotContactReportFile(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), PhoneNotContact.class);
        Iterable<PhoneNotContact> response = phoneNotContactRepository.findAll(spec);
        byte[] out = ExcelHelper.createExcelData(response, PhoneNotContact.class);
        return TMSResponse.buildExcelFileResponse(out, "phone_not_contact.xlsx");
    }

    @PostMapping("/PhoneNotContact")
    public TMSResponse<List<CustomerAddReport>> getPhoneNotContactReport(@RequestBody ReportWrapperDto reportWrapperDto)
            throws TMSException {
        Specification spec = SpecificationsBuilder.build(reportWrapperDto.getConditions(), PhoneNotContact.class);
        PageRequest pagging = PageRequest.of(reportWrapperDto.getOffset(), reportWrapperDto.getLimit());
        Page<PhoneNotContact> response = phoneNotContactRepository.findAll(spec, pagging);
        return TMSResponse.buildResponse(response.getContent(), (int) response.getTotalElements());
    }
}
