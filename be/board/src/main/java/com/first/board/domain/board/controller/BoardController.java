package com.first.board.domain.board.controller;

import com.first.board.domain.board.dto.request.BoardCreateDto;
import com.first.board.domain.board.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @Tag(name = "board")
    @Operation(summary="게시판 게시글 생성", description = "게시판 게시글을 생성합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createBoard(
            @AuthenticationPrincipal String memberId,
            @RequestPart("board") BoardCreateDto boardCreateRequest,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        boardService.createBoard(memberId, boardCreateRequest, file);
        return ResponseEntity.ok().build();
    }


    //todo
    @Tag(name = "board")
    @Operation(summary="게시판 페이징 조회(+ 검색, 정렬)", description = "게시판에서 게시글을 조회합니다 (검색과 정렬 포함)")
    @GetMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> getBoards(){
        return ResponseEntity.ok().build();
    }

    //todo
    @Tag(name = "board")
    @Operation(summary="게시글 상세조회", description = "게시글에 대한 상세 조회를 합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> getBoard(){
        return ResponseEntity.ok().build();
    }

    //todo
    @Tag(name = "board")
    @Operation(summary="게시글 수정", description = "게시글을 수정합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> modifyBoard(){
        return ResponseEntity.ok().build();
    }

    //todo
    @Tag(name = "board")
    @Operation(summary="게시글 삭제", description = "게시글을 삭제합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> deleteBoard(){
        return ResponseEntity.ok().build();
    }

    //todo
    @Tag(name = "board")
    @Operation(summary="json 파일 게시글 등록", description = "json 파일로 게시글을 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> jsonBoard(
            ) throws IOException {
        return ResponseEntity.ok().build();
    }
}
