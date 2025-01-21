package com.first.board.batch;

import com.first.board.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DeleteUserNotActivate {
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 2 * * ?", zone = "Asia/Seoul")
    public void deleteUserNotActivate() {
        log.info("Delete user not activate in DB");

        memberRepository.deleteByIsActiveFalse();
    }
}
