package com.tms.api.dto.Response;

public class RescueTypeResponse {

    private int jobType;
    private String name;

    public RescueTypeResponse() {
    }

    public RescueTypeResponse(int jobType, String name) {
        this.jobType = jobType;
        this.name = name;
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
}
