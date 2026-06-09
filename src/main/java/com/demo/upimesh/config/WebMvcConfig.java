package com.demo.upimesh.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures the MVC layer to register custom interceptors.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Only protect the endpoints that are computationally expensive or core production routes.
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/api/bridge/**")
                .addPathPatterns("/api/demo/send");
    }
}
