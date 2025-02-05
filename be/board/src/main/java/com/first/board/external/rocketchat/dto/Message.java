package com.first.board.external.rocketchat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Message {
    private String rid;
    private String msg;
    private String emoji;
    private List<Attachment> attachments;
}
