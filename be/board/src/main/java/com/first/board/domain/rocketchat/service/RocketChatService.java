package com.first.board.domain.rocketchat.service;

import com.first.board.batch.service.TaskManager;
import com.first.board.domain.rocketchat.adaptor.RocketChatAdaptor;
import com.first.board.domain.rocketchat.dto.RocketChatDto;
import com.first.board.domain.rocketchat.dto.request.CreateRocketChatRequest;
import com.first.board.domain.rocketchat.dto.request.DeleteRocketChatRequest;
import com.first.board.domain.rocketchat.dto.request.ModifyRocketChatRequest;
import com.first.board.domain.rocketchat.dto.response.GetNumberTodayResponse;
import com.first.board.domain.rocketchat.dto.response.GetRocketChatsResponse;
import com.first.board.domain.rocketchat.entity.RocketChat;
import com.first.board.domain.rocketchat.repository.RocketChatRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import com.first.board.global.mongodb.MongoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RocketChatService {
    private final RocketChatRepository rocketChatRepository;
    private final RocketChatAdaptor rocketChatAdaptor;
    private final TaskManager taskManager;
    private final MongoUtil mongoUtil;

    public void createRocketChat(CreateRocketChatRequest createRocketChatRequest) {
        if (rocketChatRepository.findCountByXUserId(createRocketChatRequest.getXUserId()) >= 50) {
            throw new BusinessException(ErrorCode.ROCKET_CHAT_CANNOT_CREATE_OVER_MAX);
        }

        RocketChat rocketChat = createRocketChatRequest.toEntity();
        rocketChat = rocketChatRepository.save(rocketChat);
        taskManager.addTask(rocketChat);
    }

    public GetRocketChatsResponse getRocketChats(String XUserId) {
        List<RocketChat> rocketChats = rocketChatRepository.findByXUserId(XUserId);

        List<RocketChatDto> rocketChatDtos = new ArrayList<>();

        for (RocketChat rocketChat : rocketChats) {
            rocketChatDtos.add(RocketChatDto.from(rocketChat));
        }

        return GetRocketChatsResponse.builder()
                .rocketChats(rocketChatDtos)
                .build();
    }

    public void modifyRocketChat(ModifyRocketChatRequest modifyRocketChatRequest) {
        RocketChat rocketChat = modifyRocketChatRequest.toEntity();
        rocketChat.setId(mongoUtil.ConvertStringToObjectId(modifyRocketChatRequest.getId()));

        taskManager.removeTask(rocketChat);

        rocketChat = rocketChatRepository.update(modifyRocketChatRequest);
        taskManager.addTask(rocketChat);
    }

    public void deleteRocketChat(DeleteRocketChatRequest deleteRocketChatRequest) {
        RocketChat rocketChat = rocketChatAdaptor.findById(deleteRocketChatRequest.getRocketChatId()); // valid 확인
        taskManager.removeTask(rocketChat);
        rocketChatRepository.deleteById(deleteRocketChatRequest.getRocketChatId());
    }

    public StringBuilder allGetRocketChat() {
        return taskManager.getAllTask();
    }

    public GetNumberTodayResponse checkNumberTodayRocketChat(String XUserId) {
        return taskManager.checkNumberTodayRocketChat(XUserId);
    }
}
