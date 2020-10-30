package com.test.wuqy.test.java8;


public class ThreadLocalExample {


    public static class MyRunnable implements Runnable {


        private static ThreadLocal threadLocal = new ThreadLocal();

        private static Integer data = 1;

        @Override

        public void run() {

            threadLocal.set((int) (Math.random() * 100D));
            data = (int) (Math.random() * 100D);
            try {

                Thread.sleep(2000);

            } catch (InterruptedException e) {


            }

            System.out.println(threadLocal.get()+","+threadLocal+","+data);

        }

    }


    public static void main(String[] args) {

        MyRunnable sharedRunnableInstance = new MyRunnable();

        Thread thread1 = new Thread(sharedRunnableInstance);

        Thread thread2 = new Thread(sharedRunnableInstance);

        thread1.start();

        thread2.start();

    }
}


