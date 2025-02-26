package com.first.board.domain.rocketchat.controller;

import com.first.board.domain.rocketchat.dto.request.CreateRocketChatRequest;
import com.first.board.domain.rocketchat.dto.request.DeleteRocketChatReqest;
import com.first.board.domain.rocketchat.dto.request.ModifyRocketChatRequest;
import com.first.board.domain.rocketchat.dto.response.GetRocketChatsResponse;
import com.first.board.domain.rocketchat.service.RocketChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rocket-chat")
@RequiredArgsConstructor
public class RocketChatController {
    private final RocketChatService rocketChatService;

    @Tag(name = "rocket-chat")
    @Operation(summary = "유저 알람 확인", description = "유저의 알람을 확인합니다.")
    @GetMapping
    public ResponseEntity<GetRocketChatsResponse> getRocketChat(@RequestParam String XUserId) {
        return ResponseEntity.ok(rocketChatService.getRocketChats(XUserId));
    }

    @Tag(name = "rocket-chat")
    @Operation(summary = "유저 알람 생성", description = "유저의 알람을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createRocketChat(@RequestBody CreateRocketChatRequest createRocketChatRequest) {
        rocketChatService.createRocketChat(createRocketChatRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "rocket-chat")
    @Operation(summary = "유저 알람 수정", description = "유저의 알람을 수정합니다.")
    @PutMapping
    public ResponseEntity<?> modifyRocketChat(@RequestBody ModifyRocketChatRequest modifyRocketChatRequest) {
        rocketChatService.modifyRocketChat(modifyRocketChatRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "rocket-chat")
    @Operation(summary = "유저 알람 삭제", description = "유저의 알람을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<?> deleteRocketChat(@RequestBody DeleteRocketChatReqest deleteRocketChatRequest) {
        rocketChatService.deleteRocketChat(deleteRocketChatRequest);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "rocket-chat")
    @Operation(summary = "모든 task 예약 rocketChat message 확인", description = "실제 task에 있는 모든 예약 rocketChat messaget 확인")
    @GetMapping("/all")
    public ResponseEntity<?> allGetRocketChat() {
        return ResponseEntity.ok(rocketChatService.allGetRocketChat());
    }
}
