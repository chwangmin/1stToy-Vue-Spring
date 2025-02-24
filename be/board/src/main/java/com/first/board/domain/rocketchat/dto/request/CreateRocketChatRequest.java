package com.first.board.domain.rocketchat.dto.request;

import com.first.board.domain.rocketchat.entity.RocketChat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateRocketChatRequest {
    private Integer memberId;
    private Short week;
    private Boolean loop;
    private LocalTime time;
    private String icon;
    private String message;
    private Boolean isGpt;
    private String XAuthToken;
    private String XUserId;

    public RocketChat toEntity() {
        return RocketChat.builder()
                .memberId(this.memberId)
                .week(this.week)
                .loop(this.loop)
                .time(this.time)
                .icon(this.icon)
                .message(this.message)
                .isGpt(this.isGpt)
                .XAuthToken(this.XAuthToken)
                .XUserId(this.XUserId)
                .build();
    }

}
