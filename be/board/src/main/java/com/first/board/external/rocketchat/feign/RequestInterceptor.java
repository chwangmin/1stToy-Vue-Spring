package com.first.board.external.rocketchat.feign;

import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class RequestInterceptor implements feign.RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("accept", "application/json");
        requestTemplate.header("content-type", "application/json");
    }
}
