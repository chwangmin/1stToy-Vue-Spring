package com.first.board.domain.member.adaptor;

import com.first.board.domain.member.entity.Member;
import com.first.board.domain.member.repository.MemberRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.AuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAdaptor {
    private MemberRepository memberRepository;

    public Member findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new AuthenticationException(ErrorCode.ALREADY_REGISTERED_MEMBER));
    }
}
