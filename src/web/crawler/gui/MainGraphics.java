package web.crawler.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;


/**
 * This is the class that creates the main screen that the user sees. This
 * screen will contain controls that the user will use to manage the web
 * crawler.
 *
 * @author Jordan Hartwick
 * May 14, 2016
 */
public class MainGraphics {


    /** The main frame that holds the main panel in the application. */
    private JFrame      windowFrame;


    /** The main panel that holds graphical components in the application. */
    private JPanel      windowPanel;


    /** The JList that contains the collected links. */
    private JList       collectedLinksList;


    /** Buttons to change the working status of the web crawler. */
    private JButton     startButton, stopButton, pauseButton;


    /**
     * The MainGraphics constructor method will instantiate the classes needed
     * to carry out web crawling operations.
     */
    public MainGraphics() {
        // Instantiate classes in here.
    }


    /**
     * Creates and shows the GUI to the user.
     */
    public void createAndShowGUI() {
        windowFrame = new JFrame("Web Crawler");
        windowPanel = new JPanel(new BorderLayout());

        windowFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        windowFrame.setContentPane(windowPanel);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setSize(800, 600);
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setVisible(true);
    }

}