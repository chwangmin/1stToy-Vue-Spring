package com.first.board.domain.rocketchat.dto.response;

import com.first.board.domain.rocketchat.dto.RocketChatDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GetRocketChatsResponse {
    private List<RocketChatDto> rocketChats;
}
