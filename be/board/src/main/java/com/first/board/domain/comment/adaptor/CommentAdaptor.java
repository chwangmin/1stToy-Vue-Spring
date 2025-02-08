package com.first.board.domain.comment.adaptor;

import com.first.board.domain.comment.entity.Comment;
import com.first.board.domain.comment.repository.CommentRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentAdaptor {
    private final CommentRepository commentRepository;

    public Comment findByCommentId(String commentId){
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
