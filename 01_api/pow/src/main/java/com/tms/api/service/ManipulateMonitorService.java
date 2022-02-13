package com.tms.api.service;

import com.tms.api.dto.Request.ManipulateMonitorRequestDTO;
import com.tms.api.dto.Request.ObjectRequestDTO;
import com.tms.api.dto.Response.ManipulateMonitorResponseDTO;

public interface ManipulateMonitorService {

    ManipulateMonitorResponseDTO snagAllManipulatesMonitor(ManipulateMonitorRequestDTO manipulateMonitorRequestDTO, ObjectRequestDTO objectRequestDTO);

    byte[] exportExcelManipulateMonitor(String sheetName, ManipulateMonitorRequestDTO manipulateMonitorRequestDTO, ObjectRequestDTO objectRequestDTO);
}
