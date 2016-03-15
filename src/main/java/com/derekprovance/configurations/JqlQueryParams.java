package com.derekprovance.configurations;

import com.derekprovance.apiObject.JqlQuery;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class JqlQueryParams {

    private static JqlQueryParams instance = null;
    private static String jqlQuery = null;

    protected JqlQueryParams() {

    }

    public static JqlQueryParams getInstance() {
        if(instance == null) {
            instance = new JqlQueryParams();

            ApplicationContext context = new ClassPathXmlApplicationContext("AppConfig.xml");

            JiraCookie cookie = JiraCookie.getInstance();

            String uri = context.getBean("baseJiraURL") + "rest/api/2/filter/" + context.getBean("ticketPool");
            ResponseEntity rssResponse = cookie.getRestTemplate().exchange(uri, HttpMethod.GET, cookie.getRequestEntity(), JqlQuery.class);
            JqlQuery rss = (JqlQuery) rssResponse.getBody();
            jqlQuery = rss.getJql();
        }
        return instance;
    }

    public String getJqlQuery() {
        return jqlQuery;
    }
}
