package com.first.board.domain.rocketchat.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class DeleteRocketChatReqest {
    private ObjectId rocketChatId;
}
