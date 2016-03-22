package com.derekprovance.services;

import java.util.ArrayList;
import java.util.Arrays;

public class DeploymentWarGenerator {

    public static String createWarDeploymentString(String newDepWar, String existingDepWar) {
        existingDepWar = notAvailableCheck(existingDepWar);
        ArrayList<String> existingWars = splitWarString(existingDepWar);
        ArrayList<String> newWars = splitWarString(newDepWar);

        for (int i=0; i<newWars.size(); i++) {
            String project = newWars.get(i).split(" ")[0];
            int index = existingWarContainsProject(existingWars, project);
            if(index != -1) {
                existingWars.set(index, newWars.get(i));
            } else {
                existingWars.add(newWars.get(i));
            }
        }

        return rebuildDeploymentWarString(existingWars);
    }

    private static String notAvailableCheck(String existingDepWar) {
        if(existingDepWar == null)
            return null;

        if(existingDepWar.toLowerCase().equals("n/a")) {
            existingDepWar = null;
        }

        return existingDepWar;
    }

    private static String rebuildDeploymentWarString(ArrayList<String> depWars) {
        String depWarsAssembly = "";
        if(depWars.size() < 1)
            return null;

        for(int i=0; i<depWars.size()-1; i++) {
            depWarsAssembly += depWars.get(i) + " && ";
        }
        depWarsAssembly += depWars.get(depWars.size()-1);

        return depWarsAssembly;
    }

    private static int existingWarContainsProject(ArrayList<String> existingWar, String project) {
        for(int i=0; i<existingWar.size(); i++) {
            if(existingWar.get(i).contains(project)) {
                return i;
            }
        }

        return -1;
    }

    private static ArrayList<String> splitWarString(String depWar) {
        ArrayList<String> existingSeperator = determineSeperatorUsed(depWar);
        ArrayList<String> seperatedString = new ArrayList<String>();

        if(existingSeperator.size() > 0) {
            seperatedString = new ArrayList<String>(Arrays.asList(depWar.split((String) determineSeperatorUsed(depWar).get(0))));
        } else if(depWar != null) {
            seperatedString.add(depWar);
        }

        return seperatedString;
    }

    private static ArrayList determineSeperatorUsed(String wars) {
        //TODO - find data store
        String[] separators = new String[]{" & ", " && ", " , ", " and "};

        ArrayList<String> foundSeparator = new ArrayList<>();
        if(wars != null) {
            for (String seperator : separators) {
                if (wars.toLowerCase().contains(seperator.toLowerCase()))
                    foundSeparator.add(seperator);
            }
        }

        return foundSeparator;
    }

}
