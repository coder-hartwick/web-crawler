package web.crawler.gui;

import errorreport.ErrorReport;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import org.apache.commons.validator.routines.UrlValidator;


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
    private JLabel mainDirections, site, queries, amount;


    /**
     * Buttons to start the crawling, cancel the setup, add a site, and delete
     * a site.
     */
    private JButton confirm, cancel, addSite;


    /**
     * Text fields to allow the user places to specify a site to add and some
     * search queries.
     */
    private JTextField siteField, queryField, amountField;


    /**
     * The list that contains the starting sites.
     */
    private JList sites;


    /**
     * The list model for the site list.
     */
    private DefaultListModel listModel;


    /**
     * String arrays to hold the sites and the queries.
     */
    private String[] siteList, queryList;


    /**
     * Holds the amount of sites to be scanned.
     */
    private int amountToScan;


    /**
     * Used to validate URLs.
     */
    private UrlValidator validator;


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
        validator = new UrlValidator();
    }


    /**
     * Creates the components that need to be added to the main container. Also,
     * a popup menu is added so the user can more easily remove a list item.
     */
    private void createComponents() {
        container = new JPanel(new GridBagLayout());

        mainDirections = new JLabel("Fill out the fields below.");

        site = new JLabel("Site:");
        site.setHorizontalAlignment(SwingConstants.RIGHT);

        queries = new JLabel("Add search queries below separated by commas.");

        amount = new JLabel("Amount to scan:");
        amount.setHorizontalAlignment(SwingConstants.RIGHT);

        listModel = new DefaultListModel();
        sites = new JList(listModel);
        sites.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPopupMenu popup = new JPopupMenu();
        JMenuItem remove = new JMenuItem("Remove");
        remove.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.removeElementAt(sites.getSelectedIndex());
            }
        });
        popup.add(remove);

        // Add a mouse listener to listen for clicks in the list.
        sites.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3 && listModel.size() > 0) {
                    sites.setSelectedIndex(sites.locationToIndex(e.getPoint()));
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        confirm = new JButton("Confirm");
        confirm.addActionListener(new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                ListModel data = sites.getModel();

                // Check if the values entered in the dialog are valid.
                if(data.getSize() == 0) {
                    JOptionPane.showMessageDialog(null,
                                                  "No sites provided.",
                                                  "Error",
                                                  0);
                } else if(queryField.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(null,
                                                  "No search queries provided.",
                                                  "Error",
                                                  0);
                } else if(amountField.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(null,
                                                  "No amount provided.",
                                                  "Error",
                                                  0);
                } else {
                    List<String> listData = new ArrayList<>();

                    for(int i = 0; i < data.getSize(); i++) {
                        listData.add((String) data.getElementAt(i));
                    }

                    siteList = Arrays.copyOf(listData.toArray(),
                                              listData.size(),
                                              String[].class);

                    queryList = queryField.getText().trim().split(",");

                    try {
                        amountToScan = Integer.parseInt(amountField.getText());
                        closeDialog();
                    } catch (InputMismatchException err) {
                        ErrorReport.createErrorReport(err);
                        JOptionPane.showMessageDialog(null,
                                                      "Amount specified is not a number.",
                                                      "Error",
                                                      0);
                    }
                }
            }
        }); // End of confirm button action listener.

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

                String siteEntered = siteField.getText().trim();

                if(siteEntered.length() != 0
                        && validator.isValid(siteEntered)) {

                    listModel.addElement(siteEntered);
                    siteField.setText(null);
                    siteField.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "Not a valid URL", "Error", 0);
                }
            }
        });

        siteField = new JTextField(15);
        siteField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                String siteEntered = siteField.getText().trim();

                if(e.getKeyCode() == KeyEvent.VK_ENTER
                        && siteEntered.length() != 0) {

                    if(validator.isValid(siteEntered)) {
                        listModel.addElement(siteField.getText().trim());
                        siteField.setText(null);
                        siteField.requestFocus();
                    } else {
                        JOptionPane.showMessageDialog(null, "Not a valid URL","Error",0);
                    }
                }
            }
        });

        queryField = new JTextField(20);

        amountField = new JTextField(5);

        // Check user's input after they entered the amount.
        amountField.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                if(amountField.getText().length() != 0) {
                    try {
                        Integer.parseInt(amountField.getText());
                    } catch (NumberFormatException err) {
                        ErrorReport.createErrorReport(err);
                        JOptionPane.showMessageDialog(null, "Amount is not a number", "Error", 0);
                        amountField.setText(null);
                        amountField.requestFocus();
                    }
                }
            }
        });
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
        container.add(siteField, c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridheight = 3;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        JScrollPane scrollPane = new JScrollPane(sites);
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
        container.add(amount, c);

        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        container.add(amountField, c);

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
        container.add(queryField, c);

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
        return new Object[]{siteList, queryList, amountToScan};
    }


    /**
     * Hides this dialog.
     */
    private void closeDialog() {
        this.setVisible(false);
    }
}