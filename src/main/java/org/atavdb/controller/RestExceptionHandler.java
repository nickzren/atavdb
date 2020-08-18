package org.atavdb.controller;

import org.atavdb.model.ErrorResponse;
import org.atavdb.exception.DatabaseException;
import org.atavdb.exception.InvalidQueryException;
import org.atavdb.exception.NotFoundException;
import org.atavdb.exception.UserAccessException;
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
    public ErrorResponse databaseException(DatabaseException ex) {
        return new ErrorResponse(HttpStatus.CONFLICT.value(), "Database error");
    }

    @ExceptionHandler(value = {InvalidQueryException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse invalidQueryException(InvalidQueryException ex) {
        return new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid input query");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(NotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not found");
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse unKnownException(Exception ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Unknown error");
    }

    @ExceptionHandler(value = {UserAccessException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse userAccessException(UserAccessException ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
    }
}
