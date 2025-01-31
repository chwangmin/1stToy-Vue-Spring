package com.first.board.domain.board.dto.response;

import com.first.board.domain.board.dto.BoardDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class GetBoardsResponse {
    List<BoardDto> boards;
    int currentPage;
    long maxBoardNum;
}
