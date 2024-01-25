package com.chunkie.live_lyrics_server.interceptor;

import com.chunkie.live_lyrics_server.annotation.LoginRequired;
import com.chunkie.live_lyrics_server.exception.UnauthorizedException;
import com.chunkie.live_lyrics_server.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(LoginRequired.class)) {
            String authToken = request.getHeader("Authorization");
            if (authService.isTokenValid(authToken)) {
                return true;
            } else {
                throw new UnauthorizedException();
            }
        }else return true;
    }
}
