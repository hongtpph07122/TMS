package com.oauthcentralization.app.tmsoauth2.model.request;

public class RolesRequest {

    private String name;
    private String label;
    private String description;

    public RolesRequest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
