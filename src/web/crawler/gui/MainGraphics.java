package web.crawler.gui;

import errorreport.ErrorReport;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


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


    /**
     * The tool bar that holds the buttons.
     */
    private JToolBar    toolBar;


    /** Buttons to change the working status of the web crawler. */
    private JButton     startButton, stopButton;


    /** The JTextArea that displays console output. */
    private JTextArea   consoleArea;


    /**
     * Displays information contained in the InformationPackage that is realated
     * to the item in the valid websites list.
     */
    private JTextArea   quickInfoTextArea;


    /**
     * Contains a list of the valid websites.
     */
    private JList       validWebsitesList;


    /**
     * The split panes to help organize the layout of the UI.
     */
    private JSplitPane  northSouth, eastWest;
    
    
    /** Controls input events from this class' components. */
    private Controller  controller;


    /**
     * The MainGraphics constructor method will instantiate the classes needed
     * to carry out web crawling operations.
     */
    public MainGraphics() {}


    /**
     * Creates and shows the UI to the user.
     */
    public void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException err) {
            ErrorReport.createErrorReport(err);
        }

        windowFrame = new JFrame("Web Crawler");
        windowPanel = new JPanel(new BorderLayout());

        windowFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });

        controller = new Controller();

        createButtons();
        setupConsole();
        createQuickInfoAreaAndList();
        addComponents();

        windowFrame.setContentPane(windowPanel);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setSize(800, 600);
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setVisible(true);
    }


    /**
     * Creates the buttons and adds their action listeners.
     */
    private void createButtons() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        startButton = new JButton("Start");
        startButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setupAndStartCrawler();
            }
        });

        stopButton = new JButton("Stop");
        stopButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.stopWebCrawler();
            }
        });

        toolBar.add(startButton);
        toolBar.add(stopButton);

        windowPanel.add(toolBar, BorderLayout.PAGE_START);
    }


    /**
     * Sets up the console. Redirects the systems output stream to the console 
     * text area.
     */
    private void setupConsole() {
        consoleArea = new JTextArea(5,20);
        consoleArea.setLineWrap(true);

//        redirectOutputStream();
    }
    
    
    /**
     * Creates the quick info text area and the list that will contain valid 
     * website links. Right now, the list is populated with items that all say 
     * Hello.
     */
    private void createQuickInfoAreaAndList() {
        quickInfoTextArea = new JTextArea(5,15);
        validWebsitesList = new JList();
                
        String[] data = {
            "Hello","Hello","Hello","Hello","Hello","Hello","Hello","Hello",
            "Hello","Hello","Hello","Hello","Hello","Hello","Hello","Hello",
            "Hello","Hello","Hello","Hello","Hello","Hello","Hello","Hello"
        };
        
        validWebsitesList.setListData(data);
        
        eastWest = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        eastWest.setLeftComponent(new JScrollPane(quickInfoTextArea));
        eastWest.setRightComponent(new JScrollPane(validWebsitesList));
    }        
    
    
    /**
     * Adds the console to the bottom of the split pane and then the 
     */
    private void addComponents() {
        northSouth = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        northSouth.setTopComponent(eastWest);
        northSouth.setBottomComponent(new JScrollPane(consoleArea));
        
        windowPanel.add(northSouth, BorderLayout.CENTER);
    }


    /**
     * Updates the text displayed in the console area.
     *
     * @param text
     */
    private void updateConsoleArea(String text) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                consoleArea.append(text);
            }
        });
    }


    /**
     * Redirects the standard output stream. After calling this method, all
     * output will be displayed in the consoleArea text area.
     */
    private void redirectOutputStream() {
        OutputStream out = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                updateConsoleArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateConsoleArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
    }
}