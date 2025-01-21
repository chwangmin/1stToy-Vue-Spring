package com.first.board.domain.board.service;

import com.first.board.domain.board.constant.BoardConstant;
import com.first.board.domain.board.dto.BoardDto;
import com.first.board.domain.board.dto.request.BoardCreateDto;
import com.first.board.domain.board.dto.response.GetBoardsResponse;
import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.repository.BoardRepository;
import com.first.board.domain.board.type.SortType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository boardRepository;

    public void createBoard(String memberId, BoardCreateDto boardCreateRequest, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String boardPath = System.getProperty("user.dir") + BoardConstant.File.FILE_PATH;

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(boardPath, fileName);
            file.transferTo(saveFile);

            boardCreateRequest.setFileName(fileName);
            boardCreateRequest.setFilePath("/files/" + fileName);
        }

        Board board = boardCreateRequest.toEntity(memberId);
        boardRepository.save(board);
    }

    public GetBoardsResponse getBoards(String keyword, int page, SortType sort) {
        Pageable pageable = PageRequest.of(page, BoardConstant.Page.DEFAULT_SIZE, sort.getSort());

        Page<Board> boardPage;
        if (StringUtils.hasText(keyword)) {
            boardPage = boardRepository.findByTitleContainingOrContentContaining(
                    keyword,
                    keyword,
                    pageable
            );
        } else {
            boardPage = boardRepository.findAll(pageable);
        }

        List<BoardDto> boardDtos = boardPage.getContent().stream()
                .map(BoardDto::from)
                .collect(Collectors.toList());

        return GetBoardsResponse.builder()
                .boards(boardDtos)
                .totalPages(boardPage.getTotalPages())
                .totalElements(boardPage.getTotalElements())
                .currentPage(page + 1)
                .build();
    }

}
