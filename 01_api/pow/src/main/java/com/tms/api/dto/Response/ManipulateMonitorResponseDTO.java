package com.tms.api.dto.Response;

import com.tms.api.dto.Request.ManipulateMonitorRequestDTO;

import java.util.List;

public class ManipulateMonitorResponseDTO {

    private List<ManipulateMonitorRequestDTO> manipulateMonitorRequestDTOList;
    private Integer counter;

    public List<ManipulateMonitorRequestDTO> getManipulateMonitorRequestDTOList() {
        return manipulateMonitorRequestDTOList;
    }

    public void setManipulateMonitorRequestDTOList(List<ManipulateMonitorRequestDTO> manipulateMonitorRequestDTOList) {
        this.manipulateMonitorRequestDTOList = manipulateMonitorRequestDTOList;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }
}
