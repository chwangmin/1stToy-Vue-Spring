package com.first.board.domain.member.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModifyMemberRequest {
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$",
            message = "비밀번호는 최소 8자리이면서 영문 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다"
    )
    private String password;

    private String koName;

    private String enName;

    private String email;

    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$",
            message = "올바른 휴대폰 번호 형식이 아닙니다. (예: 01012345678)")
    private String phoneNumber;
}
