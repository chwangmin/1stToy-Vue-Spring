package com.first.board.domain.board.fixture;

import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.type.BoardType;

import java.time.LocalDateTime;

public class BoardFixture {
    public static final String INIT_BOARD_TITLE = "테스트 게시글";
    public static final String INIT_BOARD_CONTENT = "테스트 내용입니다.";

    public static Board create(String memberId) {
        return Board.builder()
                .title(INIT_BOARD_TITLE)
                .content(INIT_BOARD_CONTENT)
                .authorID(memberId)
                .fileName("test_file.txt")
                .filePath("/uploads/test_file.txt")
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .boardType(BoardType.OPEN)
                .build();
    }
}
