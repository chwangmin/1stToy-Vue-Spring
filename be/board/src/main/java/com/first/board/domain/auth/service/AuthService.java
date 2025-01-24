package com.first.board.domain.auth.service;

import com.first.board.domain.member.adaptor.MemberAdaptor;
import com.first.board.domain.member.entity.Member;
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
    private final MemberAdaptor memberAdaptor;
    private final JwtTokenProvider jwtTokenProvider;
    private final Encryption encryption;

    public String reissue(String refreshToken) {

        if (jwtTokenProvider.validateToken(refreshToken)) {
            String memberId = jwtTokenProvider.getInfoMemberId(refreshToken);

            Member member = memberAdaptor.findByMemberId(memberId);

            return jwtTokenProvider.createAccessToken(member);
        } else {
            throw new AuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
    }

    @Transactional
    public JwtTokenDto login(String memberId, String password) {

        Member member = memberAdaptor.findByMemberId(memberId);

        if (member.getFailCnt() >= 5) {
            throw new AuthenticationException(ErrorCode.MEMBER_COUNT_OUT);
        }

        try {
            String encPassword = encryption.encryptPassword(password, member.getSalt());

            if (!member.getEncryptPassword().equals(encPassword)) {
                throw new AuthenticationException(ErrorCode.MEMBER_NOT_MATCH);
            }

            String accessToken = jwtTokenProvider.createAccessToken(member);
            String refreshToken = jwtTokenProvider.createRefreshToken(member.getIdtoString());

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
        Member member = memberAdaptor.findByMemberId(memberId);
        member.updateRefreshToken(null);
    }

    public String generateTestToken() {
        Member member = memberAdaptor.findByMemberId("string");

        return jwtTokenProvider.createAccessToken(member);
    }
}
