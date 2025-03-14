package com.first.board.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    TEST(HttpStatus.INTERNAL_SERVER_ERROR, "001", "business exception test"),

    // 인증 && 인가
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-001", "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-002", "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-003", "Authorization Header가 빈값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-004", "인증 타입이 Bearer 타입이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-005", "해당 refresh token은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-006", "해당 refresh token은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-007", "해당 토큰은 ACCESS TOKEN이 아닙니다."),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, "A-008", "관리자 Role이 아닙니다."),
    ACCESS_TOKEN_REFRESH(HttpStatus.UNAUTHORIZED, "A-009", "액세스 토큰 재발급 하였습니다."),

    // 회원
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, "M-001", "잘못된 회원 타입 입니다.(memberType : KAKAO)"),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, "M-002", "이미 가입된 회원 입니다."),
    MEMBER_NOT_EXISTS(HttpStatus.BAD_REQUEST, "M-003", "해당 회원은 존재하지 않습니다."),
    MEMBER_COUNT_OUT(HttpStatus.BAD_REQUEST, "M-004", "해당 회원 로그인 시도 횟수가 초과되었습니다. 비밀번호 찾기로 초기화 하세요."),
    MEMBER_NOT_MATCH(HttpStatus.BAD_REQUEST, "M-005", " 아이디(로그인 전용 아이디) 또는 비밀번호를 잘못 입력했습니다.\n" +
            "입력하신 내용을 다시 확인해주세요."),
    ALREADY_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, "M-006", "이미 가입된 이메일 입니다."),
    EMAIL_NOT_EXISTS(HttpStatus.BAD_REQUEST, "M-007", "해당 이메일은 존재하지 않습니다."),


    // 암호화
    INVALID_AES_KEY(HttpStatus.BAD_REQUEST, "C-001", "암호화 에러"),

    // 이메일
    EMAIL_FAIL(HttpStatus.BAD_REQUEST, "E-001", "이메일 전송 에러"),

    // 게시판
    BOARD_NOT_FOUND(HttpStatus.BAD_REQUEST, "B-001", "찾고자 하는 게시글이 존재하지 않습니다."),
    CANNOT_DELETE_BOARD_YOU_NOT_CREATE(HttpStatus.BAD_REQUEST, "B-002", "자신이 작성하지 않은 게시글을 삭제할 수 없습니다."),
    CANNOT_MODIFY_BOARD_YOU_NOT_CREATE(HttpStatus.BAD_REQUEST, "B-003", "자신이 작성하지 않은 게시글을 수정할 수 없습니다."),

    // 게시판 댓글
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CO-001", "찾고자 하는 댓글이 존재하지 않습니다."),
    CANNOT_UPDATE_COMMENT_YOU_NOT_CREATE(HttpStatus.BAD_REQUEST, "CO-002", "자신이 작성하지 않은 댓글은 수정할 수 없습니다."),
    CANNOT_DELETE_COMMENT_YOU_NOT_CREATE(HttpStatus.BAD_REQUEST, "CO-003", "자신이 작성하지 않은 댓글은 삭제할 수 없습니다."),
    BOARD_MISMATCH_FROM_BOARD(HttpStatus.BAD_REQUEST, "CO-004", "해당 게시글에 댓글이 존재하지 않습니다."),

    // FileReader
    FILE_READ_ERROR(HttpStatus.BAD_GATEWAY, "F-001", "파일을 읽을 수 없습니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_GATEWAY, "F-002", "파일 요청이 JSON 이지만, JSON 형식이 아닙니다."),

    // rocketchat
    ROCKET_CHAT_NOT_FOUND(HttpStatus.BAD_GATEWAY, "R-001", "해당 rocket chat은 존재하지 않습니다."),
    ROCKET_CHAT_CANNOT_CREATE_OVER_MAX(HttpStatus.BAD_GATEWAY, "R-002", "알림예약은 최대 50개 입니다."),

    // cron 형식
    CRON_HAVE_TO_TIME(HttpStatus.BAD_GATEWAY, "CR-001", "cron 표현식에 Time이 필요합니다.")
    ;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
    }

    private HttpStatus httpStatus;
    private String errorCode;
    private String message;

}

