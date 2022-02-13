package com.tms.api.response;

public class AppointmentDateResponse {

    private boolean active;
    private int folioLimit;
    private String modLimit;

    public AppointmentDateResponse() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getFolioLimit() {
        return folioLimit;
    }

    public void setFolioLimit(int folioLimit) {
        this.folioLimit = folioLimit;
    }

    public String getModLimit() {
        return modLimit;
    }

    public void setModLimit(String modLimit) {
        this.modLimit = modLimit;
    }
}
