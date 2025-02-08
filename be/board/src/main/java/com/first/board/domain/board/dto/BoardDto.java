package com.first.board.domain.board.dto;

import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.type.BoardType;
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
    private BoardType boardType;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public BoardDto(String id, String title, String content, String authorID, String fileName, String filePath, Long views, LocalDateTime createdDate, LocalDateTime modifiedDate, BoardType boardType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorID = authorID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.views = views;
        this.boardType = boardType;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public static BoardDto from(Board board) {
        return BoardDto.builder()
                .id(board.getIdtoString())
                .title(board.getTitle())
                .content(board.getContent())
                .authorID(board.getAuthorID())
                .fileName(board.getFileName())
                .filePath(board.getFilePath())
                .views(board.getViews())
                .boardType(board.getBoardType())
                .createdDate(board.getCreateDate())
                .modifiedDate(board.getModifyDate())
                .build();
    }

    public Board toEntity() {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .authorID(this.authorID)
                .fileName(this.fileName)
                .filePath(this.filePath)
                .boardType(this.boardType)
                .createDate(this.createdDate)
                .modifyDate(this.modifiedDate)
                .build();
    }

}

