package com.first.board.domain.board.controller;

import com.first.board.domain.board.dto.request.CreateBoardRequest;
import com.first.board.domain.board.dto.request.ModifyBoardRequest;
import com.first.board.domain.board.dto.response.GetBoardResponse;
import com.first.board.domain.board.dto.response.GetBoardsResponse;
import com.first.board.domain.board.service.BoardService;
import com.first.board.domain.board.type.BoardType;
import com.first.board.domain.board.type.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;

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
            @RequestPart("board") CreateBoardRequest boardCreateRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        boardService.createBoard(memberId, boardCreateRequest, file);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "board")
    @Operation(summary="게시판 페이징 조회(+ 검색, 정렬)", description = "게시판에서 게시글을 조회합니다 (검색과 정렬 포함)")
    @GetMapping
    public ResponseEntity<?> getBoards(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "CREATED_DESC") SortType sort
    ){
        GetBoardsResponse getBoardsResponse = boardService.getBoards(keyword, page, sort, null);
        return ResponseEntity.ok(getBoardsResponse);
    }

    @Tag(name = "board")
    @Operation(summary="질문 게시판 페이징 조회(+ 검색, 정렬)", description = "질문 게시판에서 게시글을 조회합니다 (검색과 정렬 포함)")
    @GetMapping("/question")
    public ResponseEntity<?> getQuestionBoards(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "CREATED_DESC") SortType sort
    ){
        GetBoardsResponse getBoardsResponse = boardService.getBoards(keyword, page, sort, BoardType.QUESTION);
        return ResponseEntity.ok(getBoardsResponse);
    }

    @Tag(name = "board")
    @Operation(summary="자유 질문 게시판 페이징 조회(+ 검색, 정렬)", description = "자유 게시판에서 게시글을 조회합니다 (검색과 정렬 포함)")
    @GetMapping("/open")
    public ResponseEntity<?> getOpenBoards(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "CREATED_DESC") SortType sort
    ){
        GetBoardsResponse getBoardsResponse = boardService.getBoards(keyword, page, sort, BoardType.OPEN);
        return ResponseEntity.ok(getBoardsResponse);
    }

    @Tag(name = "board")
    @Operation(summary="게시글 상세조회", description = "게시글에 대한 상세 조회를 합니다.")
    @GetMapping(path="/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable String boardId){
        GetBoardResponse getBoardResponse = boardService.getBoard(boardId);
        return ResponseEntity.ok(getBoardResponse);
    }

    @Tag(name = "board")
    @Operation(summary="게시글 수정", description = "게시글을 수정합니다.")
    @PutMapping(path="/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> modifyBoard(
            @AuthenticationPrincipal String memberId,
            @PathVariable String boardId,
            @RequestPart ModifyBoardRequest modifyBoardRequest,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        boardService.modifyBoard(memberId, boardId, modifyBoardRequest, file);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "board")
    @Operation(summary="게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping(path="/{boardId}")
    public ResponseEntity<Void> deleteBoard(@AuthenticationPrincipal String memberId, @PathVariable String boardId){
        boardService.deleteBoard(memberId, boardId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "board")
    @Operation(summary="조회수 증가", description = "파일을 다운로드 합니다.")
    @PostMapping(path="/{boardId}/view")
    public ResponseEntity<Void> viewBoard(@PathVariable String boardId) {
        boardService.viewBoard(boardId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "board")
    @Operation(summary="json 파일 게시글 등록", description = "json 파일로 게시글을 등록합니다.")
    @PostMapping(path="/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> jsonBoard(
            @RequestPart("file") MultipartFile jsonFile
    ) {
        boardService.jsonBoard(jsonFile);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "board")
    @Operation(summary="파일 다운로드", description = "파일을 다운로드 합니다.")
    @GetMapping(path="/download/{filePath}")
    public ResponseEntity<Resource> fileDownloadBoard(@PathVariable String filePath) throws MalformedURLException {
        Resource resource = boardService.fileDownloadBoard(filePath);

        String fileName = filePath;

        if (filePath.contains("_")) {
            fileName = filePath.split("_", 2)[1];
        }

        String contentDisposition = "attachment; filename=\"" + UriUtils.encode(fileName, "UTF-8") + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
