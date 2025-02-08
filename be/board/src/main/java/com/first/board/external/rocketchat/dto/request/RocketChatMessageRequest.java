package com.first.board.external.rocketchat.dto.request;

import com.first.board.external.rocketchat.dto.Message;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RocketChatMessageRequest {
    private Message message;
}
