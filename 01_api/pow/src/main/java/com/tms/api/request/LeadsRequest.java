package com.tms.api.request;

import java.util.Date;

public class LeadsRequest {

    private Date appointmentDate;
    private boolean overrideOnSaleOrder;
    private boolean overrideOnDeliveryOrder;

    public LeadsRequest() {
        setOverrideOnSaleOrder(false);
        setOverrideOnDeliveryOrder(false);
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public boolean isOverrideOnSaleOrder() {
        return overrideOnSaleOrder;
    }

    public void setOverrideOnSaleOrder(boolean overrideOnSaleOrder) {
        this.overrideOnSaleOrder = overrideOnSaleOrder;
    }

    public boolean isOverrideOnDeliveryOrder() {
        return overrideOnDeliveryOrder;
    }

    public void setOverrideOnDeliveryOrder(boolean overrideOnDeliveryOrder) {
        this.overrideOnDeliveryOrder = overrideOnDeliveryOrder;
    }
}
