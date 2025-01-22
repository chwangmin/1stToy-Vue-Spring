package com.first.board.domain.board.dto.response;

import com.first.board.domain.board.dto.BoardDto;
import com.first.board.domain.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
public class GetBoardResponse {
    BoardDto boardDto;
}
