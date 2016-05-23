package web.crawler.crawling;

import java.util.ArrayList;
import java.util.List;
import web.crawler.gui.MainGraphics;


/**
 * The Crawler class holds a queue of links that need to be scanned by the 
 * CrawlerLeg. When a CrawlerLeg finds at least one of the search queries on the
 * page it was assigned, the Crawler class will add an InformationPackage 
 * containing the CrawlerLeg's results to the CrawlerManager.
 * 
 * @see web.crawler.crawling.CrawlerManager
 * @see web.craw.er.crawling.InformationPackage
 *
 * @author Jordan Hartwick
 * May 16, 2016
 */
public class Crawler implements Runnable {


    /** Contains the links that need to be scanned. */
    private List<String> linkQueue;


    /** The search queries to look for. */
    private String[] queries;


    /** The first link to scan. */
    private String startLink;


    /** The user agent for the CrawlerLegs. */
    private String userAgent;


    /** The amount of links to scan. */
    private int amountOfLinks;


    /**
     * Constructor method for the Crawler class.
     *
     * @param queries       The search queries.
     * @param startLink     The starting link for this crawler.
     * @param userAgent     The user agent for this crawler.
     * @param a             The amount of links this crawler should scan.
     */
    public Crawler(String[] queries, String startLink, String userAgent, int a) {
        this.queries        = queries;
        this.startLink      = startLink;
        this.userAgent      = userAgent;
        this.amountOfLinks  = a;

        linkQueue       = new ArrayList<>();
    }


    /**
     * Returns a link to a page that has not been scanned.
     *
     * @return  A link to a page that has not been scanned.
     */
    private String getNextLink() {
        String nextLink;

        do
        {
            nextLink = linkQueue.remove(0);
        } while(CrawlerManager.checkForScannedLink(nextLink));
        CrawlerManager.addScannedLink(nextLink);

        return nextLink;
    }


    /**
     * Creates a crawler leg and assigns it a link to search. If the crawler leg
     * found queries on the page specified by the link, then add an
     * InformationPackage to the CrawlerManager. 
     * 
     * @param link  The link to scan.
     * 
     * @see web.crawler.crawling.CrawlerManager
     * @see web.crawler.crawling.InformationPackage
     */
    private void search(String link) {
        String linkToScan;

        int amountScanned = 0;

        while(amountScanned < amountOfLinks && !Thread.interrupted()) {
            if(linkQueue.isEmpty()) {
                linkToScan = link;
                CrawlerManager.addScannedLink(link);
            } else {
                linkToScan = getNextLink();
            }

            CrawlerLeg cl = new CrawlerLeg(queries, userAgent);

            if(cl.crawl(linkToScan)) {    
                CrawlerManager.addInfoPackage(
                                    new InformationPackage(linkToScan, 
                                                           queries, 
                                                           cl.getAllAmounts()));
            }

            linkQueue.addAll(cl.getLinks());
            amountScanned++;
        }
    }


    /*
        Starts the scanning process and displays a message in the console area 
        telling the user that a web crawler has finished.
    */
    @Override
    public void run() {
        search(startLink);
        
        MainGraphics.updateConsoleArea("Web Crawler Finished");
    }
}