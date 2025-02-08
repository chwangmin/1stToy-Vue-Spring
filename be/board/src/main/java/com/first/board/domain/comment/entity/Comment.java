package com.first.board.domain.comment.entity;

import com.first.board.global.entity.BaseTimeEntity;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Comment extends BaseTimeEntity {
    @BsonId
    private ObjectId id;

    @BsonProperty("content")
    @NonNull
    private String content;

    @BsonProperty("authorId")
    @NonNull
    private String authorId;

    @BsonProperty("authorName")
    @NonNull
    private String authorName;

    @BsonProperty("boardId")
    @NonNull
    private String boardId;

    @BsonProperty("parentId")
    private String parentId;  // 부모 댓글 ID

    @BsonProperty("isReply")
    private Boolean isReply;      // 댓글 깊이 (0: 일반 댓글, 1: 답글)

    @BsonProperty("replyNum")
    private Integer replyNum;

    @Builder
    public Comment(String content, String authorId, String boardId,
                   String parentId, Boolean isReply, String authorName,
                   LocalDateTime createDate, LocalDateTime modifyDate) {
        this.id = new ObjectId();
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.boardId = boardId;
        this.parentId = parentId;
        this.isReply = isReply;
        this.replyNum = 0;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
        initCreateDate();
    }

    public void modify(String content) {
        this.content = content;
        initCreateDate();
    }

    public String getIdtoString() {
        return id != null ? id.toHexString() : null;
    }
}

