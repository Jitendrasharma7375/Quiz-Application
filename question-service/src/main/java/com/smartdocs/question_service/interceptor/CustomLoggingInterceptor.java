//package com.smartdocs.question_service.interceptor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.util.UUID;
//
//@Component
//public class CustomLoggingInterceptor implements HandlerInterceptor {
//
//    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingInterceptor.class);
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//
//        String requestId = UUID.randomUUID().toString();
//        MDC.put("requestId", requestId);
//        request.setAttribute("requestId", requestId);
//
//        String username = request.getRemoteUser() != null ? request.getRemoteUser() : "Anonymous";
//        String url = request.getRequestURL().toString();
//        String method = request.getMethod();
//
//        logger.info("[START] Request ID: {} - Incoming request: {} {} by user: {}", requestId, method, url, username);
//
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//            throws Exception {
//
//        String requestId = (String) request.getAttribute("requestId");
//        MDC.clear();
//
//        int status = response.getStatus();
//        logger.info("[END] Request ID: {} - Response status: {}", requestId, status);
//
//        if (ex != null) {
//            logger.error("[ERROR] Request ID: {} - Error: {}", requestId, ex.getMessage());
//        }
//    }
//}
