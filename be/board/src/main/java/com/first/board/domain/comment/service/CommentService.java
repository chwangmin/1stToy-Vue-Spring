package com.first.board.domain.comment.service;

import com.first.board.domain.board.adaptor.BoardAdaptor;
import com.first.board.domain.comment.adaptor.CommentAdaptor;
import com.first.board.domain.comment.constant.CommentConstant;
import com.first.board.domain.comment.dto.request.CreateCommentRequest;
import com.first.board.domain.comment.dto.request.ModifyCommentRequest;
import com.first.board.domain.comment.dto.response.GetCommentResponse;
import com.first.board.domain.comment.entity.Comment;
import com.first.board.domain.comment.repository.CommentRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import com.first.board.global.mongodb.MongoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardAdaptor boardAdaptor;
    private final CommentAdaptor commentAdaptor;
    private final CommentRepository commentRepository;
    private final MongoUtil mongoUtil;

    public void createComment(String memberId, String boardId, CreateCommentRequest request) {
        validateBoard(boardId);

        Comment comment = Comment.builder()
                .content(request.getContent())
                .authorId(memberId)
                .boardId(boardId)
                .isReply(false)
                .build();

        commentRepository.save(comment);
    }

    public List<GetCommentResponse> getComments(String boardId, int page) {
        validateBoard(boardId);

        List<Comment> comments = commentRepository.findByBoardIdWithPagination(boardId, page, CommentConstant.Page.DEFAULT_SIZE);

        return comments.stream()
                .filter(comment -> !comment.getIsReply())  // 최상위 댓글만 필터링
                .map(GetCommentResponse::from)
                .collect(Collectors.toList());
    }

    public void modifyComment(String memberId, String commentId, ModifyCommentRequest request) {
        Comment comment = findCommentAndValidateAuthor(commentId, memberId);

        comment.modify(request.getContent());
        commentRepository.modify(comment);
    }

    public void deleteComment(String memberId, String commentId) {
        Comment comment = findCommentAndValidateAuthor(commentId, memberId);
        commentRepository.delete(comment);
        if (comment.getIsReply()) {
            commentRepository.updateReplyNumDown(comment.getParentId());
        }
    }

    public void createReply(String memberId, String boardId, String parentCommentId, CreateCommentRequest request) {
        validateBoard(boardId);
        validateParentComment(parentCommentId);

        Comment reply = Comment.builder()
                .content(request.getContent())
                .authorId(memberId)
                .boardId(boardId)
                .parentId(parentCommentId)
                .isReply(true)
                .build();

        commentRepository.save(reply);
        commentRepository.updateReplyNumUp(parentCommentId);
    }

    private void validateBoard(String boardId) {
        boardAdaptor.findByBoardId(boardId);
    }

    private void validateParentComment(String parentCommentId) {
        commentAdaptor.findByCommentId(parentCommentId);
    }

    private Comment findCommentAndValidateAuthor(String commentId, String memberId) {
        Comment comment = commentAdaptor.findByCommentId(commentId);

        if (!comment.getAuthorId().equals(memberId)) {
            throw new BusinessException(ErrorCode.CANNOT_UPDATE_COMMENT_YOU_NOT_CREATE);
        }

        return comment;
    }

    public List<GetCommentResponse> getReplies(String boardId, String parentId) {
        validateBoard(boardId);
        validateParentComment(parentId);

        List<Comment> replies = commentRepository.findByParentId(parentId);

        return replies.stream()
                .map(GetCommentResponse::from)
                .collect(Collectors.toList());
    }
}

