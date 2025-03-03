package com.first.board.domain.rocketchat.adaptor;

import com.first.board.domain.rocketchat.entity.RocketChat;
import com.first.board.domain.rocketchat.entity.ScheduledMessageStatus;
import com.first.board.domain.rocketchat.repository.RocketChatRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RocketChatAdaptor {
    private final RocketChatRepository rocketChatRepository;

    public RocketChat findById(String id) {
        return rocketChatRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROCKET_CHAT_NOT_FOUND));
    }

    public List<RocketChat> findAllByStatus(ScheduledMessageStatus status) {
        return rocketChatRepository.findAllByStatus(ScheduledMessageStatus.PENDING);
    }
}
