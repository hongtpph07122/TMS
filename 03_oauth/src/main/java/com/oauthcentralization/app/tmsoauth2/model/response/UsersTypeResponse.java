package com.oauthcentralization.app.tmsoauth2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersTypeResponse {

    private int value;
    private String name;
    private String nameOriginalFilter;

    public UsersTypeResponse() {
    }

    public UsersTypeResponse(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public UsersTypeResponse(int value, String name, String nameOriginalFilter) {
        this.value = value;
        this.name = name;
        this.nameOriginalFilter = nameOriginalFilter;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOriginalFilter() {
        return nameOriginalFilter;
    }

    public void setNameOriginalFilter(String nameOriginalFilter) {
        this.nameOriginalFilter = nameOriginalFilter;
    }
}
