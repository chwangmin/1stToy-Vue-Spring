package com.first.board.domain.board.dto.request;

import com.first.board.domain.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateBoardRequest {
    private String title;
    private String content;
    private String fileName;
    private String filePath;


    public Board toEntity(String memberId) {
        return Board.builder()
                .title(title)
                .content(content)
                .authorID(memberId)
                .fileName(fileName)
                .filePath(filePath)
                .build();
    }
}
