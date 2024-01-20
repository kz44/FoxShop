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

    @ExceptionHandler(value
            = { Exception.class })
//    runtime(method level) exceptions
    protected ResponseEntity<Object> handleConflict(
            Exception ex, WebRequest request) {
        Map<String, String> bodyOfResponse = new HashMap<>();
        bodyOfResponse.put("Response","The page you are looking for does not exist.");
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
//        String errorMessage = ex.getLocalizedMessage();
        ResponseEntity r = new ResponseEntity<>("The page you are looking for does not exist.", HttpStatus.NOT_FOUND);
return r;
    }
}
