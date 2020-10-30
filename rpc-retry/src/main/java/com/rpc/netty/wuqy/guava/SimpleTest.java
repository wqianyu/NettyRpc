package com.rpc.netty.wuqy.guava;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static javafx.scene.input.KeyCode.T;

@Slf4j
public class SimpleTest {

    public static void main(String[] args) {
        /*Callable<Boolean> callable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                // do something useful here
                log.info("call...");
                throw new RuntimeException();
            }
        };*/
        Callable<Boolean> callable = ()->{
            // do something useful here
            log.info("call...");
            throw new Exception();
        };

        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                .retryIfResult(Predicates.isNull())
                .retryIfExceptionOfType(Exception.class)
                .retryIfRuntimeException()
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(2))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("the {}st retry, hasResult:{}, hasError:{}, delay since first attempt:{}", attempt.getAttemptNumber(),
                                attempt.hasResult(), attempt.hasException(), attempt.getDelaySinceFirstAttempt());
                    }
                })
                .build();
        try {
            retryer.call(callable);
        } catch (RetryException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    private static final RetryListener LOG_LISTENER = new RetryLogListener();

    public static class RetryLogListener implements RetryListener {

        private static final Logger logger = LoggerFactory.getLogger(RetryLogListener.class);

        @Override
        public <V> void onRetry(Attempt<V> attempt) {
            logger.info("the {}st retry, hasResult:{}, hasError:{}, delay since first attempt:{}", attempt.getAttemptNumber(),
                    attempt.hasResult(), attempt.hasException(), attempt.getDelaySinceFirstAttempt());
        }
    }
}
