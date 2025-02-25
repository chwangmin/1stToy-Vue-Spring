package com.first.board.external.rocketchat.service;

import com.first.board.external.rocketchat.dto.Attachment;
import com.first.board.external.rocketchat.dto.Message;
import com.first.board.external.rocketchat.dto.request.RocketChatMessageRequest;
import com.first.board.external.rocketchat.feign.RocketChatAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.first.board.external.rocketchat.constant.RocketChatConstant.*;

@Service
@RequiredArgsConstructor
public class RocketChatAPIService {
    private final RocketChatAPI rocketChatAPI;

    @Value("${spring.rocketchat.authtoken}")
    private String authToken;

    @Value("${spring.rocketchat.userid}")
    private String userId;

    @Value("${spring.rocketchat.roomid}")
    private String rid;

    public void sendMessage(String title, String qName) {
        RocketChatMessageRequest chatMessage = RocketChatMessageRequest.builder()
                .message(Message.builder()
                        .rid(rid)
                        .msg(GREETING_EMOJI + "`" + qName + "`" + HELP_MESSAGE + VIEW_DETAILS_LINK)
                        .emoji(DEFAULT_EMOJI)
                        .attachments(Collections.singletonList(
                                Attachment.builder()
                                        .color(ATTACH_COLOR)
                                        .text(MESSAGE_HEADER + title)
                                        .build()
                        ))
                        .build())
                .build();

        rocketChatAPI.sendMessage(chatMessage, authToken, userId);
    }
}

