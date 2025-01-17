package com.first.board.domain.board.dto.request;

import com.first.board.domain.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardCreateDto {
    private String title;
    private String content;
    private String fileUrl;

    public Board toEntity(String memberId) {
        return Board.builder()
                .title(title)
                .content(content)
                .authorID(memberId)
                .fileUrl(fileUrl)
                .build();
    }
}
