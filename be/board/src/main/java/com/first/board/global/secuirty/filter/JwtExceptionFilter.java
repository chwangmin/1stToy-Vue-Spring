package com.first.board.global.secuirty.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.ErrorResponse;
import io.jsonwebtoken.JwtException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            setErrorResponse(response, ErrorCode.ACCESS_TOKEN_REFRESH);
        } catch (JwtException | IllegalArgumentException | NullPointerException | UnsupportedEncodingException e) {
            setErrorResponse(response, ErrorCode.NOT_VALID_TOKEN);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
        log.error("filter에서 에러 체크");

        ObjectMapper objectMapper = new ObjectMapper();
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        try {
            response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(errorCode.getErrorCode(),errorCode.getMessage())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
