package com.first.board.domain.member.entity;

import com.first.board.domain.member.dto.request.ModifyMemberRequest;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Member{
    @BsonId
    private ObjectId id;

    @BsonProperty("memberId")
    private String memberId;

    @BsonProperty("koName")
    @NonNull
    private String koName;

    @BsonProperty("enName")
    @NonNull
    private String enName;

    @BsonProperty("email")
    @NonNull
    private String email;

    @BsonProperty("encryptBirthDate")
    @NonNull
    private String encryptBirthDate;

    @BsonProperty("encryptPhoneNumber")
    @NonNull
    private String encryptPhoneNumber;

    @BsonProperty("encryptPassword")
    @NonNull
    private String encryptPassword;

    @BsonProperty("role")
    @NonNull
    private Role role;

    @BsonProperty("isActive")
    @NonNull
    private Boolean isActive;

    @BsonProperty("failCnt")
    @NonNull
    private short failCnt;

    @BsonProperty("salt")
    @NonNull
    private String salt;

    @BsonProperty("refreshToken")
    private String refreshToken;

    @Builder
    public Member(String memberId, String koName, String enName, String email,
                  String encryptBirthDate, String encryptPhoneNumber,
                  String encryptPassword, Role role, short failCnt,
                  String salt, String refreshToken) {
        this.id = new ObjectId();
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
        this.failCnt += 1;
    }

    public void leave() {
        isActive = false;
    }

    public void modify(ModifyMemberRequest modifyMemberRequest) {
        this.encryptPassword = modifyMemberRequest.getPassword();
        this.koName = modifyMemberRequest.getKoName();
        this.enName = modifyMemberRequest.getEnName();
        this.email = modifyMemberRequest.getEmail();
        this.encryptPhoneNumber = modifyMemberRequest.getPhoneNumber();
    }

    public String getIdtoString() {
        return id != null ? id.toHexString() : null;
    }

    public void modifyPassword(String newPassword) {
        this.encryptPassword = newPassword;
    }
}
