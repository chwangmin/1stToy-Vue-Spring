package com.first.board.external.llm.feign;

import com.first.board.external.llm.config.PerplexityFeignConfig;
import com.first.board.external.llm.dto.request.PerplexityRequest;
import com.first.board.external.llm.dto.response.PerplexityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        url = "https://api.perplexity.ai",
        name = "perplexityAPIClient",
        configuration = PerplexityFeignConfig.class
)
public interface PerplexityAPI {

    @PostMapping(value = "/chat/completions")
    PerplexityResponse sendPrompt(@RequestBody PerplexityRequest perplexityRequest);
}
