package com.derekprovance.apiObject.issues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssue {
    String id;
    String self;
    String key;
    JiraIssueFields fields = new JiraIssueFields();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JiraIssueFields getFields() {
        return fields;
    }

    public void setFields(JiraIssueFields fields) {
        this.fields = fields;
    }
}
