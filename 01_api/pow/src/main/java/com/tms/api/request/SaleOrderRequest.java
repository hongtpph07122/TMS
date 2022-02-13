package com.tms.api.request;

import java.util.Date;

public class SaleOrderRequest {

    private Date appointmentDate;

    public SaleOrderRequest() {
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}
