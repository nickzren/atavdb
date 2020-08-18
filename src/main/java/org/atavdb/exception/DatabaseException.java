package org.atavdb.exception;

/**
 *
 * @author nick
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(Throwable t) {
        super(t);
    }
}
