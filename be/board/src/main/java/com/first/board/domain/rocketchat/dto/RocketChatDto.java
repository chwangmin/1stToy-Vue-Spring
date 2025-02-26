package com.first.board.domain.rocketchat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.first.board.domain.rocketchat.entity.RocketChat;
import com.first.board.domain.rocketchat.entity.ScheduledMessageStatus;
import com.first.board.domain.rocketchat.entity.WeekType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RocketChatDto {
    private String id;
    private Integer sqlId;
    private Integer memberId;
    private List<WeekType> week;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    private LocalTime time;
    private String icon;
    private String message;
    private Boolean isGpt;
    private String XAuthToken;
    private String XUserId;
    private Boolean isConnectable;
    private ScheduledMessageStatus status;
    private String roomId;

    @Builder
    public RocketChatDto(String id, Integer sqlId, Integer memberId, List<WeekType> week,
                         LocalDate date, LocalTime time, String icon,
                         String message, Boolean isGpt, String XAuthToken,
                         String XUserId, Boolean isConnectable, ScheduledMessageStatus status, String roomId) {
        this.id = id;
        this.sqlId = sqlId;
        this.memberId = memberId;
        this.week = week;
        this.date = date;
        this.time = time;
        this.icon = icon;
        this.message = message;
        this.isGpt = isGpt;
        this.XAuthToken = XAuthToken;
        this.XUserId = XUserId;
        this.isConnectable = isConnectable;
        this.status = status;
        this.roomId = roomId;
    }

    public static RocketChatDto from(RocketChat rocketChat) {
        return RocketChatDto.builder()
                .id(rocketChat.getIdtoString())
                .memberId(rocketChat.getMemberId())
                .week(rocketChat.getWeek())
                .date(rocketChat.getDate())
                .time(rocketChat.getTime())
                .icon(rocketChat.getIcon())
                .message(rocketChat.getMessage())
                .isGpt(rocketChat.getIsGpt())
                .XAuthToken(rocketChat.getXAuthToken())
                .XUserId(rocketChat.getXUserId())
                .isConnectable(rocketChat.getIsConnectable())
                .status(rocketChat.getStatus())
                .roomId(rocketChat.getRoomId())
                .build();
    }

}
