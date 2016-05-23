package web.crawler.gui;


import errorreport.ErrorReport;
import java.util.Arrays;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import web.crawler.crawling.CrawlerManager;


/**
 * The Controller is the bridge between the GUI and the logic of this
 * application. It helps handle actions made by the user when they want to 
 * start a web crawling session. Its main purpose is to start the web crawler
 * and to stop the web crawler when the user specifies to.
 * 
 * Please read the documentation in classes mentioned in the @see annotations 
 * for more insight on what this class does. 
 * 
 * @see web.crawler.crawling.CrawlerManager
 * @see web.crawler.crawling.InformationPackage
 * @see web.crawler.gui.MainGraphics
 * 
 * @author Jordan Hartwick
 * May 18, 2016
 */
public class Controller {

    
    /**
     * The CrawlerManager is a Thread (it extends thread). When its start method
     * is called, the web crawling operations will start. The CrawlerManager 
     * will add InformationPackages to the InformationPackages list in the 
     * MainGraphics class.
     * 
     */
    private CrawlerManager crawlerManager;
    
    
    /**
     * This ListModel will contain InformationPackages that will be added during
     * the web crawling session. 
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
     * 
     * @param quickInfo     The text area to clear when the crawling starts.
     */
    public void setupWebCrawler(JTextArea quickInfo) {
        CrawlerSetupDialog csd = new CrawlerSetupDialog(null, true);
        
        Object[] data = csd.getData();
        if(data[0] != null) {
            MainGraphics.updateConsoleArea("---------------------------------------------");
            MainGraphics.updateConsoleArea("Setup Web Crawler Info");
            MainGraphics.updateConsoleArea("Sites: " + Arrays.toString((String[])data[0]));
            MainGraphics.updateConsoleArea("Search Queries: " + Arrays.toString((String[])data[1]));
            MainGraphics.updateConsoleArea("Amount of Links to Collect: " + data[2]);
            MainGraphics.updateConsoleArea("---------------------------------------------");
            
            quickInfo.setText(null);
            listModel.clear();
            MainGraphics.clearConsoleArea();
            
            crawlerManager = new CrawlerManager((String[])data[1], 
                                                (String[])data[0], 
                                                (int)data[2],
                                                listModel);

            // Start scanning on a new thread.
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                    
                    crawlerManager.start();
                    
                    try { 
                        Thread.sleep(1000);
                    } catch (InterruptedException err) {
                        ErrorReport.createErrorReport(err);
                    }
                    
                    while(crawlerManager.activeCrawlers() > 0) {
                        MainGraphics.updateConsoleArea("Active Crawlers: " + crawlerManager.activeCrawlers());
                        
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException err) {
                            ErrorReport.createErrorReport(err);
                        }
                    }
                    
                    JOptionPane.showMessageDialog(null, 
                                                  "Web Crawling Finished", 
                                                  "Web Crawler", 
                                                  1);
                }
            }).start();
        }           
    }
    
    
    /**
     * Stops the web crawling operations.
     */
    public void stopWebCrawler() {
        if(crawlerManager.isAlive()) {
            crawlerManager.stopCrawlers();    
        } else {
            JOptionPane.showMessageDialog(null, "No Crawlers Active", "Error", 0);
        }
    }
}