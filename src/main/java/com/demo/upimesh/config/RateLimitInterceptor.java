package com.demo.upimesh.config;

import com.demo.upimesh.service.RateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Intercepts HTTP requests and applies the Token Bucket rate limit algorithm.
 * If the client has exhausted their tokens, it returns 429 Too Many Requests
 * before the request ever reaches the Controller.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RateLimitInterceptor.class);

    @Autowired
    private RateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();

        if (rateLimiter.allowRequest(clientIp)) {
            return true; // Allow to proceed
        }

        log.warn("Rate limit exceeded for IP: {}", clientIp);
        response.setStatus(429); // HTTP 429 Too Many Requests
        response.getWriter().write("Too Many Requests");
        return false; // Stop execution
    }
}
