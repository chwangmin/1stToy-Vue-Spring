package com.first.board.external.llm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PerplexityFeignConfig {

    @Bean
    public PerplexityAuthInterceptor perplexityAuthInterceptor() {
        return new PerplexityAuthInterceptor();
    }
}
