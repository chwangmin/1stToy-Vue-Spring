package com.first.board.global.secuirty;

import com.first.board.domain.auth.service.AuthService;
import com.first.board.global.secuirty.filter.JwtAuthenticationFilter;
import com.first.board.global.secuirty.filter.JwtExceptionFilter;
import com.first.board.global.secuirty.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
        ;

        // JWT 필터 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, authService),
                        BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(
                        "/swagger-ui/index.html",
                        "/v1/users/{id}",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/api-docs"
                );
    }
}
