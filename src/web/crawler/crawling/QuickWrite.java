package web.crawler.crawling;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/**
 *
 *
 * @author Jordan Hartwick
 * May 17, 2016
 */
public class QuickWrite {  
    
    public static void writeToFile(String message) {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("src/log.txt", true)));
            pw.println(message);
            pw.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}
