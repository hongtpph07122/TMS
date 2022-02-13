package com.tms.api.dto;

import java.util.Date;

public class LogAgentTraceDto {
    // required parameters
    private final Integer activityId;

    // optional parameters
    private final Boolean isLatest; // optional
    private final Integer limit; // optional
    private final Integer offset; // optional
    private final Date dateFrom; // optional
    private final Date dateTo; // optional

    public int getActivityId() {
        return this.activityId;
    }

    public boolean isLatest() {
        return this.isLatest;
    }

    public Date getDateFrom() {
        return this.dateFrom;
    }

    public Date getDateTo() {
        return this.dateTo;
    }

    public int getLimit() {
        return this.limit;
    }

    public int getOffset() {
        return this.offset;
    }

    private LogAgentTraceDto(LogAgentTraceDtoBuilder builder) {
        this.activityId = builder.activityId;
        this.isLatest = builder.isLatest;
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.dateFrom = builder.dateFrom;
        this.dateTo = builder.dateTo;
    }

    //Builder Class
    public static class LogAgentTraceDtoBuilder {

        // required parameters
        private final Integer activityId;

        // optional parameters
        private Boolean isLatest = true; // optional
        private Integer limit = 1; // optional
        private Integer offset = 0; // optional
        private Date dateFrom; // optional
        private Date dateTo; // optional

        public LogAgentTraceDtoBuilder(Integer activityId) {
            this.activityId=activityId;
        }

        public LogAgentTraceDtoBuilder setLatest(boolean isLatest) {
            this.isLatest = isLatest;
            return this;
        }

        public LogAgentTraceDtoBuilder setDateFrom(Date dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public LogAgentTraceDtoBuilder setDateTo(Date dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public LogAgentTraceDtoBuilder setLimit (Integer limit) {
            this.limit = limit;
            return this;
        }

        public LogAgentTraceDtoBuilder setOffset (Integer offset) {
            this.offset = offset;
            return this;
        }

        public LogAgentTraceDto build(){
            return new LogAgentTraceDto(this);
        }

    }
}
