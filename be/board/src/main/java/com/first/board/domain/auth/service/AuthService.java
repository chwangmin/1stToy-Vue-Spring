package com.first.board.domain.auth.service;

import com.first.board.domain.member.entity.Member;
import com.first.board.domain.member.repository.MemberRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.AuthenticationException;
import com.first.board.global.secuirty.dto.JwtTokenDto;
import com.first.board.global.secuirty.encryption.Encryption;
import com.first.board.global.secuirty.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final Encryption encryption;

    public String reissue(String refreshToken) {

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String memberId = jwtTokenProvider.getInfoMemberId(refreshToken);

            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new AuthenticationException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

            return jwtTokenProvider.createAccessToken(member);
        } else {
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    @Transactional
    public JwtTokenDto login(String memberId, String password) {

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH));

        if (member.getFailCnt() >= 5) {
            throw new AuthenticationException(ErrorCode.MEMBER_COUNT_OUT);
        }

        try {
            String encPassword = encryption.Hashing(password.getBytes(), member.getSalt());

            if (!member.getPassword().equals(encPassword)) {
                throw new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH);
            }

            String accessToken = jwtTokenProvider.createAccessToken(member);
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

            member.initCount();

            member.updateRefreshToken(refreshToken);

            return JwtTokenDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (AuthenticationException e){
            member.updateCount();
            e.printStackTrace();
        } catch (Exception e) {
            member.updateCount();
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    public void removeRefreshToken(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.MEMBER_NOT_EXISTS));
        member.updateRefreshToken(null);
    }
}
