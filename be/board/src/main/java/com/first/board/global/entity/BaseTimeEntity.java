package com.first.board.global.entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseTimeEntity {
    @BsonProperty("createDate")
    protected LocalDateTime createDate;

    @BsonProperty("modifyDate")
    protected LocalDateTime modifyDate;

    // 생성자나 save 메서드에서 호출
    protected void initCreateDate() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
        this.modifyDate = LocalDateTime.now();
    }
}

