//package com.smartdocs.quiz_service.interceptor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.time.Instant;
//import java.util.UUID;
//
//@Component
//public class CustomLoggingInterceptor implements HandlerInterceptor {
//    private static final Logger logger = LoggerFactory.getLogger(CustomLoggingInterceptor.class);
//
//    private static final String REQUEST_ID = "requestId";
//    private static final String START_TIME = "startTime";
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String requestId = UUID.randomUUID().toString();
//        long startTime = Instant.now().toEpochMilli();
//
//        // Set context for logs
//        MDC.put(REQUEST_ID, requestId);
//        MDC.put(START_TIME, String.valueOf(startTime));
//
//        String user = request.getRemoteUser() != null ? request.getRemoteUser() : "Anonymous";
//        String url = request.getRequestURL().toString();
//        String method = request.getMethod();
//        String clientIp = request.getRemoteAddr();
//
//        logger.info("Incoming request: [{}] Method: {} URL: {} User: {} Client IP: {} Request Size: unknown bytes",
//                requestId, method, url, user, clientIp);
//
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        String requestId = MDC.get(REQUEST_ID);
//        long startTime = Long.parseLong(MDC.get(START_TIME));
//        long executionTime = Instant.now().toEpochMilli() - startTime;
//
//        int status = response.getStatus();
//        String responseSize = response.getHeader("Content-Length") != null ? response.getHeader("Content-Length") : "unknown";
//
//        logger.info("Response for [{}]: Status: {} Response Size: {} bytes Execution Time: {} ms",
//                requestId, status, responseSize, executionTime);
//
//        // Clear MDC context
//        MDC.clear();
//    }
//}
