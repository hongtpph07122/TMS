package com.tms.api.service;

import com.tms.api.response.AppointmentDateResponse;

public interface AppointmentDateService {

    AppointmentDateResponse init();

    boolean isValid(String appointmentDate);
}
