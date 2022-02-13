package com.oauthcentralization.app.tmsoauth2.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersTypeResponse;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersRequest {

    private String username;
    @JsonIgnore
    private String usernameDomain;
    private String fullName;
    private String email;
    private String phone;
    private String password;
    private UsersTypeResponse userType;
    private Date birthday;
    @JsonIgnore
    private String errorsReason;


    public UsersRequest() {
    }

    public UsersTypeResponse getUserType() {
        return userType;
    }

    public void setUserType(UsersTypeResponse userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getUsernameDomain() {
        return usernameDomain;
    }

    public void setUsernameDomain(String usernameDomain) {
        this.usernameDomain = usernameDomain;
    }

    public String getErrorsReason() {
        return errorsReason;
    }

    public void setErrorsReason(String errorsReason) {
        this.errorsReason = errorsReason;
    }
}
