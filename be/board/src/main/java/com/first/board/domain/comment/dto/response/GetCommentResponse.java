package com.first.board.domain.comment.dto.response;

import com.first.board.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetCommentResponse {
    private String id;
    private String content;
    private String authorId;
    private String boardId;
    private String parentId;
    private Boolean isReply;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Integer replyNum;

    public static GetCommentResponse from(Comment comment) {
        return GetCommentResponse.builder()
                .id(comment.getIdtoString())
                .content(comment.getContent())
                .authorId(comment.getAuthorId())
                .boardId(comment.getBoardId())
                .parentId(comment.getParentId())
                .isReply(comment.getIsReply())
                .createDate(comment.getCreateDate())
                .modifyDate(comment.getModifyDate())
                .replyNum(comment.getReplyNum())
                .build();
    }
}

