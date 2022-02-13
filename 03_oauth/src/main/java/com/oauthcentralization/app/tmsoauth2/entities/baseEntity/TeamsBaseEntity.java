package com.oauthcentralization.app.tmsoauth2.entities.baseEntity;

import javax.persistence.*;

import static com.oauthcentralization.app.tmsoauth2.variables.ColumnsVariable.*;

@MappedSuperclass
public class TeamsBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID, nullable = false)
    private Integer id;

    @Column(name = NAME)
    private String name;

    @Column(name = ENABLE)
    private Boolean enable;

    @Column(name = MANAGER_ID)
    private Integer managerId;

    @Column(name = TEAM_TYPE)
    private Integer teamType;

    public TeamsBaseEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getTeamType() {
        return teamType;
    }

    public void setTeamType(Integer teamType) {
        this.teamType = teamType;
    }
}
