package com.derekprovance.services;

import com.derekprovance.apiObject.JqlQuery;
import com.derekprovance.configurations.JiraCookie;
import com.derekprovance.configurations.JqlQueryParams;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class CurrentTickets {

    public void getCurrentTickets() {
        //TODO - investigate if there is a better way to deal with all of these application contexts in the file
        ApplicationContext context = new ClassPathXmlApplicationContext("AppConfig.xml");
        String jqlQuery = JqlQueryParams.getInstance().getJqlQuery();

        JiraCookie cookie = JiraCookie.getInstance();

        //TODO - finish this post request
        String uri = context.getBean("baseJiraURL") + "rest/api/2/search";
        ResponseEntity rssResponse = cookie.getRestTemplate().exchange(uri, HttpMethod.POST, cookie.getRequestEntity(), JqlQuery.class);
        JqlQuery rss = (JqlQuery) rssResponse.getBody();
    }

}
