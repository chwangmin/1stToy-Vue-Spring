package com.first.board.domain.member.controller;

import com.first.board.domain.member.dto.request.RegisterRequest;
import com.first.board.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
