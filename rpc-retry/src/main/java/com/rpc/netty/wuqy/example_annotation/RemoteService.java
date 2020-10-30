package com.rpc.netty.wuqy.example_annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoteService {

    /**
     * 调用方法
     */
    @Retryable(value = Exception.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    public void call() {
        log.info("Call something...");
        throw new RuntimeException("RPC调用异常");
    }

    /**
     * recover 机制
     * @param e 异常
     */
    @Recover
    public void recover(Exception e) {
        log.info("Start do recover things....");
        log.warn("We meet ex: ", e);
    }

}
