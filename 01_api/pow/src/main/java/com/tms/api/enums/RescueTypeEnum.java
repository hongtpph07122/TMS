package com.tms.api.enums;


import com.tms.api.dto.Response.RescueTypeResponse;

public enum RescueTypeEnum {

    ALL_RESCUE_TYPE(1, "All Rescue", ""),
    PRE_DELIVERY_TYPE(2, "Pre Delivery", ""),
    APPOINTMENT_DATE_TYPE(3, "Appointment Date", "");


    private int jobType;
    private String name;
    private String description;

    RescueTypeEnum(int jobType, String name, String description) {
        this.jobType = jobType;
        this.name = name;
        this.description = description;
    }


    public static RescueTypeResponse findRescueTypeBy(int jobType) {
        for (RescueTypeEnum rescueType : RescueTypeEnum.values()) {
            if (rescueType.getJobType() == jobType) {
                return new RescueTypeResponse(rescueType.getJobType(), rescueType.getName());
            }
        }
        throw new IllegalArgumentException("The given index does not match any RescueTypeEnum.");
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
