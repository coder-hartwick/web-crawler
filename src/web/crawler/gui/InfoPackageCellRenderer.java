package web.crawler.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import web.crawler.crawling.InformationPackage;


/**
 * The InfoPackageCellRenderer will render a list cell that contains the link to
 * the website stored in InformationPackage.
 *
 * @see web.crawler.crawling.InformationPackage
 *
 * @author Jordan Hartwick
 * May 22, 2016
 */
public class InfoPackageCellRenderer extends JLabel implements ListCellRenderer {


    /**
     * The JTextArea that should be updated when a list item is clicked on.
     */
    private JTextArea quickInfo;


    /**
     * InfoPackageCellRenderer constructor method will create a new instance of
     * the InfoPackageCellRenderer and set the text area that should update when
     * a list item is selected. It also sets the cell to be opaque so colors can
     * be seen when selected.
     *
     * @param quickInfo     The JTextArea to display a list items information
     *                      package in.
     */
    public InfoPackageCellRenderer(JTextArea quickInfo) {
        this.quickInfo = quickInfo;
        this.setOpaque(true);
    }


    /*
        Sets the text of this JLabel to the link in the InformationPackage. When
        a list item is selected, the text inside of the quick info text area
        will be updated to contain the information from the cell's
        InformationPackage.
    */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                    int index,
                                                    boolean isSelected,
                                                    boolean cellHasFocus) {

        InformationPackage infoPackage = (InformationPackage) value;
        this.setText(infoPackage.getLink());

        if(isSelected) {
            this.setBackground(Color.RED);
            this.setForeground(Color.WHITE);
            quickInfo.setText(infoPackage.toString());
        } else {
            this.setBackground(Color.WHITE);
            this.setForeground(Color.BLACK);
        }

        return this;
    }
}