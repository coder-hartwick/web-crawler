package web.crawler.gui;

import errorreport.ErrorReport;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
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

    
    /** The tool bar that holds the buttons. */
    private JToolBar    toolBar;


    /** Buttons to change the working status of the web crawler. */
    private JButton     startButton, stopButton;


    /**
     * This is the console area that will display output as the web crawling 
     * program is running. Think of it as a log area.
     */
    private static JTextArea   consoleArea;


    /**
     * When a list item in the valid site list is clicked on, the text of this
     * JTextArea will be set to the content in the InformationPackage that is
     * related to the list item that was clicked on. Each list item has an 
     * InformationPackage related to it.
     * 
     * @see web.crawler.crawling.InformationPackage
     */
    private JTextArea   quickInfoTextArea;


    /**
     * This is referred to as the valid site list throughout this class and 
     * others. 
     * 
     * It contains a list of web sites encountered containing the search queries
     * specified by the user.
     */
    private JList       validWebsitesList;


    /**
     * The split panes to help organize the layout of the GUI.
     */
    private JSplitPane  northSouth, eastWest;


    /**
     * Helps set up the crawler, start the crawler, stop the crawler, and 
     * display information about the crawler to the user.
     */
    private Controller  controller;


    /** 
     * The ListModel to update when wanting to add a valid site to the valid 
     * site list. 
     */
    private DefaultListModel listModel;
    
    
    /** Default constructor for the MainGraphics class. */
    public MainGraphics() {}


    /**
     * Creates and shows the GUI to the user.
     * 
     * Sets the look and feel and creates the GUI components.
     */
    public void createAndShowGUI() {
        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
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

        createButtons();
        setupConsole();
        createQuickInfoAreaAndList();
        addComponents();
        
        controller = new Controller(listModel);

        windowFrame.setContentPane(windowPanel);
        windowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        windowFrame.setSize(800, 600);
        windowFrame.setLocationRelativeTo(null);
        windowFrame.setVisible(true);
    }


    /**
     * Creates the tool bar that will contain the buttons. It will then create
     * the buttons to add to the tool bar and adds them.
     */
    private void createButtons() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        startButton = new JButton("Start");
        startButton.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setupWebCrawler();
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
     * Creates the console text area.
     */
    private void setupConsole() {
        consoleArea = new JTextArea(5,20);
        consoleArea.setLineWrap(true);
    }


    /**
     * Creates a JTextArea that will display information about the selected item
     * in the valid sites list. Also, it creates the valid sites list and 
     * instantiates the list's ListModel.
     */
    private void createQuickInfoAreaAndList() {
        quickInfoTextArea = new JTextArea(5,15);

        listModel = new DefaultListModel();
        validWebsitesList = new JList(listModel);

        eastWest = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        eastWest.setLeftComponent(new JScrollPane(quickInfoTextArea));
        eastWest.setRightComponent(new JScrollPane(validWebsitesList));
    }


    /**
     * Adds the eastWest split pane (contains the valid site links list and the
     * quick info text area) and the console text area to the northSouth split
     * pane, then adds the northSouth split pane to the window panel. The result
     * will be a split pane containing the console area as the bottom component
     * and the eastWest split pane as the top component.
     */
    private void addComponents() {
        northSouth = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        northSouth.setTopComponent(eastWest);
        northSouth.setBottomComponent(new JScrollPane(consoleArea));

        windowPanel.add(northSouth, BorderLayout.CENTER);
    }


    /**
     * Appends a message to the end of the console text area. The console text 
     * area is located at the bottom of the main application window.
     * 
     * @param text  The text to append to the console text area.
     */
    public static void updateConsoleArea(String text) {
        consoleArea.append(text+"\n");
    }
}