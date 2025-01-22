package com.first.board.domain.board.entity;

import com.first.board.domain.board.dto.request.ModifyBoardRequest;
import com.first.board.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "board")
@Getter
public class Board extends BaseTimeEntity {
    @Id
    private String id;

    @NonNull
    private String title;

    @NonNull
    private String content;

    @NonNull
    private String authorID;

    private String fileName;

    private String filePath;

    @NonNull
    private Long views;

    @Builder
    public Board(String id, String title, String content, String authorID, String fileName, String filePath, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorID = authorID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.views = 0L;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    public void modify(ModifyBoardRequest modifyBoardRequest) {
        this.title = modifyBoardRequest.getTitle();
        this.content = modifyBoardRequest.getContent();
        this.fileName = modifyBoardRequest.getFileName();
        this.filePath = modifyBoardRequest.getFilePath();
    }

    public void view() {
        this.views++;
    }
}
