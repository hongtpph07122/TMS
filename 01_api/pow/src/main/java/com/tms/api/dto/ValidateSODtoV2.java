package com.tms.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ValidateSODtoV2 {
    @NotEmpty(message = "Cannot be empty")
    List<Integer> soIds;
    @NotNull(message = "Cannot be null")
    Integer status;
    String creationDate;
    String reason;
    String qaNote;
    @JsonAlias({"appointment_schedule", "appointment_date"})
    private String appointmentDate;

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public List<Integer> getSoIds() {
        return soIds;
    }

    public void setSoIds(List<Integer> soIds) {
        this.soIds = soIds;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getQaNote() {
        return qaNote;
    }

    public void setQaNote(String qaNote) {
        this.qaNote = qaNote;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
