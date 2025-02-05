package com.first.board.external.rocketchat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Attachment {
    private String color;
    private String text;
}
