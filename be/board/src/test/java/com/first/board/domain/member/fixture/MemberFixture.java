package com.first.board.domain.member.fixture;

import com.first.board.domain.member.entity.Member;
import com.first.board.domain.member.entity.Role;

public class MemberFixture {
    public static final String INIT_MEMBER_ID = "testUser";
    public static final String INIT_MEMBER_EN_PW = "hashedPassword123";

    public static Member create() {
        return Member.builder()
                .memberId(INIT_MEMBER_ID)
                .koName("홍길동")
                .enName("Hong Gil Dong")
                .email("test@example.com")
                .encryptBirthDate("19900101")
                .encryptPhoneNumber("01012345678")
                .encryptPassword(INIT_MEMBER_EN_PW)
                .role(Role.USER)
                .failCnt((short) 0)
                .salt("testSalt")
                .refreshToken("testRefreshToken")
                .build();
    }
}
