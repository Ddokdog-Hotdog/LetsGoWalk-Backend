package com.ddokdoghotdog.gowalk.global.jwt;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import com.ddokdoghotdog.gowalk.global.exception.ErrorCode;
import com.ddokdoghotdog.gowalk.global.exception.TokenException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TokenExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            ErrorCode errorCode = e.getErrorCode();
            response.sendError(errorCode.getStatus(), errorCode.getMessage());
        }
    }
}
