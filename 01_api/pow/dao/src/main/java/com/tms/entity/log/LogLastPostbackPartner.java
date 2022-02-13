package com.tms.entity.log;

public class LogLastPostbackPartner {

    private Integer leadId;
    private Integer logId;
    private Integer orgId;
    private Integer partnerId;
    private String objectPartnerCode;
    private String requestMethod;
    private String requestUrl;
    private String requestBody;
    private String requestJson;
    private String responseMethod;
    private String responseUrl;
    private String responseBody;
    private String responseJson;
    private String statusResponse;
    private Integer statusPostback;
    private String postbackDate;

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getObjectPartnerCode() {
        return objectPartnerCode;
    }

    public void setObjectPartnerCode(String objectPartnerCode) {
        this.objectPartnerCode = objectPartnerCode;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }

    public String getResponseMethod() {
        return responseMethod;
    }

    public void setResponseMethod(String responseMethod) {
        this.responseMethod = responseMethod;
    }

    public String getResponseUrl() {
        return responseUrl;
    }

    public void setResponseUrl(String responseUrl) {
        this.responseUrl = responseUrl;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public String getPostbackDate() {
        return postbackDate;
    }

    public void setPostbackDate(String postbackDate) {
        this.postbackDate = postbackDate;
    }

    public String getStatusResponse() {
        return statusResponse;
    }

    public void setStatusResponse(String statusResponse) {
        this.statusResponse = statusResponse;
    }

    public Integer getStatusPostback() {
        return statusPostback;
    }

    public void setStatusPostback(Integer statusPostback) {
        this.statusPostback = statusPostback;
    }

}
