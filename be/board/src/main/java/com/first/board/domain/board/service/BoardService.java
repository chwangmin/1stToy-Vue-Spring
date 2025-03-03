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
import com.first.board.domain.board.type.BoardType;
import com.first.board.domain.board.type.SortType;
import com.first.board.domain.member.adaptor.MemberAdaptor;
import com.first.board.domain.member.entity.Member;
import com.first.board.external.rocketchat.service.RocketChatAPIService;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import com.first.board.global.json.ReadJsonFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
@Slf4j
// @Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardAdaptor boardAdaptor;
    private final ReadJsonFile readJsonFile;
    private final RocketChatAPIService rocketChatAPIService;
    private final MemberAdaptor memberAdaptor;

    // @Transactional
    public void createBoard(String memberId, CreateBoardRequest boardCreateRequest, MultipartFile file) throws IOException {
        String UUIDFileName = saveFile(file);
        String realFileName = null;

        if (UUIDFileName != null){
            realFileName = file.getOriginalFilename();
        }

        if(boardCreateRequest.getBoardType().isQuestion()){
            Member member = memberAdaptor.findByMemberId(memberId);
            rocketChatAPIService.sendMessage(boardCreateRequest.getTitle(), member.getKoName());
        }

        Board board = boardCreateRequest.toEntity(memberId, realFileName, UUIDFileName);
        boardRepository.save(board);
    }

    public GetBoardsResponse getBoards(String keyword, int page, SortType sort, BoardType boardType) {
        List<Board> boards = boardRepository.searchBoards(keyword, page, sort, boardType);

        List<BoardDto> boardDtos = new ArrayList<>();

        for (Board board : boards) {
            boardDtos.add(BoardDto.from(board));
        }

        long maxBoardNum = (boardRepository.findMaxNum(keyword, boardType) - 1) / 10 + 1;

        return GetBoardsResponse.builder()
                .boards(boardDtos)
                .currentPage(page + 1)
                .maxBoardNum(maxBoardNum)
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

        String UUIDFileName = saveFile(file);

        if (UUIDFileName != null){
            String realFileName = file.getOriginalFilename();
            modifyBoardRequest.setFileName(realFileName);
            modifyBoardRequest.setFilePath(UUIDFileName);
        }

        board.modify(modifyBoardRequest);
        boardRepository.modifyBoard(board);
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String boardPath = System.getProperty("user.dir") + BoardConstant.File.FILE_PATH;

            // 디렉토리 생성 코드 추가
            File directory = new File(boardPath);
            if (!directory.exists()) {
                directory.mkdirs(); // 여러 단계의 디렉토리를 한 번에 생성
            }

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();
            File saveFile = new File(boardPath, fileName);
            file.transferTo(saveFile);

            return fileName;
        }
        return null;
    }

    // @Transactional
    public void deleteBoard(String memberId, String boardId) {
        Board board = boardAdaptor.findByBoardId(boardId);

        //todo filter로 이동 예정
        if (!Objects.equals(memberId, board.getAuthorID())){
            throw new BusinessException(ErrorCode.CANNOT_MODIFY_BOARD_YOU_NOT_CREATE);
        }

        boardRepository.delete(board);
    }

    // @Transactional
    public void viewBoard(String boardId) {
        Board board = boardAdaptor.findByBoardId(boardId);
        board.view();
        boardRepository.view(board);
    }

    // @Transactional
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
                    .fileName(jsonObject.has("fileName") ? jsonObject.getString("fileName") : null)
                    .filePath(jsonObject.has("filePath") ? jsonObject.getString("filePath") : null)
                    .views(Long.parseLong(jsonObject.getJSONObject("views").getString("$numberLong")))
                    .boardType(BoardType.valueOf(jsonObject.getString("boardType")))
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

    public Resource fileDownloadBoard(String filePath) throws MalformedURLException {
        return new UrlResource(Paths.get(System.getProperty("user.dir")+ BoardConstant.File.FILE_PATH + filePath).toUri());
    }
}
