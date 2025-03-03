package com.first.board.domain.rocketchat.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetNumberTodayResponse {
    Integer count;
}
