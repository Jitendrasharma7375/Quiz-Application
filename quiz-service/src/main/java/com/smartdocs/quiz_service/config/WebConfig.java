package com.smartdocs.quiz_service.config;

import com.smartdocs.quiz_service.logging.UnifiedLoggingHandler;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UnifiedLoggingHandler unifiedLoggingHandler;

    public WebConfig(UnifiedLoggingHandler unifiedLoggingHandler) {
        this.unifiedLoggingHandler = unifiedLoggingHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(unifiedLoggingHandler)
                .addPathPatterns("/**"); 
    }
}
