package com.greenfoxacademy.foxshopnullpointerninjasotocyon.config.ControllerAdvice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    //   Runtime exceptions display a custom message instead of whitelabel error page using the method below.
    // However, spring servlet exceptions, like NoResourceFoundException, are not handled through this, they need extra overriding their respective method (from ResponseEntityExceptionHandler class): see method override: handleNoResourceFoundException()
    /**
     * Exception handler for handling generic exceptions.
     *
     * This method is annotated with {@code @ExceptionHandler} to handle exceptions of type Exception.
     * It returns a ResponseEntity with a response body containing a message indicating that the requested
     * page does not exist, and sets the HTTP status to NOT_FOUND (404).
     *
     * @param ex      The Exception that was thrown.
     * @param request The WebRequest associated with the request.
     * @return A ResponseEntity containing an error message and the specified HTTP status.
     */
    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(
            Exception ex, WebRequest request) {
        Map<String, String> bodyOfResponse = new HashMap<>();
        bodyOfResponse.put("Response", "The page you are looking for does not exist.");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // The spring servlet's NoResourceFoundException displays a custom message via the below method override. Useful sources:
    // https://stackoverflow.com/questions/51991992/getting-ambiguous-exceptionhandler-method-mapped-for-methodargumentnotvalidexce
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html
    /**
     * Overrides the default handling for NoResourceFoundException.
     *
     * This method is called when a NoResourceFoundException is thrown. It returns a ResponseEntity
     * with a message indicating that the requested page does not exist, and sets the HTTP status to NOT_FOUND (404).
     *
     * @param ex       The NoResourceFoundException that was thrown.
     * @param headers  HttpHeaders to be included in the response.
     * @param status   The desired HTTP status for the response.
     * @param request  The WebRequest associated with the request.
     * @return A ResponseEntity containing an error message and the specified HTTP status.
     */
    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers,
                                                                    HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>("The page you are looking for does not exist.", HttpStatus.NOT_FOUND);
    }
}
