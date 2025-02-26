package com.first.board.domain.rocketchat.service;

import com.first.board.batch.service.TaskManager;
import com.first.board.domain.rocketchat.adaptor.RocketChatAdaptor;
import com.first.board.domain.rocketchat.dto.RocketChatDto;
import com.first.board.domain.rocketchat.dto.request.CreateRocketChatRequest;
import com.first.board.domain.rocketchat.dto.request.DeleteRocketChatReqest;
import com.first.board.domain.rocketchat.dto.request.ModifyRocketChatRequest;
import com.first.board.domain.rocketchat.dto.response.GetRocketChatsResponse;
import com.first.board.domain.rocketchat.entity.RocketChat;
import com.first.board.domain.rocketchat.repository.RocketChatRepository;
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

    public void createRocketChat(CreateRocketChatRequest createRocketChatRequest) {
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
        taskManager.removeTask(rocketChat);
        rocketChatRepository.update(modifyRocketChatRequest);
        taskManager.addTask(rocketChat);
    }

    public void deleteRocketChat(DeleteRocketChatReqest deleteRocketChatRequest) {
        RocketChat rocketChat = rocketChatAdaptor.findById(deleteRocketChatRequest.getRocketChatId()); // valid 확인
        taskManager.removeTask(rocketChat);
        rocketChatRepository.deleteById(deleteRocketChatRequest.getRocketChatId());
    }

    public StringBuilder allGetRocketChat() {
        return taskManager.getAllTask();
    }
}
