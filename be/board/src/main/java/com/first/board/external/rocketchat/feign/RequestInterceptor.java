package com.first.board.external.rocketchat.feign;

import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor(staticName = "of")
public class RequestInterceptor implements feign.RequestInterceptor {

    @Value("${spring.rocketchat.authtoken}")
    private String authToken;

    @Value("${spring.rocketchat.userid}")
    private String userId;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("X-Auth-Token", authToken);
        requestTemplate.header("X-User-Id", userId);
        requestTemplate.header("accept", "application/json");
        requestTemplate.header("content-type", "application/json");
    }
}
