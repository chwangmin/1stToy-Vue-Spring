package com.first.board.global.secuirty.interceptor;

import com.first.board.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
public class UserActivationInterceptor implements HandlerInterceptor {
    private final MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return false;
        }

        String memberId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return true;
    }
}
