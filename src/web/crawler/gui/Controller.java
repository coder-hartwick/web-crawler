package web.crawler.gui;


import errorreport.ErrorReport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
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
     * Displays a dialog so the user can enter their desired settings for the 
     * web crawler and then starts the crawling if the user did not cancel the 
     * dialog. It also displays some output to the user.
     */
    public void setupWebCrawler() {
        CrawlerSetupDialog csd = new CrawlerSetupDialog(null, true);
        
        Object[] data = csd.getData();
        if(data[0] != null) {
            MainGraphics.updateConsoleArea("---------------------------------------------");
            MainGraphics.updateConsoleArea("Setup Web Crawler Info");
            MainGraphics.updateConsoleArea("Sites: "+Arrays.toString((String[])data[0]));
            MainGraphics.updateConsoleArea("Search Queries: "+Arrays.toString((String[])data[1]));
            MainGraphics.updateConsoleArea("Amount of Links to Collect: "+data[2]);
            MainGraphics.updateConsoleArea("---------------------------------------------");
            
            crawlerManager = new CrawlerManager((String[])data[1], 
                                                (String[])data[0], 
                                                (int)data[2],
                                                listModel);
            
            Thread thread = new Thread(crawlerManager);
            thread.start();
        }           
    }
    
    
    /**
     * Stops the web crawling operations.
     */
    public void stopWebCrawler() {
        crawlerManager.stopCrawlers();
        JOptionPane.showMessageDialog(null, 
                                        "Crawler Stopped", 
                                        "Crawling Operations", 
                                        JOptionPane.INFORMATION_MESSAGE);
    }
}