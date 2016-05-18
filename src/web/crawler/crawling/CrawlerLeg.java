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


    /** Contains the links that are on the page this CrawlerLeg scanned. */
    private List<String> links = new ArrayList<>();

    
    /** Contains the queries to search for. */
    private String[] queries;

    
    private int[] occurrencesOfWords;

    /**
     * Contains the user agent this CrawlerLeg should when connecting to a site.
     */
    private String userAgent;


    /**
     * Constructor method for the CrawlerLeg creates a new instance of the
     * CrawlerLeg class.
     *
     * @param queries   The search queries.
     * @param userAgent The user agent to use when connecting to a site.
     */
    public CrawlerLeg(String[] queries, String userAgent) {
        this.queries    = queries;
        this.userAgent  = userAgent;
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
        System.out.println("Crawling page: "+link);

        try {            
            
            if(link.isEmpty()) {
                return false;
            }
            
            // Try and get the document.
            Connection con = Jsoup.connect(link).userAgent(userAgent);            
            Document doc = con.get();
            if(con.response().statusCode() != 200) {
                return false;
            }                        
            
            searchDocument(doc);

            // Get the links on the page.
            Elements linksOnPage = doc.select("a[href]");
            for(Element e : linksOnPage) {
                links.add(e.absUrl("href"));
            }            
                        
            // Check if at least one of the queries occur on the page.
            for(int i : occurrencesOfWords) {
                if(i > 0) return true;
            }
            
            return false;
        } catch (IOException err) {            
            ErrorReport.createErrorReport(err);
            return false;
        }
    }


    /**
     * Searches the specified document for the specified search queries. 
     * 
     * @param doc   The document to search.
     */
    private void searchDocument(Document doc) {
        occurrencesOfWords = new int[queries.length];
        
        if(doc != null && doc.body() != null) {
            
            // Find each occurrence of the word.
            for(int i = 0; i < queries.length; i++) {
                occurrencesOfWords[i] = 0;

                Pattern pattern = Pattern.compile("\\b" + queries[i] + "\\b");
                Matcher matcher = pattern.matcher(doc.body().text().toLowerCase());
                while(matcher.find()) {
                    occurrencesOfWords[i]++;
                }
            }
        } else {
            for(int i = 0; i < queries.length; i++) {
                occurrencesOfWords[i] = 0;
            }
        }
    }
    
    
    /**
     * Returns the occurrences of each search query. 
     * 
     * @return  The occurrences of each search query.
     */
    public int[] getSearchQueryOccurrences() {
        return occurrencesOfWords;
    }


    /**
     * Returns the links this CrawlerLeg found.
     *
     * @return  The links this CrawlerLeg found.
     */
    public List<String> getLinks() {
        return links;
    }
}