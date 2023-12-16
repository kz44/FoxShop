package com.greenfoxacademy.foxshopnullpointerninjasotocyon.config.CustomHandlerExceptionResolver;

//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
//
//import java.io.IOException;
//
//@Component
//public class RestResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver {
//    @Override
//    protected ModelAndView doResolveException(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Object handler,
//            Exception ex) {
//        try {
//            return handleIllegalArgument(ex, request, response);
//        } catch (Exception handlerException) {
//            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
//        }
//        return null;
//    }
//
//    private ModelAndView
//    handleIllegalArgument(Exception ex, HttpServletRequest request, HttpServletResponse response)
//            throws IOException {
//        response.sendError(HttpServletResponse.SC_NOT_FOUND);
////        String accept = request.getHeader(HttpHeaders.ACCEPT);
////        response.setHeader(accept, HttpHeaders.CONTENT_TYPE);
//
//        return new ModelAndView();
//    }
//}
