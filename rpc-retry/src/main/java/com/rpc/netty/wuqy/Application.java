package com.rpc.netty.wuqy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

/**
 * Created by gui on 2017/3/21.
 */
@EnableRetry   //开启重试机制
@SpringBootApplication
//@ComponentScan(basePackages = {"com.test.wuqy.action,com.netty.pool4"})//所有spring相关的包路径都要在此配置
//@ComponentScan(basePackages = {"com.test.wuqy.action,com.netty.pool5"})//所有spring相关的包路径都要在此配置
@ComponentScan(basePackages = {"com.rpc.netty.wuqy"})//所有spring相关的包路径都要在此配置
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
