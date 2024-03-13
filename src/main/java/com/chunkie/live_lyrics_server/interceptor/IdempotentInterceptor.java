package com.chunkie.live_lyrics_server.interceptor;

import com.chunkie.live_lyrics_server.annotation.Idempotent;
import com.chunkie.live_lyrics_server.service.LiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IdempotentInterceptor implements HandlerInterceptor {


    private final Map<String, Boolean> processedRequests = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(IdempotentInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(Idempotent.class)) {
            String prefix = ((HandlerMethod) handler).getMethodAnnotation(Idempotent.class).prefix();

        }else return true;
        return true;
    }

}
