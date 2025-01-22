package com.first.board.domain.member.controller;

import com.first.board.domain.member.dto.request.ModifyMemberRequest;
import com.first.board.domain.member.dto.request.RegisterRequest;
import com.first.board.domain.member.dto.response.MemberInfoResponse;
import com.first.board.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Tag(name = "member")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest registerRequest){
        memberService.register(registerRequest);

        return ResponseEntity.ok().build();
    }

    @Tag(name = "member")
    @Operation(summary = "계정 탈퇴", description = "계정을 비활성화 합니다.")
    @PostMapping(path = "/leave", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> leave(@AuthenticationPrincipal String memberId){
        memberService.leave(memberId);

        return ResponseEntity.ok().build();
    }

    @Tag(name = "member")
    @Operation(summary = "계정 정보", description = "계정 정보를 보여줍니다.")
    @PostMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> info(@AuthenticationPrincipal String memberId){
        MemberInfoResponse memberInfoResponse = memberService.info(memberId);

        return ResponseEntity.ok(memberInfoResponse);
    }

    @Tag(name = "member")
    @Operation(summary = "계정 변경", description = "계정 정보를 변경합니다.")
    @PostMapping(path = "/modify", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> modify(@AuthenticationPrincipal String memberId, @RequestBody @Valid ModifyMemberRequest modifyMemberRequest){
        memberService.modify(memberId, modifyMemberRequest);

        return ResponseEntity.ok().build();
    }
}
