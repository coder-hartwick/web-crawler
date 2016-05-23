package web.crawler.crawling;

import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import web.crawler.gui.MainGraphics;


/**
 * The CrawlerManager is used to start and manage the Crawler threads, and add
 * InformationPackages to the InformationPackage list's list model.
 *
 * @see web.crawler.crawling.CrawlerLeg
 * @see web.crawler.crawling.InformationPackage
 *
 * @author Jordan Hartwick
 * May 16, 2016
 */
public class CrawlerManager extends Thread {


    /**
     * Contains the links that have already been scanned.
     */
    private static Set<String> linksScanned = new HashSet<>();


    /**
     * All of the crawler threads belong to this thread group.
     */
    private ThreadGroup crawlerThreadGroup;


    /**
     * Contains all of the crawler threads.
     */
    private Thread[] crawlerThreads;


    /**
     * The list model to add InformationPackages to.
     */
    private static DefaultListModel listModel;


    /**
     * The UserAgentAssigner that assigns user agents to the Crawlers.
     */
    private UserAgentAssigner uas;


    /**
     * Creates a new instance of the CrawlerManager class.
     *
     * @param queries           The search queries (contains one or more query).
     * @param startingLinks     The first links to scan.
     * @param amountToScan      The maximum amount of links to scan.
     * @param model             The list model to add InformationPackages to.
     */
    public CrawlerManager(String[] queries, String[] startingLinks, int amountToScan, ListModel model) {
        listModel = (DefaultListModel)model;

        uas = new UserAgentAssigner("src/agents.txt", 7);

        crawlerThreadGroup = new ThreadGroup("thread_group");

        crawlerThreads = new Thread[startingLinks.length];
        for(int i = 0; i < startingLinks.length; i++) {
            crawlerThreads[i] = new Thread(crawlerThreadGroup,
                                           new Crawler(queries,
                                                       startingLinks[i],
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
        MainGraphics.updateConsoleArea("Visited site "+link);
    }


    /**
     * Adds an InformationPackage to the list model so it is displayed to the
     * user.
     *
     * @param infoPackage   The InformationPackage to add to the list model.
     */
    public static void addInfoPackage(InformationPackage infoPackage) {
        listModel.addElement(infoPackage);
    }


    /**
     * Returns true if a link has been scanned. If not, returns false.
     *
     * @param link  The link to check.
     * @return      true id a link has been scanned. If not, returns false.
     */
    public static boolean checkForScannedLink(String link) {
        return linksScanned.contains(link);
    }


    /*
        Starts the crawling operation.
    */
    @Override
    public void run() {
        startCrawlers();
    }
}