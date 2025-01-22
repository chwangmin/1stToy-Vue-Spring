package com.first.board.global.secuirty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;

@Getter
public enum WebSecurityPath {
    REQUIRE_AUTH_PATH(
            new SecurityPath("/board", HttpMethod.POST),
            new SecurityPath("/board/{boardId}", HttpMethod.DELETE),
            new SecurityPath("/board/{boardId}", HttpMethod.PUT),
            new SecurityPath("/member/modify", HttpMethod.POST),
            new SecurityPath("/member/leave", HttpMethod.POST),
            new SecurityPath("/member/info", HttpMethod.GET),
            new SecurityPath("/auth/logout", HttpMethod.POST)
    );

    private final SecurityPath[] paths;

    WebSecurityPath(SecurityPath... paths) {
        this.paths = paths;
    }

    @Getter
    @AllArgsConstructor
    public static class SecurityPath {
        private String path;
        private HttpMethod method;
    }
}


