package com.first.board.domain.board.service;

import com.first.board.config.TestConfig;
import com.first.board.domain.board.adaptor.BoardAdaptor;
import com.first.board.domain.board.dto.request.CreateBoardRequest;
import com.first.board.domain.board.dto.request.ModifyBoardRequest;
import com.first.board.domain.board.dto.response.GetBoardResponse;
import com.first.board.domain.board.dto.response.GetBoardsResponse;
import com.first.board.domain.board.entity.Board;
import com.first.board.domain.board.fixture.BoardFixture;
import com.first.board.domain.board.repository.BoardRepository;
import com.first.board.domain.board.type.BoardType;
import com.first.board.domain.board.type.SortType;
import com.first.board.domain.member.entity.Member;
import com.first.board.domain.member.fixture.MemberFixture;
import com.first.board.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
@DisplayName(("[게시글 관리]"))
public class BoardServiceTest extends TestConfig {
    private final BoardService boardService;
    private final BoardAdaptor boardAdaptor;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public BoardServiceTest(BoardService boardService, BoardRepository boardRepository, MemberRepository memberRepository, BoardAdaptor boardAdaptor) {
        this.boardService = boardService;
        this.boardAdaptor = boardAdaptor;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    private Board initBoard;
    private Member initMember;

    private final int FIRST_PAGE = 0;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAll();
        boardRepository.deleteAll();

        initMember = memberRepository.save(MemberFixture.create());

        initBoard = boardRepository.save(BoardFixture.create(initMember.getMemberId()));
    }

    @Nested
    class createBoard {
        @Test
        void 성공_게시판_생성한다() throws IOException {
            //given
            CreateBoardRequest createBoardRequest = CreateBoardRequest.builder()
                    .title("생성 테스트 게시글")
                    .content("생성 테스트 내용입니다.")
                    .boardType(BoardType.OPEN)
                    .build();

            //when
            boardService.createBoard(initMember.getMemberId(), createBoardRequest, null);

            //then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(boardRepository.searchBoards(null, FIRST_PAGE, SortType.CREATED_DESC, null)).hasSize(2);
                Board testBoard = boardRepository.searchBoards(null, FIRST_PAGE, SortType.CREATED_DESC, null).get(0);

                softly.assertThat(testBoard.getTitle()).isEqualTo(createBoardRequest.getTitle());
                softly.assertThat(testBoard.getContent()).isEqualTo(createBoardRequest.getContent());
                softly.assertThat(testBoard.getBoardType()).isEqualTo(createBoardRequest.getBoardType());
                softly.assertThat(testBoard.getAuthorID()).isEqualTo(initMember.getMemberId());
            });
        }
    }

    @Nested
    class getBoards {
        @Test
        void 성공_게시판을_가져온다() throws IOException {
            //given
            //when
            GetBoardsResponse getBoardsResponse = boardService.getBoards(null, FIRST_PAGE, SortType.CREATED_DESC, null);

            //then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(getBoardsResponse.getBoards()).hasSize(1);
                softly.assertThat(getBoardsResponse.getBoards().get(0).getTitle()).isEqualTo(BoardFixture.INIT_BOARD_TITLE);
                softly.assertThat(getBoardsResponse.getBoards().get(0).getContent()).isEqualTo(BoardFixture.INIT_BOARD_CONTENT);
            });
        }
    }

    @Nested
    class getBoard {
        @Test
        void 성공_게시글을_가져온다() {
            //given
            String boardId = initBoard.getId().toString();

            //when
            GetBoardResponse getBoardResponse = boardService.getBoard(boardId);

            //then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(getBoardResponse.getBoardDto().getTitle()).isEqualTo(BoardFixture.INIT_BOARD_TITLE);
                softly.assertThat(getBoardResponse.getBoardDto().getContent()).isEqualTo(BoardFixture.INIT_BOARD_CONTENT);
            });
        }
    }

    @Nested
    class modifyBoard {
        @Test
        void 성공_게시글을_수정한다() throws IOException {
            //given
            String boardId = initBoard.getId().toString();
            ModifyBoardRequest modifyBoardRequest = ModifyBoardRequest.builder()
                    .title("수정된 제목")
                    .content("수정된 내용")
                    .build();

            //when
            boardService.modifyBoard(initMember.getMemberId(), boardId, modifyBoardRequest, null);

            //then
            SoftAssertions.assertSoftly(softly -> {
                Board modifiedBoard = boardAdaptor.findByBoardId(boardId);
                softly.assertThat(modifiedBoard.getTitle()).isEqualTo("수정된 제목");
                softly.assertThat(modifiedBoard.getContent()).isEqualTo("수정된 내용");
            });
        }
    }

    @Nested
    class deleteBoard {
        @Test
        void 성공_게시글을_삭제한다() {
            //given
            String boardId = initBoard.getId().toString();

            //when
            boardService.deleteBoard(initMember.getMemberId(), boardId);

            //then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(boardRepository.searchBoards(null, FIRST_PAGE, SortType.CREATED_DESC, null)).isEmpty();
            });
        }
    }

    @Nested
    class viewBoard {
        @Test
        void 성공_조회수가_증가한다() {
            //given
            String boardId = initBoard.getId().toString();
            long initialViews = initBoard.getViews();

            //when
            boardService.viewBoard(boardId);

            //then
            SoftAssertions.assertSoftly(softly -> {
                Board viewedBoard = boardAdaptor.findByBoardId(boardId);
                softly.assertThat(viewedBoard.getViews()).isEqualTo(initialViews + 1);
            });
        }
    }

    @Nested
    class jsonBoard {
        @Test
        void 성공_JSON_파일을_통해_게시글을_생성한다() {
            // JSON 파일 테스트는 별도 구현 필요
        }
    }
}