package com.first.board.domain.rocketchat.dto.response;

import com.first.board.domain.rocketchat.dto.RocketChatDto;
import lombok.Builder;

import java.util.List;

@Builder
public class GetRocketChatsResponse {
    private List<RocketChatDto> rocketChats;
}
