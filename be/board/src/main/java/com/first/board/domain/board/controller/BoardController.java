package com.first.board.domain.board.controller;

import com.first.board.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @Operation(summary="게시판 게시글 전체 조회", description = "게시판 게시글을 전체 조회합니다.")
    @GetMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getBoardAll() {
        return ResponseEntity.noContent().build();
    }
}
