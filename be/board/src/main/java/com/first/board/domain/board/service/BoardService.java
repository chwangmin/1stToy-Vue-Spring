package com.first.board.domain.board.service;

import com.first.board.domain.board.adaptor.BoardAdaptor;
import com.first.board.domain.board.constant.BoardConstant;
import com.first.board.domain.board.dto.BoardDto;
import com.first.board.domain.board.dto.request.CreateBoardRequest;
import com.first.board.domain.board.dto.request.ModifyBoardRequest;
import com.first.board.domain.board.dto.response.GetBoardResponse;
import com.first.board.domain.board.dto.response.GetBoardsResponse;
import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.repository.BoardRepository;
import com.first.board.domain.board.type.SortType;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import com.first.board.global.json.ReadJsonFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardAdaptor boardAdaptor;
    private final ReadJsonFile readJsonFile;

    @Transactional
    public void createBoard(String memberId, CreateBoardRequest boardCreateRequest, MultipartFile file) throws IOException {
        String fileName = saveFile(file);

        if (fileName != null){
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

    public GetBoardResponse getBoard(String boardId) {
        Board board = boardAdaptor.findByBoardId(boardId);
        BoardDto boardDto = BoardDto.from(board);
        return GetBoardResponse.builder()
                .boardDto(boardDto)
                .build();
    }

    public void modifyBoard(String memberId, String boardId, ModifyBoardRequest modifyBoardRequest, MultipartFile file) throws IOException {
        Board board = boardAdaptor.findByBoardId(boardId);

        //todo filter로 이동 예정
        if (!Objects.equals(memberId, board.getAuthorID())){
            throw new BusinessException(ErrorCode.CANNOT_MODIFY_BOARD_YOU_NOT_CREATE);
        }

        String fileName = saveFile(file);

        if (saveFile(file) != null){
            modifyBoardRequest.setFileName(fileName);
            modifyBoardRequest.setFilePath("/files/" + fileName);
        }

        board.modify(modifyBoardRequest);
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String boardPath = System.getProperty("user.dir") + BoardConstant.File.FILE_PATH;

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(boardPath, fileName);
            file.transferTo(saveFile);

            return fileName;
        }
        return null;
    }

    @Transactional
    public void deleteBoard(String memberId, String boardId) {
        Board board = boardAdaptor.findByBoardId(boardId);

        //todo filter로 이동 예정
        if (!Objects.equals(memberId, board.getAuthorID())){
            throw new BusinessException(ErrorCode.CANNOT_MODIFY_BOARD_YOU_NOT_CREATE);
        }

        boardRepository.delete(board);
    }

    @Transactional
    public void viewBoard(String boardId) {
        Board board = boardAdaptor.findByBoardId(boardId);
        board.view();
    }

    @Transactional
    public void jsonBoard(MultipartFile jsonFile) {
        JSONArray array = readJsonFile.readArrays(jsonFile);
        List<BoardDto> boards = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            BoardDto boardDto = BoardDto.builder()
                    .id(jsonObject.getJSONObject("_id").getString("$oid"))
                    .title(jsonObject.getString("title"))
                    .content(jsonObject.getString("content"))
                    .authorID(jsonObject.getString("authorID"))
                    .fileName(jsonObject.getString("fileName"))
                    .filePath(jsonObject.getString("filePath"))
                    .views(Long.parseLong(jsonObject.getJSONObject("views").getString("$numberLong")))
                    .createdDate(LocalDateTime.parse(jsonObject.getJSONObject("createDate")
                            .getString("$date").replace("Z", "")))
                    .modifiedDate(LocalDateTime.parse(jsonObject.getJSONObject("modifyDate")
                            .getString("$date").replace("Z", "")))
                    .build();
            boards.add(boardDto);
        }

        for (BoardDto boardDto : boards) {
            Board board = boardDto.toEntity();
            boardRepository.save(board);
        }
    }

    public Resource fileDownloadBoard(String fileName) throws MalformedURLException {
        return new UrlResource(Paths.get(System.getProperty("user.dir")+ BoardConstant.File.FILE_PATH + fileName).toUri());
    }
}
