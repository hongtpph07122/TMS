package com.tms.api.response;

public class SynonymsConfigurationResponseDTO {

    private Integer id;
    private String type;
    private String name;
    private Integer value;

    public SynonymsConfigurationResponseDTO() {
    }

    public SynonymsConfigurationResponseDTO(Integer id, String type, String name, Integer value) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
