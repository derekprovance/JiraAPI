package com.derekprovance.services;

import com.derekprovance.apiObject.JqlQuery;
import com.derekprovance.apiObject.requests.SearchRequest;
import com.derekprovance.configurations.JiraCookie;
import com.derekprovance.configurations.JqlQueryParams;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class CurrentTickets {

    public void getCurrentTickets() {
        //TODO - investigate if there is a better way to deal with all of these application contexts in the file
        ApplicationContext context = new ClassPathXmlApplicationContext("AppConfig.xml");
        String jqlQuery = JqlQueryParams.getInstance().getJqlQuery();

        JiraCookie cookie = JiraCookie.getInstance();


        HttpEntity requestEntity = new HttpEntity(preparePostCurrentTicketsQuery(jqlQuery, 0, 15), cookie.getRequestHeaders());

        //TODO - Post request is not working, next steps will be to try to fix this
        String uri = context.getBean("baseJiraURL") + "rest/api/2/search";
        ResponseEntity rssResponse = cookie.getRestTemplate().exchange(uri, HttpMethod.POST, requestEntity, JqlQuery.class);
        JqlQuery rss = (JqlQuery) rssResponse.getBody();
        System.out.println(rss);
    }

    public void getSpecificTicket() {
        //TODO - implement this function
    }

    public SearchRequest preparePostCurrentTicketsQuery(String jql, int min, int max) {
        SearchRequest request = new SearchRequest();
        request.setJql(jql);
        request.setStartsAt(min);
        request.setMaxResults(max);
        return request;
    }

}
