package com.derekprovance;

import com.derekprovance.configurations.JqlQueryParams;
import com.derekprovance.services.CurrentTickets;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.net.URISyntaxException;

@SpringBootApplication
public class JiraApiApplication {

    public static void main(String[] args) throws URISyntaxException {
		SpringApplication.run(JiraApiApplication.class, args);

		CurrentTickets tickets = new CurrentTickets();
		tickets.getCurrentTickets();
	}

}
