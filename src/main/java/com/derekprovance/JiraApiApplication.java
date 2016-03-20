package com.derekprovance;

import com.derekprovance.apiObject.issues.JiraIssueFields;
import com.derekprovance.services.CurrentTickets;
import java.net.URISyntaxException;

public class JiraApiApplication {

    public static void main(String[] args) throws URISyntaxException {
		//TODO - implement actual args
        if(args.length < 2) {
            System.out.println("Error: Parameters are <ticket> <war info> <dep notes>");
            return;
        }

		String ticketNumber = args[0];
		String deploymentWar = args[1];
		String deploymentNotes = args[2];

		CurrentTickets tickets = new CurrentTickets();

        JiraIssueFields newInfo = new JiraIssueFields(deploymentWar, deploymentNotes);
        tickets.updateDeploymentInfoSpecificTicket(ticketNumber, newInfo, tickets.getSpecificTicket(ticketNumber).getFields());
    }

}
