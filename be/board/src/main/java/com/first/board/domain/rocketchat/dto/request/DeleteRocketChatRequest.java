package com.first.board.domain.rocketchat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRocketChatRequest {
    private String xUserId;
    private String rocketChatId;
}
