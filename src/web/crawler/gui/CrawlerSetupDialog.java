package web.crawler.gui;

import errorreport.ErrorReport;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;


/**
 * The CrawlerSetupDialog is a JDialog that will allow the user specify the 
 * starting sites they want the web crawler to go to first and the search 
 * queries to search for while crawling.
 * 
 * @author  Jordan Hartwick
 * May 19, 2016
 */
public class CrawlerSetupDialog extends JDialog {


    /** 
     * The panel that will hold the graphical components. This is referred to as
     * the "main container" throughout this class.
     */
    private JPanel container;


    /** Labels that label what to do and what certain fields are. */
    private JLabel mainDirections, site, queries, amountToScan;


    /**
     * Buttons to start the crawling, cancel the setup, add a site, and delete
     * a site.
     */
    private JButton confirm, cancel, addSite;


    /**
     * Text fields to allow the user places to specify a site to add and some
     * search queries.
     */
    private JTextField siteLink, searchQueries, amount;


    /** The list that contains the starting sites. */
    private JList siteList;
    
    
    /** The list model for the siteList. */
    private DefaultListModel listModel;
    
    
    /** String arrays to hold the sites and the queries. */
    private String[] siteLinks, queryList;
    
    
    /** Holds the amount of sites to be scanned. */
    private int siteAmount;
    
    
    /**
     * Creates a new instance of the SetupCrawler class and displays this
     * dialog.
     *
     * @param parent    The parent to this dialog.
     * @param modal     Whether this dialog is modal or not.
     */
    public CrawlerSetupDialog(JFrame parent, boolean modal) {
        super(parent, modal);        
        this.setTitle("Setup Web Crawler");
        initComponents();
        this.setContentPane(container);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }


    /**
     * Creates and adds the components to the main container.
     */
    private void initComponents() {
        createComponents();
        addComponentsToMainContainer();
    }


    /**
     * Creates the components that need to be added to the main container. Also,
     * a popup menu is added so the user can more easily remove a list item.
     */
    private void createComponents() {
        container = new JPanel(new GridBagLayout());

        mainDirections  = new JLabel("Fill out the fields below.");
        site            = new JLabel("Site:");
        site.setHorizontalAlignment(SwingConstants.RIGHT);
        queries         = new JLabel("Add search queries below separated by commas.");
        amountToScan    = new JLabel("Amount to scan:");
        amountToScan.setHorizontalAlignment(SwingConstants.RIGHT);
        
        listModel   = new DefaultListModel();
        siteList    = new JList(listModel);
        siteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove");
        removeItem.addActionListener(new AbstractAction() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.removeElementAt(siteList.getSelectedIndex());
            }
        });
        popupMenu.add(removeItem);
        
        siteList.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3 && listModel.size() > 0) {
                    siteList.setSelectedIndex(siteList.locationToIndex(e.getPoint()));
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        
        confirm = new JButton("Confirm");
        confirm.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ListModel data = siteList.getModel();
                
                /*
                    Check the settings and the user entered data. If everything
                    checks out, then set the data to the globals and close this
                    dialog.
                */
                if(data.getSize() == 0) {
                    JOptionPane.showMessageDialog(null, 
                                                    "No sites provided.", 
                                                    "Error", 
                                                    JOptionPane.ERROR_MESSAGE);
                } else if(searchQueries.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, 
                                                    "No search queries provided.", 
                                                    "Error", 
                                                    JOptionPane.ERROR_MESSAGE);
                } else if(amount.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, 
                                                    "No amount provided.", 
                                                    "Error", 
                                                    JOptionPane.ERROR_MESSAGE);
                } else {
                    List<String> listData = new ArrayList<>();
                    for(int i = 0; i < data.getSize(); i++) {
                        listData.add((String) data.getElementAt(i));
                    }
                    
                    siteLinks = Arrays.copyOf(listData.toArray(),
                                                listData.size(), 
                                                String[].class);
                    
                    queryList = searchQueries.getText().trim().split(",");
                    
                    try {
                        siteAmount = Integer.parseInt(amount.getText());
                        closeDialog();
                    } catch (InputMismatchException err) {
                        ErrorReport.createErrorReport(err);
                        JOptionPane.showMessageDialog(null, 
                                                        "Amount specified is not a number.", 
                                                        "Error", 
                                                        JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        cancel = new JButton("Cancel");
        cancel.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });

        addSite = new JButton("Add Site");
        addSite.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(siteLink.getText().trim().length() != 0) {
                    listModel.addElement(siteLink.getText().trim());
                    siteLink.setText(null);
                    siteLink.requestFocus();
                }
            }
        });

        siteLink = new JTextField(15);
        siteLink.addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(siteLink.getText().trim().length() != 0) {
                        listModel.addElement(siteLink.getText().trim());
                        siteLink.setText(null);
                        siteLink.requestFocus();
                    }
                }
            }
        });
        
        searchQueries = new JTextField(20);
        amount = new JTextField(5);
    }


    /**
     * Adds the components to the main container. The main container uses a
     * GridBagLayout.
     */
    private void addComponentsToMainContainer() {
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 5, 5, 5);
        container.add(mainDirections, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        container.add(site, c);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        container.add(siteLink, c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        JScrollPane scrollPane = new JScrollPane(siteList);
        scrollPane.setPreferredSize(new Dimension(130, 100));
        container.add(scrollPane, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(addSite, c);
        
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(amountToScan, c);

        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(amount, c);        

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(queries, c);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 4;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(searchQueries, c);        
        
        c.gridx = 2;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(confirm, c);

        c.gridx = 3;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(cancel, c);
    }
    
    
    /**
     * Returns the data that was entered in this dialog.
     * 
     * @return  The data that was entered in this dialog.
     */
    public Object[] getData() {
        return new Object[]{siteLinks, queryList, siteAmount};
    }
    
    
    /**
     * Hides this dialog.
     */
    private void closeDialog() {
        this.setVisible(false);
    }
}