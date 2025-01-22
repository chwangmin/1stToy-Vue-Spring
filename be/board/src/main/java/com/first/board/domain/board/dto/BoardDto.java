package com.first.board.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.first.board.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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

    @Builder
    public BoardDto(String id, String title, String content, String authorID, String fileName, String filePath, Long views, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorID = authorID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.views = views;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

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

    public Board toEntity() {
        return Board.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .authorID(this.authorID)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .createDate(this.createdDate)
                .modifyDate(this.modifiedDate)
                .build();
    }

}

