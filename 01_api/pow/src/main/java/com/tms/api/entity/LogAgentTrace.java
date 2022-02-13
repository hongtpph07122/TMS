package com.tms.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "log_agent_trace")
public class LogAgentTrace implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false)
    private Integer id;

    @Column(name = "agent_id", nullable = false)
    private Integer agentId;
    @Column(name = "activity_id", nullable = false)
    private Integer activityId;
    @Column(name = "object_type")
    private String objectType;
    @Column(name = "object_id")
    private Integer objectId;
    @Column(name = "object_value")
    private String objectValue;
    @Column(name = "on_field")
    private String onField;
    @Column(name = "last_value")
    private String lastValue;
    @Column(name = "value", nullable = false)
    private String value;
    @Column(name = "message")
    private String message;
    @Column(name = "action_time", nullable = false)
    private Date actionTime;
    @Column(name = "created_at", insertable = false, updatable = false)
    private Date createdAt;
    @Column(name = "session_id")
    private UUID sessionId;
    @Column(name = "value_code")
    private Integer valueCode;
    @Column(name = "last_value_code")
    private Integer lastValueCode;
    @Column(name = "flag_code")
    private Integer flagCode;
    @Column(name = "flag_message")
    private String flagMessage;
    @Column(name = "flag_value")
    private String flagValue;

    public String getFlagValue() {
        return flagValue;
    }

    @Transient
    @JsonIgnore
    private Boolean isTemporary;

    public Boolean getTemporary() {
        return isTemporary;
    }

    public void setTemporary(Boolean temporary) {
        isTemporary = temporary;
    }

    public String getFlagMessage() {
        return flagMessage;
    }

    public Integer getValueCode() {
        return valueCode;
    }

    public Integer getLastValueCode() {
        return lastValueCode;
    }

    public Integer getFlagCode() {
        return flagCode;
    }

    public LogAgentTrace(LogAgentTraceBuilder builder) {
        this.id = builder.id;
        this.agentId = builder.agentId;
        this.activityId = builder.activityId;
        this.objectId = builder.objectId;
        this.objectType = builder.objectType;
        this.objectValue = builder.objectValue;
        this.onField = builder.onField;
        this.lastValue = builder.lastValue;
        this.value = builder.value;
        this.sessionId = builder.sessionId;
        this.message = builder.message;
        this.actionTime = builder.actionTime;
        this.createdAt = builder.createdAt;
        this.valueCode = builder.valueCode;
        this.lastValueCode = builder.lastValueCode;
        this.flagCode = builder.flagCode;
        this.flagMessage = builder.flagMessage;
        this.flagValue = builder.flagValue;
    }

    public LogAgentTrace() {

    }

    public Integer getId() {
        return id;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public String getObjectType() {
        return objectType;
    }

    public Integer getObjectId() {
        if (objectId == null) {
            return 0;
        }
        return objectId;
    }

    public String getObjectValue() {
        return objectValue;
    }

    public String getOnField() {
        return onField;
    }

    public String getLastValue() {
        return lastValue;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFlagCode(Integer flagCode) {
        this.flagCode = flagCode;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    //Builder Class
    public static class LogAgentTraceBuilder {
        // required parameters
        private final Integer agentId;
        private final Integer activityId;
        private final String value;
        private final Integer valueCode;
        private Date actionTime;

        // optional parameters
        private Integer id;
        private String objectType;
        private Integer objectId;
        private String objectValue;
        private String onField;
        private String lastValue;
        private String message;
        private UUID sessionId;
        private Date createdAt;
        private Integer lastValueCode;
        private Integer flagCode;
        private String flagMessage;
        private String flagValue;

        public LogAgentTraceBuilder(Integer agentId, Integer activityId, String value, Integer valueCode, Date actionTime) {
            this.agentId = agentId;
            this.activityId=activityId;
            this.value = value;
            this.valueCode = valueCode;
            this.actionTime = actionTime;
        }

        public LogAgentTraceBuilder setId(Integer id) {
            this.id = id;
            return this;
        }

        public LogAgentTraceBuilder setObjectType(String objectType) {
            this.objectType = objectType;
            return this;
        }

        public LogAgentTraceBuilder setObjectId(Integer objectId) {
            this.objectId = objectId;
            return this;
        }

        public LogAgentTraceBuilder setObjectValue(String objectValue) {
            this.objectValue = objectValue;
            return this;
        }

        public LogAgentTraceBuilder setOnField(String onField) {
            this.onField = onField;
            return this;
        }

        public LogAgentTraceBuilder setLastValue(String lastValue) {
            this.lastValue = lastValue;
            return this;
        }

        public LogAgentTraceBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public LogAgentTraceBuilder setSessionId(UUID sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public LogAgentTraceBuilder setActionTime(Date actionTime) {
            this.actionTime = actionTime;
            return this;
        }

        public LogAgentTraceBuilder setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public LogAgentTraceBuilder setLastValueCode(Integer lastValueCode) {
            this.lastValueCode = lastValueCode;
            return this;
        }

        public LogAgentTraceBuilder setFlagCode(Integer flagCode) {
            this.flagCode = flagCode;
            return this;
        }

        public LogAgentTraceBuilder setFlagMessage(String flagMessage) {
            this.flagMessage = flagMessage;
            return this;
        }

        public LogAgentTraceBuilder setFlagValue(String flagValue) {
            this.flagValue = flagValue;
            return this;
        }

        public LogAgentTrace build(){
            return new LogAgentTrace(this);
        }

    }
}
