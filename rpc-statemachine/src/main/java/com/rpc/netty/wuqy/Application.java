package com.rpc.netty.wuqy;

import com.rpc.netty.wuqy.statemachine.Events;
import com.rpc.netty.wuqy.statemachine.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
//public class Application {
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        stateMachine.start();
//        stateMachine.sendEvent(Events.PAY);
//        stateMachine.sendEvent(Events.RECEIVE);
    }

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        stateMachine.start();
        stateMachine.sendEvent(Events.RECEIVE);
        stateMachine.sendEvent(Events.PAY);
    }

}
