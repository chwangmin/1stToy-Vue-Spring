package com.first.board.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "member")
@Getter
public class Member {
    @Id
    private String id;

    @NonNull
    private String memberId;

    @NonNull
    private String koName;

    @NonNull
    private String enName;

    @NonNull
    private String email;

    @NonNull
    private String birthDate;

    @NonNull
    private String phoneNumber;

    @NonNull
    private String password;

    @NonNull
    private Role role;

    @NonNull
    private Boolean isActive = true;

    @NonNull
    private short failCnt;

    @NonNull
    private String salt;

    private String refreshToken;

    @Builder
    public Member(String id, String memberId, String koName, String enName, String email, String birthDate, String phoneNumber, String password, Role role, Boolean isActive, short failCnt, String salt, String refreshToken) {
        this.id = id;
        this.memberId = memberId;
        this.koName = koName;
        this.enName = enName;
        this.email = email;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.isActive = isActive;
        this.failCnt = failCnt;
        this.salt = salt;
        this.refreshToken = refreshToken;
    }

    public void initCount() {
        this.failCnt = 0;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateCount() {
        this.failCnt+=1;
    }

    public void leave() {
        isActive = false;
    }
}
