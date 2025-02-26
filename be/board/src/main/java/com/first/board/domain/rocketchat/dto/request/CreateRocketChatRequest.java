package com.first.board.domain.rocketchat.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.first.board.domain.rocketchat.entity.RocketChat;
import com.first.board.domain.rocketchat.entity.ScheduledMessageStatus;
import com.first.board.domain.rocketchat.entity.WeekType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateRocketChatRequest {
    private Integer memberId;
    private List<WeekType> week;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Schema(type = "string", example = "00:00:00")
    private LocalTime time;

    private String icon;
    private String message;
    private Boolean isGpt;
    private String XAuthToken;
    private String XUserId;
    private String roomId;

    public RocketChat toEntity() {
        return RocketChat.builder()
                .memberId(this.memberId)
                .week(this.week)
                .date(this.date)
                .time(this.time)
                .icon(this.icon)
                .message(this.message)
                .isGpt(this.isGpt)
                .XAuthToken(this.XAuthToken)
                .XUserId(this.XUserId)
                .status(ScheduledMessageStatus.PENDING)
                .roomId(this.roomId)
                .build();
    }

}
