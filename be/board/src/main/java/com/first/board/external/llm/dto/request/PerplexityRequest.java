package com.first.board.external.llm.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PerplexityRequest {
    private String model;
    private List<Message> messages;

    @Getter
    @Setter
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}
