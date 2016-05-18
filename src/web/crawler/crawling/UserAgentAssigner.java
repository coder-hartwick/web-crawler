package web.crawler.crawling;

import errorreport.ErrorReport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * This class returns one of the available user agent strings located in a file.
 * A valid list will separate the user agent strings by the regular expression
 * "\[::\]" which is equal to the non-regex string "[::]".
 *
 * @author Jordan Hartwick
 * May 16, 2016
 */
public class UserAgentAssigner {


    /** Contains user agents to assign to the crawlers. */
    private List<String> userAgents = new ArrayList<>();


    /** Contains the user agents that have been removed. */
    private List<String> removedAgents = new ArrayList<>();


    /**
     * Creates a new instance of the UserAgentAssigner class.
     *
     * @param filePath      The path to the file containing the user
     *                      agents.
     * @param amountToLoad  The amount of user agents to load.
     */
    public UserAgentAssigner(String filePath, int amountToLoad) {
        try {
            loadUserAgents(filePath, amountToLoad);
        } catch (IOException err) {            
            ErrorReport.createErrorReport(err);
        }
    }


    /**
     * Adds a specified amount of user agents to the list. The user agents added
     * are chosen at random and are the ones that will be assigned to the
     * crawlers.
     *
     * @param filePath      The path to the user agents list.
     * @param amountToLoad  The amount of user agents to load.
     * @throws IOException
     */
    private void loadUserAgents(String filePath, int amountToLoad) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line, content = "";

        while((line = br.readLine()) != null) {
            content += line;
        }

        br.close();

        List<String> temp = new ArrayList<>(Arrays.asList(content.split("\\[::\\]")));

        Random random = new Random();
        for(int i = 0; i < amountToLoad; i++) {
            userAgents.add(temp.remove(random.nextInt(temp.size())));
        }
    }


    /**
     * Returns a random user agent from the list of user agents.
     *
     * @return  A random user agent from the list of user agents.
     */
    public String getUserAgent() {
        if(userAgents.isEmpty()) {
            userAgents = new ArrayList<>(removedAgents);
        }

        String userAgent = userAgents.get(new Random().nextInt(userAgents.size()));
        userAgents.remove(userAgent);
        removedAgents.add(userAgent);
        return userAgent;
    }
}
