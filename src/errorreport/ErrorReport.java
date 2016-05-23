package errorreport;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * The ErrorReport creates a file that contains an exception's stack trace.
 *
 * @author Jordan Hartwick
 * May 18, 2016
 */
public class ErrorReport {


    /** Private constructor so the class cannot be instantiated. */
    private ErrorReport(){}


    /**
     * Creates an error report. The error report will contain the stack trace
     * of an exception. The error report file will be located in the following
     * location: "errorreports/". The error report file name is "error_at_" with
     * the system's time in nanoseconds appended to the end. The file extension
     * will be ".txt".
     *
     * @param cause     The exception that was thrown.
     */
    public static void createErrorReport(Exception cause) {
        StringWriter sw = new StringWriter();
        cause.printStackTrace(new PrintWriter(sw));
        String errMessage = sw.toString();

        File file = new File("errorreports");
        if(!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }

        try {
            PrintWriter pw = new PrintWriter("errorreports/error_at_" + System.nanoTime() + ".txt");
            pw.println(errMessage);
            pw.close();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}
