package com.first.board.external.llm.service;

import com.first.board.external.llm.dto.request.PerplexityRequest;
import com.first.board.external.llm.dto.response.PerplexityResponse;
import com.first.board.external.llm.feign.PerplexityAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.first.board.external.llm.constant.PerplexityConstant.*;

@Service
@RequiredArgsConstructor
public class PerplexityAPIService {
    private final PerplexityAPI perplexityAPI;

    public PerplexityResponse sendPrompt(String prompt) {
        PerplexityRequest perplexityRequest = PerplexityRequest.builder()
                .model(PERPLEXITY_MODEL)
                .messages(Arrays.asList(
                        PerplexityRequest.Message.builder()
                                .role(PERPLEXITY_SYSTEM)
                                .content(PERPLEXITY_SET)
                                .build(),
                        PerplexityRequest.Message.builder()
                                .role(PERPLEXITY_USER)
                                .content(prompt)
                                .build()
                ))
                .build();

        return perplexityAPI.sendPrompt(perplexityRequest);
    }
}
