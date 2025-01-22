package com.first.board.domain.board.adaptor;

import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.repository.BoardRepository;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardAdaptor {
    private final BoardRepository boardRepository;

    public Board findByBoardId(String boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
    }
}
