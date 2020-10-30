package com.rpc.netty.wuqy.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AnnoTationUse {

    public static void main(String[] args) {
        List<Integer> useCases = new ArrayList<Integer>();
        Collections.addAll(useCases, 47, 48, 49, 50);
        trackUseCases(useCases, AnnoTationTest.class);
    }
    public static void trackUseCases(List<Integer> useCases, Class<?> cl) {
        for (Method m : cl.getDeclaredMethods()) {
            //获得注解的对象
            Retryable uc = m.getAnnotation(Retryable.class);
            if (uc != null) {
                System.out.println("Found Use Case:" + uc.maxAttempts() + " "
                        + uc.value());
                useCases.remove(new Integer(uc.maxAttempts()));
            }
        }
        for (int i : useCases) {
            System.out.println("Warning: Missing use case-" + i);
        }
    }
}
