package web.crawler.gui;


import javax.swing.DefaultListModel;
import web.crawler.crawling.CrawlerManager;


/**
 * Contains actions to handle input events from the MainGraphics class.
 *
 * @author Jordan Hartwick
 * May 18, 2016
 */
public class Controller {

    
    /** The CrawlerManager that this class will retrieve information from. */
    private CrawlerManager crawlerManager;
    
    
    /** 
     * This list model is used to update the values in the valid websites list.
     */
    private DefaultListModel listModel;
    
    
    /**
     * The constructor method for the Controller class.
     * 
     * @param listModel     The list model to update when crawling websites.
     */
    public Controller(DefaultListModel listModel) {
        this.listModel = listModel;
    }
    
    
    /**
     * What this method does is still being worked on. However, it will still 
     * display the dialog that is required for web crawler setup.
     */
    public void setupWebCrawler() {
        new CrawlerSetupDialog(null, true);
    }
    
    
    /**
     * Stops the web crawling operations.
     */
    public void stopWebCrawler() {
//        crawlerManager.stopCrawlers();
    }
}
