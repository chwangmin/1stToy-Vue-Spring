package com.first.board.domain.rocketchat.entity;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class RocketChat {
    @BsonId
    private ObjectId id;

    @BsonProperty("memberId")
    @NonNull
    private Integer memberId;

    @BsonProperty("week")
    @NonNull
    private List<WeekType> week;

    @BsonProperty("date")
    private LocalDate date;

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

    @BsonProperty("status")
    @NonNull
    private ScheduledMessageStatus status;

    @BsonProperty("roomId")
    @NonNull
    private String roomId;

    @Builder
    public RocketChat(Integer memberId, List<WeekType> week, LocalDate date,
                      LocalTime time, String icon, String message,
                      Boolean isGpt, String XAuthToken, String XUserId, ScheduledMessageStatus status, String roomId) {
        this.id = new ObjectId();
        this.memberId = memberId;
        this.week = week;
        this.date = date;
        this.time = time;
        this.icon = icon;
        this.message = message;
        this.isGpt = isGpt;
        this.XAuthToken = XAuthToken;
        this.XUserId = XUserId;
        this.isConnectable = true;
        this.status = status;
        this.roomId = roomId;
    }

    public String getIdtoString() {
        return id != null ? id.toHexString() : null;
    }
}
