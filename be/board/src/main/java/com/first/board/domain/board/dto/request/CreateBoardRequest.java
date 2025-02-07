package com.first.board.domain.board.dto.request;

import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.type.BoardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBoardRequest {
    private String title;
    private String content;
    private BoardType boardType;

    @Builder
    public CreateBoardRequest(String title, String content, BoardType boardType) {
        this.title = title;
        this.content = content;
        this.boardType = boardType;
    }

    public Board toEntity(String memberId, String fileName, String filePath) {
        return Board.builder()
                .title(title)
                .content(content)
                .authorID(memberId)
                .fileName(fileName)
                .filePath(filePath)
                .boardType(boardType)
                .build();
    }
}
