package com.example.demo.config.token.util;

import com.example.demo.config.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class TokenAuthorization {
    private final JwtUtil jwtUtil;

    @Autowired
    public TokenAuthorization(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public String getUsernameFromAuthorizationHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return jwtUtil.extractBodyToken(token).getSubject();
        }

        return null;
    }
}

