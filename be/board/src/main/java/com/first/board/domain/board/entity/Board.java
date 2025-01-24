package com.first.board.domain.board.entity;

import com.first.board.domain.board.dto.request.ModifyBoardRequest;
import com.first.board.global.entity.BaseTimeEntity;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Board extends BaseTimeEntity {
    @BsonId
    private ObjectId id;

    @BsonProperty("title")
    @NonNull
    private String title;

    @BsonProperty("content")
    @NonNull
    private String content;

    @BsonProperty("authorID")
    @NonNull
    private String authorID;

    @BsonProperty("fileName")
    private String fileName;

    @BsonProperty("filePath")
    private String filePath;

    @BsonProperty("views")
    @NonNull
    private Long views;

    @Builder
    public Board(String title, String content, String authorID, String fileName, String filePath, LocalDateTime createDate, LocalDateTime modifyDate) {
        this.id = new ObjectId();
        this.title = title;
        this.content = content;
        this.authorID = authorID;
        this.fileName = fileName;
        this.filePath = filePath;
        this.views = 0L;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        initCreateDate();
    }

    public void modify(ModifyBoardRequest modifyBoardRequest) {
        this.title = modifyBoardRequest.getTitle();
        this.content = modifyBoardRequest.getContent();
        this.fileName = modifyBoardRequest.getFileName();
        this.filePath = modifyBoardRequest.getFilePath();
        initCreateDate();
    }

    public void view() {
        this.views++;
    }


    public String getIdtoString() {
        return id != null ? id.toHexString() : null;
    }
}
