package com.tms.api.dto.Request;

public class QueryRequestDTO {

    private String query;
    private String queryAsCounters;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryAsCounters() {
        return queryAsCounters;
    }

    public void setQueryAsCounters(String queryAsCounters) {
        this.queryAsCounters = queryAsCounters;
    }
}
