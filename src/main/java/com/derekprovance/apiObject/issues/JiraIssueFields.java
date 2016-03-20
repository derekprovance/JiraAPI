package com.derekprovance.apiObject.issues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraIssueFields {

    //TODO - remove hardcoded values
    @JsonProperty("customfield_10108")
    String deployment_war;
    @JsonProperty("customfield_10025")
    String deployment_info;

    public JiraIssueFields() {}

    public JiraIssueFields(String deployment_war, String deployment_info) {
        this.deployment_war = deployment_war;
        this.deployment_info = deployment_info;
    }

    public String getDeployment_war() {
        return deployment_war;
    }

    public void setDeployment_war(String deployment_war) {
        this.deployment_war = deployment_war;
    }

    public String getDeployment_info() {
        return deployment_info;
    }

    public void setDeployment_info(String deployment_info) {
        this.deployment_info = deployment_info;
    }
}
