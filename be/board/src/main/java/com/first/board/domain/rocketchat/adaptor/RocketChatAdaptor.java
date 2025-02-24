package com.first.board.domain.rocketchat.adaptor;

import com.first.board.domain.rocketchat.entity.RocketChat;
import com.first.board.domain.rocketchat.repository.RocketChatRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RocketChatAdaptor {
    private final RocketChatRepository rocketChatRepository;

    public RocketChat findById(ObjectId id) {
        return rocketChatRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROKCKET_CHAT_NOT_FOUND));
    }
}
