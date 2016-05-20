package web.crawler.crawling;

import errorreport.ErrorReport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Manages the Crawlers that are active. Used to relay information that the GUI
 * needs to display to the user. This information includes the amount of active
 * crawlers, amount of links that have been scanned, amount of links containing
 * the search query, and a list of links that contain the search query.
 *
 * @author Jordan Hartwick
 * May 16, 2016
 */
public class CrawlerManager implements Runnable {


    /** Contains the links that have already been scanned. */
    private static Set<String> linksScanned = new HashSet<>();


    /** Contains the links that contain the search query. */
    private static Set<String> validLinks = new HashSet<>();


    /** Contains all of the information packages. */
    private static List<InformationPackage> infoPackages = new ArrayList<>();


    /** All of the crawler threads belong to this thread group. */
    private ThreadGroup crawlerThreadGroup;


    /** Contains all of the crawler threads. */
    private Thread[] crawlerThreads;


    /** The UserAgentAssigner to assign user agents to the crawler classes. */
    private UserAgentAssigner uas;


    /**
     * Creates a new instance of the CrawlerManager class.
     *
     * @param queries           The search queries (contains one or more query).
     * @param startingLinks     The first links to scan.
     * @param amountToScan      The maximum amount of links to scan.
     */
    public CrawlerManager(String[] queries, String[] startingLinks, int amountToScan) {
        uas = new UserAgentAssigner("src/agents.txt", 7);

        crawlerThreadGroup = new ThreadGroup("thread_group");

        crawlerThreads = new Thread[startingLinks.length];
        for(int i = 0; i < startingLinks.length; i++) {
            crawlerThreads[i] = new Thread(crawlerThreadGroup,
                                            new Crawler(queries, startingLinks[i],
                                                        uas.getUserAgent(),
                                                        amountToScan));
        }
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


    /**
     * Adds a collection of InformationPackages to the infoPackages list.
     *
     * @param c The collection to add to the list.
     */
    public static void addAllInformationPackages(Collection<? extends InformationPackage> c) {
        infoPackages.addAll(c);
    }


    /**
     * Returns the list containing the InformationPackages.
     *
     * @return  The list containing the InformationPackages.
     */
    public static List<InformationPackage> getAllInformationPackages() {
        return infoPackages;
    }


    @Override
    public void run() {
        startCrawlers();
        while(activeCrawlers() > 0) {
            System.out.println("Active Threads: "+activeCrawlers());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException err) {
                ErrorReport.createErrorReport(err);
            }
        }

        for(InformationPackage p : infoPackages) {
            System.out.println(p.toString());
        }
    }
}