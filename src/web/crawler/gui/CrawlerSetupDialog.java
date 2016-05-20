package web.crawler.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
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
    private JButton confirm, cancel, addSite, deleteSite;


    /**
     * Text fields to allow the user places to specify a site to add and some
     * search queries.
     */
    private JTextField siteLink, searchQueries, amount;


    /** The list that contains the starting sites. */
    private JList siteLinks;
    
    
    /**
     * Creates a new instance of the SetupCrawler class.
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
     * Creates the components and adds them to the main container.
     */
    private void initComponents() {
        createComponents();
        addComponentsToMainContainer();
    }


    /**
     * Creates the components to add to the main container.
     */
    private void createComponents() {
        container = new JPanel(new GridBagLayout());

        mainDirections  = new JLabel("Fill out the fields below.");
        site            = new JLabel("Site:");
        site.setHorizontalAlignment(SwingConstants.RIGHT);
        queries         = new JLabel("Add search queries below separated by commas.");
        amountToScan    = new JLabel("Amount to scan:");
        amountToScan.setHorizontalAlignment(SwingConstants.RIGHT);
        
        DefaultListModel listModel = new DefaultListModel();
        siteLinks = new JList(listModel);
        siteLinks.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        confirm = new JButton("Confirm");
        confirm.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });

        cancel = new JButton("Cancel");
        cancel.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
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

        deleteSite = new JButton("Delete Site");
        deleteSite.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
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
        JScrollPane scrollPane = new JScrollPane(siteLinks);
        scrollPane.setPreferredSize(new Dimension(130, 100));
        container.add(scrollPane, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(addSite, c);

        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        container.add(deleteSite, c);
        
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
}