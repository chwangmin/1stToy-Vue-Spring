package com.first.board.external.rocketchat.feign;

import com.first.board.external.rocketchat.dto.request.RocketChatMessageRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "https://dev.lutes.co.kr", name = "rocketChatMessageClient")
public interface RocketChatAPI {

    @PostMapping(value = "/api/v1/chat.sendMessage")
    void sendMessage(
            @RequestBody RocketChatMessageRequest rocketChatMessageRequest,
            @RequestHeader(value = "X-Auth-Token") String authToken,
            @RequestHeader(value = "X-User-Id") String userId
    );

    @PostMapping(value = "/api/v1/chat.sendMessage")
    void sendScheduledMessage(
            @RequestBody RocketChatMessageRequest rocketChatMessageRequest,
            @RequestHeader(value = "X-Auth-Token") String authToken,
            @RequestHeader(value = "X-User-Id") String userId
    );
}
