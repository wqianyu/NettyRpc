package com.rpc.netty.wuqy.example_annotation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Slf4j
public class SimpleDemo {


    public static void main(String[] args) throws Exception {
        RetryTemplate template = new RetryTemplate();

        // 策略
        SimpleRetryPolicy policy = new SimpleRetryPolicy();
        policy.setMaxAttempts(3);
        template.setRetryPolicy(policy);

        /*String result = template.execute(
                new RetryCallback<String, Exception>() {
                    @Override
                    public String doWithRetry(RetryContext arg0) {
                        throw new NullPointerException();
                    }
                }
                ,
                new RecoveryCallback<String>() {
                    @Override
                    public String recover(RetryContext context) {
                        return "recovery callback";
                    }
                }
        );*/
        String result = template.execute(
                (RetryContext arg0) -> {throw new NullPointerException();},
                (RetryContext context) -> "recovery callback"
        );

        log.info("result: {}", result);
    }
}
