package com.tms.api.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.NotNull;

public class ConfigLogDevRequestDTO {
    @NotNull(message = "module is not null")
    private String module;
    private String description;
    @JsonAlias("active")
    private Boolean isActive;

    public ConfigLogDevRequestDTO() {
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
