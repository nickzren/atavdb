package org.atavdb.controller;

import org.atavdb.model.MessageResponse;
import org.atavdb.exception.DatabaseException;
import org.atavdb.exception.InvalidSearchException;
import org.atavdb.exception.NotFoundException;
import org.atavdb.exception.RegionMaxLimitException;
import org.atavdb.exception.TooManyRequestException;
import org.atavdb.exception.UnauthorizedException;
import org.atavdb.exception.UserAccessException;
import org.atavdb.model.RegionManager;
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

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageResponse unKnownException(Exception ex) {
        return new MessageResponse(HttpStatus.BAD_REQUEST.value(), ErrorManager.convertStackTraceToString(ex));
    }

    @ExceptionHandler(value = {InvalidSearchException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public MessageResponse invalidSearchException(InvalidSearchException ex) {
        return new MessageResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Invalid search");
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResponse notFoundException(NotFoundException ex) {
        return new MessageResponse(HttpStatus.NOT_FOUND.value(), "No results found");
    }

    @ExceptionHandler(value = {RegionMaxLimitException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public MessageResponse regionMaxLimitException(RegionMaxLimitException ex) {
        return new MessageResponse(HttpStatus.NOT_ACCEPTABLE.value(), 
                "Invalid region or exceeds maximum limit " + RegionManager.MAX_SEARCH_LIMIT + " base pair.");
    }
    
    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageResponse UnauthorizedException(UnauthorizedException ex) {
        return new MessageResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access, login required.");
    }

    @ExceptionHandler(value = {UserAccessException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageResponse userAccessException(UserAccessException ex) {
        return new MessageResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized access");
    }
    
    @ExceptionHandler(value = {TooManyRequestException.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public MessageResponse tooManyRequestException(TooManyRequestException ex) {
        return new MessageResponse(HttpStatus.TOO_MANY_REQUESTS.value(), HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
    }
}
