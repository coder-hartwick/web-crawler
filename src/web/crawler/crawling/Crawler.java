package web.crawler.crawling;

import java.util.ArrayList;
import java.util.List;


/**
 * The Crawler manages a Queue of links needing to be scanned for the specified
 * search query.
 *
 * @author Jordan Hartwick
 * May 16, 2016
 */
public class Crawler implements Runnable {


    /** Contains the links that need to be scanned. */
    private List<String> linkQueue = new ArrayList<>();


    /**
     * Contains information about the website where the search query was found.
     */
    private List<InformationPackage> infoPackages = new ArrayList<>();

    
    /** Contains the search queries. */
    private String[] queries;


    /** Contains the first link to scan. */
    private String startLink;


    /** Contains this crawlers user agent. */
    private String userAgent;
    
    
    /** The amount of links to scan. */
    private int amountOfLinks;


    /**
     * Creates a new instance of the Crawler class.
     *
     * @param queries       The queries to search for.
     * @param startLink     The first link to scan.
     * @param userAgent     The user agent for this crawler to use.
     */
    public Crawler(String[] queries, String startLink, String userAgent, int a) {
        this.queries        = queries;
        this.startLink      = startLink;
        this.userAgent      = userAgent;
        this.amountOfLinks  = a;
    }


    /**
     * Returns the link that should be scanned next.
     *
     * @return  The next link to scan.
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


    private void search(String link) {
        String linkToScan;

        int amountScanned = 0;
        
        while (amountScanned < amountOfLinks) {
            if (linkQueue.isEmpty()) {
                linkToScan = link;
                CrawlerManager.addScannedLink(link);
            } else {
                linkToScan = getNextLink();
            }

            CrawlerLeg cl = new CrawlerLeg(queries, userAgent);

            if (cl.crawl(linkToScan)) {
                CrawlerManager.addValidLink(linkToScan);
                int[] searchQueryOccurrences = cl.getSearchQueryOccurrences();
                infoPackages.add(new InformationPackage(linkToScan, queries, searchQueryOccurrences));
            }
            
            linkQueue.addAll(cl.getLinks());
            amountScanned++;
        }
    }


    @Override
    public void run() {
        int amountScanned = 0;
        
        search(startLink);
        
        CrawlerManager.addAllInformationPackages(infoPackages);
        
        System.out.println("Web Crawler Finished");
    }
}