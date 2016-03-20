package com.derekprovance;

import com.derekprovance.apiObject.issues.JiraIssueFields;
import com.derekprovance.services.CurrentTickets;
import java.net.URISyntaxException;

public class JiraApiApplication {

    public static void main(String[] args) throws URISyntaxException {
		//TODO - implement actual args
		String ticketNumber = "PS-23020";
		String deploymentWar = "HillsVetUS RandomGarbage";
		String deploymentNotes = "This is the deployment description\nThis is a new line";

		CurrentTickets tickets = new CurrentTickets();
        JiraIssueFields newInfo = new JiraIssueFields(deploymentWar, deploymentNotes);
        tickets.updateDeploymentInfoSpecificTicket(ticketNumber, newInfo, tickets.getSpecificTicket(ticketNumber).getFields());
    }

}
