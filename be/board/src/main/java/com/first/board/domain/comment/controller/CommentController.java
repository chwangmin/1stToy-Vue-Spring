package com.first.board.domain.comment.controller;

import com.first.board.domain.comment.dto.request.CreateCommentRequest;
import com.first.board.domain.comment.dto.request.ModifyCommentRequest;
import com.first.board.domain.comment.dto.response.GetCommentResponse;
import com.first.board.domain.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board/{boardId}/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @Tag(name = "comment")
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @PostMapping
    public ResponseEntity<Void> createComment(
            @AuthenticationPrincipal String memberId,
            @PathVariable String boardId,
            @RequestBody CreateCommentRequest createCommentRequest
    ) {
        commentService.createComment(memberId, boardId, createCommentRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "comment")
    @Operation(summary = "댓글 조회", description = "게시글의 모든 댓글을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<GetCommentResponse>> getComments(
            @PathVariable String boardId,
            @RequestParam(defaultValue = "0") int page
    ) {
        List<GetCommentResponse> comments = commentService.getComments(boardId, page);
        return ResponseEntity.ok(comments);
    }

    @Tag(name = "comment")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @PutMapping("/{commentId}")
    public ResponseEntity<Void> modifyComment(
            @AuthenticationPrincipal String memberId,
            @PathVariable String boardId,
            @PathVariable String commentId,
            @RequestBody ModifyCommentRequest modifyCommentRequest
    ) {
        commentService.modifyComment(memberId, commentId, modifyCommentRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "comment")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @AuthenticationPrincipal String memberId,
            @PathVariable String boardId,
            @PathVariable String commentId
    ) {
        commentService.deleteComment(memberId, commentId);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "comment")
    @Operation(summary = "대댓글 작성", description = "댓글에 답글을 작성합니다.")
    @PostMapping("/{commentId}/reply")
    public ResponseEntity<Void> createReply(
            @AuthenticationPrincipal String memberId,
            @PathVariable String boardId,
            @PathVariable String commentId,
            @RequestBody CreateCommentRequest createCommentRequest
    ) {
        commentService.createReply(memberId, boardId, commentId, createCommentRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "comment")
    @Operation(summary = "대댓글 조회", description = "댓글의 모든 대댓글을 조회합니다.")
    @GetMapping("/{commentId}/reply")
    public ResponseEntity<List<GetCommentResponse>> getReplies(
            @PathVariable String boardId,
            @PathVariable String commentId
    ) {
        List<GetCommentResponse> replies = commentService.getReplies(boardId, commentId);
        return ResponseEntity.ok(replies);
    }
}
