package com.first.board.domain.board.controller;

import com.first.board.domain.board.dto.request.BoardCreateDto;
import com.first.board.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @Operation(summary="게시판 게시글 생성", description = "게시판 게시글을 생성합니다.")
    @PostMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> createBoard(@AuthenticationPrincipal String memberId, @RequestBody BoardCreateDto boardCreateRequest) {
        boardService.createBoard(memberId, boardCreateRequest);
        return ResponseEntity.ok().build();
    }
}
