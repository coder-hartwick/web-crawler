package testing;

import web.crawler.crawling.CrawlerManager;


/**
 * This will test the web crawler bot.
 *
 * @author Jordan Hartwick
 * May 17, 2016
 */
public class WebCrawlUtilitiesTester {

    
    /**
     * Main method for the web crawler testing bot.
     * 
     * @param args  Command line arguments.
     */
    public static void main(String[] args) {        
        String[] queries = {"number","school","java","password"};
        String[] websites = {"https://www.random.org/","http://www.randomthingstodo.com/","http://www.huffingtonpost.com/"};
        int amount = 50;
        new Thread(new CrawlerManager(queries, websites, amount)).start();
    }
}
