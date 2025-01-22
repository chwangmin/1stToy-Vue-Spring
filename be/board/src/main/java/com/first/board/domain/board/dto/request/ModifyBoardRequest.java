package com.first.board.domain.board.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModifyBoardRequest {
    String title;
    String content;
    String fileName;
    String filePath;
}
