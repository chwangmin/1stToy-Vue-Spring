package com.first.board.domain.board.type;

import org.springframework.data.domain.Sort;

public enum SortType {
    CREATED_DESC("createDate", Sort.Direction.DESC),    // 등록날짜 최신순
    CREATED_ASC("createDate", Sort.Direction.ASC),     // 등록날짜 오래된순
    VIEWS_DESC("views", Sort.Direction.DESC),      // 조회수 높은순
    VIEWS_ASC("views", Sort.Direction.ASC);        // 조회수 낮은순

    private final String field;
    private final Sort.Direction direction;

    SortType(String field, Sort.Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public Sort getSort() {
        return Sort.by(direction, field);
    }
}
