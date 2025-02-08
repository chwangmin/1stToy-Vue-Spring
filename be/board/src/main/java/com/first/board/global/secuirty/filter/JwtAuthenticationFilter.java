package com.first.board.global.secuirty.filter;

import com.first.board.domain.auth.service.AuthService;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.AuthenticationException;
import com.first.board.global.secuirty.WebSecurityPath;
import com.first.board.global.secuirty.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        return Arrays.stream(WebSecurityPath.REQUIRE_AUTH_PATH.getPaths())
                .noneMatch(securityPath ->
                        pathMatches(uri, securityPath.getPath()) &&
                                method.equals(securityPath.getMethod().name())
                );
    }

    private boolean pathMatches(String uri, String pattern) {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return pathMatcher.match(pattern, uri);
    }


    // Jwt Provier 주입
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, AuthService authService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, IOException {
        final String authHeader = request.getHeader("Authorization");

        String token = "";

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            refreshTokenCheck(request, response, token);
            throw new AuthenticationException(ErrorCode.NOT_EXISTS_AUTHORIZATION);
        }

        try{
            token = authHeader.split(" ")[1].trim();
        } catch (Exception e) {
            refreshTokenCheck(request, response, token);
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }

        if(!jwtTokenProvider.validateToken(token)){
            refreshTokenCheck(request, response, token);
            throw new AuthenticationException(ErrorCode.TOKEN_EXPIRED);
        }

        String memberId = jwtTokenProvider.getInfoMemberId(token);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                memberId,
                null, List.of(new SimpleGrantedAuthority("DEFAULT_ROLE")));

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private void refreshTokenCheck(HttpServletRequest request, HttpServletResponse response, String token){
        Cookie[] cookies = request.getCookies();

        String refreshToken = null;

        if (cookies != null) {
            refreshToken =  Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        try {
            token = authService.reissue(refreshToken);
        } catch (Exception e){
            throw new JwtException("리프레시 토큰 검증 실패");
        }
        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, token);
    }

}
