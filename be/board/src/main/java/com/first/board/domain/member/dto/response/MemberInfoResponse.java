package com.first.board.domain.member.dto.response;

import com.first.board.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MemberInfoResponse {
    private String memberId;
    private String koName;
    private String enName;
    private String email;
    private String phoneNumber;

    public static MemberInfoResponse from(Member member, String phoneNumber) {
        return MemberInfoResponse.builder()
                .memberId(member.getMemberId())
                .koName(member.getKoName())
                .enName(member.getEnName())
                .email(member.getEmail())
                .phoneNumber(phoneNumber)
                .build();
    }
}
