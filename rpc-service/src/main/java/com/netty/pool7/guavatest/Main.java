package com.netty.pool7.guavatest;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


/**
 * Created by Administrator on 2018/9/28.
 */
public class Main {
    public static void main(String[] args) {
        List<String> list = Lists.newArrayList();

        int[] array = { 1, 2, 3, 4, 5 };
        int a = 5;
        if(Ints.contains(array, a)) {
            System.out.println("true11111");
        } else {
            System.out.println("false2222");
        }

        int[] array2 = { 1, 2, 3, 6, 7, 8};
        int indexOf
                = Ints.indexOf(array2, a);
        int max
                = Ints.max(array2);
        int min
                = Ints.min(array2);
        int[]
                concat = Ints.concat(array, array2);

        System.out.println(indexOf+","+max+","+min+",");
        System.out.println(concat.length);

        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<Object> submit = service.submit(new Callable<Object>() {
            public Object call() throws InterruptedException {
                System.out.println("deal begin");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //throw new InterruptedException();
                System.out.println("deal finish");
                return "返回结果";
            }
        });
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println("wait finish");
        Futures.addCallback(submit, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object result) {
                try {
                    System.out.println("integer = " + submit.get().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                System.out.println("result = " + result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("t = " + t);
            }
        }, MoreExecutors.directExecutor());
        System.out.println("~~~~~~~~~");
        service.shutdown();
        test("a", 1);
    }


    public static void test(String a, Integer b) {
        Optional.of(a);
        Optional.of(b);
    }
}
