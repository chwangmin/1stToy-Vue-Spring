package com.first.board.domain.board.service;

import com.first.board.domain.board.dto.request.BoardCreateDto;
import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    final private static String filePath = "/src/main/resources/static/files";

    private final BoardRepository boardRepository;

    public void createBoard(String memberId, BoardCreateDto boardCreateRequest, MultipartFile file) throws IOException {
        String boardPath = System.getProperty("user.dir") + filePath;

        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(boardPath, fileName);
        file.transferTo(saveFile);

        boardCreateRequest.setFileName(fileName);
        boardCreateRequest.setFilePath("/files/" + fileName);

        Board board = boardCreateRequest.toEntity(memberId);
        boardRepository.save(board);
    }
}
