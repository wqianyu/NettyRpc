package com.test.wuqy;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import static org.springframework.boot.SpringApplication.run;

/**
 * Created by gui on 2017/3/21.
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"com.test.wuqy.action,com.netty.pool4"})//所有spring相关的包路径都要在此配置
//@ComponentScan(basePackages = {"com.test.wuqy.action,com.netty.pool5"})//所有spring相关的包路径都要在此配置
@ComponentScan(basePackages = {"com.test.wuqy.action,com.netty.pool6,test"})//所有spring相关的包路径都要在此配置
public class Application {
    public static void main(String[] args) {
        run(Application.class, args);
    }

}
