package com.example.demo.config;

import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class SystemFailureMonitorAspect {

    @AfterThrowing(
            pointcut = "execution(* com.example.demo.service.OrderService.createOrder(..))",
            throwing = "ex"
    )
    public void afterThrowingOrderService(JoinPoint joinPoint, Throwable ex) {
        String requestId = UUID.randomUUID().toString();
        try {
            MDC.put("requestId", requestId);

            log.error(
                    "He thong loi. requestId={}, method={}, errorCode={}, errorMessage={}",
                    requestId,
                    joinPoint.getSignature().toShortString(),
                    ex.getClass().getSimpleName(),
                    ex.getMessage(),
                    ex
            );
        } finally {
            MDC.clear();
        }
    }
}