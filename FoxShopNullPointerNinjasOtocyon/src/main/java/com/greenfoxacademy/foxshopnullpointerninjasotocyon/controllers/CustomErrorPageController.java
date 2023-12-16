//package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;
//
//import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.HttpStatusErrorDTO;
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.ModelAndView;
//
//
//@RestController
////@RestController
////@ConditionalOnMissingBean
//public class CustomErrorPageController implements ErrorController {
//
//    @RequestMapping("/error")
////    public ModelAndView handleError
//    public ResponseEntity<?> handleError(HttpServletRequest request) {
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//
//        if (status != null) {
//            Integer statusCode = Integer.valueOf(status.toString());
//
//            if(statusCode == HttpStatus.NOT_FOUND.value()) {
////                return new ModelAndView("error-404");
//                return ResponseEntity.badRequest().body(new HttpStatusErrorDTO(404));
////                        .body(new Error404DTO());
//            }
//            else if(statusCode == HttpStatus.FORBIDDEN.value() || statusCode == HttpStatus.UNAUTHORIZED.value()) {
//                return ResponseEntity.badRequest().body(new HttpStatusErrorDTO(403));
//            }
//            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//                return ResponseEntity.badRequest().body(new HttpStatusErrorDTO(500));
//            }
//
//        }
//        return ResponseEntity.badRequest().body(new HttpStatusErrorDTO(0));
//
//    }
//}