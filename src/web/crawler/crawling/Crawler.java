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


    /** Contains the search queries. */
    private String[] queries;


    /** Contains the first link to scan. */
    private String startLink;


    /** Contains this crawlers user agent. */
    private String userAgent;


    /**
     * Creates a new instance of the Crawler class.
     *
     * @param queries       The queries to search for.
     * @param startLink     The first link to scan.
     * @param userAgent     The user agent for this crawler to use.
     */
    public Crawler(String[] queries, String startLink, String userAgent) {
        this.queries    = queries;
        this.startLink  = startLink;
        this.userAgent  = userAgent;
    }


    @Override
    public void run() {

    }
}