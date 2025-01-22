package com.first.board.global.secuirty;

import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum WebSecurityPath {
    REQUIRE_AUTH_PATH(
            new String[]{"/board", "/board/{boardId}", "/auth/logout", "/member/modify", "/member/leave", "/member/info","/auth/logout" },
            new HttpMethod[]{HttpMethod.POST, HttpMethod.DELETE, HttpMethod.POST}
    );

    private final String[] paths;
    private final HttpMethod[] method;

    WebSecurityPath(String[] paths, HttpMethod[] method) {
        this.paths = paths;
        this.method = method;
    }
}

