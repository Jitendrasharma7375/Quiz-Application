package com.smartdocs.quiz_service.logging;

import java.time.Instant;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Aspect
public class UnifiedLoggingHandler implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UnifiedLoggingHandler.class);

    private static final String REQUEST_ID = "requestId";
    private static final String START_TIME = "startTime";

    
    @Value("${spring.application.name:unknown-service}")
    private String microserviceName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = generateRequestId();
        long startTime = Instant.now().toEpochMilli();

        MDC.put(REQUEST_ID, requestId);
        MDC.put(START_TIME, String.valueOf(startTime));
        MDC.put("microserviceName", microserviceName);

        String user = request.getRemoteUser() != null ? request.getRemoteUser() : "Anonymous";
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        String clientIp = request.getRemoteAddr();

        int requestSize = getRequestSize(request);

        logger.info("Microservice: {} | Incoming request: Request ID: {} Method: {} URL: {} User: {} Client IP: {} Request Size: {} bytes",
                microserviceName, requestId, method, url, user, clientIp, requestSize);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 Exception ex) {
        String requestId = MDC.get(REQUEST_ID);
        long startTime = Long.parseLong(MDC.get(START_TIME));
        long executionTime = Instant.now().toEpochMilli() - startTime;

        int status = response.getStatus();
        String responseSize = getResponseSize(response);

        logger.info("Microservice: {} | Response for Request ID: {} Status: {} Response Size: {} bytes Execution Time: {} ms",
                microserviceName, requestId, status, responseSize, executionTime);

        MDC.clear();
    }

    @Around("execution(* com.smartdocs.quiz_service.controller..*(..)) || execution(* com.smartdocs.quiz_service.service..*(..))")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestId = MDC.get(REQUEST_ID);
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();

        Logger layerLogger = LoggerFactory.getLogger(className);

        if (className.contains("controller")) {
            layerLogger.info("Microservice: {} | [START] Request ID: {} Method: '{}' started", microserviceName, requestId, methodName);
        } else if (className.contains("service")) {
            String additionalInfo = extractServiceMethodContext(joinPoint);
            layerLogger.info("Microservice: {} | [START] Request ID: {} {}Fetching data or performing operation", microserviceName,
                    requestId, additionalInfo != null ? additionalInfo : "");
        }

        Object result;
        try {
            result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;

            if (className.contains("controller")) {
                layerLogger.info("Microservice: {} | [END] Request ID: {} Method: '{}' completed in {} ms. Response: {}", microserviceName,
                        requestId, methodName, elapsedTime,
                        result instanceof org.springframework.http.ResponseEntity
                                ? ((org.springframework.http.ResponseEntity<?>) result).getStatusCode()
                                : "Unknown");
            } else if (className.contains("service")) {
                String additionalInfo = extractServiceMethodContext(joinPoint);
                layerLogger.info("Microservice: {} | [END] Request ID: {} {} completed in {} ms", microserviceName,
                        requestId, additionalInfo != null ? additionalInfo : "", elapsedTime);
            }
        } catch (Exception ex) {
            layerLogger.error("Microservice: {} | [ERROR] Request ID: {} Method: '{}' failed: {}", microserviceName, requestId, methodName, ex.getMessage());
            throw ex;
        }

        return result;
    }

    private String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    private int getRequestSize(HttpServletRequest request) {
        try {
            return request.getContentLength() > 0 ? request.getContentLength() : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private String getResponseSize(HttpServletResponse response) {
        try {
           
            if (response instanceof CustomResponseWrapper) {
                int size = ((CustomResponseWrapper) response).getResponseSize();
                return String.valueOf(size);
            }
           
            String contentLength = response.getHeader("Content-Length");
            if (contentLength != null) {
                return contentLength;
            }
        } catch (Exception e) {
            logger.warn("Error getting response size", e);
        }
        
     
        return "0";
    }

    private String extractServiceMethodContext(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();

        switch (methodName) {
            case "getQuizQuestions":
                return args.length > 0 ? "Fetching questions for Quiz ID: " + args[0] + ". " : "";
            case "createQuiz":
                return args.length > 0 ? "Creating quiz with details. " : "";
            case "calculateResult":
                return args.length > 0 ? "Calculating result for Quiz ID: " + args[0] + ". " : "";
            default:
                return null;
        }
    }
}
