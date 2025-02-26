package com.first.board.batch.service;

import com.first.board.domain.rocketchat.adaptor.RocketChatAdaptor;
import com.first.board.domain.rocketchat.entity.RocketChat;
import com.first.board.domain.rocketchat.entity.ScheduledMessageStatus;
import com.first.board.domain.rocketchat.entity.WeekType;
import com.first.board.domain.rocketchat.repository.RocketChatRepository;
import com.first.board.external.rocketchat.dto.Attachment;
import com.first.board.external.rocketchat.dto.Message;
import com.first.board.external.rocketchat.dto.request.RocketChatMessageRequest;
import com.first.board.external.rocketchat.feign.RocketChatAPI;
import com.first.board.global.error.ErrorCode;
import com.first.board.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.first.board.external.rocketchat.constant.RocketChatConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManager {
    @Qualifier("taskScheduler")
    private final TaskScheduler taskScheduler;
    private final RocketChatAdaptor rocketChatAdaptor;
    private final RocketChatAPI rocketChatAPI;
    private final RocketChatRepository rocketChatRepository;

    private final Map<String, Map<String, ScheduledFuture<?>>> scheduledTasks = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void initScheduleMessage() {
        List<RocketChat> rocketChats =
                rocketChatAdaptor.findAllByStatus(ScheduledMessageStatus.PENDING);

        rocketChats.forEach(rocketChat -> {
            if (rocketChat.getStatus() == ScheduledMessageStatus.PENDING) {
                addTask(rocketChat);
            }
        });
    }

    public void addTask(RocketChat rocketChat) {
        if (!scheduledTasks.containsKey(rocketChat.getXUserId())) {
            scheduledTasks.put(rocketChat.getXUserId(), new HashMap<>());
        }

        Map<String, ScheduledFuture<?>> myScheduledTasks = scheduledTasks.get(rocketChat.getXUserId());

        if (rocketChat.getWeek().isEmpty()) {
            myScheduledTasks.put(rocketChat.getIdtoString(),
                    taskScheduler.schedule(scheduledSend(rocketChat),
                            Instant.from(LocalDateTime
                                    .of(rocketChat.getDate(), rocketChat.getTime())
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant())));
        } else {
            String cron = convertCronExpression(rocketChat.getWeek(), rocketChat.getTime());
            myScheduledTasks.put(rocketChat.getIdtoString(),
                    taskScheduler.schedule(scheduledSend(rocketChat),
                            new CronTrigger(cron, TimeZone.getTimeZone(TimeZone.getDefault().getID()))));
        }
    }

    public void removeTask(RocketChat rocketChat) {
        Map<String, ScheduledFuture<?>> myScheduledTasks = scheduledTasks.get(rocketChat.getXUserId());

        myScheduledTasks.get(rocketChat.getIdtoString()).cancel(true);
        myScheduledTasks.remove(rocketChat.getIdtoString());
    }

    private Runnable scheduledSend(RocketChat rocketChat) {
        return () -> {
            if (rocketChat.getIsGpt()) {
                //todo chatGPT 연결 필요
            }

            RocketChatMessageRequest chatMessage = RocketChatMessageRequest.builder()
                    .message(Message.builder()
                            .rid(rocketChat.getRoomId())
                            .msg(rocketChat.getMessage())
                            .emoji(rocketChat.getIcon().isEmpty() ? DEFAULT_EMOJI : rocketChat.getIcon())
                            .attachments(Collections.singletonList(
                                    Attachment.builder()
                                            .color(ATTACH_COLOR)
                                            .text(MESSAGE_HEADER)
                                            .build()
                            ))
                            .build())
                    .build();

            rocketChatAPI.sendScheduledMessage(chatMessage, rocketChat.getXAuthToken(), rocketChat.getXUserId());

            if (rocketChat.getWeek().isEmpty()) {
                rocketChat.setStatus(ScheduledMessageStatus.COMPLETED);
                rocketChatRepository.updateStatus(rocketChat);
                removeTask(rocketChat);
            }
        };
    }

    /**
     * loop 가 true일 때 실행합니다.
     * 주어진 week, time 값을 cron 표현식으로 변환합니다.
     *
     * @param week 요일 (0-6, 0=일요일, 6=토요일)
     * @param time 실행 시간
     * @return cron 표현식
     */
    private String convertCronExpression(List<WeekType> week, LocalTime time) {
        // 필수 필드 검증
        if (time == null) {
            throw new BusinessException(ErrorCode.CRON_HAVE_TO_TIME);
        }

        // 시간 정보 추출
        int hour = time.getHour();
        int minute = time.getMinute();
        int second = time.getSecond();

        // 요일 정보 처리
        String dayOfWeek = "?";
        if (week != null && !week.isEmpty()) {
            StringBuilder days = new StringBuilder();
            for (WeekType day : week) {
                if (days.length() > 0) {
                    days.append(",");
                }
                // WeekType enum에서 요일 번호 추출 (SUN=0, MON=1, ...)
                days.append(day.ordinal());
            }
            dayOfWeek = days.toString();
        }

        // cron 표현식 생성: 초 분 시 일 월 요일
        return String.format("%d %d %d ? * %s", second, minute, hour, dayOfWeek);
    }

    public StringBuilder getAllTask() {
        StringBuilder sb = new StringBuilder();
        AtomicInteger i = new AtomicInteger(1);
        scheduledTasks.forEach((key, value) -> {
            value.forEach((key2, value2) -> {
                sb.append(i.getAndIncrement()).append(": ").append(value2.getDelay(TimeUnit.MINUTES)).append("분 후 시작\n");
            });
        });
        return sb;
    }

    public Integer checkNumberTodayRocketChat(String xUserId) {
        return scheduledTasks.get(xUserId).size();
    }
}
