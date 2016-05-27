package web.crawler.gui;

import errorreport.ErrorReport;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import web.crawler.infoexport.InfoExporter;


/**
 * This is the main graphics class of the crawler application. It contains a
 * list that displays the sites that were found during web crawling, a console
 * that displays output about the crawling session to the user, and a quick
 * info area that will display info about the currently selected link in the
 * list.
 *
 * @author Jordan Hartwick
 * May 14, 2016
 */
public class MainGraphics {


    /**
     * The main frame that holds the main panel in the application.
     */
    private JFrame windowFrame;


    /**
     * The main panel that holds graphical components in the application.
     */
    private JPanel windowPanel;


    /**
     * The tool bar that holds the buttons.
     */
    private JToolBar toolBar;


    /**
     * Buttons to change the running state of the web crawler and to export the
     * information contained in the information packages list.
     */
    private JButton start, stop, export;


    /**
     * Displays information to the user about the progress of the crawling
     * operations.
     */
    private static JTextArea consoleArea;


    /**
     * Its text will be set to the data in an InformationPackage when an item
     * in the information package list is clicked on.
     *
     * @see web.crawler.crawling.InformationPackage
     */
    private JTextArea quickInfo;


    /**
     * Contains a list of InformationPackages.
     *
     * @see web.crawler.crawling.InformationPackage.
     */
    private JList infoPackagesList;


    /**
     * The split panes to help organize the layout of the GUI.
     */
    private JSplitPane northSouth, eastWest;


    /**
     * Helps set up the crawler, start the crawler, stop the crawler, and
     * display information about the crawler to the user.
     */
    private Controller controller;


    /**
     * The ListModel to add an InformationPackage to.
     */
    private DefaultListModel listModel;


    /**
     * Default constructor for the MainGraphics class.
     */
    public MainGraphics() {}


    /**
     * Creates and shows the GUI to the user.
     *
     * Sets the look and feel and creates the GUI components.
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

        createButtons();
        createConsole();
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

        start = new JButton("Start");
        start.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setupWebCrawler(quickInfo, start, stop);
            }
        });

        stop = new JButton("Stop");
        stop.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.stopWebCrawler();
            }
        });
        stop.setEnabled(false);

        export = new JButton("Export");
        export.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(listModel.getSize() == 0) {
                    JOptionPane.showMessageDialog(null, "No sites in list", "Error", 0);
                } else {
                    try {
                        new InfoExporter().exportData(listModel);
                    } catch (IOException err) {
                        ErrorReport.createErrorReport(err);
                    }
                }
            }
        });

        toolBar.add(start);
        toolBar.addSeparator();
        toolBar.add(stop);
        toolBar.addSeparator();
        toolBar.add(export);

        windowPanel.add(toolBar, BorderLayout.PAGE_START);
    }


    /**
     * Creates the console text area.
     */
    private void createConsole() {
        consoleArea = new JTextArea(5,20);
        consoleArea.setLineWrap(true);
    }


    /**
     * Creates a JTextArea that will contain the information from an
     * InformationPackage. Also creates a JList containing the
     * InformationPackages.
     *
     * @see web.crawler.crawling.InformationPackage
     * @see web.crawler.crawling.CrawlerManager
     */
    private void createQuickInfoAreaAndList() {
        quickInfo = new JTextArea(5,15);

        listModel = new DefaultListModel();

        infoPackagesList = new JList(listModel);
        infoPackagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        infoPackagesList.setCellRenderer(new InfoPackageCellRenderer(quickInfo));
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem remove = new JMenuItem("Remove");
        remove.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.removeElementAt(infoPackagesList.getSelectedIndex());
            }
        });
        popup.add(remove);

        // Add mouse listener to listen for clicks on the info packages list.
        infoPackagesList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3 && listModel.size() > 0) {
                    infoPackagesList.setSelectedIndex(infoPackagesList.locationToIndex(e.getPoint()));
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        eastWest = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        eastWest.setLeftComponent(new JScrollPane(quickInfo));
        eastWest.setRightComponent(new JScrollPane(infoPackagesList));
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


    /**
     * Clears the console text area.
     */
    public static void clearConsoleArea() {
        consoleArea.setText(null);
    }
}