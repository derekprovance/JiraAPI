package com.derekprovance;

import com.derekprovance.apiObject.issues.JiraIssueFields;
import com.derekprovance.services.CurrentTickets;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class JiraApiApplication {

    public static void main(String[] args) throws URISyntaxException {
        CurrentTickets tickets = new CurrentTickets();
        JiraIssueFields newInfo = null;
        switch(args.length) {
            case 3:
                newInfo = new JiraIssueFields(args[2], null);
                break;
            case 4:
                newInfo = new JiraIssueFields(args[2], args[3]);
                break;
            default:
                System.out.println("Error: Parameters are <ticket> <bool: deploy_staging> <war info> <depl notes>");
                System.out.println("Note: New War Format (<Project Name> <Tag Number>)");
                System.exit(0);
        }

        tickets.updateDeploymentInfoSpecificTicket(args[0], Boolean.valueOf(args[1]), newInfo, tickets.getSpecificTicket(args[0]).getFields());
    }
}
