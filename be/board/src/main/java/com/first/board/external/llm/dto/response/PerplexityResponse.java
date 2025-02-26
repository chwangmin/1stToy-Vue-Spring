package com.first.board.external.llm.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerplexityResponse {
    private String id;
    private String model;
    private Long created;
    private Usage usage;
    private List<String> citations;
    private String object;
    private List<Choice> choices;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        private Integer promptTokens;
        private Integer completionTokens;
        private Integer totalTokens;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Integer index;
        private String finishReason;
        private Message message;
        private Message delta;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String role;
        private String content;
    }

    // 편의 메서드: 응답 내용에 인용 링크 추가
    public String getContentWithCitationLinks() {
        if (choices != null && !choices.isEmpty() && choices.get(0).getMessage() != null) {
            String message = choices.get(0).getMessage().getContent();

            // 인용 번호 패턴 찾기 (예: [1], [2] 등)
            Pattern pattern = Pattern.compile("\\[(\\d+)\\]");
            Matcher matcher = pattern.matcher(message);

            StringBuffer result = new StringBuffer();

            // 각 인용 번호를 찾아 링크로 변환
            while (matcher.find()) {
                int citationIndex = Integer.parseInt(matcher.group(1)) - 1;
                if (citationIndex >= 0 && citationIndex < citations.size()) {
                    String url = citations.get(citationIndex);
                    // [1] 형태를 [[1]](url) 형태로 변환
                    String replacement = "[[" + matcher.group(1) + "]](" + url + ")";
                    matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
                }
            }
            matcher.appendTail(result);

            return result.toString();
        }
        return null;
    }

}
