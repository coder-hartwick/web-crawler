package web.crawler.crawling;

import java.util.HashSet;


/**
 * Manages the Crawlers that are active. Used to relay information that the GUI
 * needs to display to the user. This information includes the amount of active
 * crawlers, amount of links that have been scanned, amount of links containing
 * the search query, and a list of links that contain the search query.
 *
 * @author Jordan Hartwick
 * May 16, 2016
 */
public class CrawlerManager {


    /** Contains the links that have already been scanned. */
    private static HashSet<String> linksScanned = new HashSet<>();


    /** Contains the links that contain the search query. */
    private static HashSet<String> validLinks   = new HashSet<>();


    /** All of the crawler threads belong to this thread group. */
    private ThreadGroup crawlerThreadGroup;


    /** Contains all of the crawler threads. */
    private Thread[]    crawlerThreads;


    /**
     * Creates a new instance of the CrawlerManager class.
     *
     * @param queries           The search queries (contains one or more query).
     * @param startingLinks     The first links to scan.
     * @param amountToScan      The maximum amount of links to scan.
     */
    public CrawlerManager(String[] queries, String[] startingLinks, int amountToScan) {

    }


    /**
     * Starts the crawlers.
     */
    public void startCrawlers() {
        for(Thread t : crawlerThreads) t.start();
    }


    /**
     * Stops the active crawlers.
     */
    public void stopCrawlers() {
        crawlerThreadGroup.interrupt();
    }


    /**
     * Returns the amount of active crawlers.
     *
     * @return  The amount of active crawlers.
     */
    public int activeCrawlers() {
        return crawlerThreadGroup.activeCount();
    }


    /**
     * Add a link to the links that have been scanned so it's not scanned again.
     *
     * @param link  The link that was scanned.
     */
    public static void addScannedLink(String link) {
        linksScanned.add(link);
    }


    /**
     * Adds a link to the links that contain the search query so it is not
     * scanned again.
     *
     * @param link  The link that contains the search query.
     */
    public static void addValidLink(String link) {
        validLinks.add(link);
    }


    /**
     * Returns whether or not a link has already been scanned.
     *
     * @param link  The link to check for.
     * @return      true if the link is in the list, false if not.
     */
    public static boolean checkForScannedLink(String link) {
        return linksScanned.contains(link);
    }

}