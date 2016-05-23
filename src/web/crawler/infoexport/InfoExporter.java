package web.crawler.infoexport;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import web.crawler.crawling.InformationPackage;


/**
 * Exports the data in the InformationPackage list as a text file to a 
 * specified file location. 
 *
 * @author Jordan Hartwick
 * May 22, 2016
 */
public class InfoExporter {
    
    
    /**
     * Exports the data contained in the InformationPackage list to a specified
     * file location.
     * 
     * @param listModel     The ListModel containing the data to export.
     * @throws IOException  If the file was not found.
     */
    public void exportData(ListModel listModel) throws IOException {
        String fileName;
        
        Pattern pattern = Pattern.compile("^[^*&%]+$");
        
        while((fileName = getFileName()) != null) {
            
            if(fileName.equals("cancel_option")) {
                return;
            }
            
            Matcher matcher = pattern.matcher(fileName);    
            if(!matcher.find()) {
                JOptionPane.showMessageDialog(null, 
                                              "Not a valid file name", 
                                              "Error", 
                                              0);
                fileName = null;
            } else {
                break;
            }
        }        
            
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

        for(int i = 0; i < listModel.getSize(); i++) {
            pw.println(((InformationPackage) listModel.getElementAt(i)).toString());
        }

        pw.close();
        JOptionPane.showMessageDialog(null, "File saved.", "Save File", 1);
    }
    
    private String getFileName() {
        final JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().toString();
        }
        return "cancel_option";
    }
}
