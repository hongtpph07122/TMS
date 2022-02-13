package com.oauthcentralization.app.tmsoauth2.entities.baseEntity;

import javax.persistence.*;

import static com.oauthcentralization.app.tmsoauth2.variables.ColumnsVariable.*;

@MappedSuperclass
public class TeamMembersBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ID, nullable = false)
    private Integer id;

    @Column(name = TEAM_ID)
    private Integer teamId;

    @Column(name = USER_ID, nullable = false)
    private Integer userId;

    @Column(name = ENABLED)
    private Boolean enabled;

    public TeamMembersBaseEntity() {
        setEnabled(true);
        setUserId(0);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
