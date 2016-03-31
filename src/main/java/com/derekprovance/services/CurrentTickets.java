package com.derekprovance.services;

import com.derekprovance.TicketStatus;
import com.derekprovance.apiObject.issues.JiraIssue;
import com.derekprovance.apiObject.JqlQuery;
import com.derekprovance.apiObject.issues.JiraIssueFields;
import com.derekprovance.apiObject.requests.SearchRequest;
import com.derekprovance.configurations.JiraCookie;
import com.derekprovance.configurations.JqlQueryParams;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;

public class CurrentTickets {

    private ApplicationContext context = new ClassPathXmlApplicationContext("AppConfig.xml");
    private ApplicationContext langFile = new ClassPathXmlApplicationContext("language.xml");
    private JiraCookie cookie = JiraCookie.getInstance();

    public void getCurrentTickets() {
        String jqlQuery = JqlQueryParams.getInstance().getJqlQuery();

        HttpEntity<SearchRequest> requestEntity = new HttpEntity<SearchRequest>(new SearchRequest(jqlQuery, 0, 15), cookie.getRequestHeaders());

        //TODO - Post request is not working, next steps will be to try to fix this
        String uri = context.getBean("baseJiraURL") + "rest/api/2/search";
        ResponseEntity<JqlQuery> rssResponse = cookie.getRestTemplate().exchange(uri, HttpMethod.POST, requestEntity, JqlQuery.class);
        JqlQuery rss = rssResponse.getBody();
    }

    public JiraIssue getSpecificTicket(String ticketNumber) {
        String uri = context.getBean("baseJiraURL") + "/rest/api/2/issue/" + ticketNumber;
        ResponseEntity<JiraIssue> rssResponse = cookie.getRestTemplate().exchange(uri, HttpMethod.GET, cookie.getRequestEntity(), JiraIssue.class);
        return rssResponse.getBody();
    }

    private String getRandomCatFact() {
        String uri = "http://catfacts-api.appspot.com/api/facts";
        ResponseEntity<String> rssResponse = cookie.getRestTemplate().exchange(uri, HttpMethod.GET, cookie.getRequestEntity(), String.class);

        String catFact = "";
        try {
            JSONObject obj = new JSONObject(rssResponse.getBody());
            JSONArray catFacts = obj.getJSONArray("facts");
            catFact = (String) catFacts.get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return catFact;
    }

    private void assignIssue(String name, String ticketNumber) {
        String uri = context.getBean("baseJiraURL") + "rest/api/2/issue/" + ticketNumber + "/assignee";
        HttpHeaders requestHeaders = cookie.getRequestHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>("{\"name\":\"" + context.getBean("defaultTicketAssignee") + "\"}", requestHeaders);

        try {
            ResponseEntity<String> request = cookie.getRestTemplate().exchange(uri, HttpMethod.PUT, requestEntity, String.class);
        } catch (Exception e) {
            System.out.println(requestEntity.getBody());
            e.getCause();
        }
    }

    public void updateDeploymentInfoSpecificTicket(String ticketNumber, boolean stageDeploy, JiraIssueFields newInfo, JiraIssueFields existingInfo) {
        newInfo = importNewInfo(newInfo, existingInfo);

        String uri = context.getBean("baseJiraURL") + "/rest/api/2/issue/" + ticketNumber;
        HttpHeaders requestHeaders = cookie.getRequestHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<String>("{\"fields\":" + getJson(newInfo) + "}", requestHeaders);

        ResponseEntity<String> request = cookie.getRestTemplate().exchange(uri, HttpMethod.PUT, requestEntity, String.class);

        if(request.getStatusCode() == HttpStatus.NO_CONTENT && stageDeploy) {
            writeCommentDeploymentSuccessful(ticketNumber, DeploymentWarGenerator.determineDeploymentStatus(newInfo.getDeployment_war()));

            //TODO - automate assigning to project managers instead
            assignIssue((String) context.getBean("defaultTicketAssignee"), ticketNumber);
        }
    }

    private JiraIssueFields importNewInfo(JiraIssueFields newInfo, JiraIssueFields existingInfo) {
        newInfo.setDeployment_war(DeploymentWarGenerator.createWarDeploymentString(newInfo.getDeployment_war(), existingInfo.getDeployment_war()));
        newInfo.setDeployment_info(createDeploymentInfoString(newInfo.getDeployment_info(), existingInfo.getDeployment_info()));

        return newInfo;
    }

    private String createDeploymentInfoString(String newDepInfo, String existingDepInfo) {
        if(existingDepInfo == null) {
            return newDepInfo;
        }

        newDepInfo = (newDepInfo != null) ? newDepInfo : "";
        return existingDepInfo + "\n" + newDepInfo;
    }

    private void writeCommentDeploymentSuccessful(String ticketNumber, TicketStatus status) {
        String deploymentMessage = (status == TicketStatus.STAGING) ? (String) langFile.getBean("successfulDeploymentStage") : (String) langFile.getBean("successfulDeploymentQA");
        String body = deploymentMessage + "\\n\\n[JiraBot] Fact: " + getRandomCatFact();

        String uri = context.getBean("baseJiraURL") + "/rest/api/2/issue/" + ticketNumber + "/comment";
        HttpHeaders requestHeaders = cookie.getRequestHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<String>("{\"body\":\"" + body + "\"}", requestHeaders);

        try {
            cookie.getRestTemplate().exchange(uri, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e){
            e.printStackTrace();
            requestEntity = new HttpEntity<String>("{\"body\":\"" + langFile.getBean("successfulDeploymentStage") + "\"}", requestHeaders);
            cookie.getRestTemplate().exchange(uri, HttpMethod.POST, requestEntity, String.class);
        }
    }

    private String getJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }

}
