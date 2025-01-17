package com.first.board.global.secuirty;

import lombok.Getter;

@Getter
public enum WebSecurityPath {
    REQUIRE_AUTH_PATH("/board/**", "/auth/logout");

    private final String[] paths;

    WebSecurityPath(String... paths) {
        this.paths = paths;
    }
}

