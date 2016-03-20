package com.derekprovance.configurations;

import com.derekprovance.apiObject.session.SessionLogin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;


public class JiraCookie {

    private static JiraCookie instance = null;

    private static RestTemplate restTemplate = null;
    private static AuthUser user = null;
    private static SessionLogin session = null;
    private static HttpHeaders requestHeaders = null;
    private static HttpEntity requestEntity = null;

    protected JiraCookie() {}

    public static JiraCookie getInstance() {
        ApplicationContext context = new ClassPathXmlApplicationContext("AppConfig.xml");

        if(instance == null) {
            instance = new JiraCookie();
            restTemplate = new RestTemplate();
            user = context.getBean("user", AuthUser.class);
            session = restTemplate.postForObject(context.getBean("baseJiraURL") + "rest/auth/1/session", user, SessionLogin.class);

            requestHeaders = new HttpHeaders();
            requestHeaders.add("Cookie", "JSESSIONID=" + session.getSession().getValue());
            requestEntity = new HttpEntity(null, requestHeaders);
        }
        return instance;
    }

    public static void logOut() {
        //TODO - implement this function
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public HttpEntity getRequestEntity() {
        return requestEntity;
    }

    public HttpHeaders getRequestHeaders() {
        return requestHeaders;
    }

}
