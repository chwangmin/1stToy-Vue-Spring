package com.first.board.domain.board.dto;

import com.first.board.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardDto {
    private String id;
    private String title;
    private String content;
    private String authorID;
    private String fileName;
    private String filePath;
    private Long views;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static BoardDto from(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .authorID(board.getAuthorID())
                .fileName(board.getFileName())
                .filePath(board.getFilePath())
                .views(board.getViews())
                .createdDate(board.getCreateDate())
                .modifiedDate(board.getModifyDate())
                .build();
    }
}

