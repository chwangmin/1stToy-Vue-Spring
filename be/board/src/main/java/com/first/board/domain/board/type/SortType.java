package com.first.board.domain.board.type;

public enum SortType {
    CREATED_DESC("createDate", "DESC"),    // 등록날짜 최신순
    CREATED_ASC("createDate", "ASC"),     // 등록날짜 오래된순
    VIEWS_DESC("views", "DESC"),      // 조회수 높은순
    VIEWS_ASC("views", "ASC");        // 조회수 낮은순

    SortType(String field, String direction) {
    }
}
