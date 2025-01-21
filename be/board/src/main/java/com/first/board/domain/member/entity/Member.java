package com.first.board.domain.member.entity;

import com.first.board.domain.member.dto.request.MemberModifyRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "member")
@Getter
public class Member {
    @Id
    private String id;

    @Indexed(unique = true)
    private String memberId;

    @NonNull
    private String koName;

    @NonNull
    private String enName;

    @NonNull
    private String email;

    @NonNull
    private String encryptBirthDate;

    @NonNull
    private String encryptPhoneNumber;

    @NonNull
    private String encryptPassword;

    @NonNull
    private Role role;

    @NonNull
    private Boolean isActive;

    @NonNull
    private short failCnt;

    @NonNull
    private String salt;

    private String refreshToken;

    @Builder
    public Member(String id, String memberId, String koName, String enName, String email, String encryptBirthDate, String encryptPhoneNumber, String encryptPassword, Role role, short failCnt, String salt, String refreshToken) {
        this.id = id;
        this.memberId = memberId;
        this.koName = koName;
        this.enName = enName;
        this.email = email;
        this.encryptBirthDate = encryptBirthDate;
        this.encryptPhoneNumber = encryptPhoneNumber;
        this.encryptPassword = encryptPassword;
        this.role = role;
        this.isActive = true;
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

    public void modify(MemberModifyRequest memberModifyRequest){
        this.encryptPassword = memberModifyRequest.getPassword();
        this.koName = memberModifyRequest.getKoName();
        this.enName = memberModifyRequest.getEnName();
        this.email = memberModifyRequest.getEmail();
        this.encryptPhoneNumber = memberModifyRequest.getPhoneNumber();
    }
}
