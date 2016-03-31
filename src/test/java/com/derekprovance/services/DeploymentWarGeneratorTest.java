package com.derekprovance.services;

import com.derekprovance.TicketStatus;
import org.junit.Test;

import static com.derekprovance.services.DeploymentWarGenerator.createWarDeploymentString;
import static org.junit.Assert.*;

public class DeploymentWarGeneratorTest {

    @Test
    public void createWarDeploymentStringTest() throws Exception {
        String project1 = "RandomProject 34343432_2322_34232";
        String project2 = "DweqwProjcet 3243242_234234234";
        String project3 = "Qefweffwee 4343434_343434343";
        String malFormed1 = "23423423_324234234 [Project1]";

        String replaceProject1 = "RandomProject 4444444_444444444";
        String replaceProject2 = "DweqwProjcet 555555_55555555";
        String[] seperator1 = new String[] {" & ", " && ", "and"};
        String defaultSeperator = " && ";

        assertEquals(project1, createWarDeploymentString(project1, null));
        assertEquals(project2, createWarDeploymentString(project2, project2));
        assertEquals(project1 + defaultSeperator + project2, createWarDeploymentString(project2, project1));
        assertEquals(replaceProject2, createWarDeploymentString(replaceProject2, project2));
        assertEquals(replaceProject1, createWarDeploymentString(replaceProject1, project1));

        assertEquals(project1 + defaultSeperator + replaceProject2 + defaultSeperator + project3,
                createWarDeploymentString(replaceProject2, project1 + defaultSeperator + project2 + defaultSeperator + project3));
    }

    @Test
    public void determineDeploymentStatusTest() throws Exception {
        assertEquals(TicketStatus.QA, DeploymentWarGenerator.determineDeploymentStatus(""));
        assertEquals(TicketStatus.QA, DeploymentWarGenerator.determineDeploymentStatus("n/a"));
        assertEquals(TicketStatus.STAGING, DeploymentWarGenerator.determineDeploymentStatus("TestProject 2343_323423234"));
        assertEquals(TicketStatus.STAGING, DeploymentWarGenerator.determineDeploymentStatus("TestProject 2343_323423234 && Test2 34334_2342342342"));
    }

}