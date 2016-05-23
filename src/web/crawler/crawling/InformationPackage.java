package web.crawler.crawling;

import java.util.Arrays;


/**
 * The InformationPackage contains a link to a site, the search queries,
 * and the occurrences of each search query on the site. 
 *
 * @author Jordan Hartwick
 * May 17, 2016
 */
public class InformationPackage {

    
    /** The link this InformationPackage's data is related to. */
    private String link;
    
    
    /** The search queries. */
    private String[] searchQueries;
    
    
    /** The amount of occurrences for each search query. */
    private int[] searchQueryOccurrences;
    
    
    /**
     * Constructor method for the InformationPackage creates a new instance of 
     * an InformationPackage. 
     * 
     * @param link                      The link for this InformationPackage.
     * @param searchQueries             The search queries.
     * @param searchQueryOccurrences    The occurrences for each search query.
     */
    public InformationPackage(String link, String[] searchQueries, int[] searchQueryOccurrences) {
        this.link                   = link;
        this.searchQueries          = searchQueries;
        this.searchQueryOccurrences = searchQueryOccurrences;
    }

    
    /**
     * Returns the link for this InformationPackage.
     * 
     * @return  The link for this InformationPackage.
     */
    public String getLink() {
        return link;
    }

    
    /**
     * Returns the search queries for this InformationPackage.
     * 
     * @return  The search queries for this InformationPackage.
     */
    public String[] getSearchQueries() {
        return searchQueries;
    }

    
    /**
     * Returns the amount of times each search query occurs in this
     * InformationPackage.
     * 
     * @return  The amount of times each search query occurs in this 
     *          InformationPackage.
     */
    public int[] getSearchQueryOccurrences() {
        return searchQueryOccurrences;
    }
 
    
    /*
        Returns a string representing this InformationPackages data.
    */
    @Override
    public String toString() {
        String searchQueryData = "";
        for(int i = 0; i < searchQueries.length; i++) {
            searchQueryData += searchQueries[i] + ": " + searchQueryOccurrences[i] + "\n";
        }
        
        return "------------------------------------\n"
                + "Link: " + getLink() + "\n"
                + searchQueryData
                + "------------------------------------";
    }
}