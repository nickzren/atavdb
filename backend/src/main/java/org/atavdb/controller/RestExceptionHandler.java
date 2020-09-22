package org.atavdb.controller;

import org.atavdb.model.MessageResponse;
import org.atavdb.exception.DatabaseException;
import org.atavdb.exception.InvalidQueryException;
import org.atavdb.exception.NotFoundException;
import org.atavdb.exception.UserAccessException;
import org.atavdb.util.ErrorManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author nick
 */
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {DatabaseException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public MessageResponse databaseException(DatabaseException ex) {
        return new MessageResponse(HttpStatus.CONFLICT.value(), "Database error");
    }

    @ExceptionHandler(value = {InvalidQueryException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public MessageResponse invalidQueryException(InvalidQueryException ex) {
        return new MessageResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid input query");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResponse notFoundException(NotFoundException ex) {
        return new MessageResponse(HttpStatus.NOT_FOUND.value(), "Not found");
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageResponse unKnownException(Exception ex) {
        return new MessageResponse(HttpStatus.BAD_REQUEST.value(), ErrorManager.convertStackTraceToString(ex));
    }

    @ExceptionHandler(value = {UserAccessException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageResponse userAccessException(UserAccessException ex) {
        return new MessageResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
    }
}
