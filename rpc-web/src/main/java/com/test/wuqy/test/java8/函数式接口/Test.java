package com.test.wuqy.test.java8.函数式接口;

public class Test {
    public static void main(String[] args) {
        GreetingService greetingService = message -> System.out.println("Hello" + message);
        greetingService.sayMessage(" World");
    }
}
