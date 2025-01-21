package com.first.board.domain.board.service;

import com.first.board.domain.board.dto.request.BoardCreateDto;
import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    public void createBoard(String memberId, BoardCreateDto boardCreateRequest) {
        Board board = boardCreateRequest.toEntity(memberId);
        boardRepository.save(board);
    }
}
