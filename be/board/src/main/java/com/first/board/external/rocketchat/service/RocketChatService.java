package com.first.board.external.rocketchat.service;

import com.first.board.external.rocketchat.dto.Attachment;
import com.first.board.external.rocketchat.dto.Message;
import com.first.board.external.rocketchat.dto.request.RocketChatMessageRequest;
import com.first.board.external.rocketchat.feign.RocketChatAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RocketChatService {
    private final RocketChatAPI rocketChatAPI;

    private static final String DEFAULT_EMOJI = ":thinking:";
    private static final String GREETING_EMOJI = ":raising_hand: ";
    private static final String HELP_MESSAGE = "님의 궁금증을 해결해주세요!";
    private static final String VIEW_DETAILS_LINK = " - [[View Details]](http://lutesinfo.shop/question)";

    private static final String ATTACH_COLOR = "#007bff";
    private static final String MESSAGE_HEADER = "질문 : ";

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

        rocketChatAPI.sendMessage(chatMessage);
    }
}

