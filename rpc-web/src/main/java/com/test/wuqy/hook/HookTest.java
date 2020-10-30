package com.test.wuqy.hook;

public class HookTest extends Thread{
    private volatile static boolean bizState = true;

    private volatile static boolean shutdown = false;

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        try {
            if (!shutdown && bizState) {
                System.out.println("begin");
                bizState = false;
                System.out.println("waiting...");
                Thread.sleep(3000);
                bizState = true;
                System.out.println("end");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Do something in Shutdown Hook end");
            shutdown = true;
            try {
                while (!bizState) {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Do something in Shutdown Hook finish");
        }));
    }

    public static void main(String[] args) throws Exception {
        HookTest thread = new HookTest();
        thread.start();
//        Thread.sleep(1000);
        System.exit(1);
    }
}
