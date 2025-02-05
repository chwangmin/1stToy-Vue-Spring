package com.first.board.external.rocketchat.feign;

import com.first.board.external.rocketchat.dto.request.RocketChatMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "https://dev.lutes.co.kr", name = "rocketChatMessageClient")
public interface RocketChatAPI {

    @PostMapping(value = "/api/v1/chat.sendMessage")
    void sendMessage(
            @RequestBody RocketChatMessageRequest rocketChatMessageRequest
    );
}
