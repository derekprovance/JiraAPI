package com.derekprovance.apiObject.requests;

public class SearchRequest {

    String jql;
    int startsAt;
    int maxResults;
    String[] fields = new String[]{"summary", "status", "assignee"};

    public String getJql() {
        return jql;
    }

    public void setJql(String jql) {
        this.jql = jql;
    }

    public int getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(int startsAt) {
        this.startsAt = startsAt;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }
}
