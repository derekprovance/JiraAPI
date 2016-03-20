package com.derekprovance.services;

import com.derekprovance.apiObject.issues.JiraIssue;
import com.derekprovance.apiObject.JqlQuery;
import com.derekprovance.apiObject.issues.JiraIssueFields;
import com.derekprovance.apiObject.requests.SearchRequest;
import com.derekprovance.configurations.JiraCookie;
import com.derekprovance.configurations.JqlQueryParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;

public class CurrentTickets {

    private ApplicationContext context = new ClassPathXmlApplicationContext("AppConfig.xml");
    private ApplicationContext langFile = new ClassPathXmlApplicationContext("language.xml");
    private JiraCookie cookie = JiraCookie.getInstance();

    public void getCurrentTickets() {
        String jqlQuery = JqlQueryParams.getInstance().getJqlQuery();

        HttpEntity requestEntity = new HttpEntity(preparePostCurrentTicketsQuery(jqlQuery, 0, 15), cookie.getRequestHeaders());

        //TODO - Post request is not working, next steps will be to try to fix this
        String uri = context.getBean("baseJiraURL") + "rest/api/2/search";
        ResponseEntity rssResponse = cookie.getRestTemplate().exchange(uri, HttpMethod.POST, requestEntity, JqlQuery.class);
        JqlQuery rss = (JqlQuery) rssResponse.getBody();
        System.out.println(getJson(rss));
    }

    private SearchRequest preparePostCurrentTicketsQuery(String jql, int min, int max) {
        SearchRequest request = new SearchRequest();
        request.setJql(jql);
        request.setStartsAt(min);
        request.setMaxResults(max);
        return request;
    }

    public JiraIssue getSpecificTicket(String ticketNumber) {
        String uri = context.getBean("baseJiraURL") + "/rest/api/2/issue/" + ticketNumber;
        ResponseEntity rssResponse = cookie.getRestTemplate().exchange(uri, HttpMethod.GET, cookie.getRequestEntity(), JiraIssue.class);
        return (JiraIssue) rssResponse.getBody();
    }

    public void updateDeploymentInfoSpecificTicket(String ticketNumber, JiraIssueFields newInfo, JiraIssueFields existingInfo) {
        newInfo.setDeployment_info(createDeploymentInfoString(newInfo, existingInfo));

        String uri = context.getBean("baseJiraURL") + "/rest/api/2/issue/" + ticketNumber;
        HttpHeaders requestHeaders = cookie.getRequestHeaders();
        requestHeaders.add("Content-Type", "application/json");
        HttpEntity requestEntity = new HttpEntity("{\"fields\":" + getJson(newInfo) + "}", requestHeaders);

        ResponseEntity request = cookie.getRestTemplate().exchange(uri, HttpMethod.PUT, requestEntity, String.class);
        if(request.getStatusCode() == HttpStatus.NO_CONTENT) {
            writeCommentDeploymentSuccessful(ticketNumber);
        }
    }

    private String createDeploymentInfoString(JiraIssueFields newInfo, JiraIssueFields existingInfo) {
        if(existingInfo.getDeployment_info() == null) {
            return newInfo.getDeployment_info();
        }

        return existingInfo.getDeployment_info() + "\n" + newInfo.getDeployment_info();
    }

    private void writeCommentDeploymentSuccessful(String ticketNumber) {
        String uri = context.getBean("baseJiraURL") + "/rest/api/2/issue/" + ticketNumber + "/comment";
        HttpHeaders requestHeaders = cookie.getRequestHeaders();
        HttpEntity requestEntity = new HttpEntity("{\"body\":\"" + langFile.getBean("successfulDeployment") + "\"}", requestHeaders);

        try {
            ResponseEntity request = cookie.getRestTemplate().exchange(uri, HttpMethod.POST, requestEntity, String.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    private String getJson(Object obj) {
        //Note - It's bad practice to include notes in functions, but this is a learning project
        //Could override the toString class for each object. Duplicate code though? Perhaps a
        //Utility Class and each object inherits that class. Performance implications could also arise
        //Using the objectmapper constantly for toString actions.
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }

}
