package com.first.board.domain.rocketchat.dto;

import com.first.board.domain.rocketchat.entity.RocketChat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
public class RocketChatDto {
    private String id;
    private String pk;
    private Integer memberId;
    private Short week;
    private Boolean loop;
    private LocalTime time;
    private String icon;
    private String message;
    private Boolean isGpt;
    private String XAuthToken;
    private String XUserId;
    private Boolean isConnectable;

    @Builder
    public RocketChatDto(String id, String pk, Integer memberId, Short week,
                         Boolean loop, LocalTime time, String icon,
                         String message, Boolean isGpt, String XAuthToken,
                         String XUserId, Boolean isConnectable) {
        this.id = id;
        this.pk = pk;
        this.memberId = memberId;
        this.week = week;
        this.loop = loop;
        this.time = time;
        this.icon = icon;
        this.message = message;
        this.isGpt = isGpt;
        this.XAuthToken = XAuthToken;
        this.XUserId = XUserId;
        this.isConnectable = isConnectable;
    }

    public static RocketChatDto from(RocketChat rocketChat) {
        return RocketChatDto.builder()
                .id(rocketChat.getIdtoString())
                .pk(rocketChat.getPktoString())
                .memberId(rocketChat.getMemberId())
                .week(rocketChat.getWeek())
                .loop(rocketChat.getLoop())
                .time(rocketChat.getTime())
                .icon(rocketChat.getIcon())
                .message(rocketChat.getMessage())
                .isGpt(rocketChat.getIsGpt())
                .XAuthToken(rocketChat.getXAuthToken())
                .XUserId(rocketChat.getXUserId())
                .isConnectable(rocketChat.getIsConnectable())
                .build();
    }

}
