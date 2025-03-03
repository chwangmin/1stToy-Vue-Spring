package com.first.board.external.llm.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PerplexityAuthInterceptor implements RequestInterceptor {

    @Value("${spring.perplexity.api.token}")
    private String perplexityToken;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", "Bearer " + perplexityToken);
    }
}
