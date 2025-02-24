package com.first.board.domain.rocketchat.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RocketChat {
    @BsonId
    private ObjectId id;

    @BsonProperty("pk")
    private ObjectId pk;

    @BsonProperty("memberId")
    @NonNull
    private Integer memberId;

    @BsonProperty("week")
    @NonNull
    private Short week;

    @BsonProperty("loop")
    @NonNull
    private Boolean loop;

    @BsonProperty("time")
    @NonNull
    private LocalTime time;

    @BsonProperty("icon")
    @NonNull
    private String icon;

    @BsonProperty("message")
    @NonNull
    private String message;

    @BsonProperty("isGpt")
    @NonNull
    private Boolean isGpt;

    @BsonProperty("XAuthToken")
    @NonNull
    private String XAuthToken;

    @BsonProperty("XUserId")
    @NonNull
    private String XUserId;

    @BsonProperty("isConnectable")
    @NonNull
    private Boolean isConnectable;

    @Builder
    public RocketChat(Integer memberId, Short week, Boolean loop,
                      LocalTime time, String icon, String message,
                      Boolean isGpt, String XAuthToken, String XUserId) {
        this.id = new ObjectId();
        this.pk = new ObjectId();
        this.memberId = memberId;
        this.week = week;
        this.loop = loop;
        this.time = time;
        this.icon = icon;
        this.message = message;
        this.isGpt = isGpt;
        this.XAuthToken = XAuthToken;
        this.XUserId = XUserId;
        this.isConnectable = true;
    }

    public String getIdtoString() {
        return id != null ? id.toHexString() : null;
    }

    public String getPktoString() {
        return pk != null ? pk.toHexString() : null;
    }
}
