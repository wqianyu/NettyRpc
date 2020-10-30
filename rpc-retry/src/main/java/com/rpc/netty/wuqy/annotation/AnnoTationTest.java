package com.rpc.netty.wuqy.annotation;

import org.springframework.stereotype.Service;

@Service
public class AnnoTationTest {

    @Retryable(maxAttempts = 5, value = Exception.class)
    public void fiveTimes() throws Exception {
        System.out.println("fiveTimes called!");
        return;
    }
}
