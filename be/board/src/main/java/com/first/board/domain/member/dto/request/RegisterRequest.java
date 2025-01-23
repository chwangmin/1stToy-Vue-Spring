package com.first.board.domain.member.dto.request;

import com.first.board.domain.member.entity.Member;
import com.first.board.domain.member.entity.Role;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    private String memberId;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "비밀번호는 최소 8자리이면서 영문 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다"
    )
    private String password;

    private String koName;

    private String enName;

    private String email;

    @Past(message = "생년월일은 과거 날짜만 가능합니다")
    private LocalDate birthdate;

    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$",
            message = "올바른 휴대폰 번호 형식이 아닙니다. (예: 01012345678)")
    private String phoneNumber;

    public Member toEntity(String encryptPassword, String encryptBirthDate, String encryptPhoneNumber, String salt) {
        return Member.builder()
                .memberId(memberId)
                .encryptPassword(encryptPassword)
                .koName(koName)
                .enName(enName)
                .email(email)
                .encryptBirthDate(encryptBirthDate)
                .encryptPhoneNumber(encryptPhoneNumber)
                .salt(salt)
                .build();
    }
}
