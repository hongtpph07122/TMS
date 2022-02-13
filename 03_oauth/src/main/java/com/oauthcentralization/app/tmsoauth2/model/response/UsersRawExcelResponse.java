package com.oauthcentralization.app.tmsoauth2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersRawExcelResponse {

    private String username;
    private String usernameDomain;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private String userType;
    private String errorsReason;

    public UsersRawExcelResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsernameDomain() {
        return usernameDomain;
    }

    public void setUsernameDomain(String usernameDomain) {
        this.usernameDomain = usernameDomain;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getErrorsReason() {
        return errorsReason;
    }

    public void setErrorsReason(String errorsReason) {
        this.errorsReason = errorsReason;
    }
}
