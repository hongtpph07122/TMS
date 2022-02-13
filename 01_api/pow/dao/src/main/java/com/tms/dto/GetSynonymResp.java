package com.tms.dto;

public class GetSynonymResp {

    private Integer synonymId;
    private Integer typeId;
    private String type;
    private String name;
    private Integer value;
    private String dscr;
    private Integer id;

    public Integer getSynonymId() {
        return synonymId;
    }

    public void setSynonymId(Integer synonymId) {
        this.synonymId = synonymId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
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

    public String getDscr() {
        return dscr;
    }

    public void setDscr(String dscr) {
        this.dscr = dscr;
    }

    public Integer getId() {
        return synonymId;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
