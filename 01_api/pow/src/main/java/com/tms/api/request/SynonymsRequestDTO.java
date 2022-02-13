package com.tms.api.request;

public class SynonymsRequestDTO {

    private String type;
    private String name;
    private Integer value;
    private Integer typeId;

    public SynonymsRequestDTO() {
    }

    public SynonymsRequestDTO(String type, String name, Integer value, Integer typeId) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
