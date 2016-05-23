package web.crawler.crawling;

import errorreport.ErrorReport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * The CrawlerLeg will scan a web page for the search queries specified and also
 * gather links to add to the this CrawlerLeg's Crawler's link queue. The
 * CrawlerLeg will only scan one page.
 *
 * @author Jordan Hartwick
 * May 17, 2016
 */
public class CrawlerLeg {


    /**
     * Contains the links found on the page this crawler leg scanned.
     */
    private List<String> links;


    /**
     * Contains the search queries.
     */
    private String[] queries;


    /**
     * Contains the occurrences of each search query on the page that was
     * scanned.
     */
    private int[] amountFound;


    /**
     * The user agent for this crawler to use when connecting to a page.
     */
    private String userAgent;


    /**
     * Constructor method for the CrawlerLeg creates a new instance of the
     * CrawlerLeg class and assigns the queries and user agent.
     *
     * @param queries   The search queries.
     * @param userAgent The user agent to use when connecting to a site.
     */
    public CrawlerLeg(String[] queries, String userAgent) {
        this.queries = queries;
        this.userAgent = userAgent;

        this.links = new ArrayList<>();
    }


    /**
     * Connects to the web page using the specified user agent, gets the
     * document, and processes the document's content. It will first call a
     * method to get the instances of each search query in the document and then
     * gather links from the document. If the document does not contain any of
     * the search queries, it will return false; it will return true otherwise.
     * If the link is empty, it will return false. If the connection could not
     * receive the web page, it will return false.
     *
     * @param link  The link to the page to process.
     * @return      True if the page contains a search query one or more times.
     *              False if the link was empty, the web page could not be
     *              received, or if all search queries were not found.
     */
    public boolean crawl(String link) {
        try {

            if(link.isEmpty()) {
                return false;
            }

            Connection con = Jsoup.connect(link).userAgent(userAgent);
            Document doc = con.get();
            if(con.response().statusCode() != 200) {
                return false;
            }

            searchDocument(doc);

            Elements linksOnPage = doc.select("a[href]");
            for(Element e : linksOnPage) {
                links.add(e.absUrl("href"));
            }

            for(int i : amountFound) {
                if(i > 0) return true;
            }

            return false;
        } catch (IOException err) {
            ErrorReport.createErrorReport(err);
            return false;
        }
    }


    /**
     * Searches the document for the specified search queries.
     *
     * @param doc   The document to search.
     */
    private void searchDocument(Document doc) {
        amountFound = new int[queries.length];

        if(doc != null && doc.body() != null) {
            for(int i = 0; i < queries.length; i++) {
                amountFound[i] = 0;

                Pattern pattern = Pattern.compile("\\b" + queries[i] + "\\b");
                Matcher matcher = pattern.matcher(doc.body().text().toLowerCase());
                while(matcher.find()) {
                    amountFound[i]++;
                }
            }
        } else {
            amountFound = new int[]{0,0,0,0};
        }
    }


    /**
     * Returns the occurrences of each search query.
     *
     * @return  The occurrences of each search query.
     */
    public int[] getAllAmounts() {
        return amountFound;
    }


    /**
     * Returns the links found on the page this crawler leg scanned.
     *
     * @return  The links found on the page this crawler leg scanned.
     */
    public List<String> getLinks() {
        return links;
    }
}