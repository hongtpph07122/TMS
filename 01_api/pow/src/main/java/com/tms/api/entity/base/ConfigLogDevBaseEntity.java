package com.tms.api.entity.base;

import javax.persistence.*;

@MappedSuperclass
public class ConfigLogDevBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "value", nullable = false)
    private Integer value;

    @Column(name = "module")
    private String module;

    @Column(name = "is_active")
    private Boolean isActive;


    public ConfigLogDevBaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
