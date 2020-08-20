package org.atavdb.service.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author nick
 */
public class ErrorManager {
    /**
     * Convert a stack trace to a string for printing or logging including
     * nested exception ("caused by")
     *
     * @param pThrowable
     * @return
     */
    public static String convertStackTraceToString(Throwable pThrowable) {
        if (pThrowable == null) {
            return null;
        } else {
            StringWriter sw = new StringWriter();
            pThrowable.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
}
