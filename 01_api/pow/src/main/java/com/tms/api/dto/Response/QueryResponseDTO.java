package com.tms.api.dto.Response;

import javax.persistence.Query;

public class QueryResponseDTO {

    private Query query;
    private Query queryAsCounter;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Query getQueryAsCounter() {
        return queryAsCounter;
    }

    public void setQueryAsCounter(Query queryAsCounter) {
        this.queryAsCounter = queryAsCounter;
    }
}
