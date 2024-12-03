package com.smartdocs.question_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.smartdocs.question_service.logging.UnifiedLoggingHandler;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UnifiedLoggingHandler unifiedLoggingHandler;

    public WebConfig(UnifiedLoggingHandler unifiedLoggingHandler) {
        this.unifiedLoggingHandler = unifiedLoggingHandler;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(unifiedLoggingHandler).addPathPatterns("/**");
    }
}
