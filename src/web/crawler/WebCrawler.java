package web.crawler;

import java.awt.EventQueue;
import web.crawler.gui.MainGraphics;


/**
 * This is a web crawling application that uses the Jsoup libraries. It has a
 * GUI written with Java Swing, which will display to the user information about
 * what the program is doing. This GUI is also where the user will tell the
 * application what to do.
 *
 * @author  Jordan Hartwick
 * May 14, 2016
 */
public class WebCrawler {


    /**
     * Main method of the program. Starts the GUI.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainGraphics().createAndShowGUI();
            }

        });
    }
}
