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

    //   all runtime(method level) exceptions display this message instead of whitelabel:
    // spring servlet exceptions, like NoResourceFoundException, are not intercepted through this, they need overriding their respective method (from ResponseEntityExceptionHandler class): handleNoResourceFoundException()
    @ExceptionHandler(value
            = {Exception.class})
    protected ResponseEntity<Object> handleConflict(
            Exception ex, WebRequest request) {
        Map<String, String> bodyOfResponse = new HashMap<>();
        bodyOfResponse.put("Response", "The page you are looking for does not exist.");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    // the spring servlet NoResourceFoundException displays the custom text:
    // https://stackoverflow.com/questions/51991992/getting-ambiguous-exceptionhandler-method-mapped-for-methodargumentnotvalidexce
    // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/mvc/method/annotation/ResponseEntityExceptionHandler.html
     @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return new ResponseEntity<>("The page you are looking for does not exist.", HttpStatus.NOT_FOUND);
    }
}
