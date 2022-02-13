package com.tms.api.dto.Response;

public class ScaleoCreateLeadRes {

    private String status;
    private String code;
    private String name;
    private String message;
    private ScaleoLeadInfoRes info;
    private String error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ScaleoLeadInfoRes getInfo() {
        return info;
    }

    public void setInfo(ScaleoLeadInfoRes info) {
        this.info = info;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
