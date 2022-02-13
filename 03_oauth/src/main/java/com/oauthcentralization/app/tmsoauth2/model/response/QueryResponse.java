package com.oauthcentralization.app.tmsoauth2.model.response;

import javax.persistence.Query;

public class QueryResponse {

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
