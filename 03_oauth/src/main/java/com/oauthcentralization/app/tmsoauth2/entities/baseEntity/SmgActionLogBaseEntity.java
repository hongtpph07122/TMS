package com.oauthcentralization.app.tmsoauth2.entities.baseEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.time.LocalDateTime;

@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@MappedSuperclass
public class SmgActionLogBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(
            name = "created_date",
            columnDefinition = "TIMESTAMP",
            nullable = false
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "request_json", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private JsonNode requestJson;

    @Column(name = "log_json_after", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private JsonNode logJsonAfter;

    @Column(name = "log_json_before", columnDefinition = "jsonb")
    @Type(type = "jsonb")
    private JsonNode logJsonBefore;

    @Column(name = "action_type")
    private Integer actionType;

    @Column(name = "module_type")
    private Integer moduleType;

    @Column(name = "on_table")
    private String onTable;

    public SmgActionLogBaseEntity() {
        setCreatedDate(LocalDateTime.now());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setRequestJson(JsonNode requestJson) {
        this.requestJson = requestJson;
    }

    public void setLogJsonAfter(JsonNode logJsonAfter) {
        this.logJsonAfter = logJsonAfter;
    }

    public void setLogJsonBefore(JsonNode logJsonBefore) {
        this.logJsonBefore = logJsonBefore;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }

    public void setOnTable(String onTable) {
        this.onTable = onTable;
    }
}
