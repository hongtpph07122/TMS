package com.tms.api.request;

import java.util.Date;

public class DeliveriesOrderRequest {

    private Date appointmentDate;

    public DeliveriesOrderRequest() {}

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
