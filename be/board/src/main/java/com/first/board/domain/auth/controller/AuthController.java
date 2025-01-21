package com.first.board.domain.auth.controller;

import com.first.board.domain.auth.dto.request.LoginRequest;
import com.first.board.domain.auth.service.AuthService;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.AuthenticationException;
import com.first.board.global.secuirty.dto.JwtTokenDto;
import com.first.board.global.secuirty.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary="회원 로그인", description = "회원 로그인을 합니다.")
    @PostMapping(path="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        JwtTokenDto jwtTokenDto = authService.login(loginRequest.getMemberId(), loginRequest.getPassword());

        if(jwtTokenDto == null){
            throw new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH);
        }

        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, jwtTokenDto.getAccessToken());

        Cookie cookie = new Cookie("refreshToken", jwtTokenDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        log.info(response.toString());

        return ResponseEntity.ok().build();
    }

    @Operation(summary="토큰 재발급", description = "리프레시 토큰으로 액세스 토큰을 재발급 합니다.")
    @PostMapping(path="/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createAccessToken(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();

        String refreshToken = null;

        if (cookies != null) {
            refreshToken =  Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("refreshToken"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }

        if(!jwtTokenProvider.validateToken(refreshToken)){
            throw new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, authService.reissue(refreshToken));

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃 API")
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal String memberId,
                                         HttpServletResponse response) {

        Cookie myCookie = new Cookie("refreshToken", null);
        myCookie.setMaxAge(0); // 쿠키의 expiration 타임을 0으로 하여 없앤다.
        myCookie.setPath("/"); // 모든 경로에서 삭제 됬음을 알린다.
        response.addCookie(myCookie);

        authService.removeRefreshToken(memberId);

        return ResponseEntity.ok().build();
    }
}
