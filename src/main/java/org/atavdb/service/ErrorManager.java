package org.atavdb.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.springframework.stereotype.Service;

/**
 *
 * @author nick
 */
@Service
public class ErrorManager {
        /**
     * Convert a stack trace to a string for printing or logging including
     * nested exception ("caused by")
     *
     * @param pThrowable
     * @return
     */
    public String convertStackTraceToString(Throwable pThrowable) {
        if (pThrowable == null) {
            return null;
        } else {
            StringWriter sw = new StringWriter();
            pThrowable.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
}
