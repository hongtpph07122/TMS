package com.oauthcentralization.app.tmsoauth2.model.dto;

public class StatusCodeResponseDTO {
    private int code;
    private String text;
    private String type;
    private String description;

    public StatusCodeResponseDTO() {
    }

    public StatusCodeResponseDTO(int code, String text, String type, String description) {
        this.code = code;
        this.text = text;
        this.type = type;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "StatusCodeResponseDTO{" +
                "code=" + code +
                ", text='" + text + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
