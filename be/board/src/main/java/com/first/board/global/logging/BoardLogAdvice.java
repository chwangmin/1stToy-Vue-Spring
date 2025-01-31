package com.first.board.global.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class BoardLogAdvice {

    @Around("execution(* com.first.board.domain.auth.controller.*Controller.*(..)) || " +
            "execution(* com.first.board.domain.board.controller.*Controller.*(..)) || " +
            "execution(* com.first.board.domain.member.controller.*Controller.*(..))")

    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();

        long startTime = System.currentTimeMillis();
        log.info("[START] {}.{} - Arguments: {}",
                className,
                methodName,
                Arrays.toString(joinPoint.getArgs())
        );

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            log.info("[END] {}.{} - Execution Time: {}ms",
                    className,
                    methodName,
                    endTime - startTime
            );

            return result;
        } catch (Throwable e) {
            log.error("[ERROR] {}.{} - Exception: {}",
                    className,
                    methodName,
                    e.getMessage()
            );
            throw e;
        }
    }
}

