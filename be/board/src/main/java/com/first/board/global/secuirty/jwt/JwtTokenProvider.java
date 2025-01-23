package com.first.board.global.secuirty.jwt;

import com.first.board.domain.member.entity.Member;
import com.first.board.global.secuirty.dto.JwtTokenDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Value("${jwt.secretKey:test-where-is-my-home-your-256-bit-secret-key}")
    private String secret;

    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    private Date createAccessTokenExpireTime(Date now) {
        return new Date(now.getTime() + (1000L * 60 * 1000000));
    }

    private Date createRefreshTokenExpireTime(Date now) {
        return new Date(now.getTime() + (1000L * 60 * 60));
    }

    public JwtTokenDto createJwtTokenResponse(Member member) {
        return JwtTokenDto.builder()
                .accessToken(createAccessToken(member))
                .refreshToken(createRefreshToken(member.getId()))
                .build();
    }

    public String createAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getMemberId());
        claims.put("name", member.getKoName());

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(createAccessTokenExpireTime(now))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(String id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", id);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(createRefreshTokenExpireTime(now))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getInfoMemberId(String token) {
        return parseJson(token).getString("memberId");
    }

    public String getInfoName(String accessToken) {
        return parseJson(accessToken).getString("name");
    }

    private JSONObject parseJson(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return new JSONObject(claims);
    }
}
